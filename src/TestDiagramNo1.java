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
//		���C��
		public static void main(String[] args) throws InterruptedException, IOException{
			System.out.println("Program Start");
			Pattern SPFILE_ELEMENT_PATTERN = Pattern.compile("(?:(^M\\w+) (\\w+) (\\w+) "
					+ "(\\w+) (\\w+) (cmos(?:p|n)))|(?:((^R|^C|)\\w+) (\\w+) (\\w+))",Pattern.CASE_INSENSITIVE);
			
			HashMap<String, String> np_channel = new HashMap<String, String>();
			
			//		4n=��[�q�@1+4n=�f�q�@2+4n=���[�q�@3+4n=�Q�[�g n=0~
			ArrayList<String> element_card_ = new ArrayList<String>();

			//		sp�t�@�C�����璊�o
			BufferedReader br = new BufferedReader(new FileReader(spfile_name_));
			String tmp_read;
			while((tmp_read = br.readLine())!=null){
				Matcher spfile_element_Matcher = SPFILE_ELEMENT_PATTERN.matcher(tmp_read);
				if (spfile_element_Matcher.find()) {
					if ("cmosn".equals(spfile_element_Matcher.group(6))) {
						element_card_.add(spfile_element_Matcher.group(2));
						element_card_.add(spfile_element_Matcher.group(1));
						element_card_.add(spfile_element_Matcher.group(4));
						element_card_.add(spfile_element_Matcher.group(3));
						np_channel.put(spfile_element_Matcher.group(1), "n");
					}else if ("cmosp".equals(spfile_element_Matcher.group(6))) {
						element_card_.add(spfile_element_Matcher.group(4));
						element_card_.add(spfile_element_Matcher.group(1));
						element_card_.add(spfile_element_Matcher.group(2));
						element_card_.add(spfile_element_Matcher.group(3));
						np_channel.put(spfile_element_Matcher.group(1), "p");
					}else if ("R".equals(spfile_element_Matcher.group(8))||
							  "r".equals(spfile_element_Matcher.group(8))||
							  "C".equals(spfile_element_Matcher.group(8))||
							  "c".equals(spfile_element_Matcher.group(8))) {
						element_card_.add(spfile_element_Matcher.group(9));
						element_card_.add(spfile_element_Matcher.group(7));
						element_card_.add(spfile_element_Matcher.group(10));
						element_card_.add("    ");
					}
				}
			}
			
			System.out.println("���o�ɐ������܂���");
			
			
			
//			��H�}�쐬
			String[][] diagram = new String[200][200];
			for (int i=0; i < 10; i++) {
				for (int j = 0; j <40; j++) {
					diagram[i][j] = "   ";
				}
			}
			int hight=0;
			int wide=3;
			String bottom_node = "vdd";
//			���X�g���O������
			while (element_card_.size()!=0) {
//				���[�q�ɍ����f�q��T���i������vdd�j
				for (int i = 0; i <= element_card_.size(); i=i+4) {
					if (i==element_card_.size()) {
						System.out.println("����ȏ�̒T���͖��p");
						hight=hight-2;
						bottom_node=diagram[hight][wide-3];
						System.out.println("�[�q������ɂЂƂ߂�"+bottom_node);
//						if (bottom_node.equals("vdd")) {
//							w=w+4;
//							System.out.println("��ύX");
//						}
						i=i-element_card_.size();
					}
					System.out.println("���X�g�̒�");
					for (int k = 0; k < element_card_.size(); k=k+4) {
						System.out.print(element_card_.get(k)+" ");
						System.out.print(element_card_.get(k+1)+" ");
						System.out.print(element_card_.get(k+2)+" ");
						System.out.print(element_card_.get(k+3));
						System.out.println();
					}
//					System.out.println("�J��Ԃ��ϐ�i="+i);
					if (element_card_.get(i).equals(bottom_node)) {
						diagram[hight][wide] = element_card_.remove(i);
						System.out.println(hight+" "+wide+" "+diagram[hight][wide]);
						hight++;
						diagram[hight][wide] = element_card_.remove(i);
						System.out.println(hight+" "+wide+" "+diagram[hight][wide]);
						bottom_node   = element_card_.remove(i);
						System.out.println("��"+bottom_node);
						diagram[hight][wide+1] = element_card_.remove(i);
						System.out.println(hight+" "+(wide+1)+" "+diagram[hight][wide+1]);
						hight++;
						i=i-4;
						System.out.println();
					}
					if (bottom_node.equals("vss")) {
						System.out.println("vss�[�q�ɓ��B");
						hight=hight-2;
						bottom_node = diagram[hight][wide];
						wide=wide+3;
						System.out.println("�[�q���ЂƂ߂�"+bottom_node);
					}
				}
			}
			
			String node_001 = "001";
			String node_tmp;
			int diagram_line = 0;
			int j = 0;
//			001�̒T��
//			001����������f�q���o�́i���Vdd�[�q�ɕύX�j
			while (element_card_.size()!=0) {int i=1

				if (element_card_.get(index).equals(node_001)){
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
//							���[�q��gnd����Ȃ������̂ŒT���ĊJ
							}
						}
				    }
				}else if (element_diagram[i][above_node].equals(null)) {
					break;
				}
			}
			
			
//			���X�g�̎c����o��
			for (int i = 0; i < element_card_.size(); i=i+4) {
				System.out.println(element_card_.get(i));
			}
			
		}
	}