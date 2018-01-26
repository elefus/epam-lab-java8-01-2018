package streams.part1.example;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;

@SuppressWarnings("ConstantConditions")
public class Example3 {

    @Test
    public void getIvansLastNames() {
        String[] ivansLastNames = null;

        assertArrayEquals(new String[]{"Мельников", "Александров"}, ivansLastNames);
    }

    @Test
    public void checkAny25AgedIvanHasDevExperience() {
        boolean any25IvanHasDevExperience = false;

        assertTrue(any25IvanHasDevExperience);
    }
}
