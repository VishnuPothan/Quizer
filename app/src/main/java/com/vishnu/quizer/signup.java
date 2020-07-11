package com.vishnu.quizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class signup extends AppCompatActivity implements View.OnClickListener {

    ImageView profilePic;
    public Uri imageUri;
    EditText uname;
    EditText phone;
    FirebaseFirestore fstore;
    EditText fname;
    ProgressBar progressBar;
    EditText Email, Password;
    private FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String ProUrl;
    Boolean imgUp = false;
    Boolean emaVer = false;

    private static final int CHOOSE_IMAGE =101 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        profilePic = findViewById(R.id.imageView3);

        fname = (EditText) findViewById(R.id.fname);
        uname = findViewById(R.id.name);
        Email = (EditText) findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        Password = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        fAuth = FirebaseAuth.getInstance();


        findViewById(R.id.login).setOnClickListener(this);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePic();
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });

    }



    private void choosePic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Select profile image"),CHOOSE_IMAGE);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                profilePic.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference proIm = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(imageUri != null ){
            progressBar.setVisibility(View.VISIBLE);
            proIm.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    imgUp = true;
                    ProUrl = taskSnapshot.getStorage().getDownloadUrl().toString();


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(signup.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;

        }
    }





    private void registerUser() {
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if (email.isEmpty()){
            Email.setError("Email Required");
            Email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Password.setError("Enter valid email address.");
            Password.requestFocus();
            return;
        }

        if(password.length()<6){
            Password.setError("Minimum length of password should be 6 char");
            Password.requestFocus();
            return;
        }

        if (password.isEmpty()){
            Password.setError("Password required");
            Password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    emaVer = true;
                    databaseUpload();

                }
                else{
                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"User already registered, Try login.",Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }





    private void databaseUpload() {

        String fnameS = fname.getText().toString().trim();
        String unameS = uname.getText().toString().trim();
        String phoneP = phone.getText().toString().trim();
        String imgurl = ProUrl;


        if (fnameS.isEmpty()){
            fname.setError("Name Required");
            fname.requestFocus();
            return;
        }
        if (unameS.isEmpty()){
            uname.setError("User name Required");
            uname.requestFocus();
            return;
        }
        if (phoneP.isEmpty()){
            phone.setError("Phone number Required");
            phone.requestFocus();
            return;
        }
        if (imgUp != true ){
            Toast.makeText(this, "Upload a profile picture" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (emaVer != true ){
            Toast.makeText(this, "Email and password authentication failed" , Toast.LENGTH_SHORT).show();
            return;
        }


        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        String rank = "0";
        String follower = "0";
        String following = "0";
        String post = uid;
        String quiz = "0";
        String poll = "0";

        UserHelperClass helperclass = new UserHelperClass(fnameS , unameS , phoneP , imgurl , rank , follower , following , post , quiz , poll);

        Task initTask = reference.child(uid).setValue(helperclass);

        initTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                finish();
                Toast.makeText(getApplicationContext(),"User registered successfully",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(signup.this , Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        initTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup.this, "Error"+e , Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show();

    }


}
