package com.example.notificationApp.model;

public class ProductDTO {
    private String title;
    private String link;
    private String description;

    public ProductDTO(String title, String description, String link) {
        this.title = title;
        this.description=description;
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
