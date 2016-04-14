package services;

import java.io.File;

import jws.Jws;
import mpsdk4j.api.WechatAPI;
import mpsdk4j.api.WechatAPIImpl;
import mpsdk4j.util.ConfigReader;
import mpsdk4j.vo.MPAccount;
import mpsdk4j.vo.api.AccessToken;
import mpsdk4j.vo.api.UserInfo;
import utils.MCSessionCache;

public class WechatSupportService {

    protected static final WechatAPI whchatApi;
    static {
        whchatApi = init();
    }

    private static WechatAPI init() {
        ConfigReader _cr = new ConfigReader(new File(Jws.applicationPath, "conf/mp.properties"));
        MPAccount mpact = new MPAccount();
        mpact.setMpId(_cr.get("mpId"));
        mpact.setAppId(_cr.get("appId"));
        mpact.setAppSecret(_cr.get("appSecret"));
        mpact.setToken(_cr.get("token"));
        mpact.setAESKey(_cr.get("aesKey"));
        WechatAPI impl = WechatAPIImpl.create(mpact);
        ((WechatAPIImpl) impl).setCache(new MCSessionCache());
        return impl;
    }

    public static String getAPIAccessToken() {
        return whchatApi.getAccessToken();
    }

    public static String getSnsApiBaseUrl() {
        // String redirectUri = Request.current().getBase() + "/wx/oauth";
        String redirectUri = "http://androidppproxy.dev.uae.uc.cn/wx/oauth";
        String state = "base";
        return whchatApi.snsApiBase(redirectUri, state);
    }

    public static String getSnsApiUserInfoUrl() {
        // String redirectUri = Request.current().getBase() + "/wx/oauth";
        String redirectUri = "http://androidppproxy.dev.uae.uc.cn/wx/oauth";
        String state = "userinfo";
        return whchatApi.snsApiUserInfo(redirectUri, state);
    }

    public static AccessToken getUserAccessToken(String code) {
        AccessToken at = whchatApi.getAccessTokenByCode(code);
        return at;
    }

    public static UserInfo getUserInfoByOpenid(String openid) {
        return whchatApi.getUserInfo(openid);
    }

    public static UserInfo getUserInfo(String openid) {
        return whchatApi.getUserInfo(openid);
    }

}
