# ACME22
## How to STARTUP
Dopo aver aperto DockerDesktop esegui clear.bat e poi execute.bat.
A questo punto avrai le una serie di servizi esterni accessibili
Per far partire ACME: 
* scaricare il server con già installati i servizi di camunda
* copiare i file war, contenuti in ACME-frontend/target e ACME-internal/target, nella cartella webapps del server che avete scaricare
* lanciare il server con l'apposito comando di start
* la pagina iniziale di acme sarà visualizzabile a http://localhost:8080/ACMEat/
* per poter utilizzare le funzionalità riservate al ristorante (updateMenu e changeAV) è necessario inizializzare il processo attraverso "http://localhost:8080/camunda/app/tasklist/default/" (da automatizzare)


* controllo orario consegna front e back

* comunicazione con banca e acme-cliente dopo pagamento
* comunicazione con banca attualmente non c'è
* non so quali servlet mancano ancora
* sistemare frontend per chiamare servlet corrette (dopo pagamento)
* report
* revisione uml

* sistemare nomi messaggi nel bmpn





