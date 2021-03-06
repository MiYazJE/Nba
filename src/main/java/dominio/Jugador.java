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
    private String codigo;

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
        //pulgadasToMetros();
        //librasToKg();
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    /**
     * Convertir la altura de pies-pulgadas a metros
     */
    private void pulgadasToMetros() {

        /*
            Multiplicar los pies por 30.48
            Multiplicar las pulgadas por 2.54
            Metros = pies + pulgadas
         */

        this.altura = altura.trim();

        double pies = 0;
        double pulgadas;
        double metros;

        if (altura.contains("-")) {
            // PIES Y PULGADAS -> "5-7"

            pies     = Integer.valueOf(altura.split("-")[0]);
            pulgadas = Integer.valueOf(altura.split("-")[1]);

            pies *= 30.48;
            pulgadas *= 2.54;
            metros = pies + pulgadas;
        }
        else {
            // SOLO PIES -> "50"
            pies *= 30.48;
            metros = pies;
        }

        this.altura = String.valueOf( metros );
        refrescarAltura();
    }

    /**
     * Parsea el contenido de altura:
     *  1.- 1.43 -> 1,43
     *  2.- 1.433333434 -> 1,43
     */
    private void refrescarAltura() {
        this.altura = altura.replace(".", ",");
        if (altura.contains(",") && altura.length() > 4) {
            this.altura = altura.substring(0, 5);
        }
        this.altura = altura + " cm";
    }

    /**
     * Convierte el peso en libras a Kg
     */
    private void librasToKg() {

        double kg = Integer.valueOf(this.peso);
        kg = kg * 0.453592;
        this.peso = String.valueOf((int)kg);

    }

}
