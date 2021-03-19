package com.yash1213.miic.Model;

public class COs {

    public String cName, cEmail, cPhone, cImage;

    public COs(String cName, String cEmail, String cPhone, String cImage) {
        this.cName = cName;
        this.cEmail = cEmail;
        this.cPhone = cPhone;
        this.cImage = cImage;
    }

    public COs() {
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getcPhone() {
        return cPhone;
    }

    public void setcPhone(String cPhone) {
        this.cPhone = cPhone;
    }

    public String getcImage() {
        return cImage;
    }

    public void setcImage(String cImage) {
        this.cImage = cImage;
    }
}
