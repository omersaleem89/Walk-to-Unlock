package com.walktounlock.model;

/**
 * Created by M Umer Saleem on 9/6/2017.
 */

public class App {
    int id;
    String name, status, limit, check;

    public App() {
    }

    public App(String name, String check) {
        this.name = name;
        this.check = check;
    }

    public App(String name, String status, String limit) {
        this.name = name;
        this.status = status;
        this.limit = limit;
    }

    public App(String name, String status, String limit, String check) {
        this.name = name;
        this.status = status;
        this.limit = limit;
        this.check = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
