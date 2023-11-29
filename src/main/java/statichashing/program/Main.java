package statichashing.program;

import cormack.example.Cormack;

public class Main {
    public static void main(String[] args) {
        int[] input = {249,415,118,791,609,904,862,391,631,889,332,166,650,208,116};
        Cormack cormack = new Cormack(7);

        for (int num: input) {
            System.out.print("===");
            System.out.print(num);
            System.out.println("===");
            cormack.add(num);
            cormack.printContent();
        }
    }

}