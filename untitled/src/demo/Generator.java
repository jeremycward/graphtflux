package demo;

@FunctionalInterface
public interface Generator<ContentType> {
    ContentType generate();
}
