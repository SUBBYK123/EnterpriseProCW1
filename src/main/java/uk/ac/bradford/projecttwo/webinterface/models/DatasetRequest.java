package uk.ac.bradford.projecttwo.webinterface.models;

import java.util.List;
import java.util.Map;

public class DatasetRequest {
    private String datasetName;
    private int roleId;
    private List<String> columns;
    private List<Map<String, Object>> data;

    // Getters and Setters
    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
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
}
