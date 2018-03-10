package spliterators.exercise.example1;

import java.util.Spliterator;
import java.util.Spliterators;
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

    private final int[][] array;
    private final MatrixElement startElement;
    private final MatrixElement finalElement;

    private static class MatrixElement {
        final int[][] array;
        int x;
        int y;

        MatrixElement(int[][] array, int x, int y) {
            this.array = array;
            this.x = x;
            this.y = y;
        }

        private void getNext() {
            this.y++;
            if (this.x < array.length && this.y >= array[0].length) {
                this.y = 0;
                this.x++;
            }
        }
    }

    public FairRectangleSpliterator(int[][] array) {
        this(
                array,
                new MatrixElement(array, 0, 0),
                new MatrixElement(array, array.length - 1, array[0].length - 1)
        );
    }

    private FairRectangleSpliterator(int[][] array, MatrixElement startElement, MatrixElement finalElement) {
        super(estimateSize(array, startElement, finalElement),
                Spliterator.IMMUTABLE | Spliterator.SIZED | Spliterator.NONNULL | Spliterator.ORDERED
        );
        this.array = array;
        this.startElement = startElement;
        this.finalElement = finalElement;
    }

    @Override
    public OfInt trySplit() {
        long rightSpliteratorStart = estimateSize() / 2;
        if (rightSpliteratorStart <= 0){
            return null;
        }
        MatrixElement element = new MatrixElement(array, startElement.x, startElement.y);
        for (int i = 0; i < rightSpliteratorStart - 1; i++) {
            element.getNext();
        }
        MatrixElement finalElementSec = new MatrixElement(array, this.finalElement.x, this.finalElement.y);
        this.finalElement.x = element.x-1;
        this.finalElement.y = element.y-1;
        return new FairRectangleSpliterator(array, new MatrixElement(array, element.x, element.y),
                new MatrixElement(array, finalElementSec.x, finalElementSec.y));
    }

    @Override
    public long estimateSize() {
        return estimateSize(this.array, this.startElement, this.finalElement);
    }

    private static long estimateSize(int[][] array, MatrixElement startElement, MatrixElement finalElement) {
        return (finalElement.x - startElement.x) * array[0].length + finalElement.y;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startElement.x < finalElement.x) {
            return false;
        }
        action.accept(array[startElement.x][startElement.x]);
        startElement.getNext();
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        while (startElement.x <= finalElement.x) {
            action.accept(array[startElement.x][startElement.y]);
            startElement.getNext();
        }
    }
}
