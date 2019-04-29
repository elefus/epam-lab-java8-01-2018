package streams.part2.exercise.pairs;

import lambda.data.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CompanyPersonPair {

    private final Person person;
    private final String employer;

}
