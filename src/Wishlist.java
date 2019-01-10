import java.util.Comparator;
import java.util.ListIterator;

public class Wishlist extends ItemList {

    private Strategy strategy;

    Wishlist(Comparator comparator, String strategy){
        super(comparator);

        if(strategy.equals("A"))
            this.strategy = new StrategyA();

        if(strategy.equals("B"))
            this.strategy = new StrategyB();

        if(strategy.equals("C"))
            this.strategy = new StrategyC();
    }

    Wishlist(String strategy){
        super();

        if(strategy.equals("A"))
            this.strategy = new StrategyA();

        if(strategy.equals("B"))
            this.strategy = new StrategyB();

        if(strategy.equals("C"))
            this.strategy = new StrategyC();
    }


    public Strategy getStrategy() {
        return strategy;
    }

    public void executeStrategy(){
        strategy.execute(this);
    }
}
