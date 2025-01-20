# CONEXA CHALLENGE

![img_2.png](img_2.png)


## Tabla de Contenidos

- [Descripción](#descripción)
- [Prerrequisitos](#prerrequisitos)
- [Instalación](#instalación)
- [Variables de entorno](#variables-de-entorno)
- [Manejo de token en Swagger](#manejo-de-token-en-swagger)
- [Despliegue](#despliegue)


## Descripción

Esta aplicacion se ha desarrollado utilizando Spring Boot y Java 8 y esta diseñada para consumir la
[API de Star Wars](https://www.swapi.tech/documentation) y obtener información sobre personas, películas, naves espaciales
y vehículos, de manera paginada o
filtrando por nombre o ID. <br>
Se incluyeron pruebas para garantizar el correcto funcionamiento de las funcionalidades clave y
la integración con la API externa.
<br>
La aplicación también cuenta con un sistema de autenticación basado en JWT, que permite a los usuarios obtener un token
para autenticarse y acceder a los endpoints de la aplicación. Por motivo de simplicidad, no existe un sistema de registro
de usuarios y no se almacenan credenciales en una base de datos, sino que se utilizan credenciales precargadas en el código.
```json
{
  "username": "admin",
  "password": "1234"
}
```

Estos detalles y más acerca del funcionamiento de cada funcionalidad pueden ser consultados en la documentación de Swagger.

## Prerrequisitos

- Java 8
- Apache Maven


## Instalación

1. Clonar el repositorio:
```bash
git clone https://github.com/andyholes/conexa-challenge.git
```

2. Ingresar al directorio del proyecto:
```bash 
cd conexa-challenge
```

3. Compilar el proyecto usando Maven:
```bash
mvn clean install
```

4. Ejecutar el proyecto:
```bash
mvn spring-boot:run
```

## Variables de entorno

La aplicación cuenta con las siguientes variables de entorno configurables:

| Nombre                      | Descripción                                     | Valor por defecto             |
|-----------------------------|-------------------------------------------------|-------------------------------|
| `JWT_SECRET`                | Clave secreta utilizada para firmar el token JWT | `mySecretKey`                 |
| `JWT_EXPIRATION_TIME`       | Tiempo de expiración del token en milisegundos  | `600000` (10 minutos)         |
| `SERVER_PORT`               | Puerto en el que se ejecuta la aplicación       | `8080`                        |
| `DEPLOYMENT_URL`            | Url del despliegue para acceder desde Swagger   | `http://localhost:8080`       |
| `SWAPI_BASE_URL`            | URL base de la API de Star Wars                 | `https://www.swapi.tech/api/` |
| `SWAPI_PEOPLE_PAGINATED`    | Endpoint para obtener personas de forma paginada | `/people?page=%s&limit=%s`    |
| `SWAPI_PEOPLE_BY_ID`        | Endpoint para obtener personas por ID           | `/people/%s`                  |
| `SWAPI_PEOPLE_BY_NAME`      | Endpoint para obtener personas por nombre       | `/people/?name=%s`            |
| `SWAPI_VEHICLES_PAGINATED`  | Endpoint para obtener vehículos de forma paginada | `/vehicles?page=%s&limit=%s`  |
| `SWAPI_VEHICLES_BY_ID`      | Endpoint para obtener vehículos por ID          | `/vehicles/%s`                |
| `SWAPI_VEHICLES_BY_NAME`    | Endpoint para obtener vehículos por nombre      | `/vehicles/?name=%s`          |
| `SWAPI_STARSHIPS_PAGINATED` | Endpoint para obtener naves espaciales de forma paginada | `/starships?page=%s&limit=%s` |
| `SWAPI_STARSHIPS_BY_ID`     | Endpoint para obtener naves espaciales por ID   | `/starships/%s`               |
| `SWAPI_STARSHIPS_BY_NAME`   | Endpoint para obtener naves espaciales por nombre | `/starships/?name=%s`         |
| `SWAPI_FILMS_ALL`           | Endpoint para obtener todas las películas       | `/films`                      |
| `SWAPI_FILMS_BY_ID`         | Endpoint para obtener una película por ID       | `/films/%s`                   |
| `SWAPI_FILMS_BY_TITLE`      | Endpoint para obtener una película por título   | `/films/?title=%s`            |


## Manejo de token en Swagger

Para mayor comodidad a la hora de probar la aplicación, se configuró Swagger para que luego de autenticarse, el token se pueda enviar en el header de las peticiones de manera sencilla. Para ello, se debe seguir los siguientes pasos:
1. Ingresar a la [URL de Swagger desplegada](https://glad-gael-andyholes-054436ef.koyeb.app/swagger-ui/index.html) o si
   se está trabajando en el ambiente local a [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).
2. Una vez dentro nos dirigimos al [endpoint de autenticacion](https://glad-gael-andyholes-054436ef.koyeb.app/swagger-ui/index.html#/Authorization%20Controller/login)  y hacemos click en el boton "Try it out".
3. Dado que las credenciales de usuario válidas vienen precargadas, haremos click en "Execute" y obtendremos un token de autenticación como el siguiente:
```json
{
  "token": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTczNzM0NTA5MSwiZXhwIjoxNzM3MzQ1NjkxfQ.UkHBI-30TO3AHeDt2oi0LG3wuaEbWic1XII1pkep_yE"
}
```
4. Copiamos el contenido del token (sin las comillas) y nos dirigimos al boton Authorize que se encuentra en la parte superior derecha de la pantalla.<br>
![img.png](img.png)
5. En el campo "Value" pegamos el token y hacemos click en "Authorize".<br>
![img_1.png](img_1.png)
6. Listo! Ahora podremos hacer uso de los endpoints de la aplicacion sin tener que preocuparnos por el token ya que este se incluira automaticamente en los headers de las peticiones. Recordar que, a menos que se haya modificado, el token tiene una duracion de 10 minutos.


## Despliegue

La aplicación se encuentra desplegada de manera gratuita en la plataforma Koyeb y se puede acceder a ella a traves
de la siguiente URL: [https://glad-gael-andyholes-054436ef.koyeb.app/swagger-ui/index.html](https://glad-gael-andyholes-054436ef.koyeb.app/swagger-ui/index.html)
