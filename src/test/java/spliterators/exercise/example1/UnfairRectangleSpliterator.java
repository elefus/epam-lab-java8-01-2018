package spliterators.exercise.example1;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class UnfairRectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] data;
    private int start, end;

    /**
     * 0 1 2 3 4
     * ---------
     * 2 3 4 5 6
     * 2 4 5 6 7
     *
     * 0 1 2 3 4
     * 2 3 / 4 5 6
     * 2 4 5 6 7
     */
    private UnfairRectangleSpliterator(int[][] data, int start, int end) {
        super(end - start,
            Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.NONNULL);
        this.data = data;
        this.start = start;
        this.end = end;
    }

    UnfairRectangleSpliterator(int[][] data) {
        this(data, 0, data.length);
    }

    @Override
    public OfInt trySplit() {
        final int s = start;
        start += (end - start) / 2;
        return new UnfairRectangleSpliterator(data, s, start);
    }

    @Override
    public long estimateSize() {
        return end - start;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if(start == end) return false;
        Arrays.stream(data[start++]).forEach(action);
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        while (start != end) {
            Arrays.stream(data[start++]).forEach(action);
        }
    }
}