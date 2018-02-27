package spliterators.exercise.example1;

import java.util.Spliterator;
import java.util.function.IntConsumer;

public class UnfairRectangleSpliterator implements Spliterator.OfInt {

    private int xStart;
    private int xEnd;
    private int yStart;
    private int yEnd;
    private int[][] data;

    public UnfairRectangleSpliterator(int[][] data) {
        this(data, 0, data[0].length, 0, data.length);
    }

    private UnfairRectangleSpliterator(int[][] data, int xStart, int xEnd, int yStart, int yEnd) {
        this.data = data;
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yStart = yStart;
        this.yEnd = yEnd;
    }

    @Override
    public OfInt trySplit() {
        if (estimateSize() > data[0].length) {
            long size = estimateSize();
            int remaining = (int) (size / 2);

            int xMid = xStart;
            int yMid = yStart;
            for (int i = 0; i <= remaining; i++) {
                xMid++;
                if (xMid == data[0].length) {
                    xMid = 0;
                    yMid++;
                }
            }
            xStart = xMid;
            yStart = yMid;
            return new UnfairRectangleSpliterator(data, 0, data[0].length, 0, yMid);
        } else {
            return null;
        }
    }

    @Override
    public long estimateSize() {
        return (xEnd - xStart) + (data[0].length * (yEnd - yStart - 1));
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
        while (yStart != yEnd) {
            action.accept(data[yStart][xStart++]);
            resetStart();
        }
    }

    @Override
    public int characteristics() {
        return ORDERED | NONNULL | SIZED | IMMUTABLE | SUBSIZED;
    }

    private void resetStart() {
        if (xStart == data[0].length) {
            xStart = 0;
            yStart++;
        }
    }
}
