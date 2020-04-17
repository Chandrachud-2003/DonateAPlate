package restaurantapp.randc.com.restaurant_app;


import android.media.Image;

public class filterItem {

    private String iconName;
    private int iconImage;
    private boolean itemChanged;

    public filterItem(String iconName, int iconImage, boolean itemChanged) {
        this.iconName = iconName;
        this.iconImage = iconImage;
        this.itemChanged = itemChanged;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public int getIconImage() {
        return iconImage;
    }

    public void setIconImage(int iconImage) {
        this.iconImage = iconImage;
    }

    public boolean isItemChanged() {
        return itemChanged;
    }

    public void setItemChanged(boolean itemChanged) {
        this.itemChanged = itemChanged;
    }
}

