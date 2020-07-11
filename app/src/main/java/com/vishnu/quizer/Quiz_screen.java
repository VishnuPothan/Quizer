package com.vishnu.quizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;


public class Quiz_screen extends AppCompatActivity {

    private static final long COUNTDOWN_IN_MILLIS = 10000;


    RadioButton rb1 , rb2 , rb3 , rb4;
    RadioGroup radioGroup;
    private TextView temp , timer , question , next , questionNumber , confirm;
    DatabaseReference reference, refQes , refScore;

    String questionComplete , optionComplete , answerComplete , currentQuestion ;
    String[] questionList , optionList  , answerList , allScore;
    String uid , date , scoreInd="";

    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;



    private ColorStateList textColorDefaultDb;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ColorStateList textColorDefaultRb;
    private int questionCounter = 0;
    private int questionCountTotal , solution;
    private long score;
    private boolean answred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_screen);
/*initialize all view*/
        temp = (TextView) findViewById(R.id.temp);
        timer = (TextView) findViewById(R.id.timer);
        question = (TextView) findViewById(R.id.question);
        rb1 = (RadioButton) findViewById(R.id.radioButton1);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        rb4 = (RadioButton) findViewById(R.id.radioButton4);

        mAuth = FirebaseAuth.getInstance();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        questionNumber = (TextView) findViewById(R.id.qno);
        confirm = (TextView) findViewById(R.id.next);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultDb = timer.getTextColors();




        /*firebase today getter*/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("Quiz").child("today");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String today = (dataSnapshot.getValue(String.class));
                dataOut(today);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void dataOut(String toda) {

        date = toda;

        refQes = FirebaseDatabase.getInstance().getReference().child("Quiz").child(toda);

        refQes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                questionComplete = dataSnapshot.child("question").getValue(String.class);
                optionComplete = dataSnapshot.child("option").getValue(String.class);
                answerComplete = dataSnapshot.child("answer").getValue(String.class);

                questionSetter(questionComplete, optionComplete, answerComplete);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!answred){
                            if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()  ){
                                checkAnswer();
                            }else{
                                Toast.makeText(Quiz_screen.this, "select one", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            questionSetter(questionComplete, optionComplete, answerComplete);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void questionSetter(String questionComplete, String optionComplete, String answerComplete) {
        String[] optionLists;
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        Log.i("Here" , optionComplete);
        questionList = questionComplete.split(",");
        optionList = optionComplete.split(",");
        answerList = answerComplete.split(",");

        questionCountTotal = questionList.length;

        if(questionCounter < questionCountTotal){
            currentQuestion = questionList[questionCounter];

            Log.i("Here" , optionList[questionCounter]);
            optionLists = optionList[questionCounter].split("/");

            Log.i("Here" , currentQuestion);
            question.setText(currentQuestion);

            Log.i("Here" , optionLists[0]);
            rb1.setText(optionLists[0]);
            rb2.setText(optionLists[1]);
            rb3.setText(optionLists[2]);
            rb4.setText(optionLists[3]);

            questionCounter++;


            questionNumber.setText(String.valueOf(questionCounter));

            answred = false;

            confirm.setText("confirm");
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        }else{
            finishQuiz();
        }


    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                    timeLeftInMillis = 0;
                    updateCountDownText();
                    checkAnswer();

            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int)(timeLeftInMillis / 1000) / 60;
        int seconds = (int)(timeLeftInMillis/1000)%60;

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);

        timer.setText(timeFormatted);

        if(timeLeftInMillis<5000){
            timer.setTextColor(Color.RED);
        }else{
            timer.setTextColor(textColorDefaultDb);
        }
    }


    private void checkAnswer() {

        answred = true;

        countDownTimer.cancel();

        long scoreTime = timeLeftInMillis;

        Log.i("Here",String.valueOf(timeLeftInMillis));
        Log.i("Here", String.valueOf(questionCounter));
        RadioButton rbSelected = findViewById(radioGroup.getCheckedRadioButtonId());
        int answerNum = radioGroup.indexOfChild(rbSelected)+1;

        int answerReal = Integer.parseInt(answerList[questionCounter-1]);

        if (answerNum == answerReal){
            score = score+ timeLeftInMillis;
            Log.i("Here" , String.valueOf(questionCounter));
            temp.setText("Score"+ score);
            scoreInd = scoreInd.concat("1/");
        }else{
            scoreInd = scoreInd.concat("0/");
        }

        /*ScoreUpload scu = new ScoreUpload(String.valueOf(questionCounter) , String.valueOf(timeLeftInMillis));
        refScore.setValue(scu);*/
        if(questionCounter == questionCountTotal){
            refScore = FirebaseDatabase.getInstance().getReference().child("Quiz").child(date).child("score").child(uid);
            ScoreUpload scuFinal = new ScoreUpload(scoreInd , String.valueOf(score));
            refScore.setValue(scuFinal);
        }
        showSolution(answerReal);
    }

    private void showSolution(int answerReal) {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        switch (answerReal){
            case 1:
                rb1.setTextColor(Color.GREEN);
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                break;
        }

        if (questionCounter < questionCountTotal) {
            confirm.setText("Next");
        }
        else{
            confirm.setText("Finsh");
        }
    }


    private void finishQuiz() {

        Intent intent = new Intent(Quiz_screen.this, Quiz_finish_screen.class);
        Log.i("Here", date);
        intent.putExtra("allScore",scoreInd);
        intent.putExtra("score",score);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }
}




