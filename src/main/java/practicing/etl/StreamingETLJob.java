package practicing.etl;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import practicing.config.KafkaClient;
import practicing.util.PropertiesUtil;
import practicing.util.TimeUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author altenchen
 * @time 2020/12/17
 * @description 功能
 */
public class StreamingETLJob {
    
    public static void main(String[] args) throws Exception {

        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

//        String restartStrategy = params.get("restartStrategy");
//        if ("fixedDelayRestart".equals(restartStrategy)) {
//            env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
//                    3,
//                    Time.of(60, TimeUnit.SECONDS)
//            ));
//        } else if ("noRestart".equals(restartStrategy)) {
//            env.setRestartStrategy(RestartStrategies.noRestart());
//        } else if ("fallBackRestart".equals(restartStrategy)) {
//            env.setRestartStrategy(RestartStrategies.fallBackRestart());
//        } else {
        env.setRestartStrategy(RestartStrategies.failureRateRestart(
                3,
                Time.of(5, TimeUnit.MINUTES),
                Time.of(60, TimeUnit.SECONDS)
        ));
//        }

//        env.enableCheckpointing(TimeUnit.MINUTES.toMinutes(1));
        
        DataStreamSource<String> stream = env.addSource(KafkaClient.getKafkaConsumer(parameterTool), "kafka-source");

        SingleOutputStreamOperator<String> map = stream.map(new RichMapFunction<String, String>() {
            private transient AtomicLong atomicLong = new AtomicLong(0);
            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                atomicLong = new AtomicLong(0);
            }

            @Override
            public String map(String value) throws Exception {
                if(atomicLong.getAndIncrement() % 1_000L == 0) {
                    System.out.println(TimeUtils.dateToString(new Date()) + ": 消费 数量: " + atomicLong.get());
                }
                return null;
            }
        });
        DataStreamSink<String> stringDataStreamSink = map.filter(new FilterFunction<String>() {
            @Override
            public boolean filter(String value) throws Exception {
                if (value == null) {
                    return false;
                }
                return true;
            }
        }).printToErr("123");
        
        env.execute("StreamingETLJob");
    }
    
    
}
