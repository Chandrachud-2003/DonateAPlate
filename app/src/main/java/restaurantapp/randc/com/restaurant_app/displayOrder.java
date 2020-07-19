package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;


import java.util.ArrayList;
import java.util.HashMap;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class displayOrder extends AppCompatActivity {

    private TextView nameText;
    private TextView requestsbardonatetxt;
    private TextView requestsbarreqtxt;
    private TextView norequeststext;
    private ImageButton backButton;
    private TextView addressText;
    private SmoothBottomBar categoriesBar;
    private TextView categoryName;
    private TextView categoryWeight;
    private Button completeButton;
    private String From;
    private RecyclerView displayOrderRecycler;
    private displayOrderAdapter mDisplayOrderAdapter;
    private LinearLayoutManager verticalLayout;
    private TextView totalWeightText;
    private ConstraintLayout requestLayout;
    private ConstraintLayout requestButton;
    private ConstraintLayout mainConstraintLayout;
    private View profileClick;
    private TextView requestText;
    private ProgressDialog dialog;
    private TextView totaltxt;
    private SmoothBottomBar requestsBar;
    private LottieAnimationView displayAnimation;
    private LottieAnimationView categoryLoadingAnimation;
    private LottieAnimationView loadingAnimation1;
    private LottieAnimationView loadingAnimation2;
    private LottieAnimationView loadingAnimation3;
    private String uid;
    private String orderID;
    private String  name;
    private String address;
    private String restid;
    private boolean isFruits;
    private boolean isVeggies;
    private boolean isMeat;
    private boolean isGrains;
    //private boolean isDishes;
    private boolean isDairy;
    private ImageView requestArrow;

    private DatabaseReference mDatabaseReference;
    private FirebaseFirestore db;


    private ArrayList<categoryItem> fruitsList;
    private ArrayList<categoryItem> veggiesList;
    private ArrayList<categoryItem> grainsList;
    private ArrayList<categoryItem> dairyList;
    private ArrayList<categoryItem> meatList;
    private ArrayList<displayRequestsItem> requestedItemList;


    private float total_Weight;
    private float fruitsWeight;
    private float grainsWeight;
    private float dairyWeight;
    private float veggiesWeight;
    private float meatWeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_order);


        Intent intent = getIntent();

        orderID = intent.getStringExtra(Constants.orderId_intent);

        isFruits = intent.getBooleanExtra(Constants.isFruits_intent, false);
        isDairy = intent.getBooleanExtra(Constants.isDairy_intent, false);
        isGrains = intent.getBooleanExtra(Constants.isGrains_intent, false);
        isVeggies = intent.getBooleanExtra(Constants.isVeggies_intent, false);
        isMeat = intent.getBooleanExtra(Constants.isMeat_intent, false);

        total_Weight = Float.parseFloat(intent.getStringExtra(Constants.total_weight_intent));

        From = intent.getStringExtra("From");


        Log.d("tag","From:"+From);
        if(From.equals("requestItem"))
        {

        }
        else if (From.equals("ongoingItem")||From.equals("ongoingNGOItem"))
        {
            uid = intent.getStringExtra(Constants.uid_intent);
            name = intent.getStringExtra(Constants.name_intent);
            address = intent.getStringExtra(Constants.address_intent);
        }
        else if (From.equals("mainItem"))
        {

            uid = intent.getStringExtra(Constants.uid_intent);
            name = intent.getStringExtra(Constants.name_intent);
            address = intent.getStringExtra(Constants.address_intent);

        }
        findViewsById();
        setOnClickOnListeners();



/*
        MenuObject mo1 = new MenuObject("Phone Call");
        mo1.setDrawable(getDrawable(R.drawable.icons8_phone2));
        mo1.setBgColorValue(R.color.phoneColor);
        ArrayList<MenuObject> menuObjects = new ArrayList<>();
        menuObjects.add(mo1);
        MenuParams menuParams = new MenuParams();
        menuParams.setMenuObjects(menuObjects);
        menuParams.setClosableOutside(true);
        // set other settings to meet your needs
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);*/
    }

    private void findViewsById()
    {
        displayAnimation = findViewById(R.id.displayLottieAnimation);
        categoryLoadingAnimation = findViewById(R.id.categoryLoadingAnimation);
        loadingAnimation2 = findViewById(R.id.loadingTextAnimation2);
        loadingAnimation1 = findViewById(R.id.loadingTextAnimation1);
        loadingAnimation3 = findViewById(R.id.loadingTextAnimation3);
        dialog = new ProgressDialog(displayOrder.this);
        profileClick = findViewById(R.id.profileClick);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screen_width = displayMetrics.widthPixels;

        displayAnimation.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                displayAnimation.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                displayAnimation.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }



            int newDimensions = (int) (screen_width*0.28);

            displayAnimation.getLayoutParams().width = newDimensions;


            displayAnimation.getLayoutParams().height = newDimensions;

            displayAnimation.playAnimation();


        }
    });

        categoryLoadingAnimation.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    categoryLoadingAnimation.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    categoryLoadingAnimation.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }



                int newDimensions = (int) (screen_width*0.472);

                categoryLoadingAnimation.getLayoutParams().width = newDimensions;


                categoryLoadingAnimation.getLayoutParams().height = newDimensions;

                categoryLoadingAnimation.playAnimation();


            }
        });

        loadingAnimation1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    loadingAnimation1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    loadingAnimation1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }



                int newDimensions = (int) (screen_width*0.139);

                loadingAnimation1.getLayoutParams().width = newDimensions;


                loadingAnimation1.getLayoutParams().height = newDimensions;

                loadingAnimation1.playAnimation();


            }
        });

        loadingAnimation2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    loadingAnimation2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    loadingAnimation2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }



                int newDimensions = (int) (screen_width*0.139);

                loadingAnimation2.getLayoutParams().width = newDimensions;


                loadingAnimation2.getLayoutParams().height = newDimensions;

                loadingAnimation2.playAnimation();


            }
        });


        loadingAnimation3.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    loadingAnimation3.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    loadingAnimation3.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }



                int newDimensions = (int) (screen_width*0.024);

                loadingAnimation3.getLayoutParams().width = newDimensions;


                loadingAnimation3.getLayoutParams().height = newDimensions;

                loadingAnimation3.playAnimation();


            }
        });


        requestArrow = findViewById(R.id.requestArrow);
        nameText = findViewById(R.id.nameText);
        nameText.setText(name);
        backButton = findViewById(R.id.backButton);
        totaltxt = findViewById(R.id.totalText);
        addressText = findViewById(R.id.addressText);
        addressText.setText(address);
        requestsbardonatetxt = findViewById(R.id.donatetext);
        completeButton = findViewById(R.id.completeButton);
        requestsbarreqtxt = findViewById(R.id.requeststext);
        categoriesBar = findViewById(R.id.bubbleBottomSheetBar);
        categoryName = findViewById(R.id.categoryHeading);
        categoryWeight = findViewById(R.id.categoryWeight);
        mainConstraintLayout = findViewById(R.id.mainlayout);
        norequeststext = findViewById(R.id.noRequestsText);
        requestsBar = findViewById(R.id.bubbleRequestBar);
        displayOrderRecycler = findViewById(R.id.categoryRecycler);
        requestText = findViewById(R.id.requestText);
        requestLayout = findViewById(R.id.requestLayout);
        verticalLayout = new LinearLayoutManager(
                displayOrder.this,
                LinearLayoutManager.VERTICAL,
                false);

        totalWeightText = findViewById(R.id.totalWeight);
        requestButton = findViewById(R.id.requestButtonLayout);
        if(From.equals("requestItem"))
        {
            requestsbardonatetxt.setVisibility(View.VISIBLE);
            requestsbarreqtxt.setVisibility(View.VISIBLE);
            requestsBar.setVisibility(View.VISIBLE);
            requestsBar.setItemActiveIndex(1);
            categoriesBar.setVisibility(View.GONE);
        }

        if (isFruits)
        {
            categoriesBar.setItemActiveIndex(0);
        }
        else if (isVeggies)
        {
            categoriesBar.setItemActiveIndex(1);

        }

        else if (isDairy)
        {
            categoriesBar.setItemActiveIndex(2);

        }

        else if (isGrains)
        {
            categoriesBar.setItemActiveIndex(3);

        }

        else if (isMeat)
        {
            categoriesBar.setItemActiveIndex(4);
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        fruitsList = new ArrayList<>();
        veggiesList = new ArrayList<>();
        grainsList = new ArrayList<>();
        meatList = new ArrayList<>();
        dairyList = new ArrayList<>();

        requestedItemList = new ArrayList<>();

        fruitsWeight =0;
        veggiesWeight=0;
        meatWeight=0;
        grainsWeight=0;
        dairyWeight=0;

        GetBackgroundInfo backgroundInfo = new GetBackgroundInfo();
        backgroundInfo.execute();

        float density = displayOrder.this.getResources()
                .getDisplayMetrics()
                .density;
        if(From.equals("mainItem"))
        {
            requestButton.setClickable(false);
        }
        if(From.equals("requestItem"))
        {

            requestText.setText("Remove");
            requestButton.setBackground(ContextCompat.getDrawable(displayOrder.this, R.drawable.cancel_button_bg));
            requestButton.setPadding((int)(20 * density),(int)(5 * density),(int)(20 * density),(int)(5 * density));
            requestArrow.setImageResource(R.drawable.cancel_white);
        }
        if(From.equals("ongoingItem")||From.equals("ongoingNGOItem"))
        {
            requestButton.setVisibility(View.GONE);
            completeButton.setVisibility(View.VISIBLE);
            completeButton.setClickable(false);
            requestLayout.setBackgroundColor(getResources().getColor(R.color.displayOrderGray));
            mainConstraintLayout.setBackgroundColor(getResources().getColor(R.color.displayOrderGray));
            totaltxt.setVisibility(View.GONE);
            totalWeightText.setVisibility(View.GONE);
        }

    }

    private void setOnClickOnListeners()
    {
        profileClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(displayOrder.this, profileClass.class);
                intent.putExtra("uid",uid);
                intent.putExtra("From",From);
                startActivity(intent);
            }
        });

        PushDownAnim.setPushDownAnimTo(backButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(displayOrder.this, Main_Activity.class);
                        startActivity(intent);
                    }


    });


        PushDownAnim.setPushDownAnimTo(completeButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        AlertDialog.Builder builder = new AlertDialog.Builder(displayOrder.this);
                        builder.setCancelable(true);
                        builder.setTitle("Complete Donation");
                        if(From.equals("ongoingItem"))
                            builder.setMessage("Are you sure want to complete your donation? \n(Click yes only if the donation has been handed over to the NGO)");
                        else
                            builder.setMessage("Are you sure want to complete your donation? \n(Click yes only if you have received the donation)");
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                            }
                        });
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();

                                dialog.setMessage("Completing Donation...");
                                dialog.show();
                                mDatabaseReference.child("Orders").child(orderID).child("Info").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshotInfo) {
                                        if (snapshotInfo.child("State").getValue().toString().equals("CompletedRest")||snapshotInfo.child("State").getValue().toString().equals("CompletedNGO")) {
                                            DocumentReference orderRef=null;

                                            if(From.equals("ongoingItem"))
                                              orderRef = db.collection(Constants.ngo_fire).document(uid);
                                            else if(From.equals("ongoingNGOItem"))
                                                orderRef = db.collection(Constants.ngo_fire).document(user.getUid());
                                                orderRef.update("Number of donations", FieldValue.increment(1));
                                                orderRef.update("Points", FieldValue.increment(50));
                                               orderRef.update(Constants.ngo_ongoing_list_fire, FieldValue.arrayRemove(orderID))
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            restid="";
                                                            if(From.equals("ongoingItem"))
                                                                restid = user.getUid();
                                                            else if(From.equals("ongoingNGOItem"))
                                                                restid = uid;

                                                            db.collection(Constants.rest_fire).document(restid).get()
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                            if (documentSnapshot.exists()) {
                                                                                ArrayList<Boolean> orderNum;
                                                                                orderNum = (ArrayList) documentSnapshot.get(Constants.order_id_num);
                                                                                char no = orderID.charAt(orderID.length()-1);
                                                                                int number = Integer.parseInt(""+no);
                                                                                orderNum.set(number,false);

                                                                                HashMap<String, Object> updateMap = new HashMap<>();
                                                                                updateMap.put(Constants.order_id_num, orderNum);
                                                                                db.collection(Constants.rest_fire).document(restid).update("Number of donations", FieldValue.increment(1));
                                                                                db.collection(Constants.rest_fire).document(restid).update("Points", FieldValue.increment(50));
                                                                                db.collection(Constants.rest_fire).document(restid)
                                                                                        .update(updateMap)
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                                mDatabaseReference.child("Orders").child(orderID).removeValue()
                                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void aVoid) {
                                                                                                                dialog.dismiss();

                                                                                                                Toast.makeText(displayOrder.this,"Donation Completed",Toast.LENGTH_LONG).show();
                                                                                                                Log.d("tag","Donation Completion Success");
                                                                                                                Intent intent = new Intent(displayOrder.this,Main_Activity.class);
                                                                                                                startActivity(intent);
                                                                                                            }
                                                                                                        })
                                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                                            @Override
                                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                                Log.d(Constants.tag, "error: " + e + " add");
                                                                                                                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                                                                                                                dialog.dismiss();
                                                                                                            }
                                                                                                        });
                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                Log.d(Constants.tag, "error: " + e + " add");
                                                                                                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                                                                                                dialog.dismiss();
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.d(Constants.tag, "error: " + e + " add");
                                                                    Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d(Constants.tag, "error: " + e + " add");
                                                            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                                                            dialog.dismiss();
                                                        }
                                                    });

                                        }
                                        else
                                        {
                                            String value = "";
                                            if(From.equals("ongoingItem"))
                                                value = "CompletedRest";
                                            else if(From.equals("ongoingNGOItem"))
                                                value = "CompletedNGO";

                                            mDatabaseReference.child("Orders").child(orderID).child("Info").child("State").setValue(value)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            if(From.equals("ongoingItem"))
                                                                completeButton.setText("Awaiting NGO's confirmation");
                                                            else if(From.equals("ongoingNGOItem"))
                                                                completeButton.setText("Awaiting confirmation");
                                                            completeButton.setClickable(false);
                                                            dialog.dismiss();

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d(Constants.tag, "error: " + e + " add");
                                                            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                                                            dialog.dismiss();
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.d(Constants.tag, "error: " + error + " add");
                                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });


                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });


        PushDownAnim.setPushDownAnimTo(requestButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if(From.equals("mainItem")) {
                            String id = user.getUid();
                            db.collection("NGO").document(id).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                mDatabaseReference.child("Orders").child(orderID).child("Requests").push().setValue(id + ";;"+ documentSnapshot.get("Name").toString())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d(Constants.tag, "Request has been sent");
                                                                requestArrow.setImageResource(R.drawable.tick_white);
                                                                requestText.setText("Requested");
                                                                requestButton.setClickable(false);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d(Constants.tag, "error: " + e + " add");
                                                                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(Constants.tag, "error: " + e + " add");
                                    Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                                }
                            });


                        }
                        if(From.equals("requestItem"))
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(displayOrder.this);
                            builder.setCancelable(true);
                            builder.setTitle("Remove Donation");
                            builder.setMessage("Are you sure want to remove your donation? \n(All requests will be automatically declined)");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                    dialog1.dismiss();

                                    dialog.setMessage("Removing Donation...");
                                    dialog.show();
                                    DocumentReference orderRef = db.collection(Constants.orderName_fire).document(Constants.order_list_fire);
                                    orderRef.update(Constants.order_list_field, FieldValue.arrayRemove(orderID))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    db.collection(Constants.rest_fire).document(user.getUid()).get()
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                    if (documentSnapshot.exists()) {
                                                                        ArrayList<Boolean> orderNum;
                                                                        orderNum = (ArrayList) documentSnapshot.get(Constants.order_id_num);
                                                                        char no = orderID.charAt(orderID.length()-1);
                                                                        int number = Integer.parseInt(""+no);
                                                                        orderNum.set(number,false);

                                                                        HashMap<String, Object> updateMap = new HashMap<>();
                                                                        updateMap.put(Constants.order_id_num, orderNum);
                                                                        db.collection(Constants.rest_fire).document(user.getUid())
                                                                                .update(updateMap)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {

                                                                                        mDatabaseReference.child("Orders").child(orderID).removeValue()
                                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                        dialog.dismiss();
                                                                                                        Toast.makeText(displayOrder.this,"Donation Removed",Toast.LENGTH_LONG).show();
                                                                                                        Log.d("tag","Donation Remove Success");
                                                                                                        Intent intent = new Intent(displayOrder.this,Main_Activity.class);
                                                                                                        startActivity(intent);
                                                                                                    }
                                                                                                })
                                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                                    @Override
                                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                                        Log.d(Constants.tag, "error: " + e + " add");
                                                                                                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                                                                                                        dialog.dismiss();
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Log.d(Constants.tag, "error: " + e + " add");
                                                                                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d(Constants.tag, "error: " + e + " add");
                                                            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(Constants.tag, "error: " + e + " add");
                                                    Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();
                                                }
                                            });
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }
                        if (From.equals("ongoingItem"))
                        {


                        }



                    }


                });

        categoriesBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {

                if (i==0)
                {
                    categoryName.setText("Fruits Summary");
                    if ( isFruits && fruitsList.size()>0 && fruitsWeight>0.0f) {
                        displayOrderRecycler.setVisibility(View.VISIBLE);
                        mDisplayOrderAdapter = new displayOrderAdapter(fruitsList);
                        displayOrderRecycler.setAdapter(mDisplayOrderAdapter);
                        categoryWeight.setText(Float.toString(fruitsWeight)+"kg");
                    }
                    else {
                        categoryWeight.setText("0.0kg");
                        displayOrderRecycler.setVisibility(View.INVISIBLE);
                    }
                }

                else if (i==1)
                {

                    categoryName.setText("Vegetables Summary");
                    if ( isVeggies && veggiesList.size()>0 && veggiesWeight>0.0f) {
                        displayOrderRecycler.setVisibility(View.VISIBLE);
                        mDisplayOrderAdapter = new displayOrderAdapter(veggiesList);
                        displayOrderRecycler.setAdapter(mDisplayOrderAdapter);
                        categoryWeight.setText(Float.toString(veggiesWeight)+"kg");
                    }
                    else {
                        categoryWeight.setText("0.0kg");

                        displayOrderRecycler.setVisibility(View.INVISIBLE);
                    }
                }

                else if (i==2 )
                {

                    categoryName.setText("Dairy Summary");
                    if (isDairy && dairyList.size()>0 && dairyWeight>0.0f) {
                        displayOrderRecycler.setVisibility(View.VISIBLE);
                        mDisplayOrderAdapter = new displayOrderAdapter(dairyList);
                        displayOrderRecycler.setAdapter(mDisplayOrderAdapter);
                        categoryWeight.setText(Float.toString(dairyWeight)+"kg");
                    }
                    else {
                        categoryWeight.setText("0.0kg");

                        displayOrderRecycler.setVisibility(View.INVISIBLE);
                    }
                }

                else if (i==3 )
                {

                    categoryName.setText("Grains Summary");
                    if (isGrains && grainsList.size()>0 && grainsWeight>0.0f) {
                        displayOrderRecycler.setVisibility(View.VISIBLE);
                        mDisplayOrderAdapter = new displayOrderAdapter(grainsList);
                        displayOrderRecycler.setAdapter(mDisplayOrderAdapter);
                        categoryWeight.setText(Float.toString(grainsWeight)+"kg");
                    }
                    else {
                        categoryWeight.setText("0.0kg");

                        displayOrderRecycler.setVisibility(View.INVISIBLE);
                    }
                }

                else if (i==4)
                {
                    categoryName.setText("Meat Summary");
                    if (isMeat && meatList.size()>0 && meatWeight>0.0f) {
                        displayOrderRecycler.setVisibility(View.VISIBLE);
                        mDisplayOrderAdapter = new displayOrderAdapter(meatList);
                        displayOrderRecycler.setAdapter(mDisplayOrderAdapter);
                        categoryWeight.setText(Float.toString(meatWeight)+"kg");
                    }
                    else {
                        categoryWeight.setText("0.0kg");

                        displayOrderRecycler.setVisibility(View.INVISIBLE);
                    }
                }

                return false;
            }
        });
        requestsBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                if (i==0)
                {
                    categoriesBar.setVisibility(View.VISIBLE);
                    categoriesBar.setItemActiveIndex(0);
                    categoryWeight.setVisibility(View.VISIBLE);
                    norequeststext.setVisibility(View.GONE);
                    categoryName.setText("Fruits Summary");
                    if ( isFruits && fruitsList.size()>0 && fruitsWeight>0.0f) {
                        displayOrderRecycler.setVisibility(View.VISIBLE);
                        mDisplayOrderAdapter = new displayOrderAdapter(fruitsList);
                        displayOrderRecycler.setAdapter(mDisplayOrderAdapter);
                        categoryWeight.setText(Float.toString(fruitsWeight)+"kg");
                    }
                    else {
                        categoryWeight.setText("0.0kg");
                        displayOrderRecycler.setVisibility(View.INVISIBLE);
                    }
                    totalWeightText.setText(Float.toString(total_Weight) + "kg");
                }

                else if (i==1)
                {
                    displayOrderRecycler.setVisibility(View.VISIBLE);
                    categoriesBar.setVisibility(View.GONE);
                    categoryName.setText("Requests");
                    displayOrderRecycler.setAdapter(new displayRequestsAdapter(displayOrder.this,requestedItemList));
                    categoryWeight.setVisibility(View.GONE);
                    if(requestedItemList.size()==0)
                    {
                        norequeststext.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });

    }

    private float getTotalWeight(ArrayList<categoryItem> list)
    {
        float weight=0.0f;

        for (int i=0;i<list.size();i++)
        {
            weight+=list.get(i).getFoodWeight();
        }


        return weight;

    }

    private class GetBackgroundInfo extends AsyncTask<Void, Integer, Boolean> {

        //private final WeakReference<Activity> weakActivity;

        GetBackgroundInfo() {
            //this.weakActivity = new WeakReference<>(myActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TAG", "onPreExecute: task started");


        }

        protected Boolean doInBackground(Void... voids) {
            // code that will run in the background

            // Activity activity = weakActivity.get();

            Log.d("TAG", "doInBackground: task running");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if(From.equals("mainItem")) {
                mDatabaseReference.child("Orders").child(orderID).child("Requests").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.d("TAG", "doInBackground: checking for requests");
                            boolean found = false;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String req = snapshot.getValue().toString();
                                String[] split = req.split(";;");
                                Log.d("tag","split id:"+ split[0]);
                                if (split[0].equals(user.getUid())) {
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                requestArrow.setImageResource(R.drawable.tick_white);
                                requestText.setText("Requested");
                                requestButton.setClickable(false);
                            }
                            else
                            {
                                requestButton.setClickable(true);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(Constants.tag, "error: " + databaseError + " add");
                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }

            if(From.equals("requestItem"))
            {
                mDatabaseReference.child("Orders").child(orderID).child("Requests").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            norequeststext.setVisibility(View.GONE);
                            Log.d("TAG", "doInBackground: getting requests");
                            boolean found = false;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String req = snapshot.getValue().toString();
                                String[] split = req.split(";;");
                                requestedItemList.add(new displayRequestsItem(split[0],split[1],orderID));

                            }
                            onPostExecute(true);
                        }
                        else
                        {
                            norequeststext.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(Constants.tag, "error: " + databaseError + " add");
                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
            if(From.equals("ongoingItem"))
            {
                mDatabaseReference.child("Orders").child(orderID).child("Info").child("State").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue().toString().equals("CompletedRest"))
                        {
                            completeButton.setText("Awaiting NGO's confirmation");
                            completeButton.setClickable(false);
                        }
                        else
                        {
                            completeButton.setClickable(true);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(Constants.tag, "error: " + databaseError + " add");
                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
            if(From.equals("ongoingNGOItem"))
            {
                mDatabaseReference.child("Orders").child(orderID).child("Info").child("State").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue().toString().equals("CompletedNGO"))
                        {
                            completeButton.setText("Awaiting confirmation");
                            completeButton.setClickable(false);
                        }
                        else
                        {
                            completeButton.setClickable(true);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(Constants.tag, "error: " + databaseError + " add");
                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }

            mDatabaseReference.child(Constants.orderName_fire).child(orderID).child("Food").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    GenericTypeIndicator<ArrayList<categoryItem>> t = new GenericTypeIndicator<ArrayList<categoryItem>>() {};


                    if (isFruits && snapshot.hasChild(Constants.fruitName_fire))
                    {

                        fruitsList = snapshot.child(Constants.fruitName_fire).child("Items").getValue(t);
                        if (fruitsList != null &&fruitsList.size()>0) {
                            fruitsWeight = getTotalWeight(fruitsList);
                        }



                    }

                    if (isVeggies && snapshot.hasChild(Constants.vegName_fire))
                    {

                        veggiesList = snapshot.child(Constants.vegName_fire).child("Items").getValue(t);
                        if (veggiesList != null &&veggiesList.size()>0) {
                            veggiesWeight = getTotalWeight(veggiesList);
                        }

                    }

                    if (isDairy && snapshot.hasChild(Constants.dairyName_fire))
                    {

                        dairyList = snapshot.child(Constants.dairyName_fire).child("Items").getValue(t);
                        if (dairyList != null &&dairyList.size()>0) {
                            dairyWeight = getTotalWeight(dairyList);
                        }

                    }

                    if (isGrains && snapshot.hasChild(Constants.grainsName_fire))
                    {

                        grainsList = snapshot.child(Constants.grainsName_fire).child("Items").getValue(t);
                        if (grainsList != null &&grainsList.size()>0) {
                            grainsWeight = getTotalWeight(grainsList);
                        }

                    }

                    if (isMeat && snapshot.hasChild(Constants.meatName_fire))
                    {

                        meatList = snapshot.child(Constants.meatName_fire).child("Items").getValue(t);
                        if (meatList != null &&meatList.size()>0) {
                            meatWeight = getTotalWeight(meatList);
                        }

                    }

                    onPostExecute(true);



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    onPostExecute(false);


                }
            });





            return null;
        }


        @Override
        protected void onPostExecute(Boolean done) {
            super.onPostExecute(done);

            if (done!=null)
            {
                Log.d("TAG", "onPostExecute: task completed : "+done);

                if (done)
                {
                    if(From.equals("mainItem")||From.equals("ongoingItem")||From.equals("ongoingNGOItem")) {
                        float currentWeight = 0.0f;
                        String currentCategory = "";
                        if (isFruits && fruitsList != null && fruitsList.size() > 0) {

                            mDisplayOrderAdapter = new displayOrderAdapter(fruitsList);
                            categoriesBar.setItemActiveIndex(0);
                            currentCategory = "Fruits";
                            currentWeight = fruitsWeight;
                        } else if (isVeggies && veggiesList != null && veggiesList.size() > 0) {
                            mDisplayOrderAdapter = new displayOrderAdapter(veggiesList);
                            categoriesBar.setItemActiveIndex(1);
                            currentCategory = "Vegetables";
                            currentWeight = veggiesWeight;

                        } else if (isDairy && dairyList != null && dairyList.size() > 0) {
                            mDisplayOrderAdapter = new displayOrderAdapter(dairyList);
                            categoriesBar.setItemActiveIndex(2);
                            currentCategory = "Dairy";
                            currentWeight = dairyWeight;

                        } else if (isGrains && grainsList != null && grainsList.size() > 0) {
                            mDisplayOrderAdapter = new displayOrderAdapter(grainsList);
                            categoriesBar.setItemActiveIndex(3);
                            currentCategory = "Grains";
                            currentWeight = grainsWeight;

                        } else if (isMeat && meatList != null && meatList.size() > 0) {
                            mDisplayOrderAdapter = new displayOrderAdapter(meatList);
                            categoriesBar.setItemActiveIndex(4);
                            currentCategory = "Meat";
                            currentWeight = meatWeight;

                        }

                        Log.d("TAG", "onPostExecute: ");

                        loadingAnimation1.setVisibility(View.INVISIBLE);
                        loadingAnimation1.cancelAnimation();
                        categoryName.setText(currentCategory + " Summary");
                        loadingAnimation2.setVisibility(View.INVISIBLE);
                        loadingAnimation2.cancelAnimation();
                        categoryWeight.setText(Float.toString(currentWeight) + "kg");

                        loadingAnimation3.setVisibility(View.INVISIBLE);
                        loadingAnimation3.cancelAnimation();
                        totalWeightText.setText(Float.toString(total_Weight) + "kg");

                        displayOrderRecycler.setLayoutManager(verticalLayout);
                        displayOrderRecycler.setItemAnimator(new DefaultItemAnimator());

                        displayOrderRecycler.setAdapter(mDisplayOrderAdapter);

                        categoryLoadingAnimation.setVisibility(View.INVISIBLE);
                        categoryLoadingAnimation.cancelAnimation();
                    }
                    if(From.equals("requestItem"))
                    {

                        displayOrderRecycler.setLayoutManager(verticalLayout);
                        displayOrderRecycler.setItemAnimator(new DefaultItemAnimator());
                        displayOrderRecycler.setAdapter(new displayRequestsAdapter(displayOrder.this,requestedItemList));
                        categoryName.setText("Requests");
                        totalWeightText.setText(Float.toString(total_Weight) + "kg");
                        loadingAnimation1.setVisibility(View.INVISIBLE);
                        loadingAnimation1.cancelAnimation();
                        loadingAnimation2.setVisibility(View.INVISIBLE);
                        loadingAnimation2.cancelAnimation();
                        loadingAnimation3.setVisibility(View.INVISIBLE);
                        loadingAnimation3.cancelAnimation();
                        categoryLoadingAnimation.setVisibility(View.INVISIBLE);
                        categoryLoadingAnimation.cancelAnimation();

                    }

                }

                else {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(displayOrder.this);
                    builder.setTitle("Order Retrieval Error");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(displayOrder.this, Main_Activity.class);
                            dialogInterface.dismiss();
                            startActivity(intent);

                        }
                    });

                    builder.setMessage("Something went wrong while retrieving the Donation, please Try Again");

                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();

                }
            }


        }


    }

   /* @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                mMenuDialogFragment.show(fragmentManager, "ContextMenuDialogFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

}

