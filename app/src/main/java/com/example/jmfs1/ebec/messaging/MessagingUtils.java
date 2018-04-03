package com.example.jmfs1.ebec.messaging;

import android.util.Log;
import android.widget.Toast;

import com.example.jmfs1.ebec.MainApplication;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pushraven.Notification;
import com.pushraven.Pushraven;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by root on 3/5/17.
 */

public class MessagingUtils {

    public static void subscribeTopic(String team) {

        switch (team) {
            case "mo":
            case "board":
            case "mgmt":
                FirebaseMessaging.getInstance().subscribeToTopic("core-team");
                FirebaseMessaging.getInstance().subscribeToTopic("topic-group");
                FirebaseMessaging.getInstance().subscribeToTopic("organisers");
                break;
            case "core-team":
                FirebaseMessaging.getInstance().subscribeToTopic("core-team");
                FirebaseMessaging.getInstance().subscribeToTopic("organisers");
                break;
            case "topic-group":
                FirebaseMessaging.getInstance().subscribeToTopic("topic-group");
                FirebaseMessaging.getInstance().subscribeToTopic("organisers");
                break;
            case "organisers":
                FirebaseMessaging.getInstance().subscribeToTopic("organisers");
                break;
            default:
                FirebaseMessaging.getInstance().subscribeToTopic("team-" + team);
        }

    }

    public static void sendMessageNotNotification(String teamName, String topic)
    {
        // Set headers
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization",
                "key=AAAAAo9oPmQ:APA91bHlUO_JuMwr5uKNQQdx636QGUImEzGbYq6czpYMCCNTpNCUfDHixRXl-vDQdDs_" +
                        "KYtk39TGI8xVGVzHYMa2hTykqmg0SjBvDoGk37wpIQMXBQA464r9yiEHVsI_pt-IOtxlnTUJ");
        client.addHeader("Content-Type", "application/json");

        StringEntity entity;
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("TEAMNAME", teamName);
            entity = new StringEntity(getPostData(map , topic));
            if (entity == null) throw new Exception();
        }
        catch (Exception e) {
            // deu erro. Retornar e mostrar um toast
            // TODO mostrar toast em caso de erro
            e.printStackTrace();
            return;
        }

        client.post(MainApplication.getContext(), "https://fcm.googleapis.com/fcm/send",
                entity, "application/json", new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                        // Handle resulting parsed JSON response here
                        Toast.makeText(MainApplication.getContext(), "Mensagem enviada com sucesso", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess (int statusCode, Header[] headers, JSONArray response)
                    {
                        Toast.makeText(MainApplication.getContext(), "Mensagem enviada com sucesso", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t)
                    {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.d("Messages", "an error ocurred");
                        Toast.makeText(MainApplication.getContext(), "Erro no envio da mensagem", Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });
    }

    public static String getPostData(HashMap<String, String> hashMap, String topic)
    {
        try
        {
            JSONObject payloadObject = new JSONObject();


            JSONObject data = new JSONObject(hashMap);
            payloadObject.put("data", data);
            payloadObject.put("to", "/topics/"+topic);

            Log.d("Messages", String.format("Message to send : %s", payloadObject.toString()));

            return payloadObject.toString();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }


    }

    public static void sendMessage(final String message, final String topic) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Pushraven.setKey("AAAAAo9oPmQ:APA91bHlUO_JuMwr5uKNQQdx636QGUImEzGbYq6czpYMCCNTpNCUf" +
                        "DHixRXl-vDQdDs_KYtk39TGI8xVGVzHYMa2hTykqmg0SjBvDoGk37wpIQMXBQA464r9yiEHVsI" +
                        "_pt-IOtxlnTUJ");

                Notification raven = new Notification();
                raven.title("EBEC Challenge Aveiro 2018")
                        .body(message)
                        .to("/topics/"+topic);

                Log.d("PushRaven response", Pushraven.push(raven).toString());
            }
        }).start();
    }

}
