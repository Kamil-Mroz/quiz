package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "kamil";
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "com.example.quiz.correctAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button resetButton;
    private TextView questionTextView;
    private TextView resultTextView;
    private Button hintButton;

    private boolean answerWasShown = false;


    private Question[] questions = new Question[] {
            new Question(R.string.q_activity, true),
            new Question(R.string.q_find_resources, false),
            new Question(R.string.q_listener, true),
            new Question(R.string.q_resources, true),
            new Question(R.string.q_version, false),
    };
    private int currentIndex = 0;
    private int correctAnswers = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Log.d(TAG,"On Create");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        resetButton = findViewById(R.id.reset_button);
        questionTextView = findViewById(R.id.question_text_view);
        resultTextView = findViewById(R.id.result_text_view);

        resultTextView.setVisibility(View.GONE);
        resetButton.setVisibility(View.GONE);
        hintButton = findViewById(R.id.hint_button);



        hintButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, PromptActivity.class);
                boolean correctAnswer = questions[currentIndex].isTrueAnswer();
                intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
                startActivityForResult(intent, REQUEST_CODE_PROMPT);
            }
        });

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkAnswerCorrectness(true);
            }
        });
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkAnswerCorrectness(false);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerWasShown = false;
                moveToNextQuestion();

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetQuiz();
            }
        });

        setNextQuestion();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){return;}
        if(requestCode == REQUEST_CODE_PROMPT){
            if(data == null){return;}
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG,"On Start");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG,"On Resume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG,"On Pause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG,"On Stop");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"On Destroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.d(TAG,"Wywołana została metoda: onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }


    private void setNextQuestion(){
        if(currentIndex < questions.length) {
            questionTextView.setText(questions[currentIndex].getQuestionId());
        }
    }

    private void checkAnswerCorrectness(boolean userAnswer){
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if(answerWasShown){

            resultMessageId = R.string.answer_was_shown;
        }
        else{

            if(userAnswer == correctAnswer){
                resultMessageId = R.string.correct_answer;
                correctAnswers += 1;

            }else{
                resultMessageId = R.string.incorrect_answer;

            }
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
        moveToNextQuestion();

    }

    private void moveToNextQuestion(){
        currentIndex = (currentIndex + 1);
        if(currentIndex < questions.length){
            setNextQuestion();
        }else{
            showFinalScore();
        }
    }

    private void showFinalScore(){
    trueButton.setVisibility(View.GONE);
    falseButton.setVisibility(View.GONE);
    nextButton.setVisibility(View.GONE);
    questionTextView.setVisibility(View.GONE);
    resultTextView.setText("Correct answers: "+correctAnswers);
    resultTextView.setVisibility(View.VISIBLE);
    resetButton.setVisibility(View.VISIBLE);

    }

    private void resetQuiz(){
        correctAnswers = 0;
        currentIndex = 0;

        trueButton.setVisibility(View.VISIBLE);
        falseButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        questionTextView.setVisibility(View.VISIBLE);

        resultTextView.setVisibility(View.GONE);
        resetButton.setVisibility(View.GONE);
        setNextQuestion();
    }


}

