package restaurantapp.randc.com.restaurant_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addClass extends AppCompatActivity {

    private RecyclerView selectCategory;

    private RecyclerView bottomRecycler;
    private donationBottomAdapter donationBottomAdapter;
    private ArrayList<donationBottomItem> donationBottomItems;
    private Button clearButton;
    private PieChart mPieChart;
    private PieDataSet mPieDataSet;
    private PieData mPieData;

    private List<PieEntry> mEntries;
    private ArrayList<String> xVals;
    private ImageButton menuButton;
    private RelativeLayout recentLayout;
    private List<CategorySelectItem> selectList;
    private LinearLayoutManager HorizontalLayout;
    private RecyclerView.LayoutManager RecyclerViewLayoutManager;
    private CategorySelectAdapter mCategoryItemAdapter;


    private float totalFruitWeight=0.0f;
    private String fruitNames[];
    private Float fruitWeights[];

    private float totalVeggesWeight=0.0f;
    private String VeggesNames[];
    private Float VeggesWeights[];

    private float totalMeatWeight=0.0f;
    private String MeatNames[];
    private Float MeatWeights[];

    private float totalGrainsWeight=0.0f;
    private String GrainsNames[];
    private Float GrainsWeights[];

    private float totalDairyWeight=0.0f;
    private String DairyNames[];
    private Float DairyWeights[];

    private TextView dairyPercent;
    private TextView meatPercent;
    private TextView fruitPercent;
    private TextView vegPercent;
    private TextView dishPercent;
    private TextView grainPercent;

    private TextView donateButton;

    private LinearLayoutManager verticalLayout;

    private donationBottomAdapter mDonationBottomAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);


        selectCategory = findViewById(R.id.categorySelectView);
        mPieChart = findViewById(R.id.categoryChart);
        clearButton = findViewById(R.id.clearButton1);
        menuButton = findViewById(R.id.menuButton);
        recentLayout = findViewById(R.id.recentLayout);
        dairyPercent = findViewById(R.id.dairyPercentText);
        meatPercent = findViewById(R.id.meatPercentText);

        grainPercent = findViewById(R.id.grainsPercentText);

        fruitPercent = findViewById(R.id.fruitPercentText);

        vegPercent = findViewById(R.id.veggiesPercentText);

        donateButton = findViewById(R.id.donateTextView);


        selectList = new ArrayList<>();

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

                       /* SharedPreferences sharedPreferences = getSharedPreferences("CategorySave",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        mEntries.clear();
                        retrieveValuesFromMem();
                        createPie();*/
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

        mCategoryItemAdapter = new CategorySelectAdapter(selectList,addClass.this);
        selectCategory.setLayoutManager(HorizontalLayout);
        selectCategory.setAdapter(mCategoryItemAdapter);

        mEntries = new ArrayList<>();

        retrieveValuesFromMem();
        createPie();


        bottomRecycler = findViewById(R.id.bottomRecycler);

        setUpBottomDonation();



    }

    private void setOnClickListeners()
    {
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                FirebaseAuth auth = FirebaseAuth.getInstance();

                if (auth!=null) {

                    editor.putString(Constants.DairyPref, "");
                    editor.putString(Constants.dishesPref, "");
                    editor.putString(Constants.fruitPref, "");
                    editor.putString(Constants.vegetablePref, "");
                    editor.putString(Constants.grainsPref, "");
                    editor.putString(Constants.meatPref, "");
                    editor.commit();

                    String uid = auth.getUid();

                    addToFirebase(uid);
                }

                else {

                    Toast.makeText(addClass.this, "Please Sign in First", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(addClass.this, loginpage.class);
                    startActivity(intent);

                }






            }
        });
    }

    private void retrieveValuesFromMem(){


        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId,MODE_PRIVATE);
        Gson gson = new Gson();
        List<categoryItem> mmItems;
        ArrayList<categoryItem> mItems;


        //FRUITS
        String retrievedCategoryItems = sharedPreferences.getString(Constants.fruitPref, "");
        if(retrievedCategoryItems!="") {
            mmItems = Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class));
            mItems = new ArrayList(mmItems);
            fruitNames = new String[mItems.size()];
            fruitWeights = new Float[mItems.size()];
            for(int i =0;i<mItems.size();i++) {
                fruitNames[i] = mItems.get(i).getFoodItem();
                fruitWeights[i] = mItems.get(i).getFoodWeight();
                totalFruitWeight = totalFruitWeight + mItems.get(i).getFoodWeight();
            }
        }
        else {
            fruitNames = null;
            fruitWeights = null;
            totalFruitWeight = 0;
        }


        //Veggies
        retrievedCategoryItems = sharedPreferences.getString(Constants.vegetablePref, "");
        if(retrievedCategoryItems!="") {
            mmItems = Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class));
            mItems = new ArrayList(mmItems);
            VeggesNames = new String[mItems.size()];
            VeggesWeights = new Float[mItems.size()];
            for(int i =0;i<mItems.size();i++) {
                VeggesNames[i] = mItems.get(i).getFoodItem();
                VeggesWeights[i] = mItems.get(i).getFoodWeight();
                totalVeggesWeight = totalVeggesWeight + mItems.get(i).getFoodWeight();
            }
        }
        else {
            VeggesNames = null;
            VeggesWeights = null;
            totalVeggesWeight = 0;
        }


        //Meat
        retrievedCategoryItems = sharedPreferences.getString(Constants.meatPref, "");
        if(retrievedCategoryItems!="") {
            mmItems = Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class));
            mItems = new ArrayList(mmItems);
            MeatNames = new String[mItems.size()];
            MeatWeights = new Float[mItems.size()];
            for(int i =0;i<mItems.size();i++) {
                MeatNames[i] = mItems.get(i).getFoodItem();
                MeatWeights[i] = mItems.get(i).getFoodWeight();
                totalMeatWeight = totalMeatWeight + mItems.get(i).getFoodWeight();
            }
        }
        else {
            MeatNames = null;
            MeatWeights = null;
            totalMeatWeight = 0;
        }

        //Grains
        retrievedCategoryItems = sharedPreferences.getString(Constants.grainsPref, "");
        if(retrievedCategoryItems!="") {
            mmItems = Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class));
            mItems = new ArrayList(mmItems);
            GrainsNames = new String[mItems.size()];
            GrainsWeights = new Float[mItems.size()];
            for(int i =0;i<mItems.size();i++) {
                GrainsNames[i] = mItems.get(i).getFoodItem();
                GrainsWeights[i] = mItems.get(i).getFoodWeight();
                totalGrainsWeight = totalGrainsWeight + mItems.get(i).getFoodWeight();
            }
        }
        else {
            GrainsNames = null;
            GrainsWeights = null;
            totalGrainsWeight = 0;
        }

        //Grains
        retrievedCategoryItems = sharedPreferences.getString(Constants.DairyPref, "");
        if(retrievedCategoryItems!="") {
            mmItems = Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class));
            mItems = new ArrayList(mmItems);
            DairyNames = new String[mItems.size()];
            DairyWeights = new Float[mItems.size()];
            for(int i =0;i<mItems.size();i++) {
                DairyNames[i] = mItems.get(i).getFoodItem();
                DairyWeights[i] = mItems.get(i).getFoodWeight();
                totalDairyWeight = totalDairyWeight + mItems.get(i).getFoodWeight();
            }
        }
        else {
            DairyNames = null;
            DairyWeights = null;
            totalDairyWeight = 0;
        }



    }

    private void addXVals()
    {
        xVals.add("Fruits");
        xVals.add("Dairy");
        xVals.add("Veggies");
        xVals.add("Meat");
        xVals.add("Grains");
        xVals.add("Dishes");
    }

    private void createPie()
    {
        mEntries.add(new PieEntry(totalFruitWeight, 0));
        mEntries.add(new PieEntry(totalVeggesWeight, 1));
        mEntries.add(new PieEntry(0, 2));
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
        colors.add(Color.parseColor("#2F4F4F"));
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

        setUpPieText();
    }

    private void setUpPieText()
    {
        float total = totalDairyWeight+totalFruitWeight+totalGrainsWeight+totalMeatWeight+totalVeggesWeight;
        float dairyPercentage = (totalDairyWeight/total)*100.0f;
        float meatPercentage = (totalMeatWeight/total)*100.0f;
        float fruitPercentage = (totalFruitWeight/total)*100.0f;
        float vegPercentage = (totalVeggesWeight/total)*100.0f;
        float grainsPercentage = (totalGrainsWeight/total)*100.0f;
        float dishesPercentage = (totalDairyWeight/total)*100.0f;

        dairyPercent.setText((int)dairyPercentage+"%");
        meatPercent.setText((int)meatPercentage+"%");
        grainPercent.setText((int)grainsPercentage+"%");
        fruitPercent.setText((int)fruitPercentage+"%");
        vegPercent.setText((int)vegPercentage+"%");


    }

    private void setUpBottomDonation()
    {
        donateButton.setVisibility(View.GONE);
        float total = totalDairyWeight+totalFruitWeight+totalGrainsWeight+totalMeatWeight+totalVeggesWeight;

        ArrayList<donationBottomItem> foodList= new ArrayList<>();


        if (totalFruitWeight!=0)
        {
            foodList.add(new donationBottomItem("Fruits", totalFruitWeight, fruitNames, R.drawable.fruits_donation_bottom));
        }
        if (totalVeggesWeight!=0)
        {
            foodList.add(new donationBottomItem("Vegetables", totalVeggesWeight, VeggesNames, R.drawable.veg_donation_bottom));
        }
        if (totalDairyWeight!=0)
        {
            foodList.add(new donationBottomItem("Dairy", totalDairyWeight, DairyNames, R.drawable.dairy_donation_bottom));
        }
        if (totalMeatWeight!=0)
        {
            foodList.add(new donationBottomItem("Meat", totalMeatWeight, MeatNames, R.drawable.meat_donation_bottom));
        }
        if (totalGrainsWeight!=0)
        {
            foodList.add(new donationBottomItem("Grains", totalGrainsWeight, GrainsNames, R.drawable.grains_donation_bottom));
        }


        if (foodList.size()!=0) {

            mDonationBottomAdapter = new donationBottomAdapter(foodList);
            verticalLayout = new LinearLayoutManager(
                    addClass.this,
                    LinearLayoutManager.VERTICAL,
                    false);

            bottomRecycler.setLayoutManager(verticalLayout);
            bottomRecycler.setAdapter(mDonationBottomAdapter);

            donateButton.setVisibility(View.VISIBLE);
            donateButton.setText("Make Donation · "+total+"kg");
        }




    }

    private void addToFirebase(String uid)
    {

        if (totalFruitWeight!=0 && fruitNames.length>0)
        {
            Map<String, Object> fruitMap = new HashMap<>();
            fruitMap.put(Constants.name_fire, Arrays.asList(fruitNames));
            fruitMap.put(Constants.weight_fire, Arrays.asList(fruitWeights));
            addFood(Constants.fruitName_fire, fruitMap, uid);

        }

        if (totalGrainsWeight!=0 && GrainsNames.length>0)
        {
            Map<String, Object> grainsMap = new HashMap<>();
            grainsMap.put(Constants.name_fire, Arrays.asList(GrainsNames));
            grainsMap.put(Constants.weight_fire, Arrays.asList(GrainsWeights));
            addFood(Constants.grainsName_fire, grainsMap, uid);

        }

        if (totalVeggesWeight!=0 && VeggesNames.length>0)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.name_fire, Arrays.asList(VeggesNames));
            map.put(Constants.weight_fire, Arrays.asList(VeggesWeights));
            addFood(Constants.vegName_fire, map, uid);

        }

        if (totalDairyWeight!=0 && DairyNames.length>0)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.name_fire, Arrays.asList(DairyNames));
            map.put(Constants.weight_fire, Arrays.asList(DairyWeights));
            addFood(Constants.dairyName_fire, map, uid);

        }

        if (totalMeatWeight!=0 && MeatNames.length>0)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.name_fire, Arrays.asList(MeatNames));
            map.put(Constants.weight_fire, Arrays.asList(MeatWeights));
            addFood(Constants.meatName_fire, map, uid);

        }


    }

    private void addFood(String category, Map<String, Object> map, String uid)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Constants.orderName_fire).document(uid).collection(Constants.foodName_fire).document(category)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(Constants.tag, "onSuccess: "+category+" add");

                    }
                });

        Toast.makeText(getBaseContext(), "Order Added", Toast.LENGTH_SHORT).show();


    }
}
