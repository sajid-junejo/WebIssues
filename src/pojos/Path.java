package pojos;

public class Path {
    private static Path instance;
    private String address;

    private Path() {
        // Private constructor to prevent direct instantiation
    }

    public static Path getInstance() {
        if (instance == null) {
            instance = new Path();
        }
        return instance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
