package com.notifyme;

/**
 * Created by gepard on 20.04.17.
 */

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;


public class User {

    @Id
    public String id;

    public String firstName;
    public String lastName;

    public List<String> projects;

    public User() {}

    public User(String firstName, String lastName) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.projects = new ArrayList<String>();
    }

    public void addProject( String p ) {
        projects.add(p);
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

}
