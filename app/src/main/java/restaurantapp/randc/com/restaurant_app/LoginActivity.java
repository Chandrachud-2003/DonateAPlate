package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {


    private TextView registrationbutton;
    private TextView resetbutton;
    private FirebaseAuth mAuth;
    private CardView signinButton;
    private String email;
    private String password;
    private EditText emailView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, Main_Activity.class);
            startActivity(intent);
        }
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
                } else {
                    Toast.makeText(LoginActivity.this, "Logging in...",
                            Toast.LENGTH_SHORT).show();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "signInWithEmail:success");
                                        FirebaseAuth auth = mAuth.getInstance();

                                        //getSharedPreferences(Constants.sharedPrefId, MODE_PRIVATE).edit().putString(Constants.userIdPref, mAuth.getUid()).apply();


                                        PeriodicWorkRequest.Builder periodicWorkRequest =
                                                new PeriodicWorkRequest.Builder(BackgroundWork.class, 15,
                                                        TimeUnit.MINUTES);
                                        PeriodicWorkRequest periodicWork = periodicWorkRequest.build();
                                        WorkManager instance = WorkManager.getInstance();
                                        instance.enqueueUniquePeriodicWork(Constants.workManager_tag, ExistingPeriodicWorkPolicy.REPLACE, periodicWork);


                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(LoginActivity.this, Main_Activity.class);
                                                startActivity(intent);
                                            }
                                        }, 1000);


                                        //   updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Incorrect username or password.",
                                                Toast.LENGTH_LONG).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Disabling back button for current activity
    }
}