package com.snail.french.temp;

import java.util.ArrayList;
import java.util.List;

public class DetailManager
{
    public static List<OptionItem> getBodyOptionItem() {
        List<OptionItem> optionItemList = new ArrayList<OptionItem>();
        
        OptionItem item1 = new OptionItem();
        item1.id = 1;
        item1.name = "体重";
        item1.units = "kg";
        optionItemList.add(item1);
        
        OptionItem item2 = new OptionItem();
        item2.id = 2;
        item2.name = "身高";
        item2.units = "cm";
        optionItemList.add(item2);
        
        OptionItem item3 = new OptionItem();
        item3.id = 3;
        item3.name = "腰围";
        item3.units = "cm";
        optionItemList.add(item3);
        
        OptionItem item4 = new OptionItem();
        item4.id = 4;
        item4.name = "胸围";
        item4.units = "cm";
        optionItemList.add(item4);
        
        OptionItem item5 = new OptionItem();
        item5.id = 5;
        item5.name = "臀维";
        item5.units = "cm";
        optionItemList.add(item5);
        
        return optionItemList;
    }
    
    public static List<OptionItem> getPsychologyOptionItem() {
        List<OptionItem> optionItemList = new ArrayList<OptionItem>();
        
        OptionItem item1 = new OptionItem();
        item1.id = 1;
        item1.name = "心率";
        item1.units = "次/分钟";
        optionItemList.add(item1);
        
        OptionItem item2 = new OptionItem();
        item2.id = 2;
        item2.name = "收缩压";
        item2.units = "mmHg";
        optionItemList.add(item2);
        
        OptionItem item3 = new OptionItem();
        item3.id = 3;
        item3.name = "舒张压";
        item3.units = "mmHg";
        optionItemList.add(item3);
        
        return optionItemList;
    }
}
