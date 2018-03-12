package spliterators.exercise.exercise4;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class AlternatingSpliterator<T> extends Spliterators.AbstractSpliterator<T> {

    private final Spliterator<T> first;
    private final Spliterator<T> second;

    private AlternatingSpliterator(long estimatedSize, int characteristics, Spliterator<T> first, Spliterator<T> second) {
        super(estimatedSize, check(characteristics, first, second));
        this.first = first;
        this.second = second;
    }

    private static <T> int check(int characteristics, Spliterator<T> first, Spliterator<T> second) {
        int characteristicsNeeded = Spliterator.SIZED | Spliterator.IMMUTABLE  | Spliterator.SUBSIZED | Spliterator.ORDERED;
        if(first.hasCharacteristics(characteristicsNeeded) && second.hasCharacteristics(characteristicsNeeded)) return characteristics;
        throw new IllegalArgumentException("Illegal characteristics");
    }

    public static <T> AlternatingSpliterator<T> combine(Stream<T> firstStream, Stream<T> secondStream) {
        Spliterator<T> first = firstStream.spliterator();
        Spliterator<T> second = secondStream.spliterator();
        return new AlternatingSpliterator(first.getExactSizeIfKnown() + second.getExactSizeIfKnown(),
                first.characteristics() | second.characteristics(),
                first, second);
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return false;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        while(first.getExactSizeIfKnown() != 0 && second.getExactSizeIfKnown() != 0) {
            first.tryAdvance(action);
            second.tryAdvance(action);
        }
        while(first.getExactSizeIfKnown() != 0) {
            first.tryAdvance(action);
        }
        while(second.getExactSizeIfKnown() != 0) {
            second.tryAdvance(action);
        }
    }

    @Override
    public long getExactSizeIfKnown() {
        return first.getExactSizeIfKnown() + second.getExactSizeIfKnown();
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        if(first.hasCharacteristics(characteristics) && second.hasCharacteristics(characteristics)) return true;
        return false;
    }
}
