import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class CGPAGenerator extends JFrame {

    // ---------- Current Semester ----------
    private JTextField txtCourse, txtCredit, txtMarks;
    private JComboBox<String> cmbScale;
    private JButton btnAdd, btnDeleteCourse, btnClearCourse;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblCurrentResult;
    private int currentEditRow = -1;

    private double currentTotalCredit = 0;
    private double currentTotalPoint = 0;

    // ---------- Previous Semesters ----------
    private JTextField txtSemName, txtPrevCredit, txtPrevCgpa;
    private JButton btnAddSemester, btnDeleteSemester, btnClearSemester;
    private JTable tablePrev;
    private DefaultTableModel modelPrev;
    private JLabel lblPrevResult;
    private int prevEditRow = -1;

    private double prevTotalCredit = 0;
    private double prevTotalPoint = 0;

    // ---------- Student Info ----------
    private JTextField txtStudentName, txtStudentId;

    // ---------- Overall ----------
    private final JLabel lblOverallResult;

    public CGPAGenerator() {
        setTitle("UGV Student Mark and CGPA Generator");
        setSize(970, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        add(buildStudentInfoPanel(), BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Current Semester", buildCurrentSemesterPanel());
        tabbedPane.addTab("Previous Semesters", buildPreviousSemesterPanel());
        add(tabbedPane, BorderLayout.CENTER);

        // ---------- Overall Result Panel (always visible) ----------
        lblOverallResult = new JLabel("Overall CGPA: 0.00     (Total Credits: 0)", SwingConstants.CENTER);
        lblOverallResult.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblOverallResult.setForeground(Color.WHITE);
        lblOverallResult.setOpaque(true);
        lblOverallResult.setBackground(new Color(31, 78, 121));
        lblOverallResult.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(lblOverallResult, BorderLayout.SOUTH);

        setVisible(true);
    }

    // =========================================================
    // STUDENT INFO PANEL
    // =========================================================
    private JPanel buildStudentInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(31, 78, 121));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        JLabel lblName = new JLabel("Student Name");
        lblName.setForeground(Color.WHITE);
        JLabel lblId = new JLabel("Student ID");
        lblId.setForeground(Color.WHITE);

        gbc.gridx = 0;
        panel.add(lblName, gbc);
        gbc.gridx = 1;
        txtStudentName = new JTextField(18);
        panel.add(txtStudentName, gbc);

        gbc.gridx = 2;
        panel.add(lblId, gbc);
        gbc.gridx = 3;
        txtStudentId = new JTextField(12);
        panel.add(txtStudentId, gbc);

        // Update the overall CGPA banner whenever the name/ID changes
        txtStudentName.addActionListener(e -> updateOverallCgpa());
        txtStudentId.addActionListener(e -> updateOverallCgpa());
        txtStudentName.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                updateOverallCgpa();
            }
        });
        txtStudentId.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                updateOverallCgpa();
            }
        });

        return panel;
    }

    // =========================================================
    // CURRENT SEMESTER TAB
    // =========================================================
    private JPanel buildCurrentSemesterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        // ---------- Input Fields ----------
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;

        gbc.gridx = 0;
        inputPanel.add(new JLabel("Course"), gbc);
        gbc.gridx = 1;
        txtCourse = new JTextField(14);
        inputPanel.add(txtCourse, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Credit"), gbc);
        gbc.gridx = 3;
        txtCredit = new JTextField(4);
        inputPanel.add(txtCredit, gbc);

        gbc.gridx = 4;
        inputPanel.add(new JLabel("Marks"), gbc);
        gbc.gridx = 5;
        txtMarks = new JTextField(4);
        inputPanel.add(txtMarks, gbc);

        gbc.gridx = 6;
        inputPanel.add(new JLabel("Scale"), gbc);
        gbc.gridx = 7;
        cmbScale = new JComboBox<>(new String[]{"Standard"});
        inputPanel.add(cmbScale, gbc);

        gbc.gridx = 8;
        btnAdd = new JButton("Add Course");
        btnAdd.setBackground(new Color(31, 78, 121));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        inputPanel.add(btnAdd, gbc);

        // ---------- Second row: hint + Delete/Clear ----------
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel hint = new JLabel("Tip: click a row in the table below to edit or delete it.");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hint.setForeground(Color.GRAY);
        inputPanel.add(hint, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 7;
        btnDeleteCourse = new JButton("Delete Selected");
        btnDeleteCourse.setBackground(new Color(178, 34, 34));
        btnDeleteCourse.setForeground(Color.WHITE);
        btnDeleteCourse.setFocusPainted(false);
        inputPanel.add(btnDeleteCourse, gbc);

        gbc.gridx = 8;
        btnClearCourse = new JButton("Clear Form");
        btnClearCourse.setFocusPainted(false);
        inputPanel.add(btnClearCourse, gbc);

        panel.add(inputPanel, BorderLayout.NORTH);

        // ---------- Table ----------
        String[] columns = {"Course", "Credit", "Total Marks", "Obtained Marks", "Percentage", "Grade", "Point"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        panel.add(scrollPane, BorderLayout.CENTER);

        // ---------- Current Semester Result ----------
        lblCurrentResult = new JLabel("This Semester -> Credits: 0     CGPA: 0.00");
        lblCurrentResult.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblCurrentResult.setForeground(new Color(20, 90, 50));
        lblCurrentResult.setOpaque(true);
        lblCurrentResult.setBackground(new Color(216, 237, 216));
        lblCurrentResult.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 15));

        JPanel bottomWrap = new JPanel(new BorderLayout());
        bottomWrap.setBackground(Color.WHITE);
        bottomWrap.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        bottomWrap.add(lblCurrentResult, BorderLayout.CENTER);
        panel.add(bottomWrap, BorderLayout.SOUTH);

        // ---------- Actions ----------
        btnAdd.addActionListener(e -> addOrUpdateCourse());

        btnDeleteCourse.addActionListener(e -> deleteCourse());

        btnClearCourse.addActionListener(e -> clearCourseForm());

        // Clicking a row loads it into the form for editing
        table.getSelectionModel().addListSelectionListener((ListSelectionListener) e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                currentEditRow = row;
                txtCourse.setText(model.getValueAt(row, 0).toString());
                txtCredit.setText(model.getValueAt(row, 1).toString());
                txtMarks.setText(model.getValueAt(row, 3).toString());
                btnAdd.setText("Update Course");
            }
        });

        return panel;
    }

    private void addOrUpdateCourse() {
        String course = txtCourse.getText().trim();
        String creditStr = txtCredit.getText().trim();
        String marksStr = txtMarks.getText().trim();

        if (course.isEmpty() || creditStr.isEmpty() || marksStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill in all the fields!");
            return;
        }

        double credit, marks;
        try {
            credit = Double.parseDouble(creditStr);
            marks = Double.parseDouble(marksStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credit and Marks must be valid numbers!");
            return;
        }

        if (credit <= 0) {
            JOptionPane.showMessageDialog(this, "The credit must be greater than 0!");
            return;
        }

        // Total marks depend on credit: 1 credit = 50, 2 credit = 100, 3 credit = 150, etc.
        double totalMarks = credit * 50;

        if (marks < 0 || marks > totalMarks) {
            JOptionPane.showMessageDialog(this,
                    "For a " + trimZero(credit) + " credit course, marks must be between 0 and " + trimZero(totalMarks) + "!");
            return;
        }

        double percentage = (marks / totalMarks) * 100.0;
        String grade = getGrade(percentage);
        double point = getPoint(percentage);

        Object[] rowData = {
                course,
                trimZero(credit),
                trimZero(totalMarks),
                trimZero(marks),
                String.format("%.1f%%", percentage),
                grade,
                String.format("%.2f", point)
        };

        if (currentEditRow == -1) {
            model.addRow(rowData);
        } else {
            for (int col = 0; col < rowData.length; col++) {
                model.setValueAt(rowData[col], currentEditRow, col);
            }
        }

        clearCourseForm();
        recalcCurrentTotals();
    }

    private void deleteCourse() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row from the table first!");
            return;
        }
        model.removeRow(row);
        clearCourseForm();
        recalcCurrentTotals();
    }

    private void clearCourseForm() {
        currentEditRow = -1;
        btnAdd.setText("Add Course");
        txtCourse.setText("");
        txtCredit.setText("");
        txtMarks.setText("");
        table.clearSelection();
        txtCourse.requestFocus();
    }

    private void recalcCurrentTotals() {
        currentTotalCredit = 0;
        currentTotalPoint = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            double credit = Double.parseDouble(model.getValueAt(i, 1).toString());
            double point = Double.parseDouble(model.getValueAt(i, 6).toString());
            currentTotalCredit += credit;
            currentTotalPoint += credit * point;
        }
        double currentCgpa = currentTotalCredit == 0 ? 0 : currentTotalPoint / currentTotalCredit;
        lblCurrentResult.setText(String.format("This Semester -> Credits: %s     CGPA: %.2f", trimZero(currentTotalCredit), currentCgpa));
        updateOverallCgpa();
    }

    // =========================================================
    // PREVIOUS SEMESTERS TAB
    // =========================================================
    private JPanel buildPreviousSemesterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        // ---------- Input Fields ----------
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;

        gbc.gridx = 0;
        inputPanel.add(new JLabel("Semester Name"), gbc);
        gbc.gridx = 1;
        txtSemName = new JTextField(14);
        inputPanel.add(txtSemName, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Total Credit"), gbc);
        gbc.gridx = 3;
        txtPrevCredit = new JTextField(5);
        inputPanel.add(txtPrevCredit, gbc);

        gbc.gridx = 4;
        inputPanel.add(new JLabel("Semester CGPA"), gbc);
        gbc.gridx = 5;
        txtPrevCgpa = new JTextField(5);
        inputPanel.add(txtPrevCgpa, gbc);

        gbc.gridx = 6;
        btnAddSemester = new JButton("Add Semester");
        btnAddSemester.setBackground(new Color(31, 78, 121));
        btnAddSemester.setForeground(Color.WHITE);
        btnAddSemester.setFocusPainted(false);
        inputPanel.add(btnAddSemester, gbc);

        // ---------- Second row: hint + Delete/Clear ----------
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel hint = new JLabel("Tip: click a row in the table below to edit or delete it.");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hint.setForeground(Color.GRAY);
        inputPanel.add(hint, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 5;
        btnDeleteSemester = new JButton("Delete Selected");
        btnDeleteSemester.setBackground(new Color(178, 34, 34));
        btnDeleteSemester.setForeground(Color.WHITE);
        btnDeleteSemester.setFocusPainted(false);
        inputPanel.add(btnDeleteSemester, gbc);

        gbc.gridx = 6;
        btnClearSemester = new JButton("Clear Form");
        btnClearSemester.setFocusPainted(false);
        inputPanel.add(btnClearSemester, gbc);

        panel.add(inputPanel, BorderLayout.NORTH);

        // ---------- Table ----------
        String[] columns = {"Semester", "Credit", "CGPA"};
        modelPrev = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePrev = new JTable(modelPrev);
        tablePrev.setRowHeight(25);
        tablePrev.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePrev.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(tablePrev);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        panel.add(scrollPane, BorderLayout.CENTER);

        // ---------- Previous Semesters Result ----------
        lblPrevResult = new JLabel("Previous Semesters -> Credits: 0     CGPA: 0.00");
        lblPrevResult.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblPrevResult.setForeground(new Color(20, 90, 50));
        lblPrevResult.setOpaque(true);
        lblPrevResult.setBackground(new Color(216, 237, 216));
        lblPrevResult.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 15));

        JPanel bottomWrap = new JPanel(new BorderLayout());
        bottomWrap.setBackground(Color.WHITE);
        bottomWrap.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        bottomWrap.add(lblPrevResult, BorderLayout.CENTER);
        panel.add(bottomWrap, BorderLayout.SOUTH);

        // ---------- Actions ----------
        btnAddSemester.addActionListener(e -> addOrUpdateSemester());

        btnDeleteSemester.addActionListener(e -> deleteSemester());

        btnClearSemester.addActionListener(e -> clearSemesterForm());

        // Clicking a row loads it into the form for editing
        tablePrev.getSelectionModel().addListSelectionListener((ListSelectionListener) e -> {
            if (!e.getValueIsAdjusting() && tablePrev.getSelectedRow() != -1) {
                int row = tablePrev.getSelectedRow();
                prevEditRow = row;
                txtSemName.setText(modelPrev.getValueAt(row, 0).toString());
                txtPrevCredit.setText(modelPrev.getValueAt(row, 1).toString());
                txtPrevCgpa.setText(modelPrev.getValueAt(row, 2).toString());
                btnAddSemester.setText("Update Semester");
            }
        });

        return panel;
    }

    private void addOrUpdateSemester() {
        String semName = txtSemName.getText().trim();
        String creditStr = txtPrevCredit.getText().trim();
        String cgpaStr = txtPrevCgpa.getText().trim();

        if (semName.isEmpty() || creditStr.isEmpty() || cgpaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill in all the fields!");
            return;
        }

        double credit, cgpa;
        try {
            credit = Double.parseDouble(creditStr);
            cgpa = Double.parseDouble(cgpaStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credit and CGPA must be valid numbers!");
            return;
        }

        if (credit <= 0) {
            JOptionPane.showMessageDialog(this, "The credit must be greater than 0!");
            return;
        }
        if (cgpa < 0 || cgpa > 4.0) {
            JOptionPane.showMessageDialog(this, "CGPA must be between 0.00 and 4.00!");
            return;
        }

        Object[] rowData = {semName, trimZero(credit), String.format("%.2f", cgpa)};

        if (prevEditRow == -1) {
            modelPrev.addRow(rowData);
        } else {
            for (int col = 0; col < rowData.length; col++) {
                modelPrev.setValueAt(rowData[col], prevEditRow, col);
            }
        }

        clearSemesterForm();
        recalcPrevTotals();
    }

    private void deleteSemester() {
        int row = tablePrev.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row from the table first!");
            return;
        }
        modelPrev.removeRow(row);
        clearSemesterForm();
        recalcPrevTotals();
    }

    private void clearSemesterForm() {
        prevEditRow = -1;
        btnAddSemester.setText("Add Semester");
        txtSemName.setText("");
        txtPrevCredit.setText("");
        txtPrevCgpa.setText("");
        tablePrev.clearSelection();
        txtSemName.requestFocus();
    }

    private void recalcPrevTotals() {
        prevTotalCredit = 0;
        prevTotalPoint = 0;
        for (int i = 0; i < modelPrev.getRowCount(); i++) {
            double credit = Double.parseDouble(modelPrev.getValueAt(i, 1).toString());
            double cgpa = Double.parseDouble(modelPrev.getValueAt(i, 2).toString());
            prevTotalCredit += credit;
            prevTotalPoint += credit * cgpa;
        }
        double prevCgpaCombined = prevTotalCredit == 0 ? 0 : prevTotalPoint / prevTotalCredit;
        lblPrevResult.setText(String.format("Previous Semesters -> Credits: %s     CGPA: %.2f", trimZero(prevTotalCredit), prevCgpaCombined));
        updateOverallCgpa();
    }

    // =========================================================
    // OVERALL CGPA (Previous semesters + Current semester combined)
    // =========================================================
    private void updateOverallCgpa() {
        double overallCredit = prevTotalCredit + currentTotalCredit;
        double overallPoint = prevTotalPoint + currentTotalPoint;
        double overallCgpa = overallCredit == 0 ? 0 : overallPoint / overallCredit;

        String name = (txtStudentName != null) ? txtStudentName.getText().trim() : "";
        String id = (txtStudentId != null) ? txtStudentId.getText().trim() : "";

        StringBuilder sb = new StringBuilder();
        if (!name.isEmpty()) {
            sb.append(name).append("   ");
        }
        if (!id.isEmpty()) {
            sb.append("(ID: ").append(id).append(")   ");
        }
        sb.append(String.format("Overall CGPA: %.2f     (Total Credits: %s)", overallCgpa, trimZero(overallCredit)));

        lblOverallResult.setText(sb.toString());
    }

    // =========================================================
    // HELPERS
    // =========================================================

    // Removes trailing ".0" for whole numbers, keeps decimals otherwise (e.g. 3 instead of 3.0, 2.5 stays 2.5)
    private String trimZero(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    // Grade scale based on PERCENTAGE (same breakpoints apply to 1, 2, 3 credit courses)
    private String getGrade(double percentage) {
        if (percentage >= 80) return "A+";
        else if (percentage >= 75) return "A";
        else if (percentage >= 70) return "A-";
        else if (percentage >= 65) return "B+";
        else if (percentage >= 60) return "B";
        else if (percentage >= 55) return "B-";
        else if (percentage >= 50) return "C+";
        else if (percentage >= 45) return "C";
        else if (percentage >= 40) return "D";
        else return "F";
    }

    private double getPoint(double percentage) {
        if (percentage >= 80) return 4.00;
        else if (percentage >= 75) return 3.75;
        else if (percentage >= 70) return 3.50;
        else if (percentage >= 65) return 3.25;
        else if (percentage >= 60) return 3.00;
        else if (percentage >= 55) return 2.75;
        else if (percentage >= 50) return 2.50;
        else if (percentage >= 45) return 2.25;
        else if (percentage >= 40) return 2.00;
        else return 0.00;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CGPAGenerator::new);
    }
}