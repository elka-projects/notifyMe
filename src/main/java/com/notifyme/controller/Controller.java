package com.notifyme;

/**
 * Created by gepard on 20.04.17.
 */
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private UserRepository repository;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public String greet() {
        return "Hello!";
    }

    @RequestMapping("/projects/{proj}")
    public String getProjectCollaborators(@PathVariable String proj ) {
        List<User> users = repository.findByProjects(proj);
        String result = new String();
        for ( User u : users )
            result = result.concat( u.lastName + " " );
        return result;
    }

    @RequestMapping("/save")
    public String saved() {
        User user = new User("Janusz", "Kabloid");
        user.addProject("PIK");
        user.addProject("piec_procent");
        user.addProject("muszka");
        repository.save(user);
        return "I did it!";
    }

    @RequestMapping("/deleteAll")
    public String deleteAll() {
        repository.deleteAll();
        return "All data deleted";
    }
}
