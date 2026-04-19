package finals;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.UIManager;

import java.awt.event.*;

public class LoginForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

	public LoginForm() {
		setTitle("User Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 280);
		setLocationRelativeTo(null);
        setResizable(false);
        
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Art Commission Management System");
		lblNewLabel.setFont(new Font("Arial Black", Font.BOLD, 13));
		lblNewLabel.setBounds(53, 20, 280, 27);
		contentPane.add(lblNewLabel);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Arial Black", Font.BOLD, 11));
		lblUsername.setBounds(53, 57, 75, 17);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Arial Black", Font.BOLD, 11));
		lblPassword.setBounds(53, 93, 75, 17);
		contentPane.add(lblPassword);
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 11));
		txtUsername.setBounds(136, 54, 197, 19);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 11));
		txtPassword.setColumns(10);
		txtPassword.setBounds(136, 91, 197, 19);
		contentPane.add(txtPassword);
		
		btnLogin = new JButton("Login");
		btnLogin.setBackground(new Color(0, 102, 153));
		btnLogin.setForeground(new Color(255, 255, 255));
		btnLogin.setFont(new Font("Arial Black", Font.BOLD, 11));
		btnLogin.setBounds(53, 125, 280, 30);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		contentPane.add(btnLogin);
		
		btnRegister = new JButton("Register");
		btnRegister.setFont(new Font("Arial Black", Font.BOLD, 11));
		btnRegister.setBounds(53, 165, 280, 30);
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RegisterForm().setVisible(true);;
			}
		});
		contentPane.add(btnRegister);
	}
	
	private void login() {
		String username = txtUsername.getText().trim();
		String password = new String(txtPassword.getPassword()).trim();
		
		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		User user = SystemManager.getInstance().login(username, password);
		
		if (user == null) {
			JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		switch (user.getRole()) {
			case "Admin":
				new AdminPanel().setVisible(true);
				dispose();
				break;
			case "Client":
				new ClientPanel().setVisible(true);
				dispose();
				break;
			case "Artist":
				new ArtistPanel().setVisible(true);
				dispose();
				break;
		}
	}
}