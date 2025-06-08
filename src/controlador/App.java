package controlador;

import vista.Interfaz;
import modelo.Entrenador;
import modelo.Pokemon;
import modelo.Excepciones;

import java.util.Scanner;
import javax.swing.SwingUtilities;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class App {
    private static boolean enInterfazGrafica = false; // Bandera para saber si está en la interfaz gráfica
    private static Entrenador entrenador1, entrenador2; // Entrenadores compartidos entre modos

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (!enInterfazGrafica) {
                System.out.println("Bienvenido al Simulador de Batalla Pokémon");
                System.out.println("Elige el modo de batalla:");
                System.out.println("1. Interfaz Gráfica");
                System.out.println("2. Consola");
                System.out.println("3. Salir");
                System.out.println("4. Cargar partida (para consola)");
                System.out.print("Ingresa tu elección (1, 2, 3 o 4): ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea pendiente

                if (opcion == 1) {
                    enInterfazGrafica = true;
                    SwingUtilities.invokeLater(() -> new Interfaz(entrenador1, entrenador2));
                } else if (opcion == 2) {
                    if (entrenador1 == null || entrenador2 == null) {
                        configurarEntrenadores(scanner);
                    }
                    iniciarBatallaConsola(scanner);
                } else if (opcion == 3) {
                    System.out.println("Saliendo del programa...");
                    break;
                } else if (opcion == 4) {
                    cargarPartidaConsola();
                } else {
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            } else {
                System.out.println("Actualmente estás en la interfaz gráfica.");
                System.out.println("¿Deseas volver al modo consola? (s/n): ");
                String respuesta = scanner.nextLine().trim();

                if (respuesta.equalsIgnoreCase("s")) {
                    enInterfazGrafica = false;
                    entrenador1 = null;
                    entrenador2 = null;
                    System.out.println("Volviendo al modo consola...");
                } else {
                    System.out.println("Permaneciendo en la interfaz gráfica.");
                }
            }
        }

        scanner.close();
    }

    // Método para configurar los entrenadores en consola
    private static void configurarEntrenadores(Scanner scanner) {
        // --- Entrenador 1 ---
        System.out.print("Ingresa el nombre del Entrenador 1: ");
        String nombre1 = scanner.nextLine();
        entrenador1 = new Entrenador(nombre1);

        boolean equipoValido1 = false; // Bandera para controlar si el equipo es válido
        while (!equipoValido1) { // Repetir hasta que el equipo no tenga Pokémon repetidos
            try {
                // Intenta asignar el equipo al entrenador 1
                entrenador1.setEquipo(Pokemon.seleccionarEquipoPokemon());
                equipoValido1 = true; // Si no hay excepción, el equipo es válido
            } catch (Excepciones.PokemonRepetidoEnEquipoException e) {
                // Si hay Pokémon repetidos, muestra el mensaje y vuelve a pedir el equipo
                System.out.println(e.getMessage());
                System.out.println("Por favor, selecciona un equipo sin repetir Pokémon.");
            }
        }

        // --- Entrenador 2 ---
        System.out.print("Ingresa el nombre del Entrenador 2: ");
        String nombre2 = scanner.nextLine();
        entrenador2 = new Entrenador(nombre2);

        boolean equipoValido2 = false; // Bandera para controlar si el equipo es válido
        while (!equipoValido2) { // Repetir hasta que el equipo no tenga Pokémon repetidos
            try {
                // Intenta asignar el equipo al entrenador 2
                entrenador2.setEquipo(Pokemon.seleccionarEquipoPokemon());
                equipoValido2 = true; // Si no hay excepción, el equipo es válido
            } catch (Excepciones.PokemonRepetidoEnEquipoException e) {
                // Si hay Pokémon repetidos, muestra el mensaje y vuelve a pedir el equipo
                System.out.println(e.getMessage());
                System.out.println("Por favor, selecciona un equipo sin repetir Pokémon.");
            }
        }
    }

    // Método para iniciar la batalla en consola
    private static void iniciarBatallaConsola(Scanner scanner) {
        System.out.println("\n¡Que comience la batalla!");
        boolean turnoJugador1 = true;

        while (!entrenador1.todosDebilitados() && !entrenador2.todosDebilitados()) {
            Entrenador atacante = turnoJugador1 ? entrenador1 : entrenador2;
            Entrenador defensor = turnoJugador1 ? entrenador2 : entrenador1;

            System.out.println("\nTurno de " + atacante.getNombre());
            Pokemon pokemonAtacante = atacante.getPokemonActual();
            Pokemon pokemonDefensor = defensor.getPokemonActual();

            System.out.println("Tu Pokémon: " + pokemonAtacante.getNombre() + " (HP: " + pokemonAtacante.getPuntosSalud() + ")");
            System.out.println("Pokémon rival: " + pokemonDefensor.getNombre() + " (HP: " + pokemonDefensor.getPuntosSalud() + ")");

            boolean accionValida = false;

            while (!accionValida) {
                System.out.println("¿Qué deseas hacer?");
                System.out.println("1. Atacar");
                System.out.println("2. Cambiar de Pokémon");
                System.out.println("3. Guardar partida");
                int accion = -1;
                while (accion < 1 || accion > 3) {
                    System.out.print("Elige una opción (1-3): ");
                    if (scanner.hasNextInt()) {
                        accion = scanner.nextInt();
                    } else {
                        scanner.next();
                    }
                }

                if (accion == 3) {
                    guardarPartidaConsola();
                    System.out.println("Partida guardada.");
                    // No avanza el turno, permite seguir eligiendo acción
                    continue;
                }

                if (accion == 2) {
                    // Mostrar todos los Pokémon del equipo, incluso los debilitados
                    System.out.println("Pokémon disponibles:");
                    for (int i = 0; i < atacante.getEquipo().size(); i++) {
                        Pokemon p = atacante.getEquipo().get(i);
                        String estado = (p.getPuntosSalud() > 0) ? "" : " (DEBILITADO)";
                        String actual = (p == pokemonAtacante) ? " [ACTUAL]" : "";
                        System.out.println((i + 1) + ". " + p.getNombre() + " (HP: " + p.getPuntosSalud() + ")" + estado + actual);
                    }
                    int seleccion = -1;
                    boolean cambioExitoso = false;
                    while (!cambioExitoso) {
                        System.out.print("Selecciona el número del Pokémon al que deseas cambiar: ");
                        if (scanner.hasNextInt()) {
                            seleccion = scanner.nextInt();
                            // Validar selección dentro de rango y que no sea el actual
                            if (seleccion >= 1 && seleccion <= atacante.getEquipo().size() &&
                                atacante.getEquipo().get(seleccion - 1) != pokemonAtacante) {
                                try {
                                    // Lanza excepción si el Pokémon está debilitado
                                    if (atacante.getEquipo().get(seleccion - 1).getPuntosSalud() <= 0) {
                                        throw new Excepciones.PokemonDebilitadoException("No puedes cambiar a un Pokémon debilitado.");
                                    }
                                    atacante.setPokemonActual(seleccion - 1); // Cambia el Pokémon actual al seleccionado
                                    System.out.println("¡" + atacante.getNombre() + " cambió a " + atacante.getPokemonActual().getNombre() + "!");
                                    cambioExitoso = true; // Salir del ciclo de cambio
                                    accionValida = true;  // Acción válida, termina el turno
                                } catch (Excepciones.PokemonDebilitadoException e) {
                                    System.out.println(e.getMessage());
                                    break; // Sale del ciclo de cambio y vuelve al menú de opciones
                                }
                            } else {
                                System.out.println("Selección inválida. No puedes elegir el Pokémon actual o fuera de rango.");
                            }
                        } else {
                            scanner.next();
                            System.out.println("Entrada inválida. Ingresa un número.");
                        }
                    }
                } else if (accion == 1) {
                    System.out.println("Elige un ataque:");
                    for (int i = 0; i < pokemonAtacante.getAtaques().length; i++) {
                        System.out.println((i + 1) + ". " + pokemonAtacante.getAtaques()[i].getNombre());
                    }

                    int ataqueIndex = -1;
                    while (ataqueIndex < 0 || ataqueIndex >= pokemonAtacante.getAtaques().length) {
                        System.out.print("Ingresa el número del ataque: ");
                        if (scanner.hasNextInt()) {
                            ataqueIndex = scanner.nextInt() - 1;
                            if (ataqueIndex < 0 || ataqueIndex >= pokemonAtacante.getAtaques().length) {
                                System.out.println("Ataque no válido. Inténtalo de nuevo.");
                            }
                        } else {
                            System.out.println("Entrada inválida. Ingresa un número.");
                            scanner.next(); // Consumir entrada inválida
                        }
                    }

                    try {
                        int danio = pokemonAtacante.calcularDanio(pokemonAtacante.getAtaques()[ataqueIndex], pokemonDefensor);
                        pokemonDefensor.recibirDanio(danio);
                        System.out.println(pokemonAtacante.getNombre() + " usó " + pokemonAtacante.getAtaques()[ataqueIndex].getNombre() + " y causó " + danio + " puntos de daño.");
                        if (pokemonDefensor.getPuntosSalud() <= 0) {
                            System.out.println(pokemonDefensor.getNombre() + " se ha debilitado.");
                            defensor.siguientePokemon();
                        }
                        accionValida = true; // Acción válida, termina el turno
                    } catch (Excepciones.PokemonDebilitadoException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            turnoJugador1 = !turnoJugador1;
        }

        String ganador = entrenador1.todosDebilitados() ? entrenador2.getNombre() : entrenador1.getNombre();
        System.out.println("\n¡" + ganador + " gana la batalla!");
    }

    // Método para guardar la partida en consola
    private static void guardarPartidaConsola() {
        File carpeta = new File("src/partidas_txt");
        if (!carpeta.exists()) carpeta.mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter("src/partidas_txt/partida_guardada.txt"))) {
            writer.println(entrenador1.getNombre());
            writer.println(entrenador2.getNombre());
            for (Pokemon p : entrenador1.getEquipo()) {
                writer.println("E1;" + p.getNombre() + ";" + p.getPuntosSalud());
            }
            writer.println("E1_ACTUAL;" + entrenador1.getPokemonActual().getNombre());
            for (Pokemon p : entrenador2.getEquipo()) {
                writer.println("E2;" + p.getNombre() + ";" + p.getPuntosSalud());
            }
            writer.println("E2_ACTUAL;" + entrenador2.getPokemonActual().getNombre());
            // No se guarda el historial ni el turno en consola, pero puedes agregarlo si lo deseas
            System.out.println("¡Partida guardada exitosamente!");
        } catch (IOException e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
        }
    }

    // Método para cargar una partida guardada en consola
    private static void cargarPartidaConsola() {
        File archivo = new File("src/partidas_txt/partida_guardada.txt");
        if (!archivo.exists()) {
            System.out.println("No hay ninguna partida guardada.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String nombre1 = reader.readLine();
            String nombre2 = reader.readLine();
            entrenador1 = new Entrenador(nombre1);
            entrenador2 = new Entrenador(nombre2);
            ArrayList<Pokemon> equipo1 = new ArrayList<>();
            ArrayList<Pokemon> equipo2 = new ArrayList<>();
            String actual1 = null, actual2 = null;
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("E1;")) {
                    String[] parts = linea.split(";");
                    Pokemon p = new Pokemon(modelo.PokemonEnum.valueOf(parts[1].toUpperCase()));
                    p.recibirDanio(p.getPuntosSalud() - Integer.parseInt(parts[2]));
                    equipo1.add(p);
                } else if (linea.startsWith("E2;")) {
                    String[] parts = linea.split(";");
                    Pokemon p = new Pokemon(modelo.PokemonEnum.valueOf(parts[1].toUpperCase()));
                    p.recibirDanio(p.getPuntosSalud() - Integer.parseInt(parts[2]));
                    equipo2.add(p);
                } else if (linea.startsWith("E1_ACTUAL;")) {
                    actual1 = linea.split(";")[1];
                } else if (linea.startsWith("E2_ACTUAL;")) {
                    actual2 = linea.split(";")[1];
                }
            }
            entrenador1.setEquipo(equipo1);
            entrenador2.setEquipo(equipo2);
            for (Pokemon p : equipo1) if (p.getNombre().equals(actual1)) entrenador1.setPokemonInicial(p);
            for (Pokemon p : equipo2) if (p.getNombre().equals(actual2)) entrenador2.setPokemonInicial(p);
            System.out.println("¡Partida cargada exitosamente!");
        } catch (IOException e) {
            System.out.println("Error al cargar la partida: " + e.getMessage());
        }
    }
}
