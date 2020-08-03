package restaurantapp.randc.com.restaurant_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.hbb20.CountryCodePicker;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static restaurantapp.randc.com.restaurant_app.Constants.ERROR_DIALOG_REQUEST;
import static restaurantapp.randc.com.restaurant_app.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static restaurantapp.randc.com.restaurant_app.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class SignupActivity extends AppCompatActivity {

    private CardView next_button;
    private TextView loginbutton;
    private EditText emailView;
    private EditText passwordView;
    private EditText passwordConfirm;
    private EditText phno;
    private EditText nameView;
    private EditText typeView;
    private String email;
    private FirebaseAuth mAuth;
    private ProgressDialog lbar;
    private String password;
    private String Type;
    private ConstraintLayout selectorLayout;
    private ConstraintLayout signupLayout;
    private CardView restCard;
    private TextView restText;
    private CountryCodePicker ccp;
    private ImageView restImage;
    private CardView ngoCard;
    private TextView ngoText;
    private ImageView ngoImage;
    private CardView indCard;
    private TextView indText;
    private ImageView indImage;
    private ConstraintLayout categoryLayout;
    private TextView categoryText;

    private int screenWidth;
    private boolean mLocationPermissionGranted = false;
    private boolean choose;
    private TextView subheading;

    private ImageButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

        choose = false;


        setContentView(R.layout.activity_signup);
        phno = findViewById(R.id.phoneView);
        nameView = findViewById(R.id.nameView);
        next_button = findViewById(R.id.nextButton);
        mAuth = FirebaseAuth.getInstance();
        emailView = findViewById(R.id.emailView);
        passwordConfirm = findViewById(R.id.confirmView);
        loginbutton = findViewById(R.id.signin);
        lbar = new ProgressDialog(this);
        passwordView = findViewById(R.id.passwordView);
        selectorLayout = findViewById(R.id.selectorLayout);
        signupLayout = findViewById(R.id.subSignupLayout);
        restCard = findViewById(R.id.chooseRestCard);
        restImage = findViewById(R.id.restImage);
        restText = findViewById(R.id.restName);
        ngoCard = findViewById(R.id.chooseNGOCard);
        ngoImage = findViewById(R.id.NGOImage);
        ngoText = findViewById(R.id.NGOName);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        typeView = findViewById(R.id.categoryView);
        indCard = findViewById(R.id.chooseIndCard);
        indImage = findViewById(R.id.IndImage);
        categoryLayout = findViewById(R.id.categoryLayout);
        categoryText = findViewById(R.id.categoryText);
        indText = findViewById(R.id.IndName);
        subheading = findViewById(R.id.subheadingText);
        backButton = findViewById(R.id.backButton);
        Type = "";
        ccp.registerCarrierNumberEditText(phno);

/*        ngoImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


                ngoImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int newDimen = (int) (screenWidth*0.14);

                ngoImage.getLayoutParams().height = newDimen;
                ngoImage.getLayoutParams().width = newDimen;

                ngoCard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        ngoCard.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        ngoCard.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                        int height = ngoCard.getMeasuredHeight();

                        ngoCard.getLayoutParams().width = height;


                    }
                });
            }
        });

        restImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                restImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int newDimen = (int) (screenWidth*0.14);

                restImage.getLayoutParams().height = newDimen;
                restImage.getLayoutParams().width = newDimen;

                restCard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        restCard.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        int newDimen = (int) (screenWidth*0.4);

                        //restCard.getLayoutParams().height = newDimen;
                        restCard.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                        int height = restCard.getMeasuredHeight();

                        restCard.getLayoutParams().width = height;

                    }
                });




            }
        });


        indImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                indImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int newDimen = (int) (screenWidth*0.14);

                indImage.getLayoutParams().height = newDimen;
                indImage.getLayoutParams().width = newDimen;

                indCard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        indCard.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        int newDimen = (int) (screenWidth*0.4);

                        //restCard.getLayoutParams().height = newDimen;
                        indCard.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                        int ww = indCard.getMeasuredHeight();

                        indCard.getLayoutParams().width = ww;

                    }
                });

            }
        });*/

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        restCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeCards(true, restText, restImage, restCard, R.drawable.registration_rest_white);
                changeCards(false, ngoText, ngoImage, ngoCard, R.drawable.registration_ngo_blue);
                changeCards(false, indText, indImage, indCard, R.drawable.registration_induviaul_blue);
                Type = "Restaurant";
            }
        });

        ngoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeCards(false, restText, restImage, restCard, R.drawable.registration_rest_blue);
                changeCards(true, ngoText, ngoImage, ngoCard, R.drawable.registration_ngo_white);
                changeCards(false, indText, indImage, indCard, R.drawable.registration_induviaul_blue);
                Type = "NGO";

            }
        });

        indCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeCards(false, restText, restImage, restCard, R.drawable.registration_rest_blue);
                changeCards(false, ngoText, ngoImage, ngoCard, R.drawable.registration_ngo_blue);
                changeCards(true, indText, indImage, indCard, R.drawable.registration_individual_white);
                Type = "Individual";
            }
        });


        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (choose) {

                    email = emailView.getText().toString();
                    password = passwordView.getText().toString();
                    email = email.trim();
                    password = password.trim();


                    if (passwordConfirm.getText().toString().trim().equals("") || passwordView.getText().toString().trim().equals("") || emailView.getText().toString().trim().equals("") || phno.getText().toString().trim().equals("") || nameView.getText().toString().trim().equals("")) {
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

                    } else if (phno.getText().toString().replace(" ", "").length() != 10) {
                        phno.setError("Invalid Phone number");
                        Toast.makeText(SignupActivity.this, "Invalid Phone number", Toast.LENGTH_SHORT).show();
                    } else if (password.length() < 6) {
                        Toast.makeText(SignupActivity.this, "Password must be atleast 6 charecters long", Toast.LENGTH_SHORT).show();
                        passwordView.setError("Password too short!");
                    } else if (!(isEmailValid(email))) {
                        emailView.setError("Invalid Email");
                        Toast.makeText(SignupActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    } else {

                        lbar.setMessage("Please wait...");
                        lbar.setCanceledOnTouchOutside(false);
                        lbar.show();

                        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()) {
                                    SignInMethodQueryResult result = task.getResult();
                                    List<String> signInMethods = result.getSignInMethods();
                                    if (signInMethods.size() == 0) {
                                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putString("rType", Type);
                                        if (Type.equals("Restaurant")) {
                                            editor.putString("rRestType", typeView.getText().toString().trim());
                                        }
                                        editor.putString("rEmail", email);
                                        editor.putString("rPass", password);
                                        editor.putString("rPhone", ccp.getFormattedFullNumber());

                                        editor.putString("rName", nameView.getText().toString().trim());
                                        editor.apply();
                                        if (checkMapServices()) {
                                            if (mLocationPermissionGranted) {
                                                Intent intent = new Intent(SignupActivity.this, MapActivity.class);
                                                startActivity(intent);
                                                lbar.dismiss();
                                            } else {
                                                getLocationPermission();
                                                lbar.dismiss();
                                            }
                                        }

                                    } else {
                                        emailView.setError("Email already exists");
                                        lbar.dismiss();
                                        Toast.makeText(SignupActivity.this, "An account with this email already exists!", Toast.LENGTH_LONG).show();
                                    }
                                } else
                                    lbar.dismiss();
                            }

                        });


                    }
                } else {

                    if (!Type.isEmpty()) {
                        choose = true;
                        selectorLayout.setVisibility(View.GONE);
                        signupLayout.setVisibility(View.VISIBLE);
                        subheading.setText(Type);
                        nameView.setHint(Type + " name");
                        if (Type.equals("Individual")) {
                            nameView.setHint("Your name");

                        }
                        if (Type.equals("Restaurant")) {
                            categoryLayout.setVisibility(View.VISIBLE);
                            categoryText.setVisibility(View.VISIBLE);

                        }

                    } else {
                        Toast.makeText(SignupActivity.this, "Please select a type of account", Toast.LENGTH_LONG).show();
                    }


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
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("rEmail");
        editor.remove("rName");
        editor.remove("rPhone");
        editor.remove("rPass");
        editor.remove("rType");
        editor.apply();
        Log.d("TAG", "DONE2");
    }

    private void changeCards(boolean select, TextView textView, ImageView imageView, CardView cardView, int image) {
        imageView.setImageResource(image);

        if (select) {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.blue));
            textView.setTextColor(getResources().getColor(R.color.white));
            cardView.setCardElevation(100.0f);
        } else {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
            textView.setTextColor(getResources().getColor(R.color.blue));
            cardView.setCardElevation(15.0f);
        }


    }

    private boolean checkMapServices() {
        if (isServicesOK()) {
            return isMapsEnabled();
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Location services have to be turned on to provide your location. Do you want to turn it on?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            Intent intent = new Intent(SignupActivity.this, MapActivity.class);
            startActivity(intent);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK() {
        Log.d("TAG", "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SignupActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d("TAG", "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d("TAG", "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SignupActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {
                    Intent intent = new Intent(SignupActivity.this, MapActivity.class);
                    startActivity(intent);
                } else {
                    getLocationPermission();
                }
            }
        }

    }
}