package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.auth.User;

public class registration1 extends AppCompatActivity {
    private Button next_button;
    private Button loginbutton;
    private FirebaseAuth mAuth;
    private EditText emailView;
    private EditText passwordView;
    private EditText passwordConfirm;
    private EditText name;
    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration1);
        mAuth = FirebaseAuth.getInstance();
        next_button = findViewById(R.id.next_button);
        emailView = findViewById(R.id.editText2);
        passwordConfirm = findViewById(R.id.editText3);
        name = findViewById(R.id.editText);
        loginbutton = findViewById(R.id.buttonlogin);
        passwordView= findViewById(R.id.editText5);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(registration1.this, registration2.class);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailView.getText().toString();
                password = passwordView.getText().toString();
                email = email.trim();
                password = password.trim();
                if(passwordConfirm.getText().toString().trim().equals("")||passwordView.getText().toString().trim().equals("")||emailView.getText().toString().trim().equals("")||name.getText().toString().trim().equals("")){
                    Toast.makeText(registration1.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    if(passwordConfirm.getText().toString().trim().equals(""))
                    {
                        passwordConfirm.setError("Enter Password again");
                    }
                    if(passwordView.getText().toString().trim().equals(""))
                    {
                        passwordView.setError("Enter Password");
                    }
                    if(emailView.getText().toString().trim().equals(""))
                    {
                        emailView.setError("Enter Email");
                    }
                    if(name.getText().toString().trim().equals(""))
                    {
                        name.setError("Enter Organisation Name");
                    }
                }
                else if(!(password.equals(passwordConfirm.getText().toString().trim())))
                {
                    Toast.makeText(registration1.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    passwordConfirm.setError("Confirm Password again");

                }

                else if (password.length() < 6) {
                    Toast.makeText(registration1.this, "Password must be atleast 6 charecters long", Toast.LENGTH_SHORT).show();
                    passwordView.setError("Password too short!");
                }

                else {
                    Log.d("TAG", "" + email + "   " + password);
                    mAuth.createUserWithEmailAndPassword(email, password)

                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {


                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "createUserWithEmail:success");
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name.getText().toString().trim()).build();

                                        user.updateProfile(profileUpdates);

                                        // FirebaseDatabase.
//                                  FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(registration1.this, "Success", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(registration1.this, registration2.class);
                                        intent.putExtra("name",name.getText().toString().trim());
                                        intent.putExtra("email",email);
                                        startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.e("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(registration1.this, "Email does not exist. Please enter valid email", Toast.LENGTH_SHORT).show();
                                        emailView.setError("Enter Valid Email");

                                    }
                                }

                                // ...
                            });
                }
            }

        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(registration1.this, loginpage.class);
                startActivity(intent);
            }
        });

    }

}