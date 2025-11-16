package service;

import config.DatabaseConfigPool;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

/**
 * Servicio encargado de manejar operaciones transaccionales sobre proyectos y asignaciones.
 * Justificación académica:
 * - Se aplican transacciones manuales con commit y rollback (CE2.4)
 * - Uso de savepoints para rollback parcial
 * - Todas las operaciones se ejecutan mediante PreparedStatement (CE2.5)
 */
public class TransaccionesService {

    /**
     * Transfiere presupuesto de un proyecto a otro en una transacción.
     * @param proyectoOrigenId ID del proyecto origen
     * @param proyectoDestinoId ID del proyecto destino
     * @param monto Monto a transferir
     * @return true si la transacción se completa correctamente, false si ocurre algún error
     */
    public boolean transferirPresupuesto(int proyectoOrigenId, int proyectoDestinoId, BigDecimal monto) {
        Connection conn = null;

        try {
            conn = DatabaseConfigPool.getConexion();
            conn.setAutoCommit(false); // Iniciar transacción manual

            // 1. Restar del proyecto origen
            String sqlRestar = "UPDATE proyectos SET presupuesto = presupuesto - ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlRestar)) {
                ps.setBigDecimal(1, monto);
                ps.setInt(2, proyectoOrigenId);
                ps.executeUpdate();
            }

            // 2. Sumar al proyecto destino
            String sqlSumar = "UPDATE proyectos SET presupuesto = presupuesto + ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlSumar)) {
                ps.setBigDecimal(1, monto);
                ps.setInt(2, proyectoDestinoId);
                ps.executeUpdate();
            }

            // 3. Confirmar transacción
            conn.commit();
            System.out.println("Transacción completada: $" + monto + " transferidos correctamente.");
            return true;

        } catch (SQLException e) {
            // Si ocurre un error, revertimos toda la transacción
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Error en la transacción: " + e.getMessage());
                    System.err.println("Rollback ejecutado, no se aplicaron cambios.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;

        } finally {
            // Restaurar autoCommit y cerrar conexión
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Asigna múltiples empleados a un proyecto usando savepoints.
     * Si una asignación falla, solo se revierte esa operación y las anteriores permanecen.
     * @param proyectoId ID del proyecto
     * @param empleadoIds Lista de IDs de empleados a asignar
     */
    public void asignarEmpleadosConSavepoint(int proyectoId, List<Integer> empleadoIds) {
        Connection conn = null;
        String sqlInsert = "INSERT INTO asignaciones(empleado_id, proyecto_id, fecha_asignacion, horas_asignadas) " +
                "VALUES (?, ?, CURRENT_DATE, 40)"; // Se puede ajustar horas

        try {
            conn = DatabaseConfigPool.getConexion();
            conn.setAutoCommit(false);

            for (int empId : empleadoIds) {
                Savepoint sp = conn.setSavepoint("SP_EMP_" + empId);
                try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                    ps.setInt(1, empId);
                    ps.setInt(2, proyectoId);
                    ps.executeUpdate();
                    System.out.println("Empleado " + empId + " asignado correctamente.");
                } catch (SQLException e) {
                    // Rollback parcial al savepoint de este empleado
                    conn.rollback(sp);
                    System.err.println("No se pudo asignar al empleado " + empId + ": " + e.getMessage());
                    System.err.println("Rollback parcial al savepoint de este empleado, las demás asignaciones permanecen.");
                }
            }

            // Confirmar todas las asignaciones exitosas
            conn.commit();
            System.out.println("Asignaciones completadas con éxito.");

        } catch (SQLException e) {
            // Rollback completo si falla algo crítico
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Error crítico en asignaciones: " + e.getMessage());
                    System.err.println("Rollback completo ejecutado.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Restaurar autoCommit y cerrar conexión
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
