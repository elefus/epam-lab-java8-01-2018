package streams.part2.example.data;

import lambda.data.Person;

public class PersonCompanyDuration {
    private final Person person;
    private final String company;
    private final int duration;

    public PersonCompanyDuration(Person person, String company, int duration) {
        this.person = person;
        this.company = company;
        this.duration = duration;
    }

    public Person getPerson() {
        return person;
    }

    public String getCompany() {
        return company;
    }

    public int getDuration() {
        return duration;
    }
}
