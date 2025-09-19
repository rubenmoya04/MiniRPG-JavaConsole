package PruebasTest.practicar.JuegoRol;

import java.util.ArrayList;
import java.util.Random;

public class Monstruo {
    private String nombre;
    private int vida;
    private int ataque;
    private int defensa;
    private String tipo;  // puedes usar 'tipo' en vez de 'clase'

    public Monstruo(String nombre, int vida, int ataque, int defensa, String tipo) {
        this.nombre = nombre;
        this.vida = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.tipo = tipo;
    }

    public Monstruo() {
        this.nombre = null;
        this.vida = 0;
        this.ataque = 0;
        this.defensa = 0;
        this.tipo = null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void recibirDaño(int daño) {
        int vidaActual = getVida();
        int nuevaVida = vidaActual - daño;
        if (nuevaVida < 0) {
            nuevaVida = 0;
        }
        setVida(nuevaVida);
    }

    public boolean estaVivo() {
        return getVida() > 0;
    }

    public int atacar() {
        return getAtaque();
    }

    public void mostrarMonstruo() {
        System.out.println("==========================================");
        System.out.println("              " + getNombre() + " (" + getTipo() + ")              ");
        System.out.println("· Vida: " + getVida() + "\n· Ataque: " + getAtaque() + "\n· Defensa: " + getDefensa());
        System.out.println("==========================================");
    }


    public int roboDeVida(Monstruo m, Jugador j){
        Random rd = new Random();
        if (this.tipo.equals("BOSS")){
            if(rd.nextInt(100) < 10){
                m.atacar();
            }
        }
        return 34;
    }


}
