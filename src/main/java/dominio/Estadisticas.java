package dominio;

public class Estadisticas {

    private String temporada;
    private String puntosPartido;
    private String asistenciasPartido;
    private String taponesPartido;
    private String rebotesPartido;

    public Estadisticas(String temporada, String puntosPartido, String asistenciasPartido,
                        String taponesPartido, String rebotesPartido) {
        this.temporada = temporada;
        this.puntosPartido = puntosPartido;
        this.asistenciasPartido = asistenciasPartido;
        this.taponesPartido = taponesPartido;
        this.rebotesPartido = rebotesPartido;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    public String getPuntosPartido() {
        return puntosPartido;
    }

    public void setPuntosPartido(String puntosPartido) {
        this.puntosPartido = puntosPartido;
    }

    public String getAsistenciasPartido() {
        return asistenciasPartido;
    }

    public void setAsistenciasPartido(String asistenciasPartido) {
        this.asistenciasPartido = asistenciasPartido;
    }

    public String getTaponesPartido() {
        return taponesPartido;
    }

    public void setTaponesPartido(String taponesPartido) {
        this.taponesPartido = taponesPartido;
    }

    public String getRebotesPartido() {
        return rebotesPartido;
    }

    public void setRebotesPartido(String rebotesPartido) {
        this.rebotesPartido = rebotesPartido;
    }

    @Override
    public String toString() {
        return "Estadisticas{" +
                "temporada='" + temporada + '\'' +
                ", puntosPartido='" + puntosPartido + '\'' +
                ", asistenciasPartido='" + asistenciasPartido + '\'' +
                ", taponesPartido='" + taponesPartido + '\'' +
                ", rebotesPartido='" + rebotesPartido + '\'' +
                '}';
    }

}
