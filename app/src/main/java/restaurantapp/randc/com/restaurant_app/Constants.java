package restaurantapp.randc.com.restaurant_app;

import java.util.ArrayList;

public class Constants {

    public static final String tag = "tag";

    public static String[] foodItemList = {"Fruits", "Vegetables", "Grains","Dairy Products","Non-Veg/Meat","Greens","Spices"};

    public static final String sharedPrefId = "RestaurantAppSharedPrefernces";

    public static final String grainsPref = "Grains";
    public static final String meatPref = "Meat";
    public static final String DairyPref = "Dairy";
    public static final String fruitPref = "Fruits";
    public static final String vegetablePref = "Vegetables";
    public static final String dishesPref = "Dishes";
    public static final String userIdPref = "UID";

    public static final String item_fire = "Items";
    public static final String orderName_fire = "Orders";
    public static final String fruitName_fire = "Fruits";
    public static final String vegName_fire = "Vegetables";
    public static final String grainsName_fire = "Grains";
    public static final String dairyName_fire = "Dairy";
    public static final String dishesName_fire = "Dishes";
    public static final String meatName_fire = "Meat";
    public static final String foodName_fire = "Food";

    public static final String order_list_fire = "Order List";
    public static final String order_list_field = "Order Ids";
    public static final String order_id_num = "order_array";
    public static final String rest_fire = "Restaurant";
    public static final String ngo_fire = "NGO";

    public static final String username = "Name";
    public static final String url_user = "Url";
    public static final String type_user = "Type";

    public static final String uid_intent = "UID";
    public static final String orderId_intent = "Order ID";
    public static final String total_weight_intent = "TotalWeight";
    public static final String name_intent = "Name of Restaurant";
    public static final String isFruits_intent = "isFruits";
    public static final String isVeggies_intent = "isVeggies";
    public static final String isMeat_intent = "isMeat";
    public static final String isDairy_intent = "isDairy";
    public static final String isGrains_intent = "isGrains";
    public static final String isDishes_intent = "isDishes";
    public static final String address_intent = "Address";

    public static final String ngo_ongoing_list_fire = "Ongoing List";

    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9002;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9003;

    public static final String requests_fire = "Requests";

    public static final String notifications = "Notifications";
    public static final String notify_fire = "ToNotify";
    public static final String notifyText_fire = "NotifyText";

    public static final String workManager_tag = "WorkManager-Restaurant-App";

    public static final ArrayList<Integer> fruit_imgs = new ArrayList<Integer>() {
        {
            add(R.drawable.circle_apple);
            add(R.drawable.circle_banana);
            add(R.drawable.mango_img);
            add(R.drawable.pineapple);
            add(R.drawable.watermellon_img);
            add(R.drawable.lemon);
            add(R.drawable.strawberry);
            add(R.drawable.orange);
            add(R.drawable.grapes);
            add(R.drawable.avacado_img);
        }
    };


    public static final ArrayList<Integer> veg_imgs = new ArrayList<Integer>() {
        {
            add(R.drawable.carrot);
            add(R.drawable.tomato);
            add(R.drawable.potato);
            add(R.drawable.onion);
            add(R.drawable.corn);
            add(R.drawable.cucumber);
            add(R.drawable.capsicum);
            add(R.drawable.garlic);
            add(R.drawable.mushrooms);
            add(R.drawable.greenbeans);
            add(R.drawable.lettuce);
            add(R.drawable.spinach);
            add(R.drawable.broccoli);
            add(R.drawable.cabbage);
        }
    };

    public static final ArrayList<Integer> dairy_imgs = new ArrayList<Integer>() {
        {
            add(R.drawable.milk);
            add(R.drawable.paneer);
            add(R.drawable.butter);
            add(R.drawable.cheese);
            add(R.drawable.tofu);
            add(R.drawable.curd);

        }
    };

    public static final ArrayList<Integer> meat_imgs = new ArrayList<Integer>() {
        {
            add(R.drawable.chicken);
            add(R.drawable.lamb);
            add(R.drawable.fish);
            add(R.drawable.pork);
            add(R.drawable.beef);

        }
    };

    public static final ArrayList<Integer> grains_imgs = new ArrayList<Integer>() {
        {
            add(R.drawable.rice);
            add(R.drawable.wheat);
            add(R.drawable.dal);
            add(R.drawable.oats);
            add(R.drawable.millets);
            add(R.drawable.barley);

        }
    };

    public static final ArrayList<Integer> dishes_imgs = new ArrayList<Integer>() {
        {
            add(R.drawable.dishes_custom_1);
            add(R.drawable.dishes_custom_2);
            add(R.drawable.dishes_custom_3);


        }
    };

}
