package pojos;

public class Alerts {

    private int alert_id;
    private int user_id;
    private int project_id;
    private int folder_id;
    private int type_id;
    private int view_id;
    private byte alert_type;
    private byte alert_frequency;
    private int stamp_id;

    public int getAlert_id() {
        return alert_id;
    }

    public void setAlert_id(int alert_id) {
        this.alert_id = alert_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getView_id() {
        return view_id;
    }

    public void setView_id(int view_id) {
        this.view_id = view_id;
    }

    public byte getAlert_type() {
        return alert_type;
    }

    public void setAlert_type(byte alert_type) {
        this.alert_type = alert_type;
    }

    public byte getAlert_frequency() {
        return alert_frequency;
    }

    public void setAlert_frequency(byte alert_frequency) {
        this.alert_frequency = alert_frequency;
    }

    public int getStamp_id() {
        return stamp_id;
    }

    public void setStamp_id(int stamp_id) {
        this.stamp_id = stamp_id;
    }

}
