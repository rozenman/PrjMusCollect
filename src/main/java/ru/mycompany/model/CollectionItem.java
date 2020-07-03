package ru.mycompany.model;

public class CollectionItem {
    private int id;
    private String Title;
    private String Artists;
    private String Composer;
    private String Genre;
    private String Album;
    private String Duration;
    private String File_path;
    private String File_name;

    public CollectionItem(int id, String Title, String Artists, String Composer,
                    String Genre, String Album, String Duration,
                    String File_path, String File_name) {
        this.id = id;
        this.Title = Title;
        this.Artists = Artists;
        this.Composer = Composer;
        this.Genre = Genre;
        this.Album = Album;
        this.Duration = Duration;
        this.File_path = File_path;
        this.File_name = File_name;
    }
    public CollectionItem() {

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        if (Title==null){
            Title = "";
        }
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }

    public String getArtists() {
        if (Artists==null) {
            Artists = "";
        }
        return Artists;
    }
    public void setArtists(String artists) {
        Artists = artists;
    }

    public String getCompose() {
        if (Composer==null){
            Composer="";
        }
        return Composer;
    }
    public void setCompose(String composer) {
        Composer = composer;
    }

    public String getGenre() {
        if (Genre==null){
            Genre="";
        }
        return Genre;
    }
    public void setGenre(String genye) {
        Genre = genye;
    }

    public String getAlbum() {
        if (Album==null){
            Album="";
        }
        return Album;
    }
    public void setAlbum(String album) {
        Album = album;
    }

    public String getDuration() {
        if (Duration==null) {
            Duration = "0";
        }
        return Duration;
    }
    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getFile_path() {
        return File_path;
    }
    public void setFile_path(String file_path) {
        File_path = file_path;
    }

    public String getFile_name() {
        if (File_name==null){
            File_name="";
        }
        return File_name;
    }
    public void setFile_name(String file_name) {
        File_name = file_name;
    }

    public void print(){
        System.out.println("\n***\n"+
                        "ID      :" + id + "\n" +
                        "TITLE   :" +Title + "\n" +
                        "ARTISTS :" + Artists + "\n" +
                        "COMPOSER:" + Composer + "\n" +
                        "GENRE   :" + Genre + "\n" +
                        "ALBUM   :" + Album + "\n" +
                        "DURATION:" + Duration + "\n" +
                        "FILEPATH:" + File_path + "\n" +
                        "FILENAME:" + File_name + "\n" +
                        "***"
        );
    }
}
