Swagger: http://localhost:7777/swagger-ui/index.html

Docker Commands:

1. check docker live or not: docker info
2. docker build -t current-usa-time .
3. docker run -p dockerPort:CodePort current-usa-time
4. docker pull seturini/current-usa-time
5. docker tag current-usa-time seturini/current-usa-time:latest
6. docker push seturini/current-usa-time:latest
7. docker ps (list down all active containers)
8. docker ps -a (list down all containers)
9. docker stop ContainerId
10. docker-compose.yml used to define and configure multiple containers
11. docker run - to create and start container
12. docker start - to only start container
13. docker stop - to stop container
14. docker rm ContainedId - to remove container
15. docker-compose.override.yml - to override setting and config of docker-compose.yml
16. docker logs containerId - To see logs

**Kubernetes:**
   Why we need it ?
1. Scaling: Automatically scale containers based on demand.
2. High Availability: Ensure containers are restarted and rescheduled if they fail.
3. Load Balancing: Distribute traffic across multiple containers.
4. Self-Healing: Automatically restart or reschedule failed containers.
5. Service Discovery: Manage networking between containers automatically.
6. Automated Rollouts: Handle safe deployments and rollbacks.
7. Resource Management: Allocate CPU/memory to prevent overuse.
8. Multi-container Coordination: Manage complex applications with multiple containers.
9. Environment Consistency: Ensure consistent behavior across environments.
10. Multi-cloud Support: Run containers across different cloud providers.


   check kubernetes live or not: kubectl cluster-info

1. In Kubernetes, a Pod is the smallest deployable unit that you can create and manage. It's a fundamental concept in Kubernetes and represents a single instance of a running process in your cluster:
2. Imagine you have a web application that consists of a web server and a caching server. You could deploy these two components in separate containers within the same Pod. This allows them to easily communicate with each other (e.g., the web server can access the cache on localhost) and be managed as a single unit.
3. kubectl apply -f deployment.yaml   --> Create pods
4. kubectl apply -f service.yaml  --> A Service routes traffic to pods based on the labels defined in the Service's selector field.
5. kubectl get deployment
6. kubectl get service
7. kubectl get pods
8. to kill deployment: kubectl delete deployment current-usa-time-deployment ( will kill pods)
9. to kill service: kubectl delete service current-usa-time-service

   ![img.png](img.png)
   ![img_1.png](img_1.png)

**Kafka**
1. Start kafka in docker container: docker-compose up -d
2. To see topics: docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --list
3. Create Topic: docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --create --topic my-topic --partitions 3 --replication-factor 1
      Replication Factor: The number of copies of each partition that are maintained across different brokers in your Kafka cluster, we have only one broker so kept it as 1
      How it works: When you create a topic with a replication factor greater than 1, Kafka distributes the partitions and their replicas across the available brokers. For example, with a replication factor of 3 and 2partitions, each partition will have 3 copies stored on 3 different brokers (ideally), Each replica contains the exact same data as the other replicas of that partition.. 
      Fault Tolerance: If a broker goes down, the replicas on the other brokers can take over, ensuring that data is still available and consumers can continue to read and write messages.
      Consumer LAG: Group property : Messages in the topic - Messages Consumed
      If there is one partition and two consumer groups, the message will be read by both consumer groups independently.
      The maximum number of active consumers in a group should be equals to the number of partitions. Extra consumers will sit idle.
      Different Partition will have different data, we can control it too by passing same key, same key data will go to same partition
      Kafka Streams used send messaage from one topic to another (KafkaStream.java)
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

