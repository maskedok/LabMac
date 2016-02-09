import java.awt.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestDiagramNo1{
	static String spfile_name_ = "/Network/Servers/minerva.ktlab.el.gunma-u.ac.jp/Volumes/UsersN01/kazuto.okouchi/"
			+ "Desktop/Common-Differential-amplifer49s4-db.sp";
//		メイン
		public static void main(String[] args) throws InterruptedException, IOException{
			System.out.println("Program Start");
			Pattern SPFILE_ELEMENT_PATTERN = Pattern.compile("(?:(^M\\w+) (\\w+) (\\w+) "
					+ "(\\w+) (\\w+) (cmos(?:p|n)))|(?:((^R|^C|)\\w+) (\\w+) (\\w+))",Pattern.CASE_INSENSITIVE);
			
			HashMap<String, String> np_channel = new HashMap<String, String>();
			
			//		4n=上端子　1+4n=素子　2+4n=下端子　3+4n=ゲート n=0~
			ArrayList<String> element_card_ = new ArrayList<String>();

			HashMap<String, String> gate_node = new HashMap<String, String>();
			HashMap<String, String> bulk_node = new HashMap<String, String>();
			//		spファイルから抽出
			BufferedReader br = new BufferedReader(new FileReader(spfile_name_));
			String tmp_read;
			while((tmp_read = br.readLine())!=null){
				Matcher spfile_element_Matcher = SPFILE_ELEMENT_PATTERN.matcher(tmp_read);
				if (spfile_element_Matcher.find()) {
					if ("cmosn".equals(spfile_element_Matcher.group(6))) {
						element_card_.add(spfile_element_Matcher.group(2));
						element_card_.add(spfile_element_Matcher.group(1));
						element_card_.add(spfile_element_Matcher.group(4));
						gate_node.put(spfile_element_Matcher.group(1),spfile_element_Matcher.group(3));
						bulk_node.put(spfile_element_Matcher.group(1),spfile_element_Matcher.group(5));
						np_channel.put(spfile_element_Matcher.group(1), "n");
					}else if ("cmosp".equals(spfile_element_Matcher.group(6))) {
						element_card_.add(spfile_element_Matcher.group(4));
						element_card_.add(spfile_element_Matcher.group(1));
						element_card_.add(spfile_element_Matcher.group(2));
						gate_node.put(spfile_element_Matcher.group(1),spfile_element_Matcher.group(3));
						bulk_node.put(spfile_element_Matcher.group(1),spfile_element_Matcher.group(5));
						np_channel.put(spfile_element_Matcher.group(1), "p");
					}else if ("R".equals(spfile_element_Matcher.group(8))||
							  "r".equals(spfile_element_Matcher.group(8))||
							  "C".equals(spfile_element_Matcher.group(8))||
							  "c".equals(spfile_element_Matcher.group(8))) {
						element_card_.add(spfile_element_Matcher.group(9));
						element_card_.add(spfile_element_Matcher.group(7));
						element_card_.add(spfile_element_Matcher.group(10));
					}
				}
			}
			
			System.out.println("抽出に成功しました");
//			`vdd個数のカウント
			int vdd_num = 0;
			for (int i = 0; i < element_card_.size(); i++) {
				if (element_card_.get(i).equals("vdd")) {
					vdd_num++;
				}
				System.out.println(element_card_.get(i));
				if
			}
//			二次元配列
			
			ArrayList[] line = new ArrayList[(vdd_num*8)];
			
			for (int j = 0; j < (vdd_num*8); j++) {
				line[j] = new ArrayList();
			}
			System.out.println((vdd_num*8));
			for (int i = 0; i < (vdd_num*8); i++) {
				line[i].add("ffffff");
			}
			
			
			ArrayList<ArrayList<String>> column = new ArrayList<ArrayList<String>>();
			for (int j = 0; j < vdd_num; j++) {
				column.add(line[j]);
			}
			System.out.println(column.get(0).get(0));
//			System.out.println(column.get(0).get(0));
//			List<ArrayList>[] column = new List[vdd_num*8];
//			hairetuso-toyou
//			ArrayList<String> tmpArrayList = new ArrayList<String>();
			
			
//			回路図作成
//			for (int i=0; i < vdd_num; i++) {
//				for (int j = 0; j <element_card_.size; j++) {
//					if (element_card_.get(j).equals("vdd")) {
//						element_card_.remove(j);
//						
//					}
//				}
//				}
//			}
//			int co=0;
//			int wide=;
//			String bottom_node = "vdd";
////			リストか０か判定
//			while (element_card_.size()!=0) {
////				下端子に合う素子を探索（初期はvdd）
//				for (int i = 0; i <= element_card_.size(); i=i+4) {
//					if (i==element_card_.size()) {
//						System.out.println("これ以上の探索は無用");
//						hight=hight-2;
//						bottom_node=diagram[hight][wide-3];
//						System.out.println("端子をさらにひとつ戻す"+bottom_node);
////						if (bottom_node.equals("vdd")) {
////							w=w+4;
////							System.out.println("列変更");
////						}
//						i=i-element_card_.size();
//					}
//					System.out.println("リストの中");
//					for (int k = 0; k < element_card_.size(); k=k+4) {
//						System.out.print(element_card_.get(k)+" ");
//						System.out.print(element_card_.get(k+1)+" ");
//						System.out.print(element_card_.get(k+2)+" ");
//						System.out.print(element_card_.get(k+3));
//						System.out.println();
//					}
////					System.out.println("繰り返し変数i="+i);
//					if (element_card_.get(i).equals(bottom_node)) {
//						diagram[hight][wide] = element_card_.remove(i);
//						System.out.println(hight+" "+wide+" "+diagram[hight][wide]);
//						hight++;
//						diagram[hight][wide] = element_card_.remove(i);
//						System.out.println(hight+" "+wide+" "+diagram[hight][wide]);
//						bottom_node   = element_card_.remove(i);
//						System.out.println("下"+bottom_node);
//						diagram[hight][wide+1] = element_card_.remove(i);
//						System.out.println(hight+" "+(wide+1)+" "+diagram[hight][wide+1]);
//						hight++;
//						i=i-4;
//						System.out.println();
//					}
//					if (bottom_node.equals("vss")) {
//						System.out.println("vss端子に到達");
//						hight=hight-2;
//						bottom_node = diagram[hight][wide];
//						wide=wide+3;
//						System.out.println("端子をひとつ戻す"+bottom_node);
//					}
//				}
//			}
//			
//			String node_001 = "001";
//			String node_tmp;
//			int diagram_line = 0;
//			int j = 0;
////			001の探索
////			001があったら素子を出力（後はVdd端子に変更）
//			while (element_card_.size()!=0) {int i=1
//
//				if (element_card_.get(index).equals(node_001)){
//					element_diagram_full[j][diagram_line] = " |      ";
//					diagram_line++;
//					element_diagram_full[j][diagram_line] = element_diagram[i][element_num]+" ";
//					diagram_line++;
//					element_diagram_full[j][diagram_line] = " |      ";
//					diagram_line++;
//					element_diagram_full[j][diagram_line] = element_diagram[i][bottom_node]+"     ";
//					diagram_line++;
//				    node_tmp = element_diagram[i][bottom_node];
//				    for (int k = 0; k < 20; k++) {
//						if (element_diagram[k][above_node].equals(node_tmp)) {
//							element_diagram_full[j][diagram_line] = " |      ";
//							diagram_line++;
//							element_diagram_full[j][diagram_line] = element_diagram[k][element_num]+" ";
//							diagram_line++;
//							element_diagram_full[j][diagram_line] = " |      ";
//							diagram_line++;
//							element_diagram_full[j][diagram_line] = element_diagram[k][bottom_node]+"     ";
//							diagram_line++;
//						
//							if (element_diagram[k][bottom_node].equals("gnd")){
//							j++;
//							diagram_line = 0;
//							break;
//							}else {
//								node_tmp = element_diagram[k][bottom_node];
////							下端子がgndじゃなかったので探索再開
//							}
//						}
//				    }
//				}else if (element_diagram[i][above_node].equals(null)) {
//					break;
//				}
//			}
//			
//			
////			リストの残りを出力
//			for (int i = 0; i < element_card_.size(); i=i+4) {
//				System.out.println(element_card_.get(i));
//			}
			
		}
	}