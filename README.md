# Resource Scheduler Exercise

### Running Commentary
After having studied the documentation I start with the decisions:

it is required a **test-driven development** approach so the first thing I'm going to define and the core of all development process will be tests.

The most important point of logic revolves around the decision of how to implement the mechanism of scheduling with a customizable priority strategy.

I googled about priority queues and I find a Java Documentation [result](http://docs.oracle.com/javase/7/docs/api/java/util/PriorityQueue.html)

that refers to the thread-safe class [PriorityBlockingQueue] (http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/PriorityBlockingQueue.html)

there is an example in the documentation page in which is defined a **Comparable** custom class.

This is what I'm looking for.

First I define **Gateway** and **Message** interfaces.

I define **GatewayImplTest** class and the **testGatewaySend()** test which will test the simulation of sending a message that will be subjected to time consuming operations.

Consequently I implement **GatewayImpl** and **MessageImpl** classes.

I define **ResourceSchedulerTest** class and draft tests deducted from documentation:
```
- testZeroResource()
- testSingleResource()
- testMultipleResource()
- testDeliveryOrder() as the example case described in documentation
```

Conducting tests I implement **ResourceScheduler** class and as decided previously I use a **ThreadPoolExecutor** with a **PriorityBlockingQueue** of **Runnables** to which I pass a **Comparator** of **PriorityRunnable** which adds a priority attribute to **Runnable**.

The tests now passes, and the primary requirement is satisfied.

I implement new tests for the cancellation and termination:
```
- testTerminatedGroupException()
- testCancelledGroupException()
```
consequently I modify **ResourceScheduler** and I implement the corresponding exceptions.

I define **PriorityStrategy** interface, **testCustomPriorityStrategy()** test and add a **PriorityStrategy** parameter in the **ResourceScheduler** constructor.

I define **PriorityStrategyTest** class and tests:
```
- testDefaultPriorityStrategy()
- testCustomPriorityStrategy()
```
consequently I implement **PriorityStrategy** classes.

To test the example case described in the documentation I define an observer that awaits the asynchronous execution of tasks and returns the exact order of
sending messages to the gateway. 

I decided that it is not necessary to use a mocking framework in this case.

### Dependencies
* [jUnit] - for testing
* [Log4j] - for logging

### Installation
Just git import project in Eclipse

### License
MIT

[Log4j]:http://logging.apache.org/log4j/
[jUnit]:http://junit.org/