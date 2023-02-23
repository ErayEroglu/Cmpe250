import java.util.ArrayList;
import java.util.NoSuchElementException;

public class FactoryImpl implements Factory {

    private Holder first;
    private Holder last;
    private Integer size;

    public FactoryImpl(Holder first, Holder last, Integer size) {
        this.first = first;
        this.last = last;
        this.size = size;
    }

    public Holder getFirst() {
        return first;
    }

    public void setFirst(Holder first) {
        this.first = first;
    }

    public Holder getLast() {
        return last;
    }

    public void setLast(Holder last) {
        this.last = last;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String printList() {
        Holder current = first;
        String elements = "{";
        if (isEmpty()) {
            elements = elements + "}";
        }
        while (current != null) {
            if (current.getNextHolder() == null) {
                elements = elements + current.getProduct().toString() + "}";
                break;
            }
            elements = elements + current.getProduct().toString() + ",";
            current = current.getNextHolder();
        }
        return elements;
    }

    public boolean isEmpty() { // helper method to check if the linked list is empty
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Holder holderFinder(int index) { // finds the holder at the specific index
        Holder current = first;
        int counter = 0;
        if (index == 0) {
            return current;
        }
        if (index == size - 1) {
            return last;
        }
        while (counter != index + 1) {
            if (counter == index) {
                return current;
            }
            current = current.getNextHolder();
            counter++;
        }
        return null;
    }

    @Override
    public void addFirst(Product product) {
        if (isEmpty()) {
            Holder newProduct = new Holder(null, product, null);
            last = first = newProduct;
            size++;
        } else if (size == 1) {
            Holder newProduct = new Holder(null, product, first);
            last.setPreviousHolder(newProduct);
            first = newProduct;
            size++;
        } else {
            Holder newProduct = new Holder(null, product, first);
            first.setPreviousHolder(newProduct);
            first = newProduct;
            size++;
        }
    }

    @Override
    public void addLast(Product product) {
        if (isEmpty()) {
            addFirst(product);
        } else if (size == 1) {
            Holder newProduct = new Holder(first, product, null);
            first.setNextHolder(newProduct);
            last = newProduct;
            size++;
        } else {
            Holder newProduct = new Holder(last, product, null);
            size++;
            last.setNextHolder(newProduct);
            last = newProduct;
        }
    }

    @Override
    public Product removeFirst() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else if (size == 1) {
            Product removedP = first.getProduct();
            first = last = null;
            size--;
            return removedP;
        } else {
            Product removedP = first.getProduct();
            first = first.getNextHolder();
            first.setPreviousHolder(null);
            size--;
            return removedP;
        }
    }

    @Override
    public Product removeLast() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else if (size == 1) {
            Product removedP = last.getProduct();
            first = last = null;
            size--;
            return removedP;
        } else {
            Product removedP = last.getProduct();
            last = last.getPreviousHolder();
            last.setNextHolder(null);
            size--;
            return removedP;
        }
    }

    @Override
    public Product find(int id) throws NoSuchElementException {
        try {
            if (first.getProduct().getId() == id) {
                return first.getProduct();
            } else {
                Holder current = first.getNextHolder();
                Product foundProduct = new Product(-1, -1);
                while (current.getPreviousHolder() != null) {
                    if (current.getProduct().getId() == id) {
                        foundProduct = current.getProduct();
                        return foundProduct;
                    } else {
                        current = current.getNextHolder();
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Product update(int id, Integer value) throws NoSuchElementException {
        Product foundP = find(id);
        Product oldP = new Product(id, foundP.getValue());
        foundP.setValue(value);
        return oldP;
    }

    @Override
    public Product get(int index) throws IndexOutOfBoundsException {
        if (index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        return holderFinder(index).getProduct();
    }

    @Override
    public void add(int index, Product product) throws IndexOutOfBoundsException {
        if (index == 0) {
            addFirst(product);
        } else if (index > size) {
            throw new IndexOutOfBoundsException();
        } else if (index == 0) {
            addFirst(product);
        } else if (index == size) {
            addLast(product);
        } else {
            Holder temp = holderFinder(index);
            Holder newProduct = new Holder(temp.getPreviousHolder(), product, temp);
            temp.getPreviousHolder().setNextHolder(newProduct);
            temp.setPreviousHolder(newProduct);
            size++;
        }
    }

    @Override
    public Product removeIndex(int index) throws IndexOutOfBoundsException {
        if (index > size - 1) {
            throw new IndexOutOfBoundsException();
        } else if (index == 0) {
            Product removedP = removeFirst();
            return removedP;
        } else if (index == size - 1) {
            Product removedP = removeLast();
            return removedP;
        } else {
            Holder temp = holderFinder(index);
            Product removedProduct = temp.getProduct();
            temp.getPreviousHolder().setNextHolder(temp.getNextHolder());
            temp.getNextHolder().setPreviousHolder(temp.getPreviousHolder());
            size--;
            return removedProduct;
        }
    }

    @Override
    public Product removeProduct(int value) throws NoSuchElementException {
        try {
            Holder current = first;
            if (first.getProduct().getValue() == value) {
                Product temp = removeFirst();
                return temp;
            }
            while (current.getProduct().getValue() != value) {
                if (current.getNextHolder().getProduct().getValue() == value) {
                    if (current.getNextHolder() == last) {
                        Product removedProduct = removeLast();
                        return removedProduct;
                    } else {
                        Product removedProduct = current.getNextHolder().getProduct();
                        current.setNextHolder(current.getNextHolder().getNextHolder());
                        current.getNextHolder().setPreviousHolder(current);
                        size--;
                        return removedProduct;
                    }
                }
                current = current.getNextHolder();
            }
            return null;
        } catch (Exception e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public int filterDuplicates() {
        Holder current = first;
        int countOfDup = 0;
        ArrayList<Integer> items = new ArrayList<>();
        while (current != null) {
            if (!(items.contains(current.getProduct().getValue()))) {
                items.add(current.getProduct().getValue());
                current = current.getNextHolder();
                continue;
            } else if (current == last) {
                removeLast();
                countOfDup++;
                return countOfDup;
            } else {
                current.getPreviousHolder().setNextHolder(current.getNextHolder());
                current.getNextHolder().setPreviousHolder(current.getPreviousHolder());
                countOfDup++;
                size--;
                current = current.getNextHolder();
            }
        }
        return countOfDup;
    }

    @Override
    public void reverse() {
        Holder current = first;
        Holder temp = null;
        boolean flag = true;
        if (isEmpty() || size == 1) {
            flag = false;
        } else if (flag) {
            while (current != null) {
                temp = current.getPreviousHolder();
                current.setPreviousHolder(current.getNextHolder());
                current.setNextHolder(temp);
                current = current.getPreviousHolder();
            }
            Holder info = first;
            first = temp.getPreviousHolder();
            last = info;
        }

    }

}