package com.example.accal.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.accal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateTeacherActivity extends AppCompatActivity {
    private ImageView updtechimg;
    private EditText updtechnme,updemail,updpost;
    private Bitmap bitmap = null;
    private Button updtechbtn,deletetchbtn;
    private final int req = 1;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private String downloadUrl,category,uniquekey;


    private String name,email,post,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        post = getIntent().getStringExtra("post");
        image = getIntent().getStringExtra("image");

        uniquekey = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");


        reference = FirebaseDatabase.getInstance().getReference().child("teacher");
        storageReference = FirebaseStorage.getInstance().getReference();



        updtechimg = findViewById(R.id.updateTechImg);
        updtechnme = findViewById(R.id.updatetechname);
        updemail = findViewById(R.id.updatetechEmail);
        updpost = findViewById(R.id.updatetechpost);
        updtechbtn = findViewById(R.id.updatetechbtn);
        deletetchbtn = findViewById(R.id.deletetechbtn);

        updtechimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }

        });

        updtechbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = updtechnme.getText().toString();
                email = updemail.getText().toString();
                post = updpost.getText().toString();
                checkValidation();

            }
        });
        deletetchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();

            }
        });

        try {
            Picasso.get().load(image).into(updtechimg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updemail.setText(email);
        updtechnme.setText(name);
        updpost.setText(post);

    }

    private void deleteData() {
        reference.child(category).child(uniquekey).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UpdateTeacherActivity.this, "Teacher deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateTeacherActivity.this,UpdateFaculty.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(UpdateTeacherActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
   private void checkValidation() {
        if(name.isEmpty()){
            updtechnme.setError("Empty");
            updtechnme.requestFocus();
        }else if(email.isEmpty()) {
            updemail.setError("Empty");
            updemail.requestFocus();
        }else if(post.isEmpty()) {
            updpost.setError("Empty");
            updpost.requestFocus();
        }else if(bitmap == null){
            updatedata(image);
        }else {
            uploadimg();
        }
    }

    private void updatedata(String s) {

        HashMap hp = new HashMap();
        hp.put("name",name);
        hp.put("email",email);
        hp.put("post",post);
        hp.put("image",s);





        reference.child(category).child(uniquekey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateTeacherActivity.this, "Teacher updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateTeacherActivity.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateTeacherActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadimg() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child("Teachers").child(finalimg+"jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UpdateTeacherActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);

                                    updatedata(downloadUrl);

                                }
                            });
                        }
                    });
                } else {
                    //pd.dismiss();
                    Toast.makeText(UpdateTeacherActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openGallery() {
        Intent pickimg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickimg,req);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == req && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updtechimg.setImageBitmap(bitmap);
        }
    }
}