# Smart Campus REST API (5COSC022W Coursework)

## API Design Overview
This API is built using JAX-RS (Jersey) and in-memory data structures (`HashMap` and `ArrayList`) to manage Smart Campus resources:
- `Room`
- `Sensor`
- `SensorReading`

Base path: `http://localhost:8080/smart-campus-api/api/v1`

Resource structure:
- `GET /api/v1` (discovery endpoint)
- `GET/POST /api/v1/rooms`
- `GET/DELETE /api/v1/rooms/{id}`
- `GET/POST /api/v1/sensors`
- `GET /api/v1/sensors?type=CO2`
- `GET/POST /api/v1/sensors/{sensorId}/readings`

## Build and Launch Instructions
1. Open terminal in the project root.
2. Build the project:
   - `mvn clean package`
3. Deploy the generated WAR file:
   - `target/smart-campus-api-1.0-SNAPSHOT.war`
4. Start your servlet container (for example Tomcat).
5. Access the API from:
   - `http://localhost:8080/smart-campus-api/api/v1`

## Sample curl Commands
1. Discovery endpoint
   - `curl -X GET http://localhost:8080/smart-campus-api/api/v1`

2. Create room (`201 Created` + `Location`)
   - `curl -i -X POST http://localhost:8080/smart-campus-api/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"LIB-301\",\"name\":\"Library Quiet Study\",\"capacity\":80}"`

3. Get all rooms
   - `curl -X GET http://localhost:8080/smart-campus-api/api/v1/rooms`

4. Get one room
   - `curl -X GET http://localhost:8080/smart-campus-api/api/v1/rooms/LIB-301`

5. Create sensor with valid roomId
   - `curl -i -X POST http://localhost:8080/smart-campus-api/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"CO2-001\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":0.0,\"roomId\":\"LIB-301\"}"`

6. Create sensor with invalid roomId (`422`)
   - `curl -i -X POST http://localhost:8080/smart-campus-api/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-404\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":0.0,\"roomId\":\"UNKNOWN\"}"`

7. Filter sensors by type
   - `curl -X GET "http://localhost:8080/smart-campus-api/api/v1/sensors?type=CO2"`

8. Add reading to sensor (`201`)
   - `curl -i -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/CO2-001/readings -H "Content-Type: application/json" -d "{\"value\":412.7}"`

9. Get reading history
   - `curl -X GET http://localhost:8080/smart-campus-api/api/v1/sensors/CO2-001/readings`

10. Delete room with linked sensor (`409`)
   - `curl -i -X DELETE http://localhost:8080/smart-campus-api/api/v1/rooms/LIB-301`

## Report Answers (Questions Only)

### Part 1.1
By default, JAX-RS creates a new resource instance per request. This avoids shared state in resource objects themselves. However, shared in-memory maps/lists are still global data, so concurrent requests can still update them at the same time. To avoid race conditions, write operations should be synchronized or carefully controlled.

### Part 1.2
Hypermedia (HATEOAS) means responses include links that guide clients to available actions and related resources. This helps clients navigate the API dynamically and reduces dependence on static documentation when endpoints evolve.

### Part 2.1
Returning IDs only gives smaller payloads and lower bandwidth usage, but clients need extra calls for details. Returning full room objects increases payload size, but reduces additional round trips and simplifies client processing when details are needed immediately.

### Part 2.2
DELETE is idempotent because repeating the same DELETE does not create additional side effects after the first successful deletion. The first call removes the room (if allowed), and later identical calls keep the system in the same final state.

### Part 3.1
`@Consumes(MediaType.APPLICATION_JSON)` requires JSON input. If a client sends another format such as `text/plain` or `application/xml`, JAX-RS rejects the request (typically `415 Unsupported Media Type`) before entering the method logic.

### Part 3.2
Query parameters are better for filtering collections because filters are optional and can be combined (`?type=CO2&status=ACTIVE`). Path parameters are better for identifying a specific resource, not for search/filter criteria.

### Part 4.1
The sub-resource locator pattern keeps nested logic in separate classes. Returning `SensorReadingResource` from `SensorResource` improves separation of concerns, keeps classes smaller, and makes large APIs easier to maintain than putting all nested paths in one class.

### Part 5.2
HTTP 422 is more semantically correct than 404 here because the endpoint exists and the JSON format is valid, but the linked value inside the payload (`roomId`) refers to a resource that does not exist.

### Part 5.4
Exposing stack traces leaks sensitive internal details such as package/class names, server paths, and library information. Attackers can use this information to map the system and target known weaknesses.

### Part 5.5
JAX-RS filters centralize cross-cutting behavior like logging in one place. This avoids duplicated logging statements in every resource method and ensures consistent request/response logging across the API.