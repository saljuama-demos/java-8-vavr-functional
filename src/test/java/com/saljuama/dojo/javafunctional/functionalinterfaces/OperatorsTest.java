package com.saljuama.dojo.javafunctional.functionalinterfaces;

import org.junit.Test;

import java.util.function.*;

import static org.junit.Assert.assertEquals;


public class OperatorsTest {

    @Test
    public void unary_operators_are_functions_that_take_an_input_and_return_an_output_with_the_same_type() {

        UnaryOperator<Integer> negator = a -> -a;

        assertEquals(new Integer(-5), negator.apply(5));
    }

    @Test
    public void binary_operators_are_functions_that_takes_two_inputs_of_the_same_type_and_return_an_output_of_the_same_type() {

        BinaryOperator<Integer> sum = (a, b) -> a + b;

        assertEquals(new Integer(5), sum.apply(2,3));
    }

    @Test
    public void there_are_also_numeric_primitive_specializations() {

        IntUnaryOperator intNegator = a -> -a;
        assertEquals(-5, intNegator.applyAsInt(5));

        LongUnaryOperator longNegator = a -> -a;
        assertEquals(-5L, longNegator.applyAsLong(5L));

        DoubleUnaryOperator doubleNegator = a -> -a;
        assertEquals(-5.5, doubleNegator.applyAsDouble(5.5), 0.0);

        IntBinaryOperator intSum = (a,b) -> a + b;
        assertEquals(5, intSum.applyAsInt(2, 3));

        LongBinaryOperator longSum = (a,b) -> a + b;
        assertEquals(5L, longSum.applyAsLong(2L, 3L));

        DoubleBinaryOperator doubleSum = (a,b) -> a + b;
        assertEquals(5.5, doubleSum.applyAsDouble(2.0, 3.5), 0.0);
    }
}
