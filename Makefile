COMPOSE=docker-compose
DB= $(COMPOSE) exec db


all: init run ## Build and run application

init: destroy pull build ## Setup build environment

build: ## build docker image
	$(COMPOSE) pull
	$(COMPOSE) build

destroy: ## Destroy containers
	-$(COMPOSE) stop
	-$(COMPOSE) rm -f

pull:
	$(COMPOSE) pull

run: ## Run containers in background
	$(COMPOSE) up -d

up: ## Run containers
	$(COMPOSE) up

stop: ## Stop containers
	$(COMPOSE) stop

restart: stop run ## Restart containers