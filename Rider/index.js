const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const readline = require('readline');
var fs = require('fs');

const app = express();
var autoFiller = true;
var bufferingInput = false;
const port = 3000;
var consegne= [];


const rl = readline.createInterface({
		input : process.stdin,
		output : process.stdout,
		terminal: false
	});



app.use(cors())
app.use(express.urlencoded({extended: false}))
app.use(express.json())

app.post('/dispRider', (req, res) => {
	
	const rl = readline.createInterface({
		input : process.stdin,
		output : process.stdout
	});
	var risposta;
	var body = req.body;
	//show info to Rider
	console.log("\n--- NEW AVAILABILITY REQUEST --- ")
	console.log("Rider: " + body.rider)
	console.log("Valore consegna: " + body.bill)
	console.log("Indirizzo ristorante: " + body.resAddress )
	console.log("Indirizzo cliente: " + body.clientAddress)
	if(!autoFiller){
		bufferingInput = true;
		//ask question in bash
		rl.question("\nAre you available ? Y/n", function(disp){
			if(disp == 'Y' || disp == 'y' ){
				
				rl.question("Set your price: ", function(readPrice){
					risposta = {
						"disp": true,
						"prezzo": readPrice
					};
					bufferingInput = false;
					res.send(risposta);
				})
				
			} else {
				risposta = {
					"disp" : false
				};
				bufferingInput = false;
				res.send(risposta);
			}
			
		})
	} else {
		prezzo = (Math.random()* 5).toFixed(2);
		risposta = {
			"disp" : true,
			"prezzo" : prezzo
		};
		res.send(risposta);
	}
});

//function to automatize availability response
app.get('/toggle-autoFiller', (req, res) => {
	autoFiller = !autoFiller;
	console.log("***autoFiller set at "+autoFiller+" *** ")
	res.send(autoFiller)
});


app.post('/consAff', (req,res) => {
    const consegna= req.body;
    var exit = false
    fs.readFile('consegne.json', 'utf8', (err, data) => {
            if (err) {
                console.log(err);
            } else {
                consegne = JSON.parse(data); //now it an object
                if(consegne.length > 0){
                    var i = 0
                    while(exit == false && i < consegne.length){
                            if(consegna.id == consegne[i].id){
                                var risposta = {
                                    "consegna" : false,
                                    "info" : 'Impossibile prendere in carico la consegna: id ' + consegna.id + ' già presente!'
                                }
                                res.send(risposta);
                                exit = true;
                            }
                        i=i+1;
                    }
                }
                if(exit == false){
                    consegne.push(consegna); //add some data
                    json = JSON.stringify(consegne,null,4); //convert it back to json
                    fs.writeFile('consegne.json', json, 'utf8', function(err) { if (err) { console.log(err); } }); // write it back 
                    var risposta = {
                        "consegna" : true,
                        "info" : 'Consegna: id ' + consegna.id + ' presa in carico!'
                    }
                    res.send(risposta);
                    
                    
                }
            }
        })
    
})


app.post('/notificaCons', (req,res) => {
    const id = req.body.id;
    var exit = false

    fs.readFile('consegne.json', 'utf8', (err, data) => {
        if (err) {
            console.log(err);
        } else {
            consegne = JSON.parse(data); //now it an object
            if(consegne.length > 0){
                var i = 0
                while(exit == false && i < consegne.length){
                        if(id == consegne[i].id){
                            consegne.splice(i,1);
                            json = JSON.stringify(consegne,null,4); //convert it back to json
                            fs.writeFile('consegne.json', json, 'utf8', function(err) { if (err) { console.log(err); } }); // write it back 
                            var risposta = {
                                "consegna" : true,
                                "info" : 'Consegna ' + id + ' In Corso'
                            }
                            res.send(risposta);
                            
                            exit = true;
                        }
                    i=i+1;
                }
            }
            if(exit == false){
                var risposta = {
                    "consegna" : false,
                    "info" : 'Consegna: id ' + id + ' non trovata'
                }
                res.send(risposta);
            }
            
        }
    })
})


app.post('/consAnnul', (req, res) => {
    const id = req.body.id;
    var exit = false

    fs.readFile('consegne.json', 'utf8', (err, data) => {
        if (err) {
            console.log(err);
        } else {
            consegne = JSON.parse(data); //now it an object
            if(consegne.length > 0){
                var i = 0
                while(exit == false && i < consegne.length){
                        if(id == consegne[i].id){
                            consegne.splice(i,1);
                            json = JSON.stringify(consegne,null,4); //convert it back to json
                            fs.writeFile('consegne.json', json, 'utf8', function(err) { if (err) { console.log(err); } }); // write it back 
                            var risposta = {
                                "consegna" : true,
                                "info" : 'Consegna ' + id + ' Annullata'
                            }
                            res.send(risposta);
                            exit = true;
                        }
                    i=i+1;
                }
            }
            if(exit == false){
                var risposta = {
                    "consegna" : true,
                    "info" : 'Consegna: id ' + id + ' non trovata'
                }
                res.send(risposta);
            }
        }
    })
})

app.listen(port, () => console.log(`Rider Server is listening ${port}!`))