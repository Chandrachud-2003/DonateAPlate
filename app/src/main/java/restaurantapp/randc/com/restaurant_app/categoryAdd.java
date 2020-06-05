package restaurantapp.randc.com.restaurant_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hanks.htextview.HTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class categoryAdd extends AppCompatActivity {

    private ImageView foodImage;
    private TextView foodText;
    private TextView TitleView;
    private ImageButton plus;
    private ImageButton minus;
    private TextView foodWeight;
    private ImageButton customButton;
    private ImageButton backButton;
    private Button confirmButton;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private String Title;
    private List<String> itemName = new ArrayList<>();

    private TypedArray itemImg;

    private ArrayList<Float> weights;

    private RecyclerView filterView;

    private ArrayList<categoryItem> mCategoryItems;

    private RecyclerView.LayoutManager RecyclerViewLayoutManager;

    private LinearLayoutManager HorizontalLayout;

    private categoryItemAdapter mCategoryItemAdapter;




    private int pos=0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_add);


        foodImage = findViewById(R.id.foodPic);
        foodText = findViewById(R.id.foodTextview);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        foodWeight = findViewById(R.id.weight);
        customButton = findViewById(R.id.custom);
        backButton = findViewById(R.id.back);
        confirmButton = findViewById(R.id.confirmButton);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        filterView = findViewById(R.id.filterView);
        TitleView = findViewById(R.id.CategoryView);
        leftButton.setVisibility(View.INVISIBLE);


        int type = getIntent().getIntExtra("type",-1);
        if(type == 0)
        {
            Title = "Fruits";
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_fruits));
            itemImg = getResources().obtainTypedArray(R.array.fruits_img);
            TitleView.setText("Fruits");
        }
        if(type == 1)
        {
            Title = "Vegetables";
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_veg));
            itemImg = getResources().obtainTypedArray(R.array.vegge_img);
            TitleView.setText("Vegetables");
        }
        if(type == 2)
        {
         //   Title = "Vegetables";
         //   Collections.addAll(itemName, getResources().getStringArray(R.array.common_veg));
          //  itemImg = getResources().obtainTypedArray(R.array.vegge_img);
          //  TitleView.setText("Vegetables");
        }
        if(type == 3)
        {
            Title = "Meat";
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_meat));
            itemImg = getResources().obtainTypedArray(R.array.meat_img);
            TitleView.setText("Meat");
        }
        if(type == 4)
        {
            Title = "Grains";
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_grains));
            itemImg = getResources().obtainTypedArray(R.array.grains_img);
            TitleView.setText("Grains");
        }
        if(type == 5)
        {
            Title = "Dairy";
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_dairy));
            itemImg = getResources().obtainTypedArray(R.array.dairy_img);
            TitleView.setText("Dairy");
        }


        List<categoryItem> mmCategoryItems;
        List<Float> mmweights;

        SharedPreferences sharedPreferences = getSharedPreferences("CategorySave",MODE_PRIVATE);
        Gson gson = new Gson();
        String retrievedCategoryItems = sharedPreferences.getString(Title, null);
        String retrievedweights = sharedPreferences.getString(Title+"weights", null);
        if(retrievedCategoryItems!=null) {
            mmCategoryItems = Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class));
            mCategoryItems = new ArrayList(mmCategoryItems);
            mmweights = Arrays.asList(gson.fromJson(retrievedweights, Float[].class));
            weights = new ArrayList(mmweights);
        }
        else {
            mCategoryItems = new ArrayList<>();
            weights = new ArrayList<>();
            for (int i = 0; i < itemName.size(); i++) {
                weights.add(0.0f);
            }
        }



        HorizontalLayout
                = new LinearLayoutManager(
                categoryAdd.this,
                LinearLayoutManager.HORIZONTAL,
                false);

        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getApplicationContext());

        mCategoryItemAdapter = new categoryItemAdapter(mCategoryItems);
        filterView.setLayoutManager(HorizontalLayout);
        filterView.setAdapter(mCategoryItemAdapter);

        foodImage.setImageResource(itemImg.getResourceId(0, -1));
        foodText.setText(itemName.get(0));
        foodWeight.setText(weights.get(0).toString());






        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeLeft();

            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeRight();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addWeight();

            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeWeight();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                SharedPreferences.Editor editor = sharedPreferences.edit();


                String mCategoryItemsText = gson.toJson(mCategoryItems);
                String weightsText = gson.toJson(weights);
                editor.putString(Title,mCategoryItemsText);
                editor.putString(Title+"weights",weightsText);
                editor.apply();
                Intent intent = new Intent(categoryAdd.this, addClass.class);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categoryAdd.this, addClass.class);
                startActivity(intent);
            }
        });



    }

    private void changeLeft()
    {

        weights.set(pos, Float.parseFloat(foodWeight.getText().toString()));

        pos-=1;
        if (pos==0)
        {
            leftButton.setVisibility(View.INVISIBLE);
        }
        else if (pos==weights.size()-2)
        {
            rightButton.setVisibility(View.VISIBLE);
        }

        foodImage.setImageResource(itemImg.getResourceId(pos, -1));
        foodText.setText(itemName.get(pos));

        foodWeight.setText(Float.toString(weights.get(pos)));

    }

    private void changeRight()
    {

        weights.set(pos, Float.parseFloat(foodWeight.getText().toString()));



        pos+=1;
        if (pos==(weights.size()-1))
        {
            rightButton.setVisibility(View.INVISIBLE);

        }
        else  if (pos==1)
        {
            leftButton.setVisibility(View.VISIBLE);
        }

        foodImage.setImageResource(itemImg.getResourceId(pos, -1));
        foodText.setText(itemName.get(pos));

        foodWeight.setText(Float.toString(weights.get(pos)));
    }

    private void addWeight()
    {


        if (weights.get(pos)==0.0f)
        {
            mCategoryItems.add(new categoryItem(itemName.get(pos), 0.5f));


            mCategoryItemAdapter.notifyItemInserted(mCategoryItems.size()-1);
        }
        else {
            float orig =weights.get(pos);


            int newPos = findItem(itemName.get(pos));
            mCategoryItems.get(newPos).setFoodWeight(0.5f+orig);
            mCategoryItemAdapter.notifyItemChanged(newPos);
        }


        foodWeight.setText(Float.toString(weights.get(pos)+0.5f));
        weights.set(pos,weights.get(pos)+0.5f);
        for(int i = 0;i<weights.size();i++) {
            Log.d("TAG", weights.get(i).toString());
        }

        Log.d("TAG", "DONE");

    }

    private void removeWeight()
    {

        if (weights.get(pos)==0.5f)
        {
            int newPos = findItem(itemName.get(pos));
            mCategoryItems.remove(newPos);
            mCategoryItemAdapter.notifyItemRemoved(newPos);
            foodWeight.setText(Float.toString(weights.get(pos) - 0.5f));
            weights.set(pos, weights.get(pos) - 0.5f);
        }

        else if(weights.get(pos)>0.5) {
            foodWeight.setText(Float.toString(weights.get(pos) - 0.5f));
            weights.set(pos, weights.get(pos) - 0.5f);

            int newPos = findItem(itemName.get(pos));
            mCategoryItems.get(newPos).setFoodWeight(weights.get(pos));
            mCategoryItemAdapter.notifyItemChanged(newPos);
        }

        for(int i = 0;i<weights.size();i++) {
            Log.d("TAG", weights.get(i).toString());
        }

        Log.d("TAG", "DONE");

    }


    private int findItem(String name)
    {

        for (int i=0;i<mCategoryItems.size();i++)
        {
            if (mCategoryItems.get(i).getFoodItem().equalsIgnoreCase(name))
            {
                return i;
            }

        }

        return -1;
    }



}
