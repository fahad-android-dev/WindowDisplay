package com.orbits.queuingsystem;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.elcapi.jnielc;

public class Utils extends Activity {
    private static final int seek_red = 0xa1;
    private static final int seek_green = 0xa2;
    private static final int seek_blue = 0xa3;
    private static final int led_red = 2;
    private static final int led_blue = 3;
    private static final int led_green = 4;
    int fb;

    public void ledOnRed(int red_bright) {

        // jnielc.ledmix(fb);
        jnielc.seekstart();
        jnielc.ledseek(seek_red, red_bright);
        jnielc.seekstop();
        Intent intent = new Intent("android.intent.action.ledctl");
        intent.putExtra("led", led_red);
        intent.putExtra("ledbrightness", red_bright);
        sendBroadcast(intent);
        // SystemProperteisProxy.set("persist.demo.ledswitch", "1");
        Toast.makeText(Utils.this, "LED red On !!!!!", Toast.LENGTH_SHORT).show();

    }

    public void ledOnGreen(int green_bright) {

        // jnielc.ledmix(fb);
        jnielc.seekstart();
        jnielc.ledseek(seek_green, green_bright);
        jnielc.seekstop();
        Intent intent = new Intent("android.intent.action.ledctl");
        intent.putExtra("led", led_green);
        intent.putExtra("ledbrightness", green_bright);
        sendBroadcast(intent);
        // SystemProperteisProxy.set("persist.demo.ledswitch", "1");
        Toast.makeText(Utils.this, "LED green On !!!!!", Toast.LENGTH_SHORT).show();

    }

    public void ledOnBlue(int blue_bright) {

        // jnielc.ledmix(fb);
        jnielc.seekstart();
        jnielc.ledseek(seek_blue, blue_bright);
        jnielc.seekstop();
        Intent intent = new Intent("android.intent.action.ledctl");
        intent.putExtra("led", led_blue);
        intent.putExtra("ledbrightness", blue_bright);
        sendBroadcast(intent);
        // SystemProperteisProxy.set("persist.demo.ledswitch", "1");
        Toast.makeText(Utils.this, "LED blue On !!!!!", Toast.LENGTH_SHORT).show();

    }

    public void ledOff() {
        fb = jnielc.open();
        jnielc.ledoff(fb);
    }
}
