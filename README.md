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
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "tu_usuario";
    private static final String PASSWORD = "tu_contraseña";
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {
            String sql = "INSERT INTO productos (nombre, descripcion, precio) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                System.out.print("Nombre del producto: ");
                String nombre = scanner.nextLine();
                System.out.print("Descripción del producto: ");
                String descripcion = scanner.nextLine();
                System.out.print("Precio del producto: ");
                double precio = scanner.nextDouble();
                pstmt.setString(1, nombre);
                pstmt.setString(2, descripcion);
                pstmt.setDouble(3, precio);
                int filasInsertadas = pstmt.executeUpdate();
                System.out.println(filasInsertadas + " producto(s) insertado(s).");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
