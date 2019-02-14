# CursorHelper
### 主要功能
* Cursor解析成对象
* 通过输入SQL和相关参数,查找，删除，更新。
###### 配置项目根目录 build.gradle
```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

###### 配置app build.gradle
```
   implementation 'com.github.oobest.CursorHelper:cursorhandler:v1.6'
    annotationProcessor 'com.github.oobest.CursorHelper:db_annotation_compiler:v1.6'
```

###### 注解bean
```
@AptCursorWrapper
public class Customer {

    @Cols("id")
    protected int id;

    @Cols("name")
    protected String name;

    @Cols("address")
    protected String address;

    @Cols("phone")
    protected String phone;



```


###### 解析单个对象Cursor
```
//"SELECT id, name, address, phone From customers WHERE id = ?"
// 注意数据库返回得Cursor中，列字段要与注释中的字符串一致
Customer user = new BeanHandler<>(Customer.class).handle(cursor);

```

###### 解析List Cursor
```
//"SELECT id, name, address, phone From customers"
// 注意数据库返回得Cursor中，列字段要与注释中的字符串一致
Customer user = new BeanListHandler<>(Customer.class).handle(cursor);

```

###### 解析基础数据
```
//"SELECT count(name) From customers WHERE name = ?"
// 只支持Integer,Long,Double,Float,String
Customer user = new new ScalarHandler<>(Long.class).handle(cursor);

```
