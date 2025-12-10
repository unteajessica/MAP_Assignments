package model.adt;

import exceptions.StackIsEmpty;
import java.util.Stack;

public class MyStack<T> implements MyIStack<T> {

    private final Stack<T> stack = new Stack<>();

    @Override
    public T pop() {
        if (stack.isEmpty()) {
            throw new StackIsEmpty();
        }
        return stack.pop();
    }

    @Override
    public void push(T elem) {
        stack.push(elem);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}
