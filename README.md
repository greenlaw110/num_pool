# NumPool util

A library to take/return number from/to a pool

## API

```java
long max = 10L;
NumPool pool = new NumPool(0, max);
pool.take(3); // take a number from the pool
System.out.println(pool); // print out "0-2,4-10"
pool.offer(3); // return a number to the pool
System.out.println(pool); // print out "0-10"
```

## Configuration

```java
// set the bitset words limit to 2, meaning a BitSet
// backed a single block can host at most 2*64 = 128 number
NumPoolConfig.configureBitSetWords(2);
```

## Benchmark

Checkout [benchmark](benchmark.md) document to understand the system performance under different configuration

