package spliterators.exercise.example1;

import org.junit.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class Exercise1 {

    @Test
    public void testSumUsingSpliterator() {
        int[][] data = Stream.generate(() -> IntStream.generate(() -> 1)
                                                      .limit(5)
                                                      .toArray())
                             .limit(10)
                             .toArray(int[][]::new);

        IntStream stream = StreamSupport.intStream(new UnfairRectangleSpliterator(data), false);
        IntStream stream1 = StreamSupport.intStream(new FairRectangleSpliterator(data), false);

        assertEquals(50, stream.sum());
        assertEquals(50, stream1.sum());
    }

    @Test
    public void testMinUsingSpliterator() {
        int[][] data = Stream.generate(() -> IntStream.generate(() -> 42)
                                                      .limit(5)
                                                      .toArray())
                             .limit(10)
                             .toArray(int[][]::new);
        data[0][0] = 0;

        IntStream stream = StreamSupport.intStream(new UnfairRectangleSpliterator(data), false);
        IntStream stream1 = StreamSupport.intStream(new FairRectangleSpliterator(data), false);

        assertEquals(0, stream.min().orElseThrow(IllegalStateException::new));
        assertEquals(0, stream1.min().orElseThrow(IllegalStateException::new));
    }

    @Test
    public void testMaxUsingSpliterator() {
        int[][] data = Stream.generate(() -> IntStream.generate(() -> 42)
                                                      .limit(5)
                                                      .toArray())
                             .limit(10)
                             .toArray(int[][]::new);
        data[3][3] = 50;

        IntStream stream = StreamSupport.intStream(new UnfairRectangleSpliterator(data), true);
        IntStream stream1 = StreamSupport.intStream(new FairRectangleSpliterator(data), true);

        assertEquals(50, stream.max().orElseThrow(IllegalStateException::new));
        assertEquals(50, stream1.max().orElseThrow(IllegalStateException::new));
    }
}
