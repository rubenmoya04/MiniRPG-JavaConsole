package PruebasTest.practicar.JuegoRol;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class mainRol {

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        String nombreJ = inicioSesion(sc);
        // Jugador
        Jugador j1 = new Jugador(nombreJ);

        // ─────── Monstruos disponibles ───────
        Monstruo m1 = new Monstruo("Esqueleto", 50, 15, 5, "No muerto");
        Monstruo m2 = new Monstruo("Ogro", 100, 20, 10, "Bestia");
        Monstruo m3 = new Monstruo("Troll", 120, 18, 15, "Gigante");
        Monstruo m4 = new Monstruo("Dragón", 200, 30, 25, "Dragón");
        Monstruo m5 = new Monstruo("Zombie", 80, 12, 8, "No muerto");

        ArrayList<Monstruo> monstruos = new ArrayList<>();
        monstruos.add(m1);
        monstruos.add(m2);
        monstruos.add(m3);
        monstruos.add(m4);
        monstruos.add(m5);

        ArrayList<Monstruo> vivos = new ArrayList<>();
        for(Monstruo m : monstruos){
            if(m.estaVivo()){
                vivos.add(m);
            }
        }
        int vecesCura = 4;
        int ulti = 3;

        while (true) {
            try{
                menuPrincipal();
                int opcionPrin = sc.nextInt();

                if (opcionPrin == 1) {
                    String clase = j1.getClase();
                    if (clase == null || clase.equals("null")) {
                        mostrarClases();
                        int elecClase = sc.nextInt();
                        j1.asignarClasePorNumero(elecClase);
                        System.out.println("\n-- Clase seleccionada: " + j1.getClase().toUpperCase() + " ¡Buena Suerte!\n");
                    }
                    boolean salir = false;

                    // Dentro del juego
                    while (!salir) {
                        menuJuego();
                        int opcRol = sc.nextInt();
                        switch (opcRol) {
                            case 1 -> {

                                monstruosEleccion(vivos);
                                int atacMons = sc.nextInt() - 1;
                                if (atacMons >= 0 && atacMons < vivos.size()){
                                    int daño = j1.atacar();
                                    System.out.println("- Daño producido: " + daño + " 💢");
                                    vivos.get(atacMons).recibirDaño(daño);
                                    boolean esquivar = j1.esquivar();

                                    if (!esquivar){
                                        if (devolverDañoMonst()) {
                                            double factorDef = j1.getDefensa() * 0.04;
                                            if(factorDef > 0.6) factorDef = 0.6;
                                            int dañoOg = vivos.get(atacMons).atacar();
                                            int dañoReducido = (int) (dañoOg * (1- factorDef));
                                            if(dañoReducido < 1) dañoReducido = 1;

                                            j1.recibirDaño(dañoReducido);

                                            System.out.println("\n** " + vivos.get(atacMons).getNombre() + " te ha devuelto un golpe de " + dañoReducido + " de daño **");
                                            System.out.println("❤️ Vida Jugador: " + j1.getVida() + " ❤❤❤");
                                            validarJugador(j1);
                                        }

                                    }

                                    System.out.println("💀 Vida de " + vivos.get(atacMons).getNombre() + ": " + vivos.get(atacMons).getVida() + " ❤❤❤");

                                    if (!vivos.get(atacMons).estaVivo()) {
                                        System.out.println("\n=== Monstruo " + vivos.get(atacMons).getNombre() + " ha muerto! ===\n");
                                        vivos.remove(atacMons);
                                    }

                                    validarMonstruos(vivos);
                                    validarJugador(j1);
                                }


                            }
                            case 2 -> {
                                j1.mostrarPersonaje();
                                Thread.sleep(3000);
                            }
                            case 3 -> {
                                monstruosEleccion(vivos);
                                int infoMons = sc.nextInt();
                                monstruos.get(infoMons - 1).mostrarMonstruo();
                                Thread.sleep(3000);
                            }
                            case 4 -> {
                                System.out.println("⚠️  NO PUEDES CAMBIAR VIDA, SOLO CLASE.");
                                mostrarClases();
                                int elecClase = sc.nextInt();
                                j1.asignarClasePorNumeroMedioPartida(elecClase);
                                System.out.println("\n-- Clase seleccionada: " + j1.getClase().toUpperCase() + " ¡Buena Suerte!\n");
                            }
                            case 5 -> {
                                if (vecesCura > 0) {
                                    j1.curacionJugador();
                                    vecesCura--;
                                    System.out.println("♻ Curaciones restantes: " + vecesCura);
                                } else {
                                    System.out.println("\n******** YA NO TIENES MÁS CURACIONES, ¡BUENA SUERTE! ********");
                                    Thread.sleep(2000);
                                }
                            }
                            case 6 -> {
                                validarMonstruos(vivos);
                                System.out.println("❕ INFO: SOLO HAY 3 ULTI POR PARTIDA ❕");
                                if (ulti > 0) {
                                    monstruosEleccion(vivos);
                                    int ultiMons = sc.nextInt() - 1;
                                    j1.ultimateTiro(vivos, ultiMons);
                                    ulti--;
                                    System.out.println("-- TE QUEDAN " + ulti + " ultis --");
                                }
                            }
                            case 7 -> {
                                System.out.println("Saliendo al menú principal...");
                                salir = true;
                            }
                            default -> System.out.println("⚠️  Opción inválida");
                        }
                        validarMonstruos(vivos);
                        avisoVida(j1);

                    }
                }

                if (opcionPrin == 2) {
                    while (true) {
                        String clase = j1.getClase();
                        if (clase == null || clase.equals("null")) {
                            mostrarClases();
                            int elecClase = sc.nextInt();
                            j1.asignarClasePorNumero(elecClase);
                            System.out.println("\n-- Clase seleccionada: " + j1.getClase().toUpperCase() + " ¡Buena Suerte!\n");
                        }

                        cheatMenu();
                        int cheatOp = sc.nextInt();
                        int Vidaog = j1.getVida();
                        int dañoOG = j1.getAtaque();
                        int defOG = j1.getDefensa();

                        if (cheatOp == 1) {
                            System.out.print("\nNombre jugador: " + j1.getNombre() + "\nAumento de vida: ");
                            int aumentoCheat = sc.nextInt();
                            j1.setVida(aumentoCheat + Vidaog);
                            System.out.println("🧡  Vida jugador actualizado a: " + j1.getVida());
                        }

                        if (cheatOp == 2) {
                            System.out.print("\nNombre jugador: " + j1.getNombre() + "\nAumento de ataque: ");
                            int aumentoCheat = sc.nextInt();
                            j1.setAtaque(aumentoCheat + dañoOG);
                            System.out.println("⚔️  Daño jugador actualizado a: " + j1.getAtaque());
                        }
                        if (cheatOp == 3) {
                            System.out.print("\nNombre jugador: " + j1.getNombre() + "\nAumento de defensa: ");
                            int aumentoCheat = sc.nextInt();
                            j1.setDefensa(aumentoCheat + dañoOG);
                            System.out.println("⚔️  Defensa jugador actualizado a: " + j1.getDefensa());
                        }
                        if (cheatOp == 4) {
                            System.out.print("\nNombre jugador: " + j1.getNombre() + "\nAumentos de ULTI: ");
                            int aumentoCheat = sc.nextInt();
                            ulti += aumentoCheat;
                            System.out.println("⚔️  Ultimates actualizado a: " + ulti);
                        }

                        if (cheatOp == 5) {
                            System.out.print("\nNombre jugador: " + j1.getNombre() + "\nAumento para todo: ");
                            int aumentoCheat = sc.nextInt();
                            j1.setDefensa(aumentoCheat + defOG);
                            j1.setAtaque(aumentoCheat + dañoOG);
                            j1.setVida(aumentoCheat);
                            System.out.println("👾  Atributos actualizado a: " + aumentoCheat);
                        }
                        if (cheatOp == 6) {
                            break;
                        }
                    }
                }

                if (opcionPrin == 3) {
                    System.out.println("\nFinalizando juego...\n");
                    break;
                }
            }catch (InputMismatchException e) {
                System.out.println("⚠️ Entrada inválida. Por favor, ingresa un número.");
                sc.nextLine(); // limpiar buffer
            } catch (IndexOutOfBoundsException e) {
                System.out.println("❌ Opción fuera de rango: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("⚠️ Ha ocurrido un error inesperado: " + e.getMessage());
            }

        }
    }

    //  Menús del juego

    public static void menuPrincipal() {
        System.out.println("\n════════════════ MENÚ PRINCIPAL ════════════════");
        System.out.println("  1. Entrar al juego");
        System.out.println("  2. Cambiar valores (cheat)");
        System.out.println("  3. Salir");
        System.out.print("Opción: ");
    }

    public static void menuJuego() {
        System.out.println("══════════════ MENÚ JUEGO ROL ══════════════");
        System.out.println("  1. ⚔️  Atacar monstruo");
        System.out.println("  2. 📊 Ver estadísticas del jugador");
        System.out.println("  3. 👁️ Ver estadísticas de un monstruo");
        System.out.println("  4. 🔁 Cambiar de clase");
        System.out.println("  5. 💊 Curación para personaje");
        System.out.println("  6. 🌟 Tirar ULTIMATE / TIRO CARGADO");
        System.out.println("  7. 🚪 Salir del combate");

        System.out.print("Opción: ");
    }

    public static void mostrarClases() {
        System.out.println("\n══ 🧙‍♂️ CLASES DISPONIBLES 🌩 ══");
        System.out.println("  1. 🛡️ Guerrero (fácil)         ⭐");
        System.out.println("  2. 🔮 Mago (medio)             ⭐⭐");
        System.out.println("  3. 🏹 Arquero (medio)          ⭐⭐");
        System.out.println("  4. ✝️ Sacerdote (difícil)      ⭐⭐⭐");
        System.out.println("  5. 🔗 Esclavo (extremo)        ⭐⭐⭐⭐");

        System.out.print("Opción: ");
    }

    public static void monstruosEleccion(ArrayList<Monstruo> vivos) {
        System.out.println("\n👹 Monstruos disponibles 👹");
        int i;
        for (i = 0; i < vivos.size(); i++) {
            System.out.println((i + 1) + ". " + vivos.get(i).getNombre() + " - (" + vivos.get(i).getVida() + " HP 🧡)");
        }
        //System.out.println(i+1 + " OCULTO - " + "(???) HP");

        System.out.print("Opción: ");
    }

    public static void cheatMenu() {
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("  /$$$$$$  /$$   /$$ /$$$$$$$$  /$$$$$$  /$$$$$$$$");
        System.out.println(" /$$__  $$| $$  | $$| $$_____/ /$$__  $$|__  $$__/");
        System.out.println("| $$  \\__/| $$  | $$| $$      | $$  \\ $$   | $$   ");
        System.out.println("| $$      | $$$$$$$$| $$$$$   | $$$$$$$$   | $$   ");
        System.out.println("| $$      | $$__  $$| $$__/   | $$__  $$   | $$   ");
        System.out.println("| $$    $$| $$  | $$| $$      | $$  | $$   | $$   ");
        System.out.println("|  $$$$$$/| $$  | $$| $$$$$$$$| $$  | $$   | $$   ");
        System.out.println(" \\______/ |__/  |__/|________/|__/  |__/   |__/   ");
        System.out.println("                                                  ");
        System.out.println("               MENÚ CHEAT 🧪");
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("  1. Aumentar vida");
        System.out.println("  2. Aumentar ataque");
        System.out.println("  3. Aumentar defensa");
        System.out.println("  4. Aumentar cantidades de ULTI");
        System.out.println("  5. Aumentar TODO!");
        System.out.println("  6. Volver al menú principal");
        System.out.print("Opción: ");
    }


    public static String inicioSesion(Scanner sc){
        String nombreJ = "";
        System.out.println("═══════════════════════════════════════");
        System.out.println("          📌 INICIO DE SESIÓN");
        System.out.println("═══════════════════════════════════════");

        System.out.print("Nombre: ");
        nombreJ = sc.nextLine().trim();

        if(!nombreJ.isEmpty()) {
            System.out.println("✅ Bienvenido, " + nombreJ + "! Prepara tu aventura...");
            return nombreJ;
        }
        System.out.println("⛔ No has iniciado sessión con un nombre, se te asiganara el nombre: Rodolfo");
        return nombreJ = "Rodolfo";


    }



    // ───────────── Validaciones ─────────────

    public static void validarMonstruos(ArrayList<Monstruo> m) {
        if (m.isEmpty()) {
            final String RESET = "\u001B[0m";
            final String GREEN = "\u001B[32m";
            final String YELLOW = "\u001B[33m";
            final String BOLD = "\u001B[1m";

            System.out.println("\n" + BOLD + GREEN);
            System.out.println("████████████████████████████████████████████████████████████");
            System.out.println("█                                                          █");
            System.out.println("█      🏆 ¡TODOS LOS MONSTRUOS HAN SIDO DERROTADOS! 🏆     █");
            System.out.println("█                                                          █");
            System.out.println(YELLOW +
                    "█     💪 Has demostrado ser un verdadero héroe.            █");
            System.out.println("█          🎉 ¡La victoria es completamente tuya! 🎉       █");
            System.out.println(GREEN +
                    "█                                                          █");
            System.out.println("████████████████████████████████████████████████████████████" + RESET);
            System.out.println("\n");
            System.exit(0);
        }
    }



    public static void validarJugador(Jugador j) {
        if (j.getVida() == 0) {
            final String RESET = "\u001B[0m";
            final String RED = "\u001B[31m";
            final String WHITE = "\u001B[37m";
            final String BOLD = "\u001B[1m";

            System.out.println("\n" + BOLD + RED);
            System.out.println("████████████████████████████████████████████████████████████");
            System.out.println("█                                                          █");
            System.out.println("█                      ☠️  ¡DERROTA! ☠️                    █");
            System.out.println("█                                                          █");
            System.out.println(WHITE +
                    "█      💀 Los monstruos han acabado contigo...             █");
            System.out.println("█     🛡️  ¡Pero podrás volver a intentarlo pronto!         █");
            System.out.println(RED +
                    "█                                                          █");
            System.out.println("████████████████████████████████████████████████████████████" + RESET);
            System.out.println("\n");
            System.exit(0);
        }
    }


    public static boolean devolverDañoMonst() {
        Random rd = new Random();
        int random = rd.nextInt(100);
        return random < 70;
    }

    public static void avisoVida(Jugador j){
        if(j.getVida() < 20){
            System.out.println("=== TIENES POCA VIDA ===");
        }
    }


}
