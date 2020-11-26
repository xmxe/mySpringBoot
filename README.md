## mySpringBoot项目简介

|                           项目名称                           |           介绍            |                             备注                             |
| :----------------------------------------------------------: | :-----------------------: | :----------------------------------------------------------: |
| [springboot-cas-server](https://github.com/xmxe/mySpringBoot/tree/master/springboot-cas-server) |      搭建cas server       | 将自定义的cas_server项目中的pom.xml及src目录替换到官方cas-overlay-template,部署到服务器可完成自定义登录页面，连接数据库校验等功能 |
| [springboot-casclient-one](https://github.com/xmxe/mySpringBoot/tree/master/springboot-casclient-one) |     cas_client子系统      |                                                              |
| [springboot-casclient-two](https://github.com/xmxe/mySpringBoot/tree/master/springboot-casclient-two) |     cas_client子系统      |                                                              |
| [springboot-security-casclient-one](https://github.com/xmxe/mySpringBoot/tree/master/springboot-security-casclient-one) | cas+spring security子系统 | spring security不在作登陆校验，校验在cas server端完成，spring security在登陆成功后根据用户名做权限校验 |
| [springboot-security-casclient-two](https://github.com/xmxe/mySpringBoot/tree/master/springboot-security-casclient-two) | cas+spring security子系统 | spring security不在作登陆校验，校验在cas server端完成，spring security在登陆成功后根据用户名做权限校验 |
| [springboot-task](https://github.com/xmxe/mySpringBoot/tree/master/springboot-task) |    springboot定时任务     |                                                              |
| [springboot-xmxe](https://github.com/xmxe/mySpringBoot/tree/master/springboot-xmxe) |      springboot web       | 主要做集成测试学习的spring boot项目，包括自定义全局异常处理，aop，过滤器（websocket），拦截器，mybatis多数据源，quartz，redis，shiro，swagger，freemarker，spring boot中mvc的配置,服务器跨域配置等 |

---

### spring boot+cas单点登陆

- [cas服务器搭建](https://blog.csdn.net/lhc0512/article/details/82466246)

- [搭建CAS - server服务器](https://blog.csdn.net/oumuv/article/details/83377945)

- [松哥手把手教你入门 Spring Boot + CAS 单点登录](https://mp.weixin.qq.com/s?__biz=MzI1NDY0MTkzNQ==&mid=2247488872&idx=1&sn=3ac483e2e4b58b9940e1aa5458baddd8&chksm=e9c34708deb4ce1eab17c6b9a43d8058558708890a7cfaa053b7effd7f593dd112290d4fed34&scene=158#rd)

- [cas查询数据库验证用户](https://blog.csdn.net/zzy730913/article/details/80825800)

- [Spring Boot 实现单点登录的第三种方案！](https://mp.weixin.qq.com/s?__biz=MzI1NDY0MTkzNQ==&mid=2247488913&idx=1&sn=605b35708ddf3b0e6e32a170cd1aea57&chksm=e9c347f1deb4cee795228ba6eb56c928b826e2ff1356f182b6dce2a14c2c0cb209d0a3936b98&scene=158#rd)

- [cas自定义登录页](<https://blog.csdn.net/yelllowcong/article/details/79236506>)

- [cas server 官方模板下载](https://github.com/apereo/cas-overlay-template)

  > 将springboot-cas-server项目中的 src目录下放到服务器中的cas-overlay-template目录下启动即可完成自定义登录页
  想要实现不同子系统不同登录页主要在service目录下配置name-id.json文件,在name-id.json中配置theme

------
#### spring-boot-starter-thymeleaf 模板引擎

- [Thymeleaf【快速入门】](https://mp.weixin.qq.com/s/aotHeEvGl3usy5BkBBwrFA)

---
#### spring-boot-devtools
 > Spring Boot应用支持热部署，无需手动重启Spring Boot应用,spring-boot-devtools是一个为开发者服务的一个模块，其中最重要的功能就是修改代码后自动启动springboot服务，速度比手动停止后再启动要快，节省出来的并不是手工操作的时间，具体原理主要是使用了两个ClassLoader，一个Classloader加载不会改变的类（第三方Jar包），另一个ClassLoader加载会更改的类，称为  restart ClassLoader,这样在有代码更改的时候，原来的restartClassLoader 被丢弃，重新创建一个restartClassLoader，由于需要加载的类相比较少，所以实现了较快的重启时间

---

#### 相关文章

- [3W 字的 Spring Boot 超详细总结](https://mp.weixin.qq.com/s/ZEFyH0A7GOlDAEuV5M_fFA)

- [2020年排名前20的基于SpringBoot搭建的开源项目](https://mp.weixin.qq.com/s/lvMikTf55xUEf-C-dzmrcg)

- [15 个优秀开源的 Spring Boot 学习项目，一网打尽！](https://mp.weixin.qq.com/s/ZKUr_5VBd7ZD1fkzHbFU_g)

- [Spring Boot 深度调优，6得飞起~](https://mp.weixin.qq.com/s/4DlMT007f8zM6PWPf8y4bQ)

- [Spring Boot 核心知识剖析，写得太好了](https://mp.weixin.qq.com/s/MX2YxMASHfz4dr3a4sgFcw)

- [SpringBoot初始化几大招式，看了终于明白了](https://mp.weixin.qq.com/s/YNFFBuokPHfQxcWTbdVfwQ)

- [SpringBoot 多种读取配置文件中参数的方式](https://mp.weixin.qq.com/s/Zes3ILR4t50lCftX7Rebnw)

- [面试官扎心一问：Tomcat 在 SpringBoot 中是如何启动的？](https://mp.weixin.qq.com/s/Jh0zv6fkxflWY3IgRL9SvQ)

- [手把手带你剖析 Springboot 启动原理！](https://mp.weixin.qq.com/s/78Oso6_yECCk0Rr0tQg-CA)

- [SpringBoot启动原理](https://mp.weixin.qq.com/s/tQQW1Bg-P3B1cIrl1Y_oVA)

- [玩转SpringBoot的启动过程](https://mp.weixin.qq.com/s/qTa6hbOOSFqZ7gV6UnPj4g)

- [为什么SpringBoot的 jar 可以直接运行？](https://mp.weixin.qq.com/s/JoEmiVP1lp9OVO7x1-x4zw)

