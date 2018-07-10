package com.saljuama.javafunctional.functionalinterfaces;

import io.vavr.Predicates;
import io.vavr.collection.List;
import org.junit.Test;

import java.util.function.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class PredicatesTest {

    @Test
    public void predicates_are_used_to_test_conditions() {
        Predicate<Integer> greaterThanZero = input -> input > 0;

        assertTrue(greaterThanZero.test(1));
        assertFalse(greaterThanZero.test(-1));
    }

    @Test
    public void there_are_also_bi_predicates() {
        BiPredicate<String, Integer> isSizeOf = (string, integer) -> string.length() == integer;

        assertTrue(isSizeOf.test("Hello", 5));
        assertFalse(isSizeOf.test("World", 10));
    }

    @Test
    public void there_are_also_specialized_numeric_primitives_predicates() {
        IntPredicate isEqualToHundred = input -> input == 100;
        assertTrue(isEqualToHundred.test(100));

        DoublePredicate isGreaterThanHalf = input -> input > 0.5;
        assertTrue(isGreaterThanHalf.test(0.6));

        LongPredicate isEqualToBillion = input -> input == 1000000000L;
        assertFalse(isEqualToBillion.test(10L));
    }

    @Test
    public void predicates_can_be_NOT_composed() {
        Predicate<Integer> greaterThanFive = input -> input > 5;
        Predicate<Integer> lesserOrEqualThanFive = greaterThanFive.negate();

        assertTrue(lesserOrEqualThanFive.test(4));
        assertFalse(lesserOrEqualThanFive.test(6));
    }

    @Test
    public void predicates_can_be_AND_composed() {
        Predicate<Integer> greaterThanZero = input -> input > 0;
        Predicate<Integer> lesserThanTen = input -> input < 10;
        Predicate<Integer> betweenOneAndNine = greaterThanZero.and(lesserThanTen);

        assertTrue(betweenOneAndNine.test(5));
        assertFalse(betweenOneAndNine.test(15));
    }

    @Test
    public void predicates_can_be_OR_composed() {
        Predicate<Integer> lesserOrEqualThanZero = input -> input <= 0;
        Predicate<Integer> greaterOrEqualThanTen = input -> input >= 10;
        Predicate<Integer> outsideOfRangeZeroToTen = lesserOrEqualThanZero.or(greaterOrEqualThanTen);

        assertTrue(outsideOfRangeZeroToTen.test(-5));
        assertTrue(outsideOfRangeZeroToTen.test(15));
        assertFalse(outsideOfRangeZeroToTen.test(5));
    }

    @Test
    public void vavr_offers_predicate_composition_helpers() {

        assertTrue(Predicates.noneOf(
                // need to specify type at least on one when inlined ¯\_(ツ)_/¯
                (Predicate<String>) input -> input.matches("[aeiouAEIOU]"),
                input -> input.matches("[A-Z]"),
                input -> input.matches("[a-z]")
        ).test(""));

        assertTrue(Predicates.allOf(
                (Predicate<String>) input -> input.contains(" "),
                input -> input.matches("[^uU]+"),
                input -> input.length() > 10
        ).test("Hello World"));

        assertTrue(Predicates.anyOf(
                (Predicate<Integer>) input -> input % 2 == 0,
                input -> input < 0,
                input -> input == 50
        ).test(10));
    }

    @Test
    public void vavr_offers_predicate_collections_helpers() {

        Predicate<Iterable<Integer>> allArePositive = Predicates.forAll(input -> input > 0);
        assertTrue(allArePositive.test(List.of(1, 2, 3, 4, 5)));

        Predicate<String> x = input -> input.contains("X");
        assertFalse(Predicates.exists(x).test(List.of("a", "b", "c", "x", "y", "z")));

        Predicate<String> isVowel = Predicates.isIn("a", "e", "i", "o", "u", "A", "E", "I", "O", "U");
        assertTrue(isVowel.test("e"));
    }
}