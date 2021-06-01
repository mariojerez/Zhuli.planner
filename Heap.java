// Mario Jerez
// Assignment 11

import java.util.*;

public class Heap<E extends Comparable<E>> extends HeapTester<E> {

    // instance variables
    public ArrayList<E> elements;

    // constructor
    public Heap() {
        this.elements = new ArrayList<E>();
        this.elements.add(null);  // element 0 is not used
    }

    // required for testing purposes
    public Heap(ArrayList<E> elements) { this.elements = elements; }
    public ArrayList<E> getElements() { return this.elements; }

    // returns a string representation of the elements array
    public String toString() {
        return this.elements.toString();
    }

    //--------------------------------------------------------------------
    // other methods to be implemented
    
    // returns the number of elements in the heap, which is one less than
    // the size of the ArrayList, due to the unused element at position 0.
    public int heapSize() {
        return this.elements.size() - 1;
    }

    // returns true if the heap is empty
    public boolean isEmpty() {
      if (this.elements.size() == 1) {
        return true;
      }
      return false;
    }

    // returns the smallest element in the heap, without removing it
    public E peek() {
      return this.elements.get(1);
    }

    // returns element number i
    public E getElement(int i) {
        return this.elements.get(i);
    }

    // returns the index of the last element in the heap
    public int lastIndex() {
      return this.elements.size() - 1;
    }

    // returns the index of element number i's parent
    public int parentIndex(int i) {
        return i / 2;
    }

    // returns the index of element number i's left child
    public int leftChildIndex(int i) {
        return i * 2;
    }

    // returns the index of element number i's right child
    public int rightChildIndex(int i) {
        return i * 2 + 1;
    }

    // returns true if element number i has a left child
    public boolean hasLeftChild(int i) {
      if (this.elements.size() - 1 >= i * 2) {
        return true;
      }
      return false;
    }

    // returns true if element number i has a right child
    public boolean hasRightChild(int i) {
      if (this.elements.size() - 1 >= i * 2 + 1) {
        return true;
      }
      return false;
    }

    // returns the parent element of element number i
    public E getParent(int i) {
        int parentIndex = i / 2;
        return this.elements.get(parentIndex);
    }

    // returns the left child element of element number i
    public E getLeftChild(int i) {
      if (hasLeftChild(i)) {
        return this.elements.get(leftChildIndex(i));
      }
      return null;
    }

    // returns the right child element of element number i
    public E getRightChild(int i) {
      if (hasRightChild(i)) {
        return this.elements.get(rightChildIndex(i));
      }
      return null;
    }

    // swaps the elements at positions i and j
    public void swap(int i, int j) {
      E temp = this.elements.get(i);
      this.elements.set(i, this.elements.get(j));
      this.elements.set(j, temp);
    }

    // returns true if element number i has at least one child that is smaller
    public boolean hasSmallerChild(int i) {
      if (hasLeftChild(i) && getLeftChild(i).compareTo(getElement(i)) < 0) {
        return true;
      }
      else if (hasRightChild(i) && getRightChild(i).compareTo(getElement(i)) < 0) {
        return true;
      }
      return false;
    }

    // returns the index of the smaller child of element number i, assuming
    // that element i is not a leaf. if both child elements are present and
    // equal to each other, the index of the right child should be returned.
    public int smallerChildIndex(int i) {
        if (hasLeftChild(i) && !hasRightChild(i)) {
            return leftChildIndex(i);
        }
        else if (!hasLeftChild(i) && hasRightChild(i)) {
            return rightChildIndex(i);
        }
        else if (getLeftChild(i).compareTo(getRightChild(i)) == 0) { // if left and right are same
            return rightChildIndex(i);
        }
        else if (getLeftChild(i).compareTo(getRightChild(i)) < 0) { // if left child is less than right
            return leftChildIndex(i);
        }
        else if (getRightChild(i).compareTo(getLeftChild(i)) < 0) { // if right child is less than left
            return rightChildIndex(i);
        }
        return -1;
    }

    // adds a new element to the heap
    public void add(E newElement) {
        elements.add(newElement);
        int currentPosition = lastIndex();
        for (;currentPosition != 1 && getElement(currentPosition).compareTo(getParent(currentPosition)) < 0;)  {
          swap(currentPosition, parentIndex(currentPosition));
          currentPosition = parentIndex(currentPosition);
        }
    }

    // removes and returns the smallest element in the heap
    public E remove() {
      if (heapSize() == 1) {
        E smallestElement = peek();
        this.elements.remove(1);
        return smallestElement;
      }
      E smallestElement = peek();
      this.elements.set(1, getElement(lastIndex()));
      this.elements.remove(lastIndex());
      int cp = 1;
      while (hasSmallerChild(cp)) {
        int sci = smallerChildIndex(cp);
        swap(cp, smallerChildIndex(cp));
        cp = sci;
      }
      return smallestElement;
    }
    
    public ArrayList<E> getOrderedArray() {
        // duplicate this heap
        Heap<E> duplicate = new Heap<E>();
        for (E element : this.elements) {
            if (element != null) {
                duplicate.add(element);
                System.out.println(duplicate);
            }
        }
        // get ordered array by removing one element at a time
        ArrayList<E> orderedArray = new ArrayList<E>();
        int size = duplicate.heapSize();
        for (int i = 1; i <= size; i++) {
            E nextElement = duplicate.remove();
            orderedArray.add(nextElement);
        }
        return orderedArray;
    }
}

    // main method is inherited from HeapTester<E>

