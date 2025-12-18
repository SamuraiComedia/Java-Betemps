import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.util.function.Predicate;

// ====================================================================
// CLASSE 1: ItemMusical (Registro de Dados)
// ====================================================================
class ItemMusical {
    private int identificador;
    private String compositor;
    private String tituloPeca;
    private String textoLirico;

    // Construtor
    public ItemMusical(int identificador, String compositor, String tituloPeca, String textoLirico) {
        this.identificador = identificador;
        this.compositor = compositor;
        this.tituloPeca = tituloPeca;
        this.textoLirico = textoLirico;
    }

    // Método principal de comparação (baseado no 'identificador', a chave da B-Tree)
    public int comparar(ItemMusical item) {
        return Integer.compare(this.identificador, item.identificador);
    }
    
    // Getters para os atributos
    public String getTextoLirico() {
        return this.textoLirico;
    }
    public String getCompositor() {
        return this.compositor;
    }
    public String getTituloPeca() {
        return this.tituloPeca;
    }
    public int getIdentificador() {
        return this.identificador;
    }

    // Representação em String
    @Override
    public String toString() {
        return "ID: " + this.identificador + ", Compositor: " + this.compositor +
               ", Título: " + this.tituloPeca + ", Letra: " + this.textoLirico;
    }

    // Outros métodos (mantidos)
    public void alteraIdentificador(int identificador) {
        this.identificador = identificador;
    }
    public void gravaArq(RandomAccessFile arq) throws IOException {
        arq.writeInt(this.identificador);
        arq.writeUTF(this.compositor);
        arq.writeUTF(this.tituloPeca);
        arq.writeUTF(this.textoLirico);
    }
    public void leArq(RandomAccessFile arq) throws IOException {
        this.identificador = arq.readInt();
        this.compositor = arq.readUTF();
        this.tituloPeca = arq.readUTF();
        this.textoLirico = arq.readUTF();
    }
}

// ====================================================================
// CLASSE 2: CatalogoB (Estrutura de Dados - Árvore B)
// ====================================================================
class CatalogoB {
    
    // Classe interna aninhada para representar a página/nó da Árvore B
    private static class NoArvore {
        int contagem; 
        ItemMusical pecas[]; 
        NoArvore ligacoes[]; 

        public NoArvore(int maxOrdem) {
            this.contagem = 0;
            this.pecas = new ItemMusical[maxOrdem];
            this.ligacoes = new NoArvore[maxOrdem + 1];
        }
    }

    private NoArvore topo; 
    private int minOrdem; 
    private int maxOrdem; 

    // Construtor
    public CatalogoB(int minOrdem) {
        this.topo = null;
        this.minOrdem = minOrdem;
        this.maxOrdem = 2 * minOrdem;
    }

    // -------------------------------------------------------------------
    // MÉTODOS PÚBLICOS DE INTERFACE
    // -------------------------------------------------------------------
    public ItemMusical buscarItem(ItemMusical registro) {
        return this.buscarItem(registro, this.topo);
    }

    public void adicionar(ItemMusical registro) {
        ItemMusical regRetorno[] = new ItemMusical[1];
        boolean cresceu[] = new boolean[1];
        NoArvore noRetorno = this.adicionar(registro, this.topo, regRetorno, cresceu);
        
        if (cresceu[0]) {
            NoArvore noTemp = new NoArvore(this.maxOrdem);
            noTemp.pecas[0] = regRetorno[0];
            noTemp.ligacoes[0] = this.topo;
            noTemp.ligacoes[1] = noRetorno;
            this.topo = noTemp;
            this.topo.contagem++;
        } else {
            this.topo = noRetorno;
        }
    }

    public void eliminar(ItemMusical registro) {
        boolean diminuiu[] = new boolean[1];
        this.topo = this.eliminar(registro, this.topo, diminuiu);
        if (diminuiu[0] && (this.topo != null) && (this.topo.contagem == 0)) {
            this.topo = this.topo.ligacoes[0];
        } else if (this.topo == null) {
            System.out.println("Catálogo vazio após eliminação.");
        }
    }

    public void exibirEstrutura() {
        System.out.println("--- ESTRUTURA DA ÁRVORE B (CATÁLOGO) ---");
        this.exibirEstrutura(this.topo, 0);
        System.out.println("----------------------------------------");
    }
    
    // Métodos públicos de busca e remoção customizada
    public ItemMusical[] localizarPorTrechoLirico(String trecho) {
        return localizarPorTrechoLirico(trecho, this.topo, new ArrayList<>());
    }
    public ItemMusical[] localizarPorCompositor(String compositor) {
        return localizarPorCompositor(compositor, this.topo, new ArrayList<>());
    }
    public ItemMusical[] localizarPorTituloPeca(String titulo) {
        return localizarPorTituloPeca(titulo, this.topo, new ArrayList<>());
    }
    public void eliminarPorTrechoLirico(String trecho) {
        this.removerTodasOcorrencias(item -> item.getTextoLirico().toLowerCase().contains(trecho.toLowerCase()));
    }
    public void eliminarPorCompositor(String compositor) {
        this.removerTodasOcorrencias(item -> item.getCompositor().equalsIgnoreCase(compositor));
    }
    public void eliminarPorTituloPeca(String titulo) {
        this.removerTodasOcorrencias(item -> item.getTituloPeca().equalsIgnoreCase(titulo));
    }


    // =================================================================
    // FUNÇÃO DE IMPORTAÇÃO CSV REFORÇADA (CORRIGIDA PARA FORMATO DE ARQUIVO)
    // =================================================================
    public static void importarAcervoDoCSV(CatalogoB catalogo, String caminhoArquivoCSV) throws IOException {
        System.out.println("Importando dados do arquivo: " + caminhoArquivoCSV);
        
        int linhasProcessadas = 0;
        int linhasIgnoradas = 0;
        
        try (BufferedReader fileReader = new BufferedReader(new FileReader(caminhoArquivoCSV))) {
            
            String linha;
            while ((linha = fileReader.readLine()) != null) {
                
                if (linha.trim().isEmpty()) {
                    linhasIgnoradas++;
                    continue;
                }
                
                String limpa = linha.replace("\"", "");
                String[] dados = limpa.trim().split(",");

                if (dados.length < 4) { 
                     linhasIgnoradas++;
                     continue;
                } 
                
                try {
                    // Assumindo a ordem: 0:Compositor, 1:Identificador, 2:Título, 3:Texto Lírico
                    String compositor = dados[0].trim();
                    String idStr = dados[1].trim();
                    String tituloPeca = dados[2].trim();
                    String textoLirico = dados[3].trim();
                    
                    // Filtra linhas que parecem lixo/cabeçalho (se o Compositor for um número ou vazio)
                    if (compositor.matches("^[0-9]+$") || compositor.isEmpty()) {
                        linhasIgnoradas++;
                        continue;
                    }
                    
                    int identificador = Integer.parseInt(idStr);
                    
                    ItemMusical item = new ItemMusical(identificador, compositor, tituloPeca, textoLirico);
                    catalogo.adicionar(item);
                    linhasProcessadas++;
                    
                } catch (NumberFormatException e) {
                     linhasIgnoradas++; 
                } catch (Exception e) {
                    linhasIgnoradas++; 
                }
            }

            System.out.println("--- Resumo da Importação ---");
            System.out.println("Linhas processadas e adicionadas: " + linhasProcessadas);
            System.out.println("Linhas ignoradas (cabeçalho, formato incorreto ou ID duplicado): " + linhasIgnoradas);
            System.out.println("Acervo importado com sucesso!");

        } catch (FileNotFoundException e) {
            System.out.println("Erro: Arquivo não encontrado no caminho especificado: " + caminhoArquivoCSV);
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        }
    }

    public static void exibirBusca(ItemMusical[] resultado) {
        if (resultado == null || resultado.length == 0) {
            System.out.println("Nenhum item musical encontrado.");
            return;
        }

        for (ItemMusical item : resultado) {
            System.out.println(item);
        }
    }


    // -------------------------------------------------------------------
    // MÉTODOS PRIVADOS DE IMPLEMENTAÇÃO (Lógica da Árvore B)
    // -------------------------------------------------------------------

    private ItemMusical buscarItem(ItemMusical registro, NoArvore noAtual) {
        if (noAtual == null)
            return null; 
        else {
            int i = 0;
            while (i < noAtual.contagem) {
                ItemMusical itemExistente = noAtual.pecas[i];
                // *** VERIFICAÇÃO DE SEGURANÇA (Null Check) ***
                if (itemExistente == null) break; 

                if (registro.comparar(itemExistente) <= 0) break; 
                i++;
            }
            
            if (i < noAtual.contagem && noAtual.pecas[i] != null && registro.comparar(noAtual.pecas[i]) == 0)
                return noAtual.pecas[i]; 
            else
                return buscarItem(registro, noAtual.ligacoes[i]);
        }
    }

    private void inserirNoNo(NoArvore noAtual, ItemMusical registro, NoArvore noDireita) {
        int k = noAtual.contagem - 1;
        
        // CORREÇÃO: Limita o índice k para prevenir ArrayIndexOutOfBoundsException
        while (k >= 0) {
            ItemMusical itemExistente = noAtual.pecas[k];
            // Verifica NULL (segurança)
            if (itemExistente == null) break; 

            if (registro.comparar(itemExistente) >= 0) {
                break;
            }
            
            // O shift só deve ocorrer se a posição de destino (k+1) for um índice válido
            if (k + 1 < this.maxOrdem) {
                noAtual.pecas[k + 1] = noAtual.pecas[k];
                noAtual.ligacoes[k + 2] = noAtual.ligacoes[k + 1];
            }
            k--;
        }
        
        // Garante que a inserção final respeite o limite do array
        if (k + 1 < this.maxOrdem) {
            noAtual.pecas[k + 1] = registro;
            noAtual.ligacoes[k + 2] = noDireita;
            noAtual.contagem++;
        }
    }

    private NoArvore adicionar(ItemMusical registro, NoArvore noAtual, ItemMusical[] regRetorno,
          boolean[] cresceu) {
        NoArvore noRetorno = null;
        if (noAtual == null) {
            cresceu[0] = true;
            regRetorno[0] = registro;
        } else {
            int i = 0;
            while (i < noAtual.contagem) {
                ItemMusical itemExistente = noAtual.pecas[i];
                // Verifica NULL (segurança)
                if (itemExistente == null) break; 
                
                if (registro.comparar(itemExistente) > 0) {
                    i++;
                } else {
                    break;
                }
            }
            
            if (i < noAtual.contagem && noAtual.pecas[i] != null && registro.comparar(noAtual.pecas[i]) == 0) {
                System.out.println("Erro: Item já registrado. ID: " + registro.getIdentificador());
                cresceu[0] = false;
            } else {
                  
                noRetorno = adicionar(registro, noAtual.ligacoes[i], regRetorno, cresceu);
                
                if (cresceu[0])
                    if (noAtual.contagem < this.maxOrdem) { 
                        this.inserirNoNo(noAtual, regRetorno[0], noRetorno);
                        cresceu[0] = false;
                        noRetorno = noAtual;
                    } else { // Overflow: Divisão do Nó
                        NoArvore noTemp = new NoArvore(this.maxOrdem);
                        noTemp.ligacoes[0] = null;
                        
                        if (i <= this.minOrdem) {
                            this.inserirNoNo(noTemp, noAtual.pecas[this.maxOrdem - 1], noAtual.ligacoes[this.maxOrdem]);
                            noAtual.contagem--;
                            this.inserirNoNo(noAtual, regRetorno[0], noRetorno);
                        } else {
                            this.inserirNoNo(noTemp, regRetorno[0], noRetorno);
                        }
                        
                        for (int j = this.minOrdem + 1; j < this.maxOrdem; j++) {
                            if (noAtual.pecas[j] != null) {
                                this.inserirNoNo(noTemp, noAtual.pecas[j], noAtual.ligacoes[j + 1]);
                            }
                            noAtual.ligacoes[j + 1] = null; 
                        }
                        
                        noAtual.contagem = this.minOrdem;
                        noTemp.ligacoes[0] = noAtual.ligacoes[this.minOrdem + 1];
                        regRetorno[0] = noAtual.pecas[this.minOrdem];
                        noRetorno = noTemp;
                    }
            }
        }
        return (cresceu[0] ? noRetorno : noAtual);
    }
    
    // =================================================================
    // MÉTODOS PRIVADOS AUXILIARES (DEFINIÇÃO ÚNICA)
    // =================================================================

    private void coletarItens(NoArvore noAtual, List<ItemMusical> lista, Predicate<ItemMusical> criterio) {
        if (noAtual == null) return;
        for (int i = 0; i < noAtual.contagem; i++) {
            coletarItens(noAtual.ligacoes[i], lista, criterio); 
            if (noAtual.pecas[i] != null && criterio.test(noAtual.pecas[i])) {
                lista.add(noAtual.pecas[i]);
            }
        }
        coletarItens(noAtual.ligacoes[noAtual.contagem], lista, criterio); 
    }
    
    private void removerTodasOcorrencias(Predicate<ItemMusical> criterio) {
        List<ItemMusical> itensParaRemover = new ArrayList<>();
        coletarItens(this.topo, itensParaRemover, criterio);

        if (itensParaRemover.isEmpty()) {
            System.out.println("Nenhum item encontrado para o critério de eliminação.");
            return;
        }

        System.out.println("Iniciando eliminação de " + itensParaRemover.size() + " item(ns).");
        for (ItemMusical item : itensParaRemover) {
            boolean diminuiu[] = new boolean[1];
            System.out.println(" -> Eliminando: " + item);
            this.topo = this.eliminar(item, this.topo, diminuiu);
            if (diminuiu[0] && (this.topo != null) && (this.topo.contagem == 0)) {
                this.topo = this.topo.ligacoes[0];
            } else if (this.topo == null) {
                break;
            }
        }
        System.out.println("Eliminação concluída.");
    }

    private void exibirEstrutura(NoArvore noAtual, int nivel) {
        if (noAtual != null) {
            System.out.print("  Nivel " + nivel + " [Qtd: " + noAtual.contagem + "]: ");
            for (int i = 0; i < noAtual.contagem; i++)
                if (noAtual.pecas[i] != null) System.out.print(" {" + noAtual.pecas[i].getIdentificador() + "} "); 
            System.out.println();
            for (int i = 0; i <= noAtual.contagem; i++) {
                exibirEstrutura(noAtual.ligacoes[i], nivel + 1);
            }
        }
    }
    
    private ItemMusical[] localizarPorTrechoLirico(String trecho, NoArvore noAtual, List<ItemMusical> resultado) {
        if (noAtual == null) return resultado.toArray(new ItemMusical[0]);

        for (int i = 0; i < noAtual.contagem; i++) {
            localizarPorTrechoLirico(trecho, noAtual.ligacoes[i], resultado);
            if (noAtual.pecas[i] != null && noAtual.pecas[i].getTextoLirico().toLowerCase().contains(trecho.toLowerCase())) {
                resultado.add(noAtual.pecas[i]);
            }
        }
        localizarPorTrechoLirico(trecho, noAtual.ligacoes[noAtual.contagem], resultado);
        return resultado.toArray(new ItemMusical[0]);
    }

    private ItemMusical[] localizarPorCompositor(String compositor, NoArvore noAtual, List<ItemMusical> resultado) {
        if (noAtual == null) return resultado.toArray(new ItemMusical[0]);

        for (int i = 0; i < noAtual.contagem; i++) {
            localizarPorCompositor(compositor, noAtual.ligacoes[i], resultado);
            if (noAtual.pecas[i] != null && noAtual.pecas[i].getCompositor().equalsIgnoreCase(compositor)) {
                resultado.add(noAtual.pecas[i]);
            }
        }
        localizarPorCompositor(compositor, noAtual.ligacoes[noAtual.contagem], resultado);
        return resultado.toArray(new ItemMusical[0]);
    }

    private ItemMusical[] localizarPorTituloPeca(String titulo, NoArvore noAtual, List<ItemMusical> resultado) {
        if (noAtual == null) return resultado.toArray(new ItemMusical[0]);

        for (int i = 0; i < noAtual.contagem; i++) {
            localizarPorTituloPeca(titulo, noAtual.ligacoes[i], resultado);
            if (noAtual.pecas[i] != null && noAtual.pecas[i].getTituloPeca().equalsIgnoreCase(titulo)) {
                resultado.add(noAtual.pecas[i]);
            }
        }
        localizarPorTituloPeca(titulo, noAtual.ligacoes[noAtual.contagem], resultado);
        return resultado.toArray(new ItemMusical[0]);
    }
    
    private boolean reconstituir(NoArvore noAtual, NoArvore noPai, int posPai) {
        boolean diminuiu = true;
        if (posPai < noPai.contagem) { // Com irmão à direita
            NoArvore aux = noPai.ligacoes[posPai + 1];
            int dispAux = (aux.contagem - this.minOrdem + 1) / 2;
            noAtual.pecas[noAtual.contagem++] = noPai.pecas[posPai];
            noAtual.ligacoes[noAtual.contagem] = aux.ligacoes[0];
            aux.ligacoes[0] = null; 
            if (dispAux > 0) { // Redistribuição
                for (int j = 0; j < dispAux - 1; j++) {
                    this.inserirNoNo(noAtual, aux.pecas[j], aux.ligacoes[j + 1]);
                    aux.ligacoes[j + 1] = null; 
                }
                noPai.pecas[posPai] = aux.pecas[dispAux - 1];
                aux.contagem = aux.contagem - dispAux;
                for (int j = 0; j < aux.contagem; j++)
                    aux.pecas[j] = aux.pecas[j + dispAux];
                for (int j = 0; j <= aux.contagem; j++)
                    aux.ligacoes[j] = aux.ligacoes[j + dispAux];
                aux.ligacoes[aux.contagem + dispAux] = null;
                diminuiu = false;
            } else { // Fusão
                for (int j = 0; j < aux.contagem; j++) {
                    this.inserirNoNo(noAtual, aux.pecas[j], aux.ligacoes[j + 1]);
                    aux.ligacoes[j + 1] = null; 
                }
                noPai.ligacoes[posPai + 1] = null; 
                for (int j = posPai; j < noPai.contagem - 1; j++) {
                    noPai.pecas[j] = noPai.pecas[j + 1];
                    noPai.ligacoes[j + 1] = noPai.ligacoes[j + 2];
                }
                noPai.ligacoes[noPai.contagem--] = null; 
                diminuiu = noPai.contagem < this.minOrdem;
            }
        } else { // Com irmão à esquerda
            NoArvore aux = noPai.ligacoes[posPai - 1];
            int dispAux = (aux.contagem - this.minOrdem + 1) / 2;
            
            for (int j = noAtual.contagem - 1; j >= 0; j--)
                noAtual.pecas[j + 1] = noAtual.pecas[j];
            noAtual.pecas[0] = noPai.pecas[posPai - 1];
            
            for (int j = noAtual.contagem; j >= 0; j--)
                noAtual.ligacoes[j + 1] = noAtual.ligacoes[j];
            noAtual.contagem++;
            
            if (dispAux > 0) { // Redistribuição
                for (int j = 0; j < dispAux - 1; j++) {
                    this.inserirNoNo(noAtual, aux.pecas[aux.contagem - j - 1], aux.ligacoes[aux.contagem - j]);
                    aux.ligacoes[aux.contagem - j] = null; 
                }
                noAtual.ligacoes[0] = aux.ligacoes[aux.contagem - dispAux + 1];
                aux.ligacoes[aux.contagem - dispAux + 1] = null; 
                noPai.pecas[posPai - 1] = aux.pecas[aux.contagem - dispAux];
                aux.contagem = aux.contagem - dispAux;
                diminuiu = false;
            } else { // Fusão
                for (int j = 0; j < noAtual.contagem; j++) {
                    this.inserirNoNo(aux, noAtual.pecas[j], noAtual.ligacoes[j + 1]);
                    noAtual.ligacoes[j + 1] = null; 
                }
                noPai.ligacoes[posPai] = aux;
                noPai.ligacoes[noPai.contagem--] = null; 
                diminuiu = noPai.contagem < this.minOrdem;
            }
        }
        return diminuiu;
    }

    private boolean antecessor(NoArvore noAtual, int ind, NoArvore noPai) {
        boolean diminuiu = true;
        if (noPai.ligacoes[noPai.contagem] != null) {
            diminuiu = antecessor(noAtual, ind, noPai.ligacoes[noPai.contagem]);
            if (diminuiu)
                diminuiu = reconstituir(noPai.ligacoes[noPai.contagem], noPai, noPai.contagem);
        } else {
            noAtual.pecas[ind] = noPai.pecas[--noPai.contagem];
            diminuiu = noPai.contagem < this.minOrdem;
        }
        return diminuiu;
    }

    private NoArvore eliminar(ItemMusical registro, NoArvore noAtual, boolean[] diminuiu) {
        if (noAtual == null) {
            // Se o item não for encontrado, a função não deve tentar eliminar nada.
            // Para remoção pela chave (ID), isso é um erro.
            // Para remoção por critério (compositor, letra, etc.), o erro de não encontrar
            // é capturado em removerTodasOcorrencias.
            if (registro.getCompositor().isEmpty() && registro.getTituloPeca().isEmpty() && registro.getTextoLirico().isEmpty()) {
                // Apenas exibe erro se for uma busca direta por ID que falhou
                System.out.println("Erro: Item não encontrado para eliminação. ID: " + registro.getIdentificador());
            }
            diminuiu[0] = false;
        } else {
            int ind = 0;
            while ((ind < noAtual.contagem) && (registro.comparar(noAtual.pecas[ind]) > 0))
                ind++;
                
            if (ind < noAtual.contagem && registro.comparar(noAtual.pecas[ind]) == 0) { // Achou
                if (noAtual.ligacoes[ind] == null) { // Nó folha
                    noAtual.contagem--;
                    diminuiu[0] = noAtual.contagem < this.minOrdem;
                    for (int j = ind; j < noAtual.contagem; j++) {
                        noAtual.pecas[j] = noAtual.pecas[j + 1];
                        noAtual.ligacoes[j] = noAtual.ligacoes[j + 1];
                    }
                    noAtual.ligacoes[noAtual.contagem] = noAtual.ligacoes[noAtual.contagem + 1];
                    noAtual.ligacoes[noAtual.contagem + 1] = null; 
                } else { // Nó não é folha: trocar com antecessor
                    diminuiu[0] = antecessor(noAtual, ind, noAtual.ligacoes[ind]);
                    if (diminuiu[0])
                        diminuiu[0] = reconstituir(noAtual.ligacoes[ind], noAtual, ind);
                }
            } else { // Não achou
                if (ind < noAtual.contagem && registro.comparar(noAtual.pecas[ind]) > 0)
                    ind++; 
                    
                noAtual.ligacoes[ind] = eliminar(registro, noAtual.ligacoes[ind], diminuiu);
                
                if (diminuiu[0])
                    diminuiu[0] = reconstituir(noAtual.ligacoes[ind], noAtual, ind);
            }
        }
        return noAtual;
    }
}


// ====================================================================
// CLASSE 3: AppCatalogo (Interface do Usuário com método main)
// ====================================================================
public class AppCatalogo {

    public static void main(String[] args) throws Exception {
        // Inicializa o Catálogo (Árvore B) com ordem mínima 2 (maxOrdem = 4)
        CatalogoB catalogo = new CatalogoB(2); 
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("\n===== Menu do Sistema de Catálogo Musical (Árvore B) =====");
            System.out.println("1 - Registrar novo item (manual)");
            System.out.println("2 - Importar acervo de arquivo CSV");
            System.out.println("--- Consulta Rápida (pela chave de ordenação da B-Tree)");
            System.out.println("3 - Consultar por Identificador Único");
            System.out.println("--- Busca Completa (percorre a estrutura)");
            System.out.println("4 - Localizar por Nome do Compositor");
            System.out.println("5 - Localizar por Título da Peça");
            System.out.println("6 - Localizar por Trecho Lírico");
            System.out.println("--- Manutenção");
            System.out.println("7 - Visualizar estrutura do Catálogo (B-Tree)");
            System.out.println("8 - Remover por Identificador Único");
            System.out.println("9 - Eliminar por Título da Peça (todas as ocorrências)");
            System.out.println("10 - Eliminar por Nome do Compositor (todas as ocorrências)");
            System.out.println("11 - Eliminar por Trecho Lírico (todas as ocorrências)");
            System.out.println("0 - Encerrar aplicação");
            System.out.print("Selecione uma operação: ");
            
            try {
                String input = in.readLine().trim();
                if (input.isEmpty()) continue;
                int opcao = Integer.parseInt(input);

                switch (opcao) {
                    case 1: { // Registrar manualmente
                        System.out.print("Digite o Identificador Único (inteiro): ");
                        int identificador = Integer.parseInt(in.readLine());
                        System.out.print("Digite o nome do Compositor: ");
                        String compositor = in.readLine();
                        System.out.print("Digite o Título da Peça: ");
                        String tituloPeca = in.readLine();
                        System.out.print("Digite o Trecho Lírico (ou letra completa): ");
                        String textoLirico = in.readLine();
                        ItemMusical novoItem = new ItemMusical(identificador, compositor, tituloPeca, textoLirico);
                        catalogo.adicionar(novoItem);
                        System.out.println("Item musical registrado.");
                        break;
                    }

                    case 2: { // Importar de arquivo
                        System.out.print("Digite o caminho do arquivo CSV (ex: musica.csv): ");
                        String caminhoArquivo = in.readLine().trim();
                        CatalogoB.importarAcervoDoCSV(catalogo, caminhoArquivo);
                        break;
                    }

                    case 3: { // Consultar por Identificador Único (Chave)
                        System.out.print("Digite o Identificador Único para consulta: ");
                        int identificador = Integer.parseInt(in.readLine());
                        ItemMusical itemBusca = new ItemMusical(identificador, "", "", "");
                        ItemMusical resultado = catalogo.buscarItem(itemBusca);
                        if (resultado == null) {
                            System.out.println("Item não encontrado pelo ID.");
                        } else {
                            System.out.println("Item encontrado: " + resultado);
                        }
                        break;
                    }

                    case 4: { // Localizar por Compositor
                        System.out.print("Digite o nome do Compositor para buscar: ");
                        String buscaCompositor = in.readLine();
                        CatalogoB.exibirBusca(catalogo.localizarPorCompositor(buscaCompositor));
                        break;
                    }

                    case 5: { // Localizar por Título da Peça
                        System.out.print("Digite o Título da Peça para buscar: ");
                        String buscaTitulo = in.readLine();
                        CatalogoB.exibirBusca(catalogo.localizarPorTituloPeca(buscaTitulo));
                        break;
                    }

                    case 6: { // Localizar por Trecho Lírico
                        System.out.print("Digite um Trecho Lírico para buscar (busca parcial): ");
                        String buscaTrecho = in.readLine();
                        CatalogoB.exibirBusca(catalogo.localizarPorTrechoLirico(buscaTrecho));
                        break;
                    }

                    case 7: // Visualizar estrutura
                        catalogo.exibirEstrutura();
                        break;

                    case 8: { // Remover por Identificador Único (Chave)
                        System.out.print("Digite o Identificador Único para remover: ");
                        int identificador = Integer.parseInt(in.readLine());
                        ItemMusical itemParaRemover = new ItemMusical(identificador, "", "", "");
                        catalogo.eliminar(itemParaRemover);
                        System.out.println("Tentativa de remoção por ID concluída.");
                        break;
                    }

                    case 9: { // Eliminar por Título da Peça
                        System.out.print("Digite o Título da Peça para eliminar (remove todas as ocorrências): ");
                        String tituloRemover = in.readLine();
                        catalogo.eliminarPorTituloPeca(tituloRemover);
                        break;
                    }

                    case 10: { // Eliminar por Compositor
                        System.out.print("Digite o nome do Compositor para eliminar (remove todas as ocorrências): ");
                        String compositorRemover = in.readLine();
                        catalogo.eliminarPorCompositor(compositorRemover);
                        break;
                    }

                    case 11: { // Eliminar por Trecho Lírico
                        System.out.print("Digite o Trecho Lírico para eliminar (remove todas as ocorrências): ");
                        String trechoRemover = in.readLine();
                        catalogo.eliminarPorTrechoLirico(trechoRemover);
                        break;
                    }

                    case 0: // Sair
                        System.out.println("Encerrando aplicação. Adeus!");
                        return;

                    default:
                        System.out.println("Operação inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um número para a opção e um inteiro para os Identificadores.");
            } catch (Exception e) {
                System.out.println("Ocorreu um erro no sistema: " + e.getMessage());
                // e.printStackTrace(); 
            }
        }
    }
}