package modelo;

/**
 * Objeto jugador
 */

public class Jugador {

    private String Nombre;
    private String Procedencia;
    private String Peso;


    /**
     * Contructor de jugador vacio
     */
    public Jugador() {
        this(null, null, null);
    }


    /**
     * Contructor de jugador con 3 parametros
     * @param nombre
     * @param procedencia
     * @param peso
     */
    public Jugador(String nombre, String procedencia, String peso) {
        this.Nombre = nombre;
        this.Procedencia = procedencia;
        this.Peso = peso;
    }


    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getProcedencia() {
        return Procedencia;
    }

    public void setProcedencia(String procedencia) {
        Procedencia = procedencia;
    }

    public String getPeso() {
        return Peso;
    }

    public void setPeso(String peso) {
        Peso = peso;
    }


    @Override
    public String toString() {
        return "Jugador{" +
                "Nombre='" + Nombre + '\'' +
                ", Procedencia='" + Procedencia + '\'' +
                ", Peso='" + Peso + '\'' +
                '}';
    }

}
