package com.wugx_utils.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Wugx
 * @Entity：将我们的java普通类变为一个能够被greenDAO识别的数据库类型的实体类;
 * @nameInDb：在数据库中的名字，如不写则为实体中类名；
 * @Id：选择一个long / Long属性作为实体ID。 在数据库方面，它是主键。 参数autoincrement是设置ID值自增；
 * @NotNull：使该属性在数据库端成为“NOT NULL”列。 通常使用@NotNull标记原始类型（long，int，short，byte）是有意义的；
 * @Transient：表明这个字段不会被写入数据库，只是作为一个普通的java类字段，用来临时存储数据的，不会被持久化。 </p>
 * @date 2018/11/20
 */
@Entity
public class TestBean {
    @Id
    public Long id;
    public String name;
    public int count;
    @Generated(hash = 671426627)
    public TestBean(Long id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }
    @Generated(hash = 2087637710)
    public TestBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }

}
