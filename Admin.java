package finals;

// Inheritance: Admin (Child Class) inherents the attributes of User (Parent Class)
public class Admin extends User {
	
	public Admin(String fullname, String username, String password) {
		super(fullname, username, password, "Admin");
	}
}