# ACME22
## How to STARTUP
Dopo aver aperto DockerDesktop esegui clear.bat e poi execute.bat.
A questo punto avrai le una serie di servizi esterni accessibili
Per far partire ACME: 
* scaricare il server con già installati i servizi di camunda
* copiare i file war, contenuti in ACME-frontend/target e ACME-internal/target, nella cartella webapps del server che avete scaricare
* lanciare il server con l'apposito comando di start
* la pagina iniziale di acme sarà visualizzabile a http://localhost:8080/ACMEat/


* sistemare abort order nei veri casi (comunciazione acme-rest acme-cliente acme-rider)
* comunicazione con banca attualmente non c'è
* chiamata al rider per consegna asseganta
