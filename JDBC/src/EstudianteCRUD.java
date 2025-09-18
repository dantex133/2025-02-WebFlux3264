


import java.sql.*;

public class EstudianteCRUD {
    private static final String URL = "jdbc:mysql://localhost:3306/universidad";
    private static final String USER = "root";
    private static final String PASSWORD = "123j";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver JDBC no encontrado");
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void insertarEstudiante(String nombre, String apellido, String correo, int edad, String estadoCivil) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO estudiantes (nombre, apellido, correo, edad, estado_civil) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nombre);
                pstmt.setString(2, apellido);
                pstmt.setString(3, correo);
                pstmt.setInt(4, edad);
                pstmt.setString(5, estadoCivil);

                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    System.out.println("Estudiante insertado correctamente");
                } else {
                    System.out.println("No se pudo insertar el estudiante");
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Código de error para duplicados
                System.out.println("Error: El correo electrónico ya existe");
            } else {
                System.out.println("Error al insertar estudiante: " + e.getMessage());
            }
        }
    }

    public void actualizarEstudiante(int id, String nombre, String apellido, int edad, String estadoCivil) {
        try (Connection conn = getConnection()) {
            // Primero verificar si el estudiante existe
            if (!existeEstudiante(id)) {
                System.out.println("No se encontró ningún estudiante con ID: " + id);
                return;
            }

            StringBuilder sql = new StringBuilder("UPDATE estudiantes SET ");
            boolean first = true;

            if (nombre != null && !nombre.isEmpty()) {
                sql.append("nombre = ?");
                first = false;
            }
            if (apellido != null && !apellido.isEmpty()) {
                if (!first) sql.append(", ");
                sql.append("apellido = ?");
                first = false;
            }
            if (edad > 0) {
                if (!first) sql.append(", ");
                sql.append("edad = ?");
                first = false;
            }
            if (estadoCivil != null) {
                if (!first) sql.append(", ");
                sql.append("estado_civil = ?");
            }

            sql.append(" WHERE id = ?");

            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                int paramIndex = 1;

                if (nombre != null && !nombre.isEmpty()) {
                    pstmt.setString(paramIndex++, nombre);
                }
                if (apellido != null && !apellido.isEmpty()) {
                    pstmt.setString(paramIndex++, apellido);
                }
                if (edad > 0) {
                    pstmt.setInt(paramIndex++, edad);
                }
                if (estadoCivil != null) {
                    pstmt.setString(paramIndex++, estadoCivil);
                }

                pstmt.setInt(paramIndex, id);

                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    System.out.println("Estudiante actualizado correctamente");
                } else {
                    System.out.println("No se pudo actualizar el estudiante");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar estudiante: " + e.getMessage());
        }
    }

    public void eliminarEstudiante(int id) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM estudiantes WHERE id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    System.out.println("Estudiante eliminado correctamente");
                } else {
                    System.out.println("No se encontró ningún estudiante con ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar estudiante: " + e.getMessage());
        }
    }

    public void consultarTodosEstudiantes() {
        try (Connection conn = getConnection()) {
            System.out.println("\n--- TODOS LOS ESTUDIANTES ---");

            String sql = "SELECT * FROM estudiantes ORDER BY id";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                if (!rs.isBeforeFirst()) {
                    System.out.println("No hay estudiantes registrados");
                    return;
                }

                System.out.printf("%-5s %-20s %-20s %-30s %-5s %-15s%n",
                        "ID", "Nombre", "Apellido", "Correo", "Edad", "Estado Civil");
                System.out.println("-------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    String correo = rs.getString("correo");
                    int edad = rs.getInt("edad");
                    String estadoCivil = rs.getString("estado_civil");

                    System.out.printf("%-5d %-20s %-20s %-30s %-5d %-15s%n",
                            id, nombre, apellido, correo, edad, estadoCivil);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar estudiantes: " + e.getMessage());
        }
    }

    public void consultarEstudiantePorEmail(String correo) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM estudiantes WHERE correo = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, correo);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\n--- DATOS DEL ESTUDIANTE ---");
                        System.out.println("ID: " + rs.getInt("id"));
                        System.out.println("Nombre: " + rs.getString("nombre"));
                        System.out.println("Apellido: " + rs.getString("apellido"));
                        System.out.println("Correo: " + rs.getString("correo"));
                        System.out.println("Edad: " + rs.getInt("edad"));
                        System.out.println("Estado Civil: " + rs.getString("estado_civil"));
                    } else {
                        System.out.println("No se encontró ningún estudiante con el correo: " + correo);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar estudiante: " + e.getMessage());
        }
    }

    private boolean existeEstudiante(int id) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT id FROM estudiantes WHERE id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar estudiante: " + e.getMessage());
            return false;
        }
    }


}