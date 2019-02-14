# CursorHelper
通过输入SQL和相关参数,查找，删除，更新。主要是查找时，返回对象

** 配置app build.gradle **
```
   implementation 'com.github.oobest.CursorHelper:cursorhandler:v1.2'
    annotationProcessor 'com.github.oobest.CursorHelper:db_annotation_compiler:v1.2'
```

** 注解bean **
```
public class User {

    @Cols("id")
    protected int id;

    @Cols("name")
    protected String name;

    @Cols("password")
    protected String password;

    // get、set方法
}


```


** 解析Cursor **
```
// select id, name, password from tb_user
// 注意数据库返回得Cursor中，列字段要与注释中的字符串一致
User user = new BeanHandler<>(User.class).handle(cursor);

```
