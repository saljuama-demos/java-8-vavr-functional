package com.saljuama.dojo.javafunctional.functionalfeatures;

import io.vavr.Function2;
import io.vavr.control.Option;
import org.junit.Test;

import static io.vavr.control.Option.none;
import static io.vavr.control.Option.some;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LiftingTest {

    @Test
    public void partial_functions_can_be_converted_to_total_functions_with_lifting() {

        Function2<Integer, Integer, Integer> divideBy = (a, b) -> a / b;

        try {
            divideBy.apply(1, 0);
            fail("when divideBy 0 does not throw an exception");
        } catch (Exception e) {
            System.out.println("divide by is not a total function because is not defined for b=0, so it is a partial function");
        }

        Function2<Integer, Integer, Option<Integer>> safeDivideBy = Function2.lift(divideBy);

        assertEquals(some(4), safeDivideBy.apply(8, 2));
        assertEquals(none(), safeDivideBy.apply(1, 0));
    }
}
