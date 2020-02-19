import java.io.{File, PrintWriter, RandomAccessFile}
import java.util.Properties

import lombok.extern.slf4j.Slf4j
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.api.scala._
import org.slf4j.{Logger, LoggerFactory}
//import org.apache.spark.{SparkConf, SparkContext}
import redis.clients.jedis.Jedis


object ByKey {
  def main(array: Array[String]): Unit = {

    val logger = LoggerFactory.getLogger(this.getClass)
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val props = new Properties()
    // 设置kafka的服务器，任何一台就可以
    props.put("bootstrap.servers", "192.168.136.50:9092")
    // 指定一个消费组，这样就可以保证同一组的客户端不会重复消费kafka中新增加的数据，消费组的名称任意，一般写上topic的名称
    props.put("group.id","dangbook")
    //一般像kafka中存放的数据都是字符串格式的，所以使用字符串进行序列化和反序列化0
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val topic = "dangbook"
    //创建flink消费者
    val consumer = new FlinkKafkaConsumer010[String](topic,new SimpleStringSchema(),props)

    // 添加数据源，具体就是从kafka中读取数据到数据集中
    val ds = env.addSource[String](consumer)

    val rdd = ds.
      map(line=> {
        val arr = line.split("//")

        (arr(2).replaceAll("bookEnd",""),1)
      }).keyBy(0).sum(1)

    rdd.map(line =>{
      println("结果" + line)
      logger.info("line"+line)


      val randomFile = new RandomAccessFile("c:\\test.txt","rw")
      val fileLength = randomFile.length; //得到文件长度

      randomFile.seek(fileLength);//指针指向文件末尾

      randomFile.writeBytes(line.toString()+"//");//写入数据
      randomFile.close();

    })

    env.execute("kafka")


  }
}