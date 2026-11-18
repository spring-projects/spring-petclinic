# Evidencia del pipeline DevSecOps

## Paso 1 · Escaneo de secretos con Gitleaks

**Entregable.** El job `secretos` está definido en [`.github/workflows/devsecops.yml`](../.github/workflows/devsecops.yml). GitHub Actions lo ejecuta al abrir o actualizar un pull request con destino a `develop` o `main`.

**Cómo funciona.** GitHub Actions descarga el repositorio con su historial de Git y ejecuta Gitleaks con permisos de solo lectura. Gitleaks busca posibles contraseñas, tokens y llaves en los commits, incluso si esos valores se borraron de la versión actual del archivo. Si detecta un posible secreto, el job falla y los valores detectados se ocultan en el log. El escaneo usa la imagen oficial fijada por digest y no requiere configurar una clave de API.

**Resultado observado.** En la corrida de GitHub Actions que se muestra abajo, el job `secretos` terminó en verde en 12 segundos. La captura sirve como evidencia de la ejecución del escaneo de secretos.

![Job secretos completado en GitHub Actions en 12 segundos](gitleaks.png)

**Para la defensa.** GitHub Actions ejecuta el workflow del PR. Gitleaks realiza el análisis del historial y su resultado determina si el check pasa o falla. Si configuramos el check como obligatorio en la rama destino, un fallo impide fusionar el PR.

## Paso 2 · Análisis estático con Semgrep

**Entregable.** El job `sast` ejecuta Semgrep con una [regla propia de PetClinic](../.semgrep/petclinic-rules.yml). Semgrep lee los archivos del repositorio y compara su contenido con las reglas configuradas. El análisis se ejecuta sin iniciar la aplicación.

**Qué revisa esta primera regla.** Busca la propiedad `management.endpoints.web.exposure.include=*` en los archivos `application*.properties` de `src/main/resources`. El asterisco permite exponer por HTTP todos los endpoints de monitoreo y diagnóstico disponibles de Spring Boot Actuator. La regla reporta esta configuración con severidad `ERROR` y el job falla si la detecta.

**Resultado observado.** En la corrida #3 del workflow DevSecOps para el PR #1, `secretos` pasó y `sast` falló. Semgrep ejecutó una regla sobre tres archivos y reportó un hallazgo bloqueante, `semgrep.petclinic-actuator-wildcard-exposure`, en la [línea 21 de `application.properties`](../src/main/resources/application.properties#L21). El análisis se completó y el código de salida `1` hizo que GitHub Actions marcara el job en rojo.

![Corrida 3 del workflow DevSecOps con secretos en verde y sast en rojo](semgrep.png)

**Aviso de la regla.** Semgrep recomendó indicar que la ruta de los archivos se cuenta desde la raíz del repositorio. Se agregó `/` al inicio del patrón. Queda pendiente comprobar el ajuste en la próxima corrida.

## Paso 3 · Corrección de la exposición de Actuator

En `fix/actuator-exposure` se cambió el valor de `management.endpoints.web.exposure.include` de `*` a `health,info` en [`application.properties`](../src/main/resources/application.properties#L20). Con este cambio, la exposición web de Actuator se limita a los endpoints de salud e información.

**Resultado observado.** En la corrida #5 del workflow DevSecOps para el PR #2 hacia `develop`, `secretos` y `sast` pasaron. El workflow terminó en 20 segundos. El resultado de `sast` confirma que la regla de Actuator ya no generó el hallazgo bloqueante documentado en el paso 2.

![Corrida 5 del workflow DevSecOps con secretos y sast en verde](fix_actuator.png)

## Paso 4 · Inventario de dependencias con Syft

**Entregable.** El job `sca` empaqueta PetClinic con Maven y ejecuta Syft sobre el JAR generado. Syft identifica los componentes incluidos en el paquete y publica su inventario en formato CycloneDX JSON como artefacto `sbom.cdx.json`. El job usa Java 17, que es la versión declarada en el POM, y omite las pruebas en esta compilación dedicada al inventario.

**Resultado observado.** El job `sca` pasó en 55 segundos. El [SBOM descargado](sbom.cdx.json) se pudo leer como JSON y declara el formato CycloneDX 1.7. El inventario contiene 94 componentes, de los cuales 93 están clasificados como bibliotecas. Entre ellas figuran Spring Boot 4.1.0, Tomcat embebido 11.0.22 y H2 2.4.240.

![Checks del PR en verde, incluido el job sca](SCA.png)

## Paso 5 · Análisis de vulnerabilidades con Grype

**Entregable.** El job `sca` ejecuta Grype sobre el SBOM que Syft genera en la misma corrida. Grype compara los componentes con la base de vulnerabilidades de Anchore sin usar `NVD_API_KEY`.

**Política de bloqueo.** `--only-fixed` limita el bloqueo a hallazgos con corrección disponible y `--fail-on high` hace fallar el job cuando alguno tiene severidad `High` o `Critical`. La tabla del log muestra también hallazgos filtrados, como los que todavía no tienen corrección.

**Incidencia de empaquetado.** El primer intento se detuvo antes del análisis porque `NoHttp` detectó seis URLs del esquema y de licencias en la copia generada `evidencia/sbom.cdx.json`. Maven y Gradle excluyen específicamente esa copia de la entrada de `NoHttp` para conservar el artefacto original.

**Resultado observado.** Tras el ajuste, `sca` completó el empaquetado y la generación del SBOM. Grype analizó el inventario en 53 segundos y devolvió el código de salida `2` porque encontró dependencias con hallazgos `High` o `Critical` y correcciones disponibles. El log reportó hallazgos bloqueantes en Tomcat embebido, Spring MVC, Jackson y el controlador JDBC de PostgreSQL. Este resultado documenta que la política de bloqueo se aplicó. La remediación se comprobará con un SBOM nuevo y otra corrida del mismo check.

![Evidencia de la corrida de SCA](sbom%20y%20politica%20de%20bloqueo.png)

## Paso 6 · Actualización de dependencias

Spring Boot se actualizó de `4.1.0` a `4.1.2` en Maven y Gradle. La versión reúne las actualizaciones de Tomcat embebido `11.0.27`, Spring MVC `7.0.10`, Jackson `3.1.7` y PostgreSQL JDBC `42.7.14`. [Versiones gestionadas por Spring Boot](https://docs.spring.io/spring-boot/appendix/dependency-versions/coordinates.html)

**Resultado observado.** En la corrida DevSecOps #12 del PR #3, `secretos`, `sast` y `sca` pasaron. `sca` tardó 1 minuto y 21 segundos e incluyó el empaquetado, la publicación del SBOM y el análisis de Grype. Con el mismo umbral, Grype no detectó hallazgos bloqueantes en esa corrida.

![Corrida DevSecOps 12 con el análisis de Grype en verde](sbom%20y%20politica%20de%20bloqueo%20solucionada.png)

**Verificación pendiente.** Confirmar los checks independientes de Maven y Gradle en el PR. El SBOM del paso 4 conserva el inventario anterior a la actualización. El inventario actualizado está disponible como artefacto de la corrida #12.

## Paso 7 · Publicación de Semgrep en Code scanning

Semgrep escribe sus resultados en `semgrep.sarif`. GitHub Actions publica ese reporte en Code scanning para los PR con origen y destino en el mismo repositorio. Si Semgrep genera el reporte y detecta un hallazgo bloqueante, el job conserva el código de salida y publica el SARIF de esa corrida. El permiso `security-events: write` se concede al job `sast`.

**Resultado observado.** En la corrida DevSecOps #14 del PR #3 hacia `develop`, el job `sast` terminó en verde en 25 segundos. El paso **Publicar SARIF en GitHub Code scanning** validó `semgrep.sarif` y completó la carga de resultados.

**Evidencia pendiente.** Incorporar al repositorio la captura de la corrida #14 y comprobar el análisis en la pestaña **Security → Code scanning**. Con la configuración corregida de Actuator, se espera que esa regla no genere una alerta en esta corrida.
