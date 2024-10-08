package ser322;

import java.util.Scanner;
import java.sql.*;

public class EventRentalQuerying {

    static Scanner input = new Scanner(System.in);
    public static void main (String[] args) {

        String url = "jdbc:mysql://localhost:3306/EventRental";
        String user = "root";
        String password = "Ru$7yB1k35";


        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        System.out.println("Welcome to the event rental database!");
        System.out.println("Select from the following options:");
        System.out.println("1. View Employee Data");
        System.out.println("2. View Event Data");
        System.out.println("3. View Equipment Data");


        System.out.println("Enter Employee ID#: ");
        String empID = input.next();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database");
            System.out.println("Populating Database...");



            stmt = conn.prepareStatement("SELECT e.eventID, e.eventDetails, e.startDate, e.endDate\n" +
                    "FROM EVENT e\n" +
                    "JOIN HAS_EVENT h ON e.eventID = h.eventID\n" +
                    "WHERE h.clientID = ? ");
            stmt.setString(1, empID);
            rs = stmt.executeQuery();
            System.out.println("eventID\teventDetails\tstartDate\tendDate");
            System.out.println("-----------------------");
            while (rs.next()) {
                System.out.print(rs.getString(1) + "\t");
                System.out.print(rs.getString(2) + "\t ");
                System.out.print(rs.getDate(3) + "\n");
                System.out.println(rs.getDate(4) + "\t" + rs.getString(5));
            }
            System.out.println("-----------------------");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static void selectClientEventDetails(String url, String user, String password) {

        // ask for client ID from user
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter Client ID: ");
        String clientID = input.next();

        // create connection stuff
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {

            // load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // connect to database
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database");

            // prepare SQL statement with user-provided clientID
            stmt = conn.prepareStatement("SELECT e.eventID, e.eventDetails, e.startDate, e.endDate\n" +
                    "FROM EVENT e\n" +
                    "JOIN HAS_EVENT h ON e.eventID = h.eventID\n" +
                    "WHERE h.clientID = ? ");
            stmt.setString(1, clientID);

            // execute, pretty-print
            rs = stmt.executeQuery();
            System.out.println("Event ID\tDetails\tStart Date\tEnd Date");
            System.out.println("-----------------------");
            while (rs.next()) {
                System.out.print(rs.getString(1) + "\t");
                System.out.print(rs.getString(2) + "\t ");
                System.out.print(rs.getDate(3) + "\t");
                System.out.println(rs.getDate(4) + "\n");
            }
            System.out.println("-----------------------");

            // catch exceptions, print error to console
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
        }
        finally {
            // close stuff
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static void selectEmployeeDetailsByEvent(String url, String user, String password) {

        // ask for client ID from user
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter Event ID: ");
        String eventID = input.next();

        // create connection stuff
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {

            // load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // connect to database
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database");

            // prepare SQL statement with user-provided clientID
            stmt = conn.prepareStatement("SELECT emp.Name, emp.employeeType, wa.workStartDate, wa.workEndDate\n" +
                    "FROM EMPLOYEE emp\n" +
                    "JOIN WORKS_AT wa ON emp.ESSN = wa.ESSN\n" +
                    "WHERE wa.eventID = ?");
            stmt.setString(1, eventID);

            // execute, pretty-print
            rs = stmt.executeQuery();
            System.out.println("Name\tEmployee Type\tWork Start\tWork End");
            System.out.println("-----------------------");
            while (rs.next()) {
                System.out.print(rs.getString(1) + "\t");
                System.out.print(rs.getInt(2) + "\t ");
                System.out.print(rs.getDate(3) + "\t");
                System.out.println(rs.getDate(4) + "\n");
            }
            System.out.println("-----------------------");

            // catch exceptions, print error to console
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
        }
        finally {
            // close stuff
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static void selectEquipmentDetailsByEvent(String url, String user, String password) {

        // ask for client ID from user
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter Event ID: ");
        String eventID = input.next();

        // create connection stuff
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {

            // load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // connect to database
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database");

            // prepare SQL statement with user-provided clientID
            stmt = conn.prepareStatement("SELECT eq.make, eq.model, r.outDate, r.dueDate\n" +
                    "FROM EQUIPMENT eq\n" +
                    "JOIN RESERVED_FOR r ON eq.equipmentID = r.equipmentID\n" +
                    "WHERE r.eventID = ?");
            stmt.setString(1, eventID);

            // execute, pretty-print
            rs = stmt.executeQuery();
            System.out.println("Make\tModel\tOut Date\tDue Date");
            System.out.println("-----------------------");
            while (rs.next()) {
                System.out.print(rs.getString(1) + "\t");
                System.out.print(rs.getString(2) + "\t ");
                System.out.print(rs.getDate(3) + "\t");
                System.out.println(rs.getDate(4) + "\n");
            }
            System.out.println("-----------------------");

            // catch exceptions, print error to console
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
        }
        finally {
            // close stuff
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static void listAllClientsWhoHaveHadEvents(String url, String user, String password) {

        // ask for client ID from user
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter Event ID: ");
        String eventID = input.next();

        // create connection stuff
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {

            // load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // connect to database
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database");

            // prepare SQL statement with user-provided clientID
            stmt = conn.prepareStatement("SELECT DISTINCT CLIENT.clientID FROM CLIENT\n" +
                    "LEFT JOIN HAS_EVENT ON CLIENT.clientID=HAS_EVENT.clientID\n" +
                    "LEFT JOIN EVENT ON HAS_EVENT.eventID=EVENT.eventID");

            // execute, pretty-print
            rs = stmt.executeQuery();
            System.out.println("Client ID");
            System.out.println("-----------------------");
            while (rs.next()) {
                System.out.print(rs.getInt(1) + "\n");
            }
            System.out.println("-----------------------");

            // catch exceptions, print error to console
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
        }
        finally {
            // close stuff
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static void getPhoneNumbersForAllEmployeesWorkingAnEvent(String url, String user, String password) {

        // ask for client ID from user
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter Event ID: ");
        String eventID = input.next();

        // create connection stuff
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {

            // load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // connect to database
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database");

            // prepare SQL statement with user-provided clientID
            stmt = conn.prepareStatement("SELECT phone\n" +
                    "FROM EMPLOYEE, WORKS_AT\n" +
                    "WHERE EMPLOYEE.ESSN=WORKS_AT.ESSN AND eventID= ? ;");
            stmt.setString(1, eventID);

            // execute, pretty-print
            rs = stmt.executeQuery();
            System.out.println("Phone #");
            System.out.println("-----------------------");
            while (rs.next()) {
                System.out.print(rs.getString(1) + "\n");
            }
            System.out.println("-----------------------");

            // catch exceptions, print error to console
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
        }
        finally {
            // close stuff
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static void selectAllEquipmentByLastServiceDate(String url, String user, String password) {

        // ask for client ID from user
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter Date (format YYYY-MM-DD): ");
        String serviceDate = input.next();

        // create connection stuff
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {

            // load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // connect to database
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database");

            // prepare SQL statement with user-provided clientID
            stmt = conn.prepareStatement("SELECT equipmentID, make, model, equipmentType\n" +
                    "FROM EQUIPMENT\n" +
                    "WHERE lastServiceDate < ?");
            stmt.setString(1, serviceDate);

            // execute, pretty-print
            rs = stmt.executeQuery();
            System.out.println("Equipment ID\tMake\tModel\tEquipment Type");
            System.out.println("-----------------------");
            while (rs.next()) {
                System.out.print(rs.getString(1) + "\t");
                System.out.print(rs.getString(2) + "\t ");
                System.out.print(rs.getString(3) + "\t");
                System.out.println(rs.getString(4) + "\n");
            }
            System.out.println("-----------------------");

            // catch exceptions, print error to console
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
        }
        finally {
            // close stuff
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static void updateClientPhoneNumber(String url, String user, String password) {

        // ask for client ID from user
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter client ID: ");
        String clientID = input.next();
        System.out.println("Please enter new phone number: ");
        String newPhone = input.next();

        // create connection stuff
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {

            // load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // connect to database
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database");

            // prepare SQL statement with user-provided clientID
            stmt = conn.prepareStatement("UPDATE CLIENT\n" +
                    "SET phone = ?" +
                    "WHERE clientID = ?");
            stmt.setString(1, newPhone);
            stmt.setString(2, clientID);

            // execute, pretty-print
            rs = stmt.executeQuery();

            stmt = conn.prepareStatement("SELECT clientID, phone from CLIENT WHERE clientID = ?");
            stmt.setString(1, clientID);
            rs = stmt.executeQuery();
            System.out.println("Updated information:");
            System.out.println("Client ID\tPhone #:");
            while (rs.next()) {
                System.out.print(rs.getString(1) + "\t");
                System.out.println(rs.getString(2) + "\n");
            }

            // catch exceptions, print error to console
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
        }
        finally {
            // close stuff
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static void setEquipmentAsCheckedOut(String url, String user, String password) {

        // ask for client ID from user
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter equipment ID: ");
        String equipmentID = input.next();

        // create connection stuff
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {

            // load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // connect to database
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database");

            // prepare SQL statement with user-provided clientID
            stmt = conn.prepareStatement("UPDATE EQUIPMENT\n" +
                    "SET isCheckedOut = TRUE\n" +
                    "WHERE equipmentID = ?");
            stmt.setString(1, equipmentID);

            // execute, pretty-print
            rs = stmt.executeQuery();

            System.out.println("Equipment checked out!");
            stmt = conn.prepareStatement("SELECT equipmentID, isCheckedOut from EQUIPMENT WHERE equipmentID = ?");
            stmt.setString(1, equipmentID);
            rs = stmt.executeQuery();
            System.out.println("Updated information:");
            System.out.println("Equipment ID\tChecked Out");
            while (rs.next()) {
                System.out.print(rs.getString(1) + "\t");
                System.out.println(rs.getString(2) + "\n");
            }

            // catch exceptions, print error to console
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
        }
        finally {
            // close stuff
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("SQL ERROR: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }





}
