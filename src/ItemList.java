import java.util.*;

import static java.util.Collections.sort;

public abstract class ItemList{
    private Node<Item> beginning;
    private Node<Item> end;
    private Comparator<Item> comparator;

    ItemList(Comparator<Item> comparator) {
        this.comparator = comparator;
        this.beginning = null;
        this.end = null;
    }

    ItemList() {
        this.comparator = null;
        this.beginning = null;
        this.end = null;
    }

    public void setComparator(Comparator comparator){
        this.comparator = comparator;
    }

    static class Node<T> {
        private T value;
        private Node<T> next;
        private Node<T> prev;

        Node(T value, Node<T> next, Node<T> prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }

        Node(T value, Node<T> prev) {
            this.value = value;
            this.next = null;
            this.prev = prev;
        }

        public T getValue() {
            return value;
        }

        public Node<T> getNext() {
            return next;
        }

        public Node<T> getPrev() {
            return prev;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public void setPrev(Node<T> prev) {
            this.prev = prev;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    class ItemIterator<T> implements ListIterator<T> {
        private Node<T> first;
        private Node<T> current;
        private int index;
        private T lastParsed = null;
        private int lastParsedIndex = -1;

        public ItemIterator(Node<T> first) {
            super();
            this.first = first;
            this.current = first;
            index = 0;
        }

        public ItemIterator(int index, Node<T> first) {
            super();
            this.first = first;
            this.current = first;

            for (int i = 0; i < index - 1 ; ++i)
                this.current = this.current.getNext();


            this.index = index;
        }

        @Override
        public boolean hasNext() {
            if(current == null) //1st element
                return false;

//            if (current.getNext() == null)
//                return false;

            return true;
        }

        @Override
        public T next() {
            lastParsed = current.getValue();
            lastParsedIndex = index;
            current = current.getNext();
            index++;
            return lastParsed;
        }

        @Override
        public boolean hasPrevious() {
            if (current.getPrev().equals(null))
                return false;
            return true;
        }

        @Override
        public T previous() {
            lastParsed = current.getValue();
            lastParsedIndex = index;
            current = current.getPrev();
            index--;
            return lastParsed;
        }

        @Override
        public int nextIndex() {
            return index + 1;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            if(lastParsed == null)
                throw new IllegalStateException();

            int currentIndex = 0;
            Node<T> currentNode = first;

            while(currentIndex < lastParsedIndex) {
                currentIndex++;
                currentNode = currentNode.getNext();
            }

            if(currentNode.getNext() != null && currentNode.getPrev() != null) {
                currentNode.getNext().setPrev(currentNode.getPrev());
                currentNode.getPrev().setNext(currentNode.getNext());
            }

            if(currentNode.getPrev() == null && currentNode.getNext() == null){   //single element
              first = null;
              end = null;
            }
            currentNode = null;
            lastParsedIndex = -1; lastParsed = null;

        }

        @Override
        public void set(T t) {
            if(lastParsed == null)
                throw new IllegalStateException();

            int currentIndex = 0;
            Node<T> currentNode = first;

            while(currentIndex < lastParsedIndex) {
                currentIndex++;
                currentNode = currentNode.getNext();
            }

            currentNode.setValue(t);

            lastParsedIndex = -1; lastParsed = null;
        }

        @Override
        public void add(T t){
            Node<T> toAdd = new Node<T>(t, current, current.getPrev());

            current.getPrev().setNext(toAdd);
            current.setPrev(toAdd);
            index++;
        }

        public Node<T> getNode() {
            return current;
        }

        public int getIndex() {
            return index;
        }
    }

    public boolean add(Item element) {
        Node<Item> newNode = new Node<Item>(new Item(element), null, end);

        if(beginning == null){
            beginning = newNode;
            end = newNode;

            return true;
        }

        end.setNext(newNode);
        end = newNode;

        this.sortList();

        return true;
    }

    public boolean addPlain(Item element){
        Node <Item> newNode = new Node<Item>(new Item(element), null, end);

        if(beginning == null){
            beginning = newNode;
            end = newNode;

            return true;
        }

        end.setNext(newNode);
        end = newNode;

        return true;
    }

    public boolean removeAll(Collection<? extends Item> c){
        return true;
    }

    public Item getItem(int index) {
        ItemIterator<Item> it = new ItemIterator<Item>(index, beginning);

        return it.next();
    }

    public Item getItem(Item element){
        return getItem(this.indexOf(element));
    }

    public Node<Item> getNode(int index) {
        ItemIterator<Item> it = new ItemIterator<Item>(index, beginning);

        return it.getNode();
    }

    public int indexOf(Item item) {
        ItemIterator<Item> it = new ItemIterator<Item>(beginning);

        while (it.hasNext()) {
            Item value = it.next();

            if (value.equals(item))
                return it.getIndex();
        }
        return -1;
    }

    public int indexOf(Node<Item> node) {
        ItemIterator<Item> it = new ItemIterator<Item>(beginning);

        while (it.hasNext()) {
            Node<Item> value = it.getNode();

            if (value.equals(node)) {
                return it.getIndex();
            }
        }

        return -1;

    }

    public boolean contains(Node<Item> node) {
        return (this.indexOf(node) != -1);
    }

    public boolean contains(Item item) {
        return (this.indexOf(item) != -1);
    }

    public Item remove(int index) {
        ItemIterator<Item> it = new ItemIterator<Item>(index, beginning);

        Node<Item> node = it.getNode();

        if(node.getPrev() == null){

            if(node.getNext() == null){   // only 1 element
                beginning = null;
                end = null;
            }
            else{
                node.getNext().setPrev(null); // first element
                beginning = node.getNext();
            }
        }
        else{

            if(node.getNext() == null){ // last element
                node.getPrev().setNext(null);
                end = node.getPrev();
            }
            else{   //some element
                node.getPrev().setNext(node.getNext());
                node.getNext().setPrev(node.getPrev());
            }
        }

        return node.getValue();
    }

    public Item remove(Item item){
        int index = indexOf(item);

        if(index == -1)
            return null;

        return remove(index);
    }

    public Item removeNoNotification(int index){
        ItemIterator<Item> it = new ItemIterator<Item>(index, beginning);

        Node<Item> node = it.getNode();

        if(node.getPrev() == null){

            if(node.getNext() == null){   // only 1 element
                beginning = null;
                end = null;
            }
            else{
                node.getNext().setPrev(null); // first element
                beginning = node.getNext();
            }
        }
        else{

            if(node.getNext() == null){ // last element
                node.getPrev().setNext(null);
                end = node.getPrev();
            }
            else{   //some element
                node.getPrev().setNext(node.getNext());
                node.getNext().setPrev(node.getPrev());
            }
        }

        return node.getValue();
    }

    public Item removeNoNotification(Item item){
        int index = indexOf(item);

        if(index == -1)
            return null;

        return removeNoNotification(index);
    }

    public boolean isEmpty(){
        return (beginning == null);
    }

    public ListIterator<Item> listIterator(int index) {
        return new ItemIterator<Item>(index, beginning);
    }

    public ListIterator<Item> listIterator() {
        return new ItemIterator<Item>(beginning);
    }

    public Vector<Item> toVector(){
        ItemIterator<Item> it = new ItemIterator<Item>(beginning);
        Vector<Item> v = new Vector<Item>();

        while(it.hasNext()){
            v.add(it.next());
        }

        return v;
    }

    public int size() {
        ListIterator<Item> it = this.listIterator();
        int size = 0;

        while (it.hasNext()) {
            it.next();
            size++;
        }

        return size;
    }

    public String toString(){
        Vector<Item> items = this.toVector();

        sort(items, new Comparator<Item>() {
            public int compare(Item o1, Item o2) {
                if (o1.getPrice().equals(o2.getPrice()))
                    return 0;

                return (o1.getPrice() < o2.getPrice()) ? -1 : 1;
            }
        });

        String resultString = "[";

        for (int i = 0; i < items.size(); ++i) {
            resultString += items.elementAt(i).toString();
            if (i != items.size() - 1)
                resultString += ", ";
        }

        resultString += "]";

        return resultString;
    }

    public void sortList(){
        Vector<Item> v = this.toVector();

        v.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if(o1.getPrice() == o2.getPrice())
                    return 0;

                return o1.getPrice() < o2.getPrice() ? -1 : 1;
            }
        });

        for(int i = 0; i < v.size(); ++i)
            this.removeNoNotification(0);

        for(Item i : v)
            addPlain(i);
    }

    public Double getTotalPrice(){
        ItemIterator<Item> it = new ItemIterator<Item>(beginning);
        Item item;
        Double totalPrice = 0.0;

        while(it.hasNext()){
            item = it.next();
            totalPrice += item.getPrice();
        }

        return totalPrice;
    }

}
