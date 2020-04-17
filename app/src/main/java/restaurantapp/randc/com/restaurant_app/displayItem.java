package restaurantapp.randc.com.restaurant_app;

import android.media.Image;

public class displayItem {

    private String itemName;
    private String itemWeight;
    private int itemImage;

    public displayItem(String itemName, String itemWeight, int itemImage) {
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

    public String getItemWeight() {
        return itemWeight;
    }

    public void setItemWeight(String itemWeight) {
        this.itemWeight = itemWeight;
    }

    public int getItemImage() {
        return itemImage;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }
}
