import java.time.LocalTime;
import java.time.Duration;

public class Cliente {
    private String turno; 
    private String nombre;
    private String identificacion;
    private String tipoTramite;
    private boolean esPreferencial;
    private LocalTime horaLlegada;

    public Cliente(String turno, String nombre, String identificacion, String tipoTramite, boolean esPreferencial) {
        this.turno = turno;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.tipoTramite = tipoTramite;
        this.esPreferencial = esPreferencial;
        this.horaLlegada = LocalTime.now();
    }

    public String getTurno() { return turno; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getIdentificacion() { return identificacion; }
    
    public String getTipoTramite() { return tipoTramite; }
    public void setTipoTramite(String tipoTramite) { this.tipoTramite = tipoTramite; }
    
    public boolean isEsPreferencial() { return esPreferencial; }
    
    public LocalTime getHoraLlegada() { return horaLlegada; }

    public String getTiempoEspera() {
        Duration duracion = Duration.between(horaLlegada, LocalTime.now());
        return duracion.toMinutes() + " min y " + (duracion.getSeconds() % 60) + " seg.";
    }

    @Override
    public String toString() {
        String tipo = esPreferencial ? "[PREFERENCIAL] " : "";
        return tipo + "Turno: " + turno + " | " + nombre + " | ID: " + identificacion + " | Trámite: " + tipoTramite;
    }
}