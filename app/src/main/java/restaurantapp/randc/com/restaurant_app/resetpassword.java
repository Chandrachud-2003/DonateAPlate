package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetpassword extends AppCompatActivity {
    private Button back_button;
    private Button submit_button;
    private FirebaseAuth mAuth;
    private EditText emailView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        emailView = findViewById(R.id.editText2);
        back_button = findViewById(R.id.next_button2);
        submit_button = findViewById(R.id.next_button3);
        mAuth = FirebaseAuth.getInstance();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(resetpassword.this, loginpage.class);
                startActivity(intent);
            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(emailView.getText().toString().trim().equals(""))
                {
                    Toast.makeText(resetpassword.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                        emailView.setError("Enter Email");
                }
                else {

                    mAuth.sendPasswordResetEmail(emailView.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "Email sent.");
                                        Toast.makeText(resetpassword.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(resetpassword.this, loginpage.class);
                                                startActivity(intent);
                                            }
                                        }, 1000);

                                    } else {
                                        Toast.makeText(resetpassword.this, " Sorry, we don't recognize this email.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
    }
}
