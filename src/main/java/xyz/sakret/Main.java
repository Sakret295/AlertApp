package xyz.sakret;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import xyz.zaddrot.Constance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Author: Starikov Vyacheslav
 * Department: 8011
 * Date: 18.01.19 14:47
 */
public class Main {

    public static void main(String[] args) {
        //StrawPool sp = new StrawPool();
        //String[] option = getUser().getMessage().replaceAll("!poll ","").split(" ");
        //sp.CreatePool(option);
        String[] op = new String[]{"www", "wwq"};
        CreatePool(op);
    }
    public static void CreatePool(String[] options) {
        long id;
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            //HttpPost request = new HttpPost("http://www.strawpoll.me/api/v2/polls");
            HttpPost request = new HttpPost("https://strawpoll.me/api/v2/polls");

            JSONArray array = new JSONArray();

            array.addAll(Arrays.asList(options));

            JSONObject obj = new JSONObject();

            obj.put("title", "Тестовый пул #" + Calendar.getInstance().getTimeInMillis() + " created by Alice");
            obj.put("options",array);
            obj.put("multi",true);

           // obj.put("captcha",false);


            StringEntity params = new StringEntity(obj.toJSONString(), "UTF-8");
            request.addHeader("Content-Type", "application/json");
            request.setEntity(params);

            System.err.println(request);
            HttpResponse response = httpClient.execute(request);

            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String inputLine;
            StringBuffer sb = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();

            JSONParser parser = new JSONParser();
            JSONObject entity;

            try {
                entity = (JSONObject) parser.parse(sb.toString());
                id = (long) entity.get("id");
                System.err.println("Голосование создано и доступно по ссылке - http://www.strawpoll.me/" + id);
            } catch (ParseException e) { e.printStackTrace(); }
        }catch (IOException ex) {ex.printStackTrace();}
    }

}
