package com.xwkj.ygjces.controller;

import com.xwkj.ygjces.Mapper.OvertimeModelMapperManual;
import com.xwkj.ygjces.Mapper.RemindModelMapperManual;
import com.xwkj.ygjces.Mapper.UserInfoMapper;
import com.xwkj.ygjces.Mapper.auto.NoticeModelMapper;
import com.xwkj.ygjces.Mapper.auto.OvertimeModelMapper;
import com.xwkj.ygjces.Mapper.auto.RemindModelMapper;
import com.xwkj.ygjces.model.*;
import com.xwkj.ygjces.security.WxUserIdToken;
import com.xwkj.ygjces.service.UserInfoService;
import com.xwkj.ygjces.service.WxService;
import com.xwkj.ygjces.utils.AesException;
import com.xwkj.ygjces.utils.MD5Util;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.crypto.WxCryptUtil;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class WxController {

    private static Log log = LogFactory.getLog(WxController.class);

    @Autowired
    WxService wxService;
    @Autowired
    WxService myWxService;
    @Autowired
    WxConfigStorage wxConfigStorage;
    @Autowired
    NoticeModelMapper noticeModelMapper;
    @Autowired
    RemindModelMapperManual remindModelMapperManual;
    @Autowired
    OvertimeModelMapperManual overtimeModelMapperManual;
    @Autowired
    RemindModelMapper remindModelMapper;
    @Autowired
    OvertimeModelMapper overtimeModelMapper;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserInfoMapper userInfoMapper;

    /**
     * 企业微信验证url
     * @param msg_signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/checkWxMsg", method = RequestMethod.GET)
    public void checkWxMsg(String msg_signature, String timestamp, String nonce, String echostr, HttpServletResponse response) throws IOException {
        try {
            if (wxService.checkSignature(msg_signature, timestamp, nonce, echostr)) {
                WxCryptUtil cryptUtil = new WxCryptUtil(wxConfigStorage.getToken(), wxConfigStorage.getAesKey(), wxConfigStorage.getCorpId());
                String result = cryptUtil.decrypt(echostr);
                response.getWriter().write(result);
            } else {
                System.out.println("企业微信验证消息推送错误！");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 企业微信通讯录回调通知
     * @param request
     * @param response
     * @throws IOException
     * @throws AesException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    @PostMapping("/checkWxMsg")
    public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException, AesException, ParserConfigurationException, SAXException, WxErrorException {
        wxService.callbackNotification(request,response);
        response.getWriter().write("企业微信通讯录回调通知");
    }

    /**
     * 微信点击提示消息跳转的页面
     * @param id
     * @return
     */
    @GetMapping("/jumpPage/{id}")
    public String msg(@PathVariable("id") Long id, Map<String,Object> map){
        List<Object> finalList = new ArrayList<>();
        NoticeModel noticeModel = noticeModelMapper.selectByPrimaryKey(id);
        //修改该消息状态和查看时间
        if (noticeModel.getIsRender()==0){
            noticeModel.setIsRender(1);
            noticeModel.setRenderDate(new Date());
            noticeModelMapper.updateByPrimaryKeySelective(noticeModel);
        }


        if (noticeModel.getCategory()==1){
            List<RemindModel> list = remindModelMapperManual.getRemindModelListByCreateTimeAndStage(noticeModel.getCreateDate(), noticeModel.getStage());
            if (noticeModel.getStage()==1){
                List<String> itemIdsByUserId = userInfoMapper.getItemIdsByUserId(noticeModel.getUserid());
                for (RemindModel r : list) {
                    if(itemIdsByUserId.contains(r.getXmNum())){
                        finalList.add(r);
                    }
                }
            }else if (noticeModel.getStage()==2){
                List<String> itemIdsByAuditorId = userInfoMapper.getItemIdsByAuditorId(noticeModel.getUserid());
                for (RemindModel r : list){
                    if (itemIdsByAuditorId.contains(r.getXmNum())){
                        finalList.add(r);
                    }
                }
            }else if (noticeModel.getStage()==3){
                List<String> itemIdsByApproverId = userInfoMapper.getItemIdsByApproverId(noticeModel.getUserid());
                for (RemindModel r : list){
                    if (itemIdsByApproverId.contains(r.getXmNum())){
                        finalList.add(r);
                    }
                }
            }


        }else {
            List<OvertimeModel> list = overtimeModelMapperManual.getOvertimeModelListByUpdateDateAndStage(noticeModel.getCreateDate(), noticeModel.getStage());
            if (noticeModel.getStage()==1){
                List<String> itemIdsByUserId = userInfoMapper.getItemIdsByUserId(noticeModel.getUserid());
                for (OvertimeModel o : list){
                    if (itemIdsByUserId.contains(o.getXmId())){
                        finalList.add(o);
                    }
                }
            }else if (noticeModel.getStage()==2){
                List<String> itemIdsByAuditorId = userInfoMapper.getItemIdsByAuditorId(noticeModel.getUserid());
                for (OvertimeModel o : list){
                    if(itemIdsByAuditorId.contains(o.getXmId())){
                        finalList.add(o);
                    }
                }
            }else if (noticeModel.getStage()==3){
                List<String> itemIdsByApproverId = userInfoMapper.getItemIdsByApproverId(noticeModel.getUserid());
                for (OvertimeModel o : list){
                    if (itemIdsByApproverId.contains(o.getXmId())){
                        finalList.add(o);
                    }
                }
            }
        }

        map.put("noticeModel",noticeModel);
        map.put("list",finalList);
        System.out.println(finalList);

        return "wx/list";
    }

    /**
     * 点击样品显示样品详情信息
     * @param category
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/details")
    public String details(Integer category,Long id,Map<String,Object> map){

        if (category==1){
            RemindModel remindModel = remindModelMapper.selectByPrimaryKey(id);
            map.put("model",remindModel);
        }else {
            OvertimeModel overtimeModel = overtimeModelMapper.selectByPrimaryKey(id);
            map.put("model",overtimeModel);
        }

        map.put("category",category);

        return "wx/info";
    }

    /**
     * 微信身份验证
     * @param code
     * @param state
     * @param request
     * @param response
     * @return
     * @throws WxErrorException
     * @author zyh
     */
    @GetMapping("/wx/oauth")
    public String test1(String code, String state, HttpServletRequest request, HttpServletResponse response) throws WxErrorException {
        System.out.println(state);
        WxCpOauth2UserInfo userInfo = myWxService.getOauth2Service().getUserInfo(code);
        UserInfo userInfoByAcount = userInfoMapper.getUserInfoByAcount(userInfo.getUserId());
        System.out.println(userInfoByAcount);
        if (userInfoByAcount != null){
            request.getSession().setAttribute("user",userInfoByAcount.getId());
            UsernamePasswordToken token = new UsernamePasswordToken();
            token.setUsername(userInfoByAcount.getAccount());
            String password = userInfoByAcount.getPassword();
            token.setPassword(password.toCharArray());
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) {
                subject.login(token);
            }
            return "redirect:"+state;
        }
        return "403";
    }


}
