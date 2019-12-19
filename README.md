# ScheduleMailTopNews
一、前言：  
本代码片段的主要作用是用jsoup每小时分别读取下面四个站点的top新闻标题并将排名变更：  
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
