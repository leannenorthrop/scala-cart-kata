# scala-cart-kata

[![Travis Build](https://travis-ci.org/leannenorthrop/scala-cart-kata.svg?branch=master)](https://travis-ci.org/leannenorthrop/scala-cart-kata)
[![Coverage Status](https://coveralls.io/repos/leannenorthrop/scala-cart-kata/badge.svg?branch=master&service=github)](https://coveralls.io/github/leannenorthrop/scala-cart-kata?branch=master)

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
sbt clean compile "run orange,apple,orange"
```


