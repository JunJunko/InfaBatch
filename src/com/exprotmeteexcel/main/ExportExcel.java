package com.exprotmeteexcel.main;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.exprotmeteexcel.utl.Utl;

public class ExportExcel {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JButton button = new JButton("点击按钮进入excel文件选择");

		button.addMouseListener(new ShowDialogLintener(frame));
		frame.add(button, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		
	}
}

class ShowDialogLintener extends MouseAdapter {
	JFrame frame;

	public ShowDialogLintener(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		super.mouseClicked(arg0);
		JFileChooser chooser = new JFileChooser(".");
		chooser.showOpenDialog(frame);
		String filePath = chooser.getSelectedFile().getAbsolutePath();
		if (Utl.IsFileExists(filePath)) {
			com.exprotmeteexcel.main.Testdbconn.ReadExcelExprot(filePath,"InfaBatch\\properties\\EXPORTMETA.properties");
		} else {
			JOptionPane.showMessageDialog(null, "您好，您要导入的excel路径" + filePath + "不存在!");

		}
	}
}