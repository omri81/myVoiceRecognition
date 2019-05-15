package com.example.myvoicerecognition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "omri";
    private TextToSpeech myTTs;
    private SpeechRecognizer mSpeechRecognizer;
    private Button startRecordBtn;
    private boolean permissionGranted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},1);
        }
        initializeTextToSpeech();
        initialSpeechRecognizer();
        startRecordBtn = findViewById(R.id.start_record_btn);
        startRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permissionGranted) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                    mSpeechRecognizer.startListening(intent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            permissionGranted = true;
        }else{
            permissionGranted = false;
        }
    }

    private void initialSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {
                    Log.d(TAG, "onError: "+error);
                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processResults(results.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResults(String commend) {
        commend = commend.toLowerCase();
        if(commend.indexOf("your name")!= -1){
            Log.d(TAG, "processResults: my name is omri");
        }
    }

    private void initializeTextToSpeech() {
        myTTs = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTs.getEngines().size() == 0){
                    Toast.makeText(MainActivity.this, "no text to speech option", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    myTTs.setLanguage(Locale.US);
                    Log.d(TAG , "ready to use ");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        myTTs.shutdown();
    }
}
