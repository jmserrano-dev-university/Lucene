/**
 * Clase Procesamiento.java
 * @author José Manuel Serrano Mármol
 * @author Raul Salazar de Torres
 */
package Procesamiento;

import java.io.IOException;
import javax.swing.text.Document;
import org.apache.lucene.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;

public class Indexacion {

    public Indexacion(Analyzer analizador) throws CorruptIndexException, LockObtainFailedException, IOException {
        // INDEXACION

        // Almaceno el índice en memoria
        Directory directory = new RAMDirectory();

        // Si quiero almacenar el índice en un fichero, utilizo esta orden
        //Directory directory = FSDirectory.open("/tmp/testindex");

        // Creo un objeto índice, que utiliza el directorio indicado, el analizador anterior, y le indico un tamaño máximo de campo
        IndexWriter iwriter = new IndexWriter(directory, analizador, true, new IndexWriter.MaxFieldLength(25000));
        // Creo un documento
        Document doc = new Document();
        // Creo un texto de ejemplo
        String text = "This is the text to be indexed.";
        // Añado al documento un nuevo campo con ese texto anterior. Le indico que lo almacena y lo indexe
        doc.add(new Field("fieldname", text, Field.Store.YES, Field.Index.ANALYZED));
        // Añado este documento al índice
        iwriter.addDocument(doc);
        iwriter.close();
    }
    
}
