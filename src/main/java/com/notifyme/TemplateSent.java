package com.notifyme;

import org.springframework.data.annotation.Id;

/**
 * Created by gepard on 04.06.17.
 */
public class TemplateSent {
    @Id
    private String id;

    private String templateId;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
