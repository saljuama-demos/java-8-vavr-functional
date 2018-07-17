package com.saljuama.javafunctional.functionalinterfaces;

import io.vavr.CheckedConsumer;
import io.vavr.collection.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.*;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class ConsumersTest {

    private class DummyGreeter {
        void simpleGreeting(String name) {/* do some work */}

        void enthusiasticGreeting(String name) {/* do some work */}

        void throwingGreeting(String name) throws InterruptedException {
            Thread.sleep(10L);
            /* do some work */
        }
    }

    @Spy
    private final DummyGreeter greeter = new DummyGreeter();


    @Test
    public void consumers_process_an_input_and_return_void() {

        Consumer<String> consoleGreeter = name -> greeter.simpleGreeting(name);

        consoleGreeter.accept("World");

        verify(greeter).simpleGreeting("World");
    }

    @Test
    public void there_are_also_bi_consumers() {

        BiConsumer<String, Integer> multiGreeter = (name, times) -> Stream.range(0, times).forEach(x -> greeter.simpleGreeting(name));

        multiGreeter.accept("World", 5);

        verify(greeter, times(5)).simpleGreeting("World");
    }

    @Test
    public void there_are_also_primitive_specializations() {
        IntConsumer intConsumer = number -> { /* do some work */ };
        intConsumer.accept(10);

        LongConsumer longConsumer = number -> { /* do some work */ };
        longConsumer.accept(10L);

        DoubleConsumer doubleConsumer = number -> { /* do some work */ };
        doubleConsumer.accept(10.50);

        ObjIntConsumer<String> objIntConsumer = (name, number) -> { /* do some work */ };
        objIntConsumer.accept("hello world", 10);

        ObjLongConsumer<String> objLongConsumer = (name, number) -> { /* do some work */ };
        objLongConsumer.accept("hello world", 10L);

        ObjDoubleConsumer<String> objDoubleConsumer = (name, number) -> { /* do some work */ };
        objDoubleConsumer.accept("hello world", 10.50);
    }

    @Test
    public void composing_consumers_apply_all_the_functions_to_the_same_input_in_sequence() {

        Consumer<String> simpleConsoleGreeter = name -> greeter.simpleGreeting(name);
        Consumer<String> enthusiasticGreeter = name -> greeter.enthusiasticGreeting(name);

        simpleConsoleGreeter.andThen(enthusiasticGreeter).accept("World");

        verify(greeter).simpleGreeting("World");
        verify(greeter).enthusiasticGreeting("World");
    }

    @Test
    public void vavr_offers_checked_exception_helper() {

        CheckedConsumer<String> throwingGreeter = name -> greeter.throwingGreeting(name);

        try {
            throwingGreeter.accept("World");
            verify(greeter).throwingGreeting("World");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
