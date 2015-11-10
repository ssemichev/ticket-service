# README #

### What is this repository for? ###

* Quick summary

Java, AKKA, Distributed lock, akka distributed-data

### How do I get set up? ###

* Maven
```
> mvn clean compile
> mvn exec:java -Dexec.mainClass="ticketbooking.Driver"
> mvn test
```

* Sbt
```
> sbt clean compile
> sbt run
> sbt test
```

The proposed design will work for applications with high load, partial failure and network partitions.

The application allows visitors to find and book tickets for an event.

There is a strict requirement that for one show each seat can be booked only once.


For traditional enterprise application the domain model may be represented as a relational database. It may be a good solution because we can use database integrity, transactions and it may guarantee that a seat will be hold or booked one and only once.

But this would require our backend to read data from a database for each visitor request to take details about shows, events, venues, etc. It requires for seats booking but it will add an additional overhead for metadata that are not changing frequently. One of the standard solution to solve this problem is that of caching. But again, it introduces another issues, like how this cache is invalidated - in the best case directly when an administrator changes the data - but these cache invalidation would have to be propagated back to the backend in a reliable fashion.

This also means that for seats booking, if we use one table, it would be a single point of seri serialization for the application and this is what I would like to avoid.

So we basically have two sets of data:

1) metadata, data that normally doesn’t change or added frequently. They should be visible to the users as soon as possible - but small delays are not critical. We can say that we need to guarantee eventual consistency between nodes, for example if an administrator adds a new event, it should be  visible for all our visitors as soon as possible but zero latency is not required. We can call these data as facts or facts database. The amount of facts may grow but is always expected to fit into a node’s RAM.

2) data that represents seats bookings and reservations and must be serialized. This is the part that should to be scalable. 


Facts can be distributed as CRDTs via akka distributed-data. That means that each backend node can create or update a fact and this update will be automatically propagated to all other cluster members using akka’s cluster gossip mechanism. AKKA has LWWMap (last write wins map) as CRDT-structure - so our facts database amy act pretty much like a distributed cache. Thus it doesn’t really make any guarantees over concurrent fact modification - only that all nodes will end in the same state. This is not so much a problem since the creation of facts can be coordinated between administrators.

This setup makes it possible that each backend node can answer any non-booking related user requests locally, without further network interaction. New facts will be distributed to all cluster nodes eventually and with low latency.

I use MemoryFactDatabase actor for this prototype, it should be replaced by LWWMap implementation.

Within a booking request, a visitor can book tickets for different sections of a venue for a show. The decision over an booking request must therefore be made sequentially by something that keeps track over all reserved seats of a performance. This is the perfect use case for an actor: PerformanceBookingNode.

To ensure that there exists only one single PerformanceBookingNode for a single performance in the cluster AKKA cluster-sharding should be used. This allows an equal distribution of load or responsibility for performance bookings over the whole cluster.

To reduce latency with extra high load, the location of the performances venue could be used to co-locate the deployment to a node that is closer to the venues location, assuming that visitors rather book tickets for venues where there are close to. This design with AKKA cluster-sharding uses a different conceptual paradigm to solve distributed lock problem in comparing with Zookeeper / Redisson algorithms.

Existing bookings and holds must not be to get lost when the cluster is, for whatever reason, going down. This is why akka persistence should be used to persist the change of seat reservations using event sourcing.

Persisting the seat reservations also ensures that the cluster can easily recover from a partial failure. As soon as akka-cluster detects a defect node, it will relocate all PerformanceBookingNode from the failing node to another healthy cluster node. The revived PerformanceBookingNode can be recreated on the healthy node by simply replaying all persisted events (seat holds and booking).

Akka cluster sharding uses the same mechanism to deal with re-balancing if the number of cluster nodes changes.

Compared to an RDMS implementation, the current serialization scope is no longer the table (the set of all seat reservations) but just a single performance. It is also prepared for elastic deployments where nodes come up and go offline at any speed. In this model we don’t need to implement multithreading synchronization. Behind the scenes it will run sets of actors on sets of real threads, where typically many actors share one thread, and subsequent invocations of one actor may end up being processed on different threads. 
Thus it is very easy to test such implementation, see BookingTicketServiceTest.

### Actors model

![Alt text](images/actors_model.png?raw=true "Actors Model")

### High level architecture diagram

![Alt text](images/high-level-architecture_diagram.png?raw=true "High level architecture diagram")

