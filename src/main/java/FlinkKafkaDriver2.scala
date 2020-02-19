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
//����kafka�ķ����� ���κ�һ̨�Ϳ���
    props.put("bootstrap.servers","192.168.136.51:9092");
//    ָ��һ�������飬�����Ļ��Ϳ��Ա�֤ͬһ��Ŀͻ��˲�������kafka���������
//    ��������������⣬һ��д��topic������
    props.put("group.id","xpc");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val topic = "xpc";

    val consume = new FlinkKafkaConsumer010[String](topic,new SimpleStringSchema(),props);
//    �������Դ
    val ds = env.addSource[String](consume);
    //�������ݼ��е�ÿһ������ ���Ѹ������ݴ����line������
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
