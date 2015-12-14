package com.snail.french.temp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailItem
{
    public List<OptionItem> optionItemList;
    public String name;
    public int total;
    public int test;
    public int level;
    public float progress;
    
    public DetailItem() {
        optionItemList = new ArrayList<OptionItem>();
    }
    
}
