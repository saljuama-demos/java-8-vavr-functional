package com.saljuama.dojo.javafunctional.functionalinterfaces;

import io.vavr.CheckedFunction1;
import io.vavr.Function0;
import io.vavr.Function3;
import io.vavr.Function8;
import io.vavr.collection.Stream;
import org.junit.Test;

import java.util.function.*;

import static org.junit.Assert.assertEquals;


public class FunctionsTest {

    @Test
    public void functions_take_an_input_and_return_an_output() {

        Function<String, String> upperCaser = string -> string.toUpperCase();

        assertEquals("HELLO", upperCaser.apply("hello"));
    }

    @Test
    public void functions_can_be_composed() {

        Function<Integer, String> stringifier = number -> number.toString();
        Function<String, String> enthusiastic = string -> string + "!!!";

        Function<Integer, String> enthusiasticStringifier1 = enthusiastic.compose(stringifier);
        assertEquals("10!!!", enthusiasticStringifier1.apply(10));

        Function<Integer, String> enthusiasticStringifier2 = stringifier.andThen(enthusiastic);
        assertEquals("10!!!", enthusiasticStringifier2.apply(10));
    }

    @Test
    public void identity_function_just_returns_the_input() {

        Function<Integer, Integer> integerIdentity = number -> number;

        assertEquals(integerIdentity.apply(1), Function.identity().apply(1));
    }

    @Test
    public void there_are_also_bi_functions() {

        BiFunction<String, Integer, String> stringRepeater = (string, number) ->
                Stream.from(0).take(number).map(x -> string).mkString();

        assertEquals("hellohellohello", stringRepeater.apply("hello", 3));
    }

    @Test
    public void there_are_also_primitive_specializations() {

        IntFunction<String> intFunction = number -> "number " + number;
        assertEquals("number 10", intFunction.apply(10));

        LongFunction<String> longFunction = number -> "number " + number;
        assertEquals("number 10", longFunction.apply(10L));

        DoubleFunction<String> doubleFunction = number -> "number " + number;
        assertEquals("number 10.5", doubleFunction.apply(10.50));

        IntToLongFunction intToLongFunction = number -> number;
        assertEquals(10L, intToLongFunction.applyAsLong(10));

        IntToDoubleFunction intToDoubleFunction = number -> number;
        assertEquals(10.0, intToDoubleFunction.applyAsDouble(10), 0.0);

        LongToIntFunction longToIntFunction = number -> Long.valueOf(number).intValue();
        assertEquals(10, longToIntFunction.applyAsInt(10L));

        LongToDoubleFunction longToDoubleFunction = number -> number;
        assertEquals(10.0, longToDoubleFunction.applyAsDouble(10L), 0.0);

        DoubleToIntFunction doubleToIntFunction = number -> Double.valueOf(number).intValue();
        assertEquals(10, doubleToIntFunction.applyAsInt(10.1));

        DoubleToLongFunction doubleToLongFunction = number -> Double.valueOf(number).longValue();
        assertEquals(10L, doubleToLongFunction.applyAsLong(10.1));

        ToIntFunction<String> toIntFunction = string -> Integer.parseInt(string);
        assertEquals(10, toIntFunction.applyAsInt("10"));

        ToLongFunction<String> toLongFunction = string -> Long.parseLong(string);
        assertEquals(10L, toLongFunction.applyAsLong("10"));

        ToDoubleFunction<String> toDoubleFunction = string -> Double.parseDouble(string);
        assertEquals(10.50, toDoubleFunction.applyAsDouble("10.50"), 0.0);
    }

    @Test
    public void vavr_offers_multiple_parameter_helpers() {

        Function0<String> greeter = () -> "Hello!";
        assertEquals("Hello!", greeter.apply());

        Function3<Integer, Integer, Integer, String> sumThreeAndStringify =
                (a, b, c) -> ((Integer) (a + b + c)).toString();
        assertEquals("6", sumThreeAndStringify.apply(1, 2, 3));

        Function8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, String> sumEightAndStringify =
                (a, b, c, d, e, f, g, h) -> ((Integer) (a + b + c + d + e + f + g + h)).toString();
        assertEquals("8", sumEightAndStringify.apply(1, 1, 1, 1, 1, 1, 1, 1));
    }

    @Test(expected = NumberFormatException.class)
    public void vavr_offers_checked_exception_throwing_functions() throws Throwable {

        CheckedFunction1<String, Integer> parseInteger = string -> Integer.parseInt(string);

        parseInteger.apply("not a number");
    }
}
