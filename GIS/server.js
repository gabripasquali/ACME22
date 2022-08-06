const express = require('express');
const bodyparser = require('body-parser');
var turf = require('@turf/turf');

const app = express();
app.use(bodyparser.json());

app.post("/isInDistance", (req, res) => {
	//controllo che ci sia il body
	//controllo che listaRider not empty
	let results = [];
	//creazione point con lat e lng
	var from = turf.point([req.body.lng, req.body.lat])
	let riderList = req.body.listarider;
	for (let i = 0; i < riderList.length; i++ ){
		let to = turf.point([riderList[i].lng, riderList[i].lat])
		let d = turf.distance(from, to, {units: 'kilometers'});
		//results[i] = {"nome": riderList[i].properties.name, "distance": d};
		results[i] = {"nome": riderList[i].name, "isInDistance": d<req.body.distance  };
	}
	
	res.status(200).json({
			"results": results
		});
})

app.listen(process.env.PORT || 8080)
