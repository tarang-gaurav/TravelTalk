package com.example.ankit.traveltalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;
    private static final int MAX_LENGTH = 100;
    private DatabaseReference userdatabase;
    private CircleImageView mdisplayimage;
    private TextView mname, mstatus;
    Button cimg, cstatus;
    private FirebaseUser xcurrentuser;
    private StorageReference mStorageRef;
   // private ProgressDialog mprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mStorageRef = FirebaseStorage.getInstance().getReference();

        mdisplayimage = (CircleImageView) findViewById(R.id.img_display);
        mname = (TextView) findViewById(R.id.txtname);
        mstatus = (TextView) findViewById(R.id.txtstatus);
        cimg = (Button) findViewById(R.id.btn_cimg);
        cstatus = (Button) findViewById(R.id.btnstatus);


        cstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status_value = mstatus.getText().toString();

                Intent x = new Intent(SettingsActivity.this, StatusActivity.class);
                x.putExtra("status_value", status_value);
                startActivity(x);


            }
        });


        cimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this);


                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });


         xcurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = xcurrentuser.getUid();


        userdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        userdatabase.keepSynced(true);

        userdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumbnail_image = dataSnapshot.child("thumbnail_image").getValue().toString();

                mname.setText(name);
                mstatus.setText(status);
          if(!image.equals("default")) {
              Picasso.with(SettingsActivity.this).load(image).into(mdisplayimage);
          }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageuri = data.getData();
            CropImage.activity(imageuri).setAspectRatio(1, 1)
                    .start(SettingsActivity.this);
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

              /*  mprogress = new ProgressDialog(SettingsActivity.this);
                mprogress.setTitle("Saving Changes");
                mprogress.setMessage("Please Wait while we save changes");
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.show();*/

                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());


                String current_user_id = xcurrentuser.getUid();


                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();


                StorageReference filepath = mStorageRef.child("profile_images").child(current_user_id + ".jpg");
                final StorageReference thumb_filepath = mStorageRef.child("profile_images").child("thumbs").child(current_user_id + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {


                            final String download_url = task.getResult().getDownloadUrl().toString();
                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                                    if (thumb_task.isSuccessful()) {

                                        Map update_hashMap = new HashMap();
                                        update_hashMap.put("image", download_url);
                                        update_hashMap.put("thumbnail_image", thumb_downloadUrl);

                                        userdatabase.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //  mprogress.dismiss();
                                                    Toast.makeText(SettingsActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(SettingsActivity.this, "Error in Uploading  ", Toast.LENGTH_LONG).show();
                                                }
                                                // Toast.makeText(SettingsActivity.this, "working ", Toast.LENGTH_LONG).show();
                                            }
                                        });


                                    } else {

                                        Toast.makeText(SettingsActivity.this, "Error in uploading thumbnail.", Toast.LENGTH_LONG).show();
                                        // mProgressDialog.dismiss();

                                    }


                                }
                            });
                        } else {

                            Toast.makeText(SettingsActivity.this, "Error in uploading .", Toast.LENGTH_LONG).show();
                            // mProgressDialog.dismiss();

                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            Exception error = result.getError();

                        }

            }
        }
    

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}

