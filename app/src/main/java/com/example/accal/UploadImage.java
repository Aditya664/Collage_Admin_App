package com.example.accal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UploadImage extends AppCompatActivity {

    private Spinner imagecategory;
    private CardView selectimage;
    private Button UploadImg;
    private ImageView gallaryImgView;
    private final int Req = 1;
    private Bitmap bitmap;
    private StorageReference storageReference;
    private DatabaseReference reference;
    String downloadUrl;


    private ProgressDialog pd;
    private String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        selectimage = findViewById(R.id.addgallaryImg);
        imagecategory = findViewById(R.id.imageCategory);
        UploadImg = findViewById(R.id.uploadImgBtn);
        gallaryImgView = findViewById(R.id.GallaryImageView);
        reference = FirebaseDatabase.getInstance().getReference().child("gallery");
        storageReference = FirebaseStorage.getInstance().getReference().child("gallery");
        pd = new ProgressDialog(this);


        String[] items = new String[]{"Select Category", "Convocation", "Independant day", "Other day"};
        imagecategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));

        imagecategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,  int i , long l) {
                category = imagecategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        UploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap == null) {
                    Toast.makeText(UploadImage.this, "Please upload image", Toast.LENGTH_SHORT).show();
                } else if (category.equals("Select Category")) {
                    Toast.makeText(UploadImage.this, "Please select image category", Toast.LENGTH_SHORT).show();
                } else {
                    pd.show();
                    pd.setMessage("Uploading....");
                    uploadimg();
                }
            }
        });
    }
    private void uploadimg() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child(finalimg+"jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UploadImage.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    uploadData();
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(UploadImage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData() {
        reference = reference.child(category);
        final String uniqekey = reference.push().getKey();

        reference.child(uniqekey).setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(UploadImage.this, "Image upladed", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadImage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void openGallery() {
        Intent pickimg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickimg,Req);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Req && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gallaryImgView.setImageBitmap(bitmap);
        }
    }
}