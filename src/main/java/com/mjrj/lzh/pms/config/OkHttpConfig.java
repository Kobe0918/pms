package com.mjrj.lzh.pms.config;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.config
 * @Author: lzh
 * @CreateTime: 2020-03-10 00:27
 * @Description: ${Description}
 */
@Slf4j
public class OkHttpConfig {
    @Autowired
    private OkHttpClient okHttpClient;

    private  String execNewCall(Request request){
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("okhttp3 put error >> ex = {}");
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return "";
    }

    public void main(){
        String sb = "http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=25.44845014,119.07773096&output=json&pois=1&ak=42b8ececa9cd6fe72ae4cddd77c0da5d";
        Request okhttp = new Request.Builder()
                .url(sb.toString())
                .build();
        String result = execNewCall(okhttp);
        System.out.println(result+"result");
    }
}
