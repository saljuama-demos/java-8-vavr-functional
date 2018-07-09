package com.saljuama.dojo.javafunctional.functionalfeatures;

import io.vavr.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class PartialApplicationTest {

    @Test
    public void functions_can_be_partially_applied_and_evaluated_lazily() {

        Function2<Integer, Integer, Integer> sum = (a, b) -> {
            System.out.println("Evaluating the function now");
            return a + b;
        };

        Function1<Integer, Integer> partialAppliedSum = sum.apply(2);

        System.out.println("After applying the first parameter");

        Integer allParamsAppliedSum = partialAppliedSum.apply(3);

        System.out.println("After applying the second parameter");

        assertEquals(new Integer(5), allParamsAppliedSum);
    }

    @Test
    public void for_each_partial_application_returns_a_function_with_one_less_arity_than_the_original() {

        Function8<Integer, Integer, Integer, Integer,Integer, Integer, Integer, Integer, String> veryLargeFunction =
                (a, b, c, d, e, f, g, h) -> "hello";

        assertTrue(veryLargeFunction.apply(1) instanceof Function7);
        assertTrue(veryLargeFunction.apply(1, 2, 3, 4) instanceof Function4);
    }
}