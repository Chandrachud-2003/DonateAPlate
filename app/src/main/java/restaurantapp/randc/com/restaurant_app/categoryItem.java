package restaurantapp.randc.com.restaurant_app;

public class categoryItem {
    private String foodItem;
    private float foodWeight;
    private boolean custom;

    public categoryItem()
    {

    }

    public categoryItem(String foodItem, float foodWeight, boolean custom) {
        this.foodItem = foodItem;
        this.foodWeight = foodWeight;
        this.custom = custom;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    public float getFoodWeight() {
        return foodWeight;
    }

    public void setFoodWeight(float foodWeight) {
        this.foodWeight = foodWeight;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }
}
