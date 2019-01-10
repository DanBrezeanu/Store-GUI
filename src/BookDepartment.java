import java.util.ListIterator;

public class BookDepartment extends Department{

    BookDepartment(Integer ID, String name){
        super(ID, name);
    }

    @Override
    public void accept(ShoppingCart shoppingCart) {
        shoppingCart.visit(this);
    }

    @Override
    public void accept(Visitor visitor) {
        //TODO: THIS
    }
}
