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
     *
     * 0 1 2 3 4
     * 2 3 / 4 5 6
     * 2 4 5 6 7
     */

    private int startLineInclusive;
    private int endLineExclusive;
    private int[][] data;

    public UnfairRectangleSpliterator(int[][] data) {
        this(data, 0, data.length);
    }

    private UnfairRectangleSpliterator(int[][] data, int startLineInclusive, int endLineExclusive) {
        super(endLineExclusive - startLineInclusive,
 Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SIZED);
        this.data = data;
        this.startLineInclusive = startLineInclusive;
        this.endLineExclusive = endLineExclusive;
    }

    @Override
    public OfInt trySplit() {
        int mid = startLineInclusive + ((endLineExclusive-startLineInclusive) / 2);
        return new UnfairRectangleSpliterator(data, startLineInclusive, startLineInclusive = mid);
    }

    @Override
    public long estimateSize() {
        return  endLineExclusive-startLineInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startLineInclusive == endLineExclusive ) {
            return false;
        }
        Arrays.stream(data[startLineInclusive++]).forEach(action);
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        while (startLineInclusive != endLineExclusive) {
            Arrays.stream(data[startLineInclusive++]).forEach(action);
        }
    }
}