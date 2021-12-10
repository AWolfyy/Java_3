package ru.gb.hw.j3_l6.task_2And3;

public final class HomeworkSix {
    public static int[] taskTwo(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Пустой массив!");
        }

        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 4) {
                index = i + 1;
            }
        }

        if (index == -1) {
            throw new RuntimeException("В массиве нет четверок.");
        }

        int[] copyArray = new int[array.length - index];
        System.arraycopy(array, index, copyArray, 0, copyArray.length);

        return copyArray;
    }

    public static boolean taskThree(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Пустой массив!");
        }

        boolean containsOne = false;
        boolean containsFour = false;
        boolean containsOther = false;

        for (int value :
                array) {
            if (value == 1 || value == 4) {
                if (value == 1) {
                    containsOne = true;
                }

                if (value == 4) {
                    containsFour = true;
                }
            } else {
                containsOther = true;
            }
        }
        return (containsOne && containsFour) && !containsOther;
    }
}
