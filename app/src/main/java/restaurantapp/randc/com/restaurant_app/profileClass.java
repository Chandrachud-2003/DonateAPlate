package restaurantapp.randc.com.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class profileClass extends AppCompatActivity implements PasswordDialog.DialogListener{

    private FloatingActionMenu fab;
    private Button save;
    private FloatingActionButton edit;
    private Uri mImageUri;
    private ImageButton cancelButton;
    private EditText emailView;
    private EditText AddressView;
    private EditText phnoView;
    private EditText nameView;
    private KeyListener listener;
    private KeyListener listener2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private ImageView userProfileImage;
    private String newEmail;
    private ImageButton editPicture;
    private int width;
    private StorageReference storageReference;
    private int height;
    private boolean pictureChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_class);

        storageReference= FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        fab = findViewById(R.id.fabfab);
        edit = findViewById(R.id.menu_item);
        save = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancel_button);
        emailView = findViewById(R.id.emailView);
        phnoView = findViewById(R.id.phoneView);
        AddressView = findViewById(R.id.addressView);
        nameView = findViewById(R.id.nameView);
        userProfileImage = findViewById(R.id.profilePic);
        listener = emailView.getKeyListener();
        listener2 = phnoView.getKeyListener();
        editPicture = findViewById(R.id.editPicture);

        nameView.setKeyListener(null);
        AddressView.setKeyListener(null);
        phnoView.setKeyListener(null);
        emailView.setKeyListener(null);


        width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.8);
        height = (int) ((width*2)/3);

        updateDetails();


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.close(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab.setVisibility(View.GONE);
                        save.setVisibility(View.VISIBLE);

                    }
                }, 300);


                emailView.setPadding(25,25,25,25);
                emailView.setKeyListener(listener);
                emailView.setBackgroundResource(R.drawable.edittext_bg);

                phnoView.setPadding(25,25,25,25);
                phnoView.setKeyListener(listener2);
                phnoView.setBackgroundResource(R.drawable.edittext_bg);

                AddressView.setPadding(25,25,25,25);
                AddressView.setKeyListener(listener);
                AddressView.setBackgroundResource(R.drawable.edittext_bg);

                nameView.setPadding(25,25,25,25);
                nameView.setKeyListener(listener);
                nameView.setBackgroundResource(R.drawable.edittext_bg);

                cancelButton.setVisibility(View.VISIBLE);

                editPicture.setVisibility(View.VISIBLE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



                String name = nameView.getText().toString().trim();
                String phno = phnoView.getText().toString().trim();
                String address = AddressView.getText().toString().trim();
                String email = emailView.getText().toString().trim();



                if(name.equals(""))
                    Toast.makeText(profileClass.this, "Name field is empty. Please fill all fields", Toast.LENGTH_LONG).show();
                else if(phno.equals(""))
                    Toast.makeText(profileClass.this, "Phone Number field is empty. Please fill all fields", Toast.LENGTH_LONG).show();
                else if(address.equals(""))
                    Toast.makeText(profileClass.this, "Address field is empty. Please fill all fields", Toast.LENGTH_LONG).show();
                else if(email.equals(""))
                    Toast.makeText(profileClass.this, "Email field is empty. Please fill all fields", Toast.LENGTH_LONG).show();

                else {
                    Map<String, Object> note = new HashMap<>();
                    note.put("Name", name);
                    note.put("Address", address);
                    note.put("Phone Number", phno);

                    if(!(email.equals(user.getEmail()))) {
                        PasswordDialog exampleDialog = new PasswordDialog();
                        exampleDialog.show(getSupportFragmentManager(), "example dialog");
                        newEmail = email;
                    }

                    if(pictureChanged)
                    {
                        uploadImageToFirebase(mImageUri);
                        pictureChanged = false;
                    }

                    db.collection(user.getDisplayName()).document(user.getUid()).update(note)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(profileClass.this, "Updated", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(profileClass.this, "Update failed. Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", e.toString());
                                }
                            });

                    save.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    cancelButton.setVisibility(View.GONE);
                    editPicture.setVisibility(View.GONE);

                    nameView.setKeyListener(null);
                    AddressView.setKeyListener(null);
                    phnoView.setKeyListener(null);
                    emailView.setKeyListener(null);

                    emailView.setPadding(0,10,0,0);
                    emailView.setBackgroundColor(Color.TRANSPARENT);

                    phnoView.setPadding(0,10,0,0);
                    phnoView.setBackgroundColor(Color.TRANSPARENT);

                    AddressView.setPadding(0,10,0,0);
                    AddressView.setBackgroundColor(Color.TRANSPARENT);

                    nameView.setPadding(0,10,0,0);
                    nameView.setBackgroundColor(Color.TRANSPARENT);

                  updateDetails();

                }

            }
        });


        editPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.GONE);
                editPicture.setVisibility(View.GONE);

                nameView.setKeyListener(null);
                AddressView.setKeyListener(null);
                phnoView.setKeyListener(null);
                emailView.setKeyListener(null);

                emailView.setPadding(0,10,0,0);
                emailView.setBackgroundColor(Color.TRANSPARENT);

                phnoView.setPadding(0,10,0,0);
                phnoView.setBackgroundColor(Color.TRANSPARENT);

                AddressView.setPadding(0,10,0,0);
                AddressView.setBackgroundColor(Color.TRANSPARENT);

                nameView.setPadding(0,10,0,0);
                nameView.setBackgroundColor(Color.TRANSPARENT);

                updateDetails();
            }
        });

    }

    void updateDetails()
    {

        db.collection(user.getDisplayName()).document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String url = documentSnapshot.getString("Url");

                            if(!(url.equals(""))) {
                                Picasso.get().load(url).into(userProfileImage);
                            }

                            emailView.setText(documentSnapshot.getString("Email"));
                            phnoView.setText(documentSnapshot.getString("Phone Number"));
                            nameView.setText(documentSnapshot.getString("Name"));
                            AddressView.setText(documentSnapshot.getString("Address"));

                        } else {
                            nameView.setText("User not found");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profileClass.this, "Error! Check your Internet Connection", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });
    }

    @Override
    public void applyTexts(String password) {

        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG", "User re-authenticated.");

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(newEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("TAG", "User email address updated.");
                                            Map<String, Object> note = new HashMap<>();
                                            note.put("Email", newEmail);
                                            db.collection(user.getDisplayName()).document(user.getUid()).update(note)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(profileClass.this, "Email Updated", Toast.LENGTH_LONG).show();

                                                            updateDetails();

                                                            //SEND VERIFICATION MAIL
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(profileClass.this, "Update failed. Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                                                            Log.d("TAG", e.toString());
                                                        }
                                                    });


                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(profileClass.this, "Email already exsits or is invalid!", Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", e.toString());
                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profileClass.this, "INCORRECT PASSWORD. Email not changed.", Toast.LENGTH_SHORT).show();


                    }
                });
        updateDetails();


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
                                                        updateDetails();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(profileClass.this, "Error! Check internet connection!", Toast.LENGTH_SHORT).show();
                                                        Log.d("tag","URLuplaodFail ---- Error:"+ e.toString());
                                                    }
                                                });
                                    }
                                });





                    }
                    else{
                        Toast.makeText(profileClass.this, "Error! Check internet connection!", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_LONG);

        }
    }
}

