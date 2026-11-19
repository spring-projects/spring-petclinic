# Requisitos de seguridad (OWASP ASVS 4.0.3, nivel 1 y 2)

Cada requisito se aplica a Spring PetClinic con una verificación concreta y se liga al control del pipeline que lo comprueba. El estado refleja el repositorio el 18-nov-2026.

**Nota sobre los identificadores:** corresponden a ASVS 4.0.3 y deben contrastarse con el documento oficial antes de la entrega final.

**Estados:** Cumple, Parcial, No cumple, Pendiente de verificar.

| # | ASVS | Nivel | Requisito aplicado a PetClinic | Estado | Evidencia o verificación | Control del pipeline |
|---|---|---|---|---|---|---|
| 1 | V5.1.3 | L1 | Toda entrada del usuario (dueño, mascota, visita, búsqueda) se valida con reglas positivas: longitud, formato y obligatoriedad. | Parcial | `@NotBlank`, `@Size` y `@Pattern` en `Owner` y `Visit`. `PetValidator` valida nombre, tipo y fecha de nacimiento. Falta revisar la búsqueda por apellido. | Pruebas unitarias |
| 2 | V5.3.4 | L1 | Las consultas a la base usan consultas parametrizadas o JPA, nunca concatenación de SQL. | Cumple | Repositorios Spring Data JPA. | Semgrep (regla propia contra concatenación, pendiente de añadir) |
| 3 | V5.3.3 | L1 | Los datos del usuario se codifican al salir en HTML (p. ej. la descripción de la visita) para evitar XSS. | Cumple | Thymeleaf escapa con `th:text`; las plantillas no usan `th:utext`. | ZAP (DAST) |
| 4 | V5.1.2 | L1 | Se impide la asignación masiva: los binders rechazan campos como `id`. | Cumple | `setDisallowedFields("id", "*.id")` en `OwnerController`, `PetController` y `VisitController`. | Semgrep (regla propia `@InitBinder`, pendiente de añadir) |
| 5 | V4.1.1 | L1 | El control de acceso se aplica en el servidor y cada operación de alta, edición o consulta de datos personales exige un usuario autenticado y autorizado. | No cumple | No hay Spring Security; cualquier visitante accede a todo. | Modelo de amenazas (STRIDE); ZAP |
| 6 | V4.2.2 | L1 | Los formularios que cambian estado están protegidos contra CSRF. | No cumple | Sin Spring Security no hay token CSRF en los POST. | ZAP (DAST) |
| 7 | V9.1.1 | L1 | Todo el tráfico del cliente usa TLS. | No cumple | `k8s/petclinic.yml` expone un Service NodePort por HTTP, sin Ingress ni TLS. | Checkov y Trivy sobre `k8s/` |
| 8 | V14.4.3 / V14.4.4 / V14.4.5 | L1 | Las respuestas incluyen `Content-Security-Policy`, `X-Content-Type-Options` y `Strict-Transport-Security`. | No cumple | Sin Spring Security no se envían cabeceras de seguridad por defecto. | ZAP baseline (`.zap/rules.tsv`) |
| 9 | V7.4.1 | L1 | Los errores muestran un mensaje genérico sin trazas de pila ni detalles internos. | Pendiente de verificar | Existe `error.html`; confirmar que `server.error.include-stacktrace` no esté habilitado y revisar `/oups`. | ZAP (DAST) |
| 10 | V14.3.2 / V14.2.2 | L1 | No hay endpoints de depuración ni de administración expuestos. Actuator solo publica `health` e `info`. | Cumple | `management.endpoints.web.exposure.include=health,info` en `application.properties`. | Semgrep (regla ERROR contra `include=*`) |
| 11 | V2.10.4 | L2 | Ningún secreto está en el repositorio ni en los manifiestos; las credenciales vienen de un gestor de secretos o del entorno del CI. | Parcial | El Secret de `k8s/db.yml` guarda `pass` en claro y `docker-compose.yml` tiene valores fijos. El historial no tiene secretos reales. | Gitleaks (job `secretos`) |
| 12 | V14.2.1 | L1 | Las dependencias de terceros no tienen vulnerabilidades conocidas con corrección disponible. | Cumple | Grype falla con High o Critical con corrección; tras actualizar a Spring Boot 4.1.2 el job `sca` pasó en verde (`evidencia/`). Dependabot está pendiente de configurar (Persona 3). | Syft y Grype (job `sca`) |
| 13 | V14.2.5 | L1 | Existe un inventario (SBOM) de los componentes y sus versiones. | Cumple | SBOM CycloneDX generado por Syft en el pipeline. | Job `sca` |
| 14 | V14.1.1 | L2 | El pipeline de construcción es seguro: permisos mínimos, acciones fijadas por SHA y gates que fallan de verdad. | Cumple | `permissions: contents: read` en `devsecops.yml`; acciones fijadas por SHA. | Revisión de `devsecops.yml` |
| 15 | V7.1.1 / V7.1.2 | L1 | Los registros no contienen datos personales (nombre, teléfono, dirección) ni credenciales. | Pendiente de verificar | El código de la aplicación no escribe logs propios; falta revisar los logs que genera Spring con `logging.level`. | Revisión manual |
| 16 | V1.1.2 | L2 | El diseño tiene un modelo de amenazas actualizado con cada cambio de arquitectura. | Parcial | Modelo STRIDE inicial con 16 amenazas en `ThreatDragonModels/` y `security/threat-model/`. Falta mantenerlo con cada cambio. | Revisión de PR |
| 17 | V5.1.3 / V5.1.4 | L1 | Las rutas con parámetros (`ownerId`, `petId`) y la paginación validan el rango y no desbordan enteros. | Parcial | Commits recientes añadieron mitigaciones de desbordamiento y redirección de páginas fuera de rango. Falta cubrir `petId` y `ownerId` inexistentes. | Pruebas unitarias; ZAP |
| 18 | V8.3.4 | L2 | Los datos personales se clasifican y se protegen según su nivel. | Parcial | Clasificación en `clasificacion-datos.md`; los controles técnicos (TLS y acceso) siguen pendientes. | Revisión de diseño |
| 19 | V2.1.1 | L1 | La aplicación autentica a los usuarios y exige contraseñas de al menos 12 caracteres. | No cumple | PetClinic no tiene cuentas ni inicio de sesión. Es la base del requisito 5. | Modelo de amenazas (STRIDE); ZAP |
| 20 | V3.4.1 / V3.4.2 / V3.4.3 | L1 | La cookie de sesión lleva los atributos `Secure`, `HttpOnly` y `SameSite`. | Parcial | La aplicación crea sesión para los mensajes flash de las redirecciones. Spring Boot activa `HttpOnly` por defecto; `Secure` requiere TLS y `SameSite` no está configurado. Pendiente de confirmar con ZAP. | ZAP baseline (`.zap/rules.tsv`) |
| 21 | V7.2.2 | L1 | Las decisiones de control de acceso, y en especial las fallidas, se registran en un log de auditoría sin datos personales. | No cumple | No hay autenticación ni auditoría de quién cambia los datos (amenaza T6). | Revisión manual |
| 22 | V11.1.4 | L1 | La aplicación limita el abuso automatizado (límite de tasa y tamaño de página) para evitar la denegación de servicio. | No cumple | Solo se valida el tamaño de página; no hay límite de tasa (amenazas T12 y T13). | ZAP full scan local (pendiente) |

### Aporte de Persona 3: configuración, dependencias y registros

Los siguientes requisitos complementan la evaluación de OWASP ASVS 4.0.3 para Spring PetClinic. Se han seleccionado considerando los mecanismos de seguridad existentes y las mejoras pendientes del proyecto.

**R-P3-01 — Gestión segura de dependencias (V14.2.1 y V14.2.5).**

La aplicación debe disponer de un inventario actualizado de componentes de terceros y de mecanismos automatizados para detectar vulnerabilidades conocidas. El pipeline utiliza Syft para generar un SBOM CycloneDX y Grype para detectar vulnerabilidades. Dependabot permitirá proponer actualizaciones de dependencias.

- Estado: Parcial.
- Evidencia: `security/sbom/sbom.cdx.json`, `.github/dependabot.yml` y job `sca`.
- Verificación: comprobar la generación del SBOM y que Grype aplique el umbral de bloqueo documentado.
- Mejora pendiente: integrar Dependabot en `main` y verificar sus actualizaciones.

**R-P3-02 — Seguridad del proceso de construcción (V14.1.1).**

El pipeline debe aplicar privilegios mínimos, fijar las versiones de las acciones utilizadas e impedir que se integren cambios que incumplan los controles de seguridad definidos.

- Estado: Parcial.
- Evidencia: `.github/workflows/devsecops.yml` y `security/politica-de-bloqueo.md`.
- Verificación: inspeccionar permisos del workflow y comprobar que un hallazgo bloqueante provoca el fallo del job.
- Mejora pendiente: verificar que los checks de seguridad sean obligatorios en la protección de `main`.

**R-P3-03 — Registro seguro de eventos (V7.2.2).**

La aplicación debe registrar los eventos de seguridad relevantes para facilitar auditorías e investigaciones, evitando incluir credenciales o datos personales innecesarios en los registros.

- Estado: No cumple.
- Evidencia: evaluación inicial de los mecanismos de auditoría de Spring PetClinic.
- Verificación: revisión manual de los eventos registrados y pruebas funcionales de operaciones sensibles.
- Mejora pendiente: definir eventos auditables, implementar su registro y verificar que los logs no expongan información sensible.

## Resumen

| Estado | Cantidad |
|---|---|
| Cumple | 7 |
| Parcial | 6 |
| No cumple | 7 |
| Pendiente de verificar | 2 |

Los 7 requisitos que **no cumplen** (autenticación, acceso, CSRF, TLS, cabeceras, auditoría y control de abuso) son el origen de la remediación y del modelo de amenazas de las fases siguientes.
