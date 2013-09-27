package me.loki2302.controllers;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class ArticleModel {
    @NotNull
    @NotEmpty
    private String title;
    
    @NotNull
    @NotEmpty
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}