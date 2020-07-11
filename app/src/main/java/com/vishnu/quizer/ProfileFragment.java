package com.vishnu.quizer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends AppCompatActivity {
    TextView name , rank , follower , following;
    ImageView profile;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        getSupportActionBar().hide();

        rank = (TextView)findViewById(R.id.rank);
        follower = (TextView)findViewById(R.id.follower);
        following = (TextView)findViewById(R.id.following);
        profile = (ImageView)findViewById(R.id.imageView4);
        name = (TextView)findViewById(R.id.name);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                name.setText(post.fname);
                String imgUrl = post.imgUrl;
                Log.i("Here", imgUrl);
                rank.setText(post.rank);
                follower.setText(post.follower);
                following.setText(post.following);
               /* profile.setImageURI(Uri.parse(imgUrl));
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imgUrl);
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profile.setImageURI(Uri.parse(uri.toString()));
                    }
                });*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent2 = new Intent(ProfileFragment.this , Home.class);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.poll:
                        Intent intent = new Intent(ProfileFragment.this , PollFragment.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });
    }

}
