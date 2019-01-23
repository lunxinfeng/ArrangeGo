//package cn.izis.arrange;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.sql.PreparedStatement;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.google.gson.Gson;
//
//import cn.izis.dao.MatchInfoDao;
//import cn.izis.dao.MatchsDao;
//import cn.izis.db.DBPool;
//import cn.izis.db.JdbcHelper;
//import cn.izis.entity.Applyform;
//import cn.izis.entity.ChessUser;
//import cn.izis.entity.EndSchedule;
//import cn.izis.entity.GameRoom;
//import cn.izis.entity.GroupChessUser;
//import cn.izis.entity.MakeGrouping;
//import cn.izis.entity.MatchApplyInfo;
//import cn.izis.entity.MatchGameRoom;
//import cn.izis.entity.MatchInfo;
//import cn.izis.entity.MatchRank;
//import cn.izis.entity.MatchStatus;
//import cn.izis.entity.RefreeOptionInfo;
//import cn.izis.entity.TApplyformEntity;
//import cn.izis.entity.TBoardEntity;
//import cn.izis.entity.TMatchEntity;
//import cn.izis.entity.TScheduleEntity;
//import cn.izis.entity.UserComparator;
//import cn.izis.entity.UserEntity;
//import cn.izis.entity.UserSchedule;
//import cn.izis.entity.UserScoreMessage;
//import cn.izis.service.MatchsService;
//import cn.izis.service.UserScoreService;
//import cn.izis.service.impl.MatchsServiceImpl;
//import cn.izis.service.impl.UserScoreServiceImpl;
//import cn.izis.util.Hungary;
//
//public class MatchInfoDaoImpl implements MatchInfoDao {
//
//	@Override
//	public List<MatchInfo> GetMacthList(String info) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		JSONObject jsonresult = new JSONObject(info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//		int pageIndex = -1;
//		int pageSize = -1;
//		int startIndex = -1;
//		int endIndex = -1;
//		int type = 0;
//		int userid = 0;
//		if (JsonObj.has("pageIndex")) {
//			pageIndex = JsonObj.getInt("pageIndex");
//		}
//		if (JsonObj.has("pageSize")) {
//			pageSize = JsonObj.getInt("pageSize");
//		}
//		if (JsonObj.has("type")) {
//			type = JsonObj.getInt("type");
//		}
//		if (JsonObj.has("userid")) {
//			userid = JsonObj.getInt("userid");
//		}
//		if (pageSize != -1 || pageIndex != -1) {
//			startIndex = pageSize * (pageIndex - 1);
//			endIndex = pageSize;
//		}
//		List<MatchInfo> list = new ArrayList<MatchInfo>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			if (type != 2) {
//				sql = "SELECT  a.*,IFNULL(b.`status`,-1) as applystatus from (SELECT * from t_match where 1=1 ";
//				if (type == 0) {
//					sql += " and status!=2";
//				} else if (type == 1) {
//					sql += " and status=2 ";
//				}
//
//				sql += ") a LEFT JOIN t_applyform b on a.id=b.matchid and b.userid="
//						+ userid + "";
//				sql += " GROUP BY a.id";
//			} else if (type == 2) {
//				sql = "SELECT b.*,a.applystatus from (SELECT matchid,`status` as applystatus from t_applyform where userid="
//						+ userid
//						+ " GROUP BY matchid) a LEFT JOIN t_match b on a.matchid=b.id";
//				sql += " UNION SELECT  a.*,IFNULL(b.`status`,-1) as applystatus from (SELECT * from t_match where adduser="
//						+ userid
//						+ ") a LEFT JOIN t_applyform b on a.id=b.matchid and b.userid="
//						+ userid + " GROUP BY a.id ";
//			}
//			if (startIndex != -1 || endIndex != -1) {
//				sql += " limit " + startIndex + "," + endIndex + "";
//			}
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			MatchInfo model = null;
//			while (rs.next()) {
//				model = new MatchInfo();
//				model.setId(rs.getInt("id"));
//				model.setMatchname(rs.getString("matchname"));
//				model.setMatchDate(rs.getDate("match_date"));
//				model.setMatchEndDate(rs.getDate("match_enddate"));
//				model.setApplyendtime(rs.getTimestamp("apply_endtime"));
//				model.setApplystarttime(rs.getTimestamp("apply_starttime"));
//				model.setLevel_lower(rs.getInt("level_lower"));
//				model.setLevel_upper(rs.getInt("level_upper"));
//				model.setStatus(rs.getInt("status"));
//				model.setApplystatus(rs.getInt("applystatus"));
//				model.setMaxRound(GetMaxRound(rs.getInt("id"), 0));
//				// model.setMatchnum(rs.getInt("matchnum"));
//				model.setAdduser(rs.getInt("adduser"));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public List<TScheduleEntity> GetGroupidList(int matchid) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<TScheduleEntity> list = new ArrayList<TScheduleEntity>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT groupid,groupname,refree FROM t_match_group WHERE matchid=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			TScheduleEntity model = null;
//			while (rs.next()) {
//				model = new TScheduleEntity();
//				model.setGroupId(rs.getInt("groupid"));
//				model.setRefreeid(rs.getInt("refree"));
//				model.setGroupname(rs.getString("groupname"));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public MatchStatus GetMatchDetailInfoList(int matchid, int groupid,
//			int type, int round) throws Exception {
//		MatchStatus model = new MatchStatus();
//		// TODO Auto-generated method stub
//
//		List<TScheduleEntity> list = new ArrayList<TScheduleEntity>();
//
//		if (type == 0) {
//			// list = GetMatchUserList(matchid, groupid);
//		} else if (type == 1) {
//			model = GetCounterpartTableList(matchid, groupid, round);
//		} else if (type == 2) {
//			// list = GetScoreTableList(matchid, groupid);
//		}
//		return model;
//	}
//
//	/**
//	 * 比赛成员
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @param groupid
//	 *            分组id
//	 * @return
//	 * @throws Exception
//	 */
//	private List<TScheduleEntity> GetMatchUserList(int matchid, int groupid)
//			throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<TScheduleEntity> list = new ArrayList<TScheduleEntity>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT * from v_schedule where matchid=?  and userid!=9191";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			TScheduleEntity model = null;
//			while (rs.next()) {
//				model = new TScheduleEntity();
//				model.setUserid(rs.getInt("userid"));
//				model.setUsername(rs.getString("username"));
//				model.setPlaycount(rs.getInt("playcount"));
//				model.setLosecount(rs.getInt("losecount"));
//				model.setWincount(rs.getInt("wincount"));
//				model.setMildcount(rs.getInt("mildcount"));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	/**
//	 * 对阵表
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @param groupid
//	 *            分组id
//	 * @param round
//	 *            轮次
//	 * @return
//	 * @throws Exception
//	 */
//	private MatchStatus GetCounterpartTableList(int matchid, int groupid,
//			int round) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int maxround = GetMaxRound(matchid, groupid);
//		if (round == 0) {
//			round = maxround;
//		}
//		MatchStatus model = new MatchStatus();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT (SELECT COUNT(0) from t_applyform where matchid=? and num >0)drawinstatus,(SELECT COUNT(0) from t_schedule where match_id=?)schedulestatus";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, matchid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				model.setMaxround(maxround);
//				model.setDrawinstatus(rs.getInt("drawinstatus"));
//				model.setSchedulestatus(rs.getInt("schedulestatus"));
//				model.setSchedulelist(GetScheduleList(matchid, 0, round));
//				model.setStarttime(GetStartTime(matchid, round));
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return model;
//	}
//
//	private List<TScheduleEntity> GetScheduleList(int matchid, int groupid,
//			int round) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<TScheduleEntity> list = new ArrayList<TScheduleEntity>();
//		if (round == 0) {
//			round = GetMaxRound(matchid, groupid);
//		}
//		try {
//			conn = DBPool.getInstance().getConnection();
//
//			String orderStr = "";
//
//			if (round % 2 == 0) // 偶数轮
//			{
//				orderStr = "order by totalscore-myscore desc, num desc";
//			} else {
//				orderStr = "order by totalscore-myscore desc, num asc";
//			}
//			sql = "SELECT *,case WHEN score is null then 0 else score END as myscore,f_table_code,case WHEN opponentscore is null then 0 else opponentscore END as opponentscore,(totalscore-score) as scoresum,(otherTotalScore-opponentscore)as opponentscoresum  FROM v_schedule_make WHERE matchid = "
//					+ matchid
//					+ "  AND round ="
//					+ round
//					+ "  and userid=f_blackid  " + orderStr;
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			TScheduleEntity model = null;
//			while (rs.next()) {
//				model = new TScheduleEntity();
//				model.setId(rs.getInt("id"));
//				model.setUserid(rs.getInt("userid"));
//				model.setOpponent(rs.getInt("opponent"));
//				model.setScore(rs.getInt("myscore"));
//				model.setNum(rs.getInt("num"));
//				model.setOpponentnum(rs.getInt("bnum"));
//				model.setOpponentscore(rs.getInt("opponentscore"));
//				model.setOpponenttotalscore(rs.getInt("opponentscoresum"));
//				model.setUsername(rs.getString("realname"));
//				model.setOpponentname(rs.getString("othersidename"));
//				model.setGameid(rs.getInt("gameid"));
//				model.setRefreeid(rs.getInt("referee"));
//				model.setRound(rs.getInt("round"));
//				model.setTotalscore(rs.getInt("scoresum"));
//				model.setBlackorganization(rs.getString("blackorganization"));
//				model.setWhiteorganization(rs.getString("whiteorganization"));
//				model.setMark(String.valueOf(rs.getInt("myscore")) + "-"
//						+ String.valueOf(rs.getInt("opponentscore")));
//				model.setMatch_state(rs.getInt("f_match_state"));
//				model.setTableNum(rs.getInt("f_table_code"));
//				// model.setStarttime(GetStartTime(matchid, round));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	/**
//	 * 成绩表
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @param groupid
//	 *            分组id
//	 * @return
//	 * @throws Exception
//	 */
//	private List<TScheduleEntity> GetScoreTableList(int matchid, int groupid)
//			throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int opponentscore = 0;
//		List<TScheduleEntity> list = new ArrayList<TScheduleEntity>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			// sql =
//			// "SELECT a.*,b.opponent,b.totalscore,b.round from(SELECT matchid,groupid,userid,realname as username,Num from t_applyform where userid!=9191 and matchid="
//			// + matchid
//			// + " and groupid="
//			// + groupid
//			// +
//			// ") a LEFT JOIN (SELECT match_id,group_id,userid,opponent,totalscore,round FROM t_schedule where match_id="
//			// + matchid
//			// + " and group_id="
//			// + groupid
//			// +
//			// ") b on  a.userid=b.userid   WHERE b.round=(SELECT MAX(round) from t_schedule where match_id="
//			// + matchid
//			// + " and group_id="
//			// + groupid
//			// + ") ORDER BY b.totalscore DESC";
//			sql = "SELECT a.userid,a.realname as username,b.totalscore,b.opponentscore,b.num from  t_applyform  a LEFT JOIN v_schedule_make b on a.matchid=b.matchid  and a.userid=b.userid WHERE a.matchid="
//					+ matchid
//					+ "  and a.userid!=9191 and b.round=(SELECT MAX(round) from t_schedule where match_id="
//					+ matchid + ")";
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			TScheduleEntity model = null;
//			while (rs.next()) {
//				model = new TScheduleEntity();
//				model.setUserid(rs.getInt("userid"));
//				model.setNum(rs.getInt("num"));
//				model.setUsername(rs.getString("username"));
//				model.setTotalscore(rs.getInt("totalscore"));
//				// opponentscore = GetOpponentScore(matchid, groupid,
//				// rs.getInt("opponent"));
//				model.setOpponentscore(rs.getInt("opponentscore"));
//				model.setProgressscore(rs.getInt("totalscore"));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	/**
//	 * 获取对手总积分
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @param groupid
//	 *            分组id
//	 * @param opponent
//	 *            对手id
//	 * @return
//	 * @throws Exception
//	 */
//	private int GetOpponentScore(int matchid, int groupid, int opponent)
//			throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int opponentscore = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT totalscore FROM t_schedule where match_id="
//					+ matchid
//					+ " and userid="
//					+ opponent
//					+ " and round =(SELECT MAX(round) from t_schedule where match_id="
//					+ matchid + ")";
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				opponentscore = rs.getInt(1);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return opponentscore;
//	}
//
//	@Override
//	public int MatchApplyForm(TApplyformEntity model) throws Exception {
//		// TODO Auto-generated method stub
//		if (IfExistApply(model.getMatchid(), model.getUserid()) > 0) {
//			return UpdateMatchApply(model.getMatchid(), model.getUserid());
//		}
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		if (model.getLevel() == null || model.getLevel().equals("")) {
//			model.setLevel("30级");
//		}
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "insert into t_applyform(uuid,userid,matchid,realname,level,tel,batch,addtime,organization) values(?,?,?,?,?,?,?,now(),?)";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, java.util.UUID.randomUUID().toString());
//			pstmt.setInt(2, model.getUserid());
//			pstmt.setInt(3, model.getMatchid());
//			pstmt.setString(4, model.getRealname());
//			pstmt.setString(5, model.getLevel());
//			pstmt.setString(6, model.getTel());
//			pstmt.setInt(7, model.getBatch());
//			pstmt.setString(8, model.getOrganization());
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	/**
//	 * 检测是否报名过
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @param userid
//	 *            用户id
//	 * @return
//	 * @throws Exception
//	 */
//	private int IfExistApply(int matchid, int userid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "select uuid from t_applyform where matchid=? and userid=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, userid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				res = 1;
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	/**
//	 * 修改报名状态
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @param userid
//	 *            用户id
//	 * @return
//	 * @throws Exception
//	 */
//	private int UpdateMatchApply(int matchid, int userid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "update t_applyform  set status=1 where matchid=? and userid=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, userid);
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	// private int GetUserLevel(String level)throws Exception{
//	// Connection conn = null;
//	// PreparedStatement pstmt = null;
//	// ResultSet rs = null;
//	// String sql = "";
//	// int res=0;
//	// try {
//	// conn=DBPool.getInstance().getConnection();
//	// sql="select id from t_mygo_level where level=?";
//	// pstmt=conn.prepareStatement(sql);
//	// pstmt.setString(1, level);
//	// rs=pstmt.executeQuery();
//	// if(rs.next()){
//	// res=rs.getInt(1);
//	// }
//	// } catch (Exception e) {
//	// // TODO: handle exception
//	// e.printStackTrace();
//	// }finally{
//	// DBPool.close(conn, pstmt, rs);
//	// }
//	// return res;
//	// }
//	@Override
//	public List<MatchApplyInfo> GetMatchApplyInfoList(int userid)
//			throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<MatchApplyInfo> list = new ArrayList<MatchApplyInfo>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT a.*,b.matchname,b.match_date,b.batch,c.center_name from (SELECT uuid,matchid,`status`,realname from t_applyform where userid=?)a LEFT JOIN t_match b on a.matchid=b.id LEFT JOIN t_test_center c on b.test_center=c.id";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, userid);
//			rs = pstmt.executeQuery();
//			MatchApplyInfo model = null;
//			while (rs.next()) {
//				model = new MatchApplyInfo();
//				model.setMatchname(rs.getString("matchname"));
//				model.setStatus(rs.getInt("status"));
//				model.setUsername(rs.getString("realname"));
//				model.setMatchdate(rs.getDate("match_date"));
//				model.setTestcenter(rs.getString("center_name"));
//				model.setBatch(rs.getInt("batch"));
//				model.setUuid(rs.getString("uuid"));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public List<TMatchEntity> GetMatchList(int id) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<TMatchEntity> list = new ArrayList<TMatchEntity>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT id,matchname,match_date,apply_endtime,batch from t_match where test_center=? and TO_DAYS(apply_endtime)-TO_DAYS(now())>=0";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			rs = pstmt.executeQuery();
//			TMatchEntity model = null;
//			while (rs.next()) {
//				model = new TMatchEntity();
//				model.setId(rs.getInt("id"));
//				model.setMatchname(rs.getString("matchname"));
//				model.setMatchDate(rs.getDate("match_date"));
//				model.setApplyEndtime(rs.getDate("apply_endtime"));
//				model.setBatch(rs.getInt("batch"));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public int CancelApply(int matchid, int userid, int groupid)
//			throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "update  t_applyform set status=-1 where matchid=? and userid=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, userid);
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public String MakeSchedule(String info) throws Exception {
//		// TODO Auto-generated method stub
//		JSONObject jsonresult = new JSONObject(info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//		int round = JsonObj.getInt("round");
//		int matchid = JsonObj.getInt("matchid");
//		int adduserid = JsonObj.getInt("userid");
//		String sql = "select * from t_applyform where matchid=" + matchid
//				+ " and status=1 and num>0 order by num asc";
//		List list1 = JdbcHelper.query(sql);
//		// 第一步：状态检测。看当前赛事报名情况和抽签编号情况
//		// ==============总人数================
//
//		if (list1.size() % 2 != 0) {
//			list1.remove(list1.size() - 1);
//		}
//		int UserCount = list1.size();
//		if (UserCount == 0 || UserCount % 2 != 0) {
//			return "false"; //
//		}
//		String sel2 = "select * from t_match where id= " + matchid
//				+ " order by addtime desc";
//		List list2 = JdbcHelper.query(sel2);
//		if (list2.size() <= 0)
//			return "false";
//		String insertStr1 = "";
//		String insertStr2 = "";
//		String insertStr3 = "";
//		int stype = 1;
//		String blackuser = "";
//		String whiteuser = "";
//		Map fmap1 = (Map) list1.get(0);
//		String f_gamename = fmap1.get("matchname").toString();
//
//		String f_roomname = "";
//		Map fmap2 = (Map) list2.get(0);
//		String f_roomnum = fmap2.get("boardsize").toString(); // 盘面大小（9路或者19路，或者其他）
//		String f_ruler = fmap2.get("ruler").toString();
//		String[] split1 = f_ruler.split("#");
//		String basetime = "0";
//		if (split1.length >= 3) {
//			basetime = String.valueOf(Integer.parseInt(split1[1]) * 60 * 1000);
//		}
//		List<String> SQLStringList = new ArrayList<String>();
//		if (f_roomnum != null && !f_roomnum.equals("")) {
//			String[] split = f_ruler.split("#");
//			switch (Integer.parseInt(f_roomnum)) {
//			case 19:
//				if (split.length >= 5 && split[1].equals("0")
//						&& split[2].equals("0") && split[3].equals("0")
//						&& split[4].equals("0")) {
//					stype = 1; // 不限时
//				} else {
//					stype = 99; // 限时的友谊赛
//				}
//				break;
//			case 13:
//				stype = 2; // 13路不限时，已取消
//				break;
//			case 9:
//				stype = 4; // 9路不限时
//				break;
//			}
//		}
//		// 对局开始状态，不计时的，建好对局即可立即开始下棋，反正不限时。限时对局需满足一定条件才开始。
//		int match_state = 2;
//		if (stype == 5 || stype == 99 || stype == 98) {
//			match_state = 0;
//		}
//		if (round == 0) {
//			// 第一轮简单处理，需找规律
//
//			// step 1 : 获取积分段，初始就一个积分段。
//			for (int i = 0; i < UserCount; i++) {
//				if (SQLStringList != null && SQLStringList.size() > 0)
//					SQLStringList.clear();
//				Map fmapnew1 = (Map) list1.get(i);
//				blackuser = fmapnew1.get("userid").toString();
//
//				Map fmapnew2 = (Map) list1.get(i + 1);
//				whiteuser = fmapnew2.get("userid").toString();
//				f_roomname = fmapnew1.get("realname").toString() + "-"
//						+ fmapnew2.get("realname").toString();
//
//				// 产生两条对局记录信息
//				insertStr1 = "insert into schedule(matchid,round,userid,opponent,adduser,addtime) values("
//						+ matchid
//						+ ","
//						+ String.valueOf(round + 1)
//						+ ","
//						+ blackuser
//						+ ",'"
//						+ whiteuser
//						+ "',"
//						+ adduserid
//						+ ",CURRENT_TIMESTAMP)";
//				insertStr2 = "insert into schedule(matchid,round,userid,opponent,adduser,addtime) values("
//						+ matchid
//						+ ","
//						+ String.valueOf(round + 1)
//						+ ","
//						+ whiteuser
//						+ ",'"
//						+ blackuser
//						+ "',"
//						+ adduserid
//						+ ",CURRENT_TIMESTAMP)";
//
//				// 产生一条对局记录表
//				insertStr3 = "insert into t_gameroom(f_gamename,f_roomname,f_roomnum,f_blackid,f_whiteid,f_ruler,f_roomtype,f_blackleft,f_whiteleft,f_add_user,f_matchid,f_round,f_match_state) values('"
//						+ f_gamename
//						+ "','"
//						+ f_roomname
//						+ "',"
//						+ f_roomnum
//						+ ","
//						+ blackuser
//						+ ","
//						+ whiteuser
//						+ ",'"
//						+ f_ruler
//						+ "','"
//						+ stype
//						+ "','"
//						+ basetime
//						+ "','"
//						+ basetime
//						+ "','"
//						+ adduserid
//						+ "',"
//						+ matchid
//						+ ","
//						+ String.valueOf(round + 1) + "," + match_state + ")";
//
//				SQLStringList.add(insertStr1);
//				SQLStringList.add(insertStr2);
//				SQLStringList.add(insertStr3);
//
//				JdbcHelper.batchUpdate(SQLStringList);
//
//				i++;
//			}
//
//			// 加载最新一轮比赛的编排信息
//			// string strLast =
//			// "select a.*,b.num,b.realname,c.realname as othersidename from schedule a left join applyform b on a.matchid=b.matchid and a.userid=b.userid left join applyform c on a.matchid=c.matchid and a.opponent=c.userid where a.matchid="
//			// + matchid + " and a.round=" + (round + 1).ToString() +
//			// " order by b.num asc";
//			Gson gson = new Gson();
//			String json = "";
//			String strLast = "select * from v_schedule where userid=f_blackid and matchid="
//					+ matchid
//					+ " and round="
//					+ String.valueOf(round + 1)
//					+ " order by num asc";
//			List info11 = JdbcHelper.query(strLast);
//			// System.out.println(info);
//			return getJsonMakeSchedule(info11);
//		} else {
//			// 原则一：比过的人不能再比，已经比赛过的人不能再编排配对。
//			// 原则二：首先使用积分段充分配对，积分段人数为单时，临近配对。
//			// 原则三：先后手尽量平衡。先手多的执白。
//			// 得到按积分从高到低排序的记录集
//			boolean acs = true;
//
//			if ((round + 1) % 2 == 0) {
//				acs = false;
//			} else {
//				acs = true;
//			}
//			MatchsDao MD = new MatchsDaoImpl();
//			UserSchedule US = MD.GetListUserSchedule(matchid);
//			US.MakeSchedule(acs);
//			Hungary hungary = new Hungary();
//			hungary.Test(US, 0, 0, 0);
//			List<EndSchedule> list_ES = new ArrayList<EndSchedule>();
//			list_ES = hungary.getMatchSchdule(US.getUAFM_list());
//			if (list_ES.size() == 0
//					|| list_ES.size() != US.getUAFM_list().size() / 2) {
//				return "false_end";
//			}
//
//			for (int i = 0; i < list_ES.size(); i++) {
//				String userid = String.valueOf(list_ES.get(i).getUserid());
//				int snum = list_ES.get(i).getUser_num();
//				String srealname = list_ES.get(i).getName();
//				String stotalscore = String.valueOf(list_ES.get(i)
//						.getUser_totalscore());
//				String otherid = String.valueOf(list_ES.get(i).getOuserid());// 对手ID
//				int onum = list_ES.get(i).getOuser_num();
//				String orealname = list_ES.get(i).getOname();
//				String ototalscore = String.valueOf(list_ES.get(i)
//						.getOuser_totalscore());
//				// region 第三步：更新数据库，创建新一轮对局和编排表
//
//				if (SQLStringList != null && SQLStringList.size() > 0)
//					SQLStringList.clear();
//
//				if (sblackorwhite == "+") {
//					blackuser = userid;
//					whiteuser = otherid;
//					f_roomname = srealname + "-" + orealname;
//				} else {
//					blackuser = otherid;
//					whiteuser = userid;
//					f_roomname = orealname + "-" + srealname;
//				}
//
//				// 产生两条对局记录信息
//				insertStr1 = "insert into schedule(matchid,round,userid,opponent,adduser,addtime,totalscore) values("
//						+ matchid
//						+ ","
//						+ String.valueOf(round + 1)
//						+ ","
//						+ userid
//						+ ",'"
//						+ otherid
//						+ "',"
//						+ adduserid
//						+ ",CURRENT_TIMESTAMP," + stotalscore + ")";
//				insertStr2 = "insert into schedule(matchid,round,userid,opponent,adduser,addtime,totalscore) values("
//						+ matchid
//						+ ","
//						+ String.valueOf(round + 1)
//						+ ","
//						+ otherid
//						+ ",'"
//						+ userid
//						+ "',"
//						+ userid
//						+ ",CURRENT_TIMESTAMP," + ototalscore + ")";
//
//				// 产生一条对局记录表
//				insertStr3 = "insert into t_gameroom(f_gamename,f_roomname,f_roomnum,f_blackid,f_whiteid,f_ruler,f_roomtype,f_blackleft,f_whiteleft,f_add_user,f_matchid,f_round,f_match_state) values('"
//						+ f_gamename
//						+ "','"
//						+ f_roomname
//						+ "',"
//						+ f_roomnum
//						+ ","
//						+ blackuser
//						+ ","
//						+ whiteuser
//						+ ",'"
//						+ f_ruler
//						+ "','"
//						+ stype
//						+ "','"
//						+ basetime
//						+ "','"
//						+ basetime
//						+ "','"
//						+ adduserid
//						+ "',"
//						+ matchid
//						+ ","
//						+ String.valueOf(round + 1) + "," + match_state + ")";
//
//				SQLStringList.add(insertStr1);
//				SQLStringList.add(insertStr2);
//				SQLStringList.add(insertStr3);
//				JdbcHelper.batchUpdate(SQLStringList);
//			}
//			String orderStr = "";
//			if ((round + 1) % 2 == 0) // 偶数轮
//			{
//				orderStr = "order by totalscore desc, num desc";
//			} else {
//				orderStr = "order by totalscore desc, num asc";
//			}
//			// 加载最新一轮比赛的编排信息
//			String strLastest = "select * from v_schedule where userid=f_blackid and matchid="
//					+ matchid
//					+ " and round="
//					+ String.valueOf(round + 1)
//					+ " "
//					+ orderStr;
//			List result = JdbcHelper.query(strLastest);
//			return getJsonMakeSchedule(result);
//		}
//	}
//
//	private String getJsonMakeSchedule(List list) throws Exception {
//
//		String json = "";
//		List<TScheduleEntity> schList = new ArrayList<TScheduleEntity>();
//		for (int i = 0; i < list.size(); i++) {
//			TScheduleEntity schedule = new TScheduleEntity();
//			Map mp = (Map) list.get(i);
//			schedule.setId(Integer.parseInt(mp.get("Id").toString()));
//			schedule.setNum(Integer.parseInt(mp.get("num").toString()));
//			schedule.setUsername(mp.get("realname").toString());
//			schedule.setOpponentname(mp.get("othersidename").toString());
//			String totalscore = mp.get("totalscore").toString();
//			if (totalscore == null || totalscore.length() == 0) {
//				schedule.setTotalscore(null);
//			} else {
//				schedule.setTotalscore(Integer.parseInt(totalscore));
//			}
//			String score = mp.get("score").toString();
//			if (score == null || score.length() == 0) {
//				schedule.setScore(null);
//			} else {
//				schedule.setScore(Integer.parseInt(score));
//			}
//			String round = mp.get("round").toString();
//			if (round == null || round.length() == 0) {
//				schedule.setRound(null);
//			} else {
//				schedule.setRound(Integer.parseInt(round));
//			}
//			String opponent = mp.get("opponent").toString();
//			if (opponent == null || opponent.length() == 0) {
//				schedule.setOpponent(null);
//			} else {
//				schedule.setOpponent(Integer.parseInt(opponent));
//			}
//			String iresult = mp.get("iresult").toString();
//			if (iresult == null || iresult.length() == 0) {
//				schedule.setIresult(null);
//			} else {
//				schedule.setIresult(Integer.parseInt(iresult));
//			}
//			schedule.setUserid(Integer.parseInt(mp.get("userid").toString()));
//			String bnum = mp.get("bnum").toString();
//			// if (bnum == null || bnum.length() == 0) {
//			// schedule.setBnum(null);
//			// } else {
//			// schedule.setBnum(Integer.parseInt(bnum));
//			// }
//			schList.add(schedule);
//		}
//		Gson gson = new Gson();
//		TScheduleEntity s = new TScheduleEntity();
//		for (int i = 0; i < schList.size(); i++) {
//			if (schList.get(i).getUserid() == 9191
//					|| schList.get(i).getOpponent() == 9191) {
//				s = schList.get(i);
//				schList.remove(i);
//			}
//
//		}
//		if (schList != null
//				&& ((s.getUserid() != null && s.getUserid() == 9191) || (s
//						.getOpponent() != null && s.getOpponent() == 9191))) {
//			schList.add(s);
//		}
//		json = gson.toJson(schList);
//		return json;
//
//	}
//
//	@Override
//	public String GetDrawDate(String info) throws Exception {
//		JSONObject jsonresult = new JSONObject(info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//		// int round=JsonObj.getInt("round");
//		int matchid = JsonObj.getInt("matchid");
//		int groupid = 0;
//		if (IfExistNum(matchid, groupid, 1) > 0) {
//			return "false";
//		}
//		int rowCount = IfExistNum(matchid, groupid, 0);
//		if (rowCount > 0) {
//			// int[] code = BuildRandomSequence(1, rowCount);
//			boolean flag = true;
//			List<String> sign_InInitialize = Sign_InInitialize(matchid, groupid);
//			for (int i = 0; i < sign_InInitialize.size(); i++) {
//				int iret = JdbcHelper.update(sign_InInitialize.get(i));
//				if (iret > 0) {
//					// 继续即可。
//				} else {
//					flag = false;
//					String update1 = " update t_applyform set num=0 where matchid="
//							+ matchid + "";
//					JdbcHelper.update(update1);
//					// 出差错则回滚
//					break;
//				}
//			}
//			if (flag) {
//				String CreateGameStr = "insert into t_applyform(userid,realname,matchid,addtime,status,num) values(9191"
//						+ ",'轮空',"
//						+ matchid
//						+ ",CURRENT_TIMESTAMP,1,"
//						+ (rowCount + 1) + ")";
//				Object obj2 = JdbcHelper
//						.insertWithReturnPrimeKey(CreateGameStr);
//				// 重新获取一遍信息，根据num排序
//				String strSel2 = "select * from t_applyform where matchid="
//						+ matchid + " and status=1 order by num asc";
//				List list2 = JdbcHelper.query(strSel2);
//				if (list2.size() % 2 != 0) {
//					list2.remove(list2.size() - 1);
//				}
//				return getJsonInitializeSchedule(list2);
//			} else {
//				return "false";
//			}
//		} else {
//			return "false";
//		}
//	}
//
//	/**
//	 * 一键抽签，同时给赛事内的多个组进行抽签。
//	 *
//	 * @param matchid
//	 * @return
//	 */
//	@SuppressWarnings("resource")
//	@Override
//	public String DrawInMatch(int matchid) {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		// 第一步：校验数据.（1）如果已有抽签信息，则返回false.
//		sql = "SELECT b.num as b_num from"
//				+ "(SELECT COUNT(*) as num FROM t_applyform WHERE matchid=? and status=1 and  Num>0) b ";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				// 签到者序号排在前面，未签到者预留在后面
//				int b_num = rs.getInt("b_num");
//
//				if (b_num > 0)
//					return "false";
//
//				// 第二步：获取分组数目
//				// sql = "SELECT groupid from t_applyform WHERE matchid="
//				// + matchid + " DESC limit 0,1";
//				// pstmt = conn.prepareStatement(sql);
//				// rs = pstmt.executeQuery();
//
//				// if (rs.next()) {
//				// int groupNum = rs.getInt(1); // 分组数目
//
//				// 第三步：每组内进行分别抽签
//				// for (int i = 0; i < groupNum; i++) {
//				DrawInGroup(matchid, 0);
//				// }
//				try {
//					UpdateMatchStatus(matchid);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return "true";
//				// }
//
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//		return "false";
//
//	}
//
//	private void UpdateMatchStatus(int matchid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "update t_match set status=1 where id=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//	}
//
//	/**
//	 * 组内抽签
//	 *
//	 * @param matchid
//	 * @param groupid
//	 * @return
//	 */
//	@SuppressWarnings("resource")
//	private boolean DrawInGroup(int matchid, int groupid) {
//
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		Random rd = new Random();
//		List<String> sqlList = new ArrayList<String>();
//		List<Integer> Slist = new ArrayList<Integer>();
//		List<Integer> Slist_user = new ArrayList<Integer>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT COUNT(*) as 'num' FROM t_applyform WHERE matchid = "
//					+ matchid + " and  status=1 ";
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//
//				int total_num = rs.getInt("num");
//				// 得到总个数为total_num 的随机序列[1，total_num]
//				while (Slist.size() < total_num) {
//					int num = rd.nextInt(total_num) + 1;
//					if (!Slist.contains(num)) {
//						Slist.add(num);
//					}
//				}
//
//				pstmt = conn
//						.prepareStatement("SELECT userid from t_applyform WHERE  matchid = "
//								+ matchid + " and status=1");
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					Slist_user.add(rs.getInt("userid"));
//				}
//
//			}
//			if (Slist.size() == Slist_user.size()) {
//				for (int i = 0; i < Slist.size(); i++) {
//					String strUpdate = " update t_applyform set num= "
//							+ Slist.get(i) + " where matchid=" + matchid
//							+ " and userid=" + Slist_user.get(i);
//					sqlList.add(strUpdate);
//				}
//			}
//			int rowCount = Slist_user.size();
//			int[] iResArray = JdbcHelper.batchUpdate(sqlList);
//			int totalNum = 0; // 批量操作成功影响的
//			for (int i = 0; i < iResArray.length; i++) {
//				totalNum += iResArray[i];
//			}
//
//			if (totalNum == rowCount) {
//				String CreateGameStr = "insert into t_applyform(uuid,userid,realname,matchid,addtime,status,num) values(UUID(),9191"
//						+ ",'轮空',"
//						+ matchid
//						+ ",CURRENT_TIMESTAMP,1,"
//						+ (rowCount + 1) + ")";
//				Object obj2 = JdbcHelper
//						.insertWithReturnPrimeKey(CreateGameStr);
//				// 重新获取一遍信息，根据num排序
//				String strSel2 = "select * from t_applyform where matchid="
//						+ matchid + " and status=1 order by num asc";
//				List list2 = JdbcHelper.query(strSel2);
//				if (list2.size() % 2 != 0) {
//					list2.remove(list2.size() - 1);
//				}
//				return true;
//			} else {
//				return false;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//		return true;
//	}
//
//	private Timestamp GetStartTime(int matchid, int round) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		Timestamp ts = null;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT roundtime from t_match_round where matchid=? and round=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, round + 1);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				ts = rs.getTimestamp(1);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return ts;
//	}
//
//	/**
//	 * 对局编排
//	 *
//	 * @param matchid
//	 * @param groupid
//	 * @param round
//	 * @param adduserid
//	 * @return
//	 * @throws Exception
//	 */
//	@Override
//	public String MakeSchedule(int matchid, int groupid, int round,
//			int adduserid) throws Exception {
//		Timestamp ts = GetStartTime(matchid, round);
//		if (ts == null) {
//			return "false1";
//		}
//		String sql = "select * from v_userapply where matchid=" + matchid
//				+ " and status=1 and num>0 order by num asc";
//		List list1 = JdbcHelper.query(sql);
//		// 第一步：状态检测。看当前赛事报名情况和抽签编号情况
//
//		// ==============总人数================
//		if (list1.size() % 2 != 0) {
//			list1.remove(list1.size() - 1); // 最后一人为系统添加的轮空，在抽签阶段固定增加了一个轮空用户。
//		}
//		int UserCount = list1.size();
//		if (UserCount == 0 || UserCount % 2 != 0) {
//			return "false"; //
//		}
//		String sel2 = "select * from t_match where id= " + matchid
//				+ " order by addtime desc";
//		List list2 = JdbcHelper.query(sel2);
//		if (list2.size() <= 0)
//			return "false";
//		String insertStr1 = "";
//		String insertStr2 = "";
//		String insertStr3 = "";
//		int stype = 1;
//		String blackuser = "";
//		String whiteuser = "";
//		String f_allstep = "";
//		String f_positon = "";
//		int f_num = 0;
//		Map fmap1 = (Map) list1.get(0);
//		String f_gamename = fmap1.get("matchname").toString();
//
//		String f_roomname = "";
//		Map fmap2 = (Map) list2.get(0);
//		String f_roomnum = fmap2.get("board_size").toString(); // 盘面大小（9路或者19路，或者其他）
//		String f_ruler = fmap2.get("match_ruler").toString();
//		String[] split1 = f_ruler.split("#");
//		String basetime = "0";
//		if (split1.length >= 3) {
//			basetime = String.valueOf(Integer.parseInt(split1[3]) * 60 * 1000);
//		}
//		List<String> SQLStringList = new ArrayList<String>();
//		if (f_roomnum != null && !f_roomnum.equals("")) {
//			String[] split = f_ruler.split("#");
//			stype = Integer.parseInt(split[7]);
//			if (stype == 21) {
//				f_allstep = "+0505-0506+0606-0605";
//				f_positon = "-0605";
//				f_num = 4;
//
//			} else if (stype == 22 || stype == 23) {
//				f_allstep = "+0707-0708+0808-0807";
//				f_positon = "-0807";
//				f_num = 4;
//			}
//			// switch (Integer.parseInt(split[7])) {
//			// case 19:
//			// // if (split.length >= 9 && split[1].equals("0")
//			// // && split[2].equals("0") && split[3].equals("0")
//			// // && split[4].equals("0")) {
//			// // stype = 1; // 不限时
//			// // } else {
//			// stype = 99; // 限时的友谊赛
//			// // }
//			// break;
//			// case 13:
//			// stype = 2; // 13路不限时，已取消
//			// break;
//			// case 9:
//			// stype = 21; // 9路不限时
//			// break;
//			// }
//		}
//		// 对局开始状态，不计时的，建好对局即可立即开始下棋，反正不限时。限时对局需满足一定条件才开始。
//		int match_state = 0;
//		// if (stype == 5 || stype == 99 || stype == 98) {
//		// match_state = 0;
//		// }
//		if (round == 0) {
//			// 第一轮简单处理，需找规律
//
//			// step 1 : 获取积分段，初始就一个积分段。
//			for (int i = 0; i < UserCount; i++) {
//				if (SQLStringList != null && SQLStringList.size() > 0)
//					SQLStringList.clear();
//				Map fmapnew1 = (Map) list1.get(i);
//				blackuser = fmapnew1.get("userid").toString();
//
//				Map fmapnew2 = (Map) list1.get(i + 1);
//				whiteuser = fmapnew2.get("userid").toString();
//				f_roomname = fmapnew1.get("realname").toString() + "-"
//						+ fmapnew2.get("realname").toString();
//
//				// 产生两条对局记录信息
//				insertStr1 = "insert into t_schedule(match_id,round,userid,opponent,adduser,addtime) values("
//						+ matchid
//						+ ","
//						+ String.valueOf(round + 1)
//						+ ","
//						+ blackuser
//						+ ",'"
//						+ whiteuser
//						+ "',"
//						+ adduserid
//						+ ",CURRENT_TIMESTAMP)";
//				insertStr2 = "insert into t_schedule(match_id,round,userid,opponent,adduser,addtime) values("
//						+ matchid
//						+ ","
//						+ String.valueOf(round + 1)
//						+ ","
//						+ whiteuser
//						+ ",'"
//						+ blackuser
//						+ "',"
//						+ adduserid
//						+ ",CURRENT_TIMESTAMP)";
//
//				// 产生一条对局记录表
//				insertStr3 = "insert into t_gameroom(f_gamename,f_roomname,f_roomnum,f_blackid,f_whiteid,f_ruler,f_roomtype,f_blackleft,f_whiteleft,f_add_user,f_matchid,f_round,f_match_state,f_table_code,f_starttime,f_num,f_position,f_allstep) values('"
//						+ f_gamename
//						+ "','"
//						+ f_roomname
//						+ "',"
//						+ f_roomnum
//						+ ","
//						+ blackuser
//						+ ","
//						+ whiteuser
//						+ ",'"
//						+ f_ruler
//						+ "','"
//						+ stype
//						+ "','"
//						+ basetime
//						+ "','"
//						+ basetime
//						+ "','"
//						+ adduserid
//						+ "',"
//						+ matchid
//						+ ","
//						+ String.valueOf(round + 1)
//						+ ","
//						+ match_state
//						+ ","
//						+ (i / 2 + 1)
//						+ ",'"
//						+ ts
//						+ "',"
//						+ f_num
//						+ ",'"
//						+ f_positon + "','" + f_allstep + "')";
//
//				SQLStringList.add(insertStr1);
//				SQLStringList.add(insertStr2);
//				SQLStringList.add(insertStr3);
//
//				JdbcHelper.batchUpdate(SQLStringList);
//
//				i++; // 一次循环，处理两个人。
//			}
//
//			// 加载最新一轮比赛的编排信息
//			// string strLast =
//			// "select a.*,b.num,b.realname,c.realname as othersidename from schedule a left join applyform b on a.matchid=b.matchid and a.userid=b.userid left join applyform c on a.matchid=c.matchid and a.opponent=c.userid where a.matchid="
//			// + matchid + " and a.round=" + (round + 1).ToString() +
//			// " order by b.num asc";
//			// Gson gson = new Gson();
//			// String json = "";
//			// String strLast =
//			// "select * from v_schedule_make where userid=f_blackid and matchid="
//			// + matchid
//			// + " and groupid="
//			// + groupid
//			// + " and round="
//			// + String.valueOf(round + 1) + " order by num asc";
//			// List firstSchedule = JdbcHelper.query(strLast);
//			// return getJsonMakeSchedule(firstSchedule);
//			return "true";
//		} else {
//			// 原则一：比过的人不能再比，已经比赛过的人不能再编排配对。
//			// 原则二：首先使用积分段充分配对，积分段人数为单时，临近配对。
//			// 原则三：先后手尽量平衡。先手多的执白。
//			// 得到按积分从高到低排序的记录集
//			boolean acs = true;
//
//			if ((round + 1) % 2 == 0) {
//				acs = false;
//			} else {
//				acs = true;
//			}
//			MatchsDaoImpl MD = new MatchsDaoImpl();
//			// 获取本赛事本组下的基本报名编排信息。
//			UserSchedule US = MD.GetListUserSchedule(matchid, groupid);
//			// 对待处理的用户列表进行排序，按照总分、序号（按轮次奇偶性交替先后）
//			US.MakeSchedule(acs);
//			// 匈牙利算法，
//			Hungary hungary = new Hungary();
//			hungary.Test(US, 0, 0, 0);
//
//			List<EndSchedule> list_ES = new ArrayList<EndSchedule>();
//			list_ES = hungary.getMatchSchdule(US.getUAFM_list());
//			if (list_ES.size() == 0
//					|| list_ES.size() != US.getUAFM_list().size() / 2) {
//				return "false_end"; // 无法完成编排匹配，积分循环编排终止。
//			}
//
//			for (int i = 0; i < list_ES.size(); i++) {
//				String userid = String.valueOf(list_ES.get(i).getUserid());
//				int snum = list_ES.get(i).getUser_num();
//				String srealname = list_ES.get(i).getName();
//				String stotalscore = String.valueOf(list_ES.get(i)
//						.getUser_totalscore());
//				String otherid = String.valueOf(list_ES.get(i).getOuserid());// 对手ID
//				int onum = list_ES.get(i).getOuser_num();
//				String orealname = list_ES.get(i).getOname();
//				String ototalscore = String.valueOf(list_ES.get(i)
//						.getOuser_totalscore());
//
//				// region 第二步：先后手的确定
//				// 第二步：先后手的确定。在尽可能变换参赛者上一轮先后手的条件下，同时尽可能地充分平衡参赛者的先后手。
//				// 先后手次数相同时，单轮次小号执黑先行，双轮次大号执黑先行。先后手不相同时，先手多的一方执白后走，先手少的一方执黑先行。
//				// 需准备值：上一轮双方的执黑白情况，双方执黑各自总次数
//				String fstr1 = "select count(1) from t_gameroom where f_blackid="
//						+ userid + " and f_matchid=" + matchid; // 自己执黑总次数
//				String fstr2 = "select count(1) from t_gameroom where f_blackid="
//						+ otherid + " and f_matchid=" + matchid; // 对手执黑总次数
//				String fstr3 = "select Id from t_gameroom where f_blackid="
//						+ userid + " and f_matchid=" + matchid
//						+ " and f_round=" + String.valueOf(round); // 当前轮次执黑情况，有则表示执黑，否则执白
//				String fstr4 = "select Id from t_gameroom where f_blackid="
//						+ otherid + " and f_matchid=" + matchid
//						+ " and f_round=" + String.valueOf(round); // 当前轮次执黑情况，有则表示执黑，否则执白
//				List list3 = JdbcHelper.query(fstr3);
//				List list4 = JdbcHelper.query(fstr4);
//
//				String sblackorwhite = "";
//				String oblackorwhite = "";
//
//				if (list3.size() > 0) // 表示上一轮userid执黑。
//				{
//					sblackorwhite = "-";
//				} else {
//					sblackorwhite = "+";
//				}
//
//				if (list4.size() > 0) // 表示上一轮otherid执黑。
//				{
//					oblackorwhite = "-";
//				} else {
//					oblackorwhite = "+";
//				}
//
//				// 如果能够相容，则不需要后续判断，直接以此先手次序为准
//				if ((sblackorwhite == "+" && oblackorwhite == "-")
//						|| (sblackorwhite == "-" && oblackorwhite == "+")) {
//
//				} else {
//					Object obj11 = JdbcHelper.getSingle(fstr1);
//					Object obj22 = JdbcHelper.getSingle(fstr2);
//
//					int sFirstCount = Integer.parseInt(obj11.toString()); // userid总执黑次数
//					int oFirstCount = Integer.parseInt(obj22.toString()); // otherid总执黑次数
//
//					if (sFirstCount == oFirstCount) {
//						if (((round + 1) % 2 == 0) && (snum > onum)) // 偶数轮，大号先行
//						{
//							sblackorwhite = "+";
//							oblackorwhite = "-";
//						} else {
//							sblackorwhite = "-";
//							oblackorwhite = "+";
//						}
//					} else {
//						if (sFirstCount > oFirstCount) {
//							sblackorwhite = "+";
//							oblackorwhite = "-";
//						} else {
//							sblackorwhite = "-";
//							oblackorwhite = "+";
//						}
//					}
//
//				}
//
//				// endregion
//
//				// region 第三步：更新数据库，创建新一轮对局和编排表
//
//				if (SQLStringList != null && SQLStringList.size() > 0)
//					SQLStringList.clear();
//
//				if (sblackorwhite == "+") {
//					blackuser = userid;
//					whiteuser = otherid;
//					f_roomname = srealname + "-" + orealname;
//				} else {
//					blackuser = otherid;
//					whiteuser = userid;
//					f_roomname = orealname + "-" + srealname;
//				}
//
//				// 产生两条对局记录信息
//				insertStr1 = "insert into t_schedule(match_id,round,userid,opponent,adduser,addtime,totalscore) values("
//						+ matchid
//						+ ","
//						+ String.valueOf(round + 1)
//						+ ","
//						+ userid
//						+ ",'"
//						+ otherid
//						+ "',"
//						+ adduserid
//						+ ",CURRENT_TIMESTAMP," + stotalscore + ")";
//				insertStr2 = "insert into t_schedule(match_id,round,userid,opponent,adduser,addtime,totalscore) values("
//						+ matchid
//						+ ","
//						+ String.valueOf(round + 1)
//						+ ","
//						+ otherid
//						+ ",'"
//						+ userid
//						+ "',"
//						+ userid
//						+ ",CURRENT_TIMESTAMP," + ototalscore + ")";
//
//				// 产生一条对局记录表
//				insertStr3 = "insert into t_gameroom(f_gamename,f_roomname,f_roomnum,f_blackid,f_whiteid,f_ruler,f_roomtype,f_blackleft,f_whiteleft,f_add_user,f_matchid,f_round,f_match_state,f_table_code,f_starttime,f_num,f_position,f_allstep) values('"
//						+ f_gamename
//						+ "','"
//						+ f_roomname
//						+ "',"
//						+ f_roomnum
//						+ ","
//						+ blackuser
//						+ ","
//						+ whiteuser
//						+ ",'"
//						+ f_ruler
//						+ "','"
//						+ stype
//						+ "','"
//						+ basetime
//						+ "','"
//						+ basetime
//						+ "','"
//						+ adduserid
//						+ "',"
//						+ matchid
//						+ ","
//						+ String.valueOf(round + 1)
//						+ ","
//						+ match_state
//						+ ","
//						+ (i + 1)
//						+ ",'"
//						+ ts
//						+ "',"
//						+ f_num
//						+ ",'"
//						+ f_positon
//						+ "','" + f_allstep + "')";
//
//				SQLStringList.add(insertStr1);
//				SQLStringList.add(insertStr2);
//				SQLStringList.add(insertStr3);
//				JdbcHelper.batchUpdate(SQLStringList);
//			}
//			// String orderStr = "";
//			// if ((round + 1) % 2 == 0) // 偶数轮
//			// {
//			// orderStr = "order by totalscore desc, num desc";
//			// } else {
//			// orderStr = "order by totalscore desc, num asc";
//			// }
//			// // 加载最新一轮比赛的编排信息
//			// String strLastest =
//			// "select * from v_schedule_make where userid=f_blackid and matchid="
//			// + matchid
//			// + " and groupid="
//			// + groupid
//			// + " and round="
//			// + String.valueOf(round + 1) + " " + orderStr;
//			// List result = JdbcHelper.query(strLastest);
//			// return getJsonMakeSchedule(result);
//			return "true";
//		}
//	}
//
//	/**
//	 * 状态检测。看当前赛事是否已经初始化，用户报名表已经存在编号
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @return
//	 * @throws Exception
//	 */
//	private int IfExistNum(int matchid, int groupid, int type) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			if (type == 1) {
//				sql = "select count(0) from t_applyform where matchid=? and status=1 and num>0";
//			} else if (type == 0) {
//				sql = "select count(0) from t_applyform where matchid=?  and status=1";
//			}
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				res = rs.getInt(1);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	/**
//	 * 生成随机数序列
//	 *
//	 * @param low
//	 * @param high
//	 * @return
//	 */
//	private int[] BuildRandomSequence(int low, int high) {
//		Random random = new Random();
//
//		int x = 0, tmp = 0;
//		if (low > high) {
//			tmp = low;
//			low = high;
//			high = tmp;
//		}
//		int[] array = new int[high - low + 1];
//		for (int i = low; i <= high; i++) {
//			array[i - low] = i;
//		}
//		for (int i = array.length - 1; i > 0; i--) {
//
//			x = random.nextInt(i + 1);
//			tmp = array[i];
//			array[i] = array[x];
//			array[x] = tmp;
//		}
//		return array;
//	}
//
//	@SuppressWarnings("resource")
//	private List<String> Sign_InInitialize(int matchid, int groupid)
//			throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		Random rd = new Random();
//		List<String> list = new ArrayList<String>();
//		List<Integer> Slist = new ArrayList<Integer>();
//		List<Integer> Slist_user = new ArrayList<Integer>();
//		List<Integer> Nlist = new ArrayList<Integer>();
//		List<Integer> Nlist_user = new ArrayList<Integer>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT a.num as a_num,b.num as b_num FROM"
//					+ " (SELECT COUNT(*) as 'num' FROM t_applyform WHERE matchid = "
//					+ matchid
//					+ " and sign_in=1) a"
//					+ ",(SELECT COUNT(*) as 'num' FROM t_applyform WHERE matchid = "
//					+ matchid + "   and sign_in=0) b";
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				// 签到者序号排在前面，未签到者预留在后面
//				int Sign_num = rs.getInt("a_num");
//				int N_Singn_num = rs.getInt("b_num");
//				while (Slist.size() < Sign_num) {
//					int num = rd.nextInt(Sign_num) + 1;
//					if (!Slist.contains(num)) {
//						Slist.add(num);
//					}
//				}
//				while (Nlist.size() < N_Singn_num) {
//					int num = rd.nextInt(N_Singn_num) + Sign_num + 1;
//					if (!Nlist.contains(num)) {
//						Nlist.add(num);
//					}
//				}
//				pstmt = conn
//						.prepareStatement("SELECT userid from t_applyform WHERE matchid = "
//								+ matchid + " and sign_in=1");
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					Slist_user.add(rs.getInt("userid"));
//				}
//				pstmt = conn
//						.prepareStatement("SELECT userid from t_applyform WHERE matchid = "
//								+ matchid + " and sign_in=0");
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					Nlist_user.add(rs.getInt("userid"));
//				}
//			}
//			if (Slist.size() == Slist_user.size()
//					&& Nlist.size() == Nlist_user.size()) {
//				for (int i = 0; i < Slist.size(); i++) {
//					String strUpdate = " update t_applyform set num= "
//							+ Slist.get(i) + " where matchid=" + matchid
//							+ " and userid=" + Slist_user.get(i);
//					list.add(strUpdate);
//				}
//				for (int i = 0; i < Nlist.size(); i++) {
//					String strUpdate = " update t_applyform set num= "
//							+ Nlist.get(i) + " where matchid=" + matchid
//							+ "  and userid=" + Nlist_user.get(i);
//					list.add(strUpdate);
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	private static String getJsonInitializeSchedule(List list) throws Exception {
//
//		String json = "";
//		List<TScheduleEntity> sdList = new ArrayList<TScheduleEntity>();
//		for (int i = 0; i < list.size(); i++) {
//			TScheduleEntity sd = new TScheduleEntity();
//			Map mp = (Map) list.get(i);
//			sd.setId(Integer.parseInt(mp.get("Id").toString()));
//			sd.setNum(Integer.parseInt(mp.get("num").toString()));
//			sd.setUsername(mp.get("realname").toString());
//			sdList.add(sd);
//		}
//		Gson gson = new Gson();
//
//		json = gson.toJson(sdList);
//		return json;
//
//	}
//
//	/**
//	 *
//	 * @param matchid
//	 *            赛事ID
//	 * @param group
//	 *            拟分组数，group>1 ,否则调用简单的设置所有人组ID为1即可。
//	 * @return
//	 * @throws Exception
//	 */
//	public String MatchGrouping(int matchid, int group) throws Exception {
//
//		int groupnum = 32;// 每组人数
//		int lastgroupnum;// 最后一组人数
//		int residue = 0;// 余数
//		int match_appform_sum = 0; // 总的有效报名人数
//		match_appform_sum = GetMatchAppformSum(matchid);
//		if (match_appform_sum == 0) {
//			return "false";
//		}
//		if (group > 0) {
//
//			residue = match_appform_sum % group;
//			if (residue == 0) {
//				groupnum = match_appform_sum / group;
//				lastgroupnum = match_appform_sum / group;
//			} else {
//				groupnum = match_appform_sum / group;
//				lastgroupnum = groupnum + residue;
//			}
//		} else {
//			if (match_appform_sum <= 32) { // 未传分组数时,总人数少于每组最大人数值时
//				return UpdateAPPformGroupOfOne(matchid);
//			}
//			groupnum = 32;
//			int dis = match_appform_sum / 32;
//			residue = match_appform_sum % 32;
//			if (residue == 0) {
//				group = dis;
//				lastgroupnum = 32;
//			} else { // 未传分组数时,总人数不能平均分配,余数以16为基准值(以后可以考虑二分法)
//				if (residue <= 16) {
//					group = dis; // 未满余数基准值全部放入最后一组
//					lastgroupnum = 32 + residue;
//				} else {
//					group = dis + 1; // 大于余数基准值再加一组
//					lastgroupnum = residue;
//				}
//			}
//		}
//
//		if (group == 1) {
//			return UpdateAPPformGroupOfOne(matchid);
//		}
//
//		// 得到本赛事所有报名用户列表，未排序. 不能整除多余的人数，可考虑先挑选出来最后集中放一组。
//		// 按每校取一人方式轮训取满。
//		List<ChessUser> listApplyUser = GetChessUsers(matchid);
//
//		List<ChessUser> residueChessUser = new ArrayList<ChessUser>(); // 余人列表
//
//		// 预先将多出来的一批用户，剔除出来。这批用户应该轮流取，一组取一个。
//		int residueCount = lastgroupnum - groupnum;
//		if (residueCount > 0) {
//			// groupuserlist 为 棋校名称+棋校学生列表 组成的LIST
//			List<MakeGrouping> groupuserlist = new ArrayList<MakeGrouping>();
//			groupuserlist = GetChessGroupUser(matchid);
//
//			int listIndex = 0;
//			for (int i = 0; i < residueCount; i++) {
//				if (groupuserlist.size() == 0) { // 所有组都用光了，循环无意义，跳出。
//					break;
//				}
//
//				if (groupuserlist.get(listIndex).getUseridlist().size() > 0) {
//					ChessUser user = new ChessUser();
//					user.setOrganization(groupuserlist.get(listIndex)
//							.getOrganization());
//					user.setUserid(groupuserlist.get(listIndex).getUseridlist()
//							.get(0));
//
//					residueChessUser.add(user); // 添加用户到余组里面
//
//					groupuserlist.get(listIndex).getUseridlist().remove(0);
//					listIndex++;
//					// 如果到最后一个了，重新赋值为0，重新从第1个棋校开始取。
//					if (listIndex >= groupuserlist.size()) {
//						listIndex = 0;
//					}
//				} else {
//					groupuserlist.remove(listIndex);
//
//					if (groupuserlist.size() == 0) {
//						break;
//					}
//
//					i--; // 本次轮训没有成功找到目标对象，所以需要重新再找
//
//				}
//
//			}
//
//			if (groupuserlist.size() == 0) { // 如果经过上面简单的处理，就没了。应该不至于。
//
//			}
//		}
//
//		listApplyUser.removeAll(residueChessUser);
//
//		// 最终每个组分配到的用户列表 形成的LIST 列表的列表。
//		List<GroupChessUser> grouplist = new ArrayList<GroupChessUser>();
//		for (int k = 0; k < group; k++) {
//			GroupChessUser groupChessUser = new GroupChessUser();
//			groupChessUser.setGroupid(k + 1);
//			grouplist.add(groupChessUser);
//		}
//
//		for (int i = 0; i < listApplyUser.size(); i++) {
//			int res = getBestGroup(listApplyUser.get(i), grouplist, groupnum);
//
//			if (res == 0)
//				break;
//		}
//
//		// 将早先预留下来的余人 统一加到最后一个组，最后一个组的人数最多。
//		grouplist.get(group - 1).getUserList().addAll(residueChessUser);
//
//		return UpdateGroup(matchid, grouplist);
//
//	}
//
//	// 获取总的报名人列表
//	private List<ChessUser> GetChessUsers(int matchid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<ChessUser> list = new ArrayList<ChessUser>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			// sql="SELECT userid,organization from t_applyform where matchid=? and `status`=1";
//			sql = "SELECT a.* from (SELECT userid,organization from t_applyform where matchid=? and `status`=1) a"
//					+ " LEFT JOIN  (SELECT organization,COUNT(1) as totalcount FROM t_applyform WHERE matchid=? GROUP BY organization) b"
//					+ " on a.organization=b.organization ORDER BY b.totalcount asc";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, matchid);
//			rs = pstmt.executeQuery();
//			ChessUser model = null;
//			while (rs.next()) {
//				model = new ChessUser();
//				model.setUserid(rs.getInt(1));
//				model.setOrganization(rs.getString(2));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	// ==========辅助方法：为当前用户查找最合适分配到的组，此方法会影响到输入参数
//	private int getBestGroup(ChessUser curUser, List<GroupChessUser> grouplist,
//			int groupSize) {
//
//		List<GroupChessUser> tempList = new ArrayList<GroupChessUser>();
//
//		// 预处理： 针对当前用户，调整grouplist值,得到每个组已存在该人同学的数目
//		for (int i = 0; i < grouplist.size(); i++) {
//
//			if (grouplist.get(i).isFull())
//				continue; // 已满的组，直接排除。
//
//			int tempNum = 0;
//			for (int j = 0; j < grouplist.get(i).getUserList().size(); j++) {
//				if (curUser.getOrganization()
//						.equalsIgnoreCase(
//								grouplist.get(i).getUserList().get(j)
//										.getOrganization())) {
//					tempNum++;
//				}
//			}
//
//			grouplist.get(i).setTempNum(tempNum);
//			tempList.add(grouplist.get(i));
//
//		}
//
//		if (tempList.size() == 0)
//			return 0;
//
//		// 对实体对象进行排序
//		descComparator comp = new descComparator();
//		Collections.sort(tempList, comp);
//
//		int index = tempList.get(0).getGroupid(); // 索引即组号-1
//
//		// 修改原始数据，将当前用户分配到该组
//		grouplist.get(index - 1).addUser(curUser);
//
//		if (grouplist.get(index - 1).getUserList().size() == groupSize)
//			grouplist.get(index - 1).setFull(true);
//
//		return index;
//	}
//
//	private String UpdateGroup(int matchid, List<GroupChessUser> list)
//			throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			for (int i = 1; i < list.size() + 1; i++) {
//				sql = "update t_applyform set groupid=" + i + " where matchid="
//						+ matchid + " and userid in("
//						+ list.get(i - 1).getUserList().get(0).getUserid() + "";
//				for (int j = 1; j < list.get(i - 1).getUserList().size(); j++) {
//					sql += ","
//							+ list.get(i - 1).getUserList().get(j).getUserid()
//							+ "";
//				}
//				sql += ")";
//				pstmt = conn.prepareStatement(sql);
//				res = pstmt.executeUpdate();
//			}
//
//			if (res > 0) {
//				UpdateMatchGroup(matchid, list.size(), 0);
//				return "true";
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return "false";
//	}
//
//	@Override
//	public String MatchGrouping(String info) throws Exception {
//		// TODO Auto-generated method stub
//		JSONObject jsonresult = new JSONObject(info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//		int group = 0; // 组数,若没传值,默认每组按32最多人数分组
//		int matchid = 0;
//		if (JsonObj.has("matchid")) {
//			matchid = JsonObj.getInt("matchid");// 赛事id
//		}
//		if (JsonObj.has("group")) {
//			group = JsonObj.getInt("group");
//		}
//		return MatchGrouping(matchid, group);
//	}
//
//	/**
//	 * 若分组id未传,报名人数又少于基准值则将所有报名用户分为1组
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @return
//	 * @throws Exception
//	 */
//	private String UpdateAPPformGroupOfOne(int matchid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "update t_applyform set groupid=1 where matchid=? and status=1";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			res = pstmt.executeUpdate();
//			if (res > 0) {
//				UpdateMatchGroup(matchid, 1, 0);
//				return "true";
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return "false";
//	}
//
//	private String UpdateAPPformGroup(int matchid, List<List<Integer>> list)
//			throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			for (int i = 1; i < list.size() + 1; i++) {
//				sql = "update t_applyform set groupid=" + i + " where matchid="
//						+ matchid + " and userid in(" + list.get(i - 1).get(0)
//						+ "";
//				for (int j = 1; j < list.get(i - 1).size(); j++) {
//					sql += "," + list.get(i - 1).get(j) + "";
//				}
//				sql += ")";
//				pstmt = conn.prepareStatement(sql);
//				res = pstmt.executeUpdate();
//			}
//
//			if (res > 0) {
//				UpdateMatchGroup(matchid, list.size(), 0);
//				return "true";
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return "false";
//	}
//
//	/**
//	 * 赛事分组表插入数据
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @param groupnum
//	 *            分组数
//	 * @param totalnum
//	 *            组内人数
//	 * @throws Exception
//	 */
//	private void UpdateMatchGroup(int matchid, int groupnum, int totalnum)
//			throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		String matchname = GetMatchName(matchid);
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "insert into t_match_group(matchid,groupid,groupname,totalnum,addtime) values("
//					+ matchid
//					+ ",1,'"
//					+ matchname
//					+ String.valueOf(1)
//					+ "组"
//					+ "',0,now())";
//			for (int i = 2; i < groupnum + 1; i++) {
//
//				sql += ",(" + matchid + "," + i + ",'" + matchname
//						+ String.valueOf(i) + "组" + "',0,now())";
//			}
//			pstmt = conn.prepareStatement(sql);
//			pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//	}
//
//	/**
//	 * 根据赛事id获取赛事名称
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @return
//	 * @throws Exception
//	 */
//	private String GetMatchName(int matchid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		String matchname = "";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "select matchname from t_match where id=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				matchname = rs.getString(1);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return matchname;
//	}
//
//	/**
//	 * 获取当前赛事当前场次通过报名审核的总人数
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @param batch
//	 *            场次
//	 * @return
//	 * @throws Exception
//	 */
//	private int GetMatchAppformSum(int matchid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int result = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT COUNT(0) from t_applyform where matchid=? and `status`=1";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				result = rs.getInt(1);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return result;
//	}
//
//	/**
//	 * 按棋校分组,获取各组用户列表
//	 *
//	 * @param mathcid
//	 *            赛事id
//	 * @param batch
//	 *            场次
//	 * @return
//	 * @throws Exception
//	 */
//	private List<MakeGrouping> GetChessGroupUser(int matchid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<MakeGrouping> list = new ArrayList<MakeGrouping>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT organization,COUNT(*) as totalcount from t_applyform where matchid=? and `status`=1  GROUP BY organization ORDER BY totalcount desc";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			MakeGrouping model = null;
//			while (rs.next()) {
//				model = new MakeGrouping();
//				model.setOrganization(rs.getString(1));
//				model.setUseridlist(GetUserListByorganization(rs.getString(1),
//						matchid));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	/**
//	 * 获取各个单位底下用户列表
//	 *
//	 * @param info
//	 *            所属单位
//	 * @return
//	 * @throws Exception
//	 */
//	private List<Integer> GetUserListByorganization(String info, int matchid)
//			throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<Integer> list = new ArrayList<Integer>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT userid from t_applyform where organization=? and status=1 and matchid=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, info);
//			pstmt.setInt(2, matchid);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				list.add(rs.getInt("userid"));
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public List<MakeGrouping> GetGroupList(String info) throws Exception {
//		// TODO Auto-generated method stub
//		JSONObject jsonresult = new JSONObject(info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//		int matchid = JsonObj.getInt("matchid");
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<MakeGrouping> list = new ArrayList<MakeGrouping>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT a.groupid,a.groupname,a.refree,b.username FROM t_match_group a left join sys_userinfo b on a.refree=b.userid WHERE a.matchid=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			MakeGrouping model = null;
//			while (rs.next()) {
//				model = new MakeGrouping();
//				model.setGroupname(rs.getString("groupname"));
//				model.setRefreeid(rs.getInt("refree"));
//				model.setRefreename(rs.getString("username"));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	/**
//	 * 获取赛事分组用户列表
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @param groupid
//	 *            分组id
//	 * @return
//	 * @throws Exception
//	 */
//	private List<MakeGrouping> GetGroupUserInfo(int matchid, int groupid)
//			throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<MakeGrouping> list = new ArrayList<MakeGrouping>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT a.userid,a.num,a.organization,b.sex,b.`level`,b.username from t_applyform a left join sys_userinfo b on a.userid=b.userid where a.matchid=? and a.groupid=? and a.userid!=9191 ORDER BY a.num asc";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, groupid);
//			rs = pstmt.executeQuery();
//			MakeGrouping model = null;
//			while (rs.next()) {
//				model = new MakeGrouping();
//				model.setUserid(rs.getInt("userid"));
//				model.setNum(rs.getInt("num"));
//				if (rs.getString("sex") == null) {
//					model.setSex("");
//				} else {
//					model.setSex(rs.getString("sex"));
//				}
//				if (rs.getString("organization") == null) {
//					model.setOrganization("");
//				} else {
//					model.setOrganization(rs.getString("organization"));
//				}
//				if (rs.getString("level") == null) {
//					model.setLevel("");
//				} else {
//					model.setLevel(rs.getString("level"));
//				}
//				model.setUsername(rs.getString("username"));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public List<UserEntity> GetRefreeList(String info) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<UserEntity> list = new ArrayList<UserEntity>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "select b.user_id,b.username from sys_user_role a left join sys_user b on a.user_id=b.user_id where a.role_id=10";
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			UserEntity model = null;
//			while (rs.next()) {
//				model = new UserEntity();
//				model.setUserid(rs.getInt("user_id"));
//				model.setRealname(rs.getString("username"));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public int SetRefree(String info) throws Exception {
//		// TODO Auto-generated method stub
//		JSONObject jsonresult = new JSONObject(info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		int matchid = JsonObj.getInt("matchid");
//		int userid = JsonObj.getInt("userid");
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "update t_match_group set refree=? where matchid=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, userid);
//			pstmt.setInt(2, matchid);
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	// public static void main(String[] args) throws Exception {
//	// MatchInfoDaoImpl daoImpl = new MatchInfoDaoImpl();
//
//	// String info ="{\"root\":[{\"matchid\":1,\"group\":3}]}";
//	// "{\"root\":[{\"matchid\":1,\"groupid\":1,\"round\":2}]}";
//	// daoImpl.SetBoardForGame(info);
//
//	// daoImpl.MatchGrouping(2, 2);
//	// daoImpl.DrawInMatch(2);
//	// daoImpl.MakeSchedule(2, 1, 1, 110);
//	// daoImpl.MakeSchedule(2, 2, 0, 110);
//
//	// }
//
//	@Override
//	public List<MatchGameRoom> GetMatchGameRoomList(int matchid, int groupid,
//			int round) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<MatchGameRoom> list = new ArrayList<MatchGameRoom>();
//		if (round == 0) {
//			round = GetMatchLastRound(matchid, groupid);
//		}
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT  * from v_schedule_make  where matchid=? and round=? and f_blackid=userid order by f_table_code asc";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, round);
//			rs = pstmt.executeQuery();
//			MatchGameRoom model = null;
//			while (rs.next()) {
//				model = new MatchGameRoom();
//				model.setGameid(rs.getInt("gameid"));
//				model.setScheduid(rs.getInt("Id"));
//				model.setBlackuserid(rs.getInt("f_blackid"));
//				model.setWhiteuserid(rs.getInt("f_whiteid"));
//				model.setStatus(rs.getInt("f_match_state"));
//				model.setBlackusername(rs.getString("realname"));
//				model.setWhiteusername(rs.getString("othersidename"));
//				model.setBlackorganization(rs.getString("blackorganization"));
//				model.setWhiteorganization(rs.getString("whiteorganization"));
//				model.setTablenum(rs.getInt("f_table_code"));
//				model.setF_num(rs.getInt("f_num"));
//				model.setF_roomnum(rs.getInt("f_roomnum"));
//				model.setMemo(rs.getString("memo"));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	/**
//	 * 获取最大轮次
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @param groupid
//	 *            分组id
//	 * @return
//	 * @throws Exception
//	 */
//	private int GetMatchLastRound(int matchid, int groupid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int round = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT MAX(round) from  t_schedule where match_id=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				round = rs.getInt(1);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return round;
//	}
//
//	@Override
//	public GameRoom GetGameRoomMsg(int id) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		GameRoom gr = new GameRoom();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			String sql = "select  SYSDATE() as nowtime,a.* from v_gameroom a where Id=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			rs = pstmt.executeQuery();
//			if (rs.first()) {
//				gr.setId(id);
//				gr.setNowtime(rs.getString("nowtime"));
//				gr.setF_match_state(rs.getInt("f_match_state"));
//				gr.setF_blackid(rs.getInt("f_blackid"));
//				gr.setF_whiteid(rs.getInt("f_whiteid"));
//				gr.setF_roomnum(rs.getInt("f_roomnum"));
//				gr.setF_num(rs.getInt("f_num"));
//				gr.setF_position(rs.getString("f_position"));
//				gr.setF_allstep(rs.getString("f_allstep"));
//				gr.setF_lastplaytime(rs.getString("f_lastplaytime"));
//				gr.setF_blackleft(rs.getInt("f_blackleft"));
//				gr.setF_whiteleft(rs.getInt("f_whiteleft"));
//				gr.setF_result(rs.getString("f_result"));
//				gr.setF_ruler(rs.getString("f_ruler"));
//				gr.setBlackname(rs.getString("blackname"));
//				gr.setWhitename(rs.getString("whitename"));
//				gr.setF_revisiontime(rs.getString("f_revisiontime"));
//				gr.setF_roomtype(rs.getString("f_roomtype"));
//				// gr.setbLevelName(rs.getString("blackLevelName"));
//				// gr.setwLevelName(rs.getString("whiteLevelName"));
//				gr.setbLevel(rs.getString("blackLevel"));
//				gr.setwLevel(rs.getString("whiteLevel"));
//				gr.setF_matchid(rs.getInt("f_matchid"));
//				gr.setF_round(rs.getInt("f_round"));
//				gr.setScheduleid(rs.getInt("scheduleid"));
//				gr.setF_memo(rs.getString("f_memo"));
//				if (rs.getString("f_starttime") == null
//						|| rs.getString("f_starttime").equals("")) {
//					gr.setF_starttime("");
//				} else {
//					gr.setF_starttime(rs.getString("f_starttime").substring(0,
//							19));
//				}
//				gr.setReferee(rs.getInt("matchadduser"));
//				gr.setF_add_userid(rs.getString("matchadduser"));
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return gr;
//	}
//
//	// public static void main(String[] args) throws IOException {
//	// MatchInfoDaoImpl daoImpl=new MatchInfoDaoImpl();
//	// daoImpl.DrawInMatch(1);
//	// try {
//	// daoImpl.MakeSchedule(1, 1, 0, 110);
//	// } catch (Exception e) {
//	// e.printStackTrace();
//	// }
//	// }
//
//	@Override
//	public TBoardEntity GetBoardModel(String info) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		TBoardEntity model = new TBoardEntity();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT * from v_board where deviceID=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, info);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				model.setCode(rs.getString("code"));
//				model.setAreaId(rs.getInt("area_id"));
//				model.setDeviceid(rs.getString("deviceid"));
//				model.setBoardid(rs.getInt("boardid"));
//				model.setAreaname(rs.getString("area_name"));
//				model.setTestcentername(rs.getString("center_name"));
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return model;
//	}
//
//	@Override
//	public int GetRemainGameRoomId(String code) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int gameid = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT id from t_gameroom where f_board_id=? and f_state=0 ORDER BY f_add_time DESC";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, code);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				gameid = rs.getInt(1);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return gameid;
//	}
//
//	@Override
//	public int SetBoardForGame(String info) throws Exception {
//		// TODO Auto-generated method stub
//		JSONObject jsonresult = new JSONObject(info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//		int matchid = JsonObj.getInt("matchid");
//		int round = JsonObj.getInt("round");
//		List<Integer> gameidlist = new ArrayList<Integer>();
//		gameidlist = GetMatchGroupGameid(matchid, 0, round);
//		if (gameidlist.size() == 0) {
//			return 0;
//		}
//		List<String> boardidlist = new ArrayList<String>();
//		boardidlist = GetBoardCodeList("");
//		if (boardidlist.size() == 0) {
//			return 0;
//		}
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		int index = gameidlist.size() > boardidlist.size() ? boardidlist.size()
//				: gameidlist.size();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			conn.setAutoCommit(false);
//			for (int i = 0; i < index; i++) {
//				sql = "update t_gameroom set f_board_id='" + boardidlist.get(i)
//						+ "',f_table_code=" + (i + 1) + " where id="
//						+ gameidlist.get(i) + "";
//				pstmt = conn.prepareStatement(sql);
//				res += pstmt.executeUpdate();
//				conn.commit();// 提交事务
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			try {
//				conn.rollback(); // 进行事务回滚
//			} catch (SQLException ex) {
//			}
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	/**
//	 * 获取棋盘code列表
//	 *
//	 * @param info
//	 *            (暂未用)备用
//	 * @return
//	 * @throws Exception
//	 */
//	private List<String> GetBoardCodeList(String info) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<String> list = new ArrayList<String>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT code from t_board  where code not in(SELECT f_board_id from t_gameroom where f_state=0 GROUP BY f_board_id) GROUP BY code";
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				list.add(rs.getString(1));
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	/**
//	 * 获取当前赛事当前组最新轮次对局id列表
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @param groupid
//	 *            分组id
//	 * @return
//	 * @throws Exception
//	 */
//	private List<Integer> GetMatchGroupGameid(int matchid, int groupid,
//			int round) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<Integer> list = new ArrayList<Integer>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT id from t_gameroom where f_matchid=?  and f_round=? and f_blackid!=9191 and f_whiteid!=9191";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, round);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				list.add(rs.getInt(1));
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public int GetMaxRound(int matchid, int groupid) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT MAX(round) from t_schedule where match_id=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				res = rs.getInt(1);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	private int SelectUserId(int id) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String sql = "select userid  from t_schedule where id=?";
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				res = rs.getInt("userid");
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return -1;
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public String SetScore(String info) throws Exception {
//		// TODO Auto-generated method stub
//		String coderesult = "false";
//		JSONObject jsonresult = new JSONObject(info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//		int matchid = JsonObj.getInt("matchid");
//		int round = JsonObj.getInt("round");
//		int sscore = JsonObj.getInt("bscore");
//		int oscore = JsonObj.getInt("wscore");
//		int id = JsonObj.getInt("id");
//		int ouser = JsonObj.getInt("wuserid");
//		int opuserid = 0;
//		int gameid = 0;
//		if (JsonObj.has("gameid")) {
//			gameid = JsonObj.getInt("gameid");
//		}
//		if (JsonObj.has("opuserid")) {
//			opuserid = JsonObj.getInt("opuserid");
//		}
//		int memo = 0;
//		int wintype = 0;
//		if (JsonObj.has("wintype")) {
//			wintype = JsonObj.getInt("wintype");
//		}
//		String result = "";
//		if (JsonObj.has("memo")) {
//			memo = JsonObj.getInt("memo");
//		}
//		if (JsonObj.has("result")) {
//			result = JsonObj.getString("result");
//		}
//		if (SelectScore(matchid, SelectUserId(id), ouser, round, 0)) {
//			String updateStr1 = "update t_schedule set score=" + sscore
//					+ ",totalscore=totalscore+" + sscore + " where match_id="
//					+ matchid + " and Id=" + id;
//			String updateStr2 = "update t_schedule set score=" + oscore
//					+ ",totalscore=totalscore+" + oscore + " where match_id="
//					+ matchid + " and userid=" + ouser + " and round=" + round;
//
//			List<String> SQLStringList = new ArrayList<String>();
//			SQLStringList.add(updateStr1);
//			SQLStringList.add(updateStr2);
//			int iret[] = JdbcHelper.batchUpdate(SQLStringList);
//		} else {
//			UpdateScore(matchid, SelectUserId(id), sscore, round, 0);
//			UpdateScore(matchid, ouser, oscore, round, 0);
//		}
//
//		int iround = round;
//
//		String orderStr = "";
//
//		if (iround % 2 == 0) // 偶数轮
//		{
//			orderStr = "order by totalscore-myscore desc, num desc";
//		} else {
//			orderStr = "order by totalscore-myscore desc, num asc";
//		}
//		if ((sscore == 0 && oscore == 0) || wintype == 2) {
//			String sql1 = "update t_schedule set wintype=2 where match_id="
//					+ matchid + " and (userid=" + ouser + " or opponent="
//					+ ouser + ") and round=" + round;
//			int res3 = JdbcHelper.update(sql1);
//		}
//		String upgamemsg = "update t_gameroom set f_match_state=1,f_state=1,f_result='"
//				+ result
//				+ "',f_memo='"
//				+ String.valueOf(memo)
//				+ "',referee="
//				+ opuserid
//				+ "  where f_matchid="
//				+ matchid
//				+ " and f_round="
//				+ round
//				+ " and (f_blackid="
//				+ ouser
//				+ " or f_whiteid="
//				+ ouser
//				+ ")";
//		int res_upgamemsg = JdbcHelper.update(upgamemsg);
//		if (opuserid != 0) {
//			String upusergame = "INSERT INTO t_usergame (f_gamename,f_name,f_num,f_position,f_allstep,f_black,f_white,f_result,f_memo,f_sz,f_degreeofopenness,f_timestamp,f_adduser) SELECT f_gamename,f_roomname AS f_name,f_num,f_position,f_allstep,f_blackid AS f_black,f_whiteid AS f_white,f_result,f_memo,f_roomnum AS f_sz,f_degreeofopenness,NOW() AS f_timestamp,f_add_user FROM t_gameroom WHERE f_matchid ="
//					+ matchid
//					+ " and f_round="
//					+ round
//					+ " and (f_blackid="
//					+ ouser + " or f_whiteid=" + ouser + ")";
//			int res_upusergame = JdbcHelper.update(upusergame);
//			updateminastatus(501, opuserid, gameid);
//		}
//		// 加载最新一轮比赛的编排信息
//		// string strLast =
//		// "select * from v_schedule a left join applyform b on a.matchid=b.matchid and a.userid=b.userid left join applyform c on a.matchid=c.matchid and a.opponent=c.userid where a.matchid="
//		// + matchid + " and a.round=" + round.ToString() +
//		// " order by b.num asc";
//		// String strLast =
//		// "select *,case WHEN score is null then 0 else score END as myscore,case WHEN opponentscore is null then 0 else opponentscore END as opponentscore from v_schedule_make where  matchid="
//		// + matchid
//		// + " and round="
//		// + round
//		// + " and userid=f_blackid " + orderStr;
//		//
//		// List listLastRound = JdbcHelper.query(strLast);
//		coderesult = "true";
//		// return getJsonSetScore(listLastRound);
//		// DbHelperMySQL.ExecuteSqlTran(SQLStringList);
//		return coderesult;
//
//	}
//
//	private void updateminastatus(int method, int opuserid, int gameid)
//			throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String sql = "";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "update minalogs set status=1 where method=? and toid=? and spareid=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, method);
//			pstmt.setInt(2, opuserid);
//			pstmt.setInt(3, gameid);
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//	}
//
//	@SuppressWarnings("resource")
//	private String UpdateScore(int matchid, int userid, int score, int round,
//			int groupid) {
//		// x=1表示胜利方
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		int my_score = 0;
//		String sql2 = "";
//		String sql = "select score from t_schedule where match_id=? and round=? and userid=? limit 0,1";
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, round);
//			pstmt.setInt(3, userid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				my_score = rs.getInt("score");
//				if (my_score == score) {// 修改数据和元数据相同，不处理
//					return "true";
//				} else {
//					sql2 = "update t_schedule set score=" + score
//							+ " ,totalscore=((totalscore-" + my_score + ")+"
//							+ score + ") where match_id=" + matchid
//							+ " and round=" + round + " and userid=" + userid;
//				}
//				pstmt = conn.prepareStatement(sql2);
//				res = pstmt.executeUpdate();
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		if (res > 0) {
//			return "true";
//		} else {
//			return "false";
//		}
//
//	}
//
//	private boolean SelectScore(int matchid, int userid, int ouserid,
//			int round, int groupid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "select score from t_schedule where match_id=?  and round=? and (userid=? or userid=?)";
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, round);
//			pstmt.setInt(3, userid);
//			pstmt.setInt(4, ouserid);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				if (rs.getInt("score") > 0) {
//					return false;
//				}
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return true;
//	}
//
//	private static String getJsonSetScore(List list) throws Exception {
//
//		String json = "";
//		List<TScheduleEntity> schList = new ArrayList<TScheduleEntity>();
//		for (int i = 0; i < list.size(); i++) {
//			TScheduleEntity schedule = new TScheduleEntity();
//			Map mp = (Map) list.get(i);
//			schedule.setId(Integer.parseInt(mp.get("Id").toString()));
//			schedule.setNum(Integer.parseInt(mp.get("num").toString()));
//			schedule.setUsername(mp.get("realname").toString());
//			schedule.setOpponentname(mp.get("othersidename").toString());
//			String totalscore = mp.get("totalscore").toString();
//			if (totalscore.length() == 0 || totalscore == null) {
//				schedule.setTotalscore(0);
//			} else {
//				schedule.setTotalscore(Integer.parseInt(totalscore));
//			}
//			String score = mp.get("myscore").toString();
//			if (score == null || score.length() == 0) {
//				schedule.setScore(0);
//			} else {
//				schedule.setScore(Integer.parseInt(score));
//			}
//			// schedule.setScore(Integer.parseInt(mp.get("score").toString()));
//			String round = mp.get("round").toString();
//			if (round == null || round.length() == 0) {
//				schedule.setRound(0);
//			} else {
//				schedule.setRound(Integer.parseInt(round));
//			}
//			String opponent = mp.get("opponent").toString();
//			if (opponent.length() == 0 || opponent == null) {
//				schedule.setOpponent(0);
//			} else {
//				schedule.setOpponent(Integer.parseInt(opponent));
//			}
//			schedule.setMatch_state(Integer.parseInt(mp.get("f_match_state")
//					.toString()));
//			schedule.setTableNum(Integer.parseInt(mp.get("f_table_code")
//					.toString()));
//			schedule.setOpponentscore(Integer.parseInt(mp.get("opponentscore")
//					.toString()));
//			schedule.setOpponenttotalscore(Integer.parseInt(mp.get(
//					"otherTotalScore").toString()));
//			schedule.setGameid(Integer.parseInt(mp.get("gameid").toString()));
//			schedule.setRefreeid(Integer.parseInt(mp.get("referee").toString()));
//			schedule.setMark(score + "-" + mp.get("opponentscore").toString());
//			// String iresult = mp.get("iresult").toString();
//			// if (iresult == null || iresult.length() == 0) {
//			// schedule.setIresult(0);
//			// } else {
//			// schedule.setIresult(Integer.parseInt(iresult));
//			// }
//			schedule.setUserid(Integer.parseInt(mp.get("userid").toString()));
//			String bnum = mp.get("bnum").toString();
//			if (bnum == null || bnum.length() == 0) {
//				schedule.setOpponentnum(0);
//			} else {
//				schedule.setOpponentnum(Integer.parseInt(bnum));
//			}
//			schList.add(schedule);
//		}
//		Gson gson = new Gson();
//		TScheduleEntity s = new TScheduleEntity();
//		for (int i = 0; i < schList.size(); i++) {
//			if (schList.get(i).getUserid() == 9191
//					|| schList.get(i).getOpponent() == 9191) {
//				s = schList.get(i);
//				schList.remove(i);
//			}
//
//		}
//		if (schList != null
//				&& ((s.getUserid() != null && s.getUserid() == 9191) || (s
//						.getOpponent() != null && s.getOpponent() == 9191))) {
//			schList.add(s);
//		}
//		json = gson.toJson(schList);
//		return json;
//
//	}
//
//	class ascComparator implements Comparator<GroupChessUser> {
//
//		@Override
//		public int compare(GroupChessUser o1, GroupChessUser o2) {
//
//			if (o1.getTempNum() > o2.getTempNum()) {
//				return -1;
//			} else if (o1.getTempNum() == o2.getTempNum()) {
//				if (o1.getTotalNum() > o2.getTotalNum()) {
//					return 1;
//				} else if (o1.getTotalNum() == o2.getTotalNum()) {
//					return 0;
//				} else {
//					return -1;
//				}
//
//			} else {
//				return 1;
//			}
//
//		}
//
//	}
//
//	class descComparator implements Comparator<GroupChessUser> {
//
//		@Override
//		public int compare(GroupChessUser o1, GroupChessUser o2) {
//			if (o1.getTempNum() > o2.getTempNum()) {
//				return 1;
//			} else if (o1.getTempNum() == o2.getTempNum()) {
//				if (o1.getTotalNum() > o2.getTotalNum()) {
//					return -1;
//				} else if (o1.getTotalNum() == o2.getTotalNum()) {
//					return 0;
//				} else {
//					return 1;
//				}
//
//			} else {
//				return -1;
//			}
//
//		}
//
//	}
//
//	@Override
//	public String CacResult(String Info) throws Exception {
//		// TODO Auto-generated method stub
//		JSONObject jsonresult;
//		jsonresult = new JSONObject(Info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//		int matchid = JsonObj.getInt("matchid");
//		int round = JsonObj.getInt("round");
//		String sqlStr = "select * from v_schedule_make where matchid="
//				+ matchid + " and round=" + round;
//		List list = JdbcHelper.query(sqlStr);
//
//		List<String> SQLStringList = new ArrayList<String>();
//
//		for (int i = 0; i < list.size(); i++) // 计算小分。
//		{
//			Map findnew = (Map) list.get(i);
//			String userid = findnew.get("userid").toString();
//			String updateStr = "update `t_schedule` a ,(select sum(totalscore) as totalscore from `t_schedule` WHERE userid in (SELECT opponent from `t_schedule` where userid="
//					+ userid
//					+ " and match_id="
//					+ matchid
//					+ ")  AND round="
//					+ round
//					+ " and match_id="
//					+ matchid
//					+ ")  b set a.iresult=b.totalscore WHERE userid="
//					+ userid
//					+ " and match_id=" + matchid + " and round=" + round;
//
//			SQLStringList.add(updateStr);
//
//		}
//		int iret[] = JdbcHelper.batchUpdate(SQLStringList);
//		int iresu = 1;
//		for (int i = 0; i < iret[i]; i++) {
//			if (iret[i] <= 0) {
//				iresu = iret[i];
//			}
//		}
//		List<UserScoreMessage> list2 = new ArrayList<UserScoreMessage>();
//		List<UserScoreMessage> userlist = SelectUserScore(matchid, 0);
//		UserComparator comp = new UserComparator();
//		Collections.sort(userlist, comp);
//		for (UserScoreMessage userScoreMessage : userlist) {
//			UserScoreMessage USM = new UserScoreMessage();
//			USM.setUserId(userScoreMessage.getUserId());
//			USM.setUserName(userScoreMessage.getUserName());
//			USM.setTotal_points(userScoreMessage.getTotal_points());
//			USM.setOpponent_total_points(userScoreMessage
//					.getOpponent_total_points());
//			if (USM.getUserId() != 9191) {
//				list2.add(USM);
//			}
//		}
//		String setCharts = SetCharts(matchid, list2);
//		String res = "0";
//		if (iresu <= 0) {
//			res = "0";
//		} else if (iresu > 0) {
//			res = "1";
//		}
//		return res;
//	}
//
//	private String SetCharts(int matchid, List<UserScoreMessage> list) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		List<String> SQLStringList = new ArrayList<String>();
//		int MaxPlacing = 0;
//		int ress = 1;
//		int res = 0;
//		String sql = "Delete from t_match_charts where matchid=?";
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			res = pstmt.executeUpdate();
//			for (int i = 0; i < list.size(); i++) {
//				UserScoreMessage userScoreMessage = list.get(i);
//				String sql2 = "insert into t_match_charts(matchid,placing,userid,realname,total_points,ototal_points) values("
//						+ matchid
//						+ ","
//						+ (i + 1)
//						+ ","
//						+ userScoreMessage.getUserId()
//						+ ",'"
//						+ userScoreMessage.getUserName()
//						+ "',"
//						+ userScoreMessage.getTotal_points()
//						+ ","
//						+ userScoreMessage.getOpponent_total_points() + ")";
//				SQLStringList.add(sql2);
//			}
//			int[] batchUpdate = JdbcHelper.batchUpdate(SQLStringList);
//			for (int i = 0; i < batchUpdate.length; i++) {
//				if (batchUpdate[i] <= 0) {
//					ress = batchUpdate[i];
//				}
//			}
//			if (ress > 0) {
//				return "true";
//			} else {
//				return "false";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return "false";
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//	}
//
//	private List<UserScoreMessage> SelectUserScore(int matchid, int groupid) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		List<UserScoreMessage> list = new ArrayList<UserScoreMessage>();
//		conn = DBPool.getInstance().getConnection();
//		String sql = "select userid,realname from t_applyform where matchid=? and status>0";
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				UserScoreMessage UserSM = new UserScoreMessage();
//				UserSM.setUserId(rs.getInt("userid"));
//				UserSM.setUserName(rs.getString("realname"));
//				// UserSM.setOpponent_total_points(rs.getInt("userid"));
//				UserScoreMessage selectUserScore2 = SelectUserScore2(matchid,
//						rs.getInt("userid"), groupid);
//				UserSM.setOpponent_total_points(selectUserScore2
//						.getOpponent_total_points());
//				UserSM.setTotal_points(selectUserScore2.getTotal_points());
//				UserSM.setWin_list(selectUserScore2.getWin_list());
//				UserSM.setLose_list(selectUserScore2.getLose_list());
//				UserSM.top();
//				UserSM.bestUser();
//				if (UserSM.getTotal_points() != 0) {
//					list.add(UserSM);
//				}
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	private UserScoreMessage SelectUserScore2(int matchid, int userid,
//			int groupid) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int Max = 0;
//		int round = 0;
//		UserScoreMessage UserSM = new UserScoreMessage();
//		conn = DBPool.getInstance().getConnection();
//		String sql = "select * from t_schedule where match_id=?  and userid=? ORDER BY round desc";
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, userid);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				int opponent = 0;
//				if (rs.getInt("totalscore") > Max) {
//					Max = rs.getInt("totalscore");
//					UserSM.setTotal_points(Max);
//
//				}
//				if (rs.getInt("round") > round
//						&& rs.getString("iresult") != null) {
//					round = rs.getInt("round");
//					UserSM.setOpponent_total_points(rs.getInt("iresult"));
//				}
//				if (rs.getString("score") != null && rs.getInt("score") == 0) {
//					opponent = rs.getInt("opponent");
//					UserScoreMessage selectUserScore3 = SelectUserScore3(
//							matchid, opponent, groupid);
//					if (selectUserScore3 != null) {
//						UserSM.setLose_list(selectUserScore3);
//					}
//				}
//				if (rs.getString("score") != null && rs.getInt("score") == 2) {
//					opponent = rs.getInt("opponent");
//					UserScoreMessage selectUserScore3 = SelectUserScore3(
//							matchid, opponent, groupid);
//					if (selectUserScore3 != null) {
//						UserSM.setWin_list(selectUserScore3);
//					}
//				}
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return UserSM;
//	}
//
//	private UserScoreMessage SelectUserScore3(int matchid, int userid,
//			int groupid) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int round = 0;
//		UserScoreMessage UserSM = new UserScoreMessage();
//		conn = DBPool.getInstance().getConnection();
//		String sql = "select userid,totalscore,iresult,round from t_schedule where match_id=?  and userid=? ORDER BY round desc";
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, userid);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				UserSM.setUserId(rs.getInt("userid"));
//				UserSM.setTotal_points(rs.getInt("totalscore"));
//				if (rs.getInt("round") > round
//						&& rs.getString("iresult") != null) {
//					round = rs.getInt("round");
//					UserSM.setOpponent_total_points(rs.getInt("iresult"));
//				}
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return UserSM;
//	}
//
//	@Override
//	public String DeleteGiveUp(String info) throws Exception {
//		// TODO Auto-generated method stub
//		String json = "false";
//		JSONObject jsonresult;
//		JSONArray array;
//		try {
//			jsonresult = new JSONObject(info);
//			array = jsonresult.getJSONArray("root");
//			JSONObject JsonObj = array.getJSONObject(0);
//			int matchid = JsonObj.getInt("matchid");
//			// int userid =JsonObj.getInt("userid");
//			json = DeleteGiveUp(matchid, 0);
//		} catch (JSONException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//		return json;
//	}
//
//	private String DeleteGiveUp(int matchid, int groupid) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		// String sql =
//		// "SELECT * from t_schedule where round = (SELECT MAX(round) from t_schedule WHERE match_id=? and group_id=?) and match_id =? and group_id=? and iresult IS not NULL;";
//		try {
//			String sql1 = "select userid from t_applyform where matchid="
//					+ matchid + " and status=1";
//			pstmt = conn.prepareStatement(sql1);
//			rs = pstmt.executeQuery();
//			List<String> list = new ArrayList<String>();
//			while (rs.next()) {
//				int userid = 0;
//				userid = rs.getInt("userid");
//				if (userid != 9191) {
//					if (UserGiveUpNum(matchid, userid, groupid)) {
//						String sql2 = "update t_applyform set status=-1 where userid="
//								+ userid + " and matchid=" + matchid;
//						list.add(sql2);
//					}
//				}
//			}
//			if (list.size() > 0) {
//				int[] batchUpdate = JdbcHelper.batchUpdate(list);
//				int iresu = 1;
//				for (int i = 0; i < batchUpdate.length; i++) {
//					if (batchUpdate[i] <= 0) {
//						iresu = batchUpdate[i];
//					}
//				}
//				if (iresu <= 0) {
//					return "false";
//				} else {
//					return "true";
//				}
//			} else {
//				return "NoBody";
//			}
//
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return "SQL_Exception";
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//	}
//
//	private boolean UserGiveUpNum(int matchid, int userid, int groupid) {
//		int res = 0;
//		int max = 0;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		String sql = "SELECT COUNT(*) as num from t_schedule WHERE match_id=?  and userid = ? and wintype=2 and score=0";
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, userid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				if (rs.getInt("num") > 0) {
//					return true;
//				} else {
//					return false;
//				}
//			} else {
//				return false;
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return false;
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//	}
//
//	@Override
//	public int UpdateGameRoomStepTemp(String info) throws Exception {
//		// TODO Auto-generated method stub
//		JSONObject jsonresult = new JSONObject(info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//		int gameid = 0;
//		String newallstep = "";
//		int num = 0;
//		String position = "";
//		if (JsonObj.has("gameid")) {
//			gameid = JsonObj.getInt("gameid");
//		}
//		if (JsonObj.has("newallstep")) {
//			newallstep = JsonObj.getString("newallstep");
//		}
//		if (JsonObj.has("f_num")) {
//			num = JsonObj.getInt("f_num");
//		}
//		if (JsonObj.has("f_position")) {
//			position = JsonObj.getString("f_position");
//		}
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String sql = "";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "update t_gameroom set f_allstep=?,f_num=?,f_position=? where id=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, newallstep);
//			pstmt.setInt(2, num);
//			pstmt.setString(3, position);
//			pstmt.setInt(4, gameid);
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public String SetScoreForPc(String info) throws Exception {
//		// TODO Auto-generated method stub
//		JSONObject jsonresult = new JSONObject(info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//		int matchid = JsonObj.getInt("matchid");
//		int round = JsonObj.getInt("round");
//		int sscore = JsonObj.getInt("bscore");
//		int oscore = JsonObj.getInt("wscore");
//		int id = JsonObj.getInt("id");
//		int ouser = JsonObj.getInt("wuserid");
//		int memo = 0;
//		int wintype = 0;
//		if (JsonObj.has("wintype")) {
//			wintype = JsonObj.getInt("wintype");
//		}
//		String result = "";
//		if (JsonObj.has("memo")) {
//			memo = JsonObj.getInt("memo");
//		}
//		if (JsonObj.has("result")) {
//			result = JsonObj.getString("result");
//		}
//		if (SelectScore(matchid, SelectUserId(id), ouser, round, 0)) {
//			String updateStr1 = "update t_schedule set score=" + sscore
//					+ ",totalscore=totalscore+" + sscore + " where match_id="
//					+ matchid + " and Id=" + id;
//			String updateStr2 = "update t_schedule set score=" + oscore
//					+ ",totalscore=totalscore+" + oscore + " where match_id="
//					+ matchid + " and userid=" + ouser + " and round=" + round;
//
//			List<String> SQLStringList = new ArrayList<String>();
//			SQLStringList.add(updateStr1);
//			SQLStringList.add(updateStr2);
//			int iret[] = JdbcHelper.batchUpdate(SQLStringList);
//		} else {
//			UpdateScore(matchid, SelectUserId(id), sscore, round, 0);
//			UpdateScore(matchid, ouser, oscore, round, 0);
//		}
//
//		int iround = round;
//
//		String orderStr = "";
//
//		if (iround % 2 == 0) // 偶数轮
//		{
//			orderStr = "order by totalscore-myscore desc, num desc";
//		} else {
//			orderStr = "order by totalscore-myscore desc, num asc";
//		}
//		if ((sscore == 0 && oscore == 0) || wintype == 2) {
//			String sql1 = "update t_schedule set wintype=2 where match_id="
//					+ matchid + " and (userid=" + ouser + " or opponent="
//					+ ouser + ") and round=" + round;
//			int res3 = JdbcHelper.update(sql1);
//		}
//		String upgamemsg = "update t_gameroom set f_match_state=1,f_state=1,f_result='"
//				+ result
//				+ "',f_memo='"
//				+ String.valueOf(memo)
//				+ "' where f_matchid="
//				+ matchid
//				+ " and f_round="
//				+ round
//				+ " and (f_blackid=" + ouser + " or f_whiteid=" + ouser + ")";
//		int res_upgamemsg = JdbcHelper.update(upgamemsg);
//		return "true";
//		// DbHelperMySQL.ExecuteSqlTran(SQLStringList);
//
//	}
//
//	@Override
//	public int CreateMatch(MatchInfo model) throws Exception {
//		// TODO Auto-generated method stub
//		int matchid = 0;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		switch (model.getType()) {
//		case 1:
//			model.setBoardsize(9);
//			;
//			break;
//		case 2:
//			model.setBoardsize(13);
//			;
//			break;
//		case 3:
//			model.setBoardsize(13);
//			;
//			break;
//		case 4:
//			model.setBoardsize(19);
//			;
//			break;
//
//		default:
//			model.setBoardsize(19);
//			;
//		}
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "insert into t_match(matchname,match_date,match_enddate,apply_starttime,apply_endtime,max_round,match_ruler,match_type,level_lower,level_upper,number_upper,match_content,adduser,number_lower,applytype,addtime,type,board_size) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?)";
//			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//			pstmt.setString(1, model.getMatchname());
//			pstmt.setDate(2, new java.sql.Date(model.getMatchDate().getTime()));
//			pstmt.setDate(3, new java.sql.Date(model.getMatchEndDate()
//					.getTime()));
//			pstmt.setTimestamp(4, model.getApplystarttime());
//			pstmt.setTimestamp(5, model.getApplyendtime());
//			pstmt.setInt(6, model.getMaxRound());
//			pstmt.setString(7, model.getRuler());
//			pstmt.setInt(8, model.getMatchtype());
//			pstmt.setInt(9, model.getLevel_lower());
//			pstmt.setInt(10, model.getLevel_upper());
//			pstmt.setInt(11, model.getNumber_upper());
//			pstmt.setString(12, model.getMatch_content());
//			pstmt.setInt(13, model.getAdduser());
//			pstmt.setInt(14, model.getNumber_lower());
//			pstmt.setInt(15, model.getApplytype());
//			pstmt.setInt(16, model.getType());
//			pstmt.setInt(17, model.getBoardsize());
//			res = pstmt.executeUpdate();
//			rs = pstmt.getGeneratedKeys();
//			if (rs.next()) {
//				matchid = rs.getInt(1);
//				if (model.getMatchUserList().size() > 0) {
//					insertapplyform(matchid, model.getMatchUserList());
//				}
//				if (model.getRoundtimelist() != null) {
//					if (model.getRoundtimelist().size() > 0) {
//						UpdateMatchRoundTime(matchid, model.getRoundtimelist());
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return matchid;
//	}
//
//	/**
//	 * 更新赛事轮次时间表
//	 *
//	 * @param matchid
//	 * @param list
//	 * @throws Exception
//	 */
//	private void UpdateMatchRoundTime(int matchid,
//			List<Map<String, Object>> list) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			if (DeleteMatchRoundTime(matchid) != -1) {
//				sql = "insert into t_match_round(matchid,round,roundtime,addtime) values("
//						+ matchid
//						+ ","
//						+ list.get(0).get("round")
//						+ ",'"
//						+ list.get(0).get("roundtime") + "',now())";
//				for (int i = 1; i < list.size(); i++) {
//					sql += ",(" + matchid + "," + list.get(i).get("round")
//							+ ",'" + list.get(i).get("roundtime") + "',now())";
//				}
//				pstmt = conn.prepareStatement(sql);
//				pstmt.executeUpdate();
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//	}
//
//	private int DeleteMatchRoundTime(int matchid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "delete from t_match_round where matchid=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO: handle exception
//			res = -1;
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public MatchInfo GetMatchModel(int matchid) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		MatchInfo model = new MatchInfo();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT *,(SELECT COUNT(0) from t_applyform where matchid="
//					+ matchid
//					+ " and status=1)as applynum from t_match where id="
//					+ matchid + "";
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				model.setId(rs.getInt("id"));
//				model.setMatchname(rs.getString("matchname"));
//				model.setMatchDate(rs.getDate("match_date"));
//				model.setMatchEndDate(rs.getDate("match_enddate"));
//				model.setApplyendtime(rs.getTimestamp("apply_endtime"));
//				model.setApplystarttime(rs.getTimestamp("apply_starttime"));
//				model.setLevel_lower(rs.getInt("level_lower"));
//				model.setLevel_upper(rs.getInt("level_upper"));
//				model.setStatus(rs.getInt("status"));
//				model.setMatch_content(rs.getString("match_content"));
//				model.setApplynum(rs.getInt("applynum"));
//				model.setMaxRound(rs.getInt("max_round"));
//				model.setType(rs.getInt("type"));
//				model.setMatchtype(rs.getInt("match_type"));
//				model.setApplytype(rs.getInt("applytype"));
//				model.setNumber_lower(rs.getInt("number_lower"));
//				model.setNumber_upper(rs.getInt("number_upper"));
//				model.setRoundtimelist(GetMatchRoundTimeList(matchid));
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return model;
//	}
//
//	private List<Applyform> GetUserInfoList(int classid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<Applyform> list = new ArrayList<Applyform>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT b.UserId,b.Realname,b.Tel,b.num,c.chessname from t_school_class_student a LEFT JOIN t_user b on a.studentid=b.UserId LEFT JOIN t_chess_school c on b.ParentId=c.id where a.classid=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, classid);
//			rs = pstmt.executeQuery();
//			Applyform model = null;
//			while (rs.next()) {
//				model = new Applyform();
//				model.setUserid(rs.getInt("userid"));
//				model.setRealname(rs.getString("realname"));
//				model.setOrganization(rs.getString("chessname"));
//				model.setTel(rs.getString("tel"));
//				model.setNum(rs.getInt("num"));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	private int insertapplyform(int matchid, List<Applyform> list)
//			throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		// int userlevel=0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			// if(list.get(0).getLevel()!=null &&
//			// !list.get(0).getLevel().equals("")){
//			// userlevel=GetUserLevel(list.get(0).getLevel());
//			// }
//			sql = "insert into t_applyform(uuid,userid,matchid,status,realname,organization,num,sign_in,addtime,`level`) values('"
//					+ java.util.UUID.randomUUID().toString()
//					+ "',"
//					+ list.get(0).getUserid()
//					+ ","
//					+ matchid
//					+ ",1,'"
//					+ list.get(0).getRealname()
//					+ "','"
//					+ list.get(0).getOrganization()
//					+ "',0,0,now(),'"
//					+ list.get(0).getLevel() + "')";
//			for (int i = 1; i < list.size(); i++) {
//				// if(list.get(i).getLevel()!=null &&
//				// !list.get(i).getLevel().equals("")){
//				// userlevel=GetUserLevel(list.get(i).getLevel());
//				// }
//				sql += ",('" + java.util.UUID.randomUUID().toString() + "',"
//						+ list.get(i).getUserid() + "," + matchid + ",1,'"
//						+ list.get(i).getRealname() + "','"
//						+ list.get(i).getOrganization() + "',0,0,now(),'"
//						+ list.get(i).getLevel() + "')";
//			}
//			pstmt = conn.prepareStatement(sql);
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public List<Map<String, Object>> GetChessSchoolList(int userid)
//			throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<Map<String, Object>> list = new ArrayList<>();
//		List<Integer> roleidlist = GetUserRole(userid);
//		try {
//			conn = DBPool.getInstance().getConnection();
//			if (roleidlist.contains(2) || roleidlist.contains(3)) {
//				sql = "SELECT dept_id as id,`name` as chessname from sys_dept where (dept_id=(SELECT parentid from t_loginuser where UserId="
//						+ userid
//						+ ") or parent_id=(SELECT parentid from t_loginuser where UserId="
//						+ userid + ")) and del_flag!=-1";
//			} else {
//				sql = "SELECT dept_id as id,`name` as chessname from sys_dept where dept_id=(SELECT parentid from t_loginuser where UserId="
//						+ userid + ") and del_flag!=-1";
//			}
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			Map<String, Object> map = null;
//			while (rs.next()) {
//				map = new HashMap<>();
//				map.put("id", rs.getInt("id"));
//				map.put("chessname", rs.getString("chessname"));
//				list.add(map);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	/**
//	 * 获取用户角色
//	 *
//	 * @return
//	 * @throws Exception
//	 */
//	private List<Integer> GetUserRole(int userid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<Integer> list = new ArrayList<>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "select role_id from sys_user_role where user_id=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, userid);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				list.add(rs.getInt(1));
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public List<Map<String, Object>> GetClassList(int chessid, int userid)
//			throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<Map<String, Object>> list = new ArrayList<>();
//		List<Integer> roleidlist = GetUserRole(userid);
//		try {
//			conn = DBPool.getInstance().getConnection();
//			if (roleidlist.contains(2) || roleidlist.contains(3)) {
//				sql = "SELECT id,classname from t_class where ParentId="
//						+ chessid + " and `status`!=-1";
//			} else {
//				sql = "SELECT id,classname from t_class where ParentId="
//						+ chessid + " and `status`!=-1 and teacherid=" + userid
//						+ "";
//			}
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			Map<String, Object> map = null;
//			while (rs.next()) {
//				map = new HashMap<>();
//				map.put("id", rs.getInt("id"));
//				map.put("classname", rs.getString("classname"));
//				list.add(map);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public int UpdateMatch(MatchInfo model) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "update t_match set matchname=?,match_date=?,match_enddate=?,apply_starttime=?,apply_endtime=?,max_round=?,match_ruler=?,match_type=?,level_lower=?,level_upper=?,number_upper=?,match_content=?,number_lower=?,applytype=?,addtime=now() where id=? and status=0";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, model.getMatchname());
//			pstmt.setDate(2, new java.sql.Date(model.getMatchDate().getTime()));
//			pstmt.setDate(3, new java.sql.Date(model.getMatchEndDate()
//					.getTime()));
//			pstmt.setTimestamp(4, model.getApplystarttime());
//			pstmt.setTimestamp(5, model.getApplyendtime());
//			pstmt.setInt(6, model.getMaxRound());
//			pstmt.setString(7, model.getRuler());
//			pstmt.setInt(8, model.getMatchtype());
//			pstmt.setInt(9, model.getLevel_lower());
//			pstmt.setInt(10, model.getLevel_upper());
//			pstmt.setInt(11, model.getNumber_upper());
//			pstmt.setString(12, model.getMatch_content());
//			pstmt.setInt(13, model.getNumber_lower());
//			pstmt.setInt(14, model.getApplytype());
//			pstmt.setInt(15, model.getId());
//			res = pstmt.executeUpdate();
//			if (res > 0) {
//				if (model.getRoundtimelist() != null) {
//					if (model.getRoundtimelist().size() > 0) {
//						UpdateMatchRoundTime(model.getId(),
//								model.getRoundtimelist());
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public int UpdateMatchStatus(int matchid, int status) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "update t_match set status=? where id=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, status);
//			pstmt.setInt(2, matchid);
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public MatchInfo MatchMangermentDetail(int matchid) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		MatchInfo model = new MatchInfo();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT  *  from t_match where id=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				model.setId(rs.getInt("id"));
//				model.setMatchname(rs.getString("matchname"));
//				model.setMatchDate(rs.getDate("match_date"));
//				model.setMatchEndDate(rs.getDate("match_enddate"));
//				model.setApplyendtime(rs.getTimestamp("apply_endtime"));
//				model.setApplystarttime(rs.getTimestamp("apply_starttime"));
//				model.setLevel_lower(rs.getInt("level_lower"));
//				model.setLevel_upper(rs.getInt("level_upper"));
//				model.setStatus(rs.getInt("status"));
//				model.setMatch_content(rs.getString("match_content"));
//				model.setMaxRound(rs.getInt("max_round"));
//				model.setType(rs.getInt("type"));
//				model.setMatchtype(rs.getInt("match_type"));
//				model.setRuler(rs.getString("match_ruler"));
//				model.setNumber_lower(rs.getInt("number_lower"));
//				model.setNumber_upper(rs.getInt("number_upper"));
//				model.setApplytype(rs.getInt("applytype"));
//				model.setMatchUserList(GetMatchAPPlyUserList(matchid));
//				model.setRoundtimelist(GetMatchRoundTimeList(matchid));
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return model;
//	}
//
//	/**
//	 * 获取赛事报名名单
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @return
//	 * @throws Exception
//	 */
//	private List<Applyform> GetMatchAPPlyUserList(int matchid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<Applyform> list = new ArrayList<>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT  a.organization,a.userid,a.realname,(CASE WHEN b.`level` is NULL THEN '25级' ELSE b.`level` END)as `level` from t_applyform a LEFT JOIN t_user b on a.userid=b.UserId WHERE a.matchid=? and a.userid!=9191";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			Applyform model = null;
//			while (rs.next()) {
//				model = new Applyform();
//				model.setUserid(rs.getInt("userid"));
//				model.setRealname(rs.getString("realname"));
//				model.setLevel(rs.getString("level"));
//				model.setOrganization(rs.getString("organization"));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	/**
//	 * 获取赛事轮次信息列表
//	 *
//	 * @param matchid
//	 *            赛事id
//	 * @return
//	 * @throws Exception
//	 */
//	private List<Map<String, Object>> GetMatchRoundTimeList(int matchid)
//			throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<Map<String, Object>> list = new ArrayList<>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT * from t_match_round where matchid=? order by round asc";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			Map<String, Object> map = null;
//			while (rs.next()) {
//				map = new HashMap<>();
//				map.put("id", String.valueOf(rs.getInt("id")));
//				map.put("round", String.valueOf(rs.getInt("round")));
//				map.put("roundtime", rs.getTimestamp("roundtime"));
//				list.add(map);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public int UpdateMatchRoundTime(int id, String roundtime) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "update t_match_round set roundtime=? where id=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setTimestamp(1, Timestamp.valueOf(roundtime));
//			pstmt.setInt(2, id);
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public int UpdateMatchApplyform(int matchid, String userid, int status,
//			int type, String realname, String organization) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		if (status != 0 || status != 3) {
//			return 0;
//		}
//		String[] useridarr = userid.split(",");
//		if (useridarr.length == 0) {
//			return 0;
//		}
//		try {
//			conn = DBPool.getInstance().getConnection();
//			if (type == 1) { // 剔除
//				sql = "delete from t_applyform where matchid=" + matchid
//						+ " and userid in(" + Integer.parseInt(useridarr[0])
//						+ "";
//				for (int i = 1; i < useridarr.length; i++) {
//					sql += "," + Integer.parseInt(useridarr[i]) + "";
//				}
//				sql += ")";
//				pstmt = conn.prepareStatement(sql);
//				res = pstmt.executeUpdate();
//			} else if (type == 2) { // 增加
//				int maxnum = GetMatchMaxNum(matchid);
//				String[] namearr = realname.split(",");
//				String[] orgarr = organization.split(",");
//				if (maxnum > 0) {
//					for (int i = 0; i < useridarr.length; i++) {
//						sql = "insert into t_applyform(uuid,userid,matchid,realname,organization,num,addtime) values('"
//								+ java.util.UUID.randomUUID().toString()
//								+ "',"
//								+ Integer.parseInt(useridarr[i])
//								+ ","
//								+ matchid
//								+ ",'"
//								+ namearr[i]
//								+ "','"
//								+ orgarr[i] + "'," + (maxnum + i) + ",now())";
//						pstmt = conn.prepareStatement(sql);
//						res = pstmt.executeUpdate();
//					}
//				} else {
//					sql = "insert into t_applyform(uuid,userid,matchid,realname,organization,num,addtime) values('"
//							+ java.util.UUID.randomUUID().toString()
//							+ "',"
//							+ Integer.parseInt(useridarr[0])
//							+ ","
//							+ matchid
//							+ ",'"
//							+ namearr[0]
//							+ "','"
//							+ orgarr[0]
//							+ "',0,now())";
//					for (int i = 1; i < useridarr.length; i++) {
//						sql += ",('" + java.util.UUID.randomUUID().toString()
//								+ "'," + Integer.parseInt(useridarr[i]) + ","
//								+ matchid + ",'" + namearr[i] + "','"
//								+ orgarr[i] + "',0,now())";
//					}
//					pstmt = conn.prepareStatement(sql);
//					res = pstmt.executeUpdate();
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	/**
//	 * 获取赛事最大抽签号
//	 *
//	 * @param matchid
//	 * @return
//	 * @throws Exception
//	 */
//	private int GetMatchMaxNum(int matchid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT IFNULL(MAX(Num),0)  from t_applyform where matchid=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				res = rs.getInt(1);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public List<Applyform> GetMacthUserList(int matchid) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<Applyform> list = new ArrayList<>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT  a.*,d.applytype,(case when c.classname is null then '' else c.classname end)as classname from t_applyform a LEFT JOIN (SELECT * from t_school_class_student where classid!=0 ORDER BY id desc) b on a.userid=b.studentid LEFT JOIN t_class c on b.classid=c.id   LEFT JOIN t_match d on a.matchid=d.id where a.matchid=? and a.userid!=9191 GROUP BY a.userid ORDER BY a.Num ASC,CONVERT(a.realname USING gbk) ";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			Applyform model = null;
//			while (rs.next()) {
//				model = new Applyform();
//				model.setUserid(rs.getInt("userid"));
//				model.setRealname(rs.getString("realname"));
//				if (rs.getInt("applytype") == 1) {
//					model.setOrganization(rs.getString("classname"));
//				} else {
//					model.setOrganization(rs.getString("organization"));
//				}
//				model.setNum(rs.getInt("num"));
//				model.setAddtime(rs.getDate("addtime"));
//				model.setLevel(rs.getString("level"));
//				model.setAddTime(rs.getString("addtime").substring(0, 19));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public List<MatchRank> GetMatchRanking(int matchid) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int opponentscore = 0;
//		List<MatchRank> list = new ArrayList<MatchRank>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			// sql =
//			// "SELECT a.*,b.opponent,b.totalscore,b.round from(SELECT matchid,groupid,userid,realname as username,Num from t_applyform where userid!=9191 and matchid="
//			// + matchid
//			// + " and groupid="
//			// + groupid
//			// +
//			// ") a LEFT JOIN (SELECT match_id,group_id,userid,opponent,totalscore,round FROM t_schedule where match_id="
//			// + matchid
//			// + " and group_id="
//			// + groupid
//			// +
//			// ") b on  a.userid=b.userid   WHERE b.round=(SELECT MAX(round) from t_schedule where match_id="
//			// + matchid
//			// + " and group_id="
//			// + groupid
//			// + ") ORDER BY b.totalscore DESC";
//			sql = "SELECT a.organization,a.userid,a.realname as username,b.totalscore,b.opponentscore,b.num from  t_applyform  a LEFT JOIN v_schedule_make b on a.matchid=b.matchid  and a.userid=b.userid WHERE a.matchid="
//					+ matchid
//					+ "  and a.userid!=9191 and b.round=(SELECT MAX(round) from t_schedule where match_id="
//					+ matchid + ")";
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			MatchRank model = null;
//			while (rs.next()) {
//				model = new MatchRank();
//				model.setUserid(rs.getInt("userid"));
//				model.setNum(rs.getInt("num"));
//				model.setRealname(rs.getString("username"));
//				model.setTotalscore(rs.getInt("totalscore"));
//				// opponentscore = GetOpponentScore(matchid, groupid,
//				// rs.getInt("opponent"));
//				model.setOpponenttotalscore(rs.getInt("opponentscore"));
//				model.setProgressivescore(rs.getInt("totalscore"));
//				model.setOrganization(rs.getString("organization"));
//				model.setRanking(0);
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public int UpdateMatchUser(MatchInfo model) throws Exception {
//		// TODO Auto-generated method stub
//		if (GetMatchStatus(model.getId()) == 0) {
//			return 0;
//		}
//		if (model.getMatchUserList().size() == 0) {
//			return 0;
//		}
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "delete from t_applyform where matchid=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, model.getId());
//			res = pstmt.executeUpdate();
//			res = insertapplyform(model.getId(), model.getMatchUserList());
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	private int GetMatchStatus(int matchid) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "select status from t_match where id=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				if (rs.getInt(1) == 0) {
//					res = 1;
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public List<RefreeOptionInfo> GetRefreeOptionList(String info)
//			throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		List<RefreeOptionInfo> list = new ArrayList<>();
//		JSONObject jsonresult = new JSONObject(info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//		int userid = JsonObj.getInt("userid");
//		int status=0;
//		if(JsonObj.has("status")){
//			status=JsonObj.getInt("status");
//		}
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "SELECT b.f_round as round,b.f_gamename as gamename,b.id as gameid,b.f_blackid as blackid,f_whiteid as whiteid,blackname,whitename,(case when f_memo is null then 0 else f_memo end) as memo,blackLevelName as blacklevel,whiteLevelName as whitelevel,a.`status`,f_result as result,a.addtime as sendtime from minalogs a LEFT JOIN v_gameroom  b on a.spareid=b.Id where a.method=501 and a.toid=? and a.status=? and b.id is not null";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, userid);
//			pstmt.setInt(2, status);
//			rs = pstmt.executeQuery();
//			RefreeOptionInfo model = null;
//			while (rs.next()) {
//				model = new RefreeOptionInfo();
//				model.setGameid(rs.getInt("gameid"));
//				model.setGamename(rs.getString("gamename"));
//				model.setBlackid(rs.getInt("blackid"));
//				model.setWhiteid(rs.getInt("whiteid"));
//				model.setBlackname(rs.getString("blackname"));
//				model.setWhitename(rs.getString("whitename"));
//				model.setBlacklevel(rs.getString("blacklevel"));
//				model.setWhitelevel(rs.getString("whitelevel"));
//				model.setResult(rs.getString("result"));
//				model.setMemo(rs.getInt("memo"));
//				model.setStatus(rs.getInt("status"));
//				model.setSendtime(rs.getString("sendtime").substring(0, 19));
//				model.setRound(rs.getInt("round"));
//				list.add(model);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//}
