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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.baoyz.widget.PullRefreshLayout;
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
import com.wang.avi.AVLoadingIndicatorView;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class Main_Activity extends AppCompatActivity {


    private static final int PAGE_START = 1;
    private SlidingRootNav slidingRootNav;
    private TextView ongiongtxt;
    private TextView pendingtxt;
    private TextView newdonationstxt;
    private TextView accepteddonationstxt;
    private TextView nopendingdonations;
    private TextView mainNameText;
    private TextView mainLocationText;
    private TextView noongoingdonations;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private ImageButton menuButton;
    private LinearLayout searchBarLayout;
    private RecyclerView filterView;
    private ArrayList<filterItem> filterItemList;
    private RecyclerView.LayoutManager RecyclerViewLayoutManager;
    private LinearLayoutManager HorizontalLayout;
    private RecyclerView searchRecycler;
    private ArrayList<searchItem> searchList;
    private searchAdapter searchAdapter;
    private LinearLayoutManager verticalLayout;
    private RecyclerView mainRecycler;
    private MainAdapter mainAdapter;
    private ArrayList<MainItem> mainItems;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    private NestedScrollView nestedScrollView;
    private boolean atBottom;
    private AVLoadingIndicatorView mainRecyclerLoader;
    private String MyUID;
    private ArrayList<String> orderIds;
    private FirebaseFirestore db;
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
    private String tempLoaction;
    private boolean tempMeat;
    private String tempUrl;
    private boolean tempGrain;
    private boolean tempDairy;
    private boolean tempDishes;

    private CustomSmoothViewPager ongoingRecycler;
    private CustomSmoothViewPager requestedRecycler;
    private TextView nodonations;
    private DatabaseReference rootRef;
    private ArrayList<Boolean> allOrderNum;
    private String dis;
    private ArrayList<OngoingItems> ongoingItems;
    private ArrayList<OngoingItems> requestedItems;
    private ArrayList<String> currentOrderIds;
    private DrawerAdapter adapter2;
    private int currentInputCode;

    private int requestsCount = 0;

    private SmoothBottomBar switchBar;

    private int currentSwitchBarPos;

    private ArrayList<String> ongoingItems_NGO;
    private FirebaseUser user;

    private PullRefreshLayout mRefreshLayout;

    private boolean onTop;

    private LottieAnimationView mainAnim;
    private LottieAnimationView pendingAnim;
    private LottieAnimationView ongoingAnim;

    private boolean openingForFirstTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        onTop = true;
        openingForFirstTime = true;


        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        currectLat = 0.0;
        currentLon = 0.0;
        searchBarLayout = findViewById(R.id.searchBarLayout);
        nestedScrollView = findViewById(R.id.mainNestedScrollView);

        db = FirebaseFirestore.getInstance();
        filterView = findViewById(R.id.filterView);
        menuButton = findViewById(R.id.menuButton);
        mainItems = new ArrayList<>();
        mainRecycler = findViewById(R.id.mainRecycler);
        nodonations = findViewById(R.id.no_donations);
        user = FirebaseAuth.getInstance().getCurrentUser();


        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user.reload();
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);

        MyUID = user.getUid();
        currentInputCode = 0;
        nodonations.setVisibility(View.GONE);
        mainNameText = findViewById(R.id.nameMainText);
        mainLocationText = findViewById(R.id.locationMain);
        mainRecyclerLoader = findViewById(R.id.mainRecycler_loader);
        pendingtxt = findViewById(R.id.pendingDonationsHeading);
        ongiongtxt = findViewById(R.id.ongoingDonationsHeading);
        mainRecyclerLoader.setVisibility(View.GONE);
        noongoingdonations = findViewById(R.id.no_ongoing_donations);
        nopendingdonations = findViewById(R.id.no_pending_donations);
        ongoingRecycler = findViewById(R.id.ongoingSmoolider);
        requestedRecycler = findViewById(R.id.requestSmoolider);
        mainRecycler = findViewById(R.id.mainRecycler);
        newdonationstxt = findViewById(R.id.newdonationstxt);
        accepteddonationstxt = findViewById(R.id.accepteddonationstxt);
        currentOrderIds = new ArrayList<>();
        ongoingItems = new ArrayList<>();
        requestedItems = new ArrayList<>();
        rootRef = FirebaseDatabase.getInstance().getReference();
        switchBar = findViewById(R.id.SwitchBar);
        currentSwitchBarPos = 0;
        mRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);

        mainAnim = findViewById(R.id.mainAnim);
        pendingAnim = findViewById(R.id.pendingAnim);
        ongoingAnim = findViewById(R.id.ongoingAnim);

        String collection = "";
        if (user.getDisplayName().equals("NGO")) {
            collection = "NGO";
        } else {
            collection = "Restaurant";
        }
        db.collection(collection).document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    mainNameText.setText(documentSnapshot.get("Name").toString().trim());
                    String text = documentSnapshot.get("Area") + ", " + documentSnapshot.get("City");
                    mainLocationText.setText(text);
                })
                .addOnFailureListener(e -> {
                    Log.d("TAG", "onCreate: name and location retrieval failed");
                });

        mRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (onTop) {

                    finish();
                    startActivity(getIntent());
                } else {

                    mRefreshLayout.setRefreshing(false);
                }

            }
        });
        mRefreshLayout.setRefreshing(false);
        boolean check = getIntent().getBooleanExtra("Registered", false);
        if (check) {
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Main_Activity.this);
            builder.setTitle("Almost done!");
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            builder.setMessage("A verification Email has been sent to " + user.getEmail() + "\n\nPlease click on the link in the email to unlock all the features of this app!");

            android.app.AlertDialog alertDialog = builder.create();

            alertDialog.show();
        }


        PeriodicWorkRequest.Builder periodicWorkRequest =
                new PeriodicWorkRequest.Builder(BackgroundWork.class, 15,
                        TimeUnit.MINUTES);
        PeriodicWorkRequest periodicWork = periodicWorkRequest.build();
        WorkManager instance = WorkManager.getInstance();
        instance.enqueueUniquePeriodicWork(Constants.workManager_tag, ExistingPeriodicWorkPolicy.KEEP, periodicWork);


        if (user.getDisplayName().equals("Restaurant") || user.getDisplayName().equals("Individual")) {
            switchBar.setVisibility(View.GONE);
            ongoingRecycler.setVisibility(View.VISIBLE);
            ongoingRecycler.setPadding(150, 0, 150, 0);
            requestedRecycler.setVisibility(View.VISIBLE);
            requestedRecycler.setPadding(150, 0, 150, 0);
            ongiongtxt.setVisibility(View.VISIBLE);
            pendingtxt.setVisibility(View.VISIBLE);
            newdonationstxt.setVisibility(View.INVISIBLE);
            accepteddonationstxt.setVisibility(View.INVISIBLE);
            mainAnim.setVisibility(View.GONE);
            mainAnim.cancelAnimation();

            pendingAnim.setAnimation(R.raw.loading_main);
            pendingAnim.setRepeatMode(LottieDrawable.RESTART);
            pendingAnim.setRepeatCount(LottieDrawable.INFINITE);
            pendingAnim.setVisibility(View.VISIBLE);
            pendingAnim.playAnimation();

            ongoingAnim.setAnimation(R.raw.loading_main);
            ongoingAnim.setRepeatMode(LottieDrawable.RESTART);
            ongoingAnim.setRepeatCount(LottieDrawable.INFINITE);
            ongoingAnim.setVisibility(View.VISIBLE);
            ongoingAnim.playAnimation();


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

                                myDonationsRetriever(0, currentOrderIds.size());

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


        if (user.getDisplayName().equals("NGO")) {
            switchBar.setVisibility(View.VISIBLE);
            switchBar.setItemActiveIndex(0);
            newdonationstxt.setVisibility(View.VISIBLE);
            accepteddonationstxt.setVisibility(View.VISIBLE);
            currentSwitchBarPos = 0;
            mainRecycler.setVisibility(View.VISIBLE);
            //   mainRecyclerLoader.setVisibility(View.VISIBLE);
            ongiongtxt.setVisibility(View.GONE);
            pendingtxt.setVisibility(View.GONE);
            requestedRecycler.setVisibility(View.GONE);
            ongoingRecycler.setVisibility(View.GONE);
            //      searchList = new ArrayList<>();

            ongoingAnim.setVisibility(View.GONE);
            ongoingAnim.cancelAnimation();
            pendingAnim.setVisibility(View.GONE);
            pendingAnim.cancelAnimation();

            mainAnim.setAnimation(R.raw.loading_main);
            mainAnim.setRepeatCount(LottieDrawable.INFINITE);
            mainAnim.setRepeatMode(LottieDrawable.RESTART);
            mainAnim.setVisibility(View.VISIBLE);
            mainAnim.playAnimation();

            getOrderIDS();

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


            nestedScrollView.getViewTreeObserver()
                    .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                        @Override
                        public void onScrollChanged() {

                            //int scrollX = nestedScrollView.getScrollX();
                            int scrollY = nestedScrollView.getScrollY();

                            onTop = scrollY == 0;


                            if (nestedScrollView.getChildAt(0).getBottom()
                                    <= (nestedScrollView.getHeight() + nestedScrollView.getScrollY() + 500)) {
                                //scroll view is at bottom
                                if (!atBottom) {
                                    atBottom = true;
                                    Log.d("tag", "onScrollChanged: reached bottom");
                                    if (orderIds.size() > 10) {
                                        Log.d("tag", "onScrollChanged: Loading");
                                        mainRecyclerLoader.setVisibility(View.VISIBLE);
                                        mainRecyclerLoader.show();
                                        loadNextPage(currentInputCode);
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

            switchBar.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public boolean onItemSelect(int i) {
                    if (i == 0 && currentSwitchBarPos != i) {
                        currentInputCode++;
                        int size = mainItems.size();
                        mainItems.clear();
                        if (mainAdapter != null)
                            mainAdapter.notifyItemRangeRemoved(0, size);
                        newdonationstxt.setTextColor(getResources().getColor(R.color.white));
                        accepteddonationstxt.setTextColor(getResources().getColor(R.color.black));
                        currentSwitchBarPos = i;

                        ongoingAnim.setVisibility(View.GONE);
                        ongoingAnim.cancelAnimation();
                        pendingAnim.setVisibility(View.GONE);
                        pendingAnim.cancelAnimation();

                        noongoingdonations.setVisibility(View.GONE);

                        mainAnim.setAnimation(R.raw.loading_main);
                        mainAnim.setRepeatMode(LottieDrawable.RESTART);
                        mainAnim.setRepeatCount(LottieDrawable.INFINITE);
                        mainAnim.setVisibility(View.VISIBLE);
                        mainAnim.playAnimation();

                        getOrderIDS();
                        ongoingRecycler.setVisibility(View.GONE);
                        mainRecycler.setVisibility(View.VISIBLE);
                    } else if (i == 1 && currentSwitchBarPos != i) {
                        currentInputCode++;
                        int size = mainItems.size();
                        mainItems.clear();
                        if (mainAdapter != null)
                            mainAdapter.notifyItemRangeRemoved(0, size);
                        newdonationstxt.setTextColor(getResources().getColor(R.color.black));
                        accepteddonationstxt.setTextColor(getResources().getColor(R.color.white));
                        currentSwitchBarPos = i;
                        nodonations.setVisibility(View.GONE);

                        mainRecyclerLoader.hide();
                        mainRecyclerLoader.setVisibility(View.GONE);

                        mainAnim.setVisibility(View.GONE);
                        mainAnim.cancelAnimation();
                        pendingAnim.setVisibility(View.GONE);
                        pendingAnim.cancelAnimation();

                        ongoingAnim.setAnimation(R.raw.loading_main);
                        ongoingAnim.setAnimation(R.raw.loading_main);
                        ongoingAnim.setRepeatCount(LottieDrawable.INFINITE);
                        ongoingAnim.setRepeatMode(LottieDrawable.RESTART);
                        ongoingAnim.setVisibility(View.VISIBLE);
                        ongoingAnim.playAnimation();

                        db.collection(Constants.ngo_fire).document(MyUID).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {

                                            ongoingItems_NGO = (ArrayList) documentSnapshot.get(Constants.ngo_ongoing_list_fire);
                                            myDonationsRetriever_NGO(0, ongoingItems_NGO.size(), currentInputCode);

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
                    return true;
                }
            });
        }

        slidingRootNav = new SlidingRootNavBuilder(this)

                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();


        DrawerAdapter adapter2;
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();
        if (user.getDisplayName().equals("Restaurant") || user.getDisplayName().equals("Individual")) {
            adapter2 = new DrawerAdapter(Arrays.asList(
                    createItemFor(0).setChecked(true),
                    createItemFor(1),
                    createItemFor(2),
                    createItemFor(3),
                    createItemFor(4)));
        } else {
            adapter2 = new DrawerAdapter(Arrays.asList(
                    createItemFor(0).setChecked(true),
                    createItemFor(2),
                    createItemFor(3),
                    createItemFor(4)));
        }
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

        adapter2.setSelected(0);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.openMenu();
            }
        });

        searchBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main_Activity.this);
                builder.setCancelable(true);
                builder.setTitle("Search Feature");
                builder.setMessage("The search feature coming soon!");
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        // Disabling back button for current activity
    }

    public void onItemSelected(int position) {


        slidingRootNav.closeMenu();
        if (user.getDisplayName().equals("Restaurant") || user.getDisplayName().equals("Individual")) {
            switch (position) {
                case 0: {
                    slidingRootNav.closeMenu();
                    break;
                }
                case 1: {
                    Intent intent = new Intent(Main_Activity.this, addClass.class);
                    intent.putExtra("From","Navigation");
                    startActivity(intent);
                    break;
                }
                case 2: {
                    Intent intent = new Intent(Main_Activity.this, profileClass.class);
                    intent.putExtra("From", "Navigation");
                    startActivity(intent);
                    break;
                }

                case 3: {
                    Intent intent = new Intent(Main_Activity.this, info.class);
                    startActivity(intent);
                    break;
                }
                case 4: {
                    logout();
                    break;
                }


            }
        } else {
            switch (position) {
                case 0: {
                    slidingRootNav.closeMenu();
                    break;
                }
                case 1: {
                    Intent intent = new Intent(Main_Activity.this, profileClass.class);
                    intent.putExtra("From", "Navigation");
                    startActivity(intent);
                    break;
                }
                case 2: {
                    Intent intent = new Intent(Main_Activity.this, info.class);
                    startActivity(intent);
                    break;
                }

                case 3: {
                    logout();
                    break;
                }


            }
        }


    }


    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withTextTint(color(R.color.white))
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

    private void logout() {
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
                Intent intent = new Intent(Main_Activity.this, LoginActivity.class);
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


    private void loadFirstPage(int code) {
        Log.d("TAG", "loadFirstPage: " + orderIds.size());

        int max = 10;
        if (orderIds.size() < 10) {
            max = orderIds.size();
        }
        db = FirebaseFirestore.getInstance();

        retriever(0, max, false, 0, code);

    }

    public void myDonationsRetriever(int i, int num) {
        if (i < num) {
            requestsCount = 0;
            rootRef.child(Constants.orderName_fire).child(currentOrderIds.get(i)).child("Info").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshotInfo) {
                    if (snapshotInfo.exists()) {

                        if (snapshotInfo.child("State").getValue().toString().equals("New")) {

                            rootRef.child(Constants.orderName_fire).child(currentOrderIds.get(i)).child(Constants.requests_fire).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists())
                                        requestsCount = (int) snapshot.getChildrenCount();
                                    else
                                        requestsCount = 0;
                                    rootRef.child(Constants.orderName_fire).child(currentOrderIds.get(i)).child(Constants.foodName_fire).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshotFood) {
                                            tempDairy = tempFruit = tempGrain = tempMeat = tempVeg = tempDishes = false;

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

                                            if (snapshotFood.hasChild(Constants.dishesName_fire)) {
                                                Log.d("TAG", "onDataChange: tempDishes=true");
                                                tempDishes = true;
                                            }


                                            requestedItems.add(new OngoingItems(requestsCount + " Requests", null, snapshotInfo.child("Total Weight").getValue().toString(), tempVeg, tempFruit, tempDairy, tempGrain, tempMeat, tempDishes, currentOrderIds.get(i), MyUID, ""));
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

                        } else if (snapshotInfo.child("State").getValue().toString().equals("Ongoing") || snapshotInfo.child("State").getValue().toString().equals("CompletedNGO") || snapshotInfo.child("State").getValue().toString().equals("CompletedRest")) {
                            rootRef.child(Constants.orderName_fire).child(currentOrderIds.get(i)).child(Constants.foodName_fire).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotFood) {
                                    tempDairy = tempFruit = tempGrain = tempMeat = tempVeg = tempDishes = false;

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
                                    if (snapshotFood.hasChild(Constants.dishesName_fire)) {
                                        Log.d("TAG", "onDataChange: tempDishes=true");

                                        tempDishes = true;
                                    }
                                    String NGOuid = snapshotInfo.child("Accepted").getValue().toString();
                                    db.collection(Constants.ngo_fire).document(
                                            NGOuid).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {

                                                        String name = documentSnapshot.get("Name").toString();
                                                        String url = documentSnapshot.get("Url").toString();
                                                        String address = documentSnapshot.get("Address").toString();
                                                        ongoingItems.add(new OngoingItems(name, url, snapshotInfo.child("Total Weight").getValue().toString(), tempVeg, tempFruit, tempDairy, tempGrain, tempMeat, tempDishes, currentOrderIds.get(i), NGOuid, address));
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
                    } else
                        myDonationsRetriever(i + 1, num);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("TAG", "onFailure: " + error.toString());
                }
            });
        } else {
            if (ongoingItems.size() > 0) {
                ongoingRecycler.setVisibility(View.VISIBLE);
                ongoingRecycler.setAdapter(new OngoingAdapter(ongoingItems, Main_Activity.this));
                ongoingAnim.cancelAnimation();
                ongoingAnim.setVisibility(View.GONE);
                noongoingdonations.setVisibility(View.GONE);
            } else {
                ongoingAnim.cancelAnimation();
                ongoingAnim.setAnimation(R.raw.empty_box);
                ongoingAnim.setRepeatCount(0);
                ongoingAnim.setVisibility(View.VISIBLE);
                ongoingAnim.playAnimation();
                ongoingRecycler.setVisibility(View.GONE);
                noongoingdonations.setVisibility(View.VISIBLE);
            }
            if (requestedItems.size() > 0) {
                requestedRecycler.setVisibility(View.VISIBLE);
                requestedRecycler.setAdapter(new RequestAdapter(requestedItems, Main_Activity.this));
                nopendingdonations.setVisibility(View.GONE);
                pendingAnim.cancelAnimation();
                pendingAnim.setVisibility(View.GONE);
            } else {
                pendingAnim.cancelAnimation();
                pendingAnim.setAnimation(R.raw.empty_box);
                pendingAnim.setRepeatCount(0);
                pendingAnim.setVisibility(View.VISIBLE);
                pendingAnim.playAnimation();
                requestedRecycler.setVisibility(View.GONE);
                nopendingdonations.setVisibility(View.VISIBLE);
            }

        }
    }


    public void myDonationsRetriever_NGO(int i, int num, int code) {
        if (i < num) {
            String id = ongoingItems_NGO.get(i);
            String userId = id.substring(0, id.indexOf("-")).trim();

            tempDairy = tempFruit = tempGrain = tempMeat = tempVeg = tempDishes = false;
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

                                tempType = (String) documentSnapshot.get(Constants.type_user);
                                tempLoaction = documentSnapshot.get("Area") + ", " + documentSnapshot.get("City");
                                dis = "-";
                                try {
                                    GeoPoint geoPoint = documentSnapshot.getGeoPoint("Location");
                                    tempLat = geoPoint.getLatitude();
                                    tempLon = geoPoint.getLongitude();


                                    if (currectLat != 0 && currentLon != 0) {
                                        float[] results = new float[1];
                                        Location.distanceBetween(tempLat, tempLon,
                                                currectLat, currentLon, results);
                                        dis = Math.round(results[0] / 100) / 10.0 + "KM";
                                    }
                                } catch (Exception e) {
                                }


                                rootRef.child(Constants.orderName_fire).child(id).child(Constants.foodName_fire).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
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
                                            if (snapshot.hasChild(Constants.dishesName_fire)) {
                                                Log.d("TAG", "onDataChange: tempDishes=true");

                                                tempDishes = true;
                                            }

                                            rootRef.child(Constants.orderName_fire).child(id).child("Info").child("Total Weight").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    tempTotalWeight = snapshot.getValue().toString();

                                                    Log.d("CHECK", "TEMP WEIGHT:" + tempTotalWeight);
                                                    if (code == currentInputCode) {
                                                        mainItems.add(new MainItem(tempLoaction, tempType, dis, tempTotalWeight, tempName, tempFruit, tempVeg, tempMeat, tempDairy, tempGrain, tempDishes, tempUrl, userId, id, tempAddress));
                                                        myDonationsRetriever_NGO(i + 1, num, code);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.d("TAG", "onFailure: " + error.toString());
                                                }
                                            });
                                        } else
                                            myDonationsRetriever_NGO(i + 1, num, code);


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


        } else {
            if (code == currentInputCode) {
                mainAdapter = new MainAdapter(Main_Activity.this, mainItems, "Accepted");
                mainRecycler.setLayoutManager(verticalLayout);
                mainRecycler.setAdapter(mainAdapter);
                mainRecycler.setItemAnimator(new DefaultItemAnimator());

                if (ongoingItems_NGO.size() > 0) {
                    ongoingAnim.setVisibility(View.GONE);
                    ongoingAnim.cancelAnimation();
                    noongoingdonations.setVisibility(View.GONE);
                } else {
                    ongoingAnim.cancelAnimation();
                    ongoingAnim.setAnimation(R.raw.empty_box);
                    ongoingAnim.setRepeatCount(0);
                    ongoingAnim.setVisibility(View.VISIBLE);
                    ongoingAnim.playAnimation();
                    noongoingdonations.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    public void retriever(int i, int max, boolean check, int intitialPos, int code) {
        if (i < max) {
            String id = orderIds.get(i).trim();
            String userId = id.substring(0, id.indexOf("-")).trim();

            tempDairy = tempFruit = tempGrain = tempMeat = tempVeg = tempDishes = false;
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
                                tempType = (String) documentSnapshot.get(Constants.type_user);
                                tempLoaction = documentSnapshot.get("Area") + ", " + documentSnapshot.get("City");
                                dis = "-";
                                try {
                                    GeoPoint geoPoint = documentSnapshot.getGeoPoint("Location");
                                    tempLat = geoPoint.getLatitude();
                                    tempLon = geoPoint.getLongitude();


                                    if (currectLat != 0 && currentLon != 0) {
                                        float[] results = new float[1];
                                        Location.distanceBetween(tempLat, tempLon,
                                                currectLat, currentLon, results);
                                        dis = Math.round(results[0] / 100) / 10.0 + "KM";
                                    }
                                } catch (Exception e) {
                                }


                                rootRef.child(Constants.orderName_fire).child(id).child(Constants.foodName_fire).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
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
                                            if (snapshot.hasChild(Constants.dishesName_fire)) {
                                                Log.d("TAG", "onDataChange: tempDishes=true");

                                                tempDishes = true;
                                            }

                                            rootRef.child(Constants.orderName_fire).child(id).child("Info").child("Total Weight").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    tempTotalWeight = snapshot.getValue().toString();

                                                    if (code == currentInputCode) {
                                                        mainItems.add(new MainItem(tempLoaction, tempType, dis, tempTotalWeight, tempName, tempFruit, tempVeg, tempMeat, tempDairy, tempGrain, tempDishes, tempUrl, userId, id, tempAddress));
                                                        retriever(i + 1, max, check, intitialPos, code);
                                                    }

                                                }


                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.d("TAG", "onFailure: " + error.toString());
                                                }
                                            });
                                        } else
                                            retriever(i + 1, max, check, intitialPos, code);


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


        } else {
            if (code == currentInputCode) {
                if (!check) {


                    mainAdapter = new MainAdapter(Main_Activity.this, mainItems, "New");
                    mainRecycler.setLayoutManager(verticalLayout);
                    mainRecycler.setAdapter(mainAdapter);
                    mainRecycler.setItemAnimator(new DefaultItemAnimator());
                    mainAnim.setVisibility(View.GONE);
                    mainAnim.cancelAnimation();

                } else {
                    mainAdapter.notifyItemRangeChanged(intitialPos, mainItems.size() - 1);
                    mainRecyclerLoader.setVisibility(View.GONE);
                    mainRecyclerLoader.hide();

                }
            }
        }
    }

    private void loadNextPage(int code) {


        if (mainItems != null && mainAdapter != null && mainItems.size() < orderIds.size()) {
            Log.d("TAG", "loadNextPage: " + currentPage);
            //List<MainItem> newItems = new ArrayList<>();


            int intitialPos = mainItems.size() - 1;
            int listIntitialPos = currentPage * 10;
            int listFinalPos = ((currentPage + 1) * 10 - 1);


            if (currentPage == (TOTAL_PAGES - 1)) {

                listFinalPos = orderIds.size() - 1;

            }
            currentPage += 1;
            retriever(intitialPos, listFinalPos, true, intitialPos, code);
        } else {
            mainRecyclerLoader.hide();
            mainRecyclerLoader.setVisibility(View.GONE);

        }
    }

    private void getOrderIDS() {
        orderIds = new ArrayList<>();
        db.collection(Constants.orderName_fire).document(Constants.order_list_fire)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    orderIds = (ArrayList) documentSnapshot.get(Constants.order_list_field);
                    if (orderIds != null && orderIds.size() > 0) {
                        TOTAL_PAGES = (int) Math.ceil(orderIds.size() / (10.0f));
                        loadFirstPage(currentInputCode);
                        nodonations.setVisibility(View.GONE);
                    } else {
                        mainRecyclerLoader.hide();
                        mainRecyclerLoader.setVisibility(View.GONE);
                        mainAnim.cancelAnimation();
                        mainAnim.setAnimation(R.raw.empty_box);
                        mainAnim.setRepeatCount(0);
                        mainAnim.setVisibility(View.VISIBLE);
                        mainAnim.playAnimation();
                        nodonations.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void getDeviceLocation() {
        Log.d("TAG", "getDeviceLocation: getting the devices current location");

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {


            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "onComplete: found location");
                        Location currentLocation = (Location) task.getResult();

                        if (currentLocation != null) {
                            currectLat = currentLocation.getLatitude();
                            currentLon = currentLocation.getLongitude();
                        }

                    } else {
                        Log.d("TAG", "onComplete: current location is null");
                        Toast.makeText(Main_Activity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (SecurityException e) {
            Log.e("TAG", "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
