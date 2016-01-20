import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Tmp1 {
	static String spfile_name_ = "/Network/Servers/minerva.ktlab.el.gunma-u.ac.jp/Volumes/UsersN01/kazuto.okouchi/Library/"
			+ "Containers/com.coteditor.CotEditor/Data/Desktop/Topologycopy.ps";
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(spfile_name_));
		String tmp_read;
//		while((tmp_read = br.readLine())!=null){
//			System.out.println("\""+tmp_read+"\",");
		String hu = "jfiejfoa";
		String[] hStrings = {"hufiah","hufheiu"+hu,};
//		}  
		System.out.println(hStrings[1]);
	}

}
