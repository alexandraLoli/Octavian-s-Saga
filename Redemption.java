import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Redemption {
    int nrCardsOwned;
    int nrCardsWanted;
    int nrPacks;
    ArrayList<String> ownedCards;
    ArrayList<ArrayList<String>> packs;
    HashMap<String, ArrayList<Integer>> wanted;
    ArrayList<Integer> packsWanted;
    int totalPacks;

    ArrayList<String> c = new ArrayList<>();

    Redemption() {
        ownedCards = new ArrayList<>();
        packs = new ArrayList<>();
        wanted = new HashMap<>();
        packsWanted = new ArrayList<>();
        totalPacks = 0;
    }

    private void readData() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        nrCardsOwned = scanner.nextInt();
        nrCardsWanted = scanner.nextInt();
        nrPacks = scanner.nextInt();

        scanner.nextLine();

        for (int i = 0; i < nrCardsOwned; i++) {
            ownedCards.add(scanner.nextLine());
        }

        for (int i = 0; i < nrCardsWanted; i++) {
            String card = scanner.nextLine();
            int ok = 1;
            for (String c : ownedCards) {
                if (c.equals(card)) {
                    ok = 0;
                    break;
                }
            }
            if (ok == 1) {
                wanted.put(card, null);
            }
        }

        for (int i = 0; i < nrPacks; i++) {
            int nr = scanner.nextInt();
            scanner.nextLine();
            packs.add(new ArrayList<>());
            for (int j = 0; j < nr; j++) {
                String card = scanner.nextLine();
                if (wanted.containsKey(card))
                    packs.get(i).add(card);
            }
        }
    }

    private void populateWantedHashMap() {
        for (Map.Entry<String, ArrayList<Integer>> card : wanted.entrySet()) {
            ArrayList<Integer> pack = new ArrayList<>();
            for (int i = 0; i < packs.size(); i++) {
                if (packs.get(i).contains(card.getKey())) {
                    pack.add(i);
                }
            }
            card.setValue(pack);
        }
    }

    private void compare() {
        Comparator<ArrayList<Integer>> comparator = new Comparator<ArrayList<Integer>>() {
            @Override
            public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
                Integer unu = o1.size();
                Integer doi = o2.size();
                return unu.compareTo(doi);
            }
        };
        LinkedHashMap<String, ArrayList<Integer>> listLinkedHashMap = wanted.entrySet().stream()
                .sorted(Map.Entry.<String, ArrayList<Integer>>comparingByValue(comparator))
                .collect((Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)));
        findPacks(listLinkedHashMap);
    }

    private void findPacks(LinkedHashMap<String, ArrayList<Integer>> wantedCards) {
        Iterator<Map.Entry<String, ArrayList<Integer>>> iterator = wantedCards.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<Integer>> card = iterator.next();

                if (card.getValue() != null) {
                    int firstPack = card.getValue().get(0);
                    packsWanted.add(firstPack + 1);
                    totalPacks++;
                    ArrayList<String> cardsInFirstPack = packs.get(firstPack);
                    for (String i : cardsInFirstPack) {
                        if (wantedCards.containsKey(i)) {
                            if (!i.equals(card.getKey())) {
                                wantedCards.put(i, null);
                            }
                        }
                    }
                    card.setValue(null);
                }
        }
    }

    private void writeAnswer() {
        System.out.println(totalPacks);
        packsWanted.sort(Integer::compare);
        for (Integer i : packsWanted) {
            System.out.print(i + " ");
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Redemption redemption = new Redemption();
        redemption.readData();
        redemption.populateWantedHashMap();
        redemption.compare();
        redemption.writeAnswer();
    }
}

