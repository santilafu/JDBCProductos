import java.sql.Connection;         // Conexión con la base de datos
import java.sql.DriverManager;      // Abre la conexión
import java.sql.PreparedStatement;  // Para hacer INSERT con valores
import java.sql.SQLException;       // Errores de SQL

public class Main {

    // Dirección de la base de datos:
    // localhost = tu ordenador
    // 1521 = puerto típico de Oracle
    // orclpdb = nombre del servicio (tu base de datos PDB)
    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/orclpdb";

    // Usuario y contraseña que creamos en SQL Developer
    private static final String USER = "system";
    private static final String PASS = "Santilafu12";

    public static void main(String[] args) {

        // INSERT: metemos nombre, descripcion y precio.
        // El id NO lo metemos porque el trigger lo pone solo.
        String insertSql = "INSERT INTO productos (nombre, descripcion, precio) VALUES (?, ?, ?)";

        // try-with-resources:
        // Lo que se abre aquí se cierra solo al final (aunque haya errores)
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(insertSql)) {

            // 1) Primer producto
            ps.setString(1, "Auriculares");
            ps.setString(2, "Auriculares Bluetooth");
            ps.setDouble(3, 59.99);
            ps.executeUpdate(); // Ejecuta el INSERT

            // 2) Segundo producto
            ps.setString(1, "USB 64GB");
            ps.setString(2, "Memoria USB rápida");
            ps.setDouble(3, 12.90);
            ps.executeUpdate();

            // 3) Tercer producto
            ps.setString(1, "Webcam");
            ps.setString(2, "Webcam 1080p");
            ps.setDouble(3, 34.95);
            ps.executeUpdate();

            System.out.println("Productos insertados correctamente desde Java.");

        } catch (SQLException e) {
            // Si pasa algo, lo mostramos
            System.out.println("Error SQL: " + e.getMessage());
            System.out.println("Código Oracle: " + e.getErrorCode());
        }
    }
}
