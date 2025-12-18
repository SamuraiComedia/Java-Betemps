public class StringSET {
    //  Integer (ou Object) qualquer.
    private TrieST<Object> st;
    private static final Object PRESENT = new Object();

    /**
     conjunto de string vazio.
     
    public StringSET() {
        st = new TrieST<Object>();
    }

    /**
     * chave 'key' no conjunto.
     */
    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("Chave não pode ser nula");
        st.put(key, PRESENT);
    }

    /**
     * tira a chave 'key' do conjunto.
     */
    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("Chave não pode ser nula");
        st.delete(key);
    }

    /**
     * a chave está no conjunto?
     */
    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("Chave não pode ser nula");
        return st.contains(key);
    }

    /**
     * conjunto tá vazio?
     */
    public boolean isEmpty() {
        return st.isEmpty();
    }

    /**
     * num de chaves no conjunto.
     */
    public int size() {
        return st.size();
    }

    /**
     *string do conjunto.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (String s : st.keys()) {
            if (!first) sb.append(", ");
            sb.append(s);
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    // programa de teste 
    public static void main(String[] args) {
        StringSET conjunto = new StringSET();
        
        System.out.println("Adicionando: banana, maçã, uva");
        conjunto.add("banana");
        conjunto.add("maçã");
        conjunto.add("uva");
        
        System.out.println("Conjunto atual: " + conjunto.toString());
        System.out.println("Tamanho: " + conjunto.size());
        
        System.out.println("Contém 'uva'? " + conjunto.contains("uva"));
        
        System.out.println("Removendo 'maçã'...");
        conjunto.delete("maçã");
        
        System.out.println("Conjunto após remoção: " + conjunto.toString());
        System.out.println("Está vazio? " + conjunto.isEmpty());
    }
}