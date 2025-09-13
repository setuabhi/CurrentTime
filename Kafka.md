Swagger: http://localhost:7777/swagger-ui/index.html



**Kafka Commands**

      1. Start kafka in docker container: docker-compose up -d
      2. To see topics: docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --list
      3. Create Topic: docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --create --topic my-topic --partitions 3 --replication-factor 1

      Replication Factor: The number of copies of each partition that are maintained across different brokers in your Kafka cluster, we have only one broker so kept it as 1
      How it works: When you create a topic with a replication factor greater than 1, Kafka distributes the partitions and their replicas across the available brokers. For example, with a replication factor of 3 and 2 partitions, each partition will have 3 copies stored on 3 different brokers (ideally), Each replica contains the exact same data as the other replicas of that partition.. 

      Fault Tolerance: If a broker goes down, the replicas on the other brokers can take over, ensuring that data is still available and consumers can continue to read and write messages.

      Consumer LAG: Group property : Messages in the topic - Messages Consumed

      If there is one partition and two consumer groups, the same message will be read by both consumer groups independently.

      The maximum number of active consumers in a group should be equals to the number of partitions. Extra consumers will sit idle.

      Different Partition will have different data, we can control it too by passing same key, same key data will go to same partition
      Let's say we have 2 partition, one consumer group and 2 consumers setup , then c1 will get p1 data and c2 will get p2 data while if only one consumer present then c1 will get both p1, p2 data

      Kafka Streams used send messaage from one topic to another (KafkaStream.java) we need to create bean which will return KStream

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

*** Kafka Interview prep ***

      1. Replication Factor 2 and partition 3 means if any publisher publish data it will be divided into 3 partitions and all 3 partitions data will be stored in kafka1 and kafka2 broker

      2. Replication factor <= No of brokers, zookeeper + kafka1 + kafka2 = Kafka cluster

      3. Leader vs Follower
         Leader: The broker that handles all reads and writes for a partition. Producers send data here. Consumers also fetch data here.
         Follower: The broker that just replicates from the leader. It doesn’t serve client requests unless the leader fails.
         Partition	Leader	Follower
         P0	        kafka1	kafka2
         P1	        kafka1	kafka2
         P2	        kafka2	kafka1

         What happens in practice:
            Write: Producer sends message to partition’s leader (say P0 → kafka1).
            Replication: Follower (kafka2) pulls the message from kafka1 and stores it.
            Ack to producer: If acks=all, producer waits until both leader and follower confirm.
            Failover: If kafka1 (leader of P0) goes down → follower kafka2 is promoted to leader.
      
      4. ISR = In-Sync Replicas: It’s the set of  followers that are fully caught up with the leader’s log. 
         How it works:
            Each partition has 1 leader and N-1 followers.
            Followers constantly pull data from the leader.
            If a follower is keeping up (not lagging behind more than a threshold), it’s part of the ISR set otherwise it won't be part of ISR
            Only ISR members can be promoted to leader if the current leader fails.

      5. acks in Kafka = Producer acknowledgment setting
         It controls when the producer considers a message successfully “written” (and stops worrying about it).
         acks=0
            Producer doesn’t wait for any acknowledgment.
            Fire and forget → Fastest but unsafe (data may be lost if leader crashes before replication).

         acks=1 (default)
            Producer gets an ack from the leader only.
            Message is considered successful once the leader writes it to its local log.
            Followers may not have replicated yet → If leader dies immediately, data loss is possible.

         acks=all (or acks=-1)
            Producer waits until all in-sync replicas (ISR) confirm the write.
            Safest, but slowest.
            Guarantees no acknowledged message is lost as long as at least one ISR is alive.

      6. retries: 
         Default: retries=2147483647 (basically infinite, in newer Kafka versions)
         In older versions, default was 0 (no retries).
         When retries are enabled:
            Producer sends a message to the leader.
            If leader is unavailable or replication fails → retry happens after some delay.
            Retries stop once the message is acknowledged or retry count is exhausted.
            If all retries fail → exception is thrown back to your app.

      7. Zombie broker and epoch:
         What’s a Zombie Broker?
            A zombie broker is a broker that:
               Was a leader for a partition which crashed
               Meanwhile, Kafka’s controller promoted another replica as the new leader.
               The old broker comes back online but still thinks it’s the leader (because it’s working with stale metadata).

         👉 Problem: If this zombie accepts writes from producers, those writes are lost (because the real leader + ISR don’t know about them).

         ⚡ How Kafka fixes this → Epochs (understand as counter)

         Kafka uses a leader epoch to prevent zombies from corrupting data.
         Leader Epoch: Every time a new leader is elected for a partition, Kafka increments a leader epoch number.
         Producers and followers know which epoch is current.

         How it works:
            Partition P0 has leader on Broker1 with epoch 5.
            Broker1 crashes → Broker2 becomes leader with epoch 6.
            Broker1 comes back online (zombie). It still thinks it’s leader with epoch 5.
            Producers/followers now only accept writes from epoch=6 leader.
            Broker1 (zombie) tries to act like leader → cluster rejects it because its epoch is outdated.
            
            👉 This ensures only the latest leader can accept writes, killing the zombie problem.

      8. Offset: Property of consumer group that's why one partiton can have multiple Consumer group and offset will be different in each CG

        Types:
        a. Auto Commit:
                Kafka automatically commits offsets at a fixed interval.
            Controlled by:
                enable.auto.commit=true
                auto.commit.interval.ms=5000  # default 5 sec

            Behavior:
                Consumer polls messages.
                Every 5s (default), Kafka commits the latest offset the consumer has returned.
                If consumer crashes after reading but before processing → offset is already committed = message loss risk (message skipped).
                If processing is super fast → can commit offsets for messages not fully processed = data loss.
                👉 Good for simple apps where "at-most-once" semantics are okay.

        b. Manual Commit: You control when offsets get committed. Ex: ack.acknowledge()

      9. _consumer_offsets topic: It’s a Kafka internal compacted topic (created automatically) which stores committed offsets of all consumer groups.
            Store in key value par, where key: (consumer_group, topic, partition) and value: latest committed offset.
            Because it’s a compacted topic, Kafka only keeps the latest offset per key, cleaning up older commits.

            Example:
                Say you have:
                    Topic: orders with partition 0.
                    Consumer group: payment-service.

                Flow:
                    Consumer in payment-service processes offsets 0 → 49.
                    It calls commit.
                    Kafka writes to _consumer_offsets:
                    key = (payment-service, orders, 0)
                    value = 50 → meaning: next message to consume = offset 50.

                    If consumer crashes and restarts:
                    Broker looks up _consumer_offsets.
                    Finds entry (payment-service, orders, 0) → 50.
                    Consumer resumes from offset 50.
        
      10. Kafka Delivery Semantics:
          a. At-most-once : if we do ack.acknowledge() before sout
          b. At-least-once : if we do ack.acknowledge() after sout, lets assume code fails in sout and we start consumer again, we will again get same message from topic
          c. Exactly-once: Commit + processing in same transaction, No loss, no dupes

