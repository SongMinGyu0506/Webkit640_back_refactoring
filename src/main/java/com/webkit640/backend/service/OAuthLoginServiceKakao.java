package com.webkit640.backend.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.webkit640.backend.config.exception.OAuthLoginException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class OAuthLoginServiceKakao implements OAuthLoginService{
    @Value("${Kakao-oAuth-key}")
    private String kakaoRestApiKey;
    @Override
    public String getAccessToken(String code) {
        String accessToken = "";
        String refreshToken;
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqUrl);
            var conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id="+kakaoRestApiKey +
                    "&redirect_uri=http://www.webkit640.com/auth/oauth/kakao/" +
                    "&code=" + code;
            bw.write(sb);
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line="";
            String result="";

            while((line = br.readLine()) != null) {
                result += line;
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        } catch (Exception e) {
            throw new OAuthLoginException("OAuth Access Token Error");
        }

        return accessToken;
    }

    @Override
    public String accessUser(String token) {
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        String email = "";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization","Bearer "+token);

            int responseCode = conn.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            int id = element.getAsJsonObject().get("id").getAsInt();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();

            if(hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }
}
