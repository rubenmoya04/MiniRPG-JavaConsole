package PruebasTest.practicar.JuegoRol;

import java.util.ArrayList;

// Inventario del jugador. Guarda los objetos que va pillando + el oro.
public class Inventario {
    private ArrayList<Objeto> objetos;
    private int capacidadMax; // cuantas cosas puede llevar como mucho
    private int oro;

    // Empieza con 50 de oro pa pillar alguna pocion al principio
    public Inventario(int capacidadMax){
        this.objetos = new ArrayList<>();
        this.capacidadMax = capacidadMax;
        this.oro = 50;
    }

    // Getters y setters
    public ArrayList<Objeto> getObjetos(){ return objetos; }
    public int getCapacidadMax(){ return capacidadMax; }
    public int getOro(){ return oro; }

    // Suma oro al inventario, si por lo que sea queda negativo lo dejo en 0
    public void agregarOro(int cantidad){
        this.oro += cantidad;
        if(this.oro < 0) this.oro = 0;
    }

    // Intenta gastar oro, si no le llega devuelve false y no hace nada
    public boolean gastarOro(int cantidad){
        if(cantidad <= this.oro){
            this.oro -= cantidad;
            return true;
        }
        return false;
    }

    // Mete un objeto al inventario, si esta lleno lo avisa
    public boolean agregarObjeto(Objeto o){
        if(objetos.size() >= capacidadMax){
            System.out.println(ColorConsola.amarillo("⚠️  Inventario lleno. No puedes llevar más de " + capacidadMax + " objetos."));
            return false;
        }
        objetos.add(o);
        return true;
    }

    // Saca un objeto del inventario por su indice
    public Objeto sacar(int indice){
        if(indice < 0 || indice >= objetos.size()) return null;
        return objetos.remove(indice);
    }

    // Pinta el inventario por pantalla
    public void mostrar(){
        System.out.println(ColorConsola.cyan("\n╔══════════════ 🎒 INVENTARIO ══════════════╗"));
        System.out.println("💰 Oro: " + ColorConsola.amarillo(String.valueOf(oro)));
        System.out.println("📦 Capacidad: " + objetos.size() + "/" + capacidadMax);
        if(objetos.isEmpty()){
            System.out.println(ColorConsola.cyan("   ") + "(vacío)");
        } else {
            for(int i = 0; i < objetos.size(); i++){
                System.out.println("  " + (i+1) + ". " + objetos.get(i).mostrarObjeto());
            }
        }
        System.out.println(ColorConsola.cyan("╚════════════════════════════════════════════╝"));
    }
}
