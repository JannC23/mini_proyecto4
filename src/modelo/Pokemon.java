package modelo;
import java.util.ArrayList;
import java.util.Scanner;

public class Pokemon {
    private String nombre;
    private Tipo tipo;
    private int puntosSalud;
    private int ataque; // Nuevo atributo
    private int defensa; // Nuevo atributo
    private int ataqueEspecial; // Nuevo atributo
    private int defensaEspecial; // Nuevo atributo
    private int velocidad; // Nuevo atributo
    private Ataque[] ataques = new Ataque[4];
    private String gifPath; // Agrega este atributo

    // Constructor que usa el enum PokemonEnum
    public Pokemon(PokemonEnum pokemonEnum) {
        this.nombre = pokemonEnum.getNombre();
        this.tipo = pokemonEnum.getTipo();
        this.puntosSalud = pokemonEnum.getPuntosSalud();
        this.ataque = pokemonEnum.getAtaque(); // Inicializar ataque
        this.defensa = pokemonEnum.getDefensa(); // Inicializar defensa
        this.ataqueEspecial = pokemonEnum.getAtaqueEspecial(); // Inicializar ataque especial
        this.defensaEspecial = pokemonEnum.getDefensaEspecial(); // Inicializar defensa especial
        this.velocidad = pokemonEnum.getVelocidad(); // Inicializar velocidad
        this.gifPath = pokemonEnum.getGifPath(); // NUEVO
        asignarAtaquesPredeterminados(); // Asignar ataques al crear el Pokémon
    }

    // Getters y setters para los nuevos atributos
    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public int getAtaqueEspecial() {
        return ataqueEspecial;
    }

    public void setAtaqueEspecial(int ataqueEspecial) {
        this.ataqueEspecial = ataqueEspecial;
    }

    public int getDefensaEspecial() {
        return defensaEspecial;
    }

    public void setDefensaEspecial(int defensaEspecial) {
        this.defensaEspecial = defensaEspecial;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public String getGifPath() {
        return gifPath;
    }

    private void asignarAtaquesPredeterminados() {
        switch (this.tipo) {
            case FUEGO:
                this.ataques[0] = new Ataque("Lanzallamas", "Especial", 50);
                this.ataques[1] = new Ataque("Ascuas", "Especial", 30);
                this.ataques[2] = new Ataque("Giro Fuego", "Especial", 40);
                this.ataques[3] = new Ataque("Colmillo Ígneo", "Físico", 45);
                break;
            case AGUA:
                this.ataques[0] = new Ataque("Pistola Agua", "Especial", 40);
                this.ataques[1] = new Ataque("Hidrobomba", "Especial", 60);
                this.ataques[2] = new Ataque("Surf", "Especial", 50);
                this.ataques[3] = new Ataque("Aqua Jet", "Físico", 35);
                break;
            case PLANTA:
                this.ataques[0] = new Ataque("Hoja Afilada", "Especial", 50);
                this.ataques[1] = new Ataque("Latigazo", "Físico", 40);
                this.ataques[2] = new Ataque("Rayo Solar", "Especial", 60);
                this.ataques[3] = new Ataque("Drenadoras", "Especial", 30);
                break;
            case ELECTRICO:
                this.ataques[0] = new Ataque("Impactrueno", "Especial", 40);
                this.ataques[1] = new Ataque("Rayo", "Especial", 50);
                this.ataques[2] = new Ataque("Trueno", "Especial", 60);
                this.ataques[3] = new Ataque("Chispa", "Físico", 35);
                break;
        }
    }

    public enum Tipo {
        FUEGO, PLANTA, ELECTRICO, AGUA;
    }

    public String getNombre() {
        return nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getPuntosSalud() {
        return puntosSalud;
    }

    public void recibirDanio(int danio) {
        this.puntosSalud -= danio;
        if (this.puntosSalud < 0) {
            this.puntosSalud = 0;
        }
    }

    public Ataque[] getAtaques() {
        return ataques;
    }

    public void setAtaques(Ataque[] ataques) {
        this.ataques = ataques;
    }

    public int calcularDanio(Ataque ataque, Pokemon oponente) {
        if (this.puntosSalud <= 0) {
            throw new Excepciones.PokemonDebilitadoException("No puedes usar un Pokémon debilitado: " + this.nombre);
        }
        if (oponente.puntosSalud <= 0) {
            throw new Excepciones.PokemonDebilitadoException("No puedes atacar a un Pokémon ya debilitado: " + oponente.nombre);
        }
        int nivel = 50; // Nivel fijo
        int ataqueUsado = ataque.getTipoDano().equals("Físico") ? this.ataque : this.ataqueEspecial;
        int defensaOponente = ataque.getTipoDano().equals("Físico") ? oponente.getDefensa() : oponente.getDefensaEspecial();

        // Fórmula de daño
        int danioBase = (int) (((2 * nivel / 5 + 2) * ataque.getPotencia() * ataqueUsado / defensaOponente) / 50) + 2;

        // Aplicar ventaja de tipo
        if (tieneVentaja(oponente)) {
            danioBase *= 2; // Súper efectivo
        } else if (oponente.tieneVentaja(this)) {
            danioBase /= 2; // Poco efectivo
        }

        return danioBase;    }

    private boolean tieneVentaja(Pokemon oponente) {
        if (this.tipo == Tipo.FUEGO && oponente.tipo == Tipo.PLANTA) return true;
        if (this.tipo == Tipo.AGUA && oponente.tipo == Tipo.FUEGO) return true;
        if (this.tipo == Tipo.PLANTA && oponente.tipo == Tipo.AGUA) return true;
        return false;
    }

    public static ArrayList<Pokemon> seleccionarEquipoPokemon() {
        Scanner entrada = new Scanner(System.in);
        PokemonEnum[] listaPokemon = PokemonEnum.values();
        ArrayList<Pokemon> equipo = new ArrayList<>();
        boolean[] ocupados = new boolean[listaPokemon.length];

        for (int j = 0; j < 3; j++) {
            System.out.println("\nSelecciona el Pokemon " + (j + 1) + ":");
            for (int i = 0; i < listaPokemon.length; i++) {
                System.out.println((i + 1) + ". " + listaPokemon[i].getNombre() + " (TIPO: " + listaPokemon[i].getTipo() + ") - VIDA: " + listaPokemon[i].getPuntosSalud());
            }

            int opcion = -1;
            while (opcion < 1 || opcion > listaPokemon.length || ocupados[opcion - 1]) {
                System.out.print("\nIngresa el número del Pokemon que deseas (1-" + listaPokemon.length + "): ");
                if (entrada.hasNextInt()) {
                    opcion = entrada.nextInt();
                    if (opcion < 1 || opcion > listaPokemon.length) {
                        System.out.println("Opción no válida. Intenta nuevamente.");
                    } else if (ocupados[opcion - 1]) {
                        // Lanza la excepción si el Pokémon ya fue elegido
                        throw new Excepciones.PokemonRepetidoEnEquipoException("No puedes elegir el mismo Pokémon dos veces en tu equipo.");
                    }
                } else {
                    System.out.println("Entrada inválida. Ingresa un número.");
                    entrada.next(); // Consumir entrada inválida
                }
            }

            equipo.add(new Pokemon(listaPokemon[opcion - 1]));
            ocupados[opcion - 1] = true;
        }

        return equipo;
    }

    public static ArrayList<Pokemon> crearPokemonAleatorio() {
        PokemonEnum[] listaPokemon = PokemonEnum.values();
        ArrayList<Pokemon> equipo = new ArrayList<>(); // Lista para almacenar los 3 Pokemon seleccionados
        boolean[] ocupados = new boolean[listaPokemon.length]; // Array para marcar los Pokemon seleccionados

        for (int i = 0; i < 3; i++) { // Generar 3 Pokemon aleatorios
            Pokemon pokemonAleatorio;
            int numeroAleatorio;

            do {
                numeroAleatorio = (int) (Math.random() * listaPokemon.length); // Generar un numero aleatorio
                if (!ocupados[numeroAleatorio]) { // Verificar si el Pokemon no ha sido seleccionado antes
                    pokemonAleatorio = new Pokemon(listaPokemon[numeroAleatorio]); // Crear el Pokemon
                    ocupados[numeroAleatorio] = true; // Marcar como seleccionado
                    break;
                } else {
                    pokemonAleatorio = null; // Reiniciar si ya fue seleccionado
                }
            } while (pokemonAleatorio == null); // Repetir hasta que se genere un Pokemon valido

            equipo.add(pokemonAleatorio); // Agregar al equipo
        }

        return equipo; // Retorna el equipo completo
    }
}
