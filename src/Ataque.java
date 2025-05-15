// Clase que representa un ataque que puede realizar un Pokémon
public class Ataque {
    public enum TipoDanio { FISICO, ESPECIAL } // Tipos de daño que puede causar un ataque
    private String nombre; // Nombre del ataque
    private String tipoDano; // Tipo de daño: "Físico" o "Especial"
    private int potencia; // Potencia del ataque

    // Constructor para inicializar un ataque con su nombre, tipo de daño y potencia
    public Ataque(String nombre, String tipoDano, int potencia) {
        this.nombre = nombre;
        this.tipoDano = tipoDano;
        this.potencia = potencia;
    }

    // Métodos para obtener los atributos del ataque
    public String getNombre() { return nombre; }
    public String getTipoDano() { return tipoDano; }
    public int getPotencia() { return potencia; }
}
