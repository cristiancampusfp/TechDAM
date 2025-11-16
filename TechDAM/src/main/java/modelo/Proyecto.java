package modelo;

import java.math.BigDecimal;

/**
 * Modelo de la tabla 'proyectos'.
 * Justificación didáctica:
 * - BigDecimal para manejar presupuesto con precisión exacta.
 * - Estructura estándar de POJO para posteriormente trabajar con JDBC.
 */
public class Proyecto {

    private int id;
    private String nombre;
    private BigDecimal presupuesto;

    // Constructor vacío
    public Proyecto() {}

    // Constructor completo
    public Proyecto(int id, String nombre, BigDecimal presupuesto) {
        this.id = id;
        this.nombre = nombre;
        this.presupuesto = presupuesto;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", presupuesto=" + presupuesto +
                '}';
    }
}
