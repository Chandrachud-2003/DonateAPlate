package restaurantapp.randc.com.restaurant_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ramotion.fluidslider.FluidSlider;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Unit;

public class categoryAdd extends AppCompatActivity implements RecyclerViewClickListener, Bottom_Custom_Item.ButtonClickListener {

    private ImageView foodImage;
    private TextView foodText;
    private TextView TitleView;
    private Button customButtonText;
    private ImageButton customButton;
    private ImageButton backButton;
    private Button confirmButton;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private String Title;
    private List<String> itemName = new ArrayList<>();

    private List<Integer> itemImg;


    private ArrayList<Float> weights;

    private RecyclerView filterView;

    private ArrayList<categoryItem> mCategoryItems;
    private RecyclerView.LayoutManager RecyclerViewLayoutManager;

    private LinearLayoutManager HorizontalLayout;

    private categoryItemAdapter mCategoryItemAdapter;

    private int screen_width;

    private int pos = 0;

    private FluidSlider weightSlider;
    private int max = 10;
    private int min = 0;
    private int total = max - min;

    private int origSize;

    private CardView addCustomButton;

    private TextView weightText;
    private View weightDivider;

    private Bottom_Custom_Item mBottomCustomItem;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_add);

        customButton = findViewById(R.id.custom);
        foodImage = findViewById(R.id.foodPic);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screen_width = displayMetrics.widthPixels;

        foodImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                foodImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                int newDimensions = (int) (screen_width * 0.417);

                foodImage.getLayoutParams().width = newDimensions;


                foodImage.getLayoutParams().height = newDimensions;

                foodImage.requestLayout();


            }
        });

        foodText = findViewById(R.id.foodTextview);
        customButtonText = findViewById(R.id.addcustom);
        weightSlider = findViewById(R.id.weightSlider);
        weightSlider.setEndText("5 kg");
        weightSlider.setStartText(min + " kg");
        weightSlider.setPosition(0);
        customButton = findViewById(R.id.custom);
        backButton = findViewById(R.id.back);
        confirmButton = findViewById(R.id.confirmButton);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        filterView = findViewById(R.id.filterView);
        TitleView = findViewById(R.id.CategoryView);
        leftButton.setVisibility(View.INVISIBLE);
        weightText = findViewById(R.id.weightText);
        weightDivider = findViewById(R.id.weightDivider);


        addCustomButton = findViewById(R.id.addCustomButton);
        addCustomButton.setVisibility(View.GONE);

        itemImg = new ArrayList<>();
        itemName = new ArrayList<>();

        int type = getIntent().getIntExtra("type", -1);
        if (type == 0) {
            origSize = Constants.fruit_imgs.size() - 1;
            Title = Constants.fruitPref;
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_fruits));
            itemImg = Constants.fruit_imgs;
            itemImg.add(R.drawable.fruits_custom);
            //extraImg = R.drawable.fruits_custom;
            itemName.add("Custom Item");
            customButtonText.setText("Or add a fruit of your own here! ");

            TitleView.setText("Fruits");
        }
        if (type == 1) {
            origSize = Constants.veg_imgs.size() - 1;
            Title = Constants.vegetablePref;
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_veg));
            itemImg = Constants.veg_imgs;
            TitleView.setText("Vegetables");
            customButtonText.setText("Or add a vegetable of your own here! ");
            itemName.add("Enter Custom Item");
            //extraImg = R.drawable.veggies_custom;
            itemImg.add(R.drawable.veggies_custom);

        }
        if (type == 2) {
        }
        if (type == 3) {
            origSize = Constants.meat_imgs.size() - 1;
            Title = Constants.meatPref;
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_meat));
            itemImg = Constants.meat_imgs;
            itemName.add("Enter Custom Item");
            itemImg.add(R.drawable.meat_custom);
            //extraImg = R.drawable.meat_custom;
            customButtonText.setText("Or add your own meat here!");
            TitleView.setText("Meat");
        }
        if (type == 4) {
            origSize = Constants.grains_imgs.size() - 1;
            Title = Constants.grainsPref;
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_grains));
            itemImg = Constants.grains_imgs;
            itemName.add("Enter Custom Item");
            itemImg.add(R.drawable.grains_custom);
            //extraImg = R.drawable.grains_custom;
            customButtonText.setText("Or add a grain of your own here!");
            TitleView.setText("Grains");
        }
        if (type == 5) {
            origSize = Constants.dairy_imgs.size() - 1;
            Title = Constants.DairyPref;
            Collections.addAll(itemName, getResources().getStringArray(R.array.common_dairy));
            itemImg = Constants.dairy_imgs;
            itemName.add("Enter Custom Item");
            itemImg.add(R.drawable.dishes_custom_1);
            //extraImg = R.drawable.dishes_custom_1;
            customButtonText.setText("Or add your own dairy product here!");
            TitleView.setText("Dairy");
        }
        if (type == 2) {
            origSize = 0;
            Title = Constants.dishesPref;
            itemName.add("Custom Dish");
            itemImg = Constants.dishes_imgs;
            // extraImg = R.drawable.dishes_custom_2;
            customButtonText.setVisibility(View.GONE);
            TitleView.setText("Dishes");
            customButton.setVisibility(View.GONE);
            addCustomButton.setVisibility(View.VISIBLE);

        }


        List<categoryItem> mmCategoryItems;
        List<Float> mmweights;


        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId, MODE_PRIVATE);
        Gson gson = new Gson();
        String retrievedCategoryItems = sharedPreferences.getString(Title, null);
        String retrievedweights = sharedPreferences.getString(Title + "weights", null);
        if (retrievedCategoryItems != null && !retrievedCategoryItems.equals("")) {
            mmCategoryItems = Arrays.asList(gson.fromJson(retrievedCategoryItems, categoryItem[].class));
            mCategoryItems = new ArrayList(mmCategoryItems);
            mmweights = Arrays.asList(gson.fromJson(retrievedweights, Float[].class));
            weights = new ArrayList(mmweights);

        } else {
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

        mCategoryItemAdapter = new categoryItemAdapter(mCategoryItems, this::recyclerViewListClicked, categoryAdd.this, Title);
        filterView.setLayoutManager(HorizontalLayout);
        filterView.setAdapter(mCategoryItemAdapter);

        foodImage.setImageResource(itemImg.get(0));
        foodText.setText(itemName.get(0));
        if (type == 2) {
            leftButton.setVisibility(View.INVISIBLE);
            rightButton.setVisibility(View.INVISIBLE);
            weightSlider.setVisibility(View.GONE);
            weightDivider.setVisibility(View.GONE);
            weightText.setVisibility(View.GONE);

            addCustomButton.setVisibility(View.VISIBLE);

        }

        weightSlider.setPosition((float) weights.get(0) / total);
        weightSlider.setBubbleText("" + weights.get(0));


        PushDownAnim.setPushDownAnimTo(leftButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        changeLeft();
                    }
                });


        PushDownAnim.setPushDownAnimTo(rightButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        changeRight();
                    }
                });

        PushDownAnim.setPushDownAnimTo(addCustomButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomCustomItem = new Bottom_Custom_Item(categoryAdd.this, Title);
                        mBottomCustomItem.show(getSupportFragmentManager(), "BottomSheetCustomFragment");
                    }
                });
        PushDownAnim.setPushDownAnimTo(customButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomCustomItem = new Bottom_Custom_Item(categoryAdd.this, Title);
                        mBottomCustomItem.show(getSupportFragmentManager(), "BottomSheetCustomFragment");
                    }
                });
        PushDownAnim.setPushDownAnimTo(customButtonText)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomCustomItem = new Bottom_Custom_Item(categoryAdd.this, Title);
                        mBottomCustomItem.show(getSupportFragmentManager(), "BottomSheetCustomFragment");
                    }
                });


        weightSlider.setPositionListener(pos -> {

            float value = (float) (Math.round(total * weightSlider.getPosition() * 0.5 * 2) / 2.0);

            Weight(value);

            weightSlider.setBubbleText(String.valueOf(value));
            return Unit.INSTANCE;
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences.Editor editor = sharedPreferences.edit();


                String mCategoryItemsText = gson.toJson(mCategoryItems);
                String weightsText = gson.toJson(weights);
                editor.putString(Title, mCategoryItemsText);
                editor.putString(Title + "weights", weightsText);
                editor.apply();
                Intent intent = new Intent(categoryAdd.this, addClass.class);
                intent.putExtra("From","categoryAdd");
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categoryAdd.this, addClass.class);
                intent.putExtra("From","categoryAdd");
                startActivity(intent);
                finish();
            }
        });

       /* customButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                weights.set(pos, (float)((int)(min + total * weightSlider.getPosition())));

                pos=itemName.size()-1;
                foodImage.setImageResource(itemImg.get(pos));
                rightButton.setVisibility(View.INVISIBLE);
                foodText.setText("");
                leftButton.setVisibility(View.VISIBLE);
                foodText.setEnabled(true);
                foodText.setHint("Your Custom Item");
                weightSlider.setVisibility(View.INVISIBLE);
                foodText.setFocusable(true);
                previous = (int) (weights.get(pos)/10.0f);
                weightSlider.setPosition(weights.get(pos)/10.0f);

                weightSlider.setBubbleText(String.valueOf(weights.get(pos)));
            }
        });*/

    }

    private void changeLeft() {

        pos -= 1;
        if (pos == 0) {
            leftButton.setVisibility(View.INVISIBLE);
            rightButton.setVisibility(View.VISIBLE);
        } else if (pos == weights.size() - 2) {
            rightButton.setVisibility(View.VISIBLE);
            leftButton.setVisibility(View.VISIBLE);
            weightText.setVisibility(View.VISIBLE);
            weightDivider.setVisibility(View.VISIBLE);
            weightSlider.setVisibility(View.VISIBLE);
            addCustomButton.setVisibility(View.GONE);
        }
        foodImage.setImageResource(itemImg.get(pos));
        foodText.setText(itemName.get(pos));

        weightSlider.setPosition((weights.get(pos) / total) / 0.5f);
        weightSlider.setBubbleText(String.valueOf(weights.get(pos)));

    }

    private void changeRight() {
        pos += 1;
        foodImage.setImageResource(itemImg.get(pos));
        if (pos == (itemName.size() - 1)) {
            rightButton.setVisibility(View.INVISIBLE);
            foodText.setText(itemName.get(pos));
            leftButton.setVisibility(View.VISIBLE);
            weightSlider.setVisibility(View.GONE);
            weightText.setVisibility(View.GONE);
            weightDivider.setVisibility(View.GONE);
            addCustomButton.setVisibility(View.VISIBLE);


        } else {
            weightSlider.setVisibility(View.VISIBLE);
            foodText.setText(itemName.get(pos));


            leftButton.setVisibility(View.VISIBLE);

            weightText.setVisibility(View.VISIBLE);
            weightDivider.setVisibility(View.VISIBLE);


        }
        weightSlider.setPosition((weights.get(pos) / total) / 0.5f);
        weightSlider.setBubbleText(String.valueOf(weights.get(pos)));

    }

    private void Weight(float value) {

        if (value == 0.0f) {

            if (weights.get(pos) != 0.0f) {
                int newPos = findItem(foodText.getText().toString());
                int currentPos = findInNames(foodText.getText().toString());
                Log.d("TAG", "Weight: newPos item removed :"+newPos);
                mCategoryItems.remove(newPos);
                mCategoryItemAdapter.notifyItemRemoved(newPos);
                mCategoryItemAdapter.notifyDataSetChanged();
                weights.set(pos, 0.0f);
            }
        } else {

            if (weights.get(pos) == 0.0f) {
                mCategoryItems.add(new categoryItem(itemName.get(pos), value, false));
                mCategoryItemAdapter.notifyItemInserted(mCategoryItems.size() - 1);
                weights.set(pos, (float) value);
            } else {
                int newPos = findItem(itemName.get(pos));
                mCategoryItems.get(newPos).setFoodWeight(value);
                mCategoryItemAdapter.notifyItemChanged(newPos);
                weights.set(pos, (float) value);
            }
        }

    }


    private int findItem(String name) {

        for (int i = 0; i < mCategoryItems.size(); i++) {
            if (mCategoryItems.get(i).getFoodItem().equalsIgnoreCase(name)) {
                return i;
            }

        }

        return -1;
    }

    private int findInNames(String name) {

        for (int i = 0; i < itemName.size(); i++) {
            if (itemName.get(i).equalsIgnoreCase(name)) {
                return i;
            }

        }

        return -1;
    }

    @Override
    public void onBackPressed() {
        // Disabling back button for current activity
    }


    @Override
    public void recyclerViewListClicked(View v, String name) {
        int position = findInNames(name);
        pos = position;
        foodImage.setImageResource(itemImg.get(pos));

        rightButton.setVisibility(View.VISIBLE);
        leftButton.setVisibility(View.VISIBLE);
        if (position == 0) {
            leftButton.setVisibility(View.INVISIBLE);
        }

        foodText.setEnabled(false);

        weightSlider.setVisibility(View.VISIBLE);
        foodText.setText(itemName.get(pos));
        addCustomButton.setVisibility(View.GONE);
        weightSlider.setPosition((weights.get(pos) / total) / 0.5f);
        weightSlider.setBubbleText(String.valueOf(weights.get(pos)));

    }


    @Override
    public void deleteItem(int itemPos) {

        mCategoryItems.remove(itemPos);
        mCategoryItemAdapter.notifyItemRemoved(itemPos);
        mCategoryItemAdapter.notifyDataSetChanged();

    }

    @Override
    public void addItem(String name, double weight) {

        mCategoryItems.add(new categoryItem(name, (float) weight, true));


        mCategoryItemAdapter.notifyItemInserted(mCategoryItems.size() - 1);


    }

    @Override
    public void updateItem(String name, double weight, int itemPos) {

        mCategoryItems.get(itemPos).setFoodWeight((float) weight);
        mCategoryItems.get(itemPos).setFoodItem(name);
        mCategoryItemAdapter.notifyItemChanged(itemPos);

    }
}
