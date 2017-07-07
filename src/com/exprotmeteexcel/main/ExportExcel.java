package com.exprotmeteexcel.main;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exprotmeteexcel.service.DdlToolService;
import com.exprotmeteexcel.service.ExprotMeteExcelService;
import com.exprotmeteexcel.service.imp.DdlToolServiceImpl;
import com.exprotmeteexcel.service.imp.ExprotMeteExcelServiceImpl;
import com.exprotmeteexcel.utl.ExcelUtility;
import com.exprotmeteexcel.utl.FileUtil;
import com.exprotmeteexcel.utl.Utl;

public class ExportExcel {

	private static final Logger log = LoggerFactory.getLogger(ExportExcel.class);

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JButton confbutton = new JButton("1��ѡ�������ļ�·��");
		JButton button = new JButton("2�������ť����excel�ļ�ѡ��");
		JButton button1 = new JButton("3�������ť����excel�ļ�ѡ��,����DDL���");
		JButton button4 = new JButton("4������ XML�ļ�");
		confbutton.addMouseListener(new Configlj(frame));
		button1.addMouseListener(new ShowDdlLintener(frame));
		button.addMouseListener(new ShowDialogLintener(frame));
		button4.addMouseListener(new Exml(frame));		
		frame.add(confbutton, BorderLayout.WEST);
		frame.add(button, BorderLayout.CENTER);
		frame.add(button1, BorderLayout.EAST);
		frame.add(button4, BorderLayout.SOUTH);
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

			com.exprotmeteexcel.main.Testdbconn.ReadExcelExprot(filePath);
		} else {
			JOptionPane.showMessageDialog(null, "���ã���Ҫ�����excel·��" + filePath + "������!");

		}
	}
}

class ShowDdlLintener extends MouseAdapter {
	JFrame frame;
	String path;
	private static final Logger log = LoggerFactory.getLogger(ShowDdlLintener.class);

	public ShowDdlLintener(JFrame frame) {
		this.frame = frame;
	}

	public ShowDdlLintener(JFrame frame, String path) {
		this.frame = frame;
		this.path = path;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		super.mouseClicked(arg0);
		Boolean bn = false;
		JFileChooser chooser = new JFileChooser(".");
		chooser.showOpenDialog(frame);
		String filePath = chooser.getSelectedFile().getAbsolutePath();
		if (Utl.IsFileExists(filePath)) {
			DdlToolService dd = new DdlToolServiceImpl();
			bn = dd.exportDdl(filePath);
		} else {
			JOptionPane.showMessageDialog(null, "���ã���Ҫ�����excel·��" + filePath + "������!");

		}
		if (bn) {
			log.info("���гɹ���");
		} else {
			log.info("����ʧ�ܣ�");
		}

	}
}

class Configlj extends MouseAdapter {
	JFrame frame;

	public Configlj(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		super.mouseClicked(arg0);
		JFileChooser chooser = new JFileChooser(".");
		chooser.showOpenDialog(frame);
		String filePath = chooser.getSelectedFile().getAbsolutePath();
		// System.out.println(filePath);
		if (Utl.IsFileExists(filePath)) {
			// com.exprotmeteexcel.main.Testdbconn.getTableMateBean(filePath);
			//com.exprotmeteexcel.main.Testdbconn.getTableMateBeanByTest(filePath);
			ExprotMeteExcelService tt = new ExprotMeteExcelServiceImpl();
			tt.ExprotTableMateBean(filePath);
			
		} else {
			JOptionPane.showMessageDialog(null, "���ã���Ҫ�����·��" + filePath + "������!");

		}

	}
}

class Exml extends MouseAdapter {
	JFrame frame;

	public Exml(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		super.mouseClicked(arg0);
		JFileChooser chooser = new JFileChooser(".");
		chooser.showOpenDialog(frame);
		String filePath = chooser.getSelectedFile().getAbsolutePath();
		// System.out.println(filePath);
		if (Utl.IsFileExists(filePath)) {
			Boolean bn = exportXml(filePath);
			if (bn) {
				runExportXml();
			}

		} else {
			JOptionPane.showMessageDialog(null, "���ã���Ҫ�����·��" + filePath + "������!");

		}

	}

	public Boolean exportXml(String path) {
		Boolean bn = false;
		List<Object[]> ls = ExcelUtility.getReadExcelContent(path, 1);
		String system = ls.get(0)[3].toString().split("_")[1];
		bn = FileUtil.wirteProperty("properties\\Pub.properties", "PROJECTPATH",
				"properties\\businessconfig\\" + system + ".properties");
		bn = FileUtil.wirteProperty("properties\\businessconfig\\" + system + ".properties", "ExcelPath", path);
		return bn;
	}

	public void runExportXml() {
		org.FactoryMapping.main(null);

	}

}
