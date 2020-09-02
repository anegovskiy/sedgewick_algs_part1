/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;

    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    // construct an empty deque
    public Deque() {
        // empty
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        if (isEmpty()) {
            return 0;
        }

        int count = 1;
        Node currentNode = first;

        while (currentNode.next != null) {
            currentNode = currentNode.next;
            count += 1;
        }

        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        checkIfItemNotNull(item);

        Node oldFirst = first;
        Node newFirst = new Node();
        newFirst.item = item;
        first = newFirst;

        if (oldFirst != null) {
            newFirst.next = oldFirst;
            oldFirst.previous = newFirst;
        }

        if (last == null) last = newFirst;

    }

    // add the item to the back
    public void addLast(Item item) {
        checkIfItemNotNull(item);

        Node oldLast = last;
        Node newLast = new Node();
        newLast.item = item;
        last = newLast;

        if (oldLast != null) {
            oldLast.next = newLast;
            newLast.previous = oldLast;
        }

        if (first == null) first = newLast;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkIfDequeNotEmpty();

        Node oldFirst = first;
        first = oldFirst.next;

        return oldFirst.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        checkIfDequeNotEmpty();

        Node oldLast = last;
        last = oldLast.previous;

        return oldLast.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return createIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(123);
        deque.addFirst(125);
        deque.addFirst(127);
        deque.addLast(145);
        deque.addFirst(129);
        deque.addFirst(131);
        deque.addLast(149);

        for (int item: deque) {
            System.out.println(item);
        }
    }

    // MARK: Validation & checks
    private void checkIfItemNotNull(Item item) {
        if (item == null) throw new IllegalArgumentException("item can't be null");
    }

    private void checkIfDequeNotEmpty() {
        if (isEmpty()) throw new NoSuchElementException("not possible to remove on empty deque");
    }

    // MARK: -
    private Iterator<Item> createIterator() {
        Iterator<Item> iterator = new Iterator<Item>() {
            private Node currentNode = first;

            public boolean hasNext() {
                return currentNode != null;
            }

            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();

                Item item = currentNode.item;
                currentNode = currentNode.next;

                return item;
            }

            public void remove() {
                throw new UnsupportedOperationException("remove is unsupported");
            }
        };

        return iterator;
    }
}
