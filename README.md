# DOJO: JAVA 8 FUNCTIONAL

## Lambdas

A lambda expression is an anonymous function that can be: 

 - assigned to a variable
 - passed to a method
 - returned from a method


## Functional Interfaces

Any interface with a SAM (Single Abstract Method) is a functional interface, and the recommendation is that they are 
annotated with the informational `@FunctionalInterface` annotation, as the compiler can emmit an error in case there are
multiple abstract methods in it, but it is not mandatory. When multiple methods are present in a functional interface, 
all except one method, MUST provide a default implementation.

The reason of enforcing SAM is because the invocation can be easily replaced with a lambda.

Although it is possible to create your own custom functional interfaces, the Java library already provides, a collection 
of common functional interfaces, they can be found in the `java.util.function` package, and they come in both 
generic and primitive specialization flavours (see on the sections below).

The most commonly used functional interfaces are:

### Predicates

This functional interface is used to **evaluate conditions**. Given some input(s), return a boolean.
 
#### Generic format

```java
@FunctionalInterface
interface Predicate<T> {
    boolean test(T input); 
    default Predicate<T> negate() { ... }
    default Predicate<T> and(Predicate<T> other) { ... }
    default Predicate<T> or(Predicate<T> other) { ... }
}
```

There is also a `BiPredicate<T,U>` which is the same as `Predicate<T>` but accepting 2 inputs.

#### Primitive Specializations

- `IntPredicate`
- `LongPredicate`
- `DoublePredicate`

#### VAVR

VAVR offers some helpers, found in the `io.vavr.Predicates` class, to ease the composition of multiple predicates, and to 
to use predicates with collections.
