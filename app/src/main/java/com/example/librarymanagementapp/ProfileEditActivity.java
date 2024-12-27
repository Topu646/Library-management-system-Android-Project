package com.example.librarymanagementapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.librarymanagementapp.databinding.ActivityProfileEditBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ProfileEditActivity extends AppCompatActivity {

    private ActivityProfileEditBinding binding;
    private FirebaseAuth firebaseAuth;
    private Uri imageuri = null;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loaduserinfo();

        binding.backid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
         });

        binding.profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showimageattachmenu();
            }
        });

        binding.updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatedata();

            }
        });

    }

    private void validatedata() {
        name = binding.nameet.getText().toString().trim();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
        }else
        {
            if(imageuri == null)
            {
                updateprofile("");

            }else
            {
                uploadimage();
            }
        }

    }

    private void uploadimage() {
    String filepathandname = "Profilepic/" + firebaseAuth.getUid();
        StorageReference reference = FirebaseStorage.getInstance().getReference(filepathandname);
        reference.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadedimageurl = ""+uriTask.getResult();
                        updateprofile(uploadedimageurl);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileEditActivity.this, "Failed. Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void updateprofile(String imageurl) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        if(imageuri != null)
        {
            hashMap.put("profilepic",""+imageurl);
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(ProfileEditActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ProfileEditActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showimageattachmenu() {
        PopupMenu popupMenu = new PopupMenu(this, binding.profilepic);
         popupMenu.getMenu().add(Menu.NONE,0,0,"Camera");
        popupMenu.getMenu().add(Menu.NONE,1,1,"Gallery");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int which = item.getItemId();
                if(which == 0)
                {

                    pickimagecamera();
                }else if ( which == 1)
                {
                    picimagegallery();
                }
                return false;
            }
        });

    }

    private void picimagegallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryactivityresultlauncher.launch(intent);

    }

    private void pickimagecamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Pick");
        values.put(MediaStore.Images.Media.DESCRIPTION, "sample image description");
        imageuri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
        cameraactivityresultlauncher.launch(intent);

    }

    private ActivityResultLauncher<Intent> cameraactivityresultlauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        binding.profilepic.setImageURI(imageuri);
                    }else
                    {
                        Toast.makeText(ProfileEditActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private ActivityResultLauncher<Intent> galleryactivityresultlauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        imageuri = data.getData();
                        binding.profilepic.setImageURI(imageuri);
                    }else
                    {
                        Toast.makeText(ProfileEditActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    private void loaduserinfo() {
        DatabaseReference ref  = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email =""+ snapshot.child("email").getValue();
                        String name =""+ snapshot.child("name").getValue();
                        String profilepic =""+ snapshot.child("profilepic").getValue();
                        String timestamp =""+ snapshot.child("timeStamp").getValue();
                        String uid =""+ snapshot.child("UId").getValue();
                        String usertype =""+ snapshot.child("userType").getValue();


                        binding.nameet.setText(name);

                        Glide.with(ProfileEditActivity.this)
                                .load(profilepic)
                                .placeholder(R.drawable.ic_baseline_person_24)
                                .into(binding.profilepic);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}