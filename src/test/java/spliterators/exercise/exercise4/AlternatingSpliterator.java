package spliterators.exercise.exercise4;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class AlternatingSpliterator<T> extends Spliterators.AbstractSpliterator<T> {

    private final Spliterator<T> first, second;

    private AlternatingSpliterator(long estimatedSize, int characteristics, Spliterator<T> first, Spliterator<T> second) {
        super(estimatedSize, characteristics);
        this.first = first;
        this.second = second;
    }

    public static <T> AlternatingSpliterator<T> combine(Stream<T> firstStream, Stream<T> secondStream) {
        final Spliterator<T> first = firstStream.spliterator(), second = secondStream.spliterator();
        return new AlternatingSpliterator<>(
            first.estimateSize() + second.estimateSize(),
            first.characteristics() | second.characteristics(),
            first,
            second);
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return false;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        first.forEachRemaining(action);
        second.forEachRemaining(action);
    }

    @Override
    public long getExactSizeIfKnown() {
        return first.getExactSizeIfKnown() + second.getExactSizeIfKnown();
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return first.hasCharacteristics(characteristics) && second.hasCharacteristics(characteristics);
    }
}
