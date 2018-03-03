package spliterators.exercise.example1;

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
    private int startXInclusive;
    private int endXExclusive;
    private int startYInclusive;
    private int endYExclusive;
    private int[][] data;

    public UnfairRectangleSpliterator(int[][] data) {
        this(data, 0, 0, data.length, data[0].length);
    }

    private UnfairRectangleSpliterator(
            int[][] data,
            int startXInclusive,
            int startYInclusive,
            int endXExclusive,
            int endYExclusive) {
        super((endXExclusive - startXInclusive)*(endYExclusive - startYInclusive),
                Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SIZED);
        this.data = data;
        this.startXInclusive = startXInclusive;
        this.startYInclusive = startYInclusive;
        this.endXExclusive = endXExclusive-1;
        this.endYExclusive = endYExclusive-1;
    }

    @Override
    public OfInt trySplit() {
        if ((endXExclusive - startXInclusive) > 0) {
            int rightSpliteratorStart = (this.endXExclusive - this.startXInclusive) / 2 + this.startXInclusive;
            if (rightSpliteratorStart >= this.endXExclusive-1){
                return null;
            }
            int endForNewSpliterator = this.endXExclusive;
            this.endXExclusive = rightSpliteratorStart - 1;
            return new UnfairRectangleSpliterator(
                    data,
                    rightSpliteratorStart,
                    this.startYInclusive,
                    endForNewSpliterator + 1,
                    this.endYExclusive + 1
            );
        }
        return null;
    }

    @Override
    public long estimateSize() {
        return (endXExclusive - startXInclusive)*(endYExclusive - startYInclusive);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (this.startXInclusive == this.endXExclusive && this.startYInclusive == this.endYExclusive) {
            return false;
        }
        if (this.startYInclusive == this.endYExclusive){
            action.accept(data[this.startXInclusive++][this.startYInclusive = 0]);
        }
        action.accept(data[this.startXInclusive][this.startYInclusive++]);
        return true;
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        while (this.startXInclusive < this.endXExclusive || this.startYInclusive < this.endYExclusive) {
            if (this.startYInclusive == this.endYExclusive){
                action.accept(data[this.startXInclusive++][this.startYInclusive = 0]);
            }else {
                action.accept(data[this.startXInclusive][this.startYInclusive++]);
            }
        }
        if (this.startXInclusive == this.endXExclusive && this.startYInclusive == this.endYExclusive){
            action.accept(data[this.startXInclusive++][this.startYInclusive++]);
        }
    }
}