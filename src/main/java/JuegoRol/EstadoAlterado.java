package PruebasTest.practicar.JuegoRol;

public class EstadoAlterado {
    public enum Tipo {
        ENVENENADO, QUEMADO, CONGELADO, ATURDIDO, SANGRADO, BENDECIDO, FURIA
    }

    private Tipo tipo;
    private int turnosRestantes;
    private int valor; // daño/cura por turno

    public EstadoAlterado(Tipo tipo, int turnos, int valor){
        this.tipo = tipo;
        this.turnosRestantes = turnos;
        this.valor = valor;
    }

    public Tipo getTipo(){ return tipo; }
    public int getTurnosRestantes(){ return turnosRestantes; }
    public int getValor(){ return valor; }
    public void reducirTurno(){ this.turnosRestantes--; }
    public boolean expiro(){ return turnosRestantes <= 0; }

    public String nombre(){
        return switch (tipo){
            case ENVENENADO -> "☠️  Envenenado";
            case QUEMADO    -> "🔥 Quemado";
            case CONGELADO  -> "❄️  Congelado";
            case ATURDIDO   -> "💫 Aturdido";
            case SANGRADO   -> "🩸 Sangrado";
            case BENDECIDO  -> "✨ Bendecido";
            case FURIA      -> "🔥 Furia";
        };
    }

    public String descripcion(){
        return nombre() + " (" + turnosRestantes + " turnos)";
    }
}
