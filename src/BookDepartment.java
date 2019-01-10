import java.util.ListIterator;

public class BookDepartment extends Department{

    BookDepartment(Integer ID, String name){
        super(ID, name);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
