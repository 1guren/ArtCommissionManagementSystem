package finals;

import java.util.ArrayList;

// Singleton Pattern: only one instance of SystemManager exists
public class SystemManager {

    private static SystemManager instance;

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Artwork> artworks = new ArrayList<>();
    private ArrayList<CommissionRequest> commissions = new ArrayList<>();

    // Observer Pattern: list of observers that get notified on changes
    private ArrayList<CommissionObserver> observers = new ArrayList<>();

    private User loggedInUser = null;

    private SystemManager() {
        loadDummyData();
    }

    public static SystemManager getInstance() {
        if (instance == null) {
            instance = new SystemManager();
        }
        return instance;
    }

    // -------------------------------------------------------
    // Observer Pattern Methods
    // -------------------------------------------------------

    public void addObserver(CommissionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CommissionObserver observer) {
        observers.remove(observer);
    }

    // Notifies all registered observers when a commission changes
    private void notifyObservers(CommissionRequest request) {
        for (CommissionObserver observer : observers) {
            observer.onCommissionUpdated(request);
        }
    }

    // -------------------------------------------------------
    // Dummy Data
    // -------------------------------------------------------

    private void loadDummyData() {
        // Admin account
        User admin = UserFactory.createUser("Admin", "Administrator", "admin", "admin123");
        users.add(admin);

        // Artist accounts
        Artist artist1 = (Artist) UserFactory.createUser("Artist", "Sofia Reyes", "sofia", "pass1234");
        Artist artist2 = (Artist) UserFactory.createUser("Artist", "Marco Lim", "marco", "pass1234");
        Artist artist3 = (Artist) UserFactory.createUser("Artist", "Hana Dela Cruz","hana", "pass1234");
        users.add(artist1);
        users.add(artist2);
        users.add(artist3);

        // Client accounts
        Client client1 = (Client) UserFactory.createUser("Client", "James Santos",  "james", "pass1234");
        Client client2 = (Client) UserFactory.createUser("Client", "Anna Cruz", "anna", "pass1234");
        Client client3 = (Client) UserFactory.createUser("Client", "Leo Garcia", "leo", "pass1234");
        users.add(client1);
        users.add(client2);
        users.add(client3);

        // Artworks for artists
        Artwork aw1 = new Artwork("Cherry Blossom Dreams", "Anime", "sofia");
        Artwork aw2 = new Artwork("Warrior of Light", "Anime", "sofia");
        Artwork aw3 = new Artwork("Sunset Portrait", "Realism", "marco");
        Artwork aw4 = new Artwork("City at Night", "Digital Art", "marco");
        Artwork aw5 = new Artwork("Family Memory", "Portrait", "hana");
        Artwork aw6 = new Artwork("Ocean Breeze", "Realism", "hana");

        artworks.add(aw1); artist1.addArtwork(aw1);
        artworks.add(aw2); artist1.addArtwork(aw2);
        artworks.add(aw3); artist2.addArtwork(aw3);
        artworks.add(aw4); artist2.addArtwork(aw4);
        artworks.add(aw5); artist3.addArtwork(aw5);
        artworks.add(aw6); artist3.addArtwork(aw6);

        // Commission requests
        CommissionRequest cr1 = new CommissionRequest("james", "sofia", "Anime", "Draw my OC in anime style",       1500.00, "2025-08-01");
        CommissionRequest cr2 = new CommissionRequest("anna", "marco", "Realism", "Portrait of my grandmother",      2000.00, "2025-07-15");
        CommissionRequest cr3 = new CommissionRequest("leo", "hana", "Portrait", "Family portrait for anniversary", 2500.00, "2025-09-10");
        CommissionRequest cr4 = new CommissionRequest("james", "marco", "Digital Art", "Logo design for my band",         1200.00, "2025-08-20");
        CommissionRequest cr5 = new CommissionRequest("anna", "sofia", "Anime", "Chibi version of my character",    800.00, "2025-07-30");

        cr2.setStatus("Accepted");
        cr3.setStatus("Accepted");
        cr4.setStatus("Declined");

        commissions.add(cr1); client1.addRequest(cr1);
        commissions.add(cr2); client2.addRequest(cr2);
        commissions.add(cr3); client3.addRequest(cr3);
        commissions.add(cr4); client1.addRequest(cr4);
        commissions.add(cr5); client2.addRequest(cr5);
    }

    // -------------------------------------------------------
    // User Management (CRUD)
    // -------------------------------------------------------

    public void addUser(User user) {
        users.add(user);
    }

    // FIX: original had broken if-statement (semicolon after condition, no braces)
    public boolean removeUser(String username) {
        User toRemove = null;
        for (User user : users) {
            if (user.getUsername().equals(username) && !user.getRole().equalsIgnoreCase("Admin")) {
                toRemove = user;
                break;
            }
        }
        if (toRemove != null) {
            users.remove(toRemove);
            return true;
        }
        return false;
    }

    public boolean updateUser(String username, String newFullname, String newPassword) {
        User user = findUser(username);
        if (user != null && !user.getRole().equalsIgnoreCase("Admin")) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }

    public User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean usernameExists(String username) {
        return findUser(username) != null;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<User> getArtists() {
        ArrayList<User> artists = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equalsIgnoreCase("Artist")) {
                artists.add(user);
            }
        }
        return artists;
    }

    public ArrayList<User> getClients() {
        ArrayList<User> clients = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equalsIgnoreCase("Client")) {
                clients.add(user);
            }
        }
        return clients;
    }

    // -------------------------------------------------------
    // User Session
    // -------------------------------------------------------

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedInUser = user;
                return user;
            }
        }
        return null;
    }

    public User getLoggedInUser() {
    	return loggedInUser;
    }
    public void logout() {
    	loggedInUser = null;
    }

    // -------------------------------------------------------
    // Artwork Management (CRUD)
    // -------------------------------------------------------

    public void addArtwork(Artwork artwork) {
        artworks.add(artwork);
        User user = findUser(artwork.getArtistUsername());
        if (user instanceof Artist) {
            ((Artist) user).addArtwork(artwork);
        }
    }

    public boolean removeArtwork(String title, String artistUsername) {
        Artwork found = null;
        for (Artwork art : artworks) {
            if (art.getTitle().equals(title) && art.getArtistUsername().equals(artistUsername)) {
                found = art;
                break;
            }
        }
        if (found != null) {
            artworks.remove(found);
            User user = findUser(artistUsername);
            if (user instanceof Artist) {
                ((Artist) user).removeArtwork(found);
            }
            return true;
        }
        return false;
    }

    public ArrayList<Artwork> getArtworks() { return artworks; }

    public ArrayList<Artwork> getArtworksByArtist(String username) {
        ArrayList<Artwork> result = new ArrayList<>();
        for (Artwork art : artworks) {
            if (art.getArtistUsername().equals(username)) {
                result.add(art);
            }
        }
        return result;
    }

    // -------------------------------------------------------
    // Commission Management (CRUD)
    // -------------------------------------------------------

    public void addCommission(CommissionRequest request) {
        commissions.add(request);
        User client = findUser(request.getClientUsername());
        if (client instanceof Client) {
            ((Client) client).addRequest(request);
        }
        notifyObservers(request); // Observer Pattern: notify on new commission
    }

    public boolean removeCommission(CommissionRequest request) {
        boolean removed = commissions.remove(request);
        if (removed) {
            notifyObservers(request); // Observer Pattern: notify on removal
        }
        return removed;
    }

    public void updateCommissionStatus(CommissionRequest request, String newStatus) {
        request.setStatus(newStatus);
        notifyObservers(request); // Observer Pattern: notify on status change
    }

    public ArrayList<CommissionRequest> getCommissions() { return commissions; }

    public ArrayList<CommissionRequest> getCommissionsByArtist(String username) {
        ArrayList<CommissionRequest> result = new ArrayList<>();
        for (CommissionRequest request : commissions) {
            if (request.getArtistUsername().equals(username)) {
                result.add(request);
            }
        }
        return result;
    }

    public ArrayList<CommissionRequest> getCommissionsByClient(String username) {
        ArrayList<CommissionRequest> result = new ArrayList<>();
        for (CommissionRequest request : commissions) {
            if (request.getClientUsername().equals(username)) {
                result.add(request);
            }
        }
        return result;
    }
}