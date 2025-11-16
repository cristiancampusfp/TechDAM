package modelo;

import java.time.LocalDate;

/**
 * Clase de dominio que corresponde a la tabla 'asignaciones'.
 * Explicación del tipo LocalDate:
 * - Es el tipo moderno recomendado para fechas en Java (no usa java.sql.Date).
 * - Evita problemas de zona horaria y formato.
 */
public class Asignacion {

    private int id;
    private int idEmpleado;
    private int idProyecto;
    private LocalDate fechaAsignacion;

    // Constructor vacío
    public Asignacion() {}

    // Constructor completo
    public Asignacion(int id, int idEmpleado, int idProyecto, LocalDate fechaAsignacion) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.idProyecto = idProyecto;
        this.fechaAsignacion = fechaAsignacion;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    @Override
    public String toString() {
        return "Asignacion{" +
                "id=" + id +
                ", idEmpleado=" + idEmpleado +
                ", idProyecto=" + idProyecto +
                ", fechaAsignacion=" + fechaAsignacion +
                '}';
    }
}
