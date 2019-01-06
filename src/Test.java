import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Test {
    private Store store;

    private void parseStoreTxt() {
        try {
            Scanner storeSc = new Scanner(new File("store.txt"));

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

    private void parseCustomersTxt(){
        try{
            Scanner customerSc = new Scanner(new File("customers.txt"));

            Integer numberOfCustomers = Integer.parseInt(customerSc.nextLine());

            for(int i = 0; i < numberOfCustomers; ++i){
                StringTokenizer tokenizer = new StringTokenizer(customerSc.nextLine(), ";");

                store.addCustomer(new Customer(tokenizer.nextToken(), Double.parseDouble(tokenizer.nextToken()),
                        tokenizer.nextToken()));
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void parseEventsTxt(){
        try{
            Scanner eventsSc = new Scanner(new File("events.txt"));

            Integer numberOfEvents = Integer.parseInt(eventsSc.nextLine());
            StringTokenizer tokenizer;

            for(int i = 0; i < numberOfEvents; ++i){
                tokenizer = new StringTokenizer(eventsSc.nextLine(), ";");

                String event = tokenizer.nextToken();

                Integer itemID; Double itemPrice; String itemName;
                Integer depID; String customerName; String list;

                switch(event){
                    case "addItem":
                        itemID = Integer.parseInt(tokenizer.nextToken());

                        //TODO:
                        break;

                    case "delItem":
                        //TODO:
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
                        //TODO:
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
                                    FileWriter fw = null;

                                    try{
                                        fw = new FileWriter("results.txt", true);

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
                                    FileWriter fw = null;

                                    try{
                                        fw = new FileWriter("results.txt", true);

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
                                    result = c.getShoppingCart().getTotalPrice();

                                break;
                            }
                        }
                        FileWriter fw = null;

                        try{
                            fw = new FileWriter("results.txt", true);

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
                        break;

                    case "getObservers":
                        break;

                    case "getNotifications":
                        break;

                }
            }


        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Test test = new Test();

        test.parseStoreTxt();
        test.parseCustomersTxt();
        test.parseEventsTxt();
    }
}
