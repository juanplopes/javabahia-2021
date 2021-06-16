package net.juanlopes.javabahia.kdtree;

import java.util.Iterator;

public class DistanceHeap implements Iterable<Integer> {
    private double[] distances = new double[4];
    private int[] keys = new int[4];
    private int size = 0;
    private int maxSize;
    private double maxDistance;

    public void clear(int maxSize, double maxDistance) {
        if (maxSize > distances.length) {
            int newSize = Integer.highestOneBit(maxSize - 1) * 2;
            this.distances = new double[newSize];
            this.keys = new int[newSize];
        }
        this.distances[0] = Double.POSITIVE_INFINITY;
        this.maxSize = maxSize;
        this.maxDistance = maxDistance;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public int keyAt(int index) {
        return keys[index];
    }

    public double maxDistance() {
        return maxDistance;
    }

    public void add(double distance, int key) {
        //we optimize this heavily, because this is the hotspot
        if (distance > maxDistance)
            return;

        double[] distances = this.distances;
        int[] keys = this.keys;
        int maxSize = this.maxSize;
        int size = this.size;
        if (size < maxSize) {
            int index = size++;
            distances[index] = distance;
            keys[index] = key;
            if (size == maxSize) {
                //heap will be kept unheapified until it's full
                //then we heapify the whole thing at once
                for (int i = size / 2; i >= 0; i--)
                    bubbleDown(distances, keys, size, i);
                maxDistance = distances[0];
            }
            this.size = size;
        } else {
            //replace maximum if full and this distance is lower
            distances[0] = distance;
            keys[0] = key;
            bubbleDown(distances, keys, size, 0);
            maxDistance = distances[0];
        }
    }

    private void bubbleDown(double[] distances, int[] keys, int size, int index) {
        int key = keys[index];
        double distance = distances[index];
        while (true) {
            int left = (index << 1) + 1;
            int right = (index << 1) + 2;
            double leftDist = left < size ? distances[left] : Double.NEGATIVE_INFINITY;
            double rightDist = right < size ? distances[right] : Double.NEGATIVE_INFINITY;

            if (leftDist > rightDist) {
                if (leftDist > distance) {
                    keys[index] = keys[left];
                    distances[index] = leftDist;
                    index = left;
                } else {
                    keys[index] = key;
                    distances[index] = distance;
                    return;
                }
            } else {
                if (rightDist > distance) {
                    keys[index] = keys[right];
                    distances[index] = rightDist;
                    index = right;
                } else {
                    keys[index] = key;
                    distances[index] = distance;
                    return;
                }
            }

        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Integer next() {
                return keys[index++];
            }
        };
    }

}
