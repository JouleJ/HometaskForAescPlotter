import javax.swing.*;

import java.awt.Button;
import java.awt.Label;
import java.awt.event.*;
import java.util.ArrayList;

class TextParser {
	public static ArrayList<Integer> parseInts(String str) {
		ArrayList<Integer> ret = new ArrayList<Integer>() ;
		
		int n = str.length();
		
		for(int i = 0; i < n;)
		{
			if(!Character.isDigit(str.charAt(i))) {
				++i;
			} else {
				int j = i;
				
				while(j + 1 < n && Character.isDigit(str.charAt(j + 1)))
					++j;
				
				String t = str.substring(i, j + 1);
				//System.out.println("t = " + t);
				ret.add(Integer.parseInt(t));
				
				i = j + 1;
			}
		}
		
		return ret;
	
	}
	
	private static boolean isGoodForDouble(Character ch) {
		return Character.isDigit(ch) || ch == '.' || ch == ',' || ch == '-';
	}
	
	public static ArrayList<Double> parseDoubles(String str) {
		ArrayList<Double> ret = new ArrayList<Double>() ;
		
		int n = str.length();
		
		for(int i = 0; i < n;) {
			Character ch = str.charAt(i);
			
			if(!isGoodForDouble(ch)) {
				++i;
			} else {
				int j = i;
				
				while(j + 1 < n && isGoodForDouble(str.charAt(j + 1)))
					++j;
				
				String t = str.substring(i, j + 1), u = "";
				
				for(int k = 0; k < t.length(); ++k) {
					Character chr = t.charAt(k);
					
					if(chr == ',')
						chr = '.';
					
					u += String.valueOf(chr);
				}
				
				ret.add(Double.parseDouble(u));
				
				i = j + 1;
			}
		}
		
		return ret;
	}
};

class ChangeRectangle implements ActionListener {
	PlotterPanel plotter = null;
	JTextField textField = null;
	
	public ChangeRectangle(PlotterPanel _plotter, JTextField _textField) {
		plotter = _plotter;
		textField = _textField;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		String text = textField.getText();
		ArrayList<Integer> list = TextParser.parseInts(text);
		
		if(list.size() == 4) {
			plotter.setRectangle((int)list.get(0), (int)list.get(1), (int)list.get(2), (int)list.get(3));
			plotter.repaint();
		}
	}
};

class ChangeInterval implements ActionListener {
	PlotterPanel plotter = null;
	JTextField textField = null;
	
	public ChangeInterval(PlotterPanel _plotter, JTextField _textField) {
		plotter = _plotter;
		textField = _textField;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		String text = textField.getText();
		ArrayList<Double> list = TextParser.parseDoubles(text);
				
		if(list.size() == 2) {
			plotter.setInterval((double)list.get(0), (double)list.get(1));
			plotter.repaint();
		}
	}
};

class RunSecantMethod implements ActionListener {
	private PlotterPanel plotter = null;
	
	public RunSecantMethod(PlotterPanel _plotter) {
		plotter = _plotter;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		plotter.enableSecantMethod();
		plotter.repaint();
	}
};

public class Main {

	public static final int WINDOW_SIZE = 800;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		
		PlotterPanel plotter = new PlotterPanel(new FuncToPlot());
		
		JTextField rectangleTextField = new JTextField("0;0;" + String.valueOf(PlotterPanel.PANEL_SIZE) +
														";" + String.valueOf(PlotterPanel.PANEL_SIZE));
		
		rectangleTextField.addActionListener(new ChangeRectangle(plotter, rectangleTextField));
		
		JTextField intervalTextField = new JTextField("0;1");
		
		intervalTextField.addActionListener(new ChangeInterval(plotter, intervalTextField));
		
		Button buttonRunSecantMethod = new Button("Run Secant Method");
		buttonRunSecantMethod.addActionListener(new RunSecantMethod(plotter));
		
		root.add(plotter);
		root.add(new Label("Plotting rectangle coordinates"));
		root.add(rectangleTextField);
		root.add(new Label("Plotting interval"));
		root.add(intervalTextField);
		root.add(buttonRunSecantMethod);
		
		frame.setContentPane(root);
		frame.setSize(WINDOW_SIZE, WINDOW_SIZE);
		frame.setVisible(true);
	}
}
