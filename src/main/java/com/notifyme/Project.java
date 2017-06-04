package com.notifyme;

import org.springframework.data.annotation.Id;

/**
 * Created by gepard on 30.05.17.
 */
public class Project {
    @Id
    private String projectId;

    private String title;
    private String author;

    public Project() {}

    public Project(String title) {
        this.setTitle(title);
    }

    @Override
    public String toString() {
        return "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
