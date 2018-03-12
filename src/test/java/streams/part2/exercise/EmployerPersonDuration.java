package streams.part2.exercise;

import lambda.data.Person;

public class EmployerPersonDuration {
    private  Person person;
    private  int duration;
    private  String employer;

    public EmployerPersonDuration(String employer, Person person, int duration) {
        this.person = person;
        this.duration = duration;
        this.employer = employer;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setEmployer(String employer) {
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
