package restaurantapp.randc.com.restaurant_app;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class addClass extends AppCompatActivity {

    private RecyclerView selectCategory;

    private RecyclerView bottomRecycler;
    private Button clearButton;
    private PieChart mPieChart;
    private ImageButton backbutton;
    private PieDataSet mPieDataSet;
    private PieData mPieData;
    private LinearLayout percentLayout;
    private List<PieEntry> mEntries;
    private RelativeLayout recentLayout;
    private List<CategorySelectItem> selectList;
    private LinearLayoutManager HorizontalLayout;
    private RecyclerView.LayoutManager RecyclerViewLayoutManager;
    private CategorySelectAdapter mCategoryItemAdapter;

    private LottieAnimationView emptyPiAnim;
    private TextView emptyPiText;
    private LottieAnimationView emptyBottomAnim;
    private TextView emptyBottomText;


    private float totalFruitWeight = 0.0f;

    private ArrayList<categoryItem> fruitList;

    private float totalVeggesWeight = 0.0f;

    private ArrayList<categoryItem> veggiesList;


    private float totalMeatWeight = 0.0f;

    private ArrayList<categoryItem> meatList;


    private float totalGrainsWeight = 0.0f;

    private ArrayList<categoryItem> grainsList;


    private float totalDairyWeight = 0.0f;

    private ArrayList<categoryItem> dairyList;

    private float totalDishesWeight = 0;
    private ArrayList<categoryItem> dishesList;

    private ProgressDialog progressdialog;

    private TextView dairyPercent;
    private TextView meatPercent;
    private TextView fruitPercent;
    private TextView vegPercent;
    private TextView dishPercent;
    private TextView grainPercent;


    private TextView donateButton;
    private ArrayList<Boolean> orderNum;
    private LinearLayoutManager verticalLayout;

    private donationBottomAdapter mDonationBottomAdapter;
    private DatabaseReference mDatabase;
    private String orderId;

    private String restName;
    private String restAddress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        emptyBottomAnim = findViewById(R.id.emptyDonationAnim);
        emptyBottomText = findViewById(R.id.emptyDonationText);
        emptyPiAnim = findViewById(R.id.emptyChartAnim);
        emptyPiText = findViewById(R.id.chartAnimText);
        emptyBottomAnim.setVisibility(View.GONE);
        emptyBottomText.setVisibility(View.GONE);
        emptyPiAnim.setVisibility(View.GONE);
        emptyPiText.setVisibility(View.GONE);
        percentLayout = findViewById(R.id.percentLayout);
        percentLayout.setVisibility(View.VISIBLE);
        progressdialog = new ProgressDialog(addClass.this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        selectCategory = findViewById(R.id.categorySelectView);
        mPieChart = findViewById(R.id.categoryChart);
        clearButton = findViewById(R.id.clearButton1);
        backbutton = findViewById(R.id.backButton);

        recentLayout = findViewById(R.id.recentLayout);
        dairyPercent = findViewById(R.id.dairyPercentText);
        meatPercent = findViewById(R.id.meatPercentText);

        grainPercent = findViewById(R.id.grainsPercentText);

        fruitPercent = findViewById(R.id.fruitPercentText);

        vegPercent = findViewById(R.id.veggiesPercentText);

        dishPercent = findViewById(R.id.dishesPercentText);

        donateButton = findViewById(R.id.donateTextView);


        selectList = new ArrayList<>();

        fruitList = new ArrayList<>();
        veggiesList = new ArrayList<>();
        grainsList = new ArrayList<>();
        dairyList = new ArrayList<>();
        meatList = new ArrayList<>();
        dishesList = new ArrayList<>();

        setOnClickListeners();


        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(addClass.this);
                builder.setCancelable(true);
                builder.setTitle("Clear Donation");
                builder.setMessage("Are sure you want to clear donation?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(Constants.fruitPref);
                        editor.remove(Constants.fruitPref + "weights");
                        editor.remove(Constants.dishesPref);
                        editor.remove(Constants.dishesPref + "weights");
                        editor.remove(Constants.vegetablePref);
                        editor.remove(Constants.vegetablePref + "weights");
                        editor.remove(Constants.meatPref);
                        editor.remove(Constants.meatPref + "weights");
                        editor.remove(Constants.DairyPref);
                        editor.remove(Constants.DairyPref + "weights");
                        editor.remove(Constants.grainsPref);
                        editor.remove(Constants.grainsPref + "weights");
                        editor.remove(Constants.dishesPref);
                        editor.remove(Constants.dishesPref + "weights");
                        editor.apply();


                        mEntries.clear();
                        if (fruitList != null) {
                            fruitList.clear();
                        }
                        totalFruitWeight = 0.0f;
                        if (veggiesList != null) {
                            veggiesList.clear();
                        }
                        totalVeggesWeight = 0.0f;
                        if (meatList != null) {
                            meatList.clear();
                        }
                        totalMeatWeight = 0.0f;
                        if (grainsList != null) {
                            grainsList.clear();
                        }
                        totalGrainsWeight = 0.0f;
                        if (dishesList != null) {
                            dishesList.clear();
                        }
                        totalDishesWeight = 0.0f;
                        if (dairyList != null) {
                            dairyList.clear();
                        }
                        totalDairyWeight = 0.0f;

                        createPie();
                        setUpBottomDonation();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        selectList.add(new CategorySelectItem("Fruits", R.drawable.icons8_fruit_category, R.drawable.fruit_category_bg));
        selectList.add(new CategorySelectItem("Veggies", R.drawable.icons8_category_veg, R.drawable.veg_category_bg));
        selectList.add(new CategorySelectItem("Dishes", R.drawable.icons8_category_dishes, R.drawable.dishes_cateogry_bg));
        selectList.add(new CategorySelectItem("Meat", R.drawable.icons8_category_meat, R.drawable.meat_category_bg));
        selectList.add(new CategorySelectItem("Grains", R.drawable.icons8_category_grains, R.drawable.grains_category_bg));
        selectList.add(new CategorySelectItem("Dairy", R.drawable.icons8_category_dairy, R.drawable.dairy_category_bg));

        HorizontalLayout
                = new LinearLayoutManager(
                addClass.this,
                LinearLayoutManager.HORIZONTAL,
                false);

        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getApplicationContext());

        mCategoryItemAdapter = new CategorySelectAdapter(selectList, addClass.this);
        selectCategory.setLayoutManager(HorizontalLayout);
        selectCategory.setAdapter(mCategoryItemAdapter);

        //selectCategory.smoothScrollToPosition(mCategoryItemAdapter.getItemCount());

        if(getIntent().getStringExtra("From").equals("Navigation")) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    smoothScroll(selectCategory, mCategoryItemAdapter.getItemCount(), 6000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            selectCategory.smoothScrollToPosition(0);
                        }
                    }, 5000);
                }
            }, 1000);
        }

        mEntries = new ArrayList<>();

        retrieveValuesFromMem();
        createPie();


        bottomRecycler = findViewById(R.id.bottomRecycler);

        setUpBottomDonation();

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addClass.this, Main_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Disabling back button for current activity
    }

    private void setOnClickListeners() {

        recentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId, MODE_PRIVATE);

                if (sharedPreferences.getBoolean("RecentPresent", false)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.fruitPref, sharedPreferences.getString(Constants.recentfruitPref, ""));
                    editor.putString(Constants.fruitPref + "weights", sharedPreferences.getString(Constants.recentfruitPref + "weights", ""));
                    editor.putString(Constants.dishesPref, sharedPreferences.getString(Constants.recentdishesPref, ""));
                    editor.putString(Constants.dishesPref + "weights", sharedPreferences.getString(Constants.recentdishesPref + "weights", ""));
                    editor.putString(Constants.DairyPref, sharedPreferences.getString(Constants.recentDairyPref, ""));
                    editor.putString(Constants.DairyPref + "weights", sharedPreferences.getString(Constants.recentDairyPref + "weights", ""));
                    editor.putString(Constants.grainsPref, sharedPreferences.getString(Constants.recentgrainsPref, ""));
                    editor.putString(Constants.grainsPref + "weights", sharedPreferences.getString(Constants.recentgrainsPref + "weights", ""));
                    editor.putString(Constants.meatPref, sharedPreferences.getString(Constants.recentmeatPref, ""));
                    editor.putString(Constants.meatPref + "weights", sharedPreferences.getString(Constants.recentmeatPref + "weights", ""));
                    editor.putString(Constants.vegetablePref, sharedPreferences.getString(Constants.recentvegetablePref, ""));
                    editor.putString(Constants.vegetablePref + "weights", sharedPreferences.getString(Constants.recentvegetablePref + "weights", ""));
                    editor.apply();

                    totalDairyWeight = 0.0f;
                    totalDishesWeight = 0.0f;
                    totalFruitWeight = 0.0f;
                    totalGrainsWeight = 0.0f;
                    totalMeatWeight = 0.0f;
                    totalVeggesWeight = 0.0f;

                    retrieveValuesFromMem();
                    setUpBottomDonation();
                    createPie();
                } else {

                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(addClass.this);
                    builder.setTitle("No recent donations");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    builder.setMessage("This feature will be available after you make your first donation!");

                    android.app.AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                }
            }
        });

        PushDownAnim.setPushDownAnimTo(donateButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user.isEmailVerified()) {

                            editor.putString(Constants.recentfruitPref, sharedPreferences.getString(Constants.fruitPref, ""));
                            editor.putString(Constants.recentfruitPref + "weights", sharedPreferences.getString(Constants.fruitPref + "weights", ""));
                            editor.putString(Constants.recentdishesPref, sharedPreferences.getString(Constants.dishesPref, ""));
                            editor.putString(Constants.recentdishesPref + "weights", sharedPreferences.getString(Constants.dishesPref + "weights", ""));
                            editor.putString(Constants.recentDairyPref, sharedPreferences.getString(Constants.DairyPref, ""));
                            editor.putString(Constants.recentDairyPref + "weights", sharedPreferences.getString(Constants.DairyPref + "weights", ""));
                            editor.putString(Constants.recentgrainsPref, sharedPreferences.getString(Constants.grainsPref, ""));
                            editor.putString(Constants.recentgrainsPref + "weights", sharedPreferences.getString(Constants.grainsPref + "weights", ""));
                            editor.putString(Constants.recentmeatPref, sharedPreferences.getString(Constants.meatPref, ""));
                            editor.putString(Constants.recentmeatPref + "weights", sharedPreferences.getString(Constants.meatPref + "weights", ""));
                            editor.putString(Constants.recentvegetablePref, sharedPreferences.getString(Constants.vegetablePref, ""));
                            editor.putString(Constants.recentvegetablePref + "weights", sharedPreferences.getString(Constants.vegetablePref + "weights", ""));
                            editor.putBoolean("RecentPresent", true);
                            editor.remove(Constants.fruitPref);
                            editor.remove(Constants.fruitPref + "weights");
                            editor.remove(Constants.dishesPref);
                            editor.remove(Constants.dishesPref + "weights");
                            editor.remove(Constants.vegetablePref);
                            editor.remove(Constants.vegetablePref + "weights");
                            editor.remove(Constants.meatPref);
                            editor.remove(Constants.meatPref + "weights");
                            editor.remove(Constants.DairyPref);
                            editor.remove(Constants.DairyPref + "weights");
                            editor.remove(Constants.grainsPref);
                            editor.remove(Constants.grainsPref + "weights");

                            editor.apply();
                            progressdialog.setMessage("Adding Donation...");
                            progressdialog.setCanceledOnTouchOutside(false);
                            progressdialog.show();
                            String uid = user.getUid();
                            addToFirebase(uid);


                        } else {

                            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(addClass.this);
                            builder.setTitle("Oh! Looks like you have not verified your email.");
                            builder.setNegativeButton("Resend Email", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("TAG", "Email sent.");
                                                        Toast.makeText(addClass.this, "Verification email sent", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            });
                            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });


                            builder.setMessage("Please verify your email using the link sent to " + user.getEmail() + " during registration\n\nIf you have not received the mail, please click on Resend Email.");
                            android.app.AlertDialog alertDialog = builder.create();

                            alertDialog.show();
                        }


                    }
                });
    }

    private void retrieveValuesFromMem() {


        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId, MODE_PRIVATE);
        Gson gson = new Gson();
        List<categoryItem> mmItems;
        ArrayList<categoryItem> mItems;


        //FRUITS
        String retrievedCategoryItems = sharedPreferences.getString(Constants.fruitPref, "");
        if (!retrievedCategoryItems.equals("")) {

            fruitList = new ArrayList<>();
            fruitList.addAll(Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class)));

            for (int i = 0; i < fruitList.size(); i++) {
                totalFruitWeight += fruitList.get(i).getFoodWeight();
            }

            Log.d("TAG", "retrieveValuesFromMem: " + fruitList.toString());

        } else {
            fruitList = null;
            totalFruitWeight = 0;
        }


        //Veggies
        retrievedCategoryItems = sharedPreferences.getString(Constants.vegetablePref, "");
        if (!retrievedCategoryItems.equals("")) {

            veggiesList = new ArrayList<>();
            Log.d("TAG", "retrieveValuesFromMem: " + retrievedCategoryItems);
            veggiesList.addAll(Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class)));
            Log.d("TAG", "retrieveValuesFromMem: entered " + veggiesList.toString());

            for (int i = 0; i < veggiesList.size(); i++) {
                totalVeggesWeight += veggiesList.get(i).getFoodWeight();
            }


        } else {
            veggiesList = null;
            totalVeggesWeight = 0;
        }


        //Meat
        retrievedCategoryItems = sharedPreferences.getString(Constants.meatPref, "");
        if (!retrievedCategoryItems.equals("")) {
            meatList = new ArrayList<>();
            meatList.addAll(Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class)));

            for (int i = 0; i < meatList.size(); i++) {
                totalMeatWeight += meatList.get(i).getFoodWeight();
            }

        } else {
            meatList = null;
            totalMeatWeight = 0;
        }

        //Grains
        retrievedCategoryItems = sharedPreferences.getString(Constants.grainsPref, "");
        if (!retrievedCategoryItems.equals("")) {
            grainsList = new ArrayList<>();
            grainsList.addAll(Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class)));

            for (int i = 0; i < grainsList.size(); i++) {
                totalGrainsWeight += grainsList.get(i).getFoodWeight();
            }

        } else {
            grainsList = null;
            totalGrainsWeight = 0;
        }

        //Grains
        retrievedCategoryItems = sharedPreferences.getString(Constants.DairyPref, "");
        if (!retrievedCategoryItems.equals("")) {
            dairyList = new ArrayList<>();
            dairyList.addAll(Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class)));

            for (int i = 0; i < dairyList.size(); i++) {
                totalDairyWeight += dairyList.get(i).getFoodWeight();
            }

        } else {
            dairyList = null;
            totalDairyWeight = 0;
        }

        //Dishes
        retrievedCategoryItems = sharedPreferences.getString(Constants.dishesPref, "");
        if (!retrievedCategoryItems.equals("")) {
            dishesList = new ArrayList<>();
            dishesList.addAll(Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class)));

            for (int i = 0; i < dishesList.size(); i++) {
                totalDishesWeight += dishesList.get(i).getFoodWeight();
            }

        } else {
            dishesList = null;
            totalDishesWeight = 0.0f;
        }


    }


    private void createPie() {
        if (mEntries != null) {
            mEntries.clear();
        }

        if ((totalVeggesWeight != 0.0f) || (totalMeatWeight != 0.0f) || (totalGrainsWeight != 0.0f) || (totalDishesWeight != 0.0f) || (totalFruitWeight != 0.0f) || (totalDairyWeight != 0.0f)) {

            mPieChart.setVisibility(View.VISIBLE);

            if (emptyPiAnim.getVisibility() == View.VISIBLE) {
                emptyPiAnim.setVisibility(View.GONE);
                emptyPiAnim.cancelAnimation();
                emptyPiText.setVisibility(View.GONE);
                percentLayout.setVisibility(View.VISIBLE);
            }

            if (emptyBottomAnim.getVisibility() == View.VISIBLE) {
                emptyBottomAnim.setVisibility(View.GONE);
                emptyBottomAnim.cancelAnimation();
                emptyBottomText.setVisibility(View.GONE);
            }

            mEntries.add(new PieEntry(totalFruitWeight, 0));
            mEntries.add(new PieEntry(totalVeggesWeight, 1));
            mEntries.add(new PieEntry(totalDishesWeight, 2));
            mEntries.add(new PieEntry(totalMeatWeight, 3));
            mEntries.add(new PieEntry(totalGrainsWeight, 4));
            mEntries.add(new PieEntry(totalDairyWeight, 5));


            mPieDataSet = new PieDataSet(mEntries, "");


            mPieChart.setDrawHoleEnabled(true);
            mPieChart.setHoleColor(Color.TRANSPARENT);
            mPieChart.getLegend().setEnabled(false);
            mPieChart.getDescription().setEnabled(false);


            mPieChart.setHoleRadius(85f);

            mPieChart.setDrawEntryLabels(false);
            mPieChart.setDrawCenterText(false);

            mPieChart.setHighlightPerTapEnabled(false);

            mPieChart.setRotationAngle(0);
            // enable rotation of the chart by touch
            mPieChart.setRotationEnabled(true);

            ArrayList<Integer> colors = new ArrayList<>();

            colors.add(Color.parseColor("#FFE101"));
            colors.add(Color.parseColor("#3DC073"));
            colors.add(Color.parseColor("#C0C0C0"));
            colors.add(Color.parseColor("#FF2B5B"));
            colors.add(Color.parseColor("#796DFF"));
            colors.add(Color.parseColor("#02BDE0"));


            mPieDataSet.setDrawValues(false);
            mPieDataSet.setDrawValues(false);
            mPieDataSet.setSliceSpace(3f);


            mPieDataSet.setColors(colors);

            mPieData = new PieData(mPieDataSet);
            mPieData.setValueFormatter(new PercentFormatter(mPieChart));


            mPieChart.animateY(1400, Easing.EaseInOutQuad);

            mPieChart.setData(mPieData);
            mPieChart.highlightValues(null);
            mPieChart.invalidate();

        } else {

            mPieChart.setVisibility(View.GONE);

            emptyPiAnim.setVisibility(View.VISIBLE);
            emptyPiAnim.playAnimation();
            emptyPiText.setVisibility(View.VISIBLE);
            emptyBottomAnim.setVisibility(View.VISIBLE);
            emptyBottomAnim.playAnimation();
            emptyBottomText.setVisibility(View.VISIBLE);

            percentLayout.setVisibility(View.GONE);

        }

        setUpPieText();
    }

    private void setUpPieText() {
        float total = totalDairyWeight + totalFruitWeight + totalGrainsWeight + totalMeatWeight + totalVeggesWeight + totalDishesWeight;
        float dairyPercentage = (totalDairyWeight / total) * 100.0f;
        float meatPercentage = (totalMeatWeight / total) * 100.0f;
        float fruitPercentage = (totalFruitWeight / total) * 100.0f;
        float vegPercentage = (totalVeggesWeight / total) * 100.0f;
        float grainsPercentage = (totalGrainsWeight / total) * 100.0f;
        float dishesPercentage = (totalDishesWeight / total) * 100.0f;

        dairyPercent.setText((int) dairyPercentage + "%");
        meatPercent.setText((int) meatPercentage + "%");
        grainPercent.setText((int) grainsPercentage + "%");
        fruitPercent.setText((int) fruitPercentage + "%");
        vegPercent.setText((int) vegPercentage + "%");
        dishPercent.setText((int) dishesPercentage + "%");


    }

    private void setUpBottomDonation() {
        donateButton.setVisibility(View.GONE);
        float total = totalDairyWeight + totalFruitWeight + totalGrainsWeight + totalMeatWeight + totalVeggesWeight + totalDishesWeight;

        ArrayList<donationBottomItem> foodList = new ArrayList<>();


        if (totalFruitWeight != 0) {
            foodList.add(new donationBottomItem("Fruits", totalFruitWeight, fruitList, R.drawable.fruits_donation_bottom));
        }
        if (totalVeggesWeight != 0) {
            foodList.add(new donationBottomItem("Vegetables", totalVeggesWeight, veggiesList, R.drawable.veg_donation_bottom));
        }
        if (totalDairyWeight != 0) {
            foodList.add(new donationBottomItem("Dairy", totalDairyWeight, dairyList, R.drawable.dairy_donation_bottom));
        }
        if (totalMeatWeight != 0) {
            foodList.add(new donationBottomItem("Meat", totalMeatWeight, meatList, R.drawable.meat_donation_bottom));
        }
        if (totalGrainsWeight != 0) {
            foodList.add(new donationBottomItem("Grains", totalGrainsWeight, grainsList, R.drawable.grains_donation_bottom));
        }

        if (totalDishesWeight != 0) {
            foodList.add(new donationBottomItem("Dishes", totalDishesWeight, dishesList, R.drawable.dishes_donation_bottom));
        }


        if (foodList.size() != 0) {

            mDonationBottomAdapter = new donationBottomAdapter(foodList);
            verticalLayout = new LinearLayoutManager(
                    addClass.this,
                    LinearLayoutManager.VERTICAL,
                    false);

            bottomRecycler.setLayoutManager(verticalLayout);
            bottomRecycler.setAdapter(mDonationBottomAdapter);

            donateButton.setVisibility(View.VISIBLE);
            donateButton.setText("Make Donation · " + total + "kg");
        } else {

            mDonationBottomAdapter = new donationBottomAdapter(foodList);
            verticalLayout = new LinearLayoutManager(
                    addClass.this,
                    LinearLayoutManager.VERTICAL,
                    false);

            bottomRecycler.setLayoutManager(verticalLayout);
            bottomRecycler.setAdapter(mDonationBottomAdapter);


        }


    }

    private void addToFirebase(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Constants.rest_fire).document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            orderId = "";

                            restName = documentSnapshot.getString("Name");
                            restAddress = documentSnapshot.getString("Address");

                            orderNum = (ArrayList) documentSnapshot.get(Constants.order_id_num);
                            for (int i = 0; i < orderNum.size(); i++) {
                                if (!orderNum.get(i)) {
                                    orderId = String.valueOf(i);
                                    orderNum.set(i, true);
                                    break;
                                }
                            }

                            if (!(orderId.equals(""))) {


                                HashMap<String, Object> updateMap = new HashMap<>();
                                updateMap.put(Constants.order_id_num, orderNum);
                                db.collection(Constants.rest_fire).document(uid)
                                        .update(updateMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                DocumentReference orderRef = db.collection(Constants.orderName_fire).document(Constants.order_list_fire);
                                                orderRef.update(Constants.order_list_field, FieldValue.arrayUnion(uid + "-" + orderId))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                addAllFood(uid + "-" + orderId);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(Constants.tag, "error: " + e + " add");
                                                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(Constants.tag, "error: " + e + " add");
                                                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            } else {
                                Toast.makeText(getBaseContext(), "LIMIT OF 5 DONATIONS REACHED", Toast.LENGTH_LONG).show();
                                progressdialog.dismiss();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressdialog.dismiss();
                        Log.d("TAG", e.toString());
                    }
                });


    }

    private void addAllFood(String uid) {
        if (totalFruitWeight != 0 && fruitList.size() > 0) {
            Map<String, Object> fruitMap = new HashMap<>();
            fruitMap.put(Constants.item_fire, fruitList);
            addFood(Constants.fruitName_fire, fruitMap, uid);

        }

        if (totalGrainsWeight != 0 && grainsList.size() > 0) {
            Map<String, Object> grainsMap = new HashMap<>();
            grainsMap.put(Constants.item_fire, grainsList);
            addFood(Constants.grainsName_fire, grainsMap, uid);

        }

        if (totalVeggesWeight != 0 && veggiesList.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.item_fire, veggiesList);
            addFood(Constants.vegName_fire, map, uid);

        }

        if (totalDairyWeight != 0 && dairyList.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.item_fire, dairyList);
            addFood(Constants.dairyName_fire, map, uid);

        }

        if (totalMeatWeight != 0 && meatList.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.item_fire, meatList);
            addFood(Constants.meatName_fire, map, uid);

        }

        if (totalDishesWeight != 0 && dishesList.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.item_fire, dishesList);
            addFood(Constants.dishesName_fire, map, uid);
        }

        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("Name", restName);
        infoMap.put("Address", restAddress);
        infoMap.put("Total Weight", totalDairyWeight + totalFruitWeight + totalGrainsWeight + totalMeatWeight + totalVeggesWeight + totalDishesWeight);
        infoMap.put("State", "New");


        mDatabase.child("Orders").child(uid).child("Info").setValue(infoMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(Constants.tag, "onSuccess: info add");
                        Toast.makeText(getBaseContext(), "Donation Added", Toast.LENGTH_SHORT).show();


                        progressdialog.dismiss();


                        Intent intent = new Intent(addClass.this, Main_Activity.class);
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(Constants.tag, "error: " + e + " add");
                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void addFood(String category, Map<String, Object> map, String uid) {

        mDatabase.child("Orders").child(uid).child(Constants.foodName_fire).child(category).setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Constants.tag, "onSuccess: " + category + " add");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(Constants.tag, "error: " + e + " add");
                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private static void smoothScroll(RecyclerView rv, int toPos, int duration) throws IllegalArgumentException {
        int TARGET_SEEK_SCROLL_DISTANCE_PX = 10000;
        int itemHeight = rv.getChildAt(0).getHeight();
        itemHeight = itemHeight + 33;
        int fvPos = ((LinearLayoutManager)rv.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        int i = Math.abs((fvPos - toPos) * itemHeight);
        if (i == 0) { i = (int) Math.abs(rv.getChildAt(0).getY()); }
        final int totalPix = i;
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(rv.getContext()) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
            @Override protected int calculateTimeForScrolling(int dx) {
                int ms = (int) ( duration * dx / (float)totalPix );
                if (dx < TARGET_SEEK_SCROLL_DISTANCE_PX ) { ms = ms*2; }
                return ms;
            }
        };
        smoothScroller.setTargetPosition(toPos);
        rv.getLayoutManager().startSmoothScroll(smoothScroller);
    }
}
