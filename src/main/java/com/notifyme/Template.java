package com.notifyme;

import org.springframework.data.annotation.Id;

/**
 * Created by gepard on 30.05.17.
 */
public class Template {
    @Id
    private String id;

    private String title;
    private String level;
    private String content;
    private String project;

    public Template() {};

    public Template(String title, String level, String content) {
        this.setTitle(title);
        this.setLevel(level);
        this.setContent(content);
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, title='%s']",
                getId(), getTitle());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
