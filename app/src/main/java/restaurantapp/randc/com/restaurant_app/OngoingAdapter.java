package restaurantapp.randc.com.restaurant_app;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import static com.av.smoothviewpager.utils.Smoolider_Utils.decodeSampledBitmapFromResource;
import static com.av.smoothviewpager.utils.Smoolider_Utils.openWebPage;

public class OngoingAdapter extends PagerAdapter{

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



    public OngoingAdapter(List<OngoingItems> feedItemList, Context mContext) {
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


            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                    int height = (int) ((width * 2) / 5);

            Picasso.get()
                    .load(R.drawable.restaurant2)
                    .resize(width, height)
                    .centerCrop()
                    .into(picture);





            container.addView(view);


            return view;
        }


        @Override
        public void destroyItem (ViewGroup container,int position, Object object){
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject (View view, Object object){
            return view.equals(object);
        }
    }

