import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class AddRestaurantFrame extends JFrame {
    private JTextField nameField, ratingField, reviewField;
    private JComboBox<String> categoryComboBox;
    private JTextField locationField;

    public AddRestaurantFrame() {
        setTitle("뭐 먹을까?");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 200);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("식당 이름:");
        nameField = new JTextField();
        nameField.setColumns(20); // 텍스트 필드 크기 조정
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);

        JLabel ratingLabel = new JLabel("평점(1-5):");
        ratingField = new JTextField();
        ratingField.setColumns(20); // 텍스트 필드 크기 조정
        inputPanel.add(ratingLabel);
        inputPanel.add(ratingField);

        JLabel reviewLabel = new JLabel("리뷰:");
        reviewField = new JTextField();
        reviewField.setColumns(20); // 텍스트 필드 크기 조정
        inputPanel.add(reviewLabel);
        inputPanel.add(reviewField);

        JLabel categoryLabel = new JLabel("카테고리:");
        String[] categories = {"한식", "중식", "양식", "일식", "기타"};
        categoryComboBox = new JComboBox<>(categories);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryComboBox);

        JLabel locationLabel = new JLabel("위치:");
        locationField = new JTextField();
        locationField.setColumns(20); // 텍스트 필드 크기 조정
        inputPanel.add(locationLabel);
        inputPanel.add(locationField);

        JButton addButton = new JButton("리뷰 추가");
        addButton.setPreferredSize(new Dimension(100, 30)); // 버튼 크기 조정

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String rating = ratingField.getText();
                String review = reviewField.getText();
                String category = (String) categoryComboBox.getSelectedItem();
                String location = locationField.getText();

                if (!name.isEmpty() && !rating.isEmpty() && !review.isEmpty() && !category.isEmpty() && !location.isEmpty()) {
                    String restaurantInfo = name + "," + rating + "," + review + "," + category + "," + location;
                    saveRestaurantInfo(restaurantInfo);
                    JOptionPane.showMessageDialog(AddRestaurantFrame.this, "리뷰 추가 성공.");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(AddRestaurantFrame.this, "항목을 모두 입력하세요.");
                }
            }
        });

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(addButton, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void saveRestaurantInfo(String restaurantInfo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("restaurant.txt", true))) {
            writer.write(restaurantInfo);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.setText("");
        ratingField.setText("");
        reviewField.setText("");
        categoryComboBox.setSelectedIndex(0);
        locationField.setText("");
    }
}

