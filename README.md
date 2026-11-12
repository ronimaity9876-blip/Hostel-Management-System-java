# Hostel Management System

The Hostel Management System is a desktop-based Java application developed to simplify and automate hostel operations in colleges or schools. Instead of tracking room allotments manually in paper registers or spreadsheets, this application allows hostel wardens to register students, check real-time bed availability, allocate or change rooms, and see an overview of hostel occupancy in one place.

---

## 🌟 Key Features

- **📊 Live Interactive Dashboard (`HostelDashboard.java`)**:
  - **KPI Metrics Cards**: Real-time counters for Total Rooms, Total Beds, Occupied Beds, Available Beds, Total Students, and Occupancy Rate.
  - **Live Room Grid**: Visual cards representing each room, color-coded by occupancy:
    - 🟢 **Green**: Completely Available
    - 🟡 **Yellow**: Partially Occupied (displays occupant names)
    - 🔴 **Red**: Fully Occupied
  - **Students Directory**: Table of all registered students with real-time live search by ID or Name.
  - **Rooms Directory**: Table view with status filtering (*All Rooms*, *Available Only*, *Full Only*).
  - **Allocations & Waiting List**: Side-by-side view of students with rooms vs students waiting for allocation.

- **⚙️ Core Management Operations**:
  - **Student Registration**: Clean pop-up form with input validation (checks duplicate IDs and empty fields).
  - **Room Allocation**: Select unallocated students and assign them to available rooms with one click.
  - **Room Transfer**: Change a student's assigned room to any room with vacancies.
  - **Vacating**: Free up beds when students check out, instantly returning beds to the available pool.
  - **Overbooking Prevention**: Rooms reject additional allocations once capacity is reached.

- **💻 Console / CLI Mode (`HostelManagement.java`)**:
  - Complete terminal-based interactive menu for lightweight environments without a GUI.

---

## 🛠️ Technologies Used

- **Language**: Java (JDK 8 / 11 / 17 / 21+)
- **GUI Framework**: Java Swing & AWT (100% pure standard Java, zero external dependencies)
- **Architecture**: Object-Oriented Programming (OOP)
  - **Encapsulation**: Private fields with accessor/mutator methods in model classes.
  - **Collections Framework**: Dynamic in-memory management using `ArrayList`.
  - **Event-Driven Architecture**: Action listeners and event dispatch thread handling.

---

## 📁 Project Structure

```text
├── HostelDashboard.java     # Modern Swing GUI Dashboard with live metrics & grid
├── HostelManagement.java    # Core business logic and CLI terminal menu
├── Room.java                # Room model tracking capacity, occupancy & status
├── Student.java             # Student model storing personal details & room number
├── run.bat                  # One-click Windows launch script
└── README.md                # Project documentation
```

---

## 🚀 How to Run

### Method 1: One-Click Run (Windows)
Double-click `run.bat` in the project folder. It compiles the source code into a clean `bin/` directory and launches the GUI dashboard.

### Method 2: Command Line (Terminal)
Navigate to the project directory:

```bash
# 1. Compile into bin directory
javac -d bin HostelManagement.java Room.java Student.java HostelDashboard.java

# 2. Launch GUI Dashboard
java -cp bin HostelDashboard

# Or run in Console mode
java -cp bin HostelManagement
```

---

## 📄 License
This project is open source and available under the [MIT License](LICENSE).

