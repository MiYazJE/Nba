package dominio;

/**
 * Objeto jugador
 */

public class Jugador {

    private String nombre;
    private String procedencia;
    private String peso;
    private String altura;
    private String equipo;
    private String posicion;


    /**
     * Contructor de jugador con 6 parámetros
     * @param nombre
     * @param procedencia
     * @param peso
     * @param altura
     * @param posicion
     * @param equipo
     */
    public Jugador(String nombre, String procedencia, String altura, String peso, String posicion, String equipo) {
        this.nombre = nombre;
        this.peso = peso;
        this.procedencia = procedencia;
        this.altura = altura;
        this.posicion = posicion;
        this.equipo = equipo;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        nombre = nombre;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        procedencia = procedencia;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        peso = peso;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                ", procedencia='" + procedencia + '\'' +
                ", peso='" + peso + '\'' +
                ", altura='" + altura + '\'' +
                ", equipo='" + equipo + '\'' +
                ", posicion='" + posicion + '\'' +
                '}';
    }

}
