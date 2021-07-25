package queue;

import java.util.Arrays;

public class ArrayQueue extends AbstractQueue {
    private int start;
    private int end;
    private Object[] elements = new Object[5];

    protected void enqueueImpl(Object element) {
        ensureCapacity(size + 1);
        elements[end] = element;
        end = getNextPos(end);
    }

    private void ensureCapacity(int capacity) {
        if (capacity < elements.length) {
            return;
        }
        int newCapacity = 2 * capacity;
        Object[] newElements = new Object[newCapacity];
        copy(elements, newElements, start, end, size);
        elements = newElements;
        start = 0;
        end = start + size;
    }

    private int getNextPos(int pos) {
        return (pos + 1) % elements.length;
    }

    protected void dequeueImpl() {
        start = getNextPos(start);
    }

    protected Object elementImpl() {
        return elements[start];
    }

    protected void clearImpl() {
        start = 0;
        end = start;
    }

    public Object[] toArray() {
        Object[] res = new Object[size];
        copy(elements, res, start, end, size);
        return res;
    }

    public ArrayQueue makeCopy() {
        final ArrayQueue copy = new ArrayQueue();
        copy.size = size;
        copy.elements = Arrays.copyOf(elements, size);
        copy.start = start;
        copy.end = end;
        return copy;
    }

    private void copy(Object[] src, Object[] dest, int start, int end, int size) {
        if (start > end) {
            System.arraycopy(src, start, dest, 0, src.length - start);
            System.arraycopy(src, 0, dest, src.length - start, end);
        } else {
            System.arraycopy(src, start, dest, 0, size);
        }
    }
}
