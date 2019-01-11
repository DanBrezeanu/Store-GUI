import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Test {
    private Store store;

    public void parseStoreTxt(File storeTxt) {
        try {
            Scanner storeSc = new Scanner(storeTxt);

            store = Store.getInstance(storeSc.nextLine());

            while (storeSc.hasNextLine()) {
                StringTokenizer tokenizer = new StringTokenizer(storeSc.nextLine(), ";");

                String departToken = tokenizer.nextToken();

                BookDepartment bookDepartment = null;
                MusicDepartment musicDepartment = null;
                SoftwareDepartment softwareDepartment = null;
                VideoDepartment videoDepartment = null;

                if (departToken.equals("BookDepartment")) {
                    bookDepartment = new BookDepartment(Integer.parseInt(tokenizer.nextToken()), departToken);
                    store.addDepartment(bookDepartment);
                }

                if (departToken.equals("MusicDepartment")) {
                    musicDepartment = new MusicDepartment(Integer.parseInt(tokenizer.nextToken()), departToken);
                    store.addDepartment(musicDepartment);
                }

                if (departToken.equals("VideoDepartment")) {
                    videoDepartment = new VideoDepartment(Integer.parseInt(tokenizer.nextToken()), departToken);
                    store.addDepartment(videoDepartment);
                }

                if (departToken.equals("SoftwareDepartment")) {
                    softwareDepartment = new SoftwareDepartment(Integer.parseInt(tokenizer.nextToken()), departToken);
                    store.addDepartment(softwareDepartment);
                }

                Integer numberOfProducts = Integer.parseInt(storeSc.nextLine());

                for (int i = 0; i < numberOfProducts; ++i) {
                    tokenizer = new StringTokenizer(storeSc.nextLine(), ";");

                    Item it = new Item(tokenizer.nextToken(), Integer.parseInt(tokenizer.nextToken()),
                            Double.parseDouble(tokenizer.nextToken()));

                    if (bookDepartment != null)
                        bookDepartment.addItem(it);

                    if (musicDepartment != null)
                        musicDepartment.addItem(it);

                    if (softwareDepartment != null)
                        softwareDepartment.addItem(it);

                    if (videoDepartment != null)
                        videoDepartment.addItem(it);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseCustomersTxt(File customersTxt){
        try{
            Scanner customerSc = new Scanner(customersTxt);

            Integer numberOfCustomers = Integer.parseInt(customerSc.nextLine());

            for(int i = 0; i < numberOfCustomers; ++i){
                StringTokenizer tokenizer = new StringTokenizer(customerSc.nextLine(), ";");

                store.enter(new Customer(tokenizer.nextToken(), Double.parseDouble(tokenizer.nextToken()),
                        tokenizer.nextToken()));
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void parseEventsTxt(){
        try{
            Scanner eventsSc = new Scanner(new File("events.txt"));
            new FileWriter("output.txt");

            Integer numberOfEvents = Integer.parseInt(eventsSc.nextLine());
            StringTokenizer tokenizer;

            for(int i = 0; i < numberOfEvents; ++i){
                tokenizer = new StringTokenizer(eventsSc.nextLine(), ";");

                String event = tokenizer.nextToken();

                Integer itemID; Double itemPrice; String itemName;
                Integer depID; String customerName; String list;
                FileWriter fw = null;

                switch(event){
                    case "addItem":
                        itemID = Integer.parseInt(tokenizer.nextToken());
                        list = tokenizer.nextToken();
                        customerName = tokenizer.nextToken();

                        for(Customer c : store.getCustomers())
                            if(c.getName().equals(customerName))
                                for(Department d : store.getDepartments())
                                    for(Item item : d.getItems())
                                        if(item.getID().equals(itemID)){
                                            if(list.equals("ShoppingCart"))
                                                c.getShoppingCart().add(item);
                                            else if(list.equals("WishList")) {
                                                c.getWishlist().add(item);
                                                d.addObserver(c);
                                            }
                                        }
                        break;

                    case "delItem":
                        itemID = Integer.parseInt(tokenizer.nextToken());
                        list = tokenizer.nextToken();
                        customerName = tokenizer.nextToken();
                        Item refItem = null;

                        for(Department d : store.getDepartments())
                            for(Item itm : d.getItems())
                                if(itm.getID().equals(itemID))
                                    refItem = itm;

                        if(list.equals("ShoppingCart")) {
                            if(store.getCustomer(customerName).getShoppingCart().contains(refItem));
                                store.getCustomer(customerName).getShoppingCart().remove(refItem);
                        }
                        else if(list.equals("WishList")){
                            if(store.getCustomer(customerName).getWishlist().contains(refItem));
                                store.getCustomer(customerName).getWishlist().remove(refItem);
                        }
                        break;

                    case "addProduct":
                        depID = Integer.parseInt(tokenizer.nextToken());

                        for(Department d : store.getDepartments())
                            if(d.getID().equals(depID)) {
                                itemID = Integer.parseInt(tokenizer.nextToken());
                                itemPrice = Double.parseDouble(tokenizer.nextToken());
                                itemName = tokenizer.nextToken();

                                d.addItem(new Item(itemName, itemID, itemPrice));
                            }

                        break;

                    case "modifyProduct":
                        depID = Integer.parseInt(tokenizer.nextToken());

                        for(Department d : store.getDepartments()) {
                            if (d.getID().equals(depID)) {
                                itemID = Integer.parseInt(tokenizer.nextToken());
                                d.modifyItem(itemID, Double.parseDouble(tokenizer.nextToken()));

                                d.notifyAllObservers(new Notification(d.getID(), itemID,
                                        Notification.NotificationType.MODIFY));
                            }
                        }

                        break;

                    case "delProduct":
                        itemID = Integer.parseInt(tokenizer.nextToken());

                        for(Department d : store.getDepartments())
                            for(Item itm : d.getItems())
                                if(itm.getID().equals(itemID))
                                    d.removeItem(itemID);
                        break;

                    case "getItem":
                        customerName = tokenizer.nextToken();

                        for(Customer c : store.getCustomers()){
                            if(customerName.equals(c.getName())) {
                                c.getWishlist().executeStrategy();
                                break;
                            }
                        }
                        break;

                    case "getItems":
                        list = tokenizer.nextToken();
                        customerName = tokenizer.nextToken();

                        for(Customer c : store.getCustomers()){
                            if(c.getName().equals(customerName)){
                                if(list.equals("ShoppingCart")){
                                    try{
                                        fw = new FileWriter("output.txt", true);

                                        fw.write(c.getShoppingCart().toString() + "\n");

                                    }catch(IOException e){
                                        e.printStackTrace();
                                    }finally{
                                        if(fw != null) {
                                            try {
                                                fw.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                else if(list.equals("WishList")){

                                    try{
                                        fw = new FileWriter("output.txt", true);

                                        fw.write(c.getWishlist().toString() + "\n");

                                    }catch(IOException e){
                                        e.printStackTrace();
                                    }finally{
                                        if(fw != null) {
                                            try {
                                                fw.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        break;

                    case "getTotal":
                        list = tokenizer.nextToken();
                        customerName = tokenizer.nextToken();
                        Double result = 0.0;

                        for(Customer c : store.getCustomers()) {
                            if (c.getName().equals(customerName)) {
                                if(list.equals("ShoppingCart"))
                                    result = c.getShoppingCart().getTotalPrice();
                                else if(list.equals("WishList"))
                                    result = c.getWishlist().getTotalPrice();

                                break;
                            }
                        }


                        try{

                            fw = new FileWriter("output.txt", true);
                            fw.write(result + "\n");

                        }catch(IOException e){
                            e.printStackTrace();
                        }finally{
                            if(fw != null) {
                                try {
                                    fw.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case "accept":
                        depID = Integer.parseInt(tokenizer.nextToken());
                        customerName = tokenizer.nextToken();

                        store.getDepartment(depID).accept(store.getCustomer(customerName).getShoppingCart());
                        break;

                    case "getObservers":
                        depID = Integer.parseInt(tokenizer.nextToken());
                        Vector<String> observers = new Vector<>();

                        for(Customer c : store.getDepartment(depID).getObservers())
                            observers.add(c.getName());

                        try{
                            fw = new FileWriter("output.txt", true);

                            fw.write(observers + "\n");

                        }catch(IOException e){
                            e.printStackTrace();
                        }finally{
                            if(fw != null) {
                                try {
                                    fw.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        break;

                    case "getNotifications":
                        customerName =tokenizer.nextToken();
                        Vector<String> notifications = new Vector<>();


                        for(Notification n : store.getCustomer(customerName).getNotifications())
                            if(!notifications.contains(n.toString()))
                            notifications.add(n.toString());

                        try{
                            fw = new FileWriter("output.txt", true);

                            fw.write(notifications + "\n");

                        }catch(IOException e){
                            e.printStackTrace();
                        }finally{
                            if(fw != null) {
                                try {
                                    fw.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        break;

                }
            }


        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Test test = new Test();

        test.parseStoreTxt(new File("store.txt"));
        test.parseCustomersTxt(new File("customers.txt"));
        test.parseEventsTxt();


    }
}
