# 头条资讯
基于SpringBoot的头条资讯类Java Web项目，本项目实现了一个比较完善的信息流类社交网站。 
## 开发工具
* 开发框架：使用Velocity+SpringBoot+MyBatis开发，数据库使用MySQL关系型数据库和Redis非关系型数据库
* 开发工具：IntelliJ IDEA（基于Windows 10平台）
****
## 功能内容
* 用户登录注册管理
* 资讯发布，图片上传（七牛云存储）
* 评论中心，站内信（敏感词过滤）
* Redis实现赞踩功能
* 异步设计和站内信邮件通知系统
****
## 前期准备
* 搭建工程环境
  * 创建git仓库，本地配置IDEA并测试pull和push
  * 创建SpringBoot工程，生成Maven项目，pom.xml导入相关依赖
  * 新建项目目录结构
* AOP和IOC
  * IOC解决对象实例化以及依赖传递问题，解耦
  * AOP解决纵向切面问题，主要实现日志和权限控制功能，使用logger记录日志，用该切面方法来监听Controller
* 数据库设计
  * MySQL创建toutiao数据库，新建用户表(user)，新闻表(news)，评论(comment)，站内信(message)数据表
  * 编写测试类测试数据库CRUD语句
****
## 功能实现
### 登录注册
* 工具类使用：JSON和MD5工具类，通过MD5加密算法将Password和UUID随机5位作为加密密码，保证密码安全
* Ticket校验：用户登录成功时Ticket被生成并存入MySQL数据库，并被设置为Cookie，用于下次登录检测
* 拦截器：拦截所有用户请求，判断请求中是否存在有效Ticket，若存在，将用户信息写入线程ThreadLocal中，并存储在HostHolder实例中，用于全局获取用户信息
* Ajax异步加载数据，JSON数据传输
### 资讯发布
* 图片上传：使用七牛云SDK上传图片，利用CDN内容分发和缩图服务减少网站加载静态文件负载
* Postman工具：测试前端form表单数据提交POST请求是否正确
### 评论与站内信
* 评论：在登录后允许在每条资讯下评论，显示评论数量，具体内容和评论人信息
* 敏感词过滤：使用DFA算法匹配敏感词库实现评论敏感词过滤
* 站内信消息：用户在资讯下评论，建立唯一会话ID，会话中有多条交互消息，通过用户ID获取会话列表，通过会话ID获取具体消息
### 资讯赞踩
* 工具类使用：JedisUtil工具类封装Jedis的增删改查操作
* Jedis使用：每条资讯有Like集合和DisLike集合，通过Key直接查找用户状态（赞或踩），实现添加赞操作或者踩操作，缓解关系型数据库压力
### 异步框架
* 异步设计：
  * 使用Redis实现异步消息阻塞队列，通过EventModel包装一个事件，记录事件实体的相关信息
  * 开发异步工具类，事件生产者EventProducer，事件消费者EventConsumer，事件处理接口EventHandler及具体实现类
  * 流程：事件生产者作为Service,接受Controller产生一个事件，将其放在阻塞队列中；事件消费者使用单线程循环获取队列事件，查找能够处理该事件的Handler并处理（如点赞通知、登录异常通知）
* 邮件发送：引入Mail依赖，使用SMTP协议配置邮箱信息，设计邮箱模板，业务实现发送邮件
****
## 问题及解决
* 使用本地上传图片至服务器遇到图片过多过大，加大服务器加载压力问题<br>
  `解决`：使用七牛云对象存储，创建自己域名，利用云服务SDK和图片压缩技术，大大优化了系统性能
* 邮件发送报错``` javax.mail.AuthenticationFailedException: 535 authentication failed ```<br>
  `解决`：由于使用第三方邮箱登录邮箱，需要使用授权码作为邮箱密码，且需设置SSL为true
****
## 项目后期扩展
* 设置用户个性化界面（个性化推荐）
* 首页资讯列表，结合赞数，评论数和发布时间设计排序算法
* 爬虫自动填充资讯 
* 广告推广

