---
typora-copy-images-to: ipic
---


#DrawLine 多项式绘图

本程序是一个JAVA上机实验作业。据说现在变成了期末作业。

告诫各位学弟学妹：

虽然本程序是开源的，但是千万不要直接**抄袭源代码**，这样会导致你**挂科**！

本作业

1. 我已经提交过，老师是知道我的代码的。
2. 不止你一个人在参考我的代码。
3. 我额外完成了许多工作。

因此，直接抄走或者只稍作修改为了完成作业，连代码都不看的同学请小心**挂科**。

代码已经一年没碰了，如果有问题欢迎大家提出，也欢迎同样上过这门课的大神来提交Pull request。

如果对于代码有不懂的地方可以直接提交到Github中本项目的Issues列表，我会收到邮件通知，会在第一时间帮你解答。

推荐各位使用 [IntelliJ IDEA](https://www.jetbrains.com/idea/)，放弃学校推荐的 Eclipse / MyEclipse / JBuilder  。本项目是用 Eclipse 开发的，但是使用过IntelliJ才知道编程能有多爽。

顺带一提，jetbrains公司开发的所有IDE跨平台，跨语言，并且给你统一的体验。你可以写Android，IOS，C++，Java，PHP，Python，甚至HTML，CCS，使用相同的界面，相同的快捷键。对于学生是免费的，大家可以使用E江南的edu邮箱注册。



## 炫酷的界面

![](https://ww3.sinaimg.cn/large/006tNc79gy1ffnkoe936fj31c20vowlk.jpg)



![](https://ww4.sinaimg.cn/large/006tNc79gy1ffnkouhyb8j30we0ok0vh.jpg)

![](https://ww1.sinaimg.cn/large/006tNc79gy1ffnkuad6f3j30z917iq88.jpg)

![](https://ww2.sinaimg.cn/large/006tNc79gy1ffnl2tm89bj30pd0f7gmp.jpg)


##老师要求的实验内容

实现函数曲线绘制

1. 菜单选择不同多项式
2. 提供多项式参数输入和线条样式选择界面
3. 鼠标可以拖动标识框
4. 键盘控制坐标轴放大与缩小
5. 其他自拟

## 代码说明

### 界面类

1. MainFrame类，继承自JFrame，程序主窗口，包含了菜单的代码
2. GraphicPanel类，继承自JPanel，绘图类，绘制坐标轴，曲线等。
3. FunctionDialog类，继承自JDialog，提供函数的插入，修改。
4. MainFrameSettings类，继承自JPanel，坐标轴设置。

### 其他类

1. DrawLine 类，程序入口
2. Function类，Serializable接口，N阶函数，存储了每项的系数，可以由x值获得相应的y值。存储了函数的笔刷，颜色。
3. Utils类，工具类，包含高精度运算，正则表达式处理文本，坐标变换等。

##版本更新

V1.2

- 引入界面库[beautyeye](https://github.com/JackJiang2011/beautyeye)
- 修改插入函数界面
- 增加参考线，增加鼠标指示
- 其他bug的修复

----------
V1.1 实验三版本

- 加入菜单栏。
- 使用Serializable接口，新增函数的保存，载入功能。
- 新增函数图像导出功能。
- 新增表达式语义分析功能
- 解除函数数量，阶数和线条样式的限制。
- 支持自动设置分辨率
- 鼠标可以拖动标识框。
- 键盘控制坐标轴放大与缩小。
- 采用事件适配器，减少代码复杂度。
- 使用BoxLayout优化布局逻辑。
- 优化代码逻辑，增加可读性。
- 代码托管至github：[https://github.com/Ethan0w0/DrawLine](https://github.com/Ethan0w0/DrawLine)

----------
V1.0 实验二版本
- 满足实验二的基本需求


##使用的其他开源库

- [beautyeye](https://github.com/JackJiang2011/beautyeye)