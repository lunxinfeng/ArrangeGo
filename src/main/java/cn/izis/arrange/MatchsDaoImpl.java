//package cn.izis.arrange;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.google.gson.Gson;
//import com.mysql.jdbc.Statement;
//
//import cn.izis.dao.MatchsDao;
//import cn.izis.db.ConnDB;
//import cn.izis.db.JdbcHelper;
//import cn.izis.entity.Aeeange;
//import cn.izis.entity.AllArrange;
//import cn.izis.entity.Applyform;
//import cn.izis.entity.GameRoom;
//import cn.izis.entity.Matchs;
//import cn.izis.entity.MemArrange;
//import cn.izis.entity.UserApplyFrom;
//import cn.izis.entity.UserApplyFromMessage;
//import cn.izis.entity.UserSchedule;
//import cn.izis.entity.UserScoreMessage;
//import cn.izis.entity.Useridapply;
//import cn.izis.util.Arrange;
//import cn.izis.util.CurrentTime;
//import cn.izis.db.DBPool;
//
//public class MatchsDaoImpl  implements MatchsDao {
//
//	int result = 0;
//
//	public MatchsDaoImpl() throws Exception {
//		// conn=DBPool.getInstance().getConnection();
//	}
//
//	//===============获取赛事信息列表===========
//	public List<Matchs> findAllMatchs(String strWhere, String orderby,
//			int startIndex, int endIndex) throws Exception {
//		StringBuilder strSql = new StringBuilder();
//		List<Matchs> list = new ArrayList<Matchs>();
//		strSql.append("select * from matchs where  1=1 ");
//
//		if (strWhere != null && strWhere.length() > 0) {
//			strSql.append(" and " + strWhere);
//		}
//
//		if (orderby != null && orderby.length() > 0) {
//			strSql.append(" order by " + orderby);
//		} else {
//			strSql.append(" order by Id desc ");
//		}
//
//		strSql.append(" limit  " + startIndex + " ," + endIndex);
//		System.out.println("strSql" + strSql);
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(strSql.toString());
//			rs = pstmt.executeQuery();
//			Matchs matchs = null;
//			while (rs.next()) {
//				matchs = new Matchs();
//				matchs.setId(rs.getInt("Id"));
//				matchs.setMatchname(rs.getString("matchname"));
//				matchs.setMatchtype(rs.getInt("matchtype"));
//				matchs.setTotalnum(rs.getInt("totalnum"));
//				matchs.setBzxx(rs.getString("bzxx"));
//				matchs.setAdduser(rs.getString("adduser"));
//				matchs.setAddtime(rs.getString("addtime"));
//				matchs.setSummary(rs.getString("summary"));
//				matchs.setBoard_size(rs.getInt("board_size"));
//				matchs.setMatch_time(rs.getString("match_time"));
//				matchs.setMatch_address(rs.getString("match_address"));
//				matchs.setMatch_downlinemsg(rs.getString("match_downlinemsg"));
//				matchs.setMatch_endtime(rs.getString("match_endtime"));
//				matchs.setMatch_paymoney(Double.parseDouble(new DecimalFormat(
//						"#0.00").format(rs.getDouble("match_paymoney"))));
//				matchs.setMatch_zbf(rs.getString("match_zbf"));
//				matchs.setJoinnum(rs.getInt("joinnum"));
//				matchs.setMatch_joinendtime(rs
//						.getTimestamp("match_joinendtime"));
//				matchs.setMatch_paytype(rs.getString("match_paytype"));
//				if (rs.getInt("matchtype") == 2) {
//					matchs.setTeam_a(rs.getString("team_a"));
//					matchs.setTeam_b(rs.getString("team_b"));
//				}
//				list.add(matchs);
//			}
//			return list;
//		} catch (SQLException e) {
//
//			throw new SQLException(e);
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//	}
//
//	@Override
//	public List<Matchs> GetList(String strWhere) throws Exception {
//		StringBuilder strSql = new StringBuilder();
//		List<Matchs> list = new ArrayList<Matchs>();
//		strSql.append("select * from matchs where  1=1 ");
//
//		if (!(strWhere.trim() == null || strWhere.equals(""))) {
//			strSql.append(" and " + strWhere);
//		}
//		strSql.append(" order by addtime desc");
//		System.out.println("strSql" + strSql);
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//
//		try {
//
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(strSql.toString());
//			rs = pstmt.executeQuery();
//			Matchs matchs = null;
//			while (rs.next()) {
//				matchs = new Matchs();
//				matchs.setId(rs.getInt("Id"));
//				matchs.setMatchname(rs.getString("matchname"));
//				matchs.setMatchtype(rs.getInt("matchtype"));
//				matchs.setTotalnum(rs.getInt("totalnum"));
//				matchs.setBzxx(rs.getString("bzxx"));
//				matchs.setAdduser(rs.getString("adduser"));
//				matchs.setAddtime(rs.getString("addtime"));
//				matchs.setSummary(rs.getString("summary"));
//				matchs.setBoard_size(rs.getInt("board_size"));
//				matchs.setMatch_time(rs.getString("match_time"));
//				matchs.setMatch_address(rs.getString("match_address"));
//				matchs.setMatch_downlinemsg(rs.getString("match_downlinemsg"));
//				matchs.setMatch_endtime(rs.getString("match_endtime"));
//				matchs.setMatch_paymoney(Double.parseDouble(new DecimalFormat(
//						"#0.00").format(rs.getDouble("match_paymoney"))));
//				matchs.setMatch_zbf(rs.getString("match_zbf"));
//				matchs.setJoinnum(rs.getInt("joinnum"));
//				matchs.setMatch_joinendtime(rs
//						.getTimestamp("match_joinendtime"));
//				matchs.setMatch_paytype(rs.getString("match_paytype"));
//				if (rs.getInt("matchtype") == 2) {
//					matchs.setTeam_a(rs.getString("team_a"));
//					matchs.setTeam_b(rs.getString("team_b"));
//				}
//				list.add(matchs);
//			}
//			return list;
//		} catch (SQLException e) {
//			throw new SQLException(e);
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//	}
//
//	@Override
//	public int AddMatch(String info) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "";
//		int res = 0;
//		JSONObject jsonresult = new JSONObject(info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//		String matchid = JsonObj.getString("matchid").toString();
//		String matchname = JsonObj.getString("matchname").toString();
//		String totalnum = JsonObj.getString("totalnum").toString();
//		String bzxx = JsonObj.getString("bzxx").toString();
//		String summary = JsonObj.getString("summary").toString();
//		String adduser = JsonObj.getString("adduser").toString();
//		String matchtype = JsonObj.getString("matchtype").toString();
//		String team_a = JsonObj.getString("team_a").toString();
//		String team_b = JsonObj.getString("team_b").toString();
//		String boardSize = JsonObj.getString("boardSize").toString();
//		String timing_system = JsonObj.getString("timing_system").toString();
//		String period = JsonObj.getString("period").toString();
//		String set_stop_time = JsonObj.getString("set_stop_time").toString();
//		String initial_time = JsonObj.getString("initial_time").toString();
//		String match_time = JsonObj.getString("match_time").toString();
//		String match_address = JsonObj.getString("match_address");
//		if (period.equals("")) {
//			period = "0";
//		}
//		if (matchid.equals("0")) {
//			sql = "insert into matchs(matchname,matchtype,totalnum,bzxx"
//					+ ",adduser,summary,team_a,team_b,board_size,"
//					+ "timing_system,period,set_stop_time,initial_time,"
//					+ "match_time,match_address,addtime)"
//					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
//		} else {
//			sql = "update  matchs set matchname=?,matchtype=?,totalnum=?,bzxx=?"
//					+ ",adduser=?,summary=?,team_a=?,team_b=?,board_size=?,"
//					+ "timing_system=?,period=?,set_stop_time=?,initial_time=?,"
//					+ "match_time=?,match_address=? where Id="
//					+ Integer.parseInt(matchid);
//		}
//
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, matchname);
//			pstmt.setInt(2, Integer.parseInt(matchtype));
//			pstmt.setInt(3, Integer.parseInt(totalnum));
//			pstmt.setString(4, bzxx);
//			pstmt.setString(5, adduser);
//			pstmt.setString(6, summary);
//			pstmt.setString(7, team_a);
//			pstmt.setString(8, team_b);
//			pstmt.setInt(9, Integer.parseInt(boardSize));
//			pstmt.setInt(10, Integer.parseInt(timing_system));
//			pstmt.setInt(11, Integer.parseInt(period));
//			pstmt.setInt(12, Integer.parseInt(set_stop_time));
//			pstmt.setInt(13, Integer.parseInt(initial_time));
//			pstmt.setString(14, match_time);
//			pstmt.setString(15, match_address);
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			res = 0;
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public int AddApply(String info) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			JSONObject jsonresult = new JSONObject(info);
//			JSONArray array = jsonresult.getJSONArray("root");
//			JSONObject JsonObj = array.getJSONObject(0);
//			String userid = JsonObj.getString("userid").toString();
//			String realname = JsonObj.getString("realname").toString();
//			String matchid = JsonObj.getString("matchid").toString();
//			String team_sel = JsonObj.getString("team");
//			// if (team_sel.equals("")) {
//			// team_sel="null";
//			// }
//			String sql = "insert into applyform(userid,realname,matchid,status,addtime,team) values(?,?,?,?,CURRENT_TIMESTAMP,?)";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, Integer.parseInt(userid));
//			pstmt.setString(2, realname);
//			pstmt.setInt(3, Integer.parseInt(matchid));
//			pstmt.setInt(4, 0);
//			pstmt.setString(5, team_sel);
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			res = 0;
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public String GetUserApplyInfo(String info, String yhnm) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String json = "";
//		int res = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			JSONObject jsonresult = new JSONObject(info);
//			JSONArray array = jsonresult.getJSONArray("root");
//			JSONObject JsonObj = array.getJSONObject(0);
//			String matchid = JsonObj.getString("matchid");
//			if (matchid != null && !matchid.equals("")) {
//				String sql = "select * from applyform where userid=? and matchid=?";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, Integer.parseInt(yhnm));
//				pstmt.setInt(2, Integer.parseInt(matchid));
//				rs = pstmt.executeQuery();
//				if (rs.first()) {
//					json = "{\"root\":[{\"status\":\"" + rs.getString("status")
//							+ "\"}]}";
//					return json;
//
//				} else {
//					json += "{root:[{";
//					json += "status" + ":'" + "-1" + "'";
//					json += "team" + ":'" + "" + "'";
//					json += "}]}";
//					return json;
//				}
//			} else {
//				String id = JsonObj.getString("id");
//				String sql = "select * from applyform where Id=?";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, Integer.parseInt(id));
//				rs = pstmt.executeQuery();
//				if (rs.next()) {
//					json += "{root:[{";
//					json += "status" + ":'" + rs.getString("status") + "'";
//					json += "team" + ":'" + rs.getString("team") + "'";
//					json += "}]}";
//					return json;
//				} else {
//					json += "{root:[{";
//					json += "status" + ":'" + "-1" + "'";
//					json += "team" + ":'" + rs.getString("team") + "'";
//					json += "}]}";
//					return json;
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			json = "";
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//	@Override
//	public String SetGameResultByReferee(String info, String yhnm)
//			throws Exception {
//		JSONArray arrays;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int result = 0;
//		String StrResult = "";
//
//		JSONObject jsonresult = new JSONObject(info);
//		JSONArray array = jsonresult.getJSONArray("root");
//		JSONObject JsonObj = array.getJSONObject(0);
//
//		try {
//			conn = DBPool.getInstance().getConnection();
//			String id = JsonObj.getString("id");
//			String sscore = JsonObj.getString("sscore");
//			String oscore = JsonObj.getString("oscore");
//
//			if (JsonObj.has("result")) {
//				StrResult = JsonObj.getString("result");
//			}
//
//			String winner = "";
//			String winmessage = "";
//			String looser = "";// 认输的人自然是失败者。
//
//			String strSelect = "select * from v_gameroom where id=" + id;
//			pstmt = conn.prepareStatement(strSelect);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				if (rs.getString("f_state").equals("1"))
//					return "false"; // 对方已经认输了。
//
//				String blackname = rs.getString("blackname");
//				String whitename = rs.getString("whitename");
//
//				String f_black = rs.getString("f_blackid");
//
//				String f_white = rs.getString("f_whiteid");
//
//				String gamename = rs.getString("f_gamename");
//
//				String round = rs.getString("f_round");
//
//				if (round != null && !round.equals(""))
//					gamename = gamename + "【第" + round + "轮】";
//				String f_gamename = gamename;
//
//				String f_name = rs.getString("f_roomname");
//
//				String f_num = rs.getString("f_num");
//
//				String f_position = rs.getString("f_position");
//
//				String f_allstep = rs.getString("f_allstep");
//
//				String f_degreeofopenness = rs.getString("f_degreeofopenness");
//				if (sscore.equals("2")) {
//					winmessage = blackname + "（黑）胜，" + StrResult; // + whitename
//																	// + "（白）负。"
//					winner = f_black;
//					looser = f_white;
//
//				} else if (sscore.equals("1")) {
//					winmessage = blackname + "（黑）VS" + whitename + "（白），平局";
//					winner = f_black + "@" + f_white;
//					looser = "0";
//				} else if (sscore.equals("0")) {
//					if (oscore.equals("2")) {
//						winmessage = whitename + "（白）胜，" + StrResult; // +
//																		// blackname
//																		// +
//																		// "（黑）负。"
//						winner = f_white;
//						looser = f_black;
//					} else {
//						winmessage = "无效成绩";
//						winner = "0";
//						looser = "0";
//					}
//
//				}
//				// if (looser.equals(f_black)) {
//				// winner = f_white;
//				// winmessage = whitename + "（白）胜，" + blackname + "（黑）负。";
//				// } else {
//				// winner = f_black;
//				// winmessage = blackname + "（黑）胜，" + whitename + "（白）负。";
//				// }
//
//				String f_sz = rs.getString("f_roomnum");
//				String f_result = winmessage;
//				String f_memo = winner;
//				String f_matchid = rs.getString("f_matchid");
//				String f_round = rs.getString("f_round");
//
//				List<String> SQLStringList = new ArrayList<String>();
//
//				String updateStr = "update t_gameroom set f_state=1 , f_result='"
//						+ winmessage
//						+ "',f_memo='"
//						+ winner
//						+ "' where id="
//						+ id;
//				String insertStr = "insert into t_usergame(f_gamename,f_name,f_num,f_position,f_allstep,f_black,f_white,f_result,f_memo,f_sz,f_degreeofopenness) values('"
//						+ f_gamename
//						+ "','"
//						+ f_name
//						+ "',"
//						+ f_num
//						+ ",'"
//						+ f_position
//						+ "','"
//						+ f_allstep
//						+ "','"
//						+ f_black
//						+ "','"
//						+ f_white
//						+ "','"
//						+ f_result
//						+ "','"
//						+ f_memo
//						+ "','" + f_sz + "'," + f_degreeofopenness + ")";
//
//				SQLStringList.add(updateStr);
//				SQLStringList.add(insertStr);
//
//				// 自动添加积分信息，如果是比赛。【将来可扩展，普通对弈计算积分】
//				if (f_matchid != null && !f_matchid.equals("")
//						&& f_round != null && !f_round.equals("")) {
//					String updateStr1 = "";
//					String updateStr2 = "";
//
//					updateStr1 = " update schedule set score=2,totalscore=totalscore+2 where matchId="
//							+ f_matchid
//							+ " and round="
//							+ f_round
//							+ " and userid=" + winner;
//					updateStr2 = " update schedule set score=0 where matchId="
//							+ f_matchid + " and round=" + f_round
//							+ " and userid=" + looser;
//
//					SQLStringList.add(updateStr1);
//					SQLStringList.add(updateStr2);
//
//				}
//
//				int iCount = JdbcHelper.batchUpdate(SQLStringList)[0];
//
//				if (iCount > 0) {
//					return "true";
//				} else {
//					return "false";
//				}
//
//			}
//
//			else {
//				return "false";
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//		return "";
//	}
//
//	@Override
//	public String GetMatchInfo(String Info) {
//		// TODO Auto-generated method stub
//		JSONObject jsonresult;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int result = 0;
//		String json = "";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			jsonresult = new JSONObject(Info);
//			JSONArray array = jsonresult.getJSONArray("root");
//			JSONObject JsonObj = array.getJSONObject(0);
//			String id = JsonObj.getString("id");
//			String sql = "select * from matchs where Id=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, Integer.parseInt(id));
//			rs = pstmt.executeQuery();
//			json += "{root:[{";
//			while (rs.next()) {
//				json += "\"Id\":\"" + rs.getInt("Id");
//				json += "\",\"matchname\":\"" + rs.getString("matchname");
//				json += "\",\"totalnum\":\"" + rs.getInt("totalnum");
//				json += "\",\"bzxx\":\"" + rs.getInt("bzxx");
//				json += "\",\"summary\":\"" + rs.getString("summary");
//				json += "\",\"matchtype\":\"" + rs.getInt("matchtype");
//				json += "\",\"boardSize\":\"" + rs.getInt("board_size");
//				json += "\",\"timing_system\":\"" + rs.getInt("timing_system");
//				json += "\",\"period\":\"" + rs.getInt("period");
//				json += "\",\"set_stop_time\":\"" + rs.getInt("set_stop_time");
//				json += "\",\"initial_time\":\"" + rs.getInt("initial_time");
//				json += "\",\"match_time\":\"" + rs.getString("match_time");
//				json += "\",\"match_address\":\""
//						+ rs.getString("match_address") + "\"";
//			}
//			json += "}]}";
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//	@Override
//	public String UpdateApply(String Info, String yhnm) {
//		// TODO Auto-generated method stub
//		JSONObject jsonresult;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		int res1 = 0;
//		int status = 0;
//		try {
//			conn = DBPool.getInstance().getConnection();
//			jsonresult = new JSONObject(Info);
//			JSONArray array = jsonresult.getJSONArray("root");
//			JSONObject JsonObj = array.getJSONObject(0);
//			int id = Integer.parseInt(JsonObj.getString("id"));
//			status = Integer.parseInt(JsonObj.getString("status"));
//			String sql = "update applyform set status=? where id=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, status);
//			pstmt.setInt(2, id);
//			res = pstmt.executeUpdate();
//			if (status == -1) {
//				int matchid = Integer.parseInt(JsonObj.getString("matchid"));
//				res1 = pstmt
//						.executeUpdate("update matchs set joinnum=joinnum-1 where Id="
//								+ matchid);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		if (res > 0 && res1 > 0 && status == -1) {
//			return "true";
//		} else if (res > 0 && res1 == 0 && status != -1) {
//			return "true";
//		} else {
//			return "false";
//		}
//	}
//
//	@Override
//	public List<Useridapply> GetUserApplyList(String Info, String yhnm) {
//		// TODO Auto-generated method stub
//		JSONObject jsonresult;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String strSql = " 1=1 ";
//		int pageIndex = 0;
//		int pageSize = 0;
//		List<Useridapply> finapply = new ArrayList<Useridapply>();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			jsonresult = new JSONObject(Info);
//			JSONArray array = jsonresult.getJSONArray("root");
//			JSONObject JsonObj = array.getJSONObject(0);
//			String xxtype = JsonObj.getString("xxtype");
//			String user_name = JsonObj.getString("uname");
//			String match_name = JsonObj.getString("mname");
//			pageIndex = JsonObj.getInt("startIndex");
//			pageSize = JsonObj.getInt("pageSize");
//
//			String sql = "select f_userwight from t_user where f_userid=?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, Integer.parseInt(yhnm));
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				String Str = rs.getString("f_userwight");
//				if (Str.lastIndexOf("#debug") > 0) // 系统管理员
//				{
//
//				} else if (Str.lastIndexOf("#matchdebug") > 0) {
//					strSql += " and adduser=" + yhnm;
//				} else {
//					return finapply; // 无权限者
//				}
//				if (xxtype.length() > 0) {
//					strSql += " and status= " + xxtype;
//				}
//				if (user_name != null && !user_name.equals("")) {
//					strSql += " and realname= '" + user_name + "' ";
//				}
//				if (match_name != null && !match_name.equals("")) {
//					strSql += " and  matchname= '" + match_name + "'";
//				}
//				finapply = finapply(strSql, " addtime desc", pageSize
//						* (pageIndex - 1), pageSize);
//
//			} else {
//				return finapply;
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//		return finapply;
//	}
//
//	public List<Useridapply> finapply(String strWhere, String orderby,
//			int startIndex, int endIndex) {
//		String json = "";
//		StringBuilder strSql = new StringBuilder();
//		List<Useridapply> list = new ArrayList<Useridapply>();
//		strSql.append("select * from v_userapply where  1=1 ");
//		if (!(strWhere.trim() == null || strWhere.equals(""))) {
//			strSql.append(" and " + strWhere);
//		}
//
//		if (!(orderby.trim() == null || orderby.equals(""))) {
//			strSql.append(" order by " + orderby);
//		} else {
//			strSql.append(" order by Id desc ");
//		}
//		strSql.append(" limit  " + startIndex + " ," + endIndex);
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(strSql.toString());
//			rs = pstmt.executeQuery();
//			Useridapply useridapply = null;
//			while (rs.next()) {
//				useridapply = new Useridapply();
//				useridapply.setId(rs.getInt("Id"));
//				useridapply.setMatchid(rs.getInt("matchid"));
//				useridapply.setBzxx(rs.getInt("bzxx"));
//				useridapply.setMatchname(rs.getString("matchname"));
//				useridapply.setAddtime(rs.getString("addtime"));
//				useridapply.setStatus(rs.getInt("status"));
//				useridapply.setRealname(rs.getString("realname"));
//				useridapply.setTeam(rs.getString("team"));
//				list.add(useridapply);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//
//	}
//
//	@Override
//	public String findMatchBZXX(String Info) {
//		// TODO Auto-generated method stub
//		JSONObject jsonresult;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int result = 0;
//		String json = "";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			jsonresult = new JSONObject(Info);
//			JSONArray array = jsonresult.getJSONArray("root");
//			JSONObject JsonObj = array.getJSONObject(0);
//			String bzxx = JsonObj.getString("bzxx");
//			String sql = "select * from matchs where bzxx=? order by addtime desc";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, Integer.parseInt(bzxx));
//			rs = pstmt.executeQuery();
//			json += "{\"root\":[";
//			if (rs.next()) {
//				json += "{\"Id\":\"" + rs.getString("Id");
//				json += "\",\"matchname\":\"" + rs.getString("matchname")
//						+ "\"}";
//			}
//			json += "]}";
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//	public int p_findallMatchsNumber(String keyword) {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int number = 0;
//		String sql = "";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			if (keyword == null || keyword == "")
//			// if(keyword.equals(null))
//			{
//				sql = "select count(*) from matchs ";
//				pstmt = conn.prepareStatement(sql);
//			} else {
//				sql = "select count(*) from matchs where matchname like ?  ";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, "%" + keyword + "%");
//			}
//			rs = pstmt.executeQuery();
//			rs.next();
//			number = rs.getInt(1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return number;
//	}
//
//	public List<Matchs> p_findAllMatch(String keyword, int currpage,
//			int pagesize) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		List<Matchs> list = new ArrayList<Matchs>();
//		String sql = "";
//		if (currpage == 0) {
//			currpage = 1;
//		}
//		try {
//			conn = DBPool.getInstance().getConnection();
//			if (keyword == null) {
//				sql = "select * from matchs  order by addtime desc limit "
//						+ (currpage - 1) * pagesize + "," + pagesize + "  ";
//				pstmt = conn.prepareStatement(sql);
//				rs = pstmt.executeQuery();
//			} else {
//				sql = "select * from matchs  where matchname like ?  order by addtime desc limit "
//						+ (currpage - 1) * pagesize + "," + pagesize + " ";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, "%" + keyword + "%");
//				rs = pstmt.executeQuery();
//			}
//			Matchs matchs = null;
//			while (rs.next()) {
//				matchs = new Matchs();
//				matchs.setId(rs.getInt("Id"));
//				matchs.setMatchname(rs.getString("matchname"));
//				matchs.setMatchtype(rs.getInt("matchtype"));
//				matchs.setTotalnum(rs.getInt("totalnum"));
//				matchs.setBzxx(rs.getString("bzxx"));
//				matchs.setAdduser(rs.getString("adduser"));
//				matchs.setAddtime(rs.getString("addtime"));
//				matchs.setSummary(rs.getString("summary"));
//				matchs.setTeam_a(rs.getString("team_a"));
//				matchs.setTeam_b(rs.getString("team_b"));
//				matchs.setBoard_size(rs.getInt("board_size"));
//				matchs.setMatch_time(rs.getString("match_time"));
//				matchs.setMatch_address(rs.getString("match_address"));
//				list.add(matchs);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	public int deleteMatchById(int id) throws Exception {
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 1;
//
//		String sql = "delete from matchs where Id=?";
//
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			res = 0;
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	public int addOfMatchs(Matchs matchs) throws Exception {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 1;
//		CurrentTime currentTime = new CurrentTime();
//		String time = currentTime.Time();
//		try {
//			conn = DBPool.getInstance().getConnection();
//			String sql = "insert into matchs(matchname,matchtype,totalnum,bzxx,summary,adduser,addtime,match_time,match_address,board_size)"
//					+ " values(?,?,?,?,?,?,?,?,?,?)";
//			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//			pstmt.setString(1, matchs.getMatchname());
//			pstmt.setInt(2, matchs.getMatchtype());
//			pstmt.setInt(3, matchs.getTotalnum());
//			pstmt.setString(4, matchs.getBzxx());
//			pstmt.setString(5, matchs.getSummary());
//			pstmt.setString(6, matchs.getAdduser());
//			pstmt.setString(7, time);
//			pstmt.setString(8, matchs.getMatch_time());
//			pstmt.setString(9, matchs.getMatch_address());
//			pstmt.setInt(10, matchs.getBoard_size());
//			res = pstmt.executeUpdate();
//			rs = pstmt.getGeneratedKeys();
//			if (rs.next()) {
//				res = rs.getInt(1);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			res = 0;
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	public Matchs findMatchsById(int id) {
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		Matchs matchs = new Matchs();
//		matchs.setId(id);
//		String sql = "select * from matchs where Id=?";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				matchs.setId(rs.getInt("Id"));
//				matchs.setMatchname(rs.getString("matchname"));
//				matchs.setTotalnum(rs.getInt("totalnum"));
//				matchs.setBzxx(rs.getString("bzxx"));
//				matchs.setAdduser(rs.getString("adduser"));
//				matchs.setAddtime(rs.getString("addtime"));
//				matchs.setSummary(rs.getString("summary"));
//				matchs.setMatch_time(rs.getString("match_time"));
//				matchs.setMatch_address(rs.getString("match_address"));
//				matchs.setMatchtype(rs.getInt("matchtype"));
//				matchs.setBoard_size(rs.getInt("board_size"));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//		return matchs;
//	}
//
//	public int updateOfMatchs(Matchs matchs) throws Exception {
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 1;
//
//		try {
//			conn = DBPool.getInstance().getConnection();
//			if (matchs.getTiming_system() == 0) {
//				String sql = "update matchs set matchname=?,totalnum=?,bzxx=?,summary=?,match_time=?,match_address=? where Id=?";
//
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, matchs.getMatchname());
//				pstmt.setInt(2, matchs.getTotalnum());
//				pstmt.setString(3, matchs.getBzxx());
//				pstmt.setString(4, matchs.getSummary());
//				pstmt.setString(5, matchs.getMatch_time());
//				pstmt.setString(6, matchs.getMatch_address());
//				pstmt.setInt(7, matchs.getId());
//			} else if (matchs.getTiming_system() == 1) {
//				String sql = "update matchs set matchname=?,totalnum=?,bzxx=?,summary=?,match_time=?,match_address=? where Id=?";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, matchs.getMatchname());
//				pstmt.setInt(2, matchs.getTotalnum());
//				pstmt.setString(3, matchs.getBzxx());
//				pstmt.setString(4, matchs.getSummary());
//				pstmt.setString(5, matchs.getMatch_time());
//				pstmt.setString(6, matchs.getMatch_address());
//				pstmt.setInt(7, matchs.getId());
//			}
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			res = 0;
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	public int findallApplyFormNumber(int matchid, int status) {
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int number = 0;
//		String sql = "";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			if (status == 3) {
//				sql = "select count(*) from applyform where matchid=?";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, matchid);
//			} else {
//				sql = "select count(*) from applyform where matchid=? and status = ?";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, matchid);
//				pstmt.setInt(2, status);
//			}
//			rs = pstmt.executeQuery();
//			rs.next();
//			number = rs.getInt(1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return number;
//	}
//
//	public List<Applyform> findAllApplyForm(int matchid, int status,
//			int currpage, int pagesize) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		List<Applyform> list = new ArrayList<Applyform>();
//		String sql = "";
//		if (currpage == 0) {
//			currpage = 1;
//		}
//		try {
//			conn = DBPool.getInstance().getConnection();
//			if (status == 3) {
//				sql = "select * from applyform where matchid=?  order by addtime desc limit "
//						+ (currpage - 1) * pagesize + "," + pagesize + "  ";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, matchid);
//				rs = pstmt.executeQuery();
//			} else {
//				sql = "select * from applyform  where matchid=? and status=? order by addtime desc limit "
//						+ (currpage - 1) * pagesize + "," + pagesize + " ";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, matchid);
//				pstmt.setInt(2, status);
//				rs = pstmt.executeQuery();
//			}
//			Applyform applyform = null;
//			while (rs.next()) {
//				applyform = new Applyform();
//				applyform.setId(rs.getInt("Id"));
//				applyform.setUserid(rs.getInt("userid"));
//				applyform.setAddTime(rs.getString("addtime"));
//				applyform.setRealname(rs.getString("realname"));
//				applyform.setMatchid(rs.getInt("matchid"));
//				applyform.setStatus(rs.getInt("status"));
//				list.add(applyform);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	public int updateApplyForm(int id, int status) {
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 1;
//		String sql = "update applyform set status=? where Id=?";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, status);
//			pstmt.setInt(2, id);
//			res = pstmt.executeUpdate();
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public int UpLoadFile(String filename, String bytestr) {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String sql = "insert into t_talk_file(filename,bytestr) value(?,?) ";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, filename);
//			pstmt.setString(2, bytestr);
//			res = pstmt.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	public int addApplyManager(Applyform applyform) {
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String sql = "insert into applyform values(0,?,?,?,?,0,1,'')";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, applyform.getUserid());
//			pstmt.setString(2, applyform.getRealname());
//			pstmt.setInt(3, applyform.getMatchid());
//			pstmt.setString(4, applyform.getAddTime());
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			res = 0;
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	public List<Applyform> findIsApplyForm(int matchid) throws Exception {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		List<Applyform> list = new ArrayList<Applyform>();
//		String sql = "";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			sql = "select * from applyform where matchid=?  order by addtime desc ";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			Applyform applyform = null;
//			while (rs.next()) {
//				applyform = new Applyform();
//				applyform.setId(rs.getInt("Id"));
//				applyform.setUserid(rs.getInt("userid"));
//				applyform.setAddTime(rs.getString("addtime"));
//				applyform.setRealname(rs.getString("realname"));
//				applyform.setMatchid(rs.getInt("matchid"));
//				applyform.setStatus(rs.getInt("status"));
//				list.add(applyform);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	public int deleteApplyFormById(int id) throws Exception {
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 1;
//
//		String sql = "delete  from applyform where Id=?";
//
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			res = pstmt.executeUpdate();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			res = 0;
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public String getByteStr(String filename) {
//		// TODO Auto-generated method stub
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String ByteStr = "";
//		String sql = "select bytestr from t_talk_file where filename=?";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, filename);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				ByteStr = rs.getString("bytestr");
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//		return ByteStr;
//	}
//
//	@Override
//	public int addMatchsRelayGoEX(int matchsid, int timing_system, int period,
//			int set_stop_time, int initial_time, int first_pause,
//			int second_pause, int institutional_time) throws Exception {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String sql = "insert into matchs_relay_ex(matchsid, timing_system,period,set_stop_time,initial_time,first_pause,second_pause,institutional_time) values(?,?,?,?,?,?,?,?)";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchsid);
//			pstmt.setInt(2, timing_system);
//			pstmt.setInt(3, period);
//			pstmt.setInt(4, set_stop_time);
//			pstmt.setInt(5, initial_time);
//			pstmt.setInt(6, first_pause);
//			pstmt.setInt(7, second_pause);
//			pstmt.setInt(8, institutional_time);
//			res = pstmt.executeUpdate();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public String RelayGameRoomMessage(int id) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		GameRoom gr = new GameRoom();
//		String json = "";
//		Gson gson = new Gson();
//		String sql = "select * from v_relay_gameroom where Id=?";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				gr.setId(Integer.parseInt(rs.getString("Id")));
//				gr.setNowtime(rs.getString("nowtime"));
//				gr.setF_allstep(rs.getString("f_allstep"));
//				gr.setF_gamename(rs.getString("f_gamename"));
//				gr.setF_match_state(Integer.parseInt(rs
//						.getString("f_match_state")));
//				gr.setF_num(Integer.parseInt(rs.getString("f_num")));
//				gr.setF_position(rs.getString("f_position"));
//				gr.setF_lastplaytime(rs.getString("f_lastplaytime"));
//				gr.setBlackname(rs.getString("blackname"));
//				gr.setWhitename(rs.getString("whitename"));
//				gr.setF_blackid(Integer.parseInt(rs.getString("f_blackid")));
//				gr.setF_blackleft(Integer.parseInt(rs.getString("f_blackleft")));
//				gr.setF_whiteid(Integer.parseInt(rs.getString("f_whiteid")));
//				gr.setF_whiteleft(Integer.parseInt(rs.getString("f_whiteleft")));
//				gr.setF_result(rs.getString("f_result"));
//				gr.setF_state(Integer.parseInt(rs.getString("f_state")));
//				gr.setF_revisiontime(rs.getString("f_revisiontime"));
//				gr.setF_matchid(Integer.parseInt(rs.getString("matchsid")));
//				gr.setTiming_system(Integer.parseInt(rs
//						.getString("timing_system")));
//				gr.setPeriod(Integer.parseInt(rs.getString("period")));
//				gr.setSet_stop_time(Integer.parseInt(rs
//						.getString("set_stop_time")));
//				gr.setInitial_time(Integer.parseInt(rs
//						.getString("initial_time")));
//				gr.setFirst_pause(Integer.parseInt(rs.getString("first_pause")));
//				gr.setSecond_pause(Integer.parseInt(rs
//						.getString("second_pause")));
//				gr.setInstitutional_time(Integer.parseInt(rs
//						.getString("institutional_time")));
//				json = gson.toJson(gr);
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//	@Override
//	public String RelayChessStep(int id, String laststep, int f_num,
//			String userid) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		GameRoom gr = new GameRoom();
//		String json = "";
//		Gson gson = new Gson();
//		String sql = "select * from v_relay_gameroom where Id=?";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				String Id = rs.getString("Id");
//				String sNum = rs.getString("f_num");
//				String f_allstep = rs.getString("f_allstep").toString();
//				String f_blackid = rs.getString("f_blackid").toString();
//				String f_whiteid = rs.getString("f_whiteid").toString();
//				int iNum = Integer.parseInt(sNum);
//				int database_num = iNum;
//				int req_num = f_num;
//				if (database_num >= req_num + 1) // 说明可能下棋太快，没来得及获取到最新的子，
//				{
//					int ilength = (database_num - req_num) * 5;
//					String allStep = f_allstep;
//					String lastSteps = allStep.substring(allStep.length()
//							- ilength);
//					gr.setF_num(req_num);
//					gr.setF_position(rs.getString("f_position"));
//					gr.setF_lastplaytime(rs.getString("f_lastplaytime"));
//					gr.setF_revisiontime(rs.getString("f_revisiontime"));
//					gr.setNowtime(rs.getString("nowtime"));
//					gr.setF_match_state(Integer.parseInt(rs
//							.getString("f_match_state")));
//					gr.setF_blackleft(Integer.parseInt(rs
//							.getString("f_blackleft")));
//					gr.setF_whiteleft(Integer.parseInt(rs
//							.getString("f_whiteleft")));
//					gr.setF_result(rs.getString("f_result"));
//					json = gson.toJson(gr);
//				} else if (database_num == req_num) {
//					return "";
//				} else if (database_num == req_num - 1) {
//					// 对弈时没有上传成功 执行上传操作。
//
//					if ((f_blackid.equals(userid) && laststep.substring(0, 1)
//							.equals("+"))
//							|| (f_whiteid.equals(userid) && laststep.substring(
//									0, 1).equals("-"))) {
//						String updateStr = "update t_gameroom set f_position='"
//								+ laststep + "',f_allstep=CONCAT(f_allstep,'"
//								+ laststep + "'), f_num=" + f_num
//								+ " where id=" + Id + " and f_num<" + f_num;
//
//						if (JdbcHelper.update(updateStr) > 0) {
//							return "true";
//						} else {
//							return "";
//						}
//					} else {
//						return "";
//					}
//
//				} else {
//					return "";
//				}
//
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//	@Override
//	public String updateGameTime(int gameid, String btime, String wtime) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String json = "";
//		String sql = "update t_gameroom set f_lastplaytime=CURRENT_TIMESTAMP,f_revisiontime=CURRENT_TIMESTAMP,f_blackleft=?,f_whiteleft=? where Id=?";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, btime);
//			pstmt.setString(2, wtime);
//			pstmt.setInt(3, gameid);
//			res = pstmt.executeUpdate();
//			if (res > 0) {
//				json = "true";
//			} else {
//				json = "false";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//	@Override
//	public String findPause(int gaameid, int GoNum) throws Exception {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String json = "";
//		String N = "";
//		List<String> notes = new ArrayList<String>();
//		int res = 0;
//		String sql = "select * from t_relay_schedule_ex where ScheduleId=? and GoNum=?";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, gaameid);
//			pstmt.setInt(2, GoNum);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				N = rs.getString("Notes");
//				notes.add(N);
//			}
//			if (notes.size() < 1) {
//				json = "{\"root\":[{\"Notes\":\"" + "N" + "\"}]}";
//			} else if (notes.size() == 1) {
//				json = "{\"root\":[{\"Notes\":\"" + N + "\"}]}";
//			} else if (notes.size() == 2) {
//				json = "{\"root\":[{\"Notes\":\"" + "Y" + "\"}]}";
//			}
//
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//	@Override
//	public String updateGameMessage(int gameid, int teamId, int stop_num,
//			int interchange_num) throws Exception {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String json = "";
//		String sql = "update t_relay_match_su set pause_times=?,replace_times=? where gameid=? and team=?";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, stop_num);
//			pstmt.setInt(2, interchange_num);
//			pstmt.setInt(3, gameid);
//			pstmt.setInt(4, teamId);
//			res = pstmt.executeUpdate();
//			if (res > 0) {
//				json = "true";
//			} else {
//				json = "false";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return "false";
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//	@Override
//	public String SingleLoop(String matchId, String userid) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		Connection conn2 = null;
////		conn2 = getConnection();
//		PreparedStatement pstmt2 = null;
//		ResultSet rs2 = null;
//		ArrayList<MemArrange> list = new ArrayList<MemArrange>();
//		String sql = "select * from applyform where matchid=? and status = ?";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, matchId);
//			pstmt.setInt(2, 1);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				list.add(new MemArrange(rs.getString("userid"), rs
//						.getString("realname")));
//			}
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		String board_size = "";
//		String f_gamename = "";
//		String sql2 = "select * from matchs where Id=?";
//		try {
//			conn2=DBPool.getInstance().getConnection();
//			pstmt2 = conn2.prepareStatement(sql2);
//			pstmt2.setString(1, matchId);
//			rs2 = pstmt2.executeQuery();
//			while (rs2.next()) {
//				board_size = rs2.getString("board_size");
//				f_gamename = rs2.getString("matchname");
//			}
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			ConnDB.close(conn2, pstmt2, rs2);
//		}
//		List<String> SQLStringList = new ArrayList<String>();
//		List<AllArrange> allarrange = Arrange.get(list);
//		for (int i = 0; i < allarrange.size(); i++) {
//			int round = allarrange.get(i).getRound();
//			System.out.println(allarrange.get(i).toString());
//			for (int j = 0; j < allarrange.get(i).getList().size(); j++) {
//				String blackName = allarrange.get(i).getList().get(j)
//						.getBlackName();
//				String whiteName = allarrange.get(i).getList().get(j)
//						.getWhiteName();
//				String BlackRealName = allarrange.get(i).getList().get(j)
//						.getBlackRealName();
//				String WhiteRealName = allarrange.get(i).getList().get(j)
//						.getWhiteRealName();
//				String f_roomname = BlackRealName + "-" + WhiteRealName;
//				String SQL1 = "insert into schedule(matchId,round,userid,opponent,adduser) values("
//						+ matchId
//						+ ","
//						+ round
//						+ ","
//						+ blackName
//						+ ","
//						+ whiteName + "," + userid + ");";
//				String SQL2 = "insert into schedule(matchId,round,userid,opponent,adduser) values("
//						+ matchId
//						+ ","
//						+ round
//						+ ","
//						+ whiteName
//						+ ","
//						+ blackName + "," + userid + ");";
//				String SQL3 = "insert into t_gameroom(f_gamename,f_roomname,f_roomnum,f_blackid,f_whiteid,f_matchid,f_round) values('"
//						+ f_gamename
//						+ "','"
//						+ f_roomname
//						+ "',"
//						+ board_size
//						+ ","
//						+ blackName
//						+ ","
//						+ whiteName
//						+ ","
//						+ matchId
//						+ "," + round + ");";
//				SQLStringList.add(SQL1);
//				SQLStringList.add(SQL2);
//				SQLStringList.add(SQL3);
//			}
//
//		}
//		if (JdbcHelper.batchUpdate(SQLStringList)[0] > 0) {
//			return "true";
//		} else {
//			return "false";
//		}
//	}
//
//	@Override
//	public String findMatchsBriefMSG() {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		Gson gson = new Gson();
//		String json = "";
//		String sql = "select * from matchs where match_category=2 order by addtime desc LIMIT 4;";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			List<Matchs> list = new ArrayList<Matchs>();
//			while (rs.next()) {
//				Matchs matchs = new Matchs();
//				matchs = new Matchs();
//				matchs.setId(rs.getInt("Id"));
//				matchs.setMatchname(rs.getString("matchname"));
//				matchs.setMatchtype(rs.getInt("matchtype"));
//				matchs.setTotalnum(rs.getInt("totalnum"));
//				matchs.setBzxx(rs.getString("bzxx"));
//				matchs.setAdduser(rs.getString("adduser"));
//				matchs.setAddtime(rs.getString("addtime"));
//				matchs.setSummary(rs.getString("summary"));
//				matchs.setBoard_size(rs.getInt("board_size"));
//				matchs.setMatch_time(rs.getString("match_time"));
//				matchs.setMatch_address(rs.getString("match_address"));
//				matchs.setMatch_downlinemsg(rs.getString("match_downlinemsg"));
//				matchs.setMatch_endtime(rs.getString("match_endtime"));
//				matchs.setMatch_paymoney(Double.parseDouble(new DecimalFormat(
//						"#0.00").format(rs.getDouble("match_paymoney"))));
//				matchs.setMatch_zbf(rs.getString("match_zbf"));
//				matchs.setJoinnum(rs.getInt("joinnum"));
//				matchs.setMatch_joinendtime(rs
//						.getTimestamp("match_joinendtime"));
//				matchs.setMatch_paytype(rs.getString("match_paytype"));
//				if (rs.getInt("matchtype") == 2) {
//					matchs.setTeam_a(rs.getString("team_a"));
//					matchs.setTeam_b(rs.getString("team_b"));
//				}
//				list.add(matchs);
//			}
//			json = gson.toJson(list);
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//		return json;
//	}
//
//	private int findMatchsNum(int MatchsId) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String sql = "select count(*)AS num FROM applyform  WHERE matchid =? and status=1;";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, MatchsId);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				res = rs.getInt("num");
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//		return res;
//	}
//
//	@Override
//	public String findRelay(int matchId) throws Exception {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String json = "";
//		Matchs matchs = new Matchs();
//		String sql = "select * FROM v_relay_matchs  WHERE Id =?;";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchId);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				matchs.setId(rs.getInt("Id"));
//				matchs.setMatchname(rs.getString("matchname"));
//				matchs.setMatchtype(rs.getInt("matchtype"));
//				matchs.setTotalnum(rs.getInt("totalnum"));
//				matchs.setBzxx(rs.getString("bzxx"));
//				matchs.setBoard_size(rs.getInt("board_size"));
//				matchs.setSummary(rs.getString("summary"));
//				matchs.setPeriod(rs.getInt("period"));
//				matchs.setTiming_system(rs.getInt("timing_system"));
//				matchs.setInitial_time(rs.getInt("initial_time"));
//				matchs.setSet_stop_time(rs.getInt("set_stop_time"));
//				matchs.setFirstStep(rs.getInt("first_pause"));
//				matchs.setSecondStep(rs.getInt("second_pause"));
//				matchs.setInstitutional_time(rs.getInt("institutional_time"));
//				matchs.setMatch_time(rs.getString("match_time"));
//				matchs.setMatch_address(rs.getString("match_address"));
//			}
//			Gson gson = new Gson();
//			json = gson.toJson(matchs);
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//		return json;
//	}
//
//	@Override
//	public String findMatchArrange(int matchId) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String json = "";
//		String sql = "select * FROM schedule  WHERE matchId =?;";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchId);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				// 有编排消息
//				String returnMatchArrangeMaxRound = ReturnMatchArrangeMaxRound(matchId);
//				if (returnMatchArrangeMaxRound.equals("false")) {
//					return "false";
//				} else if (returnMatchArrangeMaxRound.equals("")) {
//					return "Exception";
//				} else {
//					int round = Integer.parseInt(returnMatchArrangeMaxRound);
//					json = ReturnMatchArrange(matchId, round);
//				}
//			} else {
//				// 没有编排信息
//				json = "false";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//	@Override
//	public String ReturnMatchArrangeMaxRound(int matchId) {
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String json = "";
//		String sql = "select * FROM schedule  WHERE matchId =? order by round desc;";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchId);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				json = String.valueOf(rs.getInt("round"));
//			} else {
//				json = "false";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//	@Override
//	public String ReturnMatchArrange(int matchId, int round) {
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		List<Aeeange> list = new ArrayList<Aeeange>();
//		String json = "";
//		String sql = "select * FROM v_schedule  WHERE userid=f_blackid and matchId =? and round=? order by num asc";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchId);
//			pstmt.setInt(2, round);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				Aeeange aeeange = new Aeeange();
//				aeeange.setNum(String.valueOf(rs.getInt("num")));
//				aeeange.setRealname(rs.getString("realname"));
//				aeeange.setOthersidename(rs.getString("othersidename"));
//				aeeange.setRound(String.valueOf(rs.getInt("round")));
//				aeeange.setTotalscore(rs.getString("totalscore"));
//				aeeange.setReferee(rs.getString("refereename"));
//				aeeange.setOther(rs.getString("opponent"));
//				aeeange.setUserid(rs.getString("userid"));
//				aeeange.setScore(rs.getString("score"));
//				aeeange.setIresult(rs.getString("iresult"));
//				aeeange.setMemo(rs.getString("memo"));
//				list.add(aeeange);
//			}
//			if (list.size() < 1) {
//				json += "{\"root\":[";
//				json += "{";
//				json += "\"lastRound\"" + ":\"" + round + "\"";
//				json += "}";
//				json += "]}";
//			} else {
//				Gson gson = new Gson();
//				json = gson.toJson(list);
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//	@Override
//	public String UpdateMatchCategory(int matchId, int Category) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String json = "";
//		String sql = "update matchs set match_category=? wehe Id=?";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, Category);
//			pstmt.setInt(2, matchId);
//			res = pstmt.executeUpdate();
//			if (res > 0) {
//				json = "true";
//			} else {
//				json = "false";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//	@Override
//	public String DeleteMatch(int matchId, String userid) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String json = "false";
//		String sql = "delete from users where Id=? and adduser=?";
//		String userSql="select adduser from matchs where Id="+matchId;
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(userSql);
//			rs = pstmt.executeQuery();
//			if(rs.next()){
//				String adduser = "";
//				adduser = rs.getString("adduser");
//					String debugsql = "select roleId from t_user_role where f_userid="+userid;
//					pstmt = conn.prepareStatement(debugsql);
//					rs = pstmt.executeQuery();
//					boolean roleId = false;
//					while (rs.next()) {
//						if(rs.getInt("roleId")==6){
//							roleId = true;
//						}
//					}
//					if(adduser.equals(String.valueOf(userid))||roleId){
//						pstmt = conn.prepareStatement(sql);
//						pstmt.setInt(1, matchId);
//						pstmt.setString(2, userid);
//						res = pstmt.executeUpdate();
//							if (res > 0) {
//								json = "true";
//							} else {
//								json = "false";
//							}
//					}else{
//						json = "Not_Permission";
//					}
//			}else{
//				json = "Not_FindMatch";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//		return json;
//	}
//
//	@Override
//	public String DeleteMatchRound(int matchId, int round, int userid){
//		// TODO 自动生成的方法存根
//		List<String> SQLStringList = new ArrayList<String>();
//		String json = "false";
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String userSql="select adduser from matchs where Id="+matchId;
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(userSql);
//			rs = pstmt.executeQuery();
//			if(rs.next()){
//				String adduser = "";
//				adduser = rs.getString("adduser");
//				String debugsql = "select roleId from t_user_role where f_userid="+userid;
//				pstmt = conn.prepareStatement(debugsql);
//				rs = pstmt.executeQuery();
//				boolean roleId = false;
//				while (rs.next()) {
//					if(rs.getInt("roleId")==6){
//						roleId = true;
//					}
//				}
//				if(adduser.equals(String.valueOf(userid))||roleId){
//					String sql = "delete from schedule where matchId=" + matchId
//							+ " and round=" + round;
//					String sql2 = "delete from t_gameroom where f_matchid=" + matchId
//							+ " and f_round=" + round;
//					SQLStringList.add(sql);
//					SQLStringList.add(sql2);
//					int iret[] = JdbcHelper.batchUpdate(SQLStringList);
//					int resu = 1;
//					for (int i = 0; i < iret.length; i++) {
//						if (iret[i] <= 0) {
//							resu = iret[i];
//						}
//					}
//					if (resu > 0) {
//						json = "true";
//					} else {
//						json = "false";
//					}
//				}else{
//					json = "Not_Permission";
//				}
//			}else{
//				json = "Not_FindMatch";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//	@Override
//	public String UpdateMatchRival(int matchId, int round, int o_userid,
//			int n_userid) throws Exception {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		java.sql.Statement stmt = null;
//		ResultSet rs = null;
//		String json = "false";
//		int[] executeBatch = null;
//		String[] sqllist = new String[6];
//		List<Map<String, Integer>> list = selectGetUserMsg(matchId, round,
//				o_userid);// 查出还有被更换人的
//		List<Map<String, Integer>> list1 = selectGetUserMsg(matchId, round,
//				n_userid);// 查出含有更换人的
//		Map<String, Integer> map = selectGetUserMsgGameRooom(matchId, round,
//				o_userid);// 查出还有被更换人的对局
//		Map<String, Integer> map1 = selectGetUserMsgGameRooom(matchId, round,
//				n_userid);// 查出含有更换人的对局
//		int o_userid_totalscore = 0, n_userid_totalscore = 0;
//		int m = 0, n = 0;
//		for (int i = 0; i < list.size(); i++) {
//			if (list.get(i).get("userid") == o_userid) {
//				o_userid_totalscore = list.get(i).get("totalscore");
//			}
//		}
//		for (int i = 0; i < list1.size(); i++) {
//			if (list1.get(i).get("userid") == n_userid) {
//				n_userid_totalscore = list1.get(i).get("totalscore");
//			}
//		}
//		for (int i = 0; i < list.size(); i++) {
//			if (list.get(i).get("userid").toString().equals("o_userid")) {
//				sqllist[0] = "update schedule set userid=" + n_userid
//						+ ",totalscore=" + n_userid_totalscore + " where Id="
//						+ Integer.parseInt(list.get(i).get("Id").toString());
//			}
//			if (list.get(i).get("opponent").toString().equals("o_userid")) {
//				sqllist[1] = "update schedule set opponent=" + n_userid
//						+ " where Id="
//						+ Integer.parseInt(list.get(i).get("Id").toString());
//			}
//		}
//		for (int i = 0; i < list1.size(); i++) {
//			if (list1.get(i).get("userid").toString().equals("n_userid")) {
//				sqllist[2] = "update schedule set userid=" + o_userid
//						+ ",totalscore=" + o_userid_totalscore + " where Id="
//						+ Integer.parseInt(list1.get(i).get("Id").toString());
//			}
//			if (list1.get(i).get("opponent").toString().equals("n_userid")) {
//				sqllist[3] = "update schedule set opponent=" + o_userid
//						+ " where Id="
//						+ Integer.parseInt(list1.get(i).get("Id").toString());
//			}
//		}
//		if (map.get("Tag").toString().equals("1")) {
//			sqllist[4] = "update t_gameroom set f_blackid=" + n_userid
//					+ " where Id=" + map.get("Id");
//		} else {
//			sqllist[4] = "update t_gameroom set f_whiteid=" + n_userid
//					+ " where Id=" + map.get("Id");
//		}
//		if (map1.get("Tag").toString().equals("1")) {
//			sqllist[5] = "update t_gameroom set f_blackid=" + o_userid
//					+ " where Id=" + map1.get("Id");
//		} else {
//			sqllist[5] = "update t_gameroom set f_whiteid=" + o_userid
//					+ " where Id=" + map1.get("Id");
//		}
//		conn = DBPool.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		try {
//			stmt = conn.createStatement();
//			if (sqllist != null) {
//				for (int i = 0; i < sqllist.length; i++) {
//					stmt.addBatch((String) sqllist[i]);
//				}
//				executeBatch = stmt.executeBatch();
//			}
//			conn.commit();
//			conn.setAutoCommit(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			 try {
//				conn.rollback();
//				return json;
//			} catch (Exception e1) {
//				// TODO 自动生成的 catch 块
//				e1.printStackTrace();
//			}
//		} finally {
//
//
//			DBPool.close(conn, stmt, null);
//
////			if (conn != null) {
////				conn.close();
////			}
////			if (stmt != null) {
////				stmt.close();
////			}
//		}
//		if(executeBatch.length>0){
//			json = "true";
//		}
//		return json;
//	}
//
//	public String UpdateUserMsgGameRoom(int Id, int userid, int Tag) {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String json = "";
//		String sql = "";
//		if (Tag == 1) {
//			sql = "update t_gameroom set f_blackid=? where Id=?";
//		} else if (Tag == 2) {
//			sql = "update t_gameroom set f_whiteid=? where Id=?";
//		} else {
//			return "false_01";
//		}
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, userid);
//			pstmt.setInt(2, Id);
//			res = pstmt.executeUpdate();
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally
//		{
//			DBPool.close(conn, pstmt, null);
//
//		}
//		if (res > 0) {
//			json = "true";
//		} else {
//			json = "false";
//		}
//		return json;
//	}
//
//	public String UpdateUserMsg(int Id, int userid, int totalscore, int Tag) {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String json = "";
//		String sql = "";
//		int res = 0;
//		if (Tag == 1) {
//			sql = "update schedule set userid=" + userid + ",totalscore="
//					+ totalscore + " where Id=" + Id;
//		} else if (Tag == 2) {
//			sql = "update schedule set opponent=" + userid + " where Id=" + Id;
//		} else {
//			return "false_01";
//		}
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			res = pstmt.executeUpdate();
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		if (res > 0) {
//			json = "true";
//		} else {
//			json = "false";
//		}
//		return json;
//	}
//
//	public Map<String, Integer> selectGetUserMsgGameRooom(int matchId,
//			int round, int userid) {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "select * from t_gameroom where f_matchid=? and f_round=? and (f_blackid=? or f_whiteid=?)";
//		conn = DBPool.getInstance().getConnection();
//		Map<String, Integer> map = new HashMap<String, Integer>();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchId);
//			pstmt.setInt(2, round);
//			pstmt.setInt(3, userid);
//			pstmt.setInt(4, userid);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				map.put("Id", rs.getInt("Id"));
//				map.put("f_blackid", rs.getInt("f_blackid"));
//				map.put("f_whiteid", rs.getInt("f_whiteid"));
//				if (rs.getInt("f_blackid") == userid) {
//					map.put("Tag", 1);
//				} else if (rs.getInt("f_whiteid") == userid) {
//					map.put("Tag", 2);
//				} else {
//					map.put("Tag", 0);
//				}
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return map;
//	}
//
//	public List<Map<String, Integer>> selectGetUserMsg(int matchId, int round,
//			int userid) {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "select * from schedule where matchId=? and round=? and (userid=? or opponent=?)";
//		conn = DBPool.getInstance().getConnection();
//		List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchId);
//			pstmt.setInt(2, round);
//			pstmt.setInt(3, userid);
//			pstmt.setInt(4, userid);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				Map<String, Integer> map = new HashMap<String, Integer>();
//				map.put("Id", rs.getInt("Id"));
//				map.put("userid", rs.getInt("userid"));
//				map.put("opponent", rs.getInt("opponent"));
//				map.put("iresult", rs.getInt("iresult"));
//				map.put("totalscore", rs.getInt("totalscore"));
//				list.add(map);
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
//	@Override
//	public String selectGetUserApplyList(int matchId, int status,
//			String userid, int startIndex, int pageSize) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		List<Applyform> afList = new ArrayList<Applyform>();
//		String json = "false";
//		String sql = "select * from v_userapply where matchid=" + matchId;
//		String str = "select f_userwight from t_user where f_userid=" + userid;
//		try {
//			List list1 = JdbcHelper.query(str);
//			if (list1.size() <= 0) {
//				return "not_has_userid";
//			}
//			Map mp1 = (Map) list1.get(0);
//			String yhqx = getwight(Integer.parseInt(userid));
//			if (yhqx.indexOf("#debug") > 0) // 系统管理员
//			{
//
//			} else if (yhqx.indexOf("#matchdebug") > 0) {
//				sql += " and adduser=" + userid;
//			} else {
//				return "not_has_permissions"; // 无权限者
//			}
//			int startIndexs = pageSize * (startIndex - 1);
//			int endIndex = pageSize;
//			sql += " and status=" + status + " order by addtime desc limit "
//					+ startIndexs + " ," + endIndex;
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				Applyform applyform = new Applyform();
//				applyform
//						.setId(Integer.parseInt(rs.getString("Id").toString()));
//				applyform.setMatchid(Integer.parseInt(rs.getString("matchid")
//						.toString()));
//				applyform.setMatchname(rs.getString("matchname").toString());
//				applyform.setRealname(rs.getString("realname").toString());
//				applyform.setAddTime(rs.getString("addtime").toString());
//				applyform.setBzxx(rs.getString("bzxx").toString());
//				applyform.setStatus(Integer.parseInt(rs.getString("status")
//						.toString()));
//				applyform.setTeam(rs.getString("team").toString());
//				applyform.setUserid(Integer.parseInt(rs.getString("userid")
//						.toString()));
//				applyform.setOrganization(rs.getString("organization"));
//				afList.add(applyform);
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		Gson gson = new Gson();
//		if (afList.size() > 0) {
//			json = gson.toJson(afList);
//		}
//		return json;
//	}
//	private String getwight(int userid) {
//		String json = "";
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql = "select rolecode from v_user_role WHERE F_userid=?";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, userid);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				json += rs.getString("rolecode");
//			}
//			return json;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//
//	}
//
//	@Override
//	public List SscheduleUtil(String sql){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = ConnDB.getConnection();
//		List list = new ArrayList();
//		if(conn==null){
//			return list;
//		}
//		try {
//			pstmt = conn.prepareStatement(sql);
//			rs= pstmt.executeQuery();
//			while (rs.next()) {
//				ResultSetMetaData rsmd = rs.getMetaData();
//				Map map =new HashMap();
//				int count = rsmd.getColumnCount();
//				for (int i = 1; i <=count; i++) {
//					if(rs.getObject(i)==null)
//					{
//						map.put(rsmd.getColumnLabel(i), "");
//					}else
//					{
//					map.put(rsmd.getColumnLabel(i), rs.getObject(i));
//					}
//		            list.add(map);
//				}
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}finally {
//			ConnDB.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	/**
//	 * 获取某赛事用户报名表报名用户ID列表。
//	 */
//	@Override
//	public List<UserApplyFrom> GetListUserApplyFrom(int matchid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		List<UserApplyFrom> list = new ArrayList<UserApplyFrom>();
//		String sql="SELECT userid from applyform where matchid=? and status=1;";
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs= pstmt.executeQuery();
//			while (rs.next()) {
//				UserApplyFrom UAF =new UserApplyFrom(rs.getInt("userid"));
//				list.add(UAF);
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	public List<UserApplyFrom> GetListUserApplyFrom(int matchid,int groupid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		List<UserApplyFrom> list = new ArrayList<UserApplyFrom>();
//		String sql="SELECT userid from t_applyform where matchid=? and status=1";
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs= pstmt.executeQuery();
//			while (rs.next()) {
//				UserApplyFrom UAF =new UserApplyFrom(rs.getInt("userid"));
//				list.add(UAF);
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//
//	@Override
//	public UserApplyFromMessage GetUserApplyFromMessage(int userid,int matchid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		int num =0;
//		int totalscore=0;
//		String name="";
//		List<UserApplyFrom> list = new ArrayList<UserApplyFrom>();
//		UserApplyFromMessage UAFM = new UserApplyFromMessage();
//		String sql ="SELECT totalscore FROM v_schedule WHERE matchId =? and userid=? ORDER BY totalscore  DESC LIMIT 0,1;";
//		String sql3 ="SELECT realname,num FROM applyform where matchid =? and userid=?";
//		try {
//			pstmt = conn.prepareStatement(sql3);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, userid);
//			rs= pstmt.executeQuery();
//			if(rs.next()){
//				num = rs.getInt("num");
//				name =rs.getString("realname");
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, matchid);
//				pstmt.setInt(2, userid);
//				rs= pstmt.executeQuery();
//				if(rs.next()){
//					totalscore = rs.getInt("totalscore");
//				}else{
//					totalscore = 0;
//				}
//
//				String sql2="SELECT opponent FROM v_schedule WHERE matchId =? and userid=?";
//				pstmt = conn.prepareStatement(sql2);
//				pstmt.setInt(1, matchid);
//				pstmt.setInt(2, userid);
//				rs= pstmt.executeQuery();
//				while(rs.next()){
//					UserApplyFrom UAF=new UserApplyFrom(rs.getInt("opponent"));
//					list.add(UAF);
//				}
//				if(num>0){
//					UAFM.setUserid(userid);
//					UAFM.setList(list);
//					UAFM.setNum(num);
//					UAFM.setTotalscore(totalscore);
//					UAFM.setName(name);
//				}
//			}
//
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return UAFM;
//	}
//
//
//	@SuppressWarnings("resource")
//	public UserApplyFromMessage GetUserApplyFromMessage(int userid,int matchid,int groupid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		int num =0;
//		int totalscore=0;
//		String name="";
//		List<UserApplyFrom> list = new ArrayList<UserApplyFrom>();
//		UserApplyFromMessage UAFM = new UserApplyFromMessage();
//		// 查询语句：得到当前用户的总分 【来自编排表（schedule）信息】
//		String sql ="SELECT totalscore FROM v_schedule_make WHERE matchid =? and userid=?  ORDER BY totalscore  DESC LIMIT 0,1;";
//		// 查询语句：得到当前用户的真实姓名，组内序号。【来自报名表（t_applyform）信息】
//		String sql3 ="SELECT realname,num FROM t_applyform where matchid =? and userid=?";
//		try {
//			pstmt = conn.prepareStatement(sql3);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, userid);
//			rs= pstmt.executeQuery();
//			if(rs.next()){
//				num = rs.getInt("num");
//				name =rs.getString("realname");
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, matchid);
//				pstmt.setInt(2, userid);
//				rs= pstmt.executeQuery();
//				if(rs.next()){
//					totalscore = rs.getInt("totalscore");
//				}else{
//					totalscore = 0;
//				}
//
//				String sql2="SELECT opponent FROM v_schedule_make WHERE matchid =? and userid=?";
//				pstmt = conn.prepareStatement(sql2);
//				pstmt.setInt(1, matchid);
//				pstmt.setInt(2, userid);
//				rs= pstmt.executeQuery();
//				while(rs.next()){
//					UserApplyFrom UAF=new UserApplyFrom(rs.getInt("opponent"));
//					list.add(UAF);
//				}
//				if(num>0){
//					UAFM.setUserid(userid);
//					UAFM.setList(list);
//					UAFM.setNum(num);
//					UAFM.setTotalscore(totalscore);
//					UAFM.setName(name);
//				}
//			}
//
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return UAFM;
//	}
//
//
//	/**
//	 * 获取赛事的用户编排表信息，该表信息经过适当的初步处理。
//	 */
//	@Override
//	public UserSchedule GetListUserSchedule(int matchid){
//		// TODO 自动生成的方法存根
//		UserSchedule US=new UserSchedule();
//
//		 // 存储报名报用户名单信息（完整userid列表）
//		List<UserApplyFrom> list_UAF = new ArrayList<UserApplyFrom>();
//
//		// 存储报名列表扩展表，信息更详细。
//		List<UserApplyFromMessage> list_UAFM = new ArrayList<UserApplyFromMessage>();
//
//		 // 获取简单报名名单列表。
//		list_UAF = GetListUserApplyFrom(matchid);
//
//		if(list_UAF.size()%2!=0){//判断人数是否为偶数
//			boolean has9191= false;//是否有轮空
//			for (int i = 0; i < list_UAF.size(); i++) {
//				if(list_UAF.get(i).getUserid()==9191){
//					has9191=true;
//					list_UAF.remove(i);//人数为单数时，并且存在轮空时，将轮空去除。
//				}
//
//			}
//		}
//
//		// 对每个用户进行遍历处理，得到该用户的详细扩展信息，特别要得到该用户对战过的用户列表，未对战过的用户列表。
//		for (int i = 0; i < list_UAF.size(); i++) {
//			UserApplyFromMessage UAFM = GetUserApplyFromMessage(list_UAF.get(i).getUserid(), matchid);
//			list_UAFM.add(UAFM);
//		}
//		US.setUAF_list(list_UAF);
//		US.setUAFM_list(list_UAFM);
//		return US;
//	}
//
//
//	public UserSchedule GetListUserSchedule(int matchid,int groupid){
//		// TODO 自动生成的方法存根
//		UserSchedule US=new UserSchedule();
//
//		 // 存储报名报用户名单信息（完整userid列表）
//		List<UserApplyFrom> list_UAF = new ArrayList<UserApplyFrom>();
//
//		// 存储报名列表扩展表，信息更详细。
//		List<UserApplyFromMessage> list_UAFM = new ArrayList<UserApplyFromMessage>();
//
//		 // 获取本组简单报名名单列表。
//		list_UAF = GetListUserApplyFrom(matchid,groupid);
//
//		if(list_UAF.size()%2!=0){//判断人数是否为偶数
//			boolean has9191= false;//是否有轮空
//			for (int i = 0; i < list_UAF.size(); i++) {
//				if(list_UAF.get(i).getUserid()==9191){
//					has9191=true;
//					list_UAF.remove(i);//人数为单数时，并且存在轮空时，将轮空去除。
//				}
//
//			}
//		}
//
//		// 对本组每个用户进行遍历处理，得到该用户的详细扩展信息，特别要得到该用户对战过的用户列表，未对战过的用户列表。
//		for (int i = 0; i < list_UAF.size(); i++) {
//			UserApplyFromMessage UAFM = GetUserApplyFromMessage(list_UAF.get(i).getUserid(), matchid,groupid);
//			list_UAFM.add(UAFM);
//		}
//		US.setUAF_list(list_UAF);
//		US.setUAFM_list(list_UAFM);
//		return US;
//	}
//
//	@Override
//	public boolean ChargeMatchs(int matchid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql ="select match_paymoney from matchs where Id=?";
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt= conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs= pstmt.executeQuery();
//			if(rs.next()){
//				if(rs.getDouble("match_paymoney")>0){
//					return true;
//				}else{
//					return false;
//				}
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return false;
//	}
//
//	@SuppressWarnings("resource")
//	@Override
//	public String selectChargeGetUserApplyList(int matchId, int status,
//			String userid, int startIndex, int pageSize){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		List<Applyform> afList = new ArrayList<Applyform>();
//		String json="";
//		String sql ="select * from v_charge_userapply where matchid="+matchId;
//		String str = "select f_userwight from t_user where f_userid=" + userid;
//		try {
//			List list1 = JdbcHelper.query(str);
//			if (list1.size() <= 0) {
//				return "not_has_userid";
//			}
//			Map mp1 = (Map) list1.get(0);
//			String yhqx =getwight(Integer.parseInt(userid));
//			if (yhqx.indexOf("#debug") > 0) // 系统管理员
//			{
//
//			} else if (yhqx.indexOf("#matchdebug") > 0) {
//				sql += " and adduser=" + userid;
//			} else {
//				return "not_has_permissions"; // 无权限者
//			}
//			int startIndexs = pageSize * (startIndex - 1);
//			int endIndex = pageSize;
//			conn = DBPool.getInstance().getConnection();
//			if(status==0){
//				pstmt = conn.prepareStatement("select match_paytype,match_paymoney from matchs where Id="+matchId);
//				rs = pstmt.executeQuery();
//				if(rs.next()){
//				if(rs.getString("match_paytype").equals("线上支付")&&rs.getDouble("match_paymoney")>0){
//					sql += " and state_purchase=1 ";
//				}
//				}
//			}
//			sql += " and status=" + status + " order by addtime desc limit "
//					+ startIndexs + " ," + endIndex;
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				Applyform applyform = new Applyform();
//				applyform
//						.setId(Integer.parseInt(rs.getString("Id")));
//				applyform.setMatchid(Integer.parseInt(rs.getString("matchid")));
//				applyform.setMatchname(rs.getString("matchname"));
//				applyform.setRealname(rs.getString("realname"));
//				applyform.setAddTime(rs.getString("addtime"));
//				applyform.setBzxx(rs.getString("bzxx"));
//				applyform.setStatus(Integer.parseInt(rs.getString("status")
//						.toString()));
//				if(rs.getString("team")==null){
//					applyform.setTeam("");
//				}else{
//					applyform.setTeam(rs.getString("team"));
//				}
//				applyform.setUserid(Integer.parseInt(rs.getString("userid")));
//				applyform.setOrganization(rs.getString("organization"));
//				applyform.setCharge(rs.getInt("state_purchase"));
//				if(applyform.getUserid()!=9191){
//				afList.add(applyform);
//				}
//			}
//		} catch (NumberFormatException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		Gson gson =new Gson();
//		if (afList.size() > 0) {
//			json=gson.toJson(afList);
//		}
//		return json;
//	}
//
//	@Override
//	public String UpdateUserApplyFrom(int matchid,int userid, int status) {
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String sql="update applyform set status=? where userid=? and matchid=?";
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, status);
//			pstmt.setInt(2, userid);
//			pstmt.setInt(3, matchid);
//			res = pstmt.executeUpdate();
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		if(res>0){
//			return "true";
//		}else{
//			return "false";
//		}
//	}
//
//	@SuppressWarnings("resource")
//	@Override
//	public String UpdateScore(int matchid, int userid,  int score,int round){
//		//x=1表示胜利方
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		int my_score=0;
//		String sql2="";
//		String sql="select score from schedule where matchid=? and round=? and userid=? limit 0,1";
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, round);
//			pstmt.setInt(3, userid);
//			rs = pstmt.executeQuery();
//			if(rs.next()){
//				my_score = rs.getInt("score");
//				if(my_score == score){//修改数据和元数据相同，不处理
//					return "true";
//				}else{
//					sql2="update schedule set score="+score+" ,totalscore=((totalscore-"+my_score+")+"+score+") where matchid="+matchid+" and round="+round+" and userid="+userid;
//				}
//				System.out.println("SQL="+sql2);
//				pstmt = conn.prepareStatement(sql2);
//				res =pstmt.executeUpdate();
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		if(res>0){
//			return "true";
//		}else{
//			return "false";
//		}
//
//	}
//
//	@Override
//	public boolean SelectScore(int matchid, int userid, int ouserid, int round){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql="select score from schedule where matchid=? and round=? and (userid=? or userid=?)";
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, round);
//			pstmt.setInt(3, userid);
//			pstmt.setInt(4, ouserid);
//			rs = pstmt.executeQuery();
//			while(rs.next()){
//				if(rs.getInt("score")>0){
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
//	@Override
//	public String SetStartTime(int matchid, int round, String StartTime){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res=0;
//		String sql="select id from t_match_starttime_ex where matchid=? and round=?";
//		String sql2 ="";
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, round);
//			rs = pstmt.executeQuery();
//			if(!rs.next()){
//				sql2 ="insert into t_match_starttime_ex(matchid,round,start_time) values(?,?,?)";
//				pstmt = conn.prepareStatement(sql2);
//				pstmt.setInt(1, matchid);
//				pstmt.setInt(2, round);
//				pstmt.setString(3, StartTime);
//				res =pstmt.executeUpdate();
//				if(res>0){
//					return "true";
//				}else{
//					return "false";
//				}
//			}else{
//				sql2="update t_match_starttime_ex set start_time=? where matchid=? and round=?";
//				pstmt = conn.prepareStatement(sql2);
//				pstmt.setInt(2, matchid);
//				pstmt.setInt(3, round);
//				pstmt.setString(1, StartTime);
//				res =pstmt.executeUpdate();
//				if(res>0){
//					return "true";
//				}else{
//					return "false";
//				}
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return "SQLException";
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//	}
//
//	@Override
//	public String Knockout(int matchid, int round){
//		// TODO 自动生成的方法存根
//		return null;
//	}
//
//	@Override
//	public String NewUpdateApply(int matchid, int userid, int status){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		int match_status = 0;
//		int Max_num=0;
//		int User_num=0;
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement("select bzxx from matchs where Id=" + matchid);
//			rs = pstmt.executeQuery();
//			if(rs.next()){
//				match_status = Integer.parseInt(rs.getString("bzxx"));
//			}else{
//				return "Inexistence_Match";
//			}
//			pstmt = conn.prepareStatement("select num from applyform where matchid=" + matchid +" order by num desc;");
//			rs = pstmt.executeQuery();
//			if(rs.next()){
//				Max_num = rs.getInt("num");
//			}
//			if(match_status == 2){
//				return "End_Match";
//			}else{
//				if(status==3){//是否剔除某人
//						pstmt = conn.prepareStatement("update matchs set joinnum=joinnum-1, where Id=" + matchid);
//						res = pstmt.executeUpdate();
//						if(res>0){
//							return "true";
//						}else{
//							return "false";
//						}
//				}else{
//					if(status==1){
//						pstmt = conn.prepareStatement("select f_yzres from t_user where f_userid=" + userid);
//						rs = pstmt.executeQuery();
//						if(rs.next()){
//							if(rs.getInt("f_yzres")==2){
//								pstmt = conn.prepareStatement("update t_user set f_yzres=1 where f_userid=" + userid);
//								res = pstmt.executeUpdate();
//							}
//						}
//					}
//					if(Max_num>0&&status==1){//比赛已经开始
//						pstmt = conn.prepareStatement("select num from applyform where matchid=" + matchid +" and userid= "+userid+" order by num desc;");
//						rs = pstmt.executeQuery();
//						if(rs.next()){
//							User_num = rs.getInt("num");
//						}
//						if(Max_num>0&&User_num==0){//赛事已经完成抽签
//							String sql = "update applyform set status=?,num=? where matchid=? and userid=?;";
//							pstmt = conn.prepareStatement(sql);
//							pstmt.setInt(1, status);
//							pstmt.setInt(2, Max_num);
//							pstmt.setInt(3, matchid);
//							pstmt.setInt(4, userid);
//							res = pstmt.executeUpdate();
//							if(res>0){
//								pstmt = conn.prepareStatement("update applyform set num="+(Max_num+1)+" where matchid="+matchid+" and userid=9191;");
//								res = pstmt.executeUpdate();
//								if(res>0){
//									return "true";
//								}else{
//									return "false";
//								}
//							}else{
//								return "false";
//							}
//
//						}else{
//							String sql = "update applyform set status=? where matchid=? and userid=?";
//							pstmt = conn.prepareStatement(sql);
//							pstmt.setInt(1, status);
//							pstmt.setInt(2, matchid);
//							pstmt.setInt(3, userid);
//							res = pstmt.executeUpdate();
//							if(res>0){
//								return "true";
//							}else{
//								return "false";
//							}
//						}
//					}else{//还在报名中
//						String sql = "update applyform set status=? where matchid=? and userid=?";
//						pstmt = conn.prepareStatement(sql);
//						pstmt.setInt(1, status);
//						pstmt.setInt(2, matchid);
//						pstmt.setInt(3, userid);
//						res = pstmt.executeUpdate();
//						if(res>0){
//							return "true";
//						}else{
//							return "false";
//						}
//					}
//
//				}
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return "SQLException";
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//	}
//
//	@Override
//	public String SelectStartTime(int matchid, int round){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String json="";
//		String sql="select start_time from t_match_starttime_ex where matchid=? and round=?";
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, round);
//			rs = pstmt.executeQuery();
//			if(rs.next()){
//				json = rs.getString("start_time");
//			}else{
//				json ="Not_Time";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return "SQLException";
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//	@Override
//	public String SetMatchScore(int gameid, int matchid, int round, int sscore,
//			int oscore,int userid,int suserid,int ouserid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res=0;
//		int f_num=0;
//		int blackid=0;
//		int whiteid=0;
//		int add_user=0;
//		String sql="select f_num,f_blackid,f_whiteid,f_add_user from t_gameroom where Id=?";
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, gameid);
//			rs = pstmt.executeQuery();
//			if(rs.next()){
//				f_num = rs.getInt("f_num");
//				blackid = rs.getInt("f_blackid");
//				whiteid = rs.getInt("f_whiteid");
//				add_user = Integer.parseInt(rs.getString("f_add_user"));
//				if(f_num>1){//双方都有落子
//					pstmt = conn.prepareStatement("select Id from t_gameroom where Id="+gameid+" and f_match_state!= 1 and f_state!=1");
//					rs = pstmt.executeQuery();
//					if(rs.next()){
//						String sql1="update schedule set score="+sscore+",wintype=1,totalscore=totalscore+"+sscore+" where matchId="+matchid
//								+" and userid="+suserid+" and round="+round;
//						String sql2="update schedule set score="+oscore+",wintype=1,totalscore=totalscore+"+oscore+" where matchId="+matchid
//								+" and userid="+ouserid+" and round="+round;
//						String sql3 = "update t_gameroom set f_match_state=1,f_state=1 where Id="+gameid;
//						List<String> SQLStringList = new ArrayList<String>();
//						SQLStringList.add(sql1);
//						SQLStringList.add(sql2);
//						SQLStringList.add(sql3);
//						int[] iret = JdbcHelper.batchUpdate(SQLStringList);
//						int resu = 1;
//						for (int i = 0; i < iret.length; i++) {
//							if (iret[i] <= 0) {
//								resu = iret[i];
//							}
//						}
//						if(resu>0){
//							return "true";
//						}else{
//							return "false";
//						}
//					}else{
//						return "game_close";
//					}
//				}else{
//					pstmt = conn.prepareStatement("select Id from t_gameroom where Id="+gameid+" and f_match_state!= 1 and f_state!=1");
//					rs = pstmt.executeQuery();
//					if(rs.next()){
//						String sql1="update schedule set score="+sscore+",wintype=1,totalscore=totalscore+"+sscore+" where matchId="+matchid
//								+" and userid="+suserid+" and round="+round;
//						String sql2="update schedule set score="+oscore+",wintype=2,totalscore=totalscore+"+oscore+" where matchId="+matchid
//								+" and userid="+ouserid+" and round="+round;
//						String sql3="update t_gameroom set f_match_state=1,f_state=1 where Id="+gameid;
//						List<String> SQLStringList = new ArrayList<String>();
//						SQLStringList.add(sql1);
//						SQLStringList.add(sql2);
//						SQLStringList.add(sql3);
//						int[] iret = JdbcHelper.batchUpdate(SQLStringList);
//						int resu = 1;
//						for (int i = 0; i < iret.length; i++) {
//							if (iret[i] <= 0) {
//								resu = iret[i];
//							}
//						}
//						if(resu>0){
//							return "true";
//						}else{
//							return "false";
//						}
//					}else{
//						return "game_close";
//					}
//				}
//			}else{
//				return "Not_Game";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return "SQLException";
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//	}
//
//	@Override
//	public String SelectGiveUp(int matchid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String json = "Nothing";
//		List<Applyform> afList = new ArrayList<Applyform>();
//		String sql="select round,userid,realname  from v_schedule where matchId=? and wintype=2";
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			while(rs.next()){
//				Applyform af = new Applyform();
//				af.setUserid(rs.getInt("userid"));
//				af.setId(rs.getInt("round"));
//				af.setRealname(rs.getString("realname"));
//				afList.add(af);
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return "SQLException";
//		}  finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		Gson gson = new Gson();
//		json = gson.toJson(afList);
//		return json;
//	}
//
//	@Override
//	public int SelectUserId(int id){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res = 0;
//		String sql="select userid  from schedule where Id=?";
//		conn = DBPool.getInstance().getConnection();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			rs = pstmt.executeQuery();
//			if(rs.next()){
//				res = rs.getInt("userid");
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return  -1;
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return res;
//	}
//
//	@Override
//	public boolean SetMatchScore(int gameid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		String sql="select * from schedule where matchId=? and round=? and round=? and score=?";
//		try {
//			pstmt = conn.prepareStatement("select f_match_state,f_state from t_gameroom  where Id="+gameid);
//			rs = pstmt.executeQuery();
//			if(rs.next()){
//				if(rs.getInt("f_match_state")!=1&&rs.getInt("f_state")!=1){
//					return true;
//				}else{
//					return false;
//				}
//			}else{
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
//	public String SelectMatchMessage(int matchid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		//String sql="select * from matchs where Id=?";
//		String sql="select a.*,(SELECT COUNT(*) from applyform b WHERE b.matchid=? and b.`status`=1 and b.userid!=9191) as realJoinNum from matchs a where Id=?";
//
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, matchid);
//			rs = pstmt.executeQuery();
//			if(rs.next()){
//				Matchs matchs = new Matchs();
//				matchs.setId(matchid);
//				matchs.setAdduser(rs.getString("adduser"));
//				matchs.setMatchname(rs.getString("matchname"));
//				matchs.setSummary(rs.getString("summary"));
//				matchs.setMatchtype(rs.getInt("matchtype"));
//				matchs.setTeam_a(rs.getString("team_a"));
//				matchs.setTeam_b(rs.getString("team_b"));
//				matchs.setMatch_address(rs.getString("match_address"));
//				matchs.setMatch_time(rs.getString("match_time"));
//				matchs.setMatch_zbf(rs.getString("match_zbf"));
//				matchs.setMatch_ruler(rs.getString("match_ruler"));
//				matchs.setMatch_paytype(rs.getString("match_paytype"));
//				matchs.setMatch_paymoney(rs.getDouble("match_paymoney"));
//				matchs.setMatch_joinendtime(rs.getTimestamp("match_joinendtime"));
//				matchs.setMatch_endtime(rs.getString("match_endtime"));
//				matchs.setMatch_downlinemsg(rs.getString("match_downlinemsg"));
//				matchs.setJoinnum(rs.getInt("realJoinNum"));
//				matchs.setTotalnum(rs.getInt("totalnum"));
//				return new Gson().toJson(matchs);
//			}else{
//				return "No_Matchs";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return "SQLException";
//		}finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//	}
//
//	@Override
//	public String DeleteGiveUp(int matchid,int My_userid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		String sql = "SELECT * from schedule where round = (SELECT MAX(round) from schedule WHERE matchId=?) and matchId =? and iresult IS not NULL;";
//		try {
//				String sql1 = "select userid from applyform where matchid="+matchid+" and status=1";
//				pstmt = conn.prepareStatement(sql1);
//				rs = pstmt.executeQuery();
//				List<String> list =new ArrayList<String>();
//				while(rs.next()){
//					int userid =0 ;
//					userid = rs.getInt("userid");
//					if(userid!=9191){
//						if(UserGiveUpNum(matchid, userid,2)){
//							String sql2="update applyform set status=3 where userid="+userid+" and matchid="+matchid;
//							list.add(sql2);
//						}
//					}
//				}
//				if(list.size()>0){
//					int[] batchUpdate = JdbcHelper.batchUpdate(list);
//					int iresu = 1;
//					for (int i = 0; i < batchUpdate.length; i++) {
//						if (batchUpdate[i] <= 0) {
//							iresu = batchUpdate[i];
//						}
//					}
//					if(iresu<=0){
//						return "false";
//					}else{
//						return "true";
//					}
//				}else{
//					return "NoBody";
//				}
//
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return "SQL_Exception";
//		}finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//	}
//
//	private boolean UserGiveUpNum(int matchid,int userid,int num){
//		int res = 0;
//		int max = 0;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		String sql="SELECT COUNT(*) as num from schedule WHERE matchId=? and userid = ? and wintype=2 ORDER BY round ASC LIMIT 0,?";
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, userid);
//			pstmt.setInt(3, num);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				if(rs.getInt("num")==num){
//					return true;
//				}else{
//					return false;
//				}
//			}else{
//				return false;
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return false;
//		}finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//	}
//
//	@Override
//	public String UpdateApplfUserNum(int matchid, int userid, int userid_num,
//			int ouserid_num){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int iresu = 1;
//		int max_num=0;
//		conn = DBPool.getInstance().getConnection();
//		String str= "select num from applyform where matchid=? ORDER BY num desc";
//		try {
//			pstmt = conn.prepareStatement(str);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			if(rs.next()){
//				max_num = rs.getInt("num");
//			}
//			if(max_num==0){
//				int res=0;
//				pstmt = conn.prepareStatement("update applyform set num="+ouserid_num+" where userid="+userid+" and matchid="+matchid);
//				res = pstmt.executeUpdate();
//				if(res>0){
//					return "true";
//				}else{
//					return "false";
//				}
//			}else{
//				List<String> list =new ArrayList<String>();
//				String sql="update applyform set num="+ouserid_num+" where userid="+userid+" and matchid="+matchid;
//				String sql2="update applyform set num="+userid_num+" where userid!="+userid+" and matchid="+matchid+" and num="+ouserid_num;
//				list.add(sql);
//				list.add(sql2);
//				int[] batchUpdate = JdbcHelper.batchUpdate(list);
//				for (int i = 0; i < batchUpdate.length; i++) {
//					if (batchUpdate[i] <= 0) {
//						iresu = batchUpdate[i];
//					}
//				}
//				if(iresu<=0){
//					return "false";
//				}else{
//					return "true";
//				}
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
//	@Override
//	public String UserSign_In(int matchid, int userid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		int res =0;
//		conn = DBPool.getInstance().getConnection();
//		String sql="update applyform set sign_in=1 where userid="+userid+" and matchid="+matchid;
//		try {
//			pstmt = conn.prepareStatement(sql);
//			res = pstmt.executeUpdate();
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		if(res>0){
//			return "true";
//		}else{
//			return "false";
//		}
//	}
//
//	@Override
//	public List<String> Sign_InInitialize(int matchid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		Random rd = new Random();
//		List<String> list = new ArrayList<String>();
//		List<Integer> Slist= new ArrayList<Integer>();
//		List<Integer> Slist_user= new ArrayList<Integer>();
//		List<Integer> Nlist= new ArrayList<Integer>();
//		List<Integer> Nlist_user= new ArrayList<Integer>();
//		String sql= "SELECT a.num as a_num,b.num as b_num FROM"
//				+ " (SELECT COUNT(*) as 'num' FROM applyform WHERE matchid = "+matchid+" and sign_in=1) a"
//				+ ",(SELECT COUNT(*) as 'num' FROM applyform WHERE matchid = "+matchid+" and sign_in=0) b";
//		try {
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			if(rs.next()){
//				int Sign_num = rs.getInt("a_num");
//				int N_Singn_num = rs.getInt("b_num");
//				while(Slist.size() < Sign_num) {
//					int num = rd.nextInt(Sign_num)+1;
//				     if(!Slist.contains(num)) {
//				    	 Slist.add(num);
//				     }
//				}
//				while(Nlist.size() < N_Singn_num) {
//					int num = rd.nextInt(N_Singn_num)+Sign_num+1;
//					if(!Nlist.contains(num)) {
//						Nlist.add(num);
//				     }
//				}
//				pstmt = conn.prepareStatement("SELECT userid from applyform WHERE matchid = "+matchid+" and sign_in=1");
//				rs = pstmt.executeQuery();
//				while(rs.next()){
//					Slist_user.add(rs.getInt("userid"));
//				}
//				pstmt = conn.prepareStatement("SELECT userid from applyform WHERE matchid = "+matchid+" and sign_in=0");
//				rs = pstmt.executeQuery();
//				while(rs.next()){
//					Nlist_user.add(rs.getInt("userid"));
//				}
//			}
//			if(Slist.size()==Slist_user.size()&&Nlist.size()==Nlist_user.size()){
//				for (int i = 0; i < Slist.size(); i++) {
//					String strUpdate = " update applyform set num= "
//							+ Slist.get(i) + " where matchid=" + matchid+" and userid="+Slist_user.get(i);
//					list.add(strUpdate);
//				}
//				for (int i = 0; i < Nlist.size(); i++) {
//					String strUpdate = " update applyform set num= "
//							+ Nlist.get(i) + " where matchid=" + matchid+" and userid="+Nlist_user.get(i);
//					list.add(strUpdate);
//				}
//			}
//
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return list;
//	}
//
//	@Override
//	public String SelectMySign_In(int matchid, int userid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		String sql="select * from applyform where matchid=? and userid=? and sign_in=1";
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2,userid);
//			rs = pstmt.executeQuery();
//			if(rs.next()){
//				return "true";
//			}else{
//				return "false";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return "false";
//		}finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//	}
//
//	@Override
//	public String SetCharts(int matchid, List<UserScoreMessage> list){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		List<String> SQLStringList = new ArrayList<String>();
//		int MaxPlacing =0;
//		int ress=1;
//		int res =0;
//		String sql="Delete from t_match_charts where matchid=?";
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			res = pstmt.executeUpdate();
//			for (int i = 0; i < list.size(); i++) {
//				UserScoreMessage userScoreMessage = list.get(i);
//				String sql2 = "insert into t_match_charts(matchid,placing,userid,realname,total_points,ototal_points) values("+matchid
//							+","
//							+(i+1)
//							+","
//							+userScoreMessage.getUserId()
//							+",'"
//							+userScoreMessage.getUserName()
//							+"',"
//							+userScoreMessage.getTotal_points()
//							+","
//							+userScoreMessage.getOpponent_total_points()
//							+")";
//				SQLStringList.add(sql2);
//			}
//			int[] batchUpdate = JdbcHelper.batchUpdate(SQLStringList);
//			for (int i = 0; i < batchUpdate.length; i++) {
//				if(batchUpdate[i]<=0){
//					ress=batchUpdate[i];
//				}
//			}
//			if(ress>0){
//				return "true";
//			}else{
//				return "false";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return "false";
//		}finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		}
//
//	@Override
//	public String SelectCharts(int matchid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		List<UserScoreMessage> list =new ArrayList<UserScoreMessage>();
//		String sql = "select * from t_match_charts where matchid=? order by placing asc";
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			rs = pstmt.executeQuery();
//			while(rs.next()){
//				UserScoreMessage userScoreMessage = new UserScoreMessage();
//				userScoreMessage.setUserId(rs.getInt("userid"));
//				userScoreMessage.setUserName(rs.getString("realname"));
//				userScoreMessage.setTotal_points(rs.getInt("total_points"));
//				userScoreMessage.setOpponent_total_points(rs.getInt("ototal_points"));
//				list.add(userScoreMessage);
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//		return new Gson().toJson(list);
//	}
//
//	@Override
//	public String SetAllGiveUp(int matchid, int round,int userid){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		conn = DBPool.getInstance().getConnection();
//		List<Integer> list = new ArrayList<Integer>();
//		List<Integer> list1 = new ArrayList<Integer>();
//		List<String> list2 = new ArrayList<String>();
//		int ress=1;
//		try {
//		String yhqx = getwight(userid);
//		if (yhqx.indexOf("#debug") > 0) // 系统管理员
//		{
//		} else if (yhqx.indexOf("#matchdebug") > 0) {
//			pstmt = conn.prepareStatement("select * matchs where Id="+matchid+" and adduser="+userid);
//			rs = pstmt.executeQuery();
//			if(!rs.next()){
//				return "not_has_permissions";
//			}
//		} else {
//			return "not_has_permissions"; // 无权限者
//		}
//			String sql="select Id,f_blackid,f_whiteid from t_gameroom where f_matchid=? and f_round=? and f_state=0 and f_match_state=0";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, matchid);
//			pstmt.setInt(2, round);
//			rs = pstmt.executeQuery();
//			while(rs.next()){
//				list.add(rs.getInt("f_blackid"));
//				list.add(rs.getInt("f_whiteid"));
//				list1.add(rs.getInt("Id"));
//			}
//			for (int i = 0; i < list.size(); i++) {
//				if(list.get(i)!=9191){
//					String sql1="update schedule as a set a.score=0,a.wintype=2 where a.matchId="+matchid+" and a.round="+round+" and a.userid="+list.get(i) ;
//					list2.add(sql1);
//				}else{
//					String sql1="update schedule as a set a.score=0,a.wintype=1 where a.matchId="+matchid+" and a.round="+round+" and a.userid="+list.get(i) ;
//					list2.add(sql1);
//				}
//
//			}
//			for (int i = 0; i < list1.size(); i++) {
//				String sql2="update t_gameroom set f_state=1 ,f_match_state=1 where Id="+list1.get(i);
//				list2.add(sql2);
//			}
//			int[] batchUpdate = JdbcHelper.batchUpdate(list2);
//			for (int i = 0; i < batchUpdate.length; i++) {
//				if(batchUpdate[i]<=0){
//					ress=batchUpdate[i];
//				}
//			}
//			if(ress>0){
//				return "true";
//			}else{
//				return "false";
//			}
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//			return "false";
//		}finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//
//	}
//
//	@Override
//	public String IOSfindMatchsBriefMSG(){
//		// TODO 自动生成的方法存根
//		Connection conn = null;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		Gson gson = new Gson();
//		String json = "";
//		String sql = "select * from matchs where match_category=2 and board_size=29 order by addtime desc LIMIT 4;";
//		try {
//			conn = DBPool.getInstance().getConnection();
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			List<Matchs> list = new ArrayList<Matchs>();
//			while (rs.next()) {
//				Matchs matchs = new Matchs();
//				matchs = new Matchs();
//				matchs.setId(rs.getInt("Id"));
//				matchs.setMatchname(rs.getString("matchname"));
//				matchs.setMatchtype(rs.getInt("matchtype"));
//				matchs.setTotalnum(rs.getInt("totalnum"));
//				matchs.setBzxx(rs.getString("bzxx"));
//				matchs.setAdduser(rs.getString("adduser"));
//				matchs.setAddtime(rs.getString("addtime"));
//				matchs.setSummary(rs.getString("summary"));
//				matchs.setBoard_size(rs.getInt("board_size"));
//				matchs.setMatch_time(rs.getString("match_time"));
//				matchs.setMatch_address(rs.getString("match_address"));
//				matchs.setMatch_downlinemsg(rs.getString("match_downlinemsg"));
//				matchs.setMatch_endtime(rs.getString("match_endtime"));
//				matchs.setMatch_paymoney(Double.parseDouble(new DecimalFormat(
//						"#0.00").format(rs.getDouble("match_paymoney"))));
//				matchs.setMatch_zbf(rs.getString("match_zbf"));
//				matchs.setJoinnum(rs.getInt("joinnum"));
//				matchs.setMatch_joinendtime(rs
//						.getTimestamp("match_joinendtime"));
//				matchs.setMatch_paytype(rs.getString("match_paytype"));
//				if (rs.getInt("matchtype") == 2) {
//					matchs.setTeam_a(rs.getString("team_a"));
//					matchs.setTeam_b(rs.getString("team_b"));
//				}
//				list.add(matchs);
//			}
//			json = gson.toJson(list);
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} finally {
//			DBPool.close(conn, pstmt, rs);
//		}
//		return json;
//	}
//
//
//
//}
