package com.saljuama.javafunctional.highorderfunctions.adts.product;

import io.vavr.collection.List;
import io.vavr.control.Option;
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

        List<String> result = numbers.map(stringifyNumber);

        assertEquals(List.of("1", "2", "3", "4", "5"), result);
    }

    @Test
    public void applying_a_function_that_transforms_one_element_into_a_collection_with_flatMap_to_a_list_joins_all_the_collections_into_one() {

        Function<Integer, List<Integer>> numberAndNext = x -> List.of(x, x + 1);

        List<Integer> result = numbers.flatMap(numberAndNext);

        assertEquals(List.of(1, 2, 2, 3, 3, 4, 4, 5, 5, 6), result);
    }

    @Test
    public void filtering_with_a_predicate_returns_a_list_elements_that_satisfy_the_condition_according_to_the_filter_function() {
        Predicate<Integer> isEven = x -> x % 2 == 0;

        List<Integer> resultFilter = numbers.filter(isEven);
        assertEquals(List.of(2, 4), resultFilter);

        List<Integer> resultRemoveAll = numbers.removeAll(isEven);
        assertEquals(List.of(1, 3, 5), resultRemoveAll);

        List<Integer> resultRemoveFirst = numbers.removeFirst(isEven);
        assertEquals(List.of(1, 3, 4, 5), resultRemoveFirst);

        List<Integer> resultRemoveLast = numbers.removeLast(isEven);
        assertEquals(List.of(1, 2, 3, 5), resultRemoveLast);
    }

    @Test
    public void folding_applies_a_function_to_elements_2_by_2_until_reducing_the_list_to_a_single_element() {

        BiFunction<String, String, String> concatenate = (a, b) -> a + b;

        String result = letters.fold("_", concatenate);
        assertEquals("_abcde", result);

        String resultLeft = letters.foldLeft("_", concatenate);
        assertEquals("_abcde", resultLeft);

        String resultRight = letters.foldRight("_", concatenate);
        assertEquals("abcde_", resultRight);

        String resultReduce = letters.reduce(concatenate);
        assertEquals("abcde", resultReduce);
    }

    @Test
    public void min_max_values_in_a_list_with_a_function_that_transform_elements_in_the_list_into_comparable_values() {

        Function<String, Integer> firstCharToAsciiValue = x -> (int) x.charAt(0);

        Option<String> minLetterByAsciiValue = letters.minBy(firstCharToAsciiValue);
        assertEquals(some("a"), minLetterByAsciiValue);

        Option<String> maxLetterByAsciiValue = letters.maxBy(firstCharToAsciiValue);
        assertEquals(some("e"), maxLetterByAsciiValue);


        Function<String, Integer> firstCharToNegativeAsciiValue = x -> ((int) x.charAt(0)) * -1;
        Option<String> minLetterByNegativeAsciiValue = letters.minBy(firstCharToNegativeAsciiValue);
        assertEquals(some("e"), minLetterByNegativeAsciiValue);

        List<String> emptyList = List.empty();
        Option<String> maxLetterOfEmptyList = emptyList.maxBy(firstCharToAsciiValue);
        assertEquals(none(), maxLetterOfEmptyList);
    }

    // TODO: collect peek distinctBy groupBy drop take partition scan sort split zip unzip find

}
