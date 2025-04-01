package uk.ac.bradford.projecttwo.webinterface.models;

import java.util.List;

public class PermissionRequestModel {

    private int requestId;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String requestedRole;
    private String department;
    private List<String> accessibleDatasets;
    private String status;


    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getRequestedRole() {
        return requestedRole;
    }

    public void setRequestedRole(String requestedRole) {
        this.requestedRole = requestedRole;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getAccessibleDatasets() {
        return accessibleDatasets;
    }

    public void setAccessibleDatasets(List<String> accessibleDatasets) {
        this.accessibleDatasets = accessibleDatasets;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
