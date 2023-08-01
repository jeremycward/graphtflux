package demo;


import java.util.List;
import java.util.Stack;

public interface Cell<T> {
    String getId();
    Stack<T> generations();
    Mutation process(Mutation input);

    Class<T> getContentType();

}
