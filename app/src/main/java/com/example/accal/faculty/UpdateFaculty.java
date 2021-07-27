package com.example.accal.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.accal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {
    FloatingActionButton fab;
    private RecyclerView csdept,phydept,chemdept,biodept,maradept;
    private LinearLayout csnodata,chemnodata,phynodata,bionodata,maranodata;
    private List<TeacherData> list1,list2, list3, list4,list5;
    private DatabaseReference reference,dbref;
    private TeacherAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);
        csnodata = findViewById(R.id.csnodata);
        phynodata = findViewById(R.id.phynodata);
        chemnodata = findViewById(R.id.chemnodata);
        bionodata = findViewById(R.id.bionodata);
        maranodata = findViewById(R.id.maranodata);

        csdept = findViewById(R.id.csdept);
        phydept = findViewById(R.id.phydept);
        chemdept = findViewById(R.id.chemdept);
        biodept = findViewById(R.id.biodept);
        maradept = findViewById(R.id.maradept);


        reference = FirebaseDatabase.getInstance().getReference().child("teacher");

        csdept();
        phydept();
        chemdept();
        maradept();
        biodept();




        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateFaculty.this,AddTeachers.class));
            }
        });

    }

    private void chemdept() {
        dbref = reference.child("Chem");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    chemnodata.setVisibility(View.VISIBLE);
                    chemdept.setVisibility(View.GONE);

                }else {
                    chemnodata.setVisibility(View.GONE);
                    chemdept.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list2.add(data);

                    }
                    chemdept.setHasFixedSize(true);
                    chemdept.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list2,UpdateFaculty.this,"Chem");
                    chemdept.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void biodept() {
        dbref = reference.child("bio");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                list5 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    bionodata.setVisibility(View.VISIBLE);
                    biodept.setVisibility(View.GONE);

                }else {
                    bionodata.setVisibility(View.GONE);
                    biodept.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list5.add(data);

                    }
                    biodept.setHasFixedSize(true);
                    biodept.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list5,UpdateFaculty.this,"bio");
                    biodept.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void csdept() {
        dbref = reference.child("Cs");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    csnodata.setVisibility(View.VISIBLE);
                    csdept.setVisibility(View.GONE);

                }else {
                    csnodata.setVisibility(View.GONE);
                    csdept.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list1.add(data);

                    }
                    csdept.setHasFixedSize(true);
                    csdept.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list1,UpdateFaculty.this,"Cs");
                    csdept.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void phydept() {
        dbref = reference.child("Phy");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    phynodata.setVisibility(View.VISIBLE);
                    phydept.setVisibility(View.GONE);

                }else {
                    phynodata.setVisibility(View.GONE);
                    phydept.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list3.add(data);

                    }
                    phydept.setHasFixedSize(true);
                    phydept.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list3,UpdateFaculty.this,"Phy");
                    phydept.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void maradept() {
        dbref = reference.child("marathi");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    maranodata.setVisibility(View.VISIBLE);
                    maradept.setVisibility(View.GONE);

                }else {
                    maranodata.setVisibility(View.GONE);
                    maradept.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);

                    }
                    maradept.setHasFixedSize(true);
                    maradept.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list4,UpdateFaculty.this,"marathi");
                    maradept.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}