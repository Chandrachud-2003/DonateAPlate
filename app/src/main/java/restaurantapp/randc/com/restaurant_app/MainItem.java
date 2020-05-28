package restaurantapp.randc.com.restaurant_app;

public class MainItem {

    private String location;
    private String typeRestaurant;
    private String type;
    private String time;
    private String followers;
    private String weight;
    private String name;
    private boolean fruits;
    private boolean Vegetables;
    private boolean Meat;
    private boolean Dairy;
    private boolean Dishes;
    private boolean Grains;
    private int image;


    public MainItem(String location, String typeRestaurant, String type, String time, String followers, String weight, String name, boolean fruits, boolean vegetables, boolean meat, boolean dairy, boolean dishes, boolean grains, int image) {
        this.location = location;
        this.typeRestaurant = typeRestaurant;
        this.type = type;
        this.time = time;
        this.followers = followers;
        this.weight = weight;
        this.name = name;
        this.fruits = fruits;
        Vegetables = vegetables;
        Meat = meat;
        Dairy = dairy;
        Dishes = dishes;
        Grains = grains;
        this.image = image;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTypeRestaurant(String typeRestaurant) {
        this.typeRestaurant = typeRestaurant;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFruits(boolean fruits) {
        this.fruits = fruits;
    }

    public void setVegetables(boolean vegetables) {
        Vegetables = vegetables;
    }

    public void setMeat(boolean meat) {
        Meat = meat;
    }

    public void setDairy(boolean dairy) {
        Dairy = dairy;
    }

    public void setDishes(boolean dishes) {
        Dishes = dishes;
    }

    public void setGrains(boolean grains) {
        Grains = grains;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public String getTypeRestaurant() {
        return typeRestaurant;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getFollowers() {
        return followers;
    }

    public String getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public boolean isFruits() {
        return fruits;
    }

    public boolean isVegetables() {
        return Vegetables;
    }

    public boolean isMeat() {
        return Meat;
    }

    public boolean isDairy() {
        return Dairy;
    }

    public boolean isDishes() {
        return Dishes;
    }

    public boolean isGrains() {
        return Grains;
    }

    public int getImage() {
        return image;
    }
}
