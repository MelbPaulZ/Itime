package org.unimelb.itime.bean;

import org.unimelb.itime.util.CharacterParser;

import java.io.Serializable;

/**
 * Created by Qiushuo Huang on 2017/2/7.
 */

public abstract class BeanWrapper implements Serializable, Comparable<BeanWrapper> {
    private String sortLetters = "";
    private String pinyin = "";
    private String matchStr = "";

    public BeanWrapper(String name){
        CharacterParser characterParser = CharacterParser.getInstance();

        // 汉字转换成拼音
        pinyin = characterParser.getSelling(name.toLowerCase());
        if(pinyin.length()>0) {
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                setSortLetters(sortString.toUpperCase());
            } else {
                setSortLetters("#");
            }
        }
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getMatchStr() {
        return matchStr;
    }

    public void setMatchStr(String matchStr) {
        this.matchStr = matchStr;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    abstract public String getName();

    abstract public String getPhoto();

    abstract public String getUserId();

    abstract public String getUserUid();

    @Override
    public int compareTo(BeanWrapper contactWrapper) {
        if(this.getSortLetters().equals("#")&&!contactWrapper.getSortLetters().equals("#")){
            return 1;
        }

        if(!this.getSortLetters().equals("#")&& contactWrapper.getSortLetters().equals("#")){
            return -1;
        }

        return this.getPinyin().compareTo(contactWrapper.getPinyin());
    }
}
