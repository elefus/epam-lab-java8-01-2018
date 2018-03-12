package spliterators.exercise.example1;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class UnfairRectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    /**
     * 0 1 2 3 4
     * ---------
     * 2 3 4 5 6
     * 2 4 5 6 7
     * <p>
     * 0 1 2 3 4
     * 2 3 / 4 5 6
     * 2 4 5 6 7
     */

    private int[][] data;
    private int startInclusive;
    private int endExclusive;

    public UnfairRectangleSpliterator(int[][] data, int startInclusive, int endExclusive) {
        super((endExclusive - startInclusive), Spliterator.IMMUTABLE | Spliterator.SIZED | Spliterator.NONNULL | Spliterator.ORDERED);
        this.data = data;
        this.endExclusive = endExclusive;
        this.startInclusive = startInclusive;
    }

    public UnfairRectangleSpliterator(int[][] data) {
        this(data, 0, data.length);
    }

    @Override
    public OfInt trySplit() {
        int mid = startInclusive + (int) (estimateSize() / 2);
        return new UnfairRectangleSpliterator(data, startInclusive, startInclusive = mid);
    }

    @Override
    public long estimateSize() {
        return (endExclusive - startInclusive);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInclusive == endExclusive) return false;
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