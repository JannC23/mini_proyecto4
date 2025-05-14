public enum PokemonEnum {
<<<<<<< HEAD
    CHARMANDER(1, "Charmander", Pokemon.Tipo.FUEGO, 180, 52, 43, 60, 50, 65, "charmander.gif"),
    BULBASAUR(2, "Bulbasaur", Pokemon.Tipo.PLANTA, 200, 49, 49, 65, 65, 45, "bulbasaur.gif"),
    PIKACHU(3, "Pikachu", Pokemon.Tipo.ELECTRICO, 150, 55, 40, 50, 50, 90, "pikachu.gif"),
    SQUIRTLE(4, "Squirtle", Pokemon.Tipo.AGUA, 220, 48, 65, 50, 64, 43, "squirtle.gif"),
    PSYDUCK(5, "Psyduck", Pokemon.Tipo.AGUA, 190, 52, 48, 65, 50, 55, "psyduck.gif"),
    JOLTEON(6, "Jolteon", Pokemon.Tipo.ELECTRICO, 200, 65, 60, 110, 95, 130, "jolteon.gif"),
    FLAREON(7, "Flareon", Pokemon.Tipo.FUEGO, 230, 130, 60, 95, 110, 65, "flareon.gif"),
    VAPOREON(8, "Vaporeon", Pokemon.Tipo.AGUA, 260, 65, 60, 110, 95, 65, "vaporeon.gif"),
    GROWLITHE(9, "Growlithe", Pokemon.Tipo.FUEGO, 190, 70, 45, 70, 50, 60, "growlithe.gif");
=======
    CHARMANDER(1, "Charmander", Pokemon.Tipo.FUEGO, 180, 52, 43, 60, 50, 65),
    BULBASAUR(2, "Bulbasaur", Pokemon.Tipo.PLANTA, 200, 49, 49, 65, 65, 45),
    PIKACHU(3, "Pikachu", Pokemon.Tipo.ELECTRICO, 150, 55, 40, 50, 50, 90),
    SQUIRTLE(4, "Squirtle", Pokemon.Tipo.AGUA, 220, 48, 65, 50, 64, 43);
>>>>>>> origin/main

    private final int numero;
    private final String nombre;
    private final Pokemon.Tipo tipo;
    private final int puntosSalud;
    private final int ataque;
    private final int defensa;
    private final int ataqueEspecial;
    private final int defensaEspecial;
    private final int velocidad;
<<<<<<< HEAD
    private final String gifPath;

    PokemonEnum(int numero, String nombre, Pokemon.Tipo tipo, int puntosSalud, int ataque, int defensa, int ataqueEspecial, int defensaEspecial, int velocidad, String gifPath) {
=======

    PokemonEnum(int numero, String nombre, Pokemon.Tipo tipo, int puntosSalud, int ataque, int defensa, int ataqueEspecial, int defensaEspecial, int velocidad) {
>>>>>>> origin/main
        this.numero = numero;
        this.nombre = nombre;
        this.tipo = tipo;
        this.puntosSalud = puntosSalud;
        this.ataque = ataque;
        this.defensa = defensa;
        this.ataqueEspecial = ataqueEspecial;
        this.defensaEspecial = defensaEspecial;
        this.velocidad = velocidad;
<<<<<<< HEAD
        this.gifPath = gifPath;
=======
>>>>>>> origin/main
    }

    public int getNumero() {
        return numero;
    }

    public String getNombre() {
        return nombre;
    }

    public Pokemon.Tipo getTipo() {
        return tipo;
    }

    public int getPuntosSalud() {
        return puntosSalud;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getAtaqueEspecial() {
        return ataqueEspecial;
    }

    public int getDefensaEspecial() {
        return defensaEspecial;
    }

    public int getVelocidad() {
        return velocidad;
    }
<<<<<<< HEAD

    public String getGifPath() {
        return gifPath;
    }
=======
>>>>>>> origin/main
}
