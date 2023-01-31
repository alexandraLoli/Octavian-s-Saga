import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RiseOfTheFalseProphet extends Task {
    ArrayList<ArrayList<String>> cardsInPack;
    ArrayList<String> wantedCards;
    ArrayList<String> ownedCards;
    int numberOfPacks;
    int numberCardsWanted;
    int numberCardsOwned;
    boolean solved;
    int K;
    ArrayList<Integer> nr = new ArrayList<>();
    int posibility;

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        int minPacks = 0;
        do {
            minPacks++;
            posibility = minPacks;
            formulateOracleQuestion();
            askOracle();
            decipherOracleAnswer();
        } while (!solved);
        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {
        Scanner scanner = new Scanner(System.in);
        numberCardsOwned = scanner.nextInt();
        numberCardsWanted = scanner.nextInt();
        numberOfPacks = scanner.nextInt();

        ownedCards = new ArrayList<>();
        wantedCards = new ArrayList<>();
        cardsInPack = new ArrayList<>();

        scanner.nextLine();
        for (int i = 0; i < numberCardsOwned; i++) {
            ownedCards.add(scanner.nextLine());
        }
        int decrease = 0;
        for (int i = 0; i < numberCardsWanted; i++) {
            String card = scanner.nextLine();
            int ok = 1;
            for (String c : ownedCards) {
                if (c.equals(card)) {
                    ok = 0;
                    decrease ++;
                    break;
                }
            }
            if (ok == 1) {
                wantedCards.add(card);
            }
        }
        numberCardsWanted = numberCardsWanted - decrease;
        for (int i = 0; i < numberOfPacks; i++) {
            int nr = scanner.nextInt();
            scanner.nextLine();
            cardsInPack.add(new ArrayList<>());
            for (int j = 0; j < nr; j++) {
                cardsInPack.get(i).add(scanner.nextLine());
            }
        }
    }

    @Override
    public void formulateOracleQuestion() throws IOException {
        FileWriter fileWriter = new FileWriter("sat.cnf");
        StringBuilder string = new StringBuilder();
        string.append("p cnf ")
                .append(posibility * numberOfPacks)
                .append(" nrClauses\n");
        int nrClauses = 0;

        //At least one sequence is present in the reunion
        for (int j = 0; j < posibility; j++) {
            nrClauses++;
            for (int i = 1; i <= numberOfPacks; i++) {
                string.append(i + j * numberOfPacks)
                        .append(" ");
            }
            string.append("0\n");
        }

        for (int a = 0; a < posibility; a++) {
            for (int i = 1; i < numberOfPacks; i++) {
                for (int j = i + 1; j <= numberOfPacks; j++) {
                    string.append(-i - a * numberOfPacks)
                    .append(" ")
                            .append(-j - a * numberOfPacks)
                            .append(" 0\n");
                    nrClauses++;
                }
            }
        }

        //The sequences where an element from the wanted Cards appear
        for (int i = 0; i < numberCardsWanted; i++) {
            nrClauses++;
            for (int j = 0; j < numberOfPacks; j++) {
                for (int x = 0; x < cardsInPack.get(j).size(); x++) {
                    if (wantedCards.get(i).equals(cardsInPack.get(j).get(x))) {
                        for (int y = 0; y < posibility; y++) {
                            string.append((j + 1) + y * numberOfPacks)
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
        fileWriter.write(string.toString());
        fileWriter.close();
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
            if (scanner.nextInt() != posibility * numberOfPacks) {
                solved = false;
            } else {
                nr.clear();
                for (int j = 0; j < posibility; j++) {
                    for (int i = 1; i <= numberOfPacks; i++) {
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
        if(solved) {
            System.out.println(posibility);
            for (Integer i : nr) {
                System.out.print(i + " ");
            }
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        RiseOfTheFalseProphet riseOfTheFalseProphet = new RiseOfTheFalseProphet();
        riseOfTheFalseProphet.solve();
    }
}
