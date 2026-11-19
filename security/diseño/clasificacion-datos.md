# Clasificación de datos

## Niveles de sensibilidad

| Nivel | Definición | Ejemplo en PetClinic |
|---|---|---|
| **Público** | Se puede mostrar a cualquiera sin daño. | Lista de veterinarios y sus especialidades |
| **Interno** | Información operativa sin datos personales directos. | Tipos de mascota, nombre y fecha de nacimiento de la mascota |
| **Confidencial** | Datos personales que identifican a una persona. Su fuga causa daño y obligaciones legales. | Nombre, dirección y teléfono de dueños |
| **Restringido** | Secretos que dan acceso a otros sistemas. | Credenciales de base de datos |

## Datos que PetClinic no maneja

- **Pagos:** no procesa ni guarda datos de tarjetas ni de facturación.
- **Credenciales de usuarios:** no hay cuentas ni contraseñas de usuario, porque la aplicación no tiene inicio de sesión. Las únicas credenciales son las de la base de datos.

## Datos de la aplicación

Los campos salen de `db/h2/schema.sql` y de las entidades en `src/main/java/.../owner/` y `.../vet/`.

| Entidad | Campo | Tipo de dato | Nivel | Dueño del dato | Justificación |
|---|---|---|---|---|---|
| Dueño (`owners`) | `first_name`, `last_name` | Dato personal | Confidencial | Dueño de la mascota | Identifica a una persona |
| Dueño | `address`, `city` | Dato personal | Confidencial | Dueño de la mascota | Ubicación física del domicilio |
| Dueño | `telephone` | Dato personal | Confidencial | Dueño de la mascota | Permite contactar y suplantar a la persona |
| Mascota (`pets`) | `name`, `birth_date` | Dato de la mascota | Interno | Dueño de la mascota | Sin valor por sí solo, pero ligado a un dueño identificable |
| Mascota | `owner_id`, `type_id` | Relación | Confidencial | Dueño de la mascota | Une a la mascota con una persona identificable |
| Visita (`visits`) | `visit_date` | Dato operativo | Interno | Clínica | Historial de atención |
| Visita | `description` | Texto libre | Confidencial | Clínica | Puede contener información de salud del animal y datos del dueño, y es texto controlado por el usuario |
| Veterinario (`vets`) | `first_name`, `last_name` | Dato de empleado | Público | Clínica | Se publica en `/vets.html` |
| Especialidad (`specialties`) | `name` | Catálogo | Público | Clínica | Se publica junto al veterinario |
| Tipo de mascota (`types`) | `name` | Catálogo | Público | Clínica | Valores de una lista fija |
| Configuración | `MYSQL_PASS`, `POSTGRES_PASS` | Credencial | Restringido | Equipo de desarrollo | Dan acceso a la base de datos |

## Dónde viaja y dónde se guarda cada nivel

| Nivel | En tránsito hoy | En reposo hoy | Control mínimo esperado |
|---|---|---|---|
| Confidencial | HTTP y JDBC sin cifrar | Base de datos sin cifrado en reposo | TLS, control de acceso, registros sin datos personales |
| Interno | HTTP sin cifrar | Base de datos | TLS y control de acceso |
| Público | HTTP | Base de datos y caché en memoria | Integridad |
| Restringido | Variables de entorno, Secret de k8s en claro | `k8s/db.yml` y `docker-compose.yml` | Gestor de secretos, sin valores en el repositorio |

## Hallazgos

1. Los datos **Confidenciales** (dueños) se transmiten por HTTP sin TLS y sin autenticación previa.
2. La descripción de la visita es texto libre y se muestra en HTML. Requiere validación de entrada y codificación de salida.
3. Las credenciales **Restringidas** aparecen en claro en `k8s/db.yml` (`password: "pass"`) y en `docker-compose.yml`. Son credenciales de desarrollo, pero el patrón debe corregirse.
4. Los registros de la aplicación no deben incluir datos Confidenciales. Está pendiente verificarlo.
