package mytunes.BE;

public class Playlist {

    private int id, orderID;
    private String name;

    public Playlist(int id, String name, int orderID) {
        this.id = id;
        this.name = name;
        this.orderID = orderID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOrderID() {
        return orderID;
    }
}
