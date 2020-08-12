package restaurantapp.randc.com.restaurant_app;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class categoryItemAdapter
        extends RecyclerView.Adapter<categoryItemAdapter.MyView> {

    // List with String type

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    private static RecyclerViewClickListener itemListener;
    private Context mContext;
    private String category;
    private List<categoryItem> list;
    private View mView;

    // Constructor for adapter class
    // which takes a list of String type
    public categoryItemAdapter(List<categoryItem> horizontalList, RecyclerViewClickListener itemListener, Context context, String category) {
        this.list = horizontalList;
        categoryItemAdapter.itemListener = itemListener;
        mContext = context;
        this.category = category;
    }

    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position) {

        if (list.get(position).getFoodItem() != null && list.get(position).getFoodWeight() != 0.0f) {
            holder.foodName.setText(list.get(position).getFoodItem());
            if (list.get(position).getFoodItem().length() < 9) {
                holder.foodName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            } else if (list.get(position).getFoodItem().length() < 12) {
                holder.foodName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            } else {
                holder.foodName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                final float scale = mContext.getResources().getDisplayMetrics().density;
                int dpWidthInPx = (int) (130 * scale);
                int dpHeightInPx = (int) (30 * scale);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpWidthInPx, dpHeightInPx);
                holder.space.setLayoutParams(layoutParams);
            }
            holder.foodWeight.setText(list.get(position).getFoodWeight() + "kg");

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(list.get(position).isCustom())) {
                    itemListener.recyclerViewListClicked(view, list.get(position).getFoodItem());
                } else {
                    Bottom_Custom_Item bottomCustomItem = new Bottom_Custom_Item(mContext, position, list.get(position).getFoodWeight(), list.get(position).getFoodItem(), category);
                    bottomCustomItem.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "BottomSheetCustomFragment");
                }
            }
        });

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
                .inflate(R.layout.category_add_item,
                        parent,
                        false);

        // return itemView
        return new MyView(itemView);
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount() {
        return list.size();
    }

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public static class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView foodName;
        TextView foodWeight;
        Space space;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view) {
            super(view);

            foodName = view.findViewById(R.id.foodName2);
            foodWeight = view.findViewById(R.id.foodWeight2);
            space = view.findViewById(R.id.space);

        }
    }
}