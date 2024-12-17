public class CompactDisc extends Disc implements Playable {
    private String artist;
    private List<Track> tracks;

    public String getArtist() {
        return artist;
    }

    public CompactDisc(String title, String Category, String director, int length, float cost, String artist, List<Track> tracks) {
        super(title, Category, director, cost);
        this.artist = artist;
        this.tracks = tracks;
    }

    public CompactDisc(String title, String Category, String director, int length, float cost) {
        super(title, Category, director, cost);
        this.tracks = new ArrayList<>();
    }

    public CompactDisc(String title) {
        super(title);
        this.tracks = new ArrayList<>();
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void play() {
        System.out.println("Playing CD: " + getTitle() + ", Tracks: ");
        for (Track track : tracks) {
            track.play();
        }
    }

   public String toString() {
        return "CD - " + super.toString() + (getDirector() != null ? (" - Director: " + getDirector()) : "") + (artist != null ? (" - Artist: " + artist) : "");
    }


    public void removeTrack(Track track) {
        tracks.removeIf(t -> t.equals(track));
    }

    public void addTrack(Track track) {
        if(tracks.stream().noneMatch(t -> t.equals(track))){
             tracks.add(track);
        }
    }
}

