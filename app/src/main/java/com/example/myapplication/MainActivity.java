package com.example.myapplication;

import static android.view.View.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.document.documents;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    StorageReference storageReference ;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;


    Button UploadButton;
    CardView SelectButton;
    EditText filename ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         progressDialog = new ProgressDialog(this);

        UploadButton = findViewById(R.id.upload);
        SelectButton = findViewById(R.id.select);
        filename = findViewById(R.id.filename);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploadPDF");


        UploadButton.setEnabled(false);
        filename.setEnabled(false);

        SelectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                selectPDF();


            }
        });



    }

    private void selectPDF() {


        Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),12);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==12&&resultCode ==RESULT_OK&& data!= null &&data.getData()!=null)
        {

            UploadButton.setEnabled(true);
            filename.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));
            filename.setEnabled(true);

            UploadButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {



                    documents.uploadPDFFileFirebase(data.getData(),filename,progressDialog);
                    Toast.makeText(MainActivity.this,"File Uploaded",Toast.LENGTH_LONG).show();

                }
            });


        }
    }


}