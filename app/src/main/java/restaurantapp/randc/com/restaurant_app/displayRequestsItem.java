package restaurantapp.randc.com.restaurant_app;

public class displayRequestsItem {
    private String UID;
    private String name;
    private String OrderID;

    public displayRequestsItem(String UID, String name, String orderID) {
        this.UID = UID;
        this.name = name;
        this.OrderID = orderID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

}
