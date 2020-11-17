package com.xwkj.ygjces;

import com.xwkj.ygjces.Mapper.OrganizationInfoMapperManual;
import com.xwkj.ygjces.Mapper_oracle.JcCoreSampleMapperManual;
import com.xwkj.ygjces.Mapper_oracle.JcHtContractMapperManual;
import com.xwkj.ygjces.model.OrgItemIntermediate;
import com.xwkj.ygjces.model.UrlData;
import com.xwkj.ygjces.model.WeChatData;
import com.xwkj.ygjces.service.SendMsgService;
import com.xwkj.ygjces.service.WxService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpAppChatMessage;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.apache.shiro.dao.DataAccessException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@PropertySource(value = {"classpath:/config/wxConfig.properties"})
public class YgjcesApplicationTests {


    @Autowired
    WxService wxService;
    @Value("${wx.agent-id}")
    int agentid;
    @Autowired
    UrlData urlData;
    @Autowired
    SendMsgService sendMsgService;
    @Autowired
    WxService myWxService;

    @Test
    public void  test2() throws WxErrorException {
        WxCpMessage wxCpMessage = new WxCpMessage();
        wxCpMessage.setToUser("ZhangYongHui");
        wxCpMessage.setMsgType(WxConsts.MassMsgType.TEXT);
        wxCpMessage.setContent("123456789");
        myWxService.messageSend(wxCpMessage);
    }

    @Test
    public void test() throws WxErrorException, IOException {
//        WxCpUser zhangYongHui = wxService.getUserService().getById("ZhangYongHui");
//        System.out.println(zhangYongHui);
        WeChatData weChatData = new WeChatData();
        weChatData.setTouser("ZhangYongHui");
        weChatData.setMsgtype("text");
        weChatData.setAgentid(1000002);
        Map<String,Object> map = new HashMap<>();
        map.put("content","<a href=\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wwd3538fc9dc13f8e9&redirect_uri=http://z72cj9.natappfree.cc/wx/inputTask&response_type=code&scope=snsapi_base&state=wx/inputTask#wechat_redirect\">看啊看</a>");
        weChatData.setText(map);
        sendMsgService.post(weChatData);

    }
//    @Test
//    public void test(){
//        Set set = new HashSet();
//        set.add("aaaa");
//        set.add("aaaa");
//        set.add("bbbb");
//        set.add("cccc");
//        System.out.println(set.size());
//        for (Object o:set){
//            System.out.println(o);
//        }
//    }

   /* @Autowired
    DataSource primaryDataSource;

    @Autowired
    DataSource secondaryDataSource;*/
    @Autowired
    JcHtContractMapperManual jcHtContractMapperManual;

    /*@Autowired
    @Qualifier("primaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate1;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate2;
    @Test
    public void contextLoads() {
        System.out.println(primaryDataSource.getClass());
        System.out.println(secondaryDataSource.getClass());
        String sql = "SELECT * FROM tbl_gb_organization_info";
        List<Map<String, Object>> resObj = (List<Map<String, Object>>) jdbcTemplate2.execute(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement(sql);
            }
        }, new PreparedStatementCallback<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while(rs.next()){
                    System.out.println("==" + rs.getString(1));
                    System.out.println("==" + rs.getString(2));
                    System.out.println("==" + rs.getString(3));
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("id", rs.getString("id"));
                }
                return null;
            }
        } );
    }*/

}
