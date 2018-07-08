package com.saljuama.dojo.javafunctional.interfaces;

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
        void simpleGreeting(String name) {
            System.out.println("Hi " + name);
        }

        void enthusiasticGreeting(String name) {
            System.out.println("Heeeey " + name + " !!!");
        }

        void throwingGreeting(String name) throws InterruptedException {
            Thread.sleep(10L);
            simpleGreeting(name);
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

        BiConsumer<String, Integer> multiGreeter = (name, times) ->
                Stream.from(0).take(times).forEach(x -> greeter.simpleGreeting(name));

        multiGreeter.accept("World", 5);

        verify(greeter, times(5)).simpleGreeting("World");
    }

    @Test
    public void there_are_also_primitive_specializations() {
        IntConsumer intConsumer = number -> System.out.println(number);
        intConsumer.accept(10);

        LongConsumer longConsumer = number -> System.out.println(number);
        longConsumer.accept(10L);

        DoubleConsumer doubleConsumer = number -> System.out.println(number);
        doubleConsumer.accept(10.50);

        ObjIntConsumer<String> objIntConsumer = (name, number) -> System.out.println(name + " " + number);
        objIntConsumer.accept("hello world", 10);

        ObjLongConsumer<String> objLongConsumer = (name, number) -> System.out.println(name + " " + number);
        objLongConsumer.accept("hello world", 10L);

        ObjDoubleConsumer<String> objDoubleConsumer = (name, number) -> System.out.println(name + " " + number);
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