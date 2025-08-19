import java.sql.*;
import java.util.Scanner;

public class EmployeeDBApp {
    
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "pass123";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected to Database");

            while (true) {
                System.out.println("\n--- Employee Menu ---");
                System.out.println("1) Add Employee");
                System.out.println("2) View Employees");
                System.out.println("3) Update Employee");
                System.out.println("4) Delete Employee");
                System.out.println("0) Exit");
                System.out.print("Choice: ");
                String choice = sc.nextLine();

                switch (choice) {
                    case "1" -> addEmployee(conn, sc);
                    case "2" -> viewEmployees(conn);
                    case "3" -> updateEmployee(conn, sc);
                    case "4" -> deleteEmployee(conn, sc);
                    case "0" -> { System.out.println(" Goodbye!"); return; }
                    default -> System.out.println("Invalid choice.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter role: ");
        String role = sc.nextLine();
        System.out.print("Enter salary: ");
        double salary = Double.parseDouble(sc.nextLine());

        String sql = "INSERT INTO employee (name, role, salary) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, role);
            ps.setDouble(3, salary);
            ps.executeUpdate();
            System.out.println("Employee added.");
        }
    }

    private static void viewEmployees(Connection conn) throws SQLException {
        String sql = "SELECT * FROM employee";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println(" Employee List:");
            while (rs.next()) {
                System.out.printf("[%d] %s | %s | %.2f\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getDouble("salary"));
            }
        }
    }

    private static void updateEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to update: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("New Role: ");
        String role = sc.nextLine();
        System.out.print("New Salary: ");
        double salary = Double.parseDouble(sc.nextLine());

        String sql = "UPDATE employee SET role=?, salary=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setDouble(2, salary);
            ps.setInt(3, id);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Employee updated.");
            else System.out.println("Employee not found.");
        }
    }

    private static void deleteEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to delete: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM employee WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Employee deleted.");
            else System.out.println("Employee not found.");
        }
    }
}
