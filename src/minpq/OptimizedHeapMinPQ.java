package minpq;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class OptimizedHeapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of element-priority pairs.
     */
    private final List<PriorityNode<E>> elements;
    /**
     * {@link Map} of each element to its associated index in the {@code elements} heap.
     */
    private final Map<E, Integer> elementsToIndex;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        elements = new ArrayList<>();
        elementsToIndex = new HashMap<>();
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        PriorityNode<E> node = new PriorityNode<>(element, priority);
        elements.add(node);
        int index = elements.size() -1;
        elementsToIndex.put(element, index);
        swim(index);
        }

    @Override
    public boolean contains(E element) {
        return elementsToIndex.containsKey(element);
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return elements.get(0).getElement();
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        E min = peekMin();
        swap(0, elements.size() - 1);
        elements.remove(elements.size() - 1);
        elementsToIndex.remove(min);
        if(!isEmpty()) {
            sink(0);
        }
        return min;
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }
        int index = elementsToIndex.get(element);
        PriorityNode<E> temp = elements.get(index);
        double oldPrio = temp.getPriority();
        if(oldPrio < priority) {
            temp.setPriority(priority);
            sink(index);
        } else if (oldPrio > priority) {
            temp.setPriority(priority);
            swim(index);
        }
    }

    @Override
    public int size() {
        return elements.size();
    }

    //swaps indexes
    private void swap(int ind1, int ind2) {
        PriorityNode<E> temp = elements.get(ind1);
        elements.set(ind1, elements.get(ind2));
        elements.set(ind2, temp);
        elementsToIndex.put(elements.get(ind1).getElement(), ind1);
        elementsToIndex.put(elements.get(ind2).getElement(), ind2);
    }


    //swims down
    private void sink(int index) {
        int lChild = 2 * index + 1;
        int rChild = 2 * index + 2;
        int min = index;
        if(lChild < elements.size() && greaterThan(min, lChild)){
            min = lChild;
        }
        if(rChild < elements.size() && greaterThan(min, rChild)){
            min = rChild;
        }
        if(min != index) {
            swap(index, min);
            sink(min);
        }
    }

    private boolean greaterThan(int ind1, int ind2) {
        return elements.get(ind1).getPriority() > elements.get(ind2).getPriority();
    }
    //swims up
    private void swim(int index) {
        //Find the index and make sure index is smaller than parent index
        //If it is swap the two
        //Reassign indexes
        while(index > 0 && greaterThan((index - 1) / 2, index)){
            swap(index, (index-1) / 2);
            index = (index - 1) / 2;
        }


    }
}
