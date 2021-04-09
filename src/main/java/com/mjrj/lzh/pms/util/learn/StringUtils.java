package com.mjrj.lzh.pms.util.learn;

public class StringUtils {

    public static boolean isBlank(final CharSequence cs){
        int strLen;
        if(cs == null || (strLen = cs.length()) == 0){
            return true;
        }
        for(int i = 0; i < strLen; i++){
            if(!Character.isWhitespace(cs.charAt(i))){
              return false;
            }
        }
        return true;
    }

    public static void isNotBlank(String str, String msg){
        if(isBlank(str)){
            throw new RuntimeException(msg);
        }
    }

    public static void isNotBlank(String str){
        if(isBlank(str)){
            throw new RuntimeException("参数不能为空！");
        }
    }


}
