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
    //设置kafka的服务器 ，任何一台就可以
    props.put("bootstrap.servers","192.168.136.51:9092")
    //    指定一个消费组，这样的话就可以保证同一组的客户端不会消费kafka新添的数据
    //    消费组的名称随意，一般写上topic的名称
    props.put("group.id","test")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val topic = "test"
    val arrayTest = ArrayBuffer[String]()
    val consume = new FlinkKafkaConsumer010[String](topic,new SimpleStringSchema(),props)
    //    添加数据源
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