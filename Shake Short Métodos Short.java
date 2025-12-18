import java.util.Arrays;

// classe que agrupa os metodos de ordenacao pedidos nos exercicios
public class MetodosOrdenacao {

    // metodo principal para testar os algoritmos
    public static void main(String[] args) {
        int[] vetor = {31, 41, 59, 26, 41, 58};
        
        System.out.println("vetor original: " + Arrays.toString(vetor));
        
        // executa e mostra o resultado de cada metodo
        ordenacaoInsercaoDecrescente(vetor.clone());
        shakeSort(vetor.clone());
        selecaoInversa(vetor.clone());
    }

    // exercicio: reescrever o insertion sort para ordem decrescente
    public static void ordenacaoInsercaoDecrescente(int[] a) {
        // o laco comeca do segundo elemento (indice 1)
        for (int i = 1; i < a.length; i++) {
            int chave = a[i];
            int j = i - 1;
            
            // move elementos menores que a chave para a direita
            // mudando a comparacao para a[j] < chave, temos a ordem decrescente
            while (j >= 0 && a[j] < chave) {
                a[j + 1] = a[j];
                j = j - 1;
            }
            a[j + 1] = chave;
        }
        System.out.println("insercao decrescente: " + Arrays.toString(a));
    }

    // exercicio: implementacao do shake sort (bubble sort bidirecional)
    // percorre o vetor da esquerda para a direita e depois da direita para a esquerda
    public static void shakeSort(int[] a) {
        boolean trocou = true;
        int inicio = 0;
        int fim = a.length - 1;

        while (trocou) {
            trocou = false;

            // subida: leva o maior para o final
            for (int i = inicio; i < fim; i++) {
                if (a[i] > a[i + 1]) {
                    int temp = a[i];
                    a[i] = a[i + 1];
                    a[i + 1] = temp;
                    trocou = true;
                }
            }

            if (!trocou) break;

            trocou = false;
            fim--; // diminui o limite final pois o maior ja esta la

            // descida: traz o menor para o inicio
            for (int i = fim - 1; i >= inicio; i--) {
                if (a[i] > a[i + 1]) {
                    int temp = a[i];
                    a[i] = a[i + 1];
                    a[i + 1] = temp;
                    trocou = true;
                }
            }
            inicio++; // aumenta o limite inicial pois o menor ja esta la
        }
        System.out.println("shake sort: " + Arrays.toString(a));
    }

    // exercicio: selection sort que busca o maior para colocar no fim
    public static void selecaoInversa(int[] a) {
        int n = a.length;
        
        // percorre o vetor do fim para o inicio
        for (int i = n - 1; i > 0; i--) {
            int indiceMaior = 0;
            
            // encontra o maior elemento no subvetor nao ordenado
            for (int j = 1; j <= i; j++) {
                if (a[j] > a[indiceMaior]) {
                    indiceMaior = j;
                }
            }
            
            // troca o maior encontrado com a ultima posicao disponivel
            int temp = a[indiceMaior];
            a[indiceMaior] = a[i];
            a[i] = temp;
        }
        System.out.println("selecao inversa (maior no fim): " + Arrays.toString(a));
    }
}