package restaurantapp.randc.com.restaurant_app;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class categoryItemAdapter
        extends RecyclerView.Adapter<categoryItemAdapter.MyView> {

    // List with String type

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position)
        {

                if (list.get(position).getFoodItem()!=null && list.get(position).getFoodWeight()!=0.0f) {
                    holder.foodName.setText(list.get(position).getFoodItem());
                    if(list.get(position).getFoodItem().length()<9) {
                       holder.foodName.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                    }
                    else if (list.get(position).getFoodItem().length()<12)
                    {
                        holder.foodName.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    }
                    else
                        holder.foodName.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    holder.foodWeight.setText(Float.toString(list.get(position).getFoodWeight()));
                }


    }
    private List<categoryItem> list;

    private View mView;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView foodName;
        TextView foodWeight;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view)
        {
            super(view);

            foodName = view.findViewById(R.id.foodName2);
            foodWeight = view.findViewById(R.id.foodWeight2);


        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public categoryItemAdapter(List<categoryItem> horizontalList)
    {
        this.list = horizontalList;
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
                .inflate(R.layout.category_add_item,
                        parent,
                        false);

        // return itemView
        return new MyView(itemView);
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}