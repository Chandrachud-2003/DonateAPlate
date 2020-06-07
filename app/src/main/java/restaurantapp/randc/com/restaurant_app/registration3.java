package restaurantapp.randc.com.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class registration3 extends AppCompatActivity {
    private Button next_button;
    private EditText address;
    private Spinner stateSpinner;
    private Spinner citySpinner;
    private EditText pincode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration3);
        address =  findViewById(R.id.editText7);
        pincode = findViewById(R.id.editText8);
        next_button = findViewById(R.id.next_button);

        stateSpinner = findViewById(R.id.spinner);
        citySpinner = findViewById(R.id.spinner2);

        String[] check = {"Select State","Andhra Pradesh",
                "Arunachal Pradesh",
                "Assam",
                "Bihar",
                "Chhattisgarh",
                "Goa",
                "Gujarat",
                "Haryana",
                "Himachal Pradesh",
                "Jammu and Kashmir",
                "Jharkhand",
                "Karnataka",
                "Kerala",
                "Madhya Pradesh",
                "Maharashtra",
                "Manipur",
                "Meghalaya",
                "Mizoram",
                "Nagaland",
                "Odisha",
                "Punjab",
                "Rajasthan",
                "Sikkim",
                "Tamil Nadu",
                "Telangana",
                "Tripura",
                "Uttarakhand",
                "Uttar Pradesh",
                "West Bengal",
                "Andaman and Nicobar Islands",
                "Chandigarh",
                "Dadra and Nagar Haveli",
                "Daman and Diu",
                "Delhi",
                "Lakshadweep",
                "Puducherry","Other"};
        List<String> spinnerArray = Arrays.asList(check);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(adapter);

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address.getText().toString().trim().equals("")||pincode.getText().toString().trim().equals("")||stateSpinner.getSelectedItem().toString().equals("Select State")) {
                    Toast.makeText(registration3.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();

                    if (address.getText().toString().trim().equals("")) {

                        address.setError("Enter Address");
                    }
                    if (pincode.getText().toString().trim().equals("")) {

                        pincode.setError("Enter Pincode");
                    }
                    if(stateSpinner.getSelectedItem().toString().equals("Select State")) {
                        Toast.makeText(registration3.this, "Select State", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("rAddress",address.getText().toString().trim());
                    editor.putString("rPincode",pincode.getText().toString().trim());
                    editor.putString("rState",stateSpinner.getSelectedItem().toString());
                    editor.putString("rCity","");
                    editor.apply();

                    Intent intent = new Intent(registration3.this, registration4.class);
                    startActivity(intent);
                }
            }
        });
    }
}
