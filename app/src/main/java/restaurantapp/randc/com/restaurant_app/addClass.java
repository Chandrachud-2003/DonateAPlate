package restaurantapp.randc.com.restaurant_app;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addClass extends AppCompatActivity {

    private RecyclerView selectCategory;

    private RecyclerView bottomRecycler;
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

    private ArrayList<categoryItem> fruitList;

    private float totalVeggesWeight=0.0f;

    private ArrayList<categoryItem> veggiesList;


    private float totalMeatWeight=0.0f;

    private ArrayList<categoryItem> meatList;


    private float totalGrainsWeight=0.0f;

    private ArrayList<categoryItem> grainsList;


    private float totalDairyWeight=0.0f;

    private ArrayList<categoryItem> dairyList;


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


// ...
        mDatabase = FirebaseDatabase.getInstance().getReference();
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

        fruitList = new ArrayList<>();
        veggiesList = new ArrayList<>();
        grainsList = new ArrayList<>();
        dairyList = new ArrayList<>();
        meatList = new ArrayList<>();

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

                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(Constants.DairyPref);
                        editor.remove(Constants.fruitPref);
                        editor.remove(Constants.grainsPref);
                        editor.remove(Constants.meatPref);
                        editor.remove(Constants.vegetablePref);
                        editor.remove(Constants.dishesPref);
                        editor.apply();
                        setUpBottomDonation();
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

                if (auth.getUid()!=null) {

                    editor.putString(Constants.DairyPref, "");
                    editor.putString(Constants.dishesPref, "");
                    editor.putString(Constants.fruitPref, "");
                    editor.putString(Constants.vegetablePref, "");
                    editor.putString(Constants.grainsPref, "");
                    editor.putString(Constants.meatPref, "");
                    editor.apply();

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

            fruitList.addAll(Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class)));

            for (int i=0;i<fruitList.size();i++)
            {
                totalFruitWeight+=fruitList.get(i).getFoodWeight();
            }

            Log.d("TAG", "retrieveValuesFromMem: "+fruitList.toString());

        }
        else {
            fruitList = null;
            totalFruitWeight = 0;
        }


        //Veggies
        retrievedCategoryItems = sharedPreferences.getString(Constants.vegetablePref, "");
        if(retrievedCategoryItems!="") {
            veggiesList.addAll(Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class)));
            Log.d("TAG", "retrieveValuesFromMem: entered "+veggiesList.toString());

            for (int i=0;i<veggiesList.size();i++)
            {
                totalVeggesWeight+=veggiesList.get(i).getFoodWeight();
            }


        }
        else {
            veggiesList = null;
            totalVeggesWeight = 0;
        }


        //Meat
        retrievedCategoryItems = sharedPreferences.getString(Constants.meatPref, "");
        if(retrievedCategoryItems!="") {
            meatList.addAll(Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class)));

            for (int i=0;i<meatList.size();i++)
            {
                totalMeatWeight+=meatList.get(i).getFoodWeight();
            }

        }
        else {
            meatList = null;
            totalMeatWeight = 0;
        }

        //Grains
        retrievedCategoryItems = sharedPreferences.getString(Constants.grainsPref, "");
        if(retrievedCategoryItems!="") {
            grainsList.addAll(Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class)));

            for (int i=0;i<grainsList.size();i++)
            {
                totalGrainsWeight+=grainsList.get(i).getFoodWeight();
            }

        }
        else {
            grainsList = null;
            totalGrainsWeight = 0;
        }

        //Grains
        retrievedCategoryItems = sharedPreferences.getString(Constants.DairyPref, "");
        if(retrievedCategoryItems!="") {
            dairyList.addAll(Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class)));

            for (int i=0;i<dairyList.size();i++)
            {
                totalDairyWeight+=dairyList.get(i).getFoodWeight();
            }

        }
        else {
            dairyList = null;
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
            foodList.add(new donationBottomItem("Fruits", totalFruitWeight, fruitList, R.drawable.fruits_donation_bottom));
        }
        if (totalVeggesWeight!=0)
        {
            foodList.add(new donationBottomItem("Vegetables", totalVeggesWeight, veggiesList, R.drawable.veg_donation_bottom));
        }
        if (totalDairyWeight!=0)
        {
            foodList.add(new donationBottomItem("Dairy", totalDairyWeight, dairyList, R.drawable.dairy_donation_bottom));
        }
        if (totalMeatWeight!=0)
        {
            foodList.add(new donationBottomItem("Meat", totalMeatWeight, meatList, R.drawable.meat_donation_bottom));
        }
        if (totalGrainsWeight!=0)
        {
            foodList.add(new donationBottomItem("Grains", totalGrainsWeight, grainsList, R.drawable.grains_donation_bottom));
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




        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Constants.rest_fire).document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            orderId="";

                            restName = documentSnapshot.getString("Name");
                            restAddress = documentSnapshot.getString("Address");

                            orderNum = (ArrayList) documentSnapshot.get(Constants.order_id_num);
                           for (int i=0;i<orderNum.size();i++)
                           {
                               if (!orderNum.get(i))
                               {
                                   orderId=String.valueOf(i);
                                   orderNum.set(i,true);
                                   break;
                               }
                           }

                           if(!(orderId.equals(""))) {
                               addAllFood(uid + "-" + orderId);


                               HashMap<String, Object> updateMap = new HashMap<>();
                               updateMap.put(Constants.order_id_num, orderNum);
                               db.collection(Constants.rest_fire).document(uid)
                                       .update(updateMap)
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {

                                           }
                                       })
                                       .addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Log.d(Constants.tag, "error: " + e + " add");
                                               Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                                           }
                                       });


                               DocumentReference orderRef = db.collection(Constants.orderName_fire).document(Constants.order_list_fire);

                                // Atomically add a new region to the "regions" array field.
                               orderRef.update(Constants.order_list_field, FieldValue.arrayUnion(uid + "-" + orderId));




                           }
                           else
                               Toast.makeText(getBaseContext(),"LIMIT OF 5 DONATIONS REACHED",Toast.LENGTH_LONG).show();


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("TAG", e.toString());
                    }
                });



    }

    private void addAllFood(String uid)
    {
        if (totalFruitWeight!=0 && fruitList.size()>0)
        {
            Map<String, Object> fruitMap = new HashMap<>();
            fruitMap.put(Constants.item_fire, fruitList);
            addFood(Constants.fruitName_fire, fruitMap, uid);

        }

        if (totalGrainsWeight!=0 && grainsList.size()>0)
        {
            Map<String, Object> grainsMap = new HashMap<>();
            grainsMap.put(Constants.item_fire, grainsList);
            addFood(Constants.grainsName_fire, grainsMap, uid);

        }

        if (totalVeggesWeight!=0 && veggiesList.size()>0)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.item_fire, veggiesList);
            addFood(Constants.vegName_fire, map, uid);

        }

        if (totalDairyWeight!=0 && dairyList.size()>0)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.item_fire, dairyList);
            addFood(Constants.dairyName_fire, map, uid);

        }

        if (totalMeatWeight!=0 && meatList.size()>0)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.item_fire, meatList);
            addFood(Constants.meatName_fire, map, uid);

        }

        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("Name", restName);
        infoMap.put("Address", restAddress);

        mDatabase.child("Orders").child(uid).child("Info").setValue(infoMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(Constants.tag, "onSuccess: info add");
                        Toast.makeText(getBaseContext(), "Order Added", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(Constants.tag, "error: "+e+" add");
                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void addFood(String category, Map<String, Object> map, String uid)
    {

        mDatabase.child("Orders").child(uid).child(Constants.foodName_fire).child(category).setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Constants.tag, "onSuccess: "+category+" add");
                        Toast.makeText(getBaseContext(), "Order Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(Constants.tag, "error: "+e+" add");
                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
