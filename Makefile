.PHONY: up down logs restart ps

COMPOSE_FILE=docker-compose.yml

up:
	docker compose -f $(COMPOSE_FILE) --env-file .env up -d --build $(service) 

down:
	docker compose -f $(COMPOSE_FILE) down $(service)

logs:
	docker compose -f $(COMPOSE_FILE) logs --tail=100 -f $(service)

ps:
	docker compose -f $(COMPOSE_FILE) ps

restart: down up
