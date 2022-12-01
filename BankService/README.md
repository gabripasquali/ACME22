# Banca

Il servizio della banca è composto di tre container appartenenti alla stessa network:
- il frontend: (Bank-fe) sviluppato con python per agevolare le comunicazioni soap con il backend
- un db:  postgresql
- il backend:  sviluppato utilizzando jolie soap

## FRONTEND:
il frontend è composto da due pagine principali:
### Home
alla home vengono passati già i parametri per effettuare il pagamento quali: amount da pagare, id dell'ordine e id del destinatario.
Jolie effettua da subito una verifica sullo stato dell'ordine utilizzando order_id e destinatario per verificare che lo stato del pagamento.
Nel caso in cui fosse già stato pagato all'utente verrà mostrata una schermata per informarlo di questo, altrimenti potrà effettuare il login.

### Bank
La pagina interna della banca è molto semplice, contiene l'informazione del credito attuale dell'utente, la possibilità di effettuare logout e di pagare.
Nel caso in cui l'utente effettui il pagamento (a buon fine) verrà mostrato il token necessario all'utente per confermare il pagemento ad acme

### Servizi
| Http method | url            | Input                                                                                          | Description                                                                                                                                                                                  |
|-------------|----------------|------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Post        | /login-request | username: string, <br/>password: string,<br/>bill: double,<br/>to_user: int,<br/>id_order: int | verrà restituito un'url per andare<br/>alla pagina di pagamento nel caso in cui<br/>vi siano le informazioni relative<br/>al pagamento. Altrimenti il solo sessionid<br/>per effettuare il pagamento |
| Post        | /pay-request   | sid: string                                                                                    | viene mandata la richiesta di pagamento<br/> alla banca che conosce già le informazioni<br/> del pagamento, poichè salvate durante<br/> l'operazione di login e collegate al sid             |
| Post        | /verifyToken   | sid: string,<br/>token: string,<br/>id_order: int                                              | utilizzato da acme per verificare<br/> che il token ricevuto dall'utente<br/> sia corretto e corrisponda all'ordine<br/> che sta effettuando                                                 |
| Post        | /refound              | sid: string,<br/>id_order: int                                                                 | utilizzato da acme per rimborsare<br/> il pagamento dell'utente nel caso<br/> in cui quest'ultimo voglia annullare l'ordine                                                                  |
| Post        |/confirm| sid: string, <br/>id_order                                                                     | utilizzato ad acme per richiedere <br/>la transizione bancaria                                                                                                                                    |

## DATABASE:
E' composto da due tabelle:

### Users
con le colonne
- id [PK]
- username : necessario per il login
- password : necessario per il login
- balance : bilancio dell'utente

### Transaction
con le colonne
- amount: il valore della transazione
- from_user: il mittente della commissione [FK]
- to_user: il destinatario della commissione [PK] [FK]
- state: lo stato della transizione può avere 4 valori:
  - 0 : l'utente ha effettuato il pagamento con successo
  - 1 : il pagamento è stato verificato da acme tramite il token fornito dall'utente
  - 2 : il pagamento è stato annullato dall'utente
  - 3 : il pagamento viene accreditato ad acme 
  
  Si noti come fino allo stato 3 non venga accreditato effettivamente il pagamento ad acme, per ridurre al minimo le transizioni bancarie
- order_id: [PK] 
- token: il token serve consegnato all'utente che lo fornisce ad ACME per verificare lo stato della transizione

## BACKEND
vengono messi a disposizione 5 endpoint SOAP

| Nome        | Input              | Output          |
|-------------|--------------------|-----------------|
| Login       | loginRequest       | loginResponse   |
| Logout      | sidRequest         | successResponse |
| Pay         | sidRequest         | payResponse     |
| VerifyToken | verifyTokenRequest | successResponse |
| Refound     | refoundRequest     | successResponse |
| Confirm     | confirmRequest     | successResponse |

### Login
Operazione necessaria per poter fare tutte le operazioni successive
E' l'unico punto di contatto con "l'esterno" per questo motivo vengono ricevuti tutti i parametri per effettuare il pagamento:
- username
- password
- bill ?
- id_order ? 
- to_user ?

Se bill non è presente allora si entra nel caso base nel quale non è possibile effettuare pagamenti ma solo verifyToken, refound, confirm\
Nel caso in cui la transizione sia già presente nel db (id_order e to_user sono le PK) all'utente verrà ritornata la pagina in cui viene informato della già avvenuta transizione (si potrebbe mostrare il token nel caso in cui le credenziali coincidano con l'utente che ha fatto il pagamento ma non sarebbe più logica di base come richiesta).\
Altrimenti verrà ritornato l'url necessario per il pagamento e il sid della sessione.

### Logout
E' necessario il sid dell'utente per effettuare il logout. Viene ritornato l'esito dell'operazione

### Pay
Viene verificato il saldo dell'utente, se sufficiente per il pagamento viene scalato il conto, sloggato l'utente e comunicato il token.
L'utente viene sloggato per non effettuare nuovamente il pagamento.

### VerifyToken
- sid
- token
- bill
- id_order

Viene restituito lo stato dell'operazione se uno tra to_user e from_user è uguale all'id dell'utente attuale

### Refound
- sid
- id_order

Viene settato a 2 lo stato dell'ordine
