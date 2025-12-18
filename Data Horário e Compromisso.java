public class Data {
    private int dia, mes, ano;

    public Data(int d, int m, int a) {
        if (dataEValida(d, m, a)) {
            this.dia = d; this.mes = m; this.ano = a;
        }
    }

    private boolean dataEValida(int d, int m, int a) {
        if (m < 1 || m > 12) return false;
        int[] diasMes = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if ((a % 4 == 0 && a % 100 != 0) || (a % 400 == 0)) diasMes[2] = 29;
        return d >= 1 && d <= diasMes[m];
    }
}

public class Horario {
    private int hora, min;
    public Horario(int h, int m) {
        if (h >= 0 && h < 24 && m >= 0 && m < 60) {
            this.hora = h; this.min = m;
        }
    }
}