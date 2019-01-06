import java.util.ListIterator;

public class BookDepartment extends Department{

    BookDepartment(Integer ID, String name){
        super(ID, name);
    }

    @Override
    public void accept(ShoppingCart shoppingCart) {
        ListIterator<Item> it = shoppingCart.listIterator();

        while(it.hasNext()){
            Item current = it.next();
            if(this.getItems().contains(current))
                current.setPrice(current.getPrice() - current.getPrice() * 0.1);
                it.set(current);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        //TODO: THIS
    }
}
