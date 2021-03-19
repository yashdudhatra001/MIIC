package com.yash1213.miic.Model;

public class StudentDetails {

    public String date, description;
    public int point;

    public StudentDetails() {
    }

    public StudentDetails(String date, String description, int point) {
        this.date = date;
        this.description = description;
        this.point = point;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
