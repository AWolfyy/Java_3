package ru.gb.hw.j3_l6.task_2And3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

public class HomeworkSixTest {

    @ParameterizedTest
    @MethodSource("taskTwoParametersProvided")
    public void shouldReturnNewArrayWithValuesAfterLastFour(int[] expected, int[] array) {
        Assertions.assertArrayEquals(expected, HomeworkSix.taskTwo(array));
    }

    private static Stream<Arguments> taskTwoParametersProvided() {
        return Stream.of(
                Arguments.of(new int[] {7, 1}, new int[] {1, 3, 7, 4, 7, 2, 9, 4, 7, 1}),
                Arguments.of(new int[] {1}, new int[] {5, 4, 1}),
                Arguments.of(new int[] {8, 9, 1}, new int[] {3, 7, 1, 4, 4, 4, 8, 9, 1})
        );
    }

    @ParameterizedTest
    @MethodSource("taskThreeParametersProvided")
    public void shouldReturnTrueIfArrayHaveFourAndOne(boolean expected, int[] array) {
        Assertions.assertEquals(expected, HomeworkSix.taskThree(array));
    }

    private static Stream<Arguments> taskThreeParametersProvided() {
        return Stream.of(
                Arguments.of(true, new int[] {1, 1, 1, 4, 4, 1, 4, 4}),
                Arguments.of(false, new int[] {1, 1, 1, 1, 1, 1}),
                Arguments.of(false, new int[] {4, 4, 4, 4}),
                Arguments.of(false, new int[] {1, 4, 4, 1, 1, 4, 3})
        );
    }

    @Test
    public void shouldThrowRuntimeExceptionInTaskTwo() {
        Assertions.assertThrows(RuntimeException.class, () -> HomeworkSix.taskTwo(new int[] {2, 33, 7}));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void shouldThrowIllegalArgumentExceptionInTaskTwo(int[] array) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> HomeworkSix.taskTwo(array));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void shouldThrowIllegalArgumentExceptionInTaskThree(int[] array) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> HomeworkSix.taskThree(array));
    }
}
