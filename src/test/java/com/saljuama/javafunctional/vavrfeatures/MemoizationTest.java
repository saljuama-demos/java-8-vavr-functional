package com.saljuama.javafunctional.vavrfeatures;

import io.vavr.Function0;
import io.vavr.collection.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MemoizationTest {

    class DummyHelper {
        Integer dummyInt() {
            return 10;
        }
    }

    @Spy
    private final DummyHelper dummyHelper = new DummyHelper();

    @Test
    public void memoized_functions_are_executed_once_and_results_are_cached() {

        Function0<Integer> dummyFunction = () -> dummyHelper.dummyInt();
        Function0<Integer> memoizedDummyFunction = dummyFunction.memoized();

        Stream.from(0).take(10).forEach(x -> memoizedDummyFunction.apply());

        verify(dummyHelper, times(1)).dummyInt();
    }
}
