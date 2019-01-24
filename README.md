# 搭建一个简易的微服务结构
今天我们将搭建一个最简易的微服务结构：serviceA+eureka+serviceB的结构。

## （一）准备工作
在此之前，你需要会一些springboot基础，知道如何搭建springboot结构的项目。
此处为小白同学们提供一个基于搭建springboot+mybatis+oracle项目的学习链接：
https://blog.csdn.net/qq_38050852/article/details/83186799

## （二）结构图

![注册关系图](https://upload-images.jianshu.io/upload_images/14770430-9f5b05c1ad8329c9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
我们将搭建eureka-server端，然后搭建serviceA和serviceB作为eureka的客户端注册到eureka服务端，并完成serviceB调用serviceA。

## （三）搭建eureka-server
该项目基于IDEA搭建。  

1.新建一个工程  
![](https://upload-images.jianshu.io/upload_images/14770430-27b41ca30880155e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
2.起个名字  
![](https://upload-images.jianshu.io/upload_images/14770430-1f0f383e77ee42b4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
3.选版本，选依赖  
![](https://upload-images.jianshu.io/upload_images/14770430-7cde9d424420388f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
4.选项目位置，结束  
![](https://upload-images.jianshu.io/upload_images/14770430-204637875475a121.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
5.打开application.properties进行编写  
![图示](https://upload-images.jianshu.io/upload_images/14770430-a7278c8b409d0849.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
代码如下：  
```
#服务端口号
server.port=8761
#服务名称 (若是集群的服务名称要相同）
spring.application.name=eureka
#eueka注册中心ip
eureka.instance.hostname=127.0.0.1
#是否需要注册到注册中心（如果只有单个eureka，由于自己就是注册中心，是不需要自己注册自己的，所以是false。如果是集群，由于需要相互注册，就需要置为true）
eureka.client.register-with-eureka=false
#是否需要发现服务信息（集群的时候需要相互注册为true，单个服务为false，理由同上）
eureka.client.fetch-registry=false
#注册地址
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
#本地开发时关闭自我保护机制，不可用服务及时剔除
eureka.server.enable-self-preservation=false
#本地开发时关闭自我保护机制，不可用服务及时剔除，间隔2秒钟发现服务不可用即剔除
eureka.server.eviction-interval-timer-in-ms=2000
```

6.启动项加上允许作为eureka服务端的注解@EnableEurekaServer  
![图示](https://upload-images.jianshu.io/upload_images/14770430-409f034774309e6b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

7.运行启动项  
![运行成功](https://upload-images.jianshu.io/upload_images/14770430-eef5c2cd46155fe2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

8.运行成功后可进行页面访问  
![eureka页面](https://upload-images.jianshu.io/upload_images/14770430-39ef6be28c9c6db9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  


**至此，eureka服务端搭建完毕，我们快快再搭几个服务注册进去吧~**

我们总结一下eureka服务端的三个关键点：  
一个是我们在建项目时选的Eureka Server依赖，IDEA自动帮我们生成了pom.xml，当然如果不选我们也可以自己手动加进去，如图  
![eureka-server依赖](https://upload-images.jianshu.io/upload_images/14770430-4d5539fef651c8f1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
再一个是我们的application.properties文件的配置；  
最后一个便是启动项上的@EnableEurekaServer注解。  
掌握这三点，搭建eureka服务端便不在话下。  

***注意事项：关于为何推荐IDEA勾选依赖生成pom文件：为何不手动添加依赖呢？由于springboot还不够稳定，1.5版本跟2.x版本依赖名称区别很大。再加上springboot版本跟springcloud版本一致性问题，依赖的名称多多少少会因为版本问题导致不一致。具体踩坑参见我的有道云笔记：  
http://note.youdao.com/noteshare?id=25494c1f03d25c090010d267f0709df9&sub=A72B88850DB144E8B2F66E477F96C65B  
新人没有必要在版本问题上浪费过多时间，所以个人推荐IDEA勾选生成pom***



## （四）搭建service-a和service-b并注册到注册中心
1.新建工程  
![](https://upload-images.jianshu.io/upload_images/14770430-103174d372de5dad.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
2.项目元数据  
![](https://upload-images.jianshu.io/upload_images/14770430-d6ed055ded7cef95.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
3.选依赖的时候，本期我们用到的是 *web依赖*、 *eureka客户端依赖* 和 *feign远程调用依赖*，其他依赖我们后续再手动加。  
![选依赖](https://upload-images.jianshu.io/upload_images/14770430-2663661d052b010d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

5.配置application.properties  
```
#端口号
server.port=8081
#服务名称
spring.application.name=service-a
##======================eureka=====================##
#是否以ip地址进行注册
eureka.instance.prefer-ip-address=true
#是否注册
eureka.client.register-with-eureka=true
#是否需要从eureka上获取信息
eureka.client.fetch-registry=true
#注册地址
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
#心跳检测与续约时间（本地开发时将时间设置小些，保证服务关闭后注册中心及时剔除服务）
#eureka客户端向服务端发送心跳的时间间隔，单位为秒
eureka.instance.lease-renewal-interval-in-seconds=1
#eureka服务端在收到最后一次心跳之后等待的时间上限，单位为秒，超过则剔除
eureka.instance.lease-expiration-duration-in-seconds=2
##======================eureka=====================##
```
6.启动项加上允许作为eureka客户端的注解@EnableEurekaClient并启动项目  
![启动成功](https://upload-images.jianshu.io/upload_images/14770430-783cf87873c61d62.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
7.打开euraka-server的界面，我们发现，serviceA已经注册到eureka了  
![eureka界面](https://upload-images.jianshu.io/upload_images/14770430-631a8e78d8fb1629.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  


8.我们以同样的步骤创建serviceB并启动，将serviceA和serviceB都注册到eureka服务端。  
![eureka界面](https://upload-images.jianshu.io/upload_images/14770430-51aa8b45295a9f46.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

**至此服务注册我们已搭建完毕~**

我们总结一下eureka客户端的关键点：  
一个是我们在建项目时选的Web依赖和Eureka Client依赖  
```
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
```
feign依赖用于远程调用暂且我们还没用到,只是建项目的时候顺手生成了一下；  
再一个是我们的application.properties文件的配置和启动项上的@EnableEurekaServer注解。  

## （五）基于feign实现service-a和service-b之间的远程调用
0.关于依赖，我们需要引入feign依赖。由于之前我们创建项目的时候勾选了feign依赖，IDEA已自动帮我们生成了feign依赖。没有依赖的小伙伴可以手动添加进去  
```
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
```

1.需求：我们来假设一个场景，我们的serviceA的controller只能输出“张三”，而serviceB需要调用serviceA的“张三”再加上自己本身输出的“正在工作”从而输出一句完成的话“张三正在工作”。
示意图如下：  
![需求示意图](https://upload-images.jianshu.io/upload_images/14770430-b0fd546f22065213.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

2.在serviceA和serviceB项目分别建立controller和feign文件夹  
![](https://upload-images.jianshu.io/upload_images/14770430-ef2f40de4c59163d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
（注意：此处只做简单的controller层代码示范，service层代码及dao层代码不实现）  

3.在A服务创建AController  
![AController.java](https://upload-images.jianshu.io/upload_images/14770430-58abcdb44da49a33.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  


4.在B服务创建BController  
![BController.java](https://upload-images.jianshu.io/upload_images/14770430-8bff6b29132569a6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  


5.在服务B创建专门调用A服务的feign接口，命名为AFeign.java  
![AFeign.java](https://upload-images.jianshu.io/upload_images/14770430-1466dab5b295a909.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
注意接口要与AController.java一致、@FeignClient注解的value要与A服务的注册中心服务名一致。  

6.在BController调用AFeign  
![feign调用](https://upload-images.jianshu.io/upload_images/14770430-9f453bd383783852.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  


7.在需要用feign调用的项目的启动项上加上@EnableFeignClients注解，该示例中由于是B服务需要调用A服务，所以B的启动项上需要加上允许作为Feign客户端的注解@EnableFeignClients  
![B服务的启动项](https://upload-images.jianshu.io/upload_images/14770430-ecee1a3009cca140.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

8.分别启动eureka,service-a,service-b,进行测试  
调用A服务的getWho方法：  
![getWho](https://upload-images.jianshu.io/upload_images/14770430-dcdb5a7ba6387d89.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
调用B服务的getWhatDoing方法：  
![getWhatDoing](https://upload-images.jianshu.io/upload_images/14770430-7e70a1545da80767.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)   

调用B服务的getAll方法：  
![getAll](https://upload-images.jianshu.io/upload_images/14770430-12e48530d1a8554e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

**至此服务调用我们已搭建完毕~**

