package guimain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {

	private static final String POSITIVEDOUBLEREGEX = "\\d+\\.?\\d*";
	private static final String COLORREGEX = "\\(\\s*" + 
											POSITIVEDOUBLEREGEX + "\\s*,\\s*" +
											POSITIVEDOUBLEREGEX + "\\s*,\\s*" +
											POSITIVEDOUBLEREGEX + "\\s*\\)";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "(  3.14159  ,  1.2342 ,23  )";
		
		if (str.matches(COLORREGEX)) {
			Pattern p = Pattern.compile(POSITIVEDOUBLEREGEX);
			Matcher m = p.matcher(str);
			double r = 0, g = 0, b = 0;
			m.find();
				r = Double.valueOf(str.substring(m.start(), m.end()));
			m.find();
				g = Double.valueOf(str.substring(m.start(), m.end()));
			m.find();
				b = Double.valueOf(str.substring(m.start(), m.end()));
			
			System.out.println("r,g,b: " + r + " " + g + " " + b);
		}
		else
			System.out.println("not match.");
	}
}
