package functions.threads;

import functions.Functions;
import functions.basic.Log;

import java.util.Random;

public class SimpleGenerator implements Runnable {

    private Task task;
    private Random rnd = new Random();

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            double base = 1 + 9 * rnd.nextDouble();
            double left = rnd.nextDouble() * 100;
            double right = 100 + rnd.nextDouble() * 100;
            double step = rnd.nextDouble();

            synchronized (task) {
                task.setFunction(new Log(base));
                task.setLeftBorder(left);
                task.setRightBorder(right);
                task.setStep(step);
                task.setReady(true);
                task.notifyAll();
            }

            System.out.printf("Source %.5f %.5f %.5f%n", left, right, step);

            try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        }
    }
}
