public enum HandStrength {
    QUINTE_FLUSH_ROYALE(9),
    QUINTE_FLUSH(8),
    CARRE(7),
    FULL(6),
    COULEUR(5),
    SUITE(4),
    BRELAN(3),
    DOUBLE_PAIRE(2),
    PAIRE(1),
    CARTE_HAUTE(0);

    private  int strengthValue;

    HandStrength(int strengthValue) {
        this.strengthValue = strengthValue;
    }

    public int getStrengthValue() {
        return strengthValue;
    }
}