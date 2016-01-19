import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestDiagramNo2 {
	public static void main(String[] args) throws InterruptedException, IOException {
		try{
		//sp-file-name
		String full_pass = "/Network/Servers/minerva.ktlab.el.gunma-u.ac.jp/Volumes/UsersN01/kazuto.okouchi/Desktop/";
		String spfile_name = "Common-Differential-amplifer49s4-db";
		String lisfile_name = spfile_name; 
			
//		lisÉtÉ@ÉCÉãì«Ç›çûÇ›
		ArrayList<String> lisfile_al = new ArrayList<String>();
		BufferedReader lisfile_br = new BufferedReader(new FileReader(full_pass + lisfile_name + ".lis"));
		String tmp_lisfile_read;
		while((tmp_lisfile_read = lisfile_br.readLine()) != null){
			lisfile_al.add(tmp_lisfile_read);
		}
//
		String lisfile_element_search5 = "^ element +0:(m\\w+) +0:(m\\w+) +0:(m\\w+) +0:(m\\w+) +0:(m\\w+)";
		Pattern lisfile_element_Pattern5 = Pattern.compile(lisfile_element_search5);
		
		String lisfile_id_vgs_search5 = " +(?:id|vgs) +(?: -|  )(.+)(?: -|  )(.+)(?: -|  )(.+)(?: -|  )(.+)(?: -|  )(.+)";
		Pattern lisfile_id_vgs_Pattern5 = Pattern.compile(lisfile_id_vgs_search5);
		String[][] lisfile_data = new String[20][3];
		int mos_num = 0;
		int id =1;
		int vgs = 2;
		HashMap<String, Double> id_vgs_value_hm = new HashMap<String, Double>();  
		for (int i = 0,k=0; i < lisfile_al.size(); i++) {
			Matcher lisfile_element_Matcher5 = lisfile_element_Pattern5.matcher(lisfile_al.get(i));
			if (lisfile_element_Matcher5.find()) {
				for (int j = 1; j <=5 ; j++) {
					Matcher lisfile_id_Matcher = lisfile_id_vgs_Pattern5.matcher(lisfile_al.get(i+3));
					lisfile_id_Matcher.find();
					Matcher lisfile_vgs_Matcher = lisfile_id_vgs_Pattern5.matcher(lisfile_al.get(i+6));
//					lisfile_vgs_Matcher.find();
					
						double id_tmp = Double.parseDouble(lisfile_id_Matcher.group(j));
						double vgs_tmp = Double.parseDouble(lisfile_vgs_Matcher.group(j));
						id_vgs_value_hm.put(lisfile_element_Matcher5.group(j)+"-id", id_tmp);
						id_vgs_value_hm.put(lisfile_element_Matcher5.group(j)+"-vgs", vgs_tmp);
						lisfile_data[k][mos_num] = lisfile_element_Matcher5.group(j);
						lisfile_data[k][id] = lisfile_element_Matcher5.group(j)+"-id";
						lisfile_data[k][vgs] = lisfile_element_Matcher5.group(j)+"-vgs";
						k++;
				}
				
			}
		}
//		for (int i = 0; i < lisfile_data.length; i++) {
//			for (int j = 0; j < lisfile_data[i].length; j++) {
//				System.out.println(lisfile_data[i][j]);
//			}
//		}
		System.out.println(lisfile_data[0][0]);
		System.out.println(lisfile_data[0][1]);
		System.out.println(lisfile_data[0][2]);
		System.out.println("id value is "+id_vgs_value_hm.get(lisfile_data[0][1]));
		System.out.println("vgs value is "+ id_vgs_value_hm.get(lisfile_data[0][2]));
		}catch (Exception e) {
			System.out.println(e);
			// TODO: handle exception
		}
	}
}
