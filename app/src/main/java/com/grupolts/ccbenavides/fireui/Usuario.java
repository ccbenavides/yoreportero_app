package com.grupolts.ccbenavides.fireui;

/**
 * Created by Isabel on 10/11/2016.
 */

public class Usuario {
    private String name;
    private String text;
    private String uid;

    Usuario() {
    }

    public Usuario(String name, String text, String uid) {
        this.name = name;
        this.text = text;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return this.name;
    }
}