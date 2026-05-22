package PruebasTest.practicar.JuegoRol;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

// Clase principal del juego, donde esta el main y todos los menus
public class mainRol {

    // En que oleada vamos y la dificultad elegida, las pongo static pa tirar de ellas
    // desde cualquier metodo sin pasarlas por parametro
    private static int oleadaActual = 1;
    private static int dificultadElegida = 2; // 1=Facil 2=Normal 3=Dificil 4=Pesadilla

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);

        // Intro chulo + pedir nombre + crear el jugador
        mostrarIntro();
        String nombreJ = inicioSesion(sc);
        Jugador j1 = new Jugador(nombreJ);

        // Bucle del menu principal, no se sale hasta que pulse salir
        while (true) {
            try {
                menuPrincipal();
                String linea = sc.nextLine().trim();
                int opcionPrin = Integer.parseInt(linea);

                if (opcionPrin == 1) {
                    asegurarClase(j1, sc);   // si no tiene clase, le pido que la elija
                    elegirDificultad(sc);
                    bucleAventura(j1, sc);
                } else if (opcionPrin == 2) {
                    asegurarClase(j1, sc);
                    bucleCheats(j1, sc);
                } else if (opcionPrin == 3) {
                    mostrarManual();
                } else if (opcionPrin == 4) {
                    if(j1.getClase() != null) j1.mostrarPersonaje();
                    else System.out.println(ColorConsola.amarillo("Aún no has creado tu personaje."));
                } else if (opcionPrin == 5) {
                    despedida();
                    break;
                } else {
                    System.out.println(ColorConsola.rojo("Opción inválida."));
                }
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println(ColorConsola.rojo("⚠️  Entrada inválida. Introduce un número."));
            } catch (Exception e) {
                // Por si me peta cualquier cosa no controlada, que al menos no se cierre
                System.out.println(ColorConsola.rojo("⚠️  Error inesperado: " + e.getMessage()));
            }
        }
    }

    // ───────────── BUCLE PRINCIPAL DE AVENTURA ─────────────

    // Este es el bucle del combate: va generando oleadas, ofreciendo tienda
    // entre medias y matando a los bichos hasta llegar al boss final
    private static void bucleAventura(Jugador j1, Scanner sc) throws InterruptedException {
        int vecesCura = 4; // curaciones gratis que tienes por partida
        int ulti = 3;      // ultis tambien por partida
        oleadaActual = 1;
        ArrayList<Monstruo> vivos = generarOleada(oleadaActual);

        anunciarOleada(oleadaActual, vivos);

        boolean salir = false;
        while (!salir) {
            try {
                // ¿Me he muerto? Pa fuera
                if (!j1.estaVivo()) { validarJugador(j1); return; }

                // Si no quedan bichos, o pasamos de oleada o ganamos el juego
                if (vivos.isEmpty()) {
                    if(oleadaActual >= numeroDeOleadasTotal()){
                        finalEpico(j1);
                        return;
                    }
                    oleadaActual++;
                    interOleada(j1, sc); //evento + tienda
                    vivos = generarOleada(oleadaActual);
                    anunciarOleada(oleadaActual, vivos);
                }

                // Pinto las barras de todo el mundo y muestro el menu del combate
                mostrarEstadoCombate(j1, vivos);
                menuJuego(vecesCura, ulti);
                int opcRol = Integer.parseInt(sc.nextLine().trim());

                switch (opcRol) {
                    case 1 -> turnoAtacar(j1, vivos, sc);
                    case 2 -> { j1.mostrarPersonaje(); Thread.sleep(1500); }
                    case 3 -> turnoInfoMonstruo(vivos, sc);
                    case 4 -> turnoCambiarClase(j1, sc);
                    case 5 -> vecesCura = turnoCurar(j1, vecesCura);
                    case 6 -> ulti = turnoUlti(j1, vivos, ulti, sc);
                    case 7 -> turnoInventario(j1, sc, vivos);
                    case 8 -> turnoMejorar(j1, sc);
                    case 9 -> {
                        System.out.println(ColorConsola.amarillo("Volviendo al menú principal..."));
                        salir = true;
                    }
                    default -> System.out.println(ColorConsola.rojo("⚠️  Opción inválida"));
                }

                // Al final del turno aplico estados (veneno, quemao...) y regeneraciones
                j1.procesarEstados();
                for(Monstruo m : vivos){
                    m.regenerar();
                }

                validarMonstruos(j1, vivos);
                validarJugador(j1);
                avisoVida(j1);

            } catch (NumberFormatException e) {
                System.out.println(ColorConsola.rojo("⚠️  Introduce un número válido."));
            } catch (IndexOutOfBoundsException e) {
                System.out.println(ColorConsola.rojo("❌ Opción fuera de rango."));
            }
        }
    }

    // ───────────── GENERACIÓN DE OLEADAS ─────────────

    // Cuantas oleadas hay en total segun la dificultad
    private static int numeroDeOleadasTotal(){
        return switch (dificultadElegida){
            case 1 -> 3; //facil corto
            case 2 -> 5;
            case 3 -> 6;
            case 4 -> 7; //pesadilla largo
            default -> 5;
        };
    }

    // Genera la lista de monstruos para la oleada actual
    private static ArrayList<Monstruo> generarOleada(int oleada){
        ArrayList<Monstruo> lista = new ArrayList<>();
        Random rd = new Random();
        int totalOleadas = numeroDeOleadasTotal();

        // Si es la ultima oleada, sale el boss final, sino lista normal
        if(oleada == totalOleadas){
            lista.add(Monstruo.generarBossFinal());
            return lista;
        }

        // Segun la dificultad y la oleada, voy escalando que bichos salen
        int dificEfectiva = dificultadElegida + (oleada - 1) / 2;
        ArrayList<Monstruo> pool;
        if(dificEfectiva <= 1) pool = Monstruo.generarMonstruosFaciles();
        else if(dificEfectiva == 2) pool = Monstruo.generarMonstruosNormales();
        else if(dificEfectiva == 3) pool = Monstruo.generarMonstruosDificiles();
        else pool = Monstruo.generarMonstruosPesadilla();

        // Cuantos bichos salen en esta oleada (entre 2 y 5)
        int cantidad = 2 + rd.nextInt(2) + (oleada / 2);
        if(cantidad > 5) cantidad = 5; //si no acaba siendo una matanza
        for(int i = 0; i < cantidad; i++){
            // Pillo uno al azar del pool, pero hago copia pa no compartir referencia
            Monstruo base = pool.get(rd.nextInt(pool.size()));
            Monstruo copia = new Monstruo(base.getNombre(), base.getVidaMax(), base.getAtaque(), base.getDefensa(), base.getTipo());
            copia.setPuedeRevivir(base.puedeRevivir()); //pa que el fenix conserve eso
            lista.add(copia);
        }
        return lista;
    }

    // Avisa de la oleada que toca, con cartel mas grande si es el jefe
    private static void anunciarOleada(int oleada, ArrayList<Monstruo> vivos){
        if(oleada == numeroDeOleadasTotal()){
            System.out.println(ColorConsola.rojo("\n╔═══════════════════════════════════════════════════╗"));
            System.out.println(ColorConsola.rojo("║          👹  ENFRENTAS AL JEFE FINAL  👹            ║"));
            System.out.println(ColorConsola.rojo("╚═══════════════════════════════════════════════════╝"));
            // Pinto el dibujito del boss
            for(Monstruo m : vivos){
                if(!m.getArteAscii().isEmpty()) System.out.println(m.getArteAscii());
            }
        } else {
            System.out.println(ColorConsola.cyan("\n╔═══════════════════════════════════════════════════╗"));
            System.out.println(ColorConsola.cyan("║          🌊 OLEADA " + oleada + " / " + numeroDeOleadasTotal() + " 🌊                          ║"));
            System.out.println(ColorConsola.cyan("╚═══════════════════════════════════════════════════╝"));
            System.out.print(" Aparecen: ");
            for(int i = 0; i < vivos.size(); i++){
                System.out.print(ColorConsola.rojo(vivos.get(i).getNombre()));
                if(i < vivos.size() - 1) System.out.print(", ");
            }
            System.out.println();
        }
    }

    // Entre oleadas: posible evento aleatorio + opcion de ir a la tienda
    private static void interOleada(Jugador j, Scanner sc){
        System.out.println(ColorConsola.verde("\n🎉 ¡Has limpiado esta oleada!"));
        Random rd = new Random();
        // 50% de que salga un evento aleatorio (cofres, trampas, genios...)
        if(rd.nextInt(100) < 50){
            Evento.lanzarEventoAleatorio(j);
        }
        // Y siempre se le ofrece la tienda
        System.out.println(ColorConsola.amarillo("\n¿Quieres visitar la tienda antes de la siguiente oleada? (s/n): "));
        String resp = sc.nextLine().trim().toLowerCase();
        if(resp.startsWith("s")){
            Tienda.abrir(j, sc);
        }
    }

    // Pinta las barras de vida de todos (jugador + bichos) pa que se vea el estado
    private static void mostrarEstadoCombate(Jugador j, ArrayList<Monstruo> vivos){
        ColorConsola.separador();
        ColorConsola.barraVida(j.getNombre() + " (" + j.getClase() + " Lv" + j.getNivel() + ")", j.getVida(), j.getVidaMax());
        System.out.println();
        for(Monstruo m : vivos){
            m.mostrarBarraCorta();
        }
        ColorConsola.separador();
    }

    // ───────────── ACCIONES DE TURNO ─────────────

    // Atacar: elige bicho, le casca daño, y si sobrevive le contraatacan
    private static void turnoAtacar(Jugador j, ArrayList<Monstruo> vivos, Scanner sc){
        // Si estoy aturdido o congelao, paso turno pero ellos sí me dan
        if(j.estaAturdido()){
            System.out.println(ColorConsola.morado("💫 No puedes atacar este turno — estás incapacitado."));
            contraAtaqueMonstruos(j, vivos);
            return;
        }
        monstruosEleccion(vivos);
        int atacMons = Integer.parseInt(sc.nextLine().trim()) - 1;
        if (atacMons >= 0 && atacMons < vivos.size()){
            int daño = j.atacar();
            System.out.println(ColorConsola.amarillo("💥 Daño producido: " + daño));
            vivos.get(atacMons).recibirDaño(daño);
            ColorConsola.barraVida(vivos.get(atacMons).getNombre(), vivos.get(atacMons).getVida(), vivos.get(atacMons).getVidaMax());

            // ¿Lo he matao?
            if (!vivos.get(atacMons).estaVivo()) {
                // Que el fenix oscuro intente revivir antes de quitarlo de la lista
                if(vivos.get(atacMons).intentarRevivir()){
                    // sigue vivo, asi que no lo borro
                } else {
                    matarMonstruo(j, vivos, atacMons);
                    return; //ya no contraatacan los muertos
                }
            }
            contraAtaqueMonstruos(j, vivos);
        } else {
            System.out.println(ColorConsola.rojo("Objetivo inválido."));
        }
    }

    // Contraataque: cada bicho vivo tiene un 65% de pegarte. Si esquivo, ninguno me toca.
    private static void contraAtaqueMonstruos(Jugador j, ArrayList<Monstruo> vivos){
        if(!j.esquivar()){
            Random rd = new Random();
            for(Monstruo m : vivos){
                if(rd.nextInt(100) < 65){
                    // La defensa reduce el daño un porcentaje (con tope al 60% pa que no sea inmortal)
                    double factorDef = j.getDefensa() * 0.04;
                    if(factorDef > 0.6) factorDef = 0.6;
                    int dañoOg = m.atacar();
                    int dañoReducido = (int)(dañoOg * (1 - factorDef));
                    if(dañoReducido < 1) dañoReducido = 1;

                    j.recibirDaño(dañoReducido);
                    System.out.println(ColorConsola.rojo("👹 " + m.getNombre() + " te golpea por " + dañoReducido + " daño"));

                    // El bicho puede intentar aplicar un estado al pegarte
                    EstadoAlterado est = m.intentarAplicarEstado();
                    if(est != null) j.aplicarEstado(est);

                    if(!j.estaVivo()) return; //si me has matao, paro ya
                }
            }
        } else {
            System.out.println(ColorConsola.cyan("🌀 ¡Esquivaste todos los contraataques!"));
        }
    }

    // Le da xp y oro al jugador, lo quita de la lista y sube la racha
    private static void matarMonstruo(Jugador j, ArrayList<Monstruo> vivos, int idx){
        Monstruo muerto = vivos.get(idx);
        System.out.println(ColorConsola.verde("\n💀 ¡Has derrotado a " + muerto.getNombre() + "!"));
        j.ganarExperiencia(muerto.getXpAlMorir());
        int oro = muerto.getOroAlMorir() + new Random().nextInt(15); //le sumo un extra random pa que no sea fijo
        j.registrarGanarOro(oro);
        System.out.println(ColorConsola.amarillo("💰 +" + oro + " oro"));
        j.incrementarMonstruosDerrotados();
        vivos.remove(idx);
    }

    // Muestra info detallada de un monstruo (la opcion de "espiar")
    private static void turnoInfoMonstruo(ArrayList<Monstruo> vivos, Scanner sc) throws InterruptedException {
        if(vivos.isEmpty()){
            System.out.println(ColorConsola.cyan("No hay monstruos vivos."));
            return;
        }
        monstruosEleccion(vivos);
        int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
        if(idx >= 0 && idx < vivos.size()){
            vivos.get(idx).mostrarMonstruo();
            Thread.sleep(1500);
        }
    }

    // Cambiar de clase a mitad de combate (solo cambia atk y def, no la vida)
    private static void turnoCambiarClase(Jugador j, Scanner sc){
        System.out.println(ColorConsola.amarillo("⚠️  Cambiar de clase cambia tu ataque y defensa, pero conservas la vida actual."));
        mostrarClases();
        int op = Integer.parseInt(sc.nextLine().trim());
        j.asignarClasePorNumeroMedioPartida(op);
        System.out.println(ColorConsola.verde("\n→ Clase: " + j.getClase().toUpperCase() + ". ¡Buena suerte!"));
    }

    // Usar una de las curaciones gratis (4 por partida)
    private static int turnoCurar(Jugador j, int vecesCura) throws InterruptedException {
        if(vecesCura > 0){
            j.curacionJugador();
            vecesCura--;
            System.out.println(ColorConsola.cyan("♻️  Curaciones restantes: " + vecesCura));
        } else {
            System.out.println(ColorConsola.rojo("❌ Ya no tienes curaciones disponibles."));
            Thread.sleep(800);
        }
        return vecesCura;
    }

    // Tirar la ultimate sobre un bicho (3 por partida)
    private static int turnoUlti(Jugador j, ArrayList<Monstruo> vivos, int ulti, Scanner sc){
        if(ulti <= 0){
            System.out.println(ColorConsola.rojo("❌ No te quedan ultimates."));
            return ulti;
        }
        if(vivos.isEmpty()){
            System.out.println(ColorConsola.cyan("No hay objetivos."));
            return ulti;
        }
        System.out.println(ColorConsola.morado("⚡ ULTIs restantes: " + ulti));
        monstruosEleccion(vivos);
        int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
        if(idx >= 0 && idx < vivos.size()){
            j.ultimateTiro(vivos, idx);
            if(!vivos.get(idx).estaVivo()){
                // Por si lo mato y resulta ser un Fenix
                if(!vivos.get(idx).intentarRevivir()){
                    matarMonstruo(j, vivos, idx);
                }
            }
            ulti--;
        }
        return ulti;
    }

    // Abre el inventario y deja usar un objeto
    private static void turnoInventario(Jugador j, Scanner sc, ArrayList<Monstruo> vivos){
        j.getInventario().mostrar();
        if(j.getInventario().getObjetos().isEmpty()){
            return;
        }
        System.out.print("Elige objeto a usar (0 cancelar): ");
        int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
        if(idx < 0) return; //cancelo
        if(idx >= j.getInventario().getObjetos().size()){
            System.out.println(ColorConsola.rojo("Objeto inválido."));
            return;
        }
        Objeto o = j.getInventario().getObjetos().get(idx);
        usarObjeto(j, o, sc, vivos);

        // Si era de un solo uso lo quito, sino le bajo los usos restantes
        if(o.getUsosRestantes() <= 1){
            j.getInventario().sacar(idx);
        } else {
            o.consumirUso();
        }
    }

    // Hace el efecto del objeto segun el tipo (pocion, arma, bomba...)
    private static void usarObjeto(Jugador j, Objeto o, Scanner sc, ArrayList<Monstruo> vivos){
        String tipo = o.getTipo().toLowerCase();
        switch(tipo){
            case "pocion", "comida" -> {
                // Pociones y comida curan HP
                int cura = o.getCuracion();
                int antes = j.getVida();
                j.setVida(j.getVida() + cura);
                int real = j.getVida() - antes;
                System.out.println(ColorConsola.verde("💚 " + o.getNombre() + " te cura " + real + " HP."));
            }
            case "arma" -> {
                // Las armas se "equipan" sumando ataque permanente
                j.setAtaque(j.getAtaque() + o.getBonusAtaque());
                System.out.println(ColorConsola.amarillo("⚔️  Equipas " + o.getNombre() + " (+" + o.getBonusAtaque() + " ataque)"));
            }
            case "armadura" -> {
                // Las armaduras lo mismo pero sumando defensa
                j.setDefensa(j.getDefensa() + o.getBonusDefensa());
                System.out.println(ColorConsola.cyan("🛡️  Equipas " + o.getNombre() + " (+" + o.getBonusDefensa() + " defensa)"));
            }
            case "amuleto" -> {
                // Los amuletos suelen tener atk y def
                j.setAtaque(j.getAtaque() + o.getBonusAtaque());
                j.setDefensa(j.getDefensa() + o.getBonusDefensa());
                System.out.println(ColorConsola.morado("🔮 Te equipas " + o.getNombre()));
            }
            case "bomba" -> {
                // Las bombas le pegan daño directo al bicho que elija
                if(vivos.isEmpty()){
                    System.out.println(ColorConsola.cyan("No hay enemigos a quien lanzar la bomba."));
                    return;
                }
                monstruosEleccion(vivos);
                int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
                if(idx >= 0 && idx < vivos.size()){
                    System.out.println(ColorConsola.rojo("💣 ¡Lanzas " + o.getNombre() + " a " + vivos.get(idx).getNombre() + "!"));
                    vivos.get(idx).recibirDaño(o.getPotenciadorDaño());
                    if(!vivos.get(idx).estaVivo()){
                        if(!vivos.get(idx).intentarRevivir()){
                            matarMonstruo(j, vivos, idx);
                        }
                    }
                }
            }
            case "pergamino" -> {
                // Los pergaminos aplican un estado: el de furia o el de bendicion
                if(o.getNombre().toLowerCase().contains("furia")){
                    j.aplicarEstado(new EstadoAlterado(EstadoAlterado.Tipo.FURIA, 3, 0));
                } else {
                    j.aplicarEstado(new EstadoAlterado(EstadoAlterado.Tipo.BENDECIDO, 3, 10));
                }
            }
            default -> System.out.println(ColorConsola.cyan("Objeto inutilizable en este momento."));
        }
    }

    // Mejorar atributos con los puntos de habilidad que te dan al subir nivel
    private static void turnoMejorar(Jugador j, Scanner sc){
        if(j.getPuntosHabilidad() <= 0){
            System.out.println(ColorConsola.cyan("No tienes puntos de habilidad. Sube de nivel para ganar."));
            return;
        }
        System.out.println(ColorConsola.amarillo("\n══ MEJORAR ATRIBUTOS ══"));
        System.out.println("Puntos disponibles: " + j.getPuntosHabilidad());
        System.out.println("  1. +20 Vida máxima");
        System.out.println("  2. +5 Ataque");
        System.out.println("  3. +4 Defensa");
        System.out.println("  0. Volver");
        System.out.print("Elige: ");
        int op = Integer.parseInt(sc.nextLine().trim());
        if(op >= 1 && op <= 3) j.gastarPuntoHabilidad(op);
    }

    // ───────────── MENÚS ─────────────

    // Cartelaco de bienvenida que sale al arrancar el juego
    private static void mostrarIntro() throws InterruptedException {
        System.out.println(ColorConsola.morado("\n╔══════════════════════════════════════════════════════════════╗"));
        System.out.println(ColorConsola.morado("║                                                              ║"));
        System.out.println(ColorConsola.morado("║   ███╗   ███╗██╗███╗   ██╗██╗██████╗ ██████╗  ██████╗        ║"));
        System.out.println(ColorConsola.morado("║   ████╗ ████║██║████╗  ██║██║██╔══██╗██╔══██╗██╔════╝        ║"));
        System.out.println(ColorConsola.morado("║   ██╔████╔██║██║██╔██╗ ██║██║██████╔╝██████╔╝██║  ███╗       ║"));
        System.out.println(ColorConsola.morado("║   ██║╚██╔╝██║██║██║╚██╗██║██║██╔══██╗██╔═══╝ ██║   ██║       ║"));
        System.out.println(ColorConsola.morado("║   ██║ ╚═╝ ██║██║██║ ╚████║██║██║  ██║██║     ╚██████╔╝       ║"));
        System.out.println(ColorConsola.morado("║   ╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝╚═╝╚═╝  ╚═╝╚═╝      ╚═════╝        ║"));
        System.out.println(ColorConsola.morado("║                                                              ║"));
        System.out.println(ColorConsola.amarillo("║          ⚔️    Edición Mejorada — JavaConsole    ⚔️           ║"));
        System.out.println(ColorConsola.morado("╚══════════════════════════════════════════════════════════════╝"));
        Thread.sleep(400);
    }

    public static void menuPrincipal() {
        System.out.println(ColorConsola.cyan("\n══════════════ MENÚ PRINCIPAL ══════════════"));
        System.out.println("  1. 🎮 Entrar al juego");
        System.out.println("  2. 🧪 Menú de trampas / cheats");
        System.out.println("  3. 📖 Manual del juego");
        System.out.println("  4. 📊 Ver mi personaje");
        System.out.println("  5. 🚪 Salir");
        System.out.print("Opción: ");
    }

    public static void menuJuego(int curasRestantes, int ultiRestantes) {
        System.out.println(ColorConsola.cyan("\n══════════════ MENÚ JUEGO ══════════════"));
        System.out.println("  1. ⚔️  Atacar monstruo");
        System.out.println("  2. 📊 Ver estadísticas del jugador");
        System.out.println("  3. 👁️  Ver estadísticas de un monstruo");
        System.out.println("  4. 🔁 Cambiar de clase");
        System.out.println("  5. 💊 Curación (restantes: " + curasRestantes + ")");
        System.out.println("  6. 🌟 ULTIMATE (restantes: " + ultiRestantes + ")");
        System.out.println("  7. 🎒 Inventario");
        System.out.println("  8. ⭐ Mejorar atributos");
        System.out.println("  9. 🚪 Salir del combate");
        System.out.print("Opción: ");
    }

    public static void mostrarClases() {
        System.out.println(ColorConsola.morado("\n══ 🧙 CLASES DISPONIBLES 🧙 ══"));
        System.out.println("  1. 🛡️  Guerrero (fácil)        ⭐  - Mucha vida, golpe devastador");
        System.out.println("  2. 🔮 Mago (medio)            ⭐⭐  - Alto daño mágico");
        System.out.println("  3. 🏹 Arquero (medio)         ⭐⭐  - Críticos brutales y headshots");
        System.out.println("  4. ✝️  Sacerdote (difícil)    ⭐⭐⭐  - Curaciones potentes, resistente");
        System.out.println("  5. 🔗 Esclavo (extremo)       ⭐⭐⭐⭐ - Esquiva alta, baja vida");
        System.out.println("  6. 🥷 Ninja (medio)           ⭐⭐  - Doble ataque, asesinato");
        System.out.println("  7. 🩸 Vampiro (medio)         ⭐⭐  - Roba vida al atacar");
        System.out.println("  8. ⚜️  Paladín (fácil)        ⭐   - Escudo divino, golpe sagrado");
        System.out.println("  9. 🐺 Druida (medio)          ⭐⭐  - Transformación animal");
        System.out.println(" 10. 💀 Nigromante (difícil)   ⭐⭐⭐  - Invoca esqueletos");
        System.out.print("Opción: ");
    }

    // Pinta la lista de bichos disponibles para elegir cual atacas
    public static void monstruosEleccion(ArrayList<Monstruo> vivos) {
        System.out.println(ColorConsola.rojo("\n👹 Monstruos disponibles 👹"));
        for (int i = 0; i < vivos.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + vivos.get(i).getNombre() +
                    "  (" + vivos.get(i).getVida() + "/" + vivos.get(i).getVidaMax() + " HP)");
        }
        System.out.print("Objetivo: ");
    }

    // Menu de los cheats, pa probar cosas o pa el que sea ya muy malo jugando
    public static void cheatMenu() {
        System.out.println(ColorConsola.amarillo("\n══════════════ 🧪 CHEATS 🧪 ══════════════"));
        System.out.println("  1. Aumentar vida");
        System.out.println("  2. Aumentar ataque");
        System.out.println("  3. Aumentar defensa");
        System.out.println("  4. Aumentar oro (+1000)");
        System.out.println("  5. Subir 5 niveles");
        System.out.println("  6. Curar al máximo");
        System.out.println("  7. Aumentar TODO! (mega cheat)");
        System.out.println("  8. Volver");
        System.out.print("Opción: ");
    }

    // Bucle del menu de cheats
    private static void bucleCheats(Jugador j1, Scanner sc){
        while (true) {
            cheatMenu();
            try{
                int cheatOp = Integer.parseInt(sc.nextLine().trim());
                if(cheatOp == 8) return;

                if(cheatOp == 1){
                    System.out.print("Aumento de vida máxima: ");
                    int x = Integer.parseInt(sc.nextLine().trim());
                    j1.setVidaMax(j1.getVidaMax() + x);
                    j1.setVida(j1.getVida() + x);
                    System.out.println(ColorConsola.verde("🧡 Vida: " + j1.getVida() + "/" + j1.getVidaMax()));
                } else if(cheatOp == 2){
                    System.out.print("Aumento de ataque: ");
                    int x = Integer.parseInt(sc.nextLine().trim());
                    j1.setAtaque(j1.getAtaque() + x);
                } else if(cheatOp == 3){
                    System.out.print("Aumento de defensa: ");
                    int x = Integer.parseInt(sc.nextLine().trim());
                    j1.setDefensa(j1.getDefensa() + x);
                } else if(cheatOp == 4){
                    j1.getInventario().agregarOro(1000);
                    System.out.println(ColorConsola.amarillo("💰 +1000 oro"));
                } else if(cheatOp == 5){
                    // Tramposo: te subo 5 niveles de golpe metiendote la xp justa pa cada uno
                    for(int i=0;i<5;i++) j1.ganarExperiencia(j1.getExperienciaSiguienteNivel());
                } else if(cheatOp == 6){
                    j1.setVida(j1.getVidaMax());
                    System.out.println(ColorConsola.verde("Vida al máximo."));
                } else if(cheatOp == 7){
                    // El cheat definitivo, te pone OP
                    j1.setVidaMax(j1.getVidaMax() + 200);
                    j1.setVida(j1.getVidaMax());
                    j1.setAtaque(j1.getAtaque() + 50);
                    j1.setDefensa(j1.getDefensa() + 30);
                    j1.getInventario().agregarOro(2000);
                    System.out.println(ColorConsola.morado("👾 MEGA CHEAT aplicado."));
                } else {
                    System.out.println(ColorConsola.rojo("Opción inválida."));
                }
            } catch(NumberFormatException e){
                System.out.println(ColorConsola.rojo("Introduce un número."));
            }
        }
    }

    // Manualillo rapido pa que el que entre nuevo no se pierda
    private static void mostrarManual(){
        System.out.println(ColorConsola.cyan("\n╔══════════════ MANUAL DE JUEGO ══════════════╗"));
        System.out.println("│ Objetivo: Sobrevive todas las oleadas y derrota");
        System.out.println("│ al Señor Oscuro Malakar.");
        System.out.println("│");
        System.out.println("│ ▶ Gana XP y oro derrotando monstruos.");
        System.out.println("│ ▶ Sube de nivel para mejorar tus atributos.");
        System.out.println("│ ▶ Visita la tienda entre oleadas para comprar.");
        System.out.println("│ ▶ Cada clase tiene golpes especiales únicos.");
        System.out.println("│ ▶ Cuidado con los estados alterados (veneno,");
        System.out.println("│   quemado, congelado, aturdido, sangrado…)");
        System.out.println("│ ▶ Los eventos aleatorios pueden ayudarte o ");
        System.out.println("│   perjudicarte. ¡Hay un genio milagroso!");
        System.out.println("│ ▶ El Boss Final tiene 450 HP — prepárate bien.");
        System.out.println(ColorConsola.cyan("╚══════════════════════════════════════════════╝"));
    }

    // Pide el nombre. Si pasa, le pone Rodolfo y a tomar viento.
    public static String inicioSesion(Scanner sc){
        System.out.println(ColorConsola.cyan("\n══════════════════════════════════════════"));
        System.out.println(ColorConsola.amarillo("           📌 INICIO DE SESIÓN"));
        System.out.println(ColorConsola.cyan("══════════════════════════════════════════"));
        System.out.print("Nombre: ");
        String nombreJ = sc.nextLine().trim();
        if(!nombreJ.isEmpty()) {
            System.out.println(ColorConsola.verde("✅ Bienvenido, " + nombreJ + "! Prepara tu aventura..."));
            return nombreJ;
        }
        System.out.println(ColorConsola.amarillo("⛔ No has iniciado sesión con un nombre, te llamarás Rodolfo."));
        return "Rodolfo";
    }

    // Si el jugador no tiene clase, le obliga a elegir antes de entrar al combate
    private static void asegurarClase(Jugador j, Scanner sc){
        if(j.getClase() == null || j.getClase().equals("null")){
            mostrarClases();
            int op = Integer.parseInt(sc.nextLine().trim());
            j.asignarClasePorNumero(op);
            System.out.println(ColorConsola.verde("\n→ Clase: " + j.getClase().toUpperCase() + ". ¡Buena suerte!"));
        }
    }

    // Le pide al jugador la dificultad antes de empezar la aventura
    private static void elegirDificultad(Scanner sc){
        System.out.println(ColorConsola.amarillo("\n══ DIFICULTAD ══"));
        System.out.println("  1. 🍼 Fácil");
        System.out.println("  2. ⚔️  Normal");
        System.out.println("  3. 💀 Difícil");
        System.out.println("  4. 🔥 Pesadilla");
        System.out.print("Elige: ");
        try {
            int d = Integer.parseInt(sc.nextLine().trim());
            if(d >= 1 && d <= 4) dificultadElegida = d;
            else dificultadElegida = 2; //si pone cualquier mierda, normal
        } catch (NumberFormatException e){
            dificultadElegida = 2;
        }
        System.out.println(ColorConsola.cyan("Dificultad establecida en " + dificultadElegida));
    }

    // ───────────── Validaciones y finales ─────────────

    // Antes esto cortaba el juego al matarlos a todos. Ahora con oleadas ya no
    // hace falta cortar aqui, dejo el metodo por si lo necesito mas adelante.
    public static void validarMonstruos(Jugador j, ArrayList<Monstruo> m) {
        // Nada, ahora la victoria final la maneja finalEpico() al pasar el boss
    }

    // Si la vida llega a 0 -> game over con pantalla de derrota y stats
    public static void validarJugador(Jugador j) {
        if (j.getVida() == 0) {
            System.out.println(ColorConsola.rojo("\n████████████████████████████████████████████████████"));
            System.out.println(ColorConsola.rojo("█                  ☠️  DERROTA ☠️                   █"));
            System.out.println(ColorConsola.rojo("████████████████████████████████████████████████████"));
            System.out.println("Has caído tras llegar a la oleada " + oleadaActual + ".");
            System.out.println("Monstruos derrotados: " + j.getMonstruosDerrotados());
            System.out.println("Oro acumulado: " + j.getOroTotalGanado());
            System.out.println("Críticos dados: " + j.getGolpesCriticosDados());
            System.exit(0);
        }
    }

    // Pantalla de victoria al matar al boss final, con resumen de partida
    private static void finalEpico(Jugador j) throws InterruptedException {
        System.out.println(ColorConsola.amarillo("\n████████████████████████████████████████████████████████████"));
        System.out.println(ColorConsola.verde("█                                                          █"));
        System.out.println(ColorConsola.verde("█        🏆 ¡HAS DERROTADO AL SEÑOR OSCURO MALAKAR! 🏆     █"));
        System.out.println(ColorConsola.verde("█                                                          █"));
        System.out.println(ColorConsola.amarillo("█           El reino vuelve a vivir en paz...              █"));
        System.out.println(ColorConsola.amarillo("█           Tu nombre será recordado por siempre.          █"));
        System.out.println(ColorConsola.verde("█                                                          █"));
        System.out.println(ColorConsola.amarillo("████████████████████████████████████████████████████████████"));
        Thread.sleep(700);
        System.out.println("\n📜 ESTADÍSTICAS FINALES");
        System.out.println("  • Nivel alcanzado: " + j.getNivel());
        System.out.println("  • Clase: " + j.getClase());
        System.out.println("  • Monstruos derrotados: " + j.getMonstruosDerrotados());
        System.out.println("  • Oro acumulado: " + j.getOroTotalGanado());
        System.out.println("  • Críticos dados: " + j.getGolpesCriticosDados());
        System.out.println("  • Mejor racha: " + j.getRachaVictorias());
        System.out.println(ColorConsola.cyan("\n  🌟 ¡Gracias por jugar! 🌟"));
        System.exit(0);
    }

    // Mensajito cuando sales del juego
    private static void despedida(){
        System.out.println(ColorConsola.cyan("\n═══════════════════════════════════════"));
        System.out.println(ColorConsola.amarillo("    Hasta la próxima, aventurero..."));
        System.out.println(ColorConsola.cyan("═══════════════════════════════════════"));
    }

    // Avisa al jugador cuando tiene poca vida pa que se cure (a veces se pasa uno)
    public static void avisoVida(Jugador j){
        if(j.estaVivo() && j.getVida() < (j.getVidaMax() / 4)){
            System.out.println(ColorConsola.rojo("⚠️  ¡VIDA CRÍTICA! ¡Cúrate cuanto antes!"));
        }
    }
}
