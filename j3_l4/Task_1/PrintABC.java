package j3_l4.Task_1;

public class PrintABC {
    private volatile char currentChar = 'A';

    synchronized void printA() {
        for (int i = 0; i < 5; i++) {
            while (currentChar != 'A') {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.print(currentChar);
            currentChar = 'B';
            notifyAll();
        }
    }

    synchronized void printB() {
        for (int i = 0; i < 5; i++) {
            while (currentChar != 'B') {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.print(currentChar);
            currentChar = 'C';
            notifyAll();
        }
    }

    synchronized void printC() {
        for (int i = 0; i < 5; i++) {
            while (currentChar != 'C') {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.print(currentChar);
            currentChar = 'A';
            notifyAll();
        }
    }
}
