package functions;

import functions.meta.*;

public final class Functions {
    private Functions() { }

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    public static double integrate(Function f, double leftX, double rightX, double step) {
        if (leftX < f.getLeftDomainBorder() || rightX > f.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интеграл выходит за область определения функции");
        }
        if (step <= 0) throw new IllegalArgumentException("Шаг должен быть > 0");

        double sum = 0;
        double x = leftX;

        while (x < rightX) {
            double nextX = Math.min(x + step, rightX);
            sum += (f.getFunctionValue(x) + f.getFunctionValue(nextX)) / 2 * (nextX - x);
            x = nextX;
        }

        return sum;
    }

}
