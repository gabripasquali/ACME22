# ACME SERVER

As you can see there are 2 package, one for the frontend (ACME-frontend) and one for the back(ACME-internal) 
ACME-internal is developed with camunda and is coordinate by servlet

## SERVLET

| Method | url                                       | description                                     |
|--------|-------------------------------------------|-------------------------------------------------|
| POST   | [changeAvailability](#changeAvailability) | update the availability of specific restaurant  |
| POST   | [changeMenu](#changeMenu)                 | update menu: insert new dish (keep the old one) |
|||
|||

## changeAvailability
http://localhost:8080/ACME-internal/changeAvailability

### Request 
``` JSON
{"name" : "YinDyan", "disp" : "true" }
```
### Response
``` JSON
update restaurant YinDyan availability as true
```

## changeMenu
http://localhost:8080/ACME-internal/changeMenu

### Request
``` JSON
{
    "name" : "YinDyan", 
    "menu": [
        {"name": "Udon", "price" : 8.32}
    ]
}
```
### Response
``` JSON
menu updated
```
