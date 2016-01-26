import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReadspElement {
	private ArrayList<String> element_card_ = new ArrayList<String>();
	private String directory_name_ = "/Network/Servers/minerva.ktlab.el.gunma-u.ac.jp/Volumes/UsersN01/kazuto.okouchi/"
			+ "Desktop/";
	private HashMap<String, String> np_channel = new HashMap<String, String>();
	private HashMap<String, String> gate_node = new HashMap<String, String>();
	private HashMap<String, String> bulc_node = new HashMap<String, String>();
	private static final Pattern SPFILE_ELEMENT_PATTERN = Pattern.compile("(?:(^M\\w+) (\\w+) (\\w+) "
							+ "(\\w+) (\\w+) (cmos(?:p|n)))|(?:((^R|^C|)\\w+) (\\w+) (\\w+))",Pattern.CASE_INSENSITIVE);
	private String tmp_read;
	

	public void ReadspElement(String sp_name) throws IOException {
	//	spファイルから抽出
		try {
			BufferedReader br = new BufferedReader(new FileReader(directory_name_+sp_name));
			while((tmp_read = br.readLine())!=null){
				Matcher spfile_element_Matcher = SPFILE_ELEMENT_PATTERN.matcher(tmp_read);
				if (spfile_element_Matcher.find()) {
					if ("cmosn".equals(spfile_element_Matcher.group(6))) {
						element_card_.add(spfile_element_Matcher.group(2));
						element_card_.add(spfile_element_Matcher.group(1));
						element_card_.add(spfile_element_Matcher.group(4));
						gate_node.put(spfile_element_Matcher.group(1),spfile_element_Matcher.group(3));
						bulc_node.put(spfile_element_Matcher.group(1),spfile_element_Matcher.group(5));
						np_channel.put(spfile_element_Matcher.group(1), "n");
					}else if ("cmosp".equals(spfile_element_Matcher.group(6))) {
						element_card_.add(spfile_element_Matcher.group(4));
						element_card_.add(spfile_element_Matcher.group(1));
						element_card_.add(spfile_element_Matcher.group(2));
						gate_node.put(spfile_element_Matcher.group(1),spfile_element_Matcher.group(3));
						bulc_node.put(spfile_element_Matcher.group(1),spfile_element_Matcher.group(5));
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public arra
	public String GetGateNode(String MOSnum){
		return gate_node.get(MOSnum);
	}
}
