package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class registration4 extends AppCompatActivity {
    private Button next_button;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Spinner typeSpinner;
    private TextView typeview;
    private TextView titleView;
    private TextView question1View;
    private ToggleSwitch freqToggle;
    private ProgressDialog dialog;
    private  String freq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration4);
        mAuth = FirebaseAuth.getInstance();
        titleView = findViewById(R.id.titleView);
        typeSpinner = findViewById(R.id.spinner3);
        freqToggle = findViewById(R.id.toggle);
        question1View = findViewById(R.id.textView6);
        dialog = new ProgressDialog(registration4.this);
        String[] check = {"Select State","Italian","South indian"};
        List<String> spinnerArray = Arrays.asList(check);
        typeview = findViewById(R.id.typeView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId,MODE_PRIVATE);
        if((sharedPreferences.getString("rType",null).equals("Restaurant")))
        {
            typeSpinner.setVisibility(View.VISIBLE);
            typeview.setVisibility(View.VISIBLE);

        }
        if((sharedPreferences.getString("rType",null).equals("NGO")))
        {
            titleView.setText("Tell us more about your Organisation");
            question1View.setText("Select how often you can receive food");

        }
        if((sharedPreferences.getString("rType",null).equals("Individual")))
        {
            typeSpinner.setVisibility(View.VISIBLE);
            typeview.setVisibility(View.VISIBLE);
            typeview.setText("Select the type of food you will donate");
            titleView.setText("Tell us more about yourself");
            question1View.setText("Select how often you can donate food");
        }


        next_button = findViewById(R.id.next_button);


        freqToggle.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){

            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position == 0 && isChecked)
                {
                    freq = "Daily";
                }
                if(position == 1 && isChecked)
                {
                    freq = "Weekly";
                }
                if(position == 2 && isChecked)
                {
                    freq = "Monthly";
                }
            }
        });


        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Creating Account...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                mAuth.createUserWithEmailAndPassword(sharedPreferences.getString("rEmail","ERROR"), sharedPreferences.getString("rPass","ERROR"))

                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "createUserWithEmail:success");
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(sharedPreferences.getString("rType","ERROR")).build();

                                    user.updateProfile(profileUpdates);

                                    // FirebaseDatabase.
//                                  FirebaseUser user = mAuth.getCurrentUser();
                                    user = mAuth.getCurrentUser();
                        //SEND VERIFICATION MAIL
                    /*  user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "Email sent.");
                                }u
                            }
                        });*/

                                    Map<String, Object> note = new HashMap<>();
                                    note.put("Name",sharedPreferences.getString("rName","ERROR"));
                                    note.put("Email",sharedPreferences.getString("rEmail","rEmail"));
                                    GeoPoint location  = new GeoPoint(getIntent().getDoubleExtra("Lat",0.0f),getIntent().getDoubleExtra("Lon",0.0f));
                                    note.put("Location",location);
                                    note.put("Address",getIntent().getStringExtra("Add"));
                                    note.put("Points",0);
                                    note.put("Number of donations",0);
                                    note.put("Phone Number",sharedPreferences.getString("rPhone","ERROR"));
                                    ArrayList<String> ngoOngoingList=new ArrayList<>();
                                    ArrayList<Boolean> orderArray=new ArrayList<>();
                                    for (int i=0;i<=4;i++)
                                    {
                                        orderArray.add(false);
                                    }

                                    if(sharedPreferences.getString("rType",null).equals("NGO")) {
                                        note.put("Url", "https://firebasestorage.googleapis.com/v0/b/restaurantapp-ab461.appspot.com/o/defaultNGO.png?alt=media&token=b0de6cb2-bce8-4c5e-9441-e1edadf3cbdf");
                                        note.put(Constants.ngo_ongoing_list_fire,ngoOngoingList);
                                    }
                                    else if (sharedPreferences.getString("rType",null).equals("Restaurant")) {
                                        note.put("Url", "https://firebasestorage.googleapis.com/v0/b/restaurantapp-ab461.appspot.com/o/defaultRestaurant.png?alt=media&token=31a9b9ce-03da-4e09-9128-b69c14c59322");
                                        note.put(Constants.order_id_num,orderArray);
                                    }
                                    else {
                                        note.put("Url", "https://firebasestorage.googleapis.com/v0/b/restaurantapp-ab461.appspot.com/o/default_induvidual.png?alt=media&token=c618b327-8dfd-458f-84a1-19b8023436b2");
                                        note.put(Constants.order_id_num, orderArray);
                                    }
                                    if(sharedPreferences.getString("rType",null).equals("Individual")||sharedPreferences.getString("rType",null).equals("Restaurant"))
                                        note.put("Type",typeSpinner.getSelectedItem().toString());
                                    else
                                        note.put("Type","");

                                    note.put("Frequency",freq);

                                    db.collection(sharedPreferences.getString("rType","ERROR")).document(user.getUid()).set(note)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(registration4.this, "Account Created!", Toast.LENGTH_SHORT).show();

                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.remove("rEmail");
                                                    editor.remove("rName");
                                                    editor.remove("rAddress");
                                                    editor.remove("rPhone");
                                                    editor.remove("rPincode");
                                                    editor.remove("rState");
                                                    editor.remove("rPass");
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(registration4.this, registration5.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    dialog.dismiss();
                                                    Toast.makeText(registration4.this, "Error!", Toast.LENGTH_SHORT).show();
                                                    Log.d("TAG", e.toString());
                                                }
                                            });





                                } else {
                                    // If sign in fails, display a message to the user.
                                    dialog.dismiss();
                                    Toast.makeText(registration4.this, "Error!", Toast.LENGTH_SHORT).show();
                                    Log.e("TAG", "createUserWithEmail:failure", task.getException());
                                    Intent intent = new Intent(registration4.this, registration1.class);
                                    startActivity(intent);
                                }
                            }

                            // ...
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(registration4.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("rEmail");
        editor.remove("rName");
        editor.remove("rPhone");
        editor.remove("rPass");
        editor.apply();
        Log.d("TAG","DONE4");
    }
}


