# GIS SERVICE
## REST API
|HTTP Method	| Action		| Description				|
|---------------|---------------|---------------------------|
|POST			| [isInDistance](#isInDistance)| Restituisce la lista dei rider specificando se hanno una distanza minore di quella richiesta|

## isInDistance

### Request
`http://localhost:8080/isInDistance` 
``` JSON
{
    "distance": 8, 
    "comune":{
        "geometry": {
            "type": "Point",
            "coordinates": [
                11.329779624938965,
                44.49944393446856
            ]
        }
    }, 
    "listarider": [{
            "type": "Feature",
            "properties": {
                "name": "riderin"
            },
            "geometry": {
                "type": "Point",
                "coordinates": [
                    11.329779624938965,
                    44.49944393446856
                ]
            }
        }]
}
```
### Response
``` JSON
{
    "results": [{
            "nome": "riderin",
            "isInDistance": true
        }]
}
```