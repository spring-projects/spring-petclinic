# Universidad Galileo
## Proyecto final: Desarrollo seguro de una aplicación

### Descripción general
Cada equipo toma una aplicación pública de código abierto y la lleva por todo el ciclo de desarrollo seguro: documenta su diseño, modela sus amenazas, integra SAST, SCA, IaC y DAST en un pipeline con guardrails, corrige hallazgos y planifica una prueba de penetración. El resultado es un repositorio funcional y un documento técnico que se defiende en la última clase.

| Dato | Detalle |
|---|---|
| **Ponderación** | 20 puntos (proyecto final) |
| **Avance obligatorio** | 18 de noviembre de 2026 (clase 8) |
| **Entrega y defensa** | Miércoles 2 de diciembre de 2026 (clase 10) |

### Objetivos: al terminar el proyecto, el equipo será capaz de:
1. Documentar la arquitectura de una aplicación y sus requisitos de seguridad.
2. Elaborar un modelo de amenazas con STRIDE y trazar cada amenaza a un control.
3. Construir un pipeline CI/CD con secretos, SAST, SCA, IaC y DAST, con políticas de bloqueo justificadas.
4. Priorizar y corregir hallazgos, y documentar las excepciones.
5. Evaluar la madurez del proceso con OWASP SAMM o DSOMM antes y después del proyecto.

### Equipos, alcance y ética
* **Dónde trabajar:** un fork público de la aplicación en GitHub, con el pipeline en GitHub Actions. Se permite GitLab CI si el equipo lo prefiere.
* **Dónde se ejecuta la aplicación:** solo en contenedores propios (local o en el pipeline). Si se despliega en la nube, debe ser una cuenta propia, con acceso restringido y destruida al terminar.
* **Pruebas permitidas:** escaneos y pruebas solo contra la instancia del propio equipo. Está prohibido escanear demos públicas alojadas por terceros o sistemas de otras personas.
* **Secretos:** ningún token, contraseña o llave real en el repositorio. Todo va en los secretos del CI.
* **IA:** se permite usar asistentes de IA si se declara; el equipo debe poder explicar y defender todo lo entregado.

### Aplicaciones a elegir
Todas son de código abierto y se pueden levantar con Docker. Las tres primeras son intencionalmente vulnerables; Spring PetClinic es una aplicación normal, ideal para mostrar cómo un pipeline protege un proyecto "real".

| Aplicación | Tecnología | Tipo | Dificultad |
|---|---|---|---|
| **OWASP Juice Shop** | Node.js, TypeScript, Angular | Tienda web vulnerable | Media |
| **OWASP WebGoat** | Java, Spring Boot | Aplicación de lecciones vulnerable | Media |
| **OWASP crAPI** | Microservicios (Java, Go, Python) y APIs | Plataforma de APIs vulnerable | Alta |
| **OWASP NodeGoat** | Node.js, MongoDB | Aplicación web vulnerable | Media |
| **Spring PetClinic** | Java, Spring Boot | Aplicación de ejemplo (no vulnerable a propósito) | Baja-media |

*Otra aplicación pública de código abierto se puede proponer antes del 11 de noviembre.*

---

## Fases del proyecto
El proyecto sigue el ciclo de desarrollo seguro (SSDLC) visto en clase. Cada fase produce un entregable concreto.

### Fase 0 - Diagnóstico de madurez (clase 1)
* Evaluar el estado inicial del proyecto elegido con OWASP SAMM (o DSOMM): qué prácticas existen y en qué nivel.
* Definir el nivel objetivo que el equipo quiere alcanzar con el proyecto.
* **Entregable:** tabla de prácticas con nivel actual y objetivo.

### Fase 1 - Diseño de la aplicación (clases 1 y 9)
* **Arquitectura:** diagrama de componentes (frontend, backend, base de datos, servicios externos) y cómo se despliega.
* **Diagrama de flujo de datos (DFD):** actores, procesos, almacenes de datos, flujos y límites de confianza.
* **Clasificación de datos:** qué información maneja (credenciales, datos personales, pagos) y su nivel de sensibilidad.
* **Requisitos de seguridad:** al menos 10 requisitos basados en OWASP ASVS (nivel 1 o 2), por ejemplo autenticación, sesión, control de acceso, validación y registro de eventos.
* **Entregable:** Diseño con los diagramas y la tabla de requisitos.

### Fase 2 - Modelado de amenazas (clase 9)
* Aplicar STRIDE sobre el DFD con OWASP Threat Dragon (o Microsoft Threat Modeling Tool).
* Identificar al menos 12 amenazas, con al menos una por cada categoría STRIDE.
* Para cada amenaza: componente afectado, impacto, probabilidad, prioridad y mitigación.
* Trazar cada mitigación a un requisito ASVS y al control del pipeline que la verifica (SAST, SCA, DAST, IaC o revisión manual).
* **Entregable:** archivo del modelo (JSON de Threat Dragon) + tabla de amenazas y matriz de trazabilidad.

### Fase 3 - Seguridad del código: secretos y SAST (clase 3)
* SAST con Semgrep, CodeQL o SonarQube, con resultados en formato SARIF.
* Extensión SAST en el IDE de los integrantes (captura de un hallazgo en VS Code).
* **Entregable:** configuración en el repositorio + análisis de los 5 hallazgos SAST más relevantes (verdadero o falso positivo y causa).

### Fase 4 - Dependencias: SCA y SBOM (clase 4)
* Generar el SBOM en CycloneDX con Syft o cdxgen en cada build.
* Escanear dependencias con Grype, Trivy u OSV-Scanner, con umbral de bloqueo.
* Publicar el SBOM en Dependency-Track (local con Docker) y configurar al menos una política.
* Activar Dependabot o Renovate.
* Priorizar los hallazgos con CVSS, EPSS, CISA KEV y alcanzabilidad; documentar al menos un caso con VEX.
* **Entregable:** SBOM, captura de Dependency-Track y tabla de priorización.

### Fase 5 - Infraestructura como código (clase 7)
* Escribir un Dockerfile endurecido (imagen base mínima, usuario no root, versiones fijas).
* Escribir la infraestructura de despliegue con Terraform, Ansible o docker-compose/Kubernetes (por ejemplo, un playbook de Ansible que instale y endurezca el servidor).
* Escanear IaC con Checkov, KICS o Trivy config, y la imagen de contenedor con Trivy.
* **Entregable:** archivos de IaC + reporte de escaneo antes y después de corregir.

### Fase 6 - DAST (clase 6)
* ZAP baseline en cada pull request contra la aplicación levantada en el pipeline, con archivo de reglas.
* ZAP full scan local (o programado) contra la instancia del equipo; API scan si la aplicación expone OpenAPI.
* Al menos un escaneo autenticado (Automation Framework o configuración de contexto).
* **Entregable:** reportes de ZAP + análisis de los 5 hallazgos DAST más relevantes.

### Fase 7 - Guardrails, gestión y remediación (clases 5 y 10)
* Protección de rama, revisión obligatoria y checks requeridos.
* Política de bloqueo documentada: qué bloquea y qué solo avisa, en cada herramienta.
* Centralizar los hallazgos en DefectDojo (o el ASPM que elija el equipo).
* Corregir al menos 5 hallazgos (de cualquier herramienta) mediante pull requests que muestren el antes y el después.
* **Entregable:** política de bloqueo, capturas y enlaces a los PR de corrección.

### Fase 8 - Plan de pentest y madurez final (clases 8 y 10)
* Plan de pentest: alcance, reglas de enfrentamiento, metodología (OWASP WSTG), casos de prueba prioritarios según el modelo de amenazas, cronograma y formato del reporte. No se ejecuta: se planifica.
* Madurez final: repetir la evaluación de la Fase 0 y comparar.
* **Entregable:** plan de pentest + tabla de madurez antes y después.

---

## Pipeline mínimo requerido
El pipeline debe ejecutarse en cada pull request hacia main y tener, como mínimo, estas etapas en orden:
1. **SAST:** Semgrep, CodeQL o SonarQube, con salida SARIF.
2. **SCA:** generación del SBOM + escaneo de vulnerabilidades y licencias.
3. **Build:** construcción de la imagen de contenedor.
4. **IaC y contenedor:** Checkov, KICS o Trivy sobre la IaC y la imagen.
5. **DAST:** levantar la aplicación y ejecutar ZAP baseline.
6. **Publicación:** subir reportes como artefactos, el SBOM a Dependency-Track y los hallazgos a DefectDojo.

### Requisitos del pipeline:
* Cada etapa de seguridad tiene un umbral de bloqueo documentado y realmente falla cuando se cumple (sin `|| true` ni `continue-on-error` en los gates).
* Los permisos del token del CI son mínimos (`permissions: contents: read` por defecto).
* Las acciones de terceros están fijadas a una versión (punto extra si están fijadas por SHA).
* Los checks de seguridad son obligatorios en la protección de la rama `main`.

### Esqueleto sugerido de jobs:
```yaml
jobs:
  sast:
    # semgrep / codeql
  sca:
    # syft + grype, Dependency-Track
  build:
    needs: [secretos, sast, sca]
    # docker build
  iac:
    needs: build
    # checkov + trivy image
  dast:
    needs: build
    # app en servicio + ZAP baseline
```

---

## Hitos y entregables

| Hito | Fecha | Qué se entrega |
|---|---|---|
| **Elección de aplicación** | Mié 11-nov-2026 | Equipo, aplicación elegida y enlace al fork |
| **Avance** (cuenta en la nota de trabajos) | Mié 18-nov-2026 | Madurez inicial, diseño, pipeline con SAST y SCA |
| **Entrega final** | Mié 02-dic-2026 | Repositorio completo y documento técnico en PDF |

*Después de la entrega final no se califican cambios en el repositorio: se toma el último commit antes de la hora límite.*

---

## Estructura del repositorio y del documento

### Repositorio (además del código de la aplicación):
```text
.github/workflows/devsecops.yml   # pipeline completo
.semgrep/ o codeql/               # configuración y reglas SAST
.zap/rules.tsv y .zap/plan.yaml   # reglas y plan de ZAP
infra/                            # Dockerfile, Terraform, Ansible o k8s
security/
  diseño/                         # arquitectura, DFD, requisitos ASVS
  threat-model/                   # modelo de Threat Dragon (JSON)
  sbom/vex/                       # SBOM y declaraciones VEX
  politica-de-bloqueo.md          # qué bloquea y qué avisa
pentest-plan.md                   # plan de pentest
README.md                         # cómo ejecutar todo local y en CI
```

### Documento técnico (PDF, 15 a 25 páginas):
1. Portada y resumen ejecutivo (una página).
2. Diagnóstico de madurez inicial.
3. Diseño: arquitectura, DFD, clasificación de datos y requisitos ASVS.
4. Modelo de amenazas y matriz de trazabilidad.
5. Pipeline: arquitectura, herramientas elegidas y justificación, política de bloqueo.
6. Resultados por herramienta (SAST, SCA, IaC, DAST) y priorización.
7. Remediación: los 5 hallazgos corregidos con evidencia, y excepciones documentadas.
8. Plan de pentest.
9. Madurez final, lecciones aprendidas y próximos pasos.

---

## Rúbrica de calificación (20 puntos)

| Criterio | Puntos | Excelente | Insuficiente |
|---|---|---|---|
| **Diseño y requisitos** | 3 | Arquitectura, DFD con límites de confianza, datos clasificados y 10+ requisitos ASVS | Diagramas incompletos o requisitos genéricos |
| **Modelo de amenazas** | 3 | 12+ amenazas STRIDE con mitigación y trazabilidad a requisitos y controles | Lista de amenazas sin mitigación ni trazabilidad |
| **Pipeline (secretos, SAST, SCA, IaC, DAST)** | 6 | Las 7 etapas funcionan en cada PR con reportes y SBOM | Etapas faltantes o que no ejecutan |
| **Guardrails y política de bloqueo** | 2 | Gates reales, protección de rama y política justificada | Controles solo informativos |
| **Remediación y gestión** | 2 | 5+ hallazgos corregidos con PR, DefectDojo y al menos un VEX | Sin correcciones o sin evidencia |
| **Plan de pentest y madurez** | 2 | Plan basado en el modelo de amenazas y comparación de madurez antes y después | Plan genérico o sin evaluación de madurez |
| **Defensa oral** | 2 | Demostración en vivo y respuestas sólidas de ambos integrantes | No puede explicar lo entregado |
| **Total** | **20** | | |

*Puntos extra (hasta 2): escaneo DAST autenticado en el pipeline o reglas SAST propias.*

---

## Recursos de apoyo
* **Madurez:** OWASP SAMM, OWASP DSOMM
* **Requisitos:** OWASP ASVS
* **Modelado de amenazas:** OWASP Threat Dragon, Threat Modeling Cheat Sheet
* **SAST:** Semgrep, CodeQL
* **SCA:** Syft, Grype, Dependency-Track
* **IaC:** Checkov, KICS, Trivy
* **DAST:** ZAP, zaproxy/action-baseline
* **Gestión:** OWASP DefectDojo
* **Pentest:** OWASP Web Security Testing Guide
* Presentaciones y tareas de las clases 1 a 9.