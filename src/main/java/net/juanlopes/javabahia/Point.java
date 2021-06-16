package net.juanlopes.javabahia;

public class Point<T> {
    private final double x;
    private final double y;
    private final T data;

    public Point(double x, double y, T data) {
        this.x = x;
        this.y = y;
        this.data = data;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public T getData() {
        return data;
    }
}
