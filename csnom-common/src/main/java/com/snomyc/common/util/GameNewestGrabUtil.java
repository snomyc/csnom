package com.snomyc.common.util;//package com.genlot.ips.util;
//
//import java.math.BigDecimal;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.commons.lang.StringUtils;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//import org.springframework.util.CollectionUtils;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import net.sf.json.JsonConfig;
//
//import com.genlot.ips.common.helper.DateFormatHelper;
//import com.genlot.ips.common.helper.HttpClientHelper;
//import com.genlot.ips.domain.GameGrabResp;
//import com.genlot.ips.domain.GameGrabWinNum;
//import com.genlot.ips.domain.GameGrabWinResult;
//import com.genlot.ips.domain.hotline.GameIssue;
//import com.genlot.ips.grab.GrabHel;
//
///**
// * @author yangcan
// * 体彩网上获取最新期工具类
// */
//public class GameNewestGrabUtil {
//	
//	//查询游戏最新期参数
//	private static String LECAI_GAME_NEWEST_URL = "http://baidu.lecai.com/lottery/ajax_current.php?lottery_type=";
//	
//	//查询游戏中奖号码开奖公告信息
//	private static String LECAT_GAME_WIN_URL = "http://baidu.lecai.com/lottery/draw/ajax_get_detail.php?";
//	
//	private static String LECAT_GAME_ZC_MATCH = "http://www.lecai.com/lottery/ajax_get_match_data.php?";
//
//	//大乐透
//	private static String DLT_URL = "http://trend.lecai.com/dlt/redBaseTrend.action?recentPhase=100&onlyBody=false&phaseOrder=up&coldHotOrder=number";
//	//七星彩
//	private static String QXC_URL = "http://trend.lecai.com/qxc/baseTrend.action?recentPhase=100&onlyBody=false&phaseOrder=up&coldHotOrder=number";
//	//排列5
//	private static String PL5_URL = "http://trend.lecai.com/pl5/baseTrend.action?recentPhase=100&onlyBody=false&phaseOrder=up&coldHotOrder=number";
//	
//	
//	/**
//	 * 1---大乐透, 2 ---七星彩, 23 -- 广东11选5, 3---排列3 ,4---排列5, 7 --- 14场胜负彩,  9-----4场进球, 10---6场半全场
//	 * 获取游戏最新期数据信息
//	 * @param gameID
//	 * @return
//	 */
//	public static GameIssue getGameNewest(Integer gameID) {
//	    String json = HttpClientHelper.httpGet(LECAI_GAME_NEWEST_URL+gameID);
//        JSONObject root = JSONObject.fromObject(json);
//        JSONObject data =root.getJSONObject("data");
//        //获取新期期号
//        String issue = data.get("phase").toString();
//        
//        //开始销售时间
//        Date startSaleTime = DateFormatHelper.formatDate(data.get("time_startsale").toString(), DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//        //结束销售时间
//        Date endSaleTime = DateFormatHelper.formatDate(data.get("time_endsale").toString(), DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//        //开奖时间
//        Date drawSaleTime = DateFormatHelper.formatDate(data.get("time_draw").toString(), DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//        
//        System.out.println(startSaleTime);
//        System.out.println(endSaleTime);
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
//        
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
//		StringBuffer sb = new StringBuffer();
//		sb.append(LECAT_GAME_WIN_URL);
//		sb.append("lottery_type=").append(gameID);
//		sb.append("&phase=").append(showIssue);
//		String json = HttpClientHelper.httpGet(sb.toString());
//        JSONObject root = JSONObject.fromObject(json);
//        JSONObject data =root.getJSONObject("data");
//        
//        //开始销售时间
//        Date startSaleTime = DateFormatHelper.formatDate(data.get("time_startsale").toString(), DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//        //结束销售时间
//        Date endSaleTime = DateFormatHelper.formatDate(data.get("time_endsale").toString(), DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//        //开奖时间
//        Date drawSaleTime = DateFormatHelper.formatDate(data.get("time_draw").toString(), DateFormatHelper.YYYY_MM_DD_HH_MM_SS);
//        
//        //组装对象写入缓存
//  		// 新增game_issue游戏期信息
//  		GameIssue gameIssue = new GameIssue();
//  		// 游戏代码
//  		gameIssue.setGameId(gameID);
//  		// 显示销售期号
//  		gameIssue.setShowIssue(showIssue);
//  		// 开始销售时间
//  		gameIssue.setStartSaleTime(startSaleTime);
//  		// 结束销售时间
//  		gameIssue.setEndSaleTime(endSaleTime);
//  		// 开奖时间
//  		gameIssue.setDrawTime(drawSaleTime);
//  		// 期状态 默认为0
//  		gameIssue.setStatus(100);
//  		// 是否是当天最后一期 默认为0
//  		gameIssue.setIsLastIssue(0);
//  		return gameIssue;
//	}
//	
//	/**
//	 * 获取中奖号码信息
//	 * @param gameID
//	 * @param showIssue
//	 * @return
//	 */
//	public static GameGrabResp getGameGrabResp(Integer gameID,String showIssue) {
//		try{
//			StringBuffer sb = new StringBuffer();
//			sb.append(LECAT_GAME_WIN_URL);
//			sb.append("lottery_type=").append(gameID);
//			sb.append("&phase=").append(showIssue);
//			String json = HttpClientHelper.httpGet(sb.toString());
//	        JSONObject root = JSONObject.fromObject(json);
//	        JSONObject data =root.getJSONObject("data");
//			
//	        GameGrabResp gameGrabResp = new GameGrabResp();
//	        //获取中奖号码
//	        JSONArray result = data.getJSONObject("result").getJSONArray("result");
//	        List<GameGrabWinNum> grabNum = JSONArray.toList(result, new GameGrabWinNum(),new JsonConfig());
//	        gameGrabResp.setGameGrabWinNum(grabNum);
//			return gameGrabResp;
//		}catch (Exception e) {
//			return null;
//		}
//		
//	}
//	
//	/**
//	 * 获取游戏开奖公告信息 只适用于足球游戏
//	 * @param gameID
//	 * @param showIssue
//	 * @return
//	 */
//	public static GameGrabResp getGameZcNoticeGrabResp(Integer gameID,String showIssue) {
//		try{
//			StringBuffer sb = new StringBuffer();
//			sb.append(LECAT_GAME_WIN_URL);
//			sb.append("lottery_type=").append(gameID);
//			sb.append("&phase=").append(showIssue);
//			String json = HttpClientHelper.httpGet(sb.toString());
//	        JSONObject root = JSONObject.fromObject(json);
//	        JSONObject data =root.getJSONObject("data");
//			
//	        GameGrabResp gameGrabResp = new GameGrabResp();
//	        //获取中奖号码
//	        JSONArray result = data.getJSONObject("result").getJSONArray("result");
//	        List<GameGrabWinNum> grabNum = JSONArray.toList(result, new GameGrabWinNum(),new JsonConfig());
//	        gameGrabResp.setGameGrabWinNum(grabNum);
//	        
//	        //获取中奖信息
//	        JSONArray resultDetail = data.getJSONObject("result_detail").getJSONArray("resultDetail");
//	        List<GameGrabWinResult> winResult = JSONArray.toList(resultDetail, new GameGrabWinResult(),new JsonConfig());
//	        gameGrabResp.setGameGrabWinResult(winResult);
//	        
//	        //获取任九游戏相关的奖等，销售总额信息
//	        getZcRenJiu(8, showIssue, gameGrabResp);
//	        
//	        String sale_amount = (String)data.get("sale_amount");
//	        String pool_amount = (String)data.get("pool_amount");
//        	 //获取销售金额
//	        if(StringUtils.isNotBlank(sale_amount)) {
//	        	gameGrabResp.setSaleAmount(new BigDecimal(sale_amount));
//	        }else {
//	        	return null;
//	        }
//        	
//            //获取奖池金额
//	        if(StringUtils.isNotBlank(pool_amount)) {
//	        	gameGrabResp.setPoolAmount(new BigDecimal(pool_amount));
//	        }else {
//	        	return null;
//	        }
//	        
//	        //获取足球游戏场次信息
//	        gameGrabResp.setMatchData(getZcMatchData(gameID, showIssue));
//			return gameGrabResp;
//		}catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		
//	}
//	
//	/**
//	 * @param gameID
//	 * @param showIssue
//	 * @param gameGrabResp
//	 * @throws Exception
//	 */
//	/**
//	 * @param gameID
//	 * @param showIssue
//	 * @param gameGrabResp
//	 * @throws Exception
//	 */
//	public static void getZcRenJiu(Integer gameID,String showIssue,GameGrabResp gameGrabResp) throws Exception {
//		StringBuffer sb = new StringBuffer();
//		sb.append(LECAT_GAME_WIN_URL);
//		sb.append("lottery_type=").append(gameID);
//		sb.append("&phase=").append(showIssue);
//		String json = HttpClientHelper.httpGet(sb.toString());
//        JSONObject root = JSONObject.fromObject(json);
//        JSONObject data =root.getJSONObject("data");
//		
//        //获取中奖信息
//        JSONArray resultDetail = data.getJSONObject("result_detail").getJSONArray("resultDetail");
//        List<GameGrabWinResult> winResult = JSONArray.toList(resultDetail, new GameGrabWinResult(),new JsonConfig());
//        if(CollectionUtils.isEmpty(winResult)) {
//        	throw new Exception();
//        }
//        GameGrabWinResult gameGrabWinResult = winResult.get(0);
//        gameGrabWinResult.setName("任九");
//        //追加任九奖项
//        gameGrabResp.getGameGrabWinResult().addAll(winResult);
//        //获取任九销售金额
//        String sale_amount = (String)data.get("sale_amount");
//        //设置任九总销量
//        gameGrabResp.setPlsSaleAmount(new BigDecimal(sale_amount));
//	}
//	
//	/**
//	 * 获取足球游戏开奖场次信息,每个场次之间用-隔开
//	 * @param gameID
//	 * @param showIssue
//	 * @return
//	 */
//	public static String getZcMatchData(Integer gameID,String showIssue) {
//		StringBuffer sb = new StringBuffer();
//		sb.append(LECAT_GAME_ZC_MATCH);
//		sb.append("lottery_type=").append(gameID);
//		sb.append("&phase=").append(showIssue);
//		String json = HttpClientHelper.httpGet(sb.toString());
//        JSONObject root = JSONObject.fromObject(json);
//        JSONArray data =root.getJSONArray("data");
//        //遍历data
//        sb.delete(0, sb.length());
//        Iterator<Object> it = data.iterator();
//        if(gameID == 9) {
//        	while(it.hasNext()) {
//            	JSONObject obj = (JSONObject)it.next();
//            	sb.append(obj.getString("home_team")).append("-");
//            	sb.append(obj.getString("away_team")).append("-");
//            }
//        }else{
//        	while(it.hasNext()) {
//            	JSONObject obj = (JSONObject)it.next();
//            	String homeTeam = obj.getString("home_team");
//            	sb.append(homeTeam).append("-");
//            }
//        }
//        return sb.toString();
//	}
//	
//	/**
//	 * 获取游戏开奖公告信息 (该方法只适用于大乐透，七星彩)
//	 * @param gameID
//	 * @param showIssue
//	 * @return
//	 */
//	public static GameGrabResp getGameDltAndQxcNoticeGrabResp(Integer gameID,String showIssue) {
//		try{
//			StringBuffer sb = new StringBuffer();
//			sb.append(LECAT_GAME_WIN_URL);
//			sb.append("lottery_type=").append(gameID);
//			sb.append("&phase=").append(showIssue);
//			String json = HttpClientHelper.httpGet(sb.toString());
//	        JSONObject root = JSONObject.fromObject(json);
//	        JSONObject data =root.getJSONObject("data");
//			
//	        GameGrabResp gameGrabResp = new GameGrabResp();
//	        //获取中奖号码
//	        JSONArray result = data.getJSONObject("result").getJSONArray("result");
//	        List<GameGrabWinNum> grabNum = JSONArray.toList(result, new GameGrabWinNum(),new JsonConfig());
//	        gameGrabResp.setGameGrabWinNum(grabNum);
//	        
//	        //获取中奖信息
//	        JSONArray resultDetail = data.getJSONObject("result_detail").getJSONArray("resultDetail");
//	        List<GameGrabWinResult> winResult = JSONArray.toList(resultDetail, new GameGrabWinResult(),new JsonConfig());
//	        gameGrabResp.setGameGrabWinResult(winResult);
//	        
//	        if(!data.get("sale_amount").toString().equals("0") && !data.get("pool_amount").toString().equals("0")) {
//	        	 //获取销售金额 
//	        	BigDecimal sale_amount = new BigDecimal(data.get("sale_amount").toString());
//	            gameGrabResp.setSaleAmount(sale_amount);
//	            //获取奖池金额
//		        BigDecimal pool_amount = new BigDecimal(data.get("pool_amount").toString());
//		        gameGrabResp.setPoolAmount(pool_amount);
//	        }else {
//	        	GameGrabResp gameGrabResp2 = CatchWinNumUtil.getCatchWinNum(gameID,showIssue);
//	        	gameGrabResp.setSaleAmount(gameGrabResp2.getSaleAmount());
//	        	gameGrabResp.setPoolAmount(gameGrabResp2.getPoolAmount());
//	        }
//			return gameGrabResp;
//		}catch (Exception e) {
//			return null;
//		}
//		
//	}
//	
//	/**
//	 * @param gameID
//	 * @param curShowIssue game表中当前显示期号
//	 * @param newestShowIssue 乐彩网当前销售最新期号
//	 * @return
//	 */
//	public static List<String> getCompensateShowIssue(Integer gameID,Integer curShowIssue,Integer newestShowIssue) {
//		List<String> strList = new ArrayList<String>();
//		String url = "";
//		if(gameID == 1) {
//			url = DLT_URL;
//		}else if(gameID == 2) {
//			url = QXC_URL;
//		}else if(gameID == 4) {
//			url = PL5_URL;
//		}else if(gameID == 23) {
//			//广东11选5
//			return GameNewestGrabUtil.getGd11x5ShowIssue(String.valueOf(curShowIssue), String.valueOf(newestShowIssue));
//		}else if(gameID == 7) {
//			return CatchWinNumUtil.getZcCompensateShowIssue(gameID, curShowIssue, newestShowIssue);
//		}else if(gameID == 9) {
//			return CatchWinNumUtil.getZcCompensateShowIssue(gameID, curShowIssue, newestShowIssue);
//		}else if(gameID == 10) {
//			return CatchWinNumUtil.getZcCompensateShowIssue(gameID, curShowIssue, newestShowIssue);
//		}
//		
//		try {
//			Document doc = GrabHel.getDoc(url);
//			Elements elements = doc.getElementsByAttributeValue("class","chart_table_td");
//			for (int i = 0; i < 100; i++) {
//				String showIssue = elements.get(i).html();
//				if(curShowIssue < Integer.valueOf(showIssue) && Integer.valueOf(showIssue) < newestShowIssue) {
//					strList.add(showIssue);
//				}
//			}
//		} catch (Exception e) {
//			return null;
//		}
//		return strList;
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
//	public static void main(String[] args) {
////		GameIssue gameIssue = GameNewestGrabUtil.getGameNewest(7);
//		GameIssue gameIssue = GameNewestGrabUtil.getGameIssueByShowIssue(23, "17012701");
//		System.out.println(DateFormatHelper.parseDate(gameIssue.getDrawTime(), DateFormatHelper.YYYY_MM_DD_HH_MM_SS));
//		System.out.println("------------------------------------");
//		GameGrabResp gameGrabResp = GameNewestGrabUtil.getGameGrabResp(23, "17012701");
//		System.out.println("中奖信息为:");
//		for (GameGrabWinNum gameGrabWinNum : gameGrabResp.getGameGrabWinNum()) {
//        	for (String datas : gameGrabWinNum.getData()) {
//				//开奖号码
//				System.out.print(datas+",");
//			}
//		}
////        for (GameGrabWinResult gameGrabWinResult : gameGrabResp.getGameGrabWinResult()) {
////			System.out.print("奖项:"+gameGrabWinResult.getName());
////			System.out.print(" 中奖条件:"+gameGrabWinResult.getKey());
////			System.out.print(" 中奖注数:"+gameGrabWinResult.getBet());
////			System.out.println(" 单注奖金:"+gameGrabWinResult.getPrize());
////		}
////        System.out.println("销售金额:"+gameGrabResp.getSaleAmount());
////        System.out.println("奖池金额:"+gameGrabResp.getPoolAmount());
//        
////		List<String> strList = GameNewestGrabUtil.getCompensateShowIssue(1, 1, 17039);
////        for (String string : strList) {
////			System.out.println("期号:"+string);
////		}
////		List<String> list = GameNewestGrabUtil.getGd11x5ShowIssue("17041001", "17041054");
////		System.out.println("-----------------------------");
////		for (String string : list) {
////			System.out.println(string);
////		}
////		GameGrabResp gameGrabResp = GameNewestGrabUtil.getGameGrabResp(1, "17042");
////		System.out.println(gameGrabResp);
//	}
//}
