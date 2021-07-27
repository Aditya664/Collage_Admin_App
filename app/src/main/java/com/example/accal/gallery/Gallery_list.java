package com.example.accal.gallery;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class Gallery_list extends AppCompatActivity {
    RecyclerView gallery_recyclerview1;
    RecyclerView gallery_recyclerview2;
    RecyclerView gallery_recyclerview3;
    RecyclerView gallery_recyclerview4;
    RecyclerView gallery_recyclerview5;

    GalleryAdapter adapter;
    private DatabaseReference Drefrance, DbRef;
    ArrayList<GalleryData> listCT1,listCT2,listCT3,listCT4,listCT5;



    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_list);

        Drefrance = FirebaseDatabase.getInstance().getReference().child("Gallery");



        gallery_recyclerview1 = findViewById(R.id.gallery_recyclerview1);
        gallery_recyclerview2 = findViewById(R.id.gallery_recyclerview2);
        gallery_recyclerview3 = findViewById(R.id.gallery_recyclerview3);
        gallery_recyclerview4 = findViewById(R.id.gallery_recyclerview4);
        gallery_recyclerview5 = findViewById(R.id.gallery_recyclerview5);


        getCategory1();
        getCategory2();
        getCategory3();
        getCategory4();
        getCategory5();



    }


    private void getCategory1()
    {
        Drefrance.child("placement").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                listCT1 = new ArrayList<>();

                for (DataSnapshot Dsnapshot : snapshot.getChildren())
                {
                    GalleryData data = Dsnapshot.getValue(GalleryData.class);
                    listCT1.add(data);
                }
                adapter = new GalleryAdapter(Gallery_list.this,listCT1);
                gallery_recyclerview1.setLayoutManager(new GridLayoutManager(Gallery_list.this,3));
                gallery_recyclerview1.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error)
            {  Toast.makeText(Gallery_list.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getCategory2()
    {
        Drefrance.child("events").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
            listCT2 = new ArrayList<>();

                for (DataSnapshot Dsnapshot : snapshot.getChildren())
                {
                    GalleryData data = Dsnapshot.getValue(GalleryData.class);
                    listCT2.add(data);
                }
                adapter = new GalleryAdapter(Gallery_list.this,listCT2);
                gallery_recyclerview2.setLayoutManager(new GridLayoutManager(Gallery_list.this,3));
                gallery_recyclerview2.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error)
            {  Toast.makeText(Gallery_list.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getCategory3()
    {
        Drefrance.child("festivals").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                listCT3 = new ArrayList<>();


                for (DataSnapshot Dsnapshot : snapshot.getChildren())
                {
                    GalleryData data = Dsnapshot.getValue(GalleryData.class);
                    listCT3.add(data);
                }
                adapter = new GalleryAdapter(Gallery_list.this,listCT3);
                gallery_recyclerview3.setLayoutManager(new GridLayoutManager(Gallery_list.this,3));
                gallery_recyclerview3.setAdapter(adapter);
            }



            @Override
            public void onCancelled(@NonNull  DatabaseError error)
            {  Toast.makeText(Gallery_list.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getCategory4()
    {
        Drefrance.child("achivements").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                listCT4 = new ArrayList<>();
                for (DataSnapshot Dsnapshot : snapshot.getChildren())
                {
                    GalleryData data = Dsnapshot.getValue(GalleryData.class);
                    listCT4.add(data);
                }
                adapter = new GalleryAdapter(Gallery_list.this,listCT4);
                gallery_recyclerview4.setLayoutManager(new GridLayoutManager(Gallery_list.this,3));
                gallery_recyclerview4.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error)
            {  Toast.makeText(Gallery_list.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getCategory5()
    {
        Drefrance.child("sports").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
            listCT5 = new ArrayList<>();

                for (DataSnapshot Dsnapshot : snapshot.getChildren())
                {
                    GalleryData data = Dsnapshot.getValue(GalleryData.class);
                    listCT5.add(data);
                }
                adapter = new GalleryAdapter(Gallery_list.this,listCT5);
                gallery_recyclerview5.setLayoutManager(new GridLayoutManager(Gallery_list.this,3));
                gallery_recyclerview5.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {  Toast.makeText(Gallery_list.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }








}