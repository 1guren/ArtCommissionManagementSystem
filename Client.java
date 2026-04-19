package finals;

import java.util.ArrayList;

// Inheritance: Client (Child Class) inherits the attributes of User (Parent Class)
public class Client extends User {

    // Composition: Client "owns" its list of commission requests
    private ArrayList<CommissionRequest> clientRequests;

    public Client(String fullname, String username, String password) {
        super(fullname, username, password, "Client");
        this.clientRequests = new ArrayList<>();
    }

    // Adding a Commission Request
    public void addRequest(CommissionRequest request) {
        clientRequests.add(request);
    }

    // Removing a Commission Request
    public void removeRequest(CommissionRequest request) {
        clientRequests.remove(request);
    }

    // Getter Method for Client Commission Requests
    public ArrayList<CommissionRequest> getClientRequests() {
        return this.clientRequests;
    }
}