import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class Principal {
    public static void main(String[] args) throws IOException, SQLException {
        SingletonMySQL.getInstance();
        SingletonPostgre.getInstance();
        Principal.menu();
    }

    public static void menu() throws IOException, SQLException {
        Scanner sc = new Scanner(System.in);

        final String opciones = "1. Crear una nueva especialidad médica (PostgreSQL)"
                + "\n2. Crear un nuevo médico (PostgreSQL)"
                + "\n3. Eliminar un médico por ID (PostgreSQL)"
                + "\n4. Crear un nuevo paciente (MySQL)"
                + "\n5. Eliminar un paciente (MySQL)"
                + "\n6. Crear nuevo tratamiento (nombre, descripcion, especialidad, médico) (MySQL + PostgreSQL)"
                + "\n7. Eliminar un tratamiento por su nombre (MySQL + PostgreSQL)"
                + "\n8. Listar tratamientos (menos de X pacientes asignados) (MySQL)"
                + "\n9. Obtener el total de citas realizadas por cada paciente (MySQL)"
                + "\n10. Obtener la cantidad de tratamientos por sala (PostgreSQL)"
                + "\n11. Listar todos los tratamientos con sus respectivas especalidades y médicos (MySQL + PostgreSQL)"
                + "\n12. Obtener todos los pacientes que han recibido un tratamiento de una especialidad dada (MySQL + PostgreSQL)"
                + "\n13. SALIR"
                + "\nElige opcion";

        int opcion = -1;
        while (opcion != 13) {
            try {
                System.out.println(opciones);
                opcion = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("error, letras no");
                break;
            }

            switch (opcion) {
                case 1:
                    System.out.print("Introduce el nombre de la especialidad: ");
                    String especialidad = sc.nextLine();
                    crearEspecialidad(especialidad);
                    break;
                case 2:
                    System.out.print("Introduce el nombre del medico: ");
                    String medico= sc.nextLine();
                    System.out.print("Introduce el nombre del contacto: ");
                    String contacto = sc.nextLine();
                    System.out.print("Introduce el nif del contacto: ");
                    String nif = sc.nextLine();
                    System.out.print("Introduce el teléfono del contacto: ");
                    int telefono = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Introduce el mail del contacto: ");
                    String mail = sc.nextLine();
                    crearMedico(medico, contacto, nif, telefono, mail);
                    break;
                case 3:
                    System.out.print("Introduce el id del medico a eliminar: ");
                    try{
                        int id = sc.nextInt();
                        eliminarMedico(id);
                    } catch (java.util.InputMismatchException e) {
                        // Si ocurre una excepción, mostramos un mensaje de error
                        System.out.println("¡Error! Debes introducir un número entero válido.\n");

                        // Limpiamos el buffer del Scanner
                        sc.nextLine(); // Consumimos el salto de línea residual
                    }
                    break;
                case 4:
                    System.out.print("Introduce el nombre del paciente: ");
                    String paciente = sc.nextLine();
                    System.out.print("Introduce el mail del paciente: ");
                    String mailPaciente = sc.nextLine();
                    System.out.print("Introduce la fecha(YYYY-MM-DD) de nacimiento del paciente: ");
                    String fecha_nacimiento = sc.nextLine();
                    LocalDate fechaNacimiento = LocalDate.parse(fecha_nacimiento);
                    crearPaciente(paciente, mailPaciente, fechaNacimiento);
                    break;
                case 5:
                    System.out.print("Introduce el id del paciente a eliminar: ");
                    try{
                        int idPaciente = sc.nextInt();
                        eliminarPaciente(idPaciente);
                    } catch (java.util.InputMismatchException e) {
                        // Si ocurre una excepción, mostramos un mensaje de error
                        System.out.println("¡Error! Debes introducir un número entero válido.\n");

                        // Limpiamos el buffer del Scanner
                        sc.nextLine(); // Consumimos el salto de línea residual
                    }
                    break;
                case 6:
                    System.out.print("Introduce el nombre del tratamiento: ");
                    String nombre = sc.nextLine();
                    System.out.print("Introduce descripcion del tratamiento: ");
                    String descripcion = sc.nextLine();
                    System.out.print("Introduce el nombre de la especialidad del tratamiento: ");
                    String nombreEspecialidad = sc.nextLine();
                    System.out.print("Introduce el nif del contacto del medico: ");
                    String nifMedico = sc.nextLine();
                    crearTratamiento(nombre, descripcion, nombreEspecialidad, nifMedico);
                    break;
                case 7:
                    System.out.print("Introduce el nombre del tratamiento que quieres eliminar: ");
                    String nombreTratamiento = sc.nextLine();
                    eliminarTratamientoPorNombre(nombreTratamiento);
                    break;
                case 8:
                    System.out.print("Introduce cual es la cantidad mínima que hay del tratamiento: ");
                    try{
                        int minimo = sc.nextInt();
                        listarTratamientosConPocosPacientes(minimo);
                    } catch (java.util.InputMismatchException e) {
                        // Si ocurre una excepción, mostramos un mensaje de error
                        System.out.println("¡Error! Debes introducir un número entero válido.\n");
                        // Limpiamos el buffer del Scanner
                        sc.nextLine(); // Consumimos el salto de línea residual
                    }
                    break;
                case 9:
                    obtenerTotalCitasPorPaciente();
                    break;
                case 10:
                    obtenerCantidadTratamientosPorSala();
                    break;
                case 11:
                    listarTratamientosConEspecialidadYMedico();
                    break;
                case 12:
                    System.out.print("Introduce el id de la especialidad de los tratamientos contratados: ");
                    try {
                        // Intentamos leer un número entero
                        int esp = sc.nextInt();

                        // Si no hay excepciones, se llama a la función
                        obtenerPacientesPorEspecialidad(esp);
                    } catch (java.util.InputMismatchException e) {
                        // Si ocurre una excepción, mostramos un mensaje de error
                        System.out.println("¡Error! Debes introducir un número entero válido.\n");

                        // Limpiamos el buffer del Scanner
                        sc.nextLine(); // Consumimos el salto de línea residual
                    }
                    break;
                case 13:
                    System.out.println("Saliendo");
                    closeConnection();
                    break;
                default:
                    System.out.println("introduce una opcion válida");
            }
        }
    }

    public static void closeConnection() {
        if (SingletonMySQL.connection != null && SingletonPostgre.connection != null) {
            try {
                if (!SingletonMySQL.connection.isClosed()) {
                    SingletonMySQL.connection.close();
                    System.out.println("Conexión MySQL cerrada.");
                }
                if (!SingletonPostgre.connection.isClosed()) {
                    SingletonPostgre.connection.close();
                    System.out.println("Conexión PostgreSQL cerrada.");
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        } else {
            System.out.println("No hay conexión para cerrar.");
        }
    }
    //1
    public static void crearEspecialidad(String nombreEspecialidad) {
        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO hospital.especialidades (nombre_especialidad) VALUES (?)";

            ps = SingletonPostgre.connection.prepareStatement(sql);
            ps.setString(1, nombreEspecialidad); // Asignar el parámetro de la especialidad

            // Ejecutar la consulta de inserción
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("La especialidad '" + nombreEspecialidad + "' se ha creado exitosamente.\n");
            } else {
                System.out.println("No se pudo crear la especialidad.");
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }
    //2
    public static void crearMedico(String nombreMedico, String nombreContacto, String nif, int telefono, String email) {
        PreparedStatement ps = null;
        //"INSERT INTO hospital.medicos (nombre_medico, contacto) VALUES (?, ROW(?, ?, ?, ?)::hospital.datos_contacto)";
        try {
            // SQL para insertar el nuevo medico
            String sql = "INSERT INTO hospital.medicos (nombre_medico, contacto) VALUES (?, ROW(?, ?, ?, ?))";

            // Preparar el statement para evitar inyecciones SQL
            ps = SingletonPostgre.connection.prepareStatement(sql);
            ps.setString(1, nombreMedico); // Asignar el nombre del medico
            ps.setString(2, nombreContacto); // Asignar el nombre del contacto del medico
            ps.setString(3, nif); // Asignar el NIF
            ps.setInt(4, telefono); // Asignar el teléfono
            ps.setString(5, email); // Asignar el email

            // Ejecutar la consulta de inserción
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("El medico '" + nombreMedico + "' se ha creado exitosamente.\n");
            } else {
                System.out.println("No se pudo crear el medico.");
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }
    //3
    public static void eliminarMedico(int id) {
        PreparedStatement psCheck = null;
        PreparedStatement psDelete = null;

        try {
            // Primero, comprobamos si el medico existe con el ID proporcionado
            String checkSql = "SELECT id_medico FROM hospital.medicos WHERE id_medico = ?";
            psCheck = SingletonPostgre.connection.prepareStatement(checkSql);
            psCheck.setInt(1, id); // Asignar el ID del medico

            ResultSet rs = psCheck.executeQuery();

            // Si el medico existe, procedemos a eliminarlo
            if (rs.next()) {
                String deleteSql = "DELETE FROM hospital.medicos WHERE id_medico = ?";
                psDelete = SingletonPostgre.connection.prepareStatement(deleteSql);
                psDelete.setInt(1, id); // Asignar el ID del medico a eliminar

                int rowsAffected = psDelete.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("El medico con ID " + id + " ha sido eliminado exitosamente.\n");
                } else {
                    System.out.println("No se pudo eliminar el medico.");
                }
            } else {
                System.out.println("El medico con ID " + id + " no existe.\n");
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());

        } finally {
            // Cerrar los recursos de forma segura
            try {
                if (psCheck != null) psCheck.close();
                if (psDelete != null) psDelete.close();
                //if (SingletonPostgre.connection != null) SingletonPostgre.connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar los recursos: " + e.getMessage());
            }
        }
    }
    //4
    static void crearPaciente(String nombre, String email, LocalDate fechaNacimiento) {
        try {
            // Insertar en MySQL
            String sqlMySQL = "INSERT INTO pacientes (nombre, email, fecha_nacimiento) VALUES (?, ?, ?)";
            PreparedStatement stmtMySQL = SingletonMySQL.connection.prepareStatement(sqlMySQL, Statement.RETURN_GENERATED_KEYS);
            stmtMySQL.setString(1, nombre);
            stmtMySQL.setString(2, email);
            stmtMySQL.setDate(3, Date.valueOf(fechaNacimiento));

            int affectedRowsMySQL = stmtMySQL.executeUpdate();
            ResultSet generatedKeysMySQL = stmtMySQL.getGeneratedKeys();
            int idPacienteMySQL  = -1;
            if (generatedKeysMySQL.next()) {
                idPacienteMySQL  = generatedKeysMySQL.getInt(1);
            }
            if (affectedRowsMySQL > 0 ) {
                System.out.println("Paciente creado exitosamente en bases de datos MySQL.\n");
            } else {
                System.out.println("Error al crear el paciente en MySQL.\n");
            }
        }catch(SQLException e){
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }
    //5
    static void eliminarPaciente(int id) {
        PreparedStatement psCheck = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;

        try {
            // Desactivar auto-commit para iniciar la transacción
            SingletonMySQL.connection.setAutoCommit(false);

            // Comprobar si existe el paciente
            String checkSql = "SELECT id_paciente FROM pacientes WHERE id_paciente = ?";
            psCheck = SingletonMySQL.connection.prepareStatement(checkSql);
            psCheck.setInt(1, id);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                // Eliminar de pacientes_tratamientos
                ps1 = SingletonMySQL.connection.prepareStatement("DELETE FROM pacientes_tratamientos WHERE id_paciente = ?");
                ps1.setInt(1, id);
                ps1.executeUpdate();

                // Eliminar de citas
                ps2 = SingletonMySQL.connection.prepareStatement("DELETE FROM citas WHERE id_paciente = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();

                // Eliminar de pacientes
                ps3 = SingletonMySQL.connection.prepareStatement("DELETE FROM pacientes WHERE id_paciente = ?");
                ps3.setInt(1, id);
                ps3.executeUpdate();

                // Si todo fue bien, confirmar la transacción
                SingletonMySQL.connection.commit();
                System.out.println("Paciente eliminado con éxito.\n");

            } else {
                System.out.println("El paciente con ID " + id + " no existe.\n");
                SingletonMySQL.connection.rollback(); // Por si acaso, aunque no se hicieron cambios
            }

        } catch (SQLException e) {
            System.out.println("Error durante la operación: " + e.getMessage());
            try {
                // Revertir cambios si algo falla
                SingletonMySQL.connection.rollback();
                System.out.println("Transacción revertida.\n");
            } catch (SQLException rollbackEx) {
                System.out.println("Error al revertir la transacción: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                // Restaurar auto-commit
                SingletonMySQL.connection.setAutoCommit(true);

                if (psCheck != null) psCheck.close();
                if (ps1 != null) ps1.close();
                if (ps2 != null) ps2.close();
                if (ps3 != null) ps3.close();

            } catch (SQLException e) {
                System.out.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    //6 Crear un nuevo tratamiento (con inserción sincronizada en ambas bases de datos)
    public static void crearTratamiento(String nombre, String descripcion, String nombreEspecialidad, String nifMedico) {
        int idEspecialidad = obtenerIdEspecialidad(nombreEspecialidad);
        int idMedico = obtenerIdMedico(nifMedico);

        if (idEspecialidad == -1 || idMedico == -1) {
            System.out.println("Error: No se encontró la especialidad o el médico.");
            return;
        }

        Connection pgConn = SingletonPostgre.connection;
        Connection mySqlConn = SingletonMySQL.connection;

        try {
            pgConn.setAutoCommit(false);
            mySqlConn.setAutoCommit(false);

            // Insertar en MySQL
            String sqlMySQL = "INSERT INTO tratamientos (nombre_tratamiento, descripcion) VALUES (?, ?)";
            PreparedStatement stmtMySQL = mySqlConn.prepareStatement(sqlMySQL, Statement.RETURN_GENERATED_KEYS);
            stmtMySQL.setString(1, nombre);
            stmtMySQL.setString(2, descripcion);
            int affectedRowsMySQL = stmtMySQL.executeUpdate();

            ResultSet generatedKeys = stmtMySQL.getGeneratedKeys();
            int idTratamiento = -1;
            if (generatedKeys.next()) {
                idTratamiento = generatedKeys.getInt(1);
            } else {
                throw new SQLException("No se generó ID en MySQL");
            }

            // Insertar en PostgreSQL
            String sqlPostgre = "INSERT INTO hospital.tratamientos (id_tratamiento, id_medico, id_especialidad) VALUES (?, ?, ?)";
            PreparedStatement stmtPostgre = pgConn.prepareStatement(sqlPostgre);
            stmtPostgre.setInt(1, idTratamiento);
            stmtPostgre.setInt(2, idMedico);
            stmtPostgre.setInt(3, idEspecialidad);
            int affectedRowsPG = stmtPostgre.executeUpdate();

            if (affectedRowsMySQL > 0 && affectedRowsPG > 0) {
                pgConn.commit();
                mySqlConn.commit();
                System.out.println("Tratamiento creado exitosamente con ID: " + idTratamiento);
            } else {
                throw new SQLException("Error al insertar en una de las bases de datos");
            }

        } catch (SQLException e) {
            try {
                if (pgConn != null) pgConn.rollback();
                if (mySqlConn != null) mySqlConn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    // Obtener el id de la especialidad desde PostgreSQL
    private static int obtenerIdEspecialidad(String nombreEspecialidad) {
        int idEspecialidad = -1;

        try {
            String sqlPostgreSQL = "SELECT id_especialidad FROM especialidades WHERE nombre_especialidad = ?";
            PreparedStatement stmtPostgreSQL = SingletonPostgre.connection.prepareStatement(sqlPostgreSQL);
            stmtPostgreSQL.setString(1, nombreEspecialidad);
            ResultSet rsPostgreSQL = stmtPostgreSQL.executeQuery();

            if (rsPostgreSQL.next()) {
                idEspecialidad = rsPostgreSQL.getInt("id_especialidad");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idEspecialidad;
    }

    // Obtener el id del medico basado en el NIF desde PostgreSQL
    private static int obtenerIdMedico(String nif) {
        int idMedico = -1;

        try {
            String sqlPostgreSQL = "SELECT id_medico FROM medicos WHERE (contacto).nif = ?";
            PreparedStatement stmtPostgreSQL = SingletonPostgre.connection.prepareStatement(sqlPostgreSQL);
            stmtPostgreSQL.setString(1, nif);
            ResultSet rsPostgreSQL = stmtPostgreSQL.executeQuery();

            if (rsPostgreSQL.next()) {
                idMedico = rsPostgreSQL.getInt("id_medico");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idMedico;
    }
    //7. Eliminar un tratamiento por su nombre (borrado sincronizado en ambas bases)
    public static void eliminarTratamientoPorNombre(String nombre) {
        Connection mysqlConn = SingletonMySQL.connection;
        Connection pgConn = SingletonPostgre.connection;

        try {
            mysqlConn.setAutoCommit(false);
            pgConn.setAutoCommit(false);

            //Obtener ID tratamiento desde MySQL
            String getIdSQL = "SELECT id_tratamiento FROM tratamientos WHERE nombre_tratamiento = ?";
            PreparedStatement psGetId = mysqlConn.prepareStatement(getIdSQL);
            psGetId.setString(1, nombre);
            ResultSet rs = psGetId.executeQuery();

            if (!rs.next()) {
                System.out.println("Tratamiento no encontrado en MySQL: " + nombre);
                return;
            }

            int idTratamiento = rs.getInt("id_tratamiento");

            //Eliminar de tabla pacientes_tratamientos (relaciones)
            String deletePacientesTratamientos = "DELETE FROM pacientes_tratamientos WHERE id_tratamiento = ?";
            PreparedStatement psDeletePT = mysqlConn.prepareStatement(deletePacientesTratamientos);
            psDeletePT.setInt(1, idTratamiento);
            psDeletePT.executeUpdate();

            //Eliminar tratamiento en MySQL
            String deleteMySQL = "DELETE FROM tratamientos WHERE id_tratamiento = ?";
            PreparedStatement psDeleteMySQL = mysqlConn.prepareStatement(deleteMySQL);
            psDeleteMySQL.setInt(1, idTratamiento);
            psDeleteMySQL.executeUpdate();

            System.out.println("Tratamiento eliminado en MySQL: " + nombre);

            //Eliminar de salas_tratamientos en PostgreSQL
            String deleteSalasTratamientos = "DELETE FROM hospital.salas_tratamientos WHERE id_tratamiento = ?";
            PreparedStatement psDeleteSalas = pgConn.prepareStatement(deleteSalasTratamientos);
            psDeleteSalas.setInt(1, idTratamiento);
            psDeleteSalas.executeUpdate();

            //Eliminar tratamiento en PostgreSQL
            String deletePostgreSQL = "DELETE FROM hospital.tratamientos WHERE id_tratamiento = ?";
            PreparedStatement psDeletePG = pgConn.prepareStatement(deletePostgreSQL);
            psDeletePG.setInt(1, idTratamiento);
            psDeletePG.executeUpdate();

            System.out.println("Tratamiento eliminado en PostgreSQL: " + nombre);

            //Commit en ambas BD
            mysqlConn.commit();
            pgConn.commit();

        } catch (SQLException e) {
            try {
                if (mysqlConn != null) mysqlConn.rollback();
                if (pgConn != null) pgConn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            System.out.println("Error eliminando tratamiento: " + e.getMessage());
        }
    }

    //8
    static void listarTratamientosConPocosPacientes(int cantidad) {
        String sql = """
                SELECT 
                    t.id_tratamiento, 
                    t.nombre_tratamiento, 
                    t.descripcion, 
                    COUNT(pt.id_paciente) AS cantidad
                FROM tratamientos t
                LEFT JOIN pacientes_tratamientos pt ON t.id_tratamiento = pt.id_tratamiento
                GROUP BY t.id_tratamiento
                HAVING COUNT(pt.id_paciente) < ?
            """;
        try {
            PreparedStatement ps = SingletonMySQL.connection.prepareStatement(sql);
            ps.setInt(1, cantidad);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("Nombre: "+ rs.getString("nombre_tratamiento") +"\nCantidad: " + rs.getInt("cantidad")+"\n");
            }
        } catch (SQLException e) {
            System.out.println("Error al hacer la consulta"+ e.getMessage());
        }
    }
    //9
    static void obtenerTotalCitasPorPaciente(){
        try {
            PreparedStatement ps = SingletonMySQL.connection.prepareStatement("SELECT p.nombre, COUNT(c.id_cita) AS total_citas\n" +
                    "FROM pacientes p\n" +
                    "LEFT JOIN citas c ON p.id_paciente = c.id_paciente\n" +
                    "GROUP BY t.id_tratamiento, t.nombre_tratamiento, t.descripcion\n" +
                    "ORDER BY total_citas DESC;");

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String nombrePaciente = rs.getString("nombre");  // Nombre del paciente
                int totalCitas = rs.getInt("total_citas");  // Total de citas
                // Imprimir los resultados
                System.out.println("Nombre: " + nombrePaciente + "\nTotal citas: " + totalCitas + "\n");
            }

        } catch (SQLException e) {
            System.out.println("Error al hacer la consulta: "+ e.getMessage());
        }
    }
    //10
    static void obtenerCantidadTratamientosPorSala(){
        try {
            String sql = "SELECT s.nombre_sala, COUNT(st.id_tratamiento) AS total_tratamientos " +
                    "FROM hospital.salas s " +
                    "JOIN hospital.salas_tratamientos st ON s.id_sala = st.id_sala " +
                    "GROUP BY s.nombre_sala " +
                    "ORDER BY s.nombre_sala";
            PreparedStatement ps = SingletonPostgre.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String nombreSala = rs.getString("nombre_sala");
                int totalTratamientos = rs.getInt("total_tratamientos");
                System.out.println("Sala: " + nombreSala + "\nTotal tratamientos: " + totalTratamientos + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //11
    static void listarTratamientosConEspecialidadYMedico(){
        try {
            // Consulta en PostgreSQL (para obtener medicos, especialidades y tratamientos)
            String sqlPostgreSQL =
                    "SELECT t.id_tratamiento, m.nombre_medico,"+
                        "(m.contacto).nombre_contacto,"+
                        "(m.contacto).nif,"+
                        "(m.contacto).telefono,"+
                        "(m.contacto).email,"+
                        "e.nombre_espacialidad"+
                    "FROM hospital.tratamientos t"+
                    "JOIN hospital.medicos m ON t.id_medico = m.id_medico"+
                    "JOIN hospital.especialidades e ON t.id_especialidad = e.id_especialidad";

            Statement stmtPostgreSQL = SingletonPostgre.connection.createStatement();
            ResultSet rsPostgreSQL = stmtPostgreSQL.executeQuery(sqlPostgreSQL);

            // Consulta en MySQL (para obtener tratamiento y descripcion)
            String sqlMySQL = "SELECT id_tratamiento, nombre_tratamiento, descripcion FROM tratamientos";
            Statement stmtMySQL = SingletonMySQL.connection.createStatement();
            ResultSet rsMySQL = stmtMySQL.executeQuery(sqlMySQL);

            // Mapa para tratamientos por id_tratamiento para unir los resultados
            Map<Integer, String> tratamientoMySQL = new HashMap<>();
            Map<Integer, String> descripcionMySQL = new HashMap<>();

            // Guardar los datos de MySQL
            while (rsMySQL.next()) {
                int idTratamiento = rsMySQL.getInt("id_tratamiento");
                String nombreTratamiento = rsMySQL.getString("nombre_tratamiento");
                String descripcion = rsMySQL.getString("descripcion");
                tratamientoMySQL.put(idTratamiento, nombreTratamiento);
                descripcionMySQL.put(idTratamiento, descripcion);
            }

            // Ahora mostramos los resultados combinados de PostgreSQL y MySQL
            while (rsPostgreSQL.next()) {
                int idTratamiento = rsPostgreSQL.getInt("id_tratamiento");
                String especialidad = rsPostgreSQL.getString("nombre_especialidad");
                String nombreMedico = rsPostgreSQL.getString("nombre_medico");
                String nombreContacto = rsPostgreSQL.getString("nombre_contacto");
                String nif = rsPostgreSQL.getString("nif");
                int telefono = rsPostgreSQL.getInt("telefono");
                String email = rsPostgreSQL.getString("email");

                // Combinamos la información de ambas bases de datos
                String nombreTratamiento = tratamientoMySQL.get(idTratamiento);
                String descripcion = descripcionMySQL.get(idTratamiento);
                if (nombreTratamiento != null && descripcion != null) {
                    // Mostrar los resultados combinados
                    System.out.println("Tratamiento: " + nombreTratamiento);
                    System.out.println("Especialidad: " + especialidad);
                    System.out.println("Medico: " + nombreMedico);
                    System.out.println("Nombre del contacto del medico: " + nombreContacto);
                    System.out.println("NIF: " + nif);
                    System.out.println("Teléfono: " + telefono);
                    System.out.println("Email: " + email);
                    System.out.println("Descripcion: " + descripcion);
                    System.out.println("----------------------------------------");
                } else {
                    // Manejar el caso cuando el tratamiento sea null
                    System.out.println("No se encontraron datos para el tratamiento con ID: " + idTratamiento);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //12
    static void obtenerPacientesPorEspecialidad(int idEspecialidad) {
        try {
            // Paso 1: Obtener los tratamientos de PostgreSQL para la especialidad dada
            String sqlPostgre = "SELECT id_tratamiento FROM hospital.tratamientos WHERE id_especialidad = ?";
            PreparedStatement stmtPostgreSQL = SingletonPostgre.connection.prepareStatement(sqlPostgre);
            stmtPostgreSQL.setInt(1, idEspecialidad);
            ResultSet rsPostgreSQL = stmtPostgreSQL.executeQuery();

            // Crear una lista de id_tratamiento de PostgreSQL
            List<Integer> tratamientosIds = new ArrayList<>();
            while (rsPostgreSQL.next()) {
                tratamientosIds.add(rsPostgreSQL.getInt("id_tratamiento"));
            }

            // Si no se encontraron tratamientos para esta especialidad
            if (tratamientosIds.isEmpty()) {
                System.out.println("No se encontraron tratamientos para la especialidad con ID: " + idEspecialidad);
                return;
            }

            // Paso 2: Crear consulta dinámica en MySQL para buscar pacientes con esos tratamientos
            // Convertir la lista de tratamientos a una cadena de valores
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tratamientosIds.size(); i++) {
                sb.append("?");
                if (i < tratamientosIds.size() - 1) {
                    sb.append(", ");
                }
            }

            // Consulta para obtener los pacientes que compraron los tratamientos de la lista
            String queryMySQL = "SELECT DISTINCT p.id_paciente, p.nombre, p.email, p.fecha_nacimiento " +
                    "FROM pacientes p " +
                    "JOIN pacientes_tratamientos pt ON p.id_paciente = pt.id_paciente " +
                    "WHERE pt.id_tratamiento IN (" + sb.toString() + ")";
            PreparedStatement stmtMySQL = SingletonMySQL.connection.prepareStatement(queryMySQL);

            // Asignar los valores de los tratamientos a la consulta
            for (int i = 0; i < tratamientosIds.size(); i++) {
                stmtMySQL.setInt(i + 1, tratamientosIds.get(i));
            }

            ResultSet rsMySQL = stmtMySQL.executeQuery();

            // Paso 3: Mostrar los nombres de los pacientes
            boolean pacientesEncontrados = false;
            while (rsMySQL.next()) {
                String nombre = rsMySQL.getString("nombre");
                String email = rsMySQL.getString("email");
                Date fechaNacimiento = rsMySQL.getDate("fecha_nacimiento");

                System.out.println("Paciente:");
                System.out.println("Nombre: " + nombre);
                System.out.println("Email: " + email);
                System.out.println("Fecha de nacimiento: " + fechaNacimiento);
                System.out.println("----------------------------------------");

                pacientesEncontrados = true;
            }

            if (!pacientesEncontrados) {
                System.out.println("No hay pacientes que hayan contratado tratamientos de esta especialidad.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
