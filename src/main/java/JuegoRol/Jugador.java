package PruebasTest.practicar.JuegoRol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

// Clase Jugador. Aqui va todo lo del personaje: stats, clase, inventario,
// estados que le afectan, niveles... vaya, casi todo el follon del juego.
public class Jugador {
    // ─── Stats basicos ───
    private String nombre;
    private int vida;
    private int vidaMax;
    private int ataque;
    private int defensa;
    private String clase;

    // ─── Sistema de niveles ───
    private int nivel;
    private int experiencia;
    private int experienciaSiguienteNivel; // cuanta xp falta para subir
    private int puntosHabilidad;            // puntos para repartir al subir

    // ─── Inventario y estados ───
    private Inventario inventario;
    private ArrayList<EstadoAlterado> estados;

    // ─── Estadisticas pa mostrar al final ───
    private int rachaVictorias;
    private int monstruosDerrotados;
    private int oroTotalGanado;
    private int golpesCriticosDados;

    // Constructor: solo con el nombre, lo demas se setea al elegir clase
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.vida = 0;
        this.vidaMax = 0;
        this.ataque = 0;
        this.defensa = 0;
        this.clase = null;
        this.nivel = 1;
        this.experiencia = 0;
        this.experienciaSiguienteNivel = 100; //al principio con 100 xp ya sube
        this.puntosHabilidad = 0;
        this.inventario = new Inventario(10); // 10 huecos de mochila
        this.estados = new ArrayList<>();
        this.rachaVictorias = 0;
        this.monstruosDerrotados = 0;
        this.oroTotalGanado = 0;
        this.golpesCriticosDados = 0;
    }

    // ─────── Getters y setters ───────
    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public void setVida(int v) {
        // Lo capamos pa que no pase de la vida max ni baje de 0
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
    // Le asigna stats al jugador segun la clase que elija. Cada clase tiene su rollo.
    public void asignarClase(String clase) {
        this.clase = clase.toLowerCase();
        switch (this.clase) {
            case "guerrero" -> { this.vidaMax = 160; this.ataque = 25; this.defensa = 22; } // tanque clasico
            case "mago"     -> { this.vidaMax = 95;  this.ataque = 42; this.defensa = 8;  } // pega duro pero es de papel
            case "arquero"  -> { this.vidaMax = 90;  this.ataque = 32; this.defensa = 7;  } // criticos a saco
            case "sacerdote"-> { this.vidaMax = 110; this.ataque = 18; this.defensa = 12; } // cura y aguanta
            case "esclavo"  -> { this.vidaMax = 70;  this.ataque = 10; this.defensa = 2;  } // el modo dificil
            case "ninja"    -> { this.vidaMax = 85;  this.ataque = 28; this.defensa = 10; } // esquiva y asesina
            case "vampiro"  -> { this.vidaMax = 100; this.ataque = 30; this.defensa = 9;  } // roba vida
            case "paladin"  -> { this.vidaMax = 145; this.ataque = 22; this.defensa = 25; } // muy defensivo
            case "druida"   -> { this.vidaMax = 105; this.ataque = 27; this.defensa = 14; } // se transforma en bicho
            case "necromante"->{ this.vidaMax = 95;  this.ataque = 35; this.defensa = 9;  } // invoca esqueletos
            default         -> { this.vidaMax = 100; this.ataque = 20; this.defensa = 10; } // si pone cualquier mierda
        }
        this.vida = this.vidaMax; // empezamos a tope claro
    }

    // Cambiar de clase a mitad de partida: solo cambia ataque y defensa, la vida no la toco
    // pa que no se aproveche y se "rellene" cambiando de clase
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

    // Atajo: el menu pasa un numero del 1 al 10 y se mapea a la clase
    public void asignarClasePorNumero(int opcion) {
        String[] clases = {"guerrero", "mago", "arquero", "sacerdote", "esclavo", "ninja", "vampiro", "paladin", "druida", "necromante"};
        if(opcion < 1 || opcion > clases.length){
            System.out.println("Opción inválida, asignando Guerrero por defecto");
            asignarClase("guerrero");
            return;
        }
        asignarClase(clases[opcion - 1]);
    }

    // Igual que el de arriba pero pa cambiar a mitad de partida
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

    // Suma xp al jugador. Mientras le sobre, sigue subiendo niveles
    public void ganarExperiencia(int xp){
        this.experiencia += xp;
        System.out.println(ColorConsola.morado("✨ +" + xp + " XP"));
        while(this.experiencia >= this.experienciaSiguienteNivel){
            subirNivel();
        }
    }

    // Sube un nivel, mete bonus aleatorios pa que cada subida sea un poco distinta
    private void subirNivel(){
        this.experiencia -= this.experienciaSiguienteNivel;
        this.nivel++;
        // Cada nivel cuesta un 40% mas de xp que el anterior
        this.experienciaSiguienteNivel = (int)(this.experienciaSiguienteNivel * 1.4);

        int bonusVida = 15 + new Random().nextInt(10);
        int bonusAtaque = 3 + new Random().nextInt(3);
        int bonusDefensa = 2 + new Random().nextInt(3);
        this.vidaMax += bonusVida;
        this.vida = this.vidaMax; //ademas curamos a tope al subir, regalo
        this.ataque += bonusAtaque;
        this.defensa += bonusDefensa;
        this.puntosHabilidad += 1;

        // Cartelaco de subida de nivel
        System.out.println(ColorConsola.amarillo("\n╔═══════════════════════════════╗"));
        System.out.println(ColorConsola.amarillo("║   🌟 ¡SUBISTE DE NIVEL! 🌟    ║"));
        System.out.println(ColorConsola.amarillo("║         NIVEL " + nivel + "             ║"));
        System.out.println(ColorConsola.amarillo("╚═══════════════════════════════╝"));
        System.out.println(ColorConsola.verde("  +" + bonusVida + " ❤️ Vida máxima"));
        System.out.println(ColorConsola.verde("  +" + bonusAtaque + " ⚔️  Ataque"));
        System.out.println(ColorConsola.verde("  +" + bonusDefensa + " 🛡️  Defensa"));
        System.out.println(ColorConsola.verde("  +1 ⭐ Punto de habilidad"));
    }

    // Gasta un punto de habilidad en el atributo que elija el jugador
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

    // Probabilidad de esquivar el ataque enemigo (varia por clase)
    public boolean esquivar(){
        Random rd = new Random();
        int chance = 18; //por defecto un 18%
        if("esclavo".equals(clase)) chance = 45; //el esclavo se mueve como una rata
        if("ninja".equals(clase)) chance = 35;
        if("arquero".equals(clase)) chance = 22;
        return rd.nextInt(100) < chance;
    }

    // Cantidad de cura random (sacerdote cura mucho mas, druida tambien algo)
    public int randomCura(){
        Random rd = new Random();
        int base = rd.nextInt(30) + 20;
        if("sacerdote".equals(clase)) base = (int)(base * 1.8);
        if("druida".equals(clase)) base = (int)(base * 1.3);
        return base;
    }

    // Usa la curacion del menu. La capo a vida max pa que no se pase
    public void curacionJugador(){
        int cura = randomCura();
        int vidaAntes = this.vida;
        this.vida = Math.min(this.vida + cura, this.vidaMax);
        int curaReal = this.vida - vidaAntes;
        System.out.println(ColorConsola.verde("💚 Te has curado " + curaReal + " HP"));
        System.out.println("❤️  Vida: " + this.vida + "/" + this.vidaMax);
    }

    // Daño base de la ultimate, escalado por clase y nivel
    public int randomTiroUlti(){
        Random rd = new Random();
        int base = rd.nextInt(80) + 50;
        if("mago".equals(clase)) base = (int)(base * 1.5);
        if("necromante".equals(clase)) base = (int)(base * 1.4);
        if("arquero".equals(clase)) base = (int)(base * 1.3);
        return base + (nivel * 5); //pa que cuanto mas nivel, mas pega
    }

    // Suelta la ultimate sobre un monstruo. Cada vez le pone un nombre molon random.
    public void ultimateTiro(ArrayList<Monstruo> m, int opc){
        int dañoUlti = randomTiroUlti();
        Random rd = new Random();
        String nombreUlti;
        // Lista de nombres flipantes pa la ulti, salen al azar
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
        ColorConsola.escribirLento("⚡ " + nombreUlti + " — daño: " + ColorConsola.rojo(String.valueOf(dañoUlti)), 20);

        objetivo.recibirDaño(dañoUlti);

        // Si soy vampiro, ademas robo un tercio del daño en vida
        if("vampiro".equals(clase)){
            int absorbido = dañoUlti / 3;
            this.vida = Math.min(this.vida + absorbido, this.vidaMax);
            System.out.println(ColorConsola.rojo("🩸 Has absorbido " + absorbido + " de vida."));
        }

        if(objetivo.estaVivo()){
            ColorConsola.barraVida(objetivo.getNombre(), objetivo.getVida(), objetivo.getVidaMax());
        }
    }

    // Recibe daño. El esclavo y el ninja a veces esquivan, y el paladin a veces bloquea full
    public void recibirDaño(int daño) {
        // Esquive total (esclavo y ninja)
        if((clase.equals("esclavo") || clase.equals("ninja")) && esquivar()){
            System.out.println(ColorConsola.cyan("🌀 ¡Has esquivado el ataque!"));
            return;
        }

        // Escudo divino del paladin: 15% de no comerse nada
        if("paladin".equals(clase) && new Random().nextInt(100) < 15){
            System.out.println(ColorConsola.azul("✨ ¡Escudo Divino! Ataque bloqueado por completo."));
            return;
        }

        this.vida = Math.max(0, this.vida - daño); //pa que no quede negativa
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    // El ataque del jugador. Cada clase tiene sus golpes especiales con su probabilidad.
    public int atacar() {
        Random rd = new Random();
        int dañoFinal = this.ataque;
        boolean critico = false;

        // ─── Esclavo: critico desesperao ───
        if("esclavo".equals(clase)){
            if(rd.nextInt(100) < 25){
                System.out.println(ColorConsola.amarillo("⚔️  ¡Golpe crítico del esclavo!"));
                dañoFinal = (int)(this.ataque * 2.4);
                critico = true;
            }
        }

        // ─── Arquero: cadena de criticos (headshot, demencial, francotirador) ───
        if("arquero".equals(clase)){
            int roll = rd.nextInt(100);
            if(roll < 2){
                System.out.println(ColorConsola.rojo("🔥 ¡TIRO DE FRANCOTIRADOR! 🔥"));
                dañoFinal = this.ataque * 6; //un 2% pero te revienta a cualquier bicho
                critico = true;
            } else if(roll < 12){
                System.out.println(ColorConsola.amarillo("🏹 ¡TIRO DEMENCIAL!"));
                dañoFinal = this.ataque * 4;
                critico = true;
            } else if(roll < 35){
                System.out.println(ColorConsola.amarillo("🎯 ¡HEADSHOT!"));
                dañoFinal = this.ataque * 3;
                critico = true;
            }
        }

        // ─── Ninja: doble ataque o asesinato ───
        if("ninja".equals(clase)){
            if(rd.nextInt(100) < 30){
                System.out.println(ColorConsola.morado("🗡️  ¡Doble ataque ninja!"));
                dañoFinal = (int)(this.ataque * 1.8);
                critico = true;
            }
            // Y encima a veces le casca el asesinato silencioso
            if(rd.nextInt(100) < 8){
                System.out.println(ColorConsola.morado("💀 ¡ASESINATO SILENCIOSO!"));
                dañoFinal = this.ataque * 5;
                critico = true;
            }
        }

        // ─── Vampiro: roba vida en cada golpe (un 25% del daño) ───
        if("vampiro".equals(clase)){
            int absorbido = Math.max(1, dañoFinal / 4);
            this.vida = Math.min(this.vida + absorbido, this.vidaMax);
            System.out.println(ColorConsola.rojo("🩸 Robaste " + absorbido + " de vida"));
        }

        // ─── Paladin: golpe sagrado, ademas se cura un poco ───
        if("paladin".equals(clase)){
            if(rd.nextInt(100) < 20){
                System.out.println(ColorConsola.amarillo("⚜️  ¡Golpe sagrado!"));
                dañoFinal = (int)(this.ataque * 1.8);
                this.vida = Math.min(this.vida + 5, this.vidaMax);
                critico = true;
            }
        }

        // ─── Mago: lanza un hechizo aleatorio ───
        if("mago".equals(clase)){
            int hechizo = rd.nextInt(100);
            if(hechizo < 25){
                String[] hechizos = {"🔥 Bola de Fuego", "❄️  Rayo de Hielo", "⚡ Descarga Eléctrica", "🌪️  Tormenta Arcana"};
                System.out.println(ColorConsola.azul(hechizos[rd.nextInt(hechizos.length)] + "!"));
                dañoFinal = (int)(this.ataque * 1.6);
                critico = true;
            }
        }

        // ─── Druida: o se vuelve lobo, o saca enredaderas ───
        if("druida".equals(clase)){
            int form = rd.nextInt(100);
            if(form < 20){
                System.out.println(ColorConsola.verde("🐺 ¡Transformación en lobo feroz!"));
                dañoFinal = (int)(this.ataque * 2.0);
                critico = true;
            } else if(form < 30){
                System.out.println(ColorConsola.verde("🌿 ¡Enredaderas mágicas atacan!"));
                dañoFinal = (int)(this.ataque * 1.5);
            }
        }

        // ─── Necromante: a veces invoca un puñao de esqueletos ───
        if("necromante".equals(clase)){
            if(rd.nextInt(100) < 25){
                System.out.println(ColorConsola.morado("💀 ¡Esqueletos invocados atacan en grupo!"));
                int extraEsqueletos = 1 + rd.nextInt(3); //de 1 a 3 esqueletos
                dañoFinal += extraEsqueletos * 8;
                System.out.println(ColorConsola.morado("   " + extraEsqueletos + " esqueleto(s) golpean. +" + (extraEsqueletos * 8) + " daño."));
                critico = true;
            }
        }

        // ─── Sacerdote: golpe sagrado que ademas cura ───
        if("sacerdote".equals(clase)){
            if(rd.nextInt(100) < 15){
                System.out.println(ColorConsola.amarillo("✝️  ¡Luz divina purificadora!"));
                dañoFinal = (int)(this.ataque * 1.8);
                this.vida = Math.min(this.vida + 8, this.vidaMax);
                critico = true;
            }
        }

        // ─── Guerrero: el clasico golpe devastador ───
        if("guerrero".equals(clase)){
            if(rd.nextInt(100) < 20){
                System.out.println(ColorConsola.rojo("💪 ¡Golpe devastador!"));
                dañoFinal = (int)(this.ataque * 2.0);
                critico = true;
            }
        }

        // Si tengo Furia activa, le meto un +50% al daño final encima de todo lo demas
        for(EstadoAlterado e : estados){
            if(e.getTipo() == EstadoAlterado.Tipo.FURIA){
                dañoFinal = (int)(dañoFinal * 1.5);
                System.out.println(ColorConsola.rojo("🔥 ¡Furia! Daño aumentado."));
                break;
            }
        }

        if(critico) golpesCriticosDados++; //pa las stats finales
        return dañoFinal;
    }

    // ───────────── ESTADOS ALTERADOS ─────────────

    // Aplica un estado al jugador (envenenado, furia, etc.)
    public void aplicarEstado(EstadoAlterado est){
        // Sacerdote y paladin tienen un 40% de resistir estados malos
        if(("sacerdote".equals(clase) || "paladin".equals(clase)) && new Random().nextInt(100) < 40){
            // Pero los buenos (bendecido, furia) no los resisten claro
            if(est.getTipo() != EstadoAlterado.Tipo.BENDECIDO && est.getTipo() != EstadoAlterado.Tipo.FURIA){
                System.out.println(ColorConsola.amarillo("✨ Has resistido el estado " + est.nombre() + "!"));
                return;
            }
        }

        // Si ya tiene ese mismo estado, refresco los turnos si el nuevo dura mas
        for(EstadoAlterado e : estados){
            if(e.getTipo() == est.getTipo()){
                if(est.getTurnosRestantes() > e.getTurnosRestantes()){
                    estados.remove(e); //quito el viejo pa meter el nuevo
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

    // Cada turno: aplica el efecto de los estados y baja sus turnos.
    // Si llegan a 0 los quito de la lista.
    public void procesarEstados(){
        Iterator<EstadoAlterado> it = estados.iterator();
        while(it.hasNext()){
            EstadoAlterado e = it.next();
            switch(e.getTipo()){
                case ENVENENADO, QUEMADO, SANGRADO -> {
                    // Estos te quitan vida cada turno
                    recibirDaño(e.getValor());
                    System.out.println(ColorConsola.rojo("· " + e.nombre() + " te causa " + e.getValor() + " daño"));
                }
                case BENDECIDO -> {
                    // El bendecido te cura cada turno, super util
                    int curaB = e.getValor();
                    this.vida = Math.min(this.vida + curaB, this.vidaMax);
                    System.out.println(ColorConsola.verde("· " + e.nombre() + " te cura " + curaB + " HP"));
                }
                default -> {} //los demas (aturdido, congelao, furia) no hacen daño por turno
            }
            e.reducirTurno();
            if(e.expiro()){
                System.out.println(ColorConsola.cyan("· El efecto de " + e.nombre() + " ha expirado."));
                it.remove();
            }
        }
    }

    // Si estoy aturdido o congelao, no puedo atacar este turno
    public boolean estaAturdido(){
        for(EstadoAlterado e : estados){
            if(e.getTipo() == EstadoAlterado.Tipo.ATURDIDO || e.getTipo() == EstadoAlterado.Tipo.CONGELADO){
                return true;
            }
        }
        return false;
    }

    // Suma oro al inventario y al total ganado de la partida (pa stats finales)
    public void registrarGanarOro(int o){ this.oroTotalGanado += o; this.inventario.agregarOro(o); }

    // ───────────── MOSTRAR ─────────────
    // Imprime toda la chapa del personaje, con su barra de vida y stats
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

        // Si hay estados activos los pinto al final
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
