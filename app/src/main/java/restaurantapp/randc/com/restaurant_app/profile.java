package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.jackandphantom.blurimage.BlurImage;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class profile extends AppCompatActivity {
    private TextView emailView;
    private TextView AddressView;
    private TextView phnoView;
    private TextView nameView;
    private Button addprofilepicbutton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Uri mImageUri;
    private StorageReference storageReference;
    private StorageTask mUpload;
    private ProgressDialog lbar;
    private ImageView userProfileImage;
    private ImageView blurUserProfileImage;
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        emailView = findViewById(R.id.emailView);
        phnoView = findViewById(R.id.phoneView);
        AddressView = findViewById(R.id.addressView);
      //  nameView = findViewById(R.id.tv_name);
        userProfileImage = findViewById(R.id.profileImageDisplay);
        blurUserProfileImage = findViewById(R.id.BlurProfileImageDisplay);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        emailView.setText(user.getEmail());
//        nameView.setText(user.getDisplayName());
        lbar = new ProgressDialog(this);





        db.collection("Restaurant").document(user.getDisplayName()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            String phno = documentSnapshot.getString("Phone Number");
                            String name = documentSnapshot.getString("Name");
                            String url = documentSnapshot.getString("Url");
                            Picasso.get().load(url).into(userProfileImage);
                     
                            phnoView.setText(phno);
                            AddressView.setText(documentSnapshot.getString("Address"));

                        } else {
                            Toast.makeText(profile.this, "No phone number found.", Toast.LENGTH_SHORT).show();
                            phnoView.setText("User not found");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profile.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });



        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
                builder.setCancelable(true);
                builder.setTitle("Change Picture");
                builder.setMessage("Do you want to change your Account Picture?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        openGallery();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });




    }

    private void openGallery() {
        //   Intent intent = new Intent();
        //  intent.setAction(intent.ACTION_GET_CONTENT);
        // intent.setType("image/*");
        // startActivityForResult(intent, PICK_IMAGE_REQUEST);
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            //Picasso.with(this).load(mImageUri).into(profilePic);
            //uploadImageToFirebase(mImageUri);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode== RESULT_OK) {
                lbar.setTitle("Uploading Image");
                lbar.setMessage("Please wait,your profile picture is updating..");
                lbar.setCanceledOnTouchOutside(false);
                lbar.show();
                Uri resultUri  = result.getUri();
                uploadImageToFirebase(resultUri);
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
                        Toast.makeText(profile.this, "Uploded", Toast.LENGTH_LONG);

                        task.getResult().getMetadata().getReference().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downloadurl = uri.toString();
                                        db.collection("Restaurant").document(user.getDisplayName())
                                                .update("Url",downloadurl)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(profile.this, "URL in database", Toast.LENGTH_LONG).show();
                                                        Picasso.get().load(downloadurl).into(userProfileImage);

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(profile.this, "Error! URL to database fail", Toast.LENGTH_SHORT).show();
                                                        Log.d("TAG", e.toString());
                                                    }
                                                });
                                    }
                                });





                    }
                    else{
                        Toast.makeText(profile.this,"Error"+ task.getException().toString(), Toast.LENGTH_LONG);

                    }
                    lbar.dismiss();
                }
            });


        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_LONG);
            lbar.dismiss();
        }
    }
}



