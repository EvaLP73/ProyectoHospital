import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonMySQL{

    static Connection connection;
    private final String usuario = "root";
    private final String clave = "abc123.";
    private final String url ="jdbc:mysql://localhost:3306/ventasMySQL";

    // El constructor del singleton siempre debe ser privado para evitar llamadas de construcción directas con el operador `new`.
    private SingletonMySQL(){
        try {
            this.connection = DriverManager.getConnection(url, usuario, clave);
            System.out.println("Conexión exitosa a la base de datos: ventasMySQL" );
        }catch(SQLException sqle) {
            System.out.println("Error al abrir la conexión");
        }
    }

    // El método estático que controla el acceso a la instancia
    // singleton.
    public static Connection getInstance(){
        if (SingletonMySQL.connection == null)
            new SingletonMySQL();
        return SingletonMySQL.connection;
    }
}

