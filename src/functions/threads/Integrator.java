package functions.threads;

import functions.Function;
import functions.Functions;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            if (Thread.currentThread().isInterrupted()) return;

            try {
                semaphore.acquireRead();

                Function f = task.getFunction();
                double left = task.getLeftBorder();
                double right = task.getRightBorder();
                double step = task.getStep();

                double result = Functions.integrate(f, left, right, step);

                System.out.println("Result " + left + " " + right + " " + step + " " + result);

            } catch (InterruptedException e) {
                return;
            } finally {
                semaphore.releaseRead();
            }
        }
    }
}
