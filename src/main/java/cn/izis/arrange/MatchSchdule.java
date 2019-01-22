package cn.izis.arrange;

public class MatchSchdule {
	private int userid;
	private int ouserid;
	public MatchSchdule() {
		super();
		// TODO 自动生成的构造函数存根
	}
	public MatchSchdule(int userid, int ouserid) {
		super();
		this.userid = userid;
		this.ouserid = ouserid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getOuserid() {
		return ouserid;
	}
	public void setOuserid(int ouserid) {
		this.ouserid = ouserid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ouserid;
		result = prime * result + userid;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatchSchdule other = (MatchSchdule) obj;
		if (ouserid != other.ouserid)
			return false;
		if (userid != other.userid)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "MatchSchdule [userid=" + userid + ", ouserid=" + ouserid + "]";
	}
	
}
