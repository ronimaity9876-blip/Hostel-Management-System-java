// HostelManagement.java - Main application with menu and all operations

import java.util.ArrayList;
import java.util.Scanner;

public class HostelManagement {

    static ArrayList<Student> students = new ArrayList<>();
    static ArrayList<Room> rooms = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        initializeRooms();

        int choice;
        do {
            showMenu();
            System.out.print("Enter your choice: ");
            choice = getIntInput();

            switch (choice) {
                case 1:
                    registerStudent();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    viewAllRooms();
                    break;
                case 5:
                    viewAvailableRooms();
                    break;
                case 6:
                    allocateRoom();
                    break;
                case 7:
                    vacateRoom();
                    break;
                case 8:
                    changeRoom();
                    break;
                case 9:
                    viewAllocations();
                    break;
                case 10:
                    viewHostelSummary();
                    break;
                case 11:
                    System.out.println("\nOpening GUI Dashboard Window...");
                    HostelDashboard.launch();
                    break;
                case 0:
                    System.out.println("\nThank you for using Hostel Management System. Goodbye!");
                    break;
                default:
                    System.out.println("\nInvalid choice! Please try again.");
            }
        } while (choice != 0);
    }

    // Initialize sample hostel rooms
    static void initializeRooms() {
        rooms.add(new Room(101, "Single", 1));
        rooms.add(new Room(102, "Single", 1));
        rooms.add(new Room(103, "Double", 2));
        rooms.add(new Room(104, "Double", 2));
        rooms.add(new Room(105, "Triple", 3));
        rooms.add(new Room(201, "Single", 1));
        rooms.add(new Room(202, "Double", 2));
        rooms.add(new Room(203, "Double", 2));
        rooms.add(new Room(204, "Triple", 3));
        rooms.add(new Room(205, "Triple", 3));
    }

    // Display the main menu
    static void showMenu() {
        System.out.println("\n====================================");
        System.out.println(" HOSTEL MANAGEMENT SYSTEM");
        System.out.println("====================================");
        System.out.println("1.  Register Student");
        System.out.println("2.  View All Students");
        System.out.println("3.  Search Student");
        System.out.println("4.  View All Rooms");
        System.out.println("5.  View Available Rooms");
        System.out.println("6.  Allocate Room");
        System.out.println("7.  Vacate Room");
        System.out.println("8.  Change Student Room");
        System.out.println("9.  View Student Allocations");
        System.out.println("10. View Hostel Summary");
        System.out.println("11. Open GUI Dashboard Window");
        System.out.println("0.  Exit");
        System.out.println("====================================");
    }

    // --- 1. Register Student ---
    static void registerStudent() {
        System.out.println("\n--- Register New Student ---");

        System.out.print("Enter Student ID: ");
        String id = sc.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println("Student ID cannot be empty!");
            return;
        }

        // Check for duplicate ID
        if (findStudent(id) != null) {
            System.out.println("Error: Student with ID " + id + " already exists!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter Course: ");
        String course = sc.nextLine().trim();

        System.out.print("Enter Semester (1-8): ");
        int semester = getIntInput();
        if (semester < 1 || semester > 8) {
            System.out.println("Invalid semester! Must be between 1 and 8.");
            return;
        }

        System.out.print("Enter Phone Number: ");
        String phone = sc.nextLine().trim();

        System.out.print("Enter Gender (M/F): ");
        String gender = sc.nextLine().trim().toUpperCase();
        if (!gender.equals("M") && !gender.equals("F")) {
            System.out.println("Invalid gender! Enter M or F.");
            return;
        }

        Student student = new Student(id, name, course, semester, phone, gender);
        students.add(student);
        System.out.println("Student registered successfully!");
    }

    // --- 2. View All Students ---
    static void viewAllStudents() {
        System.out.println("\n--- All Registered Students ---");
        if (students.isEmpty()) {
            System.out.println("No students registered yet.");
            return;
        }
        for (int i = 0; i < students.size(); i++) {
            students.get(i).display();
        }
        System.out.println("Total Students: " + students.size());
    }

    // --- 3. Search Student ---
    static void searchStudent() {
        System.out.println("\n--- Search Student ---");
        System.out.print("Enter Student ID to search: ");
        String id = sc.nextLine().trim();

        Student student = findStudent(id);
        if (student != null) {
            System.out.println("Student Found:");
            student.display();
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }

    // --- 4. View All Rooms ---
    static void viewAllRooms() {
        System.out.println("\n--- All Hostel Rooms ---");
        for (int i = 0; i < rooms.size(); i++) {
            rooms.get(i).display();
        }
    }

    // --- 5. View Available Rooms ---
    static void viewAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        boolean found = false;
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).isAvailable()) {
                rooms.get(i).display();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No rooms available at the moment.");
        }
    }

    // --- 6. Allocate Room ---
    static void allocateRoom() {
        System.out.println("\n--- Allocate Room ---");

        System.out.print("Enter Student ID: ");
        String id = sc.nextLine().trim();

        Student student = findStudent(id);
        if (student == null) {
            System.out.println("Student with ID " + id + " not found. Please register first.");
            return;
        }

        // Check if student already has a room
        if (student.getAllocatedRoom() != 0) {
            System.out.println("Student already has Room " + student.getAllocatedRoom() + ".");
            System.out.println("Use 'Change Room' option to move to a different room.");
            return;
        }

        // Show available rooms
        System.out.println("Available Rooms:");
        boolean hasAvailable = false;
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).isAvailable()) {
                rooms.get(i).display();
                hasAvailable = true;
            }
        }

        if (!hasAvailable) {
            System.out.println("Sorry, no rooms are available right now.");
            return;
        }

        System.out.print("Enter Room Number to allocate: ");
        int roomNum = getIntInput();

        Room room = findRoom(roomNum);
        if (room == null) {
            System.out.println("Room " + roomNum + " does not exist.");
            return;
        }

        if (!room.isAvailable()) {
            System.out.println("Room " + roomNum + " is full. Choose another room.");
            return;
        }

        // Allocate the room
        student.setAllocatedRoom(roomNum);
        room.addStudent();
        System.out.println("Room " + roomNum + " allocated to " + student.getName() + " successfully!");
    }

    // --- 7. Vacate Room ---
    static void vacateRoom() {
        System.out.println("\n--- Vacate Room ---");

        System.out.print("Enter Student ID: ");
        String id = sc.nextLine().trim();

        Student student = findStudent(id);
        if (student == null) {
            System.out.println("Student with ID " + id + " not found.");
            return;
        }

        if (student.getAllocatedRoom() == 0) {
            System.out.println("Student does not have any room allocated.");
            return;
        }

        int oldRoomNum = student.getAllocatedRoom();
        Room oldRoom = findRoom(oldRoomNum);

        // Vacate the room
        student.setAllocatedRoom(0);
        if (oldRoom != null) {
            oldRoom.removeStudent();
        }
        System.out.println("Room " + oldRoomNum + " vacated by " + student.getName() + " successfully!");
    }

    // --- 8. Change Room ---
    static void changeRoom() {
        System.out.println("\n--- Change Student Room ---");

        System.out.print("Enter Student ID: ");
        String id = sc.nextLine().trim();

        Student student = findStudent(id);
        if (student == null) {
            System.out.println("Student with ID " + id + " not found.");
            return;
        }

        if (student.getAllocatedRoom() == 0) {
            System.out.println("Student does not have any room. Use 'Allocate Room' first.");
            return;
        }

        int oldRoomNum = student.getAllocatedRoom();
        System.out.println("Current Room: " + oldRoomNum);

        // Show available rooms
        System.out.println("Available Rooms:");
        boolean hasAvailable = false;
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).isAvailable()) {
                rooms.get(i).display();
                hasAvailable = true;
            }
        }

        if (!hasAvailable) {
            System.out.println("No other rooms available. Keeping current room.");
            return;
        }

        System.out.print("Enter New Room Number: ");
        int newRoomNum = getIntInput();

        if (newRoomNum == oldRoomNum) {
            System.out.println("Student is already in Room " + oldRoomNum + ".");
            return;
        }

        Room newRoom = findRoom(newRoomNum);
        if (newRoom == null) {
            System.out.println("Room " + newRoomNum + " does not exist. Keeping current room.");
            return;
        }

        if (!newRoom.isAvailable()) {
            System.out.println("Room " + newRoomNum + " is full. Keeping current room.");
            return;
        }

        // Release old room and assign new room
        Room oldRoom = findRoom(oldRoomNum);
        if (oldRoom != null) {
            oldRoom.removeStudent();
        }
        student.setAllocatedRoom(newRoomNum);
        newRoom.addStudent();
        System.out.println("Room changed from " + oldRoomNum + " to " + newRoomNum + " for " + student.getName() + "!");
    }

    // --- 9. View Student Allocations ---
    static void viewAllocations() {
        System.out.println("\n--- Student Room Allocations ---");

        if (students.isEmpty()) {
            System.out.println("No students registered yet.");
            return;
        }

        System.out.println("\nStudents WITH rooms:");
        boolean hasAllocated = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getAllocatedRoom() != 0) {
                students.get(i).display();
                hasAllocated = true;
            }
        }
        if (!hasAllocated) {
            System.out.println("  No students have been allocated rooms yet.");
        }

        System.out.println("\nStudents WITHOUT rooms:");
        boolean hasUnallocated = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getAllocatedRoom() == 0) {
                students.get(i).display();
                hasUnallocated = true;
            }
        }
        if (!hasUnallocated) {
            System.out.println("  All students have rooms allocated.");
        }
    }

    // --- 10. View Hostel Summary ---
    static void viewHostelSummary() {
        System.out.println("\n--- Hostel Summary ---");

        int totalStudents = students.size();
        int allocatedCount = 0;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getAllocatedRoom() != 0) {
                allocatedCount++;
            }
        }
        int unallocatedCount = totalStudents - allocatedCount;

        int totalBeds = 0;
        int occupiedBeds = 0;
        for (int i = 0; i < rooms.size(); i++) {
            totalBeds += rooms.get(i).getTotalBeds();
            occupiedBeds += rooms.get(i).getOccupiedBeds();
        }
        int availableBeds = totalBeds - occupiedBeds;

        System.out.println("Total Students Registered : " + totalStudents);
        System.out.println("Students with Rooms       : " + allocatedCount);
        System.out.println("Students without Rooms    : " + unallocatedCount);
        System.out.println("Total Rooms               : " + rooms.size());
        System.out.println("Total Beds                : " + totalBeds);
        System.out.println("Occupied Beds             : " + occupiedBeds);
        System.out.println("Available Beds            : " + availableBeds);
    }

    // --- Helper: Find student by ID ---
    static Student findStudent(String id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equalsIgnoreCase(id)) {
                return students.get(i);
            }
        }
        return null;
    }

    // --- Helper: Find room by number ---
    static Room findRoom(int roomNumber) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber() == roomNumber) {
                return rooms.get(i);
            }
        }
        return null;
    }

    // --- Helper: Read integer input safely ---
    static int getIntInput() {
        while (true) {
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
