package com.yash1213.miic.Model;

import java.util.ArrayList;

public class Students {

    public String studentId, studentName, studyYear, studentEmail, studentPh,studentPRN;
    public int totalPoint;
    public ArrayList<StudentDetails> profiles;

    public Students(){
    }

    public Students(String studentId, String studentName, String studyYear, String studentEmail, String studentPh,
        String studentPRN, int totalPoint, ArrayList<StudentDetails> profiles) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studyYear = studyYear;
        this.studentEmail = studentEmail;
        this.studentPh = studentPh;
        this.studentPRN = studentPRN;
        this.totalPoint  = totalPoint;
        this.profiles = profiles;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(String studyYear) {
        this.studyYear = studyYear;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentPh() {
        return studentPh;
    }

    public void setStudentPh(String studentPh) {
        this.studentPh = studentPh;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public ArrayList<StudentDetails> getProfiles() {
        return profiles;
    }

    public void setProfiles(ArrayList<StudentDetails> profiles) {
        this.profiles = profiles;
    }

    public String getStudentPRN() {
        return studentPRN;
    }

    public void setStudentPRN(String studentPRN) {
        this.studentPRN = studentPRN;
    }
}
