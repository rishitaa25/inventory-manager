package com.nexus.experiment3.people;

import org.springframework.http.HttpStatus; // std numeric codes sent back by server like 200, 404, 400
import org.springframework.http.ResponseEntity; // spring class that lets you send data along with status/errors
import org.springframework.web.bind.annotation.*;// brings spring classes to write web controllers like getmapping

import jakarta.validation.Valid;

import java.util.*;

@RestController //every method returns JSON by default
public class peopleController {

    HashMap<String, Person> peopleList = new HashMap<>(); //memory storage by first name

    // THIS IS THE CREATE OPERATOR
    @PostMapping("/people") //create new
    public ResponseEntity<String> createPerson(@Valid @RequestBody Person person) { //makes sure its not empty

        if (peopleList.containsKey(person.getFirstName())) {
            return new ResponseEntity<>("Person already exists.", HttpStatus.BAD_REQUEST); //400 - Bad request
        }

        peopleList.put(person.getFirstName(), person);

        return new ResponseEntity<>("Person created successfully.", HttpStatus.CREATED);// 201 - creation success
    }

    // THIS IS THE LIST OPERATOR
    @GetMapping("/people") //gets people as JSON
    public Collection<Person> getAllPersons() {
        return peopleList.values();
    }

    // THIS IS THE FILTER BY CATEGORY OPERATOR
    @GetMapping("/people/filter")
    public List<Person> filterByCategory(@RequestParam String category) { // asks for category

        List<Person> result = new ArrayList<>(); //creates new arraylist

        for (Person p : peopleList.values()) { //loops all people if category matches
            if (p.getCategory() != null &&
                    p.getCategory().equalsIgnoreCase(category)) { //not case-sensitive
                result.add(p);
            }
        }

        return result;
    }

    // THIS IS THE SORT OPERATOR
    @GetMapping("/people/sort")
    public List<Person> sortPeople(@RequestParam String by) {

        List<Person> list = new ArrayList<>(peopleList.values());

        if (by.equalsIgnoreCase("firstName")) { //sort by first name
            list.sort(Comparator.comparing(Person::getFirstName));
        }
        else if (by.equalsIgnoreCase("importance")) { //sort by level of importance/ priority
            list.sort(Comparator.comparing(Person::getImportance));
        }

        return list;
    }

    // THIS IS THE BOOLEAN OPERATOR
    @PutMapping("/people/toggle/{firstName}")
    public ResponseEntity<Person> toggleActive(@PathVariable String firstName) { // toggles/flips the active status of person

        Person p = peopleList.get(firstName);

        if (p == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //404 if person not found
        }

        p.setActive(!p.isActive());

        return new ResponseEntity<>(p, HttpStatus.OK); //success if flipped!
    }

    // THIS IS THE DELETE OPERATOR
    @DeleteMapping("/people/{firstName}")
    public ResponseEntity<String> deletePerson(@PathVariable String firstName) { //removes person by first name

        if (!peopleList.containsKey(firstName)) {
            return new ResponseEntity<>("Person not found.", HttpStatus.NOT_FOUND); //404 not found
        }

        peopleList.remove(firstName);

        return new ResponseEntity<>("Person deleted.", HttpStatus.OK); // success
    }

    // THIS IS THE COUNT OPERATOR
    @GetMapping("/people/count")
    public int countPeople() {
        return peopleList.size(); // finally returns total number of people stored
    }
}
