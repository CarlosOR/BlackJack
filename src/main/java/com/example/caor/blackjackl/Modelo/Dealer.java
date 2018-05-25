package com.example.caor.blackjackl.Modelo;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.caor.blackjackl.BlackJack;
import com.example.caor.blackjackl.R;

import java.util.ArrayList;

public class Dealer {
    private Baraja B;
    private ArrayList<Integer> mazo = new ArrayList<>();
    private String cartaOculta = "", cartaNOculta = "";;
    private ArrayList<Integer> apuesta = new ArrayList<>();

    public Dealer(){
        B = B.getBaraja();
    }
    public void playerApuesta(int apuesta){
        this.apuesta.add(apuesta);
    }
    public int deleteLastItemApuesta(){
        if(!apuesta.isEmpty()){
            this.apuesta.remove(this.apuesta.size()-1);
            return apuesta.size();
        }else{
            return -1;
        }

    }

    public String pedir(){
        Baraja carta = B.darCarta();//El metodo dar carta nos devuelve una carta
        mazo.add(validarNum(carta.getNum()));
        if(!cartaOculta.equals("") && cartaNOculta.equals("")){
            cartaNOculta = carta.getNombre();
        }
        if(cartaOculta.equals("")){
            cartaOculta = carta.getNombre();
        }
        return carta.getNombre();
    }


    public int getPuntaje(int n){
        int aux = 0;
        for(int i = n; i < mazo.size(); i++){
            aux += mazo.get(i);
        }
        return aux;
    }

    public int validarNum(int valor){
        if(valor >= 11){
            return  10;
        }else{
            return valor;
        }
    }
    public void nuevoJuego(){
        mazo.clear();
        this.cartaOculta = "";
        this.cartaNOculta = "";
        this.apuesta.clear();

    }

    public int puntajeAS(int num){
        int aux= 0, ptj = 0;
        for(int i = num; i < mazo.size(); i++){
            if(mazo.get(i) == 1){
                aux++;
            }
        }
        if (aux == 0) {
            return getPuntaje(num);
        }else { //if(aux >=1)
            aux = 0;
            for(int i = num; i < mazo.size(); i++){
                if(mazo.get(i) == 1 && aux == 0){
                    aux++;
                    ptj+=10;
                }
                ptj +=mazo.get(i);
            }
            if(ptj <= 21)
                return  ptj;
            else
                return getPuntaje(num);
        }
    }
    public String getCartaOculta(){
        return cartaOculta;
    }

    public String getCartaNOculta(){
        return cartaNOculta;
    }
    public int getApuesta(){
        int acum = 0;
        for(int i = 0; i < apuesta.size(); i++){
            acum+= apuesta.get(i);
        }
        return acum;
    }
}
