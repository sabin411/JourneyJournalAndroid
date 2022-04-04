package com.ismt.ismtproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class JournalEntry extends AppCompatActivity {
    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int GALLERY_REQUEST_CODE = 105;
    private Button add_photo_btn, journalSaveButton, select_photo_btn;
    private TextInputEditText locationName, description, caption, date;
    private ImageView backArrow, location_image;

    public String ImageUrl;

    String currentPhotoPath;



    DatabaseReference reference;
    FirebaseAuth mAuth;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry);

        add_photo_btn = findViewById(R.id.add_photo_btn);
        select_photo_btn = findViewById(R.id.select_photo_btn);
        journalSaveButton = findViewById(R.id.journalSaveButton);
        locationName = findViewById(R.id.locationName);
        description = findViewById(R.id.description);
        caption = findViewById(R.id.caption);
        date = findViewById(R.id.date);

        backArrow = findViewById(R.id.backArrow);
        location_image = findViewById(R.id.location_image);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        backArrow.setOnClickListener(view-> {
            startActivity(new Intent(JournalEntry.this, DashboardJourneyJournal.class));
        });

        add_photo_btn.setOnClickListener(view -> {

            Toast.makeText(JournalEntry.this, "clicked", Toast.LENGTH_SHORT).show();
            askCameraPermission();
        });

        select_photo_btn.setOnClickListener(view -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, GALLERY_REQUEST_CODE);
        });

        journalSaveButton.setOnClickListener(view -> {
            saveEntry();
        });
    }

    private void saveEntry() {
        String userEnteredLocationName = locationName.getText().toString().trim();
        String userEnteredDescription = description.getText().toString().trim();
        String userEnteredCaption = caption.getText().toString().trim();
        String userEnteredDate = date.getText().toString().trim();

        FirebaseUser rUser = FirebaseAuth.getInstance().getCurrentUser();
        assert rUser != null;
        String userId = rUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Entries").child(userId);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userId", userId);
        hashMap.put("placeName", userEnteredLocationName);
        hashMap.put("description", userEnteredDescription);
        hashMap.put("locationImage", ImageUrl);
        hashMap.put("shortCaption", userEnteredCaption);
        hashMap.put("date", userEnteredDate);
        String entryId = reference.push().getKey();
        hashMap.put("journalKey", entryId);
        reference.child(entryId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(JournalEntry.this, "success" + task.getResult(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(JournalEntry.this, DashboardJourneyJournal.class));
                }
                else{
                    Toast.makeText(JournalEntry.this, "Failed look once", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        else{
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//Open camera
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(JournalEntry.this, "Camera permission is required to Use Camera", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE){
            Toast.makeText(JournalEntry.this, "hope its working", Toast.LENGTH_SHORT).show();
                File f = new File(currentPhotoPath);
                location_image.setImageURI(Uri.fromFile(f));
            Log.d("tag", "Absolute url of image is " + Uri.fromFile(f));

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            uploadImageToFirebase(f.getName(),contentUri);
        }

        if(requestCode == GALLERY_REQUEST_CODE){
            Uri contentUri = data.getData();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
            Log.d("tag", "onAbsoluteResult: Gallery Image Uri: " + imageFileName);
//            location_image .setImageURI(contentUri);

            uploadImageToFirebase(imageFileName,contentUri);


        }

    }

    private void uploadImageToFirebase(String imageFileName, Uri contentUri) {

        final StorageReference image = storageReference.child("image/" + imageFileName);
            image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.get().load(uri).into(location_image);
                            ImageUrl = uri.toString();
                            Log.d("tag", "on Success: Uploaded Image url is : " + uri.toString());
                        }
                    });
                    Toast.makeText(JournalEntry.this, "Image is uploaded", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(JournalEntry.this, "Upload has failed", Toast.LENGTH_SHORT).show();
                }
            });

    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.ismt.ismtproject.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
//        }
//        else {
//            Toast.makeText(JournalEntry.this, "you don't have camera in you device", Toast.LENGTH_SHORT).show();
//
//        }
    }
}