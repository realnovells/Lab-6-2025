package functions.threads;

import functions.basic.Log;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                double base = 1 + Math.random() * 9;
                double left = Math.random() * 100;
                double right = 100 + Math.random() * 100;
                double step = Math.random();

                semaphore.acquireWrite();

                task.setFunction(new Log(base));
                task.setLeftBorder(left);
                task.setRightBorder(right);
                task.setStep(step);

                System.out.println("Source " + left + " " + right + " " + step);

                semaphore.releaseWrite();

                Thread.sleep(1);
            }
        } catch (InterruptedException ignored) { }
    }
}
