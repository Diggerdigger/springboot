package com.august.thirteen.web;

import com.alibaba.fastjson.JSONObject;
import com.august.thirteen.dto.AcessTokenDto;
import com.august.thirteen.dto.Userdto;
import okhttp3.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @GetMapping(value = "/callback" )
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state) throws IOException {

        AcessTokenDto acessToken = new AcessTokenDto();
        acessToken.setClient_id("a90828f5dc0af80a2e7c");
        acessToken.setClient_secret("ba982bc485859f5cf1e314433e09b5c98f111c58");
        acessToken.setCode(code);
        acessToken.setState(state);
        acessToken.setRedirect_uri("http://localhost:8081/callback");

        String token = getAccessToken(acessToken);
        Userdto user = getUserByToken(token);
        System.out.println(user.getLogin());

        return "index";
    }


    private String getAccessToken(AcessTokenDto acessToken){
        Object json = JSONObject.toJSON(acessToken);
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
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
                .url("https://api.github.com/user?access_token="+token)
                .build();

        try (Response response = client.newCall(request1).execute()) {
            String userinfo = response.body().string();
            System.out.println(userinfo);
            Userdto user = JSONObject.parseObject(userinfo, Userdto.class);
            return user;
        }
    }
}
