package net.juanlopes.javabahia;

import java.util.List;

public interface KNearest<T> {
    void add(double x, double y, T data);

    List<T> query(double x, double y, int limit);
}
