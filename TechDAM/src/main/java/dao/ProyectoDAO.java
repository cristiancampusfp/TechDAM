package dao;

import config.DatabaseConfigPool;
import modelo.Proyecto;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO de proyectos.
 *
 * Comentarios académicos:
 * - Implementa CRUD completo para la entidad Proyecto usando PreparedStatement (CE2.5).
 * - Todas las operaciones usan try-with-resources para asegurar cierre de conexiones y recursos (CE2.2).
 * - Optional se usa en obtenerPorId() para manejar la posible ausencia de datos.
 */
public class ProyectoDAO {

    /**
     * Crear un proyecto en la BD y devolver el ID generado automáticamente.
     *
     * @param proyecto Objeto Proyecto con los datos a insertar
     * @return ID generado o -1 si hubo error
     */
    public int crear(Proyecto proyecto) {
        String sql = "INSERT INTO proyectos(nombre, presupuesto) VALUES (?, ?)";

        try (Connection con = DatabaseConfigPool.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Asignación segura de parámetros
            ps.setString(1, proyecto.getNombre());
            ps.setBigDecimal(2, proyecto.getPresupuesto());

            // Ejecutar inserción
            ps.executeUpdate();

            // Obtener ID generado automáticamente
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error al crear proyecto: " + e.getMessage());
        }
        return -1; // Indica error
    }

    /**
     * Obtener todos los proyectos de la base de datos.
     *
     * @return Lista de proyectos, vacía si no hay resultados
     */
    public List<Proyecto> obtenerTodos() {
        List<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT * FROM proyectos";

        try (Connection con = DatabaseConfigPool.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Mapear cada fila a un objeto Proyecto
            while (rs.next()) {
                Proyecto p = new Proyecto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getBigDecimal("presupuesto")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar proyectos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Obtener un proyecto por su ID.
     *
     * @param id ID del proyecto
     * @return Optional<Proyecto> vacío si no existe
     */
    public Optional<Proyecto> obtenerPorId(int id) {
        String sql = "SELECT * FROM proyectos WHERE id=?";

        try (Connection con = DatabaseConfigPool.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Proyecto p = new Proyecto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getBigDecimal("presupuesto")
                    );
                    return Optional.of(p);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error buscando proyecto: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Actualizar un proyecto existente en la base de datos.
     *
     * @param p Objeto Proyecto con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    public boolean actualizar(Proyecto p) {
        String sql = "UPDATE proyectos SET nombre=?, presupuesto=? WHERE id=?";

        try (Connection con = DatabaseConfigPool.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setBigDecimal(2, p.getPresupuesto());
            ps.setInt(3, p.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar proyecto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Eliminar un proyecto por ID.
     *
     * @param id ID del proyecto a eliminar
     * @return true si la eliminación fue exitosa
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM proyectos WHERE id=?";

        try (Connection con = DatabaseConfigPool.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error eliminando proyecto: " + e.getMessage());
            return false;
        }
    }
}
