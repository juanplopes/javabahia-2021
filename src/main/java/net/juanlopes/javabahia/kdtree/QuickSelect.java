package net.juanlopes.javabahia.kdtree;

public class QuickSelect {
    public static void select(Accessor accessor, int begin, int end, int mid) {
        while (end - begin > 1) {
            int pivot = partition(accessor, begin, end);
            if (mid <= pivot) {
                end = pivot;
            } else {
                begin = pivot + 1;
            }
        }
    }

    private static int partition(Accessor accessor, int begin, int end) {
        accessor.swap((begin + end) / 2, end - 1);
        double pivotValue = accessor.get(end - 1);

        int i = begin;
        for (int j = begin; j < end; j++) {
            if (accessor.get(j) < pivotValue)
                accessor.swap(i++, j);
        }
        accessor.swap(i, end - 1);
        return i;
    }

    interface Accessor {
        double get(int i);

        void swap(int i, int j);
    }
}
