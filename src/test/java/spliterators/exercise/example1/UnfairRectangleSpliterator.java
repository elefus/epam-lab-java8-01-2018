package spliterators.exercise.example1;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class UnfairRectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private int xStart;
    private int xEnd;
    private int yStart;
    private int yEnd;
    private int[][] data;

    public UnfairRectangleSpliterator(int[][] data) {
        this(data, 0, data[0].length, 0, data.length);
    }

    private UnfairRectangleSpliterator(int[][] data, int xStart, int xEnd, int yStart, int yEnd) {
        super(data.length, Spliterator.IMMUTABLE | Spliterator.NONNULL
                | Spliterator.ORDERED | Spliterator.SIZED);
        this.data = data;
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yStart = yStart;
        this.yEnd = yEnd;
    }

    @Override
    public OfInt trySplit() {
//        int mid = startInclusive + (int) (estimateSize() / 2);
//        return new UnfairRectangleSpliterator(data, startInclusive, startInclusive = mid);
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        return (xEnd - xStart) + (data[0].length * (yEnd - yStart));
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (yStart == yEnd) {
            return false;
        }
        resetStart();

        action.accept(data[yStart][xStart++]);
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        while(yStart != yEnd) {
            action.accept(data[yStart][xStart++]);
            resetStart();
        }
    }

    private void resetStart() {
        if (xStart == data[0].length) {
            xStart = 0;
            yStart++;
        }
    }
}
