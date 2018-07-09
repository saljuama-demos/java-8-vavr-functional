package com.saljuama.dojo.javafunctional.functionalfeatures;

import io.vavr.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class PartialApplicationTest {

    private class LazyDummy {
        void doNothing() {}
    }

    @Spy
    private final LazyDummy dummy = new LazyDummy();

    @Test
    public void functions_can_be_partially_applied_and_evaluated_lazily() {

        Function2<Integer, Integer, Integer> sum = (a, b) -> {
            dummy.doNothing();
            return a + b;
        };

        Function1<Integer, Integer> partialAppliedSum = sum.apply(2);

        verify(dummy, never()).doNothing();
        assertEquals(new Integer(5), partialAppliedSum.apply(3));
        verify(dummy).doNothing();
    }

    @Test
    public void for_each_partial_application_returns_a_function_with_one_less_arity_than_the_original() {

        Function8<Integer, Integer, Integer, Integer,Integer, Integer, Integer, Integer, String> veryLargeFunction =
                (a, b, c, d, e, f, g, h) -> "hello";

        assertTrue(veryLargeFunction.apply(1) instanceof Function7);
        assertTrue(veryLargeFunction.apply(1, 2, 3, 4) instanceof Function4);
    }
}
