package com.abdulrehman.android.insertionfirebase2712;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class MainActivity extends AppCompatActivity {

    EditText txt;
    ImageView img;
    Button btn;
    int RC_IMAGE = 100;
    DatabaseReference mDataBase;
    Uri uri = null;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.txt);
        img = findViewById(R.id.img);
        btn = findViewById(R.id.btn);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Uploading Image");

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, RC_IMAGE);
            }
        });

        mDataBase = FirebaseDatabase.getInstance().getReference("Story").push();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgress.show();
                if (!TextUtils.isEmpty(txt.getText().toString()) && uri!=null){


                    StorageReference mref = FirebaseStorage.getInstance().getReference();
                    StorageReference filepath = mref.child("Photos").child(uri.getLastPathSegment());

                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDataBase.child("Name").setValue(txt.getText().toString());
                            mDataBase.child("Image").setValue(taskSnapshot.getDownloadUrl().toString());
                            mProgress.dismiss();
                            Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
                        }
                    });



                }
                else {
                    mProgress.dismiss();
                    Toast.makeText(MainActivity.this, "All fields Req.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RC_IMAGE && resultCode==RESULT_OK)
        {
            uri = data.getData();
            img.setImageURI(uri);
        }

    }
}