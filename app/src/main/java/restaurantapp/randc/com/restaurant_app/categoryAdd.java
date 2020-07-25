package restaurantapp.randc.com.restaurant_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.ramotion.fluidslider.FluidSlider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class categoryAdd extends AppCompatActivity {

    private ImageView foodImage;
    private EditText foodText;
    private TextView TitleView;

    private ImageButton customButton;
    private ImageButton backButton;
    private Button confirmButton;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private String Title;
    private List<String> itemName = new ArrayList<>();

    private List<Integer> itemImg;
    private int[] imgs;

    private ArrayList<Float> weights;

    private RecyclerView filterView;

    private ArrayList<categoryItem> mCategoryItems;

    private RecyclerView.LayoutManager RecyclerViewLayoutManager;

    private LinearLayoutManager HorizontalLayout;

    private categoryItemAdapter mCategoryItemAdapter;

    private int pos=0;

    private FluidSlider weightSlider;
    private int previous = 0;

    private int max = 10;
    private int min = 0;
    private int total = max-min;


    private int extraImg;

    private boolean textPresent = false;

    private int origSize;







    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_add);


        foodImage = findViewById(R.id.foodPic);
        foodText = findViewById(R.id.foodTextview);

        weightSlider = findViewById(R.id.weightSlider);
        weightSlider.setEndText(String.valueOf(max)+ " kg");
        weightSlider.setStartText(String.valueOf(min)+" kg");
        weightSlider.setPosition(0.0f);
        customButton = findViewById(R.id.custom);
        backButton = findViewById(R.id.back);
        confirmButton = findViewById(R.id.confirmButton);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        filterView = findViewById(R.id.filterView);
        TitleView = findViewById(R.id.CategoryView);
        leftButton.setVisibility(View.INVISIBLE);

        itemImg = new ArrayList<>();
        itemName = new ArrayList<>();

        int type = getIntent().getIntExtra("type",-1);
        if(type == 0)
        {
            origSize = Constants.fruit_imgs.size()-1;
            Title = Constants.fruitPref;
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_fruits));
            itemImg = Constants.fruit_imgs;
            itemImg.add(R.drawable.fruits_custom);
            extraImg = R.drawable.fruits_custom;
            itemName.add("Enter Custom Item");


            TitleView.setText("Fruits");
        }
        if(type == 1)
        {
            origSize = Constants.fruit_imgs.size()-1;
            Title = Constants.vegetablePref;
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_veg));
            itemImg = Constants.veg_imgs;
            TitleView.setText("Vegetables");
            itemName.add("Enter Custom Item");
            extraImg = R.drawable.veggies_custom;
            itemImg.add(R.drawable.veggies_custom);

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
            origSize = Constants.meat_imgs.size()-1;
            Title = Constants.meatPref;
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_meat));
            itemImg = Constants.meat_imgs;
            itemName.add("Enter Custom Item");
            itemImg.add(R.drawable.meat_custom);
            extraImg = R.drawable.meat_custom;

            TitleView.setText("Meat");
        }
        if(type == 4)
        {
            origSize = Constants.grains_imgs.size()-1;
            Title = Constants.grainsPref;
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_grains));
            itemImg = Constants.grains_imgs;
            itemName.add("Enter Custom Item");
            itemImg.add(R.drawable.grains_custom);
            extraImg = R.drawable.grains_custom;

            TitleView.setText("Grains");
        }
        if(type == 5)
        {
            origSize = Constants.dairy_imgs.size()-1;
            Title = Constants.DairyPref;
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_dairy));
            itemImg = Constants.dairy_imgs;
            itemName.add("Enter Custom Item");
            itemImg.add(R.drawable.dishes_custom_1);
            extraImg = R.drawable.dishes_custom_1;

            TitleView.setText("Dairy");
        }
        if (type==2)
        {
            origSize = 0;
            Title = Constants.dishesPref;
            itemName.add("Enter Custom Item");
            itemImg = Constants.dishes_imgs;
            extraImg = R.drawable.dishes_custom_2;

            TitleView.setText("Dishes");


        }


        List<categoryItem> mmCategoryItems;
        List<Float> mmweights;



        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId,MODE_PRIVATE);
        Gson gson = new Gson();
        String retrievedCategoryItems = sharedPreferences.getString(Title, null);
        String retrievedweights = sharedPreferences.getString(Title+"weights", null);
        if(retrievedCategoryItems!=null && !retrievedCategoryItems.equals("")) {
            mmCategoryItems = Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class));
            mCategoryItems = new ArrayList(mmCategoryItems);
            mmweights = Arrays.asList(gson.fromJson(retrievedweights, Float[].class));
            weights = new ArrayList(mmweights);
            int extraItems = weights.size()-(itemImg.size()-1);
            if (extraItems>0)
            {
                for(int i=1;i<=extraItems;i++)
                {
                    itemImg.add(itemImg.size()-2, extraImg);

                }

            }
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

        foodImage.setImageResource(itemImg.get(0));
        foodText.setText(itemName.get(0));
        if (itemName.size()==1)
        {
            leftButton.setVisibility(View.INVISIBLE);
            rightButton.setVisibility(View.INVISIBLE);
            foodText.setText("");
            foodText.setHint("Your Custom Item");
            foodText.setEnabled(true);
            weightSlider.setVisibility(View.INVISIBLE);
        }
        //foodWeight.setText(weights.get(0).toString());
        weightSlider.setPosition((float) weights.get(0)/total);
        weightSlider.setBubbleText(weights.get(0) +" kg");


        foodText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (pos==itemName.size()-1) {
                    String text = charSequence.toString();
                    if (text == null || text.equals("") || text.length() == 0) {
                        textPresent = false;
                    } else {

                        textPresent = true;
                    }
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (pos==itemName.size()-1) {
                    String text = charSequence.toString();
                    if (text.length()>0 && !textPresent)
                    {
                        weightSlider.setVisibility(View.VISIBLE);
                    }
                    else if (text.length()==0 && textPresent){

                        weightSlider.setVisibility(View.INVISIBLE);

                    }
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });






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



        weightSlider.setBeginTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {

                previous = (int) (total * weightSlider.getPosition());
                return Unit.INSTANCE;
            }
        });

        weightSlider.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {

                int current = (int) (total * weightSlider.getPosition());
                if (current>previous)
                {
                    addWeight((int) (total * weightSlider.getPosition()));
                }
                else if (current<previous){
                    removeWeight((int) (total * weightSlider.getPosition()));

                }
                return Unit.INSTANCE;
            }
        });

        // Java 8 lambda
        weightSlider.setPositionListener(pos -> {
            final String value = String.valueOf( (int)(min + total * pos) );
            weightSlider.setBubbleText(value);
            return Unit.INSTANCE;
        });



       /* plus.setOnClickListener(new View.OnClickListener() {
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
        });*/

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

        weights.set(pos, (float)((int)(min + total * weightSlider.getPosition())));

        pos-=1;
        if (pos==0)
        {
            leftButton.setVisibility(View.INVISIBLE);
            rightButton.setVisibility(View.VISIBLE);
        }
        else if (pos==weights.size()-2)
        {
            rightButton.setVisibility(View.VISIBLE);
            leftButton.setVisibility(View.VISIBLE);
            foodText.setHint("");
            foodText.setEnabled(false);
            weightSlider.setVisibility(View.VISIBLE);
        }

        foodImage.setImageResource(itemImg.get(pos));
        foodText.setText(itemName.get(pos));

        weightSlider.setPosition(weights.get(pos)/10.0f);
        weightSlider.setBubbleText(String.valueOf(weights.get(pos)));

    }

    private void changeRight()
    {

        weights.set(pos, (float)((int)(min + total * weightSlider.getPosition())));

        pos+=1;
        foodImage.setImageResource(itemImg.get(pos));
        if (pos==(itemName.size()-1))
        {
            rightButton.setVisibility(View.INVISIBLE);
            foodText.setText("");
            leftButton.setVisibility(View.VISIBLE);
            foodText.setEnabled(true);
            foodText.setHint("Your Custom Item");
            weightSlider.setVisibility(View.INVISIBLE);
            foodText.setFocusable(true);


        }
        else
        {
            foodText.setEnabled(false);

            weightSlider.setVisibility(View.VISIBLE);
            foodText.setText(itemName.get(pos));


            leftButton.setVisibility(View.VISIBLE);




        }

        weightSlider.setPosition(weights.get(pos)/10.0f);
        weightSlider.setBubbleText(String.valueOf(weights.get(pos)));

    }

    private void addWeight(int value)
    {
        Log.d("TAG", "addWeight: "+value);


        if (weights.get(pos)==0.0f)
        {


            if (pos == itemName.size()-1)
            {
                Log.d("TAG", "addWeight: new custom item weight");
                mCategoryItems.add(new categoryItem(foodText.getText().toString(), value));


                mCategoryItemAdapter.notifyItemInserted(mCategoryItems.size()-1);

                rightButton.setVisibility(View.VISIBLE);
                itemName.set(pos, foodText.getText().toString());

                itemName.add("Your Custom Item");
                itemImg.add(extraImg);
                weights.add(0.0f);

            }
            else {
                Log.d("TAG", "addWeight: new default Item");
                mCategoryItems.add(new categoryItem(itemName.get(pos), value));


                mCategoryItemAdapter.notifyItemInserted(mCategoryItems.size()-1);

            }
        }
        else {



            int newPos = findItem(itemName.get(pos));
            mCategoryItems.get(newPos).setFoodWeight(value);
            mCategoryItemAdapter.notifyItemChanged(newPos);
        }


        //foodWeight.setText(Float.toString(weights.get(pos)+0.5f));
        weights.set(pos,(float)value);


    }

    private void removeWeight(int value)
    {


        Log.d("TAG", "removeWeight: finalValue: "+value);

        if (value==0.0f)
        {

            Log.d("TAG", "removeWeight: ");
            int newPos = findItem(foodText.getText().toString());
            int currentPos = findInNames(foodText.getText().toString());
            Log.d("TAG", "removeWeight: current pos : "+currentPos);
            Log.d("TAG", "removeWeight: itemName.size(): "+itemName.size());
            Log.d("TAG", "removeWeight: origSize"+origSize);
            if (currentPos>origSize && currentPos<itemName.size()-1) {


                Log.d("TAG", "removeWeight: Entered custom");
                mCategoryItems.remove(newPos);
                mCategoryItemAdapter.notifyItemRemoved(newPos);
                itemName.remove(currentPos);
                itemImg.remove(currentPos);
                weights.remove(currentPos);
                pos-=1;
                changeRight();

            }
            else {
                Log.d("TAG", "removeWeight: default");
                mCategoryItems.remove(newPos);
                mCategoryItemAdapter.notifyItemRemoved(newPos);
                //foodWeight.setText(Float.toString(weights.get(pos) - value));
                weights.set(pos, 0.0f);
            }
        }

        else if(value>0.0f) {
            //foodWeight.setText(Float.toString(weights.get(pos) - 0.5f));
            Log.d("TAG", "removeWeight: weightChanged: "+value);
            weights.set(pos, (float)value);

            int newPos = findItem(itemName.get(pos));
            mCategoryItems.get(newPos).setFoodWeight(weights.get(pos));
            mCategoryItemAdapter.notifyItemChanged(newPos);
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

    private int findInNames(String name)
    {

        for (int i=0;i<itemName.size();i++)
        {
            if (itemName.get(i).equalsIgnoreCase(name))
            {
                return i;
            }

        }

        return -1;
    }

    @Override
    public void onBackPressed() {
        // Disabling back button for current activity
    }



}
