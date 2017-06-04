package com.notifyme.controller;

/**
 * Created by gepard on 20.04.17.
 */
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notifyme.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import javax.mail.SendFailedException;

@RestController
public class Controller {

    @Autowired
    private UserRepository userRepository;

    private String resultTrue = "{ \"result\" : \"true\" }";
    private String resultFalse = "{ \"result\" : \"false\" }";

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private TemplateSentRepository templateSentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private EmailService emailService;


    @RequestMapping("/greeting")
    public String greet() {
        return "Hello!";
    }

    @RequestMapping(value = "/projects/{proj}", method = RequestMethod.GET )
    public String getProjectCollaborators(@PathVariable String proj ) {
        List<User> users = userRepository.findByProjects(proj);

        JSONArray array = new JSONArray();

        for ( User u : users ) {
            JSONObject o = new JSONObject();
            o.put("login", u.getLogin());
            o.put("id", u.getId());
            array.put(o);
        }
        return array.toString();
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public String addUserToProject(@RequestBody String body) {
        JSONObject subscription = new JSONObject(body);
        String userId = (String)subscription.get("userId");
        String title = (String)subscription.get("title");
        User user = userRepository.findById(userId);
        user.addProject(title);
        userRepository.save(user);
        return resultTrue;
    }

    @RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
    public String deleteUserFromProject(@RequestBody String body) {
        JSONObject unsubscribe = new JSONObject(body);
        String userId = (String)unsubscribe.get("userId");
        String title = (String)unsubscribe.get("title");
        User user = userRepository.findById(userId);
        user.deleteProject(title);
        return resultTrue;
    }

    @RequestMapping("/deleteAll")
    public String deleteAll() {
        userRepository.deleteAll();
        return "All data deleted";
    }

    @RequestMapping(value = "/getUserProjects", method = RequestMethod.POST)
    public String getUserProjects(@RequestBody String body ) {
        ObjectMapper mapper = new ObjectMapper();
        User u;
        try {
            u = mapper.readValue(body, User.class);
        }
        catch( Exception e ) {
            return e.getMessage();
        }

        User user = userRepository.findByLogin(u.getLogin());
        List<String> projects = user.getProjects();

        JSONArray array = new JSONArray();
        for( String p : projects ) {
            Project project = projectRepository.findByTitle(p);
            JSONObject j = new JSONObject();
            j.put("title", project.getTitle());
            j.put("projectID", project.getProjectId());
            boolean owned = false;
            if( project.getAuthor().equals( user.getId() ) ) {
                owned = true;
            }
            j.put("owner", owned);
            array.put(j);
        }
        return array.toString();
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

    @RequestMapping(value = "/saveProject", method = RequestMethod.POST)
    public String saveProject(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        Project project;
        try {
            project = mapper.readValue(body, Project.class);
        }
        catch( Exception e ) {
            return resultFalse;
        }
        projectRepository.save(project);
        return resultTrue;
    }

    @RequestMapping(value = "/delProject", method = RequestMethod.POST)
    public String delProject(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        Project project;
        try {
            project = mapper.readValue(body, Project.class);
        }
        catch( Exception e ) {
            return resultFalse;
        }
        templateRepository.delete(project.getProjectId());
        return resultTrue;
    }

    @RequestMapping(value = "/saveTemplate", method = RequestMethod.POST)
    public String saveTemplate(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        Template template;
        try {
            template = mapper.readValue(body, Template.class);
        }
        catch( Exception e ) {
            return resultFalse;
        }
        templateRepository.save(template);
        return resultTrue;
    }

    @RequestMapping(value = "/deleteTemplate", method = RequestMethod.POST)
    public String deleteTemplate(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        Template template;
        try {
            template = mapper.readValue(body, Template.class);
        }
        catch( Exception e ) {
            return resultFalse;
        }
        templateRepository.delete(template.getId());
        return resultTrue;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        User user;
        try {
            user = mapper.readValue(body, User.class);
        }
        catch( Exception e ) {
            return e.getMessage();
        }
        userRepository.save(user);
        return "{ \"result\" : \"true\" }";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public String authenticate(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        User user;
        try {
            user = mapper.readValue(body, User.class);
        }
        catch( Exception e ) {
            return "{ \"jwt\" : \"0\" }";
        }
        if( userRepository.findByLoginAndPassword(user.getLogin(), user.getPassword()) == null ) {
            return "{ \"jwt\" : \"0\" }";
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret-code");
            String token = JWT.create()
                    .withIssuer("test")
                    .withAudience("test")
                    .sign(algorithm);
            return "{ \"jwt\" : \"" + token + "\"  } ";
        } catch (UnsupportedEncodingException exception){
            //UTF-8 encoding not supported
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return "{ \"jwt\" : \"0\" }";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(@RequestBody String body) {
        return resultTrue;
    }

    @RequestMapping(value = "/sendNotification", method = RequestMethod.POST)
    public String sendMail(@RequestBody String body) {
        JSONObject mailInfo = new JSONObject(body);

        String templateId = (String) mailInfo.get("id");
        String sendDate = (String) mailInfo.get("date");

        Template template = templateRepository.findById(templateId);

        Project project = projectRepository.findByTitle(template.getProject());
        List<User> receivers = userRepository.findByProjects(template.getProject());

        try {
            for (User user: receivers) {
                emailService.sendTemplatedMessage(
                        user.getMail(),
                        template);

                if (user.getTemplatesHistory() == null)
                    user.setTemplatesHistory(new ArrayList<String>());
                user.getTemplatesHistory().add(templateId);
                userRepository.save(user);
            }

        } catch (MailException e) {
            return resultFalse;
        }

        TemplateSent templateSent = new TemplateSent();
        templateSent.setTemplateId(templateId);
        templateSent.setDate(sendDate);

        templateSentRepository.save(templateSent);

        return resultTrue;
    }

    @RequestMapping(value = "/data/{login}", method = RequestMethod.GET)
    public String getuserHistory(@PathVariable String login) {
        User user = userRepository.findByLogin(login);
        List<String> list = user.getTemplatesHistory();
        JSONArray array = new JSONArray();
        for( String l : list ) {
            TemplateSent ts = templateSentRepository.findById(l);
            Template t = templateRepository.findById(ts.getTemplateId());

            JSONObject object = new JSONObject();
            object.put("author", t.getAuthor());
            object.put("project", t.getProject());
            object.put("title", t.getTitle());
            object.put("id", t.getId());
            object.put("content", t.getContent());
            object.put("date", ts.getDate());
            array.put(object);
        }
        return array.toString();
    }

    @RequestMapping(value = "/projectNotifications/{proj}", method = RequestMethod.GET)
    public String getProjectTemplates(@PathVariable String proj) {
        List<Template> templates = templateRepository.findByProject(proj);
        JSONArray array = new JSONArray();
        for( Template t : templates ) {
            JSONObject object = new JSONObject();
            object.put("id", t.getId());
            object.put("title", t.getTitle());

            array.put(object);
        }
        return array.toString();
    }

}
