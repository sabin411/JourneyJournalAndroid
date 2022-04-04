package com.ismt.ismtproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends FirebaseRecyclerAdapter<Journal, MyAdapter.myViewHolder> {
    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    String currentPhotoPath;


    public MyAdapter(@NonNull FirebaseRecyclerOptions<Journal> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull Journal model) {

        holder.cityName.setText(model.getPlaceName());
        holder.cityCaption.setText(model.getShortCaption());
        holder.visitedDate.setText(model.getDate());
        Glide.with(holder.cityImage.getContext()).load(model.getLocationImage()).into(holder.cityImage);

//function to edit the journal
        holder.editButton.setOnClickListener(view -> {
            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.cityName.getContext())
                    .setContentHolder(new ViewHolder(R.layout.dialogecontent))
                    .setExpanded(true, 2600)
                    .create();

            View myView = dialogPlus.getHolderView();
            ImageView imageUrl = myView.findViewById(R.id.dialogImage);
            Button takePhoto = myView.findViewById(R.id.dialogTakePhotoBtn);
            Button openGallery = myView.findViewById(R.id.dialogOpenGalleryBtn);
            TextInputEditText cityName = myView.findViewById(R.id.dialogCityName);
            TextInputEditText cityDescription = myView.findViewById(R.id.dialogDescription);
            TextInputEditText caption = myView.findViewById(R.id.dialogCaption);
            TextInputEditText date = myView.findViewById(R.id.dialogDate);
            Button editBtn = myView.findViewById(R.id.dialogEditBtn);

//            Log.d("tag", "url" + model.getLocationImage());
            Picasso.get().load(model.getLocationImage()).into(imageUrl);
//            imageUrl.setImageURI(Uri.parse(model.getLocationImage()));
            cityName.setText(model.getPlaceName());
            cityDescription.setText(model.getDescription());
            caption.setText(model.getShortCaption());
            date.setText(model.getDate());

            dialogPlus.show();

            takePhoto.setOnClickListener(view1 -> {

            });

            editBtn.setOnClickListener(view1 -> {
                Map<String, Object> map = new HashMap<>();
                map.put("userId", model.getUserId());
                map.put("placeName", cityName.getText().toString().trim());
                map.put("description", cityDescription.getText().toString().trim());
                map.put("locationImage", model.getLocationImage());
                map.put("shortCaption", caption.getText().toString().trim());
                map.put("date", date.getText().toString().trim());
                map.put("journalKey", model.getJournalKey());
                Log.d("TAG", "-------------onBindViewHolder:----------- "+model.getUserId()+model.getJournalKey());
                FirebaseDatabase.getInstance().getReference().child("Entries/"+model.getUserId()+"/"+model.getJournalKey())
                        .setValue(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                cityName.setText("");
                                cityDescription.setText("");
                                imageUrl.setImageURI(null);
                                caption.setText("");
                                date.setText("");
                                Toast.makeText(caption.getContext(), "insertion is successfull", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(caption.getContext(), "Failed to edit!", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

//        function to delete the journal
        holder.deleteButton.setOnClickListener(view -> {
            FirebaseDatabase.getInstance().getReference().child("Entries/"+model.getUserId()+"/"+model.getJournalKey()).removeValue();
            Toast.makeText(holder.cityImage.getContext(), "The Journal has been removed", Toast.LENGTH_SHORT).show();

        });

        // Function to share data across multiple platform
        holder.shareButton.setOnClickListener(view -> {
            Toast.makeText(holder.cityImage.getContext(), "your journal has been shared to facebook.", Toast.LENGTH_SHORT).show();

        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView cityImage;
        TextView cityName, cityCaption, visitedDate;
        Button editButton, deleteButton, shareButton;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            cityImage = (ImageView)itemView.findViewById(R.id.location_image);
            cityName = (TextView) itemView.findViewById(R.id.location_name);
            cityCaption = (TextView)itemView.findViewById(R.id.location_caption);
            visitedDate = (TextView)itemView.findViewById(R.id.travelled_Date);
            editButton = (Button)itemView.findViewById(R.id.edit_btn);
            deleteButton = (Button) itemView.findViewById(R.id.delete_btn);
            shareButton = (Button) itemView.findViewById(R.id.share_btn);
        }
    }
}
