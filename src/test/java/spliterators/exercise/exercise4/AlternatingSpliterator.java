package spliterators.exercise.exercise4;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class AlternatingSpliterator<T> extends Spliterators.AbstractSpliterator<T> {

    private Spliterator<T> first;
    private Spliterator<T> second;

    private AlternatingSpliterator(long size,
                                   int characteristics,
                                   Spliterator<T> first,
                                   Spliterator<T> second) {
        super(size, characteristics);
        this.first = first;
        this.second = second;
    }

    public static <T> AlternatingSpliterator<T> combine(Stream<T> firstStream,
                                                        Stream<T> secondStream) {

        Spliterator<T> first = firstStream.spliterator();
        Spliterator<T> second = secondStream.spliterator();

        return new AlternatingSpliterator<>(
                first.estimateSize() + second.estimateSize(),
                first.characteristics() | second.characteristics(),
                first, second);
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return false;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        while (first.estimateSize() > 0 && second.estimateSize() > 0) {
            first.tryAdvance(action);
            second.tryAdvance(action);
        }

        while (first.estimateSize() > 0)
            first.tryAdvance(action);

        while (second.estimateSize() > 0)
            second.tryAdvance(action);
    }

    @Override
    public long getExactSizeIfKnown() {
        return first.estimateSize() + second.estimateSize();
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return characteristics == (ORDERED | NONNULL | SIZED | IMMUTABLE );
    }
}
