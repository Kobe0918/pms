package com.mjrj.lzh.pms.util;

import com.alibaba.fastjson.JSON;
import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.beans.SimpleBeanInfo;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.alibaba.fastjson.util.IOUtils.readAll;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.util
 * @Author: lzh
 * @CreateTime: 2020-03-05 19:30
 * @Description: ${Description}
 */
@Slf4j
public class CommonUtil {

    public static final String PHONE_PRE= "phone::";

    /**
     * 产生六位的随机数
     *
     * @return
     */
    public static String randomNumnber() {
        Random random = new Random();
        String result = "";
        for (int j = 1; j < 7; j++) {
            int i = random.nextInt(9);
            result += i;
        }
        return result;
    }

     public static String subStringPath(String path){
        if(!StringUtils.isEmpty(path)){
            path = path.substring(0,path.indexOf("/",2));
            return path;
        }
      return null;
    }



    /**
     * @param ipAddr IP地址,只能精确到市
     * @param
     * @return
     */
    public static String getIPAddrByTaoBaoApi(String ipAddr) {
//        淘宝根据ip地址获取真实地址的api
         String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        HttpURLConnection connection = null;
        URL url = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(8000);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(8000);// 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);// 是否打开输出流 true|false
            connection.setDoInput(true);// 是否打开输入流true|false
            connection.setRequestMethod("POST");// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.connect();// 打开连接端口
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());// 打开输出流往对端服务器写数据
            out.writeBytes("ip=" + ipAddr);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush();// 刷新
            out.close();// 关闭输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));// 往对端写完数据对端服务器返回数据
            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    /**
     * 传入地址数据，进行解析,暂时无用
     * @param theString
     * @return
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed      encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        String ipAddr = outBuffer.toString();
        String addr = "";
        if (!StringUtils.isEmpty(ipAddr)) {
            try {
                JSONObject jsonObject = new JSONObject(ipAddr);
                jsonObject = jsonObject.getJSONObject("data");
                addr = addr + jsonObject.getString("country");
                addr = addr + jsonObject.getString("region");
                addr = addr + jsonObject.getString("city");
                return addr;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



//------------------- 百度api获取ip对应的地址


    private static final String BAIDU_APP_KEY = "42b8ececa9cd6fe72ae4cddd77c0da5d";
    //62xkDqN5ePUj4fszhfSsqLVWGW5fBeOV  自己的key


    /**
     * 获取客户端请求的IP地址
     *
     * @param request   //IP地址183.253.76.252家里连wifi状态下
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        //前面四个是在处理因为ngix等转发时的问题，我们这个系统暂时没有所以直接request.getRemoteAddr()获取也可以
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            log.info(ip + " :ipdizhi ");
        }
        // 处理多IP的情况（只取第一个IP）
        if (ip != null && ip.contains(",")) {
            String[] ipArray = ip.split(",");
            ip = ipArray[0];
        }
        return ip;
    }



    /**
     * @param ipAddr  IP地址
     * @return  返回经纬度坐标的map x(经度),y(纬度)
     */
    public static Map<String, Double> getLatitude(String ipAddr) throws Exception {
            ipAddr = URLEncoder.encode(ipAddr, "UTF-8");
            URL url = new URL("http://api.map.baidu.com/location/ip?ak=" + BAIDU_APP_KEY + "&ip=" + ipAddr + "&coor=bd09ll");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String res;
            StringBuffer sb = new StringBuffer("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            in.close();
            String str = sb.toString();
            if (!StringUtils.isEmpty(str)) {
                Map <String, Double> map = new HashMap <>();
                JSONObject jsonObject = new JSONObject(str);
                int status = jsonObject.getInt("status");
                if(0 == status){
                    JSONObject object = jsonObject.getJSONObject("content").getJSONObject("point");
                    map.put("x",object.getDouble("x"));
                    map.put("y",object.getDouble("y"));
                    log.info("x :"+map.get("x"));
                    log.info("y :"+map.get("y"));
                    return map;
                }
            }
        return null;
    }


    /**
     * @param address 经纬度
     * @return   附近地址
     */
    public static ResponseResult getAddrByBaiduApi(Map<String,Double> address)throws Exception {
            if(address != null){
                URL url = new URL("http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=" + address.get("y").toString() + "," +
                        address.get("x").toString() + "&output=json&pois=1&ak=" + BAIDU_APP_KEY);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String res;
                StringBuffer sb = new StringBuffer("");
                while ((res = in.readLine()) != null) {
                    sb.append(res.trim());
                }
                in.close();
                String str = sb.toString();
                if (!StringUtils.isEmpty(str)) {
                    int begin = str.indexOf("(")+1;
                    int end = str.length()-1;
                    String result = str.substring(begin, end);
                    JSONObject object = new JSONObject(result);
                    int status = object.getInt("status");
                    if(0 == status){
                        result = object.getJSONObject("result").getString("formatted_address");
                        return ResponseTool.success(result);
                    }
                }
            }
        return ResponseTool.fail();
    }


    public  boolean isEmptyString(String v){
        return StringUtils.isEmpty(v);
    }

    private  static SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    private  static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    public static String getCurrentDate(){
        Date date = new Date();
        return simpleDateFormat1.format(date);
    }


    public static Boolean compareToCurrentTime(String time)throws ParseException{
        String downTimeString = simpleDateFormat1.format(new Date())+" 17:30:00";
        String upTimeString = simpleDateFormat1.format(new Date())+" 07:00:00";
        Date upTime = simpleDateFormat2.parse(upTimeString);
        Date downTime = simpleDateFormat2.parse(downTimeString);
        String format = simpleDateFormat2.format(new Date());
        String s = time + format.substring(format.indexOf(" "));
        Date now = simpleDateFormat2.parse(s);
        return now.before(downTime) && now.after(upTime);
    }

    public static Boolean compareTime(String time)throws ParseException{
        String downTimeString = simpleDateFormat1.format(new Date())+" 24:00:00";
        String upTimeString = simpleDateFormat1.format(new Date())+" 07:00:00";
        Date upTime = simpleDateFormat2.parse(upTimeString);
        Date downTime = simpleDateFormat2.parse(downTimeString);
        String format = simpleDateFormat2.format(new Date());
        String s = time + format.substring(format.indexOf(" "));
        System.out.println("s :" +s);
        Date now = simpleDateFormat2.parse(s);
        return now.before(downTime) && now.after(upTime);
    }

    public static Boolean afterFiverClock(String time)throws ParseException{
        String timeString = simpleDateFormat1.format(new Date())+" 17:30:00";
        Date time1 = simpleDateFormat2.parse(timeString);
        String format = simpleDateFormat2.format(new Date());
        String s = time + format.substring(format.indexOf(" "));
        System.out.println("s :" +s);
        Date now = simpleDateFormat2.parse(s);
        return now.after(time1);

    }



    public static double getWorkHours(Date up, Date down) throws Exception {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String up_time = "2019-10-19 7:30:00";
//        String down_time = "2019-10-19 18:00:00";
//            Date up = simpleDateFormat.parse(up_time);
//            Date down = simpleDateFormat.parse(down_time);
            long workTime =  down.getTime() - up.getTime();
            double workHours = (Math.round(workTime / 1000 / 6 /60)/10.0);
            return workHours;

    }

    public static void main(String[] args) {
        try {
            System.out.println(afterFiverClock("2020-04-05"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static boolean isEmpty(Collection collection)
    {
        return collection == null || collection.isEmpty();
    }
}
