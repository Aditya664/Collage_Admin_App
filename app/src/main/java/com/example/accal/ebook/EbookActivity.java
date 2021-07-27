package com.example.accal.ebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.accal.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EbookActivity extends AppCompatActivity
{

    //====variables======

    private EditText Ebook_title;
    private Button Upload_Ebook_btn;
    private CardView select_Ebook_card;
    //  String category;
    private final int REQ = 2;
    private Uri PdfData;
    ProgressDialog pdPdf;
    private TextView EbookTextView;
    private String EbookName;
    String title,date,time;


    private DatabaseReference Drefrance;
    private StorageReference Srefrance,SRef;

    String PdfDownloadUrl ="",Sfilename;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook);

        Drefrance = FirebaseDatabase.getInstance().getReference();
        Srefrance = FirebaseStorage.getInstance().getReference();


        pdPdf = new ProgressDialog(this);
        EbookTextView = findViewById(R.id.pdttextview);


        Ebook_title = findViewById(R.id.PdfTitle);
        Upload_Ebook_btn = findViewById(R.id.uploadPdfBtn);
        Upload_Ebook_btn.setOnClickListener(v ->
        {
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
            date = currentDate.format(calForDate.getTime());


            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
            time = currentTime.format(calForTime.getTime());





            title = Ebook_title.getText().toString();

            if (title.isEmpty())
            {
                Ebook_title.setError("Empty Title");
                Ebook_title.requestFocus();
            }else if (PdfData == null)
            {
                Toast.makeText(this, "Please select PDF", Toast.LENGTH_SHORT).show();

            }else
            {
                UploadEbok();
            }

        });

        select_Ebook_card = findViewById(R.id.addEbook);
        select_Ebook_card.setOnClickListener(v -> OpenGallray());





    }


    private void UploadEbok()
    {

        pdPdf.setTitle("please wait");
        pdPdf.setMessage("Uploading....");
        pdPdf.show();

        Sfilename = EbookName + "#"+ date +" "+ time +".pdf";
        SRef = Srefrance.child("Ebook").child(Sfilename);
        SRef.putFile(PdfData)
                .addOnSuccessListener(taskSnapshot ->
                {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri uri = uriTask.getResult();
                    UploadData_Ebook(String.valueOf(uri));
                    pdPdf.dismiss();

                }).addOnFailureListener(e ->
        {
            pdPdf.dismiss();
            Toast.makeText(this, "Something went Wrong - Srefrance_E", Toast.LENGTH_SHORT).show();
        });
    }

    private void UploadData_Ebook(String PdfDownloadUrl)
    {




        String UniqueKey_Ebook = Drefrance.child("Ebook").push().getKey();

        HashMap HM=new HashMap();
        HM.put("title",Ebook_title.getText().toString());
        HM.put("Durl",PdfDownloadUrl);
        HM.put("Sfilename",Sfilename);
        HM.put("key",UniqueKey_Ebook);
        Drefrance.child("Ebook").child(UniqueKey_Ebook).setValue(HM)
                .addOnCompleteListener(task ->
                {
                    Toast.makeText(this, "Pdf Uploaded ", Toast.LENGTH_SHORT).show();
                    Ebook_title.setText("");
                }).addOnFailureListener(e ->
        {
            Toast.makeText(this, "Something Went wrong - Drefrance_E", Toast.LENGTH_SHORT).show();
        });
    }


    private void OpenGallray()
    {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF "),REQ);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK)
        {
            PdfData = data.getData();

            if (PdfData.toString().startsWith("content://"))
            {
                try {
                    Cursor cursor= null;
                    cursor = EbookActivity.this.getContentResolver().query(PdfData,null,null,null,null);
                    if (cursor != null && cursor.moveToFirst())
                    {
                        EbookName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to get Pdf name", Toast.LENGTH_SHORT).show();
                }

            }else   if(PdfData.toString().startsWith("file://"))
            {
                EbookName = new File(PdfData.toString()).getName();
            }
            EbookTextView.setText(EbookName);





        }


    }



}