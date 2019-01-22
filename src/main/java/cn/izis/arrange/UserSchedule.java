package cn.izis.arrange;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 用户编排实体类，包含编排用到的基本算法。
 * @author apple
 *
 */
public class UserSchedule implements Serializable{
	private List<UserApplyFrom> UAF_list;			// 简略的用户报名表列表，存储用户的userid
	private List<UserApplyFromMessage> UAFM_list;	// 扩展的用户报名表列表，包含了序号、姓名、总分、对战过的对手列表

	public List<UserApplyFrom> getUAF_list() {
		return UAF_list;
	}

	public void setUAF_list(List<UserApplyFrom> uAF_list) {
		UAF_list = uAF_list;
	}

	public List<UserApplyFromMessage> getUAFM_list() {
		return UAFM_list;
	}

	public void setUAFM_list(List<UserApplyFromMessage> uAFM_list) {
		UAFM_list = uAFM_list;
	}

	public UserSchedule() {
		super();
	}

	public UserSchedule(List<UserApplyFrom> uAF_list,
			List<UserApplyFromMessage> uAFM_list) {
		super();
		UAF_list = uAF_list;
		UAFM_list = uAFM_list;
	}

	@Override
	public String toString() {
		return "UserSchedule [UAF_list=" + UAF_list + ", UAFM_list="
				+ UAFM_list + "]";
	}

	public List<EndSchedule> MakeSchedule(boolean acs) {
		List<UserApplyFromMessage> list = new ArrayList<UserApplyFromMessage>();
		List<EndSchedule> list_end = new ArrayList<EndSchedule>();
		List<UserApplyFrom> UAF_list2 = new ArrayList<UserApplyFrom>();  // 临时名单列表
		UAF_list2.addAll(UAF_list);
		for (int i = 0; i < UAFM_list.size(); i++) {
			// 进一步处理，得到用户未对战过的用户列表
			List<UserApplyFromMessage> generateOlist = UAFM_list.get(i).generateOlist(UAF_list2,UAFM_list,acs);
			UAFM_list.get(i).setOlist(generateOlist);
		}
		if (acs) {// 序号升序排列
			ascComparator comp = new ascComparator();
			Collections.sort(UAFM_list, comp);
			for (UserApplyFromMessage userApplyFromMessage : UAFM_list) {
				list.add(userApplyFromMessage);
			}
		} else {// 序号降序排列
			descComparator comp = new descComparator();
			Collections.sort(UAFM_list, comp);
			for (UserApplyFromMessage userApplyFromMessage : UAFM_list) {
				list.add(userApplyFromMessage);
			}
		}
//		for (int i = 0; i < list.size(); i++) {
//			//需要判断i对应的人是否匹配过。if()
//			if(list.get(i).getTag()!=1){
//				for (int j = 1; j < list.size(); j++) {
//					if(hasOpponent(list.get(i).getOlist(),list.get(j).getUserid())&&list.get(j).getTag()!=1){//未匹配过，进行是否是其他人的唯一解
//						boolean hasj=true;
//						for (int k = 0; k < UAF_list.size(); k++) {
//							if(!UAFM_list.get(k).hasOnlyOne(UAF_list, list.get(i).getUserid(), list.get(j).getUserid())){
//								hasj =false;
//								break;
//							}
//						}
//						if(hasj&&list.get(i).getUserid()!=list.get(j).getUserid()){
//							EndSchedule toEndSchedule = ToEndSchedule(list, list.get(i).getUserid(), list.get(j).getUserid());
//							list_end.add(toEndSchedule);
//							list.get(i).setTag(1);
//							list.get(j).setTag(1);
//							break;
//						}
//					}
//				}
//			}
//		}
		if(list_end.size()<UAFM_list.size()/2){
			list_end.clear();
		}
//		for (int j = 0; j < list.size(); j++) {
//			ScheduleRecursion(UAF_list2,UAF_list, list, j);
//		}
//		for (int i = 0; i < UAF_list2.size(); i++) {
//			EndSchedule toEndSchedule = ToEndSchedule(UAFM_list, UAF_list2.get(i).getUserid(), UAF_list2.get(i).getTag());
//			list_end.add(toEndSchedule);
//		}

		return list_end;
	}

	public void ScheduleRecursion(List<UserApplyFrom> SR_list2,List<UserApplyFrom> SR_list,
			List<UserApplyFromMessage> initial_list, int i) {// 通过递归来运算结果，传入没有被编排过的人员(SR_list),排序后的人员(initial_list)
		if (SR_list.size() < 2) {// 递归是否进行
			// 所有人匹配完成
		} else {
			// 从编排的第i个人进行编排
			if (i + 2 < initial_list.size()
					&& hasOpponent(SR_list, initial_list.get(i).getUserid())) {
				if (!hasOpponent(initial_list.get(i).getList(), initial_list.get(i + 1).getUserid())) {// 下一个积分位置的人没有对战过
					// 讲对阵值设置
					setOpponent(SR_list2, initial_list.get(i).getUserid(),
							initial_list.get(i + 1).getUserid());
					UserApplyFromMessage userApplyFromMessage = initial_list.get(i);
					UserApplyFromMessage userApplyFromMessage2 = initial_list.get(i + 1);
					// 匹配到选手将两人移除队列
					initial_list.remove(userApplyFromMessage);
					initial_list.remove(userApplyFromMessage2);
					UserApplyFromRemove(SR_list, userApplyFromMessage.getUserid());
					UserApplyFromRemove(SR_list, userApplyFromMessage2.getUserid());
					ScheduleRecursion(SR_list2,SR_list, initial_list, i + 1);
				} else if (!hasOpponent(initial_list.get(i).getList(),initial_list.get(i + 2).getUserid())) {// 下两个积分位置的人没有对战过
					// 讲对阵值设置
					setOpponent(SR_list2, initial_list.get(i).getUserid(),
							initial_list.get(i + 2).getUserid());
					UserApplyFromMessage userApplyFromMessage = initial_list.get(i);
					UserApplyFromMessage userApplyFromMessage2 = initial_list.get(i + 2);
					// 匹配到选手将两人移除队列
					initial_list.remove(userApplyFromMessage);
					initial_list.remove(userApplyFromMessage2);
					UserApplyFromRemove(SR_list, userApplyFromMessage.getUserid());
					UserApplyFromRemove(SR_list, userApplyFromMessage2.getUserid());
					ScheduleRecursion(SR_list2,SR_list, initial_list, i + 1);
				}
			} else if (i + 1 < initial_list.size()
					&& hasOpponent(SR_list, initial_list.get(i).getUserid())) {
				if (!hasOpponent(initial_list.get(i).getList(), initial_list
						.get(i + 1).getUserid())) {// 下一个积分位置的人没有对战过
					// 讲对阵值设置
					setOpponent(SR_list2, initial_list.get(i).getUserid(),
							initial_list.get(i + 1).getUserid());
					UserApplyFromMessage userApplyFromMessage = initial_list.get(i);
					UserApplyFromMessage userApplyFromMessage2 = initial_list.get(i + 1);
					// 匹配到选手将两人移除队列
					initial_list.remove(userApplyFromMessage);
					initial_list.remove(userApplyFromMessage2);
					UserApplyFromRemove(SR_list, userApplyFromMessage.getUserid());
					UserApplyFromRemove(SR_list, userApplyFromMessage2.getUserid());
					ScheduleRecursion(SR_list2,SR_list, initial_list, i + 1);
				}
			}
		}
	}

	public void setOpponent(List<UserApplyFrom> SR_list, int userid,
			int opoonent) {
		for (int i = 0; i < SR_list.size(); i++) {
			if (SR_list.get(i).getUserid() == userid) {
				SR_list.get(i).setTag(opoonent);
			}
		}
	}

	public boolean hasOpponent(List<UserApplyFrom> SR_list, int userid) {// 是否有匹配过
		boolean flag = false;
		for (int i = 0; i < SR_list.size(); i++) {
			if (SR_list.get(i).getUserid() == userid) {
				flag = true;
			}
		}
		return flag;
	}

	public void UserApplyFromRemove(List<UserApplyFrom> SR_list, int userid) {
		for (int i = 0; i < SR_list.size(); i++) {
			if (SR_list.get(i).getUserid() == userid) {
				SR_list.remove(i);
			}
		}
	}

	public EndSchedule ToEndSchedule(List<UserApplyFromMessage> list,
			int userid, int ouserid) {
		EndSchedule ES = new EndSchedule();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getUserid() == userid) {
				ES.setUser_num(list.get(i).getNum());
				ES.setUserid(userid);
				ES.setUser_totalscore(list.get(i).getTotalscore());
				ES.setName(list.get(i).getName());
			}
			if (list.get(i).getUserid() == ouserid) {
				ES.setOuserid(ouserid);
				ES.setOuser_num(list.get(i).getNum());
				ES.setOuser_totalscore(list.get(i).getTotalscore());
				ES.setOname(list.get(i).getName());
			}

		}
		return ES;
	}
	public List<UserApplyFromMessage> toOlist(int userid){
		List<UserApplyFromMessage> list_return=new ArrayList<UserApplyFromMessage>();
		for (int i = 0; i < UAFM_list.size(); i++) {
			if(userid==UAFM_list.get(i).getUserid()){
				list_return.addAll(UAFM_list.get(i).getOlist());
			}
		}
		return list_return;
	}

	public static List<UserApplyFromMessage> copyUserApplyFromMessage(List<UserApplyFromMessage> list) throws IOException, ClassNotFoundException{
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	    ObjectOutputStream out = new ObjectOutputStream(byteOut);
	    out.writeObject(list);
	    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
	    ObjectInputStream in = new ObjectInputStream(byteIn);
	    List<UserApplyFromMessage> copy_list= (List<UserApplyFromMessage>) in.readObject();
	    if(byteOut!=null){
	    	byteOut.flush();
	    	byteOut.close();
	    }
	    if(out!=null){
	    	out.flush();
	    	out.close();
	    }
	    if(byteIn!=null){
	    	byteIn.close();
	    }
	    if(in!=null){
	    	in.close();
	    }
		return copy_list;
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
