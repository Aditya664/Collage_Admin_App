package com.example.accal.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddTeachers extends AppCompatActivity {

    private ImageView addtecherimg;
    private EditText addtechname,addtechemail,addtechpost;
    private Spinner addtechcat;
    private Button addtechbtn;
    private String name,email,post,downloadUrl = "";
    private Bitmap bitmap = null;
    private final int req = 1;
    private String category;
    private ProgressDialog pd;
    private StorageReference storageReference;
    private DatabaseReference reference,dbref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teachers);

        addtecherimg = findViewById(R.id.addTecherImg);
        addtechname = findViewById(R.id.addTecherName);
        addtechemail = findViewById(R.id.addTeacherEmail);
        addtechpost = findViewById(R.id.addTeacherPost);
        addtechcat = findViewById(R.id.addtechercategory);
        addtechbtn = findViewById(R.id.addTeacherbtn);
        reference = FirebaseDatabase.getInstance().getReference().child("teacher");
        storageReference = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(this);


        addtechbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        String[] items = new String[]{"Select Category", "Cs", "Chem", "Phy" ,"bio","marathi"};
        addtechcat.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));

        addtechcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,  int i , long l) {
                category = addtechcat.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        addtecherimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void checkValidation() {
        name = addtechname.getText().toString();
        email = addtechemail.getText().toString();
        post = addtechpost.getText().toString();

        if(name.isEmpty()){
            addtechname.setError("Empty");
            addtechname.requestFocus();
        }else if(email.isEmpty()){
            addtechemail.setError("Empty");
            addtechemail.requestFocus();
        }else if(post.isEmpty()){
            addtechpost.setError("Empty");
            addtechpost.requestFocus();
        }else if(category.equals("Select Category")){
            Toast.makeText(this, "Please enter techear category", Toast.LENGTH_SHORT).show();

        }else if(bitmap == null){
            insertData();
        }else {
            insertImg();
        }
    }

    private void insertImg() {
        pd.setMessage("Uploading.....");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child("Teachers").child(finalimg+"jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddTeachers.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    insertData();

                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(AddTeachers.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void insertData() {
        dbref = reference.child(category);
        final String uniquekey = dbref.push().getKey();

        TeacherData teacherData = new TeacherData(name,email,post,downloadUrl, uniquekey);
        dbref.child(uniquekey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(AddTeachers.this, "Teacher uploaded", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddTeachers.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
            addtecherimg.setImageBitmap(bitmap);
        }
    }
}