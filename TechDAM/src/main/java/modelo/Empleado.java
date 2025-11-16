package modelo;

import java.math.BigDecimal;

/**
 * Clase de modelo que representa la entidad 'empleados' de la base de datos techDAM.
 * Explicación académica:
 * - Uso BigDecimal para el salario porque es el tipo adecuado para manejar dinero
 *   evitando errores de precisión (requisito CE2.2).
 * - Incluyo constructor vacío, constructor con parámetros, getters/setters y toString()
 *   tal como exige el apartado 2.2 de la rúbrica.
 */
public class Empleado {

    private int id;
    private String nombre;
    private String departamento;
    private BigDecimal salario;
    private boolean activo;

    // Constructor vacío
    public Empleado() {}

    // Constructor completo
    public Empleado(int id, String nombre, String departamento, BigDecimal salario, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.departamento = departamento;
        this.salario = salario;
        this.activo = activo;
    }

    // Getters y setters obligatorios (CE2.2)
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

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", departamento='" + departamento + '\'' +
                ", salario=" + salario +
                ", activo=" + activo +
                '}';
    }
}
