#### 简介
lua本身
   1. 是一个“轻量小巧”的脚本语言，据说是速度最快的脚本语言
   2. 解释器是基于C实现的，可以跟C实现通信

以上就决定了它是非常适用android的脚本语言。
   
在日常的开发中，我们会遇到一些线上问题难以定位，此时我们可以借助动态下发脚本的方式动态执行我们预置的一些方法来获取辅助信息,比如：
   1. sp中的值；
   2. 数据库中的值；
   3. 用户的授权情况
   4. 上传文件
   5. 等等

经过几天的调研，在android中使用的话
   1. 需要一定的学习成本：了解lua语法，了解lua跟c的通信等等。
   2. 接入使用相对复杂
   
基于此，对整体流程做了封装。降低了android开发者在以上场景使用lua的费力度。

#### 使用
1. 引入库

   project的build.gradle中或者settings.gradle(gradle7.0之后)
   ```groovy
   maven { url 'https://www.jitpack.io' }
   ```

   module的build.gradle
   ```groovy
   implementation 'com.github.kbjay.KJLua:lua_annotation:v1.0.1'
   annotationProcessor 'com.github.kbjay.KJLua:lua_apt:v1.0.1'
   implementation 'com.github.kbjay.KJLua:lua_java:v1.0.1'
   ```

2. 预置需要执行的类方法并使用注解标记类：LuaInterfaceForObj可以执行类中非静态方法；LuaInterfaceForClass可以执行类中的静态方法；luaName是在java类在lua脚本中的“标记”,可以用来直接调用java方法。
   ```JAVA
   @LuaInterfaceForObj(luaName = "obj")
   @LuaInterfaceForClass(luaName = "class")
   ```
   eg:
   ```java

   /**
    * 动态修改sp
    */

   @LuaInterfaceForClass(luaName = "sp")
   public class SPLua {
       public static int getInt(String key){
           return LuaApplication.instance.getSharedPreferences("lua", Context.MODE_PRIVATE).getInt(key,0);
       }

       public static void putInt(String key,int value){
           LuaApplication.instance.getSharedPreferences("lua", Context.MODE_PRIVATE).edit().putInt(key,value).apply();
       }
   }
   ```

3. 使用luaName在lua脚本中调用java方法，注意是":"
   ```lua
   -- 调用putInt方法
   sp:putInt("hello_lua",123)
   -- 返回getInt方法
   return sp:getInt("hello_lua")
   ```

4. java执行lua脚本
   ```java
   String execute = LuaExecutor.execute("lua脚本内容");
   ```


具体的使用可以参考app这个module；整体跟java跟h5通信类似！！！

#### 性能影响
1. 包体：会增加200K，基本无影响。
2. 运行时内存：只在后台动态下发脚本的时候会加载so，执行lua，基本可以忽略。
