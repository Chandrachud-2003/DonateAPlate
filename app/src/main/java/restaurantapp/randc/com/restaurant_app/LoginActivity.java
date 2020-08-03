package restaurantapp.randc.com.restaurant_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class LoginActivity extends AppCompatActivity {


    private TextView registrationbutton;
    private TextView resetbutton;
    private FirebaseAuth mAuth;
    private CardView signinButton;
    private String email;
    private String password;
    private EditText emailView;
    private EditText passwordView;
    private ProgressDialog lbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, Main_Activity.class);
            startActivity(intent);
        }
        else {
            lbar = new ProgressDialog(this);


            setContentView(R.layout.activity_login);
            signinButton = findViewById(R.id.loginButton);
            registrationbutton = findViewById(R.id.createnewaccButton);

            resetbutton = findViewById(R.id.forgotPass);
            emailView = findViewById(R.id.emailView);
            passwordView = findViewById(R.id.passwordView);
            registrationbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            });
            resetbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, resetpassword.class);
                    startActivity(intent);

                }
            });
            signinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    email = emailView.getText().toString().trim();
                    password = passwordView.getText().toString().trim();
                    if (emailView.getText().toString().trim().equals("")) {
                        Toast.makeText(LoginActivity.this, "Enter Email and Password", Toast.LENGTH_SHORT).show();
                        emailView.setError("Enter Email");
                    } else if (passwordView.getText().toString().trim().equals("")) {
                        Toast.makeText(LoginActivity.this, "Enter Email and Password", Toast.LENGTH_SHORT).show();
                        passwordView.setError("Enter Password");
                    } else if (!(isEmailValid(email))) {
                        emailView.setError("Invalid Email");
                        Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    } else {

                        lbar.setCanceledOnTouchOutside(false);
                        lbar.setMessage("Logging in...");
                        lbar.setCanceledOnTouchOutside(false);
                        lbar.show();
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information

                                            Log.d("TAG", "signInWithEmail:success");
                                            FirebaseAuth auth = FirebaseAuth.getInstance();

                                            //getSharedPreferences(Constants.sharedPrefId, MODE_PRIVATE).edit().putString(Constants.userIdPref, mAuth.getUid()).apply();


                                            PeriodicWorkRequest.Builder periodicWorkRequest =
                                                    new PeriodicWorkRequest.Builder(BackgroundWork.class, 15,
                                                            TimeUnit.MINUTES);
                                            PeriodicWorkRequest periodicWork = periodicWorkRequest.build();
                                            WorkManager instance = WorkManager.getInstance();
                                            instance.enqueueUniquePeriodicWork(Constants.workManager_tag, ExistingPeriodicWorkPolicy.REPLACE, periodicWork);

                                            lbar.dismiss();
                                            Intent intent = new Intent(LoginActivity.this, Main_Activity.class);
                                            startActivity(intent);


                                            //   updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            lbar.dismiss();
                                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                                            try {
                                                throw task.getException();
                                            } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                                Log.d("TAG", "onComplete: invalid_email");
                                                emailView.setError("Invalid Email");
                                                passwordView.setText("");
                                                Toast.makeText(LoginActivity.this, "Account with this email does not exist", Toast.LENGTH_LONG).show();
                                            } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                                Log.d("TAG", "onComplete: wrong_password");
                                                passwordView.setText("");
                                                passwordView.setError("Incorrect Password");
                                                Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                                            } catch (Exception e) {
                                                Log.d("TAG", "onComplete: " + e.getMessage());
                                            }
                                        }

                                        // ...
                                    }
                                });
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        // Disabling back button for current activity
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}