package restaurantapp.randc.com.restaurant_app;

public class searchItem {

    private String location;
    private String type;
    private String name;
    private int image;

    public searchItem(String location, String type, String name, int image) {
        this.location = location;
        this.type = type;
        this.name = name;
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}


