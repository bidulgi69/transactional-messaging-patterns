all: compile setup run test

compile:
	chmod +x ./gradlew && \
	./gradlew :api:build && \
	./gradlew :services:order-service:build && \
	./gradlew :services:ticket-service:build && \
	docker-compose build

setup:
	docker-compose up -d mongo && \
	echo "Waiting for mongo container starting..." && \
	sleep 5 && \
	docker-compose exec mongo mongosh -u root -p root --eval "printjson(rs.initiate())"

run:
	docker-compose up -d && \
	chmod +x script/health-check-servers.sh && script/health-check-servers.sh

test:
	chmod +x script/post-order.sh && script/post-order.sh


clean:
	docker-compose down --remove-orphans && \
	docker volume rm $$(docker volume ls -qf dangling=true) && \
	docker rmi $$(docker images | grep -e dove-message-log-tailing)