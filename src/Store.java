import java.util.Vector;

public class Store {
    private String name;
    private Vector<Department> departments;
    private Vector<Customer> customers;
    private static Store storeInstance = null;

//    private Store(){
//        this.name = "";
//        this.departments = new Vector<Department>();
//        this.customers = new Vector<Customer>();
//    }

    private Store(String name){
        this.name = name;
        this.departments = new Vector<Department>();
        this.customers = new Vector<Customer>();
    }

    public static Store getInstance(String name){
        if(storeInstance == null)
            storeInstance = new Store(name);

        return storeInstance;
    }

    public String getName(){
        return name;
    }

    public void enter(Customer c){
        customers.add(c);
    }

    public void exit(Customer c){
        customers.remove(c);
    }

    public ShoppingCart getShoppingCart(Double budget){
        return new ShoppingCart(budget);
    }

    public Vector<Customer> getCustomers() {
        return customers;
    }

    public Vector<Department> getDepartments() {
        return departments;
    }

    public void addDepartment(Department dep){
        departments.add(dep);
    }

    public void addCustomer(Customer customer){
        customers.add(customer);
    }

    public Customer getCustomer(String name){
        for(Customer c : customers)
            if(c.getName().equals(name))
                return c;

        return null;
    }

    public Department getDepartment(Integer depID){
        for(Department i : departments)
            if(i.getID().equals(depID))
                return i;

        return null;
    }
}
