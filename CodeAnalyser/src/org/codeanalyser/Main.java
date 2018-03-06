package org.codeanalyser;

/**
 * This whole project is meant to be an addon to CloudCoder
 * as a part of the bachelor thesis. It provides code analysis scaffolder
 * and can do the code coverage measurement.
 * 
 * @author Jan Kovar
 *
 */
public class Main {
	
	public static void main(String[] args) throws Exception {
		final String string = "	public class Logic2 {\n"
				 + "		private boolean addExecuted;\n"
				 + "		\n"
				 + "		public Logic2() {\n"
				 + "			addExecuted = false;\n"
				 + "		}\n"
				 + "		\n"
				 + "		boolean neco = true;\n"
				 + "		public int add(int a, int b) {\n"
				 + "			return a+b;\n"
				 + "		}\n"
				 + "		\n"
				 + "		public int divide(int a, int b) {\n"
				 + "			return a/b;\n"
				 + "		}\n"
				 + "		\n"
				 + "		public int multiply(int a, int b) {\n"
				 + "			return a*b;\n"
				 + "		}\n"
				 + "		\n"
				 + "		public int doTheMagic(int a, int b) {\n"
				 + "			if (a == 0) {\n"
				 + "				System.out.println(\"hello there\");\n"
				 + "				\n"
				 + "				while (true) {\n"
				 + "					a += 1;\n"
				 + "					if (a == 5) {\n"
				 + "						break;\n"
				 + "					}\n"
				 + "				}\n"
				 + "				\n"
				 + "				return add(a,b);\n"
				 + "			}\n"
				 + "			else if (b == 0) {\n"
				 + "				System.out.println(\"bubalooo\");\n"
				 + "				\n"
				 + "				if (a == 0) {\n"
				 + "					System.out.println(\"empty row?\");\n"
				 + "				}\n"
				 + "				\n"
				 + "				b = 1;\n"
				 + "				\n"
				 + "				return divide(a,b);\n"
				 + "			}\n"
				 + "			\n"
				 + "			a = 5;\n"
				 + "			b = 5;\n"
				 + "			\n"
				 + "			return multiply(a,b);\n"
				 + "		}\n"
				 + "		\n"
				 + "		public boolean getExecuted(){\n"
				 + "			return this.addExecuted;\n"
				 + "		}\n"
				 + "	}";
		
		CodeAnalyser a = new CodeAnalyser(string, "ANALYSER");
		
		a.analyse();
		
		String s = a.editCode();
		
		System.out.println(s);
	}
}
