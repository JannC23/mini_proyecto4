package modelo;

// Clase contenedora para todas las excepciones personalizadas
public class Excepciones {
    // Excepción para cuando se intenta usar un Pokémon debilitado
    public static class PokemonDebilitadoException extends RuntimeException {
        public PokemonDebilitadoException(String mensaje) {
            super(mensaje);
        }
    }

    // Excepción para cuando se intenta agregar el mismo Pokémon dos veces al equipo
    public static class PokemonRepetidoEnEquipoException extends RuntimeException {
        public PokemonRepetidoEnEquipoException(String mensaje) {
            super(mensaje);
        }
    }
}