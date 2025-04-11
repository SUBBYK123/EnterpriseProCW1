package uk.ac.bradford.projecttwo.webinterface.models;

import java.time.LocalDateTime;

/**
 * Represents an individual asset in a dataset.
 * This model holds information about the asset, such as its name, coordinates,
 * the user who created it, and the time it was created.
 */
public class IndividualAssetModel {

    // Unique identifier for the asset
    private int id;

    // Name of the dataset to which the asset belongs
    private String datasetName;

    // Name of the asset
    private String name;

    // Latitude coordinate of the asset
    private double latitude;

    // Longitude coordinate of the asset
    private double longitude;

    // User who created the asset
    private String createdBy;

    // Timestamp when the asset was created
    private LocalDateTime createdAt;

    /**
     * Default constructor for creating an empty instance of IndividualAssetModel.
     */
    public IndividualAssetModel() {}

    /**
     * Constructor to initialize an individual asset with specified parameters.
     *
     * @param id The unique identifier of the asset.
     * @param datasetName The name of the dataset the asset belongs to.
     * @param name The name of the asset.
     * @param latitude The latitude coordinate of the asset.
     * @param longitude The longitude coordinate of the asset.
     * @param createdBy The user who created the asset.
     * @param createdAt The timestamp when the asset was created.
     */
    public IndividualAssetModel(int id, String datasetName, String name,
                                double latitude, double longitude,
                                String createdBy, LocalDateTime createdAt) {
        this.id = id;
        this.datasetName = datasetName;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    /**
     * Gets the name of the dataset the asset belongs to.
     *
     * @return The dataset name.
     */
    public String getDatasetName() {
        return datasetName;
    }

    /**
     * Sets the name of the dataset the asset belongs to.
     *
     * @param datasetName The name of the dataset.
     */
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    /**
     * Gets the unique identifier of the asset.
     *
     * @return The asset's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the asset.
     *
     * @param id The ID of the asset.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the asset.
     *
     * @return The name of the asset.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the asset.
     *
     * @param name The name of the asset.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the latitude coordinate of the asset.
     *
     * @return The latitude of the asset.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude coordinate of the asset.
     *
     * @param latitude The latitude of the asset.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude coordinate of the asset.
     *
     * @return The longitude of the asset.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude coordinate of the asset.
     *
     * @param longitude The longitude of the asset.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the user who created the asset.
     *
     * @return The creator's email.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created the asset.
     *
     * @param createdBy The email of the user who created the asset.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the timestamp when the asset was created.
     *
     * @return The creation time of the asset.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when the asset was created.
     *
     * @param createdAt The creation time of the asset.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
