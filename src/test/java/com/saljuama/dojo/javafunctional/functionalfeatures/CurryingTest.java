package com.saljuama.dojo.javafunctional.functionalfeatures;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Function4;
import io.vavr.collection.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class CurryingTest {

    class CurryingDummy {
        Integer hello() { return 1; }
    }

    @Spy
    private final CurryingDummy curryingDummy = new CurryingDummy();

    @Test
    public void currying_a_function_returns_nested_functions_that_only_take_one_parameter_at_a_time() {

        Function4<Integer, Integer, Integer, Integer, String> largeFunction = (a, b, c, d) -> "hello";

        Function1<Integer, Function1<Integer, Function1<Integer, Function1<Integer, String>>>> curriedLargeFunction = largeFunction.curried();

        Function1<Integer, Function1<Integer, Function1<Integer, String>>> firstApplication = curriedLargeFunction.apply(1);

        Function1<Integer, Function1<Integer, String>> secondApplication = firstApplication.apply(2);

        Function1<Integer, String> thirdApplication = secondApplication.apply(3);

        String fourthAndLastApplication = thirdApplication.apply(4);
    }

    @Test
    public void currying_can_be_used_to_reduce_boilerplate() {

        Function2<String, Integer, String> stringRepeater = (string, number) ->
                Stream.from(0).take(number).map(x -> string).mkString();

        String hello2 = stringRepeater.apply("hello", 2);
        String hello3 = stringRepeater.apply("hello", 3);
        String hello4 = stringRepeater.apply("hello", 4);
        String hello5 = stringRepeater.apply("hello", 5);

        // CAN BE REFACTORED INTO:

        Function1<Integer, String> helloRepeater = stringRepeater.curried().apply("hello");
        String curriedHello2 = helloRepeater.apply(2);
        String curriedHello3 = helloRepeater.apply(3);
        String curriedHello4 = helloRepeater.apply(4);
        String curriedHello5 = helloRepeater.apply(5);

        assertEquals(hello2, curriedHello2);
        assertEquals(hello3, curriedHello3);
        assertEquals(hello4, curriedHello4);
        assertEquals(hello5, curriedHello5);
    }

    @Test
    public void curried_functions_are_not_evaluated_until_all_parameters_are_passed_in() {

        Function3<Integer, Integer, Integer, Integer> f = (a,b,c) -> curryingDummy.hello();

        Function1<Integer, Function1<Integer, Function1<Integer, Integer>>> fCurried = f.curried();
        verify(curryingDummy, never()).hello();

        Function1<Integer, Function1<Integer, Integer>> f1 = fCurried.apply(1);
        verify(curryingDummy, never()).hello();

        Function1<Integer, Integer> f2 = f1.apply(2);
        verify(curryingDummy, never()).hello();

        f2.apply(3);
        verify(curryingDummy).hello();
    }
}
