package functions.threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {

    private Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            synchronized (task) {
                while (!task.isReady()) {
                    try { task.wait(); } catch (InterruptedException ignored) {}
                }

                double left = task.getLeftBorder();
                double right = task.getRightBorder();
                double step = task.getStep();
                double result = Functions.integrate(task.getFunction(), left, right, step);

                System.out.printf("Result %.5f %.5f %.5f %.10f%n", left, right, step, result);

                task.setReady(false);
            }
        }
    }
}
