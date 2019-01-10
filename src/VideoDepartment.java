import java.util.ListIterator;

public class VideoDepartment extends Department {

    VideoDepartment(Integer ID, String name) {
        super(ID, name);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
