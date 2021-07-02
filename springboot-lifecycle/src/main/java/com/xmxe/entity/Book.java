package com.xmxe.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Book {
	private int id;
	@NotEmpty(message = "bookname不能为空",groups = group1.class)
    private String bookname;   
    private String bookauthor;
    @Max(value = 10,message = "不能超过10元",groups = {group1.class,group2.class})
    private Float bookprice;

	/**
	 * 嵌套校验
	 */
	@Valid
	private Author author;

	public static class Author {

		@NotNull(groups = {group1.class, group2.class})
		@Length(min = 2, max = 10)
		private String name;

		@NotNull
		@Length(min = 2, max = 10, groups = {group2.class})
		private String position;
	}

	public Book() {}
 
    public Book(String bookname, String bookauthor, Float bookprice) {   
        this.bookname = bookname;   
        this.bookauthor = bookauthor;   
        this.bookprice = bookprice;   
    }


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBookname() {
		return bookname;
	}

	public void setBookname(String bookname) {
		this.bookname = bookname;
	}

	public String getBookauthor() {
		return bookauthor;
	}

	public void setBookauthor(String bookauthor) {
		this.bookauthor = bookauthor;
	}

	public Float getBookprice() {
		return bookprice;
	}

	public void setBookprice(Float bookprice) {
		this.bookprice = bookprice;
	}

	/**
	 * 分组校验
	 * 在实际项目中，可能多个方法需要使用同一个DTO类来接收参数，而不同方法的校验规则很可能是不一样的。
	 * 这个时候，简单地在DTO类的字段上加约束注解无法解决这个问题。因此，spring-validation支持了分组校验的功能，专门用来解决这类问题。
	 */
	public interface group1{}

	public interface group2{}
}
/**
 * Spring Validation最佳实践及其实现原理，参数校验没那么简单！(https://mp.weixin.qq.com/s/jwJtTl5A_TL6jBmwlQNqCQ)
 */