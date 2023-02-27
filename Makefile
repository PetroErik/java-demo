DOCKER_COMPOSE=docker-compose
DB= $(DOCKER_COMPOSE) exec db

all: init run ## Build and run application

init: destroy pull build ## Setup build environment

build: ## build docker image
	$(DOCKER_COMPOSE) pull
	$(DOCKER_COMPOSE) build

destroy: ## Destroy containers
	$(DOCKER_COMPOSE) stop
	$(DOCKER_COMPOSE) rm -f

pull:
	$(DOCKER_COMPOSE) pull

run: ## Run containers in background
	$(DOCKER_COMPOSE) up -d

up: ## Run containers
	$(DOCKER_COMPOSE) up

stop: ## Stop containers
	$(DOCKER_COMPOSE) stop

restart: stop run ## Restart containers

test: ## Run all tests
	$(DOCKER_COMPOSE) exec app ./gradlew test