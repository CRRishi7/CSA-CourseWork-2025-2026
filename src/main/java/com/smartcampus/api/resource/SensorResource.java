package com.smartcampus.api.resource;

import com.smartcampus.api.exception.LinkedResourceNotFoundException;
import com.smartcampus.api.model.Room;
import com.smartcampus.api.model.Sensor;
import com.smartcampus.api.store.InMemoryStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {
        if (sensor == null || sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            throw new BadRequestException("Sensor id is required.");
        }
        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            throw new BadRequestException("roomId is required.");
        }
        Room room = InMemoryStore.ROOMS.get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException("Room with id " + sensor.getRoomId() + " does not exist.");
        }

        InMemoryStore.SENSORS.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());
        URI location = uriInfo.getAbsolutePathBuilder().path(sensor.getId()).build();
        return Response.created(location).entity(sensor).build();
    }

    @GET
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        List<Sensor> sensors = new ArrayList<>(InMemoryStore.SENSORS.values());
        if (type == null || type.trim().isEmpty()) {
            return sensors;
        }

        List<Sensor> filtered = new ArrayList<>();
        for (Sensor sensor : sensors) {
            if (type.equalsIgnoreCase(sensor.getType())) {
                filtered.add(sensor);
            }
        }
        return filtered;
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        if (!InMemoryStore.SENSORS.containsKey(sensorId)) {
            throw new NotFoundException("Sensor with id " + sensorId + " not found.");
        }
        return new SensorReadingResource(sensorId);
    }
}
