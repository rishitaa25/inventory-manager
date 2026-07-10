package com.nexus.experiment2.people;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Comparator;

@RestController
public class PeopleController {

    HashMap<String, Person> peopleList = new HashMap<>();

    @GetMapping("/people")
    public HashMap<String, Person> getAllPersons() {
        return peopleList;
    }

    // create
    @PostMapping("/people")
    public String createPerson(@RequestBody Person person) {

        if (peopleList.containsKey(person.getFirstName())) {
            return "Error: Person already exists.";
        }

        peopleList.put(person.getFirstName(), person);

        return "New person " + person.getFirstName() + " saved.";
    }

    // read
    @GetMapping("/people/{firstName}")
    public Person getPerson(@PathVariable String firstName) {
        return peopleList.get(firstName);
    }

    // search
    @GetMapping("/people/contains")
    public List<Person> getPersonByParam(@RequestParam("name") String name) {

        List<Person> res = new ArrayList<>();

        for (Person p : peopleList.values()) {
            if (p.getFirstName().contains(name) ||
                    p.getLastName().contains(name)) {
                res.add(p);
            }
        }

        return res;
    }

    // sort
    @GetMapping("/people/sort")
    public List<Person> sortPeople(@RequestParam("by") String field) {

        List<Person> list = new ArrayList<>(peopleList.values());

        if (field.equalsIgnoreCase("firstName")) {
            list.sort(Comparator.comparing(Person::getFirstName));
        }
        else if (field.equalsIgnoreCase("lastName")) {
            list.sort(Comparator.comparing(Person::getLastName));
        }

        return list;
    }

    // count
    @GetMapping("/people/count")
    public String countPeople() {
        return "Total People: " + peopleList.size();
    }

    // update
    @PutMapping("/people/{firstName}")
    public Person updatePerson(@PathVariable String firstName,
                               @RequestBody Person p) {

        peopleList.replace(firstName, p);
        return peopleList.get(firstName);
    }

    // delete
    @DeleteMapping("/people/{firstName}")
    public String deletePerson(@PathVariable String firstName) {

        if (!peopleList.containsKey(firstName)) {
            return "Error: Person not found.";
        }

        peopleList.remove(firstName);

        return "Person " + firstName + " deleted successfully.";
    }
}
