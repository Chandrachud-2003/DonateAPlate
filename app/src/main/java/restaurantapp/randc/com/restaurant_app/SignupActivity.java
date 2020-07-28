package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

public class SignupActivity extends AppCompatActivity {

    private CardView next_button;
    private TextView loginbutton;
    private EditText emailView;
    private EditText passwordView;
    private EditText passwordConfirm;
    private EditText phno;
    private EditText nameView;
    private String email;
    private FirebaseAuth mAuth;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        phno = findViewById(R.id.phoneView);
        nameView = findViewById(R.id.nameView);
        next_button = findViewById(R.id.nextButton);
        mAuth = FirebaseAuth.getInstance();
        emailView = findViewById(R.id.emailView);
        passwordConfirm = findViewById(R.id.confirmView);
        loginbutton = findViewById(R.id.signin);
        passwordView = findViewById(R.id.passwordView);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailView.getText().toString();
                password = passwordView.getText().toString();
                email = email.trim();
                password = password.trim();


                if (passwordConfirm.getText().toString().trim().equals("") || passwordView.getText().toString().trim().equals("") || emailView.getText().toString().trim().equals("")||phno.getText().toString().trim().equals("") || nameView.getText().toString().trim().equals("")) {
                    Toast.makeText(SignupActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    if (passwordConfirm.getText().toString().trim().equals("")) {
                        passwordConfirm.setError("Enter Password again");
                    }
                    if (passwordView.getText().toString().trim().equals("")) {
                        passwordView.setError("Enter Password");
                    }
                    if (emailView.getText().toString().trim().equals("")) {
                        emailView.setError("Enter Email");
                    }
                    if (phno.getText().toString().trim().equals("")) {
                        phno.setError("Enter Phone number");
                    }
                    if (nameView.getText().toString().trim().equals("")) {
                        nameView.setError("Enter Name");
                    }

                } else if (!(password.equals(passwordConfirm.getText().toString().trim()))) {
                    Toast.makeText(SignupActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    passwordConfirm.setError("Confirm Password again");

                } else if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Password must be atleast 6 charecters long", Toast.LENGTH_SHORT).show();
                    passwordView.setError("Password too short!");
                }
                else if(!(isEmailValid(email))){
                    emailView.setError("Invalid Email");
                    Toast.makeText(SignupActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
                else {


                    mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.isSuccessful()) {
                                SignInMethodQueryResult result = task.getResult();
                                List<String> signInMethods = result.getSignInMethods();
                                if(signInMethods.size()==0)
                                {
                                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    //Todo Type has to be selected
                                    editor.putString("rType","Restaurant");
                                    editor.apply();

                                    editor.putString("rEmail", email);
                                    editor.putString("rPass", password);
                                    editor.putString("rPhone", phno.getText().toString().trim());
                                    editor.putString("rName", nameView.getText().toString().trim());
                                    editor.apply();
                                    Intent intent = new Intent(SignupActivity.this, registration3.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    emailView.setError("Email already exists");
                                    Toast.makeText(SignupActivity.this, "An account with this email already exists!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                    });




                }
            }

        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        // Disabling back button for current activity
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
        editor.remove("rType");
        editor.apply();
        Log.d("TAG","DONE2");
    }

}