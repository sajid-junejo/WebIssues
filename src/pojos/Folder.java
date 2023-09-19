package pojos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Folder {

    private String folderName;
    private int folderId;
    private String typeName;
    private Integer typeId;
    
    // Define FoldersNames as a List<String>
    private List<String> FoldersNames = new ArrayList<>();
    public Map<Integer, String> putFolderNames = new HashMap<>();

    public Folder() {
        // Default constructor with no arguments
    }

    public Folder(String FolderName) {
        this.folderName = FolderName;
    }

    public Folder(Integer folderId, String typeName, Integer typeId) {
        this.folderId = folderId;
        this.typeName = typeName;
        this.typeId = typeId;
    }

    // Add a method to add folder names to the FoldersNames list
    public void addFolderName(String folderName) {
        FoldersNames.add(folderName);
    }

    // Add a getter for the FoldersNames list
    public List<String> getFoldersNames() {
        return FoldersNames;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getTypeName() {
        return typeName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
