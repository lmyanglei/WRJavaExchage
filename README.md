WRJavaExchage
=============

##使用java语言，通过powershell，操作Exchange

* 实现了以下功能：
    * 获取powershell版本
    * 获取powershell可用命令
    * 启用邮箱
    * 禁用邮箱
    * 新建邮件组
    * 删除邮件组
    * 添加成员到邮件组
    * 从邮件组移出成员

* ExchangeServer：
    * 未使用powershell直接远程操作exchange
    * 将ExchangeServer部署到Exchange服务器，并以管理员身份运行
    * 客户端调用ExchangeUtil中的程序，执行相应操作
