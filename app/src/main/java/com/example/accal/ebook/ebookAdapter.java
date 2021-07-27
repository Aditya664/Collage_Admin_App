package com.example.accal.ebook;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.accal.R;


import java.util.List;


public class ebookAdapter extends RecyclerView.Adapter<ebookAdapter.EbookViewHolder>
{
    private final Context context;

    public ebookAdapter(Context context, List<ebookData> list)
    {
        this.context = context;
        this.list = list;
    }
    private DatabaseReference Drefrance;
    private StorageReference Srafrance,SRef;


    private final List<ebookData> list;

    private String urlpass,namepass,Sfilename;

    @NotNull
    @Override
    public EbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.ebook_item_layout,parent,false);
        return new EbookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  EbookViewHolder holder, int position)
    {   urlpass = list.get(position).getDurl();
        namepass = list.get(position).getTitle();
        Sfilename = list.get(position).getSfilename();
        holder.ebook_name.setText(namepass);
        holder.ebook_download_btn.setOnClickListener(v ->
        {
        /*

            try {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlpass));
                context.startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No application can handle this request."
                        + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

         */

            Uri uri = Uri.parse(urlpass);

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                    DownloadManager.Request.NETWORK_MOBILE);


            request.setTitle(namepass);
            request.setDescription("Downloading pdf");

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,namepass+"vvp-pdf ");
            request.setMimeType("*/*");
            downloadManager.enqueue(request);


            Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show();


        });

        holder.itemView.setOnClickListener(v ->
        {

        /*  Intent viewpdfintent = new Intent(context,pdfViewerActivity.class);
            viewpdfintent.putExtra("ebookUrl_pass",Uri.parse(list.get(position).getEbookurl()));
            viewpdfintent.putExtra("ebookname_pass",Uri.parse(list.get(position).getName()));
            context.startActivity(viewpdfintent);
            */
            Toast.makeText(context, "in app pdf viewer \n feature in devlopment", Toast.LENGTH_SHORT).show();


        });

        holder.delete_Ebook_btn.setOnClickListener(v ->
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage("delete this Notice ?");
            dialog.setCancelable(true);
            dialog.setPositiveButton("confirm", (dialog1, which) ->
            {
                Srafrance = FirebaseStorage.getInstance().getReference();
                SRef = Srafrance.child("Ebook/"+ list.get(position).getSfilename());
                SRef.delete().addOnSuccessListener(unused ->
                {
                    Toast.makeText(context, "image delete-cloud", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e ->
                {
                    Toast.makeText(context, "image delete-cloud failed", Toast.LENGTH_SHORT).show();

                })  ;



                Drefrance = FirebaseDatabase.getInstance().getReference().child("Ebook");
                Drefrance.child(list.get(position).getKey()).removeValue()
                        .addOnCompleteListener(task ->
                        {
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e ->
                {
                    Toast.makeText(context, "Seometing went wrong -Srefrance-DN", Toast.LENGTH_SHORT).show();
                });
                try {
                    notifyItemRemoved(position);
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



    });




    }





    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EbookViewHolder extends RecyclerView.ViewHolder
    {
        private final Button delete_Ebook_btn;
        private final TextView ebook_name;
        private final ImageView ebook_download_btn;


        public EbookViewHolder(@NonNull  View itemView)
        {   super(itemView);

            ebook_name = itemView.findViewById(R.id.ebook_name);
            ebook_download_btn = itemView.findViewById(R.id.ebook_download_btn);
            delete_Ebook_btn = itemView.findViewById(R.id.delete_Ebook_btn);


        }
    }




}

