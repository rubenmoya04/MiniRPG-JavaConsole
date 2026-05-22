package PruebasTest.practicar.JuegoRol;

public class Objeto {
    private String nombre;
    private String tipo; // "arma", "pocion", "armadura", "amuleto", "comida"
    private int potenciadorDano;
    private int curacion;
    private int bonusDefensa;
    private int bonusAtaque;
    private int precio;
    private int usosRestantes;
    private String descripcion;

    public Objeto(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.potenciadorDano = 0;
        this.curacion = 0;
        this.bonusDefensa = 0;
        this.bonusAtaque = 0;
        this.precio = 10;
        this.usosRestantes = 1;
        this.descripcion = "";
    }

    public Objeto(String nombre, String tipo, int potenciadorDano, int curacion, int bonusDefensa, int bonusAtaque, int precio, int usos, String descripcion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.potenciadorDano = potenciadorDano;
        this.curacion = curacion;
        this.bonusDefensa = bonusDefensa;
        this.bonusAtaque = bonusAtaque;
        this.precio = precio;
        this.usosRestantes = usos;
        this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getPotenciadorDano() { return potenciadorDano; }
    public int getCuracion() { return curacion; }
    public int getBonusDefensa() { return bonusDefensa; }
    public int getBonusAtaque() { return bonusAtaque; }
    public int getPrecio() { return precio; }
    public int getUsosRestantes() { return usosRestantes; }
    public String getDescripcion() { return descripcion; }

    public void setUsosRestantes(int usos) { this.usosRestantes = usos; }
    public void consumirUso() { this.usosRestantes--; }

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

    public String mostrarObjeto(){
        StringBuilder sb = new StringBuilder();
        sb.append(emoji()).append(ColorConsola.negrita(nombre));
        sb.append(" [").append(tipo).append("]");
        if(potenciadorDano > 0) sb.append(" 💥+").append(potenciadorDano).append(" dmg");
        if(curacion > 0) sb.append(" ❤️ +").append(curacion).append(" HP");
        if(bonusAtaque > 0) sb.append(" 🗡️+").append(bonusAtaque).append(" atk");
        if(bonusDefensa > 0) sb.append(" 🛡️+").append(bonusDefensa).append(" def");
        if(usosRestantes > 1) sb.append(" (x").append(usosRestantes).append(")");
        if(!descripcion.isEmpty()) sb.append("\n   ").append(ColorConsola.cyan(descripcion));
        return sb.toString();
    }
}
