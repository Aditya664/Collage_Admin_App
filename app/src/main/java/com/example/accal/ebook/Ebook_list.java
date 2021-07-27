package com.example.accal.ebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.accal.R;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class Ebook_list extends AppCompatActivity
{
    private RecyclerView ebook_recyclerview;
    private ProgressBar progressBar;
    private ArrayList<ebookData> list;
    private ebookAdapter adapter;
    private DatabaseReference Drefrance;
    private Intent intent;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook_list);


         fab = findViewById(R.id.AddEbookBtn_fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this,EbookActivity.class);
            startActivity(intent);
        });
        ebook_recyclerview = findViewById(R.id.ebook_recyclerview);
        progressBar = findViewById(R.id.Progressbar_listEbook);
        Drefrance = FirebaseDatabase.getInstance().getReference().child("Ebook");

        ebook_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        ebook_recyclerview.setHasFixedSize(true);


        getEbookData();





    }

    private void getEbookData()
    {
        Drefrance.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                list = new ArrayList<>();
                for(DataSnapshot Dsnapshot : snapshot.getChildren())
                {
                    ebookData data = Dsnapshot.getValue(ebookData.class);
                    list.add(data);

                }

                adapter =new ebookAdapter(Ebook_list.this,list);
                adapter.notifyDataSetChanged();
                ebook_recyclerview.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Ebook_list.this,error.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });

    }
}