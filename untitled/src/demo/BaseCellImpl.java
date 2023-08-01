package demo;

import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.UUID;

abstract public class BaseCellImpl<T> implements Cell<T> {
    private Class<T> contentType;
    private Stack<T> generations= new Stack<T>();
    private final String id;
    public BaseCellImpl(Class<T> type, String id) {
        this.id = id;
        this.contentType = getContentType();
    }

    @Override
    public Class<T> getContentType() {
        return contentType;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Stack<T> generations() {
        return generations;
    }
    public static String generateUniqueId(String nickName){
        return nickName + UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseCellImpl<?> baseCell = (BaseCellImpl<?>) o;
        return id.equals(baseCell.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
