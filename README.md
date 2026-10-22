Дипломный проект

В проекте настроен CI/CD для Spring Petclinic. Код приложения находится в src/, pipeline - в JenkinsFile, Kubernetes-манифесты - в deploy/manifests/, настройки инфраструктуры - в infra/. Jenkins собирает приложение, запускает тесты и публикует образ в Docker Hub. Образы версионируются тегом sha-<первые 12 символов коммита>. Приложение доступно по адресу http://178.154.250.190/

Для развёртывания нужна Ubuntu VM с SSH-доступом и sudo - её адрес и ключ указываются в infra/ansible/inventory.ini. K3s устанавливается командой ansible-playbook -i infra/ansible/inventory.ini infra/ansible/install-kubernetes.yaml. Локальные мониторинг и логирование запускаются командами docker compose -p devops-monitoring -f infra/monitoring/compose.yaml up -d и docker compose -p devops-logging -f infra/logging/compose.yaml up -d. Для Grafana нужен пароль GRAFANA_ADMIN_PASSWORD в исключённом из Git файле infra/monitoring/.env. Метрики отображаются в Grafana, логи — в Kibana; связь с VM поддерживает пользовательский сервис metrics-tunnel.service - настроен порт-форвадинг

Изменения приложения и инфраструктуры вносятся в ветках feature/... и объединяются с main через Pull Request после проверки. Для деплоя используется Multibranch Pipeline Jenkins с файлом JenkinsFile, агентом maven, Maven tool Maven и credentials dockerhub и yandex-vm: успешная сборка main автоматически разворачивает приложение. Изменения инфраструктуры применяются отдельно через Ansible, Compose и Kubernetes-манифесты. Локальные мониторинг и доставка логов зависят от включенного компьютера

Для задания был выбран Jenkins, настроенный в предыдущих домашних заданий
