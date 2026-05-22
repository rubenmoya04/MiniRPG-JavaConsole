package PruebasTest.practicar.JuegoRol;

import java.util.Random;

public class Evento {

    public static void lanzarEventoAleatorio(Jugador j){
        Random rd = new Random();
        int e = rd.nextInt(100);

        System.out.println(ColorConsola.morado("\n╔══════════════════════════════════════════════════╗"));
        System.out.println(ColorConsola.morado("║          ✨ EVENTO ALEATORIO ✨                   ║"));
        System.out.println(ColorConsola.morado("╚══════════════════════════════════════════════════╝"));

        if(e < 20){ // Tesoro
            int oro = 30 + rd.nextInt(80);
            ColorConsola.escribirLento(ColorConsola.amarillo("🪙 ¡Encuentras un cofre con " + oro + " monedas de oro!"), 15);
            j.registrarGanarOro(oro);
        } else if(e < 35){ // Trampa
            int dano = 10 + rd.nextInt(20);
            ColorConsola.escribirLento(ColorConsola.rojo("⚠️  ¡Pisas una trampa con púas! Recibes " + dano + " daño."), 15);
            j.recibirDano(dano);
        } else if(e < 50){ // Fuente
            int cura = 30 + rd.nextInt(40);
            j.setVida(j.getVida() + cura);
            ColorConsola.escribirLento(ColorConsola.verde("⛲ Bebes de una fuente mágica. Te curas " + cura + " HP."), 15);
        } else if(e < 60){ // Bendición
            ColorConsola.escribirLento(ColorConsola.azul("🙏 Un sacerdote misterioso te bendice."), 15);
            j.aplicarEstado(new EstadoAlterado(EstadoAlterado.Tipo.BENDECIDO, 4, 8));
        } else if(e < 70){ // Furia
            ColorConsola.escribirLento(ColorConsola.rojo("🔥 Un druida te concede el poder de la furia."), 15);
            j.aplicarEstado(new EstadoAlterado(EstadoAlterado.Tipo.FURIA, 3, 0));
        } else if(e < 80){ // Mendigo
            int oro = 15 + rd.nextInt(25);
            if(j.getInventario().getOro() >= oro){
                ColorConsola.escribirLento(ColorConsola.cyan("🧑‍🦯 Un mendigo pide " + oro + " de oro. Le ayudas..."), 15);
                j.getInventario().gastarOro(oro);
                int bonusXp = 30 + rd.nextInt(40);
                ColorConsola.escribirLento(ColorConsola.morado("✨ ¡Era un dios disfrazado! Recibes " + bonusXp + " XP."), 15);
                j.ganarExperiencia(bonusXp);
            } else {
                ColorConsola.escribirLento(ColorConsola.cyan("🧑‍🦯 Un mendigo te pide oro pero no tienes suficiente."), 15);
            }
        } else if(e < 88){ // Objeto encontrado
            Objeto o;
            int t = rd.nextInt(4);
            switch(t){
                case 0 -> o = new Objeto("Poción menor (encontrada)", "pocion", 0, 30, 0, 0, 0, 1, "Restaura 30 HP");
                case 1 -> o = new Objeto("Daga oxidada", "arma", 0, 0, 0, 5, 0, 1, "+5 ataque");
                case 2 -> o = new Objeto("Pergamino viejo", "pergamino", 0, 0, 0, 0, 0, 1, "Activa Furia 3 turnos");
                default -> o = new Objeto("Bomba pequeña", "bomba", 30, 0, 0, 0, 0, 1, "Inflige 30 daño directo");
            }
            ColorConsola.escribirLento(ColorConsola.verde("🎁 Encuentras un objeto: " + o.getNombre()), 15);
            j.getInventario().agregarObjeto(o);
        } else if(e < 95){ // Maldición
            ColorConsola.escribirLento(ColorConsola.morado("🌑 Un demonio susurra: 'Mi maldición te alcanzará'"), 15);
            j.aplicarEstado(new EstadoAlterado(EstadoAlterado.Tipo.ENVENENADO, 3, 5));
        } else { // Genio milagroso
            ColorConsola.escribirLento(ColorConsola.amarillo("🧞 ¡Un genio te concede un deseo!"), 20);
            int boost = 5 + rd.nextInt(8);
            j.setAtaque(j.getAtaque() + boost);
            j.setDefensa(j.getDefensa() + boost);
            j.setVidaMax(j.getVidaMax() + boost*3);
            j.setVida(j.getVidaMax());
            ColorConsola.escribirLento(ColorConsola.verde("   +" + boost + " ataque, +" + boost + " defensa, +" + (boost*3) + " vida máxima."), 15);
        }
    }
}
