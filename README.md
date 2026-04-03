# Plataforma de Reservas de Servicios

## Descripción

Sistema de gestión de reservas para negocios como clínicas, consultorios, salones de belleza y centros deportivos. La plataforma permite administrar citas, disponibilidad de recursos y agendas de proveedores de servicios, optimizando el uso de recursos y mejorando la experiencia del cliente.

Este proyecto corresponde al **Caso 15** del handbook de casos de Fabrica Escuela - Universidad de Antioquia.

## Funcionalidades Principales

- Registro y autenticación de usuarios (clientes y proveedores)
- Gestión de servicios ofrecidos por proveedores
- Administración de recursos (espacios físicos, profesionales, equipos)
- Reservas de citas con control de disponibilidad
- Agendas y horarios personalizables
- Historial de reservas y reportes de ocupación

## Tecnologías

### Backend
- **Java 17**
- **Spring Boot 3.2.4**
- **Spring Security** (Autenticación y autorización)
- **Spring Data JPA** (Persistencia)
- **JWT** (JSON Web Tokens para autenticación)
- **MapStruct** (Mapeo entre entidades y DTOs)
- **Lombok** (Reducción de código boilerplate)
- **Maven** (Gestión de dependencias)

### Base de Datos
- **Oracle Database 21c** (Producción)
- **PostgreSQL** (Desarrollo/Pruebas)

### Herramientas de Desarrollo
- **Git** (Control de versiones)
- **GitHub** (Repositorio remoto)
- **Postman** (Pruebas de APIs)
- **SonarCloud** (Análisis de calidad de código)

## Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **JDK 17** o superior
- **Maven 3.8+**
- **Oracle Database** o **PostgreSQL**
- **Git**

## Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/Isa-Bedoya-UdeA/backend-reservas.git
cd backend-reservas
```

### 2. Configurar la Base de Datos

Crea una base de datos en Oracle o PostgreSQL y actualiza el archivo `src/main/resources/application-dev.properties` con tus credenciales:

```properties
# Oracle
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# PostgreSQL (alternativa)
# spring.datasource.url=jdbc:postgresql://localhost:5432/reservas_db
# spring.datasource.username=tu_usuario
# spring.datasource.password=tu_password
# spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.OracleDialect
```

### 3. Configurar JWT

Agrega una clave secreta para JWT en `application.properties`:

```properties
jwt.secret=tu-clave-secreta-muy-segura-y-larga-minimo-32-caracteres
jwt.expiration=86400000
```

### 4. Compilar el Proyecto

```bash
mvn clean install
```

### 5. Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## Estructura del Proyecto

```
backend-reservas/
├── src/
│   ├── main/
│   │   ├── java/com/udea/backendreservas/
│   │   │   ├── config/              # Configuraciones de seguridad y CORS
│   │   │   ├── controller/          # Controladores REST API
│   │   │   ├── service/             # Lógica de negocio
│   │   │   ├── repository/          # Capa de acceso a datos
│   │   │   ├── entity/              # Entidades JPA
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── mapper/              # Mapeo entre entidades y DTOs
│   │   │   ├── exception/           # Manejo de excepciones
│   │   │   └── security/            # Filtros y utilidades JWT
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/                        # Pruebas unitarias e integración
├── pom.xml
└── README.md
```

## Endpoints (Próximamente)


## Licencia

Proyecto académico - Universidad de Antioquia