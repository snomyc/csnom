//package com.snomyc.common.util;
//
//import java.math.BigDecimal;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import org.apache.log4j.Logger;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.util.CollectionUtils;
//
///**
// * @author yangcan 体彩网 游戏数据接口调用
// */
//public class GameGrabLotteryUtil {
//
//	private static String LOTTERY_ZC_URL = "http://www.lottery.gov.cn/api/lottery_kj_detail.jspx?";
//
//	private static String LOTTERY_ZC_ISSUE_URL = "http://www.lottery.gov.cn/api/get_typeBytermAndnews.jspx?";
//
//	/**
//	 * 1---大乐透, 2 ---七星彩, 23 -- 广东11选5, 3---排列3 ,4---排列5, 7 --- 14场胜负彩,  9-----4场进球, 10---6场半全场
//	 * 体彩网对应游戏编码 4---大乐透, 8 ---七星彩 ,5---排列3, 6---排列5, 9 --- 14场胜负彩,  11-----4场进球, 10---6场半全场
//	 * 获取游戏最新期数据信息
//	 * @param gameID
//	 * @return
//	 */
//	public static GameIssue getGameNewest(Integer gameID,String issue) {
//
//		Integer tcGameID = null;
//		if(gameID == 23) {
//			return get11x5GameNewest(gameID, issue);
//		}else if (gameID==1){
//			tcGameID = 4;
//		}else if (gameID==4){
//			tcGameID = 6;
//		}else if (gameID==2){
//			tcGameID = 8;
//		}else if(gameID==7){
//			tcGameID = 9;
//		}else if(gameID==9){
//			tcGameID = 11;
//		}else if(gameID==10){
//			tcGameID = 10;
//		}else{
//			return null;
//		}
//
//		Integer showIssue = Integer.valueOf(issue);
//		issue = String.valueOf(showIssue+1);
//
//		StringBuffer sb = new StringBuffer();
//		sb.append(LOTTERY_ZC_URL);
//		sb.append("_ltype=").append(tcGameID);
//		String url = sb.toString() + "&_term=" + issue;
//		String json = HttpClientHelper.httpGet(url);
//		JSONArray array = JSONArray.fromObject(json);
//		JSONObject root = array.getJSONObject(0);
//		JSONObject lottery = root.getJSONObject("lottery");
//		if(lottery.isNullObject()) {
//			issue = (Integer.valueOf(issue.substring(0, 2))+1) + "001";
//			url = sb.toString() + "&_term=" + issue;
//			json = HttpClientHelper.httpGet(url);
//			array = JSONArray.fromObject(json);
//			root = array.getJSONObject(0);
//			lottery = root.getJSONObject("lottery");
//			if(lottery.isNullObject()) {
//				return null;
//			}
//		}
//
//        long timeMillis = lottery.getJSONObject("openTime").getLong("time");
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(timeMillis);
//		//开始销售时间
//        Date startSaleTime = DateFormatHelper.formatDate(lottery.get("sTime").toString(), DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//        //结束销售时间
//        Date endSaleTime = calendar.getTime();
//        //开奖时间
//        Date drawSaleTime = calendar.getTime();
//        System.out.println(endSaleTime);
//        System.out.println("期号:"+issue);
//        //组装对象写入缓存
//		// 新增game_issue游戏期信息
//		GameIssue gameIssue = new GameIssue();
//		// 游戏代码
//		gameIssue.setGameId(gameID);
//		// 显示销售期号
//		gameIssue.setShowIssue(issue);
//		// 开始销售时间
//		gameIssue.setStartSaleTime(startSaleTime);
//		// 结束销售时间
//		gameIssue.setEndSaleTime(endSaleTime);
//		// 开奖时间
//		gameIssue.setDrawTime(drawSaleTime);
//		// 期状态 默认为0
//		gameIssue.setStatus(100);
//		// 是否是当天最后一期 默认为0
//		gameIssue.setIsLastIssue(0);
//		return gameIssue;
//	}
//
//	/**
//	 * 通过游戏ID和销售期号获得该销售期号的详细信息
//	 * @param gameID
//	 * @param showIssue
//	 * @return
//	 */
//	public static GameIssue getGameIssueByShowIssue(Integer gameID,String showIssue) {
//		Integer tcGameID = null;
//		if(gameID == 23) {
//			return get11x5GameIssueByShowIssue(gameID, showIssue);
//		}else if (gameID==1){
//			tcGameID = 4;
//		}else if (gameID==4){
//			tcGameID = 6;
//		}else if (gameID==2){
//			tcGameID = 8;
//		}else if(gameID==7){
//			tcGameID = 9;
//		}else if(gameID==9){
//			tcGameID = 11;
//		}else if(gameID==10){
//			tcGameID = 10;
//		}else{
//			return null;
//		}
//
//		StringBuffer sb = new StringBuffer();
//		sb.append(LOTTERY_ZC_URL);
//		sb.append("_ltype=").append(tcGameID);
//		sb.append("&_term=").append(showIssue);
//		String json = HttpClientHelper.httpGet(sb.toString());
//		JSONArray array = JSONArray.fromObject(json);
//		JSONObject root = array.getJSONObject(0);
//		JSONObject lottery = root.getJSONObject("lottery");
//
//		if(lottery.isNullObject()) {
//			return null;
//		}
//
//        long timeMillis = lottery.getJSONObject("openTime").getLong("time");
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(timeMillis);
//		//开始销售时间
//        Date startSaleTime = DateFormatHelper.formatDate(lottery.get("sTime").toString(), DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//        //结束销售时间
//        Date endSaleTime = calendar.getTime();
//        //开奖时间
//        Date drawSaleTime = calendar.getTime();
//        System.out.println(endSaleTime);
//        System.out.println("期号:"+showIssue);
//        //组装对象写入缓存
//		// 新增game_issue游戏期信息
//		GameIssue gameIssue = new GameIssue();
//		// 游戏代码
//		gameIssue.setGameId(gameID);
//		// 显示销售期号
//		gameIssue.setShowIssue(showIssue);
//		// 开始销售时间
//		gameIssue.setStartSaleTime(startSaleTime);
//		// 结束销售时间
//		gameIssue.setEndSaleTime(endSaleTime);
//		// 开奖时间
//		gameIssue.setDrawTime(drawSaleTime);
//		// 期状态 默认为0
//		gameIssue.setStatus(100);
//		// 是否是当天最后一期 默认为0
//		gameIssue.setIsLastIssue(0);
//		return gameIssue;
//	}
//
//
//	/**
//	 * 自造11选5新期算法
//	 * @param gameID
//	 * @param issue
//	 * @return
//	 */
//	public static GameIssue get11x5GameNewest(Integer gameID,String issue) {
//        try {
//			//时间
//			String timeStr = "20"+issue.substring(0, issue.length()-2);
//			//当前期数
//			String numStr = issue.substring(issue.length()-2, issue.length());
//			DateFormat format = new SimpleDateFormat("yyyyMMdd");
//			Date time = format.parse(timeStr);
//			int num = Integer.parseInt(numStr);
//
//			//当前期的截止销售时间则是下一期的起始销售时间
//			String startSaleTimeStr = DateFormatHelper.parseDate(time, "yyyy-MM-dd ") + Game11x5Enum.valueOf("T"+num).getEndSaleTime();
//			if(num == 84) {
//				//如果当前期数是84期说明是当天的最后一期,那么下一期就是第二天的第一期
//				num = 1;
//				time.setDate(time.getDate()+1);
//
//				//如果第二天是农历腊月三十 那么往后面追加7天，因为7天年假不开盘
//				Calendar today = Calendar.getInstance();
//				today.setTime(time);
//				Lunar lunar = new Lunar(today);
//				if(lunar.toString().indexOf("腊月三十") != -1) {
//					time.setDate(time.getDate()+7);
//				}
//			}else {
//				num += 1;
//			}
//
//			//获取当前期销售截止时间 加上年月日
//			String endSaleTimeStr = DateFormatHelper.parseDate(time, "yyyy-MM-dd ") + Game11x5Enum.valueOf("T"+num).getEndSaleTime();
//
//			//期号
//			if(num < 10) {
//				numStr = "0"+num;
//			}else {
//				numStr = String.valueOf(num);
//			}
//			String nextIssue =  DateFormatHelper.parseDate(time, "yyMMdd")+numStr;
//
//			System.out.println("起始销售时间:"+startSaleTimeStr);
//			System.out.println("截止销售时间:"+endSaleTimeStr);
//			System.out.println(nextIssue);
//
//			//开始销售时间
//	        Date startSaleTime = DateFormatHelper.formatDate(startSaleTimeStr, DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//	        //结束销售时间
//	        Date endSaleTime = DateFormatHelper.formatDate(endSaleTimeStr, DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//	        //开奖时间
//	        Date drawSaleTime = DateFormatHelper.formatDate(endSaleTimeStr, DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//	        //组装对象写入缓存
//			// 新增game_issue游戏期信息
//			GameIssue gameIssue = new GameIssue();
//			// 游戏代码
//			gameIssue.setGameId(gameID);
//			// 显示销售期号
//			gameIssue.setShowIssue(nextIssue);
//			// 开始销售时间
//			gameIssue.setStartSaleTime(startSaleTime);
//			// 结束销售时间
//			gameIssue.setEndSaleTime(endSaleTime);
//			// 开奖时间
//			gameIssue.setDrawTime(drawSaleTime);
//			// 期状态 默认为0
//			gameIssue.setStatus(100);
//			// 是否是当天最后一期 默认为0
//			gameIssue.setIsLastIssue(0);
//			return gameIssue;
//        } catch (ParseException e) {
//		}
//		return null;
//	}
//
//	/**
//	 * 自造11选5历史期算法
//	 * @param gameID
//	 * @param issue
//	 * @return
//	 */
//	public static GameIssue get11x5GameIssueByShowIssue(Integer gameID,String showIssue) {
//
//		try {
//			//时间
//			String timeStr = "20"+showIssue.substring(0, showIssue.length()-2);
//			//当前期数
//			String numStr = showIssue.substring(showIssue.length()-2, showIssue.length());
//			DateFormat format = new SimpleDateFormat("yyyyMMdd");
//			Date time = format.parse(timeStr);
//			int num = Integer.parseInt(numStr);
//
//			//当前期的起始销售时间 加上年月日
//			String startSaleTimeStr = DateFormatHelper.parseDate(time, "yyyy-MM-dd ") + Game11x5Enum.valueOf("T"+num).getStartSaleTime();
//			//获取当前期销售截止时间 加上年月日
//			String endSaleTimeStr = DateFormatHelper.parseDate(time, "yyyy-MM-dd ") + Game11x5Enum.valueOf("T"+num).getEndSaleTime();
//
//			System.out.println("起始销售时间:"+startSaleTimeStr);
//			System.out.println("截止销售时间:"+endSaleTimeStr);
//			System.out.println(showIssue);
//			//开始销售时间
//	        Date startSaleTime = DateFormatHelper.formatDate(startSaleTimeStr, DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//	        //结束销售时间
//	        Date endSaleTime = DateFormatHelper.formatDate(endSaleTimeStr, DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//	        //开奖时间
//	        Date drawSaleTime = DateFormatHelper.formatDate(endSaleTimeStr, DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//	        //组装对象写入缓存
//			// 新增game_issue游戏期信息
//			GameIssue gameIssue = new GameIssue();
//			// 游戏代码
//			gameIssue.setGameId(gameID);
//			// 显示销售期号
//			gameIssue.setShowIssue(showIssue);
//			// 开始销售时间
//			gameIssue.setStartSaleTime(startSaleTime);
//			// 结束销售时间
//			gameIssue.setEndSaleTime(endSaleTime);
//			// 开奖时间
//			gameIssue.setDrawTime(drawSaleTime);
//			// 期状态 默认为0
//			gameIssue.setStatus(100);
//			// 是否是当天最后一期 默认为0
//			gameIssue.setIsLastIssue(0);
//			return gameIssue;
//        } catch (ParseException e) {
//		}
//		return null;
//	}
//
//
//	/***
//	 * 游戏编号 1---大乐透, 2 ---七星彩 , 4---排列5, 7 --- 14场胜负彩,  9-----4场进球, 10---6场半全场
//	 * 体彩网对应游戏编码 4---大乐透, 8 ---七星彩 ,5---排列3, 6---排列5, 9 --- 14场胜负彩,  11-----4场进球, 10---6场半全场
//	 *
//	 * @param gameID
//	 * @param issue
//	 * @return
//	 * @throws InterruptedException
//	 */
//	public static GameGrabResp getCatchWinNum(Integer gameID, String issue) {
//		if (gameID == 23) {
//			String url_str = "http://gd11x5.icaile.com/chart.asp?beginperiod=" + issue + "&endperiod=" + issue;
//			GameGrabResp gameGrabResp = getGd11x5(url_str, issue);
//			if(gameGrabResp == null) {
//				return CatchWinNumUtil.getGdlottery11x5(issue);
//			}
//		} else if (gameID == 1) {
//			return getGameNoticeGrabResp(4, issue);
//		} else if (gameID == 4) {
//			return getGameNoticeGrabResp(6, issue);
//		} else if (gameID == 2) {
//			return getGameNoticeGrabResp(8, issue);
//		} else if (gameID == 7) {
//			// 体彩网中14场胜负彩gameID为9
//			return getGameZcNoticeGrabResp(9, issue);
//		} else if (gameID == 9) {
//			// 体彩网中4场进球gameID为11
//			return getGameZcNoticeGrabResp(11, issue);
//		} else if (gameID == 10) {
//			// 体彩网中6场半全场gameID为10
//			return getGameZcNoticeGrabResp(10, issue);
//		}
//		return null;
//	}
//
//	/**
//	 * 根据游戏编号，和期号获取（爬取）中间期号的list集合
//	 *
//	 * @param gameID
//	 *            游戏编号 1---大乐透, 2 ---七星彩 , 4---排列5, 7 --- 14场胜负彩,  9-----4场进球, 10---6场半全场
//	 *            体彩网对应游戏编码 4---大乐透, 8 ---七星彩 ,5---排列3, 6---排列5, 9 --- 14场胜负彩,  11-----4场进球, 10---6场半全场
//	 * @param curShowIssue
//	 *            game表中当前显示期号
//	 * @param newestShowIssue
//	 *            乐彩网当前销售最新期号
//	 * @return
//	 * @throws InterruptedException
//	 */
//	public static List<String> getCompensateShowIssue(Integer gameID,Integer curShowIssue, Integer newestShowIssue) {
//		if(gameID == 1) {
//			return getLotteryShowIssue(4, curShowIssue,newestShowIssue);
//		} else if (gameID == 4) {
//			return getLotteryShowIssue(6, curShowIssue,newestShowIssue);
//		} else if (gameID == 2) {
//			return getLotteryShowIssue(8, curShowIssue,newestShowIssue);
//		} else if (gameID == 23) {
//			// 广东11选5
//			return getGd11x5ShowIssue(String.valueOf(curShowIssue),String.valueOf(newestShowIssue));
//		} else if (gameID == 7) {
//			return getLotteryShowIssue(9, curShowIssue,newestShowIssue);
//		} else if (gameID == 9) {
//			return getLotteryShowIssue(11, curShowIssue,newestShowIssue);
//		} else if (gameID == 10) {
//			return getLotteryShowIssue(10, curShowIssue,newestShowIssue);
//		}
//		return null;
//	}
//
//	public static List<String> getLotteryShowIssue(Integer gameID,
//			Integer curShowIssue, Integer newestShowIssue) {
//		try {
//			StringBuffer sb = new StringBuffer();
//			sb.append(LOTTERY_ZC_ISSUE_URL);
//			sb.append("_ltype=").append(gameID);
//			String json = HttpClientHelper.httpGet(sb.toString());
//			JSONArray array = JSONArray.fromObject(json);
//			JSONObject root = array.getJSONObject(0);
//			JSONArray tremList = root.getJSONArray("tremList");
//
//			List<String> list = new ArrayList<String>();
//			Iterator<Object> it = tremList.iterator();
//			while (it.hasNext()) {
//				JSONObject obj = (JSONObject) it.next();
//				String issue = obj.getString("term");
//				if (curShowIssue < Integer.valueOf(issue)
//						&& Integer.valueOf(issue) < newestShowIssue) {
//					list.add(issue);
//				}
//			}
//			Collections.sort(list);
//			return list;
//		} catch (Exception e) {
//			logger.error("体彩接口获取游戏ID:" + gameID + ",期号信息出错!");
//			return null;
//		}
//	}
//
//	/**
//	 * 获取游戏开奖公告信息 (大乐透，七星彩，排列5)
//	 * 游戏编号 1---大乐透, 2 ---七星彩 , 4---排列5
//	 * 体彩网对应游戏编码 4---大乐透, 8 ---七星彩 ,5---排列3, 6---排列5
//	 * @param gameID
//	 * @param showIssue
//	 * @return
//	 */
//	public static GameGrabResp getGameNoticeGrabResp(Integer gameID,String showIssue) {
//		try {
//			StringBuffer sb = new StringBuffer();
//			sb.append(LOTTERY_ZC_URL);
//			sb.append("_ltype=").append(gameID);
//			sb.append("&_term=").append(showIssue);
//			String json = HttpClientHelper.httpGet(sb.toString());
//			JSONArray array = JSONArray.fromObject(json);
//			JSONObject root = array.getJSONObject(0);
//			JSONObject data = root.getJSONObject("lottery");
//
//			// 获取中奖号码
//			//String[] number = (String[]) root.getJSONArray("codeNumber").toArray();
//			List<String> number = JSONArray.toList(root.getJSONArray("codeNumber"),String.class,new JsonConfig());
//			GameGrabResp gameGrabResp = new GameGrabResp();
//			GameGrabWinNum winNum = new GameGrabWinNum();
//			winNum.setKey("tc");
//			winNum.setData(number.toArray(new String[number.size()]));
//
//			List<GameGrabWinNum> grabNum = new ArrayList<GameGrabWinNum>();
//			grabNum.add(winNum);
//			gameGrabResp.setGameGrabWinNum(grabNum);
//
//			// 获取中奖信息
//			List<GameGrabWinResult> winResult = new ArrayList<GameGrabWinResult>();
//			JSONArray resultDetail = root.getJSONArray("details");
//			Iterator<Object> it = resultDetail.iterator();
//			while (it.hasNext()) {
//				JSONObject obj = (JSONObject) it.next();
//				GameGrabWinResult grabWinResult = new GameGrabWinResult();
//				grabWinResult.setName(obj.getString("level"));
//				String piece = obj.getString("piece");
//				String money = obj.getString("money");
//
//				if(StringUtils.isNotBlank(piece)) {
//					piece = new BigDecimal(piece.replace(",", "")).setScale(0, BigDecimal.ROUND_DOWN).toString();
//				}else {
//					piece = "0";
//				}
//				if(StringUtils.isNotBlank(money)) {
//					money = new BigDecimal(money.replace(",", "")).setScale(0, BigDecimal.ROUND_DOWN).toString();
//				}else {
//					money = "0";
//				}
//				grabWinResult.setBet(piece);
//				grabWinResult.setPrize(money);
//				winResult.add(grabWinResult);
//			}
//			if(gameID == 4) {
//				//大乐透游戏开奖只需要前11个奖项
//				gameGrabResp.setGameGrabWinResult(winResult.subList(0, 11));
//			}else if(gameID == 6){
//				//如果是排列5游戏 那么需要去获取排列三游戏的开奖公告
//				GameGrabResp gameGrabRespPl3 =  getGameNoticeGrabResp(5, showIssue);
//				//设置开奖公告信息
//				GameGrabWinResult grabWinResult = winResult.get(0);
//				grabWinResult.setName("排列五");
//				gameGrabRespPl3.getGameGrabWinResult().add(grabWinResult);
//				gameGrabResp.setGameGrabWinResult(gameGrabRespPl3.getGameGrabWinResult());
//				//设置排列三游戏的销售金额
//				gameGrabResp.setPlsSaleAmount(gameGrabRespPl3.getSaleAmount());
//			}else{
//				gameGrabResp.setGameGrabWinResult(winResult);
//			}
//
//			String sale_amount = (String) data.get("totalSales");
//			String pool_amount = (String) data.get("pool");
//			// 获取销售金额
//			if (StringUtils.isNotBlank(sale_amount)) {
//				gameGrabResp.setSaleAmount(new BigDecimal(sale_amount.replace(
//						",", "")).setScale(0, BigDecimal.ROUND_DOWN));
//			}
//			// 获取奖池金额
//			if (StringUtils.isNotBlank(pool_amount)) {
//				gameGrabResp.setPoolAmount(new BigDecimal(pool_amount.replace(
//						",", "")).setScale(0, BigDecimal.ROUND_DOWN));
//			}
//			return gameGrabResp;
//		} catch (Exception e) {
//			logger.error("体彩接口获取游戏ID:" + gameID + ",中奖信息出错!");
//			return null;
//		}
//
//	}
//
//
//	/**
//	 * 获取足球游戏开奖公告信息 体彩网对应游戏gameID 9 --- 14场胜负彩, 11-----4场进球, 10---6场半全场
//	 *
//	 * @param gameID
//	 * @param showIssue
//	 * @return
//	 */
//	private static GameGrabResp getGameZcNoticeGrabResp(Integer gameID,
//			String showIssue) {
//		try {
//			StringBuffer sb = new StringBuffer();
//			sb.append(LOTTERY_ZC_URL);
//			sb.append("_ltype=").append(gameID);
//			sb.append("&_term=").append(showIssue);
//			String json = HttpClientHelper.httpGet(sb.toString());
//			JSONArray array = JSONArray.fromObject(json);
//			JSONObject root = array.getJSONObject(0);
//			JSONObject data = root.getJSONObject("lottery");
//
//			// 获取中奖号码
//			String number = data.get("number").toString().replace("＋", "")
//					.replace(" ", "");
//			char[] numChar = number.toCharArray();
//			String[] numStr = null;
//			if (gameID == 9) {
//				numStr = new String[14];
//				for (int i = 0; i < numStr.length; i++) {
//					numStr[i] = String.valueOf(numChar[i]);
//				}
//			} else if (gameID == 11) {
//				numStr = new String[8];
//				for (int i = 0; i < numStr.length; i++) {
//					numStr[i] = String.valueOf(numChar[i]);
//				}
//			} else if (gameID == 10) {
//				numStr = new String[12];
//				for (int i = 0; i < numStr.length; i++) {
//					numStr[i] = String.valueOf(numChar[i]);
//				}
//			}
//
//			GameGrabResp gameGrabResp = new GameGrabResp();
//			GameGrabWinNum winNum = new GameGrabWinNum();
//			winNum.setKey("zc");
//			winNum.setData(numStr);
//
//			List<GameGrabWinNum> grabNum = new ArrayList<GameGrabWinNum>();
//			grabNum.add(winNum);
//			gameGrabResp.setGameGrabWinNum(grabNum);
//
//			// 获取中奖信息
//			List<GameGrabWinResult> winResult = new ArrayList<GameGrabWinResult>();
//			JSONArray resultDetail = root.getJSONArray("details");
//			Iterator<Object> it = resultDetail.iterator();
//			while (it.hasNext()) {
//				JSONObject obj = (JSONObject) it.next();
//				GameGrabWinResult grabWinResult = new GameGrabWinResult();
//				grabWinResult.setName(obj.getString("level"));
//				String piece = new BigDecimal(obj.getString("piece").replace(
//						",", "")).setScale(0, BigDecimal.ROUND_DOWN).toString();
//				String money = new BigDecimal(obj.getString("money").replace(
//						",", "")).setScale(0, BigDecimal.ROUND_DOWN).toString();
//				grabWinResult.setBet(piece);
//				grabWinResult.setPrize(money);
//				winResult.add(grabWinResult);
//			}
//			gameGrabResp.setGameGrabWinResult(winResult);
//
//			// 获取场次信息
//			JSONArray matchResult = root.getJSONArray("matchResults");
//			Iterator<Object> matchIt = matchResult.iterator();
//			StringBuffer matchSb = new StringBuffer();
//			if (gameID == 10) {
//				int seq = 1;
//				while (matchIt.hasNext()) {
//					JSONObject obj = (JSONObject) matchIt.next();
//					if (seq % 2 != 0) {
//						String homeTeam = obj.getString("homeTeamView");
//						homeTeam = homeTeam.replace("<br>", "").replace(
//								"&nbsp;", "");
//						matchSb.append(homeTeam).append("-");
//					}
//					seq++;
//				}
//			} else {
//				while (matchIt.hasNext()) {
//					JSONObject obj = (JSONObject) matchIt.next();
//					String homeTeam = obj.getString("homeTeamView");
//					homeTeam = homeTeam.replace("<br>", "").replace("&nbsp;",
//							"");
//					matchSb.append(homeTeam).append("-");
//				}
//			}
//			gameGrabResp.setMatchData(matchSb.toString());
//
//			String sale_amount = (String) data.get("totalSales");
//			String pool_amount = (String) data.get("pool");
//			// 获取销售金额
//			if (StringUtils.isNotBlank(sale_amount)) {
//				gameGrabResp.setSaleAmount(new BigDecimal(sale_amount.replace(
//						",", "")).setScale(0, BigDecimal.ROUND_DOWN));
//			}
//			// 获取奖池金额
//			if (StringUtils.isNotBlank(pool_amount)) {
//				gameGrabResp.setPoolAmount(new BigDecimal(pool_amount.replace(
//						",", "")).setScale(0, BigDecimal.ROUND_DOWN));
//			}
//			// 获取任九销量
//			gameGrabResp.setPlsSaleAmount(getRenJiuSaleAmount(showIssue));
//			return gameGrabResp;
//		} catch (Exception e) {
//			logger.error("体彩接口获取足球游戏ID:" + gameID + ",中奖信息出错!");
//			return null;
//		}
//
//	}
//
//	/**
//	 * 获取任九总销量
//	 *
//	 * @param showIssue
//	 * @return
//	 * @throws Exception
//	 * @throws
//	 */
//	public static BigDecimal getRenJiuSaleAmount(String showIssue)
//			throws Exception {
//		String url = "http://www.lottery.gov.cn/historykj/history.jspx?page=false&_ltype=sfc&termNum=0&startTerm=" + showIssue + "&endTerm=" + showIssue;
//		Document document = GrabHel.getDoc(url);
//		// 网页中表格tbody的tr对象
//		Elements trs = document.select("tbody").select("tr");
//		// 获取查询结果第一行行数
//		Element tr = trs.get(0);
//		// 获取单元格总列数
//		Elements tds = tr.children();
//		// 获取第一列值 期号
//		String issueV = tds.get(0).text();
//		if (showIssue.equals(issueV)) {
//			// 获取第13列值 任九总销量
//			String renjiuSaleAmount = tds.get(12).text();
//			// 写入销售额 不保留小数
//			return new BigDecimal(renjiuSaleAmount.replace(",", "")).setScale(0, BigDecimal.ROUND_DOWN);
//		} else {
//			throw new Exception();
//		}
//	}
//
//	/***
//	 * 网页解析获取当天广东11选5 开奖信息
//	 * @param document
//	 * @param issue
//	 * @return
//	 */
//	private static GameGrabResp getGd11x5(String url,String issue){
//		try{
//			Document document = GrabHel.getDoc(url);
//			//网页中表格tbody的tr对象
//			Elements trs = document.select("tbody").select("tr");
//
//			if( null != trs) {
//
//				//获取查询结果第一行行数
//				Element tr =trs.get(0);
//				//获取单元格总列数
//				Elements tds = tr.children();
//				//获取第一列值 期号
//				String issueV = tds.get(0).text().replace("-", "");
//				if(issue.equals(issueV)) {
//					StringBuffer redNum = new StringBuffer();
//					//获取中奖号码
//					//遍历单元格
//					for(int i = 0; i < tds.size(); i++) {
//						Element element =tds.get(i);
//						//获取单元格第一列文本
//						if("c_ba2636".equals(element.className())){
//							//开奖号
//							redNum.append(element.text()).append(",");
//						}
//					}
//					//组装对象
//					GameGrabResp gameGrabResp=new GameGrabResp();
//					List<GameGrabWinNum> gameGrabWinNumList =new ArrayList<GameGrabWinNum>();
//					GameGrabWinNum redWinNum=new GameGrabWinNum();
//					String[] red =redNum.toString().split(",");
//					redWinNum.setKey("red");
//    				redWinNum.setData(red);
//    				//写入红球号码
//    				gameGrabWinNumList.add(redWinNum);
//    				gameGrabResp.setGameGrabWinNum(gameGrabWinNumList);
//    				return gameGrabResp;
//				}else{
//					return null;
//				}
//			}
//		}catch (Exception e) {
//
//		}
//		return null;
//	}
//
//	public static List<String> getGd11x5ShowIssue(String curShowIssue,String newestShowIssue){
//		//日期
//		String t1 = "20"+curShowIssue.substring(0, curShowIssue.length()-2);
//		String t2 = "20"+newestShowIssue.substring(0, newestShowIssue.length()-2);
//		String issue1 = curShowIssue.substring(curShowIssue.length()-2, curShowIssue.length());
//		String issue2 = newestShowIssue.substring(newestShowIssue.length()-2, newestShowIssue.length());
//		//期号
//		int num1 = Integer.parseInt(issue1);
//		int num2 = Integer.parseInt(issue2);
//		List<String> list = new ArrayList<String>();
//		if(num1<1 || num1>84 ||num2<1 || num2>84){
//			return list;
//		}
//		DateFormat format = new SimpleDateFormat("yyyyMMdd");
//		try {
//			//计算所需天数
//	        Long time1 = format.parse(t1).getTime();
//	        Long time2 = format.parse(t2).getTime();
//
//	        Long days = (time2 - time1 )/(24*3600*1000);
//        	Long m = 24*3600*1000L;
//	        //System.out.println(days);
//	        for(int i=0; i<=days+1; i++){
//	        	Date date = new Date(time1+i*m);
//	        	String time = format.format(date) ;
//	        	if(i==0){
//	        		for(int j=num1; j<=((days==0)?num2:84); j++){
//	        			list.add(time+((j<10)?"0":"")+j);
//	        		}
//	        	}else if(i>0 && i<days){
//	        		for(int j=1; j<=84; j++){
//		        		list.add(time+((j<10)?"0":"")+j);
//		        	}
//	        	}else if(i == days){
//	        		for(int j=1; j<=num2; j++){
//		        		list.add(time+((j<10)?"0":"")+j);
//		        	}
//	        	}
//	        }
//
//	        //去掉头
//			list.remove(0);
//			//去掉尾
//			list.remove(list.size()-1);
//			List<String> strList = new ArrayList<String>();
//			for (String string : list) {
//				strList.add(string.substring(2));
//			}
//	        return strList;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//
//	public static void main(String[] args) throws InterruptedException {
////		List<String> srtList = GameGrabLotteryUtil.getCompensateShowIssue(4, 17041, 17082);
////		for (String string : srtList) {
////			System.out.println(string);
////		}
////		GameGrabLotteryUtil.getGameNewest(6, "16359");
////		GameGrabLotteryUtil.getGameIssueByShowIssue(6, "16359");
////		GameGrabLotteryUtil.getGameNewest(23,"18021484");
////		System.out.println("------------------");
////		GameGrabLotteryUtil.getGameIssueByShowIssue(23,"18021484");
//		GameGrabResp gameGrabResp = CatchWinNumUtil.getCatchWinNum(1,"17080");
//		for (GameGrabWinNum gameGrabWinNum : gameGrabResp.getGameGrabWinNum()) {
//        	for (String datas : gameGrabWinNum.getData()) {
//				//开奖号码
//				System.out.print(datas+",");
//			}
//		}
//		System.out.println("中奖信息为:");
//		if(!CollectionUtils.isEmpty(gameGrabResp.getGameGrabWinResult())) {
//			for (GameGrabWinResult gameGrabWinResult : gameGrabResp.getGameGrabWinResult()) {
//				System.out.print("奖项:"+gameGrabWinResult.getName());
//				System.out.print(" 中奖条件:"+gameGrabWinResult.getKey());
//				System.out.print(" 中奖注数:"+gameGrabWinResult.getBet());
//				System.out.println(" 单注奖金:"+gameGrabWinResult.getPrize());
//			}
//		}
//        System.out.println("排列3/任九销售金额:"+gameGrabResp.getPlsSaleAmount());
//        System.out.println("销售金额:"+gameGrabResp.getSaleAmount());
//        System.out.println("奖池金额:"+gameGrabResp.getPoolAmount());
//	}
//}