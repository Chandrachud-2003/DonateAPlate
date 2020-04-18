package restaurantapp.randc.com.restaurant_app;

public class categoryItem {
    private String foodItem;
    private float foodWeight;

    public categoryItem(String foodItem, float foodWeight) {
        this.foodItem = foodItem;
        this.foodWeight = foodWeight;
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
}
