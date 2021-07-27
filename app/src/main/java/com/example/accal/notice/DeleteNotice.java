package com.example.accal.notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.accal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeleteNotice extends AppCompatActivity {

        private RecyclerView DeleteNoticerecyclere;
        private ProgressBar pgbar;
        private ArrayList<NoticeData> list;
        private NoticeAdapter adapter;

        private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);

        DeleteNoticerecyclere = findViewById(R.id.DeleteNoticerecyclere);
        pgbar = findViewById(R.id.pgbar);

        reference = FirebaseDatabase.getInstance().getReference().child("Notice");

        DeleteNoticerecyclere.setLayoutManager(new LinearLayoutManager(this));
        DeleteNoticerecyclere.setHasFixedSize(true);


        getNotice();
    }

    private void getNotice() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    NoticeData data = snapshot.getValue(NoticeData.class);
                    list.add(data);

                }
                adapter = new NoticeAdapter(DeleteNotice.this, list);
                adapter.notifyDataSetChanged();
                pgbar.setVisibility(View.GONE);

                DeleteNoticerecyclere.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
pgbar.setVisibility(View.GONE);;
                Toast.makeText(DeleteNotice.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}