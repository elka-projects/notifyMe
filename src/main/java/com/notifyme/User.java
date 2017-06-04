package com.notifyme;

/**
 * Created by gepard on 20.04.17.
 */

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;


public class User {

    @Id
    private String id;

    private String login;
    private String password;

    private String firstName;
    private String lastName;

    private String role;
    private String mail;

    private List<String> projects;
    private List<String> templatesHistory;

    public User() {}

    public User(String firstName, String lastName) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setProjects(new ArrayList<String>());
    }

    public void addProject( String p ) {
        getProjects().add(p);
    }

    public void deleteProject( String p ) { getProjects().remove(p); }

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, firstName='%s', lastName='%s']",
                getId(), getFirstName(), getLastName());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public List<String> getTemplatesHistory() {
        return templatesHistory;
    }

    public void setTemplatesHistory(List<String> templatesHistory) {
        this.templatesHistory = templatesHistory;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
