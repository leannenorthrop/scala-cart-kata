# scala-cart-kata

Travis CI: ![](https://travis-ci.org/leannenorthrop/scala-cart-kata.svg?branch=master)

[Shopping cart kata](http://codekata.com/kata/kata09-back-to-the-checkout/) in Scala to learn about state transitions, sbt, scala-test etc.

## Execute tests:
```
sbt clean coverage test
```

Create coverage report:
```
sbt coverageReport
```
## Run
For shop with just oranges and apples:

```
sbt run "orange,apple,orange"
```
For a more extensive shop:

```
sbt run "orange,apple,orange" "orange:50,apple:40,grapes:100,..."
```

For a more extensive shop with offers:

```
sbt run "orange,apple,orange" "orange:50,apple:40,grapes:100,..." "orange:3;100,apple:2;60"
```

Where offers are a pair of product-name:rule and rule is quantity;price-in-pence.
