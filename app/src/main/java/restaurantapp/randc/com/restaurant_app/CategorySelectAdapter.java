package restaurantapp.randc.com.restaurant_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CategorySelectAdapter
        extends RecyclerView.Adapter<CategorySelectAdapter.MyView> {

    // List with String type
    private List<CategorySelectItem> list;
    private LinearLayout selectLayout;
    private Context mContext;

    // Constructor for adapter class
    // which takes a list of String type
    public CategorySelectAdapter(List<CategorySelectItem> horizontalList, Context context) {
        this.list = horizontalList;
        this.mContext = context;
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
                .inflate(R.layout.add_item,
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

        holder.categoryIcon.setImageResource(list.get(position).getCategoryIcon());
        holder.categoryName.setText(list.get(position).getCategoryName());

        holder.selectLayout.setBackgroundResource(list.get(position).getCategoryBg());

        holder.selectLayout.setPadding(60, 40, 60, 40);
        holder.selectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, categoryAdd.class);
                intent.putExtra("type", position);
                mContext.startActivity(intent);
            }
        });

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
        ImageView categoryIcon;
        private LinearLayout selectLayout;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view) {
            super(view);

            categoryName = view.findViewById(R.id.categoryName);
            categoryIcon = view.findViewById(R.id.categoryImage);
            selectLayout = view.findViewById(R.id.categoryLayout);


        }
    }
}