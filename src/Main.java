import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main{
    private static final String url ="jdbc:mysql://localhost:3306/hotel_db";
    private static final String username="root";
    private static final String password="Mysql@24";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e) {
            e.getStackTrace();
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            while(true) {
                System.out.println("\nHOTEL RESERVATION SYSTEM");

                Scanner scanner = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get room number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.print("Choose an Option: ");
                int choice = scanner.nextInt();

                switch(choice) {
                    case 1 -> reserveRoom(scanner, statement);
                    case 2 -> viewReservations(statement);
                    case 3 -> getRoomNo(scanner, statement);
                    case 4 -> updateReservation(scanner, statement);
                    case 5 -> deleteReservation(scanner, statement);
                    case 0 -> {
                        exit();
                        scanner.close();
                        statement.close();
                        connection.close();
                        return;}
                    default -> System.out.println("Invalid Choice! Choose again.");
                }
            }

        }catch(SQLException e) {
            e.getStackTrace();
        }
        catch(InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    static void reserveRoom(Scanner scanner, Statement statement) {
        try {
            System.out.print("Enter guest name: ");
            String guestName = scanner.next();
            System.out.print("Enter room number: ");
            int roomNumber=scanner.nextInt();
            System.out.println("Enter contact number: ");
            String contactNumber=scanner.next();

            String query = "INSERT INTO reservations(guest_name, room_number, contact_number) "
                    + "VALUES('"+guestName+"',"+roomNumber+",'"+contactNumber+"')";

            int affectedRows = statement.executeUpdate(query);

            if(affectedRows>0) {
                System.out.println("Reservation Successful!");
            }else {
                System.out.println("Reservation Failed!");
            }

        }catch(SQLException e) {
            e.getStackTrace();
        }
    };

    static void viewReservations(Statement statement) {
        try {
            String query= "SELECT * FROM reservations";
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("+----------------+----------------+-------------+----------------+-----------------------+");
            System.out.println("| Reservation ID |   Guest Name   | Room Number | Conatct Number |   Reservation Date    |");
            System.out.println("+----------------+----------------+-------------+----------------+-----------------------+");

            while(resultSet.next()) {
                int reservationID = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-14s | %-11d | %-14s | %-21s |\n",
                        reservationID,guestName,roomNumber,contactNumber,reservationDate);
            }
            System.out.println("+----------------+----------------+-------------+----------------+-----------------------+");
        }catch(SQLException e) {
            e.getStackTrace();
        }
    };

    static void getRoomNo(Scanner scanner, Statement statement) {
        try {
            System.out.print("Enter Reservation ID: ");
            int reservationID = scanner.nextInt();

            String query = "SELECT * FROM reservations WHERE reservation_id="+reservationID+"";

            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                System.out.println("Room no. of the reservation id "+reservationID+" is "+resultSet.getInt("room_number"));
            }else {
                System.out.println("Reservation not found for the given ID.");
            }
        }catch(SQLException e) {
            e.getStackTrace();
        }
    };

    static void updateReservation(Scanner scanner, Statement statement) {
        try {
            System.out.print("Enter Reservation ID to update: ");
            int reservationID = scanner.nextInt();

            if(!checkReservation(reservationID,statement)) {
                System.out.println("Reservation does not exists for given ID.");
                return;
            }

            System.out.print("Enter guest name: ");
            String guestName = scanner.next();
            System.out.print("Enter room number: ");
            int roomNumber=scanner.nextInt();
            System.out.print("Enter contact number: ");
            String contactNumber=scanner.next();

            String query = "UPDATE reservations SET guest_name='"+guestName+"', room_number="+roomNumber+", "
                    + "contact_number='"+contactNumber+"' WHERE reservation_id="+reservationID+"";

            int rowsAffected = statement.executeUpdate(query);
            if(rowsAffected>0) {
                System.out.println("Reservation Updated Successfully!");
            }else {
                System.out.println("Reservation Update Failed!");
            }
        }catch(SQLException e) {
            e.getStackTrace();
        }
    };

    static void deleteReservation(Scanner scanner, Statement statement) {
        try {
            System.out.print("Enter Reservation ID: ");
            int reservationID = scanner.nextInt();

            if(!checkReservation(reservationID,statement)) {
                System.out.println("Reservation does not exists for given ID.");
                return;
            }

            String query = "DELETE FROM reservations WHERE reservation_id="+reservationID+"";

            int rowsAffected = statement.executeUpdate(query);
            if(rowsAffected>0) {
                System.out.println("Reservation Deleted successfully!");
            }else {
                System.out.println("Reservation Delete Failed!");
            }
        }catch(SQLException e) {
            e.getStackTrace();
        }
    };

    static boolean checkReservation(int id, Statement statement) {
        boolean exists=false;
        String checkQuery = "SELECT * FROM reservations WHERE reservation_id="+id+"";

        try {
            ResultSet resultSet = statement.executeQuery(checkQuery);
            if(resultSet.next()) {
                exists=true;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return exists;
    };

    public static void exit() throws InterruptedException{
        System.out.println("Exiting System");
        int i =5;
        while(i!=0) {
            System.out.print(".");
            Thread.sleep(400);
            i--;
        }
        System.out.println("\nThank You for using Hotel Reservation System!");
    };
}
