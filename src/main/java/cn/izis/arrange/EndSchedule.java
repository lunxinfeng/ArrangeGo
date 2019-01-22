package cn.izis.arrange;

public class EndSchedule {
	private int userid;
	private int user_num;
	private int user_totalscore;
	private int ouserid;
	private int ouser_num;
	private int ouser_totalscore;
	private String name;
	private String oname;

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOname() {
		return oname;
	}

	public void setOname(String oname) {
		this.oname = oname;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getUser_num() {
		return user_num;
	}

	public void setUser_num(int user_num) {
		this.user_num = user_num;
	}

	public int getUser_totalscore() {
		return user_totalscore;
	}

	public void setUser_totalscore(int user_totalscore) {
		this.user_totalscore = user_totalscore;
	}

	public int getOuserid() {
		return ouserid;
	}

	public void setOuserid(int ouserid) {
		this.ouserid = ouserid;
	}

	public int getOuser_num() {
		return ouser_num;
	}

	public void setOuser_num(int ouser_num) {
		this.ouser_num = ouser_num;
	}

	public int getOuser_totalscore() {
		return ouser_totalscore;
	}

	public void setOuser_totalscore(int ouser_totalscore) {
		this.ouser_totalscore = ouser_totalscore;
	}

	public EndSchedule() {
		super();
		// TODO 自动生成的构造函数存根
	}

	public EndSchedule(int userid, int user_num, int user_totalscore,
			int ouserid, int ouser_num, int ouser_totalscore) {
		super();
		this.userid = userid;
		this.user_num = user_num;
		this.user_totalscore = user_totalscore;
		this.ouserid = ouserid;
		this.ouser_num = ouser_num;
		this.ouser_totalscore = ouser_totalscore;
	}

	@Override
	public String toString() {
		return "EndSchedule [userid=" + userid + ", user_num=" + user_num
				+ ", user_totalscore=" + user_totalscore + ", ouserid="
				+ ouserid + ", ouser_num=" + ouser_num + ", ouser_totalscore="
				+ ouser_totalscore + ", name=" + name + ", oname=" + oname
				+ "]";
	}

}
