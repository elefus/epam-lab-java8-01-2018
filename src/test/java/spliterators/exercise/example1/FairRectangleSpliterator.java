package spliterators.exercise.example1;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Spliterators;
import java.util.function.IntConsumer;

public class FairRectangleSpliterator extends Spliterators.AbstractIntSpliterator {
    private final int[][] source;
    private Pointer current;
    private Pointer last;

    private static class Pointer {
        private int[][] source;
        private int column;
        private int row;
        @Getter
        @Setter
        private boolean nextAvailable;

        Pointer(int[][] array, int column, int row) {
            this.source = array;
            this.column = column;
            this.row = row;
            this.setNextAvailable(column < source[0].length && row < source.length);
        }

        void proceedToNext() {
            if (column == source[0].length - 1) {
                column = 0;
                row++;
            } else {
                column++;
            }
            this.setNextAvailable(row != source.length);
        }

        Pointer getMedianPointer(Pointer last) {
            int offset = (last.size(this) + 1) / 2;
            return new Pointer(source,
                    (column + offset - 1) % source[0].length,
                    row + (column + offset - 1) / source[0].length);
        }

        int size(Pointer pointer) {
            return column - pointer.column + source[0].length * (row - pointer.row);
        }
    }

    FairRectangleSpliterator(@NonNull int[][] array) {
        this(array, new Pointer(array, 0, 0), new Pointer(array, array[0].length - 1, array.length - 1));
    }

    private FairRectangleSpliterator(@NonNull int[][] array, Pointer start, Pointer end) {
        super(end.size(start) + 1, IMMUTABLE | NONNULL | SIZED | SUBSIZED);
        this.source = array;
        this.current = new Pointer(start.source, start.column, start.row);
        this.last = new Pointer(end.source, end.column, end.row);
    }

    @Override
    public OfInt trySplit() {
        Pointer median = current.getMedianPointer(last);
        FairRectangleSpliterator newSpliterator = new FairRectangleSpliterator(source, current, median);
        current = new Pointer(median.source, median.column, median.row);
        current.proceedToNext();
        return newSpliterator;
    }

    @Override
    public long estimateSize() {
        return last.size(current) + 1;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (current.isNextAvailable()) {
            action.accept(source[current.row][current.column]);
            current.proceedToNext();
            return true;
        }
        return false;
    }
}