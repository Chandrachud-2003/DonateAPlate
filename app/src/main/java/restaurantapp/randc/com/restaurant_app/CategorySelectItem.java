package restaurantapp.randc.com.restaurant_app;

public class CategorySelectItem {
    private String categoryName;
    private int categoryIcon;
    private int categoryBg;

    public CategorySelectItem(String categoryName, int categoryIcon, int categoryBg) {
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.categoryBg = categoryBg;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(int categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public int getCategoryBg() {
        return categoryBg;
    }

    public void setCategoryBg(int categoryBg) {
        this.categoryBg = categoryBg;
    }
}
