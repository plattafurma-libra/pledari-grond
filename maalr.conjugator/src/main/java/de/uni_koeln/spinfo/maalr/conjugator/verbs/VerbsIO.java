package de.uni_koeln.spinfo.maalr.conjugator.verbs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Set;

public class VerbsIO {

	public static final String input_dir = "data/input/";
	public static final String output_dir = "data/output/";

	// Non-instantiable
	private VerbsIO() {
		throw new AssertionError();
	}

	public static LineNumberReader getReader(String fileName) throws IOException {

		FileInputStream fis = new FileInputStream(input_dir + fileName);
		InputStreamReader isr = new InputStreamReader(fis, "UTF8");
		LineNumberReader reader = new LineNumberReader(isr);
		return reader;
	}

	public static <T> void printList(List<T> list, String fileName)
			throws IOException {

		String outFile = output_dir + fileName + ".tab";
		FileOutputStream fos = new FileOutputStream(outFile);
		Writer out = new OutputStreamWriter(fos, "UTF8");
		for (T value : list) {
			out.append(value + "\n");
		}
		out.close();
	}

	public static <T> void printSet(Set<T> set, String fileName)
			throws IOException {

		String outFile = output_dir + fileName + ".tab";
		FileOutputStream fos = new FileOutputStream(outFile);
		Writer out = new OutputStreamWriter(fos, "UTF8");
		for (T value : set) {
			out.append(value + "\n");
		}
		out.close();
	}
}
