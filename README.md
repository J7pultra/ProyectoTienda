#  Boutique Uniformes - Sistema de Gestión

##  Descripción
Sistema web completo para la gestión de una boutique de uniformes desarrollado con Spring Boot. Incluye módulos para gestión de clientes, empleados, proveedores, uniformes, ventas, asistencias y pedidos a proveedores.

##  Características Principales

###  Interfaz Moderna
- **Diseño Glassmorphism**: Interfaz moderna con efectos de cristal y transparencias
- **Modo Oscuro/Claro**: Cambio dinámico de temas
- **Responsive Design**: Adaptable a dispositivos móviles y escritorio
- **Animaciones Fluidas**: Transiciones suaves y efectos visuales atractivos
- **Multiidioma**: Soporte para español e inglés

###  Autenticación y Seguridad
- Sistema de login seguro
- Gestión de roles y permisos
- Autenticación OAuth2
- Registro de usuarios

###  Módulos del Sistema

####  Gestión de Clientes
- Registro y edición de clientes
- Historial de compras
- Información de contacto

####  Gestión de Empleados
- Control de empleados
- Gestión de roles
- Seguimiento de asistencias

####  Gestión de Proveedores
- Registro de proveedores
- Órdenes de compra
- Seguimiento de pedidos

####  Gestión de Uniformes
- Catálogo de productos
- Control de inventario
- Precios y categorías

####  Sistema de Ventas
- Procesamiento de ventas
- Facturación
- Reportes de ventas

####  Control de Asistencias
- Registro de asistencias
- Reportes de puntualidad
- Gestión de horarios

####  Pedidos a Proveedores
- Creación de órdenes de compra
- Seguimiento de pedidos
- Gestión de inventario

##  Tecnologías Utilizadas

- **Backend**: Spring Boot 3.x
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap 5
- **Base de Datos**: MySQL/H2
- **Seguridad**: Spring Security
- **Plantillas**: Thymeleaf
- **Estilos**: CSS personalizado con Glassmorphism
- **Iconos**: Font Awesome

##  Instalación y Configuración

### Prerrequisitos
- Java 21 o superior
- Maven 3.6+
- MySQL 8.0+ (opcional, incluye H2 para desarrollo)

### Pasos de Instalación

1. **Clonar el repositorio**
   `ash
   git clone https://github.com/tuusuario/boutique-uniformes.git
   cd boutique-uniformes
   `

2. **Configurar base de datos**
   - Editar src/main/resources/application.properties
   - Configurar credenciales de MySQL o usar H2

3. **Compilar el proyecto**
   `ash
   mvn clean install
   `

4. **Ejecutar la aplicación**
   `ash
   mvn spring-boot:run
   `

5. **Acceder a la aplicación**
   - URL: http://localhost:8080
   - Usuario admin por defecto (si configurado)

##  Capturas de Pantalla

###  Login
- Interfaz moderna con glassmorphism
- Cambio de tema dinámico
- Formulario responsive

###  Dashboard
- Panel de control intuitivo
- Estadísticas en tiempo real
- Navegación fluida

###  Gestión de Pedidos
- Formulario completo de órdenes
- Cálculo automático de totales
- Tabla interactiva de items

##  Funcionalidades Destacadas

###  Diseño Visual
- **Glassmorphism**: Efectos de cristal y transparencias
- **Gradientes**: Colores modernos y atractivos
- **Animaciones**: Transiciones suaves
- **Responsive**: Adaptable a todos los dispositivos

###  Experiencia de Usuario
- **Multiidioma**: Español/Inglés
- **Temas**: Modo claro/oscuro
- **Navegación**: Intuitiva y fluida
- **Feedback**: Notificaciones visuales

###  Reportes y Analytics
- Reportes de ventas
- Estadísticas de empleados
- Control de inventario
- Análisis de proveedores

##  Configuración Avanzada

### Base de Datos
`properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/boutique
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password

# H2 (Desarrollo)
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
`

### Seguridad
`properties
# JWT Configuration
jwt.secret=tu_clave_secreta
jwt.expiration=86400000

# OAuth2
spring.security.oauth2.client.registration.google.client-id=tu_client_id
spring.security.oauth2.client.registration.google.client-secret=tu_client_secret
`

##  Contribución

1. Fork el proyecto
2. Crea tu rama de características (git checkout -b feature/AmazingFeature)
3. Commit tus cambios (git commit -m 'Add some AmazingFeature')
4. Push a la rama (git push origin feature/AmazingFeature)
5. Abre un Pull Request

##  Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

##  Desarrollador

**Juan Huertas** - Desarrollador Principal

- GitHub: [@J7pultra](https://github.com/J7pultra)
- Email: juan.huertas@est.ucacue.edu.ec

##  Agradecimientos

- Spring Boot Community
- Bootstrap Team
- Font Awesome
- Todos los contribuidores del proyecto

---

 **¡Si te gusta este proyecto, dale una estrella!** 
