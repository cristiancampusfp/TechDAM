package config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase de configuración de pool de conexiones HikariCP.
 * Lee los parámetros desde 'db.properties' y permite obtener conexiones de manera segura.
 */
public class DatabaseConfigPool {

    private static HikariDataSource dataSource;

    static {
        try {
            Properties props = new Properties();

            // Cargar archivo db.properties desde resources
            try (InputStream input = DatabaseConfigPool.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (input == null) {
                    throw new RuntimeException("No se encontró el archivo db.properties");
                }
                props.load(input);
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url")); // jdbc:mysql://localhost:3306/techDAM
            config.setUsername(props.getProperty("db.user"));
            config.setPassword(props.getProperty("db.password"));
            config.setMaximumPoolSize(5);
            config.setMinimumIdle(2);
            config.setIdleTimeout(10000);
            config.setConnectionTimeout(10000);
            config.setPoolName("DAMPool");

            dataSource = new HikariDataSource(config);
            System.out.println("Pool de conexiones inicializado correctamente");

        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar el pool de conexiones", e);
        }
    }

    /**
     * Obtiene una conexión del pool.
     */
    public static Connection getConexion() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Cierra el pool y libera recursos.
     */
    public static void cerrarPool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Pool de conexiones cerrado");
        }
    }
}
