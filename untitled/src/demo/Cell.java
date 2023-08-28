package demo;


import java.util.Stack;

public interface Cell<T> {
    String getId();
    Stack<T> generations();
    Action process(Action input);

    Class<T> getContentType();

}
