package PruebasTest.practicar.JuegoRol;

// Clase para los estados que pueden afectar al jugador o a los monstruos
// (envenenao, quemao, congelao, etc.)
public class EstadoAlterado {

    // Los tipos de estados que existen, los uso desde fuera con EstadoAlterado.Tipo.X
    public enum Tipo {
        ENVENENADO, QUEMADO, CONGELADO, ATURDIDO, SANGRADO, BENDECIDO, FURIA
    }

    private Tipo tipo;
    private int turnosRestantes;
    private int valor; // cuanto daño o cura mete por turno (depende del estado)

    public EstadoAlterado(Tipo tipo, int turnos, int valor){
        this.tipo = tipo;
        this.turnosRestantes = turnos;
        this.valor = valor;
    }

    // Getters
    public Tipo getTipo(){ return tipo; }
    public int getTurnosRestantes(){ return turnosRestantes; }
    public int getValor(){ return valor; }

    // Le quita un turno al estado, cuando llega a 0 se quita
    public void reducirTurno(){ this.turnosRestantes--; }
    public boolean expiro(){ return turnosRestantes <= 0; }

    // Devuelve el nombre del estado con su emoji para imprimir
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

    // Lo mismo pero añadiendo los turnos que quedan
    public String descripcion(){
        return nombre() + " (" + turnosRestantes + " turnos)";
    }
}
