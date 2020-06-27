package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class displayOrder extends AppCompatActivity {

    private TextView nameText;
    private ImageButton backButton;
    private TextView addressText;
    private SmoothBottomBar mSmoothBottomBar;
    private TextView categoryName;
    private TextView categoryWeight;
    private RecyclerView displayOrderRecycler;
    private displayOrderAdapter mDisplayOrderAdapter;
    private LinearLayoutManager verticalLayout;
    private TextView totalWeightText;
    private ConstraintLayout requestButton;

    private LottieAnimationView displayAnimation;
    private LottieAnimationView categoryLoadingAnimation;
    private LottieAnimationView loadingAnimation1;
    private LottieAnimationView loadingAnimation2;
    private LottieAnimationView loadingAnimation3;

    private String uid;
    private String orderID;
    private String  name;
    private String address;
    private boolean isFruits;
    private boolean isVeggies;
    private boolean isMeat;
    private boolean isGrains;
    //private boolean isDishes;
    private boolean isDairy;


    private DatabaseReference mDatabaseReference;
    private FirebaseFirestore db;

    private ArrayList<categoryItem> fruitsList;
    private ArrayList<categoryItem> veggiesList;
    private ArrayList<categoryItem> grainsList;
    private ArrayList<categoryItem> dairyList;
    private ArrayList<categoryItem> meatList;

    private float totalWeight;
    private float fruitsWeight;
    private float grainsWeight;
    private float dairyWeight;
    private float veggiesWeight;
    private float meatWeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_order);


        Intent intent = getIntent();
        uid = intent.getStringExtra(Constants.uid_intent);
        orderID = intent.getStringExtra(Constants.orderId_intent);
        name = intent.getStringExtra(Constants.name_intent);
        isFruits = intent.getBooleanExtra(Constants.isFruits_intent, false);
        isDairy = intent.getBooleanExtra(Constants.isDairy_intent, false);
        isGrains = intent.getBooleanExtra(Constants.isGrains_intent, false);
        isVeggies = intent.getBooleanExtra(Constants.isVeggies_intent, false);
        isMeat = intent.getBooleanExtra(Constants.isMeat_intent, false);
        address = intent.getStringExtra(Constants.address_intent);



        findViewsById();
        setOnClickOnListeners();



    }

    private void findViewsById()
    {
        displayAnimation = findViewById(R.id.displayLottieAnimation);
        categoryLoadingAnimation = findViewById(R.id.categoryLoadingAnimation);
        loadingAnimation2 = findViewById(R.id.loadingTextAnimation2);
        loadingAnimation1 = findViewById(R.id.loadingTextAnimation1);
        loadingAnimation3 = findViewById(R.id.loadingTextAnimation3);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screen_width = displayMetrics.widthPixels;

        displayAnimation.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                displayAnimation.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                displayAnimation.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }



            int newDimensions = (int) (screen_width*0.28);

            displayAnimation.getLayoutParams().width = newDimensions;


            displayAnimation.getLayoutParams().height = newDimensions;

            displayAnimation.playAnimation();


        }
    });

        categoryLoadingAnimation.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    categoryLoadingAnimation.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    categoryLoadingAnimation.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }



                int newDimensions = (int) (screen_width*0.472);

                categoryLoadingAnimation.getLayoutParams().width = newDimensions;


                categoryLoadingAnimation.getLayoutParams().height = newDimensions;

                categoryLoadingAnimation.playAnimation();


            }
        });

        loadingAnimation1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    loadingAnimation1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    loadingAnimation1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }



                int newDimensions = (int) (screen_width*0.139);

                loadingAnimation1.getLayoutParams().width = newDimensions;


                loadingAnimation1.getLayoutParams().height = newDimensions;

                loadingAnimation1.playAnimation();


            }
        });

        loadingAnimation2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    loadingAnimation2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    loadingAnimation2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }



                int newDimensions = (int) (screen_width*0.139);

                loadingAnimation2.getLayoutParams().width = newDimensions;


                loadingAnimation2.getLayoutParams().height = newDimensions;

                loadingAnimation2.playAnimation();


            }
        });


        loadingAnimation3.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    loadingAnimation3.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    loadingAnimation3.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }



                int newDimensions = (int) (screen_width*0.024);

                loadingAnimation3.getLayoutParams().width = newDimensions;


                loadingAnimation3.getLayoutParams().height = newDimensions;

                loadingAnimation3.playAnimation();


            }
        });



        nameText = findViewById(R.id.nameText);
        nameText.setText(name);
        backButton = findViewById(R.id.backButton);
        addressText = findViewById(R.id.addressText);
        addressText.setText(address);
        mSmoothBottomBar = findViewById(R.id.bubbleBottomSheetBar);
        categoryName = findViewById(R.id.categoryHeading);
        categoryWeight = findViewById(R.id.categoryWeight);
        displayOrderRecycler = findViewById(R.id.categoryRecycler);
        verticalLayout = new LinearLayoutManager(
                displayOrder.this,
                LinearLayoutManager.VERTICAL,
                false);

        totalWeightText = findViewById(R.id.totalWeight);
        requestButton = findViewById(R.id.requestButtonLayout);

        if (isFruits)
        {
            mSmoothBottomBar.setItemActiveIndex(0);
        }
        else if (isVeggies)
        {
            mSmoothBottomBar.setItemActiveIndex(1);

        }

        else if (isDairy)
        {
            mSmoothBottomBar.setItemActiveIndex(2);

        }

        else if (isGrains)
        {
            mSmoothBottomBar.setItemActiveIndex(3);

        }

        else if (isMeat)
        {
            mSmoothBottomBar.setItemActiveIndex(4);
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        fruitsList = new ArrayList<>();
        veggiesList = new ArrayList<>();
        grainsList = new ArrayList<>();
        meatList = new ArrayList<>();
        dairyList = new ArrayList<>();

        totalWeight = 0;
        fruitsWeight =0;
        veggiesWeight=0;
        meatWeight=0;
        grainsWeight=0;
        dairyWeight=0;

        GetBackgroundInfo backgroundInfo = new GetBackgroundInfo();
        backgroundInfo.execute();





    }

    private void setOnClickOnListeners()
    {
        PushDownAnim.setPushDownAnimTo(backButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {





                    }


    });


        PushDownAnim.setPushDownAnimTo(requestButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {





                    }


                });

        mSmoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {

                if (i==0)
                {
                    categoryName.setText("Fruits Summary");
                    if ( isFruits && fruitsList.size()>0 && fruitsWeight>0.0f) {

                        mDisplayOrderAdapter = new displayOrderAdapter(fruitsList);
                        displayOrderRecycler.setAdapter(mDisplayOrderAdapter);
                        categoryWeight.setText(Float.toString(fruitsWeight)+"kg");
                    }
                    else {
                        categoryWeight.setText("0.0kg");

                    }
                }

                else if (i==1)
                {

                    categoryName.setText("Vegetables Summary");
                    if ( isVeggies && veggiesList.size()>0 && veggiesWeight>0.0f) {

                        mDisplayOrderAdapter = new displayOrderAdapter(veggiesList);
                        displayOrderRecycler.setAdapter(mDisplayOrderAdapter);
                        categoryWeight.setText(Float.toString(veggiesWeight)+"kg");
                    }
                    else {
                        categoryWeight.setText("0.0kg");

                    }
                }

                else if (i==2 )
                {

                    categoryName.setText("Dairy Summary");
                    if (isDairy && dairyList.size()>0 && dairyWeight>0.0f) {

                        mDisplayOrderAdapter = new displayOrderAdapter(dairyList);
                        displayOrderRecycler.setAdapter(mDisplayOrderAdapter);
                        categoryWeight.setText(Float.toString(dairyWeight)+"kg");
                    }
                    else {
                        categoryWeight.setText("0.0kg");

                    }
                }

                else if (i==3 )
                {

                    categoryName.setText("Grains Summary");
                    if (isGrains && grainsList.size()>0 && grainsWeight>0.0f) {

                        mDisplayOrderAdapter = new displayOrderAdapter(grainsList);
                        displayOrderRecycler.setAdapter(mDisplayOrderAdapter);
                        categoryWeight.setText(Float.toString(grainsWeight)+"kg");
                    }
                    else {
                        categoryWeight.setText("0.0kg");

                    }
                }

                else if (i==4)
                {
                    categoryName.setText("Meat Summary");
                    if (isMeat && meatList.size()>0 && meatWeight>0.0f) {

                        mDisplayOrderAdapter = new displayOrderAdapter(meatList);
                        displayOrderRecycler.setAdapter(mDisplayOrderAdapter);
                        categoryWeight.setText(Float.toString(meatWeight)+"kg");
                    }
                    else {
                        categoryWeight.setText("0.0kg");

                    }
                }

                return false;
            }
        });

    }

    private float getTotalWeight(ArrayList<categoryItem> list)
    {
        float weight=0.0f;

        for (int i=0;i<list.size();i++)
        {
            weight+=list.get(i).getFoodWeight();
        }


        return weight;

    }

    private class GetBackgroundInfo extends AsyncTask<Void, Integer, Boolean> {

        //private final WeakReference<Activity> weakActivity;

        GetBackgroundInfo() {
            //this.weakActivity = new WeakReference<>(myActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TAG", "onPreExecute: task started");


        }

        protected Boolean doInBackground(Void... voids) {
            // code that will run in the background

            // Activity activity = weakActivity.get();

            Log.d("TAG", "doInBackground: task running");

            mDatabaseReference.child(Constants.orderName_fire).child(orderID).child("Food").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    GenericTypeIndicator<ArrayList<categoryItem>> t = new GenericTypeIndicator<ArrayList<categoryItem>>() {};


                    if (isFruits && snapshot.hasChild(Constants.fruitName_fire))
                    {

                        fruitsList = snapshot.child(Constants.fruitName_fire).child("Items").getValue(t);
                        if (fruitsList != null &&fruitsList.size()>0) {
                            fruitsWeight = getTotalWeight(fruitsList);
                        }



                    }

                    if (isVeggies && snapshot.hasChild(Constants.vegName_fire))
                    {

                        veggiesList = snapshot.child(Constants.vegName_fire).child("Items").getValue(t);
                        if (veggiesList != null &&veggiesList.size()>0) {
                            veggiesWeight = getTotalWeight(veggiesList);
                        }

                    }

                    if (isDairy && snapshot.hasChild(Constants.dairyName_fire))
                    {

                        dairyList = snapshot.child(Constants.dairyName_fire).child("Items").getValue(t);
                        if (dairyList != null &&dairyList.size()>0) {
                            dairyWeight = getTotalWeight(dairyList);
                        }

                    }

                    if (isGrains && snapshot.hasChild(Constants.grainsName_fire))
                    {

                        grainsList = snapshot.child(Constants.grainsName_fire).child("Items").getValue(t);
                        if (grainsList != null &&grainsList.size()>0) {
                            grainsWeight = getTotalWeight(grainsList);
                        }

                    }

                    if (isMeat && snapshot.hasChild(Constants.meatName_fire))
                    {

                        meatList = snapshot.child(Constants.meatName_fire).child("Items").getValue(t);
                        if (meatList != null &&meatList.size()>0) {
                            meatWeight = getTotalWeight(meatList);
                        }

                    }

                    onPostExecute(true);



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    onPostExecute(false);


                }
            });





            return null;
        }


        @Override
        protected void onPostExecute(Boolean done) {
            super.onPostExecute(done);

            if (done!=null)
            {
                Log.d("TAG", "onPostExecute: task completed : "+done);

                if (done)
                {
                    totalWeight = fruitsWeight+meatWeight+veggiesWeight+dairyWeight+grainsWeight;
                    float currentWeight=0.0f;
                    String currentCategory ="";
                    if (isFruits && fruitsList!=null && fruitsList.size()>0)
                    {

                        mDisplayOrderAdapter = new displayOrderAdapter(fruitsList);
                        mSmoothBottomBar.setItemActiveIndex(0);
                        currentCategory = "Fruits";
                        currentWeight = fruitsWeight;
                    }
                    else if (isVeggies && veggiesList!=null && veggiesList.size()>0)
                    {
                        mDisplayOrderAdapter = new displayOrderAdapter(veggiesList);
                        mSmoothBottomBar.setItemActiveIndex(1);
                        currentCategory = "Vegetables";
                        currentWeight = veggiesWeight;

                    }

                    else if (isDairy && dairyList!=null && dairyList.size()>0)
                    {
                        mDisplayOrderAdapter = new displayOrderAdapter(dairyList);
                        mSmoothBottomBar.setItemActiveIndex(2);
                        currentCategory = "Dairy";
                        currentWeight = dairyWeight;

                    }

                    else if (isGrains && grainsList!=null && grainsList.size()>0)
                    {
                        mDisplayOrderAdapter = new displayOrderAdapter(grainsList);
                        mSmoothBottomBar.setItemActiveIndex(3);
                        currentCategory = "Grains";
                        currentWeight = grainsWeight;

                    }

                    else if (isMeat && meatList!=null && meatList.size()>0)
                    {
                        mDisplayOrderAdapter = new displayOrderAdapter(meatList);
                        mSmoothBottomBar.setItemActiveIndex(4);
                        currentCategory = "Meat";
                        currentWeight = meatWeight;

                    }

                    Log.d("TAG", "onPostExecute: ");

                    loadingAnimation1.setVisibility(View.INVISIBLE);
                    loadingAnimation1.cancelAnimation();
                    categoryName.setText(currentCategory+" Summary");
                    loadingAnimation2.setVisibility(View.INVISIBLE);
                    loadingAnimation2.cancelAnimation();
                    categoryWeight.setText(Float.toString(currentWeight)+"kg");

                    loadingAnimation3.setVisibility(View.INVISIBLE);
                    loadingAnimation3.cancelAnimation();
                    totalWeightText.setText(Float.toString(totalWeight)+"kg");

                    displayOrderRecycler.setLayoutManager(verticalLayout);
                    displayOrderRecycler.setItemAnimator(new DefaultItemAnimator());

                    displayOrderRecycler.setAdapter(mDisplayOrderAdapter);

                    categoryLoadingAnimation.setVisibility(View.INVISIBLE);
                    categoryLoadingAnimation.cancelAnimation();

                }

                else {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(displayOrder.this);
                    builder.setTitle("Order Retrieval Error");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(displayOrder.this, Main_Activity.class);
                            dialogInterface.dismiss();
                            startActivity(intent);

                        }
                    });

                    builder.setMessage("Something went wrong while retrieving the Donation, please Try Again");

                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();

                }
            }


        }


    }

}

