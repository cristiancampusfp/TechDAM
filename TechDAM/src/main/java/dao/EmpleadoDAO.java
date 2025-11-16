package dao;

import config.DatabaseConfigPool;
import modelo.Empleado;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO de empleados.
 * Justificación académica:
 * - Implemento CRUD completo usando únicamente PreparedStatement (CE2.5).
 * - Se aplican bloques try-with-resources para garantizar el cierre de recursos (CE2.2).
 * - Uso Optional para representar resultados que pueden no existir.
 */
public class EmpleadoDAO {

    /**
     * Crear un empleado en la BD y devolver el ID generado.
     */
    public int crear(Empleado emp) {
        String sql = "INSERT INTO empleados(nombre, departamento, salario, activo) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConfigPool.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getDepartamento());
            ps.setBigDecimal(3, emp.getSalario());
            ps.setBoolean(4, emp.isActivo());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al crear empleado: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Obtener todos los empleados.
     */
    public List<Empleado> obtenerTodos() {
        List<Empleado> lista = new ArrayList<>();

        String sql = "SELECT * FROM empleados";

        try (Connection con = DatabaseConfigPool.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Empleado emp = new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("departamento"),
                        rs.getBigDecimal("salario"),
                        rs.getBoolean("activo")
                );
                lista.add(emp);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener empleados: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Obtener un empleado por ID.
     */
    public Optional<Empleado> obtenerPorId(int id) {
        String sql = "SELECT * FROM empleados WHERE id = ?";

        try (Connection con = DatabaseConfigPool.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empleado emp = new Empleado(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("departamento"),
                            rs.getBigDecimal("salario"),
                            rs.getBoolean("activo")
                    );
                    return Optional.of(emp);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener empleado por id: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Actualizar un empleado existente.
     */
    public boolean actualizar(Empleado emp) {
        String sql = "UPDATE empleados SET nombre=?, departamento=?, salario=?, activo=? WHERE id=?";

        try (Connection con = DatabaseConfigPool.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getDepartamento());
            ps.setBigDecimal(3, emp.getSalario());
            ps.setBoolean(4, emp.isActivo());
            ps.setInt(5, emp.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar empleado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Eliminar un empleado por ID.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM empleados WHERE id=?";

        try (Connection con = DatabaseConfigPool.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar empleado: " + e.getMessage());
            return false;
        }
    }
}
