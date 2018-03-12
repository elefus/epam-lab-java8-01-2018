package spliterators.exercise.example1;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class UnfairRectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private int startInclusive;
    private int endExclusive;

    private int[][] data;
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
    public UnfairRectangleSpliterator(int[][] data) {
        this(data, 0, data.length);
    }

    private UnfairRectangleSpliterator(int[][] data, int startInclusive, int endExclusive) {
        super(data.length, Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SIZED);
        this.data = data;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
    }

    @Override
    public OfInt trySplit() {
        int mid = startInclusive + (int)(estimateSize() / 2);
        return new UnfairRectangleSpliterator(data, startInclusive, startInclusive = mid);
    }

    @Override
    public long estimateSize() {
        return endExclusive - startInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInclusive == endExclusive) {
            return false;
        }
        Arrays.stream(data[startInclusive++]).forEach(action);
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        while (startInclusive != endExclusive) {
            Arrays.stream(data[startInclusive++]).forEach(action);
        }
    }
}