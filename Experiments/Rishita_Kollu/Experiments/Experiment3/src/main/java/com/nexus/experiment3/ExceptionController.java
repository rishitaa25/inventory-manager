package com.nexus.experiment3;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ExceptionController {

    @RequestMapping(method = RequestMethod.GET, path = "/oops")
    public String triggerException() {
        throw new RuntimeException("Experiment 3 exception triggered.");
    }
}
