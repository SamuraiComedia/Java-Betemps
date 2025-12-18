public class Retangulo {
    private float largura = 1.0f;
    private float comprimento = 1.0f;

    public float getLargura() { return largura; }
    public void setLargura(float largura) {
        if (largura > 0.0 && largura < 20.0) this.largura = largura;
    }

    public float getComprimento() { return comprimento; }
    public void setComprimento(float comprimento) {
        if (comprimento > 0.0 && comprimento < 20.0) this.comprimento = comprimento;
    }

    public float calcularPerimetro() { return 2 * (largura + comprimento); }
    public float calcularArea() { return largura * comprimento; }
}