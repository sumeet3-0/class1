package com.example.class1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Discussion extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<String> usersList = new ArrayList<String>();
    ListView List;
    EditText chatBox;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        List = findViewById(R.id.List);
        chatBox=findViewById(R.id.chat);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = mAuth.getCurrentUser();
                reference.child("Mapp").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userName = dataSnapshot.getValue().toString();
                        SimpleDateFormat s = new SimpleDateFormat("dd:MM:yyyy hh:mm:ss");
                        String format = s.format(new Date());
                        reference.child("Chats").child(format).child(userName).setValue(chatBox.getText().toString());
                        chatBox.setText("");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("unique", "loadPost:onCancelled", databaseError.toException());
                    }
                });
            }
        });
        reference.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String s = postSnapshot.getValue().toString(),userName="" , message="";
                    char eq='=',end='}',open='{';
                    int i = -1;
                    while(s.charAt(++i)!=eq)
                    {
                        if(s.charAt(i) == open)
                            continue;
                        userName += s.charAt(i);
                    }
                    while(s.charAt(++i)!=end)
                    {
                        message += s.charAt(i);
                    }
                    usersList.add(userName.toUpperCase()+": "+message);
                }
                arrayAdapter =
                        new ArrayAdapter<>(Discussion.this, R.layout.customlist, usersList);
                List.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("unique", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }
}
