package restaurantapp.randc.com.restaurant_app;

public class donationBottomItem {
    private String categoryName;
    private String categoryWeight;

    public donationBottomItem(String categoryName, String categoryWeight) {
        this.categoryName = categoryName;
        this.categoryWeight = categoryWeight;


    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryWeight() {
        return categoryWeight;
    }

    public void setCategoryWeight(String categoryWeight) {
        this.categoryWeight = categoryWeight;
    }
}
