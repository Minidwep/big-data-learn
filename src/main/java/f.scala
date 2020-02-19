import java.util.Properties


import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.api.scala._

object f {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val props = new Properties();
    //����kafka�ķ����� ���κ�һ̨�Ϳ���
    props.put("bootstrap.servers","192.168.136.51:9092");
    //    ָ��һ�������飬�����Ļ��Ϳ��Ա�֤ͬһ��Ŀͻ��˲�������kafka���������
    //    ��������������⣬һ��д��topic������
    props.put("group.id","dangbook")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val topic = "dangbook"
    val consume = new FlinkKafkaConsumer010[String](topic,new SimpleStringSchema(),props)
    //    �������Դ
    //    val ds = env.addSource[String](consume);
    val ds = env.addSource[String](consume).map(line =>{
      val start = line.indexOf("bookStart")
      val end = line.indexOf("bookEnd")
      if(start > 0 && end >0) {
        val bookInfo = line.substring(start + 9, end);
        //                      println(bookInfo);
        val star = bookInfo.split("//")(2);
        if(star.toInt >=0){
          (star,1)
        }
      }
      //      star(2).toInt
    })
    val att = ds.map(line=>{
      println(line);
    }).keyBy(0).sum(1)




    //    println(result2)




    //    val six = ds.filter(_>60);

    //    val arrayStar = ArrayBuffer[String]();
    //    val array = ArrayBuffer[BookBean]();
    //
    //
    //    ds.map(line=>{
    //      arrayTest.insert(0,"11");
    //      println("TestSize="+arrayTest.size +" data"+line);
    //      val start = line.indexOf("bookStart");
    //      val end = line.indexOf("bookEnd");
    //
    //      if(start > 0 && end >0){
    //        val bookInfo = line.substring(start+9,end);
    //        //                      println(bookInfo);
    //        val bookProperties = bookInfo.split("//");
    //
    //        val book = new BookBean(bookProperties(0),bookProperties(1),bookProperties(2));
    //        array+=book;
    //        arrayStar+=book.bEvaluate
    ////        println("size= "+arrayStar.size)
    ////        println("star=" + arrayStar.last)
    ////        println("sizebook= "+array.size)
    //      }
    //
    //      val starArray = ArrayBuffer[String]();
    //
    //      array.foreach(i =>{
    //        starArray+=i.bEvaluateStar;
    //      })Star;
    //
    ////      var result2=starArray.map(x=>(x,1));
    ////      var result3=result2.groupBy(x=>x._1);
    ////      for((k,v)<- result3){
    ////        println(k+"\t"+v.toBuffer)
    ////      };
    ////      val result4=result3.map(x=>(x._1,x._2.length))
    ////      println(result4);
    //
    //    })

    env.execute("kafka");
  }

}