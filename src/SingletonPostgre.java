import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonPostgre{

    static Connection connection;
    String dbURL = "jdbc:postgresql://localhost:5432/"; // URL de conexión
    String user = "postgres"; // Usuario de la base de datos
    String password = "abc123."; // Contraseña de la base de datos
    String databaseName = "almacenPostGre"; // Nombre de la base de datos

    // El constructor del singleton siempre debe ser privado para evitar llamadas de construcción directas con el operador `new`.
    private SingletonPostgre(){
        try {
            this.connection = DriverManager.getConnection(dbURL + databaseName, user, password);
            System.out.println("Conexión exitosa a la base de datos: " + databaseName);
        }catch(SQLException sqle) {
            System.out.println("Error al abrir la conexión");
        }
    }

    // El método estático que controla el acceso a la instancia
    // singleton.
    public static Connection getInstance(){
        if (SingletonPostgre.connection == null)
            new SingletonPostgre();
        return SingletonPostgre.connection;
    }
}
