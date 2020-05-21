package com.app.jzapp.videoapps.utils;

import java.io.Serializable;
import java.util.HashMap;

public class SerializableMap<T, K> implements Serializable {
    public HashMap<T, K> map;

    public SerializableMap( ) {
        map = new HashMap<T, K>();
    }

    public HashMap<T, K> getMap() {
        return map;
    }

}
