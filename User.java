package finals;

//Parent Class: Child Classes (Admin, Client, Artist) inherits its own attributes
public class User {
	private String fullname;
	private String username;
	private String password;
	private String role;
	
	public User(String fullname, String username, String password, String role) {
		this.fullname = fullname;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	// Getter Methods
	public String getFullname() {
		return fullname;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getRole() {
		return role;
	}
	
	
	// Setter Methods
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}