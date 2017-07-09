# IM即时通讯(带服务器端)

标签（空格分隔）： 开源项目

---

##基于MD风格，全局采用MVP模式，包含后台服务器，纯自定义打造自己的一套消息推送机制

###客户端效果图(持续更新ing)
<br>
![](http://ww1.sinaimg.cn/mw690/006jcGvzly1fgcfacu2bhj30u01hcgrx.jpg)
<br>
<br>
![](http://ww1.sinaimg.cn/mw690/006jcGvzgy1fglkyzu79zj30u01hc17a.jpg)
<br>
![](http://ww1.sinaimg.cn/mw690/006jcGvzgy1fglkzc9qr3j30u01hc7j0.jpg)
<br>
![](http://ww1.sinaimg.cn/mw690/006jcGvzly1fgcfawlqrwj30u01hcjy2.jpg)
<br>
####权限动态申请图片
<br>
![](http://ww1.sinaimg.cn/mw690/006jcGvzly1fgesec8042j30u01hctb4.jpg)
<br>
![](http://ww1.sinaimg.cn/mw690/006jcGvzgy1fglklwcf7nj30u01hcdty.jpg)
<br>
####联系人界面
<br>
![](http://ww1.sinaimg.cn/mw690/006jcGvzgy1fglko8gpe8j30u01hcteb.jpg)
<br>
####搜索联系人界面
<br>
![](http://ww1.sinaimg.cn/mw690/006jcGvzgy1fgxa9kgc37j30f00qo751.jpg)
<br>
####聊天信息界面
<br>
![](http://ww1.sinaimg.cn/mw690/006jcGvzgy1fgxa9trpimj30f00qogms.jpg)
<br>
####会话列表界面
<br>
![](http://ww1.sinaimg.cn/mw690/006jcGvzgy1fh2ybxwz19j30u01hck1a.jpg)
<br>
####个人信息界面
<br>
![](http://ww1.sinaimg.cn/mw690/006jcGvzgy1fh2ycq76fxj30u01hcaou.jpg)
<br>
####更新用户信息界面
<br>
![](http://ww1.sinaimg.cn/mw690/006jcGvzgy1fh2yd53rdtj30u01hcdl9.jpg)
<br>

###服务器端代码(重要的事情说三遍)
* https://github.com/wuyinlei/CNIMStudy
* https://github.com/wuyinlei/CNIMStudy
* https://github.com/wuyinlei/CNIMStudy


###更新内容
* 2017-06-07 更新底部Tab栏和ToolBar
* 2017-06-08 自定义相册选择和第三方图片裁剪
* 2017-06-09 完成图片上传到OSS和6.0以上权限适配问题
* 2017-06-10 服务端注册逻辑接口是登录接口实现、 客户端注册和登录UI实现、客户端注册逻辑实现
* 2017-06-11 完成添加好友接口、完成关注接口、完成查询联系人接口、完成查询某个人的信息接口
* 2017-06-12 完客户端搜索联系人、完成客户端添加关注、完成客户端联系人界面(一半)
* 2017-06-13 测试相关接口
* 2017-06-14 客户端用户详情界面实现、客户端用户联系人界面实现、DiffUtils工具类的了解、使用、封装
* 2017-06-22 聊天后端个推推送集成实现
* 2017-06-23 聊天后端推送接口实现
* 2017-06-24 聊天UI界面实现
* 2017-06-25 聊天功能实现(当前仅支持文字聊天) 已经测试通过
* 2017-06-27 会话列表重复bug修复
* 2017-06-29 更新用户信息逻辑完事,修复bug和添加高斯模糊
* 2017-06-30 群的相关接口的实现(添加群、拉取群信息、拉取成员信息、添加群成员、创建群)
* 2017-07-01 服务器端相关推送逻辑(添加群推送/添加人员推送/创建群推送)以及群的相关接口的测试
* 2017-07-02 实现客户端创建群、搜索群功能，相关接口服务添加
* 2017-07-03 优化客户端体验
* 2017-07-04 群聊天列表会话实现
* 2017-07-05 搜索群功能实现
* 2017-07-07 群聊天功能实现,群成员列表实现

###感谢开源(使用到的开源库)
* [uCrop  图片裁剪开源库][1]   -->[中文介绍][2] -->[uCrop使用及源码浅析][3]  
* [easypermissions][4]  -->[googlesamples之easypermissions使用][5]
* [oss图片缓存存储][6]  --->[使用简介代码][7]
* [网络请求框架Retrofit][8]
* [图片加载框架][9]
* [数据库框架DBFlow][10]
* [Butterknife注解框架][11]

###交流群(项目持续开发中)
* 136471108(群号)
* 136471108(群号)
* 136471108(群号)


  [1]: https://github.com/Yalantis/uCrop
  [2]: http://www.jianshu.com/p/523e77a10321
  [3]: http://wuxiaolong.me/2016/06/20/uCrop/#comments
  [4]: https://github.com/googlesamples/easypermissions
  [5]: http://wuxiaolong.me/2017/03/07/easypermissions/
  [6]: https://intl.aliyun.com/zh/product/oss
  [7]: https://github.com/wuyinlei/CNIm4Android/blob/master/factory/src/main/java/com/mingchu/factory/net/UploadHelper.java
  [8]: https://github.com/square/retrofit
  [9]: https://github.com/bumptech/glide
  [10]: https://github.com/Raizlabs/DBFlow
  [11]: https://github.com/JakeWharton/butterknife
  
  
  
  
  
