import java.util.ListIterator;

public class MusicDepartment extends Department {

    MusicDepartment(Integer ID, String name) {
        super(ID, name);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
