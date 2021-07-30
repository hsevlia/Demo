package com.example.document;

import android.app.ProgressDialog;
import android.net.Uri;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class documents {


    public static void uploadPDFFileFirebase(Uri data, EditText filename, ProgressDialog progressDialog) {



        StorageReference storageReference ;
        DatabaseReference databaseReference;



        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploadPDF");



        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        StorageReference reference = storageReference.child("upload"+System.currentTimeMillis()+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri uri = uriTask.getResult();

                        putPDF putPDF = new putPDF(filename.getText().toString(),uri.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(putPDF);
                        progressDialog.dismiss();



                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress = (100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File Uploading... "+(int) progress+"%");

            }
        });
    }






}
