package com.vishnu.quizer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class admin extends AppCompatActivity {

    EditText title,content,author;
    Button submit;
    FirebaseAuth mAuth;
    DatabaseReference reference , referencePost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        title = (EditText)findViewById(R.id.editText);
        content = (EditText)findViewById(R.id.editText2);
        author = (EditText)findViewById(R.id.editText3);

        mAuth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference().child("Post").child("Number");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String number = (dataSnapshot.getValue(String.class));
                addContent(number);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void addContent(String number) {

        referencePost = FirebaseDatabase.getInstance().getReference().child("Post").child(number);
        final int newPostNum = Integer.parseInt(number) + 1;

        submit = (Button)findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String titleStr = title.getText().toString().trim();
                String contentStr = content.getText().toString().trim();
                String starStr = "0";
                String authorStr = author.getText().toString().trim();



                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDateTime now = LocalDateTime.now();
                String dateStr = String.valueOf(dtf.format(now));

                postUpload up = new postUpload(titleStr , contentStr , authorStr , dateStr , starStr );
                Task init = referencePost.setValue(up);

                init.addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        reference.setValue(String.valueOf(newPostNum));
                        Toast.makeText(getApplicationContext(),"data upload successfully",Toast.LENGTH_SHORT).show();
                        finish();


                    }
                });
                init.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(admin.this, "Error"+e , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
