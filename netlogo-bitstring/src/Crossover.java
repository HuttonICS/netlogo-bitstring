import java.util.LinkedList;
import java.util.List;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.LogoList;
import org.nlogo.api.Syntax;


/**
 * <!-- Crossover -->
 * 
 * @author Gary Polhill
 */
public class Crossover extends DefaultReporter {

	@Override
	public Syntax getSyntax() {
		return Syntax.reporterSyntax(new int[] { Syntax.WildcardType(), Syntax.WildcardType(), Syntax.NumberType(), },
																	Syntax.ListType());
	}

	@Override
	public Object report(Argument[] args, Context context) throws ExtensionException, LogoException {
		NetLogoBitstring bs[] = BitstringExtension.getNetLogoBitstringArgs(args, 0, 1);
		int bit = args[2].getIntValue();

		if(bs[0].size() != bs[1].size()) {
			throw new ExtensionException("Cannot crossover bitstrings of different sizes (" + bs[0].size() + " and "
					+ bs[1].size() + ")");
		}
		
		if(bit < 0 || bit > bs[0].size()) {
			throw new ExtensionException("Crossover point outside the range [0, " + bs[0].size() + "]");
		}
		
		List<NetLogoBitstring> nlxover = new LinkedList<NetLogoBitstring>();

		if(bit == 0) {
			nlxover.add(new NetLogoBitstring(bs[0]));
			nlxover.add(new NetLogoBitstring(bs[1]));
		}
		else if(bit == bs[0].size()) {
			nlxover.add(new NetLogoBitstring(bs[1]));
			nlxover.add(new NetLogoBitstring(bs[0]));		
		}
		else {
			Bitstring xover[] = bs[0].crossover(bs[1], bit);

			nlxover.add(new NetLogoBitstring(xover[0]));
			nlxover.add(new NetLogoBitstring(xover[1]));
		}
		return LogoList.fromJava(nlxover);
	}

	@Override
	public String getAgentClassString() {
		return "OTPL";
	}

}
