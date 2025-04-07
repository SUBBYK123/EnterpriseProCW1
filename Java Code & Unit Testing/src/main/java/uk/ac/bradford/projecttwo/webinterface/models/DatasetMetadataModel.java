package uk.ac.bradford.projecttwo.webinterface.models;

import java.time.LocalDateTime;

public class DatasetMetadataModel {
    public int id;
    private String datasetName;
    private String department;
    private String uploadedBy;
    private String role;
    private LocalDateTime uploadDate;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }


    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
