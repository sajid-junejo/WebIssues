
package pojos;
public class Folder {
    private String folderName;
    private String typeName;
    private Integer typeId;
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeid) {
        this.typeId = typeid;
    }
    
    public Folder(String folderName, String typeName) {
        this.folderName = folderName;
        this.typeName = typeName;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getTypeName() {
        return typeName;
    }
}
