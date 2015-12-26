package com.snail.french.constant;

/**
 * Created by litingchang on 15-12-23.
 */
public enum FrenchKind {

    TCF("C", "TCF"),
    TEF("E", "TCF"),
    TEM4("S", "专四");

    private String kind;
    private String name;
    private FrenchKind(String kind, String name) {
        this.kind = kind;
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }
}
