package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {
    MQTTHelper mqttHelper;
    TextView textcambien1, textcambien2;
    LabeledSwitch button1, button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textcambien1 = findViewById(R.id.txtcambien1);
        textcambien2 = findViewById(R.id.txtcambien2);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        button1.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn==true){
                    sendDataMQTT("phductrung/feeds/nutnhan1","1");
                }else{
                    sendDataMQTT("phductrung/feeds/nutnhan1", "0");
                }
            }
        });

        button2.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn==true){
                    sendDataMQTT("phductrung/feeds/nutnhan2","1");
                }else{
                    sendDataMQTT("phductrung/feeds/nutnhan2", "0");
                }
            }
        });

        startMQTT();
    }

    public void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(12345678);
        msg.setQos(0);
        msg.setRetained(false);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        }catch (MqttException e){
        }
    }

    public void startMQTT(){
        mqttHelper = new MQTTHelper(this);
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST", topic + "***" + message.toString());
                if(topic.contains("cambien1")){
                    textcambien1.setText(message.toString());
                }else if(topic.contains("cambien2")){
                    textcambien2.setText(message.toString());
                }else if(topic.contains("nutnhan1")){
                    if(message.toString().equals("1")){
                        button1.setOn(true);
                    }else{
                        button1.setOn(false);
                    }
                }else if(topic.contains("nutnhan2")){
                    if(message.toString().equals("1")){
                        button2.setOn(true);
                    }else{
                        button2.setOn(false);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }
}
