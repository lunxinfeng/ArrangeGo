package cn.izis.arrange;

import java.io.Serializable;

public class UserApplyFrom implements Serializable{
	private int userid;// 用户ID
	private int tag;// 对手ID 0为未编排

	public UserApplyFrom(int userid) {
		super();
		this.userid = userid;

	}
	



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		UserApplyFrom other = (UserApplyFrom) obj;
		if (userid != other.userid)
			return false;
		return true;
	}

	public UserApplyFrom() {
		super();
		// TODO 自动生成的构造函数存根
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "UserApplyFrom [userid=" + userid + ", tag=" + tag + "]";
	}

}
