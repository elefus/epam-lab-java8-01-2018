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
        int remaining = (int) (estimateSize() / 2);

        int xMid = xStart;
        int yMid = yStart;
        for (int i = 0; i <= remaining; i++) {
            xMid++;
            if (xMid == data[0].length) {
                xMid = 0;
                yMid++;
            }
        }
        return new UnfairRectangleSpliterator(data, xStart = xMid, data[0].length, yStart = yMid, data.length );
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
