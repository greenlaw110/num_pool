# Benchmark

## Scenario I

Small pool: 1M numbers

### Case 1.1

| pool size | loop | bitset words |
| --------- | ---- | ------------ |
| 1M | 10M | 2 |

```text
#block of pool: 14382
consumed size: 500074
it takes 26321ms to finish 5250037 times take and 4749963 times offer
memory used: 504MB, free: 199MB
memory used | after clear consumed: 504MB, free: 199MB
memory used | after gc: 60MB, free: 645MB
pool toString takes: 21ms
```

### Case 1.2

| pool size | loop | bitset words |
| --------- | ---- | ------------ |
| 1M | 10M | 4 |

```text
#block of pool: 7551
consumed size: 500250
it takes 24838ms to finish 5250125 times take and 4749875 times offer
memory used: 485MB, free: 217MB
memory used | after clear consumed: 485MB, free: 217MB
memory used | after gc: 65MB, free: 639MB
pool toString takes: 27ms
```

### Case 1.3

| pool size | loop | bitset words |
| --------- | ---- | ------------ |
| 1M | 10M | 8 |

```text
#block of pool: 3791
consumed size: 500290
it takes 22541ms to finish 5250145 times take and 4749855 times offer
memory used: 480MB, free: 222MB
memory used | after clear consumed: 480MB, free: 222MB
memory used | after gc: 64MB, free: 639MB
pool toString takes: 40ms
```

### Case 1.4

| pool size | loop | bitset words |
| --------- | ---- | ------------ |
| 1M | 10M | 64 |

```test
#block of pool: 483
consumed size: 499070
it takes 18879ms to finish 5249535 times take and 4750465 times offer
memory used: 476MB, free: 226MB
memory used | after clear consumed: 476MB, free: 226MB
memory used | after gc: 59MB, free: 644MB
pool toString takes: 26ms
```

### Case 1.5

| pool size | loop | bitset words |
| --------- | ---- | ------------ |
| 1M | 10M | 1K |

```text
#block of pool: 31
consumed size: 500714
it takes 16402ms to finish 5250357 times take and 4749643 times offer
memory used: 466MB, free: 237MB
memory used | after clear consumed: 466MB, free: 237MB
memory used | after gc: 59MB, free: 645MB
pool toString takes: 32ms
```


## Scenario II

Big pool: 4294967296 numbers

### Case 2.1

| pool size | loop | bitset words |
| --------- | ---- | ------------ |
| 4B | 10M | 2 |

```text
#block of pool: 9499963
consumed size: 9953620
it takes 52147ms to finish 9976810 times take and 23190 times offer
memory used: 1385MB, free: 771MB
memory used | after clear consumed: 1385MB, free: 771MB
memory used | after gc: 684MB, free: 1561MB
pool toString takes: 2741ms
```

### Case 2.2

| pool size | loop | bitset words |
| --------- | ---- | ------------ |
| 4B | 10M | 4 |

```test
#block of pool: 8603717
consumed size: 9953612
it takes 54258ms to finish 9976806 times take and 23194 times offer
memory used: 1373MB, free: 763MB
memory used | after clear consumed: 1373MB, free: 763MB
memory used | after gc: 675MB, free: 1525MB
pool toString takes: 2457ms
```

### Case 2.3

| pool size | loop | bitset words |
| --------- | ---- | ------------ |
| 4B | 10M | 8 |

```text
#block of pool: 6665068
consumed size: 9953442
it takes 52644ms to finish 9976721 times take and 23279 times offer
memory used: 1390MB, free: 766MB
memory used | after clear consumed: 1390MB, free: 766MB
memory used | after gc: 681MB, free: 1475MB
pool toString takes: 3201ms
```

### Case 2.4

| pool size | loop | bitset words |
| --------- | ---- | ------------ |
| 4B | 10M | 64 |

```text
#block of pool: 1047556
consumed size: 9953360
it takes 44858ms to finish 9976680 times take and 23320 times offer
memory used: 1317MB, free: 593MB
memory used | after clear consumed: 1317MB, free: 593MB
memory used | after gc: 597MB, free: 1364MB
pool toString takes: 2325ms
```

### Case 2.5

| pool size | loop | bitset words |
| --------- | ---- | ------------ |
| 4B | 10M | 1K |

```text
#block of pool: 65561
consumed size: 9953630
it takes 39023ms to finish 9976815 times take and 23185 times offer
memory used: 1175MB, free: 656MB
memory used | after clear consumed: 1175MB, free: 656MB
memory used | after gc: 523MB, free: 1309MB
pool toString takes: 2170ms
```

### Case 2.6

| pool size | loop | bitset words |
| --------- | ---- | ------------ |
| 4B | 10M | 1M |


```text
#block of pool: 61
consumed size: 9953276
it takes 26588ms to finish 9976638 times take and 23362 times offer
memory used: 1181MB, free: 542MB
memory used | after clear consumed: 1181MB, free: 542MB
memory used | after gc: 495MB, free: 1272MB
pool toString takes: 1964ms
```

### Case 2.7

| pool size | loop | bitset words |
| --------- | ---- | ------------ |
| 4B | 10M | 4B/64 |


```text
#block of pool: 2
consumed size: 9953950
it takes 24981ms to finish 9976975 times take and 23025 times offer
memory used: 1210MB, free: 248MB
memory used | after clear consumed: 1210MB, free: 248MB
memory used | after gc: 519MB, free: 1014MB
pool toString takes: 2291ms
```

## Summary

| pool | words | loop | block# | mem | mem(gc) | loop time | toString time |
| ---- | ----- | ---- | ------ | --- | ------- | --------- | ------------- |
| Big | 4B/64 | 10M | 2 | 1210M | 519M | 24981ms | 2291ms |
| Big | 1M | 10M | 61 | 1181M | 495M | 26588ms | 1964ms |
| Big | 1K | 10M | 64K | 1175M | 523M | 39023ms | 2170ms |
| Big | 64 | 10M | 1M | 1317M | 597M | 44858ms | 2325ms |
| Big | 8| 10M | 64M | 1390M | 681M | 62644ms | 3201ms |
| Big | 4| 10M | 8M | 1373M | 675M | 54258ms | 2475ms |
| Big | 2| 10M | 9M | 1385 | 684M | 52147ms | 2741ms |
| Big | 1| 10M | 9M | 1364 | 690M | 54378ms | 2814m |



