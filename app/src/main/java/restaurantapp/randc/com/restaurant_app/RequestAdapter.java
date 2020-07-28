package restaurantapp.randc.com.restaurant_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class RequestAdapter extends PagerAdapter{

    private Context mContext;
    private List<OngoingItems> feedItemList;
    private ImageView picture;
    private TextView name;
    private TextView weights;
    private TextView fruitsPop;
    private TextView veggiesPop;
    private TextView meatPop;
    private TextView grainsPop;
    private TextView dairyPop;
    private TextView dishesPop;



    public RequestAdapter(List<OngoingItems> feedItemList, Context mContext) {
        this.mContext = mContext;
        this.feedItemList = feedItemList;
    }

    @Override
    public int getCount() {
        return feedItemList.size();
    }

    @NonNull
    @Override
    public View instantiateItem(@NonNull ViewGroup container, int position) {
        final OngoingItems item = feedItemList.get(position);


        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.ongoing_layout_pager, container, false);



        Log.d("TAG", "instantiateItem: entered");

        name = view.findViewById(R.id.ongoingName);
        weights = view.findViewById(R.id.ongoingWeight);
        fruitsPop = view.findViewById(R.id.fruitsPop);
        veggiesPop = view.findViewById(R.id.vegetablesPop);
        meatPop = view.findViewById(R.id.meatPop);
        grainsPop = view.findViewById(R.id.grainsPop);
        dairyPop = view.findViewById(R.id.dairyPop);
        dishesPop = view.findViewById(R.id.dishesPop);

        picture = view.findViewById(R.id.ongoingImage);
        name.setText(item.getName());
        weights.setText(item.getWeight()+" kg");





        if (item.isFruits())
        {
            fruitsPop.setVisibility(View.VISIBLE);
        }

        if (item.isDairy())
        {
            dairyPop.setVisibility(View.VISIBLE);
        }

        if (item.isGrains())
        {
            grainsPop.setVisibility(View.VISIBLE);
        }

        if (item.isMeat())
        {
            meatPop.setVisibility(View.VISIBLE);
        }

        if (item.isVeggies())
        {
            veggiesPop.setVisibility(View.VISIBLE);
        }
        if (item.isDishes())
        {
            dishesPop.setVisibility(View.VISIBLE);
        }

        float density = mContext.getResources()
                .getDisplayMetrics()
                .density;

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = (int) (((width)* 2) / 5);
        char a = item.getOrderID().charAt(item.getOrderID().length()-1);
        int res;
        if(a=='1')
           res = R.drawable.donation4;
        else if(a=='2')
            res = R.drawable.donation5;
        else if(a=='3')
            res = R.drawable.donation2;
        else if(a=='4')
            res = R.drawable.donation;
        else
            res = R.drawable.donation3;

        Picasso.get()
                .load(res)
                .resize(width, height)
                .centerInside()
                .into(picture);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((Activity)mContext, displayOrder.class);

                intent.putExtra(Constants.orderId_intent, item.getOrderID());
                intent.putExtra(Constants.isDairy_intent,item.isDairy() );
                intent.putExtra(Constants.isGrains_intent, item.isGrains());
                intent.putExtra(Constants.isFruits_intent, item.isFruits());
                intent.putExtra(Constants.isVeggies_intent, item.isVeggies());
                intent.putExtra(Constants.isMeat_intent,item.isMeat());
                intent.putExtra(Constants.isDishes_intent, item.isDishes());
                intent.putExtra(Constants.total_weight_intent,item.getWeight());
                intent.putExtra("From","requestItem");

                mContext.startActivity(intent);

            }
        });

        container.addView(view);


        return view;
    }


    @Override
    public void destroyItem (ViewGroup container,int position, Object object){
        Log.d("TAG", "destroyItem: entered, position: "+position+" Object: "+object.toString());
        //container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject (View view, Object object){
        return view.equals(object);
    }
}

