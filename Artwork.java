package finals;

//Aggregation
public class Artwork {
	private String title;
	private String category;
	private String artistUsername;
	
	public Artwork(String title, String category, String artistUsername) {
		this.title = title;
		this.category = category;
		this.artistUsername = artistUsername;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getArtistUsername() {
		return artistUsername;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return "Title: " + title + " | " + category + " | by " + artistUsername;
	}
}