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

    @Override
    public boolean add(Item item){
        Store store = Store.getInstance("dummy_text");

        for(Customer c : store.getCustomers())
            if(c.getWishlist() == this)
                for(Department d : store.getDepartments())
                    for(Item i : d.getItems())
                        if(i.equals(item)){
                            d.addObserver(c);
                            break;
                        }

        super.add(item);

        return true;
    }

    @Override
    public Item remove(Item item){
        Store store = Store.getInstance("dummy_text");

        boolean foundItemFromDep = false;
        Department departmentFound = null;

        for(Department d : store.getDepartments())
            for(Item i : d.getItems())
                if(i.equals(item))
                   for(Item j : d.getItems())
                       if(this.contains(j) && !j.equals(item)) {
                           foundItemFromDep = true;
                           departmentFound = d;
                       }

        if(!foundItemFromDep)
            for(Customer c : store.getCustomers())
                if(c.getWishlist() == this)
                    departmentFound.removeObserver(c);

        return super.remove(item);
    }

    @Override
    public Item remove(int index){
        Item item = this.getItem(index);

        return this.remove(item);
    }


    public Strategy getStrategy() {
        return strategy;
    }

    public void executeStrategy(){
        strategy.execute(this);
    }
}
