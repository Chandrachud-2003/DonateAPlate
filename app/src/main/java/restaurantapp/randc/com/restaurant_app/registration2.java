package restaurantapp.randc.com.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registration2 extends AppCompatActivity {
    private Button next_button;
    private EditText phno;
    private EditText address;
    private EditText pincode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration2);
        phno = findViewById(R.id.editText4);
        address =  findViewById(R.id.editText7);
        pincode = findViewById(R.id.editText8);
        next_button = findViewById(R.id.next_button);

       // String name = getIntent().getStringExtra("name");

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phno.getText().toString().trim().equals("")||address.getText().toString().trim().equals("")||pincode.getText().toString().trim().equals("")) {
                    Toast.makeText(registration2.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    if (phno.getText().toString().trim().equals("")) {

                        phno.setError("Enter Phone number");
                    }
                    if (address.getText().toString().trim().equals("")) {

                        address.setError("Enter Address");
                    }
                    if (pincode.getText().toString().trim().equals("")) {

                        pincode.setError("Enter Pincode");
                    }
                }
                else {
                    Intent intent = new Intent(registration2.this, registration3.class);
                    intent.putExtra("name", getIntent().getStringExtra("name"));
                    intent.putExtra("phno", phno.getText().toString());
                    intent.putExtra("address", address.getText().toString());
                    intent.putExtra("pincode", pincode.getText().toString());
                    intent.putExtra("email", getIntent().getStringExtra("email"));
                    startActivity(intent);
                }
            }
        });
    }
}
