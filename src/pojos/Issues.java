package pojos;
import java.util.Date;

public class Issues {
    private int issueId;
    private String issueName;
    private String typeName;
    private String location;
    private Date createdOn;
    private String status;
    private String priority;
    private String createdBy;
    private String severity;
    private String reason;
    private String assignedTo;

    public Issues(int issueId, String issueName, String typeName, String location, Date createdOn, String status, String priority, String createdBy, String severity, String reason, String assignedTo) {
        this.issueId = issueId;
        this.issueName = issueName;
        this.typeName = typeName;
        this.location = location;
        this.createdOn = createdOn;
        this.status = status;
        this.priority = priority;
        this.createdBy = createdBy;
        this.severity = severity;
        this.reason = reason;
        this.assignedTo = assignedTo;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}
