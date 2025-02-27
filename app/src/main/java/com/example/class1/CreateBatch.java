package com.example.class1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateBatch extends AppCompatActivity {
    Button select;
    EditText newBatch ;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressBar p;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        p.setVisibility(View.GONE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_batch);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        select=findViewById(R.id.select);
        newBatch=findViewById(R.id.newBatch);
        p=findViewById(R.id.ProgressBar5);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(newBatch.getText().toString()).child("Users").setValue("Users");
                Toast.makeText(getApplicationContext(),"Batch Successfully Created!!!",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),Batches.class);
                p.setVisibility(View.VISIBLE);
                startActivity(i);
            }
        });
    }
}
