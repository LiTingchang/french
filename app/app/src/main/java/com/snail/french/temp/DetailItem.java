package com.snail.french.temp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailItem
{
    public List<OptionItem> optionItemList;
    public Date date; // 时间
    
    public DetailItem() {
        optionItemList = new ArrayList<OptionItem>();
    }
    
}
