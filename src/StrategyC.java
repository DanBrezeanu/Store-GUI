import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

public class StrategyC implements Strategy {
    @Override
    public void execute(Wishlist wishlist) {
        Store store = Store.getInstance("dummy_text");
        ListIterator<Item> it = wishlist.listIterator();
        Item current = null;

        while(it.hasNext())
            current = it.next();


        FileWriter fw = null;


        wishlist.removeNoNotification(current);

        for(Customer c : store.getCustomers())
            if(c.getWishlist() == wishlist)
                c.getShoppingCart().addPlain(current);

        try{
            fw = new FileWriter("output.txt", true);

            fw.write(current.toString() + "\n");
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
