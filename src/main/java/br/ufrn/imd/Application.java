package br.ufrn.imd;
public class Application {

    private DocumentManager docs;  // Supondo que DocumentManager é a classe que gerencia múltiplos documentos

    public void openDocument(String name) {
        if (!canOpenDocument(name)) {
            // não pode abrir este documento
            return;
        }

        Document doc = doCreateDocument();

        if (doc != null) {
            docs.addDocument(doc);
            aboutToOpenDocument(doc);
            doc.open();
            doc.doRead();
        }
    }

    private boolean canOpenDocument(String name) {
        // Implementação dependente do contexto, exemplo genérico:
        return name != null && !name.isEmpty();
    }

    private Document doCreateDocument() {
        // Implementação dependente do contexto, retorna uma nova instância de Document
        return new Document();
    }

    private void aboutToOpenDocument(Document doc) {
        // Código que executa operações antes de abrir o documento
        System.out.println("Preparando para abrir o documento: " + doc.getName());
    }
}

class DocumentManager {
    // Classe para gerenciar documentos

    public void addDocument(Document doc) {
        // Adiciona o documento a uma lista ou coleção
        System.out.println("Documento adicionado: " + doc.getName());
    }
}

class Document {
    // Classe representando o Documento

    private String name;

    public String getName() {
        return name;
    }

    public void open() {
        // Abrir documento
        System.out.println("Abrindo o documento: " + name);
    }

    public void doRead() {
        // Ler o conteúdo do documento
        System.out.println("Lendo o documento: " + name);
    }
}
