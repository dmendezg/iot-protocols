package io.dmendezg.iot.lwm2m

import org.eclipse.leshan.LwM2mId
import org.eclipse.leshan.client.`object`.Device
import org.eclipse.leshan.client.`object`.Security
import org.eclipse.leshan.client.`object`.Server
import org.eclipse.leshan.client.californium.LeshanClientBuilder
import org.eclipse.leshan.client.resource.ObjectsInitializer
import org.eclipse.leshan.core.request.BindingMode

/**
 * http://www.openmobilealliance.org/wp/OMNA/LwM2M/LwM2MRegistry.html
 */
fun main(args: Array<String>) {

    val builder = LeshanClientBuilder("iot-protocols")

    val initializer = ObjectsInitializer()
    initializer.setInstancesForObject(LwM2mId.SECURITY, Security.noSec("coap://leshan.eclipse.org:5683", 12345))
    initializer.setInstancesForObject(LwM2mId.SERVER, Server(12345, 5 * 60, BindingMode.U, false))
    initializer.setInstancesForObject(LwM2mId.DEVICE, Device("Eclipse Leshan", "model12345", "12345", "U"))

    builder.setObjects(initializer.create(LwM2mId.SECURITY, LwM2mId.SERVER, LwM2mId.DEVICE))

    val client = builder.build()

    client.start()

}