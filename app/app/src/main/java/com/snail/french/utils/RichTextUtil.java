package com.snail.french.utils;

import android.support.v4.util.Pair;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import java.util.List;

/**
 * Created by litingchang on 15-12-30.
 */
public class RichTextUtil {

    public static class RichText{

        public String text;
        public int color;
        public float size;

    }

    // 简单的改变文字大小
    public static SpannableStringBuilder getSpannableString(List<RichText> list) {

        if (list == null || list.size() == 0) {
            return null;
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        int position = 0;
        for ( RichText richText : list ) {
            int len = richText.text.length();
            ssb.append(richText.text);

            ssb.setSpan(new RelativeSizeSpan(richText.size), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new ForegroundColorSpan(richText.color), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            position += len;
        }

        return ssb;
    }
}