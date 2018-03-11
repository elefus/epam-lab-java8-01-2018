package spliterators.exercise.example1;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class UnfairRectangleSpliterator extends Spliterators.AbstractIntSpliterator {
    private int start;
    private int end;
    private int[][] source;

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
    UnfairRectangleSpliterator(int[][] source) {
        this(source, 0, source.length);
    }

    private UnfairRectangleSpliterator(int[][] source, int start, int end) {
        super(end - start,
                + Spliterator.IMMUTABLE
                        | Spliterator.NONNULL
                        | Spliterator.ORDERED
                        | Spliterator.SIZED);
        this.source = source;
        this.start = start;
        this.end = end;
    }

    @Override
    public OfInt trySplit() {
        return new UnfairRectangleSpliterator(source, start, start += (end - start) / 2);
    }

    @Override
    public long estimateSize() {
        return end - start;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (end == start)
            return false;
        Arrays.stream(source[start++]).forEach(action);
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        while (start != end) {
            Arrays.stream(source[start++]).forEach(action);
        }
    }
}