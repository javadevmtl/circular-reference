# circular-reference project
Project is simple there is one resource at `http://localhost:8080/movies/`.

You can add a movie by sending HTTP post to `http://localhost:8080/movies/action/_add`

Sample Json that will NOT cause error:
```
{
    "tag": "action",
    "title": "Terminator"
}
```

Sample Json that will cause **CIRCULAR REFERENCE**:
```
{
    "tag": "commedy",
    "title": "Dude where is my car?"
}
```

## How it works
The premise is simple. POST a movie to the resource.

- It will add to the `movies` topic.


### MovieProcessor.java

- If the **title** does not contain `Dude` then the message will **ACKED**
- If the **title** contains `Dude` then the message will be **NACKED** and sent to `movies-retry` topic.

### MovieRetryProcessor.java
- It will automatically **NACK** the movie with `return Uni.createFrom().failure(new IllegalStateException("We tried everything in our power to like this movie. Unfortunately we do not want to watch it any more!"));` to send it to the `movies-dlq` topic. 

**Instead we get [CIRCULAR REFERENCE:java.lang.IllegalStateException: We tried everything in our power to like this movie. Unfortunately we do not want to watch it any more!]**

The full stack:
```
2021-01-26 10:41:03,028 ERROR [io.sma.rea.mes.kafka] (vert.x-eventloop-thread-0) SRMSG18207: Unable to dispatch message to Kafka: io.smallrye.mutiny.CompositeException: Multiple exceptions caught:
	[Exception 0] java.lang.IllegalStateException: We tried everything in our power to like this movie. Unfortunately we do not want to watch it any more!
	[Exception 1] io.smallrye.reactive.messaging.ProcessingException: SRMSG00103: Exception thrown when calling the method com.acme.MovieRetryProcessor#retry
	at io.smallrye.mutiny.operators.UniOnItemOrFailureFlatMap.invokeAndSubstitute(UniOnItemOrFailureFlatMap.java:35)
	at io.smallrye.mutiny.operators.UniOnItemOrFailureFlatMap$1.onFailure(UniOnItemOrFailureFlatMap.java:63)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$1.lambda$onFailure$2(ContextPropagationUniInterceptor.java:36)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$1.onFailure(ContextPropagationUniInterceptor.java:36)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.onFailure(UniSerializedSubscriber.java:100)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$1.lambda$onFailure$2(ContextPropagationUniInterceptor.java:36)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$1.onFailure(ContextPropagationUniInterceptor.java:36)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.onFailure(UniSerializedSubscriber.java:100)
	at io.smallrye.mutiny.operators.UniDelegatingSubscriber.onFailure(UniDelegatingSubscriber.java:29)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$1.lambda$onFailure$2(ContextPropagationUniInterceptor.java:36)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$1.onFailure(ContextPropagationUniInterceptor.java:36)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.onFailure(UniSerializedSubscriber.java:100)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$1.lambda$onFailure$2(ContextPropagationUniInterceptor.java:36)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$1.onFailure(ContextPropagationUniInterceptor.java:36)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.onFailure(UniSerializedSubscriber.java:100)
	at io.smallrye.mutiny.operators.uni.builders.KnownFailureUni.subscribing(KnownFailureUni.java:24)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:54)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:49)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:30)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$2.lambda$subscribing$0(ContextPropagationUniInterceptor.java:47)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$2.subscribing(ContextPropagationUniInterceptor.java:47)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:54)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:49)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:30)
	at io.smallrye.mutiny.operators.UniOnItemTransformToUni.handleInnerSubscription(UniOnItemTransformToUni.java:57)
	at io.smallrye.mutiny.operators.UniOnItemTransformToUni.invokeAndSubstitute(UniOnItemTransformToUni.java:43)
	at io.smallrye.mutiny.operators.UniOnItemTransformToUni$2.onItem(UniOnItemTransformToUni.java:74)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$1.lambda$onItem$1(ContextPropagationUniInterceptor.java:31)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$1.onItem(ContextPropagationUniInterceptor.java:31)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.onItem(UniSerializedSubscriber.java:86)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$1.lambda$onItem$1(ContextPropagationUniInterceptor.java:31)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$1.onItem(ContextPropagationUniInterceptor.java:31)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.onItem(UniSerializedSubscriber.java:86)
	at io.smallrye.mutiny.operators.UniCreateFromCompletionStage.lambda$forwardFromCompletionStage$1(UniCreateFromCompletionStage.java:31)
	at java.base/java.util.concurrent.CompletableFuture.uniWhenComplete(CompletableFuture.java:859)
	at java.base/java.util.concurrent.CompletableFuture.uniWhenCompleteStage(CompletableFuture.java:883)
	at java.base/java.util.concurrent.CompletableFuture.whenComplete(CompletableFuture.java:2251)
	at java.base/java.util.concurrent.CompletableFuture.whenComplete(CompletableFuture.java:143)
	at io.smallrye.mutiny.operators.UniCreateFromCompletionStage.forwardFromCompletionStage(UniCreateFromCompletionStage.java:23)
	at io.smallrye.mutiny.operators.UniCreateFromCompletionStage.subscribing(UniCreateFromCompletionStage.java:51)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:54)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:49)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:30)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$2.lambda$subscribing$0(ContextPropagationUniInterceptor.java:47)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$2.subscribing(ContextPropagationUniInterceptor.java:47)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:54)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:49)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:30)
	at io.smallrye.mutiny.operators.UniOnItemTransformToUni.subscribing(UniOnItemTransformToUni.java:65)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:54)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:49)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:30)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$2.lambda$subscribing$0(ContextPropagationUniInterceptor.java:47)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$2.subscribing(ContextPropagationUniInterceptor.java:47)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:54)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:49)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:30)
	at io.smallrye.mutiny.operators.UniOnItemOrFailureFlatMap.subscribing(UniOnItemOrFailureFlatMap.java:49)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:54)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:49)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:30)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$2.lambda$subscribing$0(ContextPropagationUniInterceptor.java:47)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationUniInterceptor$2.subscribing(ContextPropagationUniInterceptor.java:47)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:54)
	at io.smallrye.mutiny.operators.UniSerializedSubscriber.subscribe(UniSerializedSubscriber.java:49)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:30)
	at io.smallrye.mutiny.operators.UniOnItemTransformToMulti.subscribe(UniOnItemTransformToMulti.java:36)
	at io.smallrye.mutiny.operators.AbstractMulti.subscribe(AbstractMulti.java:31)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationMulti$1.run(ContextPropagationMultiInterceptor.java:63)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationMulti.subscribe(ContextPropagationMultiInterceptor.java:57)
	at io.smallrye.mutiny.groups.MultiCreate$2.subscribe(MultiCreate.java:185)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationMulti$1.run(ContextPropagationMultiInterceptor.java:61)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationMulti.subscribe(ContextPropagationMultiInterceptor.java:57)
	at io.smallrye.mutiny.operators.multi.MultiFlatMapOp$FlatMapMainSubscriber.onItem(MultiFlatMapOp.java:194)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.subscription.SerializedSubscriber.onItem(SerializedSubscriber.java:74)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.subscription.SafeSubscriber.onNext(SafeSubscriber.java:98)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.lambda$onNext$1(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.onNext(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.subscription.SafeSubscriber.onNext(SafeSubscriber.java:98)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.streams.utils.ConnectableProcessor.onNext(ConnectableProcessor.java:122)
	at io.smallrye.mutiny.streams.utils.WrappedProcessor.onNext(WrappedProcessor.java:44)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.lambda$onNext$1(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.onNext(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.operators.multi.MultiMapOp$MapProcessor.onItem(MultiMapOp.java:50)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.lambda$onNext$1(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.onNext(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.subscription.SafeSubscriber.onNext(SafeSubscriber.java:98)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.subscription.SafeSubscriber.onNext(SafeSubscriber.java:98)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.subscription.SafeSubscriber.onNext(SafeSubscriber.java:98)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.operators.multi.MultiOperatorProcessor.onItem(MultiOperatorProcessor.java:66)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.lambda$onNext$1(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.onNext(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.operators.multi.MultiOnItemInvoke$MultiOnItemInvokeProcessor.onItem(MultiOnItemInvoke.java:40)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.lambda$onNext$1(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.onNext(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.operators.multi.MultiMapOp$MapProcessor.onItem(MultiMapOp.java:50)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.lambda$onNext$1(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.onNext(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.operators.multi.MultiOperatorProcessor.onItem(MultiOperatorProcessor.java:66)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.lambda$onNext$1(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.onNext(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.operators.multi.MultiOperatorProcessor.onItem(MultiOperatorProcessor.java:66)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.lambda$onNext$1(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.context.SmallRyeThreadContext.lambda$withContext$1(SmallRyeThreadContext.java:530)
	at io.smallrye.mutiny.context.ContextPropagationMultiInterceptor$ContextPropagationSubscriber.onNext(ContextPropagationMultiInterceptor.java:89)
	at io.smallrye.mutiny.vertx.MultiReadStream.lambda$subscribe$2(MultiReadStream.java:76)
	at io.vertx.kafka.client.consumer.impl.KafkaConsumerImpl.lambda$handler$1(KafkaConsumerImpl.java:80)
	at io.vertx.kafka.client.consumer.impl.KafkaReadStreamImpl.run(KafkaReadStreamImpl.java:230)
	at io.vertx.kafka.client.consumer.impl.KafkaReadStreamImpl.lambda$schedule$8(KafkaReadStreamImpl.java:185)
	at io.vertx.core.impl.ContextImpl.executeTask(ContextImpl.java:366)
	at io.vertx.core.impl.EventLoopContext.lambda$executeAsync$0(EventLoopContext.java:38)
	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:164)
	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:472)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:500)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.base/java.lang.Thread.run(Thread.java:834)
	Suppressed: io.smallrye.reactive.messaging.ProcessingException: SRMSG00103: Exception thrown when calling the method com.acme.MovieRetryProcessor#retry
		at io.smallrye.reactive.messaging.ProcessorMediator.handlePostInvocationWithMessage(ProcessorMediator.java:366)
		at io.smallrye.reactive.messaging.ProcessorMediator.lambda$null$38(ProcessorMediator.java:390)
		at io.smallrye.mutiny.operators.UniOnItemOrFailureFlatMap.invokeAndSubstitute(UniOnItemOrFailureFlatMap.java:30)
		... 189 more
	Caused by: java.lang.IllegalStateException: We tried everything in our power to like this movie. Unfortunately we do not want to watch it any more!
		at com.acme.MovieRetryProcessor.retry(MovieRetryProcessor.java:25)
		at com.acme.MovieRetryProcessor_ClientProxy.retry(MovieRetryProcessor_ClientProxy.zig:157)
		at com.acme.MovieRetryProcessor_SmallRyeMessagingInvoker_retry_29f75df771eba1b590eee2e3eadcecd3c5476454.invoke(MovieRetryProcessor_SmallRyeMessagingInvoker_retry_29f75df771eba1b590eee2e3eadcecd3c5476454.zig:48)
		at io.smallrye.reactive.messaging.AbstractMediator.invoke(AbstractMediator.java:94)
		at io.smallrye.reactive.messaging.ProcessorMediator.lambda$null$37(ProcessorMediator.java:388)
		at io.smallrye.mutiny.operators.UniOnItemTransformToUni.invokeAndSubstitute(UniOnItemTransformToUni.java:31)
		... 159 more
	[CIRCULAR REFERENCE:java.lang.IllegalStateException: We tried everything in our power to like this movie. Unfortunately we do not want to watch it any more!]

2021-01-26 10:42:04,666 WARN  [io.sma.rea.mes.kafka] (vert.x-eventloop-thread-0) SRMSG18231: The amount of received messages without acking is too high for topic partition 'TopicPartition{topic=movies-retry, partition=0}', amount 1. The last committed offset was -1. It means that the Kafka connector received Kafka Records that have neither be acked nor nacked in a timely fashion. The connector accumulates records in memory, but that buffer reached its maximum capacity or the deadline for ack/nack expired. The connector cannot commit as a record processing has not completed.
2021-01-26 10:42:04,666 WARN  [io.sma.rea.mes.kafka] (vert.x-eventloop-thread-0) SRMSG18228: A failure has been reported for Kafka topics '[movies-retry]': io.smallrye.reactive.messaging.kafka.commit.KafkaThrottledLatestProcessedCommit$TooManyMessagesWithoutAckException: Too Many Messages without acknowledgement in topic movies-retry (partition:0), last committed offset is -1
```

There's a docker-compose.yaml to get a Kafka broker running.
