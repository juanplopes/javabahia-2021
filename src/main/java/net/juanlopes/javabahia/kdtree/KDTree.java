package net.juanlopes.javabahia.kdtree;

import java.util.Arrays;

public class KDTree {
    private final QuickSelect.Accessor[] accessors = {
            new PointAccessor(0), new PointAccessor(1)
    };

    private double[] points = new double[32];
    private int[] data = new int[16];
    private int updatedSize = 0;
    private int size = 0;
    private double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY;
    private double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;


    public void clear() {
        this.size = this.updatedSize = 0;
        this.minY = this.minX = Double.POSITIVE_INFINITY;
        this.maxY = this.maxX = Double.NEGATIVE_INFINITY;
    }

    public void add(double x, double y, int data) {
        int index = this.size++;
        if (index >= this.data.length) {
            this.points = Arrays.copyOf(this.points, this.points.length * 2);
            this.data = Arrays.copyOf(this.data, this.data.length * 2);
        }
        this.points[index << 1] = x;
        this.points[(index << 1) + 1] = y;
        this.data[index] = data;
        this.minX = Math.min(this.minX, x);
        this.maxX = Math.max(this.maxX, x);
        this.minY = Math.min(this.minY, y);
        this.maxY = Math.max(this.maxY, y);
    }

    public void update() {
        if (size == updatedSize)
            return;
        updateRecursive(0, 0, size);
        this.updatedSize = size;
    }

    private void updateRecursive(int accessor, int begin, int end) {
        if (end - begin <= 1)
            return;
        int mid = begin + ((end - begin) >> 1);
        QuickSelect.select(accessors[accessor], begin, end, mid);
        updateRecursive(1 - accessor, begin, mid);
        updateRecursive(1 - accessor, mid + 1, end);
    }

    public Query newQuery() {
        return new Query();
    }

    public class Query {
        private final DistanceHeap heap = new DistanceHeap();

        public void clear() {
            this.heap.clear(1, Double.POSITIVE_INFINITY);
        }

        public void execute(double x, double y, int kNearest, double maxDistance) {
            this.heap.clear(Math.min(kNearest, size), maxDistance * maxDistance);

            KDTree.this.update();
            executeRecursive(x, y, minX, maxX, minY, maxY, true, 0, size);
        }

        private void executeRecursive(double x, double y, double minX, double maxX, double minY, double maxY,
                                      boolean partitionByX, int begin, int end) {
            if (begin >= end || minDistToRect(x, y, minX, maxX, minY, maxY) > heap.maxDistance()) {
                return;
            }

            int mid = begin + ((end - begin) >> 1);
            double midX = points[mid << 1];
            double midY = points[(mid << 1) + 1];
            if (partitionByX) {
                if (x < midX) {
                    executeRecursive(x, y, minX, midX, minY, maxY, false, begin, mid);
                    heap.add(dist(x, y, midX, midY), mid);
                    executeRecursive(x, y, midX, maxX, minY, maxY, false, mid + 1, end);
                } else {
                    executeRecursive(x, y, midX, maxX, minY, maxY, false, mid + 1, end);
                    heap.add(dist(x, y, midX, midY), mid);
                    executeRecursive(x, y, minX, midX, minY, maxY, false, begin, mid);
                }
            } else {
                if (y < midY) {
                    executeRecursive(x, y, minX, maxX, minY, midY, true, begin, mid);
                    heap.add(dist(x, y, midX, midY), mid);
                    executeRecursive(x, y, minX, maxX, midY, maxY, true, mid + 1, end);
                } else {
                    executeRecursive(x, y, minX, maxX, midY, maxY, true, mid + 1, end);
                    heap.add(dist(x, y, midX, midY), mid);
                    executeRecursive(x, y, minX, maxX, minY, midY, true, begin, mid);
                }
            }
        }

        public int size() {
            return heap.size();
        }

        public int get(int index) {
            return data[heap.keyAt(index)];
        }
    }

    private static double minDistToRect(double px, double py, double minX, double maxX, double minY, double maxY) {
        return dist(
                clamp(px, minX, maxX),
                clamp(py, minY, maxY),
                px, py);
    }

    private static double clamp(double value, double min, double max) {
        //Math.min/max is kinda slow with doubles
        //noinspection ManualMinMaxCalculation
        return value < min ? min :
                value > max ? max :
                        value;
    }

    private static double dist(double x1, double y1, double x2, double y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    private class PointAccessor implements QuickSelect.Accessor {
        private final int offset;

        public PointAccessor(int offset) {
            this.offset = offset;
        }

        @Override
        public double get(int i) {
            return points[(i << 1) + offset];
        }

        @Override
        public void swap(int i, int j) {
            int tmpObj = data[i];
            data[i] = data[j];
            data[j] = tmpObj;

            i <<= 1; //2*i
            j <<= 1;
            double tmp = points[i];
            points[i] = points[j];
            points[j] = tmp;

            i++; //2*i+1
            j++;

            tmp = points[i];
            points[i] = points[j];
            points[j] = tmp;
        }
    }

}
