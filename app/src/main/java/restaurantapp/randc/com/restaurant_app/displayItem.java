package restaurantapp.randc.com.restaurant_app;

public class displayItem {

    private String itemName;
    private float itemWeight;
    private int itemImage;

    public displayItem(String itemName, float itemWeight, int itemImage) {
        this.itemName = itemName;
        this.itemWeight = itemWeight;
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getItemWeight() {
        return itemWeight;
    }

    public void setItemWeight(float itemWeight) {
        this.itemWeight = itemWeight;
    }

    public int getItemImage() {
        return itemImage;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }
}
