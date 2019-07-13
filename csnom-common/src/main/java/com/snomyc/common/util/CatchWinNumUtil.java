package com.snomyc.common.util;//package com.snomyc.util;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import org.apache.log4j.Logger;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import com.alibaba.fastjson.JSONObject;
//
///***
// * 体彩网上获取开奖信息工具类 
// * @author C968
// *
// */
//public class CatchWinNumUtil {
//	private static Logger logger = Logger.getLogger(CatchWinNumUtil.class);
//	
//	private static String LOTTERY_ZC_URL = "http://www.lottery.gov.cn/api/lottery_kj_detail.jspx?";
//	
//	private static String LOTTERY_ZC_ISSUE_URL = "http://www.lottery.gov.cn/api/get_typeBytermAndnews.jspx?";
//	
//	/***
//	 * 1---大乐透, 2 ---七星彩, 23 -- 11选5, 3---排列3 , 4---排列5 , 7 --- 14场胜负彩,  9-----4场进球, 10---6场半全场
//	 * 根据游戏编号，和期号获取（爬取）获取游戏开奖公告信息 
//	 * @param gameID
//	 * @param issue
//	 * @return
//	 * @throws InterruptedException 
//	 */
//	public static GameGrabResp getCatchWinNum(Integer gameID, String issue) throws InterruptedException{
//		String url_str="";
//		if(gameID==23){
//			url_str="http://gd11x5.icaile.com/chart.asp?beginperiod="+issue+"&endperiod="+issue;
//		}else if (gameID==1){
//			return GameGrabLotteryUtil.getGameNoticeGrabResp(4, issue);
//			//url_str="http://www.lottery.gov.cn/historykj/history.jspx?page=false&_ltype=dlt&termNum=0&startTerm="+issue+"&endTerm="+issue;
//		}else if (gameID==4){
//			url_str="http://www.lottery.gov.cn/historykj/history.jspx?page=false&_ltype=plw&termNum=0&startTerm="+issue+"&endTerm="+issue;
//		}else if (gameID==2){
//			url_str="http://www.lottery.gov.cn/historykj/history.jspx?page=false&_ltype=qxc&termNum=0&startTerm="+issue+"&endTerm="+issue;
//		}else if(gameID==7){
//			//体彩网中14场胜负彩gameID为9
//			return getGameZcNoticeGrabResp(9, issue);
//		}else if(gameID==9){
//			//体彩网中4场进球gameID为11
//			return getGameZcNoticeGrabResp(11, issue);
//		}else if(gameID==10){
//			//体彩网中6场半全场gameID为10
//			return getGameZcNoticeGrabResp(10, issue);
//		}else{
//			return null;
//		}
//		
//        GameGrabResp gameGrabResp=null;
//        
//        try {
//            Document document = GrabHel.getDoc(url_str);
//            if(gameID==23){
//            	gameGrabResp = getGd11x5(document,issue);
//            	if(gameGrabResp == null) {
//            		gameGrabResp = getGdlottery11x5(issue);
//            	}
//    		}else if (gameID==4){
//    			gameGrabResp = getPlwf(document,issue);
//    		}else if (gameID==2){
//    			gameGrabResp = getQxc(document,issue);
//    		}
////            else if (gameID==1){
////    			gameGrabResp = getDlt(document,issue);
////    		}
//            	
//            return gameGrabResp;
//        } catch (IOException e) {
//            logger.error("CatchWinNumUtil 爬取中奖信息出错!");
//        }
//		return gameGrabResp;
//	}
//	
//	/**
//	 * 根据游戏编号，和期号获取（爬取）中间期号的list集合
//	 * @param gameID 游戏编号 1---大乐透, 2 ---七星彩 , 4---排列5
//	 * @param curShowIssue game表中当前显示期号
//	 * @param newestShowIssue 乐彩网当前销售最新期号
//	 * @return
//	 * @throws InterruptedException 
//	 */
//	public static List<String> getCompensateShowIssue(Integer gameID,Integer curShowIssue,Integer newestShowIssue) {
//		String url_str="";
//		if (gameID==1){
//			url_str="http://www.lottery.gov.cn/historykj/history.jspx?page=false&_ltype=dlt&termNum=0&startTerm="+(curShowIssue==null?"":curShowIssue+1)+"&endTerm="+(newestShowIssue==null?"":newestShowIssue);
//		}else if (gameID==4){
//			url_str="http://www.lottery.gov.cn/historykj/history.jspx?page=false&_ltype=plw&termNum=0&startTerm="+(curShowIssue==null?"":curShowIssue+1)+"&endTerm="+(newestShowIssue==null?"":newestShowIssue);
//		}else if (gameID==2){
//			url_str="http://www.lottery.gov.cn/historykj/history.jspx?page=false&_ltype=qxc&termNum=0&startTerm="+(curShowIssue==null?"":curShowIssue+1)+"&endTerm="+(newestShowIssue==null?"":newestShowIssue);
//		}else if(gameID == 23){
//			//广东11选5
//			return GameGrabLotteryUtil.getGd11x5ShowIssue(String.valueOf(curShowIssue), String.valueOf(newestShowIssue));
//		}else if(gameID == 7) {
//			return getZcCompensateShowIssue(gameID, curShowIssue, newestShowIssue);
//		}else if(gameID == 9) {
//			return getZcCompensateShowIssue(gameID, curShowIssue, newestShowIssue);
//		}else if(gameID == 10) {
//			return getZcCompensateShowIssue(gameID, curShowIssue, newestShowIssue);
//		}
//		List<String> list =new ArrayList<String>();
//		try {
//            Document document = GrabHel.getDoc(url_str);
//            
//        	list = getShowIssue(document, curShowIssue, newestShowIssue);
//        	if(list!=null){
//        		Collections.sort(list); 	
//        	}
//            return list;
//        } catch (Exception e) {
//        	logger.error("爬取游戏ID:"+gameID+",期号信息出错!");
//        	return null;
//        }
//	}
//	
//	public static List<String> getZcCompensateShowIssue(Integer gameID,Integer curShowIssue,Integer newestShowIssue) {
//		try {
//			if(gameID == 7) {
//				gameID = 9;
//			}else if(gameID == 9) {
//				gameID = 11;
//			}
//			StringBuffer sb = new StringBuffer();
//			sb.append(LOTTERY_ZC_ISSUE_URL);
//			sb.append("_ltype=").append(gameID);
//			String json = HttpClientHelper.httpGet(sb.toString());
//			JSONArray array = JSONArray.fromObject(json);
//	        JSONObject root = array.getJSONObject(0);
//	        JSONArray tremList =root.getJSONArray("tremList");
//	        
//	        List<String> list = new ArrayList<String>();
//	        Iterator<Object> it = tremList.iterator();
//	        while(it.hasNext()) {
//	        	JSONObject obj = (JSONObject)it.next();
//	        	String issue = obj.getString("term");
//	        	if(curShowIssue < Integer.valueOf(issue) && Integer.valueOf(issue) < newestShowIssue) {
//					list.add(issue);
//				}
//	        }
//	        Collections.sort(list);
//			return list;
//		} catch (Exception e) {
//        	logger.error("爬取游戏ID:"+gameID+",期号信息出错!");
//        	return null;
//        }
//	}
//	
//	
//	/**
//	 * 获取足球游戏开奖公告信息 
//	 * 体彩网对应游戏gameID 9 --- 14场胜负彩,  11-----4场进球, 10---6场半全场
//	 * @param gameID
//	 * @param showIssue
//	 * @return
//	 */
//	/**
//	 * @param gameID
//	 * @param showIssue
//	 * @return
//	 */
//	/**
//	 * @param gameID
//	 * @param showIssue
//	 * @return
//	 */
//	private static GameGrabResp getGameZcNoticeGrabResp(Integer gameID,String showIssue) {
//		try{
//			StringBuffer sb = new StringBuffer();
//			sb.append(LOTTERY_ZC_URL);
//			sb.append("_ltype=").append(gameID);
//			sb.append("&_term=").append(showIssue);
//			String json = HttpClientHelper.httpGet(sb.toString());
//			JSONArray array = JSONArray.fromObject(json);
//	        JSONObject root = array.getJSONObject(0);
//	        JSONObject data =root.getJSONObject("lottery");
//			
//	        //获取中奖号码
//	        String number = data.get("number").toString().replace("＋", "").replace(" ", "");
//	        char [] numChar = number.toCharArray();
//	        String[] numStr = null;
//	        if(gameID == 9) {
//	        	numStr  = new String[14];
//	    		for (int i = 0; i < numStr.length; i++) {
//	    			numStr[i] = String.valueOf(numChar[i]);
//	    		}
//	        }else if(gameID == 11) {
//	        	numStr  = new String[8];
//	    		for (int i = 0; i < numStr.length; i++) {
//	    			numStr[i] = String.valueOf(numChar[i]);
//	    		}
//	        }else if(gameID == 10) {
//	        	numStr  = new String[12];
//	    		for (int i = 0; i < numStr.length; i++) {
//	    			numStr[i] = String.valueOf(numChar[i]);
//	    		}
//	        }
//   			
//   			GameGrabResp gameGrabResp = new GameGrabResp();
//   			GameGrabWinNum winNum = new GameGrabWinNum();
//   			winNum.setKey("zc");
//   			winNum.setData(numStr);
//	        
//	        List<GameGrabWinNum> grabNum = new ArrayList<GameGrabWinNum>();
//	        grabNum.add(winNum);
//	        gameGrabResp.setGameGrabWinNum(grabNum);
//	        
//	        //获取中奖信息
//	        List<GameGrabWinResult> winResult = new ArrayList<GameGrabWinResult>();
//	        JSONArray resultDetail = root.getJSONArray("details");
//	        Iterator<Object> it = resultDetail.iterator();
//	        while(it.hasNext()) {
//	        	JSONObject obj = (JSONObject)it.next();
//	        	GameGrabWinResult grabWinResult = new GameGrabWinResult();
//	        	grabWinResult.setName(obj.getString("level"));
//	        	String piece = new BigDecimal(obj.getString("piece").replace(",", "")).setScale(0,BigDecimal.ROUND_DOWN).toString();
//	        	String money = new BigDecimal(obj.getString("money").replace(",", "")).setScale(0,BigDecimal.ROUND_DOWN).toString();
//	        	grabWinResult.setBet(piece);
//	        	grabWinResult.setPrize(money);
//	        	winResult.add(grabWinResult);
//	        }
//	        gameGrabResp.setGameGrabWinResult(winResult);
//	        
//	        //获取场次信息
//	        JSONArray matchResult = root.getJSONArray("matchResults");
//	        Iterator<Object> matchIt = matchResult.iterator();
//	        StringBuffer matchSb = new StringBuffer();
//	        if(gameID == 10) {
//	        	int seq = 1;
//	        	while(matchIt.hasNext()) {
//	        		JSONObject obj = (JSONObject)matchIt.next();
//	        		if(seq%2 != 0) {
//			        	String homeTeam = obj.getString("homeTeamView");
//			        	homeTeam = homeTeam.replace("<br>", "").replace("&nbsp;", "");
//			        	matchSb.append(homeTeam).append("-");
//	        		}
//		        	seq++;
//		        }
//	        }else {
//	        	while(matchIt.hasNext()) {
//		        	JSONObject obj = (JSONObject)matchIt.next();
//		        	String homeTeam = obj.getString("homeTeamView");
//		        	homeTeam = homeTeam.replace("<br>", "").replace("&nbsp;", "");
//		        	matchSb.append(homeTeam).append("-");
//		        }
//	        }
//	        gameGrabResp.setMatchData(matchSb.toString());
//	        
//	        String sale_amount = (String)data.get("totalSales");
//	        String pool_amount = (String)data.get("pool");
//        	 //获取销售金额
//	        if(StringUtils.isNotBlank(sale_amount)) {
//	        	gameGrabResp.setSaleAmount(new BigDecimal(sale_amount.replace(",", "")).setScale(0,BigDecimal.ROUND_DOWN));
//	        }
//            //获取奖池金额
//	        if(StringUtils.isNotBlank(pool_amount)) {
//	        	gameGrabResp.setPoolAmount(new BigDecimal(pool_amount.replace(",", "")).setScale(0,BigDecimal.ROUND_DOWN));
//	        }
//	        //获取任九销量
//	        gameGrabResp.setPlsSaleAmount(getRenJiuSaleAmount(showIssue));
//			return gameGrabResp;
//		}catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		
//	}
//	
//	/**
//	 * 获取任九总销量
//	 * @param showIssue
//	 * @return
//	 * @throws Exception 
//	 * @throws  
//	 */
//	public static BigDecimal getRenJiuSaleAmount(String showIssue) throws Exception {
//		String url = "http://www.lottery.gov.cn/historykj/history.jspx?page=false&_ltype=sfc&termNum=0&startTerm="+showIssue+"&endTerm="+showIssue;
//		Document document = GrabHel.getDoc(url);
//		//网页中表格tbody的tr对象
//		Elements trs = document.select("tbody").select("tr");
//		//获取查询结果第一行行数
//		Element tr =trs.get(0);
//		//获取单元格总列数
//		Elements tds = tr.children();
//		//获取第一列值 期号
//		String issueV = tds.get(0).text();
//		if(showIssue.equals(issueV)) {
//			//获取第13列值 任九总销量
//			String renjiuSaleAmount = tds.get(12).text();
//			//写入销售额 不保留小数
//			return new BigDecimal(renjiuSaleAmount.replace(",", "")).setScale(0,BigDecimal.ROUND_DOWN);
//		}else{
//			throw new Exception();
//		}
//	}
//	
//	
//	/***
//	 * 网页解析获取(抓取)game表中当前显示期号和最新期之间期号的集合
//	 * @param document
//	 * @param issue
//	 * @return
//	 */
//	private static List<String> getShowIssue(Document document,Integer curShowIssue,Integer newestShowIssue){
//		try {
//			List<String> list = new ArrayList<String>();
//			//网页中表格tbody的tr对象
//	        Elements trs = document.select("tbody").select("tr");
//	        if(null != trs){
//	         	for(int i=0; i<trs.size(); i++){
//	        		//获取行数
//	        		Elements tds = trs.get(i).children();
//        			//获取该行的第一列信息(期号)
//    				Element element2 =tds.get(0);
//        			String issueV = element2.text();
//    				if(curShowIssue < Integer.valueOf(issueV) && Integer.valueOf(issueV) < newestShowIssue) {
//    					list.add(issueV);
//    				}
//	        	}
//	         	return list;
//        	}else{
//        		return null;
//        	}
//		} catch (Exception e) {
//			 e.printStackTrace();
//		}
//		return null;
//	}
//	
//	
//	/***
//	 * 网页解析获取前50期大乐透（期数可以修改上面的url_str地址） 开奖信息
//	 * @param document
//	 * @param issue
//	 * @return
//	 */
//	private static GameGrabResp getDlt(Document document,String issue){
//		
//		try{
//			//网页中表格tbody的tr对象
//			Elements trs = document.select("tbody").select("tr");
//			
//			if( null != trs) {
//				//获取查询结果第一行行数
//				Element tr =trs.get(0);
//				//获取单元格总列数
//				Elements tds = tr.children();
//				String issueV = "";
//				StringBuffer redNum = new StringBuffer();
//				StringBuffer blueNum = new StringBuffer();
//				String saleAmount = "0";
//				String poolAmount = "0";
//				//遍历单元格
//				for(int i = 0; i < tds.size(); i++) {
//					Element element =tds.get(i);
//					//获取单元格第一列文本
//					if(i == 0) {
//						issueV = element.text();
//					}else if("red".equals(element.className())){
//						//开奖号
//						redNum.append(element.text()).append(",");
//					}else if("blue".equals(element.className())){
//						//开奖号
//						blueNum.append(element.text()).append(",");
//					}else if(i == 17){
//						//销售金额
//						saleAmount = element.text();
//					}else if(i == 18){
//						//奖池金额
//						poolAmount = element.text();
//					}
//				}
//				
//				if(issue.equals(issueV)) {
//					//组装对象
//					GameGrabResp gameGrabResp=new GameGrabResp();
//					List<GameGrabWinNum> gameGrabWinNumList =new ArrayList<GameGrabWinNum>();
//					GameGrabWinNum redWinNum=new GameGrabWinNum();
//					String[] red =redNum.toString().split(",");
//					redWinNum.setKey("red");
//    				redWinNum.setData(red);
//                	
//                	GameGrabWinNum blueWinNum=new GameGrabWinNum();
//                	String[] blue=blueNum.toString().split(",");
//                	blueWinNum.setKey("blue");
//    				blueWinNum.setData(blue);
//    				//写入红球号码
//    				gameGrabWinNumList.add(redWinNum);
//    				//写入蓝球号码
//    				gameGrabWinNumList.add(blueWinNum);
//    				
//    				gameGrabResp.setGameGrabWinNum(gameGrabWinNumList);
//					//写入销售额 不保留小数
//    				gameGrabResp.setSaleAmount(new BigDecimal(saleAmount.replace(",", "")).setScale(0,BigDecimal.ROUND_DOWN));
//					//写入奖池 不保留小数
//    				gameGrabResp.setPoolAmount(new BigDecimal(poolAmount.replace(",", "")).setScale(0,BigDecimal.ROUND_DOWN));
//					
//    				return gameGrabResp;
//				}else{
//					return null;
//				}
//			}
//		}catch (Exception e) {
//			logger.error("CatchWinNumUtil 爬取大乐透中奖信息出错!");
//		}
//		return null;
//	}
//	
//	/***
//	 * 网页解析获取前50期七星彩 开奖信息
//	 * @param document
//	 * @param issue
//	 * @return
//	 */
//	private static GameGrabResp getQxc(Document document,String issue){
//		
//		try{
//			//网页中表格tbody的tr对象
//			Elements trs = document.select("tbody").select("tr");
//			
//			if( null != trs) {
//				//获取查询结果第一行行数
//				Element tr =trs.get(0);
//				//获取单元格总列数
//				Elements tds = tr.children();
//				
//				//获取第一列值 期号
//				String issueV = tds.get(0).text();
//				if(issue.equals(issueV)) {
//					
//					//获取第二列值 中奖号码
//					String redNum = tds.get(1).text();
//					//获取第16列值销售额
//					String saleAmount = tds.get(15).text();
//					//获取第17列值 奖池奖金
//					String poolAmount = tds.get(16).text();
//					
//					List<GameGrabWinResult> gameGrabWinResult = new ArrayList<GameGrabWinResult>();
//					//获取开奖公告
//					//获取一等奖 第3,4列 注数，奖金
//					GameGrabWinResult oneWin = new GameGrabWinResult("一等奖",tds.get(2).text(),tds.get(3).text());
//					//获取二等奖 第5,6列 注数，奖金
//					GameGrabWinResult twoWin = new GameGrabWinResult("二等奖",tds.get(4).text(),tds.get(5).text());
//					//获取三等奖 第7,8列 注数，奖金
//					GameGrabWinResult threeWin = new GameGrabWinResult("三等奖",tds.get(6).text(),tds.get(7).text());
//					//获取四等奖 第9,10列 注数，奖金
//					GameGrabWinResult fourWin = new GameGrabWinResult("四等奖",tds.get(8).text(),tds.get(9).text());
//					//获取五等奖 第11,12列 注数，奖金
//					GameGrabWinResult fiveWin = new GameGrabWinResult("五等奖",tds.get(10).text(),tds.get(11).text());
//					//获取六等奖 第13,14列 注数，奖金
//					GameGrabWinResult sixWin = new GameGrabWinResult("六等奖",tds.get(12).text(),tds.get(13).text());
//					
//					gameGrabWinResult.add(oneWin);
//					gameGrabWinResult.add(twoWin);
//					gameGrabWinResult.add(threeWin);
//					gameGrabWinResult.add(fourWin);
//					gameGrabWinResult.add(fiveWin);
//					gameGrabWinResult.add(sixWin);
//					
//					//组装对象
//					GameGrabResp gameGrabResp = new GameGrabResp();
//					List<GameGrabWinNum> gameGrabWinNumList =new ArrayList<GameGrabWinNum>();
//					
//					char [] redNumChar = redNum.toCharArray();
//		   			String[] redNumStr = new String[7];
//		   			for (int i = 0; i < redNumStr.length; i++) {
//		   				redNumStr[i] = String.valueOf(redNumChar[i]);
//		   			}
//		   			GameGrabWinNum redWinNum=new GameGrabWinNum();
//					redWinNum.setKey("red");
//    				redWinNum.setData(redNumStr);
//                	
//    				//写入红球号码
//    				gameGrabWinNumList.add(redWinNum);
//    				gameGrabResp.setGameGrabWinNum(gameGrabWinNumList);
//    				//写入开奖公告
//    				gameGrabResp.setGameGrabWinResult(gameGrabWinResult);
//					//写入销售额 不保留小数
//    				gameGrabResp.setSaleAmount(new BigDecimal(saleAmount.replace(",", "")).setScale(0,BigDecimal.ROUND_DOWN));
//					//写入奖池 不保留小数
//    				gameGrabResp.setPoolAmount(new BigDecimal(poolAmount.replace(",", "")).setScale(0,BigDecimal.ROUND_DOWN));
//					
//    				return gameGrabResp;
//				}else{
//					return null;
//				}
//			}
//		}catch (Exception e) {
//			logger.error("CatchWinNumUtil 爬取七星彩中奖信息出错!");
//		}
//		return null;
//	}
//	
//	/***
//	 * 网页解析获取前50期排列5 开奖信息
//	 * @param document
//	 * @param issue
//	 * @return
//	 */
//	private static GameGrabResp getPlwf(Document document,String issue){
//		
//		try{
//			//网页中表格tbody的tr对象
//			Elements trs = document.select("tbody").select("tr");
//			//获取查询结果第一行行数
//			Element tr =trs.get(0);
//			//获取单元格总列数
//			Elements tds = tr.children();
//			
//			//获取第一列值 期号
//			String issueV = tds.get(0).text();
//			if(issue.equals(issueV)) {
//				
//				//获取排列三 开奖公告 和 销售额
//				String url_str="http://www.lottery.gov.cn/historykj/history.jspx?page=false&_ltype=pls&termNum=0&startTerm="+issue+"&endTerm="+issue;
//				Document document2 = GrabHel.getDoc(url_str);
//				Elements trs2 = document2.select("tbody").select("tr");
//				//获取查询结果第一行 单元格总列数
//				Elements tds2 = trs2.get(0).children();
//				//获取排列三第一列 期号
//				issueV = tds2.get(0).text();
//				if(!issue.equals(issueV)) {
//					return null;
//				}
//				List<GameGrabWinResult> gameGrabWinResult = new ArrayList<GameGrabWinResult>();
//				
//				//获取排列三 第10列 销售额
//				String plsSaleAmount = tds2.get(9).text();
//				//获排列3取开奖公告
//				//获取二等奖 第3,4列 注数，奖金
//				GameGrabWinResult oneWin = new GameGrabWinResult("直选",tds2.get(2).text(),tds2.get(3).text());
//				//获取三等奖 第5,6列 注数，奖金
//				GameGrabWinResult twoWin = new GameGrabWinResult("组3",tds2.get(4).text(),tds2.get(5).text());
//				//获取四等奖 第7,8列 注数，奖金
//				GameGrabWinResult threeWin = new GameGrabWinResult("组6",tds2.get(6).text(),tds2.get(7).text());
//				gameGrabWinResult.add(oneWin);
//				gameGrabWinResult.add(twoWin);
//				gameGrabWinResult.add(threeWin);
//				
//				//获取第二列值 中奖号码
//				String redNum = tds.get(1).text().replace(" ", "");
//				//获取第6列值 排列五销售额
//				String saleAmount = tds.get(5).text();
//				//获取第7列值 奖池奖金
//				String poolAmount = tds.get(6).text();
//				
//				//获取排列5开奖公告
//				//获取排列5直选 第3,4列 注数，奖金
//				GameGrabWinResult pl5Win = new GameGrabWinResult("排列五",tds.get(2).text(),tds.get(3).text());
//				gameGrabWinResult.add(pl5Win);
//				
//				//组装对象
//				GameGrabResp gameGrabResp = new GameGrabResp();
//				List<GameGrabWinNum> gameGrabWinNumList =new ArrayList<GameGrabWinNum>();
//				
//				char [] redNumChar = redNum.toCharArray();
//	   			String[] redNumStr = new String[5];
//	   			for (int i = 0; i < redNumStr.length; i++) {
//	   				redNumStr[i] = String.valueOf(redNumChar[i]);
//	   			}
//	   			GameGrabWinNum redWinNum=new GameGrabWinNum();
//				redWinNum.setKey("red");
//				redWinNum.setData(redNumStr);
//            	
//				//写入红球号码
//				gameGrabWinNumList.add(redWinNum);
//				gameGrabResp.setGameGrabWinNum(gameGrabWinNumList);
//				//写入开奖公告
//				gameGrabResp.setGameGrabWinResult(gameGrabWinResult);
//				//写入排列5销售额 不保留小数
//				gameGrabResp.setSaleAmount(new BigDecimal(saleAmount.replace(",", "")).setScale(0,BigDecimal.ROUND_DOWN));
//				//写入奖池 不保留小数
//				gameGrabResp.setPoolAmount(new BigDecimal(poolAmount.replace(",", "")).setScale(0,BigDecimal.ROUND_DOWN));
//				//写入排列3销售额 不保留小数
//				gameGrabResp.setPlsSaleAmount(new BigDecimal(plsSaleAmount.replace(",", "")).setScale(0,BigDecimal.ROUND_DOWN));
//				return gameGrabResp;
//			}else{
//				return null;
//			}
//		}catch (Exception e) {
//			logger.error("CatchWinNumUtil 爬取排列五中奖信息出错!");
//		}
//        return null;
//	}
//	
//	
//	/***
//	 * 网页解析获取当天广东11选5 开奖信息
//	 * @param document
//	 * @param issue
//	 * @return
//	 */
//	private static GameGrabResp getGd11x5(Document document,String issue){
//		
//		try{
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
//					
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
//	
//	/***
//	 * 网页解析获取当天广东11选5 开奖信息 http://www.gdlottery.cn/odata/zst11xuan5.jspx
//	 * @param issue
//	 * @return
//	 */
//	public static GameGrabResp getGdlottery11x5(String issue){
//		
//		try{
//			//时间
//			String timeStr = "20"+issue.substring(0, issue.length()-2);
//			DateFormat format = new SimpleDateFormat("yyyyMMdd");
//			Date time = format.parse(timeStr);
//			String url = "http://www.gdlottery.cn/odata/zst11xuan5.jspx?method=to11x5kjggzst&date="+DateFormatHelper.parseDate(time, "yyyy-MM-dd");
//			Document document = GrabHel.getDoc(url);
//			
//			//获取网页中第三个tbody表格
//			Elements trs = document.select("tbody").get(2).select("tr");
//			if( null != trs) {
//				//获取查询结果 第3行行数开始遍历
//				for (int i = 2; i < trs.size(); i++) {
//					//获取单元格总列数
//					Elements tds = trs.get(i).children();
//					try{
//						//获取第一列值 期号
//						String issueV = tds.get(0).text();
//						
//						//如果期号和传入的期号相等
//						if(issue.equals(issueV)) {
//							//获取第二列值 中奖号码
//							String winNum = tds.get(1).text();
//							if(StringUtils.isNotBlank(winNum)){
//								//组装对象
//								GameGrabResp gameGrabResp=new GameGrabResp();
//								List<GameGrabWinNum> gameGrabWinNumList =new ArrayList<GameGrabWinNum>();
//								GameGrabWinNum redWinNum=new GameGrabWinNum();
//								String[] red =winNum.replace(" ", "").split("，");
//								redWinNum.setKey("red");
//			    				redWinNum.setData(red);
//			    				//写入红球号码
//			    				gameGrabWinNumList.add(redWinNum);
//			    				gameGrabResp.setGameGrabWinNum(gameGrabWinNumList);
//			    				return gameGrabResp;
//							}
//						}
//					}catch (Exception e) {
//					}
//				}
//			}
//		}catch (Exception e) {
//		}
//		return null;
//	}
//	
//	/***
//	 * 网页解析获取前50期大乐透（期数可以修改上面的url_str地址） 开奖信息
//	 * @param document
//	 * @param issue
//	 * @return
//	 */
//	private static GameGrabResp getDlt_bak(Document document,String issue){
//		
//		GameGrabResp gameGrabResp=new GameGrabResp();
//        
//        /*Element element = document.getElementById("fixedtable");
//        if(null ==element){
//        	System.out.println("网页解析出错！");
//        	return null;
//        }*/
//        //获取tr
//        List<QiHao> qiHaoList = new ArrayList<QiHao>();
//        //Elements trs = element.getElementsByTag("tr");
//    	//网页中表格tbody的tr对象
//        Elements trs = document.select("tbody").select("tr");
//        if(null != trs){
//         	for(int i=0; i<trs.size(); i++){
//        		//获取列数
//         		Element tr =trs.get(i);
//        		Elements tds = tr.children();
//        		if(null != tds && tds.size()>2){
//        			List<GameGrabWinNum> gameGrabWinNumList =new ArrayList<GameGrabWinNum>();
//        			String issueV="";
//        			String shijiV="";
//        			String blueV="";
//        			//for (Element element2 : tds) {
//        			for (int j = 0; j < tds.size(); j++) {
//        				Element element2 =tds.get(j);
//						if(j==0){
//							//期号
//		        			 issueV = element2.text();
//						}
//						/*if(j==17){
//							//销售额
//		        			 issueV = element2.text();
//						}
//						if(j==18){
//							//奖池奖金
//		        			 issueV = element2.text();
//						}*/
//						if("red".equals(element2.className())){
//							//开奖号
//		        			 shijiV += element2.text()+",";
//						}else if("blue".equals(element2.className())){
//							//开奖号
//							blueV += element2.text()+",";
//						}
//					}
//        			if(shijiV.endsWith(",") ){
//        				shijiV=shijiV.substring(0, shijiV.length()-1);
//        			}
//        			if(blueV.endsWith(",") ){
//        				blueV=blueV.substring(0, blueV.length()-1);
//        			}
//        			
//        			if(null!=issueV && !"".equals(issueV)){
//        				
//        				//System.out.println("期号："+issueV+",开奖号(红球)："+shijiV+",开奖号(蓝球)："+blueV);
//        				
//        				GameGrabWinNum gameGrabWinNum=new GameGrabWinNum();
//        				gameGrabWinNum.setKey("red");
//        				String[] sa=shijiV.split(",");
//                    	gameGrabWinNum.setData(sa);
//                    	
//                    	GameGrabWinNum gameGrabWinNum2=new GameGrabWinNum();
//                    	gameGrabWinNum2.setKey("blue");
//        				String[] sa2=blueV.split(",");
//        				gameGrabWinNum2.setData(sa2);
//                    	
//        				gameGrabWinNumList.add(gameGrabWinNum);
//        				gameGrabWinNumList.add(gameGrabWinNum2);
//        				
//        				QiHao qiHao = new QiHao();
//        				qiHao.setIssueV(issueV);
//        				qiHao.setGameGrabWinNum(gameGrabWinNumList);
//        				qiHaoList.add(qiHao);
//        			}
//        			
//        		}
//        	}
//        }else{
//        	return null;
//        	}
//        for(QiHao param : qiHaoList){
//   		 String issueNum = param.getIssueV();
//   		 if(issue.equals(issueNum)){
//            	gameGrabResp.setGameGrabWinNum(param.getGameGrabWinNum());
//            	return gameGrabResp;
//   		 	}
//   	 	}
//        return null;
//	}
//	
//	/***
//	 * 网页解析获取前50期七星彩 开奖信息
//	 * @param document
//	 * @param issue
//	 * @return
//	 */
//	private static GameGrabResp getQxc_bak(Document document,String issue){
//		
//		GameGrabResp gameGrabResp=new GameGrabResp();
//        GameGrabWinNum gameGrabWinNum=new GameGrabWinNum();
//        List<GameGrabWinNum> gameGrabWinNumList =new ArrayList<GameGrabWinNum>();
//        
//        List<JiHao> list = new ArrayList<JiHao>();
//    	//网页中表格tbody的tr对象
//        Elements trs = document.select("tbody").select("tr");
//        if(null != trs){
//         	for(int i=0; i<trs.size(); i++){//i从1开始，是为了不解析第一行文字描述“期号，时间，号码”
//        		//获取列数
//         		Element tr =trs.get(i);
//        		Elements tds = tr.children();
//        		if(null != tds && tds.size()>2){
//        			String issueV="";
//        			String shijiV="";
//        			for (int j = 0; j < tds.size(); j++) {
//        				Element element2 =tds.get(j);
//						if(j==0){
//							//期号
//		        			 issueV = element2.text();
//						}
//						if("red".equals(element2.className())){
//							//开奖号
//		        			 shijiV = element2.text();
//						}
//					}
//        			
//        			if(null!=issueV && !"".equals(issueV)){
//        				//System.out.println("期号："+issueV+",开奖号："+shijiV);
//        				JiHao jiHao = new JiHao();
//        				jiHao.setIssueV(issueV);
//            			jiHao.setShijiV(shijiV);
//            			list.add(jiHao);
//        			}
//        			
//        		}
//        	}
//        }
//        for(JiHao param : list){
//   		 String issueNum = param.getIssueV();
//   		 if(issue.equals(issueNum)){
//   			String str = param.getShijiV();
//   			char [] stringArr = str.toCharArray();
//   			String[] sa=new String[7];
//   			for (int i = 0; i < sa.length; i++) {
//   				sa[i]=String.valueOf(stringArr[i]);
//   				}
//	        	gameGrabWinNum.setData(sa);
//	        	gameGrabWinNumList.add(gameGrabWinNum);
//	        	gameGrabResp.setGameGrabWinNum(gameGrabWinNumList);
//	        	return gameGrabResp;
//   		 	}
//   	 	}
//        return null;
//	}
//	
//	/***
//	 * 网页解析获取前50期排列5 开奖信息
//	 * @param document
//	 * @param issue
//	 * @return
//	 */
//	private static GameGrabResp getPlwf_bak(Document document,String issue){
//		
//		GameGrabResp gameGrabResp=new GameGrabResp();
//        GameGrabWinNum gameGrabWinNum=new GameGrabWinNum();
//        List<GameGrabWinNum> gameGrabWinNumList =new ArrayList<GameGrabWinNum>();
//        
//        List<JiHao> list = new ArrayList<JiHao>();
//    	//网页中表格tbody的tr对象
//        Elements trs = document.select("tbody").select("tr");
//        if(null != trs){
//         	for(int i=0; i<trs.size(); i++){//i从1开始，是为了不解析第一行文字描述“期号，时间，号码”
//        		//获取列数
//         		Element tr =trs.get(i);
//        		Elements tds = tr.children();
//        		if(null != tds && tds.size()>2){
//        			String issueV="";
//        			String shijiV="";
//        			for (int j = 0; j < tds.size(); j++) {
//        				Element element2 =tds.get(j);
//						if(j==0){
//							//期号
//		        			 issueV = element2.text();
//						}
//						if("red".equals(element2.className())){
//							//开奖号
//		        			 shijiV = element2.text();
//						}
//					}
//        			
//        			if(null!=issueV && !"".equals(issueV)){
//        				//System.out.println("期号："+issueV+",开奖号："+shijiV);
//        				JiHao jiHao = new JiHao();
//        				jiHao.setIssueV(issueV);
//            			jiHao.setShijiV(shijiV);
//            			list.add(jiHao);
//        			}
//        			
//        		}
//        	}
//        }
//        for(JiHao param : list){
//   		 String issueNum = param.getIssueV();
//   		 if(issue.equals(issueNum)){
//   			String str = param.getShijiV();
//            	String[] sa=str.split(" ");
//            	gameGrabWinNum.setData(sa);
//            	gameGrabWinNumList.add(gameGrabWinNum);
//            	gameGrabResp.setGameGrabWinNum(gameGrabWinNumList);
//            	return gameGrabResp;
//   		 	}
//   	 	}
//        return null;
//	}
//	
//	public static void main(String[] args) throws InterruptedException {
//		
//		//测试获取11选5某期开奖数据
//		GameGrabResp gameGrabResp = CatchWinNumUtil.getCatchWinNum(23, "17051110");
//		System.out.println("11选5中奖信息为:");
//		if(null!=gameGrabResp){
//			for (GameGrabWinNum gameGrabWinNum : gameGrabResp.getGameGrabWinNum()) {
//	        	String b="";
//	        	if(null!=gameGrabWinNum){
//	        		for(int i=0;i<gameGrabWinNum.getData().length;i++){
//					    b+=gameGrabWinNum.getData()[i]+",";
//					}
//	        	}
//				
//				System.out.print("开奖号:"+b);
//			}
//		}else{
//			System.out.print("11选5 没有获取到数据！");
//		}
//		//测试获取大乐透某期开奖数据
//		GameGrabResp gameGrabResp2 = CatchWinNumUtil.getCatchWinNum(1, "17050");
//		System.out.println("大乐透中奖信息为:");
//		if(null!=gameGrabResp2){
//			for (GameGrabWinNum gameGrabWinNum : gameGrabResp2.getGameGrabWinNum()) {
//				if ("red".equals(gameGrabWinNum.getKey())){
//					String b="";
//					for(int i=0;i<gameGrabWinNum.getData().length;i++){
//					    b+=gameGrabWinNum.getData()[i]+",";
//					}
//					System.out.print("开奖号(红球):"+b);
//				}else if ("blue".equals(gameGrabWinNum.getKey())){
//					String b="";
//					for(int i=0;i<gameGrabWinNum.getData().length;i++){
//					    b+=gameGrabWinNum.getData()[i]+",";
//					}
//					System.out.print("开奖号(篮球):"+b);
//				}
//			}
//		}else{
//			System.out.print("大乐透 没有获取到数据！");
//		}
//      
//		//测试获取排列5某期开奖数据
//		GameGrabResp gameGrabResp3 = CatchWinNumUtil.getCatchWinNum(4, "17090");
//		System.out.println("排列5中奖信息为:");
//		if(null!=gameGrabResp3){
//			for (GameGrabWinNum gameGrabWinNum : gameGrabResp3.getGameGrabWinNum()) {
//	        	String b="";
//	        	if(null!=gameGrabWinNum){
//	        		for(int i=0;i<gameGrabWinNum.getData().length;i++){
//					    b+=gameGrabWinNum.getData()[i]+",";
//					}
//	        	}
//				
//				System.out.print("开奖号:"+b);
//			}
//		}else{
//			System.out.print("排列5 没有获取到数据！");
//		}
//
//		//测试获取七星彩某期开奖数据
//		GameGrabResp gameGrabResp4= CatchWinNumUtil.getCatchWinNum(2, "17039");
//		System.out.println("七星彩中奖信息为:");
//		if(null!=gameGrabResp4){
//			for (GameGrabWinNum gameGrabWinNum : gameGrabResp4.getGameGrabWinNum()) {
//	        	String b="";
//	        	if(null!=gameGrabWinNum){
//	        		for(int i=0;i<gameGrabWinNum.getData().length;i++){
//					    b+=gameGrabWinNum.getData()[i]+",";
//					}
//	        	}
//				
//				System.out.print("开奖号:"+b);
//			}
//		}else{
//			System.out.print("七星彩 没有获取到数据！");
//		}
//		
//		//获取期号集合
//		List<String> list =CatchWinNumUtil.getCompensateShowIssue(2,17043,17049);
//		if(list!=null){
//			System.out.println("期号集合："+ list.toString());	
//		}else{
//			System.out.println("没有获取到数据！");
//		}
//		
//	}
//	
//	
//	
//}
//
////解析获取最大期号的试机号
//	/*private static JiHao getMaxShiJi(Document document,String issue){
//		List<JiHao> list = get30ShiJi(document,issue);
//		if(null != list && list.size() > 0){
//			int maxIndex = list.size() - 1;
//			JiHao jiHao  = list.get(maxIndex);
//			//System.out.println("最新期号和试机号："+jiHao.getIssueV()+","+jiHao.getShijiV());
//			return jiHao;
//		}
//		return null;
//	}*/
//
///**
// * 期号对象
// * @author C968
// *
// */
//class JiHao{
//	//期号值
//	private String issueV;
//	
//	//试机号值
//	private String shijiV;
//
//	public String getIssueV() {
//		return issueV;
//	}
//
//	public void setIssueV(String issueV) {
//		this.issueV = issueV;
//	}
//
//	public String getShijiV() {
//		return shijiV;
//	}
//
//	public void setShijiV(String shijiV) {
//		this.shijiV = shijiV;
//	}
//	
//}
//class QiHao{
//	//期号值
//	private String issueV;
//	
//	public String getIssueV() {
//		return issueV;
//	}
//
//	public void setIssueV(String issueV) {
//		this.issueV = issueV;
//	}
//
//	private List<GameGrabWinNum> gameGrabWinNum;  //中奖号码
//	public List<GameGrabWinNum> getGameGrabWinNum() {
//		return gameGrabWinNum;
//	}
//
//	public void setGameGrabWinNum(List<GameGrabWinNum> gameGrabWinNum) {
//		this.gameGrabWinNum = gameGrabWinNum;
//	}
//	
//}
//
