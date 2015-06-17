package edu.ecnu.teisei.mips.test;

import edu.ecnu.teisei.mips.instruction.cate2.Ins_MUL;
import edu.ecnu.teisei.mips.utils.MyFile;

import java.io.IOException;

/**
 * Created by Teisei on 2015/4/20.
 */
public class MainTest {
    public static void main(String args[]) {
        String ins_str1 = "11000011001000100010100000000000";
        Ins_MUL ins1 = new Ins_MUL(ins_str1, "MUL");
//        ins.execute();
        System.out.println(ins1.getAssemblyCode());

        MainTest mainTest = new MainTest();

        String path3 = "./data/pro2/test/disassembly.txt";
        String path4 = "./data/pro2/correct/disassembly.txt";
        if (mainTest.testCompareTwoResult(path3, path4)) {
            System.out.println("decode OK!");
        }

        String path1 = "./data/pro2/test/simulation.txt";
        String path2 = "./data/pro2/correct/simulation.txt";
        if (mainTest.testCompareTwoResult(path1, path2)) {
            System.out.println("simulation OK!");
        }

    }

    public boolean testCompareTwoResult(String path1, String path2) {
        try {

            MyFile f1 = new MyFile(path1, "utf-8");
            MyFile f2 = new MyFile(path2, "utf-8");
            String line1 = f1.readLine();
            String line2 = f2.readLine();
            int i = 0;
            while (line1 != null && line2 != null) {
                line1 = line1.trim();
                line2 = line2.trim();
                if (!line1.equals(line2)) {
                    System.out.println("************** " + i + " **************");
                    System.out.println(line1);
                    System.out.println(line2);
                    return false;
                }
                line1 = f1.readLine();
                line2 = f2.readLine();
                i++;
            }
            if (line1 != null || line2 != null) {
                return false;
            }
            f1.close();
            f2.close();
        } catch (IOException e) {

        }
        return true;
    }
}
