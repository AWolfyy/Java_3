package j3_l5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.CountDownLatch;


public class Car implements Runnable {
    private static boolean winner;
    private static int CARS_COUNTER;
    static {
        CARS_COUNTER = 0;
    }

    private final String name;
    private final int speed;
    private final Race race;
    private final CyclicBarrier cb;
    private final CountDownLatch cdl;

    public Car(int speed, Race race, CyclicBarrier cb, CountDownLatch cdl) {
        CARS_COUNTER++;
        this.name = "Участник №" + CARS_COUNTER;
        this.speed = speed;
        this.race = race;
        this.cb = cb;
        this.cdl = cdl;
    }

    @Override
    public void run() {

        try {
            System.out.println(name + " готовится...");
            Thread.sleep((long) (500 + (Math.random() * 800)));
            System.out.println(name + " готов.");

            try {
                cb.await();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
            if (i == race.getStages().size() - 1 && !winner) {
                winner = true;
                System.out.println(name + " WIN!");
            }
        }

        cdl.countDown();
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }
}
