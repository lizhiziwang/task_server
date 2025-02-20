package com.zsh.task.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpRequestUtils {

    public static JSONObject get(String url, Map<String,Object> params){
        return JSONObject.parseObject(HttpUtil.get(url, params, 3600));
    }
    // 访问本地deepseek-r1
    //todo 图片等资源
    public static void ask_ds_r1_Model(OutputStream os1, Object prompt) throws IOException {
        String urlString = "http://localhost:11434/api/generate";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置请求方法为POST
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        // 创建要发送的JSON对象
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("model", "deepseek-r1");
        jsonInput.put("prompt", prompt);
        jsonInput.put("stream", true);
        // 将JSON输入写入请求的输出流
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        // 读取响应内容
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                JSONObject jsonResponse = JSONObject.parseObject(responseLine.trim());
//                System.out.print(jsonResponse.get("response"));
                os1.write(jsonResponse.get("response").toString().getBytes(StandardCharsets.UTF_8));
                os1.flush();
            }
            os1.close();
            // 解析JSON响应并提取response字段
//            JSONObject jsonResponse = new JSONObject(response.toString());
//            JSONObject jsonResponse = JSONObject.parseObject(response.toString());
//            return response.toString();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        Scanner sc = new Scanner(System.in);
//        while (true){
//            OutputStream os = new BufferedOutputStream(new ByteArrayOutputStream());
//            System.out.println("ask:");
//            String in = sc.nextLine();
//            System.out.println("deepspeek-r1:");
//            HttpRequestUtils.ask_ds_r1_Model(os,in);
////            System.out.println("deepspeek-r1:"+s);
//        }

//        for(int i = 0;i<10000;i++){
            String url =
                    "https://www.wjx.cn/joinnew/processjq.ashx?shortid=mHzbiyv&starttime=2025%2F2%2F20%2011%3A24%3A49&cst=1740021901414&submittype=1&ktimes=154&rn=1901384569.52647420&nw=1&jwt=16&jpm=70&t=1740021901426&wxfs=100&jqnonce=ecf206aa-c656-4e1d-b8be-ca29b1fac5c1&jqsign=agb642ee)g212)0a5%60)f%3Cfa)ge6%3Df5beg1g5";
            String cookie =
                    ".ASPXANONYMOUS=RX25t9G52wEkAAAAZDM1Nzc0M2YtODA3OC00N2RhLWIxZDUtMWY4NWZkOTdkZjQ2lYUaelWDwGOXLaLrOjDzm-DLn9E1; browserid=1a227efe-f2d7-45e1-8e0d-5004e11dac03; awardshowway10=1; awardshowway0=1; acw_tc=0a47308517400209628207479e014ae5dc0610c24c14c89c183227c200f0fa; LastActivityJoin=301808001,123088759823; join_301808001=1; awardshowway3=1; wjxawardload=14512%7C301808001; awardshowway12=1; lastshowway=12|2025-02-20 11:24; jac301808001=52647420; SERVERID=797658896bbfe7476f5f4103a02a2b4f|1740021889|1740020962; ssxmod_itna=Yq0xu7G=iQDQ3AKGHqGd6SiUDyDAEEO7hGAOxY5qoq4U=D/FIDnqD=GFDK40ooCNIAY4bqYiGaCafx=mc0mQ4oif8DQvB79G+eDHxY=DUOiu+ebDeeND5xGoDPxDeDADYo2DAqiOD7qDdRh5sZr5Dbxi3RxiaDGeDeo5kDY5DhxDC9DKDwx0CRpGx=WHqDGHYhG4D4Eqe3/Y7TUyIgO0wdjGCD7HvDlPeZtSp/4Cqx/A6B+YQjx0k=q0OZ6vG+6OPc=AsYbh50QG5b0axbGn4Ki43GAiBzQD5iQG4AD2dKGRYB9bokYzV=DDAC049Dx50DeD; ssxmod_itna2=Yq0xu7G=iQDQ3AKGHqGd6SiUDyDAEEO7hGAOxY5qoq4tG9zTYixBwde7pheqnlPiK7Db6Kglmqh2CoWUGObiQxnR4GT6IwSGm3fOZYaNH=QDl4qkZA9QBsCgAlQy6eIwtyrCGT4gAxzeaOKWTbqDYWqjTOv6B1d1ab1WmCmAWhVenTcDrtRgI8H2rh+678P1Pa6gBFdSIEYri=dfp5uffvzDx5cA8koBMFzbe7tooxiOpvV7GRF4Y1fclO560LvgiT7SdCeLA8CcY6R68kP9cFxRgck6GTUgea0THOoe6Roh4m9W5eypPN1oyQNZG5LetShKa=0SaL4C0y=j2OpMiPYBPEg3Ya=Hr6fmt4IeU02/BtjpvSCLfAi2AIEA3WffQ3j2GmKidH2606AaRTp7ws4TyEmG1eLcxsgpRDPcC34Wm8x3AYKPfp4b0Chv7ppRjqNnNrcQpgnN+0I7aY+4irvxY5Te2zbj9UqFpv=r0P=HT8cLjvza5Lnj+CaeEmO1aioA7xh4gPGBw8BqGlr4jAxlrWl3MIDNPb5o02geW86kFpLQGUWpuZ4DQIqYGGF0aSjvVqnSp9b0Q9r4DktirwZP62rXgcvG+UarXr2mI20kRRgruxzf9ui92xG5rb/A0zxWitbIXtb4bZTBGT91hI1TtXubXBT/rW2CE/ZjrMLVCAz0x08DG7Bn4BxPi=G4xTi0xeBx9dZRji3cDUeQoiUpGKRjeYnR=Qdvh5eQ45x+TeGDD===; tfstk=gK8ZK4idLV3N6eTZn6b4YfoAwa_OlNDSgE6fiIAc1OXg5PwDYQv_hiD9GprDd9I6HoixuqAPdfU1lrN9vLOu5FiOlr7OkZDSF4gW1CQAo5QzdByt-sfrl-qGnNQHyeqLt4gW6Cx-G8vKPFZka26OnZjgokfhps1gSKjctX5cGRXcIKAnT9CYssjGj64hGsUcoZvc-wXdg-bcjbyXsI4FNGc2beI7yRo1jTAGLr4zwssgSIqbDoHVgN6lx6UcOe5NbTAM5Qwb56YB8gsQGv_HwH9GTauae1-HaNf2kbqV3i8O89JrrrCpjd8h0eGxzB_NggbcYS4DTZx9ee7orJ5pxCtDWdP4o1L6PiWRYj4AcwY5q3vaMbdhSsvA2EM8WOADwUK5zVyR_HAP8g-YH626LEKanoSG96WSTXz1jdIpZY9HGoEAxaCFFf1_DoIG9rZwzzrYDMXdTTG11";

            String body = "submitdata=1%241%7D2%242%7D3%244%7D4%242%7D5%241%7D6%241%7D7%241%7D8%241%7D9%241%7D10%241%7C3%7D11%24wu%7D12%241%7D13%244";
            HttpResponse response = HttpRequest.post(url)
                    .header("content-type","application/x-www-form-urlencoded; charset=UTF-8")
                    .header("cookie",cookie)
                    .body(body)
                    .execute();
            System.out.println(response);
            Thread.sleep(100);
//        }

//        System.out.println(response.body());

    }

    public static JSONObject get(String url){
        return JSONObject.parseObject(HttpUtil.get(url, 3600));
    }

//    public static void main(String[] args) {
//        Map<String,Object> params = new HashMap<>();
//        params.put("key","17be85abdc35ac0635cfcfe31fe10936");
//        params.put("location","113.9395,22.5827");
//        get("https://restapi.amap.com/v3/geocode/regeo",params);
//    }
}
