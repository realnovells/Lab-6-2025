package functions;

public class FunctionPoint implements Cloneable {
    private double x;
    private double y;

    public static final double EPS = 1e-10;

    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPoint() {
        this(0, 0);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionPoint)) return false;

        FunctionPoint p = (FunctionPoint) o;

        return Math.abs(this.x - p.x) < EPS &&
                Math.abs(this.y - p.y) < EPS;
    }

    @Override
    public int hashCode() {
        long bitsX = Double.doubleToLongBits(x);
        long bitsY = Double.doubleToLongBits(y);
        int hashX = (int)(bitsX ^ (bitsX >>> 32));
        int hashY = (int)(bitsY ^ (bitsY >>> 32));
        return hashX ^ hashY;
    }

    @Override
    public Object clone() {
        return new FunctionPoint(this);
    }
}
