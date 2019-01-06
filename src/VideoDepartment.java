import java.util.ListIterator;

public class VideoDepartment extends Department {

    VideoDepartment(Integer ID, String name) {
        super(ID, name);
    }

    @Override
    public void accept(ShoppingCart shoppingCart) {
        ListIterator<Item> it = shoppingCart.listIterator();

        Double totalDepPrices = 0.0;
        Double maxPrice = 0.0;

        while(it.hasNext()){
            Item current = it.next();
            if(this.getItems().contains(current))
                totalDepPrices += current.getPrice();
        }

        for(Item item : this.getItems())
            if(item.getPrice() > maxPrice)
                maxPrice = item.getPrice();

        if(totalDepPrices > maxPrice){
            it = shoppingCart.listIterator();

            while(it.hasNext()){
                Item current = it.next();
                if(this.getItems().contains(current))
                    current.setPrice(current.getPrice() - current.getPrice() * 0.15);
            }
        }

        shoppingCart.setBudget(shoppingCart.getBudget() + 0.05 * totalDepPrices);
    }

    @Override
    public void accept(Visitor visitor) {

    }
}
