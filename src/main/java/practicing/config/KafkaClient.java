package practicing.config;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import practicing.util.PropertiesUtil;

import java.util.Properties;

/**
 * @author altenchen
 * @create 功能
 */
public class KafkaClient {

    public static Properties getProperties(){
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", PropertiesUtil.getStringValue("kafka.bootstrap.servers"));
        properties.setProperty("group.id", PropertiesUtil.getStringValue("kafka.group.id"));
        return properties;
    }

    public static FlinkKafkaConsumer<String> getKafkaConsumer(ParameterTool params){
        Properties consumerProperties = new Properties();


        consumerProperties.put("enable.auto.commit", "false");
        // 设置接入点，请通过控制台获取对应Topic的接入点
        consumerProperties.put("bootstrap.servers",  "118.31.69.81:9093,116.62.188.184:9093,118.31.12.3:9093");
        // 设置SSL根证书的路径，请记得将XXX修改为自己的路径
        // 与sasl路径类似，该文件也不能被打包到jar中
        consumerProperties.put("ssl.truststore.location", "/usr/local/src/kafka/ssl/aliyun/public/kafka.client.truststore.jks");
//        consumerProperties.put("ssl.truststore.location", "D:/kafka/kafka.client.truststore.jks");
        // 根证书store的密码，保持不变
        consumerProperties.put("ssl.truststore.password", "KafkaOnsClient");
        // 接入协议，目前支持使用SASL_SSL协议接入
        consumerProperties.put("security.protocol", "SASL_SSL");
        // SASL鉴权方式，保持不变
        consumerProperties.put("sasl.mechanism", "PLAIN");
        // 两次poll之间的最大允许间隔

        consumerProperties.setProperty("session.timeout.ms","300000");
        consumerProperties.setProperty("request.timeout.ms","300000");


        consumerProperties.put("max.poll.interval.ms", params.get("max.poll.interval.ms", "1000"));
        consumerProperties.put("fetch.min.bytes", params.get("fetch.min.bytes", "1024"));
        consumerProperties.put("max", params.get("fetch.min.bytes", "1024"));

//        consumerProperties.put("fetch.max.bytes", 786432);
        consumerProperties.put("fetch.max.bytes", params.get("fetch.max.bytes", "60000"));
//        consumerProperties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 60000);
        consumerProperties.put("max.partition.fetch.bytes",  params.get("max.partition.fetch.bytes", "20000"));
        consumerProperties.put("fetch.max.wait.ms",params.get("max.partition.fetch.bytes", "10000"));

        // 属于同一个组的消费实例，会负载消费消息
        consumerProperties.put("group.id", "ow_realtime_recharge_record");
        // hostname校验改成空
        consumerProperties.put("ssl.endpoint.identification.algorithm", "");

        consumerProperties.put("sasl.jaas.config",
                "org.apache.kafka.common.security.scram.ScramLoginModule required username='alikafka_pre-cn-2r42bvd8q02j' password='965pvj2fx7MEzsAwBUPU4knHhER97dyp';");
        //如果用-D或者其它方式设置过，这里不再设置。
//        if (null == System.getProperty("java.security.auth.login.config")) {
            //请注意将XXX修改为自己的路径。
            //这个路径必须是一个文件系统可读的路径，不能被打包到JAR中。
//            System.setProperty("java.security.auth.login.config", "D:/kafka/kafka_client_jaas.conf");
//        System.setProperty("java.security.auth.login.config", "/usr/local/src/kafka/ssl/aliyun/public/kafka_client_jaas.conf");
//        }



        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer(
                params.get("max.partition.fetch.bytes", "flink_4_test"),
                new SimpleStringSchema(),
                consumerProperties);
        
        setConsumingMode(consumer);
    
        return consumer;
    }
    
    private static void setConsumingMode(FlinkKafkaConsumer<String> consumer) {
        if ("true".equals(PropertiesUtil.getStringValue("kafka.enable.consume.from.group.offset"))) {
            //从kafka消费组的offset开始消费
            consumer.setStartFromGroupOffsets();
        } else if ("true".equals(PropertiesUtil.getStringValue("kafka.enable.consume.from.latest.offset"))) {
            //从kafka最新的offset开始消费
            consumer.setStartFromLatest();
        } else if ("true".equals(PropertiesUtil.getStringValue("kafka.enable.consume.from.commit.checkpoint.offset"))) {
            //从kafka提交在checkpoint的offset开始消费
            consumer.setCommitOffsetsOnCheckpoints(true);
        } else {
            consumer.setStartFromLatest();
        }
    }
    
    /**
     * 获取生产者实例
     * @return
     */
    public static FlinkKafkaProducer<String> getKafkaProducer() {
        FlinkKafkaProducer myProducer = new FlinkKafkaProducer<>(PropertiesUtil.getStringValue("kafka.bootstrap.servers"), PropertiesUtil.getStringValue("kafka.source.topic"), new SimpleStringSchema());
        return myProducer;
    }

}
