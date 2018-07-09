package com.saljuama.dojo.javafunctional.functionalinterfaces;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class SuppliersTest {

    private class DummySupplier {
        String supplyGreet() {
            return "Hello World";
        }
    }

    @Spy
    private final DummySupplier dummy = new DummySupplier();

    @Test
    public void suppliers_are_used_to_lazily_generate_values() {

        Supplier<String> sayHello = () -> dummy.supplyGreet();

        verify(dummy, never()).supplyGreet();
        assertEquals("Hello World", sayHello.get());
        verify(dummy).supplyGreet();
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
