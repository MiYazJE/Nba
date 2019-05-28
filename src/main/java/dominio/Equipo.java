/**
 * @author Ruben Saiz
 */
package dominio;

public class Equipo {

    private String nombre;
    private String ciudad;
    private String conferencia;
    private String division;
    private String Imagen;

    public Equipo(String nombre, String ciudad, String conferencia, String division, String imagen) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.conferencia = conferencia;
        this.division = division;
        Imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getConferencia() {
        return conferencia;
    }

    public void setConferencia(String conferencia) {
        conferencia = conferencia;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        division = division;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    @Override
    public String toString() {
        return "Equipo{" +
                "nombre='" + nombre + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", conferencia='" + conferencia + '\'' +
                ", division='" + division + '\'' +
                ", Imagen='" + Imagen + '\'' +
                '}';
    }

}
