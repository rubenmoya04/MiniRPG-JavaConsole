package PruebasTest.practicar.JuegoRol;

// Aqui guardo todos los codigos de color para imprimir en consola
// y un par de cosillas para barras de vida y texto que va apareciendo lento
public class ColorConsola {

    // El caracter ESC va al principio de los codigos ANSI, sin esto no funcionan los colores
    private static final String ESC = "";

    // ─────── Colores y estilos ───────
    public static final String RESET    = ESC + "[0m";
    public static final String NEGRO    = ESC + "[30m";
    public static final String ROJO     = ESC + "[31m";
    public static final String VERDE    = ESC + "[32m";
    public static final String AMARILLO = ESC + "[33m";
    public static final String AZUL     = ESC + "[34m";
    public static final String MORADO   = ESC + "[35m";
    public static final String CYAN     = ESC + "[36m";
    public static final String BLANCO   = ESC + "[37m";
    public static final String BOLD     = ESC + "[1m";
    public static final String UNDER    = ESC + "[4m";
    public static final String BG_ROJO  = ESC + "[41m";
    public static final String BG_NEGRO = ESC + "[40m";

    // Mete un texto entre el color que quieras y el reset
    public static String c(String texto, String color){
        return color + texto + RESET;
    }

    // Atajos para no escribir tanto
    public static String rojo(String t){ return ROJO + t + RESET; }
    public static String verde(String t){ return VERDE + t + RESET; }
    public static String amarillo(String t){ return AMARILLO + t + RESET; }
    public static String azul(String t){ return AZUL + t + RESET; }
    public static String morado(String t){ return MORADO + t + RESET; }
    public static String cyan(String t){ return CYAN + t + RESET; }
    public static String negrita(String t){ return BOLD + t + RESET; }

    // Pinta una barra de vida tipo [█████░░░░░] con su color segun cuanta vida le queda
    public static void barraVida(String nombre, int vidaActual, int vidaMax){
        int totalBloques = 20;
        if(vidaMax <= 0) vidaMax = 1; //pa que no divida entre 0
        int llenos = (int) Math.round(((double)vidaActual / vidaMax) * totalBloques);
        if(llenos < 0) llenos = 0;
        if(llenos > totalBloques) llenos = totalBloques;

        // verde si va sobrao, amarillo si va medio jodido, rojo si esta a punto de palmar
        String color;
        double porcentaje = (double) vidaActual / vidaMax;
        if(porcentaje > 0.6) color = VERDE;
        else if(porcentaje > 0.3) color = AMARILLO;
        else color = ROJO;

        StringBuilder sb = new StringBuilder();
        sb.append(BOLD).append(nombre).append(RESET).append(" [");
        sb.append(color);
        for(int i = 0; i < llenos; i++) sb.append("█");
        sb.append(RESET);
        for(int i = llenos; i < totalBloques; i++) sb.append("░");
        sb.append("] ").append(vidaActual).append("/").append(vidaMax).append(" HP");
        System.out.println(sb.toString());
    }

    // Va escribiendo letra a letra para que mole mas
    public static void escribirLento(String texto, int msPorChar){
        for(int i = 0; i < texto.length(); i++){
            System.out.print(texto.charAt(i));
            try { Thread.sleep(msPorChar); } catch (InterruptedException e) { /* no pasa nada */ }
        }
        System.out.println();
    }

    // Linea bonita para separar
    public static void separador(){
        System.out.println(CYAN + "─────────────────────────────────────────────────────────" + RESET);
    }
}
