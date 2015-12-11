package database;
public class BookDetails implements Comparable {
    private String id=null;         //id            varchar(10)
    private String t_name=null;      //t_name         varchar(64)
    private String image_url=null;     //image_url        varchar(120)
    private float price=0.0F;       //price         float
    private String description=null;//description   varchar(1200)
    private int inventory=0;        //inventory     int
    public BookDetails() {}
    public BookDetails(String id, String t_name, 
        float price, int inventory, String image_url, String description) {
        this.id = id;
        this.t_name = t_name;
        this.image_url = image_url;
        this.price = price;
        this.description = description;
        this.inventory = inventory;
    }
    public String getId() {return id;}
    public String getShirtName() {return t_name;}
    public String getImageUrl() {return image_url;}
    public float getPrice() {return price;}
    public String getDescription() {return description;}
    public int getInventory() {return inventory;}

    public void setId(String id) {this.id = id;}
    public void setShirtName(String t_name) {this.t_name = t_name;}
    public void setImageUrl(String image_url) {this.image_url = image_url; }
    public void setPrice(float price) {this.price = price; }
    public void setDescription(String description) {this.description = description; }
    public void setInventory(int inventory) {this.inventory = inventory;}
    public int compareTo(Object o) {
        return t_name.compareTo(((BookDetails) o).t_name);
    }
}
