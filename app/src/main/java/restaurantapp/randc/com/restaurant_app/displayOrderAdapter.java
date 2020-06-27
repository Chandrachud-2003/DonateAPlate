package restaurantapp.randc.com.restaurant_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class displayOrderAdapter extends RecyclerView.Adapter<displayOrderAdapter.ViewHolder>{


    private ArrayList<categoryItem> list;

    // RecyclerView recyclerView;
    public displayOrderAdapter(ArrayList<categoryItem> list) {
        this.list = list;
    }

    @Override
    public displayOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.display_order_recycler_item, parent, false);
        displayOrderAdapter.ViewHolder viewHolder = new displayOrderAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(displayOrderAdapter.ViewHolder holder, int position) {
        final categoryItem item = list.get(position);
        holder.nameText.setText(item.getFoodItem());
        holder.weightText.setText(item.getFoodWeight()+"kg");



    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView weightText;
        public ViewHolder(View itemView) {
            super(itemView);
            this.nameText = (TextView) itemView.findViewById(R.id.foodName);
            this.weightText = (TextView) itemView.findViewById(R.id.foodWeight);

        }
    }
}