package com.cprt.demo.utils.pagination;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;



/**
 * 分页模型，也是所有实体类的基类
 * 
 * @author jifeng
 * 
 */
public class PagerModel<E> implements ClearBean {
	private int total; // 总数
	private List<E> list; // 分页集合列表
	private int pageSize = 12;// 每页显示记录数
	private int offset; // 偏移量
	private int pagerSize;// 总页数
	protected String pagerUrl;//分页标签需要访问的ACTION地址
	private Long id;

	public String getPagerUrl() {
		return pagerUrl;
	}

	public void setPagerUrl(String pagerUrl) {
		this.pagerUrl = pagerUrl;
	}

	public int getPagerSize() {
		return pagerSize;
	}

	public void setPagerSize(int pagerSize) {
		this.pagerSize = pagerSize;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		if(offset >0)
			this.offset = (offset-1)*pageSize;
		else
			this.offset = 0;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<E> getList() {
		return list == null ? new LinkedList<E>() : list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.jeeshop.common.page.ClearBean#clear()
	 */
	@Override
	public void clear() {
		total = 0; // 总数
		list = null; // 分页集合列表
		offset = 0; // 偏移量
		pagerSize = 0;// 总页数
//		pagerUrl = null;//分页标签需要访问的ACTION地址
		
		id = null;
	}
	
	public String trim(String str){
		if(str==null){
			return null;
		}
		return str.trim();
	}
	
	public void clearList(List<String> list){
		if(list==null || list.size()==0){
			return;
		}
		list.clear();
		list = null;
	}

	public void clearSet(Set<String> set){
		if(set==null || set.size()==0){
			return;
		}
		set.clear();
		set = null;
	}
	
	public void clearListBean(List<PagerModel<E>> list){
		if(list==null || list.size()==0){
			return;
		}
		for(int i=0;i<list.size();i++){
			ClearBean item = list.get(i);
			item.clear();
			item = null;
		}
		list.clear();
		list = null;
	}
	
	public void clearArray(String[] arr){
		if(arr==null || arr.length==0){
			return;
		}
		for(int i=0;i<arr.length;i++){
			arr[i] = null;
		}
		arr = null;
	}
	
	
	
	@Override
	public String toString() {
		return "total:"+total+",list:"+list+",offset:"+offset;
	}
}
