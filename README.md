**laniakea-rpc**
依赖netty,打造注解开发与springboot无缝衔接

**环境**
jdk 1.8 

**使用教程** 

作为客户端

@KearpcClient
@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

作为服务端

@KearpcServer
@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

发布远程接口

@KearpcService
public class Remoteinterface{
    public int add(int i,int b){
        return i + b;
    }
}
接受远程接口

@KearpcReference(interface="Remoteinterface",protocol="PROTOSTUFFSERIALIZE")
public interface Nativeinterface{
    public int add(int i,int b)
}


测试用例

@RunWith(SpringRunner.class)
@SpringBootTest
public class test{

@Autowired
private Nativeinterface interface;


private
public static void main(String[] args) throws Exception {
        final MessageSendExecutor executor = new MessageSendExecutor("127.0.0.1:18888");
        int parallel = 10000;

        class RequestThread implements Runnable{
        
              public RequestThread( CountDownLatch signal, CountDownLatch finish, int taskNumber) {
                    this.signal = signal;
                    this.finish = finish;
                    this.taskNumber = taskNumber;
                }
                
            public void run() {
                try {
                    signal.await();
                    int num = interface.add(taskNumber, taskNumber);
                    System.out.println("result:" + num + "");
                    finish.countDown();
                } catch (InterruptedException ex) {
                }
            }
        }
        
        sw.start();
        long start = System.currentTimeMillis();


        CountDownLatch signal = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(parallel);

        for (int index = 0; index < parallel; index++) {
            RequestThread client = new RequestThread(signal, finish, index);
            new Thread(client).start();
        }
        
        signal.countDown();
        finish.await();
        
        long end = System.currentTimeMillis();

        String tip = String.format("耗时: [%s] 毫秒", end - start);
        System.out.println(tip);
    }
}


