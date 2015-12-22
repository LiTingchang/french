package com.snail.french.constant;

/**
 * Created by litingchang on 15-12-23.
 */
public enum FrenchKind {

    TCF("C"),
    TEF("E"),
    TEM4("S");

    private String kind;
    private FrenchKind(String kind) {
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }
}
