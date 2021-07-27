package com.example.accal.faculty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accal.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewAdapter> {
    private List<TeacherData> list;
    private Context context;
    private String category;

    public TeacherAdapter(List<TeacherData> list, Context context,String category) {
        this.list = list;
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public TeacherViewAdapter onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacherfacultyitems, parent,false);

        return new TeacherViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  TeacherAdapter.TeacherViewAdapter holder, int position) {

        final TeacherData item = list.get(position);
        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.post.setText(item.getPost());

        try {
            Picasso.get().load(item.getImage()).into(holder.Imgview);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateTeacherActivity.class);
                intent.putExtra("name",item.getName());
                intent.putExtra("email",item.getEmail());
                intent.putExtra("post",item.getPost());
                intent.putExtra("image",item.getImage());
                intent.putExtra("key",item.getKey());
                intent.putExtra("category",category);


                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TeacherViewAdapter extends RecyclerView.ViewHolder {

        private TextView name,email,post;
        private Button update;
        private ImageView Imgview;


        public TeacherViewAdapter(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teachName);
            email = itemView.findViewById(R.id.teachemail);
            post = itemView.findViewById(R.id.teachPost);
            Imgview = itemView.findViewById(R.id.techimg);
            update = itemView.findViewById(R.id.updatetech);


        }
    }
}
