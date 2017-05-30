package com.notifyme;

/**
 * Created by gepard on 20.04.17.
 */
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemplateRepository templateRepository;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public String greet() {
        return "Hello!";
    }

    @RequestMapping("/projects/{proj}")
    public String getProjectCollaborators(@PathVariable String proj ) {
        List<User> users = userRepository.findByProjects(proj);
        String result = new String();
        for ( User u : users )
            result = result.concat( u.getId() + " " );
        return result;
    }

    @RequestMapping("/save")
    public String saved() {
        User user = new User("Janusz", "Nowak");
        user.addProject("PIK");
        user.addProject("piec_procent");
        user.addProject("muszka");
        userRepository.save(user);
        user = new User("Janusz", "Kowalski");
        user.addProject("PIK");
        user.addProject("piec_procent");
        user.addProject("muszka");
        userRepository.save(user);
        user = new User("Janusz", "Macias");
        user.addProject("PIK");
        user.addProject("piec_procent");
        user.addProject("muszka");
        userRepository.save(user);
        return "I did it!";
    }

    @RequestMapping("/deleteAll")
    public String deleteAll() {
        userRepository.deleteAll();
        return "All data deleted";
    }

    @RequestMapping("/showUserId/{firstName}")
    public String getUserId(@PathVariable String firstName) {
        User user = userRepository.findByFirstName(firstName);
        return user.getId();
    }

    @RequestMapping("/getUserProjects/{userId}")
    public String getUserProjects(@PathVariable String userId ) {
       User user = userRepository.findById(userId);
       List<String> projects = user.getProjects();
       String result = new String();
       for( String p : projects ) {
           result = result + " " + p;
       }
       return result;
    }

    @RequestMapping("/getTemplatesByProject/{proj}")
    public String getTemplatesByProject(@PathVariable String proj) {
        List<Template> templates = templateRepository.findByProject(proj);
        String result = new String();
        for( Template t : templates ) {
           result = result + " " + t.getTitle();
        }
        return result;
    }

    @RequestMapping("/getTemplateContentByTitle/{proj}/{title}")
    public String getTemplateContentByTitle(@PathVariable String title, @PathVariable String proj) {
        Template temp = templateRepository.findByTitleAndProject(title, proj);
        return temp.getContent();
    }

    @RequestMapping("/saveUserData/{data}")
    public String saveUserData(@PathVariable String data) {
        ObjectMapper mapper = new ObjectMapper();
        String test = "{ \"firstName\" : \"Filip\" }";
        User user;
        try {
            user = mapper.readValue(data, User.class);
        }
        catch( Exception e ) {
            return e.getMessage();
        }
        userRepository.save(user);
        return user.getFirstName();
    }

    @RequestMapping("/saveTemplate/{data}")
    public String saveTemplate(@PathVariable String data) {
        ObjectMapper mapper = new ObjectMapper();
        String test = "{ \"firstName\" : \"Filip\" }";
        Template template;
        try {
            template = mapper.readValue(data, Template.class);
        }
        catch( Exception e ) {
            return e.getMessage();
        }
        templateRepository.save(template);
        return template.getTitle();
    }


}
