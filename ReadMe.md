# AutoItself项目

## 项目介绍

AutoItself是类 AutoIt 软件，用于Windows机器执行键盘，鼠标，窗口，注册表等的自动操作

基于JNA技术访问windows api，基于graalVM支持Javascript脚本，基于luaj支持Lua脚本

***(注意：该项目处于实验阶段，并非开箱即用，欢迎一同完善)***

### 平台

相比VBS和C，相信你会更爱Lua和JS的灵活性和Jdk信手拈来的创造力

### 体积

包含jre 11，体积约为20M，无需安装，支持win7 x86

### 三种模式

1.纯后台操作
2.发送消息驱动GUI
3.图像识别，键鼠模拟
（层级越高，可靠性和简单性越低，但使用范围越广）

### 提权

支持无操作穿越UAC提权超管

### 交互

含开发管理端工具包，可在工作现场进行客户端管理，流程纠错，即时编程，结果收集

支持无编程模式


## 支持函数

### 被控制台调用


### 执行CMD命令

控制台对话：√

执行命令并返回 ○

### 窗口操作

查找窗口 √
查找子窗口 √
固定窗口到最前 √
查询窗口 ○
取最前窗口 √
取焦点窗口 √
取激活窗口 √
取标题  √
取内容 √
设置内容 √
关闭窗口 √
弹出消息框√
取得单选框状态 ○
设置单选框状态 ○
取得复选框状态 ○
设置复选框状态 ○

### 键盘

全局：虚拟键 ○ 扫描码 ○ 字符 ○
窗口消息: 字符 √ 虚拟键 ○

### 鼠标

全局：虚拟键  ○ 扫描码 ○
窗口消息： √ 

### 消息
sendMsg ○ postMsg ○

## 注册表编辑

打开注册表键 ○
关闭注册表键 ○
强制刷新注册表 ○

取注册表键 ○
列出所有子键 ○
创建注册表键 ○
删除注册表树 ○

列出所有注册表值 ○
设置注册表值 ○
删除注册表值 ○

备份注册表树 ○


## 安装卸载

7大安装命令 ○
7大卸载命令 ○
取二进制编码自动识别安装包类型与卸载程序类型 ○

## 工具

等待窗口出现 ○
断言 ○
自动断言 ○
日志 ○
通讯 ○

queryWin
封装自动安装卸载

### 输入法管理

清理输入法 √

备份注册表 ○
获取所有已安装输入法 ○
获取所有已安装非内置输入法 ○




