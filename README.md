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

### 2. Configurar la Base de Datos y Propiedades

El proyecto maneja tres perfiles de entorno a través de archivos `.properties`:

- `application.properties`: Configuraciones globales (e.g., JWT secret) y define qué perfil está activo (actualmente `dev`).
- `application-dev.properties`: Entorno de desarrollo local. Contiene credenciales específicas para PostgreSQL local y permite la creación automática de tablas mediante Hibernate (`spring.jpa.hibernate.ddl-auto=update`).
- `application-prod.properties`: Entorno de producción. Su configuración está preparada para inyectar variables de entorno (e.g., `${DB_URL}`).

**Pasos para crear la Base de Datos en pgAdmin (Local):**

1. **Abrir pgAdmin y conectar al servidor:** Inicia pgAdmin y despliega el servidor local al que te vas a conectar (normalmente en el puerto `5432`). Introduce tu contraseña de `postgres` si te lo solicita.
2. **Crear la Base de Datos:**
   - Haz clic derecho sobre el apartado `Databases` > `Create` > `Database...`
   - En la pestaña `General`, asigna el nombre a tu base de datos: `db_reservas`.
   - Haz clic en `Save`.

*Nota: Una vez la base de datos esté creada y en blanco, Spring Boot y JPA (Hibernate) se encargarán automáticamente de crear el esquema relacional (`usuario`, `cliente`, `proveedor`, etc.) de acuerdo a las entidades de Java configuradas.*

### 3. Configurar JWT

Agrega una clave secreta para JWT en `application.properties`:

```properties
jwt.secret=tu-clave-secreta-muy-segura-y-larga-minimo-32-caracteres
jwt.expiration=86400000
```

### 4. Compilar el Proyecto

```bash
mvn clean install -U -DskipTests 
```

### 5. Ejecutar la Aplicación

```bash
# Opción 1:
mvn spring-boot:run

# Opción 2:
mvn clean spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## Estructura del Proyecto

```plain text
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
│   │       ├── application.properties         # Configuración global y perfil activo
│   │       ├── application-dev.properties     # Config. BD y JPA local (Hibernate update)
│   │       └── application-prod.properties    # Config. Producción con variables de entorno
│   └── test/                        # Pruebas unitarias e integración
├── pom.xml
└── README.md
```

## Endpoints Principales

Autenticación y Registro:

- `POST /api/auth/register/client`: Registro de Usuarios tipo Cliente
- `POST /api/auth/register/provider`: Registro de Usuarios tipo Proveedor

----

## Pruebas en Postman

Al arrancar la aplicación en local (`http://localhost:8080`), puedes probar los siguientes flujos para la creación de cuentas de clientes y proveedores utilizando validaciones exhaustivas.

> Importante: El `Content-Type` de las peticiones debe ser `application/json`.

### Pruebas de Cliente (CLIENTE)

**✅ Caso 1: Petición Válida** (Crear Cliente Exitoso)

- **Método**: `POST`
- **URL**: `http://localhost:8080/api/auth/register/client`
- **Body**:

```json
{
    "email": "juan.cliente@udea.edu.co",
    "password": "Password123",
    "nombre": "Juan Pérez",
    "telefono": "3001234567"
}
```

**Resultado Esperado:**
Recibirás un HTTP Status `201 CREATED`. El aplicativo registrará el usuario y la contraseña será encriptada mediante BCrypt. El endpoint devolverá el perfil creado con un JSON Web Token (JWT) válido para iniciar sesión.

**❌ Caso 2: Petición Inválida** (Probar Validaciones)

- **Método**: `POST`
- **URL**: `http://localhost:8080/api/auth/register/client`
- **Body**:

```json
{
    "email": "correo_invalido",
    "password": "pass", 
    "nombre": "",
    "telefono": "300 abc de" 
}
```

**Resultado Esperado:**

HTTP Status `400 Bad Request`. Gracias a nuestro `GlobalExceptionHandler`, verás un objeto JSON destilando individualmente los errores debido a expresiones regulares: contraseña sin 8 letras o números y teléfono con alfabetos en lugar de números planos.

### Pruebas de Proveedor (PROVEEDOR)

**✅ Caso 3: Petición Válida** (Crear Proveedor Exitoso)

- **Método**: `POST`
- **URL**: `http://localhost:8080/api/auth/register/provider`
- **Body**:

```json
{
    "email": "contacto@esteticabelleza.com",
    "password": "Password123",
    "nombreComercial": "Estética Belleza Natural",
    "direccion": "Calle 123 #45-67, Medellín",
    "telefonoContacto": "3119876543"
}
```

**Resultado Esperado:**

HTTP Status `201 CREATED`. Con su respectivo Token autorizando el rol exclusivo de Proveedor, y mapeándole la relación de persistencia a la tabla `proveedor` en PostgreSQL.

**❌ Caso 4: Petición Inválida** (Probar Validaciones y Duplicidad)

- **Método**: `POST`
- **URL**: `http://localhost:8080/api/auth/register/provider`
- **Body**:

```json
{
    "email": "contacto@esteticabelleza.com",
    "password": "SinNumerosYMayusculas",
    "nombreComercial": " ",
    "direccion": "Centro",
    "telefonoContacto": "+57 321"
}
```

**Resultado Esperado:**

Nuevamente generará HTTP Status `400 Bad Request` señalando todos los campos incorrectos. Adicional a esto, si utilizas exactamente el mismo correo con el que corriste la prueba válida previa, saltará un control de base de datos alertando que `"error": "El correo electrónico ya está en uso"`.

## Licencia

Proyecto académico - Universidad de Antioquia
