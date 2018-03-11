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

    private int[][] data;
    private int from;
    private int to;

    public UnfairRectangleSpliterator(int[][] data) {
        this(data, 0, data.length);
    }

    private UnfairRectangleSpliterator(int[][] data, int from, int to) {
        super(to-from,Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL | Spliterator.IMMUTABLE);
        this.data = data;
        this.from = from;
        this.to = to;
    }

    @Override
    public OfInt trySplit() {
        return new UnfairRectangleSpliterator(data, from, to = from + (int)estimateSize() / 2);
    }

    @Override
    public long estimateSize() {
        return from-to;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if(from == to) return false;
        Arrays.stream(data[from++]).forEach(action);
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        while(from != to) {
            Arrays.stream(data[from++]).forEach(action);
        }
    }
}