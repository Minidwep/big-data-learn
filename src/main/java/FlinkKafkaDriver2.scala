import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import redis.clients.jedis.Jedis

object FlinkKafkaDriver2 {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val props = new Properties();
//设置kafka的服务器 ，任何一台就可以
    props.put("bootstrap.servers","192.168.136.51:9092");
//    指定一个消费组，这样的话就可以保证同一组的客户端不会消费kafka新添的数据
//    消费组的名称随意，一般写上topic的名称
    props.put("group.id","xpc");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val topic = "xpc";

    val consume = new FlinkKafkaConsumer010[String](topic,new SimpleStringSchema(),props);
//    添加数据源
    val ds = env.addSource[String](consume);
    //遍历数据集中的每一行数据 并把该行数据存放在line变量中
    ds.map(line=>{
      val jedis = new Jedis("192.168.136.50",6379)
      val keywords = jedis.get("keywords")
      //["aa","bb","cc"]
      val arr = keywords.split(",");
      for(key <- arr){
        if(line.contains(key)){
          print(line);

        }
      }
    })
//    ds.print();
    env.execute("kafka");
  }

}
