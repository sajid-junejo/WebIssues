/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

/**
 *
 * @author sajid.ali
 */
public class Project {
    private int projectId;
    private String projectName;
    private int stampId;
    private int descrId;

    public int getStampId() {
        return stampId;
    }

    public void setStampId(int stampId) {
        this.stampId = stampId;
    }

    public int getDescrId() {
        return descrId;
    }

    public void setDescrId(int descrId) {
        this.descrId = descrId;
    }

    public int getDescrStubId() {
        return descrStubId;
    }

    public void setDescrStubId(int descrStubId) {
        this.descrStubId = descrStubId;
    }

    public byte getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(byte isPublic) {
        this.isPublic = isPublic;
    }

    public byte getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(byte isArchived) {
        this.isArchived = isArchived;
    }
    private int descrStubId;
    private byte isPublic;
    private byte isArchived;
    public Project(int projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }
}
