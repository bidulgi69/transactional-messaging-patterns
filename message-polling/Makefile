all: compile run test

compile:
	chmod +x ./gradlew && \
	./gradlew :api:build && \
	./gradlew :services:order-service:build && \
	./gradlew :services:ticket-service:build && \
	docker-compose build

run:
	docker-compose up -d && \
	chmod +x script/health-check-servers.sh && script/health-check-servers.sh

test:
	chmod +x script/post-order.sh && script/post-order.sh

clean:
	docker-compose down --remove-orphans && \
	docker volume rm $$(docker volume ls -qf dangling=true) && \
	docker rmi $$(docker images | grep -e dove-message-polling -e message-polling)