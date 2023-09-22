package DAO;
import pojos.Project;
import pojos.Folder;
import java.util.List;
public interface ProjectsDAO {  
    List<Project> getProjects();
    List<Folder> getFoldersForProject(int projectId);
    //int getTypeIdByTypeName(String typeName);
} 

