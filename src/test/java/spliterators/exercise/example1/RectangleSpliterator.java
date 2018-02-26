package spliterators.exercise.example1;

import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    public RectangleSpliterator(int[][] data) {
        this();
    }

    private RectangleSpliterator() {
        super(0, 0);
        throw new UnsupportedOperationException();
    }

    @Override
    public OfInt trySplit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        throw new UnsupportedOperationException();
    }
}