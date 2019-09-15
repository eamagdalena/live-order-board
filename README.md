# Live Order Board

by _Silver Bars Marketplace_git remote add origin https://github.com/eamagdalena/live-order-board.git

###  Quick start

```
# run tests
$ gradlew test
```

```
# generate library
$ gradlew jar`
```

### How to use

Create a new instance of _LiveOrderBoardService_

Register a new order with:

`service.register(order);`

Delete an existing order with:

`service.delete(order);`  

Get a summary of existing orders with:

`service.summary();`
