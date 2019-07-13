package com.snomyc.common.base.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * 类名称：BaseEntity<br>
 * 类描述： 实体类，所有的实体对象都需要继承该基类<br>
 * 重要：使用@MappedSupperClass注解取代原来的@Entity和@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)注解<br>
 * 原因是：@Entity注解会导致所有子类都无法使用二级缓存，而且@MappedSupperClass不需要对应数据库table<br>
 * @version v1.0
 * 
 * /**
 *@Column的作用范围是根据@Id的作用范围来的，即如果@Id标签放在字段上的，
 *那么@Column就只能作用在字段上，如果@Id放在getter方法上的，那么@Column也得放到getter上
 *
 */
@MappedSuperclass
public abstract class BaseEntity extends PersistentEntity {

	private static final long serialVersionUID = 1L;
	
	/*
     * 主键
     */
	@Id
    @GeneratedValue(generator = "uuid") //指定生成器名称
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator") //生成器名称，uuid生成类  
    @Column(length = 36, nullable = false)
    private String id;

    public BaseEntity() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
