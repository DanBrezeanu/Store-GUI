import java.util.ListIterator;

public class SoftwareDepartment extends Department {

    SoftwareDepartment(Integer ID, String name) {
        super(ID, name);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
