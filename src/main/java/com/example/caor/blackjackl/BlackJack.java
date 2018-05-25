package com.example.caor.blackjackl;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caor.blackjackl.Modelo.*;

import org.w3c.dom.Text;


public class BlackJack extends AppCompatActivity {
    private MediaPlayer mP;
    private Dealer D = new Dealer();
    private Player P = new Player();
    private Baraja B;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_jack);
        actualizarDinero();
        View pantalla = getWindow().getDecorView();
        fullScreen(pantalla);
        reActiveFullScreen(pantalla);
        Button btnPedir = (Button) findViewById(R.id.btnPedir);
        btnPedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAVista(P.pedir(),0);
                comprobar();
                actualizarPuntaje(P.puntajeAS(0), 0);
                mP = MediaPlayer.create(getApplicationContext(), R.raw.carta);
                mP.start();
            }
        });
        Button btnPlantar = (Button) findViewById(R.id.btnPlantar);
        btnPlantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plantar();
            }
        });
    }

    public void apuestaJuego(View view) {
        int apuesta = 0;
        int dinero = P.getDinero();
        mP = MediaPlayer.create(this, R.raw.perder);
        mP.start();
        Button button = (Button) findViewById(view.getId());
        String x = button.getText().toString();
        int valor = Integer.parseInt(x);
        switch (valor) {
            case 100:
                apuesta = 100;
                break;
            case 200:
                apuesta = 200;
                break;
            case 500:
                apuesta = 500;
                break;
            case 1000:
                apuesta = 1000;
                break;
            default:
                apuesta = 10;
                break;
        }
        if ((dinero - (D.getApuesta()+apuesta)) >= 0) {
        D.playerApuesta(apuesta);
        TextView textView = (TextView) findViewById(R.id.txtApuesta);
        textView.setText("Apuesta : " + D.getApuesta());
        textView.setBackgroundColor(Color.rgb(255, 51, 51));
        Button repartir = (Button) findViewById(R.id.btnRepartir);
        repartir.setEnabled(true);
        }else{
            Toast.makeText(this, "No tienes suficiente dinero", Toast.LENGTH_SHORT).show();
        }

    }

    public void repartir(View view){
        prepararJuegoNuevo();
    }

    public void prepararJuegoNuevo(){
        Button pedir = (Button) findViewById(R.id.btnPedir);
        Button plantar = (Button) findViewById(R.id.btnPlantar);
        Button cien = (Button) findViewById(R.id.btnCien);
        Button dosc = (Button) findViewById(R.id.btnDosc);
        Button quini = (Button) findViewById(R.id.btnQuini);
        Button mil = (Button) findViewById(R.id.btnMil);
        Button repartir = (Button) findViewById(R.id.btnRepartir);
        ImageButton quitar = (ImageButton) findViewById(R.id.btnQuitar);
        pedir.setEnabled(true);
        plantar.setEnabled(true);
        repartir.setEnabled(false);
        cien.setEnabled(false);
        dosc.setEnabled(false);
        quini.setEnabled(false);
        mil.setEnabled(false);
        quitar.setEnabled(false);
        iniciarJuego();
    }

    public void iniciarJuego(){
    LinearLayout player = (LinearLayout) findViewById(R.id.playerLayout);
    LinearLayout dealer = (LinearLayout) findViewById(R.id.dealerLayout);
    //Creamos cartas para el Jugador:---------------------------------
        addAVista(P.pedir(), 0);
        addAVista(P.pedir(), 0);
        actualizarPuntaje(P.puntajeAS(0), 0);
        comprobar();
     //Creamos cartas para el Dealer:---------------------------------
        D.pedir();
        addAVista("ba", 1);//Le asignamos el nombre de la carta boca abajo
        addAVista(D.pedir(), 1);
        actualizarPuntaje(D.puntajeAS(1), 1);
    }

    public void comprobar(){
        Button btndoblar = (Button) findViewById(R.id.btnDoblar);
        int n = P.getPuntaje(0);
        if(n >= 21){
            plantar();
        }
        if(n >= 12){
            btndoblar.setEnabled(false);
        }else{
            btndoblar.setEnabled(true);
        }
    }

    public void doblar(View view){
        if(P.getDinero() >= (D.getApuesta()*2)){
            mP = MediaPlayer.create(getApplicationContext(), R.raw.carta);
            mP.start();
            D.playerApuesta(D.getApuesta());
            TextView textView = (TextView) findViewById(R.id.txtApuesta);
            textView.setText("Apuesta : " + D.getApuesta());
            addAVista(P.pedir(),0);
            actualizarPuntaje(P.puntajeAS(0), 0);
            plantar();
            Button btndoblar = (Button) findViewById(R.id.btnDoblar);
            btndoblar.setEnabled(false);
        }else{
            Toast.makeText(this, "No tienes suficiente dinero para doblar", Toast.LENGTH_SHORT).show();
        }


    }

    public void plantar(){
        Button pedir = (Button) findViewById(R.id.btnPedir);
        Button planta = (Button) findViewById(R.id.btnPlantar);
        Button btndoblar = (Button) findViewById(R.id.btnDoblar);
        btndoblar.setEnabled(false);
        pedir.setEnabled(false);
        planta.setEnabled(false);
        jugarDealer();
    }

    public void jugarDealer(){
        mostrarJuegoDealer();
        int pD = D.puntajeAS(0);
        while(pD < 17){
            addAVista(D.pedir(), 1);
            pD = D.puntajeAS(0);
        }
        actualizarPuntaje(D.puntajeAS(1), 1);
        quienGana();
    }

    public void quienGana() {
        int p = P.puntajeAS(0);
        int d = D.puntajeAS(0);
        int op = P.puntajeAS(1);//Suma sin la carta oculta
        int gna = 0;
        int apuesta = D.getApuesta();
        TextView player = (TextView) findViewById(R.id.txtPlayer);
        TextView dealer = (TextView) findViewById(R.id.txtDealer);
        String aux = "";
        if (p > 21) {
            p = -1;
        }
        if (d > 21) {
            d = -1;
        }
        if (d == -1) {
            aux = "El Dealer se paso tu Ganas :" + apuesta;
            mP = MediaPlayer.create(this, R.raw.dinero);
            gna++;
        } else if (p > d && p <= 21) {
            aux = "Ganas :" + apuesta;
            mP = MediaPlayer.create(this, R.raw.dinero);
            gna++;
        } else if (d > p && d <= 21) {
            aux = "Gana la Casa, Tú pierdes " + apuesta;
            mP = MediaPlayer.create(this, R.raw.perder);
            gna--;
        } else if (d == p && d != -1 && p != -1) {
            aux = "Empataron: Ninguno pierde";
            mP = MediaPlayer.create(this, R.raw.dinero);
        } else if (d > 21 && op < 17) {
            aux = "Ganas :" + apuesta;
            mP = MediaPlayer.create(this, R.raw.dinero);
            gna++;
        } else {
            aux = "Ninguno gana";
            //mP = MediaPlayer.create(this, R.raw.muac);
        }
        if (gna >= 1) {
            player.setBackgroundColor(Color.rgb(0,255, 9));
            P.setDinero(P.getDinero() + apuesta);
        } else if (gna == 0){

    }else{
            dealer.setBackgroundColor(Color.rgb(0,255, 9));
            if((P.getDinero()-apuesta) <= 0){
                //Toast.makeText(this, "Has perdido todo tu dinero, la casa te presta 1000", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Has Perdido").setTitle("Game Over").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialogInterface, int i) {}}).show();
                P.setDinero(1000);
            }else {
                P.setDinero(P   .getDinero() - apuesta);
            }
        }
        actualizarDinero();
        Button nuevo = (Button) findViewById(R.id.btnNuevaMano);
        actualizarPuntaje(D.puntajeAS(0), 1);
        nuevo.setEnabled(true);
        Toast.makeText(this, aux, Toast.LENGTH_SHORT).show();
        mP.start();
    }

    public void nuevaMano(View view){
        mP = MediaPlayer.create(getApplicationContext(), R.raw.cartas);
        mP.start();
        LinearLayout diler = (LinearLayout) findViewById(R.id.dealerLayout);
        diler.removeAllViews();
        LinearLayout player = (LinearLayout) findViewById(R.id.playerLayout);
        player.removeAllViews();
        Button pedir = (Button) findViewById(R.id.btnPedir);
        pedir.setEnabled(false);
        Button plantar = (Button) findViewById(R.id.btnPlantar);
        plantar.setEnabled(false);
        dineroYApuesta();//Este metodo hablitia los botones de apuesta segun la cantidad de dinero del player
        Button btndoblar = (Button) findViewById(R.id.btnDoblar);
        btndoblar.setEnabled(false);
        Button repartir = (Button) findViewById(R.id.btnRepartir);
        repartir.setEnabled(false);
        ImageButton quitar = (ImageButton) findViewById(R.id.btnQuitar);
        quitar.setEnabled(true);
        TextView txtApuesta = (TextView) findViewById(R.id.txtApuesta);
        txtApuesta.setText("Apuesta:");
        txtApuesta.setBackgroundColor(Color.rgb(255,51,51));
        TextView txtdiler = (TextView) findViewById(R.id.txtDealer);
        txtdiler.setText("Dealer Game");
        txtdiler.setBackgroundColor(Color.rgb(19, 108, 22));
        TextView txtpleyer = (TextView) findViewById(R.id.txtPlayer);
        txtpleyer.setText("Player Game");
        txtpleyer.setBackgroundColor(Color.rgb(19, 108, 22));
        Button newM = (Button) findViewById(view.getId());
        newM.setEnabled(false);
        B = B.getBaraja();
        B.nuevoJuego();
        P.nuevoJuego();
        D.nuevoJuego();
    }

    public void quitar(View view){
        int aux = D.deleteLastItemApuesta();
        if(aux != -1){
            mP = MediaPlayer.create(getApplicationContext(), R.raw.perder);
            mP.start();
            TextView apuesta = (TextView) findViewById(R.id.txtApuesta);
            apuesta.setText("Apuesta: " + D.getApuesta());
        }else{
            Toast.makeText(this,"No ha apostada nada", Toast.LENGTH_SHORT).show();
        }
        if((aux - 1) == -1){
            Button repartir = (Button) findViewById(R.id.btnRepartir);
            repartir.setEnabled(false);
        }
    }

    public void addAVista(String name, int pod){//POD = (Player o Dealer)(0 o 1)
        int id = 0;
        id = getResources().getIdentifier(name, "drawable", getPackageName());
        ImageView imagenc = new ImageView(this);
        imagenc.setImageDrawable(getResources().getDrawable(id));
        imagenc.setLayoutParams(new LinearLayout.LayoutParams(120, 160));
        if(pod == 0){
            LinearLayout playerLayout = (LinearLayout) findViewById(R.id.playerLayout);
            playerLayout.addView(imagenc);
        }else{
            LinearLayout dealerLayout = (LinearLayout) findViewById(R.id.dealerLayout);
            dealerLayout.addView(imagenc);
        }
    }

    public void actualizarPuntaje(int puntaje, int pod){
        TextView textView;
        String aux = "";
        if(pod == 0){
            textView = (TextView) findViewById(R.id.txtPlayer);
            aux = "Player Game: ";
        }else{
            textView = (TextView) findViewById(R.id.txtDealer);
            aux = "Dealer Game: ";
        }
        textView.setText(aux + "" + puntaje);

    }

    public void mostrarJuegoDealer(){
        LinearLayout dealer = (LinearLayout) findViewById(R.id.dealerLayout);
        dealer.removeAllViews();
        addAVista(D.getCartaOculta(), 1);
        addAVista(D.getCartaNOculta(), 1);
    }

    public void actualizarDinero(){
        TextView textView = (TextView) findViewById(R.id.txtDinero);
        textView.setText("Dinero : " + P.getDinero());
    }

    public void dineroYApuesta(){
        int dinero  = P.getDinero();
        Button cien = (Button) findViewById(R.id.btnCien);

        Button dos = (Button) findViewById(R.id.btnDosc);

        Button quini = (Button) findViewById(R.id.btnQuini);

        Button mil = (Button) findViewById(R.id.btnMil);

        if(dinero >= 1000){
            mil.setEnabled(true);
        }
        if(dinero >= 500){
            quini.setEnabled(true);
        }
        if(dinero >= 200){
            dos.setEnabled(true);
        }
        if(dinero >= 100){
            cien.setEnabled(true);
        }
    }

    private void reActiveFullScreen(final View vw) {
        vw.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                fullScreen(vw);
            }
        });
    }

    private void fullScreen(View vw) {
        vw.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
