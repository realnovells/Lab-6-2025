package functions.threads;

import functions.Function;

public class Task {

    private Function function;
    private double leftBorder;
    private double rightBorder;
    private double step;
    private int tasksCount;
    private boolean ready = false;


    public synchronized boolean isReady() { return ready; }
    public synchronized void setReady(boolean ready) { this.ready = ready; }

    public Function getFunction() { return function; }
    public void setFunction(Function function) { this.function = function; }

    public double getLeftBorder() { return leftBorder; }
    public void setLeftBorder(double leftBorder) { this.leftBorder = leftBorder; }

    public double getRightBorder() { return rightBorder; }
    public void setRightBorder(double rightBorder) { this.rightBorder = rightBorder; }

    public double getStep() { return step; }
    public void setStep(double step) { this.step = step; }

    public int getTasksCount() { return tasksCount; }
    public void setTasksCount(int tasksCount) { this.tasksCount = tasksCount; }
}
