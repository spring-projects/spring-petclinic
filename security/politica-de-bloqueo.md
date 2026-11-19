# Política de bloqueo del pipeline DevSecOps

El workflow [DevSecOps](../.github/workflows/devsecops.yml) ejecuta los checks de seguridad en los pull requests dirigidos a `develop` y `main`.

## Criterios de fallo

| Check | Herramienta | Cuándo falla por un hallazgo | Resultados informativos |
| --- | --- | --- | --- |
| `secretos` | Gitleaks | Detecta un posible secreto en el historial de Git. | Los valores detectados se ocultan en el log. |
| `sast` | Semgrep | Una regla de severidad `ERROR` detecta un hallazgo. | La ejecución actual filtra el análisis a reglas `ERROR`. |
| `sca` | Grype | Detecta una vulnerabilidad `High` o `Critical` con corrección disponible en el SBOM generado por Syft. | La tabla del log permite revisar severidades inferiores y hallazgos filtrados, incluidos los que carecen de corrección. |

Los errores de empaquetado o ejecución también pueden dejar un check en rojo. Antes de decidir cómo resolverlo, se revisa el primer paso fallido del job y el motivo que indica su log.

## Protección de ramas

El workflow comunica el resultado de cada check a GitHub. Para impedir un merge cuando falle un control, el equipo debe marcar `secretos`, `sast` y `sca` como checks obligatorios en la protección de la rama destino. La configuración efectiva debe verificarse en GitHub.

## Evidencia

El [registro del pipeline](../evidencia/pipeline-devsecops.md) reúne las corridas de Gitleaks, el bloqueo de Semgrep por la exposición de Actuator, el bloqueo de Grype y las corridas posteriores a las correcciones.
