package com.mjrj.lzh.pms.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.util
 * @Author: lzh
 * @CreateTime: 2020-03-17 21:05
 * @Description: ${Description}
 */
@Slf4j
public class MD5Util {

    private static final char[] salt = new char[]{'a', 'b', 'c', 'd', '1', '2', '3', '4', 'e'};
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        //MD5Util.md5("123456",new Date());
        Date dates = null;
        try {
            dates = df.parse("2019-5-02 10:43:06");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean flag = MD5Util.verfied("7613443ee47c8b85906180b7bf02f2ee", "123456", dates);
        log.info("s");
    }
    // 7613443ee47c8b85906180b7bf02f2ee

    /**
     * 加密
     * @param p 用户密码
     * @param d     注册日期
     * @return 加密后的密码
     */
    public static String md5(String p, Date d) {
        String encode = DigestUtils.md5DigestAsHex((p + df.format(d)).getBytes());
        StringBuilder str = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            str.append(salt[r.nextInt(9)]);
        }
        return encode.replace(encode.substring(5, 9), str);
    }


    /***
     * 解密
     * @param dbPwd 数据库里的密码
     * @param inputPwd  用户输入的密码
     * @ createdDate 用户账户注册时间
     * @return
     */
    public static boolean verfied(String dbPwd, String inputPwd, Date createdDate) {
        inputPwd = MD5Util.md5(inputPwd, createdDate);
        inputPwd = inputPwd.replace(inputPwd.substring(5, 9), "1");
        dbPwd = dbPwd.replace(dbPwd.substring(5, 9), "1");
        if (inputPwd.equals(dbPwd)) {
            return true;
        }
        return false;
    }
}
