package finals;

// Composition: CommissionRequest "owns" CommissionDetails
public class CommissionRequest {

    private String clientUsername;
    private String artistUsername;
    private String category;
    private String description;
    private double budget;
    private String deadline;
    private String status;

    private CommissionDetails details;
    
    public CommissionRequest(String clientUsername, String artistUsername, String category,
                             String description, double budget, String deadline) {
        this.clientUsername = clientUsername;
        this.artistUsername = artistUsername;
        this.category = category;
        this.description = description;
        this.budget = budget;
        this.deadline = deadline;
        this.status = "Pending";
        this.details = new CommissionDetails(description, budget, deadline);
    }

    // Getter Methods
    public String getClientUsername() { return clientUsername; }
    public String getArtistUsername()  { return artistUsername; }
    public String getCategory() { return category; }
    public String getDescription()  { return description; }
    public double getBudget() { return budget; }
    public String getDeadline() { return deadline; }
    public String getStatus() { return status; }
    public CommissionDetails getDetails() { return details; }

    // FIX: setStatus was missing — Artist needs this to Accept/Decline
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return clientUsername + " -> " + artistUsername + " | " + category + " | " + status;
    }

    // Inner Class (Composition)
    public class CommissionDetails {
        private String description;
        private double budget;
        private String deadline;

        public CommissionDetails(String description, double budget, String deadline) {
            this.description = description;
            this.budget = budget;
            this.deadline = deadline;
        }

        public String getDescription() { return description; }
        public double getBudget() { return budget; }
        public String getDeadline() { return deadline; }
    }
}
