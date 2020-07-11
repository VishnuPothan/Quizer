package com.vishnu.quizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Home extends AppCompatActivity {
    FirebaseAuth mAuth;
    View cardPost;
    LinearLayout linearLayout;
    TextView titleText , contentText;
    DatabaseReference reference , referencePost , referencePostEach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_poll);
        getSupportActionBar().hide();

        linearLayout = findViewById(R.id.linearLayoutCard);
        cardPost = getLayoutInflater().inflate(R.layout.post_view,null, false);
        titleText = (TextView) cardPost.findViewById(R.id.title);
        contentText = (TextView) cardPost.findViewById(R.id.content);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("Post").child("Number");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String number = (dataSnapshot.getValue(String.class));
                takeContent(number);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.poll:
                        Intent intent = new Intent(Home.this , PollFragment.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        Intent intent2 = new Intent(Home.this , ProfileFragment.class);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }



    private void takeContent(String number) {
        int num = Integer.parseInt(number);

        referencePost = FirebaseDatabase.getInstance().getReference().child("Post");



        for( int i=0 ; i<num ; i++ ){
            referencePostEach = referencePost.child(String.valueOf(i));
            referencePostEach.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //postToCard ptc = dataSnapshot.getValue(postToCard.class);

                    String titles = dataSnapshot.child("title").getValue(String.class);
                    String contents = dataSnapshot.child("title").getValue(String.class);
                    titleText.setText(titles);
                    contentText.setText(contents);

                    if(cardPost.getParent() != null) {
                        ((ViewGroup)cardPost.getParent()).removeView(cardPost);
                    }
                    linearLayout.addView(cardPost);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() ==null ){
            finish();
            startActivity(new Intent(Home.this , MainActivity.class));
        }
    }


}
