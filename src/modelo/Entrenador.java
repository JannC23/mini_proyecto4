package modelo;
import java.util.ArrayList;

// Clase que representa un entrenador Pokémon
// Cada entrenador tiene un nombre y un equipo de Pokémon
public class Entrenador {
    private String nombre; // Nombre del entrenador
    private ArrayList<Pokemon> equipo = new ArrayList<>(); // Equipo de Pokémon del entrenador
    private int actual = 0; // Índice del Pokémon actual en batalla

    public Entrenador(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Pokemon> getEquipo() {
        return equipo;
    }

    public void setEquipo(ArrayList<Pokemon> equipo) {
        this.equipo = equipo;
    }

    public Pokemon getPokemonActual() {
        if (actual >= 0 && actual < equipo.size()) {
            return equipo.get(actual);
        }
        return null; // Retornar null si el índice no es válido
    }

    public void setPokemonInicial(Pokemon pokemon) {
        for (int i = 0; i < equipo.size(); i++) {
            if (equipo.get(i).equals(pokemon)) {
                actual = i; // Establecer el Pokémon seleccionado como el activo
                return;
            }
        }
        System.out.println("El Pokémon seleccionado no está en el equipo.");
    }

    public void siguientePokemon() {
        if (actual + 1 < equipo.size()) {
            actual++;
        } else {
            actual = -1; // Indicar que no hay más Pokémon disponibles
        }
    }

    public boolean todosDebilitados() {
        for (Pokemon p : equipo) {
            if (p.getPuntosSalud() > 0) {
                return false;
            }
        }
        return true;
    }

    public void setPokemonActual(int indice) {
        this.actual = indice;
    }
}
