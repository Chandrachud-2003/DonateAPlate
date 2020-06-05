package restaurantapp.randc.com.restaurant_app;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private float fruitWeights[];

    private float totalVeggesWeight=0.0f;
    private String VeggesNames[];
    private float VeggesWeights[];

    private float totalMeatWeight=0.0f;
    private String MeatNames[];
    private float MeatWeights[];

    private float totalGrainsWeight=0.0f;
    private String GrainsNames[];
    private float GrainsWeights[];

    private float totalDairyWeight=0.0f;
    private String DairyNames[];
    private float DairyWeights[];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        selectCategory = findViewById(R.id.categorySelectView);
        mPieChart = findViewById(R.id.categoryChart);
        clearButton = findViewById(R.id.clearButton1);
        menuButton = findViewById(R.id.menuButton);
        recentLayout = findViewById(R.id.recentLayout);

        selectList = new ArrayList<>();

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

                        SharedPreferences sharedPreferences = getSharedPreferences("CategorySave",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        mEntries.clear();
                        retrieveValuesFromMem();
                        createPie();
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
        donationBottomItems = new ArrayList<>();
        donationBottomItems.add(new donationBottomItem("food","5kgs"));
        donationBottomItems.add(new donationBottomItem("errwerwer","65kgs"));
        donationBottomItems.add(new donationBottomItem("fooerereed","5k0gs"));

        donationBottomAdapter = new donationBottomAdapter(donationBottomItems);
        bottomRecycler.setLayoutManager(new LinearLayoutManager(this));
        bottomRecycler.setAdapter(donationBottomAdapter);



    }

    private void retrieveValuesFromMem(){


        SharedPreferences sharedPreferences = getSharedPreferences("CategorySave",MODE_PRIVATE);
        Gson gson = new Gson();
        List<categoryItem> mmItems;
        ArrayList<categoryItem> mItems;


        //FRUITS
        String retrievedCategoryItems = sharedPreferences.getString("Fruits", null);
        if(retrievedCategoryItems!=null) {
            mmItems = Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class));
            mItems = new ArrayList(mmItems);
            fruitNames = new String[mItems.size()];
            fruitWeights = new float[mItems.size()];
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
        retrievedCategoryItems = sharedPreferences.getString("Vegetables", null);
        if(retrievedCategoryItems!=null) {
            mmItems = Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class));
            mItems = new ArrayList(mmItems);
            VeggesNames = new String[mItems.size()];
            VeggesWeights = new float[mItems.size()];
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
        retrievedCategoryItems = sharedPreferences.getString("Meat", null);
        if(retrievedCategoryItems!=null) {
            mmItems = Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class));
            mItems = new ArrayList(mmItems);
            MeatNames = new String[mItems.size()];
            MeatWeights = new float[mItems.size()];
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
        retrievedCategoryItems = sharedPreferences.getString("Grains", null);
        if(retrievedCategoryItems!=null) {
            mmItems = Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class));
            mItems = new ArrayList(mmItems);
            GrainsNames = new String[mItems.size()];
            GrainsWeights = new float[mItems.size()];
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
        retrievedCategoryItems = sharedPreferences.getString("Dairy", null);
        if(retrievedCategoryItems!=null) {
            mmItems = Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class));
            mItems = new ArrayList(mmItems);
            DairyNames = new String[mItems.size()];
            DairyWeights = new float[mItems.size()];
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
    }
}
