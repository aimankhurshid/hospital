import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserForm extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField weightField;
    private JTextField heightField;
    private JButton submitButton, bmiCategoryButton, viewTrackerButton;
    private JLabel bmiResultLabel;
    private JLabel imageLabel;
    private JTextArea trackerTextArea;

    public UserForm() {
        setTitle("User Form - BMI Calculator");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(224, 255, 255));

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 20, 100, 25);
        panel.add(nameLabel);

        nameField = new JTextField(20);
        nameField.setBounds(120, 20, 250, 25);
        panel.add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 60, 100, 25);
        panel.add(emailLabel);

        emailField = new JTextField(20);
        emailField.setBounds(120, 60, 250, 25);
        panel.add(emailField);

        JLabel weightLabel = new JLabel("Weight (kg):");
        weightLabel.setBounds(20, 100, 100, 25);
        panel.add(weightLabel);

        weightField = new JTextField(10);
        weightField.setBounds(120, 100, 100, 25);
        panel.add(weightField);

        JLabel heightLabel = new JLabel("Height (cm):");
        heightLabel.setBounds(20, 140, 100, 25);
        panel.add(heightLabel);

        heightField = new JTextField(10);
        heightField.setBounds(120, 140, 100, 25);
        panel.add(heightField);

        submitButton = new JButton("Submit");
        submitButton.setBounds(120, 180, 100, 30);
        submitButton.setBackground(new Color(144, 238, 144));
        submitButton.setForeground(Color.BLACK);
        panel.add(submitButton);

        bmiResultLabel = new JLabel("Your BMI:");
        bmiResultLabel.setBounds(20, 220, 300, 25);
        bmiResultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(bmiResultLabel);

        bmiCategoryButton = new JButton("Show BMI Category");
        bmiCategoryButton.setBounds(120, 260, 200, 30);
        bmiCategoryButton.setBackground(new Color(255, 165, 0));
        bmiCategoryButton.setForeground(Color.BLACK);
        panel.add(bmiCategoryButton);

        viewTrackerButton = new JButton("View Tracker");
        viewTrackerButton.setBounds(120, 300, 200, 30);
        viewTrackerButton.setBackground(new Color(173, 216, 230));
        viewTrackerButton.setForeground(Color.BLACK);
        panel.add(viewTrackerButton);

        ImageIcon imageIcon = new ImageIcon("doctor_bmi.png");
        imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(150, 340, 150, 150);
        panel.add(imageLabel);

        trackerTextArea = new JTextArea();
        trackerTextArea.setBounds(20, 500, 450, 80);
        trackerTextArea.setEditable(false);
        panel.add(trackerTextArea);

        add(panel);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String weightText = weightField.getText();
                String heightText = heightField.getText();

                if (!isNumeric(weightText) || !isNumeric(heightText)) {
                    JOptionPane.showMessageDialog(UserForm.this, "Please enter valid numeric values for weight and height.");
                    return;
                }

                double weight = Double.parseDouble(weightText);
                double heightInCm = Double.parseDouble(heightText);
                double heightInMeters = heightInCm / 100;
                double bmi = calculateBMI(weight, heightInMeters);

                bmiResultLabel.setText("Your BMI: " + String.format("%.2f", bmi));
                saveUser(name, email, bmi);
                trackerTextArea.append("User " + name + " with email " + email + " saved with BMI " + String.format("%.2f", bmi) + "\n");
            }
        });

        bmiCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String weightText = weightField.getText();
                String heightText = heightField.getText();

                if (!isNumeric(weightText) || !isNumeric(heightText)) {
                    JOptionPane.showMessageDialog(UserForm.this, "Please enter valid numeric values for weight and height.");
                    return;
                }

                double weight = Double.parseDouble(weightText);
                double heightInCm = Double.parseDouble(heightText);
                double heightInMeters = heightInCm / 100;
                double bmi = calculateBMI(weight, heightInMeters);

                String category = categorizeBMI(bmi);
                JOptionPane.showMessageDialog(null, "Your BMI category is: " + category);
                trackerTextArea.append("BMI category for weight " + weight + " and height " + heightInCm + " is " + category + "\n");
            }
        });

        viewTrackerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(UserForm.this, trackerTextArea.getText(), "Tracker", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private double calculateBMI(double weight, double height) {
        return weight / (height * height);
    }

    private String categorizeBMI(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi >= 18.5 && bmi < 24.9) {
            return "Normal weight";
        } else if (bmi >= 25 && bmi < 29.9) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    private void saveUser(String name, String email, double bmi) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO Users (name, email, bmi) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setDouble(3, bmi);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "User saved successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving user: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserForm().setVisible(true));
    }
}