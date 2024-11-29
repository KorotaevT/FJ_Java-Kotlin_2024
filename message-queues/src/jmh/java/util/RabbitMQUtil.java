package util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtil {

    public static Connection createConnection(String host) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        return factory.newConnection();
    }

    public static Channel createProducer(Connection connection, String queueName) throws Exception {
        return createConsumer(connection, queueName);
    }

    public static Channel createConsumer(Connection connection, String queueName) throws Exception {
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, false, false, false, null);
        return channel;
    }

}