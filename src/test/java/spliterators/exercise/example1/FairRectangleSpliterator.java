package spliterators.exercise.example1;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.TimeUnit;
import java.util.function.IntConsumer;

public class FairRectangleSpliterator extends Spliterators.AbstractIntSpliterator {
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
    private int rowStart;
    private int rowEnd;
    private int colStart;
    private int colEnd;

    /**
     * constructor
     *
     * @param data     data array
     * @param rowStart start row inclusive
     * @param rowEnd   end row exclusive
     * @param colStart start column inclusive
     * @param colEnd   end column exclusive
     */
    public FairRectangleSpliterator(int[][] data, int rowStart, int rowEnd, int colStart, int colEnd) {
        super((rowEnd - rowStart - 1) * data[0].length + colEnd - colStart, Spliterator.IMMUTABLE | Spliterator.SIZED | Spliterator.NONNULL | Spliterator.ORDERED);
        this.data = data;
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
        this.colStart = colStart;
        this.colEnd = colEnd;
    }

    @Override
    public String toString() {
        return "FairRectangleSpliterator{" +
                "rowStart=" + rowStart +
                ", rowEnd=" + rowEnd +
                ", colStart=" + colStart +
                ", colEnd=" + colEnd +
                '}';
    }

    public FairRectangleSpliterator(int[][] data) {
        this(data, 0, data.length, 0, data[0].length);
    }

    @Override
    public Spliterator.OfInt trySplit() {
        int count = (int) estimateSize() / 2;
        int newRow = rowStart + count / data[0].length;
        int newColEnd = colStart + count % data[0].length;

        if (newColEnd > data[0].length) {
            newRow++;
            newColEnd = newColEnd - data[0].length;
        }
        if (newColEnd == 0) {
            newColEnd = data[0].length;
        } else {
            newRow++;
        }
        int newColStart = colStart;
        int newRowStart = rowStart;
        int newRowEnd = newRow;
        rowStart = newRow;
        if (newColEnd == data[0].length) {
            colStart = 0;
        } else {
            colStart = newColEnd;
            rowStart--;
        }
        return new FairRectangleSpliterator(data, newRowStart, newRowEnd, newColStart, newColEnd);
    }

    @Override
    public long estimateSize() {
        return (rowEnd - rowStart - 1) * data[0].length + colEnd - colStart;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (rowStart == rowEnd && colStart == colEnd) return false;
        action.accept(data[rowStart][colStart]);
        if (colStart < data[rowStart].length - 1) {
            colStart++;
        } else {
            if (rowStart < rowEnd) {
                colStart = 0;
                rowStart++;
            } else {
                return false;
            }
        }
        System.out.println("+");
        return true;

    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        for (int i = colStart; i < data[rowStart].length; i++) {
            action.accept(data[rowStart][i]);

        }
        for (int i = rowStart + 1; i < rowEnd - 1; i++) {
            for (int j = 0; j < data[i].length; j++) {
                action.accept(data[i][j]);
            }
        }
        for (int i = 0; i < colEnd; i++) {
            action.accept(data[rowEnd - 1][i]);
        }

    }
}
