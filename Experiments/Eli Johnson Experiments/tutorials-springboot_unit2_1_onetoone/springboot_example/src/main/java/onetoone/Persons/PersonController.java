package onetoone.Persons;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import onetoone.Laptops.Laptop;
import onetoone.Laptops.LaptopRepository;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@RestController
public class PersonController {

    @Autowired
    PersonRepository PersonRepository;

    @Autowired
    LaptopRepository laptopRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/Persons")
    List<Person> getAllPersons(){
        return PersonRepository.findAll();
    }

    @GetMapping(path = "/Persons/{id}")
    Person getPersonById( @PathVariable int id){
        return PersonRepository.findById(id);
    }

    @PostMapping(path = "/Persons")
    String createPerson(@RequestBody Person Person){
        if (Person == null)
            return failure;
        PersonRepository.save(Person);
        return success;
    }

    //Removed commented out method

    //Re-structured method
    @PutMapping("/update/Persons/{id}")
    //Fixed typo(s)
    Person updatePerson(@PathVariable int id, @RequestBody Person request){
        Person person = PersonRepository.findById(id);

        if(person == null) {
            throw new RuntimeException("person id does not exist");
        }
        else if (person.getId() != id){
            throw new RuntimeException("path variable id does not match person request id");
        }

        //Updated method to properly handle updated info
        person.setEmailId(request.getEmailId());
        person.setName(request.getName());

        PersonRepository.save(person); //Fixed saving issue
        return PersonRepository.findById(id);
    }

    //Assign person a laptop
    @PutMapping("/assign/Persons/{pid}/laptop/{lid}")
    Person assignLaptop(@PathVariable int pid, @PathVariable int lid) {
        Person person = PersonRepository.findById(pid);
        Laptop laptop =  laptopRepository.findById(lid);
        if (person == null) {
            throw new RuntimeException("person id does not exist");
        }
        if (laptop == null) {
            throw new RuntimeException("laptop id does not exist");
        }

        person.setLaptop(laptop);
        PersonRepository.save(person);

        return PersonRepository.findById(pid);
    }

    //Deassigning a laptop
    @PutMapping("/deassign/Persons/{id}")
    Person deassignLaptop(@PathVariable int id) {
        Person person = PersonRepository.findById(id);
        if  (person == null) {
            throw new RuntimeException("person id does not exist");
        }
        person.setLaptop(null);
        PersonRepository.save(person);
        return PersonRepository.findById(id);
    }

    //Updating laptop specs
    @PutMapping("/update/Persons/{PersonId}/laptops/{id}")
    String assignLaptopToPerson(@PathVariable int PersonId, @PathVariable int id, @RequestBody Laptop updatedLaptop){
        Person Person = PersonRepository.findById(PersonId);
        Laptop laptop = laptopRepository.findById(id);
        if(Person == null || (laptop == null))
            return failure;
        if(updatedLaptop.getId() == id)
            return "Laptop ID doesn't match";

        laptop.setCost(updatedLaptop.getCost());
        laptop.setManufacturer(updatedLaptop.getManufacturer());
        laptop.setCpuCores(updatedLaptop.getCpuCores());
        laptop.setCpuClock(updatedLaptop.getCpuClock());
        laptop.setRam(updatedLaptop.getRam());

        laptopRepository.save(updatedLaptop);

        return success;
    }

    @DeleteMapping(path = "/Persons/{id}")
    String deletePerson(@PathVariable int id){
        PersonRepository.deleteById(id);
        return success;
    }
}
