package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.jackandphantom.blurimage.BlurImage;
import com.ramotion.circlemenu.CircleMenuView;

public class displayInfoActivity extends AppCompatActivity {

    private HTextView mHTextView;
    private int textCounter;

    private RecyclerView dairyRecycler;
    private RecyclerView meatRecycler;
    private RecyclerView fruitRecycler;
    private RecyclerView vegRecycler;
    private RecyclerView grainRecycler;

    private Button requestButton;

    private BottomSheetBehavior mBottomSheetBehavior;

    private ImageView iconView;

    private ImageButton backButton;
    private ImageButton shareButton;



    private View bottomSheet;

    private displayItem[] mFruitItemsList;
    private displayItem[] mVegetablesItemsList;
    private displayItem[] mGrainsItemsList;
    private displayItem[] mDairyItemsList;
    private displayItem[] mMeatItemsList;

    private RelativeLayout fruitLayout;
    private RelativeLayout meatLayout;
    private RelativeLayout vegLayout;
    private RelativeLayout dairyLayout;
    private RelativeLayout grainLayout;
    private int state;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_info_2);
        state = 0;
        iconView = findViewById(R.id.profileImageDisplay);

        dairyRecycler = findViewById(R.id.recycler_dairy);
        meatRecycler = findViewById(R.id.recycler_meat);
        grainRecycler = findViewById(R.id.recycler_grains);
        fruitRecycler = findViewById(R.id.recycler_fruits);
        vegRecycler = findViewById(R.id.recycler_vegtables);

        bottomSheet = findViewById(R.id.bottomSheetScroll);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        requestButton = findViewById(R.id.requestButton);

        vegLayout = findViewById(R.id.vegetableRelativeLayout);
        meatLayout = findViewById(R.id.meatRelativeLayout);
        fruitLayout = findViewById(R.id.fruitRelativeLayout);
        dairyLayout = findViewById(R.id.dairyRelativeLayout);
        grainLayout = findViewById(R.id.grainsRelativeLayout);

        final CircleMenuView menu = (CircleMenuView) findViewById(R.id.circle_menu);



        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state ==0) {

                    menu.setVisibility(View.VISIBLE);
                    menu.open(true);
                    state = 1;



                }
                if(state == 1) {
                    menu.close(true);
                    state = 0;
                }
                menu.setEventListener(new CircleMenuView.EventListener() {
                    @Override
                    public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                        Log.d("D", "onMenuOpenAnimationStart");

                    }

                    @Override
                    public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
                        Log.d("D", "onMenuOpenAnimationEnd");
                        //menu.close(false);
                    }

                    @Override
                    public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
                        Log.d("D", "onMenuCloseAnimationStart");

                    }

                    @Override
                    public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
                        Log.d("D", "onMenuCloseAnimationEnd");
                        menu.setVisibility(View.GONE);

                    }

                    @Override
                    public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {
                        Log.d("D", "onButtonClickAnimationStart| index: " + index);
                    }

                    @Override
                    public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                        Log.d("D", "onButtonClickAnimationEnd| index: " + index);
                    }
                });


            }
        });

        BlurImage.with(getApplicationContext()).load(R.drawable.restaurant_sample).intensity(20).Async(false).into(iconView);

        mFruitItemsList = new displayItem[] {

          new displayItem("Banana", "2 kg", R.drawable.icons8_banana),
                new displayItem("Apple", "1 kg", R.drawable.icons8_apple)  ,
                new displayItem("Mango", "3 kg", R.drawable.icons8_mango)

        };

        mDairyItemsList = new displayItem[] {

                new displayItem("Cheese", "2 kg", R.drawable.icons8_cheese),
                new displayItem("Butter", "1 kg", R.drawable.icons8_butter)

        };

        mVegetablesItemsList = new displayItem[] {

                new displayItem("Carrot", "2 kg", R.drawable.icons8_carrot),
                new displayItem("Cauliflower", "1 kg", R.drawable.icons8_cauliflower)  ,
                new displayItem("Brocolli", "3 kg", R.drawable.icons8_broccoli)

        };

        mMeatItemsList = new displayItem[] {

                new displayItem("Chicken", "2 kg", R.drawable.icons8_friedchicken),
                new displayItem("Fish", "1 kg", R.drawable.icons8_fishskeleton)  ,
                new displayItem("Turkey", "3 kg", R.drawable.icons8_thanksgivingturkey)



        };

        mGrainsItemsList = new displayItem[] {

                new displayItem("Rice", "5 kg", R.drawable.icons8_ricebowl),
                new displayItem("Wheat", "3 kg", R.drawable.icons8_wheat)

        };

        displayItemAdapter fruitAdapter = new displayItemAdapter(mFruitItemsList);
        fruitRecycler.setHasFixedSize(true);
        fruitRecycler.setLayoutManager(new LinearLayoutManager(this));
        fruitRecycler.setAdapter(fruitAdapter);

        displayItemAdapter vegAdapter = new displayItemAdapter(mVegetablesItemsList);
        vegRecycler.setHasFixedSize(true);
        vegRecycler.setLayoutManager(new LinearLayoutManager(this));
        vegRecycler.setAdapter(vegAdapter);

        displayItemAdapter meatAdapter = new displayItemAdapter(mMeatItemsList);
        meatRecycler.setHasFixedSize(true);
        meatRecycler.setLayoutManager(new LinearLayoutManager(this));
        meatRecycler.setAdapter(meatAdapter);

        displayItemAdapter grainAdapter = new displayItemAdapter(mGrainsItemsList);
        grainRecycler.setHasFixedSize(true);
        grainRecycler.setLayoutManager(new LinearLayoutManager(this));
        grainRecycler.setAdapter(grainAdapter);

        displayItemAdapter dairyAdapter = new displayItemAdapter(mDairyItemsList);
        dairyRecycler.setHasFixedSize(true);
        dairyRecycler.setLayoutManager(new LinearLayoutManager(this));
        dairyRecycler.setAdapter(dairyAdapter);


        dairyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dairyRecycler.getVisibility()==View.VISIBLE)
                {
                    dairyRecycler.setVisibility(View.GONE);
                }
                else
                {
                    dairyRecycler.setVisibility(View.VISIBLE);
                }

            }
        });

        meatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (meatRecycler.getVisibility()==View.VISIBLE)
                {
                    meatRecycler.setVisibility(View.GONE);
                }
                else
                {
                    meatRecycler.setVisibility(View.VISIBLE);
                }

            }
        });

        vegLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (vegRecycler.getVisibility()==View.VISIBLE)
                {
                    vegRecycler.setVisibility(View.GONE);
                }
                else
                {
                    vegRecycler.setVisibility(View.VISIBLE);
                }

            }
        });

        fruitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fruitRecycler.getVisibility()==View.VISIBLE)
                {
                    fruitRecycler.setVisibility(View.GONE);
                }
                else
                {
                    fruitRecycler.setVisibility(View.VISIBLE);
                }

            }
        });

        grainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (grainRecycler.getVisibility()==View.VISIBLE)
                {
                    grainRecycler.setVisibility(View.GONE);
                }
                else
                {
                    grainRecycler.setVisibility(View.VISIBLE);
                }

            }
        });





        textCounter =0;




    }
    @Override
    protected void onStop() {
        super.onStop();
        state = 0;
    }

    /*private void switchText()
    {
        if (textCounter ==0)
        {
            mHTextView.animateText("50kg");
            textCounter=1;
        }
        else if (textCounter==1){
            mHTextView.animateText("5kg");
            textCounter=0;
        }
    }*/
}
