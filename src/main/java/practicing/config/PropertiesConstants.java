package practicing.config;

/**
 * @Auther: Liangliang.Zhang4
 * @Date: 2022/6/20
 * @Description: properties 属性变量池
 * @Version: 1.0
 */
public class PropertiesConstants {
    
    public static String sourceKafkaBootstrapServers = "source.kafka.bootstrap-servers";
    
    public static String sourceKafkaRechargeRecordTopic = "source.kafka.recharge.record.topic";
    
    public static String sourceKafkaRechargeRecordGroupId = "source.kafka.recharge.record.group.id";

    public static String sourceKafkaSslTruststoreLocation = "source.kafka.ssl.truststore.location";

    public static String sourceKafkaJaasConfigLocation = "source.kafka.jaas.config.location";

    public static String sinkKafkaBootstrapServer = "sink.kafka.bootstrap-servers";

    public static String sinkKafkaRechargeRecordTopic = "sink.kafka.recharge.record.topic";

    public static String sinkKafkaSslTruststoreLocation = "sink.kafka.ssl.truststore.location";

    public static String sinkKafkaJaasConfigLocation = "sink.kafka.jaas.config.location";


    public static String sinkRedisHosts = "sink.redis.hosts";
    public static String sinkRedisPort = "sink.redis.port";
    public static String sinkRedisPassword = "sink.redis.password";
    public static String sinkRedisDataBase = "sink.redis.data.base";

    
}
