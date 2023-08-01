package jgrapht.domain;

import java.time.LocalDateTime;

public class Build <T>{
    public Build(T product, LocalDateTime buildDate) {
        this.product = product;
        this.buildDate = buildDate;
    }

    final T product;
    private final LocalDateTime buildDate;
    public  T getProduct(){
        return product;

    }

}
