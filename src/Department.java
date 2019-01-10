import java.util.ListIterator;
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

    public String getName() {
        return name;
    }

    public Vector<Item> getItems() {
        return items;
    }

    public void addItem(Item newItem){
        items.add(newItem);

        this.notifyAllObservers(new Notification(this.ID, newItem.getID(), Notification.NotificationType.ADD));
    }

    public void modifyItem(Integer itemID, Double newPrice) {
        Store store = Store.getInstance("dummy_text");
        Item refItem = null;


        for (Item i : items)
            if (i.getID().equals(itemID)) {
                i.setPrice(newPrice);
                refItem = i;
                this.notifyAllObservers(new Notification(this.ID, itemID, Notification.NotificationType.MODIFY));
                break;
            }

        if (refItem == null) {
            System.out.println("WRONG ID TO MODIFY");
        }


        for (Customer c : store.getCustomers()) {
            if (c.getWishlist().contains(refItem))
                c.getWishlist().getItem(refItem).setPrice(newPrice);

            if (c.getShoppingCart().contains(refItem)) {
                c.getShoppingCart().getItem(refItem).setPrice(newPrice);
                c.getShoppingCart().setBudget(c.getShoppingCart().getTotalPrice());

            }
        }

    }

    public Item removeItem(Integer itemID){
        Store store = Store.getInstance("dummy_text");
        Item refItem = null;

        for(Item i : this.items)
            if(i.getID().equals(itemID)){
                Notification deleteNotification = new Notification(this.ID, itemID, Notification.NotificationType.REMOVE);
                refItem = i;
                this.notifyAllObservers(deleteNotification);
            }

        if(refItem == null) {
            System.out.print("WRONG ID TO DELETE");
            return null;
        }


        for(Customer c : store.getCustomers()){
            if(c.getWishlist().contains(refItem))
                c.getWishlist().remove(refItem);

            if(c.getShoppingCart().contains(refItem)) {
                c.getShoppingCart().remove(refItem);
                c.getShoppingCart().setBudget(c.getShoppingCart().getTotalPrice());

            }
        }
        return refItem;
    }

    public void addObserver(Customer newObserver){
        if(!observers.contains(newObserver))
            observers.add(newObserver);
    }

    public void removeObserver(Customer observer){
        observers.remove(observer);
    }

    public void notifyAllObservers(Notification notification){
        for(Customer observer : observers)
            observer.update(notification);
    }

    public Vector<Customer> getObservers(){
        return this.observers;
    }

    public abstract void accept(Visitor visitor);
}
