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
        for (int i = 0; i < task.getTasksCount(); i++) {
            if (Thread.currentThread().isInterrupted()) return;

            try {
                semaphore.acquireWrite();

                double base = 1 + Math.random() * 9;
                double left = Math.random() * 100;
                double right = 100 + Math.random() * 100;
                double step = Math.random();
                if (step == 0) step = 0.01;

                task.setFunction(new Log(base));
                task.setLeftBorder(left);
                task.setRightBorder(right);
                task.setStep(step);

                System.out.printf("Source %.4f %.4f %.4f%n", left, right, step);

                semaphore.releaseWrite();

                Thread.sleep(1);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
