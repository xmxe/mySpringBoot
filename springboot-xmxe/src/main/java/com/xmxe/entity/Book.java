package com.xmxe.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

public class Book {
	private int id;
	@NotEmpty(message = "bookname不能为空")
    private String bookname;   
    private String bookauthor;
    @Max(value = 10,message = "不能超过10元")
    private Float bookprice;   
 
    public Book() {   
    }   
 
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


}
