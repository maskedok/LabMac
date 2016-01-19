
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Master {
	
	private static final int ABOVE_NODE    = 0;
	private static final int ELEMNT_NUM    = 1;
	private static final int BOTTOM_NODE   = 2;
	private static final int MOS_GATE_NODE = 3;
	
	
	private String pass_;
	private String spfile_name_;
	

//	1+4n=素子(+n or p)　2+4n=上端子　3+4n=下端子　4+4n=ゲート
	private ArrayList<String> element_card_ = new ArrayList<String>();
	
	private String[] capaciter_ = new String[3];
	
	public Master(String pass, String spfile_name){
		this.pass_ = pass;
		this.spfile_name_ = spfile_name;
	}
	
	public void readspFile()throws IOException{
		
		
//		探索パターン
//		グループ１MOS番号　グループ２ドレイン(ソース)端子　グループ３ゲート端子　グループ４ソース(ドレイン)端子
//		グループ５バルク端子　グループ６MOStype　
//		グループ７RC番号　グループ８RorC　グループ９上端子　グループ１０下端子
		String spfile_element_search = "(?:((^M)\\w+) (\\w+) (\\w+) (\\w+) (\\w+) (cmos(?:p|n)))|(?:((^R|^C|^r)\\w+) (\\w+) (\\w+))";
		Pattern spfile_element_Pattern = Pattern.compile(spfile_element_search, Pattern.CASE_INSENSITIVE);	
		
		
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(pass_ + spfile_name_));
			String tmp_read;
			while((tmp_read = br.readLine())!=null){

			Matcher spfile_element_Matcher = spfile_element_Pattern.matcher(tmp_read);
			spfile_element_Matcher.find();
			if (spfile_element_Matcher.find()) {
				if ("cmosn".equals(spfile_element_Matcher.group(6))) {
					element_card_.add(spfile_element_Matcher.group(2));
					element_card_.add(spfile_element_Matcher.group(1)+"n");
					element_card_.add(spfile_element_Matcher.group(4));
					element_card_.add(spfile_element_Matcher.group(3));
				}
				else if ("cmosp".equals(spfile_element_Matcher.group(6))) {
					element_card_.add(spfile_element_Matcher.group(4));
					element_card_.add(spfile_element_Matcher.group(1)+"p");
					element_card_.add(spfile_element_Matcher.group(2));
					element_card_.add(spfile_element_Matcher.group(3));
				}
				}
				else if ("R".equals(spfile_element_Matcher.group(8))||
						"r".equals(spfile_element_Matcher.group(8))) {
					element_card_.add(spfile_element_Matcher.group(9));
					element_card_.add(spfile_element_Matcher.group(7));
					element_card_.add(spfile_element_Matcher.group(10));
				}
				else if ("C".equals(spfile_element_Matcher.group(8))||
						"c".equals(spfile_element_Matcher.group(8))) {
					capaciter_[0] = spfile_element_Matcher.group(9);
					capaciter_[1] = spfile_element_Matcher.group(7);
					capaciter_[2] = spfile_element_Matcher.group(10);
					}
				}
			
			} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public void showCard() {
		for (int i = 0; i < element_card_.size(); i++) {
			System.out.println(element_card_.get(i));
	
}		
	}
}
