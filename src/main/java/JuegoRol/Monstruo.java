package PruebasTest.practicar.JuegoRol;

import java.util.ArrayList;
import java.util.Random;

public class Monstruo {
    private String nombre;
    private int vida;
    private int vidaMax;
    private int ataque;
    private int defensa;
    private String tipo;
    private int xpAlMorir;
    private int oroAlMorir;
    private boolean esBoss;
    private boolean puedeRevivir;
    private String arteAscii;

    public Monstruo(String nombre, int vida, int ataque, int defensa, String tipo) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMax = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.tipo = tipo;
        this.xpAlMorir = vida / 3 + ataque;
        this.oroAlMorir = vida / 5 + 10;
        this.esBoss = false;
        this.puedeRevivir = false;
        this.arteAscii = "";
    }

    public Monstruo(String nombre, int vida, int ataque, int defensa, String tipo, boolean esBoss) {
        this(nombre, vida, ataque, defensa, tipo);
        this.esBoss = esBoss;
        if(esBoss){
            this.xpAlMorir = vida + ataque * 3;
            this.oroAlMorir = vida + 100;
        }
    }

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

    public void recibirDano(int dano) {
        int defReducida = (int)(dano * (defensa * 0.005));
        int danoFinal = Math.max(1, dano - defReducida);
        this.vida = Math.max(0, this.vida - danoFinal);
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    public int atacar() {
        Random rd = new Random();
        int danoBase = this.ataque;

        // Bosses tienen chance de golpe especial
        if(esBoss && rd.nextInt(100) < 25){
            danoBase = (int)(this.ataque * 1.8);
            System.out.println(ColorConsola.rojo("💢 ¡" + nombre + " usa un ataque devastador!"));
        }

        return danoBase;
    }

    // Algunos monstruos aplican estados al atacar
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
        return null;
    }

    // Regeneración (hidra, troll, etc.)
    public void regenerar(){
        if((nombre.contains("Hidra") || nombre.contains("Troll")) && estaVivo() && vida < vidaMax){
            int regen = (int)(vidaMax * 0.08);
            vida = Math.min(vidaMax, vida + regen);
            System.out.println(ColorConsola.verde("🌿 " + nombre + " regenera " + regen + " HP."));
        }
    }

    public boolean intentarRevivir(){
        if(puedeRevivir && vida <= 0){
            puedeRevivir = false;
            vida = vidaMax / 2;
            System.out.println(ColorConsola.morado("\n🔥🔥🔥 ¡" + nombre + " ha renacido de sus cenizas! 🔥🔥🔥"));
            return true;
        }
        return false;
    }

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

    public void mostrarBarraCorta(){
        ColorConsola.barraVida("👹 " + nombre, vida, vidaMax);
    }

    // ───────────── Fábricas de monstruos ─────────────
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
        Monstruo fenix = new Monstruo("Fénix Oscuro", 180, 35, 16, "Dragon", false);
        fenix.setPuedeRevivir(true);
        lista.add(fenix);
        return lista;
    }

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
