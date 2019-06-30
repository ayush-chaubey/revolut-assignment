# revolut-assignment
Revolut Test Assignment

Money Transfer rest service

Default starts on  http://localhost:8080

Run this jar to start application
```sh
java -jar revoult_money_transfer-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Account API - `/accounts`

**GET** - Fetches all the accounts

Response:
**Status: 200 OK**
```javascript
[
    {
        "accountNumber": 100,
        "userName": "ayush",
        "balance": 1000
    },
    {
        "accountNumber": 200,
        "userName": "prakhar",
        "balance": 2000
    }
]
```
---
**POST** - Creates new account 
**Request Body** -

Sample request:
```javascript
{
	"accountNumber" : "100",
	"userName" : "ayush",
	"balance" : "1000"
}

```

Sample response:
**Status: 200 OK**
```javascript
{
	"accountNumber" : "100",
	"userName" : "ayush",
	"balance" : "1000"
}

```
Account already exists response:
**Status: 400 Bad Request**
```javascript
Account with account Number:100 already exists.
```
---
**/{accountNumber}** - account Number
**GET** - Retrieves account details based on the account number supplied

Response:
**Status: 200 OK**
```javascript
{
    "accountNumber": 1,
    "userName": "ayush",
    "balance": 1000
}
```
Account with account Number:300 doesn't exists.
**Status: 404 Not Found**

**/{accountNumber}/balance** - account Number
**GET** - Retrieves account details of particular account number

Response:
**Status: 200 OK**
```javascript
{
    "accountNumber": 100,
    "userName": "ayush",
    "balance": 1000
}
```
Account with account Number:500 doesn't exists.
**Status: 404 Not Found**

**/{accountNumber}/deposit/{amount}** - account Number
**PUT** - Deposit money into the account with the given account number

Response:
**Status: 200 OK**
```javascript
{
    "accountNumber": 100,
    "userName": "ayush",
    "balance": 500
}
```
Account with account Number:500 doesn't exists.
**Status: 404 Not Found**

**/{accountNumber}/withdraw/{amount}** - account Number
**PUT** - Withdraw money from the account with the given account number

Response:
**Status: 200 OK**
```javascript
{
    "accountNumber": 100,
    "userName": "ayush",
    "balance": 500
}
```
Account with account Number:500 doesn't exists.
**Status: 404 Not Found**

## Transaction API - `/transaction`

**POST** - Transfer money between the ccounts supplied in the transaction body

**Request Body** -

Sample request:
```javascript
{
		"fromAccountNumber" : "100",
		"toAccountNumber" : "200",
		"amount" : "100"
}
```

Sample response:
**Status: 200 OK**
```javascript
[
    {
        "accountNumber": 100,
        "userName": "ayush",
        "balance": 900
    },
    {
        "accountNumber": 200,
        "userName": "prakhar",
        "balance": 2100
    }
]
```

Source and Target account are the same :
**Status: 400 Bad Request**
```javascript
"Source and Target accounts are same : 100.
```

Not enough balance in the source account to perform the transfer:
**Status: 409 Conflict**
```javascript
Insufficient balance in the account to initiate the transfer.
```

Negative amount requested to be transferred
**Status: 400 Bad Request**
```javascript
Amount given is invalid. Cannot proceed with transaction
```
