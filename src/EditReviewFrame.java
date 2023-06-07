import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EditReviewFrame extends JFrame {
    private JTextField nameField;
    private JTextField ratingField;
    private JTextField reviewField;
    private JTextField categoryField;
    private JTextField locationField;
    private int selectedIndex;
    private DefaultListModel<String> model;

    public EditReviewFrame(int selectedIndex, DefaultListModel<String> model) {
        this.selectedIndex = selectedIndex;
        this.model = model;

        setTitle("식당 수정");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        JLabel nameLabel = new JLabel("식당 이름:");
        nameField = new JTextField();
        JLabel ratingLabel = new JLabel("평점:");
        ratingField = new JTextField();
        JLabel reviewLabel = new JLabel("리뷰:");
        reviewField = new JTextField();
        JLabel categoryLabel = new JLabel("카테고리:");
        categoryField = new JTextField();
        JLabel locationLabel = new JLabel("위치:");
        locationField = new JTextField();

        JButton saveButton = new JButton("저장");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String editedRestaurantName = nameField.getText();
                String editedRating = ratingField.getText();
                String editedReview = reviewField.getText();
                String editedCategory = categoryField.getText();
                String editedLocation = locationField.getText();

                String editedRestaurantData = editedRestaurantName + "," + editedRating + "," + editedReview + "," + editedCategory + "," + editedLocation;

                // Update the restaurant data in the file
                updateRestaurantData(selectedIndex, editedRestaurantData);

                // Update the displayed restaurant data in the list
                model.setElementAt(getFormattedRestaurantString(editedRestaurantName, editedRating, editedReview, editedCategory, editedLocation), selectedIndex);

                dispose();
                JOptionPane.showMessageDialog(null, "리뷰가 수정되었습니다.");
            }
        });

        add(nameLabel);
        add(nameField);
        add(ratingLabel);
        add(ratingField);
        add(reviewLabel);
        add(reviewField);
        add(categoryLabel);
        add(categoryField);
        add(locationLabel);
        add(locationField);
        add(saveButton);
        setPreferredSize(new Dimension(400, 300));

        pack();
        setLocationRelativeTo(null);
    }


    private void updateRestaurantData(int selectedIndex, String editedRestaurantData) {
        try {
            File inputFile = new File("restaurant.txt");
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            int lineIndex = 0;
            while ((line = reader.readLine()) != null) {
                if (lineIndex == selectedIndex) {
                    writer.write(editedRestaurantData);
                    writer.newLine();
                } else {
                    writer.write(line);
                    writer.newLine();
                }
                lineIndex++;
            }

            reader.close();
            writer.close();

            if (inputFile.delete()) {
                if (!tempFile.renameTo(inputFile)) {
                    throw new IOException("Failed to rename temporary file");
                }
            } else {
                throw new IOException("Failed to delete input file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFormattedRestaurantString(String name, String rating, String review, String category, String location) {
        return "식당 이름: " + name + " | 평점: " + rating + " | 리뷰: " + review + " | 카테고리: " + category + " | 위치: " + location;
    }
    public JTextField getNameField() {
        return nameField;
    }

    public JTextField getRatingField() {
        return ratingField;
    }

    public JTextField getReviewField() {
        return reviewField;
    }

    public JTextField getCategoryField() {
        return categoryField;
    }

    public JTextField getLocationField() {
        return locationField;
    }

}
