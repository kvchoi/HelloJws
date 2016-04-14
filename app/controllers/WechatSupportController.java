package controllers;

import java.io.File;
import java.io.IOException;

import jws.Jws;
import jws.mvc.Controller;
import jws.mvc.Http.Request;
import jws.mvc.Scope;
import jws.mvc.With;
import mpsdk4j.common.OAuthScope;
import mpsdk4j.core.WechatKernel;
import mpsdk4j.core.WechatMsgDefHandler;
import mpsdk4j.util.ConfigReader;
import mpsdk4j.vo.MPAccount;
import mpsdk4j.vo.api.AccessToken;
import mpsdk4j.vo.api.UserInfo;

import org.apache.commons.lang.StringUtils;

import services.WechatSupportService;

/**
 * 
 * 微信公众平台交互接口<br>
 * 更多详情参见 {@link <a href="http://mp.weixin.qq.com/wiki/home/index.html">公众平台开发者文档</a>}
 *
 * @author cairf@ucweb.com
 * @date 2016年1月18日
 */
@With(Handler.class)
public class WechatSupportController extends Controller {

    protected static final WechatKernel _wk;
    static {
        _wk = init();
    }

    /**
     * 公众号的消息处理器<br>
     * (!!!实际生产中需要重写此方法内的数据，以正确响应服务器消息推送!!!)
     * <ul>
     * <li>开发者的微信公众号信息：{@link MPAccount}</li>
     * <li>微信消息处理器：{@link WechatMsgDefHandler}</li>
     * </ul>
     */
    private static WechatKernel init() {
        ConfigReader _cr = new ConfigReader(new File(Jws.applicationPath, "conf/mp.properties"));
        MPAccount mpact = new MPAccount();
        mpact.setMpId(_cr.get("mpId"));
        mpact.setAppId(_cr.get("appId"));
        mpact.setAppSecret(_cr.get("appSecret"));
        mpact.setToken(_cr.get("token"));
        mpact.setAESKey(_cr.get("aesKey"));
        WechatKernel _wk = new WechatKernel();
        _wk.setMpAct(mpact);
        _wk.setWechatHandler(new WechatMsgDefHandler());
        return _wk;
    }

    /**
     * 与微信服务器互动
     * 
     * <pre/>
     * !!!实际生产中写个入口方法调用即可!!!
     * 
     * @param req 微信服务器请求
     * @param resp 响应微信服务器
     * 
     */
    public static void interact() {
        _wk.setParams(Scope.Params.current().all());
        String respmsg = "success";
        // 初始GET校验：http://mp.weixin.qq.com/wiki/8/f9a0b8382e0b77d87b3bcc1ce6fbc104.html
        if ("GET".equalsIgnoreCase(Request.current().method)) {
            respmsg = _wk.check();
        } else {
            // 后续公众平台转发数据POST：
            respmsg = _wk.handle(Request.current().body);
        }
        // 输出回复消息，应该在5秒钟内响应
        renderText(respmsg);
    }

    // #################################################################################
    /**
     * oauth:公众平台回调<br>
     * <ul>
     * <li>如果用户同意授权，页面将跳转至： <b>redirect_uri/?code=CODE&state=STATE</b></li>
     * <li>若用户禁止授权，则重定向后不会带上code参数，仅会带上state参数：<b>redirect_uri?state=STATE</b></li>
     * </ul>
     * 
     * @param code 用户授权码
     * @param state 认证前的state信息
     */
    public static void oauth(String code, String state) {
        String respmsg = "success";
        if (StringUtils.isEmpty(code)) {
            System.out.println("user not allow");
        } else {
            System.out.println("code is " + code);
            AccessToken at = WechatSupportService.getUserAccessToken(code);
            if (at == null) {
                System.out.println("no access token");
            } else if (StringUtils.equalsIgnoreCase(OAuthScope.snsapi_userinfo.name(),
                    at.getScope())) {
                UserInfo ui = WechatSupportService.getUserInfo(at.getOpenid());
                System.out.println(ui);
            } else {
                System.out.println("base auth scope");
            }
        }
        renderText(respmsg);
    }

    public static void getSnsapiBaseUrl() {
        renderText(WechatSupportService.getSnsApiBaseUrl());
    }

    public static void getSnsapiUserInfoUrl() {
        renderText(WechatSupportService.getSnsApiUserInfoUrl());
    }

    public static void getApiAccessToken() {
        System.out.println(WechatSupportService.getAPIAccessToken());
    }

    // 01104516cc3646b8eccbf3363b6b6bcy
    public static void getAccessTokenByCode(String code) {
        renderText(WechatSupportService.getUserAccessToken(code));
    }

    public static void getUserInfo(String openid) {
        openid = StringUtils.isEmpty(openid) ? "o8D1Vw2F4oPEzMUYkfmUSROYH12Q" : openid;
        renderJSON(WechatSupportService.getUserInfoByOpenid(openid));
    }
}
