package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.KeyListener;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class profileClass extends AppCompatActivity {

    private FloatingActionMenu fab;
    private Button saveButton;
    private FloatingActionButton edit;
    private ImageButton save;
    private EditText emailView;
    private EditText AddressView;
    private EditText phnoView;
    private EditText nameView;
    private KeyListener listener;
    private KeyListener listener2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private ImageView userProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_class);


        user = FirebaseAuth.getInstance().getCurrentUser();
        fab = findViewById(R.id.fabfab);
        edit = findViewById(R.id.menu_item);
        saveButton = findViewById(R.id.saveButton);
        save = findViewById(R.id.saveTick);
        emailView = findViewById(R.id.emailView);
        phnoView = findViewById(R.id.phoneView);
        AddressView = findViewById(R.id.addressView);
        nameView = findViewById(R.id.nameView);
        userProfileImage = findViewById(R.id.profilePic);
        listener = emailView.getKeyListener();
        listener2 = phnoView.getKeyListener();


        nameView.setKeyListener(null);
        AddressView.setKeyListener(null);
        phnoView.setKeyListener(null);
        emailView.setKeyListener(null);

        db.collection("Restaurant").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            String url = documentSnapshot.getString("Url");

                            if(!(url.equals(""))) {
                                Picasso.get().load(url).into(userProfileImage);
                            }

                            phnoView.setText(documentSnapshot.getString("Phone Number"));
                            nameView.setText(documentSnapshot.getString("Name"));
                            AddressView.setText(documentSnapshot.getString("Address"));

                        } else {
                            nameView.setText("User not found");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profileClass.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.close(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab.setVisibility(View.GONE);
                        save.setVisibility(View.VISIBLE);

                    }
                }, 300);


                emailView.setPadding(25,25,25,25);
                emailView.setKeyListener(listener);
                emailView.setBackgroundResource(R.drawable.edittext_bg);

                phnoView.setPadding(25,25,25,25);
                phnoView.setKeyListener(listener2);
                phnoView.setBackgroundResource(R.drawable.edittext_bg);

                AddressView.setPadding(25,25,25,25);
                AddressView.setKeyListener(listener);
                AddressView.setBackgroundResource(R.drawable.edittext_bg);

                nameView.setPadding(25,25,25,25);
                nameView.setKeyListener(listener);
                nameView.setBackgroundResource(R.drawable.edittext_bg);

                saveButton.setVisibility(View.VISIBLE);


            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                save.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);

                String name = nameView.getText().toString().trim();
                String phno = phnoView.getText().toString().trim();
                String address = AddressView.getText().toString().trim();
                String email = emailView.getText().toString().trim();



                if(name.equals(""))
                    Toast.makeText(profileClass.this, "Name field is empty. Please fill all fields", Toast.LENGTH_LONG).show();
                else if(phno.equals(""))
                    Toast.makeText(profileClass.this, "Phone Number field is empty. Please fill all fields", Toast.LENGTH_LONG).show();
                else if(address.equals(""))
                    Toast.makeText(profileClass.this, "Address field is empty. Please fill all fields", Toast.LENGTH_LONG).show();
                else if(email.equals(""))
                    Toast.makeText(profileClass.this, "Email field is empty. Please fill all fields", Toast.LENGTH_LONG).show();

                else {
                   /* UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName("FoodWorld").build();

                    user.updateProfile(profileUpdates);*/
                    Map<String, Object> note = new HashMap<>();
                    note.put("Name", name);
                    note.put("Email", email);
                    note.put("Address", address);
                    note.put("Phone Number", phno);

                    AuthCredential credential = EmailAuthProvider
                            .getCredential("user@example.com", "password1234"); // Current Login Credentials \\
                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("TAG", "User re-authenticated.");
                                    //Now change your email address \\
                                    //----------------Code for Changing Email Address----------\\
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.updateEmail("user@example.com")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("TAG", "User email address updated.");
                                                    }
                                                }
                                            });
                                    //----------------------------------------------------------\\
                                }
                            });

                    db.collection("Restaurant").document(user.getUid()).update(note)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(profileClass.this, "Updated", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(profileClass.this, "Update failed", Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", e.toString());
                                }
                            });

                    nameView.setKeyListener(null);
                    AddressView.setKeyListener(null);
                    phnoView.setKeyListener(null);
                    emailView.setKeyListener(null);

                    emailView.setPadding(0,10,0,0);
                    emailView.setBackgroundColor(Color.TRANSPARENT);

                    phnoView.setPadding(0,10,0,0);
                    phnoView.setBackgroundColor(Color.TRANSPARENT);

                    AddressView.setPadding(0,10,0,0);
                    AddressView.setBackgroundColor(Color.TRANSPARENT);

                    nameView.setPadding(0,10,0,0);
                    nameView.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        });

    }

}

