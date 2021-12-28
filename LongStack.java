/**
 * Name:           Chimingyang Huang
 * PID:            A16769768
 * USER:           cs12fa21bv
 * File name:      LongStack.java
 * Description:    implementation of an array-based stack of longs
 */

import java.io.PrintStream;

/**
 * Class:            LongStack
 * Description:      wrapper class of LongStackEngine
 * <p>
 * Public functions: wrapper
 */
public class LongStack {

    private static boolean debug; // debug option
    private static int stackCounter = 0; // number of stacks allocated so far

    /**
     * Class:            LongStackEngine
     * Description:       an array-based stack of longs
     * functions:
     * LongStackEngine (int stackSize, String caller)
     * void emptyStack ()
     * Integer getCapacity ()
     * Integer getOccupancy ()
     * boolean isEmptyStack ()
     * boolean isFullStack ()
     * Long pop ()
     * Long push (long item)
     * Long top ()
     */
    private class LongStackEngine {

        // catastrophic error messages
        static final String
                POP_EMPTY = "Popping from an empty stack!!!\n",
                PUSH_FULL = "Pushing to a full stack!!!\n",
                TOP_EMPTY = "Topping from an empty stack!!!\n",
                WRITE_NONEXIST_STREAM
                        = "Attempt to write using non-existent"
                        + " stream!!!\n";

        // Debug messags
        // HEX messages are used for negative values, used in hw4
        static final String
                ALLOCATE = "[Stack %d has been allocated]\n",
                JETTISON = "[Stack %d has been jettisoned]\n",
                HEXPOP = "[Stack %d - Popping 0x%x]\n",
                HEXPUSH = "[Stack %d - Pushing 0x%x]\n",
                HEXTOP = "[Stack %d - Topping 0x%x]\n",
                POP = "[Stack %d - Popping %d]\n",
                PUSH = "[Stack %d - Pushing %d]\n",
                TOP = "[Stack %d - Topping %d]\n",
                EMPTY = "[Stack %d - Emptied]\n";

        long[] stack;        // array to hold the data in stack order
        int stackPointer;    // index of the last occupied space
        int stackSize;        // size of the stack
        int stackID;        // which stack we are using
        Tracker tracker;    // to keep track of memory usage

        /**
         * The LongStackEngine is the constructor
         *
         * @param stackSize the size of the stack constructed
         * @param caller    the string caller of the stack constructed
         */
        LongStackEngine(int stackSize, String caller) {

            // allocate a new array to represent the stack
            stack = new long[stackSize];

            // hold the memory size of the LongStackEngine object
            long size = Size.of(stackPointer)
                    + Size.of(stack)
                    + Size.of(stackID)
                    + Size.of(stackSize)
                    + Size.of(tracker);
            tracker = new Tracker("LongStackEngine", size, caller); //construct corresponding tracker
            stackPointer = -1; //set initial stackPointer value
            stackID = ++stackCounter;//set initial stackID
            this.stackSize = stackSize;//set stackSize
            if (debug) {
                System.out.printf(ALLOCATE, stackID); //prints out allocation message
            }
        }

        /**
         * The jettisonStackEngine jettisons the stack by calling the jettison method in tracker
         *
         * @return None
         */
        void jettisonStackEngine() {
            if (debug)
                System.err.print(String.format(JETTISON,
                        stackID));
            tracker.jettison();
            stackCounter--;
        }

        /**
         * The emptyStack methods empty all elements in the stack
         *
         * @return None
         */
        void emptyStack() {
            while (!isEmptyStack()) {
                pop();
            }
            if (debug) {
                System.out.printf(EMPTY, stackID);
            }
        }

        /**
         * The getCapacity method returns how many elements can the LongStackEngine store
         *
         * @return Integer how many elements can the LongStackEngine store
         */
        Integer getCapacity() {
            return stackSize;
        }

        /**
         * The getOccupancy returns the number of elements in LongStackEngine
         *
         * @return Integer the number of elements in LongStackEngine
         */
        Integer getOccupancy() {
            return stackPointer + 1;
        }

        /**
         * The isEmptyStack returns true if LongStackEngine is empty, and false if it is not
         *
         * @return boolean true if LongStackEngine is empty, and false if it is not
         */
        boolean isEmptyStack() {
            return stackPointer == -1;
        }

        /**
         * The isFullStack returns true if LongStackEngine is full, and false if it is not
         *
         * @return boolean true if LongStackEngine is full, and false if it is not
         */
        boolean isFullStack() {
            return stackPointer == stackSize - 1;
        }

        /**
         * The pop removes an item from the top of the LongStackEngine,
         * and sends it back by returning this element. If pop failed, return null.
         *
         * @return Long element that is popped, null if pop failed
         */
        Long pop() {
            if (!isEmptyStack()) {
                Long l = stack[stackPointer--];
                if (debug) {
                    System.out.printf(HEXPOP, stackID, l);
                }
                return l;
            }
            System.err.print(POP_EMPTY);
            return null;
        }

        /**
         * The push method adds item to the top of the LongStackEngine. If push failed, return false
         *
         * @param item the long number that is being pushed to the stack
         * @return boolean true if pushed successfully, false if push failed
         */
        boolean push(long item) {
            if (!isFullStack()) {
                stack[++stackPointer] = item;
                if (debug) {
                    System.out.printf(HEXPUSH, stackID, item);
                }
                return true;
            }
            System.err.println(PUSH_FULL);
            return false;
        }

        /**
         * The top method sends back the item on the top of the LongStackEngine through returning this element,
         * If top failed, return null.
         *
         * @return Long true if topped successfully, false if top failed
         */
        Long top() {
            if (!isEmptyStack()) {
                Long l = stack[stackPointer];
                if (debug) {
                    System.err.printf(HEXTOP, stackID, l);
                }
                return l;
            }
            System.err.println(TOP_EMPTY);
            return null;
        }

        void writeStack(PrintStream stream) {

            int index = 0;    // index into the stack

            if (stream == null) {
                System.err.print(WRITE_NONEXIST_STREAM);
                return;
            }

            int stackOccupancy = getOccupancy();

            if (stream.equals(System.err)) {
                stream.print(
                        "\nStack " + stackID + ":\n"
                                + "Stack's capacity is " + stackSize + ".\n"
                                + "Stack has "
                                + stackOccupancy + " item(s) in it.\n"
                                + "Stack can store "
                                + (stackSize - stackOccupancy)
                                + " more item(s).\n");
                Tracker.checkMemoryLeaks();
            }

            for (index = 0; index < stackOccupancy; index++) {
                if (stream.equals(System.err))
                    stream.print(String.format(
                            "Value on stack is |0x%x|\n",
                            stack[index]));
                else {
                    if (stack[index] < 0)
                        stream.print(String.format(
                                "%c ",
                                (byte) stack[index]));
                    else
                        stream.print(String.format(
                                "%d ", stack[index]));
                }
            }
        }
    }

    // -------------------- DO NOT EDIT BELOW THIS LINE --------------------
    // PROVIDED INFRASTRUCTURE BELOW, YOUR CODE SHOULD GO ABOVE
    // CHANGING THE CODE BELOW WILL RESULT IN POINT DEDUCTIONS

    // catastrophic error messages
    private static final String
            CAPACITY_NONEXIST = "Capacity check from a non-existent stack!!!\n",
            EMPTY_NONEXIST = "Emptying a non-existent stack!!!\n",
            ISEMPTY_NONEXIST = "Isempty check from a non-existent stack!!!\n",
            ISFULL_NONEXIST = "Isfull check from a non-existent stack!!!\n",
            OCCUPANCY_NONEXIST = "Occupancy check from a non-existent stack!!!\n",
            POP_NONEXIST = "Popping from a non-existent stack!!!\n",
            PUSH_NONEXIST = "Pushing to a non-existent stack!!!\n",
            TOP_NONEXIST = "Topping from a non-existent stack!!!\n",
            WRITE_NONEXIST_STACK = "Writing to a non-existent stack!!!\n";

    private LongStackEngine stackEngine; // the object that holds the data

    private boolean exists() {
        return !(stackEngine == null);
    }

    public LongStack(int stackSize, String caller) {
        stackEngine = new LongStackEngine(stackSize, caller);
    }

    // Debug state methods
    public static void debugOn() {
        debug = true;
    }

    public static void debugOff() {
        debug = false;
    }

    public boolean jettisonStack() {

        // ensure stack exists
        if (!exists())
            return false;

        stackEngine.jettisonStackEngine();
        stackEngine = null;
        return true;
    }

    public void emptyStack() {

        // ensure stack exists
        if (!exists()) {
            System.err.print(EMPTY_NONEXIST);
            return;
        }

        stackEngine.emptyStack();
    }

    public Integer getCapacity() {

        // ensure stack exists
        if (!exists()) {
            System.err.print(CAPACITY_NONEXIST);
            return null;
        }

        return stackEngine.getCapacity();
    }

    public Integer getOccupancy() {

        // ensure stack exists
        if (!exists()) {
            System.err.print(OCCUPANCY_NONEXIST);
            return null;
        }

        return stackEngine.getOccupancy();
    }

    public boolean isEmptyStack() {

        // ensure stack exists
        if (!exists()) {
            System.err.print(ISEMPTY_NONEXIST);
            return false;
        }

        return stackEngine.isEmptyStack();
    }

    public boolean isFullStack() {

        // ensure stack exists
        if (!exists()) {
            System.err.print(ISFULL_NONEXIST);
            return false;
        }

        return stackEngine.isFullStack();
    }

    public Long pop() {

        // ensure stack exists
        if (!exists()) {
            System.err.print(POP_NONEXIST);
            return null;
        }

        return stackEngine.pop();
    }

    public boolean push(long item) {

        // ensure stack exists
        if (!exists()) {
            System.err.print(PUSH_NONEXIST);
            return false;
        }

        return stackEngine.push(item);
    }

    public Long top() {

        // ensure stack exists
        if (!exists()) {
            System.err.print(TOP_NONEXIST);
            return null;
        }
        return stackEngine.top();
    }

    public void writeStack(PrintStream stream) {

        // ensure stack exists
        if (!exists()) {
            System.err.print(WRITE_NONEXIST_STACK);
            return;
        }

        stackEngine.writeStack(stream);
    }
}
