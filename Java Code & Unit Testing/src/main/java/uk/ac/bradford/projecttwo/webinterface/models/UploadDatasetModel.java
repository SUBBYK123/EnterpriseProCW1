package uk.ac.bradford.projecttwo.webinterface.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class UploadDatasetModel {
    private String datasetName;
    private String role;
    private String uploadedBy;
    private String department;
    private List<String> columns;
    private List<Map<String,Object>> data;

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

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
