package restaurantapp.randc.com.restaurant_app;

public class MainItem {

    private String location;
    private String typeRestaurant;
    private String time;
    private String weight;
    private String name;
    private boolean fruits;
    private boolean Vegetables;
    private boolean Meat;
    private boolean Dairy;
    private boolean Dishes;
    private boolean Grains;
    private String image;
    private String uid;
    private String orderId;
    private String address;


    public MainItem(String location, String typeRestaurant, String time, String weight, String name, boolean fruits, boolean vegetables, boolean meat, boolean dairy, boolean grains, boolean dishes, String image, String uid, String orderId, String address) {
        this.location = location;
        this.typeRestaurant = typeRestaurant;
        this.time = time;
        this.weight = weight;
        this.name = name;
        this.fruits = fruits;
        Vegetables = vegetables;
        Meat = meat;
        Dairy = dairy;
        Dishes = dishes;
        Grains = grains;
        this.image = image;
        this.uid = uid;
        this.orderId = orderId;
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTypeRestaurant() {
        return typeRestaurant;
    }

    public void setTypeRestaurant(String typeRestaurant) {
        this.typeRestaurant = typeRestaurant;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFruits() {
        return fruits;
    }

    public void setFruits(boolean fruits) {
        this.fruits = fruits;
    }

    public boolean isVegetables() {
        return Vegetables;
    }

    public void setVegetables(boolean vegetables) {
        Vegetables = vegetables;
    }

    public boolean isMeat() {
        return Meat;
    }

    public void setMeat(boolean meat) {
        Meat = meat;
    }

    public boolean isDairy() {
        return Dairy;
    }

    public void setDairy(boolean dairy) {
        Dairy = dairy;
    }

    public boolean isDishes() {
        return Dishes;
    }

    public void setDishes(boolean dishes) {
        Dishes = dishes;
    }

    public boolean isGrains() {
        return Grains;
    }

    public void setGrains(boolean grains) {
        Grains = grains;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
