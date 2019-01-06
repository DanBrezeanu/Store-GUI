import java.util.Vector;

public abstract class Department implements Subject{
    private String name;
    private Vector<Item> items;
    private Vector<Customer> customers;
    private Vector<Customer> observers;
    private Integer ID;

    Department(Integer ID, String name){
        items = new Vector<Item>();
        customers = new Vector<Customer>();
        observers = new Vector<Customer>();
        this.ID = ID;
        this.name = name;
    }

    public void enter(Customer c){
        customers.add(c);
    }

    public void exit(Customer c){
        customers.remove(c);
    }

    public Vector<Customer> getCustomers() {
        return customers;
    }

    public Integer getID() {
        return ID;
    }

    public Vector<Item> getItems() {
        return items;
    }

    public void addItem(Item newItem){
        items.add(newItem);
    }

    public void modifyItem(Integer itemID, Double newPrice){
        for(Item i : items)
            if(i.getID().equals(itemID)) {
                i.setPrice(newPrice);
                break;
            }
    }

    public void addObserver(Customer newObserver){
        observers.add(newObserver);
    }

    public void removeObserver(Customer observer){
        observers.remove(observer);
    }

    public void notifyAllObservers(Notification notification){
        for(Customer observer : observers)
            observer.update(notification);
    }

    public abstract void accept(ShoppingCart shoppingCart);

    public abstract void accept(Visitor visitor);
}
