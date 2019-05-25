/**
 * @author Ruben Saiz
 */
package dominio;

public class Partido {

    private String local;
    private String visitante;
    private int puntosLocal;
    private int puntosVisitante;
    private String temporada;

    public Partido(String local, String visitante, int puntosLocal, int puntosVisitante, String temporada) {
        this.local = local;
        this.visitante = visitante;
        this.puntosLocal = puntosLocal;
        this.puntosVisitante = puntosVisitante;
        this.temporada = temporada;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getVisitante() {
        return visitante;
    }

    public void setVisitante(String visitante) {
        this.visitante = visitante;
    }

    public int getPuntosLocal() {
        return puntosLocal;
    }

    public void setPuntosLocal(int puntosLocal) {
        this.puntosLocal = puntosLocal;
    }

    public int getPuntosVisitante() {
        return puntosVisitante;
    }

    public void setPuntosVisitante(int puntosVisitante) {
        this.puntosVisitante = puntosVisitante;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    @Override
    public String toString() {
        return "Partido{" +
                "local='" + local + '\'' +
                ", visitante='" + visitante + '\'' +
                ", puntosLocal='" + puntosLocal + '\'' +
                ", puntosVisitante='" + puntosVisitante + '\'' +
                ", temporada='" + temporada + '\'' +
                '}';
    }

}
