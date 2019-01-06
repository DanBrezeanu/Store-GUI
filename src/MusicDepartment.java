import java.util.ListIterator;

public class MusicDepartment extends Department {

    MusicDepartment(Integer ID, String name) {
        super(ID, name);
    }

    @Override
    public void accept(ShoppingCart shoppingCart) {
        ListIterator<Item> it = shoppingCart.listIterator();

        Double totalToAdd = 0.0;

        while(it.hasNext()){
            Item current = it.next();
            if(this.getItems().contains(current))
                totalToAdd += current.getPrice() * 0.1;
        }

        shoppingCart.setBudget(shoppingCart.getBudget() + totalToAdd);
    }

    @Override
    public void accept(Visitor visitor) {

    }
}
