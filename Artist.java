package finals;

import java.util.ArrayList;

//Inheritance: Artist (Child Class) inherents the attributes of User (Parent Class)
public class Artist extends User {
	// Aggregation: Artist "has" Artworks
	private ArrayList<Artwork> artworks;

	public Artist(String fullname, String username, String password) {
		super(fullname, username, password, "Artist");
		this.artworks = new ArrayList<>();
	}
	
	/*
	 * Adding and Removing Artworks
	 */
	public void addArtwork(Artwork artwork) {
		artworks.add(artwork);
	}
	
	public void removeArtwork(Artwork artwork) {
		artworks.remove(artwork);
	}
	
	// Getter Method for Artworks
	public ArrayList<Artwork> getArtworks() {
		return artworks;
	}
}