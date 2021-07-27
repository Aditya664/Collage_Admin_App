package com.example.accal.gallery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.accal.FullImageView;
import com.example.accal.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.List;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.galleryViewAdapter>
{


    private final Context context;
    private List<GalleryData> list;
    private DatabaseReference Drefrance,DRef;
    private StorageReference Srafrance,SRef;
    private int positionref;

    public GalleryAdapter(Context context, List<GalleryData> list)
    {

        this.context = context;
        this.list = list;
    }

    @Override
    public galleryViewAdapter onCreateViewHolder(@NonNull  ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.gallrey_imageitem_layout,parent,false);
        return new galleryViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  galleryViewAdapter holder, int position)
    {
        positionref=position;

        try {

            Glide.with(context).load(list.get(position).getDurl()).into(holder.gallery_imageView);
            holder.gallery_imageView.setOnClickListener(v ->
            {
                Intent intent = new Intent(context, FullImageView.class);
                intent.putExtra("imageurl_pass",list.get(position).getDurl());
                context.startActivity(intent);
            });


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "FAILED to load image", Toast.LENGTH_SHORT).show();


        }
        holder.deletebtn.setOnClickListener(v -> { alertDelete(); });





    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class galleryViewAdapter extends RecyclerView.ViewHolder
    {
        ImageView gallery_imageView;
        Button deletebtn;
        public galleryViewAdapter(@NonNull  View itemView)
        {
            super(itemView);
            gallery_imageView = itemView.findViewById(R.id.gallery_item_imageView);
            deletebtn = itemView.findViewById(R.id.gallery_delete_btn);

        }
    }


    private void alertDelete()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("delete this Image ?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("confirm", (dialog1, which) ->
        {
            Srafrance = FirebaseStorage.getInstance().getReference().child("Gallery");
            SRef=Srafrance.child(list.get(positionref).getSfilename());
            SRef.delete().addOnSuccessListener(unused ->
            {
                Toast.makeText(context, "image delete-cloud", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e ->
            {
                Toast.makeText(context, "image delete-cloud failed", Toast.LENGTH_SHORT).show();

            })  ;



            Drefrance = FirebaseDatabase.getInstance().getReference().child("Gallery");
            DRef =  Drefrance.child(list.get(positionref).getCategory());
            DRef.removeValue()
                    .addOnCompleteListener(task ->
                    {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e ->
            {
                Toast.makeText(context, "Seometing went wrong -Srefrance-DN", Toast.LENGTH_SHORT).show();
            });
            try {
                notifyItemRemoved(positionref);
            } catch (Exception e) {
                e.printStackTrace();
            }



        });

        dialog.setNegativeButton("Cancle", (Idialog, which) ->
        {
            Idialog.cancel();

        });

        AlertDialog Adialog = null;
        try {
            Adialog = dialog.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(dialog != null)
        {
            dialog.show();
        }


    }




}
