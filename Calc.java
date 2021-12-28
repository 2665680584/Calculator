import java.io.*;      // System.in and System.out

/**
 * Name:           Chimingyang Huang
 * PID:            A16769768
 * USER:           cs12fa21bv
 * File name:      Calc.java
 * Description:     a top-of-the-line calculator which performs advanced
 * commands such as addition, subtractions, multiplication, division,
 * exponentiation, and factorial!
 */

/**
 * Class:            Calc
 * Description:      a top-of-the-line calculator which performs advanced
 * commands such as addition, subtractions, multiplication, division,
 * exponentiation, and factorial!
 */
public class Calc {
    // the calculator operation interface
    interface Operation {
        long operation(long op1, long op2);
    }

    // the calculator operations in priority order
    private static Operation[] functions = new Operation[]{
            null,
            null,
            new Operation() {
                public long operation(long op1, long op2) {
                    return add(op1, op2);
                }
            },
            new Operation() {
                public long operation(long op1, long op2) {
                    return sub(op1, op2);
                }
            },
            new Operation() {
                public long operation(long op1, long op2) {
                    return mult(op1, op2);
                }
            },
            new Operation() {
                public long operation(long op1, long op2) {
                    return divide(op1, op2);
                }
            },
            new Operation() {
                public long operation(long op1, long op2) {
                    return exponent(op1, op2);
                }
            },
            null,
            new Operation() {
                public long operation(long op1, long op2) {
                    return fact(op1, op2);
                }
            }
    };

    // maximum size of a calculator stack
    private static final int CALCSTACKSIZE = 100;

    // constants for EOF and true/false values
    private static final int
            EOF = -1,
            TRUE = 1,
            FALSE = 0,
            BYTE = 8;

    // array of operators, according to their priority
    private static final char[] operators = "()+-*/^ !".toCharArray();

    // sign bit for the operator when setupword is called
    private static final long SIGN_BIT = 1L << ((Long.BYTES << 3) - 1);

    /**
     * Getter method to find index of operator in the operators array
     *
     * @param word: the operator word to get the index of
     * @return long: index of operator in the operators array
     */
    private static int getIndex(long word) {
        return (int) (word & 0xFF00) >> BYTE;
    }

    /**
     * Getter method to extract the operator from a word
     *
     * @param word: the word to extract the operator from
     * @return char: the operator as a char
     */
    private static char getOperator(long word) {
        return (char) (word & 0xFF);
    }

    /**
     * Getter method that returns the priority of an operator
     *
     * @param word: the word that contains the operator
     * @return long: priority of the operator
     */
    private static long getPriority(long word) {
        return (word & 0xFE00);
    }

    /**
     * Checks if an item is an operator
     *
     * @param item: the item to check if it is an operator
     * @return boolean:
     * true: item is an operator
     * false: item is not an operator
     */
    private static boolean isOperator(long item) {
        return item < 0;
    }

    /**
     * Checks if an item is a number
     *
     * @param item: the item to check if it is a number
     * @return boolean:
     * true: item is a number
     * false: item is not a number
     */
    private static boolean isNumber(long item) {
        return item >= 0;
    }
    /**
     * The eval method utilizing 2 Stacks, evaluate mathematical expressions
     * from "postfix" notation
     * @param stack1 the a reference to a LongStack object containing
     *               "postfix" expressions to evaluate
     * @return the evaluated value
     */
    public static long eval(LongStack stack1) {
        LongStack stack2 = new
                LongStack(CALCSTACKSIZE, "stack2");//initialize stack2
        while (!stack1.isEmptyStack()) { //value from s1 to s2
            stack2.push(stack1.pop());
        }
        while (!stack2.isEmptyStack()) { //process calculation
            long i = stack2.pop();
            if (isNumber(i)) {
                stack1.push(i);
            } else if (isOperator(i)) {
                char ch = getOperator(i);
                if (ch == '!') {//factorial is unit operator
                    stack1.push(functions[getIndex(i)].
                            operation(stack1.pop(), 0));
                } else { //others operators are binary
                    stack1.push(functions[getIndex(i)].
                            operation(stack1.pop(), stack1.pop()));
                }
            }
        }
        stack2.jettisonStack();
        return stack1.pop();
    }

    /**
     * The intopost method Utilizing 2 Stacks, convert "infix" mathematical
     * expressions entered by the user into their "postfix" equivalents
     *
     * @param stack1 a reference to an empty LongStack object used to store
     *               "postfix" expressionss
     * @return int EOF when ^D is entered or
     * a non-zero value indicating that the function succeeded.
     */

    public static int intopost(LongStack stack1) {
        LongStack stack2 =
                new LongStack(CALCSTACKSIZE, "stack2"); //initialize stack2
        while (true) {
            int ch = MyLib.getchar();
            if (ch == EOF) { //parsing EOF
                stack2.jettisonStack();
                return EOF;
            } else if (ch == '\n') { //parsing <ret>
                while (!stack2.isEmptyStack()) {
                    stack1.push(stack2.pop());
                }
                stack2.jettisonStack();
                return 1;
            } else if (ch >= '0' && ch <= '9') { //parsing number
                MyLib.ungetc((char) ch);
                stack1.push(MyLib.decin());
            } else if (ch != ' ') { //parsing operators include parenthesis
                long word = setupword((char) ch);
                if (ch == '(') {
                    stack2.push(word);
                } else if (ch == ')') {
                    while (getOperator(stack2.top()) != '(') {
                        stack1.push(stack2.pop());
                    }
                    stack2.pop();
                } else {
                    while (!stack2.isEmptyStack() &&
                            getPriority(stack2.top()) >= getPriority(word)) {
                        stack1.push(stack2.pop());
                    }
                    stack2.push(word);
                }
            }

        }
    }

    /**
     * Add two operands together
     *
     * @param augend: the first operand
     * @param addend: the second operand
     * @return long: the result of the adding augend and addend
     */
    private static long add(long augend, long addend) {
        return augend + addend;
    }

    /**
     * Divide the divisor by the dividend
     *
     * @param divisor:  the long to be divided
     * @param dividend: the long that divides the divisor
     * @return long: the result of dividing the divisor by the dividend
     */
    private static long divide(long divisor, long dividend) {
        return dividend / divisor;
    }

    /**
     * exponent take power exponent of the base
     *
     * @param power the power of the exponential calculation
     * @param base  the base of the exponential calculation
     * @return long: the result of the exponential calculation
     */
    private static long exponent(long power, long base) {
        long result = 1;
        for (int i = 1; i <= power; i++) {
            result *= base;
        }
        return result;
    }

    /**
     * Divide take power exponent of the base
     *
     * @param num     the number to calculate factorial
     * @param ignored value to be ignored
     * @return long: the result of the factorial calculation
     */
    private static long fact(long num, long ignored) {
        if (num == 1 || num == 0) {
            return 1;
        }
        return num * fact(num - 1, 0);
    }

    /**
     * Multiply two numbers together
     *
     * @param multiplier:   the num by which the multiplicand is multiplied
     * @param multiplicand: the num to be multiplied
     * @return long: the result of the multiplication
     */
    private static long mult(long multiplier, long multiplicand) {
        return multiplier * multiplicand;
    }

    /**
     * Subtract the minuend from the subtrahend
     *
     * @param subtrahend: the number to subtract from
     * @param minuend:    the number to subtract from subtrahend
     * @return long: value of subtracting the minuend from subtrahend
     */
    private static long sub(long subtrahend, long minuend) {
        return minuend - subtrahend;
    }

    /**
     * The setupword method constructor funtion for longs representing
     * operators to be stored on your LongStack objects.
     *
     * @param character character to be setup
     * @return the set up word value of the character
     */
    private static long setupword(char character) {
        int index = 0;
        while (true) {
            if (character == operators[index]) {
                break;
            }
            index++;
        }
        return SIGN_BIT | (index << BYTE) | character;
    }
}
