package PruebasTest.practicar.JuegoRol;

public class Objeto {
    private String nombre;
    private String tipo; // ej: "arma", "poción", "escudo"
    private int potenciadorDaño;
    private int curacion;


    // Constructor básico
    public Objeto(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.potenciadorDaño = 0;
        this.curacion = 0;
    }

    // Constructor completo
    public Objeto(String nombre, String tipo, int potenciadorDaño, int curacion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.potenciadorDaño = potenciadorDaño;
        this.curacion = curacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPotenciadorDaño() {
        return potenciadorDaño;
    }

    public void setPotenciadorDaño(int potenciadorDaño) {
        this.potenciadorDaño = potenciadorDaño;
    }

    public int getCuracion() {
        return curacion;
    }

    public void setCuracion(int curacion) {
        this.curacion = curacion;
    }


    public String mostarObjeto(){
        String emoji = switch (tipo.toLowerCase()) {
            case "arma" -> "🗡️ ";
            case "poción", "pocion" -> "🧪 ";
            case "armadura" -> "🧤 ";
            default -> throw new IllegalStateException("Unexpected value: " + tipo.toLowerCase());
        };

        String descrip = emoji + nombre + "[" + tipo + "]";
        if(potenciadorDaño > 0){
            descrip += " 💥 +" + potenciadorDaño +" daño";
        }
        if(curacion > 0){
            descrip += " ❤ " + curacion + " salud";
        }
        return descrip;
    }

    public int usarObjeto(int opc, Jugador j ){
        if (opc == 1){ //potenciador dño
            System.out.println("Se a utilizado el potenciador de daño " + getNombre());
            return j.getAtaque() * getPotenciadorDaño();
        }
        return 0; //nada
    }




}
