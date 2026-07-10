/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @Modified By Tanmay Ghosh
 * @Modified By Vivek Bengre
 */
@RestController
class OwnerController {

    @Autowired
    OwnerRepository ownersRepository;

    private final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @RequestMapping(method = RequestMethod.POST, path = "/owners/new")
    public String saveOwner(@RequestBody Owners owner) {
        ownersRepository.save(owner);
        return "New Owner "+ owner.getFirstName() + " Saved";
    }
     // function just to create dummy data (Extended)
    @RequestMapping(method = RequestMethod.GET, path = "/owner/create")
    public String createDummyData() {
        Owners o1 = new Owners(1, "John", "Doe", "404 Not found", "some numbers", "Dog");
        Owners o2 = new Owners(2, "Jane", "Doe", "Its a secret", "you wish", "Cat");
        Owners o3 = new Owners(3, "Some", "Pleb", "Right next to the Library", "515-345-41213", "Tiger");
        Owners o4 = new Owners(4, "Chad", "Champion", "Reddit memes corner", "420-420-4200", "Bird");
        Owners o5 = new Owners(4, "Luke", "Woodard", "Durant", "305-648-4463", "Cow");
        Owners o6 = new Owners(4, "Myrna", "Brennan", "Hackberry", "781-326-9263", "Horse");
        Owners o7 = new Owners(4, "Dawn", "Ortega", "Lynchburg", "603-977-0174", "Aardvark");
        Owners o8 = new Owners(4, "Gail", "Avila", "Midvale", "305-258-3839", "Red Panda");
        ownersRepository.save(o1);
        ownersRepository.save(o2);
        ownersRepository.save(o3);
        ownersRepository.save(o4);
        ownersRepository.save(o5);
        ownersRepository.save(o6);
        ownersRepository.save(o7);
        ownersRepository.save(o8);
        return "Successfully created dummy data";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/owners")
    public List<Owners> getAllOwners() {
        logger.info("Entered into Controller Layer");
        List<Owners> results = ownersRepository.findAll();
        logger.info("Number of Records Fetched:" + results.size());
        return results;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/owners/{ownerId}")
    public Optional<Owners> findOwnerById(@PathVariable("ownerId") int id) {
        logger.info("Entered into Controller Layer");
        Optional<Owners> results = ownersRepository.findById(id);
        return results;
    }

    //Added Methods
    @RequestMapping(method = RequestMethod.GET, path = "/owners/range/{beginningInt}/{endingInt}")
    public List<Owners> findOwnerRange(@PathVariable("beginningInt") int beginningInt, @PathVariable("endingInt") int endingInt) {
        logger.info("Entered into Controller Layer");
        List<Owners> results = new ArrayList<>();
        for (int i = beginningInt; i <= endingInt; i++) {
            Optional<Owners> owners = ownersRepository.findById(i);
            owners.ifPresent(results::add);
        }
        return results;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/owners/upload")
    public String uploadOwner(@RequestBody List<Owners> owners) {
        ownersRepository.saveAll(owners);
        return "Successfully added " + owners.size() + " owners!" ;
    }
}
