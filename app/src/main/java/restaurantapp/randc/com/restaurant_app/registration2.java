package restaurantapp.randc.com.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registration2 extends AppCompatActivity {

    private ToggleSwitch toggleSwitch;
    private EditText phno;
    private EditText nameView;
    private Button next_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration2);
        toggleSwitch = findViewById(R.id.toggleSwitch);
        phno = findViewById(R.id.phoneView);
        nameView = findViewById(R.id.nameView);
        next_button = findViewById(R.id.next_button);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("rType","Restaurant");
        editor.apply();

        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){

            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
               if(position == 0 && isChecked)
                {
                    nameView.setHint("Restaurant Name");
                    editor.putString("rType","Restaurant");
                    editor.apply();
                }
                if(position == 1 && isChecked)
                {
                    nameView.setHint("Organisation Name");
                    editor.putString("rType","NGO");
                    editor.apply();
                }
                if(position == 2 && isChecked)
                {
                    nameView.setHint("Your Name");
                    editor.putString("rType","Individual");
                    editor.apply();
                }
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phno.getText().toString().trim().equals("") || nameView.getText().toString().trim().equals("")) {
                    Toast.makeText(registration2.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    if (phno.getText().toString().trim().equals("")) {

                        phno.setError("Enter Phone number");
                    }
                    if (nameView.getText().toString().trim().equals("")) {

                        nameView.setError("Enter Name");
                    }
                } else {
                    editor.putString("rPhone", phno.getText().toString().trim());
                    editor.putString("rName", nameView.getText().toString().trim());
                    editor.apply();
                    Intent intent = new Intent(registration2.this, registration3.class);
                    startActivity(intent);

                }
            }
        });
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
        editor.apply();
        Log.d("TAG","DONE2");
    }

    @Override
    public void onBackPressed() {
        // Disabling back button for current activity
    }
}
