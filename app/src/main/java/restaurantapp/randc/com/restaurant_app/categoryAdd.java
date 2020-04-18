package restaurantapp.randc.com.restaurant_app;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanks.htextview.HTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class categoryAdd extends AppCompatActivity {

    private ImageView foodImage;
    private TextView foodText;
    private ImageButton plus;
    private ImageButton minus;
    private TextView foodWeight;
    private ImageButton customButton;
    private ImageButton backButton;
    private Button confirmButton;
    private ImageButton leftButton;
    private ImageButton rightButton;

    private List<String> commonFruit = new ArrayList<>();
    private List<String> commonMeat = new ArrayList<>();
    private List<String> commonDairy = new ArrayList<>();
    private List<String> commonVeg = new ArrayList<>();
    private List<String> commonGrain = new ArrayList<>();

    private TypedArray fruitImg;
    private TypedArray meatImg ;
    private TypedArray dairyImg;
    private TypedArray vegImg;
    private TypedArray grainImg ;

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

        leftButton.setVisibility(View.INVISIBLE);

        mCategoryItems = new ArrayList<>();

        //mCategoryItems.add(new categoryItem("Null", 0.5f));





        // Set adapter on recycler view

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




        Collections.addAll(commonFruit, getResources().getStringArray(R.array.common_fruits));

        fruitImg = getResources().obtainTypedArray(R.array.fruits_img);

        weights = new ArrayList<>();

        for (int i=0;i<10;i++)
        {
            weights.add(0.0f);
        }

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

        foodImage.setImageResource(fruitImg.getResourceId(pos, -1));
        foodText.setText(commonFruit.get(pos));

        foodWeight.setText(Float.toString(weights.get(pos)));



        /*switch (pos)
        {

            case 0:
            {

                foodImage.setImageResource(fruitImg.getResourceId(pos, -1));
                foodText.setText(commonFruit.get(pos));

                break;
            }
            case 1:
            {

                break;
            }
            case 2:
            {

                break;
            }
            case 3:
            {

                break;
            }
            case 4:
            {

                break;
            }

            case 5:
            {


            }


        }*/


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

        foodImage.setImageResource(fruitImg.getResourceId(pos, -1));
        foodText.setText(commonFruit.get(pos));

        foodWeight.setText(Float.toString(weights.get(pos)));
    }

    private void addWeight()
    {


        if (weights.get(pos)==0.0f)
        {
            mCategoryItems.add(new categoryItem(commonFruit.get(pos), 0.5f));


            mCategoryItemAdapter.notifyItemInserted(mCategoryItems.size()-1);
        }
        else {
            float orig =weights.get(pos);


            int newPos = findItem(commonFruit.get(pos));
            mCategoryItems.get(newPos).setFoodWeight(0.5f+orig);
            mCategoryItemAdapter.notifyItemChanged(newPos);
        }


        foodWeight.setText(Float.toString(weights.get(pos)+0.5f));
        weights.set(pos,weights.get(pos)+0.5f);


    }

    private void removeWeight()
    {

        if (weights.get(pos)==0.5f)
        {
            int newPos = findItem(commonFruit.get(pos));
            mCategoryItems.remove(newPos);
            mCategoryItemAdapter.notifyItemRemoved(newPos);

        }

        if(weights.get(pos)!=0) {
            foodWeight.setText(Float.toString(weights.get(pos) - 0.5f));
            weights.set(pos, weights.get(pos) - 0.5f);
        }




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
