package queue;

public abstract class AbstractQueue implements Queue {
    protected int size;

    //Pre: element != null
    //Post: size' == size + 1 && last element in queue == element
    public void enqueue(Object element) {
        assert element != null;

        enqueueImpl(element);
        size++;
    }

    protected abstract void enqueueImpl(Object element);

    //Pre: size > 0 
    //Post: R = first item in queue && size == size'
    public Object element() {
        assert size > 0;

        return elementImpl();
    }

    protected abstract Object elementImpl();

    //Pre: size > 0
    //Post: R = first item in queue && size' == size - 1 
    public Object dequeue() {
        assert size > 0;

        Object res = element();
        size--;
        dequeueImpl();
        return res;
    }

    protected abstract void dequeueImpl();

    //Pre:
    //Post: size = 0 
    public void clear() {
        size = 0;
        clearImpl();
    }

    protected abstract void clearImpl();

    //Pre:
    //Post: size == size' && R = size
    public int size() {
        return size;
    }

    //Pre: 
    //Post: size == size' && R = (size == 0)
    public boolean isEmpty() {
        return size == 0;
    }
}
