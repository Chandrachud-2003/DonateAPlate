package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class registration5 extends AppCompatActivity {

    private ImageView userProfileImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private ProgressDialog lbar;
    private StorageReference storageReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button nextButton;
    private int width;
    private int height;
    private Transformation transformation;
    private boolean pictureChanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration5);
        lbar = new ProgressDialog(this);
        userProfileImage = findViewById(R.id.dp);
        storageReference= FirebaseStorage.getInstance().getReference();
        nextButton = findViewById(R.id.next_button);
        transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(5)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefId,MODE_PRIVATE);

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

                if(pictureChanged) {
                    lbar.setTitle("Uploading Image");
                    lbar.setMessage("Please wait,your profile picture is uploading..");
                    lbar.setCanceledOnTouchOutside(false);
                    lbar.show();
                    uploadImageToFirebase(mImageUri);

                }
                else
                {

                    Intent intent = new Intent(registration5.this,Main_Activity.class);
                    startActivity(intent);
                }
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
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
                                                        Intent intent = new Intent(registration5.this,Main_Activity.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(registration5.this, "Error! Check internet connection!", Toast.LENGTH_SHORT).show();
                                                        Log.d("tag","URLuplaodFail ---- Error:"+ e.toString());
                                                    }
                                                });
                                    }
                                });





                    }
                    else{
                        Toast.makeText(registration5.this, "Error! Check internet connection!", Toast.LENGTH_SHORT).show();
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

