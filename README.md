# JDBCProductos (Oracle + JDBC)

Proyecto de práctica para crear una tabla **productos** en Oracle y hacer inserciones desde Java usando **JDBC**.

## Qué hace este proyecto
- Crea (en Oracle) una tabla `productos`
- Crea una **secuencia** para generar IDs automáticos
- Crea un **trigger** para asignar el ID automáticamente al insertar
- Inserta productos desde Java con `PreparedStatement`

## Requisitos
- Java (JDK) instalado
- Oracle Database 21c (local)
- Oracle SQL Developer
- IntelliJ IDEA + Maven

## 1) Crear la tabla, secuencia y trigger en Oracle
```sql
CREATE TABLE productos (
id          NUMBER(10) PRIMARY KEY,
nombre      VARCHAR2(100) NOT NULL,
descripcion VARCHAR2(255),
precio      NUMBER(10,2) NOT NULL
);

CREATE SEQUENCE seq_productos
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER trg_productos_bi
BEFORE INSERT ON productos
FOR EACH ROW
BEGIN
IF :NEW.id IS NULL THEN
:NEW.id := seq_productos.NEXTVAL;
END IF;
END;
/
INSERT INTO productos (nombre, descripcion, precio)
VALUES ('Teclado', 'Teclado mecánico', 79.99);
COMMIT;

SELECT * FROM productos;
```
## 2) Configurar el proyecto Java con Maven
Agregar la dependencia de Oracle JDBC en el `pom.xml`:
```xml
    <dependencies>
        <!-- Driver JDBC de Oracle: permite a Java conectarse a Oracle -->
        <dependency>
            <groupId>com.oracle.database.jdbc</groupId>
            <artifactId>ojdbc11</artifactId>
            <version>21.11.0.0</version>
        </dependency>

    </dependencies>
```
## 3) Código Java para insertar productos
```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
public class Main {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/orclpdb";
    private static final String USER = "tu_usuario";
    private static final String PASSWORD = "tu_contraseña";
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
```
## 4) Ejecutar el proyecto
- Configura tu conexión en el código Java (usuario, contraseña)
- Ejecuta la clase `Main`
- Ingresa los datos del producto cuando se te solicite
- Verifica en Oracle que el producto se haya insertado correctamente:
```sqlSELECT * FROM productos;
```
¡Y eso es todo! Has creado una tabla en Oracle y has insertado datos desde Java usando JDBC.
