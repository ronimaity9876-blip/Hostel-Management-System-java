// HostelDashboard.java - Modern Graphical Dashboard for Hostel Management System

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class HostelDashboard extends JFrame {

    // Metric Cards labels
    private JLabel lblTotalRooms;
    private JLabel lblTotalBeds;
    private JLabel lblOccupiedBeds;
    private JLabel lblAvailableBeds;
    private JLabel lblTotalStudents;
    private JLabel lblOccupancyRate;

    // Tables & Models
    private DefaultTableModel studentsTableModel;
    private JTable studentsTable;
    private DefaultTableModel roomsTableModel;
    private JTable roomsTable;
    private DefaultTableModel allocatedTableModel;
    private JTable allocatedTable;
    private DefaultTableModel unallocatedTableModel;
    private JTable unallocatedTable;

    // Visual Room Grid Panel
    private JPanel roomGridPanel;

    // Filter & Search fields
    private JTextField txtSearchStudent;
    private JComboBox<String> cmbRoomFilter;

    // Color Palette
    private final Color PRIMARY_COLOR = new Color(30, 58, 138);       // Deep Blue
    private final Color SECONDARY_COLOR = new Color(59, 130, 246);    // Accent Blue
    private final Color BG_LIGHT = new Color(248, 250, 252);          // Slate light
    private final Color SUCCESS_COLOR = new Color(16, 185, 129);      // Emerald Green
    private final Color DANGER_COLOR = new Color(239, 68, 68);        // Coral Red
    private final Color WARNING_COLOR = new Color(245, 158, 11);      // Amber Orange
    private final Color CARD_BG = Color.WHITE;

    public HostelDashboard() {
        setTitle("Hostel Management System - Dashboard");
        setSize(1100, 750);
        setMinimumSize(new Dimension(950, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Ensure data is initialized
        if (HostelManagement.rooms.isEmpty()) {
            HostelManagement.initializeRooms();
        }
        ensureSampleStudents();

        // Main Layout
        JPanel mainContainer = new JPanel(new BorderLayout(0, 10));
        mainContainer.setBackground(BG_LIGHT);
        mainContainer.setBorder(new EmptyBorder(12, 16, 16, 16));

        // 1. Top Header Banner
        mainContainer.add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Center Content: Metrics + Tabbed Pane
        JPanel centerPanel = new JPanel(new BorderLayout(0, 12));
        centerPanel.setOpaque(false);
        centerPanel.add(createMetricsPanel(), BorderLayout.NORTH);
        centerPanel.add(createTabbedPane(), BorderLayout.CENTER);

        mainContainer.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainContainer);

        // Initial Data Refresh
        refreshAllData();
    }

    // Helper to seed initial sample data if empty
    private void ensureSampleStudents() {
        if (HostelManagement.students.isEmpty()) {
            Student s1 = new Student("STU-101", "Alex Johnson", "Computer Science", 3, "9876543210", "M");
            Student s2 = new Student("STU-102", "Priya Sharma", "Information Tech", 2, "9876543211", "F");
            Student s3 = new Student("STU-103", "Liam Davis", "Mechanical Eng", 4, "9876543212", "M");
            Student s4 = new Student("STU-104", "Ananya Patel", "Civil Eng", 1, "9876543213", "F");
            Student s5 = new Student("STU-105", "Noah Wilson", "Electrical Eng", 5, "9876543214", "M");
            Student s6 = new Student("STU-106", "Rohan Gupta", "Computer Science", 2, "9876543215", "M");

            HostelManagement.students.add(s1);
            HostelManagement.students.add(s2);
            HostelManagement.students.add(s3);
            HostelManagement.students.add(s4);
            HostelManagement.students.add(s5);
            HostelManagement.students.add(s6);

            // Allocate rooms to some
            assignStudentToRoom(s1, 101);
            assignStudentToRoom(s2, 103);
            assignStudentToRoom(s3, 103);
            assignStudentToRoom(s4, 105);
            assignStudentToRoom(s5, 201);
        }
    }

    private void assignStudentToRoom(Student student, int roomNum) {
        Room r = HostelManagement.findRoom(roomNum);
        if (r != null && r.isAvailable()) {
            student.setAllocatedRoom(roomNum);
            r.addStudent();
        }
    }

    // --- Header Panel ---
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        // Title and Subtitle
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 2));
        titlePanel.setOpaque(false);

        JLabel lblTitle = new JLabel("HOSTEL MANAGEMENT DASHBOARD");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Live Room Allocations, Student Directory & Occupancy Metrics");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(224, 231, 255));

        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);

        // Header Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        JButton btnRegister = createStyledButton("Register Student", new PlusIcon(14, Color.WHITE), SUCCESS_COLOR, Color.WHITE);
        btnRegister.addActionListener(e -> showRegisterDialog());

        JButton btnAllocate = createStyledButton("Allocate Room", new KeyIcon(14, Color.WHITE), SECONDARY_COLOR, Color.WHITE);
        btnAllocate.addActionListener(e -> showAllocateDialog());

        JButton btnVacate = createStyledButton("Vacate Room", new VacateIcon(14, Color.WHITE), new Color(220, 38, 38), Color.WHITE);
        btnVacate.addActionListener(e -> showVacateDialog());

        JButton btnRefresh = createStyledButton("Refresh", new RefreshIcon(14, Color.WHITE), new Color(71, 85, 105), Color.WHITE);
        btnRefresh.addActionListener(e -> refreshAllData());

        actionPanel.add(btnRegister);
        actionPanel.add(btnAllocate);
        actionPanel.add(btnVacate);
        actionPanel.add(btnRefresh);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(actionPanel, BorderLayout.EAST);

        return header;
    }

    // --- Metrics / KPI Cards Panel ---
    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 6, 12, 0));
        panel.setOpaque(false);

        lblTotalRooms = new JLabel("0", SwingConstants.CENTER);
        lblTotalBeds = new JLabel("0", SwingConstants.CENTER);
        lblOccupiedBeds = new JLabel("0", SwingConstants.CENTER);
        lblAvailableBeds = new JLabel("0", SwingConstants.CENTER);
        lblTotalStudents = new JLabel("0", SwingConstants.CENTER);
        lblOccupancyRate = new JLabel("0%", SwingConstants.CENTER);

        panel.add(createCard("Total Rooms", lblTotalRooms, new Color(59, 130, 246)));
        panel.add(createCard("Total Beds", lblTotalBeds, new Color(99, 102, 241)));
        panel.add(createCard("Occupied Beds", lblOccupiedBeds, WARNING_COLOR));
        panel.add(createCard("Available Beds", lblAvailableBeds, SUCCESS_COLOR));
        panel.add(createCard("Total Students", lblTotalStudents, new Color(139, 92, 246)));
        panel.add(createCard("Occupancy Rate", lblOccupancyRate, PRIMARY_COLOR));

        return panel;
    }

    private JPanel createCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitle = new JLabel(title.toUpperCase(), SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTitle.setForeground(new Color(100, 116, 139));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(accentColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // --- Tabbed Content Area ---
    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("  Live Room Grid  ", createRoomGridTab());
        tabbedPane.addTab("  Students Directory  ", createStudentsTab());
        tabbedPane.addTab("  Rooms Directory  ", createRoomsTab());
        tabbedPane.addTab("  Allocations & Summary  ", createAllocationsTab());

        return tabbedPane;
    }

    // --- Tab 1: Live Visual Room Grid ---
    private JPanel createRoomGridTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG_LIGHT);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Legend bar
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 4));
        legend.setBackground(Color.WHITE);
        legend.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));

        legend.add(createLegendItem("Fully Available", new Color(209, 250, 229), SUCCESS_COLOR));
        legend.add(createLegendItem("Partially Occupied", new Color(254, 243, 199), WARNING_COLOR));
        legend.add(createLegendItem("Full / No Beds", new Color(254, 226, 226), DANGER_COLOR));

        panel.add(legend, BorderLayout.NORTH);

        // Room Grid Container with Scroll Pane
        roomGridPanel = new JPanel(new GridLayout(0, 5, 12, 12));
        roomGridPanel.setBackground(BG_LIGHT);

        JScrollPane scrollPane = new JScrollPane(roomGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createLegendItem(String text, Color bg, Color border) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        item.setOpaque(false);

        JPanel box = new JPanel();
        box.setPreferredSize(new Dimension(16, 16));
        box.setBackground(bg);
        box.setBorder(new LineBorder(border, 1));

        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        item.add(box);
        item.add(lbl);
        return item;
    }

    // --- Tab 2: Students Directory Tab ---
    private JPanel createStudentsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG_LIGHT);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Search & Filter Toolbar
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(new EmptyBorder(8, 12, 8, 12));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Search (ID or Name):"));

        txtSearchStudent = new JTextField(18);
        txtSearchStudent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSearchStudent.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterStudentsTable(txtSearchStudent.getText().trim());
            }
        });
        searchPanel.add(txtSearchStudent);

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> {
            txtSearchStudent.setText("");
            filterStudentsTable("");
        });
        searchPanel.add(btnClear);

        // Right side student actions
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);

        JButton btnChangeRoom = createStyledButton("Change Room", new ChangeIcon(14, Color.WHITE), SECONDARY_COLOR, Color.WHITE);
        btnChangeRoom.addActionListener(e -> showChangeRoomDialog());

        JButton btnRemove = createStyledButton("Remove Student", new TrashIcon(14, Color.WHITE), DANGER_COLOR, Color.WHITE);
        btnRemove.addActionListener(e -> removeSelectedStudent());

        btnPanel.add(btnChangeRoom);
        btnPanel.add(btnRemove);

        toolbar.add(searchPanel, BorderLayout.WEST);
        toolbar.add(btnPanel, BorderLayout.EAST);

        panel.add(toolbar, BorderLayout.NORTH);

        // Students Table
        String[] columns = {"Student ID", "Name", "Course", "Semester", "Phone", "Gender", "Allocated Room"};
        studentsTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        studentsTable = new JTable(studentsTableModel);
        styleTable(studentsTable);

        JScrollPane scrollPane = new JScrollPane(studentsTable);
        scrollPane.setBorder(new LineBorder(new Color(226, 232, 240), 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // --- Tab 3: Rooms Directory Tab ---
    private JPanel createRoomsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG_LIGHT);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Filter Bar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        toolbar.setBackground(Color.WHITE);
        toolbar.add(new JLabel("Filter by Status:"));

        cmbRoomFilter = new JComboBox<>(new String[]{"All Rooms", "Available Rooms Only", "Full Rooms Only"});
        cmbRoomFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbRoomFilter.addActionListener(e -> refreshRoomsTable());
        toolbar.add(cmbRoomFilter);

        panel.add(toolbar, BorderLayout.NORTH);

        // Rooms Table
        String[] columns = {"Room #", "Room Type", "Total Beds", "Occupied Beds", "Available Beds", "Status", "Occupants"};
        roomsTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        roomsTable = new JTable(roomsTableModel);
        styleTable(roomsTable);

        JScrollPane scrollPane = new JScrollPane(roomsTable);
        scrollPane.setBorder(new LineBorder(new Color(226, 232, 240), 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // --- Tab 4: Allocations & Summary Tab ---
    private JPanel createAllocationsTab() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 0));
        panel.setBackground(BG_LIGHT);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Left Panel: Students WITH Rooms
        JPanel leftPanel = new JPanel(new BorderLayout(0, 6));
        leftPanel.setOpaque(false);

        JLabel lblLeft = new JLabel("Students WITH Allocated Rooms", SwingConstants.CENTER);
        lblLeft.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLeft.setForeground(SUCCESS_COLOR);
        leftPanel.add(lblLeft, BorderLayout.NORTH);

        String[] colsAllocated = {"Student ID", "Name", "Room #", "Phone"};
        allocatedTableModel = new DefaultTableModel(colsAllocated, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        allocatedTable = new JTable(allocatedTableModel);
        styleTable(allocatedTable);
        leftPanel.add(new JScrollPane(allocatedTable), BorderLayout.CENTER);

        // Right Panel: Students WITHOUT Rooms
        JPanel rightPanel = new JPanel(new BorderLayout(0, 6));
        rightPanel.setOpaque(false);

        JLabel lblRight = new JLabel("Students WITHOUT Rooms (Waiting List)", SwingConstants.CENTER);
        lblRight.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRight.setForeground(DANGER_COLOR);
        rightPanel.add(lblRight, BorderLayout.NORTH);

        String[] colsUnallocated = {"Student ID", "Name", "Course", "Phone"};
        unallocatedTableModel = new DefaultTableModel(colsUnallocated, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        unallocatedTable = new JTable(unallocatedTableModel);
        styleTable(unallocatedTable);
        rightPanel.add(new JScrollPane(unallocatedTable), BorderLayout.CENTER);

        panel.add(leftPanel);
        panel.add(rightPanel);

        return panel;
    }

    // --- Data Refresh Methods ---
    public void refreshAllData() {
        refreshMetrics();
        refreshRoomGrid();
        refreshStudentsTable();
        refreshRoomsTable();
        refreshAllocationsTable();
    }

    private void refreshMetrics() {
        int totalRooms = HostelManagement.rooms.size();
        int totalBeds = 0;
        int occupiedBeds = 0;

        for (Room r : HostelManagement.rooms) {
            totalBeds += r.getTotalBeds();
            occupiedBeds += r.getOccupiedBeds();
        }

        int availableBeds = totalBeds - occupiedBeds;
        int totalStudents = HostelManagement.students.size();
        int rate = totalBeds > 0 ? (occupiedBeds * 100 / totalBeds) : 0;

        lblTotalRooms.setText(String.valueOf(totalRooms));
        lblTotalBeds.setText(String.valueOf(totalBeds));
        lblOccupiedBeds.setText(String.valueOf(occupiedBeds));
        lblAvailableBeds.setText(String.valueOf(availableBeds));
        lblTotalStudents.setText(String.valueOf(totalStudents));
        lblOccupancyRate.setText(rate + "%");
    }

    private void refreshRoomGrid() {
        roomGridPanel.removeAll();

        for (Room r : HostelManagement.rooms) {
            JPanel card = new JPanel(new BorderLayout(0, 6));
            card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(203, 213, 225), 1, true),
                    new EmptyBorder(10, 10, 10, 10)
            ));

            Color bg;
            Color border;
            String status;

            if (r.getOccupiedBeds() == 0) {
                bg = new Color(240, 253, 244);     // Light green
                border = SUCCESS_COLOR;
                status = "Fully Available";
            } else if (r.isAvailable()) {
                bg = new Color(254, 252, 232);     // Light yellow
                border = WARNING_COLOR;
                status = "Partially Occupied";
            } else {
                bg = new Color(254, 242, 242);     // Light red
                border = DANGER_COLOR;
                status = "Full";
            }

            card.setBackground(bg);
            card.setBorder(new LineBorder(border, 1, true));

            // Top: Room number + Type
            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);

            JLabel lblNum = new JLabel("Room " + r.getRoomNumber());
            lblNum.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblNum.setForeground(PRIMARY_COLOR);

            JLabel lblType = new JLabel(r.getRoomType());
            lblType.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblType.setForeground(new Color(100, 116, 139));

            top.add(lblNum, BorderLayout.WEST);
            top.add(lblType, BorderLayout.EAST);
            card.add(top, BorderLayout.NORTH);

            // Center: Occupancy details & visual bed indicator
            JPanel center = new JPanel(new GridLayout(2, 1, 0, 4));
            center.setOpaque(false);

            JLabel lblBeds = new JLabel("Beds: " + r.getOccupiedBeds() + " / " + r.getTotalBeds());
            lblBeds.setFont(new Font("Segoe UI", Font.BOLD, 12));

            // Occupants list
            StringBuilder occupants = new StringBuilder("Occupants: ");
            boolean hasOccupant = false;
            for (Student s : HostelManagement.students) {
                if (s.getAllocatedRoom() == r.getRoomNumber()) {
                    if (hasOccupant) occupants.append(", ");
                    occupants.append(s.getName());
                    hasOccupant = true;
                }
            }
            if (!hasOccupant) occupants.append("None");

            JLabel lblOcc = new JLabel(occupants.toString());
            lblOcc.setFont(new Font("Segoe UI", Font.ITALIC, 10));
            lblOcc.setForeground(new Color(71, 85, 105));

            center.add(lblBeds);
            center.add(lblOcc);
            card.add(center, BorderLayout.CENTER);

            // Bottom: Status badge
            JLabel lblStatus = new JLabel(status, SwingConstants.CENTER);
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblStatus.setForeground(border);
            card.add(lblStatus, BorderLayout.SOUTH);

            roomGridPanel.add(card);
        }

        roomGridPanel.revalidate();
        roomGridPanel.repaint();
    }

    private void refreshStudentsTable() {
        studentsTableModel.setRowCount(0);
        for (Student s : HostelManagement.students) {
            String roomStr = (s.getAllocatedRoom() == 0) ? "Not Allocated" : String.valueOf(s.getAllocatedRoom());
            studentsTableModel.addRow(new Object[]{
                    s.getStudentId(),
                    s.getName(),
                    s.getCourse(),
                    s.getSemester(),
                    s.getPhone(),
                    s.getGender().equalsIgnoreCase("M") ? "Male" : "Female",
                    roomStr
            });
        }
    }

    private void filterStudentsTable(String query) {
        if (query.isEmpty()) {
            refreshStudentsTable();
            return;
        }
        studentsTableModel.setRowCount(0);
        String q = query.toLowerCase();
        for (Student s : HostelManagement.students) {
            if (s.getStudentId().toLowerCase().contains(q) || s.getName().toLowerCase().contains(q)) {
                String roomStr = (s.getAllocatedRoom() == 0) ? "Not Allocated" : String.valueOf(s.getAllocatedRoom());
                studentsTableModel.addRow(new Object[]{
                        s.getStudentId(),
                        s.getName(),
                        s.getCourse(),
                        s.getSemester(),
                        s.getPhone(),
                        s.getGender().equalsIgnoreCase("M") ? "Male" : "Female",
                        roomStr
                });
            }
        }
    }

    private void refreshRoomsTable() {
        roomsTableModel.setRowCount(0);
        String filter = cmbRoomFilter != null ? (String) cmbRoomFilter.getSelectedItem() : "All Rooms";

        for (Room r : HostelManagement.rooms) {
            boolean isAvail = r.isAvailable();
            if ("Available Rooms Only".equals(filter) && !isAvail) continue;
            if ("Full Rooms Only".equals(filter) && isAvail) continue;

            StringBuilder occ = new StringBuilder();
            for (Student s : HostelManagement.students) {
                if (s.getAllocatedRoom() == r.getRoomNumber()) {
                    if (occ.length() > 0) occ.append(", ");
                    occ.append(s.getName());
                }
            }

            roomsTableModel.addRow(new Object[]{
                    r.getRoomNumber(),
                    r.getRoomType(),
                    r.getTotalBeds(),
                    r.getOccupiedBeds(),
                    r.getAvailableBeds(),
                    isAvail ? "Available" : "Full",
                    occ.length() == 0 ? "-" : occ.toString()
            });
        }
    }

    private void refreshAllocationsTable() {
        allocatedTableModel.setRowCount(0);
        unallocatedTableModel.setRowCount(0);

        for (Student s : HostelManagement.students) {
            if (s.getAllocatedRoom() != 0) {
                allocatedTableModel.addRow(new Object[]{
                        s.getStudentId(),
                        s.getName(),
                        s.getAllocatedRoom(),
                        s.getPhone()
                });
            } else {
                unallocatedTableModel.addRow(new Object[]{
                        s.getStudentId(),
                        s.getName(),
                        s.getCourse(),
                        s.getPhone()
                });
            }
        }
    }

    // --- Action Dialogs ---

    // 1. Register Student Dialog
    private void showRegisterDialog() {
        JDialog dialog = new JDialog(this, "Register New Student", true);
        dialog.setSize(400, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 12));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtId = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtCourse = new JTextField();
        JSpinner spSemester = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        JTextField txtPhone = new JTextField();
        JComboBox<String> cmbGender = new JComboBox<>(new String[]{"M", "F"});

        form.add(new JLabel("Student ID:"));
        form.add(txtId);
        form.add(new JLabel("Full Name:"));
        form.add(txtName);
        form.add(new JLabel("Course:"));
        form.add(txtCourse);
        form.add(new JLabel("Semester (1-8):"));
        form.add(spSemester);
        form.add(new JLabel("Phone Number:"));
        form.add(txtPhone);
        form.add(new JLabel("Gender:"));
        form.add(cmbGender);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSubmit = createStyledButton("Save Student", SUCCESS_COLOR, Color.WHITE);
        JButton btnCancel = new JButton("Cancel");

        btnSubmit.addActionListener(e -> {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String course = txtCourse.getText().trim();
            int sem = (int) spSemester.getValue();
            String phone = txtPhone.getText().trim();
            String gender = (String) cmbGender.getSelectedItem();

            if (id.isEmpty() || name.isEmpty() || course.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (HostelManagement.findStudent(id) != null) {
                JOptionPane.showMessageDialog(dialog, "Student ID " + id + " already exists!", "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student s = new Student(id, name, course, sem, phone, gender);
            HostelManagement.students.add(s);
            refreshAllData();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Student registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSubmit);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // 2. Allocate Room Dialog
    private void showAllocateDialog() {
        ArrayList<Student> unallocated = new ArrayList<>();
        for (Student s : HostelManagement.students) {
            if (s.getAllocatedRoom() == 0) {
                unallocated.add(s);
            }
        }

        if (unallocated.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All registered students already have rooms allocated!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ArrayList<Room> availableRooms = new ArrayList<>();
        for (Room r : HostelManagement.rooms) {
            if (r.isAvailable()) {
                availableRooms.add(r);
            }
        }

        if (availableRooms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No rooms are currently available!", "No Vacancy", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Allocate Room to Student", true);
        dialog.setSize(440, 240);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 16));
        form.setBorder(new EmptyBorder(25, 20, 20, 20));

        JComboBox<String> cmbStudents = new JComboBox<>();
        for (Student s : unallocated) {
            cmbStudents.addItem(s.getStudentId() + " - " + s.getName());
        }

        JComboBox<String> cmbRooms = new JComboBox<>();
        for (Room r : availableRooms) {
            cmbRooms.addItem("Room " + r.getRoomNumber() + " (" + r.getRoomType() + ", " + r.getAvailableBeds() + " beds left)");
        }

        form.add(new JLabel("Select Student:"));
        form.add(cmbStudents);
        form.add(new JLabel("Select Available Room:"));
        form.add(cmbRooms);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSubmit = createStyledButton("Confirm Allocation", SECONDARY_COLOR, Color.WHITE);
        JButton btnCancel = new JButton("Cancel");

        btnSubmit.addActionListener(e -> {
            int studentIdx = cmbStudents.getSelectedIndex();
            int roomIdx = cmbRooms.getSelectedIndex();

            if (studentIdx >= 0 && roomIdx >= 0) {
                Student s = unallocated.get(studentIdx);
                Room r = availableRooms.get(roomIdx);

                s.setAllocatedRoom(r.getRoomNumber());
                r.addStudent();

                refreshAllData();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Room " + r.getRoomNumber() + " allocated to " + s.getName() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSubmit);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // 3. Vacate Room Dialog
    private void showVacateDialog() {
        ArrayList<Student> allocated = new ArrayList<>();
        for (Student s : HostelManagement.students) {
            if (s.getAllocatedRoom() != 0) {
                allocated.add(s);
            }
        }

        if (allocated.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students currently have rooms to vacate.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Vacate Room", true);
        dialog.setSize(420, 220);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 16));
        form.setBorder(new EmptyBorder(25, 20, 20, 20));

        JComboBox<String> cmbStudents = new JComboBox<>();
        for (Student s : allocated) {
            cmbStudents.addItem(s.getStudentId() + " - " + s.getName() + " (Room " + s.getAllocatedRoom() + ")");
        }

        form.add(new JLabel("Select Student to Vacate:"));
        form.add(cmbStudents);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSubmit = createStyledButton("Vacate Room", DANGER_COLOR, Color.WHITE);
        JButton btnCancel = new JButton("Cancel");

        btnSubmit.addActionListener(e -> {
            int idx = cmbStudents.getSelectedIndex();
            if (idx >= 0) {
                Student s = allocated.get(idx);
                int roomNum = s.getAllocatedRoom();
                Room r = HostelManagement.findRoom(roomNum);

                s.setAllocatedRoom(0);
                if (r != null) {
                    r.removeStudent();
                }

                refreshAllData();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Room " + roomNum + " vacated by " + s.getName() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSubmit);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // 4. Change Room Dialog
    private void showChangeRoomDialog() {
        ArrayList<Student> allocated = new ArrayList<>();
        for (Student s : HostelManagement.students) {
            if (s.getAllocatedRoom() != 0) {
                allocated.add(s);
            }
        }

        if (allocated.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students are currently allocated rooms.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Change Student Room", true);
        dialog.setSize(440, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 16));
        form.setBorder(new EmptyBorder(25, 20, 20, 20));

        JComboBox<String> cmbStudents = new JComboBox<>();
        for (Student s : allocated) {
            cmbStudents.addItem(s.getStudentId() + " - " + s.getName() + " (Current: " + s.getAllocatedRoom() + ")");
        }

        JComboBox<String> cmbNewRooms = new JComboBox<>();
        updateAvailableRoomsCombo(cmbNewRooms, allocated.get(0).getAllocatedRoom());

        cmbStudents.addActionListener(e -> {
            int idx = cmbStudents.getSelectedIndex();
            if (idx >= 0) {
                updateAvailableRoomsCombo(cmbNewRooms, allocated.get(idx).getAllocatedRoom());
            }
        });

        form.add(new JLabel("Select Student:"));
        form.add(cmbStudents);
        form.add(new JLabel("Select New Room:"));
        form.add(cmbNewRooms);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSubmit = createStyledButton("Confirm Change", SECONDARY_COLOR, Color.WHITE);
        JButton btnCancel = new JButton("Cancel");

        btnSubmit.addActionListener(e -> {
            int sIdx = cmbStudents.getSelectedIndex();
            int rIdx = cmbNewRooms.getSelectedIndex();

            if (sIdx >= 0 && rIdx >= 0) {
                Student s = allocated.get(sIdx);
                String selectedRoomStr = (String) cmbNewRooms.getSelectedItem();
                if (selectedRoomStr == null || selectedRoomStr.equals("No other rooms available")) {
                    JOptionPane.showMessageDialog(dialog, "Please select a valid room!", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int newRoomNum = Integer.parseInt(selectedRoomStr.split(" ")[1]);
                Room oldRoom = HostelManagement.findRoom(s.getAllocatedRoom());
                Room newRoom = HostelManagement.findRoom(newRoomNum);

                if (oldRoom != null) oldRoom.removeStudent();
                s.setAllocatedRoom(newRoomNum);
                if (newRoom != null) newRoom.addStudent();

                refreshAllData();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Room changed successfully to Room " + newRoomNum + " for " + s.getName() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSubmit);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void updateAvailableRoomsCombo(JComboBox<String> combo, int currentRoom) {
        combo.removeAllItems();
        boolean hasAny = false;
        for (Room r : HostelManagement.rooms) {
            if (r.getRoomNumber() != currentRoom && r.isAvailable()) {
                combo.addItem("Room " + r.getRoomNumber() + " (" + r.getRoomType() + ", " + r.getAvailableBeds() + " beds left)");
                hasAny = true;
            }
        }
        if (!hasAny) {
            combo.addItem("No other rooms available");
        }
    }

    // 5. Remove Student
    private void removeSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table first!", "Select Student", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = (String) studentsTableModel.getValueAt(selectedRow, 0);
        Student s = HostelManagement.findStudent(studentId);
        if (s == null) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove " + s.getName() + " (" + s.getStudentId() + ")?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (s.getAllocatedRoom() != 0) {
                Room r = HostelManagement.findRoom(s.getAllocatedRoom());
                if (r != null) r.removeStudent();
            }
            HostelManagement.students.remove(s);
            refreshAllData();
            JOptionPane.showMessageDialog(this, "Student removed successfully.", "Removed", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // --- Utility UI Styling Methods ---
    private JButton createStyledButton(String text, Icon icon, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color currentBg = bg;
                if (getModel().isPressed()) {
                    currentBg = bg.darker();
                } else if (getModel().isRollover()) {
                    currentBg = new Color(
                            Math.min(255, bg.getRed() + 30),
                            Math.min(255, bg.getGreen() + 30),
                            Math.min(255, bg.getBlue() + 30)
                    );
                }

                // Draw rich solid rounded background
                g2.setColor(currentBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Draw subtle darker border
                g2.setColor(new Color(Math.max(0, bg.getRed() - 35), Math.max(0, bg.getGreen() - 35), Math.max(0, bg.getBlue() - 35)));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        if (icon != null) {
            btn.setIcon(icon);
            btn.setIconTextGap(8);
        }

        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setBorder(new EmptyBorder(7, 14, 7, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        return createStyledButton(text, null, bg, fg);
    }

    // --- Vector Icon Classes (Guaranteed 100% visible across all platforms) ---
    static class PlusIcon implements Icon {
        private final int size;
        private final Color color;
        public PlusIcon(int size, Color color) { this.size = size; this.color = color; }
        public int getIconWidth() { return size; }
        public int getIconHeight() { return size; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int midX = x + size / 2;
            int midY = y + size / 2;
            int pad = 2;
            g2.drawLine(midX, y + pad, midX, y + size - pad);
            g2.drawLine(x + pad, midY, x + size - pad, midY);
            g2.dispose();
        }
    }

    static class KeyIcon implements Icon {
        private final int size;
        private final Color color;
        public KeyIcon(int size, Color color) { this.size = size; this.color = color; }
        public int getIconWidth() { return size; }
        public int getIconHeight() { return size; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(x + 1, y + 1, 6, 6);
            g2.drawLine(x + 7, y + 5, x + 13, y + 11);
            g2.drawLine(x + 10, y + 8, x + 12, y + 6);
            g2.drawLine(x + 12, y + 10, x + 14, y + 8);
            g2.dispose();
        }
    }

    static class VacateIcon implements Icon {
        private final int size;
        private final Color color;
        public VacateIcon(int size, Color color) { this.size = size; this.color = color; }
        public int getIconWidth() { return size; }
        public int getIconHeight() { return size; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawRect(x + 1, y + 1, 6, 12);
            g2.drawLine(x + 5, y + 7, x + 13, y + 7);
            g2.drawLine(x + 10, y + 4, x + 13, y + 7);
            g2.drawLine(x + 10, y + 10, x + 13, y + 7);
            g2.dispose();
        }
    }

    static class RefreshIcon implements Icon {
        private final int size;
        private final Color color;
        public RefreshIcon(int size, Color color) { this.size = size; this.color = color; }
        public int getIconWidth() { return size; }
        public int getIconHeight() { return size; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(x + 2, y + 2, size - 4, size - 4, 45, 275);
            g2.drawLine(x + size - 3, y + 2, x + size - 3, y + 6);
            g2.drawLine(x + size - 6, y + 3, x + size - 3, y + 6);
            g2.dispose();
        }
    }

    static class ChangeIcon implements Icon {
        private final int size;
        private final Color color;
        public ChangeIcon(int size, Color color) { this.size = size; this.color = color; }
        public int getIconWidth() { return size; }
        public int getIconHeight() { return size; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x + 1, y + 4, x + 12, y + 4);
            g2.drawLine(x + 9, y + 1, x + 12, y + 4);
            g2.drawLine(x + 12, y + 10, x + 1, y + 10);
            g2.drawLine(x + 4, y + 13, x + 1, y + 10);
            g2.dispose();
        }
    }

    static class TrashIcon implements Icon {
        private final int size;
        private final Color color;
        public TrashIcon(int size, Color color) { this.size = size; this.color = color; }
        public int getIconWidth() { return size; }
        public int getIconHeight() { return size; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawRect(x + 3, y + 4, 8, 9);
            g2.drawLine(x + 1, y + 3, x + 13, y + 3);
            g2.drawLine(x + 5, y + 1, x + 9, y + 1);
            g2.drawLine(x + 6, y + 6, x + 6, y + 11);
            g2.drawLine(x + 8, y + 6, x + 8, y + 11);
            g2.dispose();
        }
    }

    private void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.getTableHeader().setForeground(new Color(30, 41, 59));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setGridColor(new Color(241, 245, 249));
    }

    // Static launch method
    public static void launch() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            HostelDashboard dashboard = new HostelDashboard();
            dashboard.setVisible(true);
        });
    }

    // Entry point
    public static void main(String[] args) {
        launch();
    }
}

