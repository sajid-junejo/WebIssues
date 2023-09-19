package DAOImpl;

import DAO.FolderDAO;
import pojos.Folder;
import java.util.List;

public class FoldersDAOImpl implements FolderDAO {
    private List<Folder> folders; // Define the folders collection

    // Setter method to set the folders collection
    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    @Override
    public int getFolderId(String folderName) {
        for (Folder folder : folders) {
            if (folder.getFolderName().equals(folderName)) {
                return folder.getFolderId();
            }
        }
        return -1; // Return -1 if the folderName is not found
    }
}
