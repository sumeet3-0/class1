package com.example.class1;




import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegActivity extends AppCompatActivity {
    private final int CHOOSE_IMAGE=21;
    TextView rules;
    ProgressBar progressBar;
    private static final String TAG = "EmailPassword";
    public EditText mEmailField,name,school,address,occParent,parent,mobNo;
    public Spinner std,board,medium;
    private EditText mPasswordField;
     private FirebaseAuth mAuth;
     private ImageView studentImage;
    private Button submit;
    private Button save;
    FirebaseDatabase database;
    DatabaseReference reference;
    String profileImageurl;
    Uri uriProfileImage;
    CheckBox checkBox2;
    public  static final Pattern EMAIL_ADDRESS=Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"+"\\@"+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+"(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+")+");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        rules = findViewById(R.id.rules);
        submit = findViewById(R.id.submit);
        name = findViewById(R.id.name);
        school = findViewById(R.id.school);
        mobNo = findViewById(R.id.mobNo);
        address = findViewById(R.id.address);
        occParent = findViewById(R.id.occParent);
        parent = findViewById(R.id.parent);
        std = findViewById(R.id.std);
        board = findViewById(R.id.board);
        medium = findViewById(R.id.medium);
        save=findViewById(R.id.save);
        progressBar=findViewById(R.id.progressBar);
        studentImage=findViewById(R.id.studentImage);
        checkBox2=findViewById(R.id.checkBox2);

       /*





        String mobNoS = mobNo.getText().toString();
        if(mobNoS.length()!=10){
            mobNo.setError("Please Enter valid name");
            mobNo.setText("");
        }

      //  String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String mEmailFieldS = mEmailField.getText().toString();

        if(mEmailFieldS.isEmpty()){
            mEmailField.setError("Please Enter valid email address");
            mEmailField.setText("");
        }else{
            if(!Patterns.EMAIL_ADDRESS.matcher(mEmailFieldS).matches()){
                mEmailField.setError("Please Enter valid email address");
                mEmailField.setText("");
            }
        }
        String boardS =board.getSelectedItem().toString();
        String mediumS = medium.getSelectedItem().toString();
        String stdS= std.getSelectedItem().toString();
        String mPasswordFieldS = mPasswordField.getText().toString();
        if(mPasswordFieldS.length()<6)
        {
            mPasswordField.setError("Please set valid password");
            mPasswordField.setText("");
        }
      //  ImageView studentImage=(ImageView);



       /* studentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });*/

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

    studentImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showImageChooser();
        }
    });
        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TermsConditions.class);
                startActivity(i);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateEmail()|!validatePassword() | !validateMobno() | !validatename() | !validateschool() | !validateparent() | !validateaddress() | !validateoccupation() | !validateterms())
                {
                    return;
                }
               else {
                    createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());

                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("Users");
                    String nameS = name.getText().toString();
                    String schoolS = school.getText().toString();

                    String parentS = parent.getText().toString();


                    String occParentS = occParent.getText().toString();

                    String addressS = address.getText().toString();

                    String mobNoS = mobNo.getText().toString();


                    String mEmailFieldS = mEmailField.getText().toString();

                    

                    String boardS = board.getSelectedItem().toString();
                    String mediumS = medium.getSelectedItem().toString();
                    String stdS = std.getSelectedItem().toString();
                    String mPasswordFieldS = mPasswordField.getText().toString();
                    Info info = new Info(nameS, schoolS, parentS, occParentS, addressS, mobNoS, mEmailFieldS, mPasswordFieldS, boardS, mediumS, stdS);
                    reference.child(nameS).setValue(info);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }

            private boolean validateterms() {

                if(!checkBox2.isChecked()){
                    Toast.makeText(RegActivity.this, "Please check the terms and conditions", Toast.LENGTH_SHORT).show();
                    return false;

                }
                return true;


            }

            private boolean validateoccupation() {

                String occParentS = occParent.getText().toString();


                if(occParentS.equals("")){
                    occParent.setError("Please Enter valid address");
                    return false;
                }
                return true;
            }

            private boolean validateaddress() {

                String addressS = address.getText().toString();
                if(addressS.equals("")){
                    address.setError("Please Enter valid address");
                    return false;
                }
                return true;
            }

            private boolean validateparent() {

                String parentS = parent.getText().toString();
                if(parentS.equals("")){
                    parent.setError("Please Enter valid parent name");
                    return false;
                }
                return true;
            }

            private boolean validateschool() {

                String schoolS = school.getText().toString();
                if(schoolS.equals("")){
                    school.setError("Please Enter valid school name");
                    return false;
                }
                return true;
            }

            private boolean validatename() {

                String nameS = name.getText().toString();

                if(nameS.equals("")){
                    name.setError("Please Enter valid name");
                    return false;
                }
                return true;

            }

            private boolean validateMobno() {
                String mobNoS = mobNo.getText().toString();
                if(mobNoS.length()!=10){
                    mobNo.setError("Please Enter 10-digit valid mobile no");

                    return false;
                }
                return true;

            }

            private boolean validatePassword() {
                String mPasswordFieldS = mPasswordField.getText().toString();
                if(mPasswordFieldS.length()<6)
                {
                    mPasswordField.setError("Please set valid password");

                    return false;
                }
                return true;
            }

            private boolean validateEmail() {


                String mEmailFieldS = mEmailField.getText().toString();

                if(mEmailFieldS.isEmpty()){
                    mEmailField.setError("Please Enter valid email address");
                    return false;
                }else{
                    if(!Patterns.EMAIL_ADDRESS.matcher(mEmailFieldS).matches()){
                        mEmailField.setError("Please Enter valid email address");

                        return false;
                    }
                }
                return true;
            }
        });

    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            uriProfileImage=data.getData();

            try {
               /* Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);*/

                ImageDecoder.Source source=ImageDecoder.createSource(this.getContentResolver(),uriProfileImage);
                Bitmap bitmap=ImageDecoder.decodeBitmap(source);
                studentImage.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadImageToFirebaseStorage(){
        StorageReference profileImageRef= FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");

        if(uriProfileImage!=null)
        {
            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    profileImageurl= Objects.requireNonNull(taskSnapshot.getUploadSessionUri()).toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showImageChooser(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select profile image"),CHOOSE_IMAGE);
    }

    private void saveUserInformation()
    {
        String displayName = name.getText().toString();

        if(displayName.isEmpty()){
            name.setError("Name Required");
            name.requestFocus();
            return;
        }

        FirebaseUser user=mAuth.getCurrentUser();

        if(user!=null && profileImageurl!=null)
        {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName).setPhotoUri(Uri.parse(profileImageurl)).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegActivity.this,"Profile updated",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

