#DateBuilder#

##Creating dates, doing simple operations or comparisons, transforming can be difficult in Java, even with latest versions##

We (La Roue Verte) are sharing this library we've built during years to ease handling java dates. We share it with the community

This code is licensed in Gnu GPL v3

All comments and suggestions are welcomed !

##Usage##
DateBuilder class contains several "builder" methods to create a new date : 

```java
DateBuilder.now();
DateBuilder.dateTime(2024, 3, 31, 0, 29);
// Returns DateBuilder set to 2024-3-31 00:29
```

All these builder methods returns a DateBuilder that is a modifiable builder. One can move in time 10 minutes later, : 

```java
DateBuilder dateBuilder = DateBuilder.dateTime(2024, 3, 31, 0, 29);
dateBuilder.addMinutes(10);
// Changes to 2024-3-31 00:39

```

You can set date fields : 

```java
DateBuilder dateBuilder = DateBuilder.dateTime(2024, 3, 31, 0, 29);
dateBuilder.setHour(10);
// Changes to 2024-3-31 10:29
```

The DateBuilder can be casted to a DateConstant object to prevent modifying its state
The toXXXXX methods allow to transform the date in String or Object : 

```java
DateBuilder dateBuilder = DateBuilder.dateTime(2024, 3, 31, 0, 29);
dateBuilder.toISO8601WithTimeZone();
```

The test methods let you know useful information :
 
```java
DateBuilder dateBuilder = DateBuilder.dateTime(2025, 1, 31, 0, 29);
dateBuilder.isWeekDay();
// Returns true because it was a Friday
```


