import java.util.Observer;

public class ShoppingCart extends ItemList implements Visitor {
    private Double budget;

    ShoppingCart(Double budget){
        this.budget = budget;
    }

    ShoppingCart(){
        this.budget = 0.0;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getBudget() {
        return budget;
    }



    @Override
    public void visit(BookDepartment bookDepartment) {

    }

    @Override
    public void visit(MusicDepartment musicDepartment) {

    }

    @Override
    public void visit(SoftwareDepartment softwareDepartment) {

    }

    @Override
    public void visit(VideoDepartment videoDepartment) {

    }
}
