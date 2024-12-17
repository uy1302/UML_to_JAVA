abstract class Media {
    private int id;
    private String title;
    private String category;
    private float cost;
    private static int nbMedia = 0;

    public Media(String title) {
        this.title = title;
        this.id = nbMedia++;
    }

    public Media(String title, String category, float cost) {
        this.title = title;
        this.category = category;
        this.cost = cost;
         this.id = nbMedia++;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public float getCost() {
        return cost;
    }

    public boolean equals(Media other) {
        return this.title.equals(other.title);
    }
    
    public String toString(){
        return "ID: " + id + " - Title: " + title + (category != null ? (" - Category: " + category) : "")+  (cost > 0 ? (" - Cost: " + cost) : "");
    }
}


