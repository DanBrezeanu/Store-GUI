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
    }

    public void modifyItem(Integer itemID, Double newPrice){
        Store store = Store.getInstance("dummy_text");

        for(Item i : items)
            if(i.getID().equals(itemID)) {
                i.setPrice(newPrice);
                break;
            }

//        for(Customer c : store.getCustomers()){
//            ListIterator<Item> it = c.getShoppingCart().listIterator();
//
//            while(it.hasNext()){
//                Item currentItem = it.next();
//
//                if(currentItem.getID() == itemID)
//
//            }
//        }
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

    public abstract void accept(Visitor visitor);
}
