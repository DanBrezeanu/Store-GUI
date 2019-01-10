public class Item {
    private String name;
    private Integer ID;
    private Double price;

    Item(){
        this.name = "";
        this.ID = 0;
        this.price = 0.0;
    }

    Item(String name, Integer ID, Double price){
        this.name = name;
        this.ID = ID;
        this.price = price;
    }

    public Item(Item i) {
        this.name = i.name;
        this.ID = i.ID;
        this.price = i.price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setPrice(Double price){
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Integer getID() {
        return ID;
    }

    public Double getPrice() {
        return price;
    }

    public boolean equals(Item obj) {
        return (obj.getID().equals(ID));
    }

    public String toString(){
        return this.name + ";" + this.ID + ";" + this.price;
    }
}
