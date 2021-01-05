package com.xmxe.entity;

import java.util.List;

//分页的页面信息
public class Page<T> {
	private int currentPage;//当前页
	
	private int pageSize=5;//每页显示的记录数
	
	private int pageCount;//总页数
	
	private int total;//总记录数
	
	private int pre;//上一页  
	
	private int next;//下一页
	
	private int start;//每页的起始位置 (currentPage-1)*pageSize
	
	private List<T> rows;//每页的数据

	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return (total+pageSize-1)/pageSize;
	}

	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}

	public int getPre() {
		if(currentPage>1)
			return currentPage-1;
		return 1;
	}
	public int getNext() {
		if(currentPage<getPageCount())
			return currentPage+1;	
		return getPageCount();
	}

	public int getStart() {
		return (currentPage-1)*pageSize;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}	
}
