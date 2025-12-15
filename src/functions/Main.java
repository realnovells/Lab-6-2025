package functions;

import functions.basic.Exp;
import functions.basic.Log;
import functions.threads.*;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        System.out.println("Проверка интегрирования экспоненты на [0,1]");
        Function f = new Exp();
        double left = 0;
        double right = 1;
        double exact = Math.exp(1) - 1;
        double step = 0.1;
        double integral;

        do {
            integral = Functions.integrate(f, left, right, step);
            step /= 2;
        } while (Math.abs(integral - exact) > 1e-7);

        System.out.printf("Интеграл e^x на [0,1] = %.10f%n", integral);
        System.out.printf("шаг дискретизации = %.10f%n", step * 2);

        System.out.println("\nNon-threads");
        nonThread();

        System.out.println("\nSimple threads");
        simpleThreads();

        System.out.println("\nComplicated threads");
        complicatedThreads();
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

            double integral = Functions.integrate(task.getFunction(), left, right, step);

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
