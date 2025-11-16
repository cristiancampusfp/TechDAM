package Menu;

import config.DatabaseConfigPool;
import dao.EmpleadoDAO;
import dao.ProyectoDAO;
import modelo.Empleado;
import modelo.Proyecto;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Clase que gestiona todo el menú interactivo de TechDAM.
 * Contiene submenús de Empleados, Proyectos, Procedimientos y Transacciones.
 */
public class Menu {

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final ProyectoDAO proyectoDAO = new ProyectoDAO();
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Método principal que inicia el menú.
     */
    public void iniciar() {
        System.out.println("=== BIENVENIDO AL SISTEMA TECHDAM ===");
        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Gestionar Empleados");
            System.out.println("2. Gestionar Proyectos");
            System.out.println("3. Procedimientos Almacenados");
            System.out.println("4. Transacciones");
            System.out.println("0. Salir");

            int opcion = leerEntero("Seleccione opción: ");

            switch (opcion) {
                case 1 -> menuEmpleados();
                case 2 -> menuProyectos();
                case 3 -> menuProcedimientos();
                case 4 -> menuTransacciones();
                case 0 -> salir = true;
                default -> System.out.println("Opción no válida.");
            }
        }

        // Cerrar pool al finalizar
        DatabaseConfigPool.cerrarPool();
        System.out.println("=== FIN DEL SISTEMA ===");
    }

    // ------------------- MENÚ EMPLEADOS -------------------
    private void menuEmpleados() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- MENÚ EMPLEADOS ---");
            System.out.println("1. Crear Empleado");
            System.out.println("2. Listar Empleados");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Actualizar Empleado");
            System.out.println("5. Eliminar Empleado");
            System.out.println("0. Volver");

            int opcion = leerEntero("Seleccione opción: ");

            switch (opcion) {
                case 1 -> crearEmpleado();
                case 2 -> listarEmpleados();
                case 3 -> buscarEmpleadoPorId();
                case 4 -> actualizarEmpleado();
                case 5 -> eliminarEmpleado();
                case 0 -> volver = true;
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    private void crearEmpleado() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Departamento: ");
        String departamento = scanner.nextLine();
        BigDecimal salario = leerDecimal("Salario: ");
        boolean activo = leerBoolean("Activo (true/false): ");

        Empleado emp = new Empleado(0, nombre, departamento, salario, activo);
        int id = empleadoDAO.crear(emp);
        if (id != -1) {
            emp.setId(id);
            System.out.println("Empleado creado con ID: " + id);
        } else {
            System.out.println("Error al crear empleado.");
        }
    }

    private void listarEmpleados() {
        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        empleados.forEach(System.out::println);
    }

    private void buscarEmpleadoPorId() {
        int id = leerEntero("Ingrese ID: ");
        Optional<Empleado> emp = empleadoDAO.obtenerPorId(id);
        emp.ifPresentOrElse(System.out::println, () -> System.out.println("Empleado no encontrado."));
    }

    private void actualizarEmpleado() {
        int id = leerEntero("Ingrese ID a actualizar: ");
        Optional<Empleado> empOpt = empleadoDAO.obtenerPorId(id);
        if (empOpt.isPresent()) {
            Empleado emp = empOpt.get();
            System.out.print("Nombre (" + emp.getNombre() + "): ");
            String nombre = scanner.nextLine();
            if (!nombre.isBlank()) emp.setNombre(nombre);
            System.out.print("Departamento (" + emp.getDepartamento() + "): ");
            String dept = scanner.nextLine();
            if (!dept.isBlank()) emp.setDepartamento(dept);
            BigDecimal salario = leerDecimal("Salario (" + emp.getSalario() + "): ");
            emp.setSalario(salario);
            boolean activo = leerBoolean("Activo (" + emp.isActivo() + "): ");
            emp.setActivo(activo);

            if (empleadoDAO.actualizar(emp)) {
                System.out.println("Empleado actualizado.");
            } else {
                System.out.println("Error al actualizar.");
            }
        } else {
            System.out.println("Empleado no encontrado.");
        }
    }

    private void eliminarEmpleado() {
        int id = leerEntero("Ingrese ID a eliminar: ");
        if (empleadoDAO.eliminar(id)) {
            System.out.println("Empleado eliminado.");
        } else {
            System.out.println("Error al eliminar o ID no encontrado.");
        }
    }

    // ------------------- MENÚ PROYECTOS -------------------
    private void menuProyectos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- MENÚ PROYECTOS ---");
            System.out.println("1. Crear Proyecto");
            System.out.println("2. Listar Proyectos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Actualizar Proyecto");
            System.out.println("5. Eliminar Proyecto");
            System.out.println("0. Volver");

            int opcion = leerEntero("Seleccione opción: ");

            switch (opcion) {
                case 1 -> crearProyecto();
                case 2 -> listarProyectos();
                case 3 -> buscarProyectoPorId();
                case 4 -> actualizarProyecto();
                case 5 -> eliminarProyecto();
                case 0 -> volver = true;
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    private void crearProyecto() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        BigDecimal presupuesto = leerDecimal("Presupuesto: ");
        Proyecto proy = new Proyecto(0, nombre, presupuesto);
        int id = proyectoDAO.crear(proy);
        if (id != -1) {
            proy.setId(id);
            System.out.println("Proyecto creado con ID: " + id);
        } else {
            System.out.println("Error al crear proyecto.");
        }
    }

    private void listarProyectos() {
        List<Proyecto> proyectos = proyectoDAO.obtenerTodos();
        proyectos.forEach(System.out::println);
    }

    private void buscarProyectoPorId() {
        int id = leerEntero("Ingrese ID: ");
        Optional<Proyecto> proy = proyectoDAO.obtenerPorId(id);
        proy.ifPresentOrElse(System.out::println, () -> System.out.println("Proyecto no encontrado."));
    }

    private void actualizarProyecto() {
        int id = leerEntero("Ingrese ID a actualizar: ");
        Optional<Proyecto> proyOpt = proyectoDAO.obtenerPorId(id);
        if (proyOpt.isPresent()) {
            Proyecto proy = proyOpt.get();
            System.out.print("Nombre (" + proy.getNombre() + "): ");
            String nombre = scanner.nextLine();
            if (!nombre.isBlank()) proy.setNombre(nombre);
            BigDecimal presupuesto = leerDecimal("Presupuesto (" + proy.getPresupuesto() + "): ");
            proy.setPresupuesto(presupuesto);

            if (proyectoDAO.actualizar(proy)) {
                System.out.println("Proyecto actualizado.");
            } else {
                System.out.println("Error al actualizar.");
            }
        } else {
            System.out.println("Proyecto no encontrado.");
        }
    }

    private void eliminarProyecto() {
        int id = leerEntero("Ingrese ID a eliminar: ");
        if (proyectoDAO.eliminar(id)) {
            System.out.println("Proyecto eliminado.");
        } else {
            System.out.println("Error al eliminar o ID no encontrado.");
        }
    }

    // ------------------- MENÚ PROCEDIMIENTOS -------------------
    private void menuProcedimientos() {
        System.out.println("\n--- PROCEDIMIENTO ALMACENADO: actualizar_salario_departamento ---");
        System.out.print("Departamento: ");
        String dept = scanner.nextLine();
        BigDecimal porcentaje = leerDecimal("Incremento (%) : ");

        try (Connection conn = DatabaseConfigPool.getConexion();
             CallableStatement cstmt = conn.prepareCall("{call actualizar_salario_departamento(?, ?, ?)}")) {

            cstmt.setString(1, dept);
            cstmt.setBigDecimal(2, porcentaje);
            cstmt.registerOutParameter(3, Types.INTEGER);
            cstmt.execute();

            int empleadosActualizados = cstmt.getInt(3);
            System.out.println("Empleados actualizados: " + empleadosActualizados);

        } catch (SQLException e) {
            System.out.println("Error al ejecutar procedimiento: " + e.getMessage());
        }
    }

    // ------------------- MENÚ TRANSACCIONES -------------------
    private void menuTransacciones() {
        System.out.println("\n--- TRANSACCIÓN: Incrementar salario y descontar presupuesto ---");

        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        List<Proyecto> proyectos = proyectoDAO.obtenerTodos();

        if (empleados.isEmpty() || proyectos.isEmpty()) {
            System.out.println("No hay empleados o proyectos para realizar la transacción.");
            return;
        }

        Scanner sc = new Scanner(System.in);

        // Mostrar empleados disponibles
        System.out.println("\nEmpleados disponibles:");
        for (Empleado emp : empleados) {
            System.out.println(emp.getId() + " - " + emp.getNombre() + " (Salario: " + emp.getSalario() + ")");
        }
        System.out.print("Ingrese ID del empleado a incrementar salario: ");
        int empId = sc.nextInt();

        // Buscar empleado seleccionado
        Empleado empSeleccionado = empleados.stream()
                .filter(e -> e.getId() == empId)
                .findFirst()
                .orElse(null);

        if (empSeleccionado == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }

        // Mostrar proyectos disponibles
        System.out.println("\nProyectos disponibles:");
        for (Proyecto proy : proyectos) {
            System.out.println(proy.getId() + " - " + proy.getNombre() + " (Presupuesto: " + proy.getPresupuesto() + ")");
        }
        System.out.print("Ingrese ID del proyecto a descontar presupuesto: ");
        int proyId = sc.nextInt();

        // Buscar proyecto seleccionado
        Proyecto proySeleccionado = proyectos.stream()
                .filter(p -> p.getId() == proyId)
                .findFirst()
                .orElse(null);

        if (proySeleccionado == null) {
            System.out.println("Proyecto no encontrado.");
            return;
        }

        System.out.print("Ingrese monto a incrementar/descontar: ");
        BigDecimal monto = sc.nextBigDecimal();

        try (Connection conn = DatabaseConfigPool.getConexion()) {
            conn.setAutoCommit(false);

            try {
                System.out.println("\nAntes de la transacción:");
                System.out.println("Empleado: " + empSeleccionado);
                System.out.println("Proyecto: " + proySeleccionado);

                // Aplicar cambios
                empSeleccionado.setSalario(empSeleccionado.getSalario().add(monto));
                empleadoDAO.actualizar(empSeleccionado);

                proySeleccionado.setPresupuesto(proySeleccionado.getPresupuesto().subtract(monto));
                proyectoDAO.actualizar(proySeleccionado);

                conn.commit();

                System.out.println("\n--- TRANSACCIÓN COMPLETADA ---");
                System.out.println("Después de la transacción:");
                System.out.println("Empleado: " + empSeleccionado);
                System.out.println("Proyecto: " + proySeleccionado);

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error en la transacción, rollback realizado: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error al iniciar la transacción: " + e.getMessage());
        }
    }


    // ------------------- MÉTODOS AUXILIARES -------------------
    private int leerEntero(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Número inválido, intente de nuevo.");
            }
        }
    }

    private BigDecimal leerDecimal(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Número decimal inválido, intente de nuevo.");
            }
        }
    }

    private boolean leerBoolean(String msg) {
        while (true) {
            System.out.print(msg);
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("true")) return true;
            if (input.equals("false")) return false;
            System.out.println("Ingrese 'true' o 'false'.");
        }
    }
}
