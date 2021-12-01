package j3_l4.Task_1;

public class Main {
    public static void main(String[] args) {
        PrintABC printABC = new PrintABC();

        new Thread(printABC::printA).start();

        new Thread(printABC::printB).start();

        new Thread(printABC::printC).start();
    }
}
