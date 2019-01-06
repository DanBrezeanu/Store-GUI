import java.util.Collection;
import java.util.Vector;

public class Customer implements Observer{
    private String name;
    private ShoppingCart shoppingCart;
    private Wishlist wishlist;
    private Collection<Notification> notifications;

    public Customer(String name, Double budget, String strategy) {
        this.name = name;
        this.shoppingCart = Store.getInstance("dummy_text").getShoppingCart(budget);

        if(strategy.equals("A"))
            wishlist = new Wishlist("A");

        if(strategy.equals("B"))
            wishlist = new Wishlist("B");

        if(strategy.equals("C"))
            wishlist = new Wishlist("C");


        notifications = new Vector<Notification>();
    }

    public String getName() {
        return name;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public Collection<Notification> getNotifications() {
        return notifications;
    }

    @Override
    public void update(Notification notification) {
        notifications.add(notification);
    }
}
