package deck;

public class Deck {
    private static int n;
    private static String[] cards = new String[n];

    public void shuffle(){
        for (int i = 0; i < n; i++) {
            int r = i + (int) (Math.random() * (n-i));
            String temp = cards[r];
            cards[r] = cards[i];
            cards[i] = temp;
        }
    }

    public static void Main(String[] args){
        for (int i = 0; i < n; i++) {
            System.out.println(cards[i]);
        }
    }
}
