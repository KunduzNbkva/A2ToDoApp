package com.example.todoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.todoapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

import static android.provider.CalendarContract.CalendarCache.URI;


public class ProfileActivity extends AppCompatActivity {
    private ImageView imageViewProfile,open_gallery;
   private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Uri profileImgUri;
    EditText name,email;
    Button save;
    FirebaseFirestore dataBase;
    Users users;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageViewProfile=findViewById(R.id.imageViewProfile);
        name=findViewById(R.id.edit_name);
        email=findViewById(R.id.edit_email);

        save=findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String names=name.getText().toString().trim();
               String emails=email.getText().toString().trim();

                 users=new Users(names,emails);
                saving(users);
            }
        });

        open_gallery=findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              pickImage(v);
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String image=preferences.getString("image","");
        Glide.with(this).load(image).into(imageViewProfile);
        //getName(users);

    }
    public void pickImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(URI,"image/*");
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;

       if(requestCode==0 && resultCode==RESULT_OK && data!=null){
                    profileImgUri = data.getData();
                    preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    editor = preferences.edit();
                    editor.clear();
                    editor.putString("image",String.valueOf(profileImgUri)).apply();
                    Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profileImgUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageViewProfile.setRotation(90);
                    imageViewProfile.setImageBitmap(bitmap);
                    imageViewProfile.invalidate();
                }

    }


    public void saving(Users users){
        FirebaseFirestore.getInstance().collection("users").add(users)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful())
                            Toast.makeText(ProfileActivity.this, "Успешно", Toast.LENGTH_SHORT).show();

                    }
                });
    }
//    public void getName(final Users users){
//        dataBase= FirebaseFirestore.getInstance();
//        dataBase.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if(!queryDocumentSnapshots.isEmpty()){
//                    List<DocumentSnapshot> documentSnapshotList=queryDocumentSnapshots.getDocuments();
//                    for (DocumentSnapshot d:documentSnapshotList){
//                        Users users1=d.toObject(Users.class);
//                        name.setText(users.getTitle());
//
//                    }
//
//                }
//            }
//        });
//    }

}

