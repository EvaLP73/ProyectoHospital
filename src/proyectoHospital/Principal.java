package proyectoHospital;

import java.io.IOException;
import java.sql.*;
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
                    System.out.print("Introduce el nombre de la categoría: ");
                    String categoria = sc.nextLine();
                    crearCategoria(categoria);
                    break;
                case 2:
                    System.out.print("Introduce el nombre del contacto: ");
                    String contacto = sc.nextLine();
                    System.out.print("Introduce el nif del contacto: ");
                    String nif = sc.nextLine();
                    System.out.print("Introduce el teléfono del contacto: ");
                    int telefono = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Introduce el mail del contacto: ");
                    String mail = sc.nextLine();
                    crearNuevoProveedor(contacto, nif, telefono, mail);
                    break;
                case 3:
                    System.out.print("Introduce el id del proveedor a eliminar: ");
                    try{
                        int id = sc.nextInt();
                        eliminarProveedor(id);
                    } catch (java.util.InputMismatchException e) {
                        // Si ocurre una excepción, mostramos un mensaje de error
                        System.out.println("¡Error! Debes introducir un número entero válido.\n");

                        // Limpiamos el buffer del Scanner
                        sc.nextLine(); // Consumimos el salto de línea residual
                    }
                    break;
                case 4:
                    System.out.print("Introduce el nombre del usuario: ");
                    String usuario = sc.nextLine();
                    System.out.print("Introduce el mail del usuario: ");
                    String mailUsuario = sc.nextLine();
                    System.out.print("Introduce el año de nacimiento del usuario: ");
                    int nacimiento = sc.nextInt();
                    crearUsuario(usuario, mailUsuario, nacimiento);
                    break;
                case 5:
                    System.out.print("Introduce el id del usuario a eliminar: ");
                    try{
                        int idUsuario = sc.nextInt();
                        eliminarUsuario(idUsuario);
                    } catch (java.util.InputMismatchException e) {
                        // Si ocurre una excepción, mostramos un mensaje de error
                        System.out.println("¡Error! Debes introducir un número entero válido.\n");

                        // Limpiamos el buffer del Scanner
                        sc.nextLine(); // Consumimos el salto de línea residual
                    }
                    break;
                case 6:
                    System.out.print("Introduce el nombre del producto: ");
                    String nombreProducto = sc.nextLine();
                    System.out.print("Introduce el precio del producto: ");
                    Double precio = sc.nextDouble();
                    System.out.print("Introduce el stock del producto: ");
                    int stock = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Introduce el nombre de la categoría del producto: ");
                    String nombreCategoria = sc.nextLine();
                    System.out.print("Introduce el nif del contacto del proveedor: ");
                    String nifProveedor = sc.nextLine();
                    crearProducto(nombreProducto, precio, stock, nombreCategoria, nifProveedor);
                    break;
                case 7:
                    System.out.print("Introduce el nombre del producto que quieres eliminar: ");
                    String productoEliminar = sc.nextLine();
                    eliminarProductoPorNombre(productoEliminar);
                    break;
                case 8:
                    System.out.print("Introduce cual es el stock bajo del producto: ");
                    try{
                        int minimo = sc.nextInt();
                        listarProductosBajoStock(minimo);
                    } catch (java.util.InputMismatchException e) {
                        // Si ocurre una excepción, mostramos un mensaje de error
                        System.out.println("¡Error! Debes introducir un número entero válido.\n");

                        // Limpiamos el buffer del Scanner
                        sc.nextLine(); // Consumimos el salto de línea residual
                    }
                    break;
                case 9:
                    obtenerTotalPedidosUsuarios();
                    break;
                case 10:
                    obtenerCantidadProductosEnCadaAlmacen();
                    break;
                case 11:
                    listarTodosProductosConCategoriaYProveedor();
                    break;
                case 12:
                    System.out.print("Introduce el id de la categoría de los productos comprados: ");
                    try {
                        // Intentamos leer un número entero
                        int cat = sc.nextInt();

                        // Si no hay excepciones, se llama a la función
                        obtenerUsuariosCompraronProductosCategoria(cat);
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
    public static void crearCategoria(String nombreCategoria) {
        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO categorias (nombre_categoria) VALUES (?)";

            ps = SingletonPostgre.connection.prepareStatement(sql);
            ps.setString(1, nombreCategoria); // Asignar el parámetro de la categoría

            // Ejecutar la consulta de inserción
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("La categoría '" + nombreCategoria + "' se ha creado exitosamente.\n");
            } else {
                System.out.println("No se pudo crear la categoría.");
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }
    //2
    public static void crearNuevoProveedor(String nombreProveedor, String nif, int telefono, String email) {
        PreparedStatement ps = null;

        try {
            // SQL para insertar el nuevo proveedor
            String sql = "INSERT INTO proveedores (nombre_proveedor, contacto) VALUES (?, ROW(?, ?, ?, ?))";

            // Preparar el statement para evitar inyecciones SQL
            ps = SingletonPostgre.connection.prepareStatement(sql);
            ps.setString(1, nombreProveedor); // Asignar el nombre del proveedor
            ps.setString(2, nombreProveedor); // Asignar el nombre del proveedor
            ps.setString(3, nif); // Asignar el NIF
            ps.setInt(4, telefono); // Asignar el teléfono
            ps.setString(5, email); // Asignar el email

            // Ejecutar la consulta de inserción
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("El proveedor '" + nombreProveedor + "' se ha creado exitosamente.\n");
            } else {
                System.out.println("No se pudo crear el proveedor.");
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }
    //3
    public static void eliminarProveedor(int id) {
        PreparedStatement psCheck = null;
        PreparedStatement psDelete = null;

        try {
            // Primero, comprobamos si el proveedor existe con el ID proporcionado
            String checkSql = "SELECT id_proveedor FROM proveedores WHERE id_proveedor = ?";
            psCheck = SingletonPostgre.connection.prepareStatement(checkSql);
            psCheck.setInt(1, id); // Asignar el ID del proveedor

            ResultSet rs = psCheck.executeQuery();

            // Si el proveedor existe, procedemos a eliminarlo
            if (rs.next()) {
                String deleteSql = "DELETE FROM proveedores WHERE id_proveedor = ?";
                psDelete = SingletonPostgre.connection.prepareStatement(deleteSql);
                psDelete.setInt(1, id); // Asignar el ID del proveedor a eliminar

                int rowsAffected = psDelete.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("El proveedor con ID " + id + " ha sido eliminado exitosamente.\n");
                } else {
                    System.out.println("No se pudo eliminar el proveedor.");
                }
            } else {
                System.out.println("El proveedor con ID " + id + " no existe.\n");
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
    static void crearUsuario(String nombre, String email, int anho_nacimiento) {
        try {
            // Insertar en MySQL
            String sqlMySQL = "INSERT INTO usuarios (nombre, email, ano_nacimiento) VALUES (?, ?, ?)";
            PreparedStatement stmtMySQL = SingletonMySQL.connection.prepareStatement(sqlMySQL, Statement.RETURN_GENERATED_KEYS);
            stmtMySQL.setString(1, nombre);
            stmtMySQL.setString(2, email);
            stmtMySQL.setInt(3, anho_nacimiento);

            int affectedRowsMySQL = stmtMySQL.executeUpdate();
            ResultSet generatedKeysMySQL = stmtMySQL.getGeneratedKeys();
            int idProductoMySQL = -1;
            if (generatedKeysMySQL.next()) {
                idProductoMySQL = generatedKeysMySQL.getInt(1);
            }
            if (affectedRowsMySQL > 0 ) {
                System.out.println("Usuario creado exitosamente en bases de datos MySQL.\n");
            } else {
                System.out.println("Error al crear el usuario en MySQL.\n");
            }
        }catch(SQLException e){
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }
    //5
    static void eliminarUsuario(int id) {
        PreparedStatement psCheck = null;
        PreparedStatement psDelete = null;
        try {
            // Primero, comprobamos si el proveedor existe con el ID proporcionado
            String checkSql = "SELECT id_usuario FROM usuarios WHERE id_usuario = ?";
            psCheck = SingletonMySQL.connection.prepareStatement(checkSql);
            psCheck.setInt(1, id); // Asignar el ID del usuario

            ResultSet rs = psCheck.executeQuery();

            // Si el proveedor existe, procedemos a eliminarlo
            if (rs.next()) {
                PreparedStatement ps = SingletonMySQL.connection.prepareStatement("DELETE FROM pedidos_productos WHERE id_pedido IN (SELECT id_pedido FROM pedidos WHERE id_usuario = ?);");
                PreparedStatement ps2 = SingletonMySQL.connection.prepareStatement("DELETE FROM pedidos WHERE id_usuario = ?;");
                PreparedStatement ps3 = SingletonMySQL.connection.prepareStatement("DELETE FROM usuarios WHERE id_usuario = ?;");
                ps.setInt(1, id);
                ps2.setInt(1, id);
                ps3.setInt(1, id);
                ps.executeUpdate();
                ps2.executeUpdate();
                ps3.executeUpdate();
                System.out.println("Usuario eliminado con exito\n");

            } else {
                System.out.println("El usuario con ID " + id + " no existe.\n");
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());

        } finally {
            // Cerrar los recursos de forma segura
            try {
                if (psCheck != null) psCheck.close();
                if (psDelete != null) psDelete.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar los recursos: " + e.getMessage());
            }
        }
    }
    //6
    static void crearProducto(String nombre, Double precio, int stock, String nombre_categoria, String nif) {
        // Primero obtenemos el id_categoria y id_proveedor desde PostgreSQL
        int idCategoria = obtenerIdCategoria(nombre_categoria);
        int idProveedor = obtenerIdProveedor(nif);

        if (idCategoria != -1 && idProveedor != -1) {
            try {
                // Insertar en MySQL
                String sqlMySQL = "INSERT INTO productos (nombre_producto, precio, stock) VALUES (?, ?, ?)";
                PreparedStatement stmtMySQL = SingletonMySQL.connection.prepareStatement(sqlMySQL, Statement.RETURN_GENERATED_KEYS);
                stmtMySQL.setString(1, nombre);
                stmtMySQL.setDouble(2, precio);
                stmtMySQL.setInt(3, stock);

                int affectedRowsMySQL = stmtMySQL.executeUpdate();
                ResultSet generatedKeysMySQL = stmtMySQL.getGeneratedKeys();
                int idProductoMySQL = -1;
                if (generatedKeysMySQL.next()) {
                    idProductoMySQL = generatedKeysMySQL.getInt(1);
                }

                // Insertar en PostgreSQL con el id_producto autogenerado
                String sqlPostgreSQL = "INSERT INTO productos (id_producto, id_categoria, id_proveedor) " +
                        "VALUES (?, ?, ?)";
                PreparedStatement stmtPostgreSQL = SingletonPostgre.connection.prepareStatement(sqlPostgreSQL);
                stmtPostgreSQL.setInt(1, idProductoMySQL);  // Usamos el mismo id_producto
                stmtPostgreSQL.setInt(2, idCategoria);
                stmtPostgreSQL.setInt(3, idProveedor);

                int affectedRowsPostgreSQL = stmtPostgreSQL.executeUpdate();

                // Verificar si se insertó en ambas bases de datos
                if (affectedRowsMySQL > 0 && affectedRowsPostgreSQL > 0) {
                    System.out.println("Producto creado exitosamente en ambas bases de datos.\n");
                } else {
                    System.out.println("Error al insertar el producto en alguna de las bases de datos.\n");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error: No se pudo encontrar la categoría o el proveedor.\n");
        }
    }

    // Obtener el id de la categoría desde PostgreSQL
    private static int obtenerIdCategoria(String nombreCategoria) {
        int idCategoria = -1;

        try {
            String sqlPostgreSQL = "SELECT id_categoria FROM categorias WHERE nombre_categoria = ?";
            PreparedStatement stmtPostgreSQL = SingletonPostgre.connection.prepareStatement(sqlPostgreSQL);
            stmtPostgreSQL.setString(1, nombreCategoria);
            ResultSet rsPostgreSQL = stmtPostgreSQL.executeQuery();

            if (rsPostgreSQL.next()) {
                idCategoria = rsPostgreSQL.getInt("id_categoria");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idCategoria;
    }

    // Obtener el id del proveedor basado en el NIF desde PostgreSQL
    private static int obtenerIdProveedor(String nif) {
        int idProveedor = -1;

        try {
            String sqlPostgreSQL = "SELECT id_proveedor FROM proveedores WHERE (contacto).nif = ?";
            PreparedStatement stmtPostgreSQL = SingletonPostgre.connection.prepareStatement(sqlPostgreSQL);
            stmtPostgreSQL.setString(1, nif);
            ResultSet rsPostgreSQL = stmtPostgreSQL.executeQuery();

            if (rsPostgreSQL.next()) {
                idProveedor = rsPostgreSQL.getInt("id_proveedor");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idProveedor;
    }
    //7. Método para eliminar un producto por nombre en ambas bases de datos
    public static void eliminarProductoPorNombre(String nombre) {
        PreparedStatement psCheck = null;
        PreparedStatement psDelete = null;
        try {
            // Obtener el id_producto desde MySQL para eliminarlo de PostgreSQL
            String checkSql2 = "SELECT id_producto FROM productos WHERE nombre_producto = ?";
            psCheck = SingletonMySQL.connection.prepareStatement(checkSql2);
            psCheck.setString(1, nombre); // Asignar el nombre del producto
            ResultSet rs2 = psCheck.executeQuery();

            // Comprobamos si el producto existe con el nombre proporcionado
            String checkSql = "SELECT nombre_producto FROM productos WHERE nombre_producto = ?";
            psCheck = SingletonMySQL.connection.prepareStatement(checkSql);
            psCheck.setString(1, nombre); // Asignar el nombre del producto

            ResultSet rs = psCheck.executeQuery();


            // Si el producto existe, procedemos a eliminarlo
            if (rs.next()) {
                // Consulta para eliminar las relaciones en la tabla pedidos_productos
                String deletePedidosProductos = "DELETE FROM pedidos_productos WHERE id_producto IN (SELECT id_producto FROM productos WHERE nombre_producto = ?)";
                PreparedStatement stmtMySQL1 = SingletonMySQL.connection.prepareStatement(deletePedidosProductos);
                stmtMySQL1.setString(1, nombre);
                stmtMySQL1.executeUpdate(); // Usar executeUpdate() para DELETE

                // Eliminar el producto de la tabla productos
                String deleteProducto = "DELETE FROM productos WHERE nombre_producto = ?";
                PreparedStatement stmtMySQL2 = SingletonMySQL.connection.prepareStatement(deleteProducto);
                stmtMySQL2.setString(1, nombre);
                stmtMySQL2.executeUpdate(); // Usar executeUpdate() para DELETE

                System.out.println("Producto eliminado de MySQL: " + nombre);
            } else {
                System.out.println("El producto con nombre " + nombre + " no existe en la base de datos MySQL");
            }

            if (rs2.next()) {
                int idProducto = rs2.getInt("id_producto");
                // Ahora verificar si el id_producto existe en PostgreSQL
                String checkSqlPostgres = "SELECT id_producto FROM productos WHERE id_producto = ?";
                PreparedStatement psCheckPostgres = SingletonPostgre.connection.prepareStatement(checkSqlPostgres);

                // Asignar el id_producto obtenido de MySQL
                psCheckPostgres.setInt(1, idProducto);

                // Ejecutar la consulta en PostgreSQL
                ResultSet rsPostgres = psCheckPostgres.executeQuery();

                // Verificar si el id_producto existe en PostgreSQL
                if (rsPostgres.next()) {
                    // El id_producto existe en PostgreSQL
                    // Eliminar las relaciones en almacenes_productos en PostgreSQL
                    String deleteAlmacenesProductos = "DELETE FROM almacenes_productos WHERE id_producto = ?";
                    PreparedStatement stmtPostgreSQL2 = SingletonPostgre.connection.prepareStatement(deleteAlmacenesProductos);
                    stmtPostgreSQL2.setInt(1, idProducto);
                    stmtPostgreSQL2.executeUpdate();

                    // Eliminar el producto de la tabla productos en PostgreSQL
                    String deleteProductos = "DELETE FROM productos WHERE id_producto = ?";
                    PreparedStatement stmtPostgreSQL3 = SingletonPostgre.connection.prepareStatement(deleteProductos);
                    stmtPostgreSQL3.setInt(1, idProducto);
                    stmtPostgreSQL3.executeUpdate();

                    System.out.println("Producto eliminado de PostgreSQL: " + nombre+"\n");
                } else {
                    System.out.println("Producto no encontrado en PostgreSQL: " + nombre + "\n");
                }
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
    //8
    static void listarProductosBajoStock(int stock) {
        try {
            PreparedStatement ps = SingletonMySQL.connection.prepareStatement("SELECT id_producto, nombre_producto, precio, stock FROM productos WHERE stock < ?");
            ps.setInt(1, stock);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("Nombre: "+ rs.getString("nombre_producto") +"\nStock: " + rs.getInt("stock")+"\n");
            }
        } catch (SQLException e) {
            System.out.println("Error al hacer la consulta");
        }
    }
    //9
    static void obtenerTotalPedidosUsuarios(){
        try {
            PreparedStatement ps = SingletonMySQL.connection.prepareStatement("SELECT u.nombre, COUNT(p.id_pedido) AS total_pedidos\n" +
                    "FROM usuarios u\n" +
                    "LEFT JOIN pedidos p ON u.id_usuario = p.id_usuario\n" +
                    "GROUP BY u.id_usuario;");

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String nombreUsuario = rs.getString("nombre");  // Nombre del usuario
                int totalPedidos = rs.getInt("total_pedidos");  // Total de pedidos
                // Imprimir los resultados
                System.out.println("Nombre: " + nombreUsuario + "\nTotal Pedidos: " + totalPedidos + "\n");
            }

        } catch (SQLException e) {
            System.out.println("Error al hacer la consulta: "+ e.getMessage());
        }
    }
    //10
    static void obtenerCantidadProductosEnCadaAlmacen(){
        try {
            PreparedStatement ps = SingletonPostgre.connection.prepareStatement("SELECT a.nombre_almacen, SUM(ap.cantidad) AS total_productos\n" +
                    "        FROM almacenes a\n" +
                    "        JOIN almacenes_productos ap ON a.id_almacen = ap.id_almacen\n" +
                    "        GROUP BY a.id_almacen");

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String nombreAlmacen = rs.getString("nombre_almacen");  // Nombre del usuario
                int totalProductos = rs.getInt("total_productos");  // Total de pedidos
                // Imprimir los resultados
                System.out.println("Almacén: " + nombreAlmacen + "\nTotal productos: " + totalProductos + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //11
    static void listarTodosProductosConCategoriaYProveedor(){
        try {
            // Consulta en PostgreSQL (para obtener proveedores, categorías y productos)
            String sqlPostgreSQL = "SELECT \n" +
                    "    p.id_producto, \n" +
                    "    pr.nombre_proveedor, \n" +
                    "    (contacto).nombre_contacto, \n"+
                    "    (contacto).nif, \n"+
                    "    (contacto).telefono, \n"+
                    "    (contacto).email, \n"+
                    "    c.nombre_categoria\n" +
                    "FROM \n" +
                    "    productos p\n" +
                    "JOIN \n" +
                    "    proveedores pr ON p.id_proveedor = pr.id_proveedor\n" +
                    "JOIN \n" +
                    "    categorias c ON p.id_categoria = c.id_categoria";

            Statement stmtPostgreSQL = SingletonPostgre.connection.createStatement();
            ResultSet rsPostgreSQL = stmtPostgreSQL.executeQuery(sqlPostgreSQL);

            // Consulta en MySQL (para obtener precio y stock de los productos)
            String sqlMySQL = "SELECT id_producto, nombre_producto, precio, stock FROM productos";
            Statement stmtMySQL = SingletonMySQL.connection.createStatement();
            ResultSet rsMySQL = stmtMySQL.executeQuery(sqlMySQL);

            // Mapa para almacenar productos por id_producto para unir los resultados
            Map<Integer, String> productosMySQL = new HashMap<>();
            Map<Integer, Double> preciosMySQL = new HashMap<>();
            Map<Integer, Integer> stockMySQL = new HashMap<>();

            // Guardar los datos de MySQL
            while (rsMySQL.next()) {
                int idProducto = rsMySQL.getInt("id_producto");
                String nombreProducto = rsMySQL.getString("nombre_producto");
                double precio = rsMySQL.getDouble("precio");
                int stock = rsMySQL.getInt("stock");
                productosMySQL.put(idProducto, nombreProducto);
                preciosMySQL.put(idProducto, precio);
                stockMySQL.put(idProducto, stock);
            }

            // Ahora mostramos los resultados combinados de PostgreSQL y MySQL
            while (rsPostgreSQL.next()) {
                int idProducto = rsPostgreSQL.getInt("id_producto");
                String categoria = rsPostgreSQL.getString("nombre_categoria");
                String nombreProveedor = rsPostgreSQL.getString("nombre_proveedor");
                String nombreContacto = rsPostgreSQL.getString("nombre_contacto");
                String nif = rsPostgreSQL.getString("nif");
                int telefono = rsPostgreSQL.getInt("telefono");
                String email = rsPostgreSQL.getString("email");

                // Combinamos la información de ambas bases de datos
                String nombreProductoMySQL = productosMySQL.get(idProducto);
                Double precio = preciosMySQL.get(idProducto);
                if (precio != null) {
                    int stock = stockMySQL.get(idProducto);

                    // Mostrar los resultados combinados
                    System.out.println("Producto: " + nombreProductoMySQL);
                    System.out.println("Categoría: " + categoria);
                    System.out.println("Proveedor: " + nombreProveedor);
                    System.out.println("Nombre del contacto del proveedor: " + nombreContacto);
                    System.out.println("NIF: " + nif);
                    System.out.println("Teléfono: " + telefono);
                    System.out.println("Email: " + email);
                    System.out.println("Precio: " + precio);
                    System.out.println("Stock: " + stock);
                    System.out.println("----------------------------------------");
                } else {
                    // Manejar el caso cuando el precio es null
                    System.out.println("No se encontraron datos para el producto con ID: " + idProducto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //12
    static void obtenerUsuariosCompraronProductosCategoria(int idCategoria) {
        try {
            // Paso 1: Obtener los productos de PostgreSQL para la categoría dada
            String queryPostgreSQL = "SELECT id_producto FROM productos WHERE id_categoria = ?";
            PreparedStatement stmtPostgreSQL = SingletonPostgre.connection.prepareStatement(queryPostgreSQL);
            stmtPostgreSQL.setInt(1, idCategoria);
            ResultSet rsPostgreSQL = stmtPostgreSQL.executeQuery();

            // Crear una lista de id_producto de PostgreSQL
            List<Integer> productos = new ArrayList<>();
            while (rsPostgreSQL.next()) {
                productos.add(rsPostgreSQL.getInt("id_producto"));
            }

            // Si no se encontraron productos para esta categoría
            if (productos.isEmpty()) {
                System.out.println("No hay productos en esta categoría.");
                return;
            }

            // Paso 2: Obtener los usuarios de MySQL que compraron esos productos
            // Convertir la lista de productos a una cadena de valores
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < productos.size(); i++) {
                sb.append("?");
                if (i < productos.size() - 1) {
                    sb.append(", ");
                }
            }

            // Consulta para obtener los usuarios que compraron los productos de la lista
            String queryMySQL = "SELECT DISTINCT u.nombre " +
                    "FROM usuarios u " +
                    "JOIN pedidos p ON u.id_usuario = p.id_usuario " +
                    "JOIN pedidos_productos pp ON p.id_pedido = pp.id_pedido " +
                    "WHERE pp.id_producto IN (" + sb.toString() + ")";
            PreparedStatement stmtMySQL = SingletonMySQL.connection.prepareStatement(queryMySQL);

            // Asignar los valores de los productos a la consulta
            for (int i = 0; i < productos.size(); i++) {
                stmtMySQL.setInt(i + 1, productos.get(i));
            }

            ResultSet rsMySQL = stmtMySQL.executeQuery();

            // Paso 3: Mostrar los nombres de los usuarios
            boolean usuariosEncontrados = false;
            while (rsMySQL.next()) {
                String nombreUsuario = rsMySQL.getString("nombre");
                System.out.println("Usuario que compró un producto de la categoría: " + nombreUsuario);
                usuariosEncontrados = true;
            }

            if (!usuariosEncontrados) {
                System.out.println("No hay usuarios que hayan comprado productos de esta categoría.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
