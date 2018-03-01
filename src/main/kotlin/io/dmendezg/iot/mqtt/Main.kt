package io.dmendezg.iot.mqtt

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.*

fun main(args: Array<String>) {

    val client = MqttClient(
            "tcp://test.mosquitto.org:1883",
            UUID.randomUUID().toString())

    client.connect()

    client.subscribeWithResponse("io.dmendezg/iot-protocols/mqtt",
            { _, message -> println("Received: ${String(message.payload)}") })

    client.publish(
            "io.dmendezg/iot-protocols/mqtt",
            MqttMessage("Hello World".toByteArray()))

    Thread.sleep(1000)

    client.disconnect()

}