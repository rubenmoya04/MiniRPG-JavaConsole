package PruebasTest.practicar.JuegoRol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Jugador {
    private String nombre;
    private int vida;
    private int vidaMax;
    private int ataque;
    private int defensa;
    private String clase;

    private int nivel;
    private int experiencia;
    private int experienciaSiguienteNivel;
    private int puntosHabilidad;

    private Inventario inventario;
    private ArrayList<EstadoAlterado> estados;

    private int rachaVictorias;
    private int monstruosDerrotados;
    private int oroTotalGanado;
    private int golpesCriticosDados;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.vida = 0;
        this.vidaMax = 0;
        this.ataque = 0;
        this.defensa = 0;
        this.clase = null;
        this.nivel = 1;
        this.experiencia = 0;
        this.experienciaSiguienteNivel = 100;
        this.puntosHabilidad = 0;
        this.inventario = new Inventario(10);
        this.estados = new ArrayList<>();
        this.rachaVictorias = 0;
        this.monstruosDerrotados = 0;
        this.oroTotalGanado = 0;
        this.golpesCriticosDados = 0;
    }

    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public void setVida(int v) {
        this.vida = Math.max(0, Math.min(v, vidaMax));
    }
    public int getVidaMax() { return vidaMax; }
    public void setVidaMax(int vMax) { this.vidaMax = vMax; }
    public int getAtaque() { return ataque; }
    public void setAtaque(int a) { this.ataque = a; }
    public int getDefensa() { return defensa; }
    public void setDefensa(int d) { this.defensa = d; }
    public String getClase() { return clase; }
    public void setClase(String c) { this.clase = c; }

    public int getNivel() { return nivel; }
    public int getExperiencia() { return experiencia; }
    public int getExperienciaSiguienteNivel() { return experienciaSiguienteNivel; }
    public int getPuntosHabilidad() { return puntosHabilidad; }
    public Inventario getInventario(){ return inventario; }
    public ArrayList<EstadoAlterado> getEstados(){ return estados; }
    public int getRachaVictorias(){ return rachaVictorias; }
    public int getMonstruosDerrotados(){ return monstruosDerrotados; }
    public int getOroTotalGanado(){ return oroTotalGanado; }
    public int getGolpesCriticosDados(){ return golpesCriticosDados; }

    public void incrementarMonstruosDerrotados(){ this.monstruosDerrotados++; this.rachaVictorias++; }
    public void resetRacha(){ this.rachaVictorias = 0; }

    // ───────────── CLASES ─────────────
    public void asignarClase(String clase) {
        this.clase = clase.toLowerCase();
        switch (this.clase) {
            case "guerrero" -> { this.vidaMax = 160; this.ataque = 25; this.defensa = 22; }
            case "mago"     -> { this.vidaMax = 95;  this.ataque = 42; this.defensa = 8;  }
            case "arquero"  -> { this.vidaMax = 90;  this.ataque = 32; this.defensa = 7;  }
            case "sacerdote"-> { this.vidaMax = 110; this.ataque = 18; this.defensa = 12; }
            case "esclavo"  -> { this.vidaMax = 70;  this.ataque = 10; this.defensa = 2;  }
            case "ninja"    -> { this.vidaMax = 85;  this.ataque = 28; this.defensa = 10; }
            case "vampiro"  -> { this.vidaMax = 100; this.ataque = 30; this.defensa = 9;  }
            case "paladin"  -> { this.vidaMax = 145; this.ataque = 22; this.defensa = 25; }
            case "druida"   -> { this.vidaMax = 105; this.ataque = 27; this.defensa = 14; }
            case "necromante"->{ this.vidaMax = 95;  this.ataque = 35; this.defensa = 9;  }
            default         -> { this.vidaMax = 100; this.ataque = 20; this.defensa = 10; }
        }
        this.vida = this.vidaMax;
    }

    public void asignarClaseMedioPartida(String clase) {
        this.clase = clase.toLowerCase();
        switch (this.clase) {
            case "guerrero" -> { this.ataque = 25; this.defensa = 22; }
            case "mago"     -> { this.ataque = 42; this.defensa = 8;  }
            case "arquero"  -> { this.ataque = 32; this.defensa = 7;  }
            case "sacerdote"-> { this.ataque = 18; this.defensa = 12; }
            case "esclavo"  -> { this.ataque = 10; this.defensa = 2;  }
            case "ninja"    -> { this.ataque = 28; this.defensa = 10; }
            case "vampiro"  -> { this.ataque = 30; this.defensa = 9;  }
            case "paladin"  -> { this.ataque = 22; this.defensa = 25; }
            case "druida"   -> { this.ataque = 27; this.defensa = 14; }
            case "necromante"->{ this.ataque = 35; this.defensa = 9;  }
            default         -> { this.ataque = 25; this.defensa = 20; }
        }
    }

    public void asignarClasePorNumero(int opcion) {
        String[] clases = {"guerrero", "mago", "arquero", "sacerdote", "esclavo", "ninja", "vampiro", "paladin", "druida", "necromante"};
        if(opcion < 1 || opcion > clases.length){
            System.out.println("Opción inválida, asignando Guerrero por defecto");
            asignarClase("guerrero");
            return;
        }
        asignarClase(clases[opcion - 1]);
    }

    public void asignarClasePorNumeroMedioPartida(int opcion) {
        String[] clases = {"guerrero", "mago", "arquero", "sacerdote", "esclavo", "ninja", "vampiro", "paladin", "druida", "necromante"};
        if(opcion < 1 || opcion > clases.length){
            System.out.println("Opción inválida, asignando Guerrero por defecto");
            asignarClaseMedioPartida("guerrero");
            return;
        }
        asignarClaseMedioPartida(clases[opcion - 1]);
    }

    // ───────────── EXPERIENCIA Y NIVELES ─────────────
    public void ganarExperiencia(int xp){
        this.experiencia += xp;
        System.out.println(ColorConsola.morado("✨ +" + xp + " XP"));
        while(this.experiencia >= this.experienciaSiguienteNivel){
            subirNivel();
        }
    }

    private void subirNivel(){
        this.experiencia -= this.experienciaSiguienteNivel;
        this.nivel++;
        this.experienciaSiguienteNivel = (int)(this.experienciaSiguienteNivel * 1.4);
        int bonusVida = 15 + new Random().nextInt(10);
        int bonusAtaque = 3 + new Random().nextInt(3);
        int bonusDefensa = 2 + new Random().nextInt(3);
        this.vidaMax += bonusVida;
        this.vida = this.vidaMax;
        this.ataque += bonusAtaque;
        this.defensa += bonusDefensa;
        this.puntosHabilidad += 1;

        System.out.println(ColorConsola.amarillo("\n╔═══════════════════════════════╗"));
        System.out.println(ColorConsola.amarillo("║   🌟 ¡SUBISTE DE NIVEL! 🌟    ║"));
        System.out.println(ColorConsola.amarillo("║         NIVEL " + nivel + "             ║"));
        System.out.println(ColorConsola.amarillo("╚═══════════════════════════════╝"));
        System.out.println(ColorConsola.verde("  +" + bonusVida + " ❤️ Vida máxima"));
        System.out.println(ColorConsola.verde("  +" + bonusAtaque + " ⚔️  Ataque"));
        System.out.println(ColorConsola.verde("  +" + bonusDefensa + " 🛡️  Defensa"));
        System.out.println(ColorConsola.verde("  +1 ⭐ Punto de habilidad"));
    }

    public void gastarPuntoHabilidad(int opcion){
        if(puntosHabilidad <= 0){
            System.out.println(ColorConsola.rojo("⛔ No tienes puntos de habilidad."));
            return;
        }
        switch (opcion){
            case 1 -> { this.vidaMax += 20; this.vida += 20; System.out.println(ColorConsola.verde("+20 vida máxima")); }
            case 2 -> { this.ataque += 5; System.out.println(ColorConsola.verde("+5 ataque")); }
            case 3 -> { this.defensa += 4; System.out.println(ColorConsola.verde("+4 defensa")); }
            default -> { System.out.println(ColorConsola.rojo("Opción inválida")); return; }
        }
        puntosHabilidad--;
    }

    // ───────────── COMBATE ─────────────
    public boolean esquivar(){
        Random rd = new Random();
        int chance = 18;
        if("esclavo".equals(clase)) chance = 45;
        if("ninja".equals(clase)) chance = 35;
        if("arquero".equals(clase)) chance = 22;
        return rd.nextInt(100) < chance;
    }

    public int randomCura(){
        Random rd = new Random();
        int base = rd.nextInt(30) + 20;
        if("sacerdote".equals(clase)) base = (int)(base * 1.8);
        if("druida".equals(clase)) base = (int)(base * 1.3);
        return base;
    }

    public void curacionJugador(){
        int cura = randomCura();
        int vidaAntes = this.vida;
        this.vida = Math.min(this.vida + cura, this.vidaMax);
        int curaReal = this.vida - vidaAntes;
        System.out.println(ColorConsola.verde("💚 Te has curado " + curaReal + " HP"));
        System.out.println("❤️  Vida: " + this.vida + "/" + this.vidaMax);
    }

    public int randomTiroUlti(){
        Random rd = new Random();
        int base = rd.nextInt(80) + 50;
        if("mago".equals(clase)) base = (int)(base * 1.5);
        if("necromante".equals(clase)) base = (int)(base * 1.4);
        if("arquero".equals(clase)) base = (int)(base * 1.3);
        return base + (nivel * 5);
    }

    public void ultimateTiro(ArrayList<Monstruo> m, int opc){
        int danoUlti = randomTiroUlti();
        Random rd = new Random();
        String nombreUlti;
        String[] nombresUlti = {
            "💥 Destruye Mundos",
            "☠️  Sangrador de los Muertos",
            "🧁 Magdalenas Explosivas",
            "🌪️  Tormenta Cósmica",
            "🔥 Aliento del Dragón",
            "⚡ Rayo del Olimpo",
            "🌑 Eclipse Mortal",
            "❄️  Glaciar Eterno"
        };
        nombreUlti = nombresUlti[rd.nextInt(nombresUlti.length)];

        Monstruo objetivo = m.get(opc);
        System.out.println(ColorConsola.morado("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorConsola.morado("║      💫 ULTIMATE ACTIVADA 💫         ║"));
        System.out.println(ColorConsola.morado("╚══════════════════════════════════════╝"));
        ColorConsola.escribirLento("⚡ " + nombreUlti + " — daño: " + ColorConsola.rojo(String.valueOf(danoUlti)), 20);

        objetivo.recibirDano(danoUlti);

        // Robo de vida del vampiro con ulti
        if("vampiro".equals(clase)){
            int absorbido = danoUlti / 3;
            this.vida = Math.min(this.vida + absorbido, this.vidaMax);
            System.out.println(ColorConsola.rojo("🩸 Has absorbido " + absorbido + " de vida."));
        }

        if(objetivo.estaVivo()){
            ColorConsola.barraVida(objetivo.getNombre(), objetivo.getVida(), objetivo.getVidaMax());
        }
    }

    public void recibirDano(int dano) {
        if((clase.equals("esclavo") || clase.equals("ninja")) && esquivar()){
            System.out.println(ColorConsola.cyan("🌀 ¡Has esquivado el ataque!"));
            return;
        }

        if("paladin".equals(clase) && new Random().nextInt(100) < 15){
            System.out.println(ColorConsola.azul("✨ ¡Escudo Divino! Ataque bloqueado por completo."));
            return;
        }

        this.vida = Math.max(0, this.vida - dano);
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    public int atacar() {
        Random rd = new Random();
        int danoFinal = this.ataque;
        boolean critico = false;

        if("esclavo".equals(clase)){
            if(rd.nextInt(100) < 25){
                System.out.println(ColorConsola.amarillo("⚔️  ¡Golpe crítico del esclavo!"));
                danoFinal = (int)(this.ataque * 2.4);
                critico = true;
            }
        }

        if("arquero".equals(clase)){
            int roll = rd.nextInt(100);
            if(roll < 2){
                System.out.println(ColorConsola.rojo("🔥 ¡TIRO DE FRANCOTIRADOR! 🔥"));
                danoFinal = this.ataque * 6;
                critico = true;
            } else if(roll < 12){
                System.out.println(ColorConsola.amarillo("🏹 ¡TIRO DEMENCIAL!"));
                danoFinal = this.ataque * 4;
                critico = true;
            } else if(roll < 35){
                System.out.println(ColorConsola.amarillo("🎯 ¡HEADSHOT!"));
                danoFinal = this.ataque * 3;
                critico = true;
            }
        }

        if("ninja".equals(clase)){
            if(rd.nextInt(100) < 30){
                System.out.println(ColorConsola.morado("🗡️  ¡Doble ataque ninja!"));
                danoFinal = (int)(this.ataque * 1.8);
                critico = true;
            }
            if(rd.nextInt(100) < 8){
                System.out.println(ColorConsola.morado("💀 ¡ASESINATO SILENCIOSO!"));
                danoFinal = this.ataque * 5;
                critico = true;
            }
        }

        if("vampiro".equals(clase)){
            int absorbido = Math.max(1, danoFinal / 4);
            this.vida = Math.min(this.vida + absorbido, this.vidaMax);
            System.out.println(ColorConsola.rojo("🩸 Robaste " + absorbido + " de vida"));
        }

        if("paladin".equals(clase)){
            if(rd.nextInt(100) < 20){
                System.out.println(ColorConsola.amarillo("⚜️  ¡Golpe sagrado!"));
                danoFinal = (int)(this.ataque * 1.8);
                this.vida = Math.min(this.vida + 5, this.vidaMax);
                critico = true;
            }
        }

        if("mago".equals(clase)){
            int hechizo = rd.nextInt(100);
            if(hechizo < 25){
                String[] hechizos = {"🔥 Bola de Fuego", "❄️  Rayo de Hielo", "⚡ Descarga Eléctrica", "🌪️  Tormenta Arcana"};
                System.out.println(ColorConsola.azul(hechizos[rd.nextInt(hechizos.length)] + "!"));
                danoFinal = (int)(this.ataque * 1.6);
                critico = true;
            }
        }

        if("druida".equals(clase)){
            int form = rd.nextInt(100);
            if(form < 20){
                System.out.println(ColorConsola.verde("🐺 ¡Transformación en lobo feroz!"));
                danoFinal = (int)(this.ataque * 2.0);
                critico = true;
            } else if(form < 30){
                System.out.println(ColorConsola.verde("🌿 ¡Enredaderas mágicas atacan!"));
                danoFinal = (int)(this.ataque * 1.5);
            }
        }

        if("necromante".equals(clase)){
            if(rd.nextInt(100) < 25){
                System.out.println(ColorConsola.morado("💀 ¡Esqueletos invocados atacan en grupo!"));
                int extraEsqueletos = 1 + rd.nextInt(3);
                danoFinal += extraEsqueletos * 8;
                System.out.println(ColorConsola.morado("   " + extraEsqueletos + " esqueleto(s) golpean. +" + (extraEsqueletos * 8) + " daño."));
                critico = true;
            }
        }

        if("sacerdote".equals(clase)){
            if(rd.nextInt(100) < 15){
                System.out.println(ColorConsola.amarillo("✝️  ¡Luz divina purificadora!"));
                danoFinal = (int)(this.ataque * 1.8);
                this.vida = Math.min(this.vida + 8, this.vidaMax);
                critico = true;
            }
        }

        if("guerrero".equals(clase)){
            if(rd.nextInt(100) < 20){
                System.out.println(ColorConsola.rojo("💪 ¡Golpe devastador!"));
                danoFinal = (int)(this.ataque * 2.0);
                critico = true;
            }
        }

        // Furia activa
        for(EstadoAlterado e : estados){
            if(e.getTipo() == EstadoAlterado.Tipo.FURIA){
                danoFinal = (int)(danoFinal * 1.5);
                System.out.println(ColorConsola.rojo("🔥 ¡Furia! Daño aumentado."));
                break;
            }
        }

        if(critico) golpesCriticosDados++;
        return danoFinal;
    }

    // ───────────── ESTADOS ALTERADOS ─────────────
    public void aplicarEstado(EstadoAlterado est){
        // Sacerdote y paladín son resistentes a estados negativos
        if(("sacerdote".equals(clase) || "paladin".equals(clase)) && new Random().nextInt(100) < 40){
            if(est.getTipo() != EstadoAlterado.Tipo.BENDECIDO && est.getTipo() != EstadoAlterado.Tipo.FURIA){
                System.out.println(ColorConsola.amarillo("✨ Has resistido el estado " + est.nombre() + "!"));
                return;
            }
        }
        // Si ya tiene ese estado, refresca turnos
        for(EstadoAlterado e : estados){
            if(e.getTipo() == est.getTipo()){
                if(est.getTurnosRestantes() > e.getTurnosRestantes()){
                    estados.remove(e);
                    break;
                } else {
                    System.out.println(ColorConsola.cyan("· Ya estás afectado por " + est.nombre()));
                    return;
                }
            }
        }
        estados.add(est);
        System.out.println(ColorConsola.morado("➜ Estado aplicado: " + est.nombre()));
    }

    public void procesarEstados(){
        Iterator<EstadoAlterado> it = estados.iterator();
        while(it.hasNext()){
            EstadoAlterado e = it.next();
            switch(e.getTipo()){
                case ENVENENADO, QUEMADO, SANGRADO -> {
                    recibirDano(e.getValor());
                    System.out.println(ColorConsola.rojo("· " + e.nombre() + " te causa " + e.getValor() + " daño"));
                }
                case BENDECIDO -> {
                    int curaB = e.getValor();
                    this.vida = Math.min(this.vida + curaB, this.vidaMax);
                    System.out.println(ColorConsola.verde("· " + e.nombre() + " te cura " + curaB + " HP"));
                }
                default -> {}
            }
            e.reducirTurno();
            if(e.expiro()){
                System.out.println(ColorConsola.cyan("· El efecto de " + e.nombre() + " ha expirado."));
                it.remove();
            }
        }
    }

    public boolean estaAturdido(){
        for(EstadoAlterado e : estados){
            if(e.getTipo() == EstadoAlterado.Tipo.ATURDIDO || e.getTipo() == EstadoAlterado.Tipo.CONGELADO){
                return true;
            }
        }
        return false;
    }

    public void registrarGanarOro(int o){ this.oroTotalGanado += o; this.inventario.agregarOro(o); }

    // ───────────── MOSTRAR ─────────────
    public void mostrarPersonaje() {
        System.out.println(ColorConsola.cyan("\n╔════════════════════════════════════════════════╗"));
        String linea = String.format("║   %-10s — %-12s  Nivel %-3d         ║", nombre, clase.toUpperCase(), nivel);
        System.out.println(ColorConsola.amarillo(linea));
        System.out.println(ColorConsola.cyan("╠════════════════════════════════════════════════╣"));
        ColorConsola.barraVida("❤️  Vida    ", vida, vidaMax);
        System.out.printf("⚔️  Ataque   : %d%n", ataque);
        System.out.printf("🛡️  Defensa  : %d%n", defensa);
        System.out.printf("⭐ XP        : %d / %d%n", experiencia, experienciaSiguienteNivel);
        System.out.printf("✨ Puntos    : %d%n", puntosHabilidad);
        System.out.printf("💰 Oro       : %d%n", inventario.getOro());
        System.out.printf("🏆 Derrotados: %d  |  Racha: %d  |  Críticos: %d%n",
                monstruosDerrotados, rachaVictorias, golpesCriticosDados);

        if(!estados.isEmpty()){
            System.out.print("📛 Estados   : ");
            for(EstadoAlterado e : estados){
                System.out.print(e.descripcion() + "  ");
            }
            System.out.println();
        }
        System.out.println(ColorConsola.cyan("╚════════════════════════════════════════════════╝"));
    }
}
