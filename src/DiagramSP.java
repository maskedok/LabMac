import java.io.IOException;


public class DiagramSP {
	public static void main(String[] args) throws IOException {
		ReadspElement read_sp_element = new ReadspElement();
		TextDiagram text_diagram = new TextDiagram();
		
		int column=0,row=0;
		for (int j = 0; j < read_sp_element.getElementNum(); j++) {
			if (read_sp_element.getElementCard(j*3).equals("vdd")) {
				text_diagram.putElement(read_sp_element.getElementCard(j+1),j*2 ,row);
			}
		}
		
		for (int i = 0; i < args.length; i++) {
			text_diagram.getTextDiagram(i*2, 1);
			
		}
		
	}
}
