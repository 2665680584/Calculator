import java.io.*;      // System.in and System.out

public class Main {
	// Calculator messages   
	private static final String 
		INFIX_EXPRESSION = "\nPlease enter an expression to calculate:  ",
		POSTFIX_EXPRESSION = "\nThe expression in postfix order is:  ",
		RESULT = "\n\twhich evaluates to:  ";

	// Calculator constants
	private static final int
		CALCSTACKSIZE = 100,
		EOF = -1,
		FALSE = 0;

	public static void main (String [] args) throws EOFException {
		long intopostStatus;        // return value from intopost
		LongStack mainStack;        // stack to evaluate equations

		// initialize debug states
		LongStack.debugOff ();
		// check for stack debug options
		for (int index = 0; index < args.length; ++index) {
			if (args[index].equals ("-x"))
				LongStack.debugOn ();
		}

		mainStack = new LongStack (CALCSTACKSIZE, "main"); // for the postfix expression

		while (true) {
			MyLib.writeline (INFIX_EXPRESSION, System.out);
			if ((intopostStatus = Calc.intopost (mainStack)) == EOF)
				break;

			if (intopostStatus == FALSE) {
				mainStack.emptyStack ();
				continue;
			}

			MyLib.writeline (POSTFIX_EXPRESSION, System.out);
			mainStack.writeStack (System.out);

			MyLib.writeline (RESULT, System.out);
			MyLib.decout (Calc.eval (mainStack));
			MyLib.newline ();
		}

		mainStack.jettisonStack ();
		Tracker.checkMemoryLeaks ();
		MyLib.newline ();
	}
}
