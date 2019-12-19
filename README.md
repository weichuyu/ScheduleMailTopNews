# ScheduleMailTopNews
一、前言：  
本代码片段的主要作用是用jsoup每小时分别读取下面四个站点的top新闻标题并将排名变更邮件发出：  
（1）http://top.baidu.com/buzz?b=1&c=513&fr=topbuzz_b341_c513  
（2）https://s.weibo.com/top/summary?cate=realtimehot  
（3）https://news.google.com/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRFZxYUdjU0FtVnVHZ0pWVXlnQVAB?hl=en-US&gl=US&ceid=US%3Aen  
（4）https://news.google.com/topics/CAAqKggKIiRDQkFTRlFvSUwyMHZNRFZxYUdjU0JYcG9MVlJYR2dKVVZ5Z0FQAQ?hl=zh-TW&gl=TW&ceid=TW%3Azh-Hant  
本代码的意义在于：  
（1）有的时候你非常忙，但你仍然想知道外部发生了什么事情。  
（2）看新闻要上各大门户网站，花里胡哨的东西太多，工作期间没那么多空间给你看手机或者看外网(影响不好)。  
（3）很多你感兴趣的新闻可能根本不在头条，而在40-50名左右的位置，手动浏览显然很难关注到它们。  
（4）负面新闻往往不会停留在头条多少时间，你很有可能错过它们。  
（5）有时你希望对同一时间的不同门户网站的新闻进行对比查看，或者作事后分析。  
现在，部署本代码后，以上问题全部解决，你手机只需要装个qq邮箱每小时收邮件看新闻就好了。  

二、使用方法：  
（一）执行src/main/resources/tools.sql 初始化数据库表结构。  
（二）修改src/main/resources/application.properties：  
（1）spring.datasource.url spring.datasource.username spring.datasource.password mysql数据库连接相关  
（2）logging.path 日志路径  
（3）yu.tools.email.sendEmail yu.tools.email.pwd spring.mail.username spring.mail.password  
强烈建议yu.tools.email.sendEmail = spring.mail.username yu.tools.email.pwd=spring.mail.password  
并使用用outlook邮箱。两对字段都设置成一样的含义是：自己发给自己邮件。  
（然后outlook邮箱可能会多次验证你是不是机器人，如果有一天发现邮件不发了，用网页版登录outlook验证下就好了）  
（三）编译打包jar，并找个服务器上传。  
（五）每小时收邮件就好了，第一个小时不会有邮件，第二个小时开始会发这小时和上个小时的对比邮件。

三、效果展示：  
（待补充，图片准备中）
