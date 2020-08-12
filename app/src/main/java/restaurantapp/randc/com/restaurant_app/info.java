package restaurantapp.randc.com.restaurant_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class info extends AppCompatActivity {

    private CardView watchButton;
    private ImageButton back_button;
    private ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        back_button = findViewById(R.id.backButton);
        watchButton = findViewById(R.id.watch_button);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screen_width = displayMetrics.widthPixels;

        icon = findViewById(R.id.iconImage);
        icon.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                icon.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                int newDimensions = (int) (screen_width * 0.417);

                icon.getLayoutParams().width = newDimensions;


                icon.getLayoutParams().height = newDimensions;

                icon.requestLayout();
            }
        });

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
                Toast.makeText(info.this, "Video coming soon..", Toast.LENGTH_LONG).show();
            }
        });
    }
}