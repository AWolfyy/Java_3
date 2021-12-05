package j3_l5;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    private final Semaphore smp;

    public Tunnel(Semaphore smp) {
        this.length = 80;
        description = "Тоннель " + length + " метров.";
        this.smp = smp;
    }

    @Override
    public void go(Car car) {
        try {
            System.out.println(car.getName() + " готовиться к этапу(ждет): " + description);
            smp.acquire();
            System.out.println(car.getName() + " начал этап: " + description);
            Thread.sleep(length / car.getSpeed() * 1000L);
            System.out.println(car.getName() + " закончил этап: " + description);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            smp.release();
        }
    }
}
