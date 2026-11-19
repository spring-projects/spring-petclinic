# Evaluación de madurez inicial (OWASP SAMM v2)

**Fecha de la evaluación:** 18-nov-2026
**Alcance:** repositorio `f-magdiel/spring-petclinic` (aplicación, pipeline y manifiestos de despliegue).
**Escala:** 0 = sin práctica, 1 = práctica inicial y ad hoc, 2 = definida y repetible, 3 = optimizada y medida.
**Objetivo:** nivel que queremos demostrar en la entrega final del 2-dic-2026. Se repite la evaluación en la Fase 8 para comparar.

## Resumen

| Función | Prácticas | Promedio actual | Promedio objetivo |
|---|---|---|---|
| Gobernanza | 3 | 0.3 | 1.3 |
| Diseño | 3 | 0.0 | 1.7 |
| Implementación | 3 | 1.7 | 2.0 |
| Verificación | 3 | 1.0 | 1.7 |
| Operaciones | 3 | 0.3 | 1.0 |

## Gobernanza

| Práctica | Actual | Objetivo | Justificación |
|---|---|---|---|
| Estrategia y métricas (SM) | 0 | 1 | No hay plan de seguridad ni métricas. Se definirá una política de bloqueo y se medirán hallazgos abiertos y corregidos. |
| Políticas y cumplimiento (PC) | 1 | 2 | Existe `security/politica-de-bloqueo.md`, pero solo cubre el pipeline. Faltan requisitos de seguridad formales (ASVS) y un registro de excepciones. |
| Educación y guía (EG) | 0 | 1 | El equipo no tiene guías de codificación segura. Se documentan los requisitos ASVS aplicables a PetClinic como guía mínima. |

## Diseño

| Práctica | Actual | Objetivo | Justificación |
|---|---|---|---|
| Evaluación de amenazas (TA) | 0 | 2 | No hay modelo de amenazas. Se hará un modelo STRIDE con 12 o más amenazas a partir del DFD de `security/diseño/dfd.md`. |
| Requisitos de seguridad (SR) | 0 | 2 | Los únicos controles actuales son validaciones de Bean Validation. Se definen 10 o más requisitos ASVS verificables. |
| Arquitectura de seguridad (SA) | 0 | 1 | Hay diagrama de arquitectura nuevo, pero la aplicación no tiene autenticación, TLS ni cabeceras de seguridad por diseño. Se documentan los límites de confianza y se endurece el despliegue. |

## Implementación

| Práctica | Actual | Objetivo | Justificación |
|---|---|---|---|
| Construcción segura (SB) | 2 | 2 | El workflow `devsecops.yml` genera SBOM CycloneDX, escanea con Grype y falla con High o Critical con corrección. |
| Despliegue seguro (SD) | 1 | 2 | El pipeline usa permisos mínimos y acciones fijadas por SHA. Los manifiestos k8s y docker-compose tienen contraseñas en claro y no tienen `securityContext`. |
| Gestión de defectos (DM) | 2 | 2 | Los hallazgos de Semgrep y Grype se publican en Code scanning y se corrigen por PR (#2 Actuator, #3 Spring Boot 4.1.2), con evidencia en `evidencia/`. Falta priorización con CVSS, EPSS y KEV, y un tablero central (DefectDojo), que se tratarán como mejora dentro del mismo nivel. |

## Verificación

| Práctica | Actual | Objetivo | Justificación |
|---|---|---|---|
| Evaluación de arquitectura (AA) | 0 | 1 | No se ha revisado la arquitectura. El DFD y el modelo STRIDE servirán como primera revisión. |
| Pruebas basadas en requisitos (RT) | 1 | 2 | Hay pruebas unitarias y de integración del código de negocio, pero ninguna prueba de seguridad ligada a requisitos. Se añadirán pruebas para los requisitos ASVS de validación de entrada. |
| Pruebas de seguridad (ST) | 2 | 2 | SAST (Semgrep con una regla propia contra la exposición de Actuator) y SCA (Grype) corren en cada PR hacia `develop` y `main`, y publican SARIF en Code scanning. Se añadirán DAST (ZAP) e IaC. |

## Operaciones

| Práctica | Actual | Objetivo | Justificación |
|---|---|---|---|
| Gestión de incidentes (IM) | 0 | 1 | No hay plan de respuesta ni contacto de seguridad. Se agregará un `SECURITY.md` mínimo si el tiempo lo permite. |
| Gestión del entorno (EM) | 1 | 2 | El SBOM cubre el inventario de dependencias; Dependabot está pendiente de configurar. La configuración del entorno (Actuator, contraseñas, TLS) no está endurecida ni verificada. |
| Gestión operativa (OM) | 0 | 0 | Fuera del alcance académico: no hay operación productiva ni gestión de datos en producción. |

## Qué cambia entre hoy y el 2-dic

- **Mayor salto:** Diseño (de 0 a 2) con el modelo de amenazas y los requisitos ASVS.
- **Refuerzo:** Verificación y Construcción segura con DAST, IaC y SBOM publicado en Dependency-Track.
- **Aceptado sin cambio:** Gestión operativa (OM), porque PetClinic no se opera en producción.

> **Nota:** los niveles son una autoevaluación del equipo basada en la evidencia del repositorio. Deben revisarse en la reunión de integración y ajustarse si alguien discrepa.
