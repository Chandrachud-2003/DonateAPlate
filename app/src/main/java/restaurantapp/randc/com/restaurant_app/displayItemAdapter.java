package restaurantapp.randc.com.restaurant_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class displayItemAdapter extends RecyclerView.Adapter<displayItemAdapter.ViewHolder>{
    private displayItem[] listdata;

    // RecyclerView recyclerView;
    public displayItemAdapter(displayItem[] listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.display_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final displayItem myListData = listdata[position];
        holder.textView.setText(listdata[position].getItemName());
        holder.imageView.setImageResource(listdata[position].getItemImage());
        holder.itemWeight.setText(listdata[position].getItemWeight());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+myListData.getItemName(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public  TextView itemWeight;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.item_Image);
            this.textView = (TextView) itemView.findViewById(R.id.itemName);
            this.itemWeight = itemView.findViewById(R.id.itemWeight);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.displayItemRelativeLayout);
        }
    }
}
