package com.lyon.programacion_movil_may_ago_2021_67681_Equipo4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    //Cosas del teech
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    //Elementos pantalla 1
    private TextView lecturas;
    private TextView textView;
    private Button btnGrabar;
    private Button btnSig;
    private ProgressBar barraProgreso;
    private Chronometer cr;

    //Elementos pantalla 2
     ListView listaLecturasRealizadas;
     TextView textoLecturaRealizada;
     TextView tiempoLectura;


    //Variables del main
    int pulsado = 0;
    boolean sigBtn = false;
    int i = 0;
    String tiempo;


    RequestQueue requestQueue;

    ArrayList<Lecturas> lecturasRealizadas = new ArrayList<>();
    ArrayList<String> lecturaPrueba = new ArrayList<>();

    List<Lecturas> textos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        obtenerLecturas("https://proyectojuegojasso.000webhostapp.com/NunoProyecto/ObtenerLecturas.php");

        Resources res = getResources();
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec spec1 = tabHost.newTabSpec("");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Lecturas");

        TabHost.TabSpec spec2 = tabHost.newTabSpec("");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Realizadas");

        tabHost.addTab(spec1);
        tabHost.addTab(spec2);

        //Pantalla 1
        lecturas = findViewById(R.id.lecturas);
        lecturas.setMovementMethod(new ScrollingMovementMethod());

        textView = findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        btnGrabar = findViewById(R.id.btnGrabar);
        btnSig = findViewById(R.id.Sig);
        cr = findViewById(R.id.Cronometro);
        barraProgreso = findViewById(R.id.barraProgreso);

        //Pantalla 2
        listaLecturasRealizadas = findViewById(R.id.listaLecturasRealizadas);
        textoLecturaRealizada = findViewById(R.id.lecturaRealizada);
        tiempoLectura = findViewById(R.id.tiempo);


        //Barra de progreso
        //barraProgreso.setMax(this.textos.size());
        barraProgreso.setProgress(0);

        btnGrabar.setEnabled(false);

        //Objetos del recognizer para parsear audio
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        this.speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        this.speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        this.speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        //ListView Lista lecturas
        listaLecturasRealizadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lecturas lec = lecturasRealizadas.get(position);
                String lectura = lec.getLectura();
                String time = lec.getTiempo();
                textoLecturaRealizada.setText(lectura);
                tiempoLectura.setText(time);
            }
        });

    }

    public void Grabar(View view){
        //Estado 0=No ha comenzado, 1:Termino grabacion, 2:Empezo a grabar
        if (pulsado == 0){
            btnGrabar.setEnabled(true);
            pulsado++;
        }else{
            if (pulsado == 1){
                this.speechRecognizer.stopListening();
                btnSig.setEnabled(true);
            }
        }

    }

    public void grabanding(){
        this.speechRecognizer.startListening(this.speechRecognizerIntent);
        if (this.speechRecognizerIntent.resolveActivity(getPackageManager())!= null){

            /*this.cr.setBase(SystemClock.elapsedRealtime());
            this.cr.stop();
            this.cr.start();
            */

            Toast.makeText(getApplicationContext(), "Comenzo a grabar", Toast.LENGTH_SHORT).show();
            btnSig.setEnabled(false);
            this.speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    cr.setBase(SystemClock.elapsedRealtime());
                    cr.stop();
                    cr.start();
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

                }

                @Override
                public void onResults(Bundle results) {
                    cr.stop();
                    ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    textView.setText(data.get(0));
                    tiempo = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(tiempoLectura()));

                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }else{
            Log.e("ERROR", "No se puede encontrar el dispositivo de entrada");
        }
    }

    @SuppressLint("ResourceType")
    public void Sig(View view){

        if (!sigBtn){
            if (!textos.isEmpty()){
                btnSig.setText("Siguiente");
                btnSig.setEnabled(false);

                lecturas.setText(textos.get(this.i).getLectura());
                lecturas.setBackground(null);
                grabanding();

                i++; //i=1
                barraProgreso.setProgress(i);
                btnGrabar.setEnabled(true);

                sigBtn = true;
            }else{
                Toast.makeText(getApplicationContext(), "No hay actividades, be free :)", Toast.LENGTH_SHORT).show();
            }

        }else if (sigBtn){

            if (this.textos.size() == this.i){
                if (!lecturas.getText().toString().isEmpty()){

                    registrarLectura((this.i-1), textView.getText().toString(), this.tiempo);
                    String last = textView.getText().toString();
                    guardarLectura(new Lecturas(last, this.tiempo, textos.get(this.i-1).getIdLec()));

                    textView.setText(null);
                    this.lecturas.setText(null);
                    this.lecturas.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.backgroundfinalizado));
                    btnGrabar.setEnabled(false);
                    btnSig.setEnabled(false);
                }else{
                    Toast.makeText(getApplicationContext(),"Lecturas finalizadas. Be free :)", Toast.LENGTH_SHORT).show();
                }


            }else{
                this.lecturas.setText(textos.get(this.i).getLectura());
                grabanding();
                String anteriorLectura = this.textView.getText().toString();
                registrarLectura((this.i-1), anteriorLectura, this.tiempo);
                guardarLectura(new Lecturas(textView.getText().toString(), this.tiempo, textos.get(this.i-1).getIdLec()));

                textView.setText(null);
                this.i++;
                this.barraProgreso.setProgress(this.i);

            }
        }



    }

    private void registrarLectura(int nLectura ,String lecturaCaptada, String tiempo){
        this.lecturasRealizadas.add(new Lecturas(nLectura, lecturaCaptada, tiempo));
        this.lecturaPrueba.add(lecturaCaptada);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, this.lecturasRealizadas);
        //ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, this.lecturaPrueba);
        listaLecturasRealizadas.setAdapter(adapter);
        
    }

    private int tiempoLectura(){
        return (int) (SystemClock.elapsedRealtime() - this.cr.getBase());
    }

    public void guardarLectura(Lecturas lectura){

        String lec = lectura.getLectura();
        String t = lectura.getTiempo();
        int fklec = lectura.getIdLec();
        int fkuser = 1;

        String urlAgregar = "https://proyectojuegojasso.000webhostapp.com/NunoProyecto/AgregarLecturaLeida.php?lectura="+lec+"&tiempo="+t+"&fkLec="+fklec+"&fkUser="+fkuser;
        RequestQueue servicio= Volley.newRequestQueue(this);
        StringRequest respuesta=new StringRequest(
                Request.Method.GET, urlAgregar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*Toast.makeText(getApplicationContext(),
                        response,Toast.LENGTH_LONG).show();*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Error comunicación",Toast.LENGTH_SHORT).show();
            }
        });
        servicio.add(respuesta);
    }

    public void obtenerLecturas(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                int j;
                for (j = 0; j < response.length(); j++) {
                    try {
                        jsonObject = response.getJSONObject(j);
                        int id = jsonObject.getInt("id_lectura");
                        String lecturajson = jsonObject.getString("1");
                        textos.add(new Lecturas(id, lecturajson));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                barraProgreso.setMax(j);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de Conexión", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
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