/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final double ARRAY_DECREASE_FACTOR = 0.25;
    private static final int ARRAY_CAPACITY_MULTIPLIER = 2;

    private Item[] items = (Item[]) new Object[1];
    private int itemCount = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        // empty constructor
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return itemCount;
    }

    // add the item
    public void enqueue(Item item) {
        checkIfItemNotNull(item);
        int emptySlotIndex = findFirstEmptySlot();

        items[emptySlotIndex] = item;
        itemCount += 1;
    }

    // remove and return a random item
    public Item dequeue() {
        checkIfQueueNotEmpty();
        int randomPosition = findRandomOccupiedSlot();
        Item item = items[randomPosition];
        items[randomPosition] = null;

        itemCount -= 1;

        if (((double) size() / (double) items.length) <= ARRAY_DECREASE_FACTOR) {
            decreaseArrayCapacity();
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        checkIfQueueNotEmpty();
        return items[findRandomOccupiedSlot()];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return createIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue();
        queue.enqueue(123);
        queue.enqueue(125);
        queue.enqueue(127);
        queue.enqueue(129);
        queue.enqueue(131);
        queue.enqueue(133);
        queue.enqueue(135);
        queue.enqueue(137);
        queue.enqueue(139);

        for (int item: queue) {
            System.out.println(item);
        }

        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();

        System.out.println("=========");

        for (int item: queue) {
            System.out.println(item);
        }

        int n = 5;
        RandomizedQueue<Integer> queueR = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queueR.enqueue(i);
        for (int a : queueR) {
            for (int b : queueR)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }

    // MARK: Validation & checks
    private void checkIfItemNotNull(Item item) {
        if (item == null) throw new IllegalArgumentException("item can't be null");
    }

    private void checkIfQueueNotEmpty() {
        if (isEmpty()) throw new NoSuchElementException("not possible to remove on empty deque");
    }

    // MARK: - Array size observing

    private int findFirstEmptySlot() {
        if (size() == items.length) {
            increaseArrayCapacity();
        }

        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            if (item == null) { return i; }
        }

        return -1;
    }

    private int findRandomOccupiedSlot() {
        int randomSlot = -1;
        int arrayCount = items.length;

        while (randomSlot == -1) {
            int index = StdRandom.uniform(arrayCount);
            Item item = items[index];
            if (item != null) {
                randomSlot = index;
            }
        }

        return randomSlot;
    }

    // MARK: - Capacity

    private void increaseArrayCapacity() {
        changeItemsCapacityTo(items.length * ARRAY_CAPACITY_MULTIPLIER);
    }

    private void decreaseArrayCapacity() {
        changeItemsCapacityTo(items.length / ARRAY_CAPACITY_MULTIPLIER);
    }

    private void changeItemsCapacityTo(int newCapacity) {
        if (newCapacity <= 0) {
            throw new IllegalArgumentException("wrong capacity");
        }

        Item[] oldArray = items;
        int oldArrayCount = oldArray.length;
        Item[] newArray = (Item[]) new Object[newCapacity];

        // Copy old array content
        itemCount = 0;
        for (int i = 0; i < oldArrayCount; i++) {
            Item item = oldArray[i];
            if (item != null) {
                newArray[itemCount++] = item;
            }
        }

        items = newArray;
    }

    // MARK: -
    private Iterator<Item> createIterator() {
        Iterator<Item> iterator = new Iterator<Item>() {
            private int[] unshownOccupiedSlotIndexes = fillOccupiedSlotIndexes();
            private int shownIndexesCount = 0;

            public boolean hasNext() {
                return unshownOccupiedSlotIndexes.length - shownIndexesCount > 0;
            }

            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                return items[unshownOccupiedSlotIndexes[shownIndexesCount++]];
            }

            public void remove() {
                throw new UnsupportedOperationException("remove is unsupported");
            }

            private int[] fillOccupiedSlotIndexes() {
                int[] indexes = new int[size()];
                int counter = 0;
                for (int i = 0; i < items.length; i++) {
                    if (items[i] != null) {
                        indexes[counter++] = i;
                    }
                }

                StdRandom.shuffle(indexes);
                return indexes;
            }
        };

        return iterator;
    }

}
