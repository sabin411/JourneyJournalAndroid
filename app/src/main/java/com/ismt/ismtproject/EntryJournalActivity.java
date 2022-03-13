package com.ismt.ismtproject;

import static androidx.core.content.FileProvider.getUriForFile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import java.io.File;
import java.time.Year;
import java.util.Calendar;

public class EntryJournalActivity extends AppCompatActivity {

    private Button openCameraBtn, openDatePicker;
    private ImageView imageView;
    private AppCompatEditText title, description;
//    private AppCompatTextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_journal);

        openCameraBtn = findViewById(R.id.openCameraBtn);
        openDatePicker = findViewById(R.id.openDatePicker);
        imageView = findViewById(R.id.imageView);

        openCameraBtn.setOnClickListener(view -> {
            checkAndRequestPermissions();
            Log.e("test", "test");
        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

            }
        }, year, month, day);
        openDatePicker.setOnClickListener(v -> {
            dialog.show();
        });
    }

    int code = 102;

    void checkAndRequestPermissions() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions((new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}), 100);
        } else if (code == 102) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File imagePath = new File(getFilesDir(), "my_images");
            File newFile = new File(imagePath, "default_image.jpg");
            Uri contentUri = getUriForFile(this, "com.ismt.ismtproject", newFile);

            startActivityForResult(cameraIntent, 101);
//            launchCameraIntent.launch(cameraIntent);
        } else {
//            Intent gallery = new Intent();
//            gallery.setAction(Intent.ACTION_VIEW);
//            gallery.setType("img/*");
//            startActivityForResult(gallery, 102);
            Intent intent = new Intent(Intent.ACTION_PICK);
            startActivityForResult(intent, 103);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            imageView.setImageBitmap(photo);
        } else if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            Log.e("select response", data.getExtras().get("data").toString());
        }
    }
}


