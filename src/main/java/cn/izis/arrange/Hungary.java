package cn.izis.arrange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Hungary {
	private List<MatchSchdule> matchs=new ArrayList<MatchSchdule>();//匹配集合
//	private Set<MatchSchdule> path=new HashSet<MatchSchdule>();

	//开始从UserSchedule类里面依次编排，x为从开始循环的位置,y回溯对手位置,userid为回溯位置人员;
	int c=0;
	public void Test(UserSchedule US,int x,int y,int userid){
		//从需要编排的List<UserApplyFromMessage>集合从依次取出编排人员进行编排
		for (int i = x; i < US.getUAFM_list().size(); i++) {
			//如果取到的要编排的人员的对手TAG 为0则进行编排
			c++;
			UserApplyFromMessage My_User = US.getUAFM_list().get(i);
			if(My_User.getTag()==0){

				int m=0;
				if(My_User.getUserid()==userid){
					m=y;
				}else{
					m=0;
				}
				//从需要编排的人员的 未对战过的选手集合内取出对手依次匹配
				for (int j = m; j < My_User.getOlist().size(); j++) {
					//如果编排人员已经对上了对手，则跳出循环。进行下一个选手的匹配。
					if(My_User.getTag()!=0){  // 此句话多余。
						break;
					}
					//获取当前选手可对战选手，如果没有对手 ，并且可选选手，者将此选手取出进行匹配
					if(My_User.getOlist().get(j).getTag()==0){
						end(US,My_User, j);
					}
				}
			}
			//当每次回溯一级都失败时，
			if(My_User.getTag()==0&&matchs.size()!=US.getUAFM_list().size()/2&&i>=1&&matchs.size()>0){
				int u=i;
				MatchSchdule matchSchdule2 = new MatchSchdule();
				matchSchdule2.setUserid(matchs.get(matchs.size()-1).getUserid());
				matchSchdule2.setOuserid(matchs.get(matchs.size()-1).getOuserid());

				for (int j = 0; j < US.getUAFM_list().size(); j++) {
					if(US.getUAFM_list().get(j).getUserid()==matchSchdule2.getUserid()){
						u=j;
					}

				}
				//找出回溯位置从哪个位置开始
				for (int j = 0; j < US.getUAFM_list().size(); j++) {
					for (int j2 = 0; j2 < US.getUAFM_list().get(j).getOlist().size(); j2++) {
						if(US.getUAFM_list().get(j).getUserid()==matchSchdule2.getUserid()
								&&US.getUAFM_list().get(j).getOlist().get(j2).getUserid()==matchSchdule2.getOuserid()){
							y=j2;
						}

					}
//					if( US.getUAFM_list().get(i-1).getOlist().get(j).getUserid()==matchSchdule2.getOuserid()){
//						y=j;
//					}
				}

				setTag(matchSchdule2.getUserid(), matchSchdule2.getOuserid(), 0, 0, US);
				matchs.remove(matchSchdule2);
//				System.out.println("TEST运行次数="+c);
//				System.out.println("正确回溯对象"+matchSchdule2.getUserid());
//				System.out.println("回溯对象="+US.getUAFM_list().get(u).getUserid());
//				System.out.println("正确回溯对手"+matchSchdule2.getOuserid());
//				System.out.println("回溯对手="+US.getUAFM_list().get(u).getOlist().get(y).getUserid());
				if(u==0&&y==US.getUAFM_list().get(u).getOlist().size()-1){
					break;
				}
				Test(US,u,y+1,US.getUAFM_list().get(u).getUserid());
			}
		}
	}

	//设置所有数据的匹配选线。进行及时更新
	public void setTag(int userid,int ouserid,int u_tag,int ou_tag,UserSchedule US){
		for (int j = 0; j < US.getUAFM_list().size(); j++) {
			if(US.getUAFM_list().get(j).getUserid()==userid){
				US.getUAFM_list().get(j).setTag(ou_tag);
			}
			if(US.getUAFM_list().get(j).getUserid()==ouserid){
				US.getUAFM_list().get(j).setTag(u_tag);
			}
			for (int j2 = 0; j2 < US.getUAFM_list().get(j).getOlist().size(); j2++) {
				if(US.getUAFM_list().get(j).getOlist().get(j2).getUserid()==userid){
					US.getUAFM_list().get(j).getOlist().get(j2).setTag(ou_tag);
				}
				if(US.getUAFM_list().get(j).getOlist().get(j2).getUserid()==ouserid){
					US.getUAFM_list().get(j).getOlist().get(j2).setTag(u_tag);
				}
			}
		}
	}
	public boolean hasOne(UserSchedule US,UserApplyFromMessage UAFM,int x){//查询是否是唯一解
		boolean has = true;
		int userid = UAFM.getUserid();
		UserApplyFromMessage my_UAFN = UAFM.getOlist().get(x);
		List<UserApplyFromMessage> list = new ArrayList<UserApplyFromMessage>();
		try {
			//将原始数据 深拷贝至新集合中，防止原始数据被修改
			list.addAll(US.copyUserApplyFromMessage(US.getUAFM_list()));
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		//根据所有人员进行剔除，此次被选走的人员。
		for (int i = 0; i < list.size(); i++) {
			//如果此人经过匹配或者为此次匹配的人员，则不进行剔除
			if(list.get(i).getUserid()!=userid&&list.get(i).getTag()==0&&list.get(i).getUserid()!=my_UAFN.getUserid()){
				// 从选中符合条件的人员的对手里面剔除 此次选取的人员
				list.get(i).getOlist().remove(my_UAFN);
				//如果选中的人员不为被选中的选手，同时将主动匹配的人员也需要剔除
				list.get(i).getOlist().remove(UAFM);
				//完成剔除后，检查此人剩余的对手数量是否大于0
				if(list.get(i).getOlist().size()<1){
					return false;
				}
				//查看完成剔除后的人员的剩余对手是都都是已经匹配过的人员。
				if(!listBoolean(list.get(i).getOlist())){
					return false;
				}

			}
		}

		return has;
	}
	//list 为对手集合，
	public boolean listBoolean(List<UserApplyFromMessage> list){
		boolean has = false;
		//如果有一个可以匹配的选手 ，将返回TRUE，
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getTag()==0){
				has=true;
			}

		}
		return has;
	}
	// UAFM为需要匹配的人员，i为从对手集合中第几位选取匹配，
	public void end(UserSchedule US,UserApplyFromMessage UAFM,int i){//UAFM：寻求匹配的对象
		//判断选取的对手，是不是其他选手目前的唯一对手
		if(hasOne(US,UAFM,i)){
			//确保，选手和对手都没有发生改变，可以匹配
			if(hasUser(US, UAFM.getUserid())&&UAFM.getOlist().get(i).getTag()==0){
				MatchSchdule match=new MatchSchdule(UAFM.getUserid(),UAFM.getOlist().get(i).getUserid());
				setTag(UAFM.getUserid(), UAFM.getOlist().get(i).getUserid(), UAFM.getUserid(), UAFM.getOlist().get(i).getUserid(), US);
				//将匹配结果加入集合保存，
				matchs.add(match);
			}
		}else{
			//发现前一次选取的对手会导致后面的选手匹配失败，不选取当前对手，顺位下移一位。
			if(UAFM.getOlist().size()>i+1){
				end(US, UAFM, i+1);
			}
		}
	}
	public boolean hasUser(UserSchedule US,int userid){
		boolean has =false;
		for (int i = 0; i < US.getUAFM_list().size(); i++) {
			if(US.getUAFM_list().get(i).getUserid()==userid){
				if(US.getUAFM_list().get(i).getTag()==0){
					has = true;
				}
			}

		}
		return has;
	}

	public List<EndSchedule> getMatchSchdule(List<UserApplyFromMessage> UAFM){
		List<EndSchedule> list =new ArrayList<EndSchedule>();
		List<EndSchedule> end_list = new ArrayList<EndSchedule>();
		for (int i = 0; i < matchs.size(); i++) {
			EndSchedule ES = new EndSchedule();
			for (int j = 0; j < UAFM.size(); j++) {
				if (matchs.get(i).getUserid() == UAFM.get(j).getUserid()) {
					ES.setUser_num(UAFM.get(i).getNum());
					ES.setUserid(UAFM.get(j).getUserid());
					ES.setUser_totalscore(UAFM.get(j).getTotalscore());
					ES.setName(UAFM.get(j).getName());
				}
				if (matchs.get(i).getOuserid() == UAFM.get(j).getUserid()) {
					ES.setOuser_num(UAFM.get(i).getNum());
					ES.setOuserid(UAFM.get(j).getUserid());
					ES.setOuser_totalscore(UAFM.get(j).getTotalscore());
					ES.setOname(UAFM.get(j).getName());

				}
			}
			list.add(ES);
		}
		ESComparator comp= new ESComparator();
		Collections.sort(list, comp);
		for (EndSchedule ES : list) {
			end_list.add(ES);
		}
		Gson gson = new Gson();
		return end_list;
	}

	class ESComparator implements Comparator<EndSchedule>{

		@Override
		public int compare(EndSchedule o1, EndSchedule o2) {
			// TODO 自动生成的方法存根
			if(o1.getUserid()==9191||o1.getOuserid()==9191){
				return 1;
			}else{
				int x=0;
				int y=0;
				int a=0;
				int b=0;
				if(o1.getUser_totalscore()>o1.getOuser_totalscore()){
					x=o1.getUser_totalscore();
					y=o1.getOuser_totalscore();
				}else{
					x=o1.getOuser_totalscore();
					y=o1.getUser_totalscore();
				}
				if(o2.getUser_totalscore()>o2.getOuser_totalscore()){
					a=o2.getUser_totalscore();
					b=o2.getOuser_totalscore();
				}else{
					a=o2.getOuser_totalscore();
					b=o2.getUser_totalscore();
				}
				if(x>a){
					return -1;
				}else if(x==a){
					if(y>b){
						return -1;
					}else if(y==b){
						return 0;
					}else{
						return 1;
					}
				}else{
					return 1;
				}

			}
		}

	}
}
