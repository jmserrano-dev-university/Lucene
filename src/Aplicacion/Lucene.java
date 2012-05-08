/**
 * Proyecto de LUCENE
 * @author José Manuel Serrano Mármol
 * @author Raul Salazar de Torres
 */

package Aplicacion;

import Procesamiento.Documento;
import Procesamiento.ParseadorHTML;
import Procesamiento.SpanishAnalyzer;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;


public class Lucene {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        List<Documento> listaDocumentos = new ArrayList<Documento>();
        listaDocumentos = ParseadorHTML.ejecutarParserHTML("./Documentos");
        
        //Realizamos el procesamiento
        SpanishAnalyzer analizadorEspanol = new SpanishAnalyzer();
        //for(int i = 0; i < listaDocumentos.size(); i++){
            try {
                TokenStream resultado = analizadorEspanol.tokenStream("content", new FileReader("ejemplo.txt"));
                
                //¿Cómo recorremos en TokenStream?
                Token t;
                while((t=resultado.next()) != null){
                    System.out.println(t.term());
                }
                
                
                System.out.println(t.term());
            } catch (FileNotFoundException ex) {
                System.out.println("Error al leer archivo en procesamiento");
            }
        //}
    }
}
