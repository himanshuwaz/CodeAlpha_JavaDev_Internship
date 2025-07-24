import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID; // For generating unique reservation IDs

/**
 * Enum for Room Categories to ensure consistent categorization.
 */
enum RoomCategory {
    STANDARD,
    DELUXE,
    SUITE;

    /**
     * Converts a string to a RoomCategory enum, case-insensitive.
     * @param categoryString The string representation of the category.
     * @return The corresponding RoomCategory enum, or null if not found.
     */
    public static RoomCategory fromString(String categoryString) {
        for (RoomCategory category : RoomCategory.values()) {
            if (category.name().equalsIgnoreCase(categoryString)) {
                return category;
            }
        }
        return null; // Or throw an IllegalArgumentException
    }
}

/**
 * Represents a single hotel room.
 */
class Room {
    private String roomNumber;
    private RoomCategory category;
    private double pricePerNight;
    private boolean isAvailable; // Basic availability for initial state, actual availability depends on reservations

    /**
     * Constructor for Room.
     * @param roomNumber Unique identifier for the room.
     * @param category The type of room (e.g., STANDARD, DELUXE).
     * @param pricePerNight The cost per night for the room.
     * @param isAvailable Initial availability status.
     */
    public Room(String roomNumber, RoomCategory category, double pricePerNight, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.isAvailable = isAvailable;
    }

    // Getters
    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Setter for availability (used when booking/canceling)
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Returns a string representation of the Room object for display.
     */
    @Override
    public String toString() {
        return String.format("Room No: %-5s | Category: %-8s | Price: $%-7.2f/night | Available: %s",
                roomNumber, category.name(), pricePerNight, isAvailable ? "Yes" : "No");
    }

    /**
     * Converts Room object to a CSV string for file storage.
     * @return CSV formatted string.
     */
    public String toCsvString() {
        return String.format("%s,%s,%.2f,%b", roomNumber, category.name(), pricePerNight, isAvailable);
    }

    /**
     * Creates a Room object from a CSV string.
     * @param csvString The CSV string representing a room.
     * @return A Room object.
     * @throws IllegalArgumentException If the CSV string format is incorrect.
     */
    public static Room fromCsvString(String csvString) {
        String[] parts = csvString.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid Room CSV string format: " + csvString);
        }
        String roomNumber = parts[0];
        RoomCategory category = RoomCategory.fromString(parts[1]);
        double pricePerNight = Double.parseDouble(parts[2]);
        boolean isAvailable = Boolean.parseBoolean(parts[3]);

        if (category == null) {
            throw new IllegalArgumentException("Invalid Room Category in CSV: " + parts[1]);
        }
        return new Room(roomNumber, category, pricePerNight, isAvailable);
    }
}

/**
 * Represents a reservation made by a guest for a specific room and dates.
 */
class Reservation {
    private String reservationId;
    private String roomNumber;
    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;
    private boolean isConfirmed;

    // Date formatter for consistent parsing and formatting
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructor for Reservation.
     * @param roomNumber The number of the reserved room.
     * @param guestName The name of the guest.
     * @param checkInDate The check-in date.
     * @param checkOutDate The check-out date.
     * @param totalPrice The total price for the reservation.
     */
    public Reservation(String roomNumber, String guestName, LocalDate checkInDate, LocalDate checkOutDate, double totalPrice) {
        this.reservationId = UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // Generate a short unique ID
        this.roomNumber = roomNumber;
        this.guestName = guestName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.isConfirmed = false; // Reservations are unconfirmed until payment simulation
    }

    // Getters
    public String getReservationId() {
        return reservationId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getGuestName() {
        return guestName;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    // Setter for confirmation status
    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    /**
     * Returns a string representation of the Reservation object for display.
     */
    @Override
    public String toString() {
        return String.format("Reservation ID: %s%n" +
                        "  Room Number: %s%n" +
                        "  Guest Name: %s%n" +
                        "  Check-in Date: %s%n" +
                        "  Check-out Date: %s%n" +
                        "  Total Price: $%.2f%n" +
                        "  Status: %s",
                reservationId, roomNumber, guestName,
                checkInDate.format(DATE_FORMATTER), checkOutDate.format(DATE_FORMATTER),
                totalPrice, isConfirmed ? "Confirmed" : "Pending Payment");
    }

    /**
     * Converts Reservation object to a CSV string for file storage.
     * @return CSV formatted string.
     */
    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%s,%.2f,%b",
                reservationId, roomNumber, guestName,
                checkInDate.format(DATE_FORMATTER), checkOutDate.format(DATE_FORMATTER),
                totalPrice, isConfirmed);
    }

    /**
     * Creates a Reservation object from a CSV string.
     * @param csvString The CSV string representing a reservation.
     * @return A Reservation object.
     * @throws IllegalArgumentException If the CSV string format is incorrect or date parsing fails.
     */
    public static Reservation fromCsvString(String csvString) {
        String[] parts = csvString.split(",");
        if (parts.length != 7) {
            throw new IllegalArgumentException("Invalid Reservation CSV string format: " + csvString);
        }
        String reservationId = parts[0];
        String roomNumber = parts[1];
        String guestName = parts[2];
        LocalDate checkInDate = LocalDate.parse(parts[3], DATE_FORMATTER);
        LocalDate checkOutDate = LocalDate.parse(parts[4], DATE_FORMATTER);
        double totalPrice = Double.parseDouble(parts[5]);
        boolean isConfirmed = Boolean.parseBoolean(parts[6]);

        Reservation reservation = new Reservation(roomNumber, guestName, checkInDate, checkOutDate, totalPrice);
        reservation.reservationId = reservationId; // Set the original ID
        reservation.setConfirmed(isConfirmed);
        return reservation;
    }
}

/**
 * Manages the hotel's rooms and reservations, handling all core business logic.
 */
class Hotel {
    private List<Room> rooms;
    private List<Reservation> reservations;
    private static final String ROOMS_FILE = "rooms.txt";
    private static final String RESERVATIONS_FILE = "reservations.txt";

    /**
     * Constructor for Hotel. Initializes rooms and loads existing data.
     */
    public Hotel() {
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
        loadData(); // Load data when Hotel object is created
        // If no rooms loaded, initialize some default rooms
        if (rooms.isEmpty()) {
            initializeDefaultRooms();
            saveRooms(); // Save default rooms after initialization
        }
    }

    /**
     * Initializes some default rooms if the rooms file is empty or not found.
     */
    private void initializeDefaultRooms() {
        System.out.println("Initializing default rooms...");
        rooms.add(new Room("101", RoomCategory.STANDARD, 100.00, true));
        rooms.add(new Room("102", RoomCategory.STANDARD, 100.00, true));
        rooms.add(new Room("201", RoomCategory.DELUXE, 150.00, true));
        rooms.add(new Room("202", RoomCategory.DELUXE, 150.00, true));
        rooms.add(new Room("301", RoomCategory.SUITE, 250.00, true));
        rooms.add(new Room("302", RoomCategory.SUITE, 250.00, true));
        System.out.println("Default rooms added.");
    }

    /**
     * Loads room and reservation data from respective files.
     */
    private void loadData() {
        loadRooms();
        loadReservations();
    }

    /**
     * Saves room and reservation data to respective files.
     */
    public void saveData() {
        saveRooms();
        saveReservations();
    }

    /**
     * Loads room data from the rooms.txt file.
     */
    private void loadRooms() {
        rooms.clear(); // Clear current list before loading
        try (BufferedReader reader = new BufferedReader(new FileReader(ROOMS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    rooms.add(Room.fromCsvString(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping malformed room data: " + line + " (" + e.getMessage() + ")");
                }
            }
            System.out.println("Rooms loaded from " + ROOMS_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("Rooms file not found. A new one will be created.");
        } catch (IOException e) {
            System.err.println("Error loading rooms: " + e.getMessage());
        }
    }

    /**
     * Saves current room data to the rooms.txt file.
     */
    private void saveRooms() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ROOMS_FILE))) {
            for (Room room : rooms) {
                writer.write(room.toCsvString());
                writer.newLine();
            }
            System.out.println("Rooms saved to " + ROOMS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving rooms: " + e.getMessage());
        }
    }

    /**
     * Loads reservation data from the reservations.txt file.
     */
    private void loadReservations() {
        reservations.clear(); // Clear current list before loading
        try (BufferedReader reader = new BufferedReader(new FileReader(RESERVATIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    reservations.add(Reservation.fromCsvString(line));
                } catch (IllegalArgumentException | DateTimeParseException e) {
                    System.err.println("Skipping malformed reservation data: " + line + " (" + e.getMessage() + ")");
                }
            }
            System.out.println("Reservations loaded from " + RESERVATIONS_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("Reservations file not found. A new one will be created.");
        } catch (IOException e) {
            System.err.println("Error loading reservations: " + e.getMessage());
        }
    }

    /**
     * Saves current reservation data to the reservations.txt file.
     */
    private void saveReservations() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESERVATIONS_FILE))) {
            for (Reservation res : reservations) {
                writer.write(res.toCsvString());
                writer.newLine();
            }
            System.out.println("Reservations saved to " + RESERVATIONS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving reservations: " + e.getMessage());
        }
    }

    /**
     * Displays all available rooms (based on initial `isAvailable` flag and current reservations).
     * @param category Optional category to filter by.
     * @param checkInDate Optional check-in date for availability check.
     * @param checkOutDate Optional check-out date for availability check.
     */
    public void searchAvailableRooms(RoomCategory category, LocalDate checkInDate, LocalDate checkOutDate) {
        System.out.println("\n--- Available Rooms ---");
        boolean found = false;
        for (Room room : rooms) {
            // Filter by category if specified
            if (category != null && !room.getCategory().equals(category)) {
                continue;
            }

            // Check availability for the given date range
            if (isRoomAvailableForDates(room.getRoomNumber(), checkInDate, checkOutDate)) {
                System.out.println(room);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No rooms found matching your criteria.");
        }
        System.out.println("-----------------------");
    }

    /**
     * Checks if a specific room is available for a given date range.
     * A room is available if it's not booked by any existing, confirmed reservation
     * that overlaps with the requested check-in/check-out dates.
     * @param roomNumber The room number to check.
     * @param desiredCheckIn The desired check-in date.
     * @param desiredCheckOut The desired check-out date.
     * @return true if the room is available for the entire period, false otherwise.
     */
    public boolean isRoomAvailableForDates(String roomNumber, LocalDate desiredCheckIn, LocalDate desiredCheckOut) {
        // Basic check: Room must exist and be generally available (though this flag is less critical with date-based checks)
        Room room = getRoomByNumber(roomNumber);
        if (room == null) {
            return false; // Room doesn't exist
        }

        // A room is available if no CONFIRMED reservation overlaps with the desired dates.
        for (Reservation res : reservations) {
            if (res.isConfirmed() && res.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                // Check for overlap:
                // (StartA <= EndB) AND (EndA >= StartB)
                // where A is existing reservation, B is desired reservation
                if (desiredCheckIn.isBefore(res.getCheckOutDate()) && desiredCheckOut.isAfter(res.getCheckInDate())) {
                    return false; // Overlap found, room is not available
                }
            }
        }
        return true; // No overlapping confirmed reservations found
    }


    /**
     * Makes a new reservation for a guest.
     * @param roomNumber The room number to book.
     * @param guestName The name of the guest.
     * @param checkInDate The check-in date.
     * @param checkOutDate The check-out date.
     * @return The created Reservation object if successful, null otherwise.
     */
    public Reservation makeReservation(String roomNumber, String guestName, LocalDate checkInDate, LocalDate checkOutDate) {
        Room room = getRoomByNumber(roomNumber);

        if (room == null) {
            System.out.println("Error: Room " + roomNumber + " not found.");
            return null;
        }

        if (!isRoomAvailableForDates(roomNumber, checkInDate, checkOutDate)) {
            System.out.println("Error: Room " + roomNumber + " is not available for the selected dates.");
            return null;
        }

        long numberOfNights = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (numberOfNights <= 0) {
            System.out.println("Error: Check-out date must be after check-in date.");
            return null;
        }

        double totalPrice = room.getPricePerNight() * numberOfNights;
        Reservation newReservation = new Reservation(roomNumber, guestName, checkInDate, checkOutDate, totalPrice);
        reservations.add(newReservation);
        System.out.println("Reservation for Room " + roomNumber + " created. Total price: $" + String.format("%.2f", totalPrice));
        System.out.println("Your Reservation ID (for payment/cancellation): " + newReservation.getReservationId());
        saveReservations(); // Save immediately after creating
        return newReservation;
    }

    /**
     * Cancels an existing reservation.
     * @param reservationId The ID of the reservation to cancel.
     * @return true if cancellation is successful, false otherwise.
     */
    public boolean cancelReservation(String reservationId) {
        Reservation reservationToRemove = null;
        for (Reservation res : reservations) {
            if (res.getReservationId().equalsIgnoreCase(reservationId)) {
                reservationToRemove = res;
                break;
            }
        }

        if (reservationToRemove != null) {
            reservations.remove(reservationToRemove);
            System.out.println("Reservation " + reservationId + " has been cancelled.");
            saveReservations(); // Save after cancellation
            return true;
        } else {
            System.out.println("Reservation with ID " + reservationId + " not found.");
            return false;
        }
    }

    /**
     * Retrieves a reservation by its ID.
     * @param reservationId The ID of the reservation.
     * @return The Reservation object if found, null otherwise.
     */
    public Reservation getReservationById(String reservationId) {
        for (Reservation res : reservations) {
            if (res.getReservationId().equalsIgnoreCase(reservationId)) {
                return res;
            }
        }
        return null;
    }

    /**
     * Simulates a payment for a reservation.
     * @param reservation The reservation to pay for.
     * @param amountPaid The amount the user attempts to pay.
     * @return true if payment is successful (amountPaid >= total price), false otherwise.
     */
    public boolean simulatePayment(Reservation reservation, double amountPaid) {
        if (reservation.isConfirmed()) {
            System.out.println("Reservation " + reservation.getReservationId() + " is already confirmed.");
            return true; // Already paid
        }

        if (amountPaid >= reservation.getTotalPrice()) {
            reservation.setConfirmed(true);
            System.out.printf("Payment successful for Reservation ID %s. Amount paid: $%.2f. Change: $%.2f%n",
                    reservation.getReservationId(), amountPaid, (amountPaid - reservation.getTotalPrice()));
            saveReservations(); // Save after confirmation
            return true;
        } else {
            System.out.printf("Payment failed for Reservation ID %s. Insufficient amount. Required: $%.2f, Paid: $%.2f%n",
                    reservation.getReservationId(), reservation.getTotalPrice(), amountPaid);
            return false;
        }
    }

    /**
     * Helper method to get a Room object by its room number.
     * @param roomNumber The room number to search for.
     * @return The Room object, or null if not found.
     */
    private Room getRoomByNumber(String roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                return room;
            }
        }
        return null;
    }

    /**
     * Displays all rooms, regardless of availability.
     */
    public void displayAllRooms() {
        System.out.println("\n--- All Rooms ---");
        if (rooms.isEmpty()) {
            System.out.println("No rooms available in the system.");
            return;
        }
        for (Room room : rooms) {
            System.out.println(room);
        }
        System.out.println("-----------------");
    }

    /**
     * Displays all existing reservations.
     */
    public void displayAllReservations() {
        System.out.println("\n--- All Reservations ---");
        if (reservations.isEmpty()) {
            System.out.println("No reservations made yet.");
            return;
        }
        for (Reservation res : reservations) {
            System.out.println(res);
            System.out.println("------------------------");
        }
        System.out.println("------------------------");
    }
}

/**
 * Main class to run the Hotel Reservation System application.
 * Handles user input and orchestrates operations.
 */
public class HotelReservationSystem {
    private Hotel hotel;
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructor for HotelReservationSystem.
     */
    public HotelReservationSystem() {
        hotel = new Hotel();
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the main menu options to the user.
     */
    public void displayMenu() {
        System.out.println("\n--- Hotel Reservation System Menu ---");
        System.out.println("1. Search Available Rooms");
        System.out.println("2. Book a Room");
        System.out.println("3. Cancel Reservation");
        System.out.println("4. View Booking Details");
        System.out.println("5. Simulate Payment");
        System.out.println("6. View All Rooms (Admin)");
        System.out.println("7. View All Reservations (Admin)");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Prompts the user for a date string and parses it into a LocalDate object.
     * Includes input validation.
     * @param prompt The message to display to the user.
     * @return A valid LocalDate object, or null if parsing fails.
     */
    private LocalDate getDateInput(String prompt) {
        while (true) {
            System.out.print(prompt + " (YYYY-MM-DD): ");
            String dateString = scanner.nextLine();
            try {
                return LocalDate.parse(dateString, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    /**
     * Runs the main application loop.
     */
    public void run() {
        int choice;
        do {
            displayMenu();
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        searchRoomsOption();
                        break;
                    case 2:
                        bookRoomOption();
                        break;
                    case 3:
                        cancelReservationOption();
                        break;
                    case 4:
                        viewBookingDetailsOption();
                        break;
                    case 5:
                        simulatePaymentOption();
                        break;
                    case 6:
                        hotel.displayAllRooms();
                        break;
                    case 7:
                        hotel.displayAllReservations();
                        break;
                    case 8:
                        System.out.println("Exiting Hotel Reservation System. Goodbye!");
                        hotel.saveData(); // Save all data before exiting
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
                choice = 0; // Set choice to 0 to re-display menu
            }
        } while (choice != 8);

        scanner.close();
    }

    /**
     * Handles the "Search Available Rooms" menu option.
     */
    private void searchRoomsOption() {
        System.out.print("Enter desired room category (Standard, Deluxe, Suite) or leave blank for all: ");
        String categoryInput = scanner.nextLine().trim();
        RoomCategory category = null;
        if (!categoryInput.isEmpty()) {
            category = RoomCategory.fromString(categoryInput);
            if (category == null) {
                System.out.println("Invalid category entered. Showing all rooms.");
            }
        }

        LocalDate checkIn = getDateInput("Enter desired check-in date");
        LocalDate checkOut = getDateInput("Enter desired check-out date");

        if (checkIn.isAfter(checkOut) || checkIn.isEqual(checkOut)) {
            System.out.println("Check-out date must be after check-in date.");
            return;
        }
        if (checkIn.isBefore(LocalDate.now())) {
            System.out.println("Check-in date cannot be in the past.");
            return;
        }

        hotel.searchAvailableRooms(category, checkIn, checkOut);
    }

    /**
     * Handles the "Book a Room" menu option.
     */
    private void bookRoomOption() {
        System.out.print("Enter room number to book: ");
        String roomNumber = scanner.nextLine();
        System.out.print("Enter guest name: ");
        String guestName = scanner.nextLine();
        LocalDate checkIn = getDateInput("Enter check-in date");
        LocalDate checkOut = getDateInput("Enter check-out date");

        if (checkIn.isAfter(checkOut) || checkIn.isEqual(checkOut)) {
            System.out.println("Booking failed: Check-out date must be after check-in date.");
            return;
        }
        if (checkIn.isBefore(LocalDate.now())) {
            System.out.println("Booking failed: Check-in date cannot be in the past.");
            return;
        }

        hotel.makeReservation(roomNumber, guestName, checkIn, checkOut);
    }

    /**
     * Handles the "Cancel Reservation" menu option.
     */
    private void cancelReservationOption() {
        System.out.print("Enter Reservation ID to cancel: ");
        String reservationId = scanner.nextLine();
        hotel.cancelReservation(reservationId);
    }

    /**
     * Handles the "View Booking Details" menu option.
     */
    private void viewBookingDetailsOption() {
        System.out.print("Enter Reservation ID to view details: ");
        String reservationId = scanner.nextLine();
        Reservation reservation = hotel.getReservationById(reservationId);
        if (reservation != null) {
            System.out.println("\n--- Booking Details ---");
            System.out.println(reservation);
            System.out.println("-----------------------");
        } else {
            System.out.println("Reservation with ID " + reservationId + " not found.");
        }
    }

    /**
     * Handles the "Simulate Payment" menu option.
     */
    private void simulatePaymentOption() {
        System.out.print("Enter Reservation ID for payment: ");
        String reservationId = scanner.nextLine();
        Reservation reservation = hotel.getReservationById(reservationId);

        if (reservation == null) {
            System.out.println("Reservation with ID " + reservationId + " not found.");
            return;
        }

        if (reservation.isConfirmed()) {
            System.out.println("Reservation " + reservationId + " is already confirmed. No payment needed.");
            return;
        }

        System.out.printf("Reservation total: $%.2f%n", reservation.getTotalPrice());
        System.out.print("Enter amount to pay: ");
        double amountPaid;
        try {
            amountPaid = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid amount.");
            scanner.nextLine(); // Consume invalid input
            return;
        }

        hotel.simulatePayment(reservation, amountPaid);
    }

    /**
     * Main method to start the Hotel Reservation System application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        HotelReservationSystem app = new HotelReservationSystem();
        app.run();
    }
}