package restaurantapp.randc.com.restaurant_app;

import java.util.ArrayList;

public class donationBottomItem {
    private String categoryName;
    private float categoryWeight;
    private ArrayList<categoryItem> itemsList;
    private int categoryImage;

    public donationBottomItem(String categoryName, float categoryWeight, ArrayList<categoryItem> itemsList, int categoryImage) {
        this.categoryName = categoryName;
        this.categoryWeight = categoryWeight;
        this.itemsList = itemsList;
        this.categoryImage = categoryImage;


    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public float getCategoryWeight() {
        return categoryWeight;
    }

    public void setCategoryWeight(float categoryWeight) {
        this.categoryWeight = categoryWeight;
    }

    public ArrayList<categoryItem> getItemsList() {
        return itemsList;
    }

    public void setItemsList(ArrayList<categoryItem> itemsList) {
        this.itemsList = itemsList;
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }
}
