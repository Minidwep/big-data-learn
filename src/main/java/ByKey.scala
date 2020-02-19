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
    // ����kafka�ķ��������κ�һ̨�Ϳ���
    props.put("bootstrap.servers", "192.168.136.50:9092")
    // ָ��һ�������飬�����Ϳ��Ա�֤ͬһ��Ŀͻ��˲����ظ�����kafka�������ӵ����ݣ���������������⣬һ��д��topic������
    props.put("group.id","dangbook")
    //һ����kafka�д�ŵ����ݶ����ַ�����ʽ�ģ�����ʹ���ַ����������л��ͷ����л�0
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val topic = "dangbook"
    //����flink������
    val consumer = new FlinkKafkaConsumer010[String](topic,new SimpleStringSchema(),props)

    // �������Դ��������Ǵ�kafka�ж�ȡ���ݵ����ݼ���
    val ds = env.addSource[String](consumer)

    val rdd = ds.
      map(line=> {
        val arr = line.split("//")

        (arr(2).replaceAll("bookEnd",""),1)
      }).keyBy(0).sum(1)

    rdd.map(line =>{
      println("���" + line)
      logger.info("line"+line)


      val randomFile = new RandomAccessFile("c:\\test.txt","rw")
      val fileLength = randomFile.length; //�õ��ļ�����

      randomFile.seek(fileLength);//ָ��ָ���ļ�ĩβ

      randomFile.writeBytes(line.toString()+"//");//д������
      randomFile.close();

    })

    env.execute("kafka")


  }
}