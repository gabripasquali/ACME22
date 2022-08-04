const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors')
var fs = require('fs');

const app = express();
var disponibità = [true, false]
const port = 3000;

var consegne= [];


app.use(cors())
app.use(express.urlencoded({extended: false}))
app.use(express.json())

app.get('/dispRider', (req, res) => {
    var i = Math.round(Math.random());
    if (disponibità[i] == true){
        prezzo = (Math.random()* 10).toFixed(2);
        var risposta = {
            "disp" : true,
           "prezzo" : prezzo
        };
        res.send(risposta);
    }
    else{
        var risposta = {
            "disp": false
        };
        res.send(risposta);
    }
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
                                res.send('Impossibile prendere in carico la consegna: id ' + consegna.id + ' già presente!');
                                exit = true;
                            }
                        i=i+1;
                    }
                }
                if(exit == false){
                    consegne.push(consegna); //add some data
                    json = JSON.stringify(consegne,null,4); //convert it back to json
                    fs.writeFile('consegne.json', json, 'utf8', function(err) { if (err) { console.log(err); } }); // write it back 
                    res.send('Consegna: id ' + consegna.id + ' presa in carico!');
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
                            res.send('Consegna ' + id + ' In Corso');
                            
                            exit = true;
                        }
                    i=i+1;
                }
            }
            if(exit == false){
                res.send('Consegna: id ' + id + ' non trovata');
            }
            
        }
    })
})


app.delete('/consAnnul', (req, res) => {
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
                            res.send('Consegna ' + id + ' Annullata');
                            exit = true;
                        }
                    i=i+1;
                }
            }
            if(exit == false){
                res.send('Consegna: id ' + id + ' non trovata');
            }
        }
    })
})

app.listen(port, () => console.log(`Rider Server is listening ${port}!`))