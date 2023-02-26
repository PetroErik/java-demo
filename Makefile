DOCKER_COMPOSE=docker-compose
DB= $(COMPOSE_COMPOSE) exec db


all: init run ## Build and run application

init: destroy pull build ## Setup build environment

build: ## build docker image
	$(COMPOSE_COMPOSE) pull
	$(COMPOSE_COMPOSE) build

destroy: ## Destroy containers
	$(COMPOSE_COMPOSE) stop
	$(COMPOSE_COMPOSE) rm -f

pull:
	$(COMPOSE_COMPOSE) pull

run: ## Run containers in background
	$(COMPOSE_COMPOSE) up -d

up: ## Run containers
	$(COMPOSE_COMPOSE) up

stop: ## Stop containers
	$(COMPOSE_COMPOSE) stop

restart: stop run ## Restart containers

test: ## Run all tests
	$(DOCKER_COMPOSE) exec app ./gradlew clean test