package com.august.thirteen.web;

import com.alibaba.fastjson.JSONObject;
import com.august.thirteen.dto.AcessTokenDto;
import com.august.thirteen.dto.Userdto;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Value("${acessToken.setClient_id}")
    private String setClient_id ;
    @Value("${acessToken.setClient_secret}")
    private String setClient_secret ;
    @Value("${acessToken.setRedirect_uri}")
    private String setRedirect_uri ;
    @Value("${access_token.uri}")
    private String accessUri ;
    @Value("${userApi.uri}")
    private String userUri ;

    @GetMapping(value = "/callback" )
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request
                           ) throws IOException {

        AcessTokenDto acessToken = new AcessTokenDto();
        acessToken.setClient_id(setClient_id);
        acessToken.setClient_secret(setClient_secret);
        acessToken.setCode(code);
        acessToken.setState(state);
        acessToken.setRedirect_uri(setRedirect_uri);

        String token = getAccessToken(acessToken);
        Userdto user = getUserByToken(token);
        System.out.println(user.getLogin());
        if (user!=null){
            request.getSession().setAttribute("user",user);
            return "redirect:/";
        }else{
            return "redirect:/";
        }
    }


    private String getAccessToken(AcessTokenDto acessToken){
        Object json = JSONObject.toJSON(acessToken);
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(accessUri)
                .post(body)
                .build();
        String token=null;
        try (Response response = client.newCall(request).execute()) {
            token = response.body().string();
            System.out.println(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] rs = token.split("&");
        String[] acToken = rs[0].split("=");
        System.out.println(acToken[1]);
        return acToken[1];
    }

    private Userdto getUserByToken(String token) throws IOException{
        Request request1 = new Request.Builder()
                .url(userUri+"?access_token="+token)
                .build();

        try (Response response = client.newCall(request1).execute()) {
            String userinfo = response.body().string();
            System.out.println(userinfo);
            Userdto user = JSONObject.parseObject(userinfo, Userdto.class);
            return user;
        }
    }
}
