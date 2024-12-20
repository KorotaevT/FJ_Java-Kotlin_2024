package benchmarks.kafka;


import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import util.KafkaUtil;
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

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2g", "-Xmx2g"})
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class SimpleKafkaBenchmark {

    private static final String TOPIC = "topic";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String GROUP_ID = "benchmark-group";

    private KafkaProducer<String, String> producer;
    private KafkaConsumer<String, String> consumer;

    @Setup(Level.Trial)
    public void setup() {
        producer = KafkaUtil.createProducer(BOOTSTRAP_SERVERS);
        consumer = KafkaUtil.createConsumer(BOOTSTRAP_SERVERS, GROUP_ID, TOPIC);
    }

    @Benchmark
    public void sendMessage() {
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "key", "Hello world!");
        producer.send(record);
    }

    @Benchmark
    public void consumeMessage() {
        consumer.poll(Duration.ofMillis(100));
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        producer.close();
        consumer.close();
    }

}