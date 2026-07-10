package coms309;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
class WelcomeController {

    @GetMapping()
    public String homepage() {
        return "homepage";
    }

    @GetMapping("/:")//Changed path symbol for my created methods
    @ResponseBody
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @GetMapping("/:69")
    @ResponseBody
    public String nice() {
        return "Nice";
    }

    @GetMapping("/:{name}")
    @ResponseBody
    public String message(@PathVariable String name) {
        return "Hello and welcome to COMS 309 " + name;
    }

    //Also I added a new directory with my HTML sites and their respective style sheets.
}
