package uk.ac.bradford.projecttwo.webinterface.models;

import java.time.LocalDateTime;

public class IndividualAssetModel {
    private int id;
    private String datasetName;
    private String name;
    private double latitude;
    private double longitude;
    private String createdBy;
    private LocalDateTime createdAt;

    public IndividualAssetModel() {}

    public IndividualAssetModel(int id, String datasetName, String name, double latitude, double longitude, String createdBy, LocalDateTime createdAt) {
        this.id = id;
        this.datasetName = datasetName;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
