import java.util.ArrayList;
<<<<<<< HEAD

// Clase que representa un entrenador Pokémon
// Cada entrenador tiene un nombre y un equipo de Pokémon
public class Entrenador {
    private String nombre; // Nombre del entrenador
    private ArrayList<Pokemon> equipo = new ArrayList<>(); // Equipo de Pokémon del entrenador
    private int actual = 0; // Índice del Pokémon actual en batalla

    public Entrenador(String nombre) {
        this.nombre = nombre;
=======
import java.util.Scanner;

public class Entrenador {
    private String nombre;
    private ArrayList<Pokemon> equipo = new ArrayList<>();

    public Entrenador(String nombre) {
        this.nombre = nombre;
        this.equipo = new ArrayList<>();
>>>>>>> origin/main
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
<<<<<<< HEAD
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
=======
        if (equipo.size() > 3) {
            System.out.println("El equipo no puede tener más de tres Pokémon");
            return;
        }
        this.equipo.clear();
        for (Pokemon pokemon : equipo) {
            this.equipo.add(pokemon);
        }
    }

    public Pokemon elegirPokemonParaBatalla() {
        Scanner entrada = new Scanner(System.in);
        System.out.println("\n" + nombre + ", selecciona un Pokémon para la batalla:");
        for (int i = 0; i < equipo.size(); i++) {
            Pokemon pokemon = equipo.get(i);
            System.out.println((i + 1) + ". " + pokemon.getNombre() + " (HP: " + pokemon.getPuntosSalud() + ")");
        }
        int opcion;
        do {
            System.out.print("Seleccione el número del Pokémon: ");
            opcion = entrada.nextInt();
            if (opcion < 1 || opcion > equipo.size()) {
                System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion < 1 || opcion > equipo.size());
        return equipo.get(opcion - 1);
>>>>>>> origin/main
    }
}