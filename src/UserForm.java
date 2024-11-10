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
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(224, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("BMI Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(nameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(emailField, gbc);

        JLabel weightLabel = new JLabel("Weight (kg):");
        weightLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(weightLabel, gbc);

        weightField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(weightField, gbc);

        JLabel heightLabel = new JLabel("Height (cm):");
        heightLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(heightLabel, gbc);

        heightField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(heightField, gbc);

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setBackground(new Color(144, 238, 144));
        submitButton.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(submitButton, gbc);

        bmiResultLabel = new JLabel("Your BMI:");
        bmiResultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bmiResultLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(bmiResultLabel, gbc);

        bmiCategoryButton = new JButton("Show BMI Category");
        bmiCategoryButton.setFont(new Font("Arial", Font.BOLD, 16));
        bmiCategoryButton.setBackground(new Color(255, 165, 0));
        bmiCategoryButton.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(bmiCategoryButton, gbc);

        ImageIcon imageIcon = new ImageIcon("doctor_bmi.png");
        imageLabel = new JLabel(imageIcon);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(imageLabel, gbc);

        trackerTextArea = new JTextArea(10, 40);
        trackerTextArea.setEditable(false);
        trackerTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(trackerTextArea);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(scrollPane, gbc);

        viewTrackerButton = new JButton("View Tracker");
        viewTrackerButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewTrackerButton.setBackground(new Color(173, 216, 230));
        viewTrackerButton.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(viewTrackerButton, gbc);

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
            String sql = "INSERT INTO Users (name, email, bmi, username, password) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setDouble(3, bmi);
                stmt.setString(4, "dummy_username"); // Dummy value for username
                stmt.setString(5, "dummy_password"); // Dummy value for password
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