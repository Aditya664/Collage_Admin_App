package com.example.accal.gallery;

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

import com.example.accal.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UploadGallreyImage extends AppCompatActivity
{


    //====variables======

    private Spinner image_categorySpinner;
    private Button Upload_gallrey_img_btn;
    private CardView select_gallreyImg_card;
    private ImageView Gallrey_imageview;
    String category;
    private final int REQ = 2;
    private Bitmap bitmap_GallryImg;
    ProgressDialog pdIMG;

    private DatabaseReference Drefrance,DRef;
    private StorageReference Srefrance,SRef;
    String GalleryDownloadUrl ="",Sfilename;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_gallrey_image);

                image_categorySpinner = findViewById(R.id.Image_catagory_spinner);
                select_gallreyImg_card = findViewById(R.id.select_Gallrey_image_card);
                Gallrey_imageview  = findViewById(R.id.Gallrey_image_view);
                Upload_gallrey_img_btn = findViewById(R.id.Upload_GallreyImg_btn);
                pdIMG = new ProgressDialog(this);


        Drefrance = FirebaseDatabase.getInstance().getReference().child("Gallery");
        Srefrance = FirebaseStorage.getInstance().getReference().child("Gallery");


      select_gallreyImg_card.setOnClickListener(v ->  openGallrey());


        Upload_gallrey_img_btn.setOnClickListener(vbtn ->
        {
            if(bitmap_GallryImg==null)
            {
                Toast.makeText(this, "please select image", Toast.LENGTH_SHORT).show();
            }else if(category.equals("select category"))
            {
                Toast.makeText(this, "please select category", Toast.LENGTH_SHORT).show();
            }
            else
            {
                pdIMG.setMessage("Uploading....");
                UploadImage_Gallrey();
            }
        });



        String [] SpinnerItems = new String[]{"select category","festivals","events","sports","achivements","placement"};
        image_categorySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,SpinnerItems));
        image_categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                category = image_categorySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void UploadImage_Gallrey()
    {
        pdIMG.setMessage("uploading....");
        pdIMG.setTitle("please wait");
        pdIMG.show();

        ByteArrayOutputStream Baos = new ByteArrayOutputStream();
        bitmap_GallryImg.compress(Bitmap.CompressFormat.JPEG,50,Baos);
        byte [] finalimg = Baos.toByteArray();
        final StorageReference filepath_Gallrey;

        Sfilename = finalimg + "JPEG";
        filepath_Gallrey = Srefrance.child(Sfilename);
        final UploadTask uploadTask = filepath_Gallrey.putBytes(finalimg);
        uploadTask.addOnCompleteListener(this, task ->
        {
            if (task.isSuccessful())
            {
                uploadTask.addOnSuccessListener(taskSnapshot ->
                {
                    filepath_Gallrey.getDownloadUrl().addOnSuccessListener(uri ->
                    {
                        GalleryDownloadUrl=String.valueOf(uri);
                        uploadData_Gallrey();
                    });
                });
            }else
            {
                pdIMG.dismiss();
                Toast.makeText(this, "Something went wrong - Srafrance_G", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData_Gallrey()
    {



        DRef = Drefrance.child(category);
      final String UniqueKey_Gallrey = DRef.push().getKey();


        GalleryData galleryData = new GalleryData(GalleryDownloadUrl,category,UniqueKey_Gallrey,Sfilename);

        DRef.child(UniqueKey_Gallrey).setValue(galleryData)
              .addOnSuccessListener(unused ->
              {
                  pdIMG.dismiss();

                 recreate();
                  Toast.makeText(this, "Gallrey Image Uploaded", Toast.LENGTH_SHORT).show();


             }).addOnFailureListener(e ->
             {
                 pdIMG.dismiss();
                 Toast.makeText(this, "something went wrong-Drefrance_G", Toast.LENGTH_SHORT).show();
             });
    }

    private void openGallrey()
    {
        Intent PickImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        startActivityForResult(PickImg ,REQ);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();
            try
            {
                bitmap_GallryImg= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e)
            {
                Toast.makeText(this, "unable to pick image", Toast.LENGTH_SHORT).show();
            }
            Gallrey_imageview.setImageBitmap(bitmap_GallryImg);

        }


    }

}