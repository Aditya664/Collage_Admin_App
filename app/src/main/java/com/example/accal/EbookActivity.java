package com.example.accal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.io.File;
import java.util.HashMap;

public class EbookActivity extends AppCompatActivity
{

    private CardView addpdf;
    private Button addpdfbtn;
    private TextView pdfTitle;
    private final int Req = 1;
    private Uri pdfdata;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    String downloadUrl = "";
    private String EbookName;
    private TextView pdftextview;
    private String pdfName,title;


    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook);
        pdftextview = findViewById(R.id.pdttextview);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        pd = new ProgressDialog(this);


        pdfTitle = findViewById(R.id.PdfTitle);
        addpdfbtn = findViewById(R.id.uploadPdfBtn);
        addpdfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = pdfTitle.getText().toString();
                if(title.isEmpty()){
                    pdfTitle.setError("Empty");
                    pdfTitle.requestFocus();
                }else if(pdfdata == null){
                    Toast.makeText(EbookActivity.this, "Please upload pdf", Toast.LENGTH_SHORT).show();
                }else {
                    UploadEbook();
                }
            }
        });

        addpdf = findViewById(R.id.addEbook);
        addpdf.setOnClickListener(v -> OpenGallray());





    }

    private void UploadEbook() {
        pd.setTitle("Please wait...");
        pd.setMessage("uploading pdf...");
        pd.show();
        StorageReference reference = storageReference.child("pdf/"+pdfName+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfdata)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri>  uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        UploadData(String.valueOf(uri));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(EbookActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UploadData(String downloadUrl) {
        String uniqekey = databaseReference.child("pdf").push().getKey();

        HashMap data = new HashMap();
        data.put("pdf title", title);
        data.put("Pdf url", downloadUrl);

        databaseReference.child("pdf").child(uniqekey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(EbookActivity.this, "pdf uploaded...", Toast.LENGTH_SHORT).show();
                pdfTitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(EbookActivity.this, "failed to upload pdf", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void OpenGallray()
    {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF "),Req);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Req && resultCode == RESULT_OK)
        {
            pdfdata = data.getData();
            if (pdfdata.toString().startsWith("content://"))
            {
                Cursor cursor= null;
                try {
                    cursor = EbookActivity.this.getContentResolver().query(pdfdata,null,null,null,null);
                    if(cursor != null && cursor.moveToFirst()){
                        EbookName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else   if(pdfdata.toString().startsWith("file://"))
            {
                EbookName = new File(pdfdata.toString()).getName();
            }
            pdftextview.setText(EbookName);


        }


    }



}