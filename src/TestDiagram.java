import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestDiagram {
	public static void main(String[] args) throws InterruptedException, IOException{
		
//		sp-lis-ファイル名
		String full_pass = "/Network/Servers/minerva.ktlab.el.gunma-u.ac.jp/Volumes/UsersN01/kazuto.okouchi/Desktop/";
		String spfile_name = "Common-Differential-amplifer49s4-db";
		String lisfile_name = spfile_name; 
		
//		spファイルの読み込み　
		ArrayList<String> spfile_al = new ArrayList<String>();
		BufferedReader spfile_br = new BufferedReader(new FileReader(full_pass + spfile_name + ".sp"));
		String tmp_spfile_read;
		while((tmp_spfile_read = spfile_br.readLine())!=null){
		spfile_al.add(tmp_spfile_read);
		}
//		lisファイル読み込み
//		ArrayList<String> lisfile_al = new ArrayList<String>();
//		BufferedReader lisfile_br = new BufferedReader(new FileReader(full_pass + lisfile_name + ".lis"));
//		String tmp_lisfile_read;
//		while((tmp_lisfile_read = lisfile_br.readLine()) != null){
//			lisfile_al.add(tmp_lisfile_read);
//		}
//		


		  
//		探索パターン
//		グループ１MOS番号　グループ２ドレイン(ソース)端子　グループ３ゲート端子　グループ４ソース(ドレイン)端子
//		グループ５バルク端子　グループ６MOStype　
//		グループ７RC番号　グループ８RorC　グループ９上端子　グループ１０下端子
		String spfile_element_search = "(?:(^M\\w+) (\\w+) (\\w+) (\\w+) (\\w+) (cmos(?:p|n)))|(?:((^R|^C|)\\w+) (\\w+) (\\w+))";
		Pattern spfile_element_Pattern = Pattern.compile(spfile_element_search, Pattern.CASE_INSENSITIVE);	
		
//		lisファイルから抽出
//		String lisfile_parameter_search = "";
		
//		[素子個数][下記int変数参照]
		String[][] element_diagram = new String[100][6];
		int above_node = 0;
		int element_num = 1;
		int bottom_node = 2;
		int mos_gate_node = 3;

		
		
		
//		判定用変数
		String n_type = "cmosn";
		String p_type = "cmosp";
		String large_R_type = "R";
		String small_R_type = "r";
		String large_C_type = "C";
		String small_C_type = "c";
		
		for (int i = 0, j = 0; i < spfile_al.size(); i++) {
			Matcher spfile_element_Matcher = spfile_element_Pattern.matcher(spfile_al.get(i));
			
			if (spfile_element_Matcher.find()) {
				if (n_type.equals(spfile_element_Matcher.group(6))) {
					element_diagram[j][above_node] = spfile_element_Matcher.group(2);
					element_diagram[j][element_num] = spfile_element_Matcher.group(1)+
							                          "-" +spfile_element_Matcher.group(3);
					element_diagram[j][bottom_node] = spfile_element_Matcher.group(4);
					j++;
				}
				else if ("cmosp".equals(spfile_element_Matcher.group(6))) {
					element_diagram[j][above_node] = spfile_element_Matcher.group(4);
					element_diagram[j][element_num] = spfile_element_Matcher.group(1)+
							                          "-" +spfile_element_Matcher.group(3);
					element_diagram[j][bottom_node] = spfile_element_Matcher.group(2);
					j++;
				}
				else if (large_R_type.equals(spfile_element_Matcher.group(8))||
						small_R_type.equals(spfile_element_Matcher.group(8))) {
					element_diagram[j][above_node] = spfile_element_Matcher.group(9);
					element_diagram[j][element_num] = spfile_element_Matcher.group(7)+"    ";
					element_diagram[j][bottom_node] = spfile_element_Matcher.group(10);
					j++;
				}
				else if (large_C_type.equals(spfile_element_Matcher.group(8))||
						small_C_type.equals(spfile_element_Matcher.group(8))) {
					element_diagram[j][above_node] = spfile_element_Matcher.group(9);
					element_diagram[j][element_num] = spfile_element_Matcher.group(7);
					element_diagram[j][bottom_node] = spfile_element_Matcher.group(10);
					j++;
					}
				}
			}
//		テスト用表示ソース
//		for (int i = 0; i < 20; i++) {
//			for (int j = 0; j < 3; j++) {
//				System.out.println(element_diagram[i][j]);
//			}
//		}
		
		
//		System.out.println(spfile_element_Matcher.find());
//		for (int i = 0; i <= spfile_element_Matcher.groupCount(); i++) {
//			System.out.println("group "+ i + " is "+spfile_element_Matcher.group(i));
//		}
		
//		001ノードを探す
//		|
//		素子番号-MOSならゲート
//		｜
//		下端子
//		下端子に繋がる上端子探索
//		以下gndまで繰り返し		
//		[列][段]
		String[][] element_diagram_full = new String[100][100];
		for (int i = 0; i < element_diagram_full.length; i++) {
			for (int j = 0; j < element_diagram_full[i].length; j++) {
				element_diagram_full[i][j] = "        ";
			}
		}
		
		String node_001 = "001";
		String node_tmp;
		int diagram_line = 0;
		int j = 0;
//		001の探索
//		001があったら素子を出力（後はVdd端子に変更）
		for (int i = 0 ; i <20; i++) {

			if (element_diagram[i][above_node].equals(node_001)){
				element_diagram_full[j][diagram_line] = " |      ";
				diagram_line++;
				element_diagram_full[j][diagram_line] = element_diagram[i][element_num]+" ";
				diagram_line++;
				element_diagram_full[j][diagram_line] = " |      ";
				diagram_line++;
				element_diagram_full[j][diagram_line] = element_diagram[i][bottom_node]+"     ";
				diagram_line++;
			    node_tmp = element_diagram[i][bottom_node];
			    for (int k = 0; k < 20; k++) {
					if (element_diagram[k][above_node].equals(node_tmp)) {
						element_diagram_full[j][diagram_line] = " |      ";
						diagram_line++;
						element_diagram_full[j][diagram_line] = element_diagram[k][element_num]+" ";
						diagram_line++;
						element_diagram_full[j][diagram_line] = " |      ";
						diagram_line++;
						element_diagram_full[j][diagram_line] = element_diagram[k][bottom_node]+"     ";
						diagram_line++;
					
						if (element_diagram[k][bottom_node].equals("gnd")){
						j++;
						diagram_line = 0;
						break;
						}else {
							node_tmp = element_diagram[k][bottom_node];
//						下端子がgndじゃなかったので探索再開
						}
					}
			    }
			}else if (element_diagram[i][above_node].equals(null)) {
				break;
			}
		}
		System.out.println("Vdd___________________________________________________________");
		for (int i = 0; i < 12; i++) {
			for (int l = 0; l < 8; l++) {
				System.out.print(element_diagram_full[l][i]);
			}
			System.out.println();
		}
	
	}
}
