import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class EmployeeManagement {

    static Connection con;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {

            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/employee_db",
                "root",
                "Navya@1234"
            );

            System.out.println("Database connected successfully!");

            while (true) {

                System.out.println("\n===== Employee Management System =====");
                System.out.println("1. Insert Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Transaction Test");
                System.out.println("6. Commit Test");
                System.out.println("7. Exit");

                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();

                switch (choice) {

                    case 1:
                        insertEmployee();
                        break;

                    case 2:
                        viewEmployees();
                        break;

                    case 3:
                        updateEmployee();
                        break;

                    case 4:
                        deleteEmployee();
                        break;

                    case 5:
                        transactionTest();
                        break;

                    case 6:
                        commitTest();
                        break;

                    case 7:
                        System.out.println("Thank you!");
                        con.close();
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void insertEmployee() throws Exception {

        System.out.print("Enter Employee ID: ");
        int id = sc.nextInt();

        System.out.print("Enter Name: ");
        String name = sc.next();

        System.out.print("Enter Department: ");
        String department = sc.next();

        System.out.print("Enter Salary: ");
        double salary = sc.nextDouble();

        System.out.print("Enter Experience: ");
        int experience = sc.nextInt();

        String sql = "INSERT INTO employee (emp_id, name, department, salary, experience) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, department);
        ps.setDouble(4, salary);
        ps.setInt(5, experience);

        int rows = ps.executeUpdate();

        System.out.println(rows + " employee inserted successfully!");

        ps.close();
    }

    static void viewEmployees() throws Exception {

        String sql = "SELECT * FROM employee";

        PreparedStatement ps = con.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        System.out.println("\nID\tName\tDepartment\tSalary\tExperience");

        while (rs.next()) {

            System.out.println(
                rs.getInt("emp_id") + "\t" +
                rs.getString("name") + "\t" +
                rs.getString("department") + "\t" +
                rs.getDouble("salary") + "\t" +
                rs.getInt("experience")
            );
        }

        rs.close();
        ps.close();
    }

    static void updateEmployee() throws Exception {

        System.out.print("Enter Employee ID to update: ");
        int id = sc.nextInt();

        System.out.print("Enter new Salary: ");
        double salary = sc.nextDouble();

        String sql = "UPDATE employee SET salary = ? WHERE emp_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setDouble(1, salary);
        ps.setInt(2, id);

        int rows = ps.executeUpdate();

        System.out.println(rows + " employee updated successfully!");

        ps.close();
    }

    static void deleteEmployee() throws Exception {

        System.out.print("Enter Employee ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM employee WHERE emp_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, id);

        int rows = ps.executeUpdate();

        System.out.println(rows + " employee deleted successfully!");

        ps.close();
    }

    static void transactionTest() throws Exception {

        con.setAutoCommit(false);

        try {

            System.out.print("Enter Employee ID for transaction: ");
            int id = sc.nextInt();

            System.out.print("Enter temporary salary: ");
            double salary = sc.nextDouble();

            String sql = "UPDATE employee SET salary = ? WHERE emp_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDouble(1, salary);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            System.out.println(rows + " employee updated inside transaction.");

            System.out.println("Rolling back transaction...");

            con.rollback();

            System.out.println("Transaction rolled back successfully!");

            ps.close();

        } catch (Exception e) {

            con.rollback();

            System.out.println("Transaction failed and rollback completed.");
        }

        con.setAutoCommit(true);
    }

    static void commitTest() throws Exception {

        con.setAutoCommit(false);

        System.out.print("Enter Employee ID for commit: ");
        int id = sc.nextInt();

        System.out.print("Enter new salary: ");
        double salary = sc.nextDouble();

        String sql = "UPDATE employee SET salary = ? WHERE emp_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setDouble(1, salary);
        ps.setInt(2, id);

        int rows = ps.executeUpdate();

        con.commit();

        System.out.println(rows + " employee updated and committed successfully!");

        ps.close();

        con.setAutoCommit(true);
    }
}