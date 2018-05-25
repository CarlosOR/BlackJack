package com.example.caor.blackjackl.Modelo;

import com.example.caor.blackjackl.BlackJack;

import java.util.ArrayList;

public class Player {
    private Baraja B;
    private Dealer D = new Dealer();
    private ArrayList<Integer> mazo;
   // private int puntaje;????
    private int dinero;

    public Player(){//El constructor nos devuelve el dinero del player que va a jugar
        this.dinero = 1000;
        this.B = B.getBaraja();
        this.mazo = new ArrayList<>();
        //this.puntaje = 0;
    }
    public String pedir(){
        Baraja carta = B.darCarta();//El metodo dar carta nos devuelve una carta
        mazo.add(D.validarNum(carta.getNum()));
        return carta.getNombre();
    }

    public int comprobar(){
        int puntaje = getPuntaje(0);
        if(puntaje >= 21){//Planta si devuelve 1
            return 1;
        }
        return 0 ; //De lo contrario puede seguir pidiendo
    }

    public int getPuntaje(int n){
        int aux = 0;
        for(int i = n; i < mazo.size(); i++){
            aux += mazo.get(i);
        }
        return aux;
    }

    public int puntajeAS(int num){
        int aux= 0, ptj = 0;
        for(int i = 0; i < mazo.size(); i++){
            if(mazo.get(i) == 1){
                aux++;
            }
        }
        if (aux == 0) {
            return getPuntaje(num);
        }else { //if(aux >=1)
            aux = 0;
            for(int i = 0; i < mazo.size(); i++){
                if(mazo.get(i) == 1 && aux == 0){
                    aux++;
                    ptj+=10;
                }
                ptj +=mazo.get(i);
            }
            if(ptj <= 21)
            return  ptj;
            else
             return getPuntaje(0);
        }
    }
    public int getDinero(){
        return dinero;
    }
    public void setDinero(int mny){
        this.dinero = mny;
    }
    public void nuevoJuego(){
        mazo.clear();
    }
}
