package com.vv.attendanceteacher.models;

public class ShowAttendanceModel {
    private String Id;
    private String admissionNo;
    private String classId;
    private String classArmId;
    private String status;
    private String dateTimeTaken;
    private String assignSubjectId;
    private String firstName;
    private String lastName;
    private String admissionNumber;
    private String rollNo;
    public String getId() {
        return Id;
    }
    public void setId(String id) {
        this.Id = id;
    }
    public String getAdmissionNo() {
        return admissionNo;
    }
    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }
    public String getClassId() {
        return classId;
    }
    public void setClassId(String classId) {
        this.classId = classId;
    }
    public String getClassArmId() {
        return classArmId;
    }
    public void setClassArmId(String classArmId) {
        this.classArmId = classArmId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDateTimeTaken() {
        return dateTimeTaken;
    }
    public void setDateTimeTaken(String dateTimeTaken) {
        this.dateTimeTaken = dateTimeTaken;
    }
    public String getAssignSubjectId() {
        return assignSubjectId;
    }
    public void setAssignSubjectId(String assignSubjectId) {
        this.assignSubjectId = assignSubjectId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getAdmissionNumber() {
        return admissionNumber;
    }
    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }
    public String getRollNo() {
        return rollNo;
    }
    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }
}
