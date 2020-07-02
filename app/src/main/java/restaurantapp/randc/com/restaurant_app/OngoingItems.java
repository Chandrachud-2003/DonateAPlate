package restaurantapp.randc.com.restaurant_app;

public class OngoingItems {
    String name;
    String image;

    String weight;
    boolean isVeggies;
    boolean isFruits;
    boolean isDairy;
    boolean isGrains;
    boolean isMeat;
    String OrderID;
    String NGOID;
    String Address;

    public OngoingItems(String name, String image, String weight, boolean isVeggies, boolean isFruits, boolean isDairy, boolean isGrains, boolean isMeat, String orderID, String NGOID, String address) {
        this.name = name;
        this.image = image;
        this.weight = weight;
        this.isVeggies = isVeggies;
        this.isFruits = isFruits;
        this.isDairy = isDairy;
        this.isGrains = isGrains;
        this.isMeat = isMeat;
        OrderID = orderID;
        this.NGOID = NGOID;
        Address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean isVeggies() {
        return isVeggies;
    }

    public void setVeggies(boolean veggies) {
        isVeggies = veggies;
    }

    public boolean isFruits() {
        return isFruits;
    }

    public void setFruits(boolean fruits) {
        isFruits = fruits;
    }

    public boolean isDairy() {
        return isDairy;
    }

    public void setDairy(boolean dairy) {
        isDairy = dairy;
    }

    public boolean isGrains() {
        return isGrains;
    }

    public void setGrains(boolean grains) {
        isGrains = grains;
    }

    public boolean isMeat() {
        return isMeat;
    }

    public void setMeat(boolean meat) {
        isMeat = meat;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getNGOID() {
        return NGOID;
    }

    public void setNGOID(String NGOID) {
        this.NGOID = NGOID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
