package com.lyon.proyectoprueba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    int pulsado = 1;
    int i = 0;
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    private TextView lecturas;
    private TextView textView;
    private Button btnGrabar;
    private Button btnSig;
    private ProgressBar barraProgreso;
    private Chronometer cr;

    List<String> textos = Arrays.asList(
            "Enserio ? quieres leer manga? no deberías estar estudiando como yo? es mas importante y aporta un futuro para el pais, en cambio vos solo lees manga yo me levanto a las 6, me ducho, empiezo a leer las tendencias en el mercado, invierto en cripto monedas, desayuno a las 8, empiezo con mis clases, a las 12 como, a las 13 estoy haciendo ejercicio, salgo y socializo, a las 16 estudio y me duermo a las 18",
            "SE ESTA MURIENDO LA GENERACIÓN DE HIERRO, PARA DARLE PASO A LA GENERACION DE CRISTAL. La generación que sin estudios educó a sus hijos.La que, a pesar de la falta de todo, nunca permitió que faltara lo indispensable en casa . La que enseñó valores; empezando por Amor y Respeto. Se esta muriendo la gente que enseñaba a los hombres el valor de una mujer y a las mujeres, el respeto por los hombres.Se están muriendo los que podían vivir con pocos lujos, sin sentirse frustrados por ello. Los que trabajaron desde temprana edad y enseñaron el valor de las cosas, no el precio. Mueren los que pasaron por mil dificultades y sin rendirse nos enseñaron cómo vivir con dignidad.Los que después de una vida de sacrificio y penurias, se van con las manos arrugadas y la frente en alto.Se está muriendo la generación que enseñó a vivir sin miedo.",
            "En el sistema de justicia criminal las ofensas de origen sexual se consideran especialmente perversas. En la ciudad de Nueva York los detectives que investigan estos terribles delitos son miembros de un escuadrón de élite conocido como: Unidad de Víctimas Especiales. Con las actuaciones de Cristopher Meloni, Mariska Hargitay, Richard Belzer, Stephanie March, Ice-T, B.D Wong y Dan Florek.La ley y el orden: Unidad de Víctimas Especiales. Hoy presentamos: Tragedia."
    );


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        lecturas = findViewById(R.id.lecturas);
        textView = findViewById(R.id.textView);
        btnGrabar = findViewById(R.id.btnGrabar);
        btnSig = findViewById(R.id.Sig);
        cr = findViewById(R.id.Cronometro);
        barraProgreso = findViewById(R.id.barraProgreso);


        //Texto inicial
        if (this.textos.isEmpty()){
            lecturas.setText("No hay lecturas para hoy. C:");
        }else {
            lecturas.setText(this.textos.get(this.i));
        }

        //Barra de progreso
        barraProgreso.setMax(this.textos.size());
        barraProgreso.setProgress(0);


        //Objetos del recognizer para parsear audio
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        this.speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        this.speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        this.speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //cr.start();
/*
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                textView.setText("Grabado...");
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

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                textView.setText(data.get(i));
                i++;
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });*/


        /*btnGrabar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();

                }
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    speechRecognizer.startListening(speechRecognizerIntent);
                    Toast.makeText(getApplicationContext(), "Empezando grabacion", Toast.LENGTH_SHORT).show();
                    cr.start();
                }
                return false;
            }
        });*/
    }

    public void Grabar(View view){
        if (pulsado == 0){
            this.speechRecognizer.stopListening();
            this.cr.setBase(SystemClock.elapsedRealtime());
            this.cr.stop();
            this.pulsado = 1;
            Toast.makeText(getApplicationContext(), "Se dejo de grabar", Toast.LENGTH_SHORT).show();
        }else if (pulsado == 1){
            this.speechRecognizer.startListening(this.speechRecognizerIntent);
            if (this.speechRecognizerIntent.resolveActivity(getPackageManager())!= null){
                this.cr.setBase(SystemClock.elapsedRealtime());
                this.cr.stop();

                this.cr.start();


                Toast.makeText(getApplicationContext(), "Comenzo a grabar", Toast.LENGTH_SHORT).show();

                this.speechRecognizer.setRecognitionListener(new RecognitionListener() {
                    @Override
                    public void onReadyForSpeech(Bundle params) {

                    }

                    @Override
                    public void onBeginningOfSpeech() {
                        textView.setText("Grabado...");
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

                    }

                    @Override
                    public void onResults(Bundle results) {
                        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        textView.setText(data.get(0));
                    }

                    @Override
                    public void onPartialResults(Bundle partialResults) {

                    }

                    @Override
                    public void onEvent(int eventType, Bundle params) {

                    }
                });
            }else{
                Log.e("ERROR", "NO se puede encontrar el dispositivo de entrada");
            }
            this.pulsado = 0;
        }
        //Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        //speechRecognizer.startListening(speechRecognizerIntent);
    }
    public void Sig(View view){
        
        if (this.textos.size() == this.i){
            this.lecturas.setText("Lecturas terminadas");
        }else{
            this.i++;
            this.barraProgreso.setProgress(this.i);

            String anteriorLectura = this.textView.getText().toString();

            this.lecturas.setText(textos.get(this.i));

            this.textView.setText("Tiempo ultima lectura: "+ tiempoLectura() +"\n" +
                    anteriorLectura);
        }


    }

    private long tiempoLectura(){
        return SystemClock.elapsedRealtime() - this.cr.getBase();

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0){
            if (grantResults[0]== PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, "Permiso consecido", Toast.LENGTH_SHORT).show();
        }
    }







}