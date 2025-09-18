
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EstudianteCRUD estudianteCRUD = new EstudianteCRUD();

    public static void main(String[] args) {
        mostrarMenu();
    }

    private static void mostrarMenu() {
        try (scanner) {
            int opcion;
            do {
                System.out.println("\n=== SISTEMA DE GESTIÓN DE ESTUDIANTES ===");
                System.out.println("1. Insertar Estudiante");
                System.out.println("2. Actualizar Estudiante");
                System.out.println("3. Eliminar Estudiante");
                System.out.println("4. Consultar todos los estudiantes");
                System.out.println("5. Consultar Estudiante por email");
                System.out.println("6. Salir");
                System.out.print("Seleccione una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                switch (opcion) {
                    case 1 -> insertarEstudiante();
                    case 2 -> actualizarEstudiante();
                    case 3 -> eliminarEstudiante();
                    case 4 -> consultarTodosEstudiantes();
                    case 5 -> consultarEstudiantePorEmail();
                    case 6 -> System.out.println("Saliendo del sistema...");
                    default -> System.out.println("Opción no válida");
                }
            } while (opcion != 6);
        }
    }

    private static void insertarEstudiante() {
        System.out.println("\n--- INSERTAR ESTUDIANTE ---");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine();

        System.out.print("Edad: ");
        int edad = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        System.out.println("Estados civiles disponibles:");
        System.out.println("1. SOLTERO");
        System.out.println("2. CASADO");
        System.out.println("3. VIUDO");
        System.out.println("4. UNION_LIBRE");
        System.out.println("5. DIVORCIADO");
        System.out.print("Seleccione el estado civil (1-5): ");
        int opcionEstado = scanner.nextInt();
        scanner.nextLine();

        String estadoCivil;
        switch (opcionEstado) {
            case 1 -> estadoCivil = "SOLTERO";
            case 2 -> estadoCivil = "CASADO";
            case 3 -> estadoCivil = "VIUDO";
            case 4 -> estadoCivil = "UNION_LIBRE";
            case 5 -> estadoCivil = "DIVORCIADO";
            default -> {
                System.out.println("Opción no válida, se asignará SOLTERO");
                estadoCivil = "SOLTERO";
            }
        }

        estudianteCRUD.insertarEstudiante(nombre, apellido, correo, edad, estadoCivil);
    }

    private static void actualizarEstudiante() {
        System.out.println("\n--- ACTUALIZAR ESTUDIANTE ---");

        System.out.print("Ingrese el ID del estudiante a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        System.out.print("Nuevo nombre (dejar vacío para mantener actual): ");
        String nombre = scanner.nextLine();

        System.out.print("Nuevo apellido (dejar vacío para mantener actual): ");
        String apellido = scanner.nextLine();

        System.out.print("Nueva edad (0 para mantener actual): ");
        int edad = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Nuevo estado civil:");
        System.out.println("1. SOLTERO");
        System.out.println("2. CASADO");
        System.out.println("3. VIUDO");
        System.out.println("4. UNION_LIBRE");
        System.out.println("5. DIVORCIADO");
        System.out.println("0. Mantener actual");
        System.out.print("Seleccione una opción: ");
        int opcionEstado = scanner.nextInt();
        scanner.nextLine();

        String estadoCivil = null;
        if (opcionEstado != 0) {
            switch (opcionEstado) {
                case 1 -> estadoCivil = "SOLTERO";
                case 2 -> estadoCivil = "CASADO";
                case 3 -> estadoCivil = "VIUDO";
                case 4 -> estadoCivil = "UNION_LIBRE";
                case 5 -> estadoCivil = "DIVORCIADO";
                default -> System.out.println("Opción no válida, se mantendrá el estado actual");
            }
        }

        estudianteCRUD.actualizarEstudiante(id, nombre, apellido, edad, estadoCivil);
    }

    private static void eliminarEstudiante() {
        System.out.println("\n--- ELIMINAR ESTUDIANTE ---");

        System.out.print("Ingrese el ID del estudiante a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        estudianteCRUD.eliminarEstudiante(id);
    }

    private static void consultarTodosEstudiantes() {
        estudianteCRUD.consultarTodosEstudiantes();
    }

    private static void consultarEstudiantePorEmail() {
        System.out.println("\n--- CONSULTAR ESTUDIANTE POR EMAIL ---");

        System.out.print("Ingrese el correo electrónico: ");
        String correo = scanner.nextLine();

        estudianteCRUD.consultarEstudiantePorEmail(correo);
    }
}
