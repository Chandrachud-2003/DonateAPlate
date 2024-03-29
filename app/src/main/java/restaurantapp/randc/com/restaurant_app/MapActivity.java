package restaurantapp.randc.com.restaurant_app;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static final float DEFAULT_ZOOM = 17f;
    private com.google.maps.model.LatLng latLng;
    private String address;
    private double latitude;
    private double longitude;
    private GoogleMap mMap;
    private Marker currentMarker;
    private ProgressDialog progressDialog;
    private CardView select;
    private String cityName;
    private String stateName;
    private String areaName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Places.initialize(getApplicationContext(), "AIzaSyDinB_qTH7KMRrlK5-_NmBnpoCMxZhBB9A");
        select = findViewById(R.id.location_select);
        address = "";
        cityName = "";
        stateName = "";
        progressDialog = new ProgressDialog(this);
        areaName = "";
        latitude = 0;
        longitude = 0;
        initMap();


    }

    private void initMap() {
        Log.d("TAG", "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);



        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                moveCamera(Objects.requireNonNull(place.getLatLng()), DEFAULT_ZOOM, Objects.requireNonNull(place.getName()));
                address = place.getAddress();
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                latLng = new com.google.maps.model.LatLng(latitude, longitude);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.d("TAG", "An error occurred: " + status);
            }
        });


    }

    private void getDeviceLocation() {
        Log.d("TAG", "getDeviceLocation: getting the devices current location");

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {


            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "onComplete: found location");
                        Location currentLocation = (Location) task.getResult();

                        if (currentLocation != null) {
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "My Location");
                            latitude = currentLocation.getLatitude();
                            longitude = currentLocation.getLongitude();
                            currentMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
                            latLng = new com.google.maps.model.LatLng(latitude, longitude);
                        } else
                            Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("TAG", "onComplete: current location is null");
                        Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (SecurityException e) {
            Log.e("TAG", "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d("TAG", "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            if (currentMarker != null) {
                currentMarker.remove();
            }
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            currentMarker = mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "onMapReady: map is ready");
        mMap = googleMap;
        getDeviceLocation();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (currentMarker != null) {
                    currentMarker.remove();
                }
                latitude = point.latitude;
                longitude = point.longitude;
                currentMarker = mMap.addMarker(new MarkerOptions().position(point));
                latLng = new com.google.maps.model.LatLng(latitude, longitude);


            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Please wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                GetBackgroundInfo backgroundInfo = new GetBackgroundInfo();
                backgroundInfo.execute();

            }
        });
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onBackPressed() {
        // Disabling back button for current activity
    }

    private class GetBackgroundInfo extends AsyncTask<Void, Integer, Boolean> {

        //private final WeakReference<Activity> weakActivity;

        GetBackgroundInfo() {
            //this.weakActivity = new WeakReference<>(myActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TAG", "onPreExecute: task started");


        }

        protected Boolean doInBackground(Void... voids) {

            try {


                Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                address = addresses.get(0).getAddressLine(0);
                cityName = addresses.get(0).getLocality();
                stateName = addresses.get(0).getAdminArea();
                areaName = addresses.get(0).getSubLocality();
                onPostExecute(true);

            } catch (Exception e) {
                e.printStackTrace();
                onPostExecute(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean done) {
            super.onPostExecute(done);

            progressDialog.dismiss();
            Intent intent = new Intent(MapActivity.this, profilePictureClass.class);
            intent.putExtra("Lat", latitude);
            intent.putExtra("Lon", longitude);
            intent.putExtra("Add", address);
            intent.putExtra("City", cityName);
            intent.putExtra("Area", areaName);
            intent.putExtra("State", stateName);
            Log.d("TAG", "LAT:" + latitude + "/nLONG:" + longitude + "/nAddress" + address);
            startActivity(intent);
        }
    }
}