package streams.part2.example.data;

import lambda.data.Person;
import lombok.Data;

@Data
public class PersonEmployerDuration {
    private final Person person;
    private final String employer;
    private final int duration;
}
