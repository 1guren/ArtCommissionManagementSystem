package finals;

public class UserFactory {
	public static User createUser(String role, String fullname, String username, String password) {
		if (role.equals("Admin")) {
			return new Admin(fullname, username, password);
		} else if (role.equals("Client")) {
			return new Client(fullname, username, password);
		} else if (role.equals("Artist")) {
			return new Artist(fullname, username, password);
		}
		return null;
	}
}