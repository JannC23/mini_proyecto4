package controlador;

import vista.Interfaz;
import modelo.Entrenador;
import modelo.Pokemon;
import modelo.Excepciones;

import java.util.Scanner;
import javax.swing.SwingUtilities;

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
                System.out.print("Ingresa tu elección (1, 2 o 3): ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea pendiente

                if (opcion == 1) {
                    enInterfazGrafica = true;
                    SwingUtilities.invokeLater(() -> new Interfaz(entrenador1, entrenador2)); // Asegura que la GUI se ejecute en el hilo de eventos de Swing
                } else if (opcion == 2) {
                    if (entrenador1 == null || entrenador2 == null) {
                        configurarEntrenadores(scanner); // Configurar entrenadores si no están definidos
                    }
                    iniciarBatallaConsola(scanner); // Inicia la batalla en consola
                } else if (opcion == 3) {
                    System.out.println("Saliendo del programa...");
                    break;
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

            boolean accionValida = false; // Solo avanza el turno si la acción fue válida

            while (!accionValida) {
                System.out.println("¿Qué deseas hacer?");
                System.out.println("1. Atacar");
                System.out.println("2. Cambiar de Pokémon");
                int accion = -1;
                while (accion != 1 && accion != 2) {
                    System.out.print("Elige una opción (1-2): ");
                    if (scanner.hasNextInt()) {
                        accion = scanner.nextInt();
                    } else {
                        scanner.next();
                    }
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
}
