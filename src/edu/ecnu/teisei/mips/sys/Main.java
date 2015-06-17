package edu.ecnu.teisei.mips.sys;

/**
 * Created by Teisei on 2015/4/21.
 */
public class Main {
    /**
     * simulate a MIPS CPU to decode and run a program.
     * @param args args[0]: path of the bytecode; args[1]: if using pipeline.
     * Example: ***.class   ./sample.txt    pipeline
     */
    public static void main(String args[]) {
        args = new String[]{
                "./data/pro2/test/sample.txt",
                "pipeline"
        };
        String path = args[0];
        boolean isPipeline = false;
        if (args.length > 1) {
            if (args[1].equals("pipeline")) {
                isPipeline = true;
            }
        }
        CPU.simulate(path, isPipeline);
    }
}
