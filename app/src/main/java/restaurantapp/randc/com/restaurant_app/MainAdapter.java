package restaurantapp.randc.com.restaurant_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


    public class MainAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<MainItem> list;
        private Context mContext;
        private String newOrAccepted;

        private int width;
        private int height;

        public class donationView
                extends RecyclerView.ViewHolder {


            TextView nameText;
            TextView locationtText;
            TextView resaurantTypeText;
            TextView weightText;
            ImageView mainImage;
            TextView timeText;
            TextView fruitview;
            TextView veggiesview;
            TextView dairyview;
            TextView meatview;
            TextView dishesview;
            TextView grainsview;

            LottieAnimationView loadingAnim;

            public donationView(View view)
            {
                super(view);

                nameText = view.findViewById(R.id.mainListName);
                locationtText = view.findViewById(R.id.mainListAddr);
                resaurantTypeText = view.findViewById(R.id.typeMain);
                timeText = view.findViewById(R.id.timeMain);
                weightText = view.findViewById(R.id.MainWeight);
                mainImage = view.findViewById(R.id.mainImage);
                fruitview = view.findViewById(R.id.fruitsPop);
                veggiesview = view.findViewById(R.id.vegetablesPop);
                dairyview = view.findViewById(R.id.dairyPop);
                dishesview = view.findViewById(R.id.dishesPop);
                grainsview = view.findViewById(R.id.grainsPop);
                meatview = view.findViewById(R.id.meatPop);
                loadingAnim = view.findViewById(R.id.imageLoadingAnim);
                mainImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mainImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            mainImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }




                        mainImage.getLayoutParams().width = width;
                        mainImage.getLayoutParams().height = height;
                        mainImage.requestLayout();

                    }
                });




            }
        }



        // Constructor for adapter class
        // which takes a list of String type
        public MainAdapter(Context context, ArrayList<MainItem> mainItems, String newOrAccepted)
        {
            list = mainItems;
            mContext = context;
            this.newOrAccepted = newOrAccepted;

            width = Resources.getSystem().getDisplayMetrics().widthPixels;
            height = (int) ((width * 2) / 3);
            Log.d("TAG", "MainAdapter: " + width);
        }

        public List<MainItem> getList() {
            return list;
        }

        public void setList(List<MainItem> list) {
            this.list = list;
        }

        // Override onCreateViewHolder which deals
        // with the inflation of the card layout
        // as an item for the RecyclerView.
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {

            // Inflate item.xml using LayoutInflator
            RecyclerView.ViewHolder ViewHolder = null;
            LayoutInflater inflater  = LayoutInflater.from(parent.getContext());

            ViewHolder = new donationView(inflater.inflate(R.layout.main_item,
                        parent,
                        false));

            // return itemView
            return ViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder CommonHolder,
                                      int position) {



                    donationView holder = (donationView) CommonHolder;
                    holder.loadingAnim.playAnimation();
                    holder.nameText.setText(list.get(position).getName());
                    holder.resaurantTypeText.setText(list.get(position).getTypeRestaurant());
                    holder.weightText.setText(list.get(position).getWeight()+"Kg");
                    holder.timeText.setText(list.get(position).getTime());
                    holder.locationtText.setText(list.get(position).getLocation());


                    //holder.mainImage.setImageResource(list.get(position).getImage());



                    String uid = list.get(position).getUid();
                    String orderID = list.get(position).getOrderId();
                    String address = list.get(position).getAddress();

                    if (list.get(position).isFruits()) {
                        holder.fruitview.setVisibility(View.VISIBLE);
                    } else {
                        holder.fruitview.setVisibility(View.GONE);
                    }

                    if (list.get(position).isVegetables()) {
                        holder.veggiesview.setVisibility(View.VISIBLE);
                    } else {
                        holder.veggiesview.setVisibility(View.GONE);
                    }

                    if (list.get(position).isDairy()) {
                        holder.dairyview.setVisibility(View.VISIBLE);
                    } else {
                        holder.dairyview.setVisibility(View.GONE);
                    }

                    if (list.get(position).isDishes()) {
                        holder.dishesview.setVisibility(View.VISIBLE);
                    } else {
                        holder.dishesview.setVisibility(View.GONE);
                    }

                    if (list.get(position).isGrains()) {
                        holder.grainsview.setVisibility(View.VISIBLE);
                    } else {
                        holder.grainsview.setVisibility(View.GONE);
                    }

                    if (list.get(position).isMeat()) {
                        holder.meatview.setVisibility(View.VISIBLE);
                    } else {
                        holder.meatview.setVisibility(View.GONE);
                    }





                    Transformation transformation = new RoundedTransformationBuilder()
                            .borderColor(Color.BLACK)
                            .borderWidthDp(0)
                            .cornerRadiusDp(30)
                            .oval(false)
                            .build();

                    Picasso.get()
                            .load(list.get(position).getImage())
                            .resize(width,height)
                            .transform(transformation)
                            .centerCrop()
                            .into(holder.mainImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    holder.mainImage.setBackgroundColor(Color.TRANSPARENT);
                                    holder.loadingAnim.setVisibility(View.GONE);
                                    holder.loadingAnim.cancelAnimation();
                                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)
                                            holder.mainImage.getLayoutParams(); // View for which we need to set constrainedWidth.
                                    lp.constrainedWidth = true;lp.constrainedHeight = true;
                                    holder.mainImage.setLayoutParams(lp);
                                    holder.mainImage.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });



                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent((Activity)mContext, displayOrder.class);

                            intent.putExtra(Constants.uid_intent, uid);
                            intent.putExtra(Constants.orderId_intent, orderID);
                            intent.putExtra(Constants.name_intent, list.get(position).getName());
                            intent.putExtra(Constants.isDairy_intent,list.get(position).isDairy() );
                            intent.putExtra(Constants.isGrains_intent, list.get(position).isGrains());
                            intent.putExtra(Constants.isFruits_intent, list.get(position).isFruits());
                            intent.putExtra(Constants.isVeggies_intent, list.get(position).isVegetables());
                            intent.putExtra(Constants.isMeat_intent, list.get(position).isMeat());
                            intent.putExtra(Constants.isDishes_intent, list.get(position).isDishes());
                            intent.putExtra(Constants.address_intent, address);
                            intent.putExtra(Constants.total_weight_intent,list.get(position).getWeight());
                            if(newOrAccepted.equals("New")) {
                                intent.putExtra("From", "mainItem");
                            }
                            else
                            {
                                intent.putExtra("From", "ongoingNGOItem");
                            }
                            mContext.startActivity(intent);











                        }
                    });

                }






        // Override getItemCount which Returns
        // the length of the RecyclerView.
        @Override

        public int getItemCount() {
            return list == null ? 0 : list.size();
        }



    }


