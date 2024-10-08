import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ModificationClasses {

    // Insert a new client
    public static void insertClient(Connection conn) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter clientID: ");
        int clientID = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        String sql = "INSERT INTO CLIENT (clientID, name, address, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clientID);
            pstmt.setString(2, name);
            pstmt.setString(3, address);
            pstmt.setString(4, phone);
            pstmt.executeUpdate();
            System.out.println("Insert successful.");
            selectClientById(conn, clientID);  // Print the inserted record
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // Insert a new event
    public static void insertEvent(Connection conn) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter eventID: ");
        int eventID = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter startDate (DD-MM-YYYY): ");
        String startDate = scanner.nextLine();

        System.out.print("Enter endDate (DD-MM-YYYY): ");
        String endDate = scanner.nextLine();

        System.out.print("Enter loadInDate (DD-MM-YYYY): ");
        String loadInDate = scanner.nextLine();

        System.out.print("Enter loadOutDate (DD-MM-YYYY): ");
        String loadOutDate = scanner.nextLine();

        System.out.print("Enter eventDetails: ");
        String eventDetails = scanner.nextLine();

        System.out.print("Enter eventAddress: ");
        String eventAddress = scanner.nextLine();

        String sql = "INSERT INTO EVENT (eventID, startDate, endDate, loadInDate, loadOutDate, eventDetails, eventAddress) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventID);
            pstmt.setString(2, startDate);
            pstmt.setString(3, endDate);
            pstmt.setString(4, loadInDate);
            pstmt.setString(5, loadOutDate);
            pstmt.setString(6, eventDetails);
            pstmt.setString(7, eventAddress);
            pstmt.executeUpdate();
            System.out.println("Insert successful.");
            selectEventById(conn, eventID);  // Print the inserted record
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // Insert a new employee
    public static void insertEmployee(Connection conn) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter ESSN: ");
        int ESSN = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter employeeType: ");
        String employeeType = scanner.nextLine();

        System.out.print("Enter skillLevel: ");
        int skillLevel = Integer.parseInt(scanner.nextLine());

        String sql = "INSERT INTO EMPLOYEE (ESSN, name, phone, employeeType, skillLevel) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ESSN);
            pstmt.setString(2, name);
            pstmt.setString(3, phone);
            pstmt.setString(4, employeeType);
            pstmt.setInt(5, skillLevel);
            pstmt.executeUpdate();
            System.out.println("Insert successful.");
            selectEmployeeById(conn, ESSN);  // Print the inserted record
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // Insert new equipment
    public static void insertEquipment(Connection conn) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter equipmentID: ");
        int equipmentID = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter make: ");
        String make = scanner.nextLine();

        System.out.print("Enter model: ");
        String model = scanner.nextLine();

        System.out.print("Enter storageLocation: ");
        String storageLocation = scanner.nextLine();

        System.out.print("Enter equipmentType: ");
        String equipmentType = scanner.nextLine();

        System.out.print("Is the equipment checked out?: ");
        boolean isCheckedOut = Boolean.parseBoolean(scanner.nextLine());

        System.out.print("Enter lastServiceDate (DD-MM-YYYY): ");
        String lastServiceDate = scanner.nextLine();

        String sql = "INSERT INTO EQUIPMENT (equipmentID, make, model, storageLocation, equipmentType, isCheckedOut, lastServiceDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, equipmentID);
            pstmt.setString(2, make);
            pstmt.setString(3, model);
            pstmt.setString(4, storageLocation);
            pstmt.setString(5, equipmentType);
            pstmt.setBoolean(6, isCheckedOut);
            pstmt.setString(7, lastServiceDate);
            pstmt.executeUpdate();
            System.out.println("Insert successful.");
            selectEquipmentById(conn, equipmentID);  // Print the inserted record
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }




    // Select and print the inserted client
    public static void selectClientById(Connection conn, int clientID) {
        String sql = "SELECT * FROM CLIENT WHERE clientID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clientID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("clientID: " + rs.getInt("clientID"));
                    System.out.println("name: " + rs.getString("name"));
                    System.out.println("address: " + rs.getString("address"));
                    System.out.println("phone: " + rs.getString("phone"));
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // Select and print the inserted event
    public static void selectEventById(Connection conn, int eventID) {
        String sql = "SELECT * FROM EVENT WHERE eventID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("eventID: " + rs.getInt("eventID"));
                    System.out.println("startDate: " + rs.getString("startDate"));
                    System.out.println("endDate: " + rs.getString("endDate"));
                    System.out.println("loadInDate: " + rs.getString("loadInDate"));
                    System.out.println("loadOutDate: " + rs.getString("loadOutDate"));
                    System.out.println("eventDetails: " + rs.getString("eventDetails"));
                    System.out.println("eventAddress: " + rs.getString("eventAddress"));
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // Select and print the inserted employee
    public static void selectEmployeeById(Connection conn, int ESSN) {
        String sql = "SELECT * FROM EMPLOYEE WHERE ESSN = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ESSN);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ESSN: " + rs.getInt("ESSN"));
                    System.out.println("name: " + rs.getString("name"));
                    System.out.println("phone: " + rs.getString("phone"));
                    System.out.println("employeeType: " + rs.getString("employeeType"));
                    System.out.println("skillLevel: " + rs.getInt("skillLevel"));
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // Select and print the inserted equipment
    public static void selectEquipmentById(Connection conn, int equipmentID) {
        String sql = "SELECT * FROM EQUIPMENT WHERE equipmentID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, equipmentID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("equipmentID: " + rs.getInt("equipmentID"));
                    System.out.println("make: " + rs.getString("make"));
                    System.out.println("model: " + rs.getString("model"));
                    System.out.println("storageLocation: " + rs.getString("storageLocation"));
                    System.out.println("equipmentType: " + rs.getString("equipmentType"));
                    System.out.println("isCheckedOut: " + rs.getBoolean("isCheckedOut"));
                    System.out.println("lastServiceDate: " + rs.getString("lastServiceDate"));
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}

