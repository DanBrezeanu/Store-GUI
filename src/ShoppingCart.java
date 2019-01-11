import java.util.ListIterator;

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
    public boolean add(Item item){
        if(item.getPrice() <= budget) {
            budget -= item.getPrice();
            super.add(item);
            return true;
        }

       return false;
    }


    @Override
    public void visit(BookDepartment bookDepartment) {

        for(Item i : bookDepartment.getItems()){
            Integer itemFound = 0;

            while(this.contains(i)){
                this.remove(i);
                ++itemFound;
            }

            while(itemFound != 0) {
                Item newItem =  new Item(i);
                newItem.setPrice(newItem.getPrice() * 0.9);
                this.add(newItem);

                --itemFound;
            }
        }
    }

    @Override
    public void visit(MusicDepartment musicDepartment) {
        ListIterator<Item> it = this.listIterator();

        Double totalToAdd = 0.0;

        while(it.hasNext()){
            Item current = it.next();
            if(musicDepartment.getItems().contains(current))
                totalToAdd += current.getPrice() * 0.1;
        }

        this.setBudget(this.getBudget() + totalToAdd);
    }

    @Override
    public void visit(SoftwareDepartment softwareDepartment) {
        Double minPrice = Double.MAX_VALUE;

        for(Item item : softwareDepartment.getItems())
            if(item.getPrice() < minPrice)
                minPrice = item.getPrice();

        if(this.getBudget() < minPrice)
            for(Item i : softwareDepartment.getItems()){
                Integer itemFound = 0;

                while(this.contains(i)){
                    this.remove(i);
                    ++itemFound;
                }

                while(itemFound != 0){
                    Item newItem = new Item(i);
                    newItem.setPrice(newItem.getPrice() * 0.8);
                    this.add(newItem);

                    --itemFound;
                }
            }
    }

    @Override
    public void visit(VideoDepartment videoDepartment) {
        ListIterator<Item> it = this.listIterator();

        Double totalDepPrices = 0.0;
        Double maxPrice = 0.0;

        while(it.hasNext()){
            Item current = it.next();
            if(videoDepartment.getItems().contains(current))
                totalDepPrices += current.getPrice();
        }

        for(Item item : videoDepartment.getItems())
            if(item.getPrice() > maxPrice)
                maxPrice = item.getPrice();

        if(totalDepPrices > maxPrice)
            for(Item i : videoDepartment.getItems()){
                Integer itemFound = 0;

                while(this.contains(i)){
                    this.remove(i);
                    ++itemFound;
                }

                while(itemFound != 0){
                    Item newItem = new Item(i);
                    newItem.setPrice(newItem.getPrice() * 0.85);
                    this.add(newItem);

                    --itemFound;
                }
            }

        this.setBudget(this.getBudget() + 0.05 * totalDepPrices);
    }
}
