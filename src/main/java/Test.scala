import java.util.Properties
import java.io.PrintWriter
import scala.collection.mutable.ArrayBuffer
import java.io.File

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.api.scala._

object Test {
  class BookBean(name: String, evaluateNum: String, evaluateStar: String) {
    var bName: String = name
    var bEvaluateNum: String = evaluateNum
    var bEvaluateStar: String = evaluateStar

  }
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val props = new Properties()
    //����kafka�ķ����� ���κ�һ̨�Ϳ���
    props.put("bootstrap.servers","192.168.136.51:9092")
    //    ָ��һ�������飬�����Ļ��Ϳ��Ա�֤ͬһ��Ŀͻ��˲�������kafka���������
    //    ��������������⣬һ��д��topic������
    props.put("group.id","test")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val topic = "test"
    val arrayTest = ArrayBuffer[String]()
    val consume = new FlinkKafkaConsumer010[String](topic,new SimpleStringSchema(),props)
    //    �������Դ
    val ds = env.addSource[String](consume).map(line =>{
//      println(line)
      val star = line.split("//")
      println(star(1))
      star(1).toInt

    })
    val six = ds.filter(_>60);


    println("sixData = "+six)

//    ds.print();



    env.execute("kafka");
  }

}