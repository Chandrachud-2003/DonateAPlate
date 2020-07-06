package restaurantapp.randc.com.restaurant_app;
//This is a comment
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.ibrahimsn.lib.SmoothBottomBar;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.auth.User;
import com.wang.avi.AVLoadingIndicatorView;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;


import java.util.ArrayList;
import java.util.Arrays;

public class Main_Activity extends AppCompatActivity {


    private SlidingRootNav slidingRootNav;

    private static final int POS_DASHBOARD = 0;
    private static final int POS_NOTIFICATION= 1;
    private static final int POS_PLUS = 2;
    private static final int POS_PROFILE = 3;

    private TextView donationtxt;
    private TextView ongiongtxt;
    private TextView pendingtxt;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private ImageButton menuButton;

    private LinearLayout searchBarLayout;
    private Button settingsButton;
    private  RecyclerView filterView;
    private ArrayList<filterItem> filterItemList;
    private RecyclerView.LayoutManager RecyclerViewLayoutManager;
    private LinearLayoutManager HorizontalLayout;
    private RecyclerView searchRecycler;
    private ArrayList<searchItem> searchList;
    private searchAdapter searchAdapter;


    private LinearLayoutManager verticalLayout;
    private  RecyclerView mainRecycler;
    private MainAdapter mainAdapter;
    private ArrayList<MainItem> mainItems;

    private static final int PAGE_START = 1;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    private NestedScrollView nestedScrollView;
    private Button logoutButton;
    private boolean atBottom;
    private AVLoadingIndicatorView mainRecyclerLoader;
    private String MyUID;
    private ArrayList<String> orderIds;
    private FirebaseFirestore db ;
    private double currectLat;
    private double currentLon;

    private String tempName;
    private String tempType;
    private double tempLat;
    private double tempLon;
    private String tempAddress;
    private boolean tempFruit;
    private boolean tempVeg;
    private String tempTotalWeight;
    private boolean tempMeat;
    private String tempUrl;
    private boolean tempGrain;
    private boolean tempDairy;


    private CustomSmoothViewPager ongoingRecycler;
    private CustomSmoothViewPager requestedRecycler;
    private TextView nodonations;
    private   DatabaseReference rootRef;
    private ArrayList<Boolean> allOrderNum;
    private String dis;
    private ArrayList<OngoingItems> ongoingItems;
    private ArrayList<OngoingItems> requestedItems;
    private ArrayList<String> currentOrderIds;


    private int  requestsCount =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        currectLat=0.0;
        currentLon=0.0;
        searchBarLayout = findViewById(R.id.searchBarLayout);
        nestedScrollView = findViewById(R.id.mainNestedScrollView);

        db = FirebaseFirestore.getInstance();
        filterView = findViewById(R.id.filterView);
        menuButton = findViewById(R.id.menuButton);
        mainItems = new ArrayList<>();
        mainRecycler = findViewById(R.id.mainRecycler);
        nodonations = findViewById(R.id.no_donations);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MyUID = user.getUid();
        nodonations.setVisibility(View.GONE);
        donationtxt = findViewById(R.id.nearbyText);
        mainRecyclerLoader = findViewById(R.id.mainRecycler_loader);
        pendingtxt = findViewById(R.id.pendingDonationsHeading);
        ongiongtxt = findViewById(R.id.ongoingDonationsHeading);
        mainRecyclerLoader.setVisibility(View.GONE);
        ongoingRecycler = findViewById(R.id.ongoingSmoolider);
        requestedRecycler = findViewById(R.id.requestSmoolider);
        mainRecycler = findViewById(R.id.mainRecycler);
        currentOrderIds = new ArrayList<>();
        ongoingItems = new ArrayList<>();
        requestedItems = new ArrayList<>();
        rootRef = FirebaseDatabase.getInstance().getReference();
        //  searchRecycler = findViewById(R.id.searchRecycler);



        if(user.getDisplayName().equals("Restaurant")) {
            ongoingRecycler.setVisibility(View.VISIBLE);
            ongoingRecycler.setPadding(150, 0, 150, 0);
            requestedRecycler.setVisibility(View.VISIBLE);
            requestedRecycler.setPadding(150, 0, 150, 0);
            donationtxt.setVisibility(View.GONE);
            ongiongtxt.setVisibility(View.VISIBLE);
            pendingtxt.setVisibility(View.VISIBLE);

            db.collection(Constants.rest_fire).document(MyUID).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {

                                allOrderNum = (ArrayList) documentSnapshot.get(Constants.order_id_num);
                                for (int i = 0; i < allOrderNum.size(); i++) {
                                    if (allOrderNum.get(i)) {
                                        currentOrderIds.add(MyUID + "-" + i);

                                    }
                                }
                                Log.d("tag", "Current IDS:" + currentOrderIds);

                                myDonationsRetriever(0,currentOrderIds.size());

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onFailure: " + e.toString());
                }
            });


        }







        if(user.getDisplayName().equals("NGO")) {
            mainRecycler.setVisibility(View.VISIBLE);
            mainRecyclerLoader.setVisibility(View.VISIBLE);
            ongiongtxt.setVisibility(View.GONE);
            pendingtxt.setVisibility(View.GONE);
            donationtxt.setVisibility(View.VISIBLE);
            requestedRecycler.setVisibility(View.GONE);
            ongoingRecycler.setVisibility(View.GONE);
      //      searchList = new ArrayList<>();

            getOrderIDS();

            Log.d("TAG", "run: listIntitialPos: " + TOTAL_PAGES);
            getDeviceLocation();
/*            searchList.add(new searchItem("Bangalore", "Restaurant", "Pizza Hut", R.drawable.restaurant2));
            searchList.add(new searchItem("Mumbai", "Restaurant", "Dominos", R.drawable.restaurant3));
            searchList.add(new searchItem("Bangalore", "NGO", "Ngo 1", R.drawable.ngo1));
            searchList.add(new searchItem("Bangalore", "NGO", "Ngo 2", R.drawable.ngo2));
            searchAdapter = new searchAdapter(searchList, Main_Activity.this);
             searchRecycler.setLayoutManager(verticalLayout);
             searchRecycler.setAdapter(searchAdapter);*/


            verticalLayout = new LinearLayoutManager(
                    Main_Activity.this,
                    LinearLayoutManager.VERTICAL,
                    false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            mainRecyclerLoader.setVisibility(View.VISIBLE);
            mainRecyclerLoader.show();


            nestedScrollView.getViewTreeObserver()
                    .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                        @Override
                        public void onScrollChanged() {
                            if (nestedScrollView.getChildAt(0).getBottom()
                                    <= (nestedScrollView.getHeight() + nestedScrollView.getScrollY() + 500)) {
                                //scroll view is at bottom
                                if (!atBottom) {
                                    atBottom = true;
                                    Log.d("tag", "onScrollChanged: reached bottom");
                                    if (orderIds.size() > 10) {
                                        mainRecyclerLoader.setVisibility(View.VISIBLE);
                                        mainRecyclerLoader.show();
                                        loadNextPage();
                                        Log.d("tag", "onScrollChanged:loaded items");
                                    }


                                }

                            } else {
                                if (atBottom) {
                                    atBottom = false;
                                    Log.d("tag", "onScrollChanged:not at bottom");
                                }
                                //scroll view is not at bottom
                            }
                        }
                    });

/*            filterItemList = new ArrayList<filterItem>();

            filterItemList.add(new filterItem("Nearby", R.drawable.icons8_nearby, false));
            filterItemList.add(new filterItem("Orders", R.drawable.icons8_mostorders, false));
            filterItemList.add(new filterItem("Followers", R.drawable.icons8_person, false));
            filterItemList.add(new filterItem("Likes", R.drawable.icons8_likes2, false));
            filterItemList.add(new filterItem("Verified", R.drawable.icons8_verified_account, false));


            filterAdapter1 filterAdapter1 = new filterAdapter1(filterItemList);

            HorizontalLayout
                    = new LinearLayoutManager(
                    Main_Activity.this,
                    LinearLayoutManager.HORIZONTAL,
                    false);


            filterView.setLayoutManager(HorizontalLayout);

            // Set adapter on recycler view
            filterView.setAdapter(filterAdapter1);*/
        }

        slidingRootNav = new SlidingRootNavBuilder(this)

                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();



        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter2 = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_NOTIFICATION),
                createItemFor(POS_PLUS),
                createItemFor(POS_PROFILE)));
        adapter2.setListener(new DrawerAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                Main_Activity.this.onItemSelected(position);
            }
        });

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter2);

        adapter2.setSelected(POS_DASHBOARD);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.openMenu();
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        settingsButton = findViewById(R.id.settingsButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });





    }



    @Override
    public void onBackPressed() {
        // Disabling back button for current activity
    }

    public void onItemSelected(int position) {



        slidingRootNav.closeMenu();

        switch (position)
        {
            case 0:
            {
                slidingRootNav.closeMenu();
                break;
            }
            case 1:
            {

            }
            case 2:
            {
                Intent intent = new Intent(Main_Activity.this, addClass.class);
                startActivity(intent);
                break;
            }


            case 3:
            {
                Intent intent = new Intent(Main_Activity.this, profileClass.class);
                intent.putExtra("From","Navigation");
                startActivity(intent);
                break;
            }





        }

    }


    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withTextTint(color(R.color.greenText))
                .withSelectedIconTint(color(R.color.white))
                .withSelectedTextTint(color(R.color.white));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private void logout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main_Activity.this);
        builder.setCancelable(true);
        builder.setTitle("Log Out");
        builder.setMessage("Are you sure want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Main_Activity.this, loginpage.class);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void loadFirstPage() {
        Log.d("TAG", "loadFirstPage: " + orderIds.size());

        int max =10;
        if (orderIds.size()<10)
        {
            max = orderIds.size();
        }
        db = FirebaseFirestore.getInstance();

        retriever(0,max, false, 0);

    }
    public void myDonationsRetriever(int i, int num)
    {
        if (i < num) {
            requestsCount = 0;
            rootRef.child(Constants.orderName_fire).child(currentOrderIds.get(i)).child("Info").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshotInfo) {
                    if(snapshotInfo.exists()) {

                        Log.d("tag", "State:" + snapshotInfo.child("State").getValue().toString());
                        if (snapshotInfo.child("State").getValue().toString().equals("New")) {

                            rootRef.child(Constants.orderName_fire).child(currentOrderIds.get(i)).child(Constants.requests_fire).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot.exists())
                                        requestsCount = (int) snapshot.getChildrenCount();
                                    else
                                        requestsCount = 0;
                                    rootRef.child(Constants.orderName_fire).child(currentOrderIds.get(i)).child(Constants.foodName_fire).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshotFood) {
                                            tempDairy = tempFruit = tempGrain = tempMeat = tempVeg = false;
                                            Log.d("tag", "Entered food");
                                            if (snapshotFood.hasChild(Constants.dairyName_fire)) {
                                                tempDairy = true;
                                            }
                                            if (snapshotFood.hasChild(Constants.fruitName_fire)) {
                                                tempFruit = true;
                                            }
                                            if (snapshotFood.hasChild(Constants.vegName_fire)) {
                                                tempVeg = true;
                                            }
                                            if (snapshotFood.hasChild(Constants.meatName_fire)) {
                                                tempMeat = true;
                                            }
                                            if (snapshotFood.hasChild(Constants.grainsName_fire)) {
                                                tempGrain = true;
                                            }


                                            requestedItems.add(new OngoingItems(requestsCount+" Requests", null, snapshotInfo.child("Total Weight").getValue().toString(), tempVeg, tempFruit, tempDairy, tempGrain, tempMeat, currentOrderIds.get(i), MyUID, ""));
                                            myDonationsRetriever(i + 1, num);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.d("TAG", "onFailure: " + error.toString());
                                        }

                                    });


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                                                    } else if (snapshotInfo.child("State").getValue().toString().equals("Ongoing")) {
                            rootRef.child(Constants.orderName_fire).child(currentOrderIds.get(i)).child(Constants.foodName_fire).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotFood) {
                                    tempDairy = tempFruit = tempGrain = tempMeat = tempVeg = false;
                                    Log.d("tag", "Entered food");
                                    if (snapshotFood.hasChild(Constants.dairyName_fire)) {
                                        tempDairy = true;
                                    }
                                    if (snapshotFood.hasChild(Constants.fruitName_fire)) {
                                        tempFruit = true;
                                    }
                                    if (snapshotFood.hasChild(Constants.vegName_fire)) {
                                        tempVeg = true;
                                    }
                                    if (snapshotFood.hasChild(Constants.meatName_fire)) {
                                        tempMeat = true;
                                    }
                                    if (snapshotFood.hasChild(Constants.grainsName_fire)) {
                                        tempGrain = true;
                                    }
                                    String NGOuid = snapshotInfo.child("Accepted").getValue().toString();
                                    Log.d("tag", "NGO ID:" + NGOuid);
                                    db.collection(Constants.ngo_fire).document(
                                            NGOuid).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {
                                                        Log.d("tag", "entered NGO");
                                                        String name = documentSnapshot.get("Name").toString();
                                                        String url = documentSnapshot.get("Url").toString();
                                                        String address = documentSnapshot.get("Address").toString();
                                                        ongoingItems.add(new OngoingItems(name, url, snapshotInfo.child("Total Weight").getValue().toString(), tempVeg, tempFruit, tempDairy, tempGrain, tempMeat, currentOrderIds.get(i), NGOuid, address));
                                                        myDonationsRetriever(i + 1, num);
                                                    }
                                                }

                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("TAG", "onFailure: " + e.toString());

                                        }
                                    });


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("TAG", "onFailure: " + error.toString());
                                }
                            });


                        }
                    }
                    else
                        myDonationsRetriever(i + 1, num);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("TAG", "onFailure: " + error.toString());
                }
            });
        }
        else
        {
            ongoingRecycler.setAdapter(new OngoingAdapter(ongoingItems, Main_Activity.this));
            requestedRecycler.setAdapter(new RequestAdapter(requestedItems,Main_Activity.this));
        }
    }


    public void retriever(int i, int max, boolean check, int intitialPos) {
        if (i < max) {
            String id = orderIds.get(i).trim();
            String userId = id.substring(0, id.indexOf("-")).trim();

            tempDairy = tempFruit = tempGrain = tempMeat = tempVeg = tempVeg = false;
            tempName = tempUrl = tempType = "";




            db.collection(Constants.rest_fire).document(
                        userId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    tempName = (String) documentSnapshot.get(Constants.username);
                                    tempAddress = (String) documentSnapshot.getString("Address");
                                    tempUrl = (String) documentSnapshot.get(Constants.url_user);
                                    Log.d("TAG", "URL:" + tempUrl);
                                    tempType = (String) documentSnapshot.get(Constants.type_user);
                                    dis = "-";
                                    try {
                                        GeoPoint geoPoint = documentSnapshot.getGeoPoint("Location");
                                        tempLat = geoPoint.getLatitude();
                                        tempLon = geoPoint.getLongitude();


                                        if(currectLat!=0&&currentLon!=0) {
                                            float[] results = new float[1];
                                            Location.distanceBetween(tempLat, tempLon,
                                                    currectLat, currentLon, results);
                                            dis = Math. round(results[0] / 100) / 10.0+"KM";
                                        }
                                    }catch (Exception e) { }




                                    rootRef.child(Constants.orderName_fire).child(id).child(Constants.foodName_fire).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()) {
                                                if (snapshot.hasChild(Constants.dairyName_fire)) {
                                                    tempDairy = true;
                                                }
                                                if (snapshot.hasChild(Constants.fruitName_fire)) {
                                                    tempFruit = true;
                                                }
                                                if (snapshot.hasChild(Constants.vegName_fire)) {
                                                    tempVeg = true;
                                                }
                                                if (snapshot.hasChild(Constants.meatName_fire)) {
                                                    tempMeat = true;
                                                }
                                                if (snapshot.hasChild(Constants.grainsName_fire)) {
                                                    tempGrain = true;
                                                }

                                                rootRef.child(Constants.orderName_fire).child(id).child("Info").child("Total Weight").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                        tempTotalWeight = snapshot.getValue().toString();

                                                        Log.d("CHECK", "TEMP WEIGHT:" + tempTotalWeight);
                                                       mainItems.add(new MainItem("Bangalore, Karnataka", tempType, dis, tempTotalWeight, tempName, tempFruit, tempVeg, tempMeat, tempDairy, false, tempGrain, tempUrl, userId, id, tempAddress));
                                                        retriever(i + 1, max, check, intitialPos);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Log.d("TAG", "onFailure: " + error.toString());
                                                    }
                                                });
                                            }
                                            else
                                                retriever(i + 1, max, check, intitialPos);



                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.d("TAG", "onFailure: " + error.toString());
                                        }
                                    });

                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "onFailure: " + e.toString());
                            }
                        });


            }

        else {
            if (!check) {


                mainAdapter = new MainAdapter(Main_Activity.this, mainItems);
                mainRecycler.setLayoutManager(verticalLayout);
                mainRecycler.setAdapter(mainAdapter);
                mainRecycler.setItemAnimator(new DefaultItemAnimator());
                mainRecyclerLoader.setVisibility(View.GONE);
                mainRecyclerLoader.hide();
            }

            else {
                mainAdapter.notifyItemRangeChanged(intitialPos, mainItems.size() - 1);
                mainRecyclerLoader.setVisibility(View.GONE);
                mainRecyclerLoader.hide();
            }
        }
        }

    private void loadNextPage() {


        if (mainItems != null && mainAdapter != null &&mainItems.size()<orderIds.size()) {
            Log.d("TAG", "loadNextPage: " + currentPage);
            //List<MainItem> newItems = new ArrayList<>();


                    int intitialPos = mainItems.size() - 1;
                     int listIntitialPos = currentPage * 10;
                     int listFinalPos = ((currentPage+1)*10-1);


                    if (currentPage==(TOTAL_PAGES-1)) {

                        listFinalPos = orderIds.size()-1;

                    }
                    Log.d("TAG", "run: listFinalPos: "+listFinalPos);
                    currentPage+=1;
                    retriever(intitialPos, listFinalPos, true, intitialPos);
        }
        else
        {
            mainRecyclerLoader.hide();
            mainRecyclerLoader.setVisibility(View.GONE);

        }
    }

    private void getOrderIDS()
    {
        orderIds = new ArrayList<>();
        db.collection(Constants.orderName_fire).document(Constants.order_list_fire)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    orderIds = (ArrayList) documentSnapshot.get(Constants.order_list_field);
                    if (orderIds!=null && orderIds.size()>0) {
                        TOTAL_PAGES = (int) Math.ceil(orderIds.size() / (10.0f));
                        loadFirstPage();
                        nodonations.setVisibility(View.GONE);
                    }
                    else {
                        mainRecyclerLoader.hide();
                        mainRecyclerLoader.setVisibility(View.GONE);
                        nodonations.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void getDeviceLocation(){
        Log.d("TAG", "getDeviceLocation: getting the devices current location");

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{


            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Log.d("TAG", "onComplete: found location");
                        Location currentLocation = (Location) task.getResult();

                        if(currentLocation!=null) {
                           currectLat = currentLocation.getLatitude();
                           currentLon = currentLocation.getLongitude();
                        }

                    }else{
                        Log.d("TAG", "onComplete: current location is null");
                        Toast.makeText(Main_Activity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (SecurityException e){
            Log.e("TAG", "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

}
