package restaurantapp.randc.com.restaurant_app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class searchAdapter  extends RecyclerView.Adapter<searchAdapter.MyView>{
    private List<searchItem> list;

    private View mView;

    private Context mContext;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView nameText;
        TextView locationtText;
        TextView typeText;
        LinearLayout typeLayout;
        ImageView typeImage;
        ImageView mainImage;
        TextView tempText1;
        TextView tempText2;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view)
        {
            super(view);

           nameText = view.findViewById(R.id.searchItemNameOuter);
           locationtText = view.findViewById(R.id.searchItemTextInner);
           mainImage = view.findViewById(R.id.searchItemImage);
           typeImage = view.findViewById(R.id.searchTypeImage);
           typeText = view.findViewById(R.id.searchTypeText);
           typeLayout = view.findViewById(R.id.searchTypeLayout);
           tempText1 = view.findViewById(R.id.temptext1);
           tempText2 = view.findViewById(R.id.temptext2);









        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public searchAdapter(List<searchItem> horizontalList, Context context)
    {
        this.list = horizontalList;
        mContext = context;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType)
    {

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.search_item,
                        parent,
                        false);

        // return itemView
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final searchAdapter.MyView holder,
                                 final int position)
    {
        holder.nameText.setText(list.get(position).getName());
        holder.typeText.setText(list.get(position).getType());
        holder.tempText1.setText(list.get(position).getName());
        holder.tempText2.setText((list.get(position).getLocation()));
        holder.locationtText.setText(list.get(position).getLocation());
        //holder.mainImage.setImageResource(list.get(position).getImage());

        int width =  Resources.getSystem().getDisplayMetrics().widthPixels;


        int height = (int) ((width*2)/3);

        Bitmap bm = BitmapFactory.decodeResource( mContext.getResources(), list.get(position).getImage());

      //  holder.mainImage.setImageBitmap(new Bitmap.createScaledBitmap(bm, width, height, false));



        if(list.get(position).getType().equals("ngo")) {
            holder.typeImage.setImageResource(R.drawable.icons8_ngo);
            holder.typeLayout.setBackgroundResource(R.drawable.ngo_bg);
        }

        else {
            holder.typeImage.setImageResource(R.drawable.icons8_rest);
            holder.typeLayout.setBackgroundResource(R.drawable.rest_bg);

        }



    }


    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
