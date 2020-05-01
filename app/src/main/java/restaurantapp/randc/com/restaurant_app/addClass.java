package restaurantapp.randc.com.restaurant_app;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class addClass extends AppCompatActivity {

    private RecyclerView selectCategory;

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



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        selectCategory = findViewById(R.id.categorySelectView);
        mPieChart = findViewById(R.id.categoryChart);

        menuButton = findViewById(R.id.menuButton);
        recentLayout = findViewById(R.id.recentLayout);

        selectList = new ArrayList<>();

        selectList.add(new CategorySelectItem("Fruits", R.drawable.icons8_fruit_category, R.drawable.fruit_category_bg));
        selectList.add(new CategorySelectItem("Dairy", R.drawable.icons8_category_dairy, R.drawable.dairy_category_bg));
        selectList.add(new CategorySelectItem("Veggies", R.drawable.icons8_category_veg, R.drawable.veg_category_bg));
        selectList.add(new CategorySelectItem("Meat", R.drawable.icons8_category_meat, R.drawable.meat_category_bg));
        selectList.add(new CategorySelectItem("Grains", R.drawable.icons8_category_grains, R.drawable.grains_category_bg));
        selectList.add(new CategorySelectItem("Dishes", R.drawable.icons8_category_dishes, R.drawable.dishes_cateogry_bg));

        HorizontalLayout
                = new LinearLayoutManager(
                addClass.this,
                LinearLayoutManager.HORIZONTAL,
                false);

        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getApplicationContext());

        mCategoryItemAdapter = new CategorySelectAdapter(selectList);
        selectCategory.setLayoutManager(HorizontalLayout);
        selectCategory.setAdapter(mCategoryItemAdapter);

        mEntries = new ArrayList<>();

        AddValuesToEntry();

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
        colors.add(Color.parseColor("#02BDE0"));
        colors.add(Color.parseColor("#3DC073"));
        colors.add(Color.parseColor("#FF2B5B"));
        colors.add(Color.parseColor("#796DFF"));
        colors.add(Color.parseColor("#2F4F4F"));


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

    private void AddValuesToEntry(){

        mEntries.add(new PieEntry(5f, 0));
        mEntries.add(new PieEntry(10f, 1));

        mEntries.add(new PieEntry(40f, 2));

        mEntries.add(new PieEntry(55f, 3));
        mEntries.add(new PieEntry(40f, 4));

        mEntries.add(new PieEntry(30, 5));


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
}
