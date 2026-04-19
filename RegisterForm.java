package finals;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.Color;

public class RegisterForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtFullname;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JComboBox cbxRole;
	private JButton btnRegister, btnCancel;

	public RegisterForm() {
		setTitle("Account Registration");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		setLocationRelativeTo(null);
        setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Create Account");
		lblNewLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
		lblNewLabel.setBounds(128, 18, 126, 18);
		contentPane.add(lblNewLabel);
		
		JLabel lblFullname = new JLabel("Full Name:");
		lblFullname.setFont(new Font("Arial Black", Font.PLAIN, 11));
		lblFullname.setBounds(60, 48, 75, 13);
		contentPane.add(lblFullname);
		
		txtFullname = new JTextField();
		txtFullname.setFont(new Font("Arial", Font.PLAIN, 11));
		txtFullname.setColumns(10);
		txtFullname.setBounds(138, 46, 170, 19);
		contentPane.add(txtFullname);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Arial Black", Font.PLAIN, 11));
		lblUsername.setBounds(60, 80, 75, 13);
		contentPane.add(lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 11));
		txtUsername.setBounds(138, 78, 170, 19);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Arial Black", Font.PLAIN, 11));
		lblPassword.setBounds(60, 113, 75, 13);
		contentPane.add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 11));
		txtPassword.setColumns(10);
		txtPassword.setBounds(138, 111, 170, 19);
		contentPane.add(txtPassword);
		
		JLabel lblRole = new JLabel("Role:");
		lblRole.setFont(new Font("Arial Black", Font.PLAIN, 11));
		lblRole.setBounds(60, 149, 75, 13);
		contentPane.add(lblRole);
		
		cbxRole = new JComboBox();
		cbxRole.setFont(new Font("Arial Unicode MS", Font.PLAIN, 13));
		cbxRole.setModel(new DefaultComboBoxModel(new String[] {"Client", "Artist"}));
		cbxRole.setBounds(138, 146, 170, 21);
		contentPane.add(cbxRole);
		
		btnRegister = new JButton("Register");
		btnRegister.setBackground(new Color(51, 153, 102));
		btnRegister.setForeground(new Color(255, 255, 255));
		btnRegister.setFont(new Font("Arial Black", Font.BOLD, 11));
		btnRegister.setBounds(60, 186, 248, 30);
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				register();				
			}
		});
		contentPane.add(btnRegister);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Arial Black", Font.BOLD, 11));
		btnCancel.setBounds(60, 221, 248, 30);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();				
			}
		});
		contentPane.add(btnCancel);
	}
	
	private void register() {
		String fullname = txtFullname.getText().trim();
		String username = txtUsername.getText().trim();
		String password = new String(txtPassword.getPassword()).trim();
		String role = (String) cbxRole.getSelectedItem();
		
		if (fullname.isEmpty() || username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
		
		int response = JOptionPane.showConfirmDialog(this, "Proceed creating the account?", "Confirm", JOptionPane.YES_NO_OPTION);
		
		if (response == 0) {
			SystemManager system = SystemManager.getInstance();
			
			if (system.usernameExists(username)) {
				JOptionPane.showMessageDialog(this, "Username already taken.", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
			}
			
			User newUser = UserFactory.createUser(role, fullname, username, password);
			system.addUser(newUser);
			
			JOptionPane.showMessageDialog(this, "Account created! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
	}
}