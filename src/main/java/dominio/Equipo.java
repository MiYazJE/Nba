/**
 * @author Ruben Saiz
 */
package dominio;

public class Equipo {

    private String nombre;
    private String ciudad;
    private String Conferencia;
    private String Division;
    private String Imagen;

    public Equipo(String nombre, String ciudad, String conferencia, String division, String imagen) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        Conferencia = conferencia;
        Division = division;
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
        return Conferencia;
    }

    public void setConferencia(String conferencia) {
        Conferencia = conferencia;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

}
