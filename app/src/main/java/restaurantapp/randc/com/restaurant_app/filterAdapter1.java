package restaurantapp.randc.com.restaurant_app;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class filterAdapter1
        extends RecyclerView.Adapter<filterAdapter1.MyView> {

    // List with String type
    private List<filterItem> list;

    private View mView;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView filterText;
        ImageView filterImage;
        CardView mCardView;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view)
        {
            super(view);

            // initialise TextView with id
            filterText = (TextView)view
                    .findViewById(R.id.filterText);
            filterImage = view.findViewById(R.id.filterImage);
            mCardView = view.findViewById(R.id.cardview);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public filterAdapter1(List<filterItem> horizontalList)
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
                .inflate(R.layout.fillter_item1,
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
                                 final int position)
    {

        // Set the text of each item of
        // Recycler view with the list items
        holder.filterText.setText(list.get(position).getIconName());
        holder.filterImage.setImageResource(list.get(position).getIconImage());

        if (list.get(position).isItemChanged())
        {
            holder.filterImage.setBackgroundColor(Color.parseColor("#305F5F"));
            holder.mCardView.setElevation(40);
            holder.filterText.setTypeface(null, Typeface.BOLD);

        }
        else {
            holder.filterImage.setBackgroundColor(Color.WHITE);
            holder.mCardView.setElevation(0);
            holder.filterText.setTypeface(null, Typeface.NORMAL);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.d("TAG", "onClick: "+position);

            if(!list.get(position).isItemChanged())
            {


                switch (position)
                {
                    case 0:
                    {
                        Log.d("TAG", "onClick: entered");
                        list.set(position,  new filterItem("Nearby", R.drawable.icons8_nearby_2, true));
                        //list.get(position).setIconImage(R.drawable.icons8_nearby_2);
                        //list.get(position).setItemChanged(true);
                        Log.d("TAG", "onClick: "+list.get(position).isItemChanged());

                        notifyItemChanged(position);
                        break;
                    }
                    case 1:
                    {
                        list.get(position).setIconImage(R.drawable.icons8_mostorders_2);
                        list.get(position).setItemChanged(true);
                        notifyItemChanged(position);

                        break;
                    }
                    case 2:
                    {
                        list.get(position).setIconImage(R.drawable.icons8_person_2);
                        list.get(position).setItemChanged(true);
                        notifyItemChanged(position);

                        break;
                    }
                    case 3:
                    {
                        list.get(position).setIconImage(R.drawable.icons8_likes2_2);
                        list.get(position).setItemChanged(true);
                        notifyItemChanged(position);


                        break;
                    }
                    case 4:
                    {
                        list.get(position).setIconImage(R.drawable.icons8_verified_account_2);
                        list.get(position).setItemChanged(true);
                        notifyItemChanged(position);

                        break;
                    }


                }



            }

            else {

                switch (position)
                {
                    case 0:
                    {
                        list.get(position).setIconImage(R.drawable.icons8_nearby);
                        list.get(position).setItemChanged(false);
                        notifyItemChanged(position);
                        break;
                    }
                    case 1:
                    {
                        list.get(position).setIconImage(R.drawable.icons8_mostorders);
                        list.get(position).setItemChanged(false);
                        notifyItemChanged(position);

                        break;
                    }
                    case 2:
                    {
                        list.get(position).setIconImage(R.drawable.icons8_person);
                        list.get(position).setItemChanged(false);
                        notifyItemChanged(position);

                        break;
                    }
                    case 3:
                    {
                        list.get(position).setIconImage(R.drawable.icons8_likes2);
                        list.get(position).setItemChanged(false);
                        notifyItemChanged(position);


                        break;
                    }
                    case 4:
                    {
                        list.get(position).setIconImage(R.drawable.icons8_verified_account);
                        list.get(position).setItemChanged(false);
                        notifyItemChanged(position);

                        break;
                    }


                }

            }



        }});




    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}