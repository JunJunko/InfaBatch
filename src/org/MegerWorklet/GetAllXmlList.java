package org.MegerWorklet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetAllXmlList {
	String type = "";
	String path = new String();

	public GetAllXmlList(String path) {
		this.type = path;
	}

	public List<String> getInitCheckFileName() {
		// Â·¾¶
		if (type.equals("init")) {
			path = "xml\\InitXml";
		} else {
			path = "xml\\CheckXml";
		}
		File f = new File(path);
		if (!f.exists()) {
			System.out.println(path + " not exists");
		}

		File fa[] = f.listFiles();
		List<String> a = new ArrayList<String>();
		for (int i = 0; i < fa.length; i++) {
			File fs = fa[i];
			if (!fs.isDirectory()) {
				a.add(path + "\\" + fs.getName());
				// System.out.println(fs.getName());

			}
		}
		return a;
	}

	public static List<String> getUpsertFileName() {
		// Â·¾¶
		String path = "xml//";
		File f = new File(path);
		if (!f.exists()) {
			System.out.println(path + " not exists");
		}

		File fa[] = f.listFiles();
		List<String> a = new ArrayList<String>();
		for (int i = 0; i < fa.length; i++) {
			File fs = fa[i];
			if (fs.getName().equals("Append") || fs.getName().equals("InsertXml") || fs.getName().equals("UpsertXml")
					|| fs.getName().equals("ZipXml")) {

				// System.out.println(fs.getName());
				File ffs[] = fs.listFiles();
				for (int j = 0; j < ffs.length; j++) {
					File l = ffs[j];
					// System.out.println(l.getName());
					if (!l.isDirectory()) {
						a.add(f.getName() + "\\" +fs.getName() + "\\" + l.getName());
					}
				}

			}
		}
		return a;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetAllXmlList a = new GetAllXmlList("xml\\");
		System.out.println(a.getUpsertFileName());

	}

}
