package PruebasTest.practicar.JuegoRol;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Tienda {

    public static ArrayList<Objeto> generarStock(int nivelJugador){
        ArrayList<Objeto> stock = new ArrayList<>();
        stock.add(new Objeto("Poción menor", "pocion", 0, 30, 0, 0, 25, 1, "Restaura 30 HP"));
        stock.add(new Objeto("Poción mayor", "pocion", 0, 70, 0, 0, 60, 1, "Restaura 70 HP"));
        stock.add(new Objeto("Elixir divino", "pocion", 0, 150, 0, 0, 130, 1, "Restaura 150 HP"));
        stock.add(new Objeto("Filete jugoso", "comida", 0, 20, 0, 0, 15, 2, "Restaura 20 HP, 2 usos"));
        stock.add(new Objeto("Espada de hierro", "arma", 0, 0, 0, 8, 80, 1, "+8 ataque permanente"));
        stock.add(new Objeto("Espada mística", "arma", 0, 0, 0, 15, 180, 1, "+15 ataque permanente"));
        stock.add(new Objeto("Armadura de cuero", "armadura", 0, 0, 6, 0, 70, 1, "+6 defensa permanente"));
        stock.add(new Objeto("Coraza de placas", "armadura", 0, 0, 14, 0, 160, 1, "+14 defensa permanente"));
        stock.add(new Objeto("Amuleto de fuerza", "amuleto", 0, 0, 0, 10, 200, 1, "+10 ataque permanente"));
        stock.add(new Objeto("Bomba de fuego", "bomba", 60, 0, 0, 0, 40, 1, "Inflige 60 daño directo"));
        stock.add(new Objeto("Bomba santa", "bomba", 120, 0, 0, 0, 100, 1, "Inflige 120 daño directo"));
        stock.add(new Objeto("Pergamino de furia", "pergamino", 0, 0, 0, 0, 80, 1, "Activa Furia 3 turnos (+50% daño)"));
        stock.add(new Objeto("Pergamino de bendición", "pergamino", 0, 0, 0, 0, 70, 1, "Cura 10 HP por 3 turnos"));

        if(nivelJugador >= 3){
            stock.add(new Objeto("Espada Excálibur", "arma", 0, 0, 0, 30, 400, 1, "+30 ataque permanente"));
            stock.add(new Objeto("Escudo Aegis", "armadura", 0, 0, 25, 0, 350, 1, "+25 defensa permanente"));
        }
        if(nivelJugador >= 5){
            stock.add(new Objeto("Anillo del Dragón", "amuleto", 0, 0, 10, 20, 600, 1, "+20 atk y +10 def"));
        }

        return stock;
    }

    public static void abrir(Jugador j, Scanner sc){
        Random rd = new Random();
        ArrayList<Objeto> stock = generarStock(j.getNivel());
        // Solo mostramos 6-8 objetos aleatorios
        int aMostrar = 6 + rd.nextInt(3);
        ArrayList<Objeto> mostrados = new ArrayList<>();
        while(mostrados.size() < aMostrar && !stock.isEmpty()){
            mostrados.add(stock.remove(rd.nextInt(stock.size())));
        }

        boolean salir = false;
        while(!salir){
            System.out.println(ColorConsola.amarillo("\n╔══════════════════ 🏪 TIENDA DEL VIAJERO ══════════════════╗"));
            System.out.println(ColorConsola.amarillo("    Mercader: ") + ColorConsola.cyan("\"¡Bienvenido aventurero! Echa un vistazo.\""));
            System.out.println(ColorConsola.amarillo("    Tu oro: ") + ColorConsola.amarillo(j.getInventario().getOro() + " 💰"));
            System.out.println(ColorConsola.amarillo("╠═══════════════════════════════════════════════════════════╣"));
            for(int i = 0; i < mostrados.size(); i++){
                System.out.println("  " + (i+1) + ". " + mostrados.get(i).mostrarObjeto() +
                        ColorConsola.amarillo("   💰 " + mostrados.get(i).getPrecio()));
            }
            System.out.println("  0. Salir de la tienda");
            System.out.println(ColorConsola.amarillo("╚═══════════════════════════════════════════════════════════╝"));
            System.out.print("Elige: ");
            try {
                int opc = Integer.parseInt(sc.nextLine().trim());
                if(opc == 0){ salir = true; continue; }
                if(opc < 1 || opc > mostrados.size()){
                    System.out.println(ColorConsola.rojo("Opción no válida."));
                    continue;
                }
                Objeto o = mostrados.get(opc - 1);
                if(j.getInventario().gastarOro(o.getPrecio())){
                    if(j.getInventario().agregarObjeto(o)){
                        System.out.println(ColorConsola.verde("✅ Compraste: " + o.getNombre()));
                        mostrados.remove(opc - 1);
                    } else {
                        // devolvemos oro si no se puede añadir
                        j.getInventario().agregarOro(o.getPrecio());
                    }
                } else {
                    System.out.println(ColorConsola.rojo("💸 No tienes suficiente oro."));
                }
            } catch (NumberFormatException e){
                System.out.println(ColorConsola.rojo("Introduce un número."));
            }
        }
    }
}
