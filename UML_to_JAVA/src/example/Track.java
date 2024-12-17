public class Track implements Playable {
    private String title;
    private int length;

    public Track(String title, int length) {
        this.title = title;
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public boolean equals(Track track) {
        return this.title.equals(track.title);
    }

    public void play() {
        System.out.println("Playing Track: " + title + ", Length: " + length);
    }
}

