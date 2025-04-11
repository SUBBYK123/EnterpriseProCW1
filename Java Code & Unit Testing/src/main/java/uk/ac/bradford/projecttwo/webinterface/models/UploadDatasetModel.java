package uk.ac.bradford.projecttwo.webinterface.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * The UploadDatasetModel class represents a dataset that is being uploaded by the user.
 * It includes metadata and data content for the dataset, such as the dataset name, role,
 * the user who uploaded it, and the department associated with the dataset.
 */
public class UploadDatasetModel {

    // Name of the dataset
    private String datasetName;

    // Role of the user who uploaded the dataset
    private String role;

    // Email address of the user who uploaded the dataset
    private String uploadedBy;

    // Department associated with the dataset
    private String department;

    // List of column names in the dataset
    private List<String> columns;

    // Data content of the dataset, represented as a list of key-value pairs (Map)
    private List<Map<String, Object>> data;

    /**
     * Gets the dataset name.
     *
     * @return The name of the dataset.
     */
    public String getDatasetName() {
        return datasetName;
    }

    /**
     * Sets the dataset name.
     *
     * @param datasetName The name of the dataset.
     */
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    /**
     * Gets the role of the user who uploaded the dataset.
     *
     * @return The role of the user.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user who uploaded the dataset.
     *
     * @param role The role of the user.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the email address of the user who uploaded the dataset.
     *
     * @return The email address of the user.
     */
    public String getUploadedBy() {
        return uploadedBy;
    }

    /**
     * Sets the email address of the user who uploaded the dataset.
     *
     * @param uploadedBy The email address of the user.
     */
    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    /**
     * Gets the department associated with the dataset.
     *
     * @return The department of the dataset.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department associated with the dataset.
     *
     * @param department The department of the dataset.
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the list of columns in the dataset.
     *
     * @return The list of columns.
     */
    public List<String> getColumns() {
        return columns;
    }

    /**
     * Sets the list of columns in the dataset.
     *
     * @param columns The list of columns.
     */
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    /**
     * Gets the data content of the dataset.
     *
     * @return The data content of the dataset.
     */
    public List<Map<String, Object>> getData() {
        return data;
    }

    /**
     * Sets the data content of the dataset.
     *
     * @param data The data content of the dataset.
     */
    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    /**
     * Sets the dataset's data from a JSON string.
     * This method is used to parse and convert the JSON representation of the dataset's data into a list of maps.
     *
     * @param json The JSON string representing the dataset's data.
     */
    public void setDataFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> parsedData = mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
            this.data = parsedData;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
