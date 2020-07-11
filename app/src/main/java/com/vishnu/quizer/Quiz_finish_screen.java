package com.vishnu.quizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Quiz_finish_screen extends AppCompatActivity {


    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    String uid;
    String[] scores;

    LinearLayout linearLayout;
    TextView total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_finish_screen);

        String score = getIntent().getStringExtra("score");
        String scoreInd = getIntent().getStringExtra("allScore");

        linearLayout = findViewById(R.id.linearLayout);
        //Log.i("Her",score);

        scores = scoreInd.split("/");

        for (int i = 0 ; i< scores.length ; i++){
            View ind = getLayoutInflater().inflate(R.layout.score_item,null,false);
            TextView qnoText = (TextView) ind.findViewById(R.id.textView15);
            TextView martText = (TextView) ind.findViewById(R.id.textView16);

            qnoText.setText(String.valueOf(i+1));
            if(Integer.parseInt(scores[i])==1){
                martText.setText("Corret");
                martText.setTextColor(Color.GREEN);
            }else {
                martText.setText("Wrong");
                martText.setTextColor(Color.RED);
            }


            linearLayout.addView(ind);

        }
        total = (TextView)findViewById(R.id.textView17);
        total.setText("Total Score : " + score);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        //reference = FirebaseDatabase.getInstance().getReference().child("Quiz").child(today);


    }
}
