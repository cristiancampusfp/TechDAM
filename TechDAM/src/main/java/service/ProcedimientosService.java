package service;

import config.DatabaseConfigPool;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Servicio para invocar procedimientos almacenados en la base de datos TechDAM.
 *
 * Comentarios académicos:
 * - Uso de CallableStatement para invocar procedimientos (CE2.3).
 * - Manejo correcto de parámetros IN y OUT.
 * - Captura y muestra de resultados de parámetros OUT.
 * - Uso de try-with-resources para garantizar cierre de recursos y evitar fugas.
 */
public class ProcedimientosService {

    /**
     * Invoca el procedimiento actualizar_salario_departamento
     * para incrementar salarios de un departamento específico.
     *
     * @param departamento Nombre del departamento
     * @param porcentaje Porcentaje de incremento
     * @return Número de empleados afectados, -1 si hubo error
     */
    public int actualizarSalariosDepartamento(String departamento, double porcentaje) {
        String sql = "{call actualizar_salario_departamento(?, ?, ?)}";

        try (Connection conn = DatabaseConfigPool.getConexion();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            // Parámetros IN
            cstmt.setString(1, departamento);
            cstmt.setBigDecimal(2, BigDecimal.valueOf(porcentaje));

            // Parámetro OUT
            cstmt.registerOutParameter(3, Types.INTEGER);

            // Ejecutar procedimiento
            cstmt.execute();

            // Obtener valor OUT
            int empleadosActualizados = cstmt.getInt(3);
            System.out.println("Empleados actualizados: " + empleadosActualizados);

            return empleadosActualizados;

        } catch (SQLException e) {
            System.err.println("Error ejecutando procedimiento actualizar_salario_departamento: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Segundo procedimiento de ejemplo: asignar empleado a proyecto.
     * Parámetros IN: idEmpleado, idProyecto
     * Parámetros OUT: resultado (0 = fallo, 1 = éxito)
     *
     * @param idEmpleado ID del empleado
     * @param idProyecto ID del proyecto
     * @return 1 si asignación exitosa, 0 si falla
     */
    public int asignarEmpleadoAProyecto(int idEmpleado, int idProyecto) {
        String sql = "{call asignar_empleado_proyecto(?, ?, ?)}";

        try (Connection conn = DatabaseConfigPool.getConexion();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            // Parámetros IN
            cstmt.setInt(1, idEmpleado);
            cstmt.setInt(2, idProyecto);

            // Parámetro OUT
            cstmt.registerOutParameter(3, Types.INTEGER);

            // Ejecutar procedimiento
            cstmt.execute();

            int resultado = cstmt.getInt(3);
            if (resultado == 1) {
                System.out.println("Empleado " + idEmpleado + " asignado correctamente al proyecto " + idProyecto);
            } else {
                System.out.println("No se pudo asignar el empleado " + idEmpleado + " al proyecto " + idProyecto);
            }

            return resultado;

        } catch (SQLException e) {
            System.err.println("Error ejecutando procedimiento asignar_empleado_proyecto: " + e.getMessage());
            return 0;
        }
    }
}
