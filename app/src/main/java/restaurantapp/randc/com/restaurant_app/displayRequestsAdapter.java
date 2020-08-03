package restaurantapp.randc.com.restaurant_app;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class displayRequestsAdapter extends RecyclerView.Adapter<displayOrderAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<displayRequestsItem> list;
    private ProgressDialog dialog;

    // RecyclerView recyclerView;
    public displayRequestsAdapter(Context context, ArrayList<displayRequestsItem> list) {
        this.list = list;
        this.mContext = context;
        dialog = new ProgressDialog(context);
    }

    @Override
    public displayOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.display_order_recycler_item, parent, false);
        displayOrderAdapter.ViewHolder viewHolder = new displayOrderAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(displayOrderAdapter.ViewHolder holder, int position) {
        final displayRequestsItem item = list.get(position);
        holder.weightText.setVisibility(View.GONE);
        holder.nameText.setText(item.getName());
        holder.viewProfie.setVisibility(View.VISIBLE);
        holder.accept.setVisibility(View.VISIBLE);

        holder.viewProfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((Activity) mContext, profileClass.class);
                intent.putExtra("uid", item.getUID());
                intent.putExtra("From", "ongoingItem");
                mContext.startActivity(intent);
            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Accepting Donation Request...");
                dialog.show();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> infoMap = new HashMap<>();
                infoMap.put("State", "Ongoing");
                infoMap.put("Accepted", item.getUID());
                mDatabase.child("Orders").child(item.getOrderID()).child("Info").updateChildren(infoMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                DocumentReference orderRef = db.collection(Constants.orderName_fire).document(Constants.order_list_fire);
                                orderRef.update(Constants.order_list_field, FieldValue.arrayRemove(item.getOrderID()))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                DocumentReference orderRef = db.collection(Constants.ngo_fire).document(item.getUID());
                                                orderRef.update(Constants.ngo_ongoing_list_fire, FieldValue.arrayUnion(item.getOrderID()))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d(Constants.tag, "Accept Success");
                                                                dialog.dismiss();
                                                                Toast.makeText(mContext, "Request Accepted", Toast.LENGTH_SHORT).show();
                                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                                db.collection(Constants.rest_fire).document(user.getUid()).get()
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                if (documentSnapshot.exists()) {
                                                                                    mDatabase.child(Constants.notifications).child(item.getUID()).child(Constants.notify_fire).setValue(true);
                                                                                    mDatabase.child(Constants.notifications).child(item.getUID()).child(Constants.notifyText_fire).push().setValue(documentSnapshot.get("Name").toString() + " accepted your donation request!");
                                                                                }
                                                                            }
                                                                        });


                                                                Intent intent = new Intent((Activity) mContext, Main_Activity.class);
                                                                mContext.startActivity(intent);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                dialog.dismiss();
                                                                Log.d(Constants.tag, "error: " + e + " add");
                                                                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialog.dismiss();
                                                Log.d(Constants.tag, "error: " + e + " add");
                                                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Log.d(Constants.tag, "error: " + e + " add");
                                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView weightText;
        public Button viewProfie;
        public Button accept;

        public ViewHolder(View itemView) {
            super(itemView);
            this.nameText = (TextView) itemView.findViewById(R.id.foodName);
            this.weightText = (TextView) itemView.findViewById(R.id.foodWeight);
            this.viewProfie = itemView.findViewById(R.id.ViewButton);
            this.accept = itemView.findViewById(R.id.acceptButton);

        }
    }
}