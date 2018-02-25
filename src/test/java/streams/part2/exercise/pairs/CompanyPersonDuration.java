package streams.part2.exercise.pairs;

import lambda.data.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class CompanyPersonDuration {

    private final Person person;
    private final String company;
    private final int duration;

}
