import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class RestaurantReviewMainFrame extends JFrame {
    private JTextField nameField;
    private JComboBox<String> categoryComboBox;
    private DefaultListModel<String> model;
    private JList<String> restaurantList;

    public RestaurantReviewMainFrame() {

        setTitle("뭐 먹을까?");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2));

        JLabel nameLabel = new JLabel("식당 이름:");
        nameField = new JTextField();
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);

        JLabel categoryLabel = new JLabel("카테고리:");
        String[] categories = {"카테고리 선택", "한식", "중식", "양식", "일식", "그 이외"};
        categoryComboBox = new JComboBox<>(categories);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryComboBox);

        JButton searchButton = new JButton("검색");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchName = nameField.getText();
                String searchCategory = (String) categoryComboBox.getSelectedItem();
                if (!searchName.isEmpty() && !searchCategory.equals("카테고리 선택")) {
                    JOptionPane.showMessageDialog(RestaurantReviewMainFrame.this, "이름과 카테고리 중 하나만 선택하세요.");
                } else if (!searchName.isEmpty() && searchCategory.equals("카테고리 선택")) {
                    ArrayList<String> foundReviews = searchRestaurant(searchName, searchCategory);
                    if (!foundReviews.isEmpty()) {
                        StringBuilder result = new StringBuilder();
                        for (String review : foundReviews) {
                            result.append(review).append("\n");
                        }
                        JOptionPane.showMessageDialog(RestaurantReviewMainFrame.this, result.toString());
                    } else {
                        JOptionPane.showMessageDialog(RestaurantReviewMainFrame.this, "검색 결과가 없습니다.");
                    }
                } else if (searchName.isEmpty() && !searchCategory.equals("카테고리 선택")) {
                    ArrayList<String> foundReviews = searchRestaurant(searchName, searchCategory);
                    if (!foundReviews.isEmpty()) {
                        StringBuilder result = new StringBuilder();
                        for (String review : foundReviews) {
                            result.append(review).append("\n");
                        }
                        JOptionPane.showMessageDialog(RestaurantReviewMainFrame.this, result.toString());
                    } else {
                        JOptionPane.showMessageDialog(RestaurantReviewMainFrame.this, "검색 결과가 없습니다.");
                    }
                } else {
                    JOptionPane.showMessageDialog(RestaurantReviewMainFrame.this, "이름이나 카테고리를 선택하세요.");
                }
            }
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(searchButton, BorderLayout.CENTER);
        model = new DefaultListModel<>();
        restaurantList = new JList<>(model);
        JScrollPane scrollPane = new JScrollPane(restaurantList);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton viewButton = new JButton("식당 조회");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRestaurantData();
            }
        });
        buttonPanel.add(viewButton);

        JButton addButton = new JButton("식당 추가");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddRestaurantFrame().setVisible(true);
            }
        });
        buttonPanel.add(addButton);

        JButton editButton = new JButton("리뷰 수정");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditReview();
            }
        });
        buttonPanel.add(editButton);

        JButton recommendButton = new JButton("식당 추천");
        recommendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recommendRestaurant();
            }
        });
        buttonPanel.add(recommendButton);

        // Frame Layout
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadRestaurantData() {
        model.clear();

        try (BufferedReader br = new BufferedReader(new FileReader("restaurant.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] restaurantInfo = line.split(",");
                String name = restaurantInfo[0];
                String rating = restaurantInfo[1];
                String review = restaurantInfo[2];
                String category = restaurantInfo[3];
                String location = restaurantInfo[4];
                String restaurantDataString = "식당 이름: " + name + " | 평점: " + rating + " | 리뷰: " + review + " | 카테고리: " + category + " | 위치: " + location;
                model.addElement(restaurantDataString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEditReview() {
        int selectedIndex = restaurantList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String selectedRestaurant = restaurantList.getSelectedValue();
            String[] restaurantInfo = selectedRestaurant.split("\\|");

            String restaurantName = restaurantInfo[0].substring(restaurantInfo[0].indexOf(":") + 1).trim();
            String rating = restaurantInfo[1].substring(restaurantInfo[1].indexOf(":") + 1).trim();
            String review = restaurantInfo[2].substring(restaurantInfo[2].indexOf(":") + 1).trim();
            String category = restaurantInfo[3].substring(restaurantInfo[3].indexOf(":") + 1).trim();
            String location = restaurantInfo[4].substring(restaurantInfo[4].indexOf(":") + 1).trim();

            EditReviewFrame editReviewFrame = new EditReviewFrame(selectedIndex, model);
            editReviewFrame.getNameField().setText(restaurantName);
            editReviewFrame.getRatingField().setText(rating);
            editReviewFrame.getReviewField().setText(review);
            editReviewFrame.getCategoryField().setText(category);
            editReviewFrame.getLocationField().setText(location);
            editReviewFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "리뷰를 수정할 식당을 선택하세요.");
        }
    }


    private ArrayList<String> searchRestaurant(String name, String category) {
        ArrayList<String> foundRestaurant = new ArrayList<>();
        if (category.equals("카테고리 선택")) {
            // 이름을 검색
            try (BufferedReader br = new BufferedReader(new FileReader("restaurant.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains(name)) {
                        String[] restaurantInfo = line.split(",");
                        String restaurantName = restaurantInfo[0];
                        String rating= restaurantInfo[1];
                        String review = restaurantInfo[2];
                        String txtCategory = restaurantInfo[3];
                        String location = restaurantInfo[4];

                        String foundRestaurantString = "식당 이름: " + restaurantName + " | 평점: " + rating + " | 리뷰: " + review + " | 카테고리: " + txtCategory + " | 위치: " + location;
                        foundRestaurant.add(foundRestaurantString);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 카테고리로 검색
            try (BufferedReader br = new BufferedReader(new FileReader("restaurant.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains(category)) {
                        String[] restaurantInfo = line.split(",");
                        String restaurantName = restaurantInfo[0];
                        String rating= restaurantInfo[1];
                        String review = restaurantInfo[2];
                        String txtCategory = restaurantInfo[3];
                        String location = restaurantInfo[4];
                        String foundRestaurantString = "식당 이름: " + restaurantName + " | 평점: " + rating + " | 리뷰: " + review + " | 카테고리: " + txtCategory + " | 위치: " + location;
                        foundRestaurant.add(foundRestaurantString);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return foundRestaurant;
    }

    private void recommendRestaurant() {
        try (BufferedReader br = new BufferedReader(new FileReader("restaurant.txt"))) {
            ArrayList<String> restaurants = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                restaurants.add(line);
            }

            if (!restaurants.isEmpty()) {
                Random random = new Random();
                String randomRestaurant = restaurants.get(random.nextInt(restaurants.size()));
                String[] restaurantInfo = randomRestaurant.split(",");
                String restaurantName = restaurantInfo[0];
                String rating = restaurantInfo[1];
                String review = restaurantInfo[2];
                String category = restaurantInfo[3];
                String location = restaurantInfo[4];

                String recommendedRestaurantString = "식당 이름: " + restaurantName + " | 평점: " + rating +
                        " | 리뷰: " + review + " | 카테고리: " + category + " | 위치: " + location;

                JOptionPane.showMessageDialog(this, recommendedRestaurantString);
            } else {
                JOptionPane.showMessageDialog(this, "등록된 식당이 없습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}