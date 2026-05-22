package PruebasTest.practicar.JuegoRol;

import java.util.ArrayList;
import java.util.Random;

// Clase Monstruo: todo lo que da hostias al jugador
public class Monstruo {
    private String nombre;
    private int vida;
    private int vidaMax;
    private int ataque;
    private int defensa;
    private String tipo;          // ej: "Bestia", "No muerto", "Dragon", "Demonio"...
    private int xpAlMorir;        // xp que da el bicho al palmar
    private int oroAlMorir;       // oro que suelta al palmar
    private boolean esBoss;
    private boolean puedeRevivir; // pal Fenix Oscuro
    private String arteAscii;     // dibujito para los bosses

    // Constructor normal de toda la vida
    public Monstruo(String nombre, int vida, int ataque, int defensa, String tipo) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMax = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.tipo = tipo;
        this.xpAlMorir = vida / 3 + ataque;  //una formulilla pa que cuadre
        this.oroAlMorir = vida / 5 + 10;
        this.esBoss = false;
        this.puedeRevivir = false;
        this.arteAscii = "";
    }

    // Constructor pa cuando es boss (suelta mas xp y oro)
    public Monstruo(String nombre, int vida, int ataque, int defensa, String tipo, boolean esBoss) {
        this(nombre, vida, ataque, defensa, tipo);
        this.esBoss = esBoss;
        if(esBoss){
            this.xpAlMorir = vida + ataque * 3;
            this.oroAlMorir = vida + 100;
        }
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }
    public int getVidaMax() { return vidaMax; }
    public int getAtaque() { return ataque; }
    public void setAtaque(int ataque) { this.ataque = ataque; }
    public int getDefensa() { return defensa; }
    public void setDefensa(int defensa) { this.defensa = defensa; }
    public String getTipo() { return tipo; }
    public int getXpAlMorir() { return xpAlMorir; }
    public int getOroAlMorir() { return oroAlMorir; }
    public boolean esBoss() { return esBoss; }
    public boolean puedeRevivir() { return puedeRevivir; }
    public void setPuedeRevivir(boolean p) { this.puedeRevivir = p; }
    public String getArteAscii() { return arteAscii; }
    public void setArteAscii(String arte) { this.arteAscii = arte; }

    // Le quita vida al monstruo aplicando un poquito su defensa pa que no sea todo daño full
    public void recibirDaño(int daño) {
        int defReducida = (int)(daño * (defensa * 0.005)); // cada punto de defensa quita un 0.5%
        int dañoFinal = Math.max(1, daño - defReducida);   // como minimo 1 de daño, sino se aburre uno
        this.vida = Math.max(0, this.vida - dañoFinal);
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    // Devuelve el daño que mete al jugador (los bosses tienen golpe especial random)
    public int atacar() {
        Random rd = new Random();
        int dañoBase = this.ataque;

        // Los bosses con un 25% pegan un mamporrazo
        if(esBoss && rd.nextInt(100) < 25){
            dañoBase = (int)(this.ataque * 1.8);
            System.out.println(ColorConsola.rojo("💢 ¡" + nombre + " usa un ataque devastador!"));
        }

        return dañoBase;
    }

    // Segun el tipo de monstruo, puede dejarte un estado chungo al pegarte
    // (no muertos envenenan, dragones queman, etc.)
    public EstadoAlterado intentarAplicarEstado(){
        Random rd = new Random();
        switch(tipo){
            case "No muerto" -> {
                if(rd.nextInt(100) < 20){
                    return new EstadoAlterado(EstadoAlterado.Tipo.ENVENENADO, 3, 5);
                }
            }
            case "Dragon" -> {
                if(rd.nextInt(100) < 30){
                    return new EstadoAlterado(EstadoAlterado.Tipo.QUEMADO, 3, 8);
                }
            }
            case "Espectro" -> {
                if(rd.nextInt(100) < 25){
                    return new EstadoAlterado(EstadoAlterado.Tipo.ATURDIDO, 1, 0);
                }
            }
            case "Mago oscuro" -> {
                if(rd.nextInt(100) < 30){
                    // el lich te suelta o un congelado o un quemao a ver cual le toca
                    if(rd.nextBoolean()){
                        return new EstadoAlterado(EstadoAlterado.Tipo.CONGELADO, 1, 0);
                    } else {
                        return new EstadoAlterado(EstadoAlterado.Tipo.QUEMADO, 2, 7);
                    }
                }
            }
            case "Bestia" -> {
                if(rd.nextInt(100) < 20){
                    return new EstadoAlterado(EstadoAlterado.Tipo.SANGRADO, 3, 6);
                }
            }
            case "Marino" -> {
                if(rd.nextInt(100) < 25){
                    return new EstadoAlterado(EstadoAlterado.Tipo.ATURDIDO, 1, 0);
                }
            }
            case "Demonio" -> {
                if(rd.nextInt(100) < 30){
                    return new EstadoAlterado(EstadoAlterado.Tipo.QUEMADO, 2, 6);
                }
            }
        }
        return null; //si no le toca aplicar nada, pues nada
    }

    // La Hidra y el Troll se regeneran cada turno (un 8% de su vida max)
    public void regenerar(){
        if((nombre.contains("Hidra") || nombre.contains("Troll")) && estaVivo() && vida < vidaMax){
            int regen = (int)(vidaMax * 0.08);
            vida = Math.min(vidaMax, vida + regen);
            System.out.println(ColorConsola.verde("🌿 " + nombre + " regenera " + regen + " HP."));
        }
    }

    // El Fenix Oscuro vuelve a la vida una vez con la mitad de vida (que cabron)
    public boolean intentarRevivir(){
        if(puedeRevivir && vida <= 0){
            puedeRevivir = false; //solo una vez, no se hace eterno
            vida = vidaMax / 2;
            System.out.println(ColorConsola.morado("\n🔥🔥🔥 ¡" + nombre + " ha renacido de sus cenizas! 🔥🔥🔥"));
            return true;
        }
        return false;
    }

    // Imprime toda la chapa del monstruo
    public void mostrarMonstruo() {
        System.out.println(ColorConsola.cyan("\n╔══════════════════════════════════════════╗"));
        String etiqueta = esBoss ? ColorConsola.rojo("👑 BOSS ") + nombre : nombre;
        System.out.println("   " + etiqueta + " (" + tipo + ")");
        if(!arteAscii.isEmpty()) System.out.println(arteAscii);
        ColorConsola.barraVida("❤️ ", vida, vidaMax);
        System.out.println("⚔️  Ataque : " + ataque);
        System.out.println("🛡️  Defensa: " + defensa);
        System.out.println("✨ Da " + xpAlMorir + " XP y " + oroAlMorir + " oro");
        System.out.println(ColorConsola.cyan("╚══════════════════════════════════════════╝"));
    }

    // Una barrita rapida para verla en el combate
    public void mostrarBarraCorta(){
        ColorConsola.barraVida("👹 " + nombre, vida, vidaMax);
    }

    // ─────── Fabricas de monstruos por dificultad ───────
    // Las uso desde mainRol para ir generando las oleadas

    public static ArrayList<Monstruo> generarMonstruosFaciles(){
        ArrayList<Monstruo> lista = new ArrayList<>();
        lista.add(new Monstruo("Goblin", 40, 10, 3, "Bestia"));
        lista.add(new Monstruo("Esqueleto", 55, 14, 5, "No muerto"));
        lista.add(new Monstruo("Zombie", 70, 12, 6, "No muerto"));
        return lista;
    }

    public static ArrayList<Monstruo> generarMonstruosNormales(){
        ArrayList<Monstruo> lista = new ArrayList<>();
        lista.add(new Monstruo("Ogro", 110, 22, 12, "Bestia"));
        lista.add(new Monstruo("Troll", 130, 20, 15, "Bestia"));
        lista.add(new Monstruo("Minotauro", 140, 26, 14, "Bestia"));
        lista.add(new Monstruo("Banshee", 85, 24, 6, "Espectro"));
        return lista;
    }

    public static ArrayList<Monstruo> generarMonstruosDificiles(){
        ArrayList<Monstruo> lista = new ArrayList<>();
        lista.add(new Monstruo("Hidra", 170, 28, 14, "Bestia"));
        lista.add(new Monstruo("Quimera", 160, 30, 16, "Bestia"));
        lista.add(new Monstruo("Lich", 130, 35, 12, "Mago oscuro"));
        lista.add(new Monstruo("Vampiresa", 140, 32, 11, "No muerto"));
        return lista;
    }

    public static ArrayList<Monstruo> generarMonstruosPesadilla(){
        ArrayList<Monstruo> lista = new ArrayList<>();
        lista.add(new Monstruo("Dragón Rojo", 220, 38, 22, "Dragon", false));
        lista.add(new Monstruo("Demonio Sombra", 200, 36, 18, "Demonio", false));
        lista.add(new Monstruo("Kraken", 240, 34, 20, "Marino", false));
        // El Fenix Oscuro lleva la flag de revivir activada
        Monstruo fenix = new Monstruo("Fénix Oscuro", 180, 35, 16, "Dragon", false);
        fenix.setPuedeRevivir(true);
        lista.add(fenix);
        return lista;
    }

    // El boss final del juego
    public static Monstruo generarBossFinal(){
        Monstruo boss = new Monstruo("Señor Oscuro Malakar", 450, 45, 25, "Demonio", true);
        boss.setArteAscii(
                ColorConsola.rojo("              ,▄▓▓▓▓▓▄,           \n") +
                ColorConsola.rojo("           ▄▓▓▓▓▓▓▓▓▓▓▓▄         \n") +
                ColorConsola.rojo("          ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓        \n") +
                ColorConsola.rojo("          ▓█▓█▓▓▓▓▓▓▓▓█▓█▓        \n") +
                ColorConsola.rojo("           ▀▓▀▓▓▓▓▓▓▓▓▓▀▓▀        \n") +
                ColorConsola.amarillo("              ╲╲ ▼▼ ╱╱              ")
        );
        return boss;
    }
}
