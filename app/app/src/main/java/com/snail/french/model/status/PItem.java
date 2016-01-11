package com.snail.french.model.status;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by litingchang on 15-12-22.
 */
public class PItem extends StatusBase{
    public String name;
    public List<Status> statusList;

    public PItem() {
        statusList = new ArrayList<>();
    }
}
