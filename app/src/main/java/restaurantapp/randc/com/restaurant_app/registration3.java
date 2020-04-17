package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class registration3 extends AppCompatActivity {
    private Button next_button;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration3);
        mAuth = FirebaseAuth.getInstance();


        final List<String> spinnerList = Arrays.asList(Constants.foodItemList);
        next_button = findViewById(R.id.next_button);
        user = mAuth.getCurrentUser();
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "Email sent.");
                                }
                            }
                        });
                Map<String, Object> note = new HashMap<>();
                note.put("Name",getIntent().getStringExtra("name"));
                note.put("Email",getIntent().getStringExtra("email"));
                note.put("Address",getIntent().getStringExtra("address"));
                note.put("Phone Number",getIntent().getStringExtra("phno"));
                note.put("Pincode",getIntent().getStringExtra("pincode"));
                note.put("Url","");
                db.collection("Restaurant").document(getIntent().getStringExtra("name").trim()).set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(registration3.this, "Registration Succesfull! Verification email has been sent.", Toast.LENGTH_LONG).show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(registration3.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }, 1000);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(registration3.this, "Error!", Toast.LENGTH_SHORT).show();
                                Log.d("TAG", e.toString());
                            }
                        });

            }
        });


    }

}


