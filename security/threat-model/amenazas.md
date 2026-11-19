# Modelo de amenazas (STRIDE)

Modelo hecho en OWASP Threat Dragon sobre el DFD de [`dfd.md`](../diseño/dfd.md). El modelo editable está en [`modelo-threat-dragon.json`](modelo-threat-dragon.json). Contiene 16 amenazas, al menos una por categoría STRIDE.

## Método

- **Componente afectado:** elemento o flujo del DFD donde se manifiesta la amenaza.
- **Impacto y probabilidad:** Bajo, Medio o Alto para el impacto; Baja, Media o Alta para la probabilidad. Son juicio del equipo.
- **Prioridad:** impacto (Bajo=1, Medio=2, Alto=3) por probabilidad (Baja=1, Media=2, Alta=3). Alta si el producto es 6 o más, Media si es 3, 4 o 5, y Baja si es 2 o menos. En Threat Dragon la prioridad es la severidad.
- **Trazabilidad:** cada mitigación se liga a un requisito de [`requisitos-asvs.md`](../diseño/requisitos-asvs.md) y al control que la verifica.

## Amenazas

| # | STRIDE | Componente | Amenaza | Impacto | Prob. | Prioridad | Mitigación | Estado |
|---|---|---|---|---|---|---|---|---|
| T1 | Suplantación (S) | P1 Controladores y vistas | Suplantación de personal de la clínica. La aplicación no autentica a nadie: cualquier visitante puede hacerse pasar por personal de la clínica y operar sobre datos de dueños. | Alto | Alta | Alta | Añadir autenticación con Spring Security (formulario de acceso, contraseñas de al menos 12 caracteres y hash adaptativo) y exigir sesión en todas las rutas con datos. | Abierta |
| T2 | Suplantación (S) | F2 HTML | Robo de sesión por cookie sin atributos de protección. La aplicación crea sesión para los mensajes de las redirecciones. Sin TLS ni los atributos Secure y SameSite, un atacante en la red puede capturar la cookie JSESSIONID y reutilizarla. | Medio | Media | Media | Servir solo por HTTPS, configurar server.servlet.session.cookie.secure=true y same-site=lax, y regenerar la sesión al autenticar. | Abierta |
| T3 | Alteración (T) | F1b HTTP manipulado | Asignación masiva de campos (id) en los formularios. Un atacante envía campos adicionales, como id, para sobrescribir registros de otra entidad. | Alto | Baja | Media | Los binders de OwnerController, PetController y VisitController rechazan los campos id y *.id con setDisallowedFields. | Mitigada |
| T4 | Alteración (T) | F1 HTTP | Falsificación de petición en sitios cruzados (CSRF). Los formularios POST de alta y edición de dueños, mascotas y visitas no llevan token CSRF. Una página externa puede hacer que el navegador del personal modifique datos. | Medio | Alta | Alta | Incorporar Spring Security con protección CSRF y token en los formularios Thymeleaf, junto con SameSite en la cookie. | Abierta |
| T5 | Alteración (T) | F5 SQL (JDBC) | Inyección SQL por los parámetros de búsqueda. Parámetros como el apellido de la búsqueda podrían alterar las consultas que llegan a la base de datos. | Alto | Baja | Media | Las consultas usan Spring Data JPA con parámetros enlazados y no concatenan SQL. | Mitigada |
| T6 | Repudio (R) | D1 Base de datos | Cambios en datos personales sin registro del autor. Como no hay autenticación ni auditoría, no se puede saber quién creó o modificó un dueño, una mascota o una visita. | Medio | Media | Media | Tras autenticar, registrar usuario, acción, identificador del registro y hora en un log de auditoría, sin incluir datos personales. | Abierta |
| T7 | Repudio (R) | D3 Repositorio Git | Cambios en el código sin revisión ni autoría verificable. Si main no tiene protección de rama, cualquiera con permiso de escritura puede subir cambios sin pull request ni revisión. | Medio | Media | Media | Proteger main: solo cambios por PR con revisión obligatoria y checks requeridos (secretos, sast y sca). Valorar commits firmados. | Abierta |
| T8 | Divulgación de información (I) | F1 HTTP | Datos personales expuestos en tránsito por HTTP. Nombre, dirección y teléfono de los dueños viajan sin cifrar entre el navegador y la aplicación. Un atacante en la red puede leerlos. | Alto | Media | Alta | Terminar TLS en un Ingress o en el servidor, redirigir HTTP a HTTPS y enviar la cabecera HSTS. | Abierta |
| T9 | Divulgación de información (I) | P1 Controladores y vistas | Mensajes de error con detalles internos. Una página de error con traza de pila o rutas internas revela la tecnología y la estructura. La ruta /oups provoca un error a propósito. | Bajo | Media | Baja | Mantener un error.html genérico, no habilitar server.error.include-stacktrace y verificar el comportamiento de /oups. | Abierta |
| T10 | Divulgación de información (I) | P3 Actuator | Exposición de endpoints de administración (Actuator). Con management.endpoints.web.exposure.include=* se publicarían endpoints como env, heapdump o beans. Se detectó en el PR #1 y se corrigió en el PR #2. | Alto | Baja | Media | Se limitó la exposición a health e info en application.properties. | Mitigada |
| T11 | Divulgación de información (I) | D1 Base de datos | Credenciales de la base de datos en claro en el repositorio. docker-compose.yml y k8s/db.yml guardan usuario y contraseña de desarrollo (petclinic y pass) y MySQL admite root sin contraseña. Quien acceda al repositorio o al clúster podría conectarse a la base. | Alto | Media | Alta | Mover las credenciales a secretos del CI y a un Secret de Kubernetes real, sin valores en el repositorio, y retirar MYSQL_ALLOW_EMPTY_PASSWORD. | Abierta |
| T12 | Denegación de servicio (D) | F1 HTTP | Peticiones abusivas que saturan la aplicación. Sin límite de tasa, un atacante puede lanzar muchas búsquedas o paginaciones para consumir CPU y conexiones de la base de datos. | Medio | Media | Media | Mantener acotado el tamaño de página (ya validado), aplicar límite de tasa en el Ingress y fijar un máximo de conexiones del pool. | Abierta |
| T13 | Denegación de servicio (D) | P1 Controladores y vistas | Contenedor sin límites de recursos. k8s/petclinic.yml no define límites de CPU ni de memoria ni securityContext. Un pico de carga o una explotación puede agotar el nodo. | Medio | Media | Media | Definir resources.requests y limits, y un securityContext sin root y con sistema de archivos de solo lectura. | Abierta |
| T14 | Escalada de privilegios (E) | P1 Controladores y vistas | Acceso sin autorización a cualquier registro (IDOR). Cualquier visitante puede ver y editar cualquier dueño cambiando el identificador en /owners/{ownerId}/edit. No hay roles ni verificación de pertenencia. | Alto | Alta | Alta | Definir roles (por ejemplo recepción y veterinario) y autorizar cada operación en el servidor con Spring Security. | Abierta |
| T15 | Escalada de privilegios (E) | P2 Persistencia JPA | Explotación de una dependencia vulnerable. Una librería con CVE conocida (Tomcat, Spring MVC, Jackson o el controlador JDBC de PostgreSQL) podría permitir ejecutar código o saltarse controles. Grype bloqueó un PR por esto y se corrigió al pasar a Spring Boot 4.1.2. | Alto | Media | Alta | Gate de Grype (High o Critical con corrección), SBOM en cada build y Dependabot para las actualizaciones. | Mitigada |
| T16 | Escalada de privilegios (E) | P4 Pipeline de CI | Acción de terceros comprometida en el pipeline. Una acción de GitHub Actions modificada podría ejecutar código con el token del CI y alterar el repositorio. | Alto | Baja | Media | Permisos mínimos (contents: read), acciones fijadas por SHA y revisión de los workflows. | Mitigada |

## Matriz de trazabilidad

Amenaza, requisito ASVS que la cubre y control del pipeline que la verifica. Los controles marcados como pendientes todavía no existen en el repositorio.

| Amenaza | Requisito ASVS | Control que la verifica |
|---|---|---|
| T1 Suplantación de personal de la clínica | #19 (V2.1.1), #5 (V4.1.1) | Revisión manual del diseño; DAST autenticado con ZAP (pendiente) |
| T2 Robo de sesión por cookie sin atributos de protección | #20 (V3.4.1 a V3.4.3), #7 (V9.1.1) | ZAP baseline (pendiente); IaC con Checkov sobre k8s (pendiente) |
| T3 Asignación masiva de campos (id) en los formularios | #4 (V5.1.2) | Semgrep con regla propia @InitBinder (pendiente de añadir); pruebas unitarias |
| T4 Falsificación de petición en sitios cruzados (CSRF) | #6 (V4.2.2) | ZAP baseline (pendiente) |
| T5 Inyección SQL por los parámetros de búsqueda | #2 (V5.3.4) | Semgrep con regla propia contra concatenación (pendiente de añadir); ZAP |
| T6 Cambios en datos personales sin registro del autor | #21 (V7.2.2), #15 (V7.1.1), #19 (V2.1.1) | Revisión manual |
| T7 Cambios en el código sin revisión ni autoría verificable | #14 (V14.1.1) | Protección de rama (guardrail, pendiente de verificar en GitHub) |
| T8 Datos personales expuestos en tránsito por HTTP | #7 (V9.1.1), #8 (V14.4.5), #18 (V8.3.4) | IaC con Checkov o Trivy sobre k8s (pendiente); ZAP |
| T9 Mensajes de error con detalles internos | #9 (V7.4.1) | ZAP baseline (pendiente) |
| T10 Exposición de endpoints de administración (Actuator) | #10 (V14.3.2) | Semgrep con regla propia petclinic-actuator-wildcard-exposure (job sast, activa) |
| T11 Credenciales de la base de datos en claro en el repositorio | #11 (V2.10.4) | Gitleaks (job secretos); IaC con Checkov (pendiente) |
| T12 Peticiones abusivas que saturan la aplicación | #22 (V11.1.4), #17 (V5.1.3) | ZAP full scan local (pendiente); IaC (pendiente) |
| T13 Contenedor sin límites de recursos | #14 (V14.1.1), #22 (V11.1.4) | IaC con Checkov o Trivy (pendiente); Semgrep detecta la falta de securityContext |
| T14 Acceso sin autorización a cualquier registro (IDOR) | #5 (V4.1.1), #19 (V2.1.1) | DAST autenticado con ZAP (pendiente); revisión manual |
| T15 Explotación de una dependencia vulnerable | #12 (V14.2.1), #13 (V14.2.5) | SCA con Syft y Grype (job sca, activo); Dependabot (pendiente) |
| T16 Acción de terceros comprometida en el pipeline | #14 (V14.1.1) | Revisión de devsecops.yml; permisos y SHA verificados en el workflow |

## Resumen

| Categoría STRIDE | Amenazas |
|---|---|
| Suplantación (S) | T1, T2 |
| Alteración (T) | T3, T4, T5 |
| Repudio (R) | T6, T7 |
| Divulgación de información (I) | T8, T9, T10, T11 |
| Denegación de servicio (D) | T12, T13 |
| Escalada de privilegios (E) | T14, T15, T16 |

- **Abiertas:** 11. **Mitigadas:** 5.
- **Prioridad Alta abierta:** T1, T4, T8, T11, T14. Son el origen natural de las próximas remediaciones.
- **Controles pendientes del pipeline:** ZAP (DAST), Checkov o Trivy (IaC), Dependabot y la protección de rama de `main`.

## Notas

- Las amenazas mitigadas citan la evidencia existente: PR #2 (Actuator), PR #3 (Spring Boot 4.1.2), `devsecops.yml` y los binders de los controladores.
- Los identificadores ASVS corresponden a la versión 4.0.3 y deben contrastarse con el documento oficial.
- Si se edita el modelo en Threat Dragon, hay que copiar de nuevo `ThreatDragonModels/Spring PetClinic - Modelo de amenazas/` a `modelo-threat-dragon.json`.
