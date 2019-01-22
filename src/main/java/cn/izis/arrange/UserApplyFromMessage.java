package cn.izis.arrange;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 用户报名表对象的扩充。
 * @author apple
 *
 */
public class UserApplyFromMessage implements Serializable {
	private int userid;// 用户ID
	private int num;// 编排号码
	private int totalscore;// 积分
	private String name;
	private List<UserApplyFrom> list = new ArrayList<UserApplyFrom>(); // 对战过的对手
	private List<UserApplyFromMessage> olist = new ArrayList<UserApplyFromMessage>(); // 未对战过的对手
	private int tag=0;

	public UserApplyFromMessage() {
		super();
		// TODO 自动生成的构造函数存根
	}

	public UserApplyFromMessage(int userid, int num, int totalscore,
			List<UserApplyFrom> list) {
		super();
		this.userid = userid;
		this.num = num;
		this.totalscore = totalscore;
		this.list = list;
	}

	/**
	 *
	 * @param UAF_list 完全的报名名单列表
	 * @param list 扩展的用户报名表列表
	 * @param acs 升序或降序
	 * @return
	 */
	public List<UserApplyFromMessage>  generateOlist(List<UserApplyFrom> UAF_list,List<UserApplyFromMessage> list,boolean acs){
		 List<UserApplyFrom> list_new = new ArrayList<UserApplyFrom>();
		 List<UserApplyFromMessage> list_o = new ArrayList<UserApplyFromMessage>();
		 list_new.addAll(UAF_list);		//所有比赛人员

		 for (int i = 0; i < UAF_list.size(); i++) {  // 所有人员列表
			for (int j = 0; j < this.list.size(); j++) { // 与当前人对战过的对手列表
				if(UAF_list.get(i).getUserid()==this.list.get(j).getUserid()){
					list_new.remove(this.list.get(j));
				}
			}
		}
		// 经过上述循环， list_new 剔除了当前人对战过的userid，只包含未对战的userid

		 // 遍历所有未对战过的人的userid(排除自己），得到未对战过的对手详情列表（UserApplyFromMessage）列表
		 for (int i = 0; i < list_new.size(); i++) {
			 if(list_new.get(i).getUserid()!=userid){  // 非当前用户
				 for (int j = 0; j < list.size(); j++) {
						if(list_new.get(i).getUserid()==list.get(j).getUserid()){
							 olist.add(list.get(j));
						}
					}
			 }
		}


		 if (acs) {// 序号升序排列
				ascComparator comp = new ascComparator();
				Collections.sort(olist, comp);
				for (UserApplyFromMessage userApplyFromMessage : olist) {
					list_o.add(userApplyFromMessage);
				}
			} else {// 序号降序排列
				descComparator comp = new descComparator();
				Collections.sort(olist, comp);
				for (UserApplyFromMessage userApplyFromMessage : olist) {
					list_o.add(userApplyFromMessage);
				}
			}
		return list_o;
	}

	public boolean hasOnlyOne(List<UserApplyFrom> UAF_List,int userid,int userid2){
		List<String> list_X = new ArrayList<String>();
		List<String> list_i = new ArrayList<String>();
		List<String> list_my = new ArrayList<String>();
		for (int i = 0; i < olist.size(); i++) {
				list_my.add(String.valueOf(olist.get(i).getUserid()));
		}
		for (int i = 0; i < UAF_List.size(); i++) {
			list_i.add(String.valueOf(UAF_List.get(i).getUserid()));
		}

		list_i.retainAll(list_my);
		list_X.addAll(list_i);
		for (int i = 0; i < list_X.size(); i++) {
			if(list_X.get(i).equals(String.valueOf(userid))){
				list_i.remove(list_X.get(i));
			}
			if(list_X.get(i).equals(String.valueOf(userid2))){
				list_i.remove(list_X.get(i));
			}
		}
		list_i.remove(String.valueOf(this.userid));
		if(list_i.size()>1){
			return true;
		}else{
			return false;
		}
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public List<UserApplyFromMessage> getOlist() {
		return olist;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getTotalscore() {
		return totalscore;
	}

	public void setTotalscore(int totalscore) {
		this.totalscore = totalscore;
	}

	public List<UserApplyFrom> getList() {
		return list;
	}

	public void setList(List<UserApplyFrom> list) {
		this.list = list;
	}

	public void setList(UserApplyFrom uaf) {
		this.list.add(uaf);
	}

	public void setOlist(List<UserApplyFromMessage> olist) {
		this.olist = olist;
	}

	@Override
	public String toString() {
		return "UserApplyFromMessage [userid=" + userid + ", num=" + num
				+ ", totalscore=" + totalscore + ", name=" + name + ", list="
				+ list + ", olist=" + olist + ", tag=" + tag + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + num;
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
		UserApplyFromMessage other = (UserApplyFromMessage) obj;
		if (num != other.num)
			return false;
		if (userid != other.userid)
			return false;
		return true;
	}



	class ascComparator implements Comparator<UserApplyFromMessage> {

		@Override
		public int compare(UserApplyFromMessage o1, UserApplyFromMessage o2) {
			// TODO 自动生成的方法存根
			if (o1.getTotalscore() > o2.getTotalscore()) {
				return -1;
			} else if (o1.getTotalscore() == o2.getTotalscore()) {
				if (o1.getNum() > o2.getNum()) {
					return 1;
				} else if (o1.getNum() == o2.getNum()) {
					return 0;
				} else {
					return -1;
				}
			} else {
				return 1;
			}
		}

	}

	class descComparator implements Comparator<UserApplyFromMessage> {

		@Override
		public int compare(UserApplyFromMessage o1, UserApplyFromMessage o2) {
			// TODO 自动生成的方法存根
			if (o1.getTotalscore() > o2.getTotalscore()) {
				return -1;
			} else if (o1.getTotalscore() == o2.getTotalscore()) {
				if (o1.getNum() > o2.getNum()) {
					return -1;
				} else if (o1.getNum() == o2.getNum()) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return 1;
			}
		}

	}

}
