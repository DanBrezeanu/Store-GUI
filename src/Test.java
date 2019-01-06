import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

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

                switch(event){
                    case "addItem":
                        break;

                    case "delItem":
                        break;

                    case "addProduct":
                        break;

                    case "modifyProduct":
                        break;

                    case "delProduct":
                        break;

                    case "getItem":
                        break;

                    case "getItems":
                        break;

                    case "getTotal":
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
