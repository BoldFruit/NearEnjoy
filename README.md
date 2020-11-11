近享团队开码大吉  

[toc]
## 2020/4/5

creator: han1254  
email: 1254763408@qq.com

---
**<font color = red>position:</font>**   
setting.gradle  
**<font color = red>content:</font>**
+ 删除了一个多余的database_check
+ 添加了chat和found moudle
```groovy
def project = [

    ······
        // 业务层
        module  : [
                //添加了
                chat    : includeProject(":moudle-chat"),
                //添加了
                found   : includeProject("moudle-found"),
                login   : includeProject(":module-login"),
                splash  : includeProject(":module-splash")
        ],

        ······

]

```
+ 添加ImagePicker依赖

---
**position:**  
base  
|-build.gradle   
**content**
+ 添加ImagePicker依赖

---
## 2020/4/6
creator: han1254  
email: 1254763408@qq.com
### 删除了ImagePicker依赖
**position:**
+ setting.gradle
+ base->build.gradle
+ module-chat->ChatActivity
  
**reason:**
+ 远程库停止更新
+ 作者声明远程库存在错误。只能通过下载代码的形式添加到项目
+ 年代太久远，16年的项目，17年更新了最后一次
### 保留了Matisse
**reason:**
+ 体积小，功能单一
+ github上start数目很高，应该有其他的优势
+ 缺点
    + 是只能选择图片和拍图片，无法裁剪
    + 返回的图片信息是Uri，解析存在困难，需要对不同的手机型号和buildVersion进行适配

### 添加了PictureSelector依赖
https://github.com/LuckSiege/PictureSelector  
*此库的作用十分强大，足以满足本项目的所有选图需求，建议以后使用此库*  
**position:**   
+ setting.gradle
+ base
    + build.gradle
+ module-chat
    + ChatActivity
    + 添加了GlideEngine类


---

## 2020/4/7  
creator: han1254  
email: 1254763408@qq.com  
**position:**
+ network
+ module-chat->ChatViewModel
+ module-chat->ChatModel  

**descripetion:**  
本来是想完成图片文件传输功能，但是写了一点，发现如果用户是多文件传输，由于异步的关系，我并不能知道每个上传成功返回的图片链接是哪个图的，所以要和后端一起制定一个统一的标志。


---
## 2020/4/12
`commitID: b6b9d527c9d2160702c5f3afc8b6689eb02bb038`
+ 聊天功能暂时停止开发
+ 分类页面存在滑动卡顿问题，暂时无法解决
+ 最大可能性与"BoldFruit"的  `3e1bd87dac03d9da483e3913f52462884dda91e5`冲突会出现在MainActivity中，里面替换了“查看好友心愿”部分的点击事件，进入CategoriesHostActivity
+ CategoriesActivity作为测试活动，保留，最终发行时删除即可

---

## 2020/4/17
author: han1254  
email: 1254763408@qq.com  
+ 解决了paging页面初始化时自滚动的问题并且总结成文章  
--->[Android-Paging添加footer和header](https://blog.csdn.net/qq_42898299/article/details/105573338)

---

## 2020/4/18
|  author   | email  | 负责部分 |
|  :----:  | :----:  |:----:|
| han1254 | 1254763408@qq.com |商品详情页|

+ 完成了图片不同数量的格式混排，在调整图片比例时使用了ContraintLayout的调整比例的方式，直接在xml中规定比例。
+ 添加了lottie依赖，用json代码实现动画

---

## 2020/4/19
|  author   | email  | 负责部分 |
|  :----:  | :----:  |:----:|
| han1254 | 1254763408@qq.com |商品详情页|

+ 添加了RXLife依赖（base, network），添加了MvvmRxViewModel，以尝试避免内存泄漏（**<font size = 4>虽然目前为止没有出现过，但是为了避免特殊情况，建议了解一下</font>**）   
[一行代码解决RxJava 内存泄漏，一款轻量级别的RxJava生命周期管理库](https://github.com/liujingxing/rxjava-RxLife)
+ 复制了MyAdapter用来管理标签[<font size = 2>**(->position)**</font>]()，并且稍微改动，添加了unRegister方法，在activity销毁时置空context（感觉没这个必要）
+ TokenManger里添加了获得标签HashMap的方法。
+ lib-common中添加了gallery包，用来预览图片，效果特别棒，同时支持了图片下载的功能，一劳永逸，但是却又引入了PhotoView和RxPermission，鉴于这两个包是以后需要用的，特别是RxPermission，动态请求权限，应该还挺好的（其实是我太菜，反正不大，先引入吧😭）   
[-->仿头条、微信大图预览视图](https://juejin.im/post/5ab8b36251882548fe4a204d#heading-2)
+ 商品详情页初步完成，目前还缺少
    + 点击收藏的接口
    + “大家想要”的接口
    + "I want"的接口
    + 用户联系方式的接口
    + 缺少拉黑举报的接口
    + 缺少增加浏览量的接口？（不确定）
    + 分享，是分享啥？
+ 目前的缺陷
    + **图片预览发现无法预览比例比较小的图，比如9：16这种细长的图，只会显示中间的一部分**
    + **“分享”的dialog缺少点击回调**

## 2020/4/23
|  author   | email  | 负责部分 |
|  :----:  | :----:  |:----:|
| han1254 | 1254763408@qq.com |商品详情页|
发现问题：首页分类内容返回不足  
我的伪造数据地点：
+ 分类页
+ 分类详情页
+ 商品详情页

我的问题：
+ 如何将正在加载变成加载完了# NearEnjoy
