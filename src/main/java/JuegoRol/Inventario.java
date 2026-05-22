package PruebasTest.practicar.JuegoRol;

import java.util.ArrayList;

public class Inventario {
    private ArrayList<Objeto> objetos;
    private int capacidadMax;
    private int oro;

    public Inventario(int capacidadMax){
        this.objetos = new ArrayList<>();
        this.capacidadMax = capacidadMax;
        this.oro = 50;
    }

    public ArrayList<Objeto> getObjetos(){ return objetos; }
    public int getCapacidadMax(){ return capacidadMax; }
    public int getOro(){ return oro; }
    public void agregarOro(int cantidad){
        this.oro += cantidad;
        if(this.oro < 0) this.oro = 0;
    }
    public boolean gastarOro(int cantidad){
        if(cantidad <= this.oro){
            this.oro -= cantidad;
            return true;
        }
        return false;
    }

    public boolean agregarObjeto(Objeto o){
        if(objetos.size() >= capacidadMax){
            System.out.println(ColorConsola.amarillo("⚠️  Inventario lleno. No puedes llevar más de " + capacidadMax + " objetos."));
            return false;
        }
        objetos.add(o);
        return true;
    }

    public Objeto sacar(int indice){
        if(indice < 0 || indice >= objetos.size()) return null;
        return objetos.remove(indice);
    }

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
