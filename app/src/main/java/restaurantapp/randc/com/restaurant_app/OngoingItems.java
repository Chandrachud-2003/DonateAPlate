package restaurantapp.randc.com.restaurant_app;

public class OngoingItems {
    String name;
    String image;

    float weight;
    boolean isVeggies;
    boolean isFruits;

    boolean isDairy;
    boolean isGrains;
    boolean isMeat;

    public OngoingItems(String name, String image, float weight, boolean isVeggies, boolean isFruits, boolean isDairy, boolean isGrains, boolean isMeat) {
        this.name = name;
        this.image = image;
        this.weight = weight;
        this.isVeggies = isVeggies;
        this.isFruits = isFruits;
        this.isDairy = isDairy;
        this.isGrains = isGrains;
        this.isMeat = isMeat;
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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
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


}
