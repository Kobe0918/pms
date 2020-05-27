package com.mjrj.lzh.pms.activemq;


import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.mjrj.lzh.pms.redis.RedisComponent;
import com.mjrj.lzh.pms.util.CommonUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.util
 * @Author: lzh
 * @CreateTime: 2020-03-04 20:05
 * @Description: ${Description}
 */
@Component
@ConfigurationProperties(prefix = "aliyun")
@Getter
@Setter
@Slf4j
public class AliSendMessage {
    /**
     * 读取properties/yml文件的两种方法
     * 1.加如下注解读取
     * @Component
     * @ConfigurationProperties(prefix = "aliyun") --配置文件中的前缀
     * @Getter
     * @Setter
     *
     * 2. @Value("${aliyun.regionId}")
     *    private String regionId;
     */

//  @Value("${aliyun.regionId}")
    private String regionId;
    private String accessKeyId;
    private String secret;
    private String signName;
    private String templateCode;
    private static String doMain = "dysmsapi.aliyuncs.com";
    private static String version = "2017-05-25";
    private static String action = "SendSms";

    @Autowired
    private RedisComponent redisComponent;

    /**
     * 发送短信
     * @param phoneNumber  电话号码
     * @param code          短信验证码(6位)
     */
    public  String  sendMessage(String phoneNumber,String code){
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setMethod(MethodType.POST);
        commonRequest.setDomain(doMain);
        commonRequest.setVersion(version);
        commonRequest.setAction(action);
        commonRequest.putQueryParameter("RegionId", regionId);
        commonRequest.putQueryParameter("PhoneNumbers", phoneNumber);
        commonRequest.putQueryParameter("SignName", signName);
        commonRequest.putQueryParameter("TemplateCode", templateCode);
        commonRequest.putQueryParameter("TemplateParam", "{code:"+ code +"}");
        try {
            CommonResponse response = client.getCommonResponse(commonRequest);
            String returnMessage = response.getData();
            SmsResponse smsResponse = JSON.parseObject(returnMessage,SmsResponse.class);
            if(!"ok".equalsIgnoreCase(smsResponse.code)){
              return response.getData();
            }else{
              redisComponent.addStringAndExpire(CommonUtil.PHONE_PRE + phoneNumber,code,120);
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Data
    public static class SmsResponse{
        private String code;
        private String message;
        private String  requestId;
        private String bizId;
    }

}
