package com.orbits.queuingsystem

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.elcapi.jnielc

class Utils : Activity() {
    var fb: Int = 0

    fun ledOnRed(red_bright: Int) {
        // jnielc.ledmix(fb);

        jnielc.seekstart()
        jnielc.ledseek(seek_red, red_bright)
        jnielc.seekstop()
        val intent = Intent("android.intent.action.ledctl")
        intent.putExtra("led", led_red)
        intent.putExtra("ledbrightness", red_bright)
        sendBroadcast(intent)
        // SystemProperteisProxy.set("persist.demo.ledswitch", "1");
        Toast.makeText(this@Utils, "LED red On !!!!!", Toast.LENGTH_SHORT).show()
    }

    fun ledOnGreen(green_bright: Int) {
        // jnielc.ledmix(fb);

        jnielc.seekstart()
        jnielc.ledseek(seek_green, green_bright)
        jnielc.seekstop()
        val intent = Intent("android.intent.action.ledctl")
        intent.putExtra("led", led_green)
        intent.putExtra("ledbrightness", green_bright)
        sendBroadcast(intent)
        // SystemProperteisProxy.set("persist.demo.ledswitch", "1");
        Toast.makeText(this@Utils, "LED green On !!!!!", Toast.LENGTH_SHORT).show()
    }

    fun ledOnBlue(blue_bright: Int) {
        // jnielc.ledmix(fb);

        jnielc.seekstart()
        jnielc.ledseek(seek_blue, blue_bright)
        jnielc.seekstop()
        val intent = Intent("android.intent.action.ledctl")
        intent.putExtra("led", led_blue)
        intent.putExtra("ledbrightness", blue_bright)
        sendBroadcast(intent)
        // SystemProperteisProxy.set("persist.demo.ledswitch", "1");
        Toast.makeText(this@Utils, "LED blue On !!!!!", Toast.LENGTH_SHORT).show()
    }

    fun ledOff() {
        fb = jnielc.open()
        jnielc.ledoff(fb)
    }

    companion object {
        private const val seek_red = 0xa1
        private const val seek_green = 0xa2
        private const val seek_blue = 0xa3
        private const val led_red = 2
        private const val led_blue = 3
        private const val led_green = 4
    }
}
