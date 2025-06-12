package vista;

import modelo.Entrenador;
import modelo.Excepciones;
import modelo.Pokemon;
import modelo.PokemonEnum;
import modelo.Ataque;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedList; // Para la lista enlazada
import java.util.HashMap; // Para la tabla hash
import java.util.Scanner;
import java.util.Stack; // Importar Stack para el historial
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException; // Para guardar/cargar archivos

// Clase que representa la interfaz gráfica del simulador de batalla Pokémon
// Aquí se maneja todo lo relacionado con la interacción del usuario y la visualización
public class Interfaz {
    private JFrame frame; // Ventana principal donde se mostrará todo
    private JPanel panelInicio, panelBatalla; // Paneles para las diferentes pantallas (inicio y batalla)
    private JTextField nombre1Field, nombre2Field; // Campos de texto para que los jugadores ingresen sus nombres
    private JButton btnAleatorio1, btnManual1, btnAleatorio2, btnManual2, btnBatalla, btnReiniciar; // Botones para las opciones de equipo
    private JButton btnVerEquiposInicio;
    private JButton btnGuardarPartida, btnCargarPartida; // Ahora atributos de clase
    private JLabel nombreP1, nombreP2, hp1, hp2, gif1, gif2; // Etiquetas para mostrar información de los Pokémon
    private JComboBox<String> ataquesBox; // ComboBox para que el jugador seleccione un ataque
    private JButton btnAtacar, btnCambiarPokemon; // Botón para realizar un ataque
    private Entrenador entrenador1, entrenador2; // Objetos que representan a los entrenadores
    private boolean turnoJugador1 = true; // Variable para saber de quién es el turno (true = jugador 1)
    private Stack<String> historialMovimientos = new Stack<>(); // Pila para el historial de movimientos

    // Lista enlazada para el orden de turnos //
    private LinkedList<Pokemon> ordenTurnos = new LinkedList<>();

    // HashMap para búsquedas rápidas por nombre y tipo //
    private HashMap<String, Pokemon> pokemonsPorNombre = new HashMap<>();
    private HashMap<Pokemon.Tipo, ArrayList<Pokemon>> pokemonsPorTipo = new HashMap<>();

    // Método para configurar la pantalla de inicio
    private void inicializarInicio() {
        panelInicio = new JPanel(new GridLayout(8, 2, 10, 10)); // Crear un panel con una cuadrícula de 8x2
        panelInicio.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100)); // Agregar márgenes al panel
        panelInicio.setBackground(new Color(220, 240, 255)); // Fondo azul claro para que se vea bonito

        // Crear los campos de texto y botones para las opciones de los jugadores
        nombre1Field = new JTextField(); // Campo para el nombre del jugador 1
        nombre2Field = new JTextField(); // Campo para el nombre del jugador 2
        btnAleatorio1 = new JButton("Equipo Aleatorio (Jugador 1)"); // Botón para generar equipo aleatorio para el jugador 1
        btnManual1 = new JButton("Seleccionar Manualmente (Jugador 1)"); // Botón para seleccionar equipo manualmente para el jugador 1
        btnAleatorio2 = new JButton("Equipo Aleatorio (Jugador 2)"); // Botón para generar equipo aleatorio para el jugador 2
        btnManual2 = new JButton("Seleccionar Manualmente (Jugador 2)"); // Botón para seleccionar equipo manualmente para el jugador 2
        btnBatalla = new JButton("Iniciar Batalla"); // Botón para empezar la batalla
        btnVerEquiposInicio = new JButton("Ver Equipos"); // Botón para mostrar los equipos seleccionados
        btnReiniciar = new JButton("Reiniciar Batalla"); // Botón para reiniciar la batalla
        btnGuardarPartida = new JButton("Guardar Partida"); // Ahora es atributo de clase
        btnCargarPartida = new JButton("Cargar Partida"); // Ahora es atributo de clase

        // Agregar los componentes al panel de inicio
        panelInicio.add(new JLabel("Nombre Entrenador 1:")); // Etiqueta para el nombre del jugador 1
        panelInicio.add(nombre1Field); // Campo de texto para el jugador 1
        panelInicio.add(btnAleatorio1); // Botón para equipo aleatorio del jugador 1
        panelInicio.add(btnManual1); // Botón para equipo manual del jugador 1
        panelInicio.add(new JLabel("Nombre Entrenador 2:")); // Etiqueta para el nombre del jugador 2
        panelInicio.add(nombre2Field); // Campo de texto para el jugador 2
        panelInicio.add(btnAleatorio2); // Botón para equipo aleatorio del jugador 2
        panelInicio.add(btnManual2); // Botón para equipo manual del jugador 2
        panelInicio.add(new JLabel()); // Espacio vacío para que se vea mejor
        panelInicio.add(btnVerEquiposInicio); // Botón para ver los equipos
        panelInicio.add(new JLabel()); // Otro espacio vacío
        panelInicio.add(btnBatalla); // Botón para iniciar la batalla
        panelInicio.add(new JLabel()); // Otro espacio vacío
        panelInicio.add(btnReiniciar); // Botón para reiniciar la batalla
        panelInicio.add(btnGuardarPartida); // Ahora sí se agrega el botón de guardar partida
        panelInicio.add(btnCargarPartida);

        // Configurar las acciones de los botones
        btnAleatorio1.addActionListener(e -> {
            String n1 = nombre1Field.getText().trim(); // Obtener el nombre del jugador 1
            if (n1.isEmpty()) { // Verificar que no esté vacío
                JOptionPane.showMessageDialog(frame, "Ingresa el nombre del Entrenador 1."); // Mostrar mensaje de error
                return;
            }
            entrenador1 = new Entrenador(n1); // Crear el entrenador 1
            entrenador1.setEquipo(Pokemon.crearPokemonAleatorio()); // Asignar un equipo aleatorio
            JOptionPane.showMessageDialog(frame, "Equipo generado aleatoriamente para " + n1); // Confirmar al usuario
        });

        btnManual1.addActionListener(e -> {
            String n1 = nombre1Field.getText().trim(); // Obtener el nombre del jugador 1
            if (n1.isEmpty()) { // Verificar que no esté vacío
                JOptionPane.showMessageDialog(frame, "Ingresa el nombre del Entrenador 1."); // Mostrar mensaje de error
                return;
            }
            entrenador1 = new Entrenador(n1); // Crear el entrenador 1
            try {
                entrenador1.setEquipo(seleccionarEquipoManual(entrenador1.getNombre()));
            } catch (Excepciones.PokemonRepetidoEnEquipoException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        btnAleatorio2.addActionListener(e -> {
            String n2 = nombre2Field.getText().trim(); // Obtener el nombre del jugador 2
            if (n2.isEmpty()) { // Verificar que no esté vacío
                JOptionPane.showMessageDialog(frame, "Ingresa el nombre del Entrenador 2."); // Mostrar mensaje de error
                return;
            }
            entrenador2 = new Entrenador(n2); // Crear el entrenador 2
            entrenador2.setEquipo(Pokemon.crearPokemonAleatorio()); // Asignar un equipo aleatorio
            JOptionPane.showMessageDialog(frame, "Equipo generado aleatoriamente para " + n2); // Confirmar al usuario
        });

        btnManual2.addActionListener(e -> {
            String n2 = nombre2Field.getText().trim(); // Obtener el nombre del jugador 2
            if (n2.isEmpty()) { // Verificar que no esté vacío
                JOptionPane.showMessageDialog(frame, "Ingresa el nombre del Entrenador 2."); // Mostrar mensaje de error
                return;
            }
            entrenador2 = new Entrenador(n2); // Crear el entrenador 2
            try {
                // Intenta asignar el equipo manualmente y verifica si hay Pokémon repetidos
                entrenador2.setEquipo(seleccionarEquipoManual(entrenador2.getNombre()));
            } catch (Excepciones.PokemonRepetidoEnEquipoException ex) {
                // Si hay Pokémon repetidos, muestra el mensaje de error
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        btnVerEquiposInicio.addActionListener(e -> mostrarEquipos()); // Mostrar los equipos seleccionados

        btnBatalla.addActionListener(e -> {
            if (entrenador1 == null || entrenador2 == null) { // Verificar que ambos entrenadores tengan equipos
                JOptionPane.showMessageDialog(frame, "Ambos entrenadores deben tener equipos asignados."); // Mostrar mensaje de error
                return;
            }
            elegirPokemonInicial(); // Pasar a la selección de Pokémon inicial
        });

        btnReiniciar.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(frame, "¿Deseas reiniciar la batalla?", "Reiniciar", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                frame.dispose();
                new Interfaz(); // Reinicia la interfaz gráfica
            }
        });
        btnGuardarPartida.addActionListener(e -> guardarPartida()); // Listener global
        btnCargarPartida.addActionListener(e -> cargarPartida());

        frame.add(panelInicio, "inicio"); // Agregar el panel de inicio a la ventana
    }

    // Método para configurar la pantalla de batalla
    private void inicializarBatalla() {
        panelBatalla = new JPanel(new BorderLayout()); // Crear un panel con diseño BorderLayout

        // Panel superior para mostrar los Pokémon y sus atributos
        JPanel panelSup = new JPanel(new GridLayout(2, 5)); // Crear un panel con una cuadrícula de 2x5
        nombreP1 = new JLabel("", SwingConstants.CENTER); // Etiqueta para el nombre del Pokémon del jugador 1
        nombreP2 = new JLabel("", SwingConstants.CENTER); // Etiqueta para el nombre del Pokémon del jugador 2
        hp1 = new JLabel("", SwingConstants.CENTER); // Etiqueta para los puntos de salud del jugador 1
        hp2 = new JLabel("", SwingConstants.CENTER); // Etiqueta para los puntos de salud del jugador 2
        gif1 = new JLabel("", SwingConstants.CENTER); // Etiqueta para el GIF del Pokémon del jugador 1
        gif2 = new JLabel("", SwingConstants.CENTER); // Etiqueta para el GIF del Pokémon del jugador 2

        Dimension gifSize = new Dimension(200, 200); // Tamaño de los GIFs
        gif1.setPreferredSize(gifSize); // Establecer tamaño para el GIF del jugador 1
        gif2.setPreferredSize(gifSize); // Establecer tamaño para el GIF del jugador 2

        // Agregar componentes al panel superior
        panelSup.add(nombreP1);
        panelSup.add(new JLabel("VS", SwingConstants.CENTER)); // Etiqueta "VS" para mostrar el enfrentamiento
        panelSup.add(nombreP2);
        panelSup.add(gif1);
        panelSup.add(new JLabel()); // Espacio vacío
        panelSup.add(gif2);

        // Panel inferior para los controles de batalla
        JPanel panelInf = new JPanel(new GridLayout(2, 1)); // Panel con dos filas
        JPanel panelAtributos = new JPanel(new GridLayout(1, 2)); // Panel para los atributos de los Pokémon
        panelAtributos.add(hp1); // Agregar los puntos de salud del jugador 1
        panelAtributos.add(hp2); // Agregar los puntos de salud del jugador 2

        JPanel panelControles = new JPanel(); // Panel para los controles de batalla
        ataquesBox = new JComboBox<>(); // ComboBox para seleccionar ataques
        btnAtacar = new JButton("Atacar"); // Botón para realizar un ataque
        btnCambiarPokemon = new JButton("Cambiar Pokémon");
        JButton btnHistorial = new JButton("Historial de movimientos");

        panelControles.add(ataquesBox); // Agregar el ComboBox al panel
        panelControles.add(btnAtacar); // Agregar el botón al panel
        panelControles.add(btnCambiarPokemon); // Agregar el botón de cambiar Pokémon
        panelControles.add(btnGuardarPartida); // Agregar el botón al panel
        panelControles.add(btnHistorial); // Agregar el botón de historial de movimientos

        panelInf.add(panelAtributos); // Agregar los atributos al panel inferior
        panelInf.add(panelControles); // Agregar los controles al panel inferior

        panelBatalla.add(panelSup, BorderLayout.CENTER); // Panel central con los Pokémon
        panelBatalla.add(panelInf, BorderLayout.SOUTH);  // Panel inferior con controles

        btnAtacar.addActionListener(e -> ejecutarTurno()); // Configurar la acción del botón "Atacar"
        btnCambiarPokemon.addActionListener(e -> cambiarPokemon());
        btnHistorial.addActionListener(e -> {
            // Panel para mostrar el historial de movimientos
            JPanel panelHistorial = new JPanel(new BorderLayout());
            panelHistorial.setPreferredSize(new Dimension(350, 300));
            panelHistorial.setBorder(BorderFactory.createTitledBorder("Historial de movimientos"));
            javax.swing.JTextArea area = new javax.swing.JTextArea();
            area.setEditable(false);

            // Mostrar los movimientos desde el más reciente al más antiguo
            StringBuilder sb = new StringBuilder();
            // Recorremos la pila desde el tope hacia abajo
            for (int i = historialMovimientos.size() - 1; i >= 0; i--) {
                sb.append(historialMovimientos.get(i)).append("\n");
            }
            area.setText(sb.toString());

            panelHistorial.add(new javax.swing.JScrollPane(area), BorderLayout.CENTER);
            JOptionPane.showMessageDialog(frame, panelHistorial, "Historial de movimientos", JOptionPane.PLAIN_MESSAGE);
        });

        frame.add(panelBatalla, "batalla"); // Agregar el panel de batalla a la ventana
    }

    // Método para seleccionar un equipo manualmente
    private ArrayList<Pokemon> seleccionarEquipoManual(String nombreEntrenador) {
        ArrayList<Pokemon> equipo = new ArrayList<>(); // Crea una lista vacía para el equipo del entrenador
        PokemonEnum[] listaPokemon = PokemonEnum.values(); // Obtiene todos los Pokémon disponibles como un arreglo
        boolean[] ocupados = new boolean[listaPokemon.length]; // Arreglo para marcar si un Pokémon ya fue elegido

        for (int i = 0; i < 3; i++) { // Se repite 3 veces para elegir 3 Pokémon
            String[] opciones = new String[listaPokemon.length]; // Arreglo de nombres de Pokémon para mostrar en el diálogo
            for (int j = 0; j < listaPokemon.length; j++) {
                opciones[j] = listaPokemon[j].getNombre(); // Llena el arreglo con los nombres de los Pokémon
            }
            String seleccion = (String) JOptionPane.showInputDialog(
                frame,
                nombreEntrenador + ", elige tu Pokémon #" + (i + 1), // Mensaje personalizado para el entrenador
                "Selección de Pokémon",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
            );
            int idx = -1; // Índice del Pokémon seleccionado
            for (int j = 0; j < listaPokemon.length; j++) {
                if (opciones[j].equals(seleccion)) { // Si el nombre coincide con la selección del usuario
                    if (ocupados[j]) { // Si ese Pokémon ya fue elegido antes
                        // Lanza la excepción si el Pokémon ya fue elegido
                        throw new Excepciones.PokemonRepetidoEnEquipoException("No puedes elegir el mismo Pokémon dos veces en tu equipo.");
                    }
                    idx = j; // Guarda el índice del Pokémon seleccionado
                    ocupados[j] = true; // Marca ese Pokémon como ya elegido
                    break; // Sale del ciclo porque ya encontró el seleccionado
                }
            }
            if (idx != -1) {
                equipo.add(new Pokemon(listaPokemon[idx])); // Si la selección fue válida, agrega el Pokémon al equipo
            } else {
                i--; // Si hubo error (no se seleccionó un Pokémon válido), repite la selección para este turno
            }
        }
        return equipo; // Devuelve el equipo seleccionado
    }

    // Método para mostrar los equipos de los entrenadores
    private void mostrarEquipos() {
        if (entrenador1 == null || entrenador2 == null) {
            JOptionPane.showMessageDialog(frame, "Ambos entrenadores deben tener equipos asignados.");
            return;
        }
        StringBuilder equipos = new StringBuilder();
        equipos.append("Equipo de ").append(entrenador1.getNombre()).append(":\n");
        for (Pokemon p : entrenador1.getEquipo()) {
            equipos.append(p.getNombre()).append(" (HP: ").append(p.getPuntosSalud()).append(")\n");
        }
        equipos.append("\nEquipo de ").append(entrenador2.getNombre()).append(":\n");
        for (Pokemon p : entrenador2.getEquipo()) {
            equipos.append(p.getNombre()).append(" (HP: ").append(p.getPuntosSalud()).append(")\n");
        }
        JOptionPane.showMessageDialog(frame, equipos.toString(), "Equipos", JOptionPane.INFORMATION_MESSAGE);
    }

    // Método para llenar los HashMap al iniciar la batalla
    private void prepararBusquedasRapidas() {
        pokemonsPorNombre.clear();
        pokemonsPorTipo.clear();
        // Agregar Pokémon del equipo 1
        for (Pokemon p : entrenador1.getEquipo()) {
            pokemonsPorNombre.put(p.getNombre(), p);
            pokemonsPorTipo.computeIfAbsent(p.getTipo(), k -> new ArrayList<>()).add(p);
        }
        // Agregar Pokémon del equipo 2
        for (Pokemon p : entrenador2.getEquipo()) {
            pokemonsPorNombre.put(p.getNombre(), p);
            pokemonsPorTipo.computeIfAbsent(p.getTipo(), k -> new ArrayList<>()).add(p);
        }
    }

    private void elegirPokemonInicial() {
        // Selección del Pokémon inicial para Entrenador 1
        String[] nombres1 = entrenador1.getEquipo().stream().map(Pokemon::getNombre).toArray(String[]::new);
        String seleccion1 = (String) JOptionPane.showInputDialog(
            frame,
            "Entrenador 1, selecciona tu Pokémon inicial:",
            "Seleccionar Pokémon Inicial",
            JOptionPane.PLAIN_MESSAGE,
            null,
            nombres1,
            nombres1[0]
        );
        for (int i = 0; i < nombres1.length; i++) {
            if (nombres1[i].equals(seleccion1)) {
                entrenador1.setPokemonInicial(entrenador1.getEquipo().get(i));
                break;
            }
        }

        // Selección del Pokémon inicial para Entrenador 2
        String[] nombres2 = entrenador2.getEquipo().stream().map(Pokemon::getNombre).toArray(String[]::new);
        String seleccion2 = (String) JOptionPane.showInputDialog(
            frame,
            "Entrenador 2, selecciona tu Pokémon inicial:",
            "Seleccionar Pokémon Inicial",
            JOptionPane.PLAIN_MESSAGE,
            null,
            nombres2,
            nombres2[0]
        );
        for (int i = 0; i < nombres2.length; i++) {
            if (nombres2[i].equals(seleccion2)) {
                entrenador2.setPokemonInicial(entrenador2.getEquipo().get(i));
                break;
            }
        }

        turnoJugador1 = true;
        prepararBusquedasRapidas(); // <-- Agregado aquí
        actualizarPantalla();
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "batalla");
    }

    private ImageIcon cargarGif(String gifPath) {
        java.net.URL url = getClass().getResource("/gifs/" + gifPath);
        if (url != null) {
            return new ImageIcon(url); // Cargar el GIF sin escalar para preservar la animación
        } else {
            System.err.println("Recurso no encontrado: /gifs/" + gifPath);
            return null;
        }
    }

    private void actualizarPantalla() {
        SwingUtilities.invokeLater(() -> {
            if (entrenador1 == null || entrenador2 == null) {
                return; // No hay entrenadores
            }

            Pokemon p1 = entrenador1.getPokemonActual();
            Pokemon p2 = entrenador2.getPokemonActual();

            if (p1 == null || p2 == null) {
                return; // No hay Pokémon para mostrar
            }

            nombreP1.setText(p1.getNombre());
            nombreP2.setText(p2.getNombre());
            hp1.setText("<html>HP: " + p1.getPuntosSalud() + "<br>At: " + p1.getAtaque() + "<br>Df: " + p1.getDefensa() + "<br>Ats: " + p1.getAtaqueEspecial() + "<br>DeS: " + p1.getDefensaEspecial() + "</html>");
            hp2.setText("<html>HP: " + p2.getPuntosSalud() + "<br>At: " + p2.getAtaque() + "<br>Df: " + p2.getDefensa() + "<br>Ats: " + p2.getAtaqueEspecial() + "<br>DeS: " + p2.getDefensaEspecial() + "</html>");

            String turno = turnoJugador1 ? "Turno de " + entrenador1.getNombre() : "Turno de " + entrenador2.getNombre();
            frame.setTitle("Simulador de Batalla Pokémon - " + turno);

            ataquesBox.removeAllItems();
            Pokemon atacante = turnoJugador1 ? p1 : p2;
            for (Ataque a : atacante.getAtaques()) {
                ataquesBox.addItem(a.getNombre());
            }

            // Mostrar GIFs correctamente
            ImageIcon icon1 = cargarGif(p1.getGifPath());
            if (icon1 != null) {
                gif1.setIcon(icon1);
                gif1.setText("");
            } else {
                gif1.setIcon(null);
                gif1.setText("GIF no encontrado");
            }

            ImageIcon icon2 = cargarGif(p2.getGifPath());
            if (icon2 != null) {
                gif2.setIcon(icon2);
                gif2.setText("");
            } else {
                gif2.setIcon(null);
                gif2.setText("GIF no encontrado");
            }

            // Forzar actualización de los componentes
            gif1.revalidate();
            gif1.repaint();
            gif2.revalidate();
            gif2.repaint();
        });
    }

    private void ejecutarTurno() {
        // El atacante y defensor dependen del turno
        Pokemon atacante = turnoJugador1 ? entrenador1.getPokemonActual() : entrenador2.getPokemonActual();
        Pokemon defensor = turnoJugador1 ? entrenador2.getPokemonActual() : entrenador1.getPokemonActual();

        int ataqueIndex = ataquesBox.getSelectedIndex();
        if (ataqueIndex < 0) {
            JOptionPane.showMessageDialog(frame, "Selecciona un ataque antes de continuar.");
            return;
        }

        try {
            Ataque ataqueSeleccionado = atacante.getAtaques()[ataqueIndex];
            int danio = atacante.calcularDanio(ataqueSeleccionado, defensor);
            defensor.recibirDanio(danio);

            // Guardar el movimiento en el historial
            String movimiento = atacante.getNombre() + " usó " + ataqueSeleccionado.getNombre() +
                    " contra " + defensor.getNombre() + " y causó " + danio + " de daño.";
            historialMovimientos.push(movimiento);

            JOptionPane.showMessageDialog(frame, atacante.getNombre() + " usó " + ataqueSeleccionado.getNombre() + " y causó " + danio + " puntos de daño.");

            if (defensor.getPuntosSalud() <= 0) {
                String debil = defensor.getNombre() + " se ha debilitado.";
                historialMovimientos.push(debil);
                JOptionPane.showMessageDialog(frame, debil);
                if (turnoJugador1) {
                    entrenador2.siguientePokemon();
                    if (entrenador2.todosDebilitados()) {
                        finalizarJuego();
                        return;
                    }
                } else {
                    entrenador1.siguientePokemon();
                    if (entrenador1.todosDebilitados()) {
                        finalizarJuego();
                        return;
                    }
                }
            }

            // Cambia el turno para el siguiente jugador
            turnoJugador1 = !turnoJugador1;
            actualizarPantalla();
        } catch (Excepciones.PokemonDebilitadoException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }
    }

    private void finalizarJuego() {
        if (entrenador1.todosDebilitados() || entrenador2.todosDebilitados()) {
            String ganador = entrenador1.todosDebilitados() ? entrenador2.getNombre() : entrenador1.getNombre();
            JOptionPane.showMessageDialog(frame, "¡" + ganador + " gana la batalla!");
            int resp = JOptionPane.showConfirmDialog(frame, "¿Deseas jugar otra vez?", "Fin de la batalla", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                frame.dispose();
                new Interfaz();
            } else {
                frame.dispose();
            }
        }
    }

    // Constructor que recibe los entrenadores
    public Interfaz(Entrenador entrenador1, Entrenador entrenador2) {
        this.entrenador1 = entrenador1;
        this.entrenador2 = entrenador2;

        frame = new JFrame("Simulador de Batalla Pokémon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new CardLayout());

        inicializarInicio();
        inicializarBatalla();

        if (entrenador1 != null && entrenador2 != null) {
            elegirPokemonInicial(); // Si los entrenadores ya están definidos, pasa directamente a la batalla
        }

        frame.setVisible(true); // Asegúrate de que la ventana sea visible
    }

    // Constructor vacío para compatibilidad con otras partes del código
    public Interfaz() {
        frame = new JFrame("Simulador de Batalla Pokémon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new CardLayout());

        inicializarInicio();
        inicializarBatalla();

        frame.setVisible(true);
    }

    private void iniciarInterfaz() {
        // Implementa la lógica para inicializar la GUI
        System.out.println("Interfaz gráfica iniciada con los entrenadores:");
        System.out.println("Entrenador 1: " + (entrenador1 != null ? entrenador1.getNombre() : "No definido"));
        System.out.println("Entrenador 2: " + (entrenador2 != null ? entrenador2.getNombre() : "No definido"));
    }

    public void volverModoConsola() {
        int respuesta = JOptionPane.showConfirmDialog(frame, "¿Deseas volver al modo consola?", "Modo Consola", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            frame.dispose();
            System.out.println("Volviendo al modo consola...");
        } else {
            System.out.println("Permaneciendo en la interfaz gráfica.");
        }
    }

    // Método para manejar el turno in consola
    private static void manejarTurnoConsola(Scanner scanner, Entrenador atacante, Entrenador defensor) {
        System.out.println("\nTurno de " + atacante.getNombre());
        Pokemon pokemonAtacante = atacante.getPokemonActual();
        Pokemon pokemonDefensor = defensor.getPokemonActual();

        System.out.println("Tu Pokémon: " + pokemonAtacante.getNombre() + " (HP: " + pokemonAtacante.getPuntosSalud() + ")");
        System.out.println("Pokémon rival: " + pokemonDefensor.getNombre() + " (HP: " + pokemonDefensor.getPuntosSalud() + ")");

        System.out.println("Elige un ataque:");
        for (int i = 0; i < pokemonAtacante.getAtaques().length; i++) {
            System.out.println((i + 1) + ". " + pokemonAtacante.getAtaques()[i].getNombre());
        }

        int ataqueIndex = -1;
        while (ataqueIndex < 0 || ataqueIndex >= pokemonAtacante.getAtaques().length) {
            System.out.print("Ingresa el número del ataque: ");
            ataqueIndex = scanner.nextInt() - 1;
            if (ataqueIndex < 0 || ataqueIndex >= pokemonAtacante.getAtaques().length) {
                System.out.println("Ataque no válido. Inténtalo de nuevo.");
            }
        }

        int danio = pokemonAtacante.calcularDanio(pokemonAtacante.getAtaques()[ataqueIndex], pokemonDefensor);
        pokemonDefensor.recibirDanio(danio);
        System.out.println(pokemonAtacante.getNombre() + " usó " + pokemonAtacante.getAtaques()[ataqueIndex].getNombre() + " y causó " + danio + " puntos de daño.");

        if (pokemonDefensor.getPuntosSalud() <= 0) {
            System.out.println(pokemonDefensor.getNombre() + " se ha debilitado.");
            defensor.siguientePokemon();
        }
    }

    private void cambiarPokemon() {
        // Determina el entrenador actual según el turno
        Entrenador actual = turnoJugador1 ? entrenador1 : entrenador2;
        // Obtiene el Pokémon actualmente en batalla
        Pokemon actualPokemon = actual.getPokemonActual();

        // Construir lista de opciones de Pokémon disponibles para cambiar (excluyendo el actual)
        ArrayList<Pokemon> disponibles = new ArrayList<>();
        for (Pokemon p : actual.getEquipo()) {
            if (p != actualPokemon) { // Solo agrega si no es el Pokémon actual
                disponibles.add(p);
            }
        }
        // Si no hay otros Pokémon disponibles, muestra mensaje y termina
        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No tienes otros Pokémon disponibles.");
            return;
        }

        // Prepara los nombres de los Pokémon disponibles para mostrar en el diálogo
        String[] nombres = disponibles.stream().map(Pokemon::getNombre).toArray(String[]::new);
        // Muestra un cuadro de diálogo para seleccionar el Pokémon al que se desea cambiar
        String seleccion = (String) JOptionPane.showInputDialog(
                frame,
                "Selecciona el Pokémon al que deseas cambiar:",
                "Cambiar Pokémon",
                JOptionPane.PLAIN_MESSAGE,
                null,
                nombres,
                nombres[0]
        );
        // Si el usuario seleccionó un Pokémon 
        if (seleccion != null) {
            for (Pokemon p : disponibles) {
                if (p.getNombre().equals(seleccion)) { // Encuentra el Pokémon seleccionado
                    try {
                        // Si el Pokémon está debilitado, lanza la excepción personalizada
                        if (p.getPuntosSalud() <= 0) {
                            throw new Excepciones.PokemonDebilitadoException("No puedes cambiar a un Pokémon debilitado.");
                        }
                        // Cambia el Pokémon actual del entrenador
                        actual.setPokemonInicial(p);

                        // Guardar el cambio en el historial //
                        String cambio = actual.getNombre() + " cambió a " + p.getNombre() + ".";
                        historialMovimientos.push(cambio);

                        // Muestra mensaje de éxito
                        JOptionPane.showMessageDialog(frame, "¡Has cambiado a " + p.getNombre() + "!");
                        // Cambia el turno y actualiza la pantalla solo si el cambio fue exitoso
                        turnoJugador1 = !turnoJugador1;
                        actualizarPantalla();
                    } catch (Excepciones.PokemonDebilitadoException e) {
                        // Si ocurre la excepción, muestra el mensaje de error y no cambia el turno
                        JOptionPane.showMessageDialog(frame, e.getMessage());
                    }
                    break; // Sale del ciclo después de procesar la selección
                }
            }
        }
    }

    // Buscar Pokémon por nombre
    private Pokemon buscarPorNombre(String nombre) {
        return pokemonsPorNombre.get(nombre);
    }

    // Buscar Pokémon por tipo 
    private ArrayList<Pokemon> buscarPorTipo(Pokemon.Tipo tipo) {
        return pokemonsPorTipo.getOrDefault(tipo, new ArrayList<>());
    }


    private void mostrarPokemonsFuego() {
        ArrayList<Pokemon> fuegos = buscarPorTipo(Pokemon.Tipo.FUEGO);
        StringBuilder sb = new StringBuilder("Pokémon de tipo FUEGO:\n");
        for (Pokemon p : fuegos) {
            sb.append(p.getNombre()).append("\n");
        }
        JOptionPane.showMessageDialog(frame, sb.toString());
    }

    // Prepara la lista enlazada de turnos según la velocidad de los Pokémon //
    private void prepararOrdenTurnos() {
        ordenTurnos.clear();
        // Solo agrega los Pokémon actuales de cada entrenador que no estén debilitados
        Pokemon p1 = entrenador1.getPokemonActual();
        Pokemon p2 = entrenador2.getPokemonActual();
        if (p1 != null && p1.getPuntosSalud() > 0) ordenTurnos.add(p1);
        if (p2 != null && p2.getPuntosSalud() > 0) ordenTurnos.add(p2);
        // Ordena por velocidad descendente (el más rápido va primero)
        ordenTurnos.sort((pokeA, pokeB) -> Integer.compare(pokeB.getVelocidad(), pokeA.getVelocidad()));
    }

    // Método para guardar la partida en un archivo de texto
    private void guardarPartida() {
        // Crea la carpeta si no existe
        File carpeta = new File("src/partidas_txt");
        if (!carpeta.exists()) carpeta.mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter("src/partidas_txt/partida_guardada.txt"))) {
            // Guardar nombres de entrenadores
            writer.println(entrenador1.getNombre());
            writer.println(entrenador2.getNombre());
            // Guardar equipos (nombre, hp)
            for (Pokemon p : entrenador1.getEquipo()) {
                writer.println("E1;" + p.getNombre() + ";" + p.getPuntosSalud());
            }
            writer.println("E1_ACTUAL;" + entrenador1.getPokemonActual().getNombre());
            for (Pokemon p : entrenador2.getEquipo()) {
                writer.println("E2;" + p.getNombre() + ";" + p.getPuntosSalud());
            }
            writer.println("E2_ACTUAL;" + entrenador2.getPokemonActual().getNombre());
            // Guardar de quién es el turno
            writer.println("TURNO;" + (turnoJugador1 ? "1" : "2"));
            // Guardar historial de movimientos
            for (String mov : historialMovimientos) {
                writer.println("HIST;" + mov.replace("\n", "\\n"));
            }
            JOptionPane.showMessageDialog(frame, "¡Partida guardada exitosamente!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error al guardar la partida: " + e.getMessage());
        }
    }

    // Método para cargar la partida desde un archivo de texto
    private void cargarPartida() {
        File archivo = new File("src/partidas_txt/partida_guardada.txt");
        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(frame, "No hay ninguna partida guardada.");
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
            historialMovimientos.clear();
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("E1;")) {
                    String[] parts = linea.split(";");
                    Pokemon p = new Pokemon(buscarEnumPorNombre(parts[1]));
                    p.recibirDanio(p.getPuntosSalud() - Integer.parseInt(parts[2])); // Ajusta HP
                    equipo1.add(p);
                } else if (linea.startsWith("E2;")) {
                    String[] parts = linea.split(";");
                    Pokemon p = new Pokemon(buscarEnumPorNombre(parts[1]));
                    p.recibirDanio(p.getPuntosSalud() - Integer.parseInt(parts[2]));
                    equipo2.add(p);
                } else if (linea.startsWith("E1_ACTUAL;")) {
                    actual1 = linea.split(";")[1];
                } else if (linea.startsWith("E2_ACTUAL;")) {
                    actual2 = linea.split(";")[1];
                } else if (linea.startsWith("TURNO;")) {
                    turnoJugador1 = linea.split(";")[1].equals("1");
                } else if (linea.startsWith("HIST;")) {
                    historialMovimientos.push(linea.substring(5).replace("\\n", "\n"));
                }
            }
            entrenador1.setEquipo(equipo1);
            entrenador2.setEquipo(equipo2);
            // Setear el Pokémon actual
            for (Pokemon p : equipo1) if (p.getNombre().equals(actual1)) entrenador1.setPokemonInicial(p);
            for (Pokemon p : equipo2) if (p.getNombre().equals(actual2)) entrenador2.setPokemonInicial(p);
            prepararBusquedasRapidas();
            actualizarPantalla();
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "batalla");
            JOptionPane.showMessageDialog(frame, "¡Partida cargada exitosamente!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error al cargar la partida: " + e.getMessage());
        }
    }

    // Método auxiliar para buscar el enum por nombre
    private PokemonEnum buscarEnumPorNombre(String nombre) {
        for (PokemonEnum pe : PokemonEnum.values()) {
            if (pe.getNombre().equals(nombre)) return pe;
        }
        return null;
    }
}
