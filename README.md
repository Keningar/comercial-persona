## Descripción
Microservicio que permite consultar información de las personas registradas en el sistema del Holding de Telconet.
"http://dev-microservicios.telconet.ec/persona/".


## Modelo de datos  

Este microservicio emplea las siguientes tablas:

  - db_comercial.info_persona
    - Tabla donde se registra los datos personales de los clientes, empleados, proveedores.  
  - db_comercial.info_persona_empresa_rol  
    - Tabla donde se registra a la persona o empresa y esta a su vez se la asocia a una empresa del holding Telconet.
  
## Configuración  

  - Configuración properties  
  Se debe renombrar el archivo 
application.properties.dist
 a 
application.properties
 y de ser necesario modificar los siguientes parámetros:

| Clave                                                             | Observación                                                                              |
|-------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| aplicacion.nombre                                                 | Nombre de la aplicación.                                                                 |
| server.port                                                       | Puerto del ms.                                                                           |
| path.webservice                                                   | path del Microservicio                                                                   |
| spring.kafka.bootstrap-servers                                    | Definir los servidores de kafka.                                                         |
| kafka.request-reply.timeout-ms                                    | Tiempo en minutos de un petición kafka.                                                  |
| logging.level.org.apache.kafka.clients.admin.AdminClientConfig    | Configuracion de cliente kafka                                                           |
| ruta.parameterslocal                                              | Ruta de parameters.yml.                                                                  |
| host.parameter                                                    | Host o ambiente de parameters.yml.                                                       |
| spring.datasource.hikari.idle-timeout                             | Idle timeout hikari.                                                                     |
| spring.datasource.hikari.connection-timeout                       | Conexión timeout hikari.                                                                 |
| spring.datasource.hikari.maximum-pool-size                        | Máximo de pool de conexiones hikari.                                                     |
| spring.datasource.hikari.minimum-idle                             | Mínimo de pool de conexiones hikari.                                                     |
| spring.datasource.hikari.max-lifetime                             | Maximo de tiempo de vida de un pool.                                                     |
| logging.config                                                    | Mínimo de pool de conexiones hikari.                                                     |
| jaeger.tracer.host                                                | Definir el servidor jaeger.                                                              |
| jaeger.tracer.port                                                | Definir el puerto jaeger.                                                                |
| jaeger.tracer.logspan                                             | Definir el logspan jaeger.                                                               |
| jaeger.tracer.nametracer                                          | Definir el nametracer jaeger                                                             |
| kafka.ms.topic-sync-sufijo                                        | Sufijo topic sync.                                                                       |
| kafka.ms.topic-asyn-sufijo                                        | Sufijo topic async.                                                                      |
| management.endpoint.health.notification.mailTo                    | Correo destino para Notificacion de email.                                               |
| management.endpoint.health.notification.mailFrom                  | Correo remitemte para Notificacion de email                                              |
| management.endpoint.health.notification.memoryPercentMax          | MemoryPercent Max para notificacion email                                                |
| management.endpoint.health.show-details                           | Mostrar detalles                                                                         |
| management.endpoints.web.exposure.include                         | Exponer endpoints.                                                                       |
| management.endpoint.shutdown.enabled                              | Habilitar shutdown                                                                       |

  
## Funcionalidades  


  El microservicio tiene las siguiente funcionalidades

#### consultas-controller  
  Podremos listar la información del usuario
#### persona-controller  
  Esta funcionalidad nos permite  
  -  listaPersona
  -  listaPersonaPor
  -  listaPersonaPorDepartamento
  -  listaPersonaPorEmpresa
  -  listaPersonaPorRegion
  -  listaPersonaPorRol  
  
#### persona-empresa-rol-controller  
  Esta funcionalidad nos permite  
  -  listaPersonaEmpresaRol
  -  listaPersonaEmpresaRolPor
  -  paginaListaPersonaEmpresaRolPor  
  
#### persona-prospecto-controller  
  Esta funcionalidad nos permite  
  -  personaProspecto
  -  personaRecomendacion
  -  tarjetaRecomendacion  
   
#### pre-cliente-controller  
  Esta funcionalidad nos permite  
  -  craerPreCliente  
  
#### servicio-historial-controller  
  Esta funcionalidad nos permite  
  -  listaHistorialServicioPorFecha  



## Dependencias  

El proyecto contiene 2 tipos de dependecia:
  - Local  
  - Utilitaria  

| Local                  |
|------------------------|
| db-repositorio-comercial |
| db-repositorio-general |
| ms-core                |
| db-config              |
| ms-core-comercial        |

| Utilitaria                          |
|-------------------------------------|
| spring-boot-starter-web             |
| spring-boot-starter                 |
| spring-boot-starter-log4j2          |
| disruptor                           |
| commons-codec                       |
| lombok                              |
| gson                                |
| jackson-databind                    |
| spring-boot-configuration-processor |
| jackson-dataformat-yaml             |
| springfox-swagger2                  |
| opentracing-spring-jaeger-web-starte|
| springfox-swagger-ui                |             |


     
## Uso del microservicio  

##### Url del Swagger
  https://sites.telconet.ec/swagger/swagger-ui.html?urls.primaryName=persona%40docker#/servicio-historial-controller

##### Método  
infoUsuario  
##### Endpoint  
  http://dev-microservicios.telconet.ec/persona/infoUsuario 
                                                    

#####  Request
```curl
curl --location --request POST 'https://dev-microservicios.telconet.ec/persona/infoUsuario' \
--header 'Content-Type: application/json' \
--data-raw '{
  "{
  "empresaId": 0,
  "estado": "string",
  "login": "string"
}'
```

#####  Response  

Después de consumir el método obtendremos la siguiente respuesta  

```json
{
  "code": 0,
  "data": [
    {
      "esJefe": "string",
      "idPersona": 0,
      "idPersonaEmpresaRol": 0,
      "nombreDepartamento": "string"
    }
  ],
  "message": "string",
  "status": "string"
}

```


##### Método  
listaPersonaPor  
##### Endpoint  
  http://dev-microservicios.telconet.ec/persona/listaPersonaPor 
                                                    

#####  Request
```curl
curl --location --request POST 'https://dev-microservicios.telconet.ec/persona/listaPersonaPor' \
--header 'Content-Type: application/json' \
--data-raw '{
  "{}'
```

#####  Response  

Después de consumir el método obtendremos la siguiente respuesta  

```json
{
  "code": 0,
  "data": [
    {}
  ],
  "message": "string",
  "status": "string"
}

```


##### Método  
listaPersonaPorDepartamento  
##### Endpoint  
  http://dev-microservicios.telconet.ec/persona/listaPersonaPorDepartamento 
                                                    

#####  Request
```curl
curl --location --request POST 'https://dev-microservicios.telconet.ec/persona/listaPersonaPorDepartamento' \
--header 'Content-Type: application/json' \
--data-raw '{
  "{}'
```

#####  Response  

Después de consumir el método obtendremos la siguiente respuesta  

```json
{
  "code": 0,
  "data": [
    {}
  ],
  "message": "string",
  "status": "string"
}

```


##### Método  
listaPersonaPorEmpresa  
##### Endpoint  
  http://dev-microservicios.telconet.ec/persona/listaPersonaPorEmpresa 
                                                    

#####  Request
```curl
curl --location --request POST 'https://dev-microservicios.telconet.ec/persona/listaPersonaPorEmpresa' \
--header 'Content-Type: application/json' \
--data-raw '{
  "{
  "empresaId": 0,
  "estado": "string",
  "idPersona": 0,
  "identificacion": "string",
  "listEstado": [
    "string"
  ],
  "login": "string",
  "prefijoEmpresa": "string"
}'
```

#####  Response  

Después de consumir el método obtendremos la siguiente respuesta  

```json
{
  "code": 0,
  "data": [
    {}
  ],
  "message": "string",
  "status": "string"
}

```


##### Método  
listaPersonaPorRegion  
##### Endpoint  
  http://dev-microservicios.telconet.ec/persona/listaPersonaPorRegion 
                                                    

#####  Request
```curl
curl --location --request POST 'https://dev-microservicios.telconet.ec/persona/listaPersonaPorRegion' \
--header 'Content-Type: application/json' \
--data-raw '{
  "{
  "empresaId": 0,
  "estado": "string",
  "idPersona": 0,
  "identificacion": "string",
  "listEstado": [
    "string"
  ],
  "login": "string",
  "prefijoEmpresa": "string"
}'
```

#####  Response  

Después de consumir el método obtendremos la siguiente respuesta  

```json
{
  "code": 0,
  "data": [
    {}
  ],
  "message": "string",
  "status": "string"
}

```


##### Método  
listaPersonaPorRol  
##### Endpoint  
  http://dev-microservicios.telconet.ec/persona/listaPersonaPorRol 
                                                    

#####  Request
```curl
curl --location --request POST 'https://dev-microservicios.telconet.ec/persona/listaPersonaPorRol' \
--header 'Content-Type: application/json' \
--data-raw '{
  "{
  "descripcionRol": "string",
  "descripcionTipoRol": "string",
  "empresaId": 0,
  "estado": "string",
  "idPersona": 0,
  "identificacion": "string",
  "listEstado": [
    "string"
  ],
  "login": "string",
  "rolId": 0,
  "tipoRolId": 0
}'
```

#####  Response  

Después de consumir el método obtendremos la siguiente respuesta  

```json
{
  "code": 0,
  "data": [
    {}
  ],
  "message": "string",
  "status": "string"
}

```


##### Método  
paginaListaPersonaPor  
##### Endpoint  
  http://dev-microservicios.telconet.ec/persona/paginaListaPersonaPor 
                                                    

#####  Request
```curl
curl --location --request POST 'https://dev-microservicios.telconet.ec/persona/paginaListaPersonaPor' \
--header 'Content-Type: application/json' \
--data-raw '{
  "nombreFiltroCons": "string",
  "order": "string",
  "orderValue": "string",
  "page": 0,
  "size": 0,
  "tabla": {},
  "valorFiltroCons": "string"
}'
```

#####  Response  

Después de consumir el método obtendremos la siguiente respuesta  

```json
{
  "code": 0,
  "data": {
    "content": [
      {}
    ],
    "empty": true,
    "first": true,
    "last": true,
    "number": 0,
    "numberOfElements": 0,
    "pageable": {
      "offset": 0,
      "pageNumber": 0,
      "pageSize": 0,
      "paged": true,
      "sort": {
        "empty": true,
        "sorted": true,
        "unsorted": true
      },
      "unpaged": true
    },
    "size": 0,
    "sort": {
      "empty": true,
      "sorted": true,
      "unsorted": true
    },
    "totalElements": 0,
    "totalPages": 0
  },
  "message": "string",
  "status": "string"
}

```



##### Método  
listaHistorialServicioPorFecha  
##### Endpoint  
  http://dev-microservicios.telconet.ec/persona/listaHistorialServicioPorFecha 
                                                    

#####  Request
```curl
curl --location --request POST 'https://dev-microservicios.telconet.ec/persona/listaHistorialServicioPorFecha' \
--header 'Content-Type: application/json' \
--data-raw '{
  "fechaFin": "string",
  "fechaInicio": "string",
  "servicioId": 0
}'
```

#####  Response  

Después de consumir el método obtendremos la siguiente respuesta  

```json
{
  "code": 0,
  "data": [
    {}
  ],
  "message": "string",
  "status": "string"
}

```


##### Método  
crear  
##### Endpoint  
  http://dev-microservicios.telconet.ec/persona/precliente/crear 
                                                    

#####  Request
```curl
curl --location --request POST 'https://dev-microservicios.telconet.ec/persona/precliente/crear' \
--header 'Content-Type: application/json' \
--data-raw '{
  "arrayDatosForm": {
    "apellidos": "string",
    "bancoTipoCuentaId": "string",
    "contribuyenteEspecial": "string",
    "direccionTributaria": "string",
    "esPrepago": "string",
    "es_distribuidor": "string",
    "estadoCivil": "string",
    "fechaNacimiento": {
      "day": "string",
      "month": "string",
      "year": "string"
    },
    "formaPagoId": "string",
    "formas_contacto": "string",
    "genero": "string",
    "holding": "string",
    "id": "string",
    "idOficinaFacturacion": "string",
    "identificacionCliente": "string",
    "idperreferido": "string",
    "idreferido": "string",
    "intIdPais": "string",
    "nacionalidad": "string",
    "nombres": "string",
    "numeroConadis": "string",
    "origenIngresos": "string",
    "origen_web": "string",
    "pagaIva": "string",
    "razonSocial": "string",
    "referido": "string",
    "representanteLegal": "string",
    "tipoCuentaId": "string",
    "tipoEmpresa": "string",
    "tipoIdentificacion": "string",
    "tipoTributario": "string",
    "tituloId": "string",
    "yaexiste": "string"
  },
  "arrayFormasContacto": [
    {
      "formaContacto": "string",
      "idFormaContacto": "string",
      "valor": "string"
    }
  ],
  "arrayRecomendacionTarjeta": "string",
  "intOficinaId": "string",
  "strClientIp": "string",
  "strCodEmpresa": "string",
  "strPrefijoEmpresa": "string",
  "strUsrCreacion": "string"
}'
```

#####  Response  

Después de consumir el método obtendremos la siguiente respuesta  

```json
{
  "code": 0,
  "data": {
    "idPersona": 0,
    "idPersonaEmpresaRol": 0
  },
  "message": "string",
  "status": "string"
}

```


##### Método  
personaProspecto  
##### Endpoint  
  http://dev-microservicios.telconet.ec/persona/personaProspecto  
                                                    

#####  Request
```curl
curl --location --request POST 'https://dev-microservicios.telconet.ec/persona/personaProspecto' \
--header 'Content-Type: application/json' \
--data-raw '{
  "empresaCod": "string",
  "identificacion": "string",
  "origen": "string",
  "prefijoEmPresa": "string",
  "requireRecomendacion": true,
  "tipoIdentificacion": "string",
  "user": "string"
}'
```

#####  Response  

Después de consumir el método obtendremos la siguiente respuesta  

```json
{
  "code": 0,
  "data": {
    "admiRoles": [
      {
        "descripcionRol": "string",
        "esJefe": "string",
        "estado": "string",
        "feCreacion": "2022-01-13T00:21:38.627Z",
        "feUltMod": "2022-01-13T00:21:38.627Z",
        "idRol": 0,
        "permiteAsignacion": "string",
        "tipoRolId": 0,
        "usrCreacion": "string",
        "usrUltMod": "string"
      }
    ],
    "empresaRoles": [
      {
        "cuadrillaId": 0,
        "departamentoId": 0,
        "empresaRol": {
          "empresaCod": "string",
          "estado": "string",
          "feCreacion": "2022-01-13T00:21:38.627Z",
          "idEmpresaRol": 0,
          "ipCreacion": "string",
          "rolId": 0,
          "usrCreacion": "string"
        },
        "empresaRolId": 0,
        "esPrepago": "string",
        "estado": "string",
        "feCreacion": "2022-01-13T00:21:38.627Z",
        "feUltMod": "2022-01-13T00:21:38.627Z",
        "idPersonaRol": 0,
        "ipCreacion": "string",
        "oficinaId": 0,
        "persona": {
          "apellidos": "string",
          "calificacionCrediticia": "string",
          "cargo": "string",
          "contribuyenteEspecial": "string",
          "direccion": "string",
          "direccionTributaria": "string",
          "estado": "string",
          "estadoCivil": "string",
          "feCreacion": "2022-01-13T00:21:38.627Z",
          "fechaNacimiento": "2022-01-13T00:21:38.627Z",
          "genero": "string",
          "idPersona": 0,
          "identificacionCliente": "string",
          "ipCreacion": "string",
          "login": "string",
          "nacionalidad": "string",
          "nombres": "string",
          "numeroConadis": "string",
          "origenIngresos": "string",
          "origenProspecto": "string",
          "origenWeb": "string",
          "pagaIva": "string",
          "paisId": 0,
          "razonSocial": "string",
          "representanteLegal": "string",
          "tipoEmpresa": "string",
          "tipoIdentificacion": "string",
          "tipoTributario": "string",
          "tituloId": 0,
          "usrCreacion": "string"
        },
        "personaEmpresaRolId": 0,
        "personaEmpresaRolIdTTCO": 0,
        "personaId": 0,
        "reportaPersonaEmpresaRolId": 0,
        "usrCreacion": "string",
        "usrUltMod": "string"
      }
    ],
    "formaPago": {
      "bancoTipoCuentaId": 0,
      "estado": "string",
      "feCreacion": "2022-01-13T00:21:38.627Z",
      "feUltMod": "2022-01-13T00:21:38.627Z",
      "formaPagoId": 0,
      "idDatosPago": 0,
      "personEmpresaRolId": 0,
      "tipoCuentaId": 0,
      "usrCreacion": "string",
      "usrUltMod": "string"
    },
    "isRecomendado": true,
    "persona": {
      "apellidos": "string",
      "calificacionCrediticia": "string",
      "cargo": "string",
      "contribuyenteEspecial": "string",
      "direccion": "string",
      "direccionTributaria": "string",
      "direcciones": [
        {
          "direccion": "string"
        }
      ],
      "edad": 0,
      "estado": "string",
      "estadoCivil": "string",
      "estadoLegal": "string",
      "feCreacion": "2022-01-13T00:21:38.627Z",
      "fechaIniciCompania": "string",
      "fechaNacimiento": "2022-01-13T00:21:38.627Z",
      "genero": "string",
      "idPersona": 0,
      "identificacionCliente": "string",
      "ipCreacion": "string",
      "login": "string",
      "nacionalidad": "string",
      "nombreComercial": "string",
      "nombres": "string",
      "numeroConadis": "string",
      "origenIngresos": "string",
      "origenProspecto": "string",
      "origenWeb": "string",
      "pagaIva": "string",
      "paisId": 0,
      "razonSocial": "string",
      "representanteLegal": "string",
      "telefonos": [
        {
          "telefono": "string"
        }
      ],
      "tipoCompania": "string",
      "tipoEmpresa": "string",
      "tipoIdentificacion": "string",
      "tipoTributario": "string",
      "tituloId": 0,
      "usrCreacion": "string"
    },
    "personaReferido": {
      "apellidos": "string",
      "calificacionCrediticia": "string",
      "cargo": "string",
      "contribuyenteEspecial": "string",
      "direccion": "string",
      "direccionTributaria": "string",
      "estado": "string",
      "estadoCivil": "string",
      "feCreacion": "2022-01-13T00:21:38.627Z",
      "fechaNacimiento": "2022-01-13T00:21:38.627Z",
      "genero": "string",
      "idPersona": 0,
      "identificacionCliente": "string",
      "ipCreacion": "string",
      "login": "string",
      "nacionalidad": "string",
      "nombres": "string",
      "numeroConadis": "string",
      "origenIngresos": "string",
      "origenProspecto": "string",
      "origenWeb": "string",
      "pagaIva": "string",
      "paisId": 0,
      "razonSocial": "string",
      "representanteLegal": "string",
      "tipoEmpresa": "string",
      "tipoIdentificacion": "string",
      "tipoTributario": "string",
      "tituloId": 0,
      "usrCreacion": "string"
    }
  },
  "message": "string",
  "status": "string"
}'
```


##### Método  
personaRecomendacion  
##### Endpoint  
  http://dev-microservicios.telconet.ec/persona/personaRecomendacion 
                                                    

#####  Request
```curl
curl --location --request POST 'https://dev-microservicios.telconet.ec/persona/personaRecomendacion' \
--header 'Content-Type: application/json' \
--data-raw '{
  "{
  "actualiza_datos": "string",
  "comandoConfiguracion": "string",
  "datos": {
    "identificacion": "string",
    "tipoIdentificacion": "string"
  },
  "ejecutaComando": "string",
  "empresa": "string",
  "ipCreacion": "string",
  "opcion": "string",
  "usrCreacion": "string"
}'
```

#####  Response  

Después de consumir el método obtendremos la siguiente respuesta  

```json
{
  "code": 0,
  "data": {
    "datosPersona": {
      "apellidos": "string",
      "edad": 0,
      "estadoLegal": "string",
      "fechaInicioCompania": "string",
      "identificacion": "string",
      "nombreComercial": "string",
      "nombres": "string",
      "razonSocial": "string",
      "representanteLegal": "string",
      "tipoCompania": "string",
      "tipoIdentificacion": "string"
    },
    "direcciones": [
      {
        "direccion": "string"
      }
    ],
    "telefonos": [
      {
        "telefono": "string"
      }
    ]
  },
  "message": "string",
  "status": "string"
}

```


##### Método  
tarjetaRecomendacion  
##### Endpoint  
  http://dev-microservicios.telconet.ec/persona/tarjetaRecomendacion 
                                                    

#####  Request
```curl
curl --location --request POST 'https://dev-microservicios.telconet.ec/persona/tarjetaRecomendacion' \
--header 'Content-Type: application/json' \
--data-raw '{
  "actualiza_datos": "string",
  "comandoConfiguracion": "string",
  "datos": {
    "identificacion": "string",
    "tipoIdentificacion": "string"
  },
  "ejecutaComando": "string",
  "empresa": "string",
  "ipCreacion": "string",
  "opcion": "string",
  "usrCreacion": "string"
}'
```

#####  Response  

Después de consumir el método obtendremos la siguiente respuesta  

```json
{
  "code": 0,
  "data": {
    "recomendaciones": [
      {
        "detalle": [
          {
            "descripcion": "string",
            "ordenDetalle": 0
          }
        ],
        "ordenRecomendacion": 0,
        "tipo": "string"
      }
    ]
  },
  "message": "string",
  "status": "string"
}

```
