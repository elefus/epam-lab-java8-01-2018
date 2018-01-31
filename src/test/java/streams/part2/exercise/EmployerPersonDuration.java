package streams.part2.exercise;

import lambda.data.Person;

public class EmployerPersonDuration {
    private final Person person;
    private final int duration;
    private final String employer;

    public EmployerPersonDuration(String employer, Person person, int duration) {
        this.person = person;
        this.duration = duration;
        this.employer = employer;
    }

    public Person getPerson() {
        return person;
    }

    public int getDuration() {
        return duration;
    }

    public String getEmployer() {
        return employer;
    }
}
