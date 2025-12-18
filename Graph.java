package grafo;

import tools.In;
import java.util.*;
import java.io.*;

// Enumerador para cores de vértices
enum Color {
    WHITE, GRAY, BLACK
}

// Classe para armazenar atributos de vértices
class VertexAttributes {
    Color color;
    int pred;
    int d; // Tempo de descoberta
    int f; // Tempo de finalização
}

// Classe principal do grafo
public class Graph {
    static final Integer INF = Integer.MAX_VALUE;
    static final Integer NIL = -1;
    private int V; // número de vértices
    private int E; // número de arestas
    private Vector<Vector<Integer>> adj; // lista de adjacência
    private int[][] adjMatrix; // matriz de adjacência
    private Vector<VertexAttributes> atribs; // atributos dos vértices
    private boolean useMatrix; // flag para usar matriz
    private int time; // contador de tempo para DFS

    // Construtor para criar grafo vazio
    public Graph(int V, boolean useMatrix) {
        this.V = V;
        this.E = 0;
        this.useMatrix = useMatrix;
        if (useMatrix) {
            initializeGraphM();
        } else {
            initializeGraph();
        }
    }

    // Construtor para criar grafo a partir de arquivo
    public Graph(In in, boolean useMatrix) {
        this.useMatrix = useMatrix;
        if (useMatrix) {
            while (in.hasNextLine()) {
                String line = in.nextLine().trim();
                if (line.startsWith("c"))
                    continue;
                else if (line.startsWith("p")) {
                    String[] parts = line.split(" ");
                    this.V = Integer.parseInt(parts[2]);
                    this.E = 0;
                    initializeGraphM();
                } else if (line.startsWith("a")) {
                    String[] parts = line.split(" ");
                    int u = Integer.parseInt(parts[1]) - 1;
                    int v = Integer.parseInt(parts[2]) - 1;
                    addEdgeM(u, v);
                }
            }
        } else {
            while (in.hasNextLine()) {
                String line = in.nextLine().trim();
                if (line.startsWith("c"))
                    continue;
                else if (line.startsWith("p")) {
                    String[] parts = line.split(" ");
                    this.V = Integer.parseInt(parts[2]);
                    this.E = 0;
                    initializeGraph();
                } else if (line.startsWith("a")) {
                    String[] parts = line.split(" ");
                    int u = Integer.parseInt(parts[1]) - 1;
                    int v = Integer.parseInt(parts[2]) - 1;
                    addEdge(u, v);
                }
            }
        }
    }

    // Inicializa as estruturas internas (matriz de adjacência)
    private void initializeGraphM() {
        adjMatrix = new int[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                adjMatrix[i][j] = 0; // 0 indica ausência de aresta
            }
        }
        atribs = new Vector<>(V);
        for (int i = 0; i < V; i++) {
            atribs.add(new VertexAttributes());
        }
    }

    // Adiciona uma aresta (matriz de adjacência)
    public void addEdgeM(int u, int v) {
        adjMatrix[u][v] = 1; // Altere o peso, se necessário
        E++;
    }

    // Retorna os vizinhos de um vértice (matriz)
    public List<Integer> adjM(int v) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            if (adjMatrix[v][i] != 0) { // Considera arestas com peso não nulo
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    // Inicializa as estruturas internas do grafo
    private void initializeGraph() {
        adj = new Vector<>(V);
        for (int i = 0; i < V; i++) {
            adj.add(new Vector<>());
        }
        atribs = new Vector<>(V);
        for (int i = 0; i < V; i++) {
            atribs.add(new VertexAttributes());
        }
    }

    // Adiciona uma aresta dirigida de v para w
    public void addEdge(int v, int w) {
        adj.get(v).add(w);
        E++;
    }

    // Retorna os vértices adjacentes de um vértice
    public Iterable<Integer> adj(int v) {
        return adj.get(v);
    }

    // Realiza uma busca em largura (BFS) a partir de um vértice
    public void BFS(int s) {
        // Inicializa os atributos dos vértices
        for (VertexAttributes attr : atribs) {
            attr.color = Color.WHITE;
            attr.d = INF;
            attr.pred = NIL;
        }

        // Marca o vértice de origem
        atribs.get(s).color = Color.GRAY;
        atribs.get(s).d = 0;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);

        System.out.println("Iniciando BFS a partir do vértice " + s);

        // Executa a BFS
        while (!queue.isEmpty()) {
            int u = queue.poll();
            System.out.println("Visitando vértice " + u); // Print para ver qual vértice está sendo visitado
            for (int v : adj(u)) { // Supondo que adj(u) retorna os vizinhos de u
                if (atribs.get(v).color == Color.WHITE) {
                    atribs.get(v).color = Color.GRAY;
                    atribs.get(v).d = atribs.get(u).d + 1;
                    atribs.get(v).pred = u;
                    queue.add(v);
                }
            }
            atribs.get(u).color = Color.BLACK;
        }
    }

    // Realiza uma busca em profundidade (DFS)
    public void DFS() {
        time = 0;
        // Inicializa os atributos dos vértices
        for (VertexAttributes attr : atribs) {
            attr.color = Color.WHITE;
            attr.pred = NIL;
        }

        System.out.println("Iniciando DFS");

        // Executa a DFS para cada vértice não visitado
        for (int i = 0; i < V; i++) {
            if (atribs.get(i).color == Color.WHITE) {
                DFSVisit(i);
            }
        }
    }

    // Visita um vértice na DFS
    private void DFSVisit(int u) {
        time++;
        atribs.get(u).d = time;
        atribs.get(u).color = Color.GRAY;

        System.out.println("Visitando vértice " + u + " (DFS)"); // Print para ver qual vértice está sendo visitado na
                                                                 // DFS
        System.out.println("Tempo de descoberta do vértice " + u + ": " + atribs.get(u).d); // Print do tempo de
                                                                                            // descoberta

        // Visita os vizinhos do vértice u
        for (int v : adj(u)) {
            if (atribs.get(v).color == Color.WHITE) {
                atribs.get(v).pred = u;
                DFSVisit(v);
            }
        }

        // Finaliza a visita ao vértice u
        atribs.get(u).color = Color.BLACK;
        time++;
        atribs.get(u).f = time;

        System.out.println("Finalizando vértice " + u + " (DFS)"); // Print quando finalizar o vértice na DFS
        System.out.println("Tempo de finalização do vértice " + u + ": " + atribs.get(u).f); // Print do tempo de
                                                                                             // finalização
    }

    // Retorna o status dos atributos dos vértices
    public String StatusAtribs() {
        StringBuilder sb = new StringBuilder("Vertex Attributes (color, pred, d, f):\n");
        for (int i = 0; i < atribs.size(); i++) {
            VertexAttributes a = atribs.get(i);
            sb.append(i).append(": ")
                    .append(a.color).append(", ")
                    .append(a.pred).append(", ")
                    .append(a.d).append(", ")
                    .append(a.f).append("\n");
        }
        return sb.toString();
    }

    // Exibe o grafo em formato de string
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(V).append(" vertices, ").append(E).append(" edges\n");
        for (int i = 0; i < V; i++) {
            sb.append(i).append(": ").append(adj.get(i)).append("\n");
        }
        return sb.toString();
    }

    public int[] Bellman_Ford_Lista(int src) {
        int[] dist = new int[V]; // Array para armazenar as distâncias mínimas
        Arrays.fill(dist, INF); // Inicializa todas as distâncias como infinito
        dist[src] = 0; // A distância do vértice de origem para si mesmo é 0

        // Relaxa todas as arestas (V - 1) vezes
        for (int i = 1; i < V; i++) { // Executa V - 1 iterações
            for (int u = 0; u < V; u++) {
                for (int v : adj(u)) { // Obtém os vizinhos de u a partir da lista de adjacência
                    // Relaxamento da aresta (u, v)
                    int weight = 1; // Substituir por peso real se necessário
                    if (dist[u] != INF && dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                    }
                }
            }
        }

        // Verifica a existência de ciclos de peso negativo
        for (int u = 0; u < V; u++) {
            for (int v : adj(u)) {
                int weight = 1; // Substituir por peso real se necessário
                if (dist[u] != INF && dist[u] + weight < dist[v]) {
                    System.out.println("Ciclo de peso negativo detectado!");
                    return null;
                }
            }
        }

        return dist;
    }

    public int[] Bellman_Ford_Matriz(int src) {
        // Array para armazenar as distâncias mínimas do vértice fonte para os demais
        int[] dist = new int[V];
        Arrays.fill(dist, INF); // Inicializa todas as distâncias como infinito
        dist[src] = 0; // A distância do vértice fonte para si mesmo é 0

        // Relaxa todas as arestas (V - 1) vezes
        for (int i = 0; i < V - 1; i++) {
            for (int u = 0; u < V; u++) {
                for (int v = 0; v < V; v++) {
                    if (adjMatrix[u][v] != 0 && dist[u] != INF && dist[u] + adjMatrix[u][v] < dist[v]) {
                        dist[v] = dist[u] + adjMatrix[u][v];
                    }
                }
            }
        }

        // Verifica a existência de ciclos de peso negativo
        for (int u = 0; u < V; u++) {
            for (int v = 0; v < V; v++) {
                if (adjMatrix[u][v] != 0 && dist[u] != INF && dist[u] + adjMatrix[u][v] < dist[v]) {
                    System.out.println("Ciclo de peso negativo detectado!");
                    return null;
                }
            }
        }

        return dist;
    }

    public void Dijkstra_Lista(int src) {
        int[] dist = new int[V]; // Array para armazenar a distância mínima
        boolean[] visited = new boolean[V]; // Array para marcar os vértices visitados
        Arrays.fill(dist, INF); // Inicializa todas as distâncias como infinito
        dist[src] = 0; // A distância do vértice de origem para si mesmo é 0

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[] { src, 0 }); // Insere o vértice de origem na fila de prioridade

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];

            if (visited[u])
                continue;
            visited[u] = true;

            for (int v : adj(u)) {
                if (!visited[v]) {
                    int newDist = dist[u] + 1; // Considera peso 1 para arestas sem peso explícito
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        pq.add(new int[] { v, dist[v] });
                    }
                }
            }
        }
    }

    public void Dijkstra_Matriz(int src) {
        int[] dist = new int[V]; // Array para armazenar a distância mínima
        boolean[] visited = new boolean[V]; // Array para marcar os vértices visitados
        Arrays.fill(dist, INF); // Inicializa todas as distâncias como infinito
        dist[src] = 0; // A distância do vértice de origem para si mesmo é 0

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[] { src, 0 }); // Insere o vértice de origem na fila de prioridade

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];

            if (visited[u])
                continue;
            visited[u] = true;

            for (int v = 0; v < V; v++) {
                if (!visited[v] && adjMatrix[u][v] != 0) { // Verifica se há aresta entre u e v
                    int newDist = dist[u] + adjMatrix[u][v];
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        pq.add(new int[] { v, dist[v] });
                    }
                }
            }
        }

        System.out.println("Distâncias mínimas a partir do vértice " + src + ":");
        for (int i = 0; i < V; i++) {
            System.out.println("Vértice " + i + ": " + (dist[i] == INF ? "Infinito" : dist[i]));
        }
    }

    public void floydWarshall() {
        // Cria a matriz de distâncias inicial
        int[][] dist = new int[V][V];

        // Inicializa a matriz de distâncias com os valores da matriz de adjacência
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i == j) {
                    dist[i][j] = 0; // Distância de um vértice para ele mesmo é 0
                } else if (adjMatrix[i][j] != 0) {
                    dist[i][j] = adjMatrix[i][j]; // Aresta existente
                } else {
                    dist[i][j] = INF; // Sem aresta direta
                }
            }
        }

        // Aplica o algoritmo de Floyd-Warshall
        for (int k = 0; k < V; k++) { // Vértice intermediário
            for (int i = 0; i < V; i++) { // Vértice de origem
                for (int j = 0; j < V; j++) { // Vértice de destino
                    // Se há um caminho mais curto via k, atualiza a distância
                    if (dist[i][k] != INF && dist[k][j] != INF && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        // Verifica ciclos negativos
        for (int i = 0; i < V; i++) {
            if (dist[i][i] < 0) {
                System.out.println("Ciclo de peso negativo detectado!");
                return;
            }
        }

        // Imprime a matriz de distâncias
        System.out.println("Matriz de distâncias mínimas:");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (dist[i][j] == INF) {
                    System.out.print("INF ");
                } else {
                    System.out.print(dist[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    // Adiciona método para calcular fluxo máximo usando Ford-Fulkerson
    public int fordFulkerson(int source, int sink) {
        if (!useMatrix) {
            throw new UnsupportedOperationException("Ford-Fulkerson só pode ser usado com matriz de adjacência.");
        }

        // Cria uma matriz residual para armazenar as capacidades residuais do grafo
        int[][] residualGraph = new int[V][V];
        for (int u = 0; u < V; u++) {
            for (int v = 0; v < V; v++) {
                residualGraph[u][v] = adjMatrix[u][v];
            }
        }

        int[] parent = new int[V]; // Para armazenar o caminho aumentante
        int maxFlow = 0; // Inicializa o fluxo máximo como 0

        // Enquanto existir um caminho aumentante no grafo residual
        while (bfsForFordFulkerson(residualGraph, source, sink, parent)) {
            // Encontra a capacidade mínima no caminho encontrado
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
            }

            // Atualiza as capacidades residuais e o fluxo reverso
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residualGraph[u][v] -= pathFlow;
                residualGraph[v][u] += pathFlow;
            }

            // Adiciona o fluxo do caminho ao fluxo máximo
            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    // Implementa BFS para encontrar um caminho aumentante
    private boolean bfsForFordFulkerson(int[][] residualGraph, int source, int sink, int[] parent) {
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;
        parent[source] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 0; v < V; v++) {
                // Se existe capacidade residual e o vértice v ainda não foi visitado
                if (!visited[v] && residualGraph[u][v] > 0) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;

                    // Se alcançamos o vertice sink, retornamos true
                    if (v == sink) {
                        return true;
                    }
                }
            }
        }

        return false; // Não existe caminho aumentante
    }


    public void primMST() {
        int[] parent = new int[V]; // Array para armazenar a MST
        int[] key = new int[V]; // Valores de chave para encontrar o menor peso
        boolean[] mstSet = new boolean[V]; // Conjunto de vértices incluídos na MST

        // Inicializa todas as chaves como infinito
        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(mstSet, false);

        // Começa com o primeiro vértice
        key[0] = 0;
        parent[0] = -1;

        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet); // Escolhe o vértice de chave mínima
            mstSet[u] = true;

            // Atualiza os valores de chave e os pais dos vértices adjacentes
            for (int v = 0; v < V; v++) {
                if (adjMatrix[u][v] != 0 && !mstSet[v] && adjMatrix[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = adjMatrix[u][v];
                }
            }
        }

        printMST(parent);
    }

    // Encontra o vértice com a chave mínima que ainda não está na MST
    private int minKey(int[] key, boolean[] mstSet) {
        int min = Integer.MAX_VALUE, minIndex = -1;

        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }

        return minIndex;
    }

    // Exibe a MST
    private void printMST(int[] parent) {
        System.out.println("Aresta\tPeso");
        for (int i = 1; i < V; i++) {
            System.out.println(parent[i] + " - " + i + "\t" + adjMatrix[i][parent[i]]);
        }
    }

    // Programa principal
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Graph g = null;

        System.out.println("1 - Ler grafo de arquivo");
        System.out.println("2 - Criar grafo manualmente");
        int choice = scanner.nextInt();

        // Solicitar se deseja usar matriz ou lista
        System.out.println("1 - Usar matriz de adjacência\n2 - Usar lista de adjacência");
        int useMatrixChoice = scanner.nextInt();
        boolean useMatrix = useMatrixChoice == 1;

        if (choice == 1) {
            System.out.print("Digite o nome do arquivo: ");
            String fileName = scanner.next();
            try {
                g = new Graph(new In(new File(fileName)), useMatrix);
            } catch (Exception e) {
                System.out.println("Erro ao ler arquivo.");
                return;
            }
        } else if (choice == 2) {
            g = new Graph(4, useMatrix); // Passando o parâmetro useMatrix
            g.addEdgeM(0, 1);
            g.addEdgeM(1, 2);
            g.addEdgeM(2, 3);
        }

        System.out.println("1 - BFS\n2 - DFS");
        int method = scanner.nextInt();
        if (method == 1) {
            System.out.print("Vértice inicial: ");
            int start = scanner.nextInt();
            g.BFS(start);
        } else {
            g.DFS();
        }

        System.out.println(g.StatusAtribs());
        scanner.close();
    }

    public List<Integer> topologicalSort(int vertices, Object graph) {
        boolean[] visited = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();

        if (graph instanceof List) {
            List<List<Integer>> adjList = (List<List<Integer>>) graph;
            for (int i = 0; i < vertices; i++) {
                if (!visited[i])
                    dfsList(i, visited, stack, adjList);
            }
        } else if (graph instanceof int[][]) {
            int[][] adjMatrix = (int[][]) graph;
            for (int i = 0; i < vertices; i++) {
                if (!visited[i])
                    dfsMatrix(i, visited, stack, adjMatrix);
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty())
            result.add(stack.pop());
        return result;
    }

    private void dfsList(int node, boolean[] visited, Stack<Integer> stack, List<List<Integer>> adjList) {
        visited[node] = true;
        for (int neighbor : adjList.get(node)) {
            if (!visited[neighbor])
                dfsList(neighbor, visited, stack, adjList);
        }
        stack.push(node);
    }

    private void dfsMatrix(int node, boolean[] visited, Stack<Integer> stack, int[][] adjMatrix) {
        visited[node] = true;
        for (int neighbor = 0; neighbor < adjMatrix.length; neighbor++) {
            if (adjMatrix[node][neighbor] == 1 && !visited[neighbor])
                dfsMatrix(neighbor, visited, stack, adjMatrix);
        }
        stack.push(node);
    }

    // Classe auxiliar para arestas
    class Edge implements Comparable<Edge> {
        int src, dest, weight;

        Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    // Função que retorna a MST usando o algoritmo de Kruskal
    public void Kruskal() {
        List<Edge> edges = getEdges(); // Recupera as arestas do grafo
        Collections.sort(edges); // Ordena as arestas pelo peso

        // Estrutura para encontrar e unir conjuntos
        int[] parent = new int[V];
        int[] rank = new int[V];
        for (int i = 0; i < V; i++) {
            parent[i] = i; // Inicialmente, cada vértice é seu próprio pai
            rank[i] = 0;
        }

        List<Edge> mst = new ArrayList<>();
        int mstWeight = 0;

        for (Edge edge : edges) {
            int setU = find(parent, edge.src);
            int setV = find(parent, edge.dest);

            // Inclui a aresta se não forma ciclo
            if (setU != setV) {
                mst.add(edge);
                mstWeight += edge.weight;
                union(parent, rank, setU, setV);
            }

            // Para grafos pequenos, podemos interromper cedo
            if (mst.size() == V - 1)
                break;
        }

        printMST(mst, mstWeight);
    }

    // Recupera todas as arestas do grafo
    private List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        if (useMatrix) {
            for (int u = 0; u < V; u++) {
                for (int v = 0; v < V; v++) {
                    if (adjMatrix[u][v] != 0) {
                        edges.add(new Edge(u, v, adjMatrix[u][v]));
                    }
                }
            }
        } else {
            for (int u = 0; u < V; u++) {
                for (int v : adj.get(u)) {
                    edges.add(new Edge(u, v, 1)); // Substituir peso real se necessário
                }
            }
        }
        return edges;
    }

    // Encontra o conjunto de um elemento x (com compressão de caminho)
    private int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }

    // Une dois conjuntos (por rank)
    private void union(int[] parent, int[] rank, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }

    // Exibe a MST e seu peso total
    private void printMST(List<Edge> mst, int weight) {
        System.out.println("Árvore Geradora Mínima (MST):");
        for (Edge edge : mst) {
            System.out.println(edge.src + " - " + edge.dest + " \tPeso: " + edge.weight);
        }
        System.out.println("Peso total da MST: " + weight);
    }

}
