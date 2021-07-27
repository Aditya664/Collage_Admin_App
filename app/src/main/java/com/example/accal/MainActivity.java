package com.example.accal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.accal.ebook.EbookActivity;
import com.example.accal.ebook.Ebook_list;
import com.example.accal.faculty.UpdateFaculty;
import com.example.accal.notice.DeleteNotice;
import com.example.accal.notice.uploadNotice;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView addNotice,addGalleryImage,addEbook,faculty,DeleteNotice,logout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("Login",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getString("isLogin","false").equals("false")){
            openLogin();
        }

        DeleteNotice = findViewById(R.id.deleteNotice);
        DeleteNotice.setOnClickListener(this);
        logout = findViewById(R.id.Logout);
        logout.setOnClickListener(this);

        addNotice = findViewById(R.id.addNotice);
        addNotice.setOnClickListener(this);

        addGalleryImage = findViewById((R.id.addGalleryImage));
        addGalleryImage.setOnClickListener(this);

        addEbook = findViewById(R.id.addEbook);
        addEbook.setOnClickListener(this);

        faculty = findViewById(R.id.faculty);
        faculty.setOnClickListener(this);


    }

    private void openLogin() {
        startActivity(new Intent(MainActivity.this,LogineActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){

            case R.id.addNotice:
                intent = new Intent(MainActivity.this, uploadNotice.class);
                startActivity(intent);
                break;
            case R.id.addGalleryImage:
                intent = new Intent(MainActivity.this,UploadImage.class);
                startActivity(intent);
                break;
            case R.id.addEbook:
                intent = new Intent(MainActivity.this, Ebook_list.class);
                startActivity(intent);
                break;
            case R.id.faculty:
                intent = new Intent(MainActivity.this, UpdateFaculty.class);
                startActivity(intent);
                break;
            case R.id.deleteNotice:
                intent = new Intent(MainActivity.this, DeleteNotice.class);
                startActivity(intent);
                break;
            case R.id.Logout:
                editor.putString("isLogin","false");
                editor.commit();
                openLogin();
                break;


        }

    }
}