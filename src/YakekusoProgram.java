import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class YakekusoProgram {
	public static void main(String[] arg) throws InterruptedException {
		try {
			//sp-file-name
			String full_pass = "/Network/Servers/minerva.ktlab.el.gunma-u.ac.jp/Volumes/UsersN01/kazuto.okouchi/Desktop";
			String spfile_name = "Common-Differential-amplifer49s4-db";
			
			ArrayList<String> spfile = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(full_pass+ "/"+ spfile_name + ".sp"));
			
			String tmp_spfile_read;
			while((tmp_spfile_read = br.readLine())!=null){
			spfile.add(tmp_spfile_read);
			}
			
			
//		    テスト用spファイルの基本形式
			String test_spfile_MOS = "M01 002 002 gnd gnd cmosn l=0.2u w=5.0u";
	    	String test_spfile_R = "R1 001 002 83.9k";
			String test_spfile_C = "C1 009 out 0.5p";

			
			/*素子の探索パターン
			グループ１MOS番号　グループ２ドレイン(ソース)端子　グループ３ゲート端子　グループ４ソース(ドレイン)端子
			グループ５バルク端子　グループ６MOStype　
			グループ７RC番号　グループ８RorC　グループ９上端子　グループ１０下端子*/
			String spfile_element_search = "(?:(^M\\w+) (\\w+) (\\w+) (\\w+) (\\w+) (cmos(?:p|n)))"
				                       	+ "|(?:((^R|^C)\\w+) (\\w+) (\\w+))";
			Pattern spfile_element_Pattern = Pattern.compile(spfile_element_search);
			
//			System.out.println("spファイルの中身を表示します");
//			for (int i = 0; i < spfile.size() ; i++) {
//				System.out.println(spfile.get(i));
//			}
			
			
			System.out.println("正規表現を適用します");
			for (int i = 0; i < 88; i++) {
				Matcher spfile_element_Matcher = spfile_element_Pattern.matcher(spfile.get(i));
				if (spfile_element_Matcher.find()) {
					System.out.println(spfile_element_Matcher.group(1));
					System.out.println(spfile_element_Matcher.group(2));
					System.out.println(spfile_element_Matcher.group(3));
					System.out.println(spfile_element_Matcher.group(4));
					System.out.println(spfile_element_Matcher.group(5));
					System.out.println(spfile_element_Matcher.group(6));
					System.out.println(spfile_element_Matcher.group(7));
					System.out.println(spfile_element_Matcher.group(8));
					System.out.println(spfile_element_Matcher.group(9));
					System.out.println(spfile_element_Matcher.group(10));
					String tmpString = spfile_element_Matcher.group(6);
					Pattern MOS_type_Pattern = Pattern.compile("cmosn");
					Matcher MOS_type_Matcher = MOS_type_Pattern.matcher(tmpString);
					if (MOS_type_Matcher.find()) 
						
						System.out.println("OK");
						continue;
					
					
//					System.out.println(MOS_type);
//					System.out.println(RC_type);
//					try {
//						if (MOS_type == "cmosn") {
//							System.out.println(spfile_element_Matcher.group(1));
//							
//						}else if (MOS_type == "cmosp") {
//							System.out.println(spfile_element_Matcher.group(1));
//						}
//						else if (RC_type == "R") {
//							System.out.println(spfile_element_Matcher.group(7));
//							
//						}else if (RC_type == "C") {
//							System.out.println(spfile_element_Matcher.group(7));
//						}
//						
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
					
				}
//				String group1 = spfile_element_Matcher.group(1); 
				

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
