package j3_l1;

import java.util.Arrays;
import java.util.ArrayList;

public class Main {
    private final static String[] stringArray = new String[] {
            "first",
            "second",
            "third",
            "fourth",
            "fifth"
    };

    private final static Integer[] integerArray = new Integer[] {1, 2, 3, 4, 5};

    public static void main(String[] args) {
        doTask2();

        doTask1();
    }

    //Task 1

    public static<T> void changeTwoElementsInArray(T[] array, int firstIndex, int secondIndex) {
        T buffer = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = buffer;
    }

    public static void doTask1() {

        changeTwoElementsInArray(stringArray, 1, 4);
        changeTwoElementsInArray(integerArray, 0, 4);

        System.out.println(Arrays.toString(stringArray));
        System.out.println(Arrays.toString(integerArray));
    }

    //Task 2

    public static<T> ArrayList<T> arrayToArrayList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    public static void doTask2() {
        System.out.println(arrayToArrayList(stringArray));
        System.out.println(arrayToArrayList(integerArray));
    }
}
