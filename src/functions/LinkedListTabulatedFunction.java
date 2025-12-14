package functions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable, Cloneable {

    private static class FunctionNode {
        private FunctionPoint point;
        private FunctionNode next;
        private FunctionNode prev;

        public FunctionNode(FunctionPoint point) {
            this.point = point;
        }
    }

    private FunctionNode head;
    private int size;

    public LinkedListTabulatedFunction() {
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        this();
        if (leftX >= rightX) throw new IllegalArgumentException("Левая граница должна быть < правой");
        if (pointsCount < 2) throw new IllegalArgumentException("Количество точек ≥ 2");

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            addNodeToTail(new FunctionPoint(leftX + i * step, 0));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        this();
        if (leftX >= rightX) throw new IllegalArgumentException("Левая граница должна быть < правой");
        if (values.length < 2) throw new IllegalArgumentException("Количество точек ≥ 2");

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            addNodeToTail(new FunctionPoint(leftX + i * step, values[i]));
        }
    }

    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        this();
        if (points == null) throw new IllegalArgumentException("Массив null");
        if (points.length < 2) throw new IllegalArgumentException("Слишком мало точек");

        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].getX() >= points[i + 1].getX())
                throw new IllegalArgumentException("Точки должны быть строго возрастающими по X");
        }

        for (FunctionPoint p : points) {
            addNodeToTail(new FunctionPoint(p));
        }
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне границ");

        FunctionNode current;
        if (index < size / 2) {
            current = head.next;
            for (int i = 0; i < index; i++) current = current.next;
        } else {
            current = head.prev;
            for (int i = size - 1; i > index; i--) current = current.prev;
        }
        return current;
    }

    private FunctionNode addNodeToTail(FunctionPoint point) {
        FunctionNode node = new FunctionNode(point);
        node.prev = head.prev;
        node.next = head;
        head.prev.next = node;
        head.prev = node;
        size++;
        return node;
    }

    private FunctionNode addNodeByIndex(int index, FunctionPoint point) {
        if (index < 0 || index > size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне границ");

        FunctionNode nextNode = (index == size) ? head : getNodeByIndex(index);
        FunctionNode prevNode = nextNode.prev;
        FunctionNode node = new FunctionNode(point);
        node.next = nextNode;
        node.prev = prevNode;
        prevNode.next = node;
        nextNode.prev = node;
        size++;
        return node;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (size < 3) throw new IllegalStateException("Ошибка: меньше 3 точек");
        FunctionNode node = getNodeByIndex(index);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
        return node;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        FunctionNode current = head.next;
        while (current != head) {
            sb.append("(").append(current.point.getX()).append("; ").append(current.point.getY()).append(")");
            if (current.next != head) sb.append(", ");
            current = current.next;
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false;
        TabulatedFunction other = (TabulatedFunction) o;
        if (this.size != other.getPointsCount()) return false;

        if (other instanceof LinkedListTabulatedFunction ll) {
            FunctionNode thisNode = this.head.next;
            FunctionNode otherNode = ll.head.next;

            while (thisNode != head) {
                if (!thisNode.point.equals(otherNode.point))
                    return false;
                thisNode = thisNode.next;
                otherNode = otherNode.next;
            }
            return true;
        }
        FunctionNode node = head.next;
        for (int i = 0; i < size; i++) {
            if (!node.point.equals(other.getPoint(i)))
                return false;
            node = node.next;
        }
        return true;
    }


    @Override
    public int hashCode() {
        int hash = size;
        FunctionNode node = head.next;

        while (node != head) {
            hash = 31 * hash + node.point.hashCode();
            node = node.next;
        }

        return hash;
    }


    @Override
    public TabulatedFunction clone() {
        LinkedListTabulatedFunction copy = new LinkedListTabulatedFunction();
        FunctionNode current = this.head.next;
        while (current != this.head) {
            copy.addNodeToTail(new FunctionPoint(current.point));
            current = current.next;
        }
        return copy;
    }

    @Override
    public int getPointsCount() { return size; }

    @Override
    public FunctionPoint getPoint(int index) { return new FunctionPoint(getNodeByIndex(index).point); }

    @Override
    public double getPointX(int index) { return getNodeByIndex(index).point.getX(); }

    @Override
    public double getPointY(int index) { return getNodeByIndex(index).point.getY(); }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        FunctionNode prevNode = node.prev;
        FunctionNode nextNode = node.next;

        if (index > 0 && x <= prevNode.point.getX())
            throw new InappropriateFunctionPointException("Нарушение порядка X");
        if (index < size - 1 && x >= nextNode.point.getX())
            throw new InappropriateFunctionPointException("Нарушение порядка X");

        node.point.setX(x);
    }

    @Override
    public void setPointY(int index, double y) { getNodeByIndex(index).point.setY(y); }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        FunctionNode prevNode = node.prev;
        FunctionNode nextNode = node.next;
        double x = point.getX();
        if ((index > 0 && x <= prevNode.point.getX()) || (index < size - 1 && x >= nextNode.point.getX()))
            throw new InappropriateFunctionPointException("Нарушение порядка X");
        node.point.setX(x);
        node.point.setY(point.getY());
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double x = point.getX();
        FunctionNode current = head.next;
        int index = 0;
        while (current != head && current.point.getX() < x) {
            current = current.next;
            index++;
        }
        if (current != head && Math.abs(current.point.getX() - x) < 1e-10)
            throw new InappropriateFunctionPointException("Точка с таким X уже существует");
        addNodeByIndex(index, new FunctionPoint(point));
    }

    @Override
    public void deletePoint(int index) { deleteNodeByIndex(index); }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) return Double.NaN;

        FunctionNode current = head.next;
        if (Math.abs(x - current.point.getX()) < 1e-12) return current.point.getY();

        while (current.next != head) {
            FunctionNode next = current.next;
            double x1 = current.point.getX();
            double y1 = current.point.getY();
            double x2 = next.point.getX();
            double y2 = next.point.getY();

            if (x >= x1 && x <= x2) return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            current = next;
        }
        return Double.NaN;
    }

    @Override
    public double getLeftDomainBorder() { return getPointX(0); }

    @Override
    public double getRightDomainBorder() { return getPointX(size - 1); }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(size);
        FunctionNode current = head.next;
        while (current != head) {
            out.writeDouble(current.point.getX());
            out.writeDouble(current.point.getY());
            current = current.next;
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        int count = in.readInt();
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
        for (int i = 0; i < count; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            addNodeToTail(new FunctionPoint(x, y));
        }
    }
}
