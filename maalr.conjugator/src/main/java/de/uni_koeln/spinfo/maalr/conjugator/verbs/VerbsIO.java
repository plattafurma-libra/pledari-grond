package de.uni_koeln.spinfo.maalr.conjugator.verbs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class VerbsIO {

	public static final String input_dir = "data/input/";
	public static final String output_dir = "data/output/";

	// Non-instantiable
	private VerbsIO() {
		throw new AssertionError();
	}

	public static LineNumberReader getReader(String file) throws IOException {

		FileInputStream fis = new FileInputStream(input_dir + file);
		InputStreamReader isr = new InputStreamReader(fis, "UTF8");
		LineNumberReader reader = new LineNumberReader(isr);

		return reader;

	}

	public static <T> void printList(List<T> list, String fileName)
			throws IOException {

		FileOutputStream fos = new FileOutputStream(output_dir + fileName
				+ ".tab");
		Writer out = new OutputStreamWriter(fos, "UTF8");

		for (T value : list) {

			out.append(value + "\n");

		}

		out.close();

	}
	

}
