package com.demo.auth.util;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateIndustry;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;import java.text.SimpleDateFormat;
import java.util.*;

public class SubscriptionMessageUtil {

    public static void main(String[] args) throws Exception{
        Map<String, Object> map = new HashMap<>();
        List<WxMpTemplateData> wxMpTemplateDataList = Arrays.asList(
                new WxMpTemplateData("first", "您有一个新的订货单", "#000000"),
                new WxMpTemplateData("keyword1", "sdf"),
                new WxMpTemplateData("keyword2", "serviceName"),
                new WxMpTemplateData("keyword3", "timeNow"),
                new WxMpTemplateData("remark", "请登录系统查看订单详情并及时配货")
        );
        map.put("data", wxMpTemplateDataList);

        System.out.println(map);
    }
    /**
     * 下单后通知供货商
     */
    public static void sendOrderMsg(String appid,
                                    String appSecret,
                                    String userOpenid,
                                    String orderId,
                                    String serviceName) {
        // 模板消息 ID
        // {{first.DATA}}
        // 订单编号：{{keyword1.DATA}}
        // 订货终端：{{keyword2.DATA}}
        // 下单时间：{{keyword3.DATA}}
        // {{remark.DATA}}
        //access_token  67_7B0gJS2JLEgW_XztH_PLlTCTjeuCAOyxDW4g-JmvZRom-a32vchkusF5NtWwmw2yJ6jhKesdba3qCxTZW1BofALJPKNtGmELmUoWrPApHFk8CKz5tRGd3ZRmxAMZCWaACACFZ
        String OrderMsgTemplateId = "Hryfj7GhPleRrvqheLADk1k8O2wSCnsmxI5d1DenGtM";
        // 卡片详情跳转页，设置此值，当点击消息时会打开指定的页面
        //String detailUrl = "https://bing.com";
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm");
        Date date = new Date();
        String timeNow = sdf.format(date);
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId(appid);
        wxStorage.setSecret(appSecret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        // 此处的 key/value 需和模板消息对应
        List<WxMpTemplateData> wxMpTemplateDataList = Arrays.asList(
                new WxMpTemplateData("first", "您有一个新的订货单", "#000000"),
                new WxMpTemplateData("keyword1", orderId),
                new WxMpTemplateData("keyword2", serviceName),
                new WxMpTemplateData("keyword3", timeNow),
                new WxMpTemplateData("remark", "请登录系统查看订单详情并及时配货")
        );
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(userOpenid)
                .templateId(OrderMsgTemplateId)
                .data(wxMpTemplateDataList)
//                .url(detailUrl)
                .build();
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);

        } catch (Exception e) {
            System.out.println("推送失败：" + e.getMessage());
        }

    }

    public static void delOrderMsg(){
        WxMpService wxMpService = new WxMpServiceImpl();
        try {
            wxMpService.getTemplateMsgService().delPrivateTemplate("111111111");
        } catch (Exception e) {
            System.out.println("删除失败：" + e.getMessage());
        }
    }

    public static void allOrderMsg() throws WxErrorException {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.getTemplateMsgService().getAllPrivateTemplate();

    }

    public static void addTemplate(){
        WxMpService wxMpService = new WxMpServiceImpl();
        try {
            wxMpService.getTemplateMsgService().addTemplate("931246");
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    public static WxMpTemplateIndustry getIndustry() throws Exception{
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpTemplateIndustry industry = wxMpService.getTemplateMsgService().getIndustry();

        return industry;
    }


}