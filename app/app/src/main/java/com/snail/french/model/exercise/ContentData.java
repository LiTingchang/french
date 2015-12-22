package com.snail.french.model.exercise;

import com.snail.french.utils.StringUtils;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by litingchang on 15-12-23.
 */
public class ContentData {

    /**
     * {
     "answer_analyzation": "你住在哪\r\nA.第三区\r\nB.从尼斯来\r\nC.为了S\r\nD.3年以来",
     "answer_index": 1,
     "answer_type": 0,
     "audio_url": "http://french.qiniudn.com/3508b60f363f7e0a46b6b6e2b81f8cf9.mp3",
     "content": "",
     "option": [
     "A.      ",
     "B.      ",
     "C.      ",
     "D.      "
     ],
     "original_text": "Où habites-tu ?\r\nA Dans le 3e arrondissement.\r\nB De Nice.\r\nC Pour Sophie.\r\nD Depuis 3 ans.",
     "time": "0",
     "title": "听力交际对话，选出能正确对话的选项"
     }
     */

    public String answer_analyzation;
    public int answer_index;
    public int answer_type;
    public String audio_url;
    public String content;
    public ArrayList<String> option;
    public String original_text;
    public String time;
    public String title;
}
