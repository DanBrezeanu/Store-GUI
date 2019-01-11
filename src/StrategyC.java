import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

public class StrategyC implements Strategy {
    @Override
    public void execute(Wishlist wishlist) {
        ListIterator<Item> it = wishlist.listIterator();
        Item current = null;

        while(it.hasNext())
            current = it.next();


        FileWriter fw = null;

        try{
            fw = new FileWriter("results.txt", true);

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
