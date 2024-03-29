package restaurantapp.randc.com.restaurant_app;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class donationBottomAdapter
        extends RecyclerView.Adapter<donationBottomAdapter.MyView> {

    // List with String type
    private List<donationBottomItem> list;

    // Constructor for adapter class
    // which takes a list of String type
    public donationBottomAdapter(List<donationBottomItem> horizontalList) {
        this.list = horizontalList;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType) {

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.bottom_donation_item,
                        parent,
                        false);

        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position) {

        holder.categoryName.setText(list.get(position).getCategoryName());
        holder.categoryWeight.setText(list.get(position).getCategoryWeight() + "kg");
        ArrayList<categoryItem> items = new ArrayList<>();
        items = list.get(position).getItemsList();
        String itemText = "";
        for (int i = 0; i < items.size(); i++) {
            itemText += items.get(i).getFoodItem();
            if (i != (items.size() - 1)) {
                itemText += " · ";
            }

        }
        itemText.trim();


        holder.categoryItems.setText(itemText);
        holder.categoryNum.setText(items.size() + "Items");

        float width = Resources.getSystem().getDisplayMetrics().widthPixels / 10;
        float height = (float) (width / 0.75);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0)
                .cornerRadiusDp(7)
                .oval(false)
                .build();

        Picasso.get()
                .load(list.get(position).getCategoryImage())
                .resize((int) width, (int) height)
                .transform(transformation)
                .centerCrop()
                .into(holder.categoryImage);


    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount() {
        return list.size();
    }

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView categoryName;
        TextView categoryWeight;
        ImageView categoryImage;
        TextView categoryItems;
        TextView categoryNum;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view) {
            super(view);

            categoryName = view.findViewById(R.id.bottomDonationCategory);
            categoryWeight = view.findViewById(R.id.bottomDonationWeight);
            categoryImage = view.findViewById(R.id.bottomDonationImage);
            categoryItems = view.findViewById(R.id.bottomDonationItems);
            categoryNum = view.findViewById(R.id.bottomDonationNum);


        }
    }
}