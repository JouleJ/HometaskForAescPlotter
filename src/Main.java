import javax.swing.*;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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

class ChangeIntEpsilon implements ActionListener {
	JTextField textField;
	PlotterPanel plotter;
	
	public ChangeIntEpsilon(JTextField _textField, PlotterPanel _plotter) {
		textField = _textField;
		plotter = _plotter;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		String text = textField.getText();
		double eps = Double.parseDouble(text);
		
		Integrator.setEpsilon(eps);
		plotter.repaint();
	}
};

class ChangeSecantEpsilon implements ActionListener {
	JTextField textField;
	PlotterPanel plotter;
	
	public ChangeSecantEpsilon(JTextField _textField, PlotterPanel _plotter) {
		textField = _textField;
		plotter = _plotter;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		String text = textField.getText();
		double eps = Double.parseDouble(text);
		
		SecantMethod.setEpsilon(eps);
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
		
		JPanel right = new JPanel();
		right.setLayout(new GridLayout(4, 2));
		
		PlotterPanel plotter = new PlotterPanel(new FuncToPlot());
		
		JTextField rectangleTextField = new JTextField("0;0;" + String.valueOf(PlotterPanel.PANEL_SIZE) +
														";" + String.valueOf(PlotterPanel.PANEL_SIZE));
		
		rectangleTextField.addActionListener(new ChangeRectangle(plotter, rectangleTextField));
		
		JTextField intervalTextField = new JTextField("0;1");
		
		intervalTextField.addActionListener(new ChangeInterval(plotter, intervalTextField));
		
		Button buttonRunSecantMethod = new Button("Run Secant Method");
		buttonRunSecantMethod.addActionListener(new RunSecantMethod(plotter));
		
		JTextField intEpsilon = new JTextField(String.valueOf(Integrator.getEpsilon()));
		JTextField secantEpsilon = new JTextField(String.valueOf(SecantMethod.getEpsilon()));
		
		intEpsilon.addActionListener(new ChangeIntEpsilon(intEpsilon, plotter));
		secantEpsilon.addActionListener(new ChangeSecantEpsilon(secantEpsilon, plotter));
		
		right.add(new Label("Plotting rectangle coordinates"));
		right.add(rectangleTextField);
		right.add(new Label("Plotting interval"));
		right.add(intervalTextField);
		
		right.add(new Label("Intergrating epsilon"));
		right.add(intEpsilon);
		right.add(new Label("Secant method epsilon"));
		right.add(secantEpsilon);
		
		root.add(plotter);
		root.add(buttonRunSecantMethod);
		root.add(right);
		
		frame.setContentPane(root);
		frame.setSize(WINDOW_SIZE, WINDOW_SIZE);
		frame.setVisible(true);
	}
}
