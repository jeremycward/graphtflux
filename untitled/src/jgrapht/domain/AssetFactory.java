package jgrapht.domain;

public class AssetFactory<P,T> {
    final T type;

    public AssetFactory(T assetType) {
        this.type = assetType;
    }

    public T getAssetType(){
        return type;
    }



}
