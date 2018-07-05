package com.saljuama.dojo.javafunctional.interfaces;

import org.junit.Test;

import java.util.function.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SuppliersTest {

    @Test
    public void suppliers_are_used_to_lazily_generate_values() {

        Supplier<String> sayHello = () -> {
            System.out.println("I'm generating the value now");
            return "Hello World";
        };

        System.out.println("I didn't generate the value yet, because I didn't invoke get()");

        assertEquals("Hello World", sayHello.get());
    }

    @Test
    public void there_are_also_specialized_primitives_suppliers() {
        BooleanSupplier alwaysTrue = () -> true;
        assertTrue(alwaysTrue.getAsBoolean());

        IntSupplier alwaysTen = () -> 10;
        assertEquals(10, alwaysTen.getAsInt());

        LongSupplier alwaysEleven = () -> 11L;
        assertEquals(11L, alwaysEleven.getAsLong());

        DoubleSupplier alwaysHalf = () -> 0.5;
        assertEquals(0.5, alwaysHalf.getAsDouble(), 0.0);
    }

}
