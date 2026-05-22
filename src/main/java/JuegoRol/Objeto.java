package PruebasTest.practicar.JuegoRol;

// Objeto que puede llevar el jugador en el inventario.
// Puede ser pocion, arma, armadura, amuleto, bomba, comida o pergamino.
public class Objeto {
    private String nombre;
    private String tipo; // ej: "arma", "pocion", "armadura", "amuleto", "comida", "bomba", "pergamino"
    private int potenciadorDaño;   // daño directo si es bomba
    private int curacion;          // hp que cura si es pocion o comida
    private int bonusDefensa;      // +defensa si es armadura/amuleto
    private int bonusAtaque;       // +ataque si es arma/amuleto
    private int precio;
    private int usosRestantes;     // pa las comidas con varios usos
    private String descripcion;

    // Constructor basico, solo nombre y tipo (lo demas se queda a 0)
    public Objeto(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.potenciadorDaño = 0;
        this.curacion = 0;
        this.bonusDefensa = 0;
        this.bonusAtaque = 0;
        this.precio = 10;
        this.usosRestantes = 1;
        this.descripcion = "";
    }

    // Constructor completo, lo uso pa las cosas de la tienda
    public Objeto(String nombre, String tipo, int potenciadorDaño, int curacion, int bonusDefensa, int bonusAtaque, int precio, int usos, String descripcion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.potenciadorDaño = potenciadorDaño;
        this.curacion = curacion;
        this.bonusDefensa = bonusDefensa;
        this.bonusAtaque = bonusAtaque;
        this.precio = precio;
        this.usosRestantes = usos;
        this.descripcion = descripcion;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getPotenciadorDaño() { return potenciadorDaño; }
    public int getCuracion() { return curacion; }
    public int getBonusDefensa() { return bonusDefensa; }
    public int getBonusAtaque() { return bonusAtaque; }
    public int getPrecio() { return precio; }
    public int getUsosRestantes() { return usosRestantes; }
    public String getDescripcion() { return descripcion; }

    public void setUsosRestantes(int usos) { this.usosRestantes = usos; }
    public void consumirUso() { this.usosRestantes--; } //cada vez que se usa baja en 1

    // Devuelve el emoji que toca segun el tipo de objeto
    public String emoji(){
        return switch (tipo.toLowerCase()) {
            case "arma"     -> "⚔️ ";
            case "pocion"   -> "🧪 ";
            case "armadura" -> "🛡️ ";
            case "amuleto"  -> "🔮 ";
            case "comida"   -> "🍖 ";
            case "bomba"    -> "💣 ";
            case "pergamino"-> "📜 ";
            default          -> "❔ ";
        };
    }

    // Construye la chapa que se imprime cuando el objeto sale en una lista (tienda, inventario...)
    public String mostrarObjeto(){
        StringBuilder sb = new StringBuilder();
        sb.append(emoji()).append(ColorConsola.negrita(nombre));
        sb.append(" [").append(tipo).append("]");
        if(potenciadorDaño > 0) sb.append(" 💥+").append(potenciadorDaño).append(" dmg");
        if(curacion > 0) sb.append(" ❤️ +").append(curacion).append(" HP");
        if(bonusAtaque > 0) sb.append(" 🗡️+").append(bonusAtaque).append(" atk");
        if(bonusDefensa > 0) sb.append(" 🛡️+").append(bonusDefensa).append(" def");
        if(usosRestantes > 1) sb.append(" (x").append(usosRestantes).append(")");
        if(!descripcion.isEmpty()) sb.append("\n   ").append(ColorConsola.cyan(descripcion));
        return sb.toString();
    }
}
