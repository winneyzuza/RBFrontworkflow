package mt;

import java.util.Comparator;

public class Transaction implements Comparator<Transaction> {
	private String date;
    private int count;
    private int sort;
    private String startDate;
    private String fwdDate;
    private String revDate;
    
	public String getStartDate() {
		return startDate;
	}
	public String getFwdDate() {
		return fwdDate;
	}
	public void setFwdDate(String fwdDate) {
		this.fwdDate = fwdDate;
	}
	public String getRevDate() {
		return revDate;
	}
	public void setRevDate(String revDate) {
		this.revDate = revDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public Transaction() {
	
	}
	public Transaction(String date, int count) {
		super();
		this.date = date;
		this.count = count;
	}
	@Override
	public int compare(Transaction o1, Transaction o2) {
		 return o1.getSort() - o2.getSort();
	}

}
