package com.exprotmeteexcel.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.MegerWorklet.CreateWorkltXml;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.exprotmeteexcel.service.DdlToolService;
import com.exprotmeteexcel.service.ExprotMeteExcelService;
import com.exprotmeteexcel.service.imp.DdlToolServiceImpl;
import com.exprotmeteexcel.service.imp.ExprotMeteExcelServiceImpl;
import com.exprotmeteexcel.utl.ExcelUtility;
import com.exprotmeteexcel.utl.FileUtil;
import com.exprotmeteexcel.utl.Utl;

public class ExportExcel {

	private static final Log log = LogFactory.getLog(ExportExcel.class);

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		addComponentsToPane(frame.getContentPane());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();

	}

	public static void addComponentsToPane(Container pane) {
		JPanel xPanel = new JPanel();
		xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.X_AXIS));
//		 addButtons(xPanel);
		JPanel yPanel = new JPanel();
		yPanel.setLayout(new BoxLayout(yPanel, BoxLayout.Y_AXIS));
		addButtons(yPanel);

		pane.add(yPanel, BorderLayout.PAGE_START);
		pane.add(xPanel, BorderLayout.PAGE_END);
	}

	private static void addAButton(String text, Container container, MouseAdapter event) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
//		Dimension preferredSize = new Dimension(500,20);
//		button.setPreferredSize(preferredSize );
		// ShowDdlLintener p = new ShowDdlLintener(frame);
		button.addMouseListener(event);
		container.add(button);
	}

	private static void addButtons(Container container) {

		JFrame frame = new JFrame();
		MouseAdapter even1 = new Configlj(frame);
		MouseAdapter even2 = new ShowDialogLintener(frame);
		MouseAdapter even3 = new ShowDdlLintener(frame);
		MouseAdapter even4 = new Exml(frame);
		MouseAdapter even5 = new MegerXML(frame);
		MouseAdapter even6 = new ImprotXml(frame);
		addAButton("1��ѡ�������ļ������������ļ����Ե���Դ��Ԫ���ݵ�Excel", container, even1);
		addAButton("2�������ťѡ��excel�ļ������޸ĺ�Excel��Piֵ������߼�", container, even2);
		addAButton("3�������ťѡ����2���ɵ�excel�ļ�,����DDL���", container, even3);
		addAButton("4��ѡ��ָ��Excel�ļ������� Powercenter��XML�ļ�", container, even4);
		addAButton("5���ϲ�Worklet XML", container, even5);
		addAButton("6������XML��Repository", container, even6);

	}
}

class MegerXML extends MouseAdapter{
	JFrame frame;
	public MegerXML(JFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		CreateWorkltXml Wlinit = new CreateWorkltXml("init");
		CreateWorkltXml Wlincrement = new CreateWorkltXml("increment");
		CreateWorkltXml Wlcheck = new CreateWorkltXml("check");
		String initString = Wlinit.MegerInitCheckSessionElement();
		String incrementString = Wlincrement.MegerUpsertSessionElement();
		String checkString = Wlcheck.MegerInitCheckSessionElement();
		Wlinit.WriteXml(initString);
		Wlincrement.WriteXml(incrementString);
		Wlcheck.WriteXml(checkString);
	}
	
}

class ShowDialogLintener extends MouseAdapter {
	JFrame frame;
	private static final Log log = LogFactory.getLog(ShowDialogLintener.class);

	public ShowDialogLintener(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		super.mouseClicked(arg0);
		JFileChooser chooser = new JFileChooser(".");
		// chooser.showOpenDialog(frame);

		int option = chooser.showDialog(null, null);
		String filePath = "";
		if (option == JFileChooser.APPROVE_OPTION) { // �жϴ����Ƿ����Ǵ򿪻򱣴�
			filePath = chooser.getSelectedFile().getAbsolutePath();
			if (Utl.IsFileExists(filePath)) {
				ExprotMeteExcelService tt = new ExprotMeteExcelServiceImpl();
				tt.ReadExcelExprot(filePath);
			}
		} else {
			// û��ѡ�񣬵���ȡ����Ҫ��Щʲô
			log.info("ȡ��ѡ��");
		}

	}
}

class ShowDdlLintener extends MouseAdapter {
	JFrame frame;
	String path;
	private static final Log log = LogFactory.getLog(ShowDdlLintener.class);

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
		int option = chooser.showDialog(null, null);
		String filePath = "";
		if (option == JFileChooser.APPROVE_OPTION) { // �жϴ����Ƿ����Ǵ򿪻򱣴�
			filePath = chooser.getSelectedFile().getAbsolutePath();
			if (Utl.IsFileExists(filePath)) {
				DdlToolService dd = new DdlToolServiceImpl();
				bn = dd.exportDdl(filePath);
			}
		} else {
			// û��ѡ�񣬵���ȡ����Ҫ��Щʲô
			log.info("ȡ��ѡ��");
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
	private static final Log log = LogFactory.getLog(Configlj.class);

	public Configlj(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		super.mouseClicked(arg0);
		JFileChooser chooser = new JFileChooser(".");
		String filePath = "";
		int option = chooser.showDialog(null, null);
		if (option == JFileChooser.APPROVE_OPTION) { // �жϴ����Ƿ����Ǵ򿪻򱣴�
			filePath = chooser.getSelectedFile().getAbsolutePath();
			if (Utl.IsFileExists(filePath)) {
				// com.exprotmeteexcel.main.Testdbconn.getTableMateBean(filePath);
				// com.exprotmeteexcel.main.Testdbconn.getTableMateBeanByTest(filePath);

				ExprotMeteExcelService tt = new ExprotMeteExcelServiceImpl();
				tt.ExprotTableMateBean(filePath);
			}
		} else {
			// û��ѡ�񣬵���ȡ����Ҫ��Щʲô
			log.info("ȡ��ѡ��");
		}

	}
}

class Exml extends MouseAdapter {
	JFrame frame;

	private static final Log log = LogFactory.getLog(Exml.class);

	public Exml(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		super.mouseClicked(arg0);
		JFileChooser chooser = new JFileChooser(".");
		String filePath = "";
		int option = chooser.showDialog(null, null);
		if (option == JFileChooser.APPROVE_OPTION) { // �жϴ����Ƿ����Ǵ򿪻򱣴�
			filePath = chooser.getSelectedFile().getAbsolutePath();
			if (Utl.IsFileExists(filePath)) {
				Boolean bn = exportXml(filePath);
				if (bn) {
					runExportXml();
				}

			}
		} else {
			// û��ѡ�񣬵���ȡ����Ҫ��Щʲô
			log.info("ȡ��ѡ��");
		}

	}

	public Boolean exportXml(String path) {
		Boolean bn = false;
		List<Object[]> ls = ExcelUtility.getReadExcelContent(path, 1);
		String system = ls.get(0)[23].toString();
		String spath = system.substring(system.indexOf("(") + 1, system.indexOf(")"));
		ExprotMeteExcelService em = new ExprotMeteExcelServiceImpl();
		bn = FileUtil.wirteProperty("properties\\Pub.properties", "PROJECTPATH", spath);
		bn = FileUtil.wirteProperty(spath, "ExcelPath", path);
		// ���ɵ���XML�Ŀ����ļ�
		bn = em.ExprotXmlModel(spath);
		return bn;
	}

	public void runExportXml() {
		org.FactoryMapping.main(null);

	}

}

class ImprotXml extends MouseAdapter {
	JFrame frame;

	private static final Log log = LogFactory.getLog(Exml.class);

	public ImprotXml(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		JFileChooser chooser = new JFileChooser(".");
		Boolean bn = false;
		File[] files = null;
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// ����ֻ��ѡ��Ŀ¼
		JFileChooser parent = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		parent.setMultiSelectionEnabled(true);
		int returnVal = chooser.showOpenDialog(parent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			files = chooser.getSelectedFiles();
			if (!Utl.isEmpty(files)) {
				ExprotMeteExcelService em = new ExprotMeteExcelServiceImpl();
				Map<String, Object> map = em.ExprotXmltoBat(files);
				if (!Utl.isEmpty(map)) {
					bn = em.ExprotXmlToinfoS(map);
				}
				if (bn) {
					log.info("���гɹ���");
				} else {
					log.info("������ʧ�������");
				}

			}
		}

	}

}
