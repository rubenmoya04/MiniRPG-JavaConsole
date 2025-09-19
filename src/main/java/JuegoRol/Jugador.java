package PruebasTest.practicar.JuegoRol;

import java.util.ArrayList;
import java.util.Random;

public class Jugador {
    private String nombre;
    private int vida;
    private int ataque;
    private int defensa;
    private String clase;

    // Constructor solo con nombre
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.vida = 0;
        this.ataque = 0;
        this.defensa = 0;
        this.clase = null;
    }

    // Getters y setters
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

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    // Método para asignar clase y stats según elección
    public void asignarClase(String clase) {
        this.clase = clase.toLowerCase();

        switch (this.clase) {
            case "guerrero":
                this.vida = 140;
                this.ataque = 25;
                this.defensa = 20;
                break;
            case "mago":
                this.vida = 90;
                this.ataque = 40;
                this.defensa = 10;
                break;
            case "arquero":
                this.vida = 80;
                this.ataque = 30;
                this.defensa = 5;
                break;
            case "sacerdote":
                this.vida = 80;
                this.ataque = 13;
                this.defensa = 4;
                break;
            case "esclavo":
                this.vida = 70;
                this.ataque = 10;
                this.defensa = 2;
                break;
            default:
                this.vida = 100;
                this.ataque = 20;
                this.defensa = 10;
                break;
        }
    }

    public void asignarClaseMedioPartida(String clase) {
        this.clase = clase.toLowerCase();

        switch (this.clase) {
            case "guerrero":
                this.ataque = 25;
                this.defensa = 20;
                break;
            case "mago":
                this.ataque = 40;
                this.defensa = 5;
                break;
            case "arquero":
                this.ataque = 30;
                this.defensa = 10;
                break;
            case "sacerdote":
                this.ataque = 15;
                this.defensa = 4;
                break;
            case "esclavo":
                this.ataque = 10;
                this.defensa = 2;
                break;
            default:
                this.ataque = 25;
                this.defensa = 20;
                break;
        }
    }

    public void asignarClasePorNumeroMedioPartida(int opcion) {
        switch (opcion) {
            case 1:
                asignarClaseMedioPartida("Guerrero");
                break;
            case 2:
                asignarClaseMedioPartida("Mago");
                break;
            case 3:
                asignarClaseMedioPartida("Arquero");
                break;
            case 4:
                asignarClaseMedioPartida("Sacerdote");
                break;
            case 5:
                asignarClaseMedioPartida("esclavo");
                break;
            default:
                System.out.println("Opción inválida, asignando Guerrero por defecto");
                asignarClaseMedioPartida("Guerrero");
                break;
        }
    }

    public void asignarClasePorNumero(int opcion) {
        switch (opcion) {
            case 1:
                asignarClase("Guerrero");
                break;
            case 2:
                asignarClase("Mago");
                break;
            case 3:
                asignarClase("Arquero");
                break;
            case 4:
                asignarClase("Sacerdote");
                break;
            case 5:
                asignarClase("esclavo");
                break;
            default:
                System.out.println("Opción inválida, asignando Guerrero por defecto");
                asignarClase("Guerrero");
                break;
        }
    }

    public boolean esquivar(){
        Random rd = new Random();
        return rd.nextInt(100) < 30; //return de si es <30 true si no false
    }

    public int randomCura (){
        Random rd = new Random();
        int cura = rd.nextInt(30) + 15;
        return cura;
    }

    public void curacionJugador(){
        int cura = randomCura();
        this.vida += cura;
        System.out.println("--· Curación efectiva: " + cura + "❤️");
        System.out.println("→ Vida actual: " + this.vida + " ❤️❤️");
    }

    public int randomTiroUlti (){
        Random rd = new Random();
        int dañoUlti = rd.nextInt(100) + 20;
        return dañoUlti;
    }


    public void ultimateTiro(ArrayList<Monstruo> m, int opc){
        int dañoUlti = randomTiroUlti();
        Random rd = new Random();
        String nombreUlti = "";
        int elec = rd.nextInt(3) + 1;
        if(elec == 1){
            nombreUlti = "Destruye mundos";
        }
        if(elec == 2){
            nombreUlti = "Sangrador de los muertos";
        }if(elec == 3){
            nombreUlti = "Magdalenas explosivas";
        }

        m.get(opc).recibirDaño(dañoUlti);
        if(m.get(opc).estaVivo()){
            System.out.println("ULTIMATE UTILIZADA: "+nombreUlti+" con un daño de "+ dañoUlti +" \n· Vida de "+m.get(opc).getNombre()+": "+m.get(opc).getVida()+ "  ❤❤❤");
        }
        else {
            System.out.println("\n=== Monstruo " + m.get(opc).getNombre() + " ha muerto! ===\n");
            m.remove(m.get(opc));
        }
    }


    public void recibirDaño(int daño) {
        if(this.clase.equals("esclavo") && esquivar()){
            System.out.println("¡ HAS ESQUIVADO EL ATAQUE ! ");
            return;
        }

        int vidaActual = this.vida - daño;
        if (vidaActual < 0) vidaActual = 0;
        this.vida = vidaActual;

        if (!estaVivo()) {
            System.out.println("=== Jugador muerto! ===");
        }
    }

    public boolean estaVivo() {
        return this.vida > 0; //return de vida si no es 0
    }

    public int atacar() {
        Random rd = new Random();
        if(this.clase.equals("esclavo")){
            if(rd.nextInt(100)< 25){ //probabilidad 25
                System.out.println("¡Golpe crítico del esclavo!");
                return (int) (this.ataque * 2.2);
            }
        }
        if(this.clase.equals("arquero")){
            if (rd.nextInt(100)< 30){
                System.out.println("¡ HEADSHOT DEL ARQUERO !");
                return (int) (this.ataque * 3);
            }
            if (rd.nextInt(100)< 10){
                System.out.println(" ¡ TIRO DEMENCIAL !");
                return (int) (this.ataque * 4);

            }
            if (rd.nextInt(100)< 2){
                System.out.println("🔥 ¡ TIRO DE FRANCOTIRADOR ! 🔥");
                return (int) (this.ataque * 6);
            }
        }
        return this.ataque;
    }

    public void mostrarPersonaje() {
        System.out.println("==========================================");
        System.out.println("                 " + this.nombre + " (" + this.clase.toUpperCase() + ")                 ");
        System.out.println("· Vida: " + this.vida + "\n· Ataque: " + this.ataque + "\n· Defensa: " + this.defensa);
        System.out.println("==========================================");
    }







}
