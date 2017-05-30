package com.notifyme;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gepard on 30.05.17.
 */
public class Project {
    @Id
    public String id;

    public String title;

    public Project() {}

    public Project(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, title='%s']",
                id, title);
    }

}
