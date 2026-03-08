OpenAPI docs: http://localhost:8080/swagger-ui/index.html

# Device API

Device API is a REST API which can help you to manage and persist your devices.
For persisting, application uses PostgreSQL DB.

---

## 🚀 Getting Started

### Prerequisites
* [Docker](https://www.docker.com/get-started)
* [Docker Compose](https://docs.docker.com/compose/install/)

### Installation & Setup
1. **Download**: Please install Docker, Docker Compose and download this repository.
2. **Configuration**: You can modify the default application settings (such as database credentials or ports) in the `.env` file located in the root directory.
2. **Launch**: From the root directory, run the following command to build and start the application:
   ```bash
   docker-compose up --build (or docker-compose up -d --build to run in detached mode)
3. **Access**: The default application port is 8080
   Open your browser and visit: http://localhost:8080/swagger-ui/index.html.
   Note: If you changed the APP_PORT in the .env file, use http://localhost:yourCustomPort instead.

### Instructions to use the application
1. **OpenAPI documentation**: Please, open http://localhost:8080/swagger-ui/index.html and check the documentation of the API. You can check the API schema objects here 
and try to run your requests. (Alternatively, you can use Postman: https://www.postman.com/). 
Click on the request to expand it and press 'Try it out' button to provide a request body and press 'Execute' to send a request.
2. **POST /devices**: You can create your own device with this endpoint. Specify your own name, brand and state (AVAILABLE, IN_USE or INACTIVE) for your device.
3. **GET /devices**: To retrieve already existing devices you can use this endpoint. You can filter devices by brand and state and also you can receive a paginated view 
of the response. Page number and size are also configurable.
4. **GET /devices/{id}**: Retrieves a single device by specified ID (if exists). If such a device doesn't exist, you receive an exception.
5. **PUT /devices/{id}**: Endpoint to replace whole device with completely new brand, name and state. If at least one field is missing, you will receive an exception.
For partial update, please use PATCH /devices/{id}. Please be aware that you cannot update a device if its state = 'IN_USE'.
5. **PATCH /devices/{id}**: This endpoint helps you to partially update a device. If you update only the state of the device, it should work for every state.
But please be aware that you cannot update a device with new 'brand' or 'name' if device state = 'IN_USE'.
7. **REMOVE /devices/{id}**: Removes a device from the database. If such a device doesn't exist, you receive an exception.

### Available metrics
**All metrics**: http://localhost:8080/actuator/metrics
**Env props**: http://localhost:8080/actuator/env
**Config props**: http://localhost:8080/actuator/configprops
**App healthiness**: http://localhost:8080/actuator/health