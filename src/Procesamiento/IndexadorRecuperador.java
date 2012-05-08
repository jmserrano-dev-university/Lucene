/**
 * Clase Procesamiento.java
 * @author José Manuel Serrano Mármol
 * @author Raul Salazar de Torres
 */
package Procesamiento;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

public class IndexadorRecuperador {
    
    //----------------------- ATRIBUTOS
    Analyzer _analizador;
    List<Document> _listaDocumentos;
    Directory _directorio;
    
    
    //----------------------- MÉTODOS
    public IndexadorRecuperador(Analyzer analizador, List<Document> listaDocumentos) {
        _analizador = analizador;
        _listaDocumentos = listaDocumentos;
        try {
            _directorio = FSDirectory.getDirectory(new File("./tmp/index.txt"));
        } catch (IOException ex) {
            System.out.println("Error al abrir indece (index.txt)");
        }
    }
    
    
    public void EjecutarIndexacion() throws CorruptIndexException, LockObtainFailedException, IOException {

        // Creo un objeto índice, que utiliza el directorio indicado, el analizador anterior, y le indico un tamaño máximo de campo
        IndexWriter iwriter = new IndexWriter(_directorio, _analizador, false, new IndexWriter.MaxFieldLength(25000));
        
        // Añadimos los documentos al índice
        for(int i = 0; i < _listaDocumentos.size(); i++){
            iwriter.addDocument(_listaDocumentos.get(i));
        }
        
        iwriter.close();
    }
    
    public void EjecutarRecuperacion(String consulta) throws CorruptIndexException, IOException, ParseException{
        IndexSearcher isearcher = new IndexSearcher(_directorio); // read-only=true

        // Creo un parser, a partir de analizador creado al inicio
        QueryParser parser = new QueryParser(null,_analizador);
        
        // Lanzo la busqueda del texto "consulta"
        org.apache.lucene.search.Query query = parser.parse(consulta);
        
        // Trabajo con los resultados
        ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
        //assertEquals(1, hits.length);

        // Itero sobre los resultados
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            //assertEquals("Texto indexado:", hitDoc.get("fieldname"));
            System.out.println("Texto indexado:" + hitDoc.get("fieldname"));
        }

        isearcher.close();
        _directorio.close();
    }
    
}
