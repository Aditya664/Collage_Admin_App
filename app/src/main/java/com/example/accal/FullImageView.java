package com.example.accal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class FullImageView extends AppCompatActivity {

    private PhotoView Fullscreen_imageView;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_view);


        image = getIntent().getStringExtra("imageurl_pass");
        Fullscreen_imageView = findViewById(R.id.Fullscreen_imageView);


        try {
            Glide.with(this).load(image).into(Fullscreen_imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}