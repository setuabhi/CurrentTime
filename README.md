Swagger: http://localhost:7777/swagger-ui/index.html

Docker Commands:
   check docker live or not: docker info
1. docker build -t current-usa-time .
2. docker run -p dockerPort:CodePort current-usa-time
3. docker pull seturini/current-usa-time
4. docker tag current-usa-time seturini/current-usa-time:latest
5. docker push seturini/current-usa-time:latest
6. docker ps (list down all active containers)
7. docker ps -a (list down all containers)
8. docker stop ContainerId

**Kubernetes:**
   check kubernetes live or not: kubectl cluster-info
0. In Kubernetes, a Pod is the smallest deployable unit that you can create and manage. It's a fundamental concept in Kubernetes and represents a single instance of a running process in your cluster:
   Imagine you have a web application that consists of a web server and a caching server. You could deploy these two components in separate containers within the same Pod. This allows them to easily communicate with each other (e.g., the web server can access the cache on localhost) and be managed as a single unit.
1. kubectl apply -f deployment.yaml   --> Create pods
2. kubectl apply -f service.yaml  --> A Service routes traffic to pods based on the labels defined in the Service's selector field.
   1 and 2 will start pods
3. kubectl get deployment
4. kubectl get service
5. kubectl get pods
6. to kill deployemnt: kubectl delete deployment current-usa-time-deployment ( will kill pods)
7. to kill service: kubectl delete service current-usa-time-service

   ![img.png](img.png)
   ![img_1.png](img_1.png)

**Kakfa**
1. Start kafka in docker container: docker-compose up -d
2. To see topics: docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --list
3. Create Topic: docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --create --topic my-topic --partitions 3 --replication-factor 1
      Replication Factor: The number of copies of each partition that are maintained across different brokers in your Kafka cluster, we have only one broker so kept it as 1
      How it works: When you create a topic with a replication factor greater than 1, Kafka distributes the partitions and their replicas across the available brokers. For example, with a replication factor of 3 and 3 partitions, each partition will have 3 copies stored on 3 different brokers (ideally).   
      Fault Tolerance: If a broker goes down, the replicas on the other brokers can take over, ensuring that data is still available and consumers can continue to read and write messages.
4. Describe specific topic: docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --describe --topic my-topic
5. Delete topic: docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --delete --topic my-topic
6. Benefits of Using client-id: 
   Traceability: Makes it easier to identify which Kafka client is performing admin operations when reviewing Kafka logs or metrics.
7. Publish : curl -X POST "http://localhost:7777/publish?topic=my-topic&message=HelloKafka"
8. See current offset: docker exec -it kafka kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group my-group
9. See all messages: docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic my-topic --from-beginning --max-messages 10
   We can reset only if consumers are not active
10. Reset of earlier offset: docker exec -it kafka kafka-consumer-groups --bootstrap-server localhost:9092 --group my-group --topic my-topic --reset-offsets --to-earliest --execute
11. Reset to latest offset: docker exec -it kafka kafka-consumer-groups --bootstrap-server localhost:9092 --group my-group --topic my-topic --reset-offsets --to-latest --execute

