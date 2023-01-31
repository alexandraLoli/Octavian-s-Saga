import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TrialOfTheOracle extends Task{

    int cardUniverse;
    int nrOfSets;
    int nrOfChosenSets;
    int[][] sequences;
    boolean solved;
    ArrayList<Integer> nr = new ArrayList<>();
    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        formulateOracleQuestion();
        askOracle();
        decipherOracleAnswer();
        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {
       Scanner scanner = new Scanner(System.in);
       cardUniverse = scanner.nextInt();
       nrOfSets = scanner.nextInt();
       nrOfChosenSets = scanner.nextInt();

       sequences = new int[nrOfSets + 1][];
       for (int i = 1; i <= nrOfSets; i++) {
           int x = scanner.nextInt();
           sequences[i] = new int[x + 1];
           sequences[i][0] = x;
           for (int j = 1; j <= x; j++) {
               sequences[i][j] = scanner.nextInt();
           }
       }
    }

    @Override
    public void formulateOracleQuestion() throws IOException {
        FileWriter file = new FileWriter("sat.cnf");
        StringBuilder string = new StringBuilder();
        string.append("p cnf ")
                .append(nrOfSets * nrOfChosenSets)
                .append(" nrClauses\n");

        int nrClauses = 0;

        // At least one sequence is present in the reunion
        for(int j = 0; j < nrOfChosenSets; j++) {
            nrClauses++;
            for (int i = 1; i <= nrOfSets; i++) {
                string.append(i + nrOfSets * j)
                        .append(" ");
            }
            string.append("0\n");
        }

        // A sequence can't appear more than once
//        for (int i = 1; i <= nrOfSets; i++) {
//            nrClauses ++;
//            for (int j = 0; j < nrOfChosenSets; j++) {
//                string.append(-i - j * nrOfSets)
//                        .append(" ");
//            }
//            string.append("0\n");
//        }

        //
        for (int a = 0; a < nrOfChosenSets; a++) {
            for (int i = 1; i < nrOfSets; i++) {
                for (int j = i + 1; j <= nrOfSets; j++) {
                   string.append(- i - a * nrOfSets )
                            .append(" ")
                           .append(-j - a * nrOfSets)
                           .append(" 0\n");
                   nrClauses++;
                }
            }
        }

        //The sequences where an element from the "universe" appear
        for (int i = 1; i <= cardUniverse; i++) {
            nrClauses ++;
            for (int j = 1; j <= nrOfSets; j++) {
                for (int x = 1; x <= sequences[j][0]; x++) {
                    if (i == sequences[j][x]) {
                        for (int y = 0; y < nrOfChosenSets; y++) {
                            string.append(j + y * nrOfSets)
                                    .append(" ");
                        }
                        break;
                    }
                }
            }
            string.append("0\n");
        }

        int strIndex = string.indexOf("nrClauses");
        string.replace(strIndex, strIndex + new String("nrClauses").length(), "" + nrClauses);
        file.write(string.toString());
        file.close();
    }

    @Override
    public void decipherOracleAnswer() throws IOException {
        File oracleSolution = new File("sat.sol");
        Scanner scanner = new Scanner(oracleSolution);
        String bool = scanner.nextLine();

        if (bool.equals("False")) {
            solved = false;
        } else {
            solved = true;
        }
        if (!solved) {

        } else {
            if (scanner.nextInt() != nrOfChosenSets * nrOfSets) {
                solved = false;
            } else {
                nr.clear();
                for (int j = 0; j < nrOfChosenSets; j++) {
                    for (int i = 1; i <= nrOfSets; i++) {
                        if (scanner.nextInt() > 0) {
                            nr.add(i);
                        }
                    }
                }
                nr.sort(Integer::compare);
            }
        }
    }

    @Override
    public void writeAnswer() throws IOException {
        if (solved) {
            System.out.println("True");
            System.out.println(nrOfChosenSets);
            for (Integer i : nr) {
                System.out.print(i + " ");
            }
        } else {
            System.out.println("False");
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        TrialOfTheOracle trialOfTheOracle = new TrialOfTheOracle();
        trialOfTheOracle.solve();
    }
}
