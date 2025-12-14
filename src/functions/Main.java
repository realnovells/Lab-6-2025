package functions;

import functions.basic.Log;
import functions.threads.*;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        System.out.println("Non-thread");
        nonThread();

        System.out.println("\nSimple threads");
        simpleThreads();
    }

    // Метод для вычисления интеграла по методу трапеций
    public static double integrate(Function f, double left, double right, double step) {
        if (left < f.getLeftDomainBorder() || right > f.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интеграл за границами функции");
        }
        double result = 0;
        double x = left;
        while (x < right) {
            double nextX = Math.min(x + step, right);
            double y1 = f.getFunctionValue(x);
            double y2 = f.getFunctionValue(nextX);
            result += (y1 + y2) * (nextX - x) / 2;
            x = nextX;
        }
        return result;
    }

    public static void nonThread() {
        Task task = new Task();
        task.setTasksCount(100);
        Random rand = new Random();

        for (int i = 0; i < task.getTasksCount(); i++) {
            double base = 1 + 9 * rand.nextDouble();
            double left = 100 * rand.nextDouble();
            double right = 100 + 100 * rand.nextDouble();
            double step = rand.nextDouble();
            task.setFunction(new Log(base));
            task.setLeftBorder(left);
            task.setRightBorder(right);
            task.setStep(step);

            System.out.printf("Source %.5f %.5f %.5f%n", left, right, step);

            double integral = integrate(task.getFunction(), left, right, step);

            System.out.printf("Result %.5f %.5f %.5f %.10f%n", left, right, step, integral);
        }
    }

    public static void simpleThreads() {
        Task task = new Task();
        task.setTasksCount(100);

        Thread generator = new Thread(new SimpleGenerator(task));
        Thread integrator = new Thread(new SimpleIntegrator(task));

        generator.start();
        integrator.start();

        try {
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void complicatedThreads() {
        Task task = new Task();
        task.setTasksCount(100);

        Semaphore semaphore = new Semaphore();

        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);

        generator.start();
        integrator.start();

        try {
            Thread.sleep(50);
            generator.interrupt();
            integrator.interrupt();

            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
