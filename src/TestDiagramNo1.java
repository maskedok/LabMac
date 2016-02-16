import java.awt.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestDiagramNo1{
	static String spfile_name_ = "/Network/Servers/minerva.ktlab.el.gunma-u.ac.jp/Volumes/UsersN01/kazuto.okouchi/Desktop/ネットリスト/TopologySeed_300_0.sp";
//	static String spfile_name_ = "/Network/Servers/minerva.ktlab.el.gunma-u.ac.jp/Volumes/UsersN01/kazuto.okouchi/Desktop/ネットリスト/";
//		メイン
		public static void main(String[] args) throws InterruptedException, IOException{
			System.out.println("Program Start");
			Pattern SPFILE_ELEMENT_PATTERN = Pattern.compile("(?:(^M\\w+) (\\w+) (\\w+) "
					+ "(\\w+) (\\w+) (cmos(?:p|n)))|(?:((^R|^C|)\\w+) (\\w+) (\\w+))",Pattern.CASE_INSENSITIVE);
			
			HashMap<String, String> np_channel = new HashMap<String, String>();
			
//			n=上端子　1+n=素子　2+n=下端子
			ArrayList<String> element_card_ = new ArrayList<String>();

			HashMap<String, String> gate_node = new HashMap<String, String>();
			HashMap<String, String> bulk_node = new HashMap<String, String>();
//			spファイルから抽出
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

//			vdd個数のカウント
			int vdd_num = 0;
			for (int i = 0; i < element_card_.size(); i++) {
				if (element_card_.get(i).equals("vdd")) {
					vdd_num++;
				}
			}
			
//			二次元配列
//			列はとりあえずvddの8倍
			vdd_num = vdd_num * 8;
			ArrayList[] line = new ArrayList[(vdd_num)];
			
			for (int j = 0; j < (vdd_num); j++) {
				line[j] = new ArrayList();
			}
//			行はとりあえず列の半分で作成
			ArrayList<ArrayList<String>> column = new ArrayList<ArrayList<String>>();
			for (int j = 0; j < vdd_num/2; j++) {
				column.add(line[j]);
			}
//			残りlineのカウント変数
			int count_line_remaining = vdd_num/2+1;
			
			
//----------キャパシターの抽出start
			Pattern CAPACITOR_PATTERN = Pattern.compile("^C");
			for (int i = 0; i < element_card_.size(); i++) {
				
				Matcher CAPACITOR_MATCHER = CAPACITOR_PATTERN.matcher(element_card_.get(i));
				if (CAPACITOR_MATCHER.find()) {
					System.out.println("ok");
					System.out.println(count_line_remaining);
					line[count_line_remaining].add(element_card_.remove(i-1));
					line[count_line_remaining].add(element_card_.remove(i-1));
					line[count_line_remaining].add(element_card_.remove(i-1));
					if (line[count_line_remaining].get(2).equals("out")) {
						System.out.println("キャパシターが出力端子につながってます");
					}else {
						System.out.println("キャパシターと抵抗が出力端子につながってます");
						for (int k = 0; k < element_card_.size(); k++) {
							if (element_card_.get(k).equals(line[count_line_remaining].get(2))) {
															   element_card_.remove(k);
								line[count_line_remaining].add(element_card_.remove(k));
								line[count_line_remaining].add(element_card_.remove(k));
							}
						}
					}
				}
			}
			count_line_remaining++;
//---------キャパシターの抽出end			
			
			
			
//----------一列目start
			String above_node = "vdd";
			int line_num = 1;
			for (int i = 0; i < element_card_.size(); i++) {
				if (element_card_.get(i).equals(above_node)) {
					element_card_.remove(i);
					line[line_num].add(element_card_.remove(i));
					line[line_num].add(element_card_.remove(i));
					line_num++;
					i--;
				}
			}
			for (int i = 0; i < 10; i++) {
				System.out.println(column.get(i));
			}
//----------一列目end	
			
//----------ノード重複チェックstart
			int node_check_line = 1;
			String check1;
			for (int i = 0; i < vdd_num; i++) {
				try{
					check1 = column.get(i).get(node_check_line);
					for (int j = i; j < vdd_num; j++) {
						if (check1.equals(column.get(i+j).get(node_check_line))) {
							column.add(i+1, column.remove(i+j));
						}
					}
				}
				catch(IndexOutOfBoundsException e){
					continue;
				}
			}
			System.out.println(element_card_);
//--------- ノード重複チェックend			
			
			System.out.println(column.get(1).size());
			
			
			
//			二列目
			int branch_num = 1;
			for (int i = 1; i < column.size(); i++) {
				try{
				above_node = column.get(i).get(node_check_line);
				for (int k = 0; k < element_card_.size(); k=k+3) {
					if (element_card_.get(k).equals(above_node)) {
						switch (branch_num) {
						case 1:
							element_card_.remove(k);
							column.get(i).add(element_card_.remove(k));
							column.get(i).add(element_card_.remove(k));
						case 2:
							i++;
							column.add(i, line[count_line_remaining]);
							for (int j = 0; j < node_check_line; j++) {
								line[count_line_remaining].add("   ");
							}
							element_card_.remove(k);
							column.get(i).add(element_card_.remove(k));
							column.get(i).add(element_card_.remove(k));
						}
						
					}
				}
				}catch(IndexOutOfBoundsException e){
					continue;
				}
			}
			for (int i = 0; i < 10; i++) {
				System.out.println(column.get(i));
			}
			node_check_line = node_check_line +2;
			System.out.println(element_card_);
			
			
			
			
//			三列目
			for (int i = 1; i < column.size(); i++) {
				try{
				above_node = column.get(i).get(node_check_line);
				for (int k = 0; k < element_card_.size(); k=k+3) {
					if (element_card_.get(k).equals(above_node)) {
						element_card_.remove(k);
						column.get(i).add(element_card_.remove(k));
						column.get(i).add(element_card_.remove(k));
					}
				}
				}catch(IndexOutOfBoundsException e){
					continue;
				}
			}
			for (int i = 0; i < 10; i++) {
				System.out.println(column.get(i));
			}
			System.out.println(element_card_);
			
			
			
			
			
			
			
			
			
			
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