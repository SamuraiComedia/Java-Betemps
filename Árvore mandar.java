public class BTreeNode {
    

    // método para remover de um nó interno usando o Predecessor
    private void removeFromInternalNode(int idx) {
        int key = keys[idx];

        // se o filho tiver nós suficientes
        if (children[idx].n >= T) {
            System.out.println("-> Remoção usa PREDECESSOR para a chave: " + key);
            int pred = getPredecessor(idx);
            keys[idx] = pred;
            children[idx].remove(pred);
        }
        // o filho seguinte tem chaves suficientes
        else if (children[idx + 1].n >= T) {
            System.out.println("-> Remoção usa SUCESSOR para a chave: " + key);
            int succ = getSuccessor(idx);
            keys[idx] = succ;
            children[idx + 1].remove(succ);
        }
        // caso ambos tenham o mínimo, realiza-se a fusão dos nós
        else {
            System.out.println("-> Ambos os filhos têm chaves mínimas. Iniciando FUSÃO para remoção interna.");
            merge(idx);
            children[idx].remove(key);
        }
    }

    // buscar chave do irmão esquerdo
    private void borrowFromPrev(int idx) {
        System.out.println("-> borrowFromPrev(): emprestando chave do irmão esquerdo.");
        
        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx - 1];

        
    }

    //buscar chave do irmão direito
    private void borrowFromNext(int idx) {
        System.out.println("-> borrowFromNext(): emprestando chave do irmão direito.");
        
        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx + 1];

        
    }

    //fundir dois nós
    private void merge(int idx) {
        System.out.println("-> merge(): realizando fusão dos nós " + idx + " e " + (idx + 1));
        
        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx + 1];

        /
    }
}