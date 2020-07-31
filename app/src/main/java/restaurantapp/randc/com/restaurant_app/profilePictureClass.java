package restaurantapp.randc.com.restaurant_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class profilePictureClass extends AppCompatActivity {

    private ImageView userProfileImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private ProgressDialog lbar;
    private StorageReference storageReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CardView nextButton;
    private int width;
    private ProgressDialog dialog;
    private String CollectionType;
    private int height;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Transformation transformation;
    private boolean pictureChanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_picture_class);
        lbar = new ProgressDialog(this);
        userProfileImage = findViewById(R.id.dp);
        mAuth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        nextButton = findViewById(R.id.nextButton);
        transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(5)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId,MODE_PRIVATE);
        dialog = new ProgressDialog(profilePictureClass.this);
         width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.8);
        height = (int) ((width*2)/3);
        if(sharedPreferences.getString("rType","").equals("Restaurant")) {
            Picasso.get()
                    .load(R.drawable.default_restaurant)
                    .resize(width, height)
                    .transform(transformation)
                    .centerCrop()
                    .into(userProfileImage);
        }
        else if(sharedPreferences.getString("rType","").equals("NGO")) {
            Picasso.get()
                    .load(R.drawable.default_ngo)
                    .resize(width, height)
                    .transform(transformation)
                    .centerCrop()
                    .into(userProfileImage);
        }
        else
        {
            Picasso.get()
                    .load(R.drawable.default_induvidual)
                    .resize(width, height)
                    .transform(transformation)
                    .centerCrop()
                    .into(userProfileImage);
        }

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.setMessage("Creating Account...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                mAuth.createUserWithEmailAndPassword(sharedPreferences.getString("rEmail","ERROR"), sharedPreferences.getString("rPass","ERROR"))

                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "createUserWithEmail:success");
                                    user = FirebaseAuth.getInstance().getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(sharedPreferences.getString("rType","ERROR")).build();

                                    user.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            user = mAuth.getCurrentUser();
                                            //SEND VERIFICATION MAIL
                                            user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("TAG", "Email sent.");
                                                    }
                                                }
                                            });
                                            Log.d("tag","UID:"+user.getUid());
                                            Map<String, Object> note = new HashMap<>();
                                            note.put("Name",sharedPreferences.getString("rName","ERROR"));
                                            note.put("Email",sharedPreferences.getString("rEmail","rEmail"));
                                            GeoPoint location  = new GeoPoint(getIntent().getDoubleExtra("Lat",0.0f),getIntent().getDoubleExtra("Lon",0.0f));
                                            note.put("Location",location);
                                            note.put("Address",getIntent().getStringExtra("Add"));
                                            note.put("Points",0);
                                            note.put("Number of donations",0);
                                            note.put("Phone Number",sharedPreferences.getString("rPhone","ERROR"));
                                            ArrayList<String> ngoOngoingList=new ArrayList<>();
                                            ArrayList<Boolean> orderArray=new ArrayList<>();
                                            for (int i=0;i<=4;i++)
                                            {
                                                orderArray.add(false);
                                            }

                                            if(sharedPreferences.getString("rType","null").equals("NGO")) {
                                                note.put("Url", "https://firebasestorage.googleapis.com/v0/b/restaurantapp-ab461.appspot.com/o/defaultNGO.png?alt=media&token=b0de6cb2-bce8-4c5e-9441-e1edadf3cbdf");
                                                note.put(Constants.ngo_ongoing_list_fire,ngoOngoingList);
                                                CollectionType ="NGO";
                                            }
                                            else if (sharedPreferences.getString("rType","null").equals("Restaurant")) {
                                                note.put("Url", "https://firebasestorage.googleapis.com/v0/b/restaurantapp-ab461.appspot.com/o/defaultRestaurant.png?alt=media&token=31a9b9ce-03da-4e09-9128-b69c14c59322");
                                                note.put(Constants.order_id_num,orderArray);
                                            }
                                            else {
                                                note.put("Url", "https://firebasestorage.googleapis.com/v0/b/restaurantapp-ab461.appspot.com/o/default_induvidual.png?alt=media&token=c618b327-8dfd-458f-84a1-19b8023436b2");
                                                note.put(Constants.order_id_num, orderArray);
                                            }
                                            if(sharedPreferences.getString("rType","null").equals("Individual")) {
                                                note.put("Type", "Individual");
                                                CollectionType ="Restaurant";
                                            }
                                            if(sharedPreferences.getString("rType","null").equals("Restaurant")) {
                                                note.put("Type", sharedPreferences.getString("rRestType", "ERROR"));
                                                CollectionType ="Restaurant";
                                            }

                                            db.collection(CollectionType).document(user.getUid()).set(note)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            user.getUid();


                                                            if(pictureChanged) {
                                                                dialog.dismiss();
                                                                lbar.setTitle("Uploading Image");
                                                                lbar.setMessage("Please wait while your display picture is uploading..");
                                                                lbar.setCanceledOnTouchOutside(false);
                                                                lbar.show();
                                                                uploadImageToFirebase(mImageUri);

                                                            }
                                                            else
                                                            {
                                                                dialog.dismiss();
                                                                Toast.makeText(profilePictureClass.this, "Account Created!", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(profilePictureClass.this,Main_Activity.class);
                                                                intent.putExtra("Registered", true);
                                                                startActivity(intent);
                                                            }
                                                            PeriodicWorkRequest.Builder periodicWorkRequest =
                                                                    new PeriodicWorkRequest.Builder(BackgroundWork.class, 15,
                                                                            TimeUnit.MINUTES);
                                                            PeriodicWorkRequest periodicWork = periodicWorkRequest.build();
                                                            WorkManager instance = WorkManager.getInstance();
                                                            instance.enqueueUniquePeriodicWork(Constants.workManager_tag, ExistingPeriodicWorkPolicy.REPLACE , periodicWork);



                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.remove("rEmail");
                                                            editor.remove("rName");
                                                            editor.remove("rAddress");
                                                            editor.remove("rPhone");
                                                            editor.remove("rPincode");
                                                            editor.remove("rState");
                                                            editor.remove("rPass");


                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            dialog.dismiss();
                                                            Toast.makeText(profilePictureClass.this, "Error!", Toast.LENGTH_SHORT).show();
                                                            Log.d("TAG", e.toString());
                                                        }
                                                    });


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            Toast.makeText(profilePictureClass.this, "Error!", Toast.LENGTH_SHORT).show();
                                        }
                                    });







                                } else {
                                    // If sign in fails, display a message to the user.
                                    dialog.dismiss();
                                    Toast.makeText(profilePictureClass.this, "Error!", Toast.LENGTH_SHORT).show();
                                    Log.e("TAG", "createUserWithEmail:failure", task.getException());
                                    Intent intent = new Intent(profilePictureClass.this, SignupActivity.class);
                                    startActivity(intent);
                                }
                            }

                            // ...
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(profilePictureClass.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void openGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(3,2)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode== RESULT_OK) {
                pictureChanged =true;
                mImageUri  = result.getUri();
                Picasso.get()
                        .load(mImageUri)
                        .resize(width, height)
                        .transform(transformation)
                        .centerCrop()
                        .into(userProfileImage);

            }
        }
    }

    private void uploadImageToFirebase(Uri mImageUri) {
        if (mImageUri != null) {

            StorageReference fileref = storageReference.child(user.getUid() + ".jpeg");
            fileref.putFile(mImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()) {
                        Log.d("tag", "ImageUploaded");

                        task.getResult().getMetadata().getReference().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downloadurl = uri.toString();
                                        db.collection(user.getDisplayName()).document(user.getUid())
                                                .update("Url",downloadurl)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("tag", "URLUploaded");
                                                        lbar.dismiss();

                                                        Intent intent = new Intent(profilePictureClass.this,Main_Activity.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(profilePictureClass.this, "Error! Check internet connection!", Toast.LENGTH_SHORT).show();
                                                        Log.d("tag","URLuplaodFail ---- Error:"+ e.toString());
                                                        lbar.dismiss();
                                                    }
                                                });
                                    }
                                });





                    }
                    else{
                        Toast.makeText(profilePictureClass.this, "Error! Check internet connection!", Toast.LENGTH_SHORT).show();
                        lbar.dismiss();
                    }
                    lbar.dismiss();
                }
            });


        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_LONG);
            lbar.dismiss();
        }
    }
    @Override
    public void onBackPressed() {
        // Disabling back button for current activity
    }


}

