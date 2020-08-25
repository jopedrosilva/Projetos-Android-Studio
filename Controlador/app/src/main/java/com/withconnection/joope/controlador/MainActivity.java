package com.withconnection.joope.controlador;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.withconnection.joope.controlador.Model.dicionario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView statusLampada;
    private ImageView myImage = null;
    private String ipServidor = "192.168.1.102";
    private String ipArduino = "192.168.1.177";
    TextToSpeech textToSpeech;

    ImageView gravar;
    TextView textoGravado;

    private final int ID_TEXTO_PARA_VOZ = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gravar = (ImageView)findViewById(R.id.gravar);
        //textoGravado = (TextView)findViewById(R.id.textoGravado);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });

        gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iVoz = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                iVoz.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                iVoz.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                iVoz.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Ouvindo...");

                try{
                    startActivityForResult(iVoz, ID_TEXTO_PARA_VOZ);
                }catch (ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(), "Seu celular não suporta comando de voz...",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        myImage = (ImageView)findViewById(R.id.lampadaImagem);
        Bitmap bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.lampdesligada_launcher);
        myImage.setImageBitmap(bImage);

        final Switch simpleSwitch = (Switch) findViewById (R.id.switch1);
        simpleSwitch.setChecked(false);
        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(simpleSwitch.isChecked()){
                    action("http://"+ipArduino+"/?led_ligar");
                    imagemLigada();
                    //textToSpeech.speak("Meu mestre, você é lindo!", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    action("http://"+ipArduino+"/?led_desligar");
                    imagemDesligada();
                    //textToSpeech.speak("Oi Lindão!", TextToSpeech.QUEUE_FLUSH, null);
                }


            }
        });

        actionRequisicao("http://"+ipServidor+"/GerenciadorArduino/facil.php");

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });
    }

    public void action(String url){

        statusLampada = (TextView)findViewById(R.id.statusLampada);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        });
    }

    public void actionRequisicao(String url){
        statusLampada = (TextView)findViewById(R.id.statusLampada);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        statusLampada.setText(myResponse.toString());
                                        if(myResponse.toString().equalsIgnoreCase("Ligada")) {
                                            imagemLigada();
                                        } else {
                                            if(myResponse.toString().equalsIgnoreCase("Desligada")) {
                                                imagemDesligada();
                                            }
                                        }
                                    }
                                }, 200);
                                actionRequisicao("http://"+ipServidor+"/GerenciadorArduino/facil.php");

                            } catch (Exception e){
                                e.printStackTrace();
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() { }
                                }, 200);
                                actionRequisicao("http://"+ipServidor+"/GerenciadorArduino/facil.php");
                            }
                        }
                    });
                }
            }
        });
    }

    public void imagemLigada(){
        myImage = null;
        myImage = (ImageView)findViewById(R.id.lampadaImagem);
        Bitmap bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.lampligada_launcher);
        myImage.setImageBitmap(bImage);
        //textToSpeech.speak("Lâmpada Ligada, João!", TextToSpeech.QUEUE_FLUSH, null);
        falarMensagem("Lâmpada Ligada, Senhor!");
    }

    public void imagemDesligada(){
        myImage = null;
        myImage = (ImageView)findViewById(R.id.lampadaImagem);
        Bitmap bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.lampdesligada_launcher);
        myImage.setImageBitmap(bImage);
        //textToSpeech.speak("Lâmpada Desligada, João!", TextToSpeech.QUEUE_FLUSH, null);
        falarMensagem("Lâmpada Desligada, Senhor!");
    }

    @Override
    protected void onActivityResult(int id, int resultCodeID, Intent dados){
        super.onActivityResult(id, resultCodeID, dados);

        dicionario dic = new dicionario();

        switch (id) {
            case ID_TEXTO_PARA_VOZ:
                if (resultCodeID == RESULT_OK && null != dados) {
                    ArrayList<String> result = dados
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String ditado = result.get(0);

                    for (int j = 0; j < dic.getObjeto().size(); j++){
                        for (int k = 0; k < dic.getComandoLigar().size(); k++){
                            if (ditado.toLowerCase().contains(dic.getObjeto().get(j).toLowerCase())&&
                                    ditado.toLowerCase().contains(dic.getComandoLigar().get(k).toLowerCase())) {
                                    imagemLigada();
                                    break;
                            }
                        }
                        for (int i = 0; i < dic.getComandoDesligar().size(); i++){
                            if (ditado.toLowerCase().contains(dic.getObjeto().get(j).toLowerCase())&&
                                    ditado.toLowerCase().contains(dic.getComandoDesligar().get(i).toLowerCase())) {
                                    imagemDesligada();
                                    break;
                            }
                        }
                    }
                }
                break;
        }
    }

    public void falarMensagem(String texto) {
        textToSpeech.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
    };

    public void onPause (){
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

}
