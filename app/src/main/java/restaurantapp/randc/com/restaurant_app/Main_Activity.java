package restaurantapp.randc.com.restaurant_app;
//This is a comment
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.ramotion.foldingcell.FoldingCell;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
//

import java.util.ArrayList;
import java.util.Arrays;

public class Main_Activity extends AppCompatActivity {


    private SlidingRootNav slidingRootNav;

    private static final int POS_DASHBOARD = 0;
    private static final int POS_SEARCH = 1;
    private static final int POS_ORDER = 2;
    private static final int POS_PLUS = 3;
    private static final int POS_PROFILE = 4;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private ImageButton menuButton;


    private Button settingsButton;

    private  RecyclerView filterView;

    private Button logoutButton;

    private ArrayList<filterItem> filterItemList;

    private RecyclerView.LayoutManager RecyclerViewLayoutManager;

    private LinearLayoutManager HorizontalLayout;

    private RecyclerView searchRecycler;
    private ArrayList<searchItem> searchList;
    private searchAdapter searchAdapter;
    private LinearLayoutManager verticalLayout;

    private  RecyclerView mainRecycler;
    private ArrayList<MainItem> mainList;
    private MainAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        filterView = findViewById(R.id.filterView);
        menuButton = findViewById(R.id.menuButton);

        mainRecycler = findViewById(R.id.mainRecycler);
        searchRecycler = findViewById(R.id.searchRecycler);

        searchList = new ArrayList<>();

        searchList.add(new searchItem("Bangalore", "Restaurant", "Pizza Hut", R.drawable.restaurant2));
        searchList.add(new searchItem("Mumbai", "Restaurant", "Dominos", R.drawable.restaurant3));
        searchList.add(new searchItem("Bangalore", "NGO", "Ngo 1", R.drawable.ngo1));
        searchList.add(new searchItem("Bangalore", "NGO", "Ngo 2", R.drawable.ngo2));

        searchAdapter = new searchAdapter(searchList, Main_Activity.this);

        verticalLayout = new LinearLayoutManager(
                Main_Activity.this,
                LinearLayoutManager.VERTICAL,
                false);

       // searchRecycler.setLayoutManager(verticalLayout);
       // searchRecycler.setAdapter(searchAdapter);


        mainRecycler = findViewById(R.id.mainRecycler);
        //mainRecycler.setNestedScrollingEnabled(false);
        mainList = new ArrayList<>();

        mainList.add(new MainItem("Bangalore, Karnataka", "Fast-Food", "Resaurant","15 Mins","10.5K","3 kg","McDonalds",false,false ,true,true,true,false,R.drawable.restaurant2));
        mainList.add(new MainItem("Bangalore, Karnataka", "Mexican", "Resaurant","45 Mins","1.5K","0.2 kg","Chilli's",true,true ,false,true,false,true,R.drawable.restaurant3));
        mainAdapter = new MainAdapter(mainList, Main_Activity.this);

        verticalLayout = new LinearLayoutManager(
                Main_Activity.this,
                LinearLayoutManager.VERTICAL,
                false)
        {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };


        mainRecycler.setLayoutManager(verticalLayout);
        mainRecycler.setAdapter(mainAdapter);






        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getApplicationContext());

        filterItemList = new ArrayList<filterItem>();

        filterItemList.add(new filterItem("Nearby", R.drawable.icons8_nearby,false));
        filterItemList.add(new filterItem("Orders", R.drawable.icons8_mostorders,false));
        filterItemList.add(new filterItem("Followers", R.drawable.icons8_person,false));
        filterItemList.add(new filterItem("Likes", R.drawable.icons8_likes2,false));
        filterItemList.add( new filterItem("Verified", R.drawable.icons8_verified_account,false));

        final ArrayList<Item> items = Item.getTestingList();




        filterAdapter1 filterAdapter1 = new filterAdapter1(filterItemList);

        HorizontalLayout
                = new LinearLayoutManager(
                Main_Activity.this,
                LinearLayoutManager.HORIZONTAL,
                false);


        filterView.setLayoutManager(HorizontalLayout);

        // Set adapter on recycler view
        filterView.setAdapter(filterAdapter1);


        slidingRootNav = new SlidingRootNavBuilder(this)

                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();



        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter2 = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_SEARCH),
                createItemFor(POS_ORDER),
                createItemFor(POS_PLUS),
                createItemFor(POS_PROFILE)));
        adapter2.setListener(new DrawerAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                Main_Activity.this.onItemSelected(position);
            }
        });

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter2);

        adapter2.setSelected(POS_DASHBOARD);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.openMenu();
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        settingsButton = findViewById(R.id.settingsButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        final FoldingCellListAdapter adapter = new FoldingCellListAdapter(this, items);

        // add default btn handler for each request btn on each item if custom handler not found
        adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show();
            }
        });





    }

    @Override
    public void onBackPressed() {
        // Disabling back button for current activity
    }

    public void onItemSelected(int position) {



        slidingRootNav.closeMenu();

        switch (position)
        {
            case 0:
            {
                slidingRootNav.closeMenu();
                break;
            }
            case 1:
            {
                Intent intent = new Intent(Main_Activity.this, SearchClass.class);
                startActivity(intent);
                break;
            }
            case 2:
            {
                // Intent intent = new Intent(MainActivity.this, SearchClass.class);
                //startActivity(intent);
                break;
            }


            case 3:
            {
                //Intent intent = new Intent(MainActivity.this, SearchClass.class);
                //startActivity(intent);
                break;
            }

            case 4:
            {
                Intent intent = new Intent(Main_Activity.this, profileClass.class);
                startActivity(intent);
                break;
            }



        }

    }


    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withTextTint(color(R.color.greenText))
                .withSelectedIconTint(color(R.color.white))
                .withSelectedTextTint(color(R.color.white));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private void logout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main_Activity.this);
        builder.setCancelable(true);
        builder.setTitle("Log Out");
        builder.setMessage("Are sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Main_Activity.this, loginpage.class);
                startActivity(intent);

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





}
