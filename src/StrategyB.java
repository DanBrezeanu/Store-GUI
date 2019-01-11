import java.io.FileWriter;
import java.io.IOException;
import java.util.ListIterator;

public class StrategyB implements Strategy {

    @Override
    public void execute(Wishlist wishlist) {
        ListIterator<Item> it = wishlist.listIterator();
        String minString = null;
        Item minItem = null, current;

        while(it.hasNext()){
            current = it.next();
            if(minString == null){
                minString = current.getName();
                minItem = current;
                continue;
            }

            if(current.getName().compareTo(minString) < 0){
                minString = current.getName();
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
