// Room.java - Stores hostel room detai

public class Room {
    private int roomNumber;
    private String roomType;
    private int totalBeds;
    private int occupiedBeds;

    // Constructor
    public Room(int roomNumber, String roomType, int totalBeds) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.totalBeds = totalBeds;
        this.occupiedBeds = 0;
    }

    // Getter methods
    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getTotalBeds() {
        return totalBeds;
    }

    public int getOccupiedBeds() {
        return occupiedBeds;
    }

    // Check if room has available beds
    public boolean isAvailable() {
        return occupiedBeds < totalBeds;
    }

    // Get number of available beds
    public int getAvailableBeds() {
        return totalBeds - occupiedBeds;
    }

    // Add one student to the room
    public void addStudent() {
        occupiedBeds++;
    }

    // Remove one student from the room
    public void removeStudent() {
        if (occupiedBeds > 0) {
            occupiedBeds--;
        }
    }

    // Display room details
    public void display() {
        System.out.println("Room: " + roomNumber + " | Type: " + roomType
                + " | Total Beds: " + totalBeds + " | Occupied: " + occupiedBeds
                + " | Available: " + getAvailableBeds());
    }
}
