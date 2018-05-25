package com.example.caor.blackjackl.Modelo;


import java.util.ArrayList;

public class Baraja {
    private int num = 0;
    private String pinta = "";
    private ArrayList<Baraja> cartas = new ArrayList<>();
    private static Baraja baraja;
    private Baraja(){
        this.num = 0;
        this.pinta = "";
        this.cartas.clear();
    }
    public static Baraja getBaraja(){//Aplicamos el patron singleton
        if(baraja == null){
            baraja = new Baraja();
            baraja.crearCartas();
        }
        return baraja;
    }
    public void crearCartas(){
        String aux = "";
        Baraja crt;
        for(int i = 0; i < 4; i++){
            switch (i){
                case 0:
                    aux = "c";
                    break;
                case 1:
                    aux = "d";
                    break;
                case 2:
                    aux = "p";
                    break;
                case 3:
                    aux = "t";
                    break;
            }

            for(int j = 1; j <= 13; j++){
                crt = new Baraja();
                crt.setNum(j);
                crt.setPinta(aux);
                cartas.add(crt);
            }
        }

    }

    public void barajar(){
        ArrayList<Baraja> baraja = new ArrayList<>();
        int n =0, i = 0 ;
        while(!cartas.isEmpty()){
            n = (int) Math.random() * cartas.size();
            baraja.add(cartas.get(i));
            cartas.remove(i);
        }
        cartas = baraja;
    }

    public Baraja darCarta(){
        int n = (int)(Math.random() * cartas.size());
        Baraja aux =  cartas.get(n);
        cartas.remove(n);
        return aux;
    }
    public void nuevoJuego(){
        cartas.clear();
        crearCartas();
        barajar();
    }


    public void setNum(int num){
        this.num = num;
    }
    public int getNum(){
        return num;
    }
    public void setPinta(String pinta){
        this.pinta = pinta;
    }
    public String getPinta(){
        return pinta;
    }
    public ArrayList<Baraja> getCartas(){
        return cartas;
    }
    public String getNombre(){
        return pinta + "" + num;
    }
}
