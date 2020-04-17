package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class loginpage extends AppCompatActivity {

    private Button registrationbutton;
    private Button resetbutton;
    private FirebaseAuth mAuth;
    private Button next_button2;
    private String email;
    private String password;
    private EditText emailView;
    private EditText passwordView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(loginpage.this, MainActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_loginpage);
        next_button2 = findViewById(R.id.next_button2);
        registrationbutton = findViewById(R.id.buttonregister);

        resetbutton = findViewById(R.id.buttonreset);
        emailView = findViewById(R.id.editText2);
        passwordView = findViewById(R.id.editText5);
        registrationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(loginpage.this, registration1.class);
                startActivity(intent);
            }
        });
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginpage.this, resetpassword.class);
                startActivity(intent);

            }
        });
        next_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   Intent intent = new Intent(loginpage.this, MainActivity.class);
              //  startActivity(intent);
                email = emailView.getText().toString().trim();
                password = passwordView.getText().toString().trim();
                if(emailView.getText().toString().trim().equals(""))
                {
                    Toast.makeText(loginpage.this, "Enter Email and Password", Toast.LENGTH_SHORT).show();
                    emailView.setError("Enter Email");
                    passwordView.setError("Enter Password");
                }
                else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "signInWithEmail:success");
                                        Toast.makeText(loginpage.this, "Logging in...",
                                                Toast.LENGTH_SHORT).show();

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(loginpage.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        }, 1000);


                                        //   updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(loginpage.this, "Incorrect username or password.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }
            }
        });
    }
}
