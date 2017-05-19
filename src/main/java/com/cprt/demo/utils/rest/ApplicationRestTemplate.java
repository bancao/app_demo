package com.cprt.demo.utils.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ApplicationRestTemplate extends RestTemplate {
	
	public ApplicationRestTemplate() {
		super();
	}
	public ApplicationRestTemplate(ClientHttpRequestFactory requestFactory) {
		super(requestFactory);
	}
	private String failover = "http://localhost/";

	public void setFailover(String failover) {
		this.failover = failover;
	}
	
	public String getHost(){
		String [] hosts = failover.split(",");
		Integer random = (int) (hosts.length * Math.random());
		return hosts[random];
	}
	@Override
	public <T> ResponseEntity<T> getForEntity(String  url, Class<T> responseType, Map<String, ?> urlVariables) throws RestClientException {
		return super.getForEntity(getHost() + url, responseType, urlVariables);
	}
	@Override
	public <T> ResponseEntity<T> getForEntity(String  url, Class<T> responseType, Object... urlVariables) throws RestClientException {
		return super.getForEntity(getHost() + url, responseType, urlVariables);
	}
	@Override
	public <T> T getForObject(String  url, Class<T> responseType, Map<String, ?> urlVariables) throws RestClientException {
		return super.getForObject(getHost() + url, responseType, urlVariables);
	}
	@Override
	public <T> T getForObject(String  url, Class<T> responseType, Object... urlVariables) throws RestClientException {
		return super.getForObject(getHost() + url, responseType, urlVariables);
	}
	@Override
	public URI postForLocation(String  url, Object request, Map<String, ?> urlVariables) throws RestClientException {
		return super.postForLocation(getHost() + url, request, urlVariables);
	}
	@Override
	public URI postForLocation(String  url, Object request, Object... urlVariables) throws RestClientException {
		return super.postForLocation(getHost() + url, request, urlVariables);
	}
	@Override
	public URI postForLocation(URI  url, Object request) throws RestClientException {
		return super.postForLocation(getHost() + url, request);
	}
	@Override
	public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
		return super.postForObject(getHost() + url, request, responseType, uriVariables);
	}
	@Override
	public <T> T postForObject(String  url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
		return super.postForObject(getHost() + url, request, responseType, uriVariables);
	}

	public <T> T postForObjectWithHeaders(String url, Object responseType, Map<String, Object> uriVariables,Map<String,String> headerMap) {
		
		MultiValueMap<String,String> headers = new LinkedMultiValueMap<String, String>();
		for(String key : headerMap.keySet()){
			headers.put(key, Arrays.asList(headerMap.get(key)));
		}
		
		return postForObjectOriginal(url, responseType, uriVariables, headers);
	}
	
    @SuppressWarnings("unchecked")
    public <T> T postForObject(String url, Object responseType, Map<String, Object> uriVariables) {

        HttpClient httpClient = new DefaultHttpClient();

        List<NameValuePair> pairs = null;
        if (uriVariables != null && !uriVariables.isEmpty()) {
            pairs = new ArrayList<NameValuePair>(uriVariables.size());
            for (Map.Entry<String, Object> entry : uriVariables.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value.toString()));
                }
            }
        }
        HttpPost method = new HttpPost(this.getHost() + url);
        try {
            if (pairs != null && pairs.size() > 0) {
                method.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            }
            HttpResponse response = httpClient.execute(method);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity);
            logger.info(method.getURI()+"接口返回信息:"+body);
            try {
                if ( responseType.equals(JSONArray.class)){
                    return (T) JSONArray.fromObject(body);
                }else if ( responseType.equals(JSONObject.class)){
                    return (T) JSONObject.fromObject(body);
                }else if (responseType.equals(String.class)){
                	return (T) body;
                }
            } catch (Exception e) {
                Map<String,Object> r = new HashMap<String,Object>();
                Map<String,Object> r1 = new HashMap<String,Object>();
                r1.put("result", false);
                r.put("jsonStr", r1);
                logger.error("接口ERROR"+body);
                return (T)JSONObject.fromObject(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            //method.releaseConnection();
        }
        return null;
    }
    
	public <T> T postForObjectOriginal(String url, Object responseType, Map<String, Object> params,
			MultiValueMap<String, String> headers) {
		
		HttpHeaders httpHeader = new HttpHeaders();
		httpHeader.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.putAll(httpHeader);
		
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>(); 
		for(String key : params.keySet()){
			body.add(key, String.valueOf(params.get(key)));
		}
		
		org.springframework.http.HttpEntity<?> request = new org.springframework.http.HttpEntity<Object>(body, headers);
		
		try{
			T result = super.postForObject(getHost() + url, request, (Class<T>)responseType);
			return result;
		}catch(Exception e){
			logger.error("请求失败",e);
			return null;
		}
		
	}
}
