# JAVA 8 VAVR FUNCTIONAL

This repository is used as a demonstration of the features related to functions included in Java 8, and also the ones
provided by the [VAVR](http://www.vavr.io/) library.


## Functional Interfaces

In a functional programming language, functions are first class citizen. Java is not a functional programming language, 
but Java 8 brought some features from the FP paradigm, in the form of lambdas and functional interfaces.

Any interface with a SAM (Single Abstract Method) is a functional interface, and the recommendation is that they are 
annotated with the informational `@FunctionalInterface` annotation, as the compiler can emmit an error in case there are
multiple abstract methods in it, but it is not mandatory. When multiple methods are present in a functional interface, 
all except one method, MUST provide a default implementation.

The reason of enforcing SAM is because the invocation can be easily replaced with a lambda.

Although it is possible to create your own custom functional interfaces, the Java library already provides, a collection 
of common functional interfaces, they can be found in the `java.util.function` package, and they come in both 
generic and primitive specialization flavours (see on the sections below).

The most commonly used functional interfaces are:


### Functions

This functional interface represents a function that takes one input and returns an output. 

#### Generic format

```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T var1);
    default <V> Function<V, R> compose(Function<V, T> otherFunction) { ... }
    default <V> Function<T, V> andThen(Function<R, V> otherFunction) { ... }
}
```

#### Primitive specializations

- IntFunction<T>
- LongFunction<T>
- DoubleFunction<T>
- IntToLongFunction
- IntToDoubleFunction
- LongToIntFunction
- LongToDoubleFunction
- DoubleToIntFunction
- DoubleToLongFunction
- ToIntFunction<T>
- ToLongFunction<T>
- ToDoubleFunction<T>

#### VAVR

Java offers functional interfaces for functions with 1 or 2 parameters. VAVR offers interfaces for functions that take
from 0 parameters to 8 parameters.

Java lambdas do not play well with throwing checked exceptions, so the recommendation is to use unchecked exceptions.
But in the event integration with a 3rd party library is required, and the interfaces throw checked exceptions,
VAVR offers the `CheckedFunctionX` interfaces, from 0 to 8, with the `apply` method that includes the throwing exception
in the signature.


### Suppliers

This `Function` specialization, takes no inputs and returns **lazily generate values**.

#### Generic format

```java
@FunctionalInterface
interface Supplier<T> {
    T get(); 
}
```

#### Primitive Specializations

- `BooleanSupplier`
- `IntSupplier`
- `LongSupplier`
- `DoubleSupplier`

It is worthy to mention that these specializations have specific names for the SAM method, in order to prevent the 
compiler to complain about ambiguous method signatures. 


### Consumers

This `Function` specialization, consumes an input and does not return anything. The most common use case is to use them
as finishers / sinks in a chain of function compositions.

Due to this interface not returning any value, testing `Consumers` is not straightforward, having to resort to 
verification testing.

#### Generic format

```java
@FunctionalInterface
interface Consumer<T> {
    void accept(T input);
    default Consumer<T> andThen(Consumer<T> after) { ... };
}
```

#### Primitive Specializations

- `IntConsumer`
- `LongConsumer`
- `DoubleConsumer`
- `ObjIntConsumer<T>`
- `ObjLongConsumer<T>`
- `ObjDoubleConsumer<T>`

#### VAVR

Java lambdas do not play well with throwing checked exceptions, so the recommendation is to use unchecked exceptions.
But in the event integration with a 3rd party library is required, and the interfaces throw checked exceptions,
VAVR offers the `CheckedConsumer<T>` interface that adds to the `accept` method the throwing Exception signature.

This could also be achieved by creating a custom `@FunctionalInterface` with a `throws` in the abstract method signature.


### Operators

This `Function` specialization, are still functions but limited to one single type for all inputs and output.

#### Generic format

```java
@FunctionalInterface
public interface UnaryOperator<T> extends Function<T, T> { ... }

@FunctionalInterface
public interface BinaryOperator<T> extends BiFunction<T, T, T> { ... }
```

#### Primitive specializations

- IntUnaryOperator
- LongUnaryOperator
- DoubleUnaryOperator
- IntBinaryOperator
- LongBinaryOperator
- DoubleBinaryOperator


### Predicates

This `Function` specialization, takes an input and returns the result of **evaluating conditions** based on the given input.
 
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


## Other FP features (with VAVR)

Functional Programming is not just including functions and that is it, there are more features to it, that Java didn't 
bring into the language, but VAVR did bring some of them.

The limitation is, that these features are provided in the VAVR functional interfaces `FunctionX` and not in the Java ones.


### Currying

Currying is function decomposition, breaking down a function that takes several parameters into a series of functions 
that only accept one single parameter.

This enables to pass parameters one at a time to a function:

```
f(x,y,z) -> f(x)(y)(z)
```

### Partial application

Partial application refers to the process of fixing a number of arguments to a function, producing another function of smaller arity.

This is different than currying in: 

- With partial application, more than one parameter can be passed at a given time, while in currying only one can be passed at a time.
- The result of a partial application is a function with an arity that can be more than one, and with currying, the result
is always a series of functions with arity = 1 always. 

```
f(x,y,z) -> f(x,y)(z)  or  f(x)(y,z)  or  f(x)(y)(z)
```

### Lifting

Lifting is to transform a partial function (function that it is not defined for all inputs) into a total function (it is 
defined for all the inputs).

VAVR achieves this by wrapping the output of the function with the `Option` monad, and returning `None` for the values
where the partial function is not defined (an exception is thrown), and `Some(value)` for those inputs where the function
is defined.

### Memoization

Memoization is an optimization technique used primarily to speed up computer programs by storing the results of expensive
function calls and returning the cached result when the same inputs occur again.
