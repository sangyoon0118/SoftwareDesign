import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
public class RegisterFrame extends JFrame implements ActionListener {
    JLabel userLabel, passwordLabel, confirmPasswordLabel, telLabel, majorLabel;
    JTextField userText, telText, majorText;
    JPasswordField passwordText, confirmPasswordText;
    JButton registerButton, resetButton;
    public RegisterFrame() {

        setTitle("회원 가입");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        userLabel = new JLabel("학번:");
        passwordLabel = new JLabel("비밀번호:");
        confirmPasswordLabel = new JLabel("비밀번호 확인:");
        telLabel = new JLabel("전화번호:");
        majorLabel = new JLabel("학과:");

        userText = new JTextField(20);
        passwordText = new JPasswordField(20);
        confirmPasswordText = new JPasswordField(20);
        telText = new JTextField(20);
        majorText = new JTextField(20);

        registerButton = new JButton("회원 가입");
        resetButton = new JButton("취소");

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordText);
        panel.add(telLabel);
        panel.add(telText);
        panel.add(majorLabel);
        panel.add(majorText);
        panel.add(registerButton);
        panel.add(resetButton);
        add(panel, BorderLayout.CENTER);

        registerButton.addActionListener(this);
        resetButton.addActionListener(this);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String userid = userText.getText();
            String password = new String(passwordText.getPassword());
            String confirmPassword = new String(confirmPasswordText.getPassword());
            String tel = telText.getText();
            String major = majorText.getText();

            if (userid.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || tel.isEmpty() || major.isEmpty()) {
                JOptionPane.showMessageDialog(this, "입력되지 않은 정보가 있습니다. 모든 필드를 입력해주세요.");
                return;
            }

            if (password.equals(confirmPassword)) {
                try {
                    FileWriter writer = new FileWriter("user.txt", true);
                    writer.write(userid + ", " + password + ", " + tel + ", " + major + "\n");
                    writer.close();
                    JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다!");
                    dispose();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "회원가입에 실패하였습니다. 다시 시도해주세요.");
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다!");
            }
        } else if (e.getSource() == resetButton) {
            userText.setText("");
            passwordText.setText("");
            confirmPasswordText.setText("");
            telText.setText("");
            majorText.setText("");
        }
    }

}


