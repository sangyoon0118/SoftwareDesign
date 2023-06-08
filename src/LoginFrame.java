
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
public class LoginFrame extends JFrame implements ActionListener {
    JLabel userLabel;
    JLabel passwordLabel;
    JTextField userText;
    JPasswordField passwordText;
    JButton loginButton;
    JButton registerButton;

    public LoginFrame() {
        this.setTitle("뭐 먹을까?");
        this.setSize(300, 150);
        this.setLocationRelativeTo((Component)null);
        this.setDefaultCloseOperation(3);
        this.setResizable(false);
        this.userLabel = new JLabel("학번:");
        this.passwordLabel = new JLabel("비밀번호:");
        this.userText = new JTextField(20);
        this.passwordText = new JPasswordField(20);
        this.loginButton = new JButton("로그인");
        this.registerButton = new JButton("회원가입");
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(this.userLabel);
        panel.add(this.userText);
        panel.add(this.passwordLabel);
        panel.add(this.passwordText);
        panel.add(this.loginButton);
        panel.add(this.registerButton);
        this.add(panel, "Center");
        this.loginButton.addActionListener(this);
        this.registerButton.addActionListener(this);
    }

    public HashMap<String, String> UserInformation() throws IOException {
        HashMap<String, String> userMap = new HashMap();
        BufferedReader buffer = new BufferedReader(new FileReader("user.txt"));

        String line;
        while((line = buffer.readLine()) != null) {
            String[] token = line.split(", ");
            userMap.put(token[0], token[1]);
        }

        return userMap;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.loginButton) {
            String user = this.userText.getText();
            String password = new String(this.passwordText.getPassword());
            HashMap<String, String> list = null;

            try {
                list = this.UserInformation();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


            if (list.containsKey(user) && password.equals(list.get(user))) {
                JOptionPane.showMessageDialog(this, "로그인 되었습니다.");
                new RestaurantReviewMainFrame();
            } else if (user.equals("administrator") && password.equals("0000")) {
                UserManage viewer = new UserManage();
                viewer.loadFile("user.txt");
            } else {
                JOptionPane.showMessageDialog(this, "사용자명 또는 비밀번호가 일치하지 않습니다.");
            }
        } else if (e.getSource() == this.registerButton) {
            new RegisterFrame();
        }

    }
}