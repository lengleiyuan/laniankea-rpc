# laniakea-rpc #

项目依赖netty,兼容spring。根据简易化理念,简化xml配置,基于注解开发。


## 流程 ##



## 环境 ##

jdk 1.8



## 使用教程 ##

**客户端**


```
@SpringBootApplication
@KearpcClient
public class ClientApplication {

	public static void main(String[] args) {SpringApplication.run(ClientApplication.class, args);}
	
}
```

**服务端**

```
com.laniakea.rpc.address=127.0.0.1:10086
com.laniakea.rpc.protocol=PROTOSTUFFSERIALIZE
```

```
@KearpcServer
@SpringBootApplication
public class ServerApplication {

       public static void main(String[] args) {SpringApplication.run(ServerApplication.class, args);}
       
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
@KearpcReference(address = "127.0.0.1:10086",interface="xxx.xxx.Remoteinterface",protocol="PROTOSTUFFSERIALIZE")
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

	public static void main(String[] args) throws Exception {

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


