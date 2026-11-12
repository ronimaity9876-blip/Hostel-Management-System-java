// Stores student details

public class Student {
    private String studentId;
    private String name;
    private String course;
    private int semester;
    private String phone;
    private String gender;
    private int allocatedRoom; // 0 means no room allocated

    // Constructor
    public Student(String studentId, String name, String course, int semester, String phone, String gender) {
        this.studentId = studentId;
        this.name = name;
        this.course = course;
        this.semester = semester;
        this.phone = phone;
        this.gender = gender;
        this.allocatedRoom = 0;
    }

    // Getter methods
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public int getSemester() {
        return semester;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public int getAllocatedRoom() {
        return allocatedRoom;
    }

    // Setter for room allocation
    public void setAllocatedRoom(int roomNumber) {
        this.allocatedRoom = roomNumber;
    }

    // Display student details
    public void display() {
        System.out.println("ID: " + studentId + " | Name: " + name + " | Course: " + course
                + " | Sem: " + semester + " | Phone: " + phone + " | Gender: " + gender
                + " | Room: " + (allocatedRoom == 0 ? "Not Allocated" : allocatedRoom));
    }
}
