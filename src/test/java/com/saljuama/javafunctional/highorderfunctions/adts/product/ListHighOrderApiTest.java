package com.saljuama.javafunctional.highorderfunctions.adts.product;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.vavr.control.Option.none;
import static io.vavr.control.Option.some;
import static org.junit.Assert.assertEquals;


public class ListHighOrderApiTest {

    private final List<Integer> numbers = List.of(1, 2, 3, 4, 5);
    private final List<String> letters = List.of("a", "b", "c", "d", "e");

    @Test
    public void applying_a_function_that_transforms_one_element_into_another_with_map_to_a_list() {

        Function<Integer, String> stringifyNumber = x -> x.toString();

        assertEquals(List.of("1", "2", "3", "4", "5"), numbers.map(stringifyNumber));
    }

    @Test
    public void applying_a_function_that_transforms_one_element_into_a_collection_with_flatMap_to_a_list_joins_all_the_collections_into_one() {

        Function<Integer, List<Integer>> numberAndNext = x -> List.of(x, x + 1);

        assertEquals(List.of(1, 2, 2, 3, 3, 4, 4, 5, 5, 6), numbers.flatMap(numberAndNext));
    }

    @Test
    public void filtering_with_a_predicate_returns_a_list_elements_that_satisfy_the_condition_according_to_the_filter_function() {

        Predicate<Integer> isEvenPredicate = x -> x % 2 == 0;

        assertEquals(List.of(2, 4), numbers.filter(isEvenPredicate));

        assertEquals(List.of(1, 3, 5), numbers.removeAll(isEvenPredicate));

        assertEquals(List.of(1, 3, 4, 5), numbers.removeFirst(isEvenPredicate));

        assertEquals(List.of(1, 2, 3, 5), numbers.removeLast(isEvenPredicate));

    }

    @Test
    public void find_unique_elements_in_a_list_using_a_discriminator_function() {

        Function<Integer, Boolean> isEvenFunction = x -> x % 2 == 0;

        assertEquals(List.of(1, 2), numbers.distinctBy(isEvenFunction));
    }

    @Test
    public void folding_applies_a_function_to_elements_2_by_2_until_reducing_the_list_to_a_single_element() {

        BiFunction<String, String, String> concatenate = (a, b) -> a + b;

        assertEquals("_abcde", letters.fold("_", concatenate));

        assertEquals("_abcde", letters.foldLeft("_", concatenate));

        assertEquals("abcde_", letters.foldRight("_", concatenate));

        assertEquals("abcde", letters.reduce(concatenate));
    }

    @Test
    public void min_max_maybe_return_value_from_a_list_using_a_function_for_custom_comparision_between_elements() {

        Function<String, Integer> firstCharToAsciiValue = x -> (int) x.charAt(0); // casting char to int returns ascii value

        assertEquals(some("a"), letters.minBy(firstCharToAsciiValue));

        assertEquals(some("e"), letters.maxBy(firstCharToAsciiValue));

        Function<String, Integer> firstCharToNegativeAsciiValue = x -> ((int) x.charAt(0)) * -1;

        assertEquals(some("e"), letters.minBy(firstCharToNegativeAsciiValue));

        assertEquals(none(), List.<String>empty().maxBy(firstCharToAsciiValue));
    }

    @Test
    public void lists_can_be_partitioned_by_functions_that_evaluate_conditions() {

        Function<String, Boolean> isVowelFunction = x -> x.matches("[aeiouAEIOU]");

        assertEquals(
                HashMap.of(
                        true, List.of("a", "e"),
                        false, List.of("b", "c", "d")
                ),
                letters.groupBy(isVowelFunction)
        );

        Predicate<String> isVowelPredicate = x -> x.matches("[aeiouAEIOU]");

        assertEquals(
                Tuple.of(
                        List.of("a", "e"),
                        List.of("b", "c", "d")
                ),
                letters.partition(isVowelPredicate)
        );
    }

    @Test
    public void lists_can_be_split_into_2_by_position_of_an_element_matching_a_predicate() {

        Predicate<String> isVowel = x -> x.matches("[aeiouAEIOU]");

        assertEquals(
                Tuple.of(
                        List.of("a"),
                        List.of("b", "c", "d", "e")
                ),
                letters.splitAt(isVowel.negate())
        );

        assertEquals(
                Tuple.of(
                        List.of("a", "b"),
                        List.of("c", "d", "e")
                ),
                letters.splitAtInclusive(isVowel.negate())
        );
    }

    @Test
    public void lists_can_be_sliced_in_different_ways() {

        Predicate<Integer> isFour = x -> x == 4;
        Predicate<Integer> isNotFour = isFour.negate();

        assertEquals(List.of(1, 2), numbers.take(2));
        assertEquals(List.of(3, 4, 5), numbers.drop(2));

        assertEquals(List.of(1, 2, 3), numbers.takeUntil(isFour));
        assertEquals(List.of(4, 5), numbers.dropUntil(isFour));

        assertEquals(List.of(1, 2, 3), numbers.takeWhile(isNotFour));
        assertEquals(List.of(4,5), numbers.dropWhile(isNotFour));

        assertEquals(List.of(4, 5), numbers.takeRight(2));
        assertEquals(List.of(1, 2, 3), numbers.dropRight(2));

        // no take right until
        assertEquals(List.of(1, 2, 3, 4), numbers.dropRightUntil(isFour));

        // no take right while
        assertEquals(List.of(1, 2, 3, 4), numbers.dropRightWhile(isNotFour));

    }

    // TODO: collect peek scan sort zip unzip find

}
