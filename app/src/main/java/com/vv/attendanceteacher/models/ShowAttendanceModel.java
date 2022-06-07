package com.vv.attendanceteacher.models;

public class ShowAttendanceModel {
    private String id;
    private String admissionNo;
    private String classId;
    private String classArmId;
    private String sessionTermId;
    private String status;
    private String dateTimeTaken;
    private String assignSubjectId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getSessionTermId() {
        return sessionTermId;
    }
    public void setSessionTermId(String sessionTermId) {
        this.sessionTermId = sessionTermId;
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
}
