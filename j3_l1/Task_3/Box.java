package j3_l1.Task_3;

import java.util.ArrayList;

public class Box<T extends Fruit> implements Comparable<Box<? extends Fruit>> {

    private final ArrayList<T> fruits;

    public Box() {
        this.fruits = new ArrayList<>();
    }

    // d
    private float getWeight() {
        if (fruits.isEmpty()) {
            return 0;
        }

        return fruits.get(0).getWeight() * fruits.size();
    }

    // e
    public boolean compare(Box<? extends Fruit> box) {
        return this.getWeight() == box.getWeight();
    }

    @Override
    public int compareTo(Box<? extends Fruit> box) {
        return Float.compare(this.getWeight(), box.getWeight());
    }

    // f
    public void fromBoxToBox(Box<T> box) {
        box.fruits.addAll(fruits);
        fruits.clear();
    }

    // g
    public void add(T fruit) {
        fruits.add(fruit);
    }

}
