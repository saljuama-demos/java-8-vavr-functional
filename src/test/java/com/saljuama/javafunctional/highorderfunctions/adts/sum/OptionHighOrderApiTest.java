package com.saljuama.javafunctional.highorderfunctions.adts.sum;

import io.vavr.PartialFunction;
import io.vavr.control.Option;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.vavr.API.*;
import static io.vavr.control.Option.none;
import static io.vavr.control.Option.some;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class OptionHighOrderApiTest {

    private final Option<Integer> maybeNumber = some(10);
    private final Option<Integer> noNumber = none();


    @Test
    public void option_value_can_be_mapped_to_another_value_with_a_high_order_function() {

        Function<Integer, String> stringifyNumber = x -> x.toString();

        Option<String> maybeResult = maybeNumber.map(stringifyNumber);
        maybeResult.forEach(result -> assertEquals("10", result));

        Option<String> noResult = noNumber.map(stringifyNumber);
        noResult.forEach(result -> fail("should not apply the function"));
    }

    @Test
    public void option_value_can_be_mapped_to_another_value_with_a_high_order_function_that_returns_also_option() {

        Function<Integer, Option<String>> maybeStringifyNumber = x -> Option.of(x.toString());

        Option<String> maybeResult = maybeNumber.flatMap(maybeStringifyNumber);
        maybeResult.forEach(result -> assertEquals("10", result));

        Option<String> noResult = noNumber.flatMap(maybeStringifyNumber);
        noResult.forEach(result -> fail("should not apply the function"));
    }

    @Test
    public void option_values_can_be_filtered_with_a_predicate() {

        Predicate<Integer> greaterThanFive = x -> x > 5;
        maybeNumber.filter(greaterThanFive).forEach(result -> assertEquals(new Integer(10), result));

        Predicate<Integer> greaterThanTwenty = x -> x > 20;
        maybeNumber.filter(greaterThanTwenty).forEach(result -> fail("should not pass the filter"));
    }

    @Test
    public void option_values_can_be_collected_with_partial_functions_aka_pattern_matching() {

        // Can't use the original type Option<Integer>, has to be a Serializable for VAVR to be able to generate the Case
        // but this is usually hidden, here is extracted as a variable for demonstration purposes
        Function<Serializable, String> stringifyNumber = x -> x.toString();
        PartialFunction<Serializable, String> anyNumber = Case($(Some($())), stringifyNumber);
        PartialFunction<Serializable, String> onlyFive = Case($(Some(5)), stringifyNumber);

        maybeNumber.collect(anyNumber).forEach(result -> assertEquals("10", result));
        noNumber.collect(anyNumber).forEach(result -> fail("should not be picked up by the partial function"));
        maybeNumber.collect(onlyFive).forEach(result -> fail("should not be picked up by the partial function"));
    }

    private class DummyCollaborator {
        void simulateAnyWorkWithValue(Integer ignoredValue) {}
    }

    @Spy
    private final DummyCollaborator dummy = new DummyCollaborator();

    @Test
    public void side_effects_can_be_performed_to_option_values_if_they_are_defined() {

        Consumer<Integer> simulateWork = x -> dummy.simulateAnyWorkWithValue(x);

        noNumber.peek(simulateWork).forEach(result -> fail("should not contain any value"));
        verify(dummy, never()).simulateAnyWorkWithValue(anyInt());

        maybeNumber.peek(simulateWork).forEach(result -> assertEquals(new Integer(10), result));
        verify(dummy).simulateAnyWorkWithValue(10);
    }
}
