import java.util.ListIterator;

public class SoftwareDepartment extends Department {

    SoftwareDepartment(Integer ID, String name) {
        super(ID, name);
    }

    @Override
    public void accept(ShoppingCart shoppingCart) {

        Double minPrice = Double.MAX_VALUE;

        for(Item item : this.getItems())
            if(item.getPrice() < minPrice)
                minPrice = item.getPrice();

        if(shoppingCart.getBudget() < minPrice) {
            ListIterator<Item> it = shoppingCart.listIterator();

            while (it.hasNext()) {
                Item current = it.next();
                if (this.getItems().contains(current))
                    current.setPrice(current.getPrice() - current.getPrice() * 0.2);
                it.set(current);
            }
        }
    }

    @Override
    public void accept(Visitor visitor) {

    }
}
