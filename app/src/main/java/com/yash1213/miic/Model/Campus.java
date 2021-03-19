package com.yash1213.miic.Model;

public class Campus {

    public String titleText, secondaryText;

    public Campus(String titleText, String secondaryText) {
        this.titleText = titleText;
        this.secondaryText = secondaryText;
    }

    public Campus(){
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }
}
