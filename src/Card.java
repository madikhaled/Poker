public class Card {
    private  CardValue value;
    private  CardColor color;
    private char valueChar;
    private  char colorChar;



    public Card(CardValue value, CardColor color,char valueChar,char colorChar) {
        this.value = value;
        this.color = color;
        this.valueChar = valueChar;
        this.colorChar = colorChar;

    }

    public CardValue getValue() {
        return value;
    }

    public CardColor getColor() {
        return color;
    }
    @Override
    public String toString() {
        return value+" de "+ color.name();
    }

    public String toASCII() {
    return " _____ \n" +
           "|"+ valueChar+"    |\n" +
           "|     |\n" +
           "|    "+ colorChar +"|\n" +
           " ‾‾‾‾‾ ";
}


}