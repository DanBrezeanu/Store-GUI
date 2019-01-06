import java.io.FileWriter;
import java.io.IOException;
import java.util.ListIterator;

public class StrategyA implements Strategy {
    @Override
    public void execute(Wishlist wishlist) {
        ListIterator<Item> it = wishlist.listIterator();
        Double minPrice = Double.MAX_VALUE;
        Item minItem = null, current;

        while(it.hasNext()){
            current = it.next();
            if(current.getPrice() < minPrice){
                minPrice = current.getPrice();
                minItem = current;
            }
        }

        FileWriter fw = null;

        try{
          fw = new FileWriter("results.txt", true);

          fw.write(minItem.toString() + "\n");
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
