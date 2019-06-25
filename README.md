# laniakea-rpc #

`依赖netty与springboot无缝衔接,基于注解开发,支持多种序列化,支持高并发,非阻塞管道,对耗时(i/o)任务独立处理`

## 流程 ##

![Image text](https://github.com/lengleiyuan/laniankea-rpc/blob/master/A05DA856-8BEC-4947-BD39-F0D22FCE2351.png)

![Image text](https://github.com/lengleiyuan/laniankea-rpc/blob/master/4A1756FC-1AC8-4da9-B997-7BC22D7DA7B4.png)


## 环境 ##

`jdk 1.8` 



## 使用教程 ##

**客户端**
```
@SpringBootApplication
@KearpcClient
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
}
```

**服务端**
```
@KearpcServer
@SpringBootApplication
public class DemoApplication {

       public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		
       }
}
```

**发布远程接口**
```
@KearpcService
public class Remoteinterface{

    public int add(int i,int b){
        return i + b;
    }
    
}
```

**本地接口**
```
@KearpcReference(interface="Remoteinterface",protocol="PROTOSTUFFSERIALIZE")
public interface Nativeinterface{

    public int add(int i,int b)
    
}
```


**测试用例**
```
@RunWith(SpringRunner.class)
@SpringBootTest
public class test{

@Autowired
private Nativeinterface interface;

public static void main(String[] args) throws Exception {

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
	
        int parallel = 10000;
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
```


