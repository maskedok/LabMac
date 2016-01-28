
public class TextDiagram {

	private String[][] text_base_ = new String[100][100];
	
	public void putElement(String ele, int column, int row) {
		text_base_[column][row] = ele;
	}
//	column=èc row=â°
	public String getTextDiagram(int column, int row) {
		return text_base_[column][row];
	}
}
