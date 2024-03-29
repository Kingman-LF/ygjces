package com.xwkj.ygjces.model;

import java.io.File;

import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.common.util.http.apache.ApacheHttpClientBuilder;
import me.chanjar.weixin.cp.config.WxCpConfigStorage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource(value = {"classpath:/config/wxConfig.properties"})
@ConfigurationProperties(prefix = "wx")
@Component
public class WxConfigStorage extends WxCpDefaultConfigImpl implements WxCpConfigStorage {
	  private String wxSecret;
	  protected volatile String corpId;
	  protected volatile String corpSecret;

	  protected volatile String token;
	  protected volatile String accessToken;
	  protected volatile String aesKey;
	  protected volatile Integer agentId;
	  protected volatile long expiresTime;

	  protected volatile String oauth2redirectUri;

	  protected volatile String httpProxyHost;
	  protected volatile int httpProxyPort;
	  protected volatile String httpProxyUsername;
	  protected volatile String httpProxyPassword;

	  protected volatile String jsapiTicket;
	  protected volatile long jsapiTicketExpiresTime;

	  protected volatile File tmpDirFile;
	  private volatile String baseApiUrl;

	  private volatile ApacheHttpClientBuilder apacheHttpClientBuilder;

	public void setBaseApiUrl(String baseUrl) {
		this.baseApiUrl = baseUrl;
	}

	public String getApiUrl(String path) {
		if (this.baseApiUrl == null) {
			this.baseApiUrl = "https://qyapi.weixin.qq.com";
		}

		return this.baseApiUrl + path;
	}

	@Override
	  public String getAccessToken() {
	    return this.accessToken;
	  }

	  public void setAccessToken(String accessToken) {
	    this.accessToken = accessToken;
	  }

	  @Override
	  public boolean isAccessTokenExpired() {
	    return System.currentTimeMillis() > this.expiresTime;
	  }

	  @Override
	  public void expireAccessToken() {
	    this.expiresTime = 0;
	  }

	  @Override
	  public synchronized void updateAccessToken(WxAccessToken accessToken) {
	    updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
	  }

	  @Override
	  public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
	    this.accessToken = accessToken;
	    this.expiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000l;
	  }

	  @Override
	  public String getJsapiTicket() {
	    return this.jsapiTicket;
	  }

	  public void setJsapiTicket(String jsapiTicket) {
	    this.jsapiTicket = jsapiTicket;
	  }

	  public long getJsapiTicketExpiresTime() {
	    return this.jsapiTicketExpiresTime;
	  }

	  public void setJsapiTicketExpiresTime(long jsapiTicketExpiresTime) {
	    this.jsapiTicketExpiresTime = jsapiTicketExpiresTime;
	  }

	  @Override
	  public boolean isJsapiTicketExpired() {
	    return System.currentTimeMillis() > this.jsapiTicketExpiresTime;
	  }

	  @Override
	  public synchronized void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
	    this.jsapiTicket = jsapiTicket;
	    // 预留200秒的时间
	    this.jsapiTicketExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000l;
	  }

	@Override
	  public void expireJsapiTicket() {
	    this.jsapiTicketExpiresTime = 0;
	  }

	  @Override
	  public String getCorpId() {
	    return this.corpId;
	  }

	  public void setCorpId(String corpId) {
	    this.corpId = corpId;
	  }

	  @Override
	  public String getCorpSecret() {
	    return this.corpSecret;
	  }

	  public void setCorpSecret(String corpSecret) {
	    this.corpSecret = corpSecret;
	  }

	  @Override
	  public String getToken() {
	    return this.token;
	  }

	  public void setToken(String token) {
	    this.token = token;
	  }

	  @Override
	  public long getExpiresTime() {
	    return this.expiresTime;
	  }

	  public void setExpiresTime(long expiresTime) {
	    this.expiresTime = expiresTime;
	  }

	  @Override
	  public String getAesKey() {
	    return this.aesKey;
	  }

	  public void setAesKey(String aesKey) {
	    this.aesKey = aesKey;
	  }

	  @Override
	  public Integer getAgentId() {
	    return this.agentId;
	  }

	  public void setAgentId(Integer agentId) {
	    this.agentId = agentId;
	  }

	  @Override
	  public String getOauth2redirectUri() {
	    return this.oauth2redirectUri;
	  }

	  public void setOauth2redirectUri(String oauth2redirectUri) {
	    this.oauth2redirectUri = oauth2redirectUri;
	  }

	  @Override
	  public String getHttpProxyHost() {
	    return this.httpProxyHost;
	  }

	  public void setHttpProxyHost(String httpProxyHost) {
	    this.httpProxyHost = httpProxyHost;
	  }

	  @Override
	  public int getHttpProxyPort() {
	    return this.httpProxyPort;
	  }

	  public void setHttpProxyPort(int httpProxyPort) {
	    this.httpProxyPort = httpProxyPort;
	  }

	  @Override
	  public String getHttpProxyUsername() {
	    return this.httpProxyUsername;
	  }

	  public void setHttpProxyUsername(String httpProxyUsername) {
	    this.httpProxyUsername = httpProxyUsername;
	  }

	  @Override
	  public String getHttpProxyPassword() {
	    return this.httpProxyPassword;
	  }

	  public void setHttpProxyPassword(String httpProxyPassword) {
	    this.httpProxyPassword = httpProxyPassword;
	  }

	  @Override
	  public File getTmpDirFile() {
	    return this.tmpDirFile;
	  }

	  public void setTmpDirFile(File tmpDirFile) {
	    this.tmpDirFile = tmpDirFile;
	  }

	  @Override
	  public ApacheHttpClientBuilder getApacheHttpClientBuilder() {
	    return this.apacheHttpClientBuilder;
	  }

	  public void setApacheHttpClientBuilder(ApacheHttpClientBuilder apacheHttpClientBuilder) {
	    this.apacheHttpClientBuilder = apacheHttpClientBuilder;
	  }

		public String getWxSecret() {
			return wxSecret;
		}
		
		public void setWxSecret(String wxSecret) {
			this.wxSecret = wxSecret;
		}
	
	
}
