-- Cristian Espinar

-- 1. Crear base de datos
create database if not exists techdam;
USE techdam;

-- ==========================================
-- 2. Crear tabla empleados
-- Contendrá información de los empleados
-- ==========================================
CREATE TABLE empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,           -- Clave primaria única
    nombre VARCHAR(100) NOT NULL,               -- Nombre del empleado
    departamento VARCHAR(50) NOT NULL,          -- Departamento al que pertenece
    salario DECIMAL(10,2) NOT NULL,             -- Salario del empleado
    activo BOOLEAN DEFAULT TRUE                 -- Indica si el empleado sigue activo
);

-- Insertar registros de prueba en empleados
INSERT INTO empleados (nombre, departamento, salario, activo) VALUES
('Ana', 'Desarrollo', 1500.00, TRUE),
('Luis', 'Marketing', 1800.00, TRUE),
('Marta', 'Desarrollo', 2000.00, TRUE),
('Pedro', 'Ventas', 1750.00, TRUE),
('Sofía', 'Marketing', 1900.00, TRUE);

-- ==========================================
-- 3. Crear tabla proyectos
-- Contendrá información de los proyectos
-- ==========================================
CREATE TABLE proyectos (
    id INT AUTO_INCREMENT PRIMARY KEY,           -- Clave primaria única
    nombre VARCHAR(100) NOT NULL,               -- Nombre del proyecto
    presupuesto DECIMAL(10,2) NOT NULL          -- Presupuesto del proyecto
);

-- Insertar registros de prueba en proyectos
INSERT INTO proyectos (nombre, presupuesto) VALUES
('Proyecto A', 10000.00),
('Proyecto B', 15000.00),
('Proyecto C', 20000.00),
('Proyecto D', 5000.00),
('Proyecto E', 12000.00);

-- ==========================================
-- 4. Crear tabla asignaciones
-- Relaciona empleados con proyectos
-- ==========================================
CREATE TABLE asignaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,           -- Clave primaria única
    empleado_id INT NOT NULL,                     -- Referencia al empleado
    proyecto_id INT NOT NULL,                     -- Referencia al proyecto
    horas_asignadas INT NOT NULL,                -- Horas asignadas
    rol VARCHAR(50),                             -- Rol del empleado en el proyecto
	fecha_asignacion DATE DEFAULT (CURDATE()),  -- Fecha de asignación
    FOREIGN KEY (empleado_id) REFERENCES empleados(id),   -- Clave foránea a empleados
    FOREIGN KEY (proyecto_id) REFERENCES proyectos(id)    -- Clave foránea a proyectos
);

-- Insertar registros de prueba en asignaciones
INSERT INTO asignaciones (empleado_id, proyecto_id, horas_asignadas, rol) VALUES
(1, 1, 40, 'Desarrollador'),
(2, 2, 35, 'Marketing'),
(3, 1, 20, 'Desarrollador'),
(4, 3, 30, 'Ventas'),
(5, 2, 25, 'Marketing');

-- ==========================================
-- 5. Procedimiento almacenado: actualizar salario de un departamento
-- Incrementa el salario de todos los empleados de un departamento activo
-- ==========================================
DELIMITER $$
CREATE PROCEDURE actualizar_salario_departamento(
    IN p_departamento VARCHAR(50),
    IN p_porcentaje DECIMAL(5,2),
    OUT p_empleados_actualizados INT
)
BEGIN
    -- Actualizamos los salarios de los empleados activos en el departamento
    UPDATE empleados
    SET salario = salario * (1 + p_porcentaje / 100)
    WHERE departamento = p_departamento AND activo = TRUE;

    -- Retornamos el número de empleados afectados
    SET p_empleados_actualizados = ROW_COUNT();
END$$
DELIMITER ;

-- ==========================================
-- 6. Procedimiento almacenado: asignar empleado a proyecto
-- Registra un empleado en un proyecto con horas y rol
-- ==========================================
DELIMITER $$
CREATE PROCEDURE asignar_empleado_proyecto(
    IN p_empleado_id INT,
    IN p_proyecto_id INT,
    IN p_horas INT,
    IN p_rol VARCHAR(50)
)
BEGIN
    -- Insertamos en la tabla asignaciones
    INSERT INTO asignaciones (empleado_id, proyecto_id, horas_asignadas, rol)
    VALUES (p_empleado_id, p_proyecto_id, p_horas, p_rol);
END$$
DELIMITER ;

-- ==========================================
-- 7. Función almacenada: obtener salario de un empleado
-- Devuelve el salario actual de un empleado
-- ==========================================
DELIMITER $$
CREATE FUNCTION obtener_salario(p_empleado_id INT) RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    DECLARE v_salario DECIMAL(10,2);
    SELECT salario INTO v_salario
    FROM empleados
    WHERE id = p_empleado_id;
    RETURN v_salario;
END$$
DELIMITER ;
