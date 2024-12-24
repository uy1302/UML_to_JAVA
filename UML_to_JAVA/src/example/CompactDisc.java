public class CompactDisc extends Disc implements Playable {
    private String artist;
    private List<Track> track;

    public String getArtist() {
        return artist;
    }

    public CompactDisc(String title, String Category, String director, int length, float cost, String artist, List<Track> tracks) {
        super(title, Category, director, cost);
        this.artist = artist;
        this.track = tracks;
    }

     public CompactDisc(String title, String Category, String director, int length, float cost) {
         super(title, Category, director, cost);
         this.track = new ArrayList<>();
    }

    public CompactDisc(String title) {
        super(title);
        this.track = new ArrayList<>();
    }

    public List<Track> getTracks() {
        return track;
    }


    @Override
    public void play() {
        System.out.println("Playing CD: " + getTitle());
        if (track != null) {
            for (Track t : track) {
                t.play();
            }
        }
    }

    @Override
    public String toString() {
        return "CompactDisc{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\\'' +
                ", category='" + getCategory() + '\\'' +
                ", cost=" + getCost() +
                 ", director='" + getDirector() + '\\'' +
                ", artist='" + artist + '\\'' +
                ", track=" + track +
                '}';
    }

    public void removeTrack(Track track) {
        this.track.removeIf(t -> t.equals(track));
    }

    public void addTrack(Track track) {
        boolean contains = false;
        for(Track t : this.track) {
             if(t.equals(track)) {
                 contains = true;
             }
        }
        if(!contains)
          this.track.add(track);
    }
}
