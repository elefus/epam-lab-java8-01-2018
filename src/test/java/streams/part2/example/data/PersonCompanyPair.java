package streams.part2.example.data;

import lambda.data.Person;

public class PersonCompanyPair {


    private final Person person;
    private final String company;

    public PersonCompanyPair(Person person,String company) {
        this.person = person;
        this.company = company;
    }

    public Person getPerson() {
        return person;
    }

    public String getCompany() {
        return company;
    }
}
