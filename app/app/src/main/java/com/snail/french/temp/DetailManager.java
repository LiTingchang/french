package com.snail.french.temp;

import java.util.ArrayList;
import java.util.List;

public class DetailManager
{
    public static List<OptionItem> getBodyOptionItem() {
        List<OptionItem> optionItemList = new ArrayList<OptionItem>();
        
        OptionItem item1 = new OptionItem();
        item1.id = 1;
        item1.name = "A1";
        item1.level = 4;
        item1.total = 200;
        item1.test = 55;
        optionItemList.add(item1);
        
        OptionItem item2 = new OptionItem();
        item2.id = 2;
        item2.name = "A2";
        item2.level = 3;
        item2.total = 200;
        item2.test = 111;
        optionItemList.add(item2);
        
        OptionItem item3 = new OptionItem();
        item3.id = 3;
        item3.name = "B1";
        item3.level = 2;
        item3.total = 3332;
        item3.test = 222;
        optionItemList.add(item3);
        
        OptionItem item4 = new OptionItem();
        item4.id = 4;
        item4.name = "B2";
        item4.level = 1;
        item4.total = 3222;
        item4.test = 333;
        optionItemList.add(item4);
        
        OptionItem item5 = new OptionItem();
        item5.id = 5;
        item5.name = "C1";
        item5.level = 2;
        item5.total = 3432;
        item5.test = 55;
        optionItemList.add(item5);
        
        return optionItemList;
    }
    
}
