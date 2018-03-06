package io.dmendezg.iot.lwm2m

import org.eclipse.leshan.LwM2mId
import org.eclipse.leshan.client.`object`.Device
import org.eclipse.leshan.client.`object`.Security
import org.eclipse.leshan.client.`object`.Server
import org.eclipse.leshan.client.californium.LeshanClientBuilder
import org.eclipse.leshan.client.resource.BaseInstanceEnabler
import org.eclipse.leshan.client.resource.ObjectsInitializer
import org.eclipse.leshan.core.model.LwM2mModel
import org.eclipse.leshan.core.model.ObjectLoader
import org.eclipse.leshan.core.request.BindingMode
import org.eclipse.leshan.core.response.ReadResponse
import java.util.*

/**
 * http://www.openmobilealliance.org/wp/OMNA/LwM2M/LwM2MRegistry.html
 */
fun main(args: Array<String>) {

    val builder = LeshanClientBuilder("iot-protocols")

    val models = ObjectLoader.loadDefault()
    models.addAll(ObjectLoader.loadDdfResources("/models", arrayOf("3303.xml" )))

    val initializer = ObjectsInitializer(LwM2mModel(models))
    initializer.setInstancesForObject(LwM2mId.SECURITY, Security.noSec("coap://leshan.eclipse.org:5683", 12345))
    initializer.setInstancesForObject(LwM2mId.SERVER, Server(12345, 5 * 60, BindingMode.U, false))
    initializer.setInstancesForObject(LwM2mId.DEVICE, Device("Eclipse Leshan", "model12345", "12345", "U"))

    initializer.setInstancesForObject(TemperatureSensor.ID, TemperatureSensor())
    builder.setObjects(initializer.create(LwM2mId.SECURITY, LwM2mId.SERVER, LwM2mId.DEVICE, TemperatureSensor.ID))

    val client = builder.build()

    client.start()

}

class TemperatureSensor : BaseInstanceEnabler() {
    val rand = Random()
    override fun read(resourceid: Int): ReadResponse {
        return when (resourceid) {
            5700 -> ReadResponse.success(5700, randDouble(24.0, 26.0))
            else -> ReadResponse.notFound()
        }
    }
    fun randDouble(min: Double, max: Double): Double {
        return rand.nextDouble() * (max - min) + min
    }
    companion object {
        val ID = 3303
    }
}