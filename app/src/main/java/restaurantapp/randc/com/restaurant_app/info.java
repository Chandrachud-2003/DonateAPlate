package restaurantapp.randc.com.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class info extends AppCompatActivity {

    private CardView watchButton;
    private ImageButton back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        back_button = findViewById(R.id.backButton);
        watchButton = findViewById(R.id.watch_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(info.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        watchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(info.this,"Video coming soon..",Toast.LENGTH_LONG).show();
            }
        });
    }
}