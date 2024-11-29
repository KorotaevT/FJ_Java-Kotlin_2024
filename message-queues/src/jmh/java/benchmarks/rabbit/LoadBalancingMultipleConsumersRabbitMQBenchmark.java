package benchmarks.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import util.RabbitMQUtil;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2g", "-Xmx2g"})
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class LoadBalancingMultipleConsumersRabbitMQBenchmark {

    private static final String QUEUE_NAME = "queue";
    private static final String HOST = "localhost";

    private List<Channel> producers;
    private List<Channel> consumers;

    @Setup(Level.Trial)
    public void setup() throws Exception {
        Connection connection = RabbitMQUtil.createConnection(HOST);

        producers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            producers.add(RabbitMQUtil.createProducer(connection, QUEUE_NAME));
        }

        consumers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            consumers.add(RabbitMQUtil.createConsumer(connection, QUEUE_NAME));
        }
    }

    @Benchmark
    public void sendMessage() throws Exception {
        String message = "Hello world!";
        for (Channel producer : producers) {
            producer.basicPublish("", QUEUE_NAME, null, message.getBytes());
        }
    }

    @Benchmark
    public void consumeMessage() throws Exception {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        };

        for (Channel consumer : consumers) {
            consumer.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() throws Exception {
        for (Channel producer : producers) {
            producer.close();
        }

        for (Channel consumer : consumers) {
            consumer.close();
        }
    }

}